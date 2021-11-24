package com.mactso.speakermod.config;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.speakermod.Main;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}


	
	public static int debugLevel;
	// durations of songs by id number (0 is not a disc) for future multiplay
	public static int[] duration = {0,185,190,350,190,180,205,100,155,195,255,75,240,155};
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfigEvent configEvent) {
		System.out.println("Wireless Speakers Config Event");
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC) {
			bakeConfig();
		}
	}

	public static void pushDebugValue() {
		if (debugLevel > 0) {
			System.out.println("SpeakerMod debugLevel:" + MyConfig.debugLevel);
		}
		COMMON.debugLevel.set(MyConfig.debugLevel);
	}

	public static void bakeConfig() {
		
		debugLevel = COMMON.debugLevel.get();
		
		if (debugLevel > 0) {
			System.out.println("Wireless Speaker Debug: " + debugLevel);
		}
		

	}
	
	
	
	public static class Common {


		
		public final IntValue debugLevel;
		
		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("Spawn Biome Utility Control Values");

			debugLevel = builder.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel").defineInRange("debugLevel", () -> 0, 0, 2);
			
			builder.pop();
		}
	}
	
	public static void debugMsg (int level, String dMsg) {
		if (getDebugLevel() > level-1) {
			System.out.println("L"+level + ":" + dMsg);
		}
	}

	public static void debugMsg (int level, BlockPos pos, String dMsg) {
		if (getDebugLevel() > level-1) {
			System.out.println("L"+level+" ("+pos.getX()+","+pos.getY()+","+pos.getZ()+"): " + dMsg);
		}
	}

	// support for any color chattext
	public static void sendChat(Player p, String chatMessage, TextColor color) {
		TextComponent component = new TextComponent(chatMessage);
		component.getStyle().withColor(color);
		p.sendMessage(component, p.getUUID());
	}

	// support for any color, optionally bold text.
	public static void sendBoldChat(Player p, String chatMessage, TextColor color) {
		TextComponent component = new TextComponent(chatMessage);

		component.getStyle().withBold(true);
		component.getStyle().withColor(color);

		p.sendMessage(component, p.getUUID());
	}
}
