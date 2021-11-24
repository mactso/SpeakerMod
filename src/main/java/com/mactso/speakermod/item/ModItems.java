package com.mactso.speakermod.item;

import com.mactso.speakermod.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraft.world.item.Item.Properties;

public class ModItems {

	public static final Item WIRELESS_JUKEBOX 
	 = new BlockItem(ModBlocks.WIRELESS_JUKEBOX, new Properties().tab(CreativeModeTab.TAB_REDSTONE)).setRegistryName("wireless_jukebox");

	public static final Item WIRELESS_SPEAKER 
	 = new BlockItem(ModBlocks.WIRELESS_SPEAKER, new Properties().stacksTo(1).tab(CreativeModeTab.TAB_REDSTONE)).setRegistryName("wireless_speaker");
	
	public static void register(IForgeRegistry<Item> forgeRegistry)
	{
		forgeRegistry.register(WIRELESS_JUKEBOX );
		forgeRegistry.register(WIRELESS_SPEAKER );
	}
	
}
