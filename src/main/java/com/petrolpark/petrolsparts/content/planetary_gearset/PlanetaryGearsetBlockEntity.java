package com.petrolpark.petrolsparts.content.planetary_gearset;

import java.util.List;

import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.content.coaxial_gear.LongShaftBlockEntity;
import com.petrolpark.petrolsparts.mixin.accessor.RotationPropagatorAccessor;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PlanetaryGearsetBlockEntity extends SplitShaftBlockEntity {

    public PlanetaryGearsetBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (connectedViaAxes || LongShaftBlockEntity.connectedToLongShaft(this, target, diff)) {
            if (PetrolsPartsBlocks.PLANETARY_GEARSET.has(stateTo)) return 0;
            return Math.signum(RotationPropagatorAccessor.invokeGetAxisModifier(target, KineticsHelper.directionBetween(target.getBlockPos(), getBlockPos()))) * -2;
        };
        return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
	};

    @Override
	public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
	    super.addPropagationLocations(block, state, neighbours);
        KineticsHelper.addLargeCogwheelPropagationLocations(worldPosition, neighbours);
		return neighbours;
	};

    @Override
    public float getRotationSpeedModifier(Direction face) {
        return -2;
    };

    @Override
    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
		return true;
	};

    
};
