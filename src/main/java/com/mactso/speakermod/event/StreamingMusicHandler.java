package com.mactso.speakermod.event;

import com.mactso.speakermod.block.WirelessJukeboxBlock;
import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.tileentity.WirelessJukeboxTileEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mod.EventBusSubscriber()
public class StreamingMusicHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onStreamingMusicEvent(PlayStreamingSourceEvent event) {
		Minecraft mc = Minecraft.getInstance();
		World w = mc.world;
		
		BlockPos pos = new BlockPos (event.getSound().getX(),event.getSound().getY(),event.getSound().getZ());
		MyConfig.debugMsg(1, pos, "Handling PlayStreamingSourceEvent");

		if (w.getBlockState(pos).getBlock() instanceof WirelessJukeboxBlock) {
			WirelessJukeboxTileEntity wjb = (WirelessJukeboxTileEntity) w.getTileEntity(pos);
			wjb.setSoundSource(event.getSource());
		}
	}
}
