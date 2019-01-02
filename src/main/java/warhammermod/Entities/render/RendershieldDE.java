package warhammermod.Entities.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import warhammermod.Entities.render.model.Elfshhield_model;
import warhammermod.Entities.render.model.Shield_Model;
import warhammermod.util.reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RendershieldDE extends TileEntityItemStackRenderer {
    private static String resource_location="warhammermod:textures/items/shieldebase.png";
    static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation(resource_location);
    private final ModelShield model_Shield = new Elfshhield_model();

    public void renderByItem(ItemStack p_192838_1_, float partialTicks) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(SHIELD_TEXTURE);



        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        this.model_Shield.render();
        GlStateManager.popMatrix();
    }
}
