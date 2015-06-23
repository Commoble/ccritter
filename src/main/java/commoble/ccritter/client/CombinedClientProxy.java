package commoble.ccritter.client;

import commoble.ccritter.client.model.ModelAnuranth;
import commoble.ccritter.client.model.ModelGnome;
import commoble.ccritter.client.render.RenderAnuranth;
import commoble.ccritter.client.render.RenderGnomeDeep;
import commoble.ccritter.client.render.RenderGnomeWood;
import commoble.ccritter.client.render.tileentity.RenderTileEntityChestDeep;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.block.tileentity.TileEntityChestDeep;
import commoble.ccritter.com.entity.gnome.EntityGnomeDeep;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.entity.monster.EntityAnuranth;

/**
* CombinedClient is used to set up the mod and start it running when installed on a normal minecraft client.
* It should not contain any code necessary for proper operation on a DedicatedServer. Code required for both
* normal minecraft client and dedicated server should go into CommonProxy
*/
public class CombinedClientProxy extends CommonProxy
{
	/**
	* Run before anything else. Read your config, register renderers
	*/
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		// register my renderers
		RenderingRegistry.registerEntityRenderingHandler(EntityAnuranth.class, new RenderAnuranth(new ModelAnuranth(), 0.4F));
        RenderingRegistry.registerEntityRenderingHandler(EntityGnomeWood.class, new RenderGnomeWood(new ModelGnome(), 0.4F));
        RenderingRegistry.registerEntityRenderingHandler(EntityGnomeDeep.class, new RenderGnomeDeep(new ModelGnome(), 0.4F));

        // tile entity renderers
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChestDeep.class, new RenderTileEntityChestDeep());
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
