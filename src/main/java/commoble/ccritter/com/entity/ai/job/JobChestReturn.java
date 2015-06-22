package commoble.ccritter.com.entity.ai.job;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.util.IntLoc;
import commoble.ccritter.com.entity.ai.job.Job;

public class JobChestReturn extends Job
{
	public JobChestReturn(EntityGnomeWood ent, IntLoc loc)
	{
		super(ent, loc);
	}

	@SuppressWarnings("static-access")
	@Override
	public void finishJob(boolean near)
	{
		EntityGnomeWood gnomewood = (EntityGnomeWood)(this.gnome);
		TileEntityGnomeCache cache = (TileEntityGnomeCache) gnomewood.gnode;
		
		if (near && cache != null && cache.chestReady.size() > 0)
		{
			// mark chest spot as occupied
			IntLoc loc = cache.chestReady.poll();
			cache.chestOccupied.add(loc);
			// if this is still an air block, set as chest and move inventory
			if (gnomewood.worldObj.getBlock(loc.x, loc.y, loc.z) == Blocks.air)
			{
				gnomewood.worldObj.setBlock(loc.x, loc.y, loc.z, CCPMod.proxy.gnomeProofChest);
				TileEntityChest te = (TileEntityChest)gnomewood.worldObj.getTileEntity(loc.x, loc.y, loc.z);
				for (int i = 0; i<27; i++)
				{
					te.setInventorySlotContents(i, gnomewood.inventory[i]);
					gnomewood.inventory[i] = null;
				}
			}
			else
			{
				Vec3 dumpVec = this.getRandomSurfaceVec(gnomewood, 3, 3);
				dumpVec = this.groundify(gnomewood.worldObj, dumpVec);
				int xchest = (int) dumpVec.xCoord;
				int ychest = (int) dumpVec.yCoord;
				int zchest = (int) dumpVec.zCoord;
				gnomewood.worldObj.setBlock(xchest, ychest, zchest, Blocks.chest);
				TileEntityChest te = (TileEntityChest)gnomewood.worldObj.getTileEntity(xchest, ychest, zchest);
				for (int i = 0; i<27; i++)
				{
					te.setInventorySlotContents(i, gnomewood.inventory[i]);
					gnomewood.inventory[i] = null;
				}
			}

			gnomewood.setCarried(Blocks.air);
		}
	}
	
}
