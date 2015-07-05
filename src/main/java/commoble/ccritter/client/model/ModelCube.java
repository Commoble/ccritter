package commoble.ccritter.client.model;

import commoble.ccritter.com.block.tileentity.TileEntityWithCubicModel;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ModelCube extends ModelBase
{
	public ModelRendererOccluded cube;

	public ModelCube(int sides)
	{
		textureWidth = 64;
		textureHeight = 32;
		
		cube = new ModelRendererOccluded(this, 0, 0, sides);
		cube.addBox(0F, 0F, 0F, 16, 16, 16);
		cube.setRotationPoint(-8F, 8F, -8F);
		cube.setTextureSize(64, 32);
		cube.mirror = true;
		setRotation(cube, 0F, 0F, 0F);
	}

	public void render(float scale)
	{
		cube.render(scale);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}