package commoble.ccritter.client.render.tileentity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import commoble.ccritter.client.model.ModelChthon;
import commoble.ccritter.com.block.tileentity.TileEntityChthonicStatue;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityChthonicStatue extends TileEntitySpecialRenderer
{
	private ModelChthon model = new ModelChthon();
    private static final ResourceLocation textureNorm = new ResourceLocation("ccritter:textures/tileentities/chthon.png");
    private static final ResourceLocation textureSpecial = new ResourceLocation("ccritter:textures/tileentities/chthonSpecial.png");
    

	@Override
	public void renderTileEntityAt(TileEntity te, double x,	double y, double z, float scale)
	{
		this.renderTileEntityAt((TileEntityChthonicStatue)te, x, y, z, scale);
	}

	private void renderTileEntityAt(TileEntityChthonicStatue te, double x, double y, double z, float scale)
	{
		this.bindTexture(this.chooseTexture());
		
		int meta;

        if (!te.hasWorldObj())
        {
            meta = 0;
        }
        else
        {
            meta = te.getBlockMetadata();
        }
        
        short rotate = 0;

        if (meta == 2)
        {
            rotate = 180;
        }

        if (meta == 3)
        {
            rotate = 0;
        }

        if (meta == 4)
        {
            rotate = 90;
        }

        if (meta == 5)
        {
            rotate = -90;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        //GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef((float)rotate, 0.0F, 1.0F, 0.0F);
        //GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        model.render(0.0625F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@SideOnly(Side.CLIENT)
	private ResourceLocation chooseTexture()
	{
		 if (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.nightVision) != null)
		 {
			 GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
			 return textureSpecial;
		 }
		 else
		 {
			 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			 return textureNorm;
		 }
	}

}
