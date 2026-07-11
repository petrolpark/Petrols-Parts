package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.createmod.catnip.lang.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class SingleAxisBevelCogWheelBlock extends SimpleBevelCogWheelBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SingleAxisBevelCogWheelBlock.Type> TYPE = EnumProperty.create("type", SingleAxisBevelCogWheelBlock.Type.class);

    public SingleAxisBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS, TYPE));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        final List<BevelCogWheelPart> parts = new ArrayList<>(3);
        final Axis axis = state.getValue(AXIS);
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (type.hasTopCog()) parts.add(BevelCogWheelPart.COGS.get(Direction.get(AxisDirection.POSITIVE, axis)));
        if (type.hasBottomCog()) parts.add(BevelCogWheelPart.COGS.get(Direction.get(AxisDirection.NEGATIVE, axis)));
        if (type.hasShaft()) parts.add(BevelCogWheelPart.SHAFTS.get(axis));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Axis axis = state.getValue(AXIS);
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (part.cog) {
            if (type == Type.TOP || type == Type.BOTTOM) return Blocks.AIR.defaultBlockState();
            else if (type == Type.TOP_SHAFT || type == Type.BOTTOM_SHAFT) return AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, axis);
            else if (part.isTopCog()) return state.setValue(TYPE, Type.BOTTOM_SHAFT);
            else return state.setValue(TYPE, Type.TOP_SHAFT);
        } else {
            if (type == Type.TOP_SHAFT) return state.setValue(TYPE, Type.TOP);
            else if (type == Type.BOTTOM_SHAFT) return state.setValue(TYPE, Type.BOTTOM);
            else if (type == Type.BOTH); //TODO independent
        };
        return state; // Unreachable
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        if (face.getAxis() != state.getValue(AXIS)) return false;
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (type.hasShaft()) return true;
        return face.getAxisDirection() == AxisDirection.POSITIVE == (type == Type.TOP);
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return newState.getValue(TYPE).hasShaft() == oldState.getValue(TYPE).hasShaft()
            && newState.getValue(AXIS) == oldState.getValue(AXIS);
    };

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, rotation, Mirror.NONE));
    };

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, Rotation.NONE, mirror));
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        final Direction facing = Direction.get(AxisDirection.POSITIVE, state.getValue(AXIS));
        final Direction newFacing = transform.rotateFacing(transform.mirrorFacing(facing));
        state = state.setValue(AXIS, newFacing.getAxis());
        if (newFacing.getAxisDirection() == AxisDirection.NEGATIVE)
            state = state.setValue(TYPE, switch (state.getValue(TYPE)) {
                case TOP -> Type.BOTTOM;
                case BOTTOM -> Type.TOP;
                case TOP_SHAFT -> Type.BOTTOM_SHAFT;
                case BOTTOM_SHAFT -> Type.TOP_SHAFT;
                case BOTH -> Type.BOTH;
            });
        return state;
    };

    public static enum Type implements StringRepresentable {
        TOP,
        BOTTOM,
        TOP_SHAFT,
        BOTTOM_SHAFT,
        BOTH;

        private final String name;

        Type() {
            name = Lang.asId(name());
        };

        @Override
        public String getSerializedName() {
            return name;
        };

        public boolean hasTopCog() {
            return this != BOTTOM && this != BOTTOM_SHAFT;
        };

        public boolean hasBottomCog() {
            return this != TOP && this != TOP_SHAFT;
        };

        public boolean hasShaft() {
            return this != TOP && this != BOTTOM;
        };
    };
    
};
