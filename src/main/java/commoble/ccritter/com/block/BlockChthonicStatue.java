package commoble.ccritter.com.block;

import commoble.ccritter.client.CombinedClientProxy;
import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.block.tileentity.TileEntityChestDeep;
import commoble.ccritter.com.block.tileentity.TileEntityChthonicStatue;
import commoble.ccritter.com.world.TeleporterNeverwhere;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChthonicStatue extends BlockContainer
{
	public final static float xBound = (1.0F/16.0F);	// amount of empty space beyond the x-dimensions
	public final static float yBound = (24.0F/16.0F);	// amount of empty space above the tallest point of the block
	public final static float zBound = (4.0F/16.0F);	// amount of empty space beyond the z-dimensions

	public BlockChthonicStatue()
	{
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		setHardness(2.5F);
		setResistance(10F);
		setStepSound(soundTypePiston);
		setBlockName("chthonicStatue");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ent, int bar, float foo1, float foo2, float foo3)
	{
		if (world.isRemote)
	    {
	        return true;
	    }
	    else
	    {
			int dim = CommonProxy.neverwhereDimID;
			if ((ent.ridingEntity == null) && (ent.riddenByEntity == null) && (ent instanceof EntityPlayerMP))
			{
				EntityPlayerMP player = (EntityPlayerMP) ent;
				MinecraftServer mserv = MinecraftServer.getServer();
				if (player.dimension != CommonProxy.neverwhereDimID)	// going to Neverwhere
				{
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dim,
							new TeleporterNeverwhere(mserv.worldServerForDimension(dim), x, y, z));
				}
				else	// going to Surface
				{
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0,
							new TeleporterNeverwhere(mserv.worldServerForDimension(0), x, y, z));
				}
				
				return true;
			}
			else
			{
				return false;
			}
	    }
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    /*@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        return super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, 1 - p_149646_5_);
    }*/
	
	@Override
    @SideOnly(Side.CLIENT)
	public int getRenderType()
	{
		return -1;
	}

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.nightVision) == null) ? 0 : 1;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
	public boolean canRenderInPass(int pass)
	{
		//Set the static var in the client proxy
		 return ((Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.nightVision) == null) == (pass == 0));
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityChthonicStatue();
	}
	
	/**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 4 || meta == 5)
        {
        	this.setBlockBounds(zBound, 0.0F, xBound, 1-zBound, yBound, 1-xBound);	// TODO replace with more precise bounds
        }
        else
        {
        	this.setBlockBounds(xBound, 0.0F, zBound, 1-xBound, yBound, 1-zBound);	// TODO replace with more precise bounds
        }
    }
	
	/**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entplacer, ItemStack itemstack)
    {
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        byte b0 = 0;
        int facedir = MathHelper.floor_double((double)(entplacer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (facedir == 0)
        {
            b0 = 2;
        }

        if (facedir == 1)
        {
            b0 = 5;
        }

        if (facedir == 2)
        {
            b0 = 3;
        }

        if (facedir == 3)
        {
            b0 = 4;
        }

        world.setBlockMetadataWithNotify(x, y, z, b0, 3);
    }
}
