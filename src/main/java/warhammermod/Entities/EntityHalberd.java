package warhammermod.Entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHalberd extends EntityThrowable
{
    private float damage;
    private int fuse;
    private EntityPlayer entityplayer;

    public EntityHalberd(World worldIn)
    {
        super(worldIn);

    }

    public EntityHalberd(World worldIn, EntityLivingBase throwerIn,float AttackDamage) {
        super(worldIn, throwerIn);
        damage=AttackDamage;
        entityplayer = (EntityPlayer) throwerIn;
        fuse=2;
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 8; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public EntityHalberd(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void registerFixesSnowball(DataFixer fixer)
    {
        EntityThrowable.registerFixesThrowable(fixer, "Snowball");
    }

    private float extradamage;
    private int knocklevel;

    public void setpowerDamage(int powerIn){
        extradamage=1.5F*powerIn;
    }
    public void setknockbacklevel(int knockin){
        knocklevel=knockin;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        return false;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null) {

            damage += extradamage;
            if (result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase) result.entityHit;
                if (knocklevel > 0) {
                    float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (f1 > 0.0F) {
                        entitylivingbase.addVelocity(this.motionX * (double) knocklevel * 0.6000000238418579D / (double) f1, 0.1D, this.motionZ * (double) knocklevel * 0.6000000238418579D / (double) f1);
                    }
                }
                if (result.entityHit != entityplayer)
                    result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
            }
        }
            if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.setDead();}
    }
    public void onEntityUpdate()
    {
        this.world.profiler.startSection("entityBaseTick");
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        --fuse;
        if(fuse<=0){System.out.println(("killed"));setDead();}



    }
}
