package commoble.ccritter.com.entity.ai.job;

import net.minecraft.block.Block;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.IntLoc;

public class JobSetBlock extends Job
{
	private Block newblock;
	private Block oldblock;
	private GnomeAssignment assign;	// remembered to facilitate returning it
	
	public JobSetBlock(EntityGnome gnome, GnomeAssignment assign)
	{
		super(gnome, new IntLoc(assign.loc.x, assign.loc.y, assign.loc.z));
		this.newblock = assign.loc.block;
		this.oldblock = assign.oldblock;
		this.assign = assign;
	}

	@Override
	public void finishJob(boolean near)
	{
		if (near)
		{
			Block oldblock_actual = this.gnome.worldObj.getBlock(x, y, z);
			if (oldblock_actual == this.oldblock)	// no mismatch, continue
			{
				if (!this.gnome.worldObj.isRemote)
				{
					this.gnome.worldObj.setBlock(x,y,z, newblock);
					this.gnome.finishSetBlock(this.assign, true);					
				}
			}
			else	// mismatch, invalid job, return to blueprint
			{
				this.gnome.finishSetBlock(this.assign, true);
			}
		}
		else	// failed and not near yet
		{// do gnome-specific end-of-job stuff
			this.gnome.finishSetBlock(this.assign, false);
		}
	}
	
}
