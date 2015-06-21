package commoble.ccritter.com.entity.ai.job;

import net.minecraft.util.Vec3;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.util.IntLoc;

public class JobMoveTo extends Job
{
	public JobMoveTo(EntityGnome ent, IntLoc loc, double speed)
	{
		super(ent, loc);
		this.speed = speed;
	}
	
	public JobMoveTo(EntityGnome ent, Vec3 vec, double speed)
	{
		super(ent, vec);
		this.speed = speed;
	}

	@Override
	public void finishJob(boolean near)
	{
		return;
	}
	
}
