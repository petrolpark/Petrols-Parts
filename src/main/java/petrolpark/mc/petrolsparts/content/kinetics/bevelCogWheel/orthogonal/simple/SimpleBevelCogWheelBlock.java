package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.StructureTransform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import petrolpark.mc.library.compat.create.core.world.block.MultiPartKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;

public abstract class SimpleBevelCogWheelBlock extends MultiPartKineticBlock<BevelCogWheelPart> implements IOrthogonalBevelCogWheelBlock {

    private final Supplier<BevelCogWheelSet> set;

    public SimpleBevelCogWheelBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(properties);
        this.set = set;
        registerDefaultState(defaultBlockState()
            .setValue(WATERLOGGED, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    };
    
    @Override
    public BevelCogWheelSet getSet() {
        return set.get();
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
    public Item asItem() {
        return PetrolsPartsItems.BEVEL_COGWHEEL.get();
    };

    @Override
    public String getDescriptionId() {
        return getSet().translationKey();
    };

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, rotation, Mirror.NONE));
    };

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, Rotation.NONE, mirror));
    };
    
};
