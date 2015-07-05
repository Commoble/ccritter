package commoble.ccritter.com;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;

@Mod(modid = CCPMod.MODID, name = CCPMod.NAME, version = CCPMod.VERSION)
public class CCPMod
{
	
	@Instance("ccritter") //The instance, this is very important later on
	public static CCPMod instance = new CCPMod();

    public static final String MODID = "ccritter";
    public static final String VERSION = "1.7.10-0.4.0";
    public static final String NAME = "CCritter";
    
    @SidedProxy(clientSide="commoble.ccritter.client.CombinedClientProxy", serverSide="commoble.ccritter.server.DedicatedServerProxy")
    public static CommonProxy proxy;
    
    /**
    * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
    */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.preInit(event);
    }
     
    /**
    * Do your mod setup. Build whatever data structures you care about. Register recipes,
    * send FMLInterModComms messages to other mods.
    */
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	proxy.load();
    }
     
    /**
    * Handle interaction with other mods, complete your setup based on this.
    */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit();
    }
    
    /*@EventHandler
    public void potentialSpawns(WorldEvent.PotentialSpawns event)
    {
    	if (event.world.provider.dimensionId == CommonProxy.neverwhereDimID)
    	{
    		List<SpawnListEntry> exclusions = new ArrayList<SpawnListEntry>();
    		for (SpawnListEntry spawn : event.list)
	    	{
	    		if (!EntityMob.class.isAssignableFrom(spawn.entityClass))
	    		{
	    			exclusions.add(spawn);
	    		}
	    	}

    		for (SpawnListEntry spawn : exclusions)
    		{
    			event.list.remove(spawn);
    		}
    	}
    }*/
    
    @EventHandler
    public void checkSpawn(LivingSpawnEvent.CheckSpawn event)
    {
    	if (event.world.provider.dimensionId == CommonProxy.neverwhereDimID && !(event.entityLiving instanceof EntityMob))
    	{
    		event.setResult(Result.DENY);
    	}
    }
}
