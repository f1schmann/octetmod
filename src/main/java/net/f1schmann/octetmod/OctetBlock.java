package net.f1schmann.octetmod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;


public class OctetBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty COMPOSITION = IntegerProperty.create("composition", 1, 255);
    public static final VoxelShape[] VoxelOctets = makeVoxelOctets();
    private static final VoxelShape[] VoxelShapes = new VoxelShape[256];

    private static VoxelShape[] makeVoxelOctets() {
        VoxelShape[] voxelOctets = new VoxelShape[8];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                for(int k = 0; k < 2; k++)
                    voxelOctets[i*4+j*2+k] = Shapes.box(i/2., j/2., k/2., (i+1)/2., (j+1)/2., (k+1)/2.);
        return voxelOctets;
    }

    public static VoxelShape getVoxelShape(int index){
        if (VoxelShapes[index] == null){
            VoxelShape shape = Shapes.empty();
            for (int mask = 0; mask < 8; mask++)
                if ((index & (1<<mask)) != 0)
                    shape = Shapes.or(shape, VoxelOctets[mask]);
            VoxelShapes[index] = shape;
        }
        return VoxelShapes[index];
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getVoxelShape(pState.getValue(COMPOSITION));
    }

    public OctetBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState( getStateDefinition().any().setValue(COMPOSITION, 255) );
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND)
            pLevel.setBlock(pPos, pState.setValue(COMPOSITION, pState.getValue(COMPOSITION)%255+1), 3);
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(COMPOSITION);
    }
}
