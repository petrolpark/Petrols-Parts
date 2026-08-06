package petrolpark.mc.petrolsparts.content.kinetics.differential;

import java.util.List;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import petrolpark.mc.library.compat.create.core.world.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import petrolpark.mc.library.util.KineticsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.core.advancement.PetrolsPartsAdvancementBehaviour;
import petrolpark.mc.petrolsparts.core.advancement.PetrolsPartsAdvancementTriggers;
import petrolpark.mc.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import petrolpark.mc.petrolsparts.mixin.accessor.RotationPropagatorAccessor;

public class LegacyDifferentialBlockEntity extends SplitShaftBlockEntity {

    public PetrolsPartsAdvancementBehaviour advancementBehaviour;
    public float oldControlSpeed;

    public LegacyDifferentialBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        advancementBehaviour = new PetrolsPartsAdvancementBehaviour(this, PetrolsPartsAdvancementTriggers.DIFFERENTIAL);
        behaviours.add(advancementBehaviour);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (connectedViaAxes) {
            if (target instanceof LegacyDifferentialBlockEntity) return 0f;
            return ratio(stateFrom) * Math.signum(RotationPropagatorAccessor.invokeGetAxisModifier(target, KineticsHelper.directionBetween(target.getBlockPos(), getBlockPos())));
        };
        return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
	};

    @Override
    public void setSource(BlockPos source) {
        super.setSource(source);
        Direction directionBetween = KineticsHelper.directionBetween(getBlockPos(), source);
        if (hasLevel() && (directionBetween == null || directionBetween.getAxis() != getBlockState().getValue(LegacyDifferentialBlock.AXIS))) getLevel().destroyBlock(getBlockPos(), true);
    };

    @Override
    public void tick() {
        super.tick();
        if (!hasLevel()) return;

        Direction direction = DirectionalRotatedPillarKineticBlock.getDirection(getBlockState());
        BlockPos otherAdjacentPos = getBlockPos().relative(direction.getOpposite());

        if (getSpeed() == 0f) { // Try switching the direction if we're not powered by the existing side
            BlockPos adjacentPos = getBlockPos().relative(direction);
            if (!propagatesToMe(adjacentPos, direction.getOpposite()) && propagatesToMe(otherAdjacentPos, direction)) {
                getLevel().setBlockAndUpdate(getBlockPos(), PetrolsPartsBlocks.DUMMY_DIFFERENTIAL.getDefaultState().setValue(LegacyDifferentialBlock.AXIS, direction.getAxis()).setValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION, direction.getAxisDirection() == AxisDirection.NEGATIVE)); 
                AbstractRememberPlacerBehaviour.setPlacedBy(level, getBlockPos(), advancementBehaviour.getPlayer());
            };
        };

        if (getLevel().getBlockEntity(otherAdjacentPos) instanceof KineticBlockEntity kbe) {
            oldControlSpeed = getPropagatedSpeed(kbe, direction);
        };
    };

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (speed == 0f || advancementBehaviour.getPlayer() == null) return;
        Direction direction = DirectionalRotatedPillarKineticBlock.getDirection(getBlockState());
        BlockPos otherAdjacentPos = getBlockPos().relative(direction.getOpposite());
        if (propagatesToMe(otherAdjacentPos, direction) && getLevel().getBlockEntity(otherAdjacentPos) instanceof KineticBlockEntity kbe) {
            if (kbe.getSpeed() != 0f) advancementBehaviour.awardAdvancement(PetrolsPartsAdvancementTriggers.DIFFERENTIAL);
        };
    };

    @Override
    public void removeSource() {
        super.removeSource();
        if (hasLevel()) getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().cycle(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION));
    };

    public float ratio(BlockState stateFrom) {
        Direction towardsInput = DirectionalRotatedPillarKineticBlock.getDirection(stateFrom);
        Direction towardsControl = towardsInput.getOpposite();
        BlockPos inputPos = getBlockPos().relative(towardsInput);
        BlockPos controlPos = getBlockPos().relative(towardsControl);

        BlockEntity inputBE = getLevel().getBlockEntity(inputPos);
        float inputSpeed = 0f;
        if (propagatesToMe(inputPos, towardsControl) && inputBE instanceof KineticBlockEntity inputKBE) inputSpeed = getPropagatedSpeed(inputKBE, towardsControl);

        BlockEntity controlBE = getLevel().getBlockEntity(controlPos);
        float controlSpeed = 0f;
        if (propagatesToMe(controlPos, towardsInput) && controlBE instanceof KineticBlockEntity controlKBE) controlSpeed = getPropagatedSpeed(controlKBE, towardsInput);

        if (inputSpeed + controlSpeed == 0f) return 0f;
        return 2f * inputSpeed / (inputSpeed + controlSpeed);
    };

    public boolean propagatesToMe(BlockPos pos, Direction directionToMe) {
        if (!hasLevel()) return false;
        BlockState state = getLevel().getBlockState(pos);
        return state.getBlock() instanceof KineticBlock kineticBlock && kineticBlock.hasShaftTowards(getLevel(), pos, state, directionToMe);
    };

    public float getPropagatedSpeed(KineticBlockEntity from, Direction directionToMe) {
        if (from instanceof LegacyDifferentialBlockEntity) return 0f;
        return from.getSpeed() * RotationPropagatorAccessor.invokeGetAxisModifier(from, directionToMe);
    };

    @Override
	public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
	    super.addPropagationLocations(block, state, neighbours);
		KineticsHelper.addLargeCogwheelPropagationLocations(worldPosition, neighbours);
		return neighbours;
	};

    @Override
    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
		return true;
	};

    @Override
    public float getRotationSpeedModifier(Direction face) {
        return ratio(getBlockState());
    };

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1);
    };
    
};
