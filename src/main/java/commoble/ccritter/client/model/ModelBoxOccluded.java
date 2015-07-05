package commoble.ccritter.client.model;

import commoble.ccritter.com.block.tileentity.TileEntityWithCubicModel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This variant of ModelBox only renders sides 
 * @author Joseph
 *
 */
public class ModelBoxOccluded extends ModelBox
{
	public final static int[] listIndexer = {5, 4, 1, 0, 3, 2};
	public int sides;
	
	public ModelBoxOccluded(ModelRendererOccluded p_i1171_1_, int p_i1171_2_,
			int p_i1171_3_, float p_i1171_4_, float p_i1171_5_,
			float p_i1171_6_, int p_i1171_7_, int p_i1171_8_, int p_i1171_9_,
			float p_i1171_10_)
	{
		super(p_i1171_1_, p_i1171_2_, p_i1171_3_, p_i1171_4_, p_i1171_5_, p_i1171_6_,
				p_i1171_7_, p_i1171_8_, p_i1171_9_, p_i1171_10_);
		
		this.sides = p_i1171_1_.sides;

	}


	/**
     * Draw the six sided box defined by this ModelBox
     */
    @SideOnly(Side.CLIENT)
    public void render(Tessellator p_78245_1_, float p_78245_2_)
    {
        for (int i = 0; i < this.quadList.length; ++i)
        {	// sides iterated = 5, 4, 0, 1, 2, 3
        	int side = listIndexer[i];
        	if ((sides & (1 << side)) > 0)
            {
        		this.quadList[i].draw(p_78245_1_, p_78245_2_);
            }
        }
    }
}
