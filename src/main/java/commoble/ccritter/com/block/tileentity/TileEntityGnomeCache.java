package commoble.ccritter.com.block.tileentity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.entity.ai.job.JobChestReturn;
import commoble.ccritter.com.entity.ai.job.JobChestSteal;
import commoble.ccritter.com.entity.ai.job.JobPanicTo;
import commoble.ccritter.com.entity.ai.job.JobSetBlock;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.util.BlockLocator;
import commoble.ccritter.com.util.EnumAssignType;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.IntLoc;
import commoble.ccritter.com.util.MobbableBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

@SuppressWarnings("rawtypes")
public class TileEntityGnomeCache extends TileEntityGnode
{
	// the gnome that lives here
	public EntityGnomeWood denizen;
	public int target_chest_iter;
	private int timeout;
	
	// holds 4 locations for stashing chests
	public ArrayDeque<IntLoc> chestReady = new ArrayDeque<IntLoc>(4);
	public ArrayDeque<IntLoc> chestOccupied = new ArrayDeque<IntLoc>(4);
	private IntLoc nearestChest;
	private double nearestChestDist;
	
	public TileEntityGnomeCache()
	{
		super();
		this.denizen = null;
		this.target_chest_iter = 0;
		this.timeout = 0;
		this.nearestChest = null;
		this.resetChestTracker();
	}
	
	public void resetChestTracker()
	{
		this.setChestTracker(null, Double.POSITIVE_INFINITY);
	}
	
	private void setChestTracker(IntLoc loc, double dist)
	{
		this.nearestChest = loc;
		this.nearestChestDist = dist;
	}
	
	//public Class getGnomeType()
	{
		//return EntityGnomeWood.class;
	}
	
	@Override
	public void updateEntity()
	{
		// if this tile is associated with a gnome
		if (this.denizen != null)
		{
			super.updateEntity();
			if (this.chestReady.size() > 0)
			{
				IntLoc loc = this.chestReady.poll();
				if (this.worldObj.getBlock(loc.x, loc.y, loc.z) != Blocks.air)
				{
					this.chestOccupied.add(loc);
				}
				else
				{
					this.chestReady.add(loc);
				}
			}
			if (this.chestOccupied.size() > 0)
			{
				IntLoc loc = this.chestOccupied.poll();
				if (this.worldObj.getBlock(loc.x, loc.y, loc.z) != Blocks.air)
				{
					this.chestOccupied.add(loc);
				}
				else
				{
					this.chestReady.add(loc);
				}
			}
			IntLoc loc = this.findTargetChest();
			if (loc != null)
			{
				// if a chest was found, check the distance
				// if the distance is closer than the closest known chest,
				// 		but not too close, mark it
				double dist = this.getDistanceFrom(loc.x, loc.y, loc.z);
				if (dist+1.0D < this.nearestChestDist && dist > 10.0D)
				{
					this.setChestTracker(loc, dist);
				}
			}
		}
		else	// if no gnome, wait a while and am become dirt
		{
			this.timeout++;
			if (this.timeout > 500)
			{
				this.selfDestruct();
			}
		}
	}
	
	/**
	 * Called when the corresponding block is broken
	 */
	public void onDestroy()
	{
		if (this.denizen != null)
		{
			this.denizen.onHomeDestroyed();
		}
	}
	
	/**
	 * Wood gnomes are singular, not communal
	 * Don't get the nearest gnome, just get this gnode's denizen
	 */
	/*@Override
	public boolean attemptToAssignLocator(GnomeAssignment assign)
	{
		if (this.denizen != null)
		{
			return this.denizen.attemptToAcceptAssignment(assign);
		}
		else
		{
			return false;
		}
	}*/
	
	/*@Override
	public boolean attemptToDelegateJob()
	{
		IntLoc loc = this.chestlocs.poll();
		this.chestlocs.add(loc);
		// check if gnome can place a chest here
		if (MobbableBlock.isSoftBlock(this.worldObj.getBlockId(loc.x, loc.y, loc.z), this.worldObj))
		{
			if (this.denizen != null)
			{
				IntLoc targetloc = this.findTargetChest();
				if (targetloc != null && this.denizen.attemptToAcceptJob(new JobChestSteal(this.denizen, targetloc)))
				{
					return true;
				}
			}
		}
		return false;
	}*/
	
