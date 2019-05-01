package warhammermod.util;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;

import java.util.ArrayList;
import java.util.List;

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
    public static boolean readnbt(){
        return true;
    }
}
