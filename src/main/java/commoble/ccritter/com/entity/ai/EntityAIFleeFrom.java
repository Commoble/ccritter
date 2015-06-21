package commoble.ccritter.com.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

// better than EntityAIPanic but some required fields are private so can't inherit
public class EntityAIFleeFrom extends EntityAIBase
{

	private EntityCreature theEntityCreature;
    private double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public EntityAIFleeFrom(EntityCreature par1EntityCreature, double par2)
    {
        this.theEntityCreature = par1EntityCreature;
        this.speed = par2;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.theEntityCreature.getAITarget() == null && !this.theEntityCreature.isBurning())
        {
            return false;
        }
        else
        {
        	EntityLivingBase target = this.theEntityCreature.getAITarget();
        	//Vec3 target_vec = this.theEntityCreature.worldObj.getWorldVec3Pool().getVecFromPool(target.posX, target.posY, target.posZ);
        	Vec3 target_vec = Vec3.createVectorHelper(target.posX,  target.posY,  target.posZ);
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntityCreature, 5, 4, target_vec);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                this.randPosX = vec3.xCoord;
                this.randPosY = vec3.yCoord;
                this.randPosZ = vec3.zCoord;
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.theEntityCreature.getNavigator().noPath();
    }

}
