package commoble.ccritter.com.util;

import net.minecraft.block.Block;

//fourple used by gnode blueprints
public class BlockLocator
{
	public final int x;
	public final int y;
	public final int z;
	public final Block block;	// the expected block type
	
	public BlockLocator (int x, int y, int z, Block block)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
	}
}
