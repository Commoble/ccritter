package commoble.ccritter.com.block;

import java.util.Random;

import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGnomeCache extends BlockContainer
{

	public BlockGnomeCache()
	{
		super(Material.ground);
		this.setHardness(0.5F);
        this.setStepSound(soundTypeGravel);
        setBlockName("gnomeCache");
        this.setBlockTextureName("dirt");
	}

	@Override	// this is createNewTileEntity, I think
	public TileEntity createNewTileEntity(World world, int num)
	{
		return new TileEntityGnomeCache();
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Blocks.dirt.getItemDropped(0, p_149650_2_, p_149650_3_);
    }
}
