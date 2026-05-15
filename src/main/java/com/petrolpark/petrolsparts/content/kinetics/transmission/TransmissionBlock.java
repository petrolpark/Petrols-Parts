package com.petrolpark.petrolsparts.content.kinetics.transmission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.petrolpark.compat.create.core.block.MultiPartKineticBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.petrolsparts.core.block.IStateDependentCogWheelBlock;
import com.petrolpark.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;

public class TransmissionBlock extends MultiPartKineticBlock<TransmissionPart> implements IStateDependentCogWheelBlock, IFaceAlignedCogWheelBlock, ProperWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty LOWER_COG = BooleanProperty.create("lower_cog");
    public static final BooleanProperty MIDDLE_COG = BooleanProperty.create("middle_cog");
    public static final BooleanProperty UPPER_COG = BooleanProperty.create("upper_cog");

    public static final BooleanProperty UPPER_CONNECTION = BooleanProperty.create("upper_connection");
    public static final BooleanProperty LOWER_CONNECTION = BooleanProperty.create("lower_connection");

    public static final int getMaxTransmissionLength() {
        return 6;
    };

    public TransmissionBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(FACING, Direction.NORTH)
            .setValue(LOWER_COG, false)
            .setValue(MIDDLE_COG, false)
            .setValue(UPPER_COG, false)
            .setValue(LOWER_CONNECTION, false)
            .setValue(UPPER_CONNECTION, false)
            .setValue(WATERLOGGED, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWER_COG, MIDDLE_COG, UPPER_COG, UPPER_CONNECTION, LOWER_CONNECTION, WATERLOGGED);
    };

    public void update(Level level, BlockPos pos, BlockState state) {
        int i = 1;
        final Direction facing = state.getValue(FACING);
        while (state.getValue(LOWER_CONNECTION)) {
            pos = pos.relative(facing.getOpposite());
            state = level.getBlockState(pos);
            if (!state.hasProperty(UPPER_CONNECTION) || !state.getValue(UPPER_CONNECTION) || state.getValue(FACING) != facing) return; // Badly formatted states
            i++;
            if (i > getMaxTransmissionLength()) return;
        };
        updateController(level, pos, state);
    };

    public void updateController(Level level, BlockPos pos, BlockState state) {
        final BlockPos originalPos = pos;
        final Direction facing = state.getValue(FACING);

        int power = 0;
        int length = 0;
        boolean[] cogs = new boolean[getMaxTransmissionLength() * 3];

        while (state.getValue(UPPER_CONNECTION)) {
            if (length > getMaxTransmissionLength()) return; // Too long, don't update
            
            pos = pos.relative(facing);
            state = level.getBlockState(pos);
            if (!state.hasProperty(LOWER_CONNECTION) || !state.getValue(LOWER_CONNECTION) || state.getValue(FACING) != facing) return; // Badly formatted states

            cogs[3 * length] = state.getValue(LOWER_COG);
            cogs[3 * length + 1] = state.getValue(MIDDLE_COG);
            cogs[3 * length + 2] = state.getValue(UPPER_COG);

            power = Math.max(power, level.getBestNeighborSignal(pos));

            length++;
        };
        cogs = Arrays.copyOfRange(cogs, 0, 3 * length);

        int currentOffset = -1;
        int lastCogIndex = -1;
        for (int cog = 0; cog < cogs.length; cog++) {
            if (cogs[cog]) {
                if (currentOffset == -1) currentOffset = cog;
                lastCogIndex = cog;
            };
        };
        if (currentOffset == -1) return; // No cogs to switch

        final int maxOffset = currentOffset + cogs.length - lastCogIndex - 1; // Maximum value 'first' can take
        power = Math.min(power, maxOffset);
        if (power == currentOffset) return; // Already in position

        final boolean[] newCogs = new boolean[cogs.length];
        System.arraycopy(cogs, currentOffset, newCogs, power, cogs.length - maxOffset);

        for (int j = 0; j < length; j++) {
            final BlockPos changePos = originalPos.relative(facing, j);
            level.setBlockAndUpdate(changePos, level.getBlockState(changePos)
                .setValue(LOWER_COG, newCogs[3 * j])
                .setValue(MIDDLE_COG, newCogs[3 * j + 1])
                .setValue(UPPER_COG, newCogs[3 * j + 2])
            );
        };
    };

    @Override
    public Collection<TransmissionPart> getParts(BlockState state) {
        final Direction facing = state.getValue(FACING);
        final List<TransmissionPart> parts = new ArrayList<>(4);
        parts.add(state.getValue(UPPER_CONNECTION)
            ? state.getValue(LOWER_CONNECTION)
                ? TransmissionPart.WHOLE_CASINGS.get(facing.getAxis())
                : TransmissionPart.END_CASINGS.get(facing.getOpposite())
            : state.getValue(LOWER_CONNECTION)
                ? TransmissionPart.END_CASINGS.get(facing)
                : TransmissionPart.MIDDLE_CASINGS.get(facing.getAxis())
        );
        if (state.getValue(UPPER_COG)) parts.add(TransmissionPart.FACIAL_COGS.get(facing));
        if (state.getValue(MIDDLE_COG)) parts.add(TransmissionPart.AXIAL_COGS.get(facing.getAxis()));
        if (state.getValue(LOWER_COG)) parts.add(TransmissionPart.FACIAL_COGS.get(facing.getOpposite()));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, TransmissionPart part) {
        final Direction facing = state.getValue(FACING);
        if (part.cog) {
            return state.setValue(part.place.map(dir -> dir == facing ? UPPER_COG : LOWER_COG, $ -> MIDDLE_COG), false);
        } else { // Remove casing - replace with Assemblage
            final BlockState assemblageState = PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.getDefaultState()
                .setValue(IAssemblageBlock.AXIS, facing.getAxis())
                .setValue(IAssemblageBlock.MIDDLE_COG, state.getValue(MIDDLE_COG) ? AssemblageCog.SMALL : AssemblageCog.NONE);
            if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
                return assemblageState.setValue(IAssemblageBlock.TOP_COG, state.getValue(UPPER_COG) ? AssemblageCog.SMALL : AssemblageCog.NONE)
                    .setValue(IAssemblageBlock.BOTTOM_COG, state.getValue(LOWER_COG) ? AssemblageCog.SMALL : AssemblageCog.NONE);
            } else {
                return assemblageState.setValue(IAssemblageBlock.BOTTOM_COG, state.getValue(UPPER_COG) ? AssemblageCog.SMALL : AssemblageCog.NONE)
                    .setValue(IAssemblageBlock.TOP_COG, state.getValue(LOWER_COG) ? AssemblageCog.SMALL : AssemblageCog.NONE);
            }
        }
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        final Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
        
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        if (oldState.getValue(FACING) == newState.getValue(FACING)) {
            if (oldState.getValue(UPPER_COG) != newState.getValue(UPPER_COG)) return false;
            if (oldState.getValue(LOWER_COG) != newState.getValue(LOWER_COG)) return false;
        } else if (oldState.getValue(FACING) == newState.getValue(FACING).getOpposite()) {
            if (oldState.getValue(UPPER_COG) != newState.getValue(LOWER_COG)) return false;
            if (oldState.getValue(LOWER_COG) != newState.getValue(UPPER_COG)) return false;
        } else return false;
        return oldState.getValue(MIDDLE_COG) == newState.getValue(MIDDLE_COG);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(FACING).getAxis();
    };

    @Override
    public CogType getCogType(BlockState state) {
        return CogType.small(state.getValue(MIDDLE_COG));
    };

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    };

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    };
    
};
