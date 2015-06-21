package commoble.ccritter.com.util;

import net.minecraft.block.Block;

public class GnomeAssignment
{
	public final BlockLocator loc;
	public final Block oldblock;
	public final EnumAssignType type;
	
	/**
	 * Contains a BlockLocator
	 * -which contains a position and a block type to change a position to
	 * And the expected existing block type of that location.
	 * The existing type is used to cancel an assignment if the block changes
	 * between the assignment being assigned and the gnome reaching its target.
	 * @param loc
	 * @param oldblock
	 */
	public GnomeAssignment(BlockLocator loc, Block oldblock, EnumAssignType type)
	{
		this.loc = loc;
		this.oldblock = oldblock;
		this.type = type;
	}
}
