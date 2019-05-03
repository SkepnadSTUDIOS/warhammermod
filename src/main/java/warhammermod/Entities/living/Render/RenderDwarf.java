package warhammermod.Entities.living.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.Entities.living.EntityDwarf;
import warhammermod.Entities.living.Model.ModelDwarf;
import warhammermod.util.reference;
import warhammermod.util.utils;

@SideOnly(Side.CLIENT)
public class RenderDwarf extends RenderLiving<EntityDwarf>
{



    public RenderDwarf(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelDwarf(0.0F), 0.5F);

    }




    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityDwarf entity)
    {
        return(utils.getdwarftexture(entity));
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityDwarf entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)((double)f * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}