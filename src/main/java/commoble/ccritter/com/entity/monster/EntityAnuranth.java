package commoble.ccritter.com.entity.monster;

import java.util.Iterator;
import java.util.List;

import akka.event.Logging.Debug;
import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.entity.ai.EntityAIDiving;
import commoble.ccritter.com.entity.ai.EntityAIHopping;
import commoble.ccritter.com.entity.ai.EntityAIJumpInALake;
import commoble.ccritter.com.entity.ai.EntityAIMigrate;
import commoble.ccritter.com.entity.ai.EntityAIPredVerifyConfidence;
import commoble.ccritter.com.entity.ai.EntityAIPredate;
import commoble.ccritter.com.entity.ai.EntityAIPredatorBreed;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

@SuppressWarnings("rawtypes")
public class EntityAnuranth extends EntityMob implements IPredator
{
	// used to determine when to make more anuranths
	protected float breedvalue;
	
	// used to determine whether to attack another entity
	protected float confidence;
	
	// used to normalize confidence level periodically
	protected int conf_reset_timer;
	
	// used to increase likelihood of predation as time between kills passes
	public int hunger;
	
	public static final int hungerthreshold = 30;

    @SuppressWarnings("static-access")
	public EntityAnuranth(World par1World)
    {
        super(par1World);
        this.tasks.addTask(0, new EntityAIDiving(this));
        this.tasks.addTask(0, new EntityAIHopping(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPredVerifyConfidence(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.5D, false));
        this.tasks.addTask(3, new EntityAIJumpInALake(this, 1.5D));
        this.tasks.addTask(4, new EntityAIMigrate(this, 1.0D));
        if (CCPMod.proxy.anuranths_breed)
        	this.tasks.addTask(4, new EntityAIPredatorBreed(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        //this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAIPredate(this, EntityPlayer.class, 0, false));
        if (CCPMod.proxy.anuranths_hunt)
        	this.targetTasks.addTask(2, new EntityAIPredate(this, EntityAnimal.class, 0, false));
        this.conf_reset_timer = 0;
        this.setSize(0.6F, 1.8F);
    }
    
    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     * Anuranths can spawn anywhere in swamps but only certain chunks in rivers and beaches (slime-style)
     */
    public boolean getCanSpawnHere()
    {
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
    	
    	if (this.rand.nextFloat() > this.worldObj.getCurrentMoonPhaseFactor())
    	{
    		return false;
    	}
    	
    	if (!this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
    	{
    		return false;
    	}

    	BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));       

        if (biomegenbase == BiomeGenBase.swampland)
        {
            return super.getCanSpawnHere();
        }

        if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(7717L).nextInt(20) == 0)
        {
            return super.getCanSpawnHere();
        }
        
        return false;
    }
    
    public float getBreedValue()
    {
    	return this.breedvalue;
    }
    
    public void setBreedValue(float value)
    {
    	this.breedvalue = value;
    }
    
    public EntityLiving getThisEntity()
    {
    	return this;
    }
    
    public IPredator getNewPredator()
    {
    	return new EntityAnuranth(this.worldObj);
    }
    
    public float getConfidence()
    {
    	return this.confidence;
    }
    
    public void setConfidence(float conf)
    {
    	this.confidence = conf;
    }
    
    public float getConfidenceThresholdFor(Class targetClass)
    {
    	if (EntityAnimal.class.isAssignableFrom(targetClass))
    	{
    		return (-1.0F) * (float)this.hunger;
    	}
    	else if (EntityPlayer.class.isAssignableFrom(targetClass))
    	{
    		return 10.0F - (0.05F * (float)this.hunger);
    	}
    	else
    	{
    		return 100.0F;
    	}
    }
    
    // raise Confidence of all nearby anuranths and this one
	public void moralizeAllies(float boost)
    {
    	double d0 = 20.0D;	// range
		List list = this.worldObj.getEntitiesWithinAABB(this.getClass(), AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(d0, 10.0D, d0));
        Iterator iterator = list.iterator();

        while (iterator.hasNext())
        {
            EntityAnuranth ent = (EntityAnuranth)iterator.next();

            ent.confidence += boost;	// includes this one
            
            ent.conf_reset_timer = 0;
        }
    }
    
    public void normalizeConfidence()
    {
    	if (!this.worldObj.isRemote)
    	{
	    	double d0 = 20.0D;	// range
	        List list = this.worldObj.getEntitiesWithinAABB(this.getClass(), AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(d0, 10.0D, d0));
	        int frog_count = list.size();
	        
	        // get average, approaches group size
	        this.confidence = (float)MathHelper.ceiling_float_int((this.confidence + frog_count) * 0.5F);
	        
	        // hasn't fought anything in a while, so this is a good place to reset the target
	        this.setAttackTarget(null);
	        
			if (this.hunger < 1000)
			{
				this.hunger++;
			}
    	}
    	
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }

    /*protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, Byte.valueOf((byte)0));
        this.getDataWatcher().addObject(13, Byte.valueOf((byte)0));
        this.getDataWatcher().addObject(14, Byte.valueOf((byte)0));
    }*/

    public boolean canBreatheUnderwater()
    {
        return true;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }
    
    // used for pathing
    // strongly prefer any stagnant water, prefer low ground to high
    @Override
    public float getBlockPathWeight(int x, int y, int z)
    {
    	if (this.getHungerValue() >= this.getVeryHungryThreshold() && this.getAttackTarget() == null)
    	{
    		// if very hungry, travel as far from self as possible
    		// find distance (root of squares)
    		int selfx = MathHelper.floor_double(this.posX);
    		int selfy = MathHelper.floor_double(this.posY);
    		int selfz = MathHelper.floor_double(this.posZ);
    		return (float) Math.sqrt(Math.pow(selfx - x, 2) + Math.pow(selfy-y, 2) + Math.pow(selfz-z, 2));
    	}
    	if (this.worldObj.getBlock(x, y, z) == Blocks.water)
    		return 10000.0F;
    	else
    		return 10000.0F - y;
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        //return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    	return false;
    }

    /**
     * Set whether this creature is a child.
     */
    /*public void setChild(boolean par1)
    {
        this.getDataWatcher().updateObject(12, Byte.valueOf((byte)(par1 ? 1 : 0)));

        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            AttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            attributeinstance.removeModifier(babySpeedBoostModifier);

            if (par1)
            {
                attributeinstance.applyModifier(babySpeedBoostModifier);
            }
        }
    }*/

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        this.conf_reset_timer++;
		if (this.conf_reset_timer > 500)
		{
			this.normalizeConfidence();
			this.conf_reset_timer = 0;
		}

        super.onLivingUpdate();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
    	boolean flag = super.attackEntityFrom(par1DamageSource, par2);
    	if (flag && !this.isDead)
    	{
    		
    		if (this.isInWater())
    		{
    			moralizeAllies(5.0F);
    		}
    		else
    		{
    			moralizeAllies(-5.0F);
    		}
    		
    		
    		// will always retaliate
			if (par1DamageSource.getSourceOfDamage() != null && this.confidence < 15.0F)
				this.confidence = 20.0F;
    	}
    	return flag;
    }
    
