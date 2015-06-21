package commoble.ccritter.com.entity.ai.job;

import net.minecraft.util.Vec3;

import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.util.IntLoc;

public class JobPanicTo extends JobMoveTo
{

	public JobPanicTo(EntityGnomeWood ent, Vec3 vec, double speed)
	{
		super(ent, vec, speed);
	}
	
	public JobPanicTo(EntityGnomeWood ent, IntLoc loc, double speed)
	{
		super(ent, loc, speed);
	}

	@Override
	public void finishJob(boolean near)
	{
		if (near)
		{
			((EntityGnomeWood)this.gnome).panic = false;
		}
	}
}
