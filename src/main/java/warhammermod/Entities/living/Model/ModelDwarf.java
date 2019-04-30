package warhammermod.Entities.living.Model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDwarf extends ModelBiped
{
    /** The head box of the VillagerModel */
    public ModelRenderer villagerHead;
    /** The body of the VillagerModel */
    public ModelRenderer villagerBody;
    /** The arms of the VillagerModel */
    public ModelRenderer villagerArms;
    /** The right leg of the VillagerModel */
    public ModelRenderer rightVillagerLeg;
    /** The left leg of the VillagerModel */
    public ModelRenderer leftVillagerLeg;
    public ModelRenderer villagerNose;
    public ModelRenderer villagerbeard;
    public ModelRenderer villagermustache1;
    public ModelRenderer villagermustache2;

    public ModelDwarf(float scale)
    {
        this(scale, 0.0F, 64, 64);
    }

    public ModelDwarf(float scale, float p_i1164_2_, int width, int height)
    {
        this.villagerHead = (new ModelRenderer(this)).setTextureSize(width, height);
        this.villagerHead.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8, 10, 8, scale);
        this.villagerNose = (new ModelRenderer(this)).setTextureSize(width, height);
        this.villagerNose.setRotationPoint(0.0F, p_i1164_2_ - 2.0F, 0.0F);
        this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, 2.0F, -6.0F, 2, 4, 2, scale);
        this.villagerbeard= (new ModelRenderer(this)).setTextureSize(width,height);
        this.villagerbeard.setRotationPoint(0.0F, p_i1164_2_ - 2.0F, 0.0F);
        this.villagerbeard.setTextureOffset(40,48).addBox(-4.0F,6.0F,-5.0F,8,6,1);
        this.villagermustache1= (new ModelRenderer(this)).setTextureSize(width,height);
        this.villagermustache1.setRotationPoint(0.0F, p_i1164_2_ - 2.0F, 0.0F);
        this.villagermustache1.setTextureOffset(40,48).addBox(-4.0F,3.0F,-5.0F,2,3,1);
        this.villagermustache1.setTextureOffset(40,48).addBox(2.0F,3.0F,-5.0F,2,3,1);
        this.villagermustache1.setTextureOffset(40,48).addBox(-2.0F,3.0F,-5.0F,1,1,1);
        this.villagermustache1.setTextureOffset(40,48).addBox(1.0F,3.0F,-5.0F,1,1,1);
        this.villagerHead.addChild(this.villagerbeard);
        this.villagerHead.addChild(this.villagermustache1);
        this.villagerHead.addChild(this.villagerNose);
        this.villagerBody = (new ModelRenderer(this)).setTextureSize(width, height);
        this.villagerBody.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        this.villagerBody.setTextureOffset(0, 19).addBox(-5.0F, 4.0F, -3.0F, 10, 10, 7, scale);
        this.villagerBody.setTextureOffset(0, 38).addBox(-5.0F, 4.0F, -3.0F, 10, 16, 7, scale + 0.5F);
        this.villagerArms = (new ModelRenderer(this)).setTextureSize(width, height);
        this.villagerArms.setRotationPoint(0.0F, 0.0F + p_i1164_2_ + 2.0F, 0.0F);
        this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, 1.0F, 1.0F, 4, 8, 4, scale);
        this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, 1.0F, 1.0F, 4, 8, 4, scale);
        this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 5.0F, 1.0F, 8, 4, 4, scale);
        this.rightVillagerLeg = (new ModelRenderer(this, 35, 3)).setTextureSize(width, height);
        this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + p_i1164_2_, 0.0F);
        this.rightVillagerLeg.addBox(-3F, 2.0F, -2.0F, 5, 10, 5, scale);
        this.leftVillagerLeg = (new ModelRenderer(this, 35, 3)).setTextureSize(width, height);
        this.leftVillagerLeg.mirror = true;
        this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + p_i1164_2_, 0.0F);
        this.leftVillagerLeg.addBox(-2F, 2.0F, -2.0F, 5, 10, 5, scale);
        //this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        //this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale + 0.5F);
        //this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.villagerHead.render(scale);
        this.villagerBody.render(scale);
        this.rightVillagerLeg.render(scale);
        this.leftVillagerLeg.render(scale);
        this.villagerArms.render(scale+0.009F);
        //this.bipedHeadwear.render(scale+0.05F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.villagerHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.villagerHead.rotateAngleX = headPitch * 0.017453292F;
        this.villagerArms.rotationPointY = 3.0F;
        this.villagerArms.rotationPointZ = -1.0F;
        this.villagerArms.rotateAngleX = -0.75F;
        this.rightVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.rightVillagerLeg.rotateAngleY = 0.0F;
        this.leftVillagerLeg.rotateAngleY = 0.0F;
    }
}
