package warhammermod.util;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;
import warhammermod.Entities.living.EntityDwarf;
import warhammermod.util.Handler.inithandler.Itemsinit;

import java.util.ArrayList;
import java.util.Random;

public class utils {


    public static void addbiome(Biome[] list){
        ArrayList<Biome> TEMP= new ArrayList<>();
        for (Biome x : MapGenVillage.VILLAGE_SPAWN_BIOMES){
            TEMP.add(x);
        }
        for(Biome x : list){
            TEMP.add(x);
        }
        MapGenVillage.VILLAGE_SPAWN_BIOMES=TEMP;
    }
    public static boolean checkcorrectbiome(Biome biome){
        for(Biome x : reference.Biome_list){
            if(biome==x)return true;
        }
        return false;
    }
    public static ItemShield getRandomShield(){
        int shield = new Random().nextInt(4);
        switch(shield){
            case 0:return Itemsinit.shield;
            case 1:return Itemsinit.shield2;
            case 2:return Itemsinit.shield3;
            default:return Itemsinit.shield4;
        }
    }
    public static ItemArmor getRandomarmor(int part){
        int random = new Random().nextInt(2);
        switch(random){
            case 0:if(part<1)return Itemsinit.Diamond_Chainmail_legging;else return Itemsinit.Diamond_Chainmail_boots;
            default:if(part<1)return Itemsinit.Diamond_Chainmail_helmet;else return Itemsinit.Diamond_Chainmail_plate;
        }
    }

    private static final ResourceLocation DWARF_VILLAGER_TEXTURES = new ResourceLocation(reference.modid,"textures/entity/dwarf_farmer.png");
    private static final ResourceLocation DWARF_VILLAGER_TEXTURES1 = new ResourceLocation(reference.modid,"textures/entity/dwarf.png");
    private static final ResourceLocation DWARF_VILLAGER_TEXTURES2 = new ResourceLocation(reference.modid,"textures/entity/dwarf_engineer.png");
    private static final ResourceLocation DWARF_VILLAGER_TEXTURES3 = new ResourceLocation(reference.modid,"textures/entity/dwarf_armourer.png");
    private static final ResourceLocation DWARF_VILLAGER_TEXTURES4 = new ResourceLocation(reference.modid,"textures/entity/dwarf_lord.png");

    public static ResourceLocation getdwarftexture(EntityDwarf entity){
        int prof = entity.getProfession();
        switch(prof){
            case 0:return DWARF_VILLAGER_TEXTURES;
            case 1:return DWARF_VILLAGER_TEXTURES1;
            case 2:return DWARF_VILLAGER_TEXTURES2;
            case 3:return DWARF_VILLAGER_TEXTURES3;
            case 4 :return DWARF_VILLAGER_TEXTURES4;
            default:return DWARF_VILLAGER_TEXTURES1;

        }
    }

}
