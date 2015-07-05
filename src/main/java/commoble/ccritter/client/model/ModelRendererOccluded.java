package commoble.ccritter.client.model;

import commoble.ccritter.com.block.tileentity.TileEntityWithCubicModel;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.world.World;

public class ModelRendererOccluded extends ModelRenderer
{
	public int pseudoTexOffX;
	public int pseudoTexOffY;
	public int sides;

	public ModelRendererOccluded(ModelBase model, int x, int y, int sides)
	{
		super(model, x, y);
		this.pseudoTexOffX = x;
		this.pseudoTexOffY = y;
		this.sides = sides;
	}

    public ModelRenderer addBox(float p_78789_1_, float p_78789_2_, float p_78789_3_, int p_78789_4_, int p_78789_5_, int p_78789_6_)
    {
        this.cubeList.add(new ModelBoxOccluded(this, this.pseudoTexOffX, this.pseudoTexOffY, p_78789_1_, p_78789_2_, p_78789_3_, p_78789_4_, p_78789_5_, p_78789_6_, 0.0F));
        return this;
    }
}
