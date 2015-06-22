package commoble.ccritter.com.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * This is just here to give the deep gnome spawner block a description
 * @author Joseph
 *
 */
public class DGSBItemBlock extends ItemBlock
{

	public DGSBItemBlock(Block block)
	{
		super(block);
	}
		
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
	{
		list.add("The unblinded see it shining");
		list.add("in deepest darkness...");
	}
}
