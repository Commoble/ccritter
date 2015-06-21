package commoble.ccritter.com.item;

import java.util.List;

import commoble.ccritter.com.CCPMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;


/**
 * This is just an egg spawner for the CCritter entities
 * the code here comes from Jabelar's website
 * http://jabelarminecraft.blogspot.com/p/minecraft-forge-1721710-creating-custom.html
 *
 */
public class ItemCCMonsterPlacer extends ItemMonsterPlacer
{
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;
	protected int colorBase = 0x000000;
	protected int colorSpots = 0xFFFFFF;
	protected String entityToSpawnName = "";
	protected String entityToSpawnNameFull = "";
	protected EntityLiving entityToSpawn = null;
	
	public ItemCCMonsterPlacer()
	{
		super();
	}
	
	public ItemCCMonsterPlacer(String entname, int colorbase, int colorspots)
	{
		setHasSubtypes(false);
		maxStackSize = 64;
		setCreativeTab(CreativeTabs.tabMisc);
		setEntityToSpawnName(entname);
		this.colorBase = colorbase;
		this.colorSpots = colorspots;
	}

	/* Can't override the static method spawnCreature, so have to replace onItemUse and onItemRightclick
	*	with mostly similar functions that use this class's spawnEntity method instead
	*/
	
	
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack istack, EntityPlayer player, World world, int x, int y, int z, int side, float f1, float f2, float f3)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d0 = 0.0D;

            if (side == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            Entity entity = spawnEntity(world, (double)x + 0.5D, (double)y + d0, (double)z + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && istack.hasDisplayName())
                {
                    ((EntityLiving)entity).setCustomNameTag(istack.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode)
                {
                    --istack.stackSize;
                }
            }

            return true;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack istack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return istack;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition == null)
            {
                return istack;
            }
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!world.canMineBlock(player, i, j, k))
                    {
                        return istack;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, istack))
                    {
                        return istack;
                    }

                    if (world.getBlock(i, j, k) instanceof BlockLiquid)
                    {
                        Entity entity = spawnEntity(world, (double)i, (double)j, (double)k);

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && istack.hasDisplayName())
                            {
                                ((EntityLiving)entity).setCustomNameTag(istack.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode)
                            {
                                --istack.stackSize;
                            }
                        }
                    }
                }

                return istack;
            }
        }
    }
	
    /**
     * Spawns the creature specified by the egg's type in the location specified by 
     * the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public Entity spawnEntity(World parWorld, double parX, double parY, double parZ)
    {
     
       if (!parWorld.isRemote) // never spawn entity on client side
       {
            entityToSpawnNameFull = CCPMod.MODID+"."+entityToSpawnName;
            if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull))
            {
                entityToSpawn = (EntityLiving) EntityList
                      .createEntityByName(entityToSpawnNameFull, parWorld);
                entityToSpawn.setLocationAndAngles(parX, parY, parZ, 
                      MathHelper.wrapAngleTo180_float(parWorld.rand.nextFloat()
                      * 360.0F), 0.0F);
                parWorld.spawnEntityInWorld(entityToSpawn);
                entityToSpawn.onSpawnWithEgg((IEntityLivingData)null);
                entityToSpawn.playLivingSound();
            }
            else
            {
                //DEBUG
                System.out.println("Entity not found "+entityToSpawnName);
            }
        }
      
        return entityToSpawn;
    }
    
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item parItem, CreativeTabs parTab, List parList)
    {
        parList.add(new ItemStack(parItem, 1, 0));     
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int parColorType)
    {
        return (parColorType == 0) ? colorBase : colorSpots;
    }
    
    /*@Override
    // Doing this override means that there is no localization for language
    // unless you specifically check for localization here and convert
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        return "Spawn "+entityToSpawnName;
    }*/


    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        theIcon = par1IconRegister.registerIcon(getIconString() + "_overlay");
    }
    
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int parDamageVal, int parRenderPass)
    {
        return parRenderPass > 0 ? theIcon : super.getIconFromDamageForRenderPass(parDamageVal, 
              parRenderPass);
    }

    
    public void setColors(int parColorBase, int parColorSpots)
    {
     colorBase = parColorBase;
     colorSpots = parColorSpots;
    }
    
    public int getColorBase()
    {
     return colorBase;
    }
    
    public int getColorSpots()
    {
     return colorSpots;
    }
    
    public void setEntityToSpawnName(String parEntityToSpawnName)
    {
        entityToSpawnName = parEntityToSpawnName;
        entityToSpawnNameFull = CCPMod.MODID+"."+entityToSpawnName; 
    }
}
