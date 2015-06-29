package commoble.ccritter.com.entity.gnome;

import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.block.tileentity.TileEntityGnode;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import commoble.ccritter.com.entity.ai.EntityAICreateGnomeCache;
import commoble.ccritter.com.entity.ai.EntityAIPerformJob;
import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.entity.ai.job.JobPanicTo;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.IntLoc;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class EntityGnomeWood extends EntityGnome
{
	public TileEntityGnode gnode;
	public IntLoc homeloc = null;
	public boolean panic;
	public static final int INVENTORY_MAX = 27;
	public ItemStack[] inventory = new ItemStack[INVENTORY_MAX];
	//public boolean hasChest;

	public EntityGnomeWood(World par1World)
	{
		super(par1World);

        this.setSize(0.6F, 0.8F);
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new EntityAIPanicToHome(this, 1.5D));
        //this.tasks.addTask(1, new EntityAIFleeFrom(this, 1.25D));
        //this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.5D));
        this.tasks.addTask(3, new EntityAICreateGnomeCache(this, 1.0));
        //this.tasks.addTask(4, new EntityAIPerformGnomeAssignment(this, 1.0));
        this.tasks.addTask(5, new EntityAIPerformJob(this));
        this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
        //this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
       // this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        //this.tasks.addTask(9, new EntityAILookIdle(this));
        
        this.gnode = null;
        this.panic = false;
        //this.hasChest = false;
	}
	
	public void finishSetBlock(GnomeAssignment assign, boolean finished, boolean mismatch)
	{
		if (this.gnode != null)
		{
			this.gnode.endAssignment(assign, finished, mismatch);
		}
	}

	@Override
	public Job getNewJob()
	{
		if (this.gnode != null)
		{
			return this.gnode.generateJob();
		}
		else if (this.panic)	// no home, panicking
		{
			Vec3 vec = null;
			while (vec == null)
			{
				vec = RandomPositionGenerator.findRandomTarget(this, 32,8);
			}
			return new JobPanicTo(this, vec, 1.5D);
		}
		else	// no home, not panicking
		{
			// return new JobCreateGnomeCache
			// TODO replace hovel creation with a Job class
			return null;
		}
	}
	
	@Override
	// gnome jobs have their own path weight, prefer milling around the hovel if no job
	public float getBlockPathWeight(int x, int y, int z)
	{
		if (this.job != null)
		{
			return this.job.getBlockPathWeight(x,y,z);
		}
		if (this.gnode != null)
		{
			int dist = ((this.gnode.xCoord - x)^2 + (this.gnode.yCoord -y)^2 + (this.gnode.zCoord - z)^2);
			int weight = 10000 - dist;
			return (weight > 0) ? weight : 0;
		}
		return 0;
	}
	
	@Override
    public void onLivingUpdate()
    {
		if (this.worldObj.getBlock((int)this.posX, (int)this.posY-1, (int)this.posZ) == Blocks.dirt)
        {
        	this.heal(0.1F);
        }

        super.onLivingUpdate();
    }

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		
		//this.assignedGnode = new TileEntityGnomeCache();
		
		return data;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damage, float f)
	{
		boolean superflag = super.attackEntityFrom(damage, f);
		if (superflag && !this.isDead && damage.getSourceOfDamage() != null)
		{
			if (this.job != null)
			{
				this.job.finishJob(false);
				this.job = null;
			}
			if (this.gnode != null)
			{	// if home is closer than 4 tiles, gtfo
				if (this.getDistanceSq(this.gnode.xCoord, this.gnode.yCoord-2, this.gnode.zCoord) < 9.0D)
				{
					this.gnode.selfDestruct();
				}
			}
			this.panic = true;
		}
		return superflag;
	}
	
	@Override
	public void onDeath(DamageSource damage)
	{
		if (this.getCarried() == Blocks.chest)
		{
			this.dropChest();
		}
		if (this.gnode != null)
		{
			//this.gnode.denizen = null;
			this.gnode.onGnomeDeath();
			this.gnode.selfDestruct();
		}
	}
	
	/**
	 * Find a nearby open surface and drop the carried items in a chest there
	 */
	private void dropChest()
	{
		int x = (int) Math.floor(this.posX);
		int y = (int) Math.floor(this.posY);
		int z = (int) Math.floor(this.posZ);
		
		int xtemp = x;
		int ytemp = y;
		int ztemp = z;
		
		while (this.worldObj.getBlock(xtemp,ytemp,ztemp) != Blocks.air)
		{
			if (ytemp < this.worldObj.getActualHeight()-1)
				ytemp++;
			else
			{
				ytemp = y;
				xtemp += (this.rand.nextInt(5) - 10);
				ztemp += (this.rand.nextInt(5) - 10);
			}
		}
		y = ytemp;
		while(this.worldObj.getBlock(xtemp,  ytemp-1,  ztemp) == Blocks.air)
		{
			if (ytemp > 0)
				ytemp--;
			else
			{
				ytemp = y;
				xtemp += (this.rand.nextInt(5) - 10);
				ztemp += (this.rand.nextInt(5) - 10);
			}
		}
		this.worldObj.setBlock(xtemp,ytemp,ztemp, Blocks.chest);
		TileEntityChest te = (TileEntityChest)this.worldObj.getTileEntity(xtemp,ytemp,ztemp);
		for (int i = 0; i<27; i++)
		{
			te.setInventorySlotContents(i, this.inventory[i]);
			this.inventory[i] = null;
		}
		this.setCarried(Blocks.air);
	}
	
	public void onHomeDestroyed()
	{
		this.gnode = null;
		this.job = null;	// TODO will need to handle placing chests if in mid-job
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		
		// if gnome has a home
		if (nbt.getBoolean("hasHome"))
		{
			int x = nbt.getInteger("homeX");
			int y = nbt.getInteger("homeY");
			int z = nbt.getInteger("homeZ");
			//this.home = (TileEntityGnomeCache)this.worldObj.getBlockTileEntity(x, y, z);
			this.homeloc = new IntLoc(x,y,z);
		}
		else
		{
			this.gnode = null;
		}
		

		// read inventory data
        NBTTagList nbttaglist = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        this.inventory = new ItemStack[27];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < 27)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if (this.gnode != null)
		{
			nbt.setBoolean("hasHome", true);
			nbt.setInteger("homeX", this.gnode.xCoord);
			nbt.setInteger("homeY", this.gnode.yCoord);
			nbt.setInteger("homeZ", this.gnode.zCoord);
		}
		else
		{
			nbt.setBoolean("hasHome", false);
		}
		

		// write inventory data
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
	}
	
	@Override
	public boolean canDespawn()
	{
		return false;
	}	
	


    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
    	int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlock(i, j - 1, k) == Blocks.grass && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }
}
