package warhammermod.util.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.Entities.EntityGrenade;
import warhammermod.Entities.Entityspear;
import warhammermod.Entities.entitybullet;
import warhammermod.Entities.render.*;
import warhammermod.Items.shieldtemplate;
import warhammermod.util.confighandler.confighandler;

@SideOnly(Side.CLIENT)
public class clientproxy extends commonproxy{
    public static int repeater_value=0;
    public void registryitemrenderer(Item item, int meta, String id) {
        if(item instanceof shieldtemplate){
            ModelLoader.setCustomModelResourceLocation(item, meta,new ModelResourceLocation("minecraft:shield",id));
        }
        else if(item.getRegistryName().toString().equals("warhammermod:nuln repeater handgun")){
            ModelResourceLocation repeater_3d = new ModelResourceLocation("warhammermod:nuln repeater handgun3d",id);
            ModelResourceLocation repeater_3d2 = new ModelResourceLocation("warhammermod:nuln repeater handgun3d2",id);
            ModelResourceLocation repeater_3d3 = new ModelResourceLocation("warhammermod:nuln repeater handgun3d3",id);
            ModelResourceLocation repeater_normal = new ModelResourceLocation("warhammermod:nuln repeater handgun",id);
            ModelLoader.registerItemVariants(item,new ResourceLocation[]{repeater_3d,repeater_3d2,repeater_3d3,repeater_normal});
            ModelLoader.setCustomMeshDefinition(item,(stack -> {if(confighandler.Config_enable.repeater_3D_model && repeater_value ==1){
                return repeater_3d;}
                else if (confighandler.Config_enable.repeater_3D_model && repeater_value==2){return repeater_3d2;}
                else if (confighandler.Config_enable.repeater_3D_model){return repeater_3d3;}
                else {return repeater_normal;}
            }));
        }
        else {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
        }
    }

    public void renderentity(){
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

