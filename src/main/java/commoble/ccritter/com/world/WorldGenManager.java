package commoble.ccritter.com.world;

import java.util.Random;

import commoble.ccritter.com.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenManager implements IWorldGenerator
{

	/**
	 * Called by the game registry during world generation
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.dimensionId)
		{
		case -1:
			this.generateNether(world, random, chunkX*16, chunkZ*16);
		case 0:
			this.generateSurface(world, random, chunkX*16, chunkZ*16);
		case 1:
			this.generateEnd(world, random, chunkX*16, chunkZ*16);
		}
	}

	/**
	 * Handle End generation
	 */
	private void generateEnd(World world, Random random, int x, int z)
	{
		
	}

	/**
	 * Handle Surface generation
	 */
	private void generateSurface(World world, Random random, int x, int z)
	{
		if (CommonProxy.spawn_deep_gnomes)
			this.addOreSingle(CommonProxy.deepGnomeSpawnBlock, world, random, x, z, 16, 16, 1, 13, 40);
	}

	/**
	 * Handle Nether generation
	 */
	private void generateNether(World world, Random random, int x, int z)
	{
		
	}
	
	private void addOreSingle(Block block, World world, Random random, int x, int z, int xMax, int zMax, int chancesToSpawn, int yMin, int yMax)
	{
		assert yMax > yMin : "addOreSingle: The maximum Y must be greater than the minimum Y";
		assert yMin > 0 : "addOreSingle: The minimum Y must be greater than 0";
		assert xMax > 0 && xMax <= 16 : "addOreSingle: The Maximum X must be greater than 0 and no more than 16";
		assert zMax > 0 && zMax <= 16 : "addOreSingle: The Maximum Z must be greater than 0 and no more than 16";
		assert yMax < 256 && yMax > 0 : "addOreSingle: The Maximum Y must be less than 256 but greater than 0";

		WorldGenSingleMinable gen = new WorldGenSingleMinable(block);
		
		for (int i=0; i < chancesToSpawn; i++)
		{
			int posX = x + random.nextInt(xMax);
			int posZ = z + random.nextInt(zMax);
			int posY = yMin + random.nextInt(yMax - yMin);
			gen.generate(world,  random,  posX,  posY, posZ);
		}
	}

	private void addOreVein(Block block, World world, Random random, int x, int z, int xMax, int zMax, int maxVeinSize, int chancesToSpawn, int yMin, int yMax)
	{
		assert yMax > yMin : "addOreVein: The maximum Y must be greater than the minimum Y";
		assert yMin > 0 : "addOreVein: The minimum Y must be greater than 0";
		assert xMax > 0 && xMax <= 16 : "addOreVein: The Maximum X must be greater than 0 and no more than 16";
		assert zMax > 0 && zMax <= 16 : "addOreVein: The Maximum Z must be greater than 0 and no more than 16";
		assert yMax < 256 && yMax > 0 : "addOreVein: The Maximum Y must be less than 256 but greater than 0";
		
		WorldGenMinable gen = new WorldGenMinable(block, maxVeinSize);
		
		for (int i=0; i < chancesToSpawn; i++)
		{
			int posX = x + random.nextInt(xMax);
			int posZ = z + random.nextInt(zMax);
			int posY = yMin + random.nextInt(yMax - yMin);
			gen.generate(world, random, posX, posY, posZ);
		}
		
	}
}
