package com.mactso.speakermod.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.mactso.speakermod.block.WirelessSpeakerBlock;
import com.mactso.speakermod.config.MyConfig;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;


public class WirelessJukeboxTileEntity extends TileEntity implements ITickableTileEntity {
	List<BlockPos> speakers = new ArrayList<>();
	int discId = 0;
	
	public WirelessJukeboxTileEntity() {
	      super(ModTileEntities.WIRELESS_JUKEBOX);
	}
	
	public boolean addSpeakerPos (BlockPos pos) {
		int x = speakers.size();
		if (speakers.size() < 3) {
			MyConfig.debugMsg(0, pos, "Adding Speaker");
			speakers.add(pos);
		} else {
			world.playSound(null, pos, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.6f, 0.3f);
			return false;
		}
		return true;
	}

	@Override
	// save state when chunk unloads
	// this sucks and I should redo with something like https://forums.minecraftforge.net/topic/60302-any-solved-saving-nbt-data/
	
	public CompoundNBT write(CompoundNBT compound) {

		MyConfig.debugMsg(0, pos, "Saving Speakers");
		if (speakers.size() > 0) {

			if (speakers.size()>0) {
				compound.putInt("spkr0x", speakers.get(0).getX());
				compound.putInt("spkr0y", speakers.get(0).getY());
				compound.putInt("spkr0z", speakers.get(0).getZ());
				MyConfig.debugMsg(0, speakers.get(0), "Saving Speaker");
			}
	
			if (speakers.size()>1) {
				compound.putInt("spkr1x", speakers.get(1).getX());
				compound.putInt("spkr1y", speakers.get(1).getY());
				compound.putInt("spkr1z", speakers.get(1).getZ());
				MyConfig.debugMsg(0, speakers.get(1), "Saving Speaker");
			}
	
			if (speakers.size() > 2) {
				compound.putInt("spkr2x", speakers.get(2).getX());
				compound.putInt("spkr2y", speakers.get(2).getY());
				compound.putInt("spkr2z", speakers.get(2).getZ());
				MyConfig.debugMsg(0, speakers.get(2), "Saving Speaker");
			}

		}
		
		return super.write(compound);
	}
	
	@Override
	// restore state when chunk reloads
	public void read(BlockState state, CompoundNBT compound) {
		
		super.read(state, compound);

		MyConfig.debugMsg(0, "Restoring Speakers");
		speakers.clear();
		if (compound.hasUniqueId("spkr0x") && compound.hasUniqueId("spkr0y") && compound.hasUniqueId("spkr0z") ) {
			BlockPos speakerPos = new BlockPos (compound.getInt("spkr0x"),compound.getInt("spkr0y"),compound.getInt("spkr0z"));
			speakers.add(speakerPos);
			MyConfig.debugMsg(0, speakerPos, "Restoring Speakers");
		}
		if (compound.hasUniqueId("spkr1x") && compound.hasUniqueId("spkr1y") && compound.hasUniqueId("spkr1y") ) {
			BlockPos speakerPos = new BlockPos (compound.getInt("spkr1x"),compound.getInt("spkr1y"),compound.getInt("spkr1z"));
			speakers.add(speakerPos);
			MyConfig.debugMsg(0, "Restoring Speakers");
		}
		if (compound.hasUniqueId("spkr2x") && compound.hasUniqueId("spkr2y") && compound.hasUniqueId("spkr2z") ) {
			BlockPos speakerPos = new BlockPos (compound.getInt("spkr2x"),compound.getInt("spkr2y"),compound.getInt("spkr2z"));
			speakers.add(speakerPos);
			MyConfig.debugMsg(0, "Restoring Speakers");
		}

	}
	
	
	// starts speakers and validates they are still in place.
	public void startSpeakers (int discId) {
		
		this.discId = discId;
		
 		if (speakers.size()>0) {
			List<BlockPos> updatedList = new ArrayList<>();
			for (BlockPos spkPos : speakers) {
				if (world.getBlockState(spkPos).getBlock() instanceof WirelessSpeakerBlock) {
					updatedList.add(spkPos);
					world.playEvent((PlayerEntity)null, 1010, spkPos, discId);	
				}
			}
			speakers.clear();
			for (BlockPos fPos : updatedList) {
				speakers.add(fPos);
				MyConfig.debugMsg(0, fPos, "Speaker still valid on disc start.");
			}
		}
	}

	public void stopSpeakers () {
		this.discId = 0;
		for (BlockPos spkPos : speakers) {
			world.playEvent(1010, spkPos, 0);	
		}
	}
	
	
	@Override
	public void tick() {
		if (discId >= 1 && discId<=13) {
			if (world.getGameTime()%24000 == 1010) {
				if (world.getBlockState(this.pos.down()).getBlock() == Blocks.GLOWSTONE ) {
					startSpeakers(discId);
					world.playEvent(1010, pos, discId);
				}
			}
			if (world.getGameTime()%24000 == 13010) {
				if (world.getBlockState(this.pos.down()).getBlock() == Blocks.COAL_BLOCK ) {
					startSpeakers(discId);
					world.playEvent(1010, pos, discId);
				}
			}
		}

	}
	
}
