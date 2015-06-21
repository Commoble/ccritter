package commoble.ccritter.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import commoble.ccritter.client.model.ModelGnome;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


// Referenced classes of package net.minecraft.src:
//            RenderLiving, JWorld_EntityExample, ModelBase, EntityLiving, 
//            Entity


@SideOnly(Side.CLIENT)
public class RenderGnomeWood extends RenderLiving
{
	private static final ResourceLocation texture = new ResourceLocation("ccritter:textures/entities/gnomewood.png");  //refers to:assets/yourmod/textures/entity/yourtexture.png
    private ModelGnome model;
	
    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return texture;
    }
        
    public RenderGnomeWood(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        this.model = (ModelGnome)super.mainModel;
        this.setRenderPassModel(this.model);
    }

    public void renderGnomeWood(EntityGnomeWood ent, double d, double d1, double d2, 
            float f, float f1)
    {
    	this.model.isCarrying = (ent.getCarried() != Blocks.air);
        super.doRender(ent, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderGnomeWood((EntityGnomeWood)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderGnomeWood((EntityGnomeWood)entity, d, d1, d2, f, f1);
    }
    
    protected void renderEquippedItems(EntityLivingBase ent, float par2)
    {
        //this.renderCarrying((EntityEnderman)par1EntityLivingBase, par2);
    	super.renderEquippedItems(ent, par2);
    	EntityGnomeWood gnome = (EntityGnomeWood)ent;

        if (gnome.getCarried() != Blocks.air)
        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushMatrix();
            float f1 = 0.5F;
            GL11.glTranslatef(0.0F, 1.0F, -0.5F);//(0.0F, 0.6875F, -0.75F);	// 0, 11/16, 3/4
            f1 *= 1.0F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            //GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            int i = ent.getBrightnessForRender(par2);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindTexture(TextureMap.locationBlocksTexture);
            	// 147907 = renderblocks
            this.field_147909_c.renderBlockAsItem(gnome.getCarried(), 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
    }
}