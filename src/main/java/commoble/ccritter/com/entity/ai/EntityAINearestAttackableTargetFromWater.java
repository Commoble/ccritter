package commoble.ccritter.com.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityAINearestAttackableTargetFromWater extends EntityAINearestAttackableTarget
{
	
	
	@SuppressWarnings("rawtypes")
	public EntityAINearestAttackableTargetFromWater(
			EntityCreature par1EntityCreature, Class par2Class, int par3,
			boolean par4)
	{
		super(par1EntityCreature, par2Class, par3, par4);
	}

	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.taskOwner.isInWater())
        	return false;
        else
        	return super.shouldExecute();
    }

}
