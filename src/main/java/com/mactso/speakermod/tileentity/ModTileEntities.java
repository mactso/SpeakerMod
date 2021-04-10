package com.mactso.speakermod.tileentity;

import com.mactso.speakermod.block.ModBlocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class ModTileEntities {

	public static final TileEntityType<WirelessJukeboxTileEntity> WIRELESS_JUKEBOX = create(
			"wireless_jukebox", TileEntityType.Builder
					.create(WirelessJukeboxTileEntity::new, ModBlocks.WIRELESS_JUKEBOX).build(null));

	public static <T extends TileEntity> TileEntityType<T> create(String key, TileEntityType<T> type) {
		type.setRegistryName(key);
		return type;
	}

	public static void register(IForgeRegistry<TileEntityType<?>> forgeRegistry) {
		forgeRegistry.register(WIRELESS_JUKEBOX);

	}
	
}
