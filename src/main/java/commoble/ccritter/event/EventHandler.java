package commoble.ccritter.event;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import commoble.ccritter.com.CommonProxy;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler
{
    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=false)
    public void joinWorld(EntityJoinWorldEvent event)
    {
    	if (event.world.provider.dimensionId == CommonProxy.neverwhereDimID && (event.entity instanceof EntityCreature) && !(event.entity instanceof EntityMob))
    	{
    		event.setCanceled(true);
    	}
    }
}
