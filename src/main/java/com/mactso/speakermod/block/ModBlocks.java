package com.mactso.speakermod.block;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static final Block WIRELESS_JUKEBOX = new WirelessJukeboxBlock(Properties.create(Material.GLASS).hardnessAndResistance(0.2F).sound(SoundType.METAL)).setRegistryName("wireless_jukebox");
	public static final Block WIRELESS_SPEAKER = new WirelessSpeakerBlock(Properties.create(Material.GLASS).hardnessAndResistance(0.2F).sound(SoundType.METAL)).setRegistryName("wireless_speaker");

	public static void register(IForgeRegistry<Block> forgeRegistry	)
	{
		forgeRegistry.register(WIRELESS_JUKEBOX);
		forgeRegistry.register(WIRELESS_SPEAKER);
		
	}



}
