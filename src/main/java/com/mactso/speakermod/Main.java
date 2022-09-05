package com.mactso.speakermod;

import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.init.BlockEntityInit;
import com.mactso.speakermod.init.BlockInit;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod("speakermod")
public class Main {

	public static final String MODID = "speakermod";

	public Main() {
		System.out.println(MODID + ": Registering Mod.");
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BlockInit.BLOCKS.register(bus);
		BlockEntityInit.BLOCK_ENTITIES.register(bus);
	}

}
