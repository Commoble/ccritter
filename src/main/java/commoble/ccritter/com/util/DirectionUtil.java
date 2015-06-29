package commoble.ccritter.com.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import net.minecraftforge.common.util.ForgeDirection;

public class DirectionUtil
{
	/**
	 * Returns the set of the six cardinal directions in a random order
	 * If boolean usingNullDirection == true, will also add (0,0,0) to the list
	 * omitting the boolean = false
	 */	
	public static ForgeDirection[] getRandomizedDirections(Random rand, boolean usingNullDirection)
	{
		LinkedList<ForgeDirection> dirs = new LinkedList<ForgeDirection>();
		dirs.add(ForgeDirection.UP);
		dirs.add(ForgeDirection.DOWN);
		dirs.add(ForgeDirection.NORTH);
		dirs.add(ForgeDirection.SOUTH);
		dirs.add(ForgeDirection.EAST);
		dirs.add(ForgeDirection.WEST);
		if (usingNullDirection)
		{
			dirs.add(ForgeDirection.UNKNOWN);
		}

		Collections.shuffle(dirs);
		ForgeDirection[] dirarray = new ForgeDirection[dirs.size()];

		return dirs.toArray(new ForgeDirection[0]);
	}
	

	public static ForgeDirection[] getRandomizedDirections(Random rand)
	{
		return getRandomizedDirections(rand, false);
	}
}
