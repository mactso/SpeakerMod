package com.mactso.speakermod;

import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.init.BlockEntityInit;
import com.mactso.speakermod.init.BlockInit;
import com.mactso.speakermod.init.ItemInit;
import com.mactso.speakermod.utilities.Utility;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("speakermod")
public class Main {

	public static final String MODID = "speakermod";

	public Main() {
		Utility.debugMsg(1,MODID + ": Registering Mod.");
		Utility.debugMsg(1,MODID + ": Registering Mod.");
	    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,MyConfig.COMMON_SPEC );
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BlockInit.BLOCKS.register(bus);
		ItemInit.ITEMS.register(bus);
		BlockEntityInit.BLOCK_ENTITIES.register(bus);
	}

}
