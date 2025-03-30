package com.petrolpark.petrolsparts.content.coaxial_gear;

import com.petrolpark.petrolsparts.PetrolsPartsPartialModels;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.petrolpark.petrolsparts.core.block.entity.visual.DirectionalSingleAxisRotatingVisual;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;

public class LongShaftVisual extends DirectionalSingleAxisRotatingVisual<LongShaftBlockEntity> {

    public LongShaftVisual(VisualizationContext context, LongShaftBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Direction.UP, Models.partial(PetrolsPartsPartialModels.LONG_SHAFT));
    };

    @Override
    public Direction rotationFace() {
        return DirectionalRotatedPillarKineticBlock.getDirection(blockState);
    };
    
};
