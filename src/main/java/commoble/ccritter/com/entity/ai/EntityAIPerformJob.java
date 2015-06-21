package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.gnome.EntityGnome;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPerformJob extends EntityAIBase
{
	private EntityGnome gnome;
	// used to do assignments, may change assignments to a type of job
	int x;
	int y;
	int z;
	double xd;
	double yd;
	double zd;
	int oldid;
	int newid;

	public EntityAIPerformJob(EntityGnome gnome)
	{
		this.gnome = gnome;
		this.setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute()
	{
		this.gnome.updateJob();
		if (this.gnome.job != null)
		{
			return this.gnome.job.shouldStart();
		}
		else	// no job
		{
			return false;
		}
	}
	
	@Override
	public void startExecuting()
	{
		if (this.gnome.job != null)
		{
			this.gnome.job.start();
		}
	}
	
	@Override
	public boolean continueExecuting()
	{
		if (this.gnome.job != null)
		{
			return this.gnome.job.shouldContinue();
		}
		else	// no job
		{
			return false;
		}
	}
}
