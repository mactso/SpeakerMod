package com.mactso.speakermod.block;

import java.util.Optional;

import javax.annotation.Nullable;

import com.mactso.speakermod.blockentities.WirelessJukeboxBlockEntity;
import com.mactso.speakermod.config.MyConfig;
import com.mactso.speakermod.init.BlockEntityInit;
import com.mactso.speakermod.init.BlockInit;
import com.mactso.speakermod.utilities.Utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WirelessJukeboxBlock extends JukeboxBlock {

	public WirelessJukeboxBlock(BlockBehaviour.Properties properties) {

		super(properties);

	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {

		return BlockEntityInit.WIRELESS_JUKEBOX.get().create(pos, blockState);
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {

		return MyConfig.getJukeboxLightLevel();
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
			BlockEntityType<T> blockEntityType) {

		return level.isClientSide ? null
				: createTickerHelper(blockEntityType, BlockEntityInit.WIRELESS_JUKEBOX.get(),
						WirelessJukeboxBlockEntity::serverTick);

	}

	
	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand handIn, BlockHitResult hit) {

		

		if (level.isClientSide())
			return ItemInteractionResult.SUCCESS;

		if (stack.getItem() == BlockInit.WIRELESS_SPEAKER.get().asItem()) {
			
			ServerLevel sLevel = (ServerLevel) level;
			String jukeboxPos = "( " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " )";
			stack.set(DataComponents.CUSTOM_NAME, Component.literal(jukeboxPos));
			sLevel.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 0.6f, 0.6f);
			return ItemInteractionResult.SUCCESS;
		}


		ServerLevel sLevel = (ServerLevel) level;
		BlockEntity tileentity = sLevel.getBlockEntity(pos);
		if (!(tileentity instanceof WirelessJukeboxBlockEntity))
			return ItemInteractionResult.SUCCESS;
		
        WirelessJukeboxBlockEntity wJTE = (WirelessJukeboxBlockEntity) tileentity;
        if ((stack.isEmpty()) && player.isShiftKeyDown()) {
			Utility.debugMsg(1, pos, "Stop playing Disc " + wJTE.getSongId() + " and forget it.");
			wJTE.stopWirelessJukebox(sLevel);
			wJTE.forgetSong();
			return ItemInteractionResult.SUCCESS;
		}


        if (stack.isEmpty()) {
			wJTE.stopWirelessJukebox(sLevel);
			return ItemInteractionResult.SUCCESS;
        }
        
        Optional<Holder<JukeboxSong>> optional = JukeboxSong.fromStack(sLevel.registryAccess(), stack);
        if (optional.isEmpty()) { 
			wJTE.stopWirelessJukebox(sLevel);
			return ItemInteractionResult.SUCCESS;
        }

        Holder<JukeboxSong> jsh = optional.get();
        wJTE.startWirelessJukeBox(sLevel, jsh);
		return ItemInteractionResult.SUCCESS;

	}        
        
        

	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (!worldIn.isClientSide) {
				BlockEntity blockEntity = worldIn.getBlockEntity(pos);
				if (blockEntity instanceof WirelessJukeboxBlockEntity) {
					WirelessJukeboxBlockEntity wJTE = (WirelessJukeboxBlockEntity) blockEntity;
					wJTE.stopWirelessJukebox((ServerLevel) worldIn);
					wJTE.stopWirelessSpeakers((ServerLevel) worldIn);
				}
			}
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}

	}

}
