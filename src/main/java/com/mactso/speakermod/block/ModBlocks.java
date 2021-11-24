package com.mactso.speakermod.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static final Block WIRELESS_JUKEBOX = new WirelessJukeboxBlock(Properties.of(Material.GLASS).strength(0.2F).sound(SoundType.METAL).noOcclusion()).setRegistryName("wireless_jukebox");
	public static final Block WIRELESS_SPEAKER = new WirelessSpeakerBlock(Properties.of(Material.GLASS).strength(0.2F).sound(SoundType.METAL).noOcclusion()).setRegistryName("wireless_speaker");

	public static void register(IForgeRegistry<Block> forgeRegistry	)
	{
		forgeRegistry.register(WIRELESS_JUKEBOX);
		forgeRegistry.register(WIRELESS_SPEAKER);
		
	}



}
