package commoble.ccritter.client.render.item;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import commoble.ccritter.client.model.ModelChthon;
import commoble.ccritter.com.block.tileentity.TileEntityChestDeep;
import commoble.ccritter.com.block.tileentity.TileEntityChthonicStatue;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

public class RenderItemChthonicStatue implements IItemRenderer
{
    private TileEntityChthonicStatue tile = new TileEntityChthonicStatue();
	private ModelChthon model;
	private ResourceLocation texture = new ResourceLocation("critter:textures/tileentities/chthon.png");
	
	public RenderItemChthonicStatue()
	{
		this.model = new ModelChthon();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
		case ENTITY:
			renderModel(-0.5F, 0F, -0.5F);
			break;
		case EQUIPPED:
			renderModel(-1.0F, 0F, 0F);
			break;
		case EQUIPPED_FIRST_PERSON:
			renderModel(-1.0F, -0.5F, 0F);
			break;
		case INVENTORY:
			renderModel(-0.5F, -0.75F, -0.5F);
			break;
		default:
			break;
		}
	}

	private void renderModel(float xoff, float yoff, float zoff)
	{
		Tessellator tessellator = Tessellator.instance;

		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(xoff, yoff, zoff);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tile, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

}
