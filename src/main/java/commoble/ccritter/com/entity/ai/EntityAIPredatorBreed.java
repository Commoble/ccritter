package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.monster.IPredator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPredatorBreed extends EntityAIBase
{
    private IPredator predator;
    private EntityLiving theCreature;
    
	public EntityAIPredatorBreed(IPredator pred)
    {
        this.predator = pred;
        this.theCreature = pred.getThisEntity();
        this.setMutexBits(3);
    }
	
	@Override
	public boolean shouldExecute()
	{
		//int randint = this.theCreature.getRNG().nextInt(20);
		float randfloat = this.theCreature.getRNG().nextFloat()*1000.0F;
		
		if (this.theCreature.isInWater() &&
			this.theCreature.getAttackTarget() == null &&
			//(randint == 0) && 
			(randfloat < this.predator.getBreedValue() - 5.0F))
		{
			float breed_new = (this.predator.getBreedValue() * 0.5F) - 1.0F;
			this.predator.setBreedValue(breed_new);
			
			IPredator new_pred = this.predator.getNewPredator();
			new_pred.getThisEntity().copyLocationAndAnglesFrom(this.theCreature);
			new_pred.getThisEntity().onSpawnWithEgg((IEntityLivingData)null);
			new_pred.setBreedValue(breed_new);
			new_pred.setConfidence(this.predator.getConfidence());
			
			this.theCreature.worldObj.spawnEntityInWorld(new_pred.getThisEntity());
		}
		return false;
	}

}
