package petrolpark.mc.petrolsparts.content.kinetics.differential;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class DifferentialRenderer extends SafeBlockEntityRenderer<DifferentialBlockEntity> {

    public DifferentialRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(DifferentialBlockEntity differential, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        final VertexConsumer vc = buffer.getBuffer(RenderType.solid());

        final BlockState state = differential.getBlockState();
        final Axis axis = state.getValue(DifferentialBlock.AXIS);
        final Direction facing = Direction.get(AxisDirection.POSITIVE, axis);
        final float time = AnimationTickHolder.getRenderTime(differential.getLevel());

        final float gearOffset = KineticBlockEntityRenderer.getRotationOffsetForPosition(differential.ringCog, differential.getBlockPos(), axis);
        final float ringAngle = angle(time, differential.ringCog.getSpeed(), gearOffset);
        final float topGearAngle = angle(time, differential.topCog.getSpeed(), gearOffset);
        final float bottomGearAngle = angle(time, differential.bottomCog.getSpeed(), gearOffset);

        KineticBlockEntityRenderer.kineticRotationTransform(CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_RING_COG, state, facing), differential.ringCog, axis, ringAngle, light).renderInto(ms, vc);
        KineticBlockEntityRenderer.kineticRotationTransform(CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_INNER_COG, state, facing), differential.topCog, axis, topGearAngle, light).renderInto(ms, vc);
        KineticBlockEntityRenderer.kineticRotationTransform(CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_INNER_COG, state, facing.getOpposite()), differential.bottomCog, axis, bottomGearAngle, light).renderInto(ms, vc);

        final BlockPos topShaftPos = differential.getBlockPos().relative(facing);
        final BlockPos bottomShaftPos = differential.getBlockPos().relative(facing.getOpposite());
        final float topShaftOffset = BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, topShaftPos);
        final float bottomShaftOffset = BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, bottomShaftPos);
        final float topShaftAngle = angle(time, differential.topCog.getSpeed(), topShaftOffset);
        final float bottomShaftAngle = angle(time, differential.bottomCog.getSpeed(), bottomShaftOffset);

        KineticBlockEntityRenderer.kineticRotationTransform(CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_SHAFT, state, facing), differential.topCog, axis, topShaftAngle, light).renderInto(ms, vc);
        KineticBlockEntityRenderer.kineticRotationTransform(CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_SHAFT, state, facing.getOpposite()), differential.bottomCog, axis, bottomShaftAngle, light).renderInto(ms, vc);

        final Axis spinAxis = axis == Axis.X ? Axis.Z : Axis.X;
        final Direction spinFacing = Direction.get(AxisDirection.POSITIVE, spinAxis);
        final float spinAngle = (bottomGearAngle - topGearAngle) / 2f;

        final SuperByteBuffer positiveSpiderCog = CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_INNER_COG, state, spinFacing);
        KineticBlockEntityRenderer.kineticRotationTransform(positiveSpiderCog, differential.ringCog, axis, ringAngle, light);
        KineticBlockEntityRenderer.kineticRotationTransform(positiveSpiderCog, differential.ringCog, spinAxis, spinAngle + Mth.PI / 8, light);
        positiveSpiderCog.renderInto(ms, vc);

        final SuperByteBuffer negativeSpiderCog = CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_INNER_COG, state, spinFacing);
        KineticBlockEntityRenderer.kineticRotationTransform(negativeSpiderCog, differential.ringCog, axis, ringAngle, light);
        KineticBlockEntityRenderer.kineticRotationTransform(negativeSpiderCog, differential.ringCog, spinAxis, -spinAngle + Mth.PI / 8, light);
        negativeSpiderCog.rotateCentered((float) Math.PI, facing);
        negativeSpiderCog.renderInto(ms, vc);
    };

    private static float angle(float time, float speed, float offsetDegrees) {
        return ((time * speed * 3f / 10 + offsetDegrees) % 360) / 180 * (float) Math.PI;
    };

};
