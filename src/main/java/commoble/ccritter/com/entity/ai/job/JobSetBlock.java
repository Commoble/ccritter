package commoble.ccritter.com.entity.ai.job;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.util.EnumAssignType;
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
				{	// play block-placing sound effect if placing or changing a block
					if (this.assign.type == EnumAssignType.CREATE || this.assign.type == EnumAssignType.ALTER)
					{
						this.gnome.worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), oldblock_actual.stepSound.func_150496_b(), (oldblock_actual.stepSound.getVolume() + 1.0F) / 2.0F, oldblock_actual.stepSound.getPitch() * 0.8F);
					}	// play block-breaking sound effect if breaking a block
					else if (this.assign.type == EnumAssignType.DESTROY || this.assign.type == EnumAssignType.HARVEST)
					{
						this.gnome.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(oldblock_actual) + (this.gnome.worldObj.getBlockMetadata(x, y, z) << 12));
					}
					
					this.gnome.worldObj.setBlock(x,y,z, newblock);
					newblock.onBlockPlacedBy(this.gnome.worldObj, x, y, z, this.gnome, new ItemStack(newblock,1));
					this.gnome.finishSetBlock(this.assign, true, false);					
				}
			}
			else	// mismatch, invalid job, return to blueprint
			{
				this.gnome.finishSetBlock(this.assign, true, true);
			}
		}
		else	// failed and not near yet
		{// do gnome-specific end-of-job stuff
			this.gnome.finishSetBlock(this.assign, false, false);
		}
	}
	
}
