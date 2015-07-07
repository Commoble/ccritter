package commoble.ccritter.client;

import javafx.scene.shape.Sphere;
import net.minecraftforge.client.MinecraftForgeClient;
import commoble.ccritter.client.model.ModelAnuranth;
import commoble.ccritter.client.model.ModelBipedSimple;
import commoble.ccritter.client.model.ModelGnome;
import commoble.ccritter.client.render.RenderAnuranth;
import commoble.ccritter.client.render.RenderGnomeDeep;
import commoble.ccritter.client.render.RenderGnomeWood;
import commoble.ccritter.client.render.RenderPhantom;
import commoble.ccritter.client.render.item.RenderItemChestDeep;
import commoble.ccritter.client.render.tileentity.RenderTileEntityChestDeep;
import commoble.ccritter.client.render.tileentity.RenderTileEntityChthonicStatue;
import commoble.ccritter.client.render.tileentity.RenderTileEntityNeverPortal;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.block.tileentity.TileEntityChestDeep;
import commoble.ccritter.com.block.tileentity.TileEntityChthonicStatue;
import commoble.ccritter.com.block.tileentity.TileEntityNeverPortal;
import commoble.ccritter.com.entity.gnome.EntityGnomeDeep;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.entity.monster.EntityAnuranth;
import commoble.ccritter.com.entity.monster.EntityPhantom;

/**
* CombinedClient is used to set up the mod and start it running when installed on a normal minecraft client.
* It should not contain any code necessary for proper operation on a DedicatedServer. Code required for both
* normal minecraft client and dedicated server should go into CommonProxy
*/
public class CombinedClientProxy extends CommonProxy
{
	public static int renderPass;
	
	/**
	* Run before anything else. Read your config, register renderers
	*/
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		// entity renderers
		RenderingRegistry.registerEntityRenderingHandler(EntityAnuranth.class, new RenderAnuranth(new ModelAnuranth(), 0.4F));
        RenderingRegistry.registerEntityRenderingHandler(EntityGnomeWood.class, new RenderGnomeWood(new ModelGnome(), 0.4F));
        RenderingRegistry.registerEntityRenderingHandler(EntityGnomeDeep.class, new RenderGnomeDeep(new ModelGnome(), 0.4F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPhantom.class, new RenderPhantom(new ModelBipedSimple(), 0.4F));

        
        // renderers for special tiles
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChestDeep.class, new RenderTileEntityChestDeep());
        MinecraftForgeClient.registerItemRenderer(CommonProxy.deepChest.getItem(null, 0, 0, 0), new RenderItemChestDeep());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChthonicStatue.class, new RenderTileEntityChthonicStatue());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNeverPortal.class, new RenderTileEntityNeverPortal());
        	////MinecraftForgeClient.registerItemRenderer(CommonProxy.chthonicStatue.getItem(null, 0, 0, 0), new RenderItemChestDeep());
        //this.renderChthonic = RenderingRegistry.getNextAvailableRenderId();
        //RenderingRegistry.registerBlockHandler(new RenderChthonicBlock());
	}
	 
	/**
	* Do your mod setup. Build whatever data structures you care about.
	* send FMLInterModComms messages to other mods.
	*/
	@Override
	public void load()
	{
		super.load();
	}
	 
	/**
	* Handle interaction with other mods, complete your setup based on this.
	*/
	@Override
	public void postInit()
	{
		super.postInit();
	}
}
