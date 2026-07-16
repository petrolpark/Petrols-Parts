package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.Collection;
import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import petrolpark.mc.library.compat.create.core.world.block.composite.MultiPartCompositeKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelBlock;

public abstract class CompositeBevelCogWheelBlock extends MultiPartCompositeKineticBlock<BevelCogWheelPart> implements IOrthogonalBevelCogWheelBlock, IBE<CompositeBevelCogWheelBlockEntity> {

    public CompositeBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(WATERLOGGED, false)
        );
    };

    protected abstract List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        return getSimpleBevelCogWheelEquivalents(state).stream()
            .<BevelCogWheelPart>mapMulti((s, consumer) -> {
                if (AllBlocks.SHAFT.has(s)) consumer.accept(BevelCogWheelPart.SHAFTS.get(s.getValue(ShaftBlock.AXIS)));
                else if (s.getBlock() instanceof SimpleBevelCogWheelBlock sbcwb) sbcwb.getParts(s).forEach(consumer);
            }).toList();
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, neighborState, neighborPos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    public Item asItem() {
        return PetrolsPartsItems.BEVEL_COGWHEEL.get();
    };

    @Override
    public String getDescriptionId() {
        return TRANSLATION_KEY;
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
    public Class<CompositeBevelCogWheelBlockEntity> getBlockEntityClass() {
        return CompositeBevelCogWheelBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends CompositeBevelCogWheelBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.COMPOSITE_BEVEL_COGWHEEL.get();
    };
    
};
