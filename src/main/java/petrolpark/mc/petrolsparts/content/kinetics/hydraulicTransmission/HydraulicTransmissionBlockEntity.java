package petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission;

import java.util.List;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import petrolpark.mc.library.compat.create.core.world.block.tube.ITubeBlockEntity;
import petrolpark.mc.library.compat.create.core.world.block.tube.TubeBehaviour;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class HydraulicTransmissionBlockEntity extends KineticBlockEntity implements ITubeBlockEntity {

    public TubeBehaviour tube;

    public HydraulicTransmissionBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        tube = new TubeBehaviour(this);
        behaviours.add(tube);
    };

    @Override
    public void destroy() {
        super.destroy();
        tube.disconnect();
    };

    @Override
    public TubeBehaviour getTube() {
        return tube;
    };

    @Override
    public void invalidateTubeRenderBoundingBox() {
        invalidateRenderBoundingBox();
    };

    @Override
    protected AABB createRenderBoundingBox() {
        if (tube == null || !tube.isController() || tube.getSpline() == null) return super.createRenderBoundingBox();
        return MathsHelper.expandToInclude(tube.getSpline().getOccupiedVolume(), getBlockPos());
    };

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        updatePartnerSpeed();
    };

    @Override
    public void afterTubeConnect() {
        updateSpeed = true;
        updatePartnerSpeed();
    };

    @Override
    public void beforeTubeDisconnect() {
        detachKinetics();
    };

    public void updatePartnerSpeed() {
        if (tube.getOtherEndPos() != null) getLevel().getBlockEntity(tube.getOtherEndPos(), PetrolsPartsBlockEntityTypes.HYDRAULIC_TRANSMISSION.get()).ifPresent(be -> be.updateSpeed = true);
    };

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (tube.getOtherEndPos() != null) neighbours.add(tube.getOtherEndPos());
        return neighbours;
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target.getBlockPos().equals(tube.getOtherEndPos())) {
            return 1f;
        };
        return 0f;
    };
    
};
