package warhammermod.Entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public class EntityGrenade extends EntityArrow {
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
    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private int knockbackStrength;

    protected ItemStack getArrowStack() {
        return null;
    }

    private int knocklevel=0;

    public EntityGrenade(World worldIn, EntityLivingBase throwerIn, int damagein) {
        super(worldIn, throwerIn);

    }

    public EntityGrenade(World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        this.setSize(0.3F, 0.3F);

    }

    public EntityGrenade(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityGrenade(World worldIn, EntityLivingBase shooter) {
        this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer) {
            this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        }
    }


    public void setknockbacklevel(int knockin) {
        knocklevel = knockin;
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        if(!world.isRemote){
        world.createExplosion(null,raytraceResultIn.hitVec.x,raytraceResultIn.hitVec.y,raytraceResultIn.hitVec.z,2+knocklevel,true);}
        this.setDead();


    }
}



