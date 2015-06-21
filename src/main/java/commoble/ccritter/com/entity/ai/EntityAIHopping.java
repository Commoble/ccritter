package commoble.ccritter.com.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIHopping extends EntityAIBase
{
    private EntityLiving theEntity;

    public EntityAIHopping(EntityLiving par1EntityLiving)
    {
        this.theEntity = par1EntityLiving;
        this.setMutexBits(4);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {	// if frogman is on land and moving
        return !this.theEntity.isInWater() && this.theEntity.moveForward>0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.theEntity.getRNG().nextFloat() < 0.3F)
        {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
