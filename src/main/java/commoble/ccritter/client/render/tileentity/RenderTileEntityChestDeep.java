package commoble.ccritter.client.render.tileentity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import commoble.ccritter.com.block.BlockChestDeep;
import commoble.ccritter.com.block.tileentity.TileEntityChestDeep;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTileEntityChestDeep extends TileEntitySpecialRenderer
{
    private static final ResourceLocation texturename = new ResourceLocation("ccritter:textures/tileentities/stonechest.png");
    private ModelChest modelchest = new ModelChest();
    //private ModelChest field_147511_i = new ModelLargeChest();
    private boolean specialbool;
    //private static final String __OBFID = "CL_00000965";

	public void renderTileEntityAt(TileEntityChestDeep te, double x, double y, double z, float f)
    {
        int i;

        if (!te.hasWorldObj())
        {
            i = 0;
        }
        else
        {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockChestDeep && i == 0)
            {	// not doing large chest, remove this part
                /*try
                {
                ((BlockChestDeep)block).func_149954_e(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
                }
                catch (ClassCastException e)
                {
                    FMLLog.severe("Attempted to render a chest at %d,  %d, %d that was not a chest", te.xCoord, te.yCoord, te.zCoord);
                }*/
                i = te.getBlockMetadata();
            }

            //te.checkForAdjacentChests();
        }

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null)
        {
            /*if (te.adjacentChestXPos == null && te.adjacentChestZPos == null)
            {
                modelchest = this.field_147510_h;

                if (te.func_145980_j() == 1)
                {
                    this.bindTexture(field_147506_e);
                }
                else if (this.field_147509_j)
                {
                    this.bindTexture(field_147503_f);
                }
                else
                {
                    this.bindTexture(texturename);
                }
            }
            else
            {
                modelchest = this.field_147511_i;

                if (te.func_145980_j() == 1)
                {
                    this.bindTexture(field_147507_b);
                }
                else if (this.field_147509_j)
                {
                    this.bindTexture(field_147508_c);
                }
                else
                {
                    this.bindTexture(field_147505_d);
                }
            }*/
            
            this.bindTexture(texturename);

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short short1 = 0;

            if (i == 2)
            {
                short1 = 180;
            }

            if (i == 3)
            {
                short1 = 0;
            }

            if (i == 4)
            {
                short1 = 90;
            }

            if (i == 5)
            {
                short1 = -90;
            }

            if (i == 2 && te.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && te.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * f;
            float f2;

            if (te.adjacentChestZNeg != null)
            {
                f2 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * f;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            if (te.adjacentChestXNeg != null)
            {
                f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * f;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
            modelchest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
	{
		this.renderTileEntityAt((TileEntityChestDeep)te, x, y, z, f);
	}

}
