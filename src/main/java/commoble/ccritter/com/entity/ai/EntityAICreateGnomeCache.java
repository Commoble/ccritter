package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.util.IntLoc;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAICreateGnomeCache extends EntityAIBase
{
	private EntityGnomeWood gnome;
	private int x;
	private int y;
	private int z;
	private double xd;
	private double yd;
	private double zd;
	private double speed;
	private int assigntimer;
	
	public EntityAICreateGnomeCache(EntityGnomeWood gnome, double speed)
	{
		this.gnome = gnome;
		this.speed = speed;
		this.setMutexBits(1);
		this.assigntimer = 0;
	}

	@Override
	public boolean shouldExecute()
	{
		if ((this.gnome.gnode == null) && this.assigntimer <= 0)
		{
			this.assigntimer = 5;
			if (this.gnome.homeloc != null)
			{
				IntLoc loc = this.gnome.homeloc;
				this.gnome.gnode = (TileEntityGnomeCache)this.gnome.worldObj.getTileEntity(loc.x, loc.y, loc.z);
				this.gnome.homeloc = null;
				if (this.gnome.gnode != null)
				{
					this.gnome.gnode.addGnome(this.gnome);
				}
				return false;
			}
			Vec3 vec = RandomPositionGenerator.findRandomTarget(this.gnome, 16, 7);
			if (vec == null)
			{
				return false;
			}
			
			int xtemp = (int)vec.xCoord;
			int ytemp = (int)vec.yCoord;
			int ztemp = (int)vec.zCoord;
			if (ytemp < 2)
			{
				ytemp = 2;
			}
			
			World world = this.gnome.worldObj;

			while (world.isAirBlock(xtemp, ytemp-1, ztemp) && ytemp > 2)
			{
				ytemp--;
			}
			while (!world.isAirBlock(xtemp, ytemp, ztemp) && ytemp < 255)
			{
				ytemp++;
			}
			
			Block block = world.getBlock(xtemp, ytemp-1, ztemp);
			
			if (block != Blocks.dirt && block != Blocks.grass)
			{
				return false;
			}
			
			this.x = xtemp;
			this.y = ytemp;
			this.z = ztemp;
			
			this.xd = xtemp;
			this.yd = ytemp;
			this.zd = ztemp;
			
			this.assigntimer = 10 + this.gnome.getRNG().nextInt(10);
			return true;
		}
		else
		{
			if (this.assigntimer > 0)
				this.assigntimer--;
			return false;
		}
	}

	/**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
	@Override
    public boolean continueExecuting()
    {
    	if (this.gnome.getDistance(xd, yd, zd) < 2.0D)
    	{
    		TileEntityGnomeCache cache = this.placeGnomeCache();
    		this.gnome.gnode = cache;
    		return false;
    	}
        return !this.gnome.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.gnome.getNavigator().tryMoveToXYZ(this.xd, this.yd, this.zd, this.speed);
    }
    
    @SuppressWarnings("static-access")
	protected TileEntityGnomeCache placeGnomeCache()
    {
    	if (!this.gnome.worldObj.isRemote && this.gnome.worldObj.isAirBlock(this.x, this.y, this.z))
    	{
    		this.assigntimer = 5;
    		this.gnome.worldObj.setBlock(x, y, z, CCPMod.proxy.gnomeCache);
    		TileEntity ent = this.gnome.worldObj.getTileEntity(x, y, z);
    		if (ent != null && ent instanceof TileEntityGnomeCache)
    		{
    			((TileEntityGnomeCache)ent).denizen = this.gnome;
    			return (TileEntityGnomeCache)ent;
    		}
    		return null;
    	}
    	else
    	{
        	return null;
    	}
    }
}
