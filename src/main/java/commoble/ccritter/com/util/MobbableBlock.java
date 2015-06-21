package commoble.ccritter.com.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class MobbableBlock
{
	static boolean softBlocks[];
	static boolean mischiefBlocks[];
	static boolean unsafeBlocks[];
	
	public static boolean isSoftBlock(Block block, World world)
	{
		int id = Block.getIdFromBlock(block);
		if (id > 256 || !world.getGameRules().getGameRuleBooleanValue("mobGriefing"))
		{
			return false;
		}
		else
		{
			return softBlocks[id];
		}
	}
	
	public static boolean isMischiefBlock(Block block, World world)
	{
		int id = Block.getIdFromBlock(block);
		if (id > 256 || world.getGameRules().getGameRuleBooleanValue("mobGriefing"))
		{
			return false;
		}
		else
		{
			return mischiefBlocks[Block.getIdFromBlock(block)];
		}
	}
	
	public static boolean isUnsafeBlock(Block block)
	{
		int id = Block.getIdFromBlock(block);
		if (id > 256)
		{
			return false;
		}
		else
		{
			return unsafeBlocks[id];
		}
	}
	
	static
    {
		softBlocks = new boolean[256];
    	softBlocks[0] = true;
    	softBlocks[Block.getIdFromBlock(Blocks.grass)] = true;
    	softBlocks[Block.getIdFromBlock(Blocks.dirt)] = true;
    	softBlocks[Block.getIdFromBlock(Blocks.sand)] = true;
    	softBlocks[Block.getIdFromBlock(Blocks.gravel)] = true;
        //softBlocks[Block.leaves.blockID]= true; 
    	softBlocks[Block.getIdFromBlock(Blocks.clay)] = true;
    	softBlocks[Block.getIdFromBlock(Blocks.mycelium)] = true;
    	softBlocks[Block.getIdFromBlock(Blocks.snow)] = true;
        
        mischiefBlocks = new boolean[256];
    	mischiefBlocks[Block.getIdFromBlock(Blocks.wooden_door)] = true;
    	mischiefBlocks[Block.getIdFromBlock(Blocks.lever)] = true;
    	mischiefBlocks[Block.getIdFromBlock(Blocks.stone_button)] = true;
        
        unsafeBlocks = new boolean[256];
    	unsafeBlocks[Block.getIdFromBlock(Blocks.flowing_water)] = true;
    	unsafeBlocks[Block.getIdFromBlock(Blocks.water)] = true;
    	unsafeBlocks[Block.getIdFromBlock(Blocks.flowing_lava)] = true;
    	unsafeBlocks[Block.getIdFromBlock(Blocks.lava)] = true;
    	unsafeBlocks[Block.getIdFromBlock(Blocks.fire)] = true;
    }
}
