package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.BevelCogWheelAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsBlock;

public class SingleAxisBevelCogWheelBlock extends SimpleBevelCogWheelBlock implements IBE<KineticBlockEntity> {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SingleAxisBevelCogWheelBlock.Type> TYPE = EnumProperty.create("type", SingleAxisBevelCogWheelBlock.Type.class);

    private final Map<Direction, BlockState> faces = new EnumMap<>(Direction.class);

    public SingleAxisBevelCogWheelBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
        registerDefaultState(defaultBlockState()
            .setValue(AXIS, Axis.Y)
            .setValue(TYPE, SingleAxisBevelCogWheelBlock.Type.BOTTOM)
        );
    };

    public final BlockState get(Direction face) {
        return faces.computeIfAbsent(face, f -> defaultBlockState()
            .setValue(AXIS, f.getAxis())
            .setValue(TYPE, f.getAxisDirection() == AxisDirection.POSITIVE ? SingleAxisBevelCogWheelBlock.Type.TOP : SingleAxisBevelCogWheelBlock.Type.BOTTOM)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS, TYPE));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        final List<BevelCogWheelPart> parts = new ArrayList<>(3);
        final Axis axis = state.getValue(AXIS);
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (type.hasTopCog()) parts.add(getSet().cogParts().get(Direction.get(AxisDirection.POSITIVE, axis)));
        if (type.hasBottomCog()) parts.add(getSet().cogParts().get(Direction.get(AxisDirection.NEGATIVE, axis)));
        if (type.hasShaft()) parts.add(getSet().shaftParts().get(axis));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Axis axis = state.getValue(AXIS);
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);

        return switch (part) {
            case BevelCogWheelPart.Cog cog -> {
                if (type == SingleAxisBevelCogWheelBlock.Type.TOP || type == SingleAxisBevelCogWheelBlock.Type.BOTTOM) yield Blocks.AIR.defaultBlockState();
                else if (type == SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT || type == SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT) yield getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, axis);
                else if (cog.isTop()) yield state.setValue(TYPE, SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT);
                else yield state.setValue(TYPE, Type.TOP_SHAFT);
            } case BevelCogWheelPart.Shaft shaft -> {
                if (type == SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT) yield state.setValue(TYPE, SingleAxisBevelCogWheelBlock.Type.TOP);
                else if (type == SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT) yield state.setValue(TYPE, SingleAxisBevelCogWheelBlock.Type.BOTTOM);
                else if (type == SingleAxisBevelCogWheelBlock.Type.BOTH) yield getSet().oppositesBlock().getDefaultState().setValue(OppositeBevelCogWheelsBlock.AXIS, axis);
                else throw new IllegalArgumentException("No shaft to remove");
            }
        };
    };

    @Override
    @Nullable
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (type == SingleAxisBevelCogWheelBlock.Type.BOTH) return null; // Can never add anything
        final Axis axis = state.getValue(AXIS);

        return switch (part) {
            case BevelCogWheelPart.Cog cog -> {
                if (cog.face.getAxis() == axis) {
                    if (cog.isTop() == type.hasTopCog()) yield null;
                    if (type.hasShaft()) yield state.setValue(TYPE, SingleAxisBevelCogWheelBlock.Type.BOTH);
                    yield getSet().oppositesBlock().getDefaultState()
                        .setValue(OppositeBevelCogWheelsBlock.AXIS, axis)
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                } else {
                    yield getSet().cornerBlock().getDefaultState()
                        .setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.fromTopAndFront(cog.face, Direction.get(type.hasTopCog() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis)).asEdge())
                        .setValue(CornerBevelCogWheelsBlock.SHAFT, type.hasShaft()
                            ? axis.ordinal() < cog.face.ordinal()
                                ? CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS
                                : CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS
                            : CornerBevelCogWheelsBlock.ShaftType.NONE
                        ).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                }
            } case BevelCogWheelPart.Shaft shaft -> {
                if (type.hasShaft()) yield null;
                if (shaft.axis == axis) {
                    yield state.setValue(TYPE, type.hasTopCog() ? SingleAxisBevelCogWheelBlock.Type.TOP_SHAFT : SingleAxisBevelCogWheelBlock.Type.BOTTOM_SHAFT);
                } else {
                    yield getSet().singleAndShaftBlock().getDefaultState()
                        .setValue(BevelCogWheelAndShaftBlock.FACING, Direction.get(type.hasTopCog() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis))
                        .setValue(BevelCogWheelAndShaftBlock.SHAFT_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(axis, shaft.axis))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                }
            }
        };
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return state.getValue(TYPE).hasShaft() ? state.getValue(AXIS) : null;
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
        /**
         * Both cogs AND the Shaft connecting them
         */
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

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return getSet().singleAxisBE().get();
    };

    public static final <B extends SingleAxisBevelCogWheelBlock> void blockState(DataGenContext<Block, B> ctx, RegistrateBlockstateProvider prov) {
        final MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

        final ModelFile cog = prov.models().getExistingFile(ctx.get().getSet().id().withPrefix("block/").withSuffix("/four_teeth"));
        final ModelFile shaft = new UncheckedModelFile(ctx.get().getSet().shaftBlock().getId().withPrefix("block/"));

        for (final Axis axis : Iterate.axes) {

            final int rotX = axis == Axis.Y ? 0 : 90;
            final int rotY = axis == Axis.X ? 90 : axis == Axis.Y ? 0 : 180;

            builder
                .part()
                    .modelFile(shaft)
                    .rotationX(rotX)
                    .rotationY(rotY)
                    .addModel()
                    .condition(AXIS, axis)
                    .condition(TYPE, Type.TOP_SHAFT, Type.BOTTOM_SHAFT, Type.BOTH)
                .end()
                .part()
                    .modelFile(cog)
                    .rotationX(rotX)
                    .rotationY(rotY)
                    .addModel()
                    .condition(AXIS, axis)
                    .condition(TYPE, Type.BOTTOM, Type.BOTTOM_SHAFT, Type.BOTH)
                .end()
                .part()
                    .modelFile(cog)
                    .rotationX(rotX + 180)
                    .rotationY(rotY)
                    .addModel()
                    .condition(AXIS, axis)
                    .condition(TYPE, Type.TOP, Type.TOP_SHAFT, Type.BOTH)
                .end();
        };
    };
    
};
