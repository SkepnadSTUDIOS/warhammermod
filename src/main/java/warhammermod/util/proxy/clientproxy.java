package warhammermod.util.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.Entities.EntityGrenade;
import warhammermod.Entities.Entityspear;
import warhammermod.Entities.entitybullet;
import warhammermod.Entities.render.*;
import warhammermod.Items.gunswtemplate;
import warhammermod.Items.shieldtemplate;
import net.minecraft.init.Items;

@SideOnly(Side.CLIENT)
public class clientproxy extends commonproxy{
    public static int value=1;
    public void registryitemrenderer(Item item, int meta, String id) {
        if(item instanceof shieldtemplate){
            ModelLoader.setCustomModelResourceLocation(item, meta,new ModelResourceLocation("minecraft:shield",id));

        }else if(item.getRegistryName().toString().equals("warhammermod:diamond gunsword")){
            ModelResourceLocation sword_standing = new ModelResourceLocation("warhammermod:gunsword",id);
            ModelResourceLocation sword_fire = new ModelResourceLocation("warhammermod:gunsword2",id);
            ModelLoader.registerItemVariants(item,new ResourceLocation[]{sword_standing,sword_fire});
            ModelLoader.setCustomMeshDefinition(item,(stack -> {if(value==1){return sword_standing;}else {return sword_fire;}}));
        }
        else if(item.getRegistryName().toString().equals("warhammermod:iron gunsword")){
            ModelResourceLocation i_sword_standing = new ModelResourceLocation("warhammermod:iron_gunsword",id);
            ModelResourceLocation i_sword_fire = new ModelResourceLocation("warhammermod:iron_gunsword2",id);
            ModelLoader.registerItemVariants(item,new ResourceLocation[]{i_sword_standing,i_sword_fire});
            ModelLoader.setCustomMeshDefinition(item,(stack -> {if(value==1){return i_sword_standing;}else {return i_sword_fire;}}));
        }

        else {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
        }
    }

    public static void renderentity(){
        RenderingRegistry.registerEntityRenderingHandler(entitybullet.class, new IRenderFactory<entitybullet>() {
            @Override
            public Render<? super entitybullet> createRenderFor(RenderManager manager) {
                return new renderbullet<>(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Entityspear.class, new IRenderFactory<Entityspear>() {
            @Override
            public Render<? super Entityspear> createRenderFor(RenderManager manager) {
                return new renderspear<>(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new IRenderFactory<EntityGrenade>() {
            @Override
            public Render<? super EntityGrenade> createRenderFor(RenderManager manager) {
                return new rendergrenade<>(manager);
            }
        });
    }
}

