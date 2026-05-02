package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.petrolpark.compat.create.core.block.composite.ICompositeKineticBlock;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.petrolsparts.core.block.IStateDependentCogWheelBlock;
import com.simibubi.create.api.contraption.transformable.TransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IAssemblageBlock extends IStateDependentCogWheelBlock, ICompositeKineticBlock, TransformableBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty TOP_SHAFT_HALF = BooleanProperty.create("top_shaft_half");
    public static final BooleanProperty BOTTOM_SHAFT_HALF = BooleanProperty.create("bottom_half_shaft");
    public static final EnumProperty<AssemblageCog> TOP_COG = EnumProperty.create("top_gear", AssemblageCog.class);
    public static final EnumProperty<AssemblageCog> MIDDLE_COG = EnumProperty.create("middle_gear", AssemblageCog.class);
    public static final EnumProperty<AssemblageCog> BOTTOM_COG = EnumProperty.create("bottom_gear", AssemblageCog.class);

    public static final Collection<EnumProperty<AssemblageCog>> COG_PROPERTIES = List.of(TOP_COG, MIDDLE_COG, BOTTOM_COG);
    
    public boolean hasTopShaft(BlockState state);

    public boolean hasBottomShaft(BlockState state);

    @Override
    public default CogType getCogType(BlockState state) {
        return state.getValue(MIDDLE_COG).getCogType();
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
