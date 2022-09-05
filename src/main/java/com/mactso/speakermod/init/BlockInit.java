package com.mactso.speakermod.init;

import java.util.function.Supplier;

import com.mactso.speakermod.Main;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

	public static final RegistryObject<Block> WIRELESS_JUKEBOX = register(
			"wireless_jukebox",
			() -> new Block(BlockBehaviour.Properties.of(Material.GLASS).strength(0.2F).sound(SoundType.METAL).noOcclusion()),
			new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)
			);

	public static final RegistryObject<Block> WIRELESS_SPEAKER = register(
			"wireless_speaker",
			() -> new Block(BlockBehaviour.Properties.of(Material.GLASS).strength(0.2F).sound(SoundType.METAL).noOcclusion()),
			new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)
			);
	
	private static <T extends Block> RegistryObject<T> register (String name, Supplier<T> supplier, Item.Properties properties) {
		RegistryObject<T> block = BLOCKS.register(name, supplier);
		ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
		return block;
	}
}
