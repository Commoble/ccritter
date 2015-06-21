package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.gnome.EntityGnome;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMischief extends EntityAIBase
{
	private int x;
	private int y;
	private int z;
	private double speed;
	private EntityGnome gnome;

	public EntityAIMischief(EntityGnome gnome, double speed)
	{
		this.gnome = gnome;
		this.speed = speed;
		this.setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (this.gnome.getRNG().nextInt(10) == 0)
		{
			
		}
		return false;
	}
	
}
