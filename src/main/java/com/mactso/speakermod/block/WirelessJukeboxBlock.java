package com.mactso.speakermod.block;

import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.item.ModItems;
import com.mactso.speakermod.tileentity.WirelessJukeboxTileEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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
			Item item = stack.getItem();
	
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
    				worldIn.playEvent((PlayerEntity)null, 1010, pos, discId);
    				wJTE.startSpeakers(discId);
    				System.out.println("Play");
    			} else {
    				wJTE.stopSpeakers ();
    		    	worldIn.playEvent(1010, pos, 0);
    				System.out.println("Silence");
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
			    	worldIn.playEvent(1010, pos, 0);
	            	WirelessJukeboxTileEntity wJTE = (WirelessJukeboxTileEntity)tileentity;	            	
	    			wJTE.stopSpeakers ();
	            }
	        }
	    	super.onReplaced(state, worldIn, pos, newState, isMoving);
	    }
	    
	 }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
// from jukeboxblock	      CompoundNBT compoundnbt = stack.getOrCreateTag();
		worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 0.5f, 0.2f);
		
	}
	
}
