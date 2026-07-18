package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.block.IBE;

import net.createmod.catnip.lang.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.BevelCogWheelAndShaftBlock;

public class CornerBevelCogWheelsBlock extends SimpleBevelCogWheelBlock implements IBE<SimpleBevelCogWheelBlockEntity> {
    
    public static final EnumProperty<Orientation> ORIENTATION = Orientation.EDGE_ORIENTATION_PROPERTY;
    public static final EnumProperty<CornerBevelCogWheelsBlock.ShaftType> SHAFT = EnumProperty.create("shaft", CornerBevelCogWheelsBlock.ShaftType.class);

    public CornerBevelCogWheelsBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
        registerDefaultState(defaultBlockState()
            .setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.NONE)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, SHAFT));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final CornerBevelCogWheelsBlock.ShaftType shaft = state.getValue(SHAFT);
        final List<BevelCogWheelPart> parts = new ArrayList<>(3);
        parts.add(getSet().cogParts().get(orientation.top));
        parts.add(getSet().cogParts().get(orientation.front));
        if (shaft != CornerBevelCogWheelsBlock.ShaftType.NONE) parts.add(getSet().shaftParts().get(shaft == CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS ? orientation.top.getAxis() : orientation.front.getAxis()));
        return parts;
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final CornerBevelCogWheelsBlock.ShaftType shaftType = state.getValue(SHAFT);

        return switch (part) {
            case BevelCogWheelPart.Cog cog -> {
                if (part == getSet().cogParts().get(orientation.top.getOpposite()) && shaftType != CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS) {
                    yield getSet().threeBlock().getDefaultState()
                        .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.front.getOpposite())
                        .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.top.getAxis() == Axis.X)
                        .setValue(ThreeBevelCogWheelsBlock.SHAFT, shaftType != CornerBevelCogWheelsBlock.ShaftType.NONE)
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                } else if (part == getSet().cogParts().get(orientation.front.getOpposite()) && shaftType != CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS) {
                    yield getSet().threeBlock().getDefaultState()
                        .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.top.getOpposite())
                        .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.front.getAxis() == Axis.Y)
                        .setValue(ThreeBevelCogWheelsBlock.SHAFT, shaftType != CornerBevelCogWheelsBlock.ShaftType.NONE)
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                } else yield null;
            } case BevelCogWheelPart.Shaft shaft -> {
                if (shaftType != CornerBevelCogWheelsBlock.ShaftType.NONE) yield null;
                if (shaft.axis == orientation.top.getAxis()) {
                    yield state.setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS);
                } else if (shaft.axis == orientation.front.getAxis()) {
                    yield state.setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS);
                } else {
                    yield BlockHelper.copyAll(getSet().cornerAndShaftBlock().getDefaultState(), state);
                }
            }
        };
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final CornerBevelCogWheelsBlock.ShaftType shaftType = state.getValue(SHAFT);

        return (switch (part) {
            case BevelCogWheelPart.Cog cog -> {
                if (cog.face == orientation.top) {
                    if (shaftType == CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS) {
                        yield getSet().singleAndShaftBlock().getDefaultState()
                            .setValue(BevelCogWheelAndShaftBlock.FACING, orientation.front)
                            .setValue(BevelCogWheelAndShaftBlock.SHAFT_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(orientation.front.getAxis(), orientation.top.getAxis()));
                    } else {
                        yield getSet().singleAxisBlock().getDefaultState()
                            .setValue(SingleAxisBevelCogWheelBlock.AXIS, orientation.front.getAxis())
                            .setValue(SingleAxisBevelCogWheelBlock.TYPE, shaftType == CornerBevelCogWheelsBlock.ShaftType.NONE
                                ? orientation.front.getAxisDirection() == AxisDirection.POSITIVE
                                    ? SingleAxisBevelCogWheelBlock.Type.TOP
                                    : SingleAxisBevelCogWheelBlock.Type.BOTTOM
                                : orientation.front.getAxisDirection() == AxisDirection.POSITIVE
                                    ? SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT
                                    : SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT
                            );
                    }
                } else { // Secondary face (orientation.front)
                    if (shaftType == ShaftType.SECOND_AXIS) {
                        yield getSet().singleAndShaftBlock().getDefaultState()
                            .setValue(BevelCogWheelAndShaftBlock.FACING, orientation.top)
                            .setValue(BevelCogWheelAndShaftBlock.SHAFT_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(orientation.top.getAxis(), orientation.front.getAxis()));
                    } else {
                        yield getSet().singleAxisBlock().getDefaultState()
                            .setValue(SingleAxisBevelCogWheelBlock.AXIS, orientation.top.getAxis())
                            .setValue(SingleAxisBevelCogWheelBlock.TYPE, shaftType == CornerBevelCogWheelsBlock.ShaftType.NONE
                                ? orientation.top.getAxisDirection() == AxisDirection.POSITIVE
                                    ? SingleAxisBevelCogWheelBlock.Type.TOP
                                    : SingleAxisBevelCogWheelBlock.Type.BOTTOM
                                : orientation.top.getAxisDirection() == AxisDirection.POSITIVE
                                    ? SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT
                                    : SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT
                            );
                    }
                }
            } case BevelCogWheelPart.Shaft shaft -> {
                yield state.setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.NONE);
            }
        }).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        return switch (state.getValue(SHAFT)) {
            case NONE -> null;
            case FIRST_AXIS -> orientation.top.getAxis();
            case SECOND_AXIS -> orientation.front.getAxis();
        };
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(ORIENTATION).top.getAxis(); // Unused
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        final Orientation orientation = state.getValue(ORIENTATION);
        if (face == orientation.top || face == orientation.front) return true;
        return switch (state.getValue(SHAFT)) {
            case NONE -> false;
            case FIRST_AXIS -> face == orientation.top.getOpposite();
            case SECOND_AXIS -> face == orientation.front.getOpposite();
        };
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        final Orientation initialOrientation = state.getValue(ORIENTATION);
        final Orientation newOrientation = initialOrientation.mirror(transform.mirror).rotate(transform.rotationAxis, transform.rotation);
        return state.setValue(ORIENTATION, newOrientation.asEdge())
            .setValue(SHAFT, state.getValue(SHAFT).inverted(newOrientation != newOrientation.asEdge()));
    };

    public enum ShaftType implements StringRepresentable {

        NONE,
        FIRST_AXIS,
        SECOND_AXIS;

        private String name;

        ShaftType() {
            name = Lang.asId(name());
        };
        
        @Override
        public String getSerializedName() {
            return name;
        };

        public ShaftType inverted(boolean inverted) {
            if (!inverted || this == NONE) return this;
            return this == FIRST_AXIS ? SECOND_AXIS : FIRST_AXIS;
        };
    };

    @Override
    public Class<SimpleBevelCogWheelBlockEntity> getBlockEntityClass() {
        return SimpleBevelCogWheelBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends SimpleBevelCogWheelBlockEntity> getBlockEntityType() {
        return getSet().simpleBE().get();
    };
};
