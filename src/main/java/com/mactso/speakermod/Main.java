package com.mactso.speakermod;

import org.apache.commons.lang3.tuple.Pair;

import com.mactso.speakermod.block.ModBlocks;
import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.item.ModItems;
import com.mactso.speakermod.tileentity.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod("speakermod")
public class Main {

	    public static final String MODID = "speakermod"; 
	    
	    public Main()
	    {
	    	System.out.println(MODID + ": Registering Mod.");
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
					() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a,b) -> true));
	    	FMLJavaModLoadingContext.get().getModEventBus().register(this);
 	        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,MyConfig.COMMON_SPEC );

 	        //   	        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	    }


		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
				System.out.println(MODID + ": Registering Handlers");
//				MinecraftForge.EVENT_BUS.register(new SpawnerBreakEvent ());
//				MinecraftForge.EVENT_BUS.register(new SpawnEventHandler());
//				MinecraftForge.EVENT_BUS.register(new MonsterDropEventHandler());
//				MinecraftForge.EVENT_BUS.register(new ExperienceDropEventHandler());
//				MinecraftForge.EVENT_BUS.register(new ChunkEvent());
//				CapabilityChunkLastMobDeathTime.register();
				//				MinecraftForge.EVENT_BUS.register(new MyEntityPlaceEvent());
		}   

		@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	    public static class ModEvents
	    {
		    @SubscribeEvent
		    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
		    {
				System.out.println("SpeakerMod : Register Blocks");
		    	ModBlocks.register(event.getRegistry());
		    }
		    
	    	@SubscribeEvent
	    	public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
	    	{
				System.out.println("RedStoneMagic: Register Items");
	    		ModItems.register(event.getRegistry());
	    	}
		    
		    @SubscribeEvent
		    public static void onTileEntitiesRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
		    {
		        ModTileEntities.register(event.getRegistry());
		    }
		    
		    
	    }	
		
	    @Mod.EventBusSubscriber()
	    public static class ForgeEvents
	    {
//	        @SubscribeEvent
//	        public static void onServerStarting(FMLServerStartingEvent event)
//	        {
//	        	ModEntities.addSpawnData();
//	        }
//
//	        @SubscribeEvent
//	        public static void onServerStopping(FMLServerStoppingEvent event)
//	        {
//	        	ModEntities.removeSpawnData();
//	        }
	    }

}
