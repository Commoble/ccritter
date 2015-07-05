package commoble.ccritter.com;

import commoble.ccritter.com.block.BlockChestDeep;
import commoble.ccritter.com.block.BlockChestGnome;
import commoble.ccritter.com.block.BlockDeepGnode;
import commoble.ccritter.com.block.BlockDeepGnomeSpawn;
import commoble.ccritter.com.block.BlockGnomeCache;
import commoble.ccritter.com.block.BlockChthonicStatue;
import commoble.ccritter.com.block.BlockNeverPortal;
import commoble.ccritter.com.block.tileentity.TileEntityChestDeep;
import commoble.ccritter.com.block.tileentity.TileEntityChthonicStatue;
import commoble.ccritter.com.block.tileentity.TileEntityGnomeCache;
import commoble.ccritter.com.block.tileentity.TileEntityNeverPortal;
import commoble.ccritter.com.entity.gnome.EntityGnomeDeep;
import commoble.ccritter.com.entity.gnome.EntityGnomeWood;
import commoble.ccritter.com.entity.monster.EntityAnuranth;
import commoble.ccritter.com.item.DGSBItemBlock;
import commoble.ccritter.com.item.ItemCCMonsterPlacer;
import commoble.ccritter.com.world.WorldGenManager;
import commoble.ccritter.com.world.WorldProviderNeverwhere;
import commoble.ccritter.event.EventHandler;
import commoble.ccritter.network.PacketPortalRender;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBeach;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.biome.BiomeGenRiver;
import net.minecraft.world.biome.BiomeGenSwamp;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

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
	public static Block gnomeCache;
	public static Block gnomeProofChest;
	public static Block deepGnode;
	public static Block deepGnomeSpawnBlock;
	public static Block deepChest;
	public static Block chthonicStatue;
	public static Block neverPortal;
	
	//items
	public static Item eggGnomeWood;
	public static Item eggAnuranth;
	public static Item eggGnomeDeep;
	
	// other
	public static WorldGenManager worldgenmanager = new WorldGenManager();
	
	public static SimpleNetworkWrapper network;
	
	public static int neverwhereDimID = DimensionManager.getNextFreeDimId();
	
	
	private static int modEntityID = 0;
	
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
		
		// entity options
		conf = config.get("Creature: Anuranths", "spawn_anuranths", true);
		conf.comment = "Set this to false to prevent anuranths from spawning";
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
		conf.comment = "Set this to false to prevent wood gnomes from spawning";
		CommonProxy.spawn_wood_gnomes = conf.getBoolean(true);
		
		conf = config.get("Creature: Gnomes", "spawn_deep_gnomes", true);
		conf.comment = "Set this to false to prevent deep gnomes from spawning";
		CommonProxy.spawn_deep_gnomes = conf.getBoolean(true);
		
		config.save();
		
		// register my Items, Blocks, Entities, etc
		
		this.registerBlocks();
		this.registerTileEntities();
		this.registerEntities();
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(CCPMod.MODID);
		network.registerMessage(PacketPortalRender.Handler.class, PacketPortalRender.class, 0, Side.CLIENT);
		
		DimensionManager.registerProviderType(neverwhereDimID, WorldProviderNeverwhere.class, false);
		DimensionManager.registerDimension(neverwhereDimID, neverwhereDimID);
		GameRegistry.registerWorldGenerator(worldgenmanager, 0);
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	 
	/**
	* Do your mod setup. Build whatever data structures you care about. Register recipes,
	* send FMLInterModComms messages to other mods.
	*/
	public void load()
	{
		// register my Recipes
	}
	 
	/**
	* Handle interaction with other mods, complete your setup based on this.
	*/
	public void postInit()
	{
		
	}
	
	
	private void registerBlocks()
	{
		CommonProxy.gnomeCache = new BlockGnomeCache();
		GameRegistry.registerBlock(CommonProxy.gnomeCache, "gnomeCache");
		
		CommonProxy.gnomeProofChest = new BlockChestGnome();
		GameRegistry.registerBlock(CommonProxy.gnomeProofChest, "gnomeProofChest");
		
		CommonProxy.deepGnomeSpawnBlock = new BlockDeepGnomeSpawn();
		GameRegistry.registerBlock(CommonProxy.deepGnomeSpawnBlock, DGSBItemBlock.class, "deepGnomeSpawnBlock");
		
		CommonProxy.deepChest = new BlockChestDeep(0);
		GameRegistry.registerBlock(CommonProxy.deepChest, "deepChest");
		
		CommonProxy.chthonicStatue = new BlockChthonicStatue();
		GameRegistry.registerBlock(CommonProxy.chthonicStatue, "chthonicStatue");
		
		CommonProxy.neverPortal = new BlockNeverPortal();
		GameRegistry.registerBlock(CommonProxy.neverPortal, "neverPortal");
	}
	
	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityGnomeCache.class, "te_gnomecache");
		GameRegistry.registerTileEntity(TileEntityChestDeep.class, "te_deepchest");
		GameRegistry.registerTileEntity(TileEntityChthonicStatue.class, "te_cthonicstatue");
		GameRegistry.registerTileEntity(TileEntityNeverPortal.class, "te_neverportal");

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
	        {	// can waterspawn in any biome where temperature and rain are sufficient, except oceans
				BiomeGenBase biome = BiomeGenBase.getBiomeGenArray()[i];
	        	if (biome != null)
	        	{
	        		//EntityRegistry.addSpawn(EntityAnuranth.class, 10, 3, 5, EnumCreatureType.creature, BiomeGenBase.getBiomeGenArray()[i]);
	        		if (biome.rainfall >= 0.39F && biome.temperature >= 0.49F && !BiomeGenOcean.class.isAssignableFrom(biome.getClass()))
	        		{
		        		EntityRegistry.addSpawn(EntityAnuranth.class, 8, 3, 5, EnumCreatureType.waterCreature, biome);
	        		}	// can spawn on land in swamps
	        		if (BiomeGenSwamp.class.isAssignableFrom(biome.getClass()))
	        		{
		        		EntityRegistry.addSpawn(EntityAnuranth.class, 8, 3, 5, EnumCreatureType.creature, biome);
	        		}
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
		
		EntityRegistry.registerModEntity(EntityGnomeDeep.class,  "gnome_deep", modEntityID++, CCPMod.instance, 80, 3, false);
		CommonProxy.eggGnomeDeep = new ItemCCMonsterPlacer("gnome_deep", 0x969696, 0x7f7f7f)
			.setUnlocalizedName("spawn_egg_deep_gnome")
			.setTextureName("ccritter:spawn_egg");
		GameRegistry.registerItem(eggGnomeDeep, "eggGnomeDeep");
		
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