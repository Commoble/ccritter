package commoble.ccritter.com.entity.gnome;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import commoble.ccritter.com.entity.ai.EntityAIPerformJob;
import commoble.ccritter.com.entity.ai.job.Job;
import commoble.ccritter.com.util.GnomeAssignment;

public class EntityGnomeDeep extends EntityGnome
{
	public EntityGnomeDeep(World par1World)
	{
		super(par1World);

        this.setSize(0.6F, 0.9F);
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new EntityAIPanicToHome(this, 1.5D));
        //this.tasks.addTask(1, new EntityAIFleeFrom(this, 1.25D));
        //this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.5D));
        //this.tasks.addTask(3, new EntityAICreateGnomeCache(this, 1.0));
        //this.tasks.addTask(4, new EntityAIPerformGnomeAssignment(this, 1.0));
        this.tasks.addTask(5, new EntityAIPerformJob(this));
        this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        //this.hasChest = false;
	}
	
	@Override
	protected Job getNewJob()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finishSetBlock(GnomeAssignment assign, boolean finished)
	{
		// TODO Auto-generated method stub

	}

	/**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (this.worldObj.difficultySetting.getDifficultyId() > 0)
        {
        	/*if (this.rand.nextInt(10) != 1)
        	{
        		return false;
        	}*/
            if (this.posY < 40.0D)
            {
            	/**
                 * Checks if the entity's current position is a valid location to spawn this entity.
                 */
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.boundingBox.minY);
                int k = MathHelper.floor_double(this.posZ);
                return this.worldObj.getBlock(i, j - 1, k) == Blocks.stone && this.worldObj.getFullBlockLightValue(i, j, k) < 8 && super.getCanSpawnHere();
            }
        }

        return false;
    }
}
