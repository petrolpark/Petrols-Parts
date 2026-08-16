package petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import petrolpark.mc.library.compat.create.core.world.block.multiPart.WaterloggedDirectionalMultiPartKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.IFaceAlignedCogWheelBlock;
import petrolpark.mc.petrolsparts.core.block.IStateDependentCogWheelBlock;

public class RedstoneTransmissionBlock extends WaterloggedDirectionalMultiPartKineticBlock<RedstoneTransmissionPart> implements IBE<RedstoneTransmissionBlockEntity>, IStateDependentCogWheelBlock, IFaceAlignedCogWheelBlock {

    public static final BooleanProperty LOWER_COG = BooleanProperty.create("lower_cog");
    public static final BooleanProperty MIDDLE_COG = BooleanProperty.create("middle_cog");
    public static final BooleanProperty UPPER_COG = BooleanProperty.create("upper_cog");

    public static final BooleanProperty UPPER_CONNECTION = BooleanProperty.create("upper_connection");
    public static final BooleanProperty LOWER_CONNECTION = BooleanProperty.create("lower_connection");

    public static final int getMaxTransmissionLength() {
        return 6;
    };

    public RedstoneTransmissionBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(LOWER_COG, false)
            .setValue(MIDDLE_COG, false)
            .setValue(UPPER_COG, false)
            .setValue(LOWER_CONNECTION, false)
            .setValue(UPPER_CONNECTION, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LOWER_COG, MIDDLE_COG, UPPER_COG, UPPER_CONNECTION, LOWER_CONNECTION));
    };

    public void update(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        int i = 1;
        final Direction facing = state.getValue(FACING);
        while (state.getValue(LOWER_CONNECTION)) {
            pos = pos.relative(facing.getOpposite());
            state = levelAccessor.getBlockState(pos);
            if (state.getBlock() != this || !state.hasProperty(UPPER_CONNECTION) || state.getValue(FACING) != facing) return; // Badly formatted states
            i++;
            if (i > getMaxTransmissionLength()) return;
        };
        //updateController(levelAccessor, pos, state);
    };

    public void updateController(LevelAccessor level, BlockPos pos, BlockState state) {
        final BlockPos originalPos = pos;
        final Direction facing = state.getValue(FACING);

        int power = 0;
        int length = 0;
        boolean[] cogs = new boolean[getMaxTransmissionLength() * 3];

        cogs[0] = state.getValue(LOWER_COG);
        cogs[1] = state.getValue(MIDDLE_COG);
        cogs[2] = state.getValue(UPPER_COG);

        while (state.getValue(UPPER_CONNECTION)) {
            length++;
            if (length > getMaxTransmissionLength()) return; // Too long, don't update
            
            pos = pos.relative(facing);
            state = level.getBlockState(pos);
            if (!state.hasProperty(LOWER_CONNECTION) || !state.getValue(LOWER_CONNECTION) || state.getValue(FACING) != facing) return; // Badly formatted states

            cogs[3 * length] = state.getValue(LOWER_COG);
            cogs[3 * length + 1] = state.getValue(MIDDLE_COG);
            cogs[3 * length + 2] = state.getValue(UPPER_COG);

            power = Math.max(power, level.getBestNeighborSignal(pos));

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
        final int displacement = power - currentOffset;
        if (displacement == 0) return; // Already in position

        final boolean[] newCogs = new boolean[cogs.length];
        System.arraycopy(cogs, currentOffset, newCogs, power, cogs.length - maxOffset);

        withBlockEntityDo(level, originalPos, be -> {

            be.cogs.clear();

            for (int j = 0; j < newCogs.length; j++) {
                final BlockPos changePos = originalPos.relative(facing, j);
                be.getLevel().setBlockAndUpdate(changePos, level.getBlockState(changePos)
                    .setValue(LOWER_COG, newCogs[3 * j])
                    .setValue(MIDDLE_COG, newCogs[3 * j + 1])
                    .setValue(UPPER_COG, newCogs[3 * j + 2])
                );

                be.cogs.set(j, newCogs[j]);
            };

            be.displacement = displacement;
        });
    };

    @Override
    public Collection<RedstoneTransmissionPart> getParts(BlockState state) {
        final Direction facing = state.getValue(FACING);
        final List<RedstoneTransmissionPart> parts = new ArrayList<>(4);
        parts.add(RedstoneTransmissionPart.SHAFTS.get(facing.getAxis()));
        if (state.getValue(UPPER_COG)) parts.add(RedstoneTransmissionPart.FACIAL_COGS.get(facing));
        if (state.getValue(MIDDLE_COG)) parts.add(RedstoneTransmissionPart.AXIAL_COGS.get(facing.getAxis()));
        if (state.getValue(LOWER_COG)) parts.add(RedstoneTransmissionPart.FACIAL_COGS.get(facing.getOpposite()));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, RedstoneTransmissionPart part) {
        final Direction facing = state.getValue(FACING);
        if (part.cog) {
            return state.setValue(part.place.map(dir -> dir == facing ? UPPER_COG : LOWER_COG, $ -> MIDDLE_COG), false);
        } else { // Remove casing - replace with Assemblage
            if (!state.getValue(LOWER_COG) && !state.getValue(MIDDLE_COG) && !state.getValue(UPPER_COG)) return Blocks.AIR.defaultBlockState();
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
        BlockState stateToPlace = withWater(defaultBlockState(), context); // With waterlogging

        final Player player = context.getPlayer();
        final boolean shiftDown = player != null && player.isShiftKeyDown();

        if (!shiftDown) {
            Direction preferredTransmissionFacing = null;
            boolean transmissionFacingUndecidable = false;
            int length = 0; // Length of existing Transmission
            Direction preferredShaftSide = null;
            boolean shaftSideUndecidable = false;

            for (Direction side : Iterate.directions) {
                final BlockPos pos = context.getClickedPos().relative(side);
                final BlockState state = context.getLevel().getBlockState(pos);

                // Check if there is another transmission to connect to
                if (state.getBlock() == this) {
                    final Direction facing = state.getValue(FACING);
                    if (facing.getAxis() != side.getAxis()) continue;

                    final BooleanProperty connectionProperty = facing == side ? UPPER_CONNECTION : LOWER_CONNECTION;

                    // Already a good direction to face - can't have multiple
                    if (preferredTransmissionFacing != null && preferredTransmissionFacing != facing) { 
                        if (existingLength(context.getLevel(), pos, facing, side) == 0) continue; // If it's too long we can't connect in that direction anyway
                        preferredTransmissionFacing = null;
                        transmissionFacingUndecidable = true;

                    // Preferred facing not yet decided
                    } else if (length == 0) { 
                        length = existingLength(context.getLevel(), pos, facing, side);
                        if (length == 0) continue; // Too long
                        stateToPlace = stateToPlace.setValue(connectionProperty, true);
                        preferredTransmissionFacing = facing;

                    // We are already connecting in the opposite direction
                    } else {
                        final int newLength = existingLength(context.getLevel(), pos, facing, side);
                        if (newLength == 0) continue; // Can't connect on that side (too long)
                        if (length + newLength + 1 > getMaxTransmissionLength()) { // Connecting to BOTH would be too long - prefer the clicked face
                            if (context.getClickedFace().getAxis() != facing.getAxis()) { // We didn't click on one of the two faces - give up
                                preferredTransmissionFacing = null;
                                transmissionFacingUndecidable = true;
                            } else if (context.getClickedFace() != side) { // Clicked on this face so connect to that
                                stateToPlace = stateToPlace.setValue(facing == side ? LOWER_CONNECTION : UPPER_CONNECTION, false); // Undo connection to other
                                stateToPlace = stateToPlace.setValue(connectionProperty, true);
                            };
                            // Otherwise, clicked on other face so keep connection to that and don't add this
                        } else { // Can connect to both and stay under length limit
                            stateToPlace = stateToPlace.setValue(connectionProperty, true);
                            break;
                        };
                    };
                    
                // Otherwise, fallback to checking if there is a shaft connection
                } else if (!shaftSideUndecidable && state.getBlock() instanceof IRotate rotate && rotate.hasShaftTowards(context.getLevel(), pos, state, side.getOpposite())) {
                    if (preferredShaftSide != null && preferredShaftSide.getAxis() != side.getAxis()) {
                        preferredShaftSide = null;
                        shaftSideUndecidable = true;
                    } else {
                        preferredShaftSide = side;
                    }
                };

                if (transmissionFacingUndecidable && shaftSideUndecidable) break;
            };

            // Prefer connecting to other Transmissions if possible
            if (preferredTransmissionFacing != null) return stateToPlace.setValue(FACING, preferredTransmissionFacing);

            // Otherwise, connect to a shaft if possible
            if (preferredShaftSide != null) return stateToPlace.setValue(FACING, preferredShaftSide);
        };

        // Defer to the player's facing direction
        return stateToPlace.setValue(FACING, shiftDown ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection());
    };

    /**
     * @param level
     * @param pos
     * @param facing
     * @param direction
     * @return length of existing Transmission, or {@code 0} if it is max length already
     */
    protected int existingLength(Level level, BlockPos pos, Direction facing, Direction direction) {
        BlockState state = level.getBlockState(pos);
        final BooleanProperty connectionProperty = direction == facing ? UPPER_CONNECTION : LOWER_CONNECTION;
        int length = 1;
        while (state.getValue(connectionProperty)) {
            length++;
            if (length >= getMaxTransmissionLength()) return 0;
            pos = pos.relative(direction);
            state = level.getBlockState(pos);
            if (state.getBlock() != this || state.getValue(FACING) != facing) break;
        };
        return length;
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        final Direction facing = state.getValue(FACING);
        state = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction.getAxis() == facing.getAxis()) {

            // Remove the connection if the connected block is missing
            final BooleanProperty connectionProperty = direction.getAxisDirection() == facing.getAxisDirection() ? UPPER_CONNECTION : LOWER_CONNECTION;
            final BooleanProperty oppositeConnectionProperty = direction.getAxisDirection() == facing.getAxisDirection() ? LOWER_CONNECTION : UPPER_CONNECTION;
            if (state.getValue(connectionProperty) && !(
                neighborState.getBlock() == this &&
                neighborState.getValue(FACING) == facing &&
                neighborState.getValue(oppositeConnectionProperty)
            )) {
                state = state.setValue(connectionProperty, false);
                update(level, pos, state);
            };

            // Add new connection if needed
            if (neighborState.getBlock() == this && 
                neighborState.getValue(FACING) == facing &&
                neighborState.getValue(oppositeConnectionProperty) &&
                !state.getValue(connectionProperty)
            ) {
                state = state.setValue(connectionProperty, true);
                update(level, pos, state);
            };
        };
        return state;
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
    public Class<RedstoneTransmissionBlockEntity> getBlockEntityClass() {
        return RedstoneTransmissionBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends RedstoneTransmissionBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.TRANSMISSION.get();
    };
    
};
