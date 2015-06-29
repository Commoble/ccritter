package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.monster.IPredator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIMigrate extends EntityAIBase
{
    private EntityCreature entity;
    private IPredator pred;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;

    /**
     * This is similar to Wander except the creature tries to move long distances
     * Not extending Wander because there's some annoying problems with private fields
     * @param ent
     * @param speed
     */
    public EntityAIMigrate(IPredator ent, double speed)
    {
        this.entity = (EntityCreature) ent;
        this.pred = ent;
        this.speed = speed;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.entity.getAttackTarget() != null)// || (this.pred.getHungerValue() < this.pred.getVeryHungryThreshold()))
        {
        	return false;
        }
        else if (this.entity.getRNG().nextInt(20) != 0)
        {
            return false;
        }
        else
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 20, 7);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                this.xPosition = vec3.xCoord;
                this.yPosition = vec3.yCoord;
                this.zPosition = vec3.zCoord;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}