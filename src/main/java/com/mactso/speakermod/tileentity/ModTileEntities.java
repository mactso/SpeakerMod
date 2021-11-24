package com.mactso.speakermod.tileentity;

import com.mactso.speakermod.block.ModBlocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class ModTileEntities {

	public static final BlockEntityType<WirelessJukeboxTileEntity> WIRELESS_JUKEBOX = create(
			"wireless_jukebox", BlockEntityType.Builder
					.of(WirelessJukeboxTileEntity::new, ModBlocks.WIRELESS_JUKEBOX).build(null));

	public static <T extends BlockEntity> BlockEntityType<T> create(String key, BlockEntityType<T> type) {
		type.setRegistryName(key);
		return type;
	}

	public static void register(IForgeRegistry<BlockEntityType<?>> forgeRegistry) {
		forgeRegistry.register(WIRELESS_JUKEBOX);

	}
	
}
