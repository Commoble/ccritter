package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.monster.EntityAnuranth;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIJumpInALake extends EntityAIBase
{
	private EntityAnuranth theCreature;
    private double waterX;
    private double waterY;
    private double waterZ;
    private double movementSpeed;
    private World theWorld;

    public EntityAIJumpInALake(EntityAnuranth par1EntityCreature, double par2)
    {
        this.theCreature = par1EntityCreature;
        this.movementSpeed = par2;
        this.theWorld = par1EntityCreature.worldObj;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.theCreature.isInWater() || this.theCreature.getAttackTarget() != null)
        {
            return false;
        }
        else
        {
    		//float confidence = this.theCreature.getConfidence();
    		//float breedvalue = this.theCreature.getBreedValue();

    		//if (confidence + this.theCreature.hunger < breedvalue)
    		if (this.theCreature.getHungerValue() < this.theCreature.getVeryHungryThreshold())// && this.theCreature.getRNG().nextInt(20) == 0)
    		{
	        	Vec3 vec3 = this.findWater();
	
	            if (vec3 == null)
	            {
	                return false;
	            }
	            else
	            {
	                this.waterX = vec3.xCoord;
	                this.waterY = vec3.yCoord;
	                this.waterZ = vec3.zCoord;
	                return true;
	            }
    		}
    		else
    			return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	if (this.theCreature.isInWater() || this.theCreature.getAttackTarget() != null)
    		return false;
    	else
    		return !this.theCreature.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theCreature.getNavigator().tryMoveToXYZ(this.waterX, this.waterY, this.waterZ, this.movementSpeed);
    }

    private Vec3 findWater()
    {
        //Random random = this.theCreature.getRNG();

        // first, look for nearby water blocks at sea level
        for (int i = 0; i < 10; ++i)
        {
            int x = MathHelper.floor_double(this.theCreature.posX + (double)this.theCreature.getRNG().nextInt(40) - 20.0D);
            int y = 62;	// look at sea level
            int z = MathHelper.floor_double(this.theCreature.posZ + (double)this.theCreature.getRNG().nextInt(40) - 20.0D);
            
            //System.out.println("Looking for a lake " + x + " " + y + " " + z + " = ");
            
            if (this.theWorld.getBlock(x, y, z) == Blocks.water)
            {
                return Vec3.createVectorHelper((double)x, (double)y, (double)z);
            }
        }

        // otherwise, use the regular pather
        
        return RandomPositionGenerator.findRandomTarget(this.theCreature, 20, 3);
    }
}
