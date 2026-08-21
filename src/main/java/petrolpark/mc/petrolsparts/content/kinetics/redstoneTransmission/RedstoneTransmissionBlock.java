package petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.animation.LerpedFloat.Chaser;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
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
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;
import petrolpark.mc.library.compat.create.core.world.block.multiPart.WaterloggedDirectionalMultiPartKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsConfigs;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageSet;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.SeparateShaftHalvesAssemblageBlock;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.IFaceAlignedCogWheelBlock;
import petrolpark.mc.petrolsparts.core.block.IStateDependentCogWheelBlock;

public class RedstoneTransmissionBlock extends WaterloggedDirectionalMultiPartKineticBlock<RedstoneTransmissionPart> implements IBE<RedstoneTransmissionBlockEntity>, IReplaceableBlock, IStateDependentCogWheelBlock, IFaceAlignedCogWheelBlock {

    public static final BooleanProperty LOWER_COG = BooleanProperty.create("lower_cog");
    public static final BooleanProperty MIDDLE_COG = BooleanProperty.create("middle_cog");
    public static final BooleanProperty UPPER_COG = BooleanProperty.create("upper_cog");

    public static final BooleanProperty UPPER_CONNECTION = BooleanProperty.create("upper_connection");
    public static final BooleanProperty LOWER_CONNECTION = BooleanProperty.create("lower_connection");

    public static final int getMaxTransmissionLength() {
        return PetrolsPartsConfigs.server().redstoneTransmissionMaxLength.get();
    };

    public final int placementHelperId;

