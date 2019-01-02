package warhammermod.Entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public class entitybullet extends EntityArrow {
    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    });
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.<Byte>createKey(EntityArrow.class, DataSerializers.BYTE);
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int inData;
    protected boolean inGround;
    protected int timeInGround;
    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake;
    /**
     * The owner of this arrow.
     */
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;
    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private int knockbackStrength;

    protected ItemStack getArrowStack() {
        return null;
    }

    private int bulletdamage;
    private float extradamage;
    private int knocklevel;

    public entitybullet(World worldIn, EntityLivingBase throwerIn, int damagein) {
        super(worldIn, throwerIn);
        bulletdamage = damagein;
    }

    public entitybullet(World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        this.damage = 2.0D;
        this.setSize(0.1F, 0.1F);

    }

    public entitybullet(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public entitybullet(World worldIn, EntityLivingBase shooter) {
        this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer) {
            this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        }
    }
    public void setpowerDamage(int powerIn){
        extradamage=1.5F*powerIn;
    }
    public void setknockbacklevel(int knockin){
        knocklevel=knockin;
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;
        if (entity != null) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            float i = bulletdamage + extradamage;

            DamageSource damagesource;

            if (this.shootingEntity == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            } else {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float) i)) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity;


                    if (knocklevel > 0) {
                        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                        if (f1 > 0.0F) {
                            entitylivingbase.addVelocity(this.motionX * (double) knocklevel * 0.6000000238418579D / (double) f1, 0.1D, this.motionZ * (double) knocklevel * 0.6000000238418579D / (double) f1);
                        }
                    }

                    if (this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);

                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }

                this.playSound(SoundEvents.BLOCK_STONE_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (!(entity instanceof EntityEnderman)) {
                    this.setDead();
                }
            } else {
                this.setDead();
            }

        } else {
            this.setDead();
        }

    }




}
