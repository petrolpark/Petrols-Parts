package com.petrolpark.petrolsparts.content.kinetics.cornerShaft;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.Vec3;

//TODO
public abstract class CornerShaftVisual extends KineticBlockEntityVisual<CornerShaftBlockEntity> implements DynamicVisual {

    public CornerShaftVisual(VisualizationContext context, CornerShaftBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
    };
    
    // protected final RotatingInstance shaft1;
    // protected final RotatingInstance grip1;
    // protected final ModelData gimbal1;

    // protected final RotatingInstance shaft2;
    // protected final RotatingInstance grip2;
    // protected final ModelData gimbal2;

    // protected final ModelData centerShaft;

    // protected final Direction shaft1Direction;
    // protected final Direction shaft2Direction;

    // protected Direction sourceFacing;
    // protected boolean secondaryPositive;

    // public DoubleCardanShaftVisual(VisualizationContext visualizationContext, DoubleCardanShaftBlockEntity blockEntity, float partialTicks) {
    //     super(visualizationContext, blockEntity, partialTicks);
    //     Material<ModelData> modelMaterial = getTransformMaterial();
    //     Material<RotatingInstance> rotatingMaterial = getRotatingMaterial();
    //     Direction[] directions = DoubleCardanShaftBlock.getDirectionsConnectedByState(blockEntity.getBlockState());
    //     secondaryPositive = blockEntity.getBlockState().getValue(DoubleCardanShaftBlock.AXIS_ALONG_FIRST_COORDINATE);
        
    //     shaft1Direction = directions[0];
    //     shaft2Direction = directions[1];

    //     int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
    //     int skyLight = level.getBrightness(LightLayer.SKY, pos);

    //     updateSourceFacing();

    //     shaft1 = rotatingMaterial.getModel(PetrolsPartsPartials.DCS_SIDE_SHAFT, blockEntity.getBlockState(), shaft1Direction)
    //         .createInstance();
    //     shaft1.setRotationAxis(Direction.get(AxisDirection.POSITIVE, shaft1Direction.getAxis()).step())
    //         .setRotationOffset(getRotationOffset(shaft1Direction.getAxis()));
    //     transformShaft(shaft1, shaft1Direction, blockLight, skyLight);

    //     grip1 = rotatingMaterial.getModel(PetrolsPartsPartials.DCS_SIDE_GRIP, blockEntity.getBlockState(), shaft1Direction)
    //         .createInstance();
    //     grip1.setRotationAxis(Direction.get(AxisDirection.POSITIVE, shaft1Direction.getAxis()).step())
    //         .setRotationOffset(getAxis() == Axis.Z ? 90f : 0f);
    //     transformShaft(grip1, shaft1Direction, blockLight, skyLight);

    //     gimbal1 = modelMaterial.getModel(PetrolsPartsPartials.DCS_GIMBAL, blockEntity.getBlockState(), shaft1Direction)
    //         .createInstance();

    //     shaft2 = rotatingMaterial.getModel(PetrolsPartsPartials.DCS_SIDE_SHAFT, blockEntity.getBlockState(), shaft2Direction)
    //         .createInstance();
    //     shaft2.setRotationAxis(Direction.get(AxisDirection.POSITIVE, shaft2Direction.getAxis()).step())
    //         .setRotationOffset(getRotationOffset(shaft2Direction.getAxis())).setColor(blockEntity);
    //     transformShaft(shaft2, shaft2Direction, blockLight, skyLight);

    //     grip2 = rotatingMaterial.getModel(PetrolsPartsPartials.DCS_SIDE_GRIP, blockEntity.getBlockState(), shaft2Direction)
    //         .createInstance();
    //     grip2.setRotationAxis(Direction.get(AxisDirection.POSITIVE, shaft2Direction.getAxis()).step())
    //         .setRotationOffset(0f);
    //     transformShaft(grip2, shaft2Direction, blockLight, skyLight);

    //     gimbal2 = modelMaterial.getModel(PetrolsPartsPartials.DCS_GIMBAL, blockEntity.getBlockState(), shaft2Direction)
    //         .createInstance();

    //     centerShaft = modelMaterial.getModel(PetrolsPartsPartials.DCS_CENTER_SHAFT).createInstance();
    // };

