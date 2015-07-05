package commoble.ccritter.client.render.tileentity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import commoble.ccritter.client.model.ModelBoxOccluded;
import commoble.ccritter.client.model.ModelChthon;
import commoble.ccritter.client.model.ModelCube;
import commoble.ccritter.client.model.ModelRendererOccluded;
import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.block.tileentity.TileEntityChthonicStatue;
import commoble.ccritter.com.block.tileentity.TileEntityNeverPortal;
import commoble.ccritter.com.block.tileentity.TileEntityWithCubicModel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityNeverPortal extends TileEntitySpecialRenderer
{
	private ModelCube[] models;
    private static final ResourceLocation texture = new ResourceLocation("ccritter:textures/tileentities/neverPortal.png");
    
    public RenderTileEntityNeverPortal()
    {
    	models = new ModelCube[64];
    	for (int i=0; i<64; i++)
    	{
    		models[i] = new ModelCube(i);
    	}
    }
    

	@Override
	public void renderTileEntityAt(TileEntity te, double x,	double y, double z, float scale)
	{
		this.renderTileEntityAt((TileEntityNeverPortal)te, x, y, z, scale);
	}

	private void renderTileEntityAt(TileEntityNeverPortal te, double x, double y, double z, float scale)
	{		
		this.bindTexture(texture);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        //GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        //GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        models[te.getSidesIdentifier()].render(0.0625F);
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
