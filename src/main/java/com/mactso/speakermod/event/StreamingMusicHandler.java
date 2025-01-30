package com.mactso.speakermod.event;

import com.mactso.speakermod.block.WirelessJukeboxBlock;
import com.mactso.speakermod.blockentities.WirelessJukeboxBlockEntity;
import com.mactso.speakermod.utilities.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class StreamingMusicHandler {

	@SubscribeEvent
	public static void onStreamingMusicEvent(PlayStreamingSourceEvent event) {
		Minecraft mc = Minecraft.getInstance();
		Level w = mc.level;
		if (w == null) 
			return;

		BlockPos pos = BlockPos.containing(event.getSound().getX(), event.getSound().getY(), event.getSound().getZ());

		if (w.getBlockState(pos).getBlock() instanceof WirelessJukeboxBlock) {
			Utility.debugMsg(1, pos, "Handling PlayStreamingSourceEvent");
			WirelessJukeboxBlockEntity wjb = (WirelessJukeboxBlockEntity) w.getBlockEntity(pos);
			wjb.setSoundSource(event.getChannel());
		}

	}
}
