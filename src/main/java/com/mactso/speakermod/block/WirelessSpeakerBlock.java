package com.mactso.speakermod.block;

import com.ibm.icu.util.StringTokenizer;
import com.mactso.speakermod.tileentity.WirelessJukeboxTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WirelessSpeakerBlock extends Block {

	
	public WirelessSpeakerBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		
		if (worldIn.isRemote()) {
			return;
		}
		BlockPos jukePos = null;
		String jukeLinkedPos = stack.getDisplayName().getString().toString();
		
		if (!(jukeLinkedPos.substring(0,1).equals("("))) {
			worldIn.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 0.6f, 0.3f);
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

		TileEntity r = worldIn.getTileEntity(jukePos);
		if (r instanceof WirelessJukeboxTileEntity) {
			((WirelessJukeboxTileEntity) r).addSpeakerPos(pos);
			worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 0.6f, 0.2f);
		} else {
			worldIn.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 0.6f, 0.3f);
		}
		
	}
	
	
	
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	    if (!state.isIn(newState.getBlock())) {
	    	worldIn.playEvent(1010, pos, 0);
	    	super.onReplaced(state, worldIn, pos, newState, isMoving);
	    }
	    
	}

	
}