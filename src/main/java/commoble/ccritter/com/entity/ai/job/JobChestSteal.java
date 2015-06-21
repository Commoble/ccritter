package commoble.ccritter.com.entity.ai.job;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.util.IntLoc;

public class JobChestSteal extends Job
{
	//private Vec3 vecToChest;

	public JobChestSteal(EntityGnomeWood ent, IntLoc loc)
	{
		super(ent, loc);
	}
	
	public void finishJob(boolean near)
	{
		EntityGnomeWood gnomewood = (EntityGnomeWood)this.gnome;
		TileEntityGnomeCache cache = (TileEntityGnomeCache) gnomewood.gnode;
		if (near && gnomewood.gnode != null)
		{
			// if this is still a chest
			if (gnomewood.worldObj.getBlock(this.x, this.y, this.z) == Blocks.chest)
			{
				TileEntityChest te = (TileEntityChest)gnomewood.worldObj.getTileEntity(this.x, this.y, this.z);
				if (te.numPlayersUsing == 0)
				{
					// move contents of chest from chest to gnome
					for (int i = 0; i < 27; i++)
					{
						gnomewood.inventory[i] = te.getStackInSlot(i);
						te.setInventorySlotContents(i, null);
					}
					if (!gnomewood.worldObj.isRemote)
					{
						gnomewood.worldObj.setBlock(this.x, this.y, this.z, Blocks.air);
						gnomewood.setCarried(Blocks.chest);
					}
				}
			}
			cache.resetChestTracker();
		}
	}
	
}
