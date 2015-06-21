package commoble.ccritter.com.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;



/** Identical to a regular chest but it isn't one
 * Used by Wood Gnomes to store their stuff in so they don't
 * steal each other's chests.
 * @author Joe
 *
 */
public class BlockChestGnome extends BlockChest
{

	public BlockChestGnome()
	{
		super(0);	// parameter for chest is 1 if can provide power
		this.setCreativeTab(null);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setBlockName("gnomeProofChest");
	}
	
	/**
     * Returns the ID of the items to drop on destruction.
     */
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Blocks.chest.getItemDropped(0, p_149650_2_, p_149650_3_);
    }
}
