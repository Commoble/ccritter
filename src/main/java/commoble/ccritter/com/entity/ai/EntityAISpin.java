package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.gnome.EntityGnome;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

/** this just makes a critter spin in place for a few seconds
 * 
 * @author Joseph
 *
 */
public class EntityAISpin extends EntityAIBase
{
	private EntityCreature ent;
	private int timer;
	private float spin;

	public EntityAISpin(EntityCreature ent)
	{
		this.ent = ent;
		this.setMutexBits(1);
		this.timer = 0;
		this.spin = (this.ent.getRNG().nextInt(2) == 0 ? 5.0F : -5.0F);
	}

	@Override
	public boolean shouldExecute()
	{
		// TODO Auto-generated method stub
		return true; //ent.getRNG().nextInt(10) == 0;
	}
	
	@Override
	public void startExecuting()
	{
		this.timer = ent.getRNG().nextInt(30) + 10;
	}
	
	@Override
	public void updateTask()
	{
		this.ent.rotationYaw += spin;
		if (this.timer > 0)
		{
			this.timer--;
		}
	}
	
	@Override
	public boolean continueExecuting()
	{
		return this.timer > 0;
	}

}
