package commoble.ccritter.com.entity.ai.job;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.util.IntLoc;

/**
 * A Job is a special AI class used by gnomes
 * to perform tasks more lengthy and complicated than altering
 * a block at a specific location (which is done by Assignments).
 * 
 * They are not true AI classes (which extend EntityAIBase), but
 * instead are handled by a wrapper (EntityAIPerformJob) that calls
 * shouldExecute, start, continue, etc.
 * 
 * Assignments have higher priority than Jobs.
 * @author Joe
 *
 */
public abstract class Job
{
	// integer position of target
	protected int x;
	protected int y;
	protected int z;
	
	// the Vec3 position of target
	protected Vec3 targetVec;
	
	// the intermediate path vector between current location and target
	protected Vec3 pathVec;
	
	
	protected EntityGnome gnome;
	protected int totalJobTime;
	
	protected double speed;
	
	/**
	 * This sets this Job's target location.
	 * @param loc An IntLoc containing the coordinates for the
	 * 		target of this Job.
	 */
	public Job(EntityGnome ent, IntLoc loc)
	{
		this.x = loc.x;
		this.y = loc.y;
		this.z = loc.z;
		this.targetVec = Vec3.createVectorHelper(x, y, z);
		this.gnome = ent;
		this.totalJobTime = 0;
		this.speed = 1.0D;
	}
	
	public Job(EntityGnome ent, Vec3 vec)
	{
		this.targetVec = vec;
		this.x = (int)vec.xCoord;
		this.y = (int)vec.yCoord;
		this.z = (int)vec.zCoord;
		this.gnome = ent;
		this.totalJobTime = 0;
		this.speed = 1.0D;
	}
	
	public boolean shouldStart()
	{
		Vec3 path = this.getPathVec();
		if (path == null)
		{
			return false;
		}
		else
		{
			this.pathVec = path;
			return true;
		}
	}
	
	public void start()
	{
		this.gnome.getNavigator().tryMoveToXYZ(this.pathVec.xCoord, this.pathVec.yCoord+1, this.pathVec.zCoord, this.speed);
	}
	
	public boolean shouldContinue()
	{
		this.totalJobTime++;
		// if close to final target, finish
		if (this.gnome.getDistance(this.targetVec.xCoord, this.targetVec.yCoord, this.targetVec.zCoord) < 3.0D)
		{
			this.gnome.job = null;
			this.finishJob(true);
			return false;
		}
		// if not close to final target but close to intermediate path target, reset
		else if (this.gnome.getDistance(this.pathVec.xCoord, this.pathVec.yCoord, this.pathVec.zCoord) < 3.0D)
		{
			return false;
		}
		// if close to nothing and navigator can't path, or job timeout, cancel
		else if (this.gnome.getNavigator().noPath()  || this.totalJobTime > 10000)
		{
			this.gnome.job = null;
			this.finishJob(false);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//public abstract void update();
	
	/**
	 * Perform the task at the job's location.
	 * The gnome's Job is set to Null just before this.
	 * This function is allowed to grant the Gnome a new job.
	 * @param near True if the gnome is near the target and may fully complete the job,
	 * 		false if it is ended prematurely.
	 */
	public abstract void finishJob(boolean near);
	
	public float getBlockPathWeight(int x, int y, int z)
	{
		int dist = (this.x - x)^2 + (this.y - y)^2 + (this.z - z)^2;
		return 10000 - dist;
	}
	
	/**
	 * Returns a location toward the job target
	 * If the distance between the specified entity and the target is
	 * 		less than 12 blocks, returns that target.
	 * If the distance is greater than 12 blocks, returns a random block toward
	 * 		that target within 12 blocks horizontal / 4 vertical.
	 * @param ent the entity
	 * @return	a Vec3 containing the path location, or null.
	 */
	protected Vec3 getPathVec()
	{
		// if within 12 blocks, try to path there directly
		if (this.gnome.getDistanceSq(this.targetVec.xCoord, this.targetVec.yCoord, this.targetVec.zCoord) < 144)
		{
			return this.targetVec;
		}
		else	// if far away, wander in its general direction
		{
			return this.getSurfaceVecToward(this.gnome, this.targetVec, 12, 4);
		}
	}
	
	public Vec3 getRandomSurfaceVec(EntityCreature ent, int xrad, int yrad)
	{
		Vec3 vecRand = RandomPositionGenerator.findRandomTarget(ent, xrad, yrad);
		return this.groundify(ent.worldObj, vecRand);
	}
	
	public Vec3 getSurfaceVecToward(EntityCreature ent, Vec3 vec, int xrad, int yrad)
	{
		Vec3 vecRand = RandomPositionGenerator.findRandomTargetBlockTowards(ent, xrad, yrad, vec);
		return this.groundify(ent.worldObj, vecRand);
	}
	
	/**
	 * Alters the vector's y-coordinate to be on the next air block touching a non-air block below it,
	 * using the same x- and z-coordinates
	 * searches up first, then down
	 * ALTERS THE PASSED VECTOR. Also returns it.
	 */
	public static Vec3 groundify(World world, Vec3 vec)
	{
		if (vec == null)
		{
			return null;
		}
		
		int xtemp = (int)vec.xCoord;
		int ytemp = (int)vec.yCoord;
		int ztemp = (int)vec.zCoord;
		
		if (ytemp < 2)
		{
			ytemp = 2;
		}
		if (ytemp > 255)
		{
			ytemp = 255;
		}

		while (world.isAirBlock(xtemp, ytemp-1, ztemp) && ytemp > 2)
		{
			ytemp--;
		}
		while (!world.isAirBlock(xtemp, ytemp, ztemp) && ytemp < 255)
		{
			ytemp++;
		}
		
		vec.yCoord = ytemp;
		return vec;
	}
	
	/**
	 * Alters the location's y-coordinate to be on the next air block touching a non-air block below it,
	 * using the same x- and z-coordinates
	 * searches up first, then down
	 * ALTERS THE PASSED INTLOC. Also returns it.
	 */
	public static IntLoc groundify(World world, IntLoc loc)
	{
		if (loc == null)
		{
			return null;
		}
		
		int xtemp = loc.x;
		int ytemp = loc.y;
		int ztemp = loc.z;
		
		if (ytemp < 2)
		{
			ytemp = 2;
		}
		if (ytemp > 255)
		{
			ytemp = 255;
		}

		while (world.isAirBlock(xtemp, ytemp-1, ztemp) && ytemp > 2)
		{
			ytemp--;
		}
		while (!world.isAirBlock(xtemp, ytemp, ztemp) && ytemp < 255)
		{
			ytemp++;
		}
		
		loc.y = ytemp;
		return loc;
	}
}
