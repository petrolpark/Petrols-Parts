package com.petrolpark.petrolsparts.content.kinetics.differential;

import com.petrolpark.compat.create.core.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DifferentialBlock extends CogWheelBlock {

    public static final BooleanProperty FULL_MODEL = BooleanProperty.create("full_model");

    public DifferentialBlock(Properties properties) {
        super(true, properties);
        registerDefaultState(defaultBlockState().setValue(FULL_MODEL, true));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION);
        builder.add(FULL_MODEL);
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FULL_MODEL, false);
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(FULL_MODEL)) level.setBlockAndUpdate(pos, state.setValue(FULL_MODEL, false));
        super.onPlace(state, level, pos, oldState, isMoving);
    };

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        AbstractRememberPlacerBehaviour.setPlacedBy(worldIn, pos, placer);
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    };

    @Override
    @SuppressWarnings("null")
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        withBlockEntityDo(level, pos, be -> {
            if (!(be instanceof DifferentialBlockEntity diff) || !diff.hasLevel()) return;
            Direction face = DirectionalRotatedPillarKineticBlock.getDirection(state);
            Direction between = KineticsHelper.directionBetween(pos, neighbor);
            if (between == face || between == face.getOpposite()) {
                diff.updateGeneratedRotation();
            }
        });
        super.onNeighborChange(state, level, pos, neighbor);
    };

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        withBlockEntityDo(level, pos, be -> {
            if (be instanceof DifferentialBlockEntity diff) diff.updateGeneratedRotation();
        });
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    };

    @Override
    public boolean isLargeCog() {
        return true;
    };

    @Override
    public boolean isDedicatedCogWheel() {
        return true;
    };

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.DIFFERENTIAL.get();
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

};
