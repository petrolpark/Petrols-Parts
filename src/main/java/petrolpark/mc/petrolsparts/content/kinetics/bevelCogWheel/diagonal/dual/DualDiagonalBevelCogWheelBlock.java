package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.composite.MultiPartCompositeKineticBlock;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.DiagonalBevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.ISingleDiagonalBevelCogWheelBlock;

public class DualDiagonalBevelCogWheelBlock extends MultiPartCompositeKineticBlock<DiagonalBevelCogWheelPart> implements IDualDiagonalBevelCogWheelBlock, ProperWaterloggedBlock, EncasableBlock {

    public DualDiagonalBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(EXCLUDED_AXIS, Axis.X)
            .setValue(FACE_PARITY, true)
            .setValue(WATERLOGGED, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXCLUDED_AXIS, FACE_PARITY, WATERLOGGED));
    };

    @Override
    public Collection<DiagonalBevelCogWheelPart> getParts(BlockState state) {
        return Stream.of(IDualDiagonalBevelCogWheelBlock.getCogOrientations(state)).<DiagonalBevelCogWheelPart>map(DiagonalBevelCogWheelPart.COGS::get).toList();
    };

    @Override
    public BlockState withoutPart(BlockState state, DiagonalBevelCogWheelPart part) {
        final List<Pair<Orientation, DiagonalBevelCogWheelPart>> parts = Stream.of(IDualDiagonalBevelCogWheelBlock.getCogOrientations(state))
            .map(orientation -> Pair.<Orientation, DiagonalBevelCogWheelPart>of(orientation, DiagonalBevelCogWheelPart.COGS.get(orientation)))
            .collect(Collectors.toCollection(ArrayList::new));
        parts.removeIf(pair -> pair.getSecond() == part);
        return PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL.getDefaultState()
            .setValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION, parts.get(0).getFirst())
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return placeCogOrEncase(stack, state, level, pos, player, hand, hitResult);
    };

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canDiagonalBevelCogWheelSurvive(state, level, pos);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y; //unused
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
        return IDualDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return IDualDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };
    
};
