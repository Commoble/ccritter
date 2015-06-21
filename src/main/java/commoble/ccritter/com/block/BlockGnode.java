package commoble.ccritter.com.block;

import commoble.ccritter.com.block.tileentity.TileEntityGnode;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockGnode extends BlockContainer
{
	protected BlockGnode()
	{
		super(Material.ground);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int oldmeta)
	{
		TileEntity ent = world.getTileEntity(x, y, z);
		if (ent != null && ent instanceof TileEntityGnode)
		{
			((TileEntityGnode)ent).onDestroy();
		}
		super.breakBlock(world, x, y, z, block, oldmeta);
	}

}