    // public float getSpeed(Direction direction) {
    //     float speed = blockEntity.getSpeed();
    //     if (speed != 0 && sourceFacing != null) {
    //         if (sourceFacing.getAxisDirection() == direction.getAxisDirection() && direction != sourceFacing) speed *= -1;
    //     };
    //     return speed;
    // };

    // private void transformShaft(KineticData data, Direction face, int blockLight, int skyLight) {
    //     data
    //         .setRotationalSpeed(getSpeed(face))
    //         .setPosition(getVisualPosition())
	// 		.setBlockLight(blockLight)
	// 		.setSkyLight(skyLight);
    // };

    // @Override
    // public void beginFrame() {
    //     Axis axis = getAxis();

    //     float gimbal1Angle = Mth.PI * ((AnimationTickHolder.getRenderTime() * getSpeed(shaft1Direction) * 3f / 10) % 360) / 180f;
    //     float gimbal2Angle = Mth.PI * ((AnimationTickHolder.getRenderTime() * getSpeed(shaft2Direction) * 3f / 10) % 360) / 180f;
    //     float fluctuatingAngle1 = (float)Math.atan2(Mth.sin(gimbal1Angle), Mth.cos(gimbal1Angle) * Mth.sqrt(2) / 2) + (axis == Axis.Z ? Mth.PI / 4 * (facesHaveSameSign() ? 1f : -1f) : 0);
    //     float fluctuatingAngle2 = (float)Math.atan2(Mth.sin(gimbal2Angle), Mth.cos(gimbal2Angle) * Mth.sqrt(2) / 2);
    //     float fluctuatingAngle3 = (float)Math.atan2(Mth.sin(gimbal1Angle + Mth.PI / 2), Mth.cos(gimbal1Angle + Mth.PI / 2) * Mth.sqrt(2) / 2) + Mth.PI / 2;
    //     float gimbal1FluctuatingAngle = Mth.sin(fluctuatingAngle1 + (getAxis() == Axis.Z ? -Mth.PI / 4 : 0) + (facesHaveSameSign() ^ (axis == Axis.X && shaft1Direction.getAxis() == Axis.Z) ? Mth.PI : 0) + (axis == Axis.X ? Mth.PI / 2 : 0)) * Mth.PI / 4;
    //     float gimbal2FluctuatingAngle = Mth.sin(fluctuatingAngle2 + (facesHaveSameSign() ^ (axis == Axis.X && shaft2Direction.getAxis() == Axis.Z) ? Mth.PI : 0) + (axis == Axis.Z && !facesHaveSameSign() ? Mth.PI / 2 : 0) + (axis == Axis.X ? Mth.PI / 2 : 0)) * Mth.PI / 4;

    //     centerShaft.loadIdentity()
    //         .translate(getVisualPosition())
    //         .translate(shaft1Direction.step().mul(2.5f / 16f))
    //         .translate(shaft2Direction.step().mul(2.5f / 16f))
    //         .centre()
    //         .rotateY(axis == Axis.Z ? 90f : 0f)
    //         .rotateX(axis == Axis.Z ? (facesHaveSameSign() ? 45f : 135f) : 0f)
    //         .rotate(facesHaveSameSign() ^ axis != Axis.Y ? 135f : 45f, axis)
    //         .unCentre()
    //         .centre()
    //         .rotateZRadians((axis == Axis.X ? fluctuatingAngle3 : fluctuatingAngle1) * (axis == Axis.X || (axis == Axis.Y ^ facesHaveSameSign()) ? 1f : -1f) * (axis == Axis.X ? -1f : 1f))
    //         .unCentre();

    //     gimbal1.loadIdentity()
    //         .translate(getVisualPosition())
            
    //         .centre()
    //         .rotate(Direction.get(AxisDirection.POSITIVE, shaft1Direction.getAxis()), gimbal1Angle)
    //         .centre()

    //         .translateBack(gimbalTranslation(shaft1Direction))
    //         .rotate(gimbalRotation(shaft1Direction, axis == Axis.Z), gimbal1FluctuatingAngle)
    //         .rotateY(axis == Axis.Z && !secondaryPositive ? 90 : 0)
    //         .rotateX(axis == Axis.Z ? 90 : 0)
    //         .translate(gimbalTranslation(shaft1Direction))

    //         .unCentre()
    //         .unCentre()
    //         ;

