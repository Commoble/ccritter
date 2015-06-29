package commoble.ccritter.com.entity.gnome;

import java.util.Stack;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.entity.ai.EntityAIFleeFrom;
import commoble.ccritter.com.entity.ai.EntityAIPerformJob;
import commoble.ccritter.com.entity.ai.EntityAISpin;
import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.entity.ai.job.JobChestFill;
import commoble.ccritter.com.entity.ai.job.JobMoveTo;
import commoble.ccritter.com.entity.ai.job.JobRetrieveDroppedItem;
import commoble.ccritter.com.entity.ai.job.JobSetBlock;
import commoble.ccritter.com.item.ItemCCMonsterPlacer;
import commoble.ccritter.com.util.BlockLocator;
import commoble.ccritter.com.util.DirectionUtil;
import commoble.ccritter.com.util.EnumAssignType;
import commoble.ccritter.com.util.GnomeAssignment;
import commoble.ccritter.com.util.IntLoc;
import commoble.ccritter.com.util.MobbableBlock;

public class EntityGnomeDeep extends EntityGnome
{
	//private boolean mining;	// true if the gnome's current job intends to put a dug block in the gnome's inventory
	public IntLoc chest;	// location of this gnome's Deep Chest
	public boolean hasChest;	// mostly important for NBT save/load
	private int mineTimeOut;
	public int chestTries;
	public Stack<EntityItem> droppedItems;	// list of items the gnome dropped and needs to pick up
	
	public EntityGnomeDeep(World par1World)
	{
		super(par1World);

        this.setSize(0.6F, 0.9F);
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new EntityAIPanicToHome(this, 1.5D));
        this.tasks.addTask(1, new EntityAIFleeFrom(this, 1.5D));
        //this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.5D));
        //this.tasks.addTask(3, new EntityAICreateGnomeCache(this, 1.0));
        //this.tasks.addTask(4, new EntityAIPerformGnomeAssignment(this, 1.0));
        this.tasks.addTask(5, new EntityAIPerformJob(this));
        this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(6, new EntityAISpin(this));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        //this.tasks.addTask(9, new EntityAILookIdle(this));
        
