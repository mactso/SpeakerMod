package com.mactso.speakermod.item;

import com.mactso.speakermod.block.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraft.item.Item.Properties;

public class ModItems {

	public static final Item WIRELESS_JUKEBOX 
	 = new BlockItem(ModBlocks.WIRELESS_JUKEBOX, new Properties().group(ItemGroup.REDSTONE)).setRegistryName("wireless_jukebox");

	public static final Item WIRELESS_SPEAKER 
	 = new BlockItem(ModBlocks.WIRELESS_SPEAKER, new Properties().maxStackSize(1).group(ItemGroup.REDSTONE)).setRegistryName("wireless_speaker");
	
	public static void register(IForgeRegistry<Item> forgeRegistry)
	{
		forgeRegistry.register(WIRELESS_JUKEBOX );
		forgeRegistry.register(WIRELESS_SPEAKER );
	}
	
}
