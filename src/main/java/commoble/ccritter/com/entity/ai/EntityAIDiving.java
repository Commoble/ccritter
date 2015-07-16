package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.entity.monster.EntityAnuranth;
import commoble.ccritter.com.util.MobbableBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIDiving extends EntityAIBase
{
	//private static boolean canDigBlocks[];
    private EntityAnuranth theEntity;

    public EntityAIDiving(EntityAnuranth par1EntityLiving)
    {
        this.theEntity = par1EntityLiving;
        this.setMutexBits(4);
        par1EntityLiving.getNavigator().setCanSwim(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.theEntity.isInWater() &&
        		this.theEntity.getAttackTarget() == null && this.theEntity.getHungerValue() < this.theEntity.getVeryHungryThreshold();
    }

    /**
     * Updates the task
     */
    @SuppressWarnings("static-access")
	public void updateTask()
    {
    	// even if the config is set to false,
    	// still run this because it blocks swimming
    	if (CCPMod.proxy.anuranths_dig)
    	{
			int i = MathHelper.floor_double(this.theEntity.posX);
	        int j = MathHelper.floor_double(this.theEntity.boundingBox.minY);
	        int k = MathHelper.floor_double(this.theEntity.posZ);
	        
	    	if (this.theEntity.worldObj.getBlock(i, j+3, k) != Blocks.water) // if shallow water
	        {
	        	digDown(this.theEntity.worldObj, i, j, k);		//isCollidedVertically &&
	        }
	    	else
	    	{	// if deep water
	            if (this.theEntity.getRNG().nextFloat() < 0.8F)
	            {
	                this.theEntity.getJumpHelper().setJumping();
	            }
	    	}
    	}
    }
    
    public void digDown(World world, int x, int y, int z)
    {
    	Block wat = Blocks.water;
    	Block mat;
    	
    	for (int iter = -1; iter < 2; iter++)
    	{
    		for (int kter = -1; kter < 2; kter++)
    		{
    			mat = world.getBlock(x+iter, y-1, z+kter);
    			//if(mat < 256 && canDigBlocks[mat] && world.getBlockId(x+iter, y, z+kter) == wat)
    			if(MobbableBlock.isSoftBlock(mat, world) && world.getBlock(x+iter, y, z+kter) == wat)
    				world.setBlock(x+iter, y-1, z+kter, wat);
    		}
    	}
    }
    
    /*static
    {
    	canDigBlocks = new boolean[256];
        canDigBlocks[Block.getIdFromBlock(Blocks.grass)] = true;
        canDigBlocks[Block.getIdFromBlock(Blocks.dirt)] = true;
        canDigBlocks[Block.getIdFromBlock(Blocks.sand)] = true;
        canDigBlocks[Block.getIdFromBlock(Blocks.gravel)] = true;
        canDigBlocks[Block.getIdFromBlock(Blocks.clay)] = true;
        canDigBlocks[Block.getIdFromBlock(Blocks.mycelium)] = true;
    }*/
}
