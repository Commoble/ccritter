package commoble.ccritter.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderPhantom extends RenderLiving
{
	private static final ResourceLocation texture = new ResourceLocation("ccritter:textures/tileentities/neverportal.png");  //refers to:assets/yourmod/textures/entity/yourtexture.png
	private ModelBase model;
	
	public RenderPhantom(ModelBase model, float p_i1262_2_)
	{
		super(model, p_i1262_2_);
		this.model = model;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
	{
		// TODO Auto-generated method stub
		return texture;
	}

	
    public void doRender(EntityLiving ent, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
    	if (ent.getBrightness(1.0F) > 0.2F)
    	{
        	GL11.glEnable(GL11.GL_BLEND);
        	GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
        	super.doRender(ent, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        	GL11.glDisable(GL11.GL_BLEND);
    	}
    }
}
