package com.mactso.speakermod.blockentities;

import java.util.ArrayList;
import java.util.List;

import com.mactso.speakermod.block.WirelessSpeakerBlock;
import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.init.BlockEntityInit;
import com.mactso.speakermod.utilities.Utility;
import com.mojang.blaze3d.audio.Channel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WirelessJukeboxBlockEntity extends BlockEntity {
	// DaylightDetectorBlock look at this as example.
	static final int SUNRISE = 1001;
	static final int SUNSET = 13001;
	List<BlockPos> speakers = new ArrayList<>();
	Channel soundSource = null;
	public int NO_SONG = -1;
	private int songId = NO_SONG;
	int countdown = 0;
	boolean needsSave = false;
	static final ItemStack EMERALD_STACK = new ItemStack(Items.EMERALD, 1);
	static final ItemParticleOption EMERALD_PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, EMERALD_STACK);

	public WirelessJukeboxBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(BlockEntityInit.WIRELESS_JUKEBOX.get(), worldPosition, blockState);
	}

	public int getSongId() {
		return this.songId;
	}

	public boolean addSpeakerPos(BlockPos pos) {

		doSpeakerValidation();
		speakers.remove(pos);
		if (speakers.size() < 3) {
			Utility.debugMsg(1, pos, "JukeBox Adding Speaker");
			speakers.add(pos);
		} else {
			return false;
		}
		this.needsSave = true;

		return true;
	}

	public void doSpeakerValidation() {
		if (level == null)
			return;
		List<BlockPos> badSpeakers = new ArrayList<>();
		for (BlockPos spkPos : speakers) {
			if (!(level.getBlockState(spkPos).getBlock() instanceof WirelessSpeakerBlock)) {
				badSpeakers.add(spkPos);
			}
		}
		speakers.removeAll(badSpeakers);
	}

	@Override
	// save state when chunk unloads
	// this sucks and I should redo with something like
	// https://forums.minecraftforge.net/topic/60302-any-solved-saving-nbt-data/
	public void saveAdditional(CompoundTag compound, Provider provider) {
		// from jukeboxblock code ... CompoundNBT compoundnbt = stack.getOrCreateTag();
		// MyConfig.setDebugLevel(0); // TODO set back to 0
		Utility.debugMsg(1, worldPosition, "Jukebox Saving Speakers");
		if (speakers.size() > 0) {

			if (speakers.size() > 0) {
				compound.putInt("spkr0x", speakers.get(0).getX());
				compound.putInt("spkr0y", speakers.get(0).getY());
				compound.putInt("spkr0z", speakers.get(0).getZ());
			}

			if (speakers.size() > 1) {
				compound.putInt("spkr1x", speakers.get(1).getX());
				compound.putInt("spkr1y", speakers.get(1).getY());
				compound.putInt("spkr1z", speakers.get(1).getZ());
			}

			if (speakers.size() > 2) {
				compound.putInt("spkr2x", speakers.get(2).getX());
				compound.putInt("spkr2y", speakers.get(2).getY());
				compound.putInt("spkr2z", speakers.get(2).getZ());
			}

		}

		compound.putInt("discId", this.songId);
		Utility.debugMsg(1, "Saving discId " + songId);

		super.saveAdditional(compound, provider);
	}

	// restore state when chunk reloads

	@Override
	protected void loadAdditional(CompoundTag compound, Provider provider) {
		int x, y, z;
		super.loadAdditional(compound, provider);
		Utility.debugMsg(1, "Restoring Speakers");
		speakers.clear();

		x = compound.getInt("spkr0x");
		y = compound.getInt("spkr0y");
		z = compound.getInt("spkr0z");
		BlockPos speakerPos = new BlockPos(x, y, z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos(speakerPos);
			Utility.debugMsg(1, speakerPos, "Loading #0 Speaker");
		}

		x = compound.getInt("spkr1x");
		y = compound.getInt("spkr1y");
		z = compound.getInt("spkr1z");
		speakerPos = new BlockPos(x, y, z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos(speakerPos);
			Utility.debugMsg(1, speakerPos, "Loading #1 Speaker");
		}

		x = compound.getInt("spkr2x");
		y = compound.getInt("spkr2y");
		z = compound.getInt("spkr2z");
		speakerPos = new BlockPos(x, y, z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos(speakerPos);
			Utility.debugMsg(1, speakerPos, "Restoring #2 Speaker");
		}

		this.songId = compound.getInt("discId");
		Utility.debugMsg(1, "Loading discId " + songId);

	}

	public void startWirelessJukeBox(ServerLevel sLevel, Holder<JukeboxSong> songHolderIn) {
		Utility.debugMsg(1, this.worldPosition, "Start New Song at JukeBox");
		spawnMusicParticles(sLevel, this.worldPosition);
		this.songId = sLevel.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG).getId(songHolderIn.value());
		playEvent(sLevel, songId, this.worldPosition);
		startWirelessSpeakers(sLevel);
	}

	public void startWirelessJukeBox(ServerLevel sLevel) {
		Utility.debugMsg(1, this.worldPosition, "Start Song at JukeBox");
		spawnMusicParticles(sLevel, this.worldPosition);
		playEvent(sLevel, this.songId, this.worldPosition);
		startWirelessSpeakers(sLevel);

	}

	public void stopWirelessJukebox(ServerLevel sLevel) {
		Utility.debugMsg(1, this.worldPosition, "Stop Song at JukeBox");
		sLevel.levelEvent(1011, this.worldPosition, 0);
		stopWirelessSpeakers(sLevel);
	}

	// starts speakers and validates they are still in place.
	public void startWirelessSpeakers(ServerLevel sLevel) {

		doSpeakerValidation();
		if (speakers.size() > 0) {
			for (BlockPos spkPos : speakers) {
				Utility.debugMsg(1, spkPos, "Start Song at Speaker");
				spawnMusicParticles(sLevel, spkPos);
				playEvent(sLevel, this.songId, spkPos);
			}
		}
		countdown = getDiscDuration(this.songId) * 20;
	}

	public void stopWirelessSpeakers(ServerLevel sLevel) {

		for (BlockPos spkPos : speakers) {
			Utility.debugMsg(1, spkPos, "Stopping Song at Speaker");
			sLevel.levelEvent(1011, spkPos, 0);
		}
	}

	public void playEvent(ServerLevel sLevel, int songId, BlockPos pos) {
		Utility.debugMsg(1, pos, "Playevent songid: " + songId + "  at pos.");
		sLevel.levelEvent(null, 1010, pos, songId);
	}

	public int getDiscDuration(int songId) {
		int index = songId;
		if (index == 1027)
			return 330;
		if (index == 1028)
			return 330;
		if ((index >= 0) && (index <= 15)) {
			return MyConfig.duration[index];
		}
		return 240;
	}

	public void doLightShow() {
		boolean lightShow = false;

		if (level.getBlockState(worldPosition.below(1)).getBlock() == Blocks.EMERALD_BLOCK)
			lightShow = true;

		if (level.getBlockState(worldPosition.below(2)).getBlock() == Blocks.EMERALD_BLOCK)
			lightShow = true;

		if (lightShow) {
			doOneLightShowParticleSet(level, worldPosition);
			if (speakers.size() > 0) {
				for (BlockPos spkPos : speakers) {
					Utility.debugMsg(1, spkPos, "Confirming still Speaker");
					if (level.getBlockState(spkPos).getBlock() instanceof WirelessSpeakerBlock) {
						Utility.debugMsg(1, spkPos, "Doing Speaker Light Show");
						doOneLightShowParticleSet(level, spkPos);
					}
				}
			}
		}
	}

	private void doOneLightShowParticleSet(Level cw, BlockPos pos) {
		for (int i = 0; i < 5; i++) {
			double xSpeed = 0.33 * (level.getRandom().nextDouble() - 0.4999);
			double ySpeed = 0.44 * level.getRandom().nextDouble();
			double zSpeed = 0.33 * (level.getRandom().nextDouble() - 0.4999);
			double x = pos.getX() + 0.5f;
			double y = pos.getY() + 1.5f;
			double z = pos.getZ() + 0.5f;
			cw.addParticle(EMERALD_PARTICLE, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, WirelessJukeboxBlockEntity blockEntity) {
		blockEntity.realServerTick();
	}

	public void realServerTick() {

		long timer = level.getGameTime() % 20;

		if (countdown > 0)
			countdown--;
//		if ((countdown>0) && (timer == 1)) {
//			Utility.debugMsg(1, worldPosition, "lightshow " + countdown);
//			doLightShow();
//		}		
		// NOTE this is on the client side) // remove test on ticker

		ServerLevel sLevel = (ServerLevel) level;
		playOptionalSunriseSunsetMusic(sLevel);

		if (needsSave) {
			this.setChanged();
			needsSave = false;
		}
	}

	private void playOptionalSunriseSunsetMusic(ServerLevel sLevel) {

		if (level.isClientSide())
			return;

		if (this.songId == NO_SONG)
			return;

		long dayTime = sLevel.getDayTime() % 24000;
		if ((dayTime != SUNRISE) && (dayTime != SUNSET))
			return;
int x =3;
		Block blockBelowSpeaker = level.getBlockState(this.worldPosition.below()).getBlock();
		playSunriseMusic(sLevel, blockBelowSpeaker);
		playSunsetMusic(sLevel, blockBelowSpeaker);

	}

	private void playSunriseMusic(ServerLevel sLevel, Block blockBelowSpeaker) {
		if (sLevel.getDayTime() != SUNRISE)
			return;

		if (blockBelowSpeaker != Blocks.GLOWSTONE)
			return;

		boolean isInverted = false;
		if (blockBelowSpeaker instanceof DaylightDetectorBlock) {
			isInverted = level.getBlockState(this.worldPosition.below()).getValue(DaylightDetectorBlock.INVERTED);
		}

		if ((blockBelowSpeaker == Blocks.DAYLIGHT_DETECTOR) && (isInverted))
			return;

		startWirelessJukeBox(sLevel);
		return;
	}

	private void playSunsetMusic(ServerLevel sLevel, Block blockBelowSpeaker) {

		if (sLevel.getDayTime() != SUNSET)
			return;
		Utility.debugMsg(1, "it's sunset");
		if (blockBelowSpeaker != Blocks.COAL_BLOCK)
			return;

		boolean isInverted = false;
		if (blockBelowSpeaker instanceof DaylightDetectorBlock) {
			isInverted = level.getBlockState(this.worldPosition.below()).getValue(DaylightDetectorBlock.INVERTED);
		}
		if ((blockBelowSpeaker == Blocks.DAYLIGHT_DETECTOR) && (!isInverted))
			return;
		startWirelessJukeBox(sLevel);
		return;
	}

	private static void spawnMusicParticles(LevelAccessor p_343992_, BlockPos p_342425_) {
		if (p_343992_ instanceof ServerLevel serverlevel) {
			Vec3 vec3 = Vec3.atBottomCenterOf(p_342425_).add(0.0, 1.2F, 0.0);
			float f = (float) p_343992_.getRandom().nextInt(4) / 24.0F;
			serverlevel.sendParticles(ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), 0, (double) f, 0.0, 0.0, 1.0);
		}
	}

	public void setSoundSource(Channel source) {
		Utility.debugMsg(1, "soundsource set to " + source.toString());
		this.soundSource = source;
	}

	public void setSongId(int songId) {
		this.songId = songId;
		this.setChanged();
	}

	public void forgetSong() {
		this.songId = NO_SONG;

		this.setChanged();
	}

}
