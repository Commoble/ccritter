package commoble.ccritter.com.util;

import java.util.Comparator;

import net.minecraft.tileentity.TileEntity;
import commoble.ccritter.com.block.tileentity.TileEntityGnode;
import commoble.ccritter.com.entity.gnome.EntityGnome;

/**
 * Sorts gnomes based on their distance to a gnode
 *
 */
@SuppressWarnings("rawtypes")
public class GnomeSorter implements Comparator
{
	private TileEntity gnode;
	
	public GnomeSorter(TileEntityGnode gnode)
	{
		this.gnode = gnode;
	}
	
	private int compareGnomeDistance(EntityGnome gnome1, EntityGnome gnome2)
	{
		double d1 = this.gnode.getDistanceFrom(gnome1.posX, gnome1.posY, gnome1.posZ);
		double d2 = this.gnode.getDistanceFrom(gnome2.posX, gnome2.posY, gnome2.posZ);
		return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
	}

	@Override
	public int compare(Object obj1, Object obj2)
	{
		return this.compareGnomeDistance((EntityGnome)obj1, (EntityGnome)obj2);
	}
}
