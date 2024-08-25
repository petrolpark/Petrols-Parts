package com.petrolpark.petrolsparts.content.differential;

import com.petrolpark.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DummyDifferentialBlock extends DirectionalRotatedPillarKineticBlock implements IBE<DummyDifferentialBlockEntity> {

    public DummyDifferentialBlock(Properties properties) {
        super(properties);
    };

    @Deprecated
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, PetrolsPartsBlocks.DUMMY_DIFFERENTIAL.get(), 2);
    };

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Player placer = getBlockEntity(level, pos).advancementBehaviour.getPlayer();
        level.setBlockAndUpdate(pos, PetrolsPartsBlocks.DIFFERENTIAL.getDefaultState().setValue(AXIS, state.getValue(AXIS)).setValue(POSITIVE_AXIS_DIRECTION, state.getValue(POSITIVE_AXIS_DIRECTION)));
        AbstractRememberPlacerBehaviour.setPlacedBy(level, pos, placer);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public Class<DummyDifferentialBlockEntity> getBlockEntityClass() {
        return DummyDifferentialBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends DummyDifferentialBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.DUMMY_DIFFERENTIAL.get();
    };
    
};