        this.mineTimeOut = 0;
        this.chestTries = 0;
        this.chest = null;
        this.hasChest = false;
        this.canMineFreely = true;
        this.droppedItems = new Stack<EntityItem>();
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
    }
	
	@Override
	protected Job getNewJob()
	{
		Block carried = this.getCarried();
		if (carried == Blocks.air)
		{
			if (this.droppedItems.size() > 0 && this.hasChest)
			{
				return this.getRetrieveDroppedItemJob();
			}
			return this.getMineJob();
		}	// if this is carrying something and has a chest
		else if (this.hasChest)
		{
			return this.getDeepChestFillJob();
		}	// if this has no chest but is carrying stone
		else if (carried == Blocks.stone)
		{
			return this.getDeepChestPlaceJob();
		}
		else	// carrying a non-stone block, no chest
		{
			ItemStack dropstack = new ItemStack(this.getCarried(), 1);
			EntityItem itement = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, dropstack);
			this.worldObj.spawnEntityInWorld(itement);
			this.droppedItems.push(itement);
			this.setCarried(Blocks.air);
			return this.getMineJob();
		}
	}
	
	protected Job getRetrieveDroppedItemJob()
	{
		EntityItem item = this.droppedItems.pop();
		if (item != null)
		{
			int x = MathHelper.floor_double(item.posX);
			int y = MathHelper.floor_double(item.posY);
			int z = MathHelper.floor_double(item.posZ);
			
			return new JobRetrieveDroppedItem(this, new IntLoc(x,y,z), item);
		}
		return null;
	}
	
	protected Job getMineJob()
	{
		if (this.mineTimeOut > 0)
		{
			return null;
		}
		
		// find a suitable block to mine
		// start with the direction looking at
		Vec3 vec = this.getLookVec();	// sqrt(vecx^2 + vecy^2 + vecz^2) = 1
		
		int xrand = this.getRNG().nextInt(4);
		if (xrand == 0) vec.xCoord += 2.0D;
		if (xrand == 1) vec.xCoord -= 2.0D;
		
		int yrand = this.getRNG().nextInt(16);
		if (yrand == 0) vec.yCoord += 2.0D;
		else if (yrand == 1) vec.yCoord -= 2.0D;
		else if (yrand == 3) vec.yCoord += 1.0D;
		else if (yrand == 4) vec.yCoord -= 1.0D;
		
		int zrand = this.getRNG().nextInt(4);
		if (zrand == 0) vec.zCoord += 2.0D;
		if (zrand == 1) vec.zCoord -= 2.0D;
		
		vec.normalize();
		
		int xstart = MathHelper.floor_double(this.posX);
		int ystart = MathHelper.floor_double(this.posY);
		int zstart = MathHelper.floor_double(this.posZ);
		int xoff;
		int yoff;
		int zoff;
		for (int i=1; i<16; i++)
		{	// iterate along line of sight
			// +x (facing south) if yaw<180
			// -z (facing west) if 90 <= yaw < 270
			//int xsign = (this.rotationYaw < 180) ? 1 : -1;
			//int zsign = (this.rotationYaw >= 90 && this.rotationYaw < 270) ? -1 : 1;
			xoff = MathHelper.floor_double((vec.xCoord)*(i*0.5D));
			yoff = MathHelper.floor_double((vec.yCoord)*(i*0.5D) + 0.5);
			zoff = MathHelper.floor_double((vec.zCoord)*(i*0.5D));
			
			int xend = xstart + xoff;
			int yend = ystart + yoff;
			int zend = zstart + zoff;
			
			Block blockcheck = this.worldObj.getBlock(xend, yend, zend);
			
			// if block is generic mineable and the gnome has a chest, grab it
			if (this.hasChest && MobbableBlock.isMineableBlock(blockcheck, this.worldObj))
			{
				return new JobSetBlock(this, new GnomeAssignment(new BlockLocator(xend, yend, zend, Blocks.air), blockcheck, EnumAssignType.HARVEST));
			}
			// if this block is a stone block, grab it even if no chest
			if (blockcheck == Blocks.stone)
			{
				return new JobSetBlock(this, new GnomeAssignment(new BlockLocator(xend, yend, zend, Blocks.air), blockcheck, EnumAssignType.HARVEST));
			}	// if this is an air block, check for nearby mineable
			/*else if (blockcheck == Blocks.air)
			{
				// get random directions and check two
				ForgeDirection[] dirs = DirectionUtil.getRandomizedDirections(this.rand, false);
				for (int j=0; j<2; j++)
				{
					int xcheck = xend + dirs[j].offsetX;
					int ycheck = yend + dirs[j].offsetY;
					int zcheck = zend + dirs[j].offsetZ;
					
					Block blockcheck2 = this.worldObj.getBlock(xcheck, ycheck, zcheck);
					
					// if block is generic mineable and the gnome has a chest, grab it
					if (this.hasChest && MobbableBlock.isMineableBlock(blockcheck2, this.worldObj))
					{
						return new JobSetBlock(this, new GnomeAssignment(new BlockLocator(xcheck, ycheck, zcheck, Blocks.air), blockcheck2, EnumAssignType.HARVEST));
					}
					// if this block is a stone block, grab it even if no chest
					if (blockcheck2 == Blocks.stone)
					{
						return new JobSetBlock(this, new GnomeAssignment(new BlockLocator(xcheck, ycheck, zcheck, Blocks.air), blockcheck2, EnumAssignType.HARVEST));
					}
				}
			}*/
			else if (blockcheck.getCollisionBoundingBoxFromPool(this.worldObj, xend, yend, zend) != null && blockcheck != CommonProxy.deepChest)	// any other solid block, stop looking
			{
				return null;
			}
		}
		// if iterated this far and all is air, move in this direction

		xoff = MathHelper.floor_double(vec.xCoord*9);
		yoff = MathHelper.floor_double(vec.yCoord*9);
		zoff = MathHelper.floor_double(vec.zCoord*9);
		return new JobMoveTo(this, new IntLoc(xstart + xoff, ystart + yoff, zstart + zoff), 1.0);
	}
	
	public JobSetBlock getDeepChestPlaceJob()
	{	// 4 attempts per tick
		for (int i=0; i<4; i++)
		{
			int x = MathHelper.floor_double(this.posX) + (this.getRNG().nextInt(5) - 2);	// adds -2, -1, 0, 1, or 2 to xpos
			int y = MathHelper.floor_double(this.posY);
			int z = MathHelper.floor_double(this.posZ) + (this.getRNG().nextInt(5) - 2);
			
			if (this.worldObj.getBlock(x, y, z) == Blocks.air)
			{	// We want to place the chest in a spot with an air block above it for spawning new deep gnomes
				IntLoc loc = Job.groundify(this.worldObj, new IntLoc(x,y,z));	// first find an air block touching the ground
				if (this.worldObj.getBlock(loc.x, loc.y-1, loc.z) == Blocks.stone)
				{	// if the spot below it is stone, put the chest in the stone
					return new JobSetBlock(this, new GnomeAssignment(new BlockLocator(loc.x, loc.y, loc.z, CommonProxy.deepChest), Blocks.air, EnumAssignType.ALTER));
				}
				else if (this.worldObj.getBlock(loc.x,  loc.y+1,  loc.z) == Blocks.air)
				{	// otherwise if there's air above the spot (two air blocks in a column above the ground) put it in the lower air
					return new JobSetBlock(this, new GnomeAssignment(new BlockLocator(loc.x, loc.y, loc.z, CommonProxy.deepChest), Blocks.air, EnumAssignType.CREATE));
				}
			}
		}
		
		return null;
	}
	
	public JobChestFill getDeepChestFillJob()
	{
		return new JobChestFill(this, this.chest);
	}

	@Override
	public void finishSetBlock(GnomeAssignment assign, boolean finished, boolean mismatch)
	{
		if (finished && !mismatch)	// success
		{
			if (assign.type == EnumAssignType.HARVEST)
			{
				if (this.getCarried() == Blocks.air)
				{
					this.setCarried(assign.oldblock);
				}
				else	// if gnome already holds block, drop the mined block on the ground
				{
					ItemStack stack = new ItemStack(assign.oldblock, 1);
					EntityItem itement = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
					this.worldObj.spawnEntityInWorld(itement);
					this.droppedItems.push(itement);
				}
				this.mineTimeOut = 5 + this.getRNG().nextInt(20);
			}
			else if (assign.loc.block == CommonProxy.deepChest)	// placed a deep chest
			{
				this.chest = new IntLoc(assign.loc.x, assign.loc.y, assign.loc.z);
				this.hasChest = true;
			}
		}
		else	// failure
		{
			this.mineTimeOut = 50 + this.getRNG().nextInt(50);
		}
	}
	
	@Override
	public void onPlaceItemInChest(int x, int y, int z, ItemStack stack, boolean success)
	{
		if (success == false) // chest was full
		{
			this.hasChest = false;
			this.chest = null;
		}
		else
		{
			this.chestTries = 0;
		}
		if (stack != null && stack.getItem() == Blocks.stone.getItem(this.worldObj, x, y, z) && stack.stackSize >= 64)
		{
			stack.splitStack(64);
    		EntityGnomeDeep newgnome = (EntityGnomeDeep)((ItemCCMonsterPlacer) CCPMod.proxy.eggGnomeDeep).spawnEntity(this.worldObj, x+0.5D, y+1.0D, z+0.5D);
    		newgnome.chest = this.chest;
    		newgnome.hasChest = true;
		}
	}

	/**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        /*if (this.worldObj.difficultySetting.getDifficultyId() > 0)
        {
        	if (this.rand.nextInt(10) != 1)
        	{
        		return false;
        	}
            if (this.posY < 40.0D)
            {
            	
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.boundingBox.minY);
                int k = MathHelper.floor_double(this.posZ);
                return this.worldObj.getBlock(i, j - 1, k) == Blocks.stone && this.worldObj.getFullBlockLightValue(i, j, k) < 8 && super.getCanSpawnHere();
            }
        }*/

        return false;
    }
    
    @Override
    public void onLivingUpdate()
    {
    	super.onLivingUpdate();
    	if (this.mineTimeOut > 0)
    	{
    		this.mineTimeOut--;
    	}
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		
		// if gnome has a home
		if (nbt.getBoolean("hasChest"))
		{
			int x = nbt.getInteger("homeX");
			int y = nbt.getInteger("homeY");
			int z = nbt.getInteger("homeZ");
			//this.home = (TileEntityGnomeCache)this.worldObj.getBlockTileEntity(x, y, z);
			this.chest = new IntLoc(x,y,z);
			this.hasChest = true;
		}
		else
		{
			this.chest = null;
			this.hasChest = false;
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if (this.hasChest)
		{
			nbt.setBoolean("hasChest", true);
			nbt.setInteger("homeX", this.chest.x);
			nbt.setInteger("homeY", this.chest.y);
			nbt.setInteger("homeZ", this.chest.z);
		}
		else
		{
			nbt.setBoolean("hasChest", false);
		}
	}
    
    
}
