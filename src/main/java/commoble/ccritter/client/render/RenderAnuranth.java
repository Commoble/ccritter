package commoble.ccritter.client.render;

import commoble.ccritter.com.entity.monster.EntityAnuranth;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


// Referenced classes of package net.minecraft.src:
//            RenderLiving, JWorld_EntityExample, ModelBase, EntityLiving, 
//            Entity


@SideOnly(Side.CLIENT)
public class RenderAnuranth extends RenderLiving
{
	private static final ResourceLocation texture = new ResourceLocation("ccritter:textures/entities/anuranth.png");  //refers to:assets/yourmod/textures/entity/yourtexture.png

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return texture;
    }
        
    public RenderAnuranth(ModelBase modelbase, float f)
    {
        super(modelbase, f);
    }

    public void renderAnuranth(EntityAnuranth entityAnuranth, double d, double d1, double d2, 
            float f, float f1)
    {
        super.doRender(entityAnuranth, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderAnuranth((EntityAnuranth)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderAnuranth((EntityAnuranth)entity, d, d1, d2, f, f1);
    }
}