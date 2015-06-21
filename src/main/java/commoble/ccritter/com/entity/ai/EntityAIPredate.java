package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.monster.IPredator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

@SuppressWarnings("rawtypes")
public class EntityAIPredate extends EntityAINearestAttackableTarget
{
	IPredator predator;
	private final Class targetClass;

	public EntityAIPredate(IPredator predator, Class targetClass, int targetChance, boolean shouldCheckSight)
	{
		super((EntityCreature)predator.getThisEntity(), targetClass, targetChance, shouldCheckSight);
		this.targetClass = targetClass;
		this.predator = predator;
	}
	
	public boolean shouldExecute()
	{
		float threshold = this.predator.getConfidenceThresholdFor(targetClass);
		float confidence = this.predator.getConfidence();
		float breedvalue = this.predator.getBreedValue();
		if (confidence < threshold + breedvalue)
			return false;
		else
			return super.shouldExecute();
	}
}
