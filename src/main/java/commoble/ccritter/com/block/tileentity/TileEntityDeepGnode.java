package commoble.ccritter.com.block.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.util.BlockLocator;
import commoble.ccritter.com.util.IntLoc;

public class TileEntityDeepGnode extends TileEntityGnode
{
	// used to define what type of gnode this TE is
	public class DGRoom
	{
		public static final int ROOM_MINE = -1;
		
		// size of the room	(length of side of square floor)
		// size 1 has 1 door per side, size 2 and 3 have 2 doors per side
		public int size;
		public int doors;
		
		// loc of this gnode's parent
		// if null: has no parent (master gnode)
		public IntLoc parentloc;
		
		// 0, 1, 2, 3 valid for size=1,2,3
		// 4, 5, 6, 7 only valid for size=2,3
		public IntLoc[] doorlocs;
		public boolean[] used_doors;
		
		public DGRoom(int size, IntLoc parentloc)
		{
			this.size = size;
			this.parentloc = parentloc;
			
			if (size == 1)
			{
				this.doors = 4;
			}
			else if (size > 1)
			{
				this.doors = 8;
			}
			else
			{
				this.doors = 1;
			}
			
			doorlocs = new IntLoc[this.doors];
			used_doors = new boolean[this.doors];
		}
	}
	
	public DGRoom room;
	
	public TileEntityDeepGnode()
	{
		super();
		this.room = null;
	}
	
	@Override
	protected void buildBlueprint()
	{
		if (this.worldObj.isRemote)
		{
			return;
		}
		
		if (this.room == null)
		{
			this.blueprint = null;
			return;
		}
		
		// room is known -- use info to build blueprint
		// note that seed is set immediately before calling buildBlueprint
		
		// determine doors BEFORE making blueprint
		this.findDoorLocs();
		
		// jobs are assigned in the order they are generated, so build tunnel first
		// start from parent and go to this node
		if (this.room.parentloc != null)
		{
			this.buildTunnelToParent();
		}
		
		this.buildRoom();
		
		this.setChildLocs();
	}
	
	protected void findDoorLocs()
	{
		int size = this.room.size;
		
		int xstart = this.xCoord;
		int ystart = this.yCoord;
		int zstart = this.zCoord;
		
		for (int door = 0; door <= 1; door++)
		{
			if (size == 1 && door > 0)
			{
				continue;	// size 1 only has 1 door per side
			}
			
			for (int side = 0; side < 4; side++)
			{
				// use math to determine location of door
				
				// magnitudes of offset
				int off1 = ((size-1) * 2);
				int off2 = ((size*2) + 1);
				
				// sign (direction) of door offset
				int xsign = ((door == 1 && side < 2) || side == 1 || (side == 2 && door == 0)) ? 1 : -1;
				int zsign = ((door == 0 && side > 1) || side == 2 || (side == 1 && door == 1)) ? 1 : -1;
				
				int xoff = xsign * ((side % 2 == 0) ? off1 : off2);
				int zoff = zsign * ((side % 2 == 0) ? off2 : off1);
				
				// remember location of door
				this.room.doorlocs[4*door + side] = new IntLoc(xstart + xoff, ystart, zstart + zoff);
			}
		}
	}
	
	protected void buildTunnelToParent()
	{
		// first, find best (closest) door to connect to parent
		int px = this.room.parentloc.x;
		int py = this.room.parentloc.y;
		int pz = this.room.parentloc.z;
		
		int dx = px - this.xCoord;
		int dz = pz - this.zCoord;
		
		boolean rightdoor = (dx > 0);	// true if door is on this node's right side, false if left
		boolean botdoor = (dz > 0);	// true if door is on this node's bot side, false if top
		boolean xfurther = (Math.abs(dx) > Math.abs(dz));	// true if |dx| > |dy|
	}
	
	protected void buildRoom()
	{
		//Random rand = this.buildrand;
		int size = this.room.size;
		
		int xstart = this.xCoord;
		int ystart = this.yCoord;
		int zstart = this.zCoord;
		
		// TODO handle special rooms like mines
		
		// build center area (floor + space to walk in)
		int width = 4*size + 1;	// 5, 9, or 13
		int radius = width / 2;	// round down to 2, 4, 6
		for (int x = (xstart - radius); x <= (xstart + radius); x++)
		{
			for (int z = (ystart - radius); z <= (ystart + radius); z++)
			{
				this.blueprint.add(new BlockLocator(x, ystart, z, Blocks.stone));
				this.blueprint.add(new BlockLocator(x, ystart+1, z, Blocks.air));
				this.blueprint.add(new BlockLocator(x, ystart+2, z, Blocks.air));
			}
		}
		
		for (int door = 0; door < this.room.doors; door++)
		{
			int dx = this.room.doorlocs[door].x;
			int dy = this.room.doorlocs[door].y;
			int dz = this.room.doorlocs[door].z;
			
			// door spans across x -- door 0, 2, 4, 6
			if (door % 2 == 0)
			{
				for (int frameoff = -1; frameoff <= 1; frameoff++)
				{
					this.blueprint.add(new BlockLocator(dx + frameoff, dy, dz, Blocks.stone));
					this.blueprint.add(new BlockLocator(dx + frameoff, dy+1, dz, Blocks.air));
					this.blueprint.add(new BlockLocator(dx + frameoff, dy+2, dz, Blocks.air));
				}
			}
			else	// door spans across y -- doors 1, 3, 5, 7
			{
				for (int frameoff = -1; frameoff <= 1; frameoff++)
				{
					this.blueprint.add(new BlockLocator(dx, dy, dz + frameoff, Blocks.stone));
					this.blueprint.add(new BlockLocator(dx, dy+1, dz + frameoff, Blocks.air));
					this.blueprint.add(new BlockLocator(dx, dy+2, dz + frameoff, Blocks.air));
				}
			}
		}
	}

	@Override
	public Job generateJob()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void processBlueprintLoc(BlockLocator loc, Block oldblock)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selfDestruct()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGnomeDeath()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGnome(EntityGnome gnome)
	{
		// TODO Auto-generated method stub
		
	}
	
	private void setChildLocs()
	{
		// TODO finish this
	}
}
