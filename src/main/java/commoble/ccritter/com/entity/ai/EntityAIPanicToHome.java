package commoble.ccritter.com.entity.ai;

import commoble.ccritter.com.entity.gnome.EntityGnomeWood;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIPanicToHome extends EntityAIBase
{
    private EntityGnomeWood gnome;
    private double speed;
    private double x;
    private double y;
    private double z;
    private int panictimer;
    private boolean panicking;

    public EntityAIPanicToHome(EntityGnomeWood ent, double par2)
    {
        this.gnome = ent;
        this.speed = par2;
        this.setMutexBits(1);
        this.panictimer = 0;
        this.panicking = false;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if ((this.gnome.getAITarget() == null && !this.gnome.isBurning()) || this.panicking)
        {
            return false;
        }
        else
        {	// run home if possible
        	if (this.gnome.gnode != null)
        	{
                this.x = this.gnome.gnode.xCoord;
                this.y = this.gnome.gnode.yCoord-2;
                this.z = this.gnome.gnode.zCoord;
                return true;
        	}
        	else	// no home
        	{
        		Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.gnome, 16, 4);

                if (vec3 == null)
                {
                    return false;
                }
                else
                {
                    this.x = vec3.xCoord;
                    this.y = vec3.yCoord;
                    this.z = vec3.zCoord;
                    return true;
                }
        	}
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	if (this.panictimer <= 0)
    	{
    		this.panictimer = 100;
    	}
        this.gnome.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
        this.panicking = true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	if (this.panictimer > 0)
    	{
    		this.panictimer--;
    	}
    	else
    	{
    		this.gnome.setRevengeTarget(null);
    		this.panicking = false;
    		return false;
    	}
    	if ((this.gnome.gnode != null && this.gnome.getDistance(x, y, z) < 1.5D))
    	{
    		this.gnome.setRevengeTarget(null);
    		this.panicking = false;
        	return false;
    	}

    	if (this.gnome.getNavigator().noPath())
    	{
    		this.panicking = false;
    		return false;
    	}
    	
    	return true;
    }
}
