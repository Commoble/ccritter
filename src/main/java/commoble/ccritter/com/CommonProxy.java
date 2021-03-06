package commoble.ccritter.com;

import commoble.ccritter.com.block.BlockChestGnome;
import commoble.ccritter.com.block.BlockDeepGnode;
import commoble.ccritter.com.block.BlockGnomeCache;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import commoble.ccritter.com.entity.gnome.EntityGnomeDeep;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.entity.monster.EntityAnuranth;
import commoble.ccritter.com.item.ItemCCMonsterPlacer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBeach;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.biome.BiomeGenRiver;
import net.minecraft.world.biome.BiomeGenSwamp;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy
{

	//public static int entityid_next = 0;
	//public static final int entityid_Anuranth = 0;
	//public static final int entityid_GnomeWood = 1;
	
	public static boolean spawn_anuranths = true;
	public static boolean anuranths_dig = true;
	public static boolean anuranths_hunt = true;
	public static boolean anuranths_breed = true;
	
	public static boolean spawn_wood_gnomes = true;
	public static boolean spawn_deep_gnomes = true;
	
	//blocks
	public static Block gnomecache;
	public static Block gnomeproof_chest;
	public static Block deepgnode;
	
	//items
	public static Item eggGnomeWood;
	public static Item eggAnuranth;
	
	private static int modEntityID = 0;
	
	/*public static int gnomecache_id;
	public static int gnomeproof_chest_id;
	public static int deepgnode_id;*/
	
	/**
	* Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	*/
	public void preInit(FMLPreInitializationEvent event)
	{
		// register sounds
		MinecraftForge.EVENT_BUS.register(new SoundManager());
		
		// read config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		Property conf;
		config.load();
		
		// block options
		/*int next_id = 256;
		
		next_id = this.getNextFreeBlockId(next_id);
		conf = config.get("Block IDs", "gnomecache", next_id);
		CommonProxy.gnomecache_id = conf.getInt(next_id);
		next_id++;
		
		next_id = this.getNextFreeBlockId(next_id);
		conf = config.get("Block IDs", "gnomeproof_chest", next_id);
		CommonProxy.gnomeproof_chest_id = conf.getInt(next_id);
		next_id++;
		
		next_id = this.getNextFreeBlockId(next_id);
		conf = config.get("Block IDs", "deepgnode", next_id);
		CommonProxy.deepgnode_id = conf.getInt(next_id);
		next_id++;*/
		
		// entity options
		conf = config.get("Creature: Anuranths", "spawn_anuranths", true);
		conf.comment = "Set this to false to prevent anuranths from spawning\n" +
			"Warning! Changing this may cause BAD THINGS to happen to any current worlds";
		CommonProxy.spawn_anuranths = conf.getBoolean(true);
		conf = config.get("Creature: Anuranths", "anuranths_dig", true);
		conf.comment = "Set this to false to prevent anuranths from digging holes in underwater dirt";
		CommonProxy.anuranths_dig = conf.getBoolean(true);
		conf = config.get("Creature: Anuranths", "anuranths_hunt", true);
		conf.comment = "Set this to false to prevent anuranths from hunting animals";
		CommonProxy.anuranths_hunt = conf.getBoolean(true);
		conf = config.get("Creature: Anuranths", "anuranths_breed", true);
		conf.comment = "Set this to false to prevent anuranths from making new anuranths";
		CommonProxy.anuranths_breed = conf.getBoolean(true);
		
		conf = config.get("Creature: Gnomes", "spawn_wood_gnomes", true);
		conf.comment = "Set this to false to prevent wood gnomes from spawning (existing worlds -> bad things, etc.)";
		CommonProxy.spawn_wood_gnomes = conf.getBoolean(true);
		
		conf = config.get("Creature: Gnomes", "spawn_deep_gnomes", true);
		conf.comment = "Set this to false to prevent deep gnomes from spawning (existing worlds -> bad things, etc.)";
		CommonProxy.spawn_deep_gnomes = conf.getBoolean(true);
		
		config.save();
		
		// register my Items, Blocks, Entities, etc
		
		this.registerBlocks();
		this.registerTileEntities();
		this.registerEntities();
	}
	 
	/**
	* Do your mod setup. Build whatever data structures you care about. Register recipes,
	* send FMLInterModComms messages to other mods.
	*/
	public void load()
	{
		// register my Recipies
	}
	 
	/**
	* Handle interaction with other mods, complete your setup based on this.
	*/
	public void postInit()
	{
		
	}
	
	
	private void registerBlocks()
	{
		CommonProxy.gnomecache = new BlockGnomeCache();
		GameRegistry.registerBlock(CommonProxy.gnomecache, "gnomecache");
		//LanguageRegistry.addName(gnomecache, "Wood Gnome Hovel");
		
		CommonProxy.gnomeproof_chest = new BlockChestGnome();
		GameRegistry.registerBlock(CommonProxy.gnomeproof_chest, "gnomeproof_chest");
		//LanguageRegistry.addName(gnomeproof_chest, "Wood Gnome Chest");
		
		/*CommonProxy.gnomeproof_chest = new BlockDeepGnode(CommonProxy.gnomeproof_chest_id);
		GameRegistry.registerBlock(CommonProxy.deepgnode, "deepgnode");
		LanguageRegistry.addName(deepgnode, "Deep Gnome Marker");*/
	}
	
	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityGnomeCache.class, "te_gnomecache");
	}
	
	private void registerEntities()
	{
		int entity_id = 0;
		
		while(EntityList.getStringFromID(entity_id) !=null)
		{
			entity_id++;
		}

		//registerModEntityWithEgg(EntityGnomeWood.class, "anuranth", 0x074316, 0xbdcbd8);
		EntityRegistry.registerModEntity(EntityAnuranth.class, "anuranth", modEntityID++, CCPMod.instance, 80, 3, true);
		CommonProxy.eggAnuranth = new ItemCCMonsterPlacer("anuranth", 0x074316, 0xbdcbd8)
			.setUnlocalizedName("spawn_egg_anuranth")
			.setTextureName("ccritter:spawn_egg");
		GameRegistry.registerItem(eggAnuranth, "eggAnuranth");
		//EntityRegistry.registerGlobalEntityID(EntityAnuranth.class, "anuranth", entity_id, 0x074316, 0xbdcbd8);
        //LanguageRegistry.instance().addStringLocalization("entity.anuranth.name", "Anuranth");
		
		if (CommonProxy.spawn_anuranths)
		{
			for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
	        {	// spawn anuranths in any swamp or biome that inherits from swamps
	        	if
	        	(
	        		BiomeGenBase.getBiomeGenArray()[i] != null &&
	        		(
	        			(
	        				BiomeGenSwamp.class.isAssignableFrom(BiomeGenBase.getBiomeGenArray()[i].getClass())
	        				||
	        				BiomeGenRiver.class.isAssignableFrom(BiomeGenBase.getBiomeGenArray()[i].getClass())
	        				||
	        				BiomeGenBeach.class.isAssignableFrom(BiomeGenBase.getBiomeGenArray()[i].getClass())
	        			)
	        		)
	        	)	
	        	{
	        		EntityRegistry.addSpawn(EntityAnuranth.class, 100, 3, 5, EnumCreatureType.creature, BiomeGenBase.getBiomeGenArray()[i]);
	        	}
	        }
		}

		while(EntityList.getStringFromID(entity_id) !=null)
		{
			entity_id++;
		}
		
		//registerModEntityWithEgg(EntityGnomeWood.class, "gnome_wood", 0xd3753f, 0x774725);
		//EntityRegistry.registerGlobalEntityID(EntityGnomeWood.class, "gnome_wood", entity_id, 0xd3753f, 0x774725);
        //LanguageRegistry.instance().addStringLocalization("entity.gnome_wood.name", "Wood Gnome");
		EntityRegistry.registerModEntity(EntityGnomeWood.class, "gnome_wood", modEntityID++, CCPMod.instance, 80, 3, false);
		CommonProxy.eggGnomeWood = new ItemCCMonsterPlacer("gnome_wood", 0xd3753f, 0x774725)
			.setUnlocalizedName("spawn_egg_wood_gnome")
			.setTextureName("ccritter:spawn_egg");
		GameRegistry.registerItem(eggGnomeWood, "eggGnomeWood");
		
		if (CommonProxy.spawn_wood_gnomes)
		{
			for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
	        {	// spawn gnomes in forests
	        	if (BiomeGenBase.getBiomeGenArray()[i] != null && BiomeGenForest.class.isAssignableFrom(BiomeGenBase.getBiomeGenArray()[i].getClass()))
	        	{
	        		EntityRegistry.addSpawn(EntityGnomeWood.class, 2, 1, 3, EnumCreatureType.creature, BiomeGenBase.getBiomeGenArray()[i]);
	        	}
	        }
		}
		
		/*if (CommonProxy.spawn_deep_gnomes)
		{
			for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
	        {	// spawn gnomes in forests
	        	EntityRegistry.addSpawn(EntityGnomeDeep.class, 1, 1, 1, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray()[i]);
	        }
		}*/
	}
	
	/*private int getNextFreeBlockId(int id)
	{
		while (Block.blocksList[id] != null)
		{
			id++;
		}
		return id;
	}*/
	
	/*private void registerModEntityWithEgg(Class entclass, String name, int eggcolor, int eggspotcolor)
	{
		EntityRegistry.registerModEntity(entclass, name, modEntityID++, CCPMod.instance, 80, 3, false);
		registerSpawnEgg(name, eggcolor, eggspotcolor);
		
		
	}*/
}