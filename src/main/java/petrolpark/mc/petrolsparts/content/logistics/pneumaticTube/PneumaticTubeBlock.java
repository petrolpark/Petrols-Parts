package petrolpark.mc.petrolsparts.content.logistics.pneumaticTube;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.tube.ITubeBlock;
import petrolpark.mc.library.compat.create.core.world.block.tube.TubeSpline;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsConfigs;

public class PneumaticTubeBlock extends DirectionalKineticBlock implements IBE<PneumaticTubeBlockEntity>, ICogWheel, ITubeBlock {

    public final boolean filterable;

    public static PneumaticTubeBlock filterable(Properties properties) {
        return new PneumaticTubeBlock(properties, true);
    };

    public static PneumaticTubeBlock notFilterable(Properties properties) {
        return new PneumaticTubeBlock(properties, false);
    };

    public PneumaticTubeBlock(Properties properties, boolean filterable) {
        super(properties);
        this.filterable = filterable;
    };

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), be -> be.flip(context.getPlayer()));
    };

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (tryReconnect(context)) return InteractionResult.SUCCESS;
        return super.onSneakWrenched(state, context);
    };

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        withBlockEntityDo(level, neighborPos, be -> be.asOutput().ifPresent(PneumaticTubeBlockEntity.Output::forgetBlocked));
        return state;
    };

    @Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        getBlockEntityOptional(world, pos).ifPresent(be -> ItemHelper.dropContents(world, pos, be.itemBacklog));
	};
    
    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    };

    @Override
    public double getTubeSegmentRadius() {
        return 6 / 16d;
    };

    @Override
    public double getTubeSegmentLength() {
        return 3 / 16d;
    };

    @Override
    public double getTubeMaxAngle() {
        return 30d * Mth.PI / 180d;
    };

    @Override
    public int getItemsForTubeLength(double length) {
        return (int)Math.round(length * PetrolsPartsConfigs.server().pneumaticTubeCost.getF());
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    };

    @Override
    public Direction getTubeConnectingFace(Level level, BlockPos pos, BlockState state) {
        return state.getValue(FACING);
    };

    @Override
    public void connectTube(Level level, TubeSpline spline) {
        withBlockEntityDo(level, spline.start.getPos(), be -> be.tube.connect(spline));
    };

    @Override
    public Class<PneumaticTubeBlockEntity> getBlockEntityClass() {
        return PneumaticTubeBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends PneumaticTubeBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.PNEUMATIC_TUBE.get();
    };
    
};
