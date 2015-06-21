package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.monster.IPredator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPredVerifyConfidence extends EntityAIBase
{
	private IPredator predator;
	
	public EntityAIPredVerifyConfidence (IPredator pred)
	{
		this.predator = pred;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean shouldExecute()
	{
		EntityLiving ent = this.predator.getThisEntity();
		if (ent.getAttackTarget() != null)
		{
			Class targetClass = ent.getAttackTarget().getClass();
			if (this.predator.getConfidence() < this.predator.getConfidenceThresholdFor(targetClass))
			{
				ent.setAttackTarget(null);
			}
		}
		
		return false;
	}

}
