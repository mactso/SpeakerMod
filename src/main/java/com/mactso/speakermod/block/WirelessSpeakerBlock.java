package com.mactso.speakermod.block;


import java.util.StringTokenizer;

import com.mactso.speakermod.blockentities.WirelessJukeboxBlockEntity;
import com.mactso.speakermod.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessSpeakerBlock extends Block {

	
	public WirelessSpeakerBlock(Properties builder) {
		super(builder);
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
	    return MyConfig.getSpeakerLightLevel();
	}

    @Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		
		if (worldIn.isClientSide()) {
			return;
		}
		BlockPos jukePos = null;
		String jukeLinkedPos = stack.getHoverName().getString().toString();
		
		if (!(jukeLinkedPos.substring(0,1).equals("("))) {
			worldIn.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.6f, 0.3f);
			return;
		}
		// Trim leading and trailing parenthesis
		jukeLinkedPos = jukeLinkedPos.substring(0,jukeLinkedPos.length()-1);
		jukeLinkedPos = jukeLinkedPos.substring(1,jukeLinkedPos.length());
		
		StringTokenizer st = new StringTokenizer(jukeLinkedPos,",");
		try {
			String sX = st.nextToken().trim();
			String sY = st.nextToken().trim();
			String sZ = st.nextToken().trim();
			jukePos = new BlockPos (Integer.parseInt(sX),Integer.parseInt(sY),Integer.parseInt(sZ));
		} catch (Exception e) {
			// should never happen
		}

		BlockEntity r = worldIn.getBlockEntity(jukePos);
		if ((r instanceof WirelessJukeboxBlockEntity) && (calcDistance (jukePos, pos) < 8125.0d) && ((WirelessJukeboxBlockEntity) r).addSpeakerPos(pos)) {
			worldIn.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 0.6f, 0.2f);
		} else {
			worldIn.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.6f, 0.3f);
		}
		
	}
	
	
	
	public double calcDistance(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX() - pos2.getX();
        double d1 = pos1.getY() - pos2.getY();
        double d2 = pos1.getZ() - pos2.getZ();
        double d = d0 * d0 + d1 * d1 + d2 * d2;
        return d;
	}
	
	
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	    if (!state.is(newState.getBlock())) {
	    	worldIn.levelEvent(1010, pos, 0);
	    	super.onRemove(state, worldIn, pos, newState, isMoving);
	    }
	}


	
}
