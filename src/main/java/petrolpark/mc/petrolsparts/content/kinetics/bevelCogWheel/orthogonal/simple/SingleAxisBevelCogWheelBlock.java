package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
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
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsBlock;

public class SingleAxisBevelCogWheelBlock extends SimpleBevelCogWheelBlock implements IBE<KineticBlockEntity> {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SingleAxisBevelCogWheelBlock.Type> TYPE = EnumProperty.create("type", SingleAxisBevelCogWheelBlock.Type.class);

    private final Map<Direction, BlockState> faces = new EnumMap<>(Direction.class);

    public SingleAxisBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
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
        if (type.hasTopCog()) parts.add(BevelCogWheelPart.COGS.get(Direction.get(AxisDirection.POSITIVE, axis)));
        if (type.hasBottomCog()) parts.add(BevelCogWheelPart.COGS.get(Direction.get(AxisDirection.NEGATIVE, axis)));
        if (type.hasShaft()) parts.add(BevelCogWheelPart.SHAFTS.get(axis));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Axis axis = state.getValue(AXIS);
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (part.isCog) {
            if (type == Type.TOP || type == Type.BOTTOM) return Blocks.AIR.defaultBlockState();
            else if (type == Type.TOP_SHAFT || type == Type.BOTTOM_SHAFT) return AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, axis);
            else if (part.isTopCog()) return state.setValue(TYPE, Type.BOTTOM_SHAFT);
            else return state.setValue(TYPE, Type.TOP_SHAFT);
        } else {
            if (type == Type.TOP_SHAFT) return state.setValue(TYPE, Type.TOP);
            else if (type == Type.BOTTOM_SHAFT) return state.setValue(TYPE, Type.BOTTOM);
            else if (type == Type.BOTH) return PetrolsPartsBlocks.OPPOSITE_BEVEL_COGWHEELS.getDefaultState().setValue(OppositeBevelCogWheelsBlock.AXIS, axis);
        };
        return state; // Unreachable
    };

    @Override
    @Nullable
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        final SingleAxisBevelCogWheelBlock.Type type = state.getValue(TYPE);
        if (type == Type.BOTH) return null; // Can never add anything
        final Axis axis = state.getValue(AXIS);
        if (part.isCog) {
            if (part.axis == axis) {
                if (part.isTopCog() == type.hasTopCog()) return null;
                if (type.hasShaft()) return state.setValue(TYPE, Type.BOTH);
                return PetrolsPartsBlocks.OPPOSITE_BEVEL_COGWHEELS.getDefaultState().setValue(OppositeBevelCogWheelsBlock.AXIS, axis);
            } else {
                //TODO corners
            };
        } else { // Shaft
            if (type.hasShaft()) return null;
            if (part.axis == axis) {
                return state.setValue(TYPE, type == Type.TOP ? Type.TOP_SHAFT : Type.BOTTOM_SHAFT);
            };
            //TODO perpendicular shafts
        };
        return null;
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
        return PetrolsPartsBlockEntityTypes.SINGLE_AXIS_BEVEL_COGWHEEL.get();
    };

    public static final void blockState(DataGenContext<Block, SingleAxisBevelCogWheelBlock> ctx, RegistrateBlockstateProvider prov) {
        final MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

        final ModelFile cog = prov.models().getExistingFile(PetrolsParts.asResource("block/bevel_cogwheel/four_teeth"));
        final ModelFile shaft = prov.models().getExistingFile(Create.asResource("block/shaft"));

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
                    .modelFile(shaft)
                    .rotationX(rotX + 180)
                    .rotationY(rotY)
                    .addModel()
                    .condition(AXIS, axis)
                    .condition(TYPE, Type.TOP, Type.TOP_SHAFT, Type.BOTH)
                .end();
        };
    };
    
};
