package commoble.ccritter.com.entity.ai.job;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.entity.gnome.EntityGnomeDeep;
import commoble.ccritter.com.util.BlockLocator;
import commoble.ccritter.com.util.EnumAssignType;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.IntLoc;
import commoble.ccritter.com.util.MobbableBlock;

/**
 * This Job moves a gnome to the location of any BlockContainer and places the gnome's held item in it
 * When creating this job, you should specify the location of a BlockContainer
 * 		whose tileentity implements IInventory
 * The Job ends with no effect under the following conditions:
 *		-a BlockContainer with an IInventory-implementing tile entity no longer exists at the chest's position when the gnome reaches it
 *		-the chest's inventory has no room for the item
 *		-the gnome loses the held item at any point
 * @author Joseph
 *
 */
public class JobChestFill extends Job
{
	EntityGnomeDeep deepgnome;

	public JobChestFill(EntityGnomeDeep ent, IntLoc loc)
	{
		super(ent, loc);
		this.deepgnome = ent;
	}
	
	@Override
	public boolean shouldContinue()
	{
		return (this.gnome.getCarried() != Blocks.air && super.shouldContinue());
	}

	@Override
	public void finishJob(boolean near)
	{
		if (!near)	// job failed
		{
			if (this.gnome.canMineFreely)
			{
				// get next block between gnome and chest
				// first get a vector with the distance from the gnome to the chest
				double xdist = (this.deepgnome.chest.x - this.deepgnome.posX);
				double ydist = (this.deepgnome.chest.y - (this.deepgnome.posY + 1));
				double zdist = (this.deepgnome.chest.x - this.deepgnome.posZ);
				
				Vec3 vec = Vec3.createVectorHelper(xdist, ydist, zdist);
				
				if (vec.lengthVector() < 10.0)
				{
					int xcheck;
					int ycheck;
					int zcheck;
					for (int i=0; i<10; i++)
					{
						xcheck = MathHelper.floor_double(vec.xCoord*i);
						ycheck = MathHelper.floor_double(vec.yCoord*i);
						zcheck = MathHelper.floor_double(vec.zCoord*i);
						Block blockcheck = this.deepgnome.worldObj.getBlock(xcheck, ycheck, zcheck);
						
						if (blockcheck != Blocks.air)
						{
							if (MobbableBlock.isMineableBlock(blockcheck, deepgnome.worldObj))
							{
								this.deepgnome.job = new JobSetBlock(deepgnome, new GnomeAssignment(new BlockLocator(xcheck, ycheck, zcheck, Blocks.air), blockcheck, EnumAssignType.HARVEST));
							}
							else
							{
								return;
							}
						}
					}
				}
			}
			return;
		}
		Block carried = this.gnome.getCarried();
		if (this.gnome.getCarried() == Blocks.air)
		{
			return;
		}
		if (near && this.gnome.worldObj.getBlock(this.x, this.y, this.z) instanceof BlockContainer)
		{
			TileEntity te = this.gnome.worldObj.getTileEntity(x, y, z);
			if (te != null && te instanceof IInventory)
			{
				IInventory inventory = (IInventory)te;
				
				// Check every slot in the inventory, if a suitable one is found, put the item in it and clear the gnome's hold slot
				for (int i=0; i<inventory.getSizeInventory(); i++)
				{	// empty slot found
					ItemStack stack = inventory.getStackInSlot(i);
					if (stack == null)
					{
						ItemStack newstack = new ItemStack(carried, 1);
						inventory.setInventorySlotContents(i, newstack);
						this.gnome.setCarried(Blocks.air);
						this.gnome.onPlaceItemInChest(x, y, z, newstack, true);
						return;
					}
					else if (stack.getItem() == carried.getItem(this.gnome.worldObj, x, y, z))
					{	// non-full slot with the same item found
						if (stack.stackSize < stack.getMaxStackSize())
						{
							stack.stackSize++;
							this.gnome.setCarried(Blocks.air);
							this.gnome.onPlaceItemInChest(x, y, z, stack, true);
							return;
						}
					}
				}
				
				// if no slot found and chest is full, call function with False
				this.gnome.onPlaceItemInChest(x, y, z, null, false);
			}
			else
			{
				this.gnome.onPlaceItemInChest(x, y, z, null, false);
			}
		}
		else
		{
			this.gnome.onPlaceItemInChest(x, y, z, null, false);
		}
	}

}
