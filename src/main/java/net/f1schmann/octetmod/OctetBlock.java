package net.f1schmann.octetmod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;


public class OctetBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty COMPOSITION = IntegerProperty.create("composition", 0, 255);

    public OctetBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState( getStateDefinition().any().setValue(COMPOSITION, 0) );
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND)
            pLevel.setBlock(pPos, pState.setValue(COMPOSITION, pState.getValue(COMPOSITION)+1%256), 3);
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(COMPOSITION);
    }
}
