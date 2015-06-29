package commoble.ccritter.com.entity.ai.job;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import commoble.ccritter.com.entity.gnome.EntityGnome;
import commoble.ccritter.com.entity.gnome.EntityGnomeDeep;
import commoble.ccritter.com.util.IntLoc;

public class JobRetrieveDroppedItem extends Job
{
	private EntityItem item;
	private EntityGnomeDeep deepgnome;

	public JobRetrieveDroppedItem(EntityGnomeDeep ent, IntLoc loc, EntityItem item)
	{
		super(ent, loc);
		this.deepgnome = ent;
		this.item = item;
	}

	@Override
	public void finishJob(boolean near)
	{
		if (near)
		{
			if (item != null)
			{
				if (this.item.getEntityItem().getItem() instanceof ItemBlock)
				{
					ItemBlock iblock = (ItemBlock) this.item.getEntityItem().getItem();
					ItemStack stack = this.item.getEntityItem();
					if (this.deepgnome.getCarried() == Blocks.air)
					{
						this.deepgnome.setCarried(iblock.field_150939_a);
						stack.stackSize--;
						if (stack.stackSize <= 0)
						{
							item.setDead();
						}
						else
						{
							this.deepgnome.droppedItems.push(item);
						}
					}
				}
			}
		}
	}

}
