package com.mactso.speakermod.init;

import com.mactso.speakermod.Main;
import com.mactso.speakermod.blockentities.WirelessJukeboxBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);

    public static final RegistryObject<BlockEntityType<WirelessJukeboxBlockEntity>> WIRELESS_JUKEBOX = BLOCK_ENTITIES.register("wireless_jukebox",
            () -> BlockEntityType.Builder.of(WirelessJukeboxBlockEntity::new, BlockInit.WIRELESS_JUKEBOX.get()).build(null));
}
