package com.petrolpark.petrolsparts.content.kinetics.planetaryGearset;
// package com.petrolpark.petrolsparts.content.planetary_gearset;

// import java.util.EnumMap;

// import org.joml.Vector3f;

// import com.petrolpark.petrolsparts.PetrolsPartsPartials;
// import com.petrolpark.util.KineticsHelper;
// import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
// import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
// import com.simibubi.create.content.kinetics.base.RotatingInstance;
// import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;

// import dev.engine_room.flywheel.api.instance.Instancer;
// import dev.engine_room.flywheel.api.visualization.VisualizationContext;
// import dev.engine_room.flywheel.api.visualization.VisualizationManager;
// import dev.engine_room.flywheel.lib.instance.InstanceTypes;
// import net.minecraft.core.Direction;
// import net.minecraft.core.Direction.Axis;
// import net.minecraft.core.Direction.AxisDirection;
// import net.minecraft.world.level.LightLayer;
// import net.minecraft.world.level.block.state.BlockState;

// public class PlanetaryGearsetVisual extends KineticBlockEntityVisual<PlanetaryGearsetBlockEntity> {

//     protected final RotatingInstance ringGear;
//     protected final RotatingInstance sunGear;
//     protected final EnumMap<Direction, RotatingInstance> keys;

//     public PlanetaryGearsetVisual(VisualizationContext visualizationContext, PlanetaryGearsetBlockEntity blockEntity, float partialTick) {
//         super(visualizationContext, blockEntity, partialTick);
//         BlockState blockState = blockEntity.getBlockState();
//         Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
//         int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
//         int skyLight = level.getBrightness(LightLayer.SKY, pos);

//         ringGear = instancerProvider().getModel(PetrolsPartsPartials.PG_RING_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis))
//             .createInstance();
//         ringGear
//             .setRotationAxis(axis)
//             .setRotationOffset(getRotationOffset(axis)).setColor(blockEntity)
//             .setRotationalSpeed(blockEntity.getSpeed())
//             .setPosition(getVisualPosition())
// 			.setBlockLight(blockLight)
// 			.setSkyLight(skyLight);

//         sunGear = getRotatingMaterial().getModel(PetrolsPartsPartials.PG_SUN_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis))
//             .createInstance();
//         sunGear
//             .setRotationAxis(axis)
//             .setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)).setColor(blockEntity)
//             .setRotationalSpeed(-2 * getBlockEntitySpeed())
//             .setPosition(getVisualPosition())
// 			.setBlockLight(blockLight)
// 			.setSkyLight(skyLight);

//         keys = new EnumMap<>(Direction.class);

//         for (Direction direction : Direction.values()) {
//             if (direction.getAxis() == axis) continue;

//             Instancer<RotatingInstance> planetGear = getRotatingMaterial().getModel(PetrolsPartsPartials.PG_PLANET_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis));

// 			RotatingInstance key = planetGear.createInstance();

//             Vector3f position = new Vector3f(getVisualPosition().getX(), getVisualPosition().getY(), getVisualPosition().getZ());
//             position.add(direction.step().mul(6.25f / 16f));

// 			key
//                 .setRotationAxis(axis)
//                 .setRotationalSpeed(2 * getBlockEntitySpeed())
//                 .setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)).setColor(blockEntity)
//                 .setPosition(position)
//                 .setBlockLight(blockLight)
//                 .setSkyLight(skyLight);

//             keys.put(direction, key);
//         };
//     };

//     @Override
//     public void update(float partialTick) {
//         Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
//         updateRotation(ringGear, axis, blockEntity.getSpeed());
//         updateRotation(sunGear, axis, -2 * getBlockEntitySpeed());
//         sunGear.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos));
//         keys.values().forEach(gear -> {
//             updateRotation(gear, axis, 2 * getBlockEntitySpeed());
//             gear.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos));
//         });
//     };

//     @Override
//     public void updateLight(float partialTick) {
//         relight(pos, ringGear, sunGear);
//         relight(pos, keys.values().stream());
//     };

//     @Override
//     protected void _delete() {
//         ringGear.delete();
//         sunGear.delete();
//         keys.values().forEach(RotatingInstance::delete);
//         keys.clear();
//     };
    
// };
