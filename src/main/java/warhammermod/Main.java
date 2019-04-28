package warhammermod;


import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import warhammermod.util.proxy.commonproxy;
import warhammermod.util.reference;
import warhammermod.util.utils;
import warhammermod.worldgen.MapGenDwarfVillage;
import warhammermod.worldgen.StructureDwarfVillagePieces;
import warhammermod.worldgen.WorldGen;


@Mod(modid = reference.modid,useMetadata = true)
public class Main {


    @Mod.Instance
    public static Main instance;

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.COMMON_PROXY_CLASS)
    public static commonproxy proxy;

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        proxy.renderentity();




    }
    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(reference.modid))
        {
            ConfigManager.sync(reference.modid, Config.Type.INSTANCE);

        }
    }

    @EventHandler
    public void Init(FMLInitializationEvent event){

        //utils.addbiome(Biomes.EXTREME_HILLS);
        //utils.addbiome(Biomes.EXTREME_HILLS_WITH_TREES);
        //utils.addbiome(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES);
        //utils.addbiome(Biomes.MUTATED_EXTREME_HILLS);
        //utils.addbiome(reference.Biome_list);
        System.out.println(MapGenVillage.VILLAGE_SPAWN_BIOMES);
        GameRegistry.registerWorldGenerator(new WorldGen(), 0);
        MapGenStructureIO.registerStructure(MapGenDwarfVillage.DwarfStart.class,"Dwarf Village");
        StructureDwarfVillagePieces.registerVillagePieces();

    }

    @EventHandler
    public static void PostInit(FMLPostInitializationEvent event){
    }

    Biome biome;
    @SubscribeEvent
    public void onvillagerspawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityVillager){System.out.println(event.getEntity());}
        if ((event.getEntity() instanceof EntityVillager) && utils.checkcorrectbiome(biome=event.getWorld().getBiome(((EntityVillager) event.getEntity()).getPos()))){
        System.out.println("villager detected");
        System.out.println(MapGenVillage.VILLAGE_SPAWN_BIOMES);}
    }


}