    public RedstoneTransmissionBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(LOWER_COG, false)
            .setValue(MIDDLE_COG, false)
            .setValue(UPPER_COG, false)
            .setValue(LOWER_CONNECTION, false)
            .setValue(UPPER_CONNECTION, false)
        );
        placementHelperId = PlacementHelpers.register(new PlacementHelper());
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LOWER_COG, MIDDLE_COG, UPPER_COG, UPPER_CONNECTION, LOWER_CONNECTION));
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
            if (preferredShaftSide != null) return stateToPlace.setValue(FACING, preferredShaftSide.getOpposite());
        };

        // Defer to the player's facing direction
        return stateToPlace.setValue(FACING, shiftDown ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection());
    };

    public void update(LevelAccessor levelAccessor, BlockPos pos, BlockState state, boolean force) {
        // This one isn't the controller - update it
        if (state.getValue(LOWER_CONNECTION)) withBlockEntityDo(levelAccessor, pos, be -> be.cogPositions.clear());
        // Find and update the actual controller
        int i = 1;
        final Direction facing = state.getValue(FACING);
        while (state.getValue(LOWER_CONNECTION)) {
            pos = pos.relative(facing.getOpposite());
            state = levelAccessor.getBlockState(pos);
            if (state.getBlock() != this || !state.hasProperty(UPPER_CONNECTION) || state.getValue(FACING) != facing) return; // Badly formatted states
            i++;
            if (i > getMaxTransmissionLength()) return;
        };
        if (force) withBlockEntityDo(levelAccessor, pos, be -> be.forceUpdate = true);
        levelAccessor.scheduleTick(pos, this, 1);
    };

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LOWER_CONNECTION)) return; // Not actually the controller

        final BlockPos originalPos = pos;
        final Direction facing = state.getValue(FACING);
        final RedstoneTransmissionBlockEntity be = getBlockEntity(level, originalPos);
        final boolean forceUpdate = be.forceUpdate;

        be.forceUpdate = false;

        // Check existing cogs and power

        int power = level.getBestNeighborSignal(pos);
        int length = 1;
        boolean[] cogs = new boolean[getMaxTransmissionLength() * 3];

        cogs[0] = state.getValue(LOWER_COG);
        cogs[1] = state.getValue(MIDDLE_COG);
        cogs[2] = state.getValue(UPPER_COG);

        while (state.getValue(UPPER_CONNECTION)) {
            if (length >= getMaxTransmissionLength()) return; // Too long, don't update
            
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

        // Move cogs according to Redstone input

        int currentOffset = -1;
        int lastCogIndex = -1;
        for (int cog = 0; cog < cogs.length; cog++) {
            if (cogs[cog]) {
                if (currentOffset == -1) currentOffset = cog;
                lastCogIndex = cog;
            };
        };
        if (currentOffset == -1 && !forceUpdate) return; // No cogs to switch

        final int maxOffset = currentOffset + cogs.length - lastCogIndex - 1; // Maximum value 'first' can take
        power = Math.min(power, maxOffset);
        final int displacement = power - currentOffset;
        if (displacement == 0 && !forceUpdate) return; // Already in position and BE knows all the cogs

        final boolean[] newCogs = new boolean[cogs.length];
        if (currentOffset != -1) System.arraycopy(cogs, currentOffset, newCogs, power, cogs.length - maxOffset);

        for (int block = 0; block < newCogs.length / 3; block++) {
            final BlockPos changePos = originalPos.relative(facing, block);
            final BlockState changeState = level.getBlockState(changePos);
            super.switchBlockState(level, changePos, changeState, changeState // Super to avoid updating again
                .setValue(LOWER_COG, newCogs[3 * block])
                .setValue(MIDDLE_COG, newCogs[3 * block + 1])
                .setValue(UPPER_COG, newCogs[3 * block + 2])
            );
            level.getBlockEntity(changePos).setChanged();
        };

        be.cogPositions.clear();
        for (int cog = 0; cog < newCogs.length; cog++) {
            if (newCogs[cog]) be.cogPositions.add(LerpedFloat.linear()
                .startWithValue(getDisplacementForCog(cog - displacement))
                .chase(getDisplacementForCog(cog), 0.5d, Chaser.EXP)
            );
        };
        be.updateBoundingBox = true;
        be.sendData();
    };

    public double getDisplacementForCog(int cogIndex) {
        return (double)(cogIndex / 3) + switch (cogIndex % 3) {
            case 2 -> 5 / 16d;
            case 1 -> 0 / 16d;
            default -> -5 / 16d;
        };
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
    public void switchBlockState(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
        super.switchBlockState(level, pos, oldState, newState);
        if (oldState != newState && newState.getBlock() == this) update(level, pos, newState, true);
    };

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        final Direction facing = state.getValue(FACING);
        if (state.getValue(UPPER_CONNECTION) && pos.relative(facing).equals(neighborPos)) return; // Neighboring transmission updated - ignore
        if (state.getValue(LOWER_CONNECTION) && pos.relative(facing.getOpposite()).equals(neighborPos)) return;
        update(level, pos, state, false);
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
                update(level, pos, state, true);
            };

            // Add new connection if needed
            if (neighborState.getBlock() == this && 
                neighborState.getValue(FACING) == facing &&
                neighborState.getValue(oppositeConnectionProperty) &&
                !state.getValue(connectionProperty)
            ) {
                state = state.setValue(connectionProperty, true);
                update(level, pos, state, true);
            };
        };
        return state;
    };

    @Override
    public BlockState getReplacedState(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        final Direction facing = existingState.getValue(FACING);

        if (
            !(newState.getBlock() instanceof SeparateShaftHalvesAssemblageBlock assemblageBlock) ||
            newState.getValue(IAssemblageBlock.AXIS) != facing.getAxis() ||
            assemblageBlock.hasTopShaft(newState) || assemblageBlock.hasBottomShaft(newState) || 
            assemblageBlock.getSet().coaxialCogItem() != PetrolsPartsItems.COAXIAL_COGWHEEL
        )
            return null;
        
        final BooleanProperty topProperty = facing.getAxisDirection() == AxisDirection.POSITIVE ? UPPER_COG : LOWER_COG;
        final BooleanProperty bottomProperty = facing.getAxisDirection() == AxisDirection.POSITIVE ? LOWER_COG : UPPER_COG;

        if (switch (newState.getValue(IAssemblageBlock.TOP_COG)) {
            case NONE -> false;
            case SMALL_COAXIAL -> {
                if (existingState.getValue(topProperty)) yield false;
                existingState = existingState.setValue(topProperty, true);
                yield true;
            } default -> true;
        })
            return null;

        if (switch (newState.getValue(IAssemblageBlock.MIDDLE_COG)) {
            case NONE -> false;
            case SMALL_COAXIAL -> {
                if (existingState.getValue(MIDDLE_COG)) yield false;
                existingState = existingState.setValue(MIDDLE_COG, true);
                yield true;
            } default -> true;
        })
            return null;

        if (switch (newState.getValue(IAssemblageBlock.BOTTOM_COG)) {
            case NONE -> false;
            case SMALL_COAXIAL -> {
                if (existingState.getValue(bottomProperty)) yield false;
                existingState = existingState.setValue(bottomProperty, true);
                yield true;
            } default -> true;
        })
            return null;

        return existingState;
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // Placement helper
        final IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(stack)) {
            final PlacementOffset offset = helper.getOffset(player, level, state, pos, hitResult);
            if (offset.isSuccessful()) return offset.placeInWorld(level, (BlockItem)stack.getItem(), player, hand, hitResult);
        };

        // Place Coaxial Cog
        if (AssemblageSet.VANILLA.get().coaxialCogItem().isIn(stack)) {
            final Direction facing = state.getValue(FACING);
            final double coord = hitResult.getLocation().get(facing.getAxis()) - (double)pos.get(facing.getAxis());
            final BooleanProperty cogProperty;
            if (coord < 5 / 16d) {
                cogProperty = facing.getAxisDirection() == AxisDirection.POSITIVE ? LOWER_COG : UPPER_COG;
            } else if (coord < 11 / 16d) {
                cogProperty = MIDDLE_COG;
            } else {
                cogProperty = facing.getAxisDirection() == AxisDirection.POSITIVE ? UPPER_COG : LOWER_COG;
            };
            if (state.getValue(cogProperty)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            switchBlockState(level, pos, state, state.setValue(cogProperty, true));
            if (!player.hasInfiniteMaterials()) stack.shrink(1);
            return ItemInteractionResult.CONSUME;
        };

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    };

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        return updateAfterPlaced(context.getLevel(), newState, context.getClickedPos());
    };

    public BlockState updateAfterPlaced(Level level, BlockState newState, BlockPos pos) {
        final Direction facing = newState.getValue(FACING);
        newState = newState.setValue(UPPER_CONNECTION, false).setValue(LOWER_CONNECTION, false);

        final BlockPos belowPos = pos.relative(facing.getOpposite());
        final BlockState belowState = level.getBlockState(belowPos);
        final int belowLength;
        if (belowState.getBlock() == this && belowState.getValue(FACING) == facing) {
            belowLength = existingLength(level, belowPos, facing, facing.getOpposite());
            if (belowLength > 0) newState = newState.setValue(LOWER_CONNECTION, true);
        } else 
            belowLength = 0;

        final BlockPos abovePos = pos.relative(facing);
        final BlockState aboveState = level.getBlockState(abovePos);
        if (aboveState.getBlock() == this && aboveState.getValue(FACING) == facing) {
            final int aboveLength = existingLength(level, abovePos, facing, facing);
            if (aboveLength > 0 && belowLength + 1 + aboveLength <= getMaxTransmissionLength()) newState = newState.setValue(UPPER_CONNECTION, true);
        };

        return newState;
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        if (oldState.getValue(FACING) == newState.getValue(FACING)) {
            if (oldState.getValue(UPPER_COG) != newState.getValue(UPPER_COG)) return false;
            if (oldState.getValue(LOWER_COG) != newState.getValue(LOWER_COG)) return false;
        } else if (oldState.getValue(FACING) == newState.getValue(FACING).getOpposite()) {
            if (oldState.getValue(UPPER_COG) != newState.getValue(LOWER_COG)) return false;
            if (oldState.getValue(LOWER_COG) != newState.getValue(UPPER_COG)) return false;
        } else
            return false;
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
        return PetrolsPartsBlockEntityTypes.REDSTONE_TRANSMISSION.get();
    };

    public class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return s -> s.getItem() == RedstoneTransmissionBlock.this.asItem();
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() == RedstoneTransmissionBlock.this;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            final Direction facing = state.getValue(FACING);
            int range = AllConfigs.server().equipment.placementAssistRange.get();
            if (player != null) {
                AttributeInstance reach = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
                if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier.id())) range += 4;
            };
            eachDir: for (Direction dir : IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), facing.getAxis())) {
                final BooleanProperty connectionProperty = dir == facing ? UPPER_CONNECTION : LOWER_CONNECTION;
                final BooleanProperty oppositeConnectionProperty = dir == facing ? LOWER_CONNECTION : UPPER_CONNECTION;

                int totalLength = 1;

                BlockPos oppositePos = pos;
                BlockState oppositeState = state;
                while (oppositeState.getValue(oppositeConnectionProperty)) {
                    totalLength++;
                    if (totalLength > range || totalLength >= getMaxTransmissionLength()) return PlacementOffset.fail();
                    oppositePos = oppositePos.relative(dir.getOpposite());
                    oppositeState = world.getBlockState(oppositePos);
                    if (oppositeState.getBlock() != RedstoneTransmissionBlock.this || oppositeState.getValue(FACING) != facing || !oppositeState.getValue(connectionProperty)) return PlacementOffset.fail(); // Badly-formatted states
                };

                int distance = 1;
                
                while (state.getValue(connectionProperty)) {
                    distance++;
                    totalLength++;
                    if (distance > range || totalLength >= getMaxTransmissionLength()) return PlacementOffset.fail();
                    pos = pos.relative(dir);
                    state = world.getBlockState(pos);
                    if (state.getBlock() != RedstoneTransmissionBlock.this || state.getValue(FACING) != facing || !state.getValue(oppositeConnectionProperty)) return PlacementOffset.fail(); // Badly-formatted states 
                };

                final BlockPos newPos = pos.relative(dir);
                if (!world.getBlockState(newPos).canBeReplaced()) continue eachDir;
                return PlacementOffset.success(newPos, s -> updateAfterPlaced(world, s.setValue(FACING, facing), newPos));
            };

            return PlacementOffset.fail();
        };

    };
    
};
