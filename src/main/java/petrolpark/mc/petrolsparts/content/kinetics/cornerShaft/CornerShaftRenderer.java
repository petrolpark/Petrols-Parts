package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class CornerShaftRenderer extends EncasedCornerShaftRenderer {

    public CornerShaftRenderer(Context context) {
        super(context);
    };

    @Override
    protected void renderMiddle(CornerShaftBlockEntity cornerShaftBlockEntity, BlockState state, PoseStack ms, VertexConsumer vbSolid, int light, Direction shaft1Direction, Direction shaft2Direction, float gimbal1Angle, float gimbal2Angle) {
        Axis axis = getAxis(shaft1Direction, shaft2Direction);

        boolean facesHaveSameSign = shaft1Direction.getAxisDirection() == shaft2Direction.getAxisDirection();
        boolean secondaryPositive = state.getValue(CornerShaftBlock.AXIS_ALONG_FIRST_COORDINATE);

        float fluctuatingAngle1 = (float)Math.atan2(Mth.sin(gimbal1Angle), Mth.cos(gimbal1Angle) * Mth.sqrt(2) / 2) + (axis == Axis.Z ? Mth.PI / 4 * (facesHaveSameSign ? 1f : -1f) : 0);
        float fluctuatingAngle2 = (float)Math.atan2(Mth.sin(gimbal2Angle), Mth.cos(gimbal2Angle) * Mth.sqrt(2) / 2);
        float fluctuatingAngle3 = (float)Math.atan2(Mth.sin(gimbal1Angle + Mth.PI / 2), Mth.cos(gimbal1Angle + Mth.PI / 2) * Mth.sqrt(2) / 2) + Mth.PI / 2;
        float gimbal1FluctuatingAngle = Mth.sin(fluctuatingAngle1 + (axis == Axis.Z ? -Mth.PI / 4 : 0) + (facesHaveSameSign ^ (axis == Axis.X && shaft1Direction.getAxis() == Axis.Z) ? Mth.PI : 0) + (axis == Axis.X ? Mth.PI / 2 : 0)) * Mth.PI / 4;
        float gimbal2FluctuatingAngle = Mth.sin(fluctuatingAngle2 + (facesHaveSameSign ^ (axis == Axis.X && shaft2Direction.getAxis() == Axis.Z) ? Mth.PI : 0) + (axis == Axis.Z && !facesHaveSameSign ? Mth.PI / 2 : 0) + (axis == Axis.X ? Mth.PI / 2 : 0)) * Mth.PI / 4;
        
        SuperByteBuffer grip1 = CachedBuffers.partialFacing(PetrolsPartsPartialModels.CORNER_SHAFT_SIDE_GRIP, state, shaft1Direction);
        kineticRotationTransform(grip1, cornerShaftBlockEntity, shaft1Direction.getAxis(), gimbal1Angle + (axis == Axis.Z ? Mth.PI / 2 : 0f), light);
        grip1.renderInto(ms, vbSolid);

        SuperByteBuffer grip2 = CachedBuffers.partialFacing(PetrolsPartsPartialModels.CORNER_SHAFT_SIDE_GRIP, state, shaft2Direction);
        kineticRotationTransform(grip2, cornerShaftBlockEntity, shaft2Direction.getAxis(), gimbal2Angle, light);
        grip2.renderInto(ms, vbSolid);
        
        CachedBuffers.partial(PetrolsPartsPartialModels.CORNER_SHAFT_CENTER, state)
            .translate(shaft1Direction.step().mul(2.5f / 16f))
            .translate(shaft2Direction.step().mul(2.5f / 16f))
            .center()
            .rotateYDegrees(axis == Axis.Z ? 90f : 0f)
            .rotateXDegrees(axis == Axis.Z ? (facesHaveSameSign ? 45f : 135f) : 0f)
            .rotateDegrees(facesHaveSameSign ^ axis != Axis.Y ? 135f : 45f, axis)
            .uncenter()
            .center()
            .rotateZ((axis == Axis.X ? fluctuatingAngle3 : fluctuatingAngle1) * (axis == Axis.X || (axis == Axis.Y ^ facesHaveSameSign) ? 1f : -1f) * (axis == Axis.X ? -1f : 1f))
            .uncenter()
            .light(light)
            .renderInto(ms, vbSolid);

        CachedBuffers.partialFacing(PetrolsPartsPartialModels.CORNER_SHAFT_GIMBAL, state, shaft1Direction)
            
            .center()
            .rotate(gimbal1Angle, shaft1Direction.getAxis())
            .center()

            .translateBack(CornerShaftVisual.gimbalTranslation(shaft1Direction))
            .rotate(CornerShaftVisual.gimbalRotation(shaft1Direction, axis == Axis.Z), gimbal1FluctuatingAngle)
            .rotateYDegrees(axis == Axis.Z && !secondaryPositive ? 90 : 0)
            .rotateXDegrees(axis == Axis.Z ? 90 : 0)
            .translate(CornerShaftVisual.gimbalTranslation(shaft1Direction))
    
            .uncenter()
            .uncenter()
            .light(light)
            .renderInto(ms, vbSolid);

        CachedBuffers.partialFacing(PetrolsPartsPartialModels.CORNER_SHAFT_GIMBAL, state, shaft2Direction)
            
            .center()
            .rotate(gimbal2Angle, Direction.get(AxisDirection.POSITIVE, shaft2Direction.getAxis()))
            .center()

            .translateBack(CornerShaftVisual.gimbalTranslation(shaft2Direction))
            .rotate(CornerShaftVisual.gimbalRotation(shaft2Direction, false), gimbal2FluctuatingAngle)
            .translate(CornerShaftVisual.gimbalTranslation(shaft2Direction))

            .uncenter()
            .uncenter()
            .light(light)
            .renderInto(ms, vbSolid);
    };

    
};
