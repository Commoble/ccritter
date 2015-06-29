package commoble.ccritter.com.block.tileentity;

import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.util.BlockLocator;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.GnomeSorter;

import java.util.LinkedList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityGnode extends TileEntity
{
	/**
	 * The Blueprint is a list of locations and block IDs at those locations.
	 * The Gnode allows a gnome to build and maintain a structure around this tile
	 * by using this Blueprint.
	 * 
	 * On creation, the Gnode builds the blueprint. It periodically scans the
	 * blueprint and compares it with the actual blocks at those locations.
	 * If a mismatch is found, it adds that location to a set of locations that need
	 * updating. It periodically looks for nearby gnomes to assign that location
	 * to.
	 * 
	 */
	protected LinkedList<BlockLocator> blueprint;
	protected LinkedList<GnomeAssignment> assignmentQueue = new LinkedList<GnomeAssignment>();
	protected long buildseed;
	protected Random buildrand;
	protected final GnomeSorter sorter;
	
	public TileEntityGnode()
	{
		this.buildseed = 0;
		this.sorter = new GnomeSorter(this);
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.buildseed = nbt.getLong("buildseed");
        //this.initialize(this.worldObj);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setLong("buildseed", this.buildseed);
    }
    
    public void initialize(World world)
    {
    	// reads from NBT default to 0 if they haven't been set yet.
    	// buildseed = 0 -> very fresh gnode
    	if (this.buildseed == 0)
    	{
    		this.buildseed = world.rand.nextLong();
    		if (this.buildseed == 0)
    		{
    			this.buildseed = 1;
    		}
    	}
    	this.buildrand = new Random(buildseed);
		this.blueprint = new LinkedList<BlockLocator>();
    	this.buildBlueprint();
    }
    
    // called every tick for update stuff
    public void updateEntity()
    {
    	if (!this.worldObj.isRemote)
    	{
    		if (this.blueprint == null)
    		{
    			this.initialize(this.worldObj);
    		}
        	if (this.blueprint.size() > 0)
        	{
        		BlockLocator loc = this.blueprint.poll();
            	Block oldblock = this.getWorldObj().getBlock(loc.x, loc.y, loc.z);
            	this.processBlueprintLoc(loc, oldblock);
        	}
    	}
    }


    /**
     * Attempts to assign a BlockLocator to the closest appropriate gnome
     * Returns TRUE if a gnome was found and the locator was passed to it
     * Returns FALSE if no gnome accepted the assignment
     * @param loc the BlockLocator (x,y,z,id) for this assignment
     */
	/*protected boolean attemptToAssignLocator(GnomeAssignment assign)
	{
		int radius = this.getRadius();
		List gnomelist = this.worldObj.getEntitiesWithinAABB(this.getGnomeType(), AxisAlignedBB.getAABBPool().getAABB(this.xCoord-radius, this.yCoord-radius, this.zCoord-radius, this.xCoord+radius, this.yCoord + radius, this.zCoord + radius));
        Collections.sort(gnomelist, this.sorter); // sort the list in order of closeness
		Iterator iter = gnomelist.iterator();
		
		while (iter.hasNext())
		{
			EntityGnome gnome = (EntityGnome)iter.next();
			if (gnome.attemptToAcceptAssignment(assign))
			{
				return true;
			}
		}
		return false;
	}*/
	
	/**
	 * Called by a gnome when the gnome end an assignment.
	 * Returns the BlockLocator to the blueprint list.
	 * (regardless of whether the gnome successfully completed it)
	 * @param finished = set to true to return to blueprint, false to return to assign queue
	 */
	public void endAssignment(GnomeAssignment assign, boolean finished, boolean mismatch)
	{
		if (finished)
		{
			this.blueprint.add(assign.loc);
		}
		else
		{
			this.assignmentQueue.add(assign);
		}
	}
	
	/**
	 * Attempt to delegate a Job to the closest appropriate gnome
	 * Returns TRUE if a job was delegated
	 * returns FALSE if no gnomes were available
	 */
	//public abstract boolean attemptToDelegateJob();
    
    /**
     * Generate the gnode's blueprint. It should use the entity's
     * buildrand Random to perform this task, which gets initialized
     * with the appropriate seed just before this function is called.
     */
    protected abstract void buildBlueprint();
    
    /**
     * Returns a Class for the type of gnome this gnode
	 * is associated with
     * @return some sort of gnome class
     */
    //@SuppressWarnings("rawtypes")
	//protected abstract Class getGnomeType();
    
    /**
     * Generates a Job for a gnome.
     * Can return null if no job needs to be done.
     * @return Job
     */
    public abstract Job generateJob();
    
    /**
     * Processes a blueprint location
     * Adds it to the assignment queue if necessary,
     * otherwise returns it to the blueprint
     * @loc The blocklocator from the blueprint
     * @oldid The existing block ID at that location
     */
    protected abstract void processBlueprintLoc(BlockLocator loc, Block oldblock);
    
    /**
     * Called when the block is broken
     */
    public abstract void onDestroy();
    
    /**
     * Called to remove this tileentity and turn the block into a normal block
     */
    public abstract void selfDestruct();
    
    /**
     * Called when a resident gnome dies
     */
    public abstract void onGnomeDeath();
    
    /**
     * Adds a gnome to this gnode's known resident gnomes
     */
    public abstract void addGnome(EntityGnome gnome);
    
    
    public int getRadius()
    {
    	return 16;
    }
    
    
}
