package petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.tube.ITubeBlock;
import petrolpark.mc.library.compat.create.core.world.block.tube.TubeSpline;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsConfigs;

public class HydraulicTransmissionBlock extends DirectionalKineticBlock implements IBE<HydraulicTransmissionBlockEntity>, ICogWheel, ITubeBlock {

    public HydraulicTransmissionBlock(Properties properties) {
        super(properties);
    };

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (tryReconnect(context)) return InteractionResult.SUCCESS;
        return super.onWrenched(state, context);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    };

    @Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == state.getValue(FACING).getOpposite();
	};

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        IBE.onRemove(pState, pLevel, pPos, pNewState);
    };

    @Override
    public double getTubeSegmentRadius() {
        return 4 / 16d;
    };

    @Override
    public double getTubeSegmentLength() {
        return 3.5 / 16d;
    };

    @Override
    public double getTubeMaxAngle() {
        return 20d * Mth.PI / 180d;
    };

    @Override
    public int getItemsForTubeLength(double length) {
        return (int)Math.round(length * PetrolsPartsConfigs.server().hydraulicTransmissionCost.getF());
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
    public Class<HydraulicTransmissionBlockEntity> getBlockEntityClass() {
        return HydraulicTransmissionBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends HydraulicTransmissionBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.HYDRAULIC_TRANSMISSION.get();
    };
    
};