    @Override
    public void onDeath(DamageSource damageSource)
    {
    	// territorial, anger allies if dies in water
    	if (this.isInWater())
    	{
    		moralizeAllies(20.0F);
    	}
    	else	// but frighten allies if dies on land
    	{
    		moralizeAllies(-10.0F);
    	}
    	
    	super.onDeath(damageSource);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        boolean flag = super.attackEntityAsMob(par1Entity);

        if (flag)
        {
            moralizeAllies(0.5F);
        }

        return flag;
    }
    
    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase ent_other)
    {
        super.onKillEntity(ent_other);

        float exp_gain;
    	float mult = 0.5F * this.worldObj.difficultySetting.getDifficultyId();
    	
    	/*if (EntityPlayer.class.isAssignableFrom(ent_other.getClass()))
        {
        	exp_gain = ((EntityPlayer) ent_other).experienceLevel;
        	System.out.println("Killed player, exp increased by " + ((EntityPlayer) ent_other).experienceLevel);
        }
    	else if (EntityLiving.class.isAssignableFrom(ent_other.getClass()) && ((EntityLiving) ent_other).experienceValue > 0)
        {
        	exp_gain = ((EntityMob)ent_other).experienceValue;
        	System.out.println("Killed genereic EntityLiving");
        }
        else
        {
        	exp_gain = 5.0F;
        	System.out.println("Did NOT kill player OR entityliving");
        }*/
    	exp_gain = 5.0F;	// TODO eventually fix xp gain from Players
        
        this.breedvalue += this.getRNG().nextFloat() * exp_gain * mult;
        
        this.hunger = 0;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombiepig.zpig";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    @Override
    protected Item getDropItem()
    {
        return Items.dye;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int loot)
    {
        int j = this.rand.nextInt(2) + this.rand.nextInt(1 + loot);

        for (int k = 0; k < j; ++k)
        {	// 2 = green
        	this.entityDropItem(new ItemStack(Items.dye, 1, 2), 0.0F);
        }
    }

    protected void dropRareDrop(int par1)
    {
        switch (this.rand.nextInt(3))
        {
            case 0:
            	this.entityDropItem(new ItemStack(Blocks.brown_mushroom), 0.0F);
                break;
            case 1:
            	this.entityDropItem(new ItemStack(Blocks.brown_mushroom), 0.0F);
            	break;
            case 2:
            	this.entityDropItem(new ItemStack(Blocks.red_mushroom), 0.0F);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        
        par1NBTTagCompound.setFloat("BreedValue", this.breedvalue);
        par1NBTTagCompound.setFloat("Confidence", this.confidence);
        par1NBTTagCompound.setInteger("Hunger", this.hunger);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.breedvalue = par1NBTTagCompound.getFloat("BreedValue");
        this.confidence = par1NBTTagCompound.getFloat("Confidence");
        this.hunger = par1NBTTagCompound.getInteger("Hunger");
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
    {
        Object par1EntityLivingData1 = super.onSpawnWithEgg(par1EntityLivingData);
        // harder zone = capable of spawning more anuranths
        
        // tension function isn't named currently
        //float f = this.worldObj.getLocationTensionFactor(this.posX, this.posY, this.posZ);
        // returns a float between 0.0 and 1.25 (1.5?), max 1.0 unless on hard
        float f = this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);
        this.breedvalue = f * 4.0F;	// TODO calibrate starting breedvalue
        this.confidence = 1.0F;
        this.hunger = 0;
        return (IEntityLivingData) par1EntityLivingData1;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return (this.hunger >= 300 && this.confidence <= 3);	// if alone and hungry
    }

	@Override
	public int getHungerValue()
	{
		return this.hunger;
	}
	
	@Override
	public int getVeryHungryThreshold()
	{
		return this.hungerthreshold;
	}
}