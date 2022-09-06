package com.mactso.speakermod;

import com.mactso.speakermod.init.BlockEntityInit;
import com.mactso.speakermod.init.BlockInit;
import com.mactso.speakermod.init.ItemInit;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("speakermod")
public class Main {

	public static final String MODID = "speakermod";

	public Main() {
		System.out.println(MODID + ": Registering Mod.");
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BlockInit.BLOCKS.register(bus);
		ItemInit.ITEMS.register(bus);
		BlockEntityInit.BLOCK_ENTITIES.register(bus);
	}

}
