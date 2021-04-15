package com.mactso.speakermod.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mactso.speakermod.block.WirelessSpeakerBlock;
import com.mactso.speakermod.config.MyConfig;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class WirelessJukeboxTileEntity extends TileEntity implements ITickableTileEntity {
	List<BlockPos> speakers = new ArrayList<>();
	SoundSource ss = null;
	int discId = -1;
	boolean needsSave = false;
	static final ItemStack EMERALD_STACK = new ItemStack(Items.EMERALD, 1);
	static final ItemParticleData EMERALD_PARTICLE = new ItemParticleData(ParticleTypes.ITEM, EMERALD_STACK);
	
	public WirelessJukeboxTileEntity() {
	      super(ModTileEntities.WIRELESS_JUKEBOX);
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
		List<BlockPos> badSpeakers = new ArrayList<>();
		for (BlockPos spkPos : speakers) {
			if (!(world.getBlockState(spkPos).getBlock() instanceof WirelessSpeakerBlock)) {
				badSpeakers.add(spkPos);
			}
		}
		speakers.removeAll(badSpeakers);
	}
	
	@Override
	// save state when chunk unloads
	// this sucks and I should redo with something like https://forums.minecraftforge.net/topic/60302-any-solved-saving-nbt-data/
	
	public CompoundNBT write(CompoundNBT compound) {
		// from jukeboxblock code ...  CompoundNBT compoundnbt = stack.getOrCreateTag();
		MyConfig.debugMsg(1, pos, "Jukebox Saving Speakers");
		if (speakers.size() > 0) {

			if (speakers.size()>0) {
				compound.putInt("spkr0x", speakers.get(0).getX());
				compound.putInt("spkr0y", speakers.get(0).getY());
				compound.putInt("spkr0z", speakers.get(0).getZ());
				MyConfig.debugMsg(1, speakers.get(0), "Saving a Speaker");
			}
	
			if (speakers.size()>1) {
				compound.putInt("spkr1x", speakers.get(1).getX());
				compound.putInt("spkr1y", speakers.get(1).getY());
				compound.putInt("spkr1z", speakers.get(1).getZ());
				MyConfig.debugMsg(1, speakers.get(1), "Saving a Speaker");
			}
	
			if (speakers.size() > 2) {
				compound.putInt("spkr2x", speakers.get(2).getX());
				compound.putInt("spkr2y", speakers.get(2).getY());
				compound.putInt("spkr2z", speakers.get(2).getZ());
				MyConfig.debugMsg(1, speakers.get(2), "Saving aSpeaker");
			}

		}
		
		compound.putInt("discId", this.discId);
		return super.write(compound);
	}
	
	@Override
	// restore state when chunk reloads
	public void read(BlockState state, CompoundNBT compound) {
		int x,y,z;
		
		super.read(state, compound);

		MyConfig.debugMsg(1, "Restoring Speakers");
		speakers.clear();

		x = compound.getInt("spkr0x");
		y = compound.getInt("spkr0y");
		z = compound.getInt("spkr0z");
		BlockPos speakerPos = new BlockPos (x,y,z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos (speakerPos);
			MyConfig.debugMsg(1, speakerPos, "Restoring Speaker");
		}

		x = compound.getInt("spkr1x");
		y = compound.getInt("spkr1y");
		z = compound.getInt("spkr1z");
		speakerPos = new BlockPos (x,y,z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos (speakerPos);
			MyConfig.debugMsg(1, speakerPos, "Restoring Speaker");
		}

		x = compound.getInt("spkr2x");
		y = compound.getInt("spkr2y");
		z = compound.getInt("spkr2z");
		speakerPos = new BlockPos (x,y,z);
		if (!speakerPos.equals(BlockPos.ZERO)) {
			addSpeakerPos (speakerPos);
			MyConfig.debugMsg(1, speakerPos, "Restoring Speaker");
		}
		
		this.discId = compound.getInt("discId");

	}
	
	public void playEvent(ServerWorld sw, @Nullable PlayerEntity player, int type, BlockPos pos, int data) {
	      sw.getServer().getPlayerList().sendToAllNearExcept(player, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 96.0D, sw.getDimensionKey(), new SPlaySoundEventPacket(type, pos, data, false));
	   }
	
	// starts speakers and validates they are still in place.
	public void startSpeakers(int discId) {

		this.discId = discId;
		doSpeakerValidation();
		if (speakers.size() > 0) {
			for (BlockPos spkPos : speakers) {
				MyConfig.debugMsg(1, spkPos, "Start Song at Speaker");
				playEvent((ServerWorld) world, (PlayerEntity) null, 1010, spkPos, discId);
			}
		}
	}

//	public void doLightShow () {
//		boolean lightShow = false;
//
//		if (world.getBlockState(pos.down(1)).getBlock() == Blocks.EMERALD_BLOCK) 
//			lightShow = true;
//		
//		if (world.getBlockState(pos.down(2)).getBlock() == Blocks.EMERALD_BLOCK) 
//				lightShow = true;
//		
//		if (lightShow) {
//			doOneLightShowParticleSet(world, pos);
//	 		if (speakers.size()>0) {
//				for (BlockPos spkPos : speakers) {
//					MyConfig.debugMsg(1, spkPos, "Confirming still Speaker");
//					if (world.getBlockState(spkPos).getBlock() instanceof WirelessSpeakerBlock) {
//						MyConfig.debugMsg(1, spkPos, "Doing Speaker Light Show");
//						doOneLightShowParticleSet(world, spkPos);
//					}
//				}
//	 		}
//		}
//	}
//
//	private void doOneLightShowParticleSet(World cw, BlockPos pos) {
//		for (int i = 0; i < 5; i++) {
//			double xSpeed = 0.33  * (world.getRandom().nextDouble() -0.4999);
//			double ySpeed = 0.44  * world.getRandom().nextDouble();
//			double zSpeed = 0.33  * (world.getRandom().nextDouble() -0.4999);
//			double x = pos.getX()+0.5f;
//			double y = pos.getY()+1.5f;
//			double z = pos.getZ()+0.5f;
//			cw.addParticle(EMERALD_PARTICLE, x, y, z, xSpeed, ySpeed, zSpeed);			
//		}
//	}
	
	public void stopSpeakers () {

		this.discId = -1;
		this.ss = null;
		
		for (BlockPos spkPos : speakers) {
			playEvent((ServerWorld) world, (PlayerEntity) null, 1010, spkPos, discId);
		}
	}
	
	
	@Override
	public void tick() {
		
		long timer = world.getGameTime()%20;
		
		// NOTE this is on the client side)
		if (this.ss != null) {
			if (ss.isStopped()) {
				MyConfig.debugMsg(1, pos, "Sound Event Stopped");
				ss=null;
			} else if (timer == 1) {
//				doLightShow();
			}
		}

		if ((discId != -1) && !(world.isRemote)) {
			long gameTime = world.getDayTime() % 24000;

			if (gameTime == 1001) {
				if (world.getBlockState(this.pos.down()).getBlock() == Blocks.GLOWSTONE ) {
					startSpeakers(discId);
					this.playEvent((ServerWorld) world, (PlayerEntity) null, 1010, pos, discId);
				}
			}
			if (gameTime == 13001) {
				if (world.getBlockState(this.pos.down()).getBlock() == Blocks.COAL_BLOCK ) {
					startSpeakers(discId);
					this.playEvent((ServerWorld) world, (PlayerEntity) null, 1010, pos, discId);				}
			}
		}

		if (needsSave) {
			this.markDirty();
			needsSave = false;
		}
	}

	public void setSoundSource(SoundSource source) {
		this.ss = source;
	}

	public void setDiscId(int discId) {
		this.discId = discId;
		needsSave = true;
		
	}
	
}
