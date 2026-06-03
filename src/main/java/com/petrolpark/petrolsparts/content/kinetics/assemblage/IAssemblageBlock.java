package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.petrolpark.compat.create.core.block.composite.ICompositeKineticBlock;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.petrolsparts.core.block.IFaceAlignedCogWheelBlock;
import com.petrolpark.petrolsparts.core.block.IStateDependentCogWheelBlock;
import com.petrolpark.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;
import com.simibubi.create.api.contraption.transformable.TransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IAssemblageBlock extends IStateDependentCogWheelBlock, IFaceAlignedCogWheelBlock, ICompositeKineticBlock, TransformableBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty TOP_SHAFT_HALF = BooleanProperty.create("top_shaft_half");
    public static final BooleanProperty BOTTOM_SHAFT_HALF = BooleanProperty.create("bottom_half_shaft");
    public static final EnumProperty<AssemblageCog> TOP_COG = EnumProperty.create("top_cog", AssemblageCog.class);
    public static final EnumProperty<AssemblageCog> MIDDLE_COG = EnumProperty.create("middle_cog", AssemblageCog.class);
    public static final EnumProperty<AssemblageCog> BOTTOM_COG = EnumProperty.create("bottom_cog", AssemblageCog.class);

    public static final Collection<BooleanProperty> SHAFT_HALF_PROPERTIES = List.of(TOP_SHAFT_HALF, BOTTOM_SHAFT_HALF);
    public static final Collection<EnumProperty<AssemblageCog>> COG_PROPERTIES = List.of(TOP_COG, MIDDLE_COG, BOTTOM_COG);
    
    public boolean hasTopShaft(BlockState state);

    public boolean hasBottomShaft(BlockState state);

    @OnlyIn(Dist.CLIENT)
    public AssemblageBlockEntityPart getTargetedKineticPart(AssemblageBlockEntity be, Player player);

    @Override
    public default CogType getCogType(BlockState state) {
        return state.getValue(MIDDLE_COG).getCogType();
    };

    public default boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        final Axis axis = state.getValue(AXIS);
        if (!state.getValue(TOP_COG).isNone() && !IFaceAlignedCogWheelBlockEntity.isValidFaceAlignedCogwheelPosition(state.getValue(TOP_COG).getCogType().isLarge(), level, pos, Direction.get(AxisDirection.POSITIVE, axis))) return false;
        if (!state.getValue(BOTTOM_COG).isNone() && !IFaceAlignedCogWheelBlockEntity.isValidFaceAlignedCogwheelPosition(state.getValue(BOTTOM_COG).getCogType().isLarge(), level, pos, Direction.get(AxisDirection.NEGATIVE, axis))) return false;
        if (!state.getValue(MIDDLE_COG).isNone() && !CogWheelBlock.isValidCogwheelPosition(state.getValue(MIDDLE_COG).getCogType().isLarge(), level, pos, axis)) return false;
        return true;
    };

    @Override
    public default BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return rotate(originalState, targetedFace.getAxis(), Rotation.CLOCKWISE_90);
    };

    public static BlockState rotate(BlockState state, Axis axis, Rotation rotation) {
        Direction facing = Direction.fromAxisAndDirection(state.getValue(AXIS), AxisDirection.POSITIVE);
        for (int i = 0; i < rotation.ordinal(); i++) facing = facing.getClockWise(axis);
        if (facing.getAxis() == axis) return state;
        state = state.setValue(AXIS, facing.getAxis());
        if (facing.getAxisDirection() != AxisDirection.POSITIVE) state = invert(state);
        return state;
    };

    public static BlockState mirror(BlockState state, @Nullable Mirror mirror) {
        if ((state.getValue(AXIS) == Axis.Z && mirror == Mirror.LEFT_RIGHT) || (state.getValue(AXIS) == Axis.X && mirror == Mirror.FRONT_BACK)) return invert(state);
        return state;
    };

    public static BlockState invert(BlockState state) {
        state = state.setValue(TOP_COG, state.getValue(BOTTOM_COG)).setValue(BOTTOM_COG, state.getValue(TOP_COG));
        if (state.hasProperty(TOP_SHAFT_HALF)) state = state.setValue(TOP_SHAFT_HALF, state.getValue(BOTTOM_SHAFT_HALF)).setValue(BOTTOM_SHAFT_HALF, state.getValue(TOP_SHAFT_HALF));
        return state;
    };

    @Override
    public default BlockState transform(BlockState state, StructureTransform transform) {
        return rotate(mirror(state, transform.mirror), transform.rotationAxis, transform.rotation);
    };
};
