package commoble.ccritter.event;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;
import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.entity.monster.EntityPhantom;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler
{
    /*@SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=false)
    public void joinWorld(EntityJoinWorldEvent event)
    {
    	if (event.world.provider.dimensionId == CommonProxy.neverwhereDimID && (event.entity instanceof EntityCreature) && !(event.entity instanceof EntityMob))
    	{
    		event.setCanceled(true);
    	}
    }*/
    
    @SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
    public void potentialSpawns(PotentialSpawns event)
    {
    	if (event.world.provider.dimensionId == CommonProxy.neverwhereDimID)
    	{
    		event.list.clear();
    		event.list.add(new SpawnListEntry(EntityPhantom.class, 100, 3, 5));
    	}
    }
}