	@Override
	public Job generateJob()
	{
		if (this.denizen.panic)
		{
			return new JobPanicTo(this.denizen, new IntLoc(this.xCoord, this.yCoord-2, this.zCoord), 1.5D);
		}
		if (this.assignmentQueue.size() > 0)
		{
			if (this.denizen.getRNG().nextInt(5) == 0)
			{
				return new JobSetBlock(this.denizen, this.assignmentQueue.poll());
			}
			else
			{
				return null;
			}
		}
		else if (this.chestReady.size() > 0)
		{
			if (this.denizen.getCarried() == Blocks.air) // no chest held
			{
				if (this.denizen.getRNG().nextInt(5) == 0)
				{
					IntLoc targetloc = this.nearestChest;
					if (targetloc != null)
					{
						return new JobChestSteal(this.denizen, targetloc);
					}
					else
					{
						return null;
					}
				}
				else
				{
					return null;
				}
			}
			else if (this.denizen.getCarried() == Blocks.chest)
			{
				if (this.denizen.getRNG().nextInt(5) == 0)
				{
					IntLoc targetloc = new IntLoc(this.xCoord, this.yCoord-2, this.zCoord);
					return new JobChestReturn(this.denizen, targetloc);
				}
				else
				{
					return null;
				}
			}
			else	// carrying something other than chest
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Iterates over the list of loaded tile entities once per tick to find a chest
	 * Returns a position of the chest if a chest is found with at least one item in it
	 * @return
	 */
	public IntLoc findTargetChest()
	{
		ArrayList list = (ArrayList) this.worldObj.loadedTileEntityList;
		if (this.target_chest_iter >= list.size())
		{
			this.target_chest_iter = 0;
		}
		TileEntity ent = (TileEntity) list.get(this.target_chest_iter);
		this.target_chest_iter++;
		if (ent != null && this.worldObj.getBlock(ent.xCoord, ent.yCoord, ent.zCoord) == Blocks.chest)
		{
			// TODO maybe put a try/catch here? (life should go on)
			TileEntityChest chestent = (TileEntityChest)ent;
			for (int i = 0; i < chestent.getSizeInventory(); i++)
			{
				if (chestent.getStackInSlot(i) != null)
				{
					return new IntLoc(ent.xCoord, ent.yCoord, ent.zCoord);
				}
			}
		}
		return null;
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
    }
    
    @Override
    public void selfDestruct()
    {
    	this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.dirt);
    }
    
    protected void processBlueprintLoc(BlockLocator loc, Block oldblock)
    {
    	Block normalblock = oldblock;
    	// normalize blocks
    	if (normalblock == Blocks.grass)
    	{
    		normalblock = Blocks.dirt;
    	}
    	if (normalblock != loc.block)
    	{	// block mismatch
    		if (MobbableBlock.isSoftBlock(oldblock, this.worldObj))
    		{
    			if (oldblock == Blocks.air)
    			{	// air block
    				this.assignmentQueue.add(new GnomeAssignment(loc, oldblock, EnumAssignType.CREATE));
    			}
    			else
    			{	// not air block
    				this.assignmentQueue.add(new GnomeAssignment(loc, oldblock, EnumAssignType.ALTER));
    			}
    		}
    		else if (this.isBlockUnsafe(normalblock))
    		{	// avoid conflicts with nearby hovels by selfdestructing
    			this.selfDestruct();
    		}
    	}
    	else
    	{	// no mismatch
    		this.blueprint.add(loc);
    	}
    	
    }
    
    @SuppressWarnings("static-access")
	private boolean isBlockUnsafe(Block block)
    {
    	if (MobbableBlock.isUnsafeBlock(block))
    	{
    		return true;
    	}
    	else if (block == CCPMod.proxy.gnomeCache)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    /**
     * Creates a blueprint of dirt and air blocks to shape into a gnome hovel
     */
	@Override
	protected void buildBlueprint()
	{
		if (this.worldObj.isRemote)
		{
			return;
		}
		Random rand = this.buildrand;
		int xstart = this.xCoord;
		int ystart = this.yCoord;
		int zstart = this.zCoord;
		int orientation = rand.nextInt(4);
		int doorhole_x = ((orientation%2) == 0) ? 0 : (orientation-2)*2;
		int doorhole_z = ((orientation%2) == 1) ? 0 : 2 - (orientation*2);
		
		for (int i = 0; i < 4; i++)
		{
			int xoff = ((i/2) * 2) - 1;
			int zoff = ((i%2) * 2) - 1;
			this.chestReady.add(new IntLoc(xstart + xoff, ystart-2, zstart + zoff));
		}

		
		
		// for each side, add decorator dirts
		for (int side = 0; side < 3; side++)
		{
			int xoff = ((side%2) == 0) ? 0 : (side-2)*3;
			int zoff = ((side%2) == 1) ? 0 : 3 - (side*3);
			
			if (side%2 == 0)	// x = 0, z = 3 or -3
			{
				for (int iter = -2; iter <= 2; iter++)
				{
					this.blueprint.add(new BlockLocator(xstart + xoff + iter, ystart -2, zstart + zoff, Blocks.dirt));
					if (!(xoff + iter == doorhole_x && zoff + (zoff < 0 ? 1 : -1) == doorhole_z))
					{
						if (this.buildrand.nextInt(1+(iter*iter)) == 0)
						{
							this.blueprint.add(new BlockLocator(xstart + xoff + iter, ystart-1, zstart + zoff, Blocks.dirt));
							if (this.buildrand.nextInt(2) == 0)
							{
								this.blueprint.add(new BlockLocator(xstart + xoff + iter, ystart, zstart + zoff, Blocks.dirt));
							}
						}
					}
					else
					{
						int zplus = (zoff > 0 ? 1 : -1);	// z/3, 1 or -1
						int z_incr = 0;
						for (int i = 0; i < 3; i++)	// Make room for an entrance stairway
						{	// TODO maybe make the entrance indefinitely long if necessary
							this.blueprint.add(new BlockLocator(xstart+xoff+iter, ystart+i-1, zstart + zoff+z_incr, Blocks.air));
							this.blueprint.add(new BlockLocator(xstart+xoff+iter, ystart+i, zstart + zoff+z_incr, Blocks.air));
							z_incr += zplus;
						}
						
					}
				}
			}
			else	// z = 0, x = 3 or -3
			{
				for (int iter = -2; iter <= 2; iter++)
				{
					this.blueprint.add(new BlockLocator(xstart + xoff, ystart -2, zstart + zoff + iter, Blocks.dirt));
					if(!(xoff + (xoff < 0 ? 1 : -1) == doorhole_x && (zoff + iter == doorhole_z)))
					{
						if (this.buildrand.nextInt(1+(iter*iter)) == 0)
						{
							this.blueprint.add(new BlockLocator(xstart + xoff, ystart-1, zstart + zoff + iter, Blocks.dirt));
							if (this.buildrand.nextInt(2) == 0)
							{
								this.blueprint.add(new BlockLocator(xstart + xoff, ystart, zstart + zoff + iter, Blocks.dirt));
							}
						}
					}
					else	// doorhole place, dig stairs
					{
						int xplus = (xoff > 0 ? 1 : -1);	// x/3. 1 or -1
						int x_incr = 0;
						for (int i = 0; i < 3; i++)
						{
							this.blueprint.add(new BlockLocator(xstart+xoff+x_incr, ystart+i-1, zstart + zoff+iter, Blocks.air));
							this.blueprint.add(new BlockLocator(xstart+xoff+x_incr, ystart+i, zstart + zoff+iter, Blocks.air));
							x_incr += xplus;
						}
					}
				}
			}
		}
		
		// walls
		for (int yoff = -2; yoff < 0; yoff++)
		{
			for (int xoff = -2; xoff <= 2; xoff++)
			{
				for (int zoff = -2; zoff <= 2; zoff++)
				{
					//this.blueprint.add(new BlockLocator(xstart + xoff, ystart + yoff, zstart + zoff, Block.dirt.blockID));
					// if (exterior AND not door) OR (door AND -2), make dirt, else make air
					boolean exterior = (((xoff<0) ? -xoff : xoff) == 2 || ((zoff<0) ? -zoff : zoff) == 2);
					boolean door = (xoff == doorhole_x && zoff == doorhole_z);
					if ((exterior && !door) || (door && yoff == -2))
					{	// walls = more dirt
						this.blueprint.add(new BlockLocator(xstart + xoff, ystart + yoff, zstart + zoff, Blocks.dirt));
					}
					else
					{
						this.blueprint.add(new BlockLocator(xstart+xoff, ystart+yoff, zstart+zoff, Blocks.air));
					}
				}
			}
		}
		
		// ceiling
		for (int xoff = -2; xoff <= 2; xoff++)
		{
			for (int zoff = -2; zoff <= 2; zoff++)
			{
				// give the house a hat
				if (xoff*xoff < 4 && zoff*zoff < 4)
				{
					this.blueprint.add(new BlockLocator(xstart + xoff, ystart + 1, zstart + zoff, Blocks.dirt));
				}
				// DON'T put a blueprint loc at the tile entity
					// or it'll erase itself
				if (zoff == 0 && xoff == 0)
				{
					continue;
				}
				if (zoff != doorhole_z || xoff != doorhole_x)
				{
					this.blueprint.add(new BlockLocator(xstart + xoff, ystart, zstart + zoff, Blocks.dirt));
				}
				else
				{
					this.blueprint.add(new BlockLocator(xstart + xoff, ystart, zstart + zoff, Blocks.air));
				}
				if (xoff*xoff < 4 && zoff*zoff < 4)
				{
					this.blueprint.add(new BlockLocator(xstart + xoff, ystart + 1, zstart + zoff, Blocks.dirt));
				}
			}
		}
	}

	@Override
	public void onGnomeDeath()
	{
		this.denizen = null;
	}

	@Override
	public void addGnome(EntityGnome gnome)
	{
		this.denizen = (EntityGnomeWood)gnome;
	}
}
