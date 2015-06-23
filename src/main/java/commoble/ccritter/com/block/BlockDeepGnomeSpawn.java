package commoble.ccritter.com.block;

import java.util.List;
import java.util.Random;

import commoble.ccritter.com.CCPMod;
import commoble.ccritter.com.item.ItemCCMonsterPlacer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class BlockDeepGnomeSpawn extends Block
{
	public BlockDeepGnomeSpawn()
	{
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockTextureName("stone");
        this.setBlockName("deepGnomeSpawnBlock");
        this.setTickRandomly(true);
	}

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(Blocks.cobblestone);
    }

    /**
     * Display particles if the player has night vision
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        super.randomDisplayTick(world, x, y, z, rand);

        if (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.nightVision) != null)
        {	// all six sides
        	world.spawnParticle("magicCrit", (double)((float)x + rand.nextFloat()), (double)((float)y + 1.1F), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        	world.spawnParticle("magicCrit", (double)((float)x + rand.nextFloat()), (double)((float)y - 0.1F), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        	world.spawnParticle("magicCrit", (double)((float)x-0.1F), (double)((float)y + rand.nextFloat()), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        	world.spawnParticle("magicCrit", (double)((float)x+1.1F), (double)((float)y + rand.nextFloat()), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        	world.spawnParticle("magicCrit", (double)((float)x + rand.nextFloat()), (double)((float)y + rand.nextFloat()), (double)((float)z - 0.1F), 0.0D, 0.0D, 0.0D);
        	world.spawnParticle("magicCrit", (double)((float)x + rand.nextFloat()), (double)((float)y + rand.nextFloat()), (double)((float)z + 1.1F), 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
		//System.out.println(x + ", " + y + ", " + z);
    	// this checks a single random side for airness
    	int side = rand.nextInt(6);
    	int xoff = (side % 3 == 0) ? (side / 2 == 0 ? 1 : -1) : 0;
    	int yoff = (side % 3 == 1) ? (side / 2 == 0 ? 1 : -1) : 0;
    	int zoff = (side % 3 == 2) ? (side / 2 == 0 ? 1 : -1) : 0;
    	
    	if (world.getBlock(x + xoff, y + yoff, z + zoff) == Blocks.air)
    	{
    		// delete this block and replace it with a gnome
    		world.setBlock(x, y, z, Blocks.air);
    		((ItemCCMonsterPlacer) CCPMod.proxy.eggGnomeDeep).spawnEntity(world, x+0.5D, y, z+0.5D);
    	}
    }
}
