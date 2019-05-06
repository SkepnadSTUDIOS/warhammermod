package warhammermod.util.Handler.Registryhandler;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.Entities.*;
import warhammermod.Entities.living.EntityDwarf;
import warhammermod.Entities.living.Render.RenderDwarf;
import warhammermod.util.Handler.inithandler.Itemsinit;
import warhammermod.util.reference;

import static warhammermod.Main.proxy;

@Mod.EventBusSubscriber
public class Registryhandler {

    @SubscribeEvent
    public static void onItemregister(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(Itemsinit.ITEMS.toArray(new Item[0]));
    }

    /*@SubscribeEvent
    public static void onBlockregister(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(Modblocks.BLOCKS.toArray(new Block[0]));
    }*/
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelregister(ModelRegistryEvent event){
        for(Item item : Itemsinit.ITEMS){
            proxy.registryitemrenderer(item,0,"inventory");
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityDwarf.class, new IRenderFactory<EntityDwarf>() {
            @Override
            public RenderLiving<? super EntityDwarf> createRenderFor(RenderManager manager) {
                return new RenderDwarf(manager);
            }
        });
    }
    @SubscribeEvent
    public static void entityis(RegistryEvent.Register<EntityEntry> event){
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"bullet"), entitybullet.class,"bullet",0,reference.modid,64,30,true);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"spear"), Entityspear.class,"spear_entity",1,reference.modid,55,30,true);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"grenade"), EntityGrenade.class,"grenade_entity",2,reference.modid,64,30,true);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"halberd"), EntityHalberd.class,"halberd_entity",3,reference.modid,10,30,true);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"dwarf"), EntityDwarf.class,"dwarf",4,reference.modid,64,1,true,1599971,15721509);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"shotgun pellet"), Entityshotgun.class,"bullets",5,reference.modid,64,30,true);
    }

    /*public SoundEvent registerSound(String name) {
        final ResourceLocation res = new ResourceLocation(reference.MODID, name);//new ResourceLocation(reference.MODID, "sounds/");
        SoundEvent sound = new SoundEvent(res);
        sound.setRegistryName(res);
        sounds.add(sound);
        return sound;
    }*/
    /*@SubscribeEvent
    public void onRegisterSoundEvent(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(this.sounds.toArray(new SoundEvent[0]));
    }*/


}
