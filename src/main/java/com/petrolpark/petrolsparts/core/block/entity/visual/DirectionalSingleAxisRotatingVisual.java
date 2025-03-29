package com.petrolpark.petrolsparts.core.block.entity.visual;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;

import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DirectionalSingleAxisRotatingVisual<T extends KineticBlockEntity> extends SingleAxisRotatingVisual<T> {

    public static <T extends KineticBlockEntity> SimpleBlockEntityVisualizer.Factory<T> of(PartialModel partial) {
		return (context, blockEntity, partialTick) -> {
			return new DirectionalSingleAxisRotatingVisual<T>(context, blockEntity, partialTick, Models.partial(partial));
		};
	};

    public DirectionalSingleAxisRotatingVisual(VisualizationContext context, T blockEntity, float partialTick, Model model) {
		this(context, blockEntity, partialTick, Direction.UP, model);
	};

    public DirectionalSingleAxisRotatingVisual(VisualizationContext context, T blockEntity, float partialTick, Direction from, Model model) {
        super(context, blockEntity, partialTick, from, model);
        rotatingModel
            .rotateToFace(Direction.get(AxisDirection.POSITIVE, rotationAxis()), from) // Undo initial rotation
            .rotateToFace(from, rotationFace())
            .setChanged();
    };

    public Direction rotationFace() {
        return blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    };
    
};
