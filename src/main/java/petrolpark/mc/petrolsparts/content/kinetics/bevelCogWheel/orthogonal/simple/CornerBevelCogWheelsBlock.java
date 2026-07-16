package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class CornerBevelCogWheelsBlock extends SimpleBevelCogWheelBlock implements IBE<SimpleBevelCogWheelBlockEntity> {
    
    public static final EnumProperty<Orientation> ORIENTATION = Orientation.EDGE_ORIENTATION_PROPERTY;
    public static final EnumProperty<CornerBevelCogWheelsBlock.Shaft> SHAFT = EnumProperty.create("shaft", CornerBevelCogWheelsBlock.Shaft.class);

    public CornerBevelCogWheelsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(SHAFT, Shaft.NONE)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final CornerBevelCogWheelsBlock.Shaft shaft = state.getValue(SHAFT);
        final List<BevelCogWheelPart> parts = new ArrayList<>(3);
        parts.add(BevelCogWheelPart.COGS.get(orientation.top));
        parts.add(BevelCogWheelPart.COGS.get(orientation.front));
        if (shaft != Shaft.NONE) parts.add(BevelCogWheelPart.SHAFTS.get(shaft == Shaft.FIRST_AXIS ? orientation.top.getAxis() : orientation.front.getAxis()));
        return parts;
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final Shaft shaft = state.getValue(SHAFT);
        if (part.isCog) {
            if (part == BevelCogWheelPart.COGS.get(orientation.top.getOpposite()) && shaft != Shaft.FIRST_AXIS) {
                return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState()
                    .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.front.getOpposite())
                    .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.top.getAxis() == Axis.X)
                    .setValue(ThreeBevelCogWheelsBlock.SHAFT, shaft != Shaft.NONE)
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
            } else if (part == BevelCogWheelPart.COGS.get(orientation.front.getOpposite()) && shaft != Shaft.SECOND_AXIS) {
                return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState()
                    .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.top.getOpposite())
                    .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.front.getAxis() == Axis.Y)
                    .setValue(ThreeBevelCogWheelsBlock.SHAFT, shaft != Shaft.NONE)
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
            } else return null;
        } else { // Shafts
            if (shaft != Shaft.NONE) return null;
            if (part.axis == orientation.top.getAxis()) {
                return state.setValue(SHAFT, Shaft.FIRST_AXIS);
            } else if (part.axis == orientation.front.getAxis()) {
                return state.setValue(SHAFT, Shaft.SECOND_AXIS);
            } else {
                return BlockHelper.copyAll(PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS_AND_SHAFT.getDefaultState(), state);
            }
        }
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final Shaft shaft = state.getValue(SHAFT);
        if (part.isCog) {
            if (part.axis == orientation.top.getAxis()) {
                if (shaft == Shaft.FIRST_AXIS) {
                    //TODO single wheel and perp. shaft
                    return null;
                } else {
                    return PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL.getDefaultState()
                        .setValue(SingleAxisBevelCogWheelBlock.AXIS, orientation.front.getAxis())
                        .setValue(SingleAxisBevelCogWheelBlock.TYPE, shaft == Shaft.NONE
                            ? orientation.front.getAxisDirection() == AxisDirection.POSITIVE
                                ? SingleAxisBevelCogWheelBlock.Type.TOP
                                : SingleAxisBevelCogWheelBlock.Type.BOTTOM
                            : orientation.front.getAxisDirection() == AxisDirection.POSITIVE
                                ? SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT
                                : SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT
                        );
                }
            } else { // Secondary face (orientation.top)
                if (shaft == Shaft.SECOND_AXIS) {
                    //TODO
                    return null;
                } else {
                    return PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL.getDefaultState()
                        .setValue(SingleAxisBevelCogWheelBlock.AXIS, orientation.top.getAxis())
                        .setValue(SingleAxisBevelCogWheelBlock.TYPE, shaft == Shaft.NONE
                            ? orientation.top.getAxisDirection() == AxisDirection.POSITIVE
                                ? SingleAxisBevelCogWheelBlock.Type.TOP
                                : SingleAxisBevelCogWheelBlock.Type.BOTTOM
                            : orientation.top.getAxisDirection() == AxisDirection.POSITIVE
                                ? SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT
                                : SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT
                        );
                }
            }
        } else { // Shaft
            return state.setValue(SHAFT, Shaft.NONE);
        }
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

    public enum Shaft implements StringRepresentable {

        NONE,
        FIRST_AXIS,
        SECOND_AXIS;

        private String name;

        Shaft() {
            name = Lang.asId(name());
        };
        
        @Override
        public String getSerializedName() {
            return name;
        };

        public Shaft inverted(boolean inverted) {
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
        return PetrolsPartsBlockEntityTypes.SIMPLE_BEVEL_COGWHEEL.get();
    };
};
