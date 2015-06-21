package commoble.ccritter.com.entity.monster;

import net.minecraft.entity.EntityLiving;

public interface IPredator
{
	public float getBreedValue();
	
	public void setBreedValue(float value);
	
	public EntityLiving getThisEntity();
	
	public IPredator getNewPredator();
	
	public float getConfidence();
	
	public void setConfidence(float conf);
	
	@SuppressWarnings("rawtypes")
	public float getConfidenceThresholdFor(Class targetClass);
}
