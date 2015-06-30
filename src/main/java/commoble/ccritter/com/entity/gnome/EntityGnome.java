package commoble.ccritter.com.entity.gnome;

import java.util.Stack;

import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.IntLoc;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class EntityGnome extends EntityCreature
{
	//public EntityGnode assignedGnode;
	public boolean needsFreshGnode;	// whether a new Gnode needs to be created
	//public Vec3 gnodeVec;	// location of assigned Gnode
	//public GnomeAssignment assignment;	// the (x,y,z,id) assigned to this gnome
	public Job job;
	public boolean canMineFreely;
	
	// datawatcher ids
	public static final int carryDataId = 16;

	public EntityGnome(World par1World)
	{
		super(par1World);
        this.experienceValue = 5;
        //this.assignedGnode = null;
        this.needsFreshGnode = false;
        //this.gnodeVec = null;
        //this.assignment = null;
        this.job = null;
        this.canMineFreely = false;
	}
	
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(carryDataId, new Byte((byte)Block.getIdFromBlock(Blocks.air)));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
    }
	
	
	
	/**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();

        super.onLivingUpdate();
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    }
    
    /**
     * Attempt to accept a BlockLocator assignment from a Gnode.
     * 
     */
    /*public boolean attemptToAcceptAssignment(GnomeAssignment assign)
    {
    	if (this.assignment == null)
    	{
    		this.assignment = assign;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }*/
    
    /*public boolean attemptToAcceptJob(Job job)
    {
    	if (this.job == null)
    	{
    		this.job = job;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }*/
    
    /**
     * Checks the gnome's current job,
     * and generates a new one if no current job
     */
    public void updateJob()
    {
    	// if gnome has existing job, do nothing
    	if (this.job != null)
    	{
    		return;
    	}
    	else	// if gnome has no job, get new one
    	{
    		this.job = this.getNewJob();
    	}
    }
    
    /**
     * Returns a Job object, used by updateJob
     * to find a Job when the gnome has no existing job
     * @return Job
     */
    protected abstract Job getNewJob();
    
    /**
     * Called when a Job ends, regardless of whether it was successfully completed
     * @param assign The job's assignment data
     * @param finished Whether the job site was reached
     * @param mismatch if the job site was reached but failed due to block mismatch, this will be true, false in any other case
     */
    public abstract void finishSetBlock(GnomeAssignment assign, boolean finished, boolean mismatch);
    
    /**
     * This function is called when the gnome successfully finishes a JobChestFill. Coordinates are the
     * coordinates of the chest, stack is the ItemStack in the chest that was added to 
     * If chest is full, success is FALSE and contents do not change
     */
    public void onPlaceItemInChest(int x, int y, int z, ItemStack stack, boolean success)
    {
    	return;
    }
    
    //public abstract boolean canAlterBlock(int id);

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    /*public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return 0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
    }/*

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }
    
    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "ccritter:mob.gnome.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "ccritter:mob.gnome.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "ccritter:mob.gnome.death";
    }
    
    /*@Override
    public void playLivingSound()
    {
        String s = this.getLivingSound();

        if (s != null)
        {
            this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
        }
    }*/

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.pig.step", 0.15F, 1.0F);
    }
    
    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        
    }

    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        return 1 + this.worldObj.rand.nextInt(3);
    }
    

	
	public void setCarried(Block block)
	{
		this.dataWatcher.updateObject(carryDataId, Byte.valueOf((byte)(Block.getIdFromBlock(block) & 0xFF)));
	}
	
	public Block getCarried()
    {
        return (Block.getBlockById(this.dataWatcher.getWatchableObjectByte(carryDataId)));
    }
	
	@Override
	public boolean canDespawn()
	{
		return false;
	}
	
	@Override
	public EntityLivingBase getAITarget()
	{
		// this is mostly used for entityaifleefrom
		if (this.isBurning())
		{
			return this;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.setCarried(Block.getBlockById(nbt.getInteger("carriedBlock")));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger("carriedBlock", Block.getIdFromBlock(this.getCarried()));
	}	
}
