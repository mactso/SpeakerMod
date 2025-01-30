package com.mactso.speakermod.config;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.speakermod.Main;
import com.mactso.speakermod.utilities.Utility;

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

	public static int getJukeboxLightLevel() {
		return jukeboxLightLevel;
	}

	public static int getSpeakerLightLevel() {
		return speakerLightLevel;
	}

	public static void setDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}

	public static int debugLevel;
	private static int jukeboxLightLevel;
	private static int speakerLightLevel;
	// durations of songs by id number (0 is not a disc) for future multiplay
	public static int[] duration = { 0, 185, 190, 350, 190, 180, 205, 100, 155, 195, 255, 75, 240, 155 };

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfigEvent configEvent) {
		Utility.debugMsg(1, "Wireless Speakers Config Event");
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC) {
			bakeConfig();
		}
	}

	public static void pushDebugValue() {
		if (debugLevel > 0) {
			Utility.debugMsg(1, "SpeakerMod debugLevel:" + MyConfig.debugLevel);
		}
		COMMON.debugLevel.set(MyConfig.debugLevel);
	}

	public static void bakeConfig() {

		debugLevel = COMMON.debugLevel.get();
		jukeboxLightLevel = COMMON.jukeboxLightLevel.get();
		speakerLightLevel = COMMON.speakerLightLevel.get();

		if (debugLevel > 0) {
			Utility.debugMsg(1,"Wireless Speaker Debug: " + debugLevel);
			Utility.debugMsg(1,"Wireless Jukebox Light: " + jukeboxLightLevel);
			Utility.debugMsg(1,"Wireless Speaker Light: " + speakerLightLevel);

		}

	}

	public static class Common {

		public final IntValue debugLevel;
		public final IntValue jukeboxLightLevel;
		public final IntValue speakerLightLevel;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("Speaker Mod Control Values");

			debugLevel = builder.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel").defineInRange("debugLevel", () -> 0, 0, 2);

			jukeboxLightLevel = builder.comment("Jukebox Light Level 0-15. Overworld Night is 4.")
					.translation(Main.MODID + ".config." + "jukeboxLightLevel")
					.defineInRange("jukeboxLightLevel", () -> 9, 0, 15);

			speakerLightLevel = builder.comment("Speaker Light Level 0-15. Overworld Night is 4.")
					.translation(Main.MODID + ".config." + "speakerLightLevel")
					.defineInRange("speakerLightLevel", () -> 6, 0, 15);

			builder.pop();
		}
	}


}
