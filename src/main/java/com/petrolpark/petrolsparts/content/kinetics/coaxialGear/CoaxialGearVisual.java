package com.petrolpark.petrolsparts.content.kinetics.coaxialGear;

import com.petrolpark.petrolsparts.PetrolsPartsPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;

public class CoaxialGearVisual {
    
    public static final BlockEntityVisual<CoaxialGearBlockEntity> create(VisualizationContext context, CoaxialGearBlockEntity blockEntity, float partialTick) {
        if (ICogWheel.isLargeCog(blockEntity.getBlockState())) {
			return new SingleAxisRotatingVisual<>(context, blockEntity, partialTick, Models.partial(PetrolsPartsPartialModels.LARGE_COAXIAL_GEAR));
		} else {
			return new SingleAxisRotatingVisual<>(context, blockEntity, partialTick, Models.partial(PetrolsPartsPartialModels.COAXIAL_GEAR));
		}
    };
};
