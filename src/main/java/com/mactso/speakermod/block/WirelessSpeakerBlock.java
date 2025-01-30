package com.mactso.speakermod.block;


import java.util.StringTokenizer;

import com.mactso.speakermod.blockentities.WirelessJukeboxBlockEntity;
import com.mactso.speakermod.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WirelessSpeakerBlock extends Block {

	
	public WirelessSpeakerBlock(Properties builder) {
		super(builder);
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
	    return MyConfig.getSpeakerLightLevel();
	}

	
	@Override  // TODO return a null here to block placement.
	public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
		// TODO Auto-generated method stub
		return super.getStateForPlacement(p_49820_);
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

		BlockEntity wJBE = worldIn.getBlockEntity(jukePos); // TODO rename "r" to longer name
		if ((wJBE instanceof WirelessJukeboxBlockEntity) && (isSpeakerAudible(jukePos, pos)) && ((WirelessJukeboxBlockEntity) wJBE).addSpeakerPos(pos)) {
			worldIn.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 0.6f, 0.2f);
		} else {
			worldIn.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.6f, 0.3f);
		}
		
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack p_330929_, BlockState p_335716_, Level p_336112_,
			BlockPos p_328869_, Player p_332840_, InteractionHand p_336117_, BlockHitResult p_332723_) {
		// TODO Auto-generated method stub
		return super.useItemOn(p_330929_, p_335716_, p_336112_, p_328869_, p_332840_, p_336117_, p_332723_);
	}
	
	// note: it looks like if the player can't hear a sound, then the client won't start it.
	public boolean isSpeakerAudible(BlockPos pos1, BlockPos pos2) {
		if (calcDistance(pos1, pos2) > 5000) 
			return false;
		int x = 3;
		return true;
	}
	
	public double calcDistance(BlockPos pos1, BlockPos pos2) {
        double dx = pos1.getX() - pos2.getX();
        double dy = pos1.getY() - pos2.getY();
        double dz = pos1.getZ() - pos2.getZ();
        double d = dx * dx + dy * dy + dz * dz;
        double dsq = Math.sqrt(d);
        return d;
	}
	
	
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	    if (!state.is(newState.getBlock())) {
	    	worldIn.levelEvent(1011, pos, 0);   
	    	super.onRemove(state, worldIn, pos, newState, isMoving);
	    }
	}


	
}
