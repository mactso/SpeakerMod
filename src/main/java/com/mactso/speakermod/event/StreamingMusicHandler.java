package com.mactso.speakermod.event;

import com.mactso.speakermod.block.WirelessJukeboxBlock;
import com.mactso.speakermod.blockentities.WirelessJukeboxBlockEntity;
import com.mactso.speakermod.config.MyConfig;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber()
public class StreamingMusicHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onStreamingMusicEvent(PlayStreamingSourceEvent event) {
		Minecraft mc = Minecraft.getInstance();
		Level w = mc.level;
		
		if (w != null) {
			BlockPos pos = new BlockPos (event.getSound().getX(),event.getSound().getY(),event.getSound().getZ());
			MyConfig.debugMsg(1, pos, "Handling PlayStreamingSourceEvent");

			if (w.getBlockState(pos).getBlock() instanceof WirelessJukeboxBlock) {
				WirelessJukeboxBlockEntity wjb = (WirelessJukeboxBlockEntity) w.getBlockEntity(pos);
				wjb.setSoundSource(event.getChannel());
			}
		
		}
		
	}
}
