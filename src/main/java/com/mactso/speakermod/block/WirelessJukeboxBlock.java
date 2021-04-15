package com.mactso.speakermod.block;

import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.item.ModItems;
import com.mactso.speakermod.tileentity.WirelessJukeboxTileEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WirelessJukeboxBlock extends JukeboxBlock {

	public WirelessJukeboxBlock(AbstractBlock.Properties builder) {

    super(builder);
    
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		      return new WirelessJukeboxTileEntity();
	}
	
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
	
		if (!(worldIn.isRemote())) {
			ItemStack stack = player.getHeldItem(handIn);
	
			int discId= Item.getIdFromItem(stack.getItem());
	
			//		if ((worldIn instanceof ServerWorld)) { } plays both sides I think.
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof WirelessJukeboxTileEntity) {
            	WirelessJukeboxTileEntity wJTE = (WirelessJukeboxTileEntity)tileentity;	  
    			if (stack.getItem() == ModItems.WIRELESS_SPEAKER) {
    				String jukeboxPos = "( " + pos.getX() + ", "+ pos.getY() + ", "+ pos.getZ() + " )";
     				stack.setDisplayName(new StringTextComponent(jukeboxPos));
    				worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 0.6f, 0.6f);
    			} else if (stack.getItem() instanceof MusicDiscItem) {
	    			wJTE.playEvent((ServerWorld) worldIn, (PlayerEntity) null, 1010, pos, discId);
	    			wJTE.startSpeakers(discId);
	    			MyConfig.debugMsg(1, pos, "Start New Disc");
    			} else if ((stack.isEmpty()) && player.isSneaking()){
    				int saveId = wJTE.getDiscId();
    				wJTE.stopSpeakers ();
	            	wJTE.playEvent((ServerWorld) worldIn, (PlayerEntity) null, 1010, pos, 0);
    				MyConfig.debugMsg(1, pos, "Stop playing Disc");
    				wJTE.setDiscId(saveId);
    			} else {
    				wJTE.stopSpeakers ();
	            	wJTE.playEvent((ServerWorld) worldIn, (PlayerEntity) null, 1010, pos, 0);
    				MyConfig.debugMsg(1, pos, "Stop playing Disc");
    			}
            }
		}
	
		return ActionResultType.SUCCESS;
	}	

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	    if (!state.isIn(newState.getBlock())) {
	        if (!worldIn.isRemote) {
	            TileEntity tileentity = worldIn.getTileEntity(pos);
	            if (tileentity instanceof WirelessJukeboxTileEntity) {
	            	WirelessJukeboxTileEntity wJTE = (WirelessJukeboxTileEntity)tileentity;	
	            	wJTE.playEvent((ServerWorld) worldIn, (PlayerEntity) null, 1010, pos, 0);
	    			wJTE.stopSpeakers ();
	            }
	        }
	    	super.onReplaced(state, worldIn, pos, newState, isMoving);
	    }
	    
	 }
	
}