    //     gimbal2.loadIdentity()
    //         .translate(getVisualPosition())
            
    //         .centre()

    //         .rotate(Direction.get(AxisDirection.POSITIVE, shaft2Direction.getAxis()), gimbal2Angle)
    //         .centre()

    //         .translateBack(gimbalTranslation(shaft2Direction))
    //         .rotate(gimbalRotation(shaft2Direction, false), gimbal2FluctuatingAngle)
    //         .translate(gimbalTranslation(shaft2Direction))

    //         .unCentre()
    //         .unCentre()
    //         ;
    // };

    // private boolean facesHaveSameSign() {
    //     return shaft1Direction.getAxisDirection() == shaft2Direction.getAxisDirection();
    // };

    public static Vec3 gimbalTranslation(Direction face) {
        switch (face) {
            case NORTH:
                return new Vec3(8 / 16d, 8 / 16d, 13 / 16d);
            case EAST:
                return new Vec3(3 / 16d, 8 / 16d, 8 / 16d);
            case SOUTH:
                return new Vec3(8 / 16d, 8 / 16d, 3 / 16d);
            case WEST:
                return new Vec3(13 / 16d, 8 / 16d, 8 / 16d);
            case UP:
                return new Vec3(8 / 16d, 3 / 16d, 8 / 16d);
            case DOWN:
                return new Vec3(8 / 16d, 13 / 16d, 8 / 16d);
            default:
                return new Vec3(8 / 16d, 8 / 16d, 8 / 16d); // Shouldn't be called
        }
    };

    public static Axis gimbalRotation(Direction direction, boolean isZaxis) {
        switch (direction) {
            case NORTH:
                return Axis.X;
            case EAST:
                return isZaxis ? Axis.Y : Axis.Z;
            case SOUTH:
                return Axis.X;
            case WEST:
                return isZaxis ? Axis.Y : Axis.Z;
            case UP:
                return isZaxis ? Axis.Z : Axis.X;
            case DOWN:
                return isZaxis ? Axis.Z : Axis.X;
            default:
                throw new IllegalStateException("Unknown direction");
        }
    };

    // @SuppressWarnings("null")
    // protected void updateSourceFacing() {
    //     if (blockEntity.hasSource()) {
    //         BlockPos source = blockEntity.source.subtract(pos); // It thinks source can be null (it can't)
    //         sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
    //     } else {
    //         sourceFacing = null;
    //     }
    // };

    // private Axis getAxis() {
    //     List<Axis> axes = new ArrayList<>();
    //     axes.addAll(List.of(Axis.values()));
    //     axes.remove(shaft1Direction.getAxis());
    //     axes.remove(shaft2Direction.getAxis());
    //     return axes.get(0);
    // };

    // @Override
    // public void update() {
    //     super.update();
    //     updateSourceFacing();
    //     updateRotation(shaft1, shaft1Direction.getAxis(), getSpeed(shaft1Direction));
    //     //updateRotation(grip1, shaft1Direction.getAxis(), getSpeed(shaft1Direction));
    //     grip1
    //         .setRotationAxis(Direction.get(AxisDirection.POSITIVE, shaft1Direction.getAxis()).step())
    //         .setRotationalSpeed(getSpeed(shaft1Direction))
    //         .setRotationOffset((getAxis() == Axis.Z ? 90f : 0f));
    //     updateRotation(shaft2, shaft2Direction.getAxis(), getSpeed(shaft2Direction));
    //     //updateRotation(grip2, shaft2Direction.getAxis(), getSpeed(shaft2Direction));
    //     grip2
    //         .setRotationAxis(Direction.get(AxisDirection.POSITIVE, shaft2Direction.getAxis()).step())
    //         .setRotationalSpeed(getSpeed(shaft2Direction))
    //         .setRotationOffset(0f);
    // };

    // @Override
    // public void updateLight() {
    //     relight(pos, shaft1, shaft2, grip1, grip2, gimbal1, gimbal2, centerShaft);
    // };

    // @Override
    // protected void remove() {
    //     shaft1.delete();
    //     shaft2.delete();
    //     grip1.delete();
    //     grip2.delete();
    //     gimbal1.delete();
    //     gimbal2.delete();
    //     centerShaft.delete();
    // };
    
};
