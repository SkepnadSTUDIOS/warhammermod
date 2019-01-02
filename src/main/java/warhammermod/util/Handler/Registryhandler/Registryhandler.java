package warhammermod.util.Handler.Registryhandler;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import warhammermod.Entities.EntityGrenade;
import warhammermod.Entities.Entityspear;
import warhammermod.Entities.entitybullet;
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

    @SubscribeEvent
    public static void onModelregister(ModelRegistryEvent event){
        for(Item item : Itemsinit.ITEMS){
            proxy.registryitemrenderer(item,0,"inventory");
        }
    }
    @SubscribeEvent
    public static void entityis(RegistryEvent.Register<EntityEntry> event){
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"bullet"), entitybullet.class,"bullet",0,reference.modid,55,60,true);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"spear"), Entityspear.class,"spear_entity",1,reference.modid,55,60,true);
        EntityRegistry.registerModEntity(new ResourceLocation(reference.modid+":"+"grenade"), EntityGrenade.class,"grenade_entity",2,reference.modid,55,60,true);
    }


}
