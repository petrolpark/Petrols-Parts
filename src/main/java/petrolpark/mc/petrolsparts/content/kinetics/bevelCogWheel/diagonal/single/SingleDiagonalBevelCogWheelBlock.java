package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;
import petrolpark.mc.library.compat.create.core.world.block.MultiPartKineticBlock;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.DiagonalBevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.IDualDiagonalBevelCogWheelBlock;

public class SingleDiagonalBevelCogWheelBlock extends MultiPartKineticBlock<DiagonalBevelCogWheelPart> implements ISingleDiagonalBevelCogWheelBlock, IReplaceableBlock, ProperWaterloggedBlock, EncasableBlock {

    public SingleDiagonalBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(ORIENTATION, Orientation.UP_SOUTH)
            .setValue(FIRST_AXIS_SHAFT, false)
            .setValue(SECOND_AXIS_SHAFT, false)
            .setValue(WATERLOGGED, false)
        );
    };

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, FIRST_AXIS_SHAFT, SECOND_AXIS_SHAFT, WATERLOGGED));
    };

    @Override
    public boolean canSurviveWithout(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid, DiagonalBevelCogWheelPart part) {
        return part instanceof DiagonalBevelCogWheelPart.ShaftHalf;
    };

    @Override
    public Collection<DiagonalBevelCogWheelPart> getParts(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final List<DiagonalBevelCogWheelPart> parts = new ArrayList<>(3);
        parts.add(DiagonalBevelCogWheelPart.COGS.get(orientation));
        if (state.getValue(FIRST_AXIS_SHAFT)) parts.add(DiagonalBevelCogWheelPart.SHAFT_HALVES.get(orientation.top.getOpposite()));
        if (state.getValue(SECOND_AXIS_SHAFT)) parts.add(DiagonalBevelCogWheelPart.SHAFT_HALVES.get(orientation.front.getOpposite()));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, DiagonalBevelCogWheelPart part) {
        if (!(part instanceof DiagonalBevelCogWheelPart.ShaftHalf shaft)) return state;
        return state.setValue(state.getValue(ORIENTATION).top == shaft.face.getOpposite() ? FIRST_AXIS_SHAFT : SECOND_AXIS_SHAFT, false);
    };

    @Override
    public BlockState getReplacedState(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        if (existingState.getBlock() instanceof SingleDiagonalBevelCogWheelBlock) {
            // Place Assemblage Shaft Half on this
            final BlockState stateWithShaftHalf = getReplacedWithAssemblageShaftHalf(existingState, newState);
            if (stateWithShaftHalf != null) return stateWithShaftHalf;
            // Place two diagonal Bevel Cogwheels together
            if (newState.getBlock() instanceof SingleDiagonalBevelCogWheelBlock) {
                final Orientation existingOrientation = existingState.getValue(ORIENTATION);
                final Orientation newOrientation = newState.getValue(ORIENTATION);
                if (existingOrientation.top == newOrientation.top.getOpposite() && existingOrientation.front == existingOrientation.front.getOpposite()) {
                    final List<Axis> axes = Stream.of(Axis.values()).collect(Collectors.toCollection(ArrayList::new));
                    axes.remove(existingOrientation.top.getAxis());
                    axes.remove(existingOrientation.front.getAxis());
                    return PetrolsPartsBlocks.DUAL_DIAGONAL_BEVEL_COGWHEEL.getDefaultState()
                        .setValue(IDualDiagonalBevelCogWheelBlock.EXCLUDED_AXIS, axes.get(0))
                        .setValue(IDualDiagonalBevelCogWheelBlock.FACE_PARITY, existingOrientation.top.getAxisDirection() == existingOrientation.front.getAxisDirection())
                        .setValue(WATERLOGGED, existingState.getValue(WATERLOGGED));
                };
            };
        } else if (newState.getBlock() instanceof SingleDiagonalBevelCogWheelBlock) {
            // Place this on existing Assemblage Shaft Half
            return getReplacedWithAssemblageShaftHalf(newState, existingState);
        };
        return null;
    };

    public static final BlockState getReplacedWithAssemblageShaftHalf(BlockState bevelCogWheelState, BlockState potentialAssemblageState) {
        final Orientation orientation = bevelCogWheelState.getValue(ORIENTATION);
        if (!(potentialAssemblageState.getBlock() instanceof AssemblageBlock assemblage)) return null;
        final Axis axis = potentialAssemblageState.getValue(IAssemblageBlock.AXIS);
        if (axis == orientation.right.getAxis()) return null;
        if (!potentialAssemblageState.getValue(IAssemblageBlock.TOP_COG).isNone() || !potentialAssemblageState.getValue(IAssemblageBlock.MIDDLE_COG).isNone() || !potentialAssemblageState.getValue(IAssemblageBlock.BOTTOM_COG).isNone()) return null;
        if (assemblage.hasTopShaft(potentialAssemblageState) == assemblage.hasBottomShaft(potentialAssemblageState)) return null;
        bevelCogWheelState.setValue(WATERLOGGED, bevelCogWheelState.getValue(WATERLOGGED) || potentialAssemblageState.getValue(WATERLOGGED));
        final boolean topShaft = assemblage.hasTopShaft(potentialAssemblageState); // top and bottom both being missing should be impossible as we have checked there are no cogs, and assemblages without anything at all are invalid
        if (axis == orientation.top.getAxis()) {
            if (topShaft == (orientation.top.getAxisDirection() == AxisDirection.NEGATIVE) && !bevelCogWheelState.getValue(FIRST_AXIS_SHAFT)) return bevelCogWheelState.setValue(FIRST_AXIS_SHAFT, true);
        } else { // Axis aligned front
            if (topShaft == (orientation.front.getAxisDirection() == AxisDirection.NEGATIVE) && !bevelCogWheelState.getValue(SECOND_AXIS_SHAFT)) return bevelCogWheelState.setValue(SECOND_AXIS_SHAFT, true);
        };
        return null;
    };

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return placeCogOrEncase(stack, state, level, pos, player, hand, hitResult);
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState assemblageState) {
        return false;
    };

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
    public String getDescriptionId() {
        return TRANSLATION_KEY;
    };

    @Override
    public Item asItem() {
        return PetrolsPartsItems.BEVEL_COGWHEEL.get();
    };

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return ISingleDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return ISingleDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };
    
};
