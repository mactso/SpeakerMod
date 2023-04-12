package com.mactso.speakermod.event;


import com.mactso.speakermod.Main;
import com.mactso.speakermod.init.BlockInit;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class HandleTabSetup {

	// TODO - rename this to HandleTabSetup next time in here.
	@SubscribeEvent
	public static void handleTabSetup (CreativeModeTabEvent.BuildContents event)
    {
		
        if (event.getTab() == CreativeModeTabs.REDSTONE_BLOCKS)
        {
            event.accept(BlockInit.WIRELESS_JUKEBOX);
            event.accept(BlockInit.WIRELESS_SPEAKER);
        }
    }
}
