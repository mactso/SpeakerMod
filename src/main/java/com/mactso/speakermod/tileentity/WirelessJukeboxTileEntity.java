package com.mactso.speakermod.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mactso.speakermod.block.WirelessSpeakerBlock;
import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.init.BlockEntityInit;
import com.mojang.blaze3d.audio.Channel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessJukeboxTileEntity extends BlockEntity  {
	// DaylightDetectorBlock look at this as example.
	List<BlockPos> speakers = new ArrayList<>();
	Channel ss = null;
	int discId = -1;
	int countdown = 0;
	boolean needsSave = false;
	static final ItemStack EMERALD_STACK = new ItemStack(Items.EMERALD, 1);
	static final ItemParticleOption EMERALD_PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, EMERALD_STACK);
	
	public WirelessJukeboxTileEntity(BlockPos worldPosition, BlockState blockState) {
	      super(BlockEntityInit.WIRELESS_JUKEBOX.get(), worldPosition, blockState);
	}

	public int getDiscId ( ) {
		return this.discId;
	}
	
	public boolean addSpeakerPos (BlockPos pos) {
		
		doSpeakerValidation();
		speakers.remove(pos);
		if (speakers.size() < 3) {
			MyConfig.debugMsg(1, pos, "JukeBox Adding Speaker");
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
	// this sucks and I should redo with something like https://forums.minecraftforge.net/topic/60302-any-solved-saving-nbt-data/
	
	public void saveAdditional(CompoundTag compound) {
		// from jukeboxblock code ...  CompoundNBT compoundnbt = stack.getOrCreateTag();
		MyConfig.setDebugLevel(0);  // TODO set back to 0
		MyConfig.debugMsg(1, worldPosition, "Jukebox Saving Speakers");
		if (speakers.size() > 0) {

			if (speakers.size()>0) {
				compound.putInt("spkr0x", speakers.get(0).getX());
				compound.putInt("spkr0y", speakers.get(0).getY());
				compound.putInt("spkr0z", speakers.get(0).getZ());
				MyConfig.debugMsg(1, speakers.get(0), "Saving #0 Speaker");
			}
	
			if (speakers.size()>1) {
				compound.putInt("spkr1x", speakers.get(1).getX());
				compound.putInt("spkr1y", speakers.get(1).getY());
				compound.putInt("spkr1z", speakers.get(1).getZ());
				MyConfig.debugMsg(1, speakers.get(1), "Saving #1 Speaker");
			}
	
			if (speakers.size() > 2) {
				compound.putInt("spkr2x", speakers.get(2).getX());
				compound.putInt("spkr2y", speakers.get(2).getY());
				compound.putInt("spkr2z", speakers.get(2).getZ());
				MyConfig.debugMsg(1, speakers.get(2), "Saving #2 Speaker");
			}

		}
		
		compound.putInt("discId", this.discId);
		MyConfig.debugMsg(1, "Saving discId " + discId);
		MyConfig.setDebugLevel(0); // TODO comment out.

		super.saveAdditional(compound);
	}
	

	// restore state when chunk reloads
	@Override
	public void load(CompoundTag compound) {
		int x,y,z;
		super.load(compound);
		MyConfig.setDebugLevel(0); // TODO set back to 0
		MyConfig.debugMsg(1, "Restoring Speakers");
		speakers.clear();

		x = compound.getInt("spkr0x");
		y = compound.getInt("spkr0y");
		z = compound.getInt("spkr0z");
		BlockPos speakerPos = new BlockPos (x,y,z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos (speakerPos);
			MyConfig.debugMsg(1, speakerPos, "Loading #0 Speaker");
		}

		x = compound.getInt("spkr1x");
		y = compound.getInt("spkr1y");
		z = compound.getInt("spkr1z");
		speakerPos = new BlockPos (x,y,z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos (speakerPos);
			MyConfig.debugMsg(1, speakerPos, "Loading #1 Speaker");
		}

		x = compound.getInt("spkr2x");
		y = compound.getInt("spkr2y");
		z = compound.getInt("spkr2z");
		speakerPos = new BlockPos (x,y,z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos (speakerPos);
			MyConfig.debugMsg(1, speakerPos, "Restoring #2 Speaker");
		}
		
		this.discId = compound.getInt("discId");
		MyConfig.debugMsg(1, "Loading discId " + discId);
		MyConfig.setDebugLevel(0); // TODO remove this

	}
	
	public void playEvent(ServerLevel sw, @Nullable Player player, int type, BlockPos pos, int data) {
	      sw.getServer().getPlayerList().broadcast(player, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 96.0D, sw.dimension(), new ClientboundLevelEventPacket(type, pos, data, false));
	   }
	
	// starts speakers and validates they are still in place.
	public void startSpeakers(int discId) {

		this.discId = discId;
		doSpeakerValidation();
		if (speakers.size() > 0) {
			for (BlockPos spkPos : speakers) {
				MyConfig.debugMsg(1, spkPos, "Start Song at Speaker");
				playEvent((ServerLevel) level, (Player) null, 1010, spkPos, discId);
			}
		}
		countdown = getDiscDuration(discId) * 20;
	}

	public int getDiscDuration (int discId) {
		int index = discId;
		if (index == 1027) 
				return 330;
		if (index == 1028)
				return 330;
		if ((index >=0) && (index <=15)) {
			return MyConfig.duration[index];
		}
		return 240;
	}
	public void doLightShow () {
		boolean lightShow = false;

		if (level.getBlockState(worldPosition.below(1)).getBlock() == Blocks.EMERALD_BLOCK) 
			lightShow = true;
		
		if (level.getBlockState(worldPosition.below(2)).getBlock() == Blocks.EMERALD_BLOCK) 
				lightShow = true;
		
		if (lightShow) {
			doOneLightShowParticleSet(level, worldPosition);
	 		if (speakers.size()>0) {
				for (BlockPos spkPos : speakers) {
					MyConfig.debugMsg(1, spkPos, "Confirming still Speaker");
					if (level.getBlockState(spkPos).getBlock() instanceof WirelessSpeakerBlock) {
						MyConfig.debugMsg(1, spkPos, "Doing Speaker Light Show");
						doOneLightShowParticleSet(level, spkPos);
					}
				}
	 		}
		}
	}

	private void doOneLightShowParticleSet(Level cw, BlockPos pos) {
		for (int i = 0; i < 5; i++) {
			double xSpeed = 0.33  * (level.getRandom().nextDouble() -0.4999);
			double ySpeed = 0.44  * level.getRandom().nextDouble();
			double zSpeed = 0.33  * (level.getRandom().nextDouble() -0.4999);
			double x = pos.getX()+0.5f;
			double y = pos.getY()+1.5f;
			double z = pos.getZ()+0.5f;
			cw.addParticle(EMERALD_PARTICLE, x, y, z, xSpeed, ySpeed, zSpeed);			
		}
	}
	
	public void stopSpeakers () {

		this.discId = -1;
		this.ss = null;
		
		for (BlockPos spkPos : speakers) {
			playEvent((ServerLevel) level, (Player) null, 1010, spkPos, discId);
		}
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, WirelessJukeboxTileEntity blockEntity) {
		blockEntity.realServerTick();
	}
	

	public void realServerTick() {

		long timer = level.getGameTime()%20;
		
		if (countdown > 0)
			countdown--;
//		if ((countdown>0) && (timer == 1)) {
//			MyConfig.debugMsg(0, worldPosition, "lightshow " + countdown);
//			doLightShow();
//		}		
		// NOTE this is on the client side)  // remove test on ticker
		if (this.ss != null) {
			if (ss.stopped()) {
				MyConfig.debugMsg(1, worldPosition, "Sound Event Stopped");
				ss=null;
			} else if (timer == 1) {
				if (countdown>0) {
					MyConfig.debugMsg(0, worldPosition, "lightshow " + countdown);
				}
//				doLightShow();
			}
		}

		if ((discId != -1) && !(level.isClientSide)) {
			long gameTime = level.getDayTime() % 24000;
			Block BlockBelowSpeaker = level.getBlockState(this.worldPosition.below()).getBlock();
			boolean isInverted = false;
			if (BlockBelowSpeaker instanceof DaylightDetectorBlock) {
				isInverted = level.getBlockState(this.worldPosition.below()).getValue(DaylightDetectorBlock.INVERTED);
			}
			
			if (gameTime == 1001) {
				if (BlockBelowSpeaker == Blocks.GLOWSTONE ) {
					startSpeakers(discId);
					this.playEvent((ServerLevel) level, (Player) null, 1010, worldPosition, discId);
				}
				if ((BlockBelowSpeaker == Blocks.DAYLIGHT_DETECTOR ) && (!isInverted)) {
					startSpeakers(discId);
					this.playEvent((ServerLevel) level, (Player) null, 1010, worldPosition, discId);
				}
			}
			if (gameTime == 13001) {
				if (BlockBelowSpeaker == Blocks.COAL_BLOCK ) {
					startSpeakers(discId);
					this.playEvent((ServerLevel) level, (Player) null, 1010, worldPosition, discId);
					}
				if ((BlockBelowSpeaker == Blocks.DAYLIGHT_DETECTOR ) && (isInverted)) {
					startSpeakers(discId);
					this.playEvent((ServerLevel) level, (Player) null, 1010, worldPosition, discId);
				}

			}

		}
		
		if (needsSave) {
			this.setChanged();
			needsSave = false;
		}
	}

	public void setSoundSource(Channel source) {
		this.ss = source;
	}

	public void setDiscId(int discId) {
		this.discId = discId;
		needsSave = true;
		
	}
	
}
