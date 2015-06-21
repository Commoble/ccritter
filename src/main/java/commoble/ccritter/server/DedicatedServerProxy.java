package commoble.ccritter.server;

import commoble.ccritter.com.CommonProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
* DedicatedServerProxy is used to set up the mod and start it running when installed on a dedicated server.
* It should not contain (or refer to) any client-side code at all, since the dedicated server has no client-side code.
*/
 
public class DedicatedServerProxy extends CommonProxy
{
	
	
	/**
	* Run before anything else. Read your config.
	*/
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
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
