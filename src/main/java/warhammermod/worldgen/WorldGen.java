package warhammermod.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator{
    private MapGenDwarfVillage villageGenerator = new MapGenDwarfVillage();
    private Random rand;

    public void generate(Random random, int x, int z, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){

        rand = new Random(world.getSeed());

        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = world.getBiome(blockpos.add(16, 0, 16));
        rand.setSeed(world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ world.getSeed());
        boolean flag = false;
        ChunkPos chunkpos = new ChunkPos(x, z);
        ChunkPrimer chunkprimer = new ChunkPrimer();

        this.villageGenerator.generate(world, x, z, chunkprimer);
        villageGenerator.generateStructure(world, rand, chunkpos);


    }
}