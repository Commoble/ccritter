package commoble.ccritter.com.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.ChunkProviderGenerate;

public class ChunkProviderNeverwhere extends ChunkProviderGenerate
{

	public ChunkProviderNeverwhere(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_)
	{
		super(p_i2006_1_, p_i2006_2_, p_i2006_4_);
	}

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z)
    {
    	List list = super.getPossibleCreatures(type, x, y, z);
    	ArrayList newList = new ArrayList();
    	for (Object obj : list)
    	{
    		if (obj instanceof SpawnListEntry && EntityMob.class.isAssignableFrom(((SpawnListEntry)obj).entityClass))
    		{
    			newList.add((SpawnListEntry)obj);
    		}
    	}
    	
    	return newList;
    }
}
