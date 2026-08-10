package petrolpark.mc.petrolsparts.content.kinetics.differential;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class DifferentialRenderer extends SafeBlockEntityRenderer<DifferentialBlockEntity> {

    public DifferentialRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(DifferentialBlockEntity differential, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //if (Backend.canUseInstancing(planetaryGearsetBlockEntity.getLevel())) return;

        final VertexConsumer vc = buffer.getBuffer(RenderType.solid());

        final BlockState state = differential.getBlockState();
        final Axis axis = differential.getBlockState().getValue(DifferentialBlock.AXIS);
        final Direction facing = Direction.get(AxisDirection.POSITIVE, axis);

        KineticBlockEntityRenderer.renderRotatingBuffer(differential.ringCog, CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_RING_COG, state, facing), ms, vc, light);
        KineticBlockEntityRenderer.renderRotatingBuffer(differential.topCog, CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_SUN_COG, state, facing), ms, vc, light);
        KineticBlockEntityRenderer.renderRotatingBuffer(differential.bottomCog, CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_SUN_COG, state, facing.getOpposite()), ms, vc, light);

        final Axis spinAxis = axis == Axis.X ? Axis.Z : Axis.X;
        final Direction spinFacing = Direction.get(AxisDirection.POSITIVE, spinAxis);
        final float revolveAngle = KineticBlockEntityRenderer.getAngleForBe(differential.ringCog, differential.getBlockPos(), axis);
        final float ringMainAxisOffset = KineticBlockEntityRenderer.getRotationOffsetForPosition(differential.ringCog, differential.getBlockPos(), axis);
        final float sunMainAxisOffset = KineticBlockEntityRenderer.getRotationOffsetForPosition(differential.topCog, differential.getBlockPos(), axis);
        final float spinOffset = KineticBlockEntityRenderer.getRotationOffsetForPosition(differential.topCog, differential.getBlockPos(), spinAxis) + (ringMainAxisOffset - sunMainAxisOffset);
        final float spinAngle = ((AnimationTickHolder.getRenderTime(differential.getLevel()) * (differential.topCog.getSpeed() - differential.bottomCog.getSpeed()) * 3f / 20f + spinOffset) % 360) / 180 * (float) Math.PI;

        final SuperByteBuffer positiveSpiderCog = CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_SPIDER_COG, state, spinFacing);
        KineticBlockEntityRenderer.kineticRotationTransform(positiveSpiderCog, differential.ringCog, axis, revolveAngle, light);
        KineticBlockEntityRenderer.kineticRotationTransform(positiveSpiderCog, differential.ringCog, spinAxis, -spinAngle, light);
        positiveSpiderCog.renderInto(ms, vc);

        final SuperByteBuffer negativeSpiderCog = CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_SPIDER_COG, state, spinFacing.getOpposite());
        KineticBlockEntityRenderer.kineticRotationTransform(negativeSpiderCog, differential.ringCog, axis, revolveAngle, light);
        KineticBlockEntityRenderer.kineticRotationTransform(negativeSpiderCog, differential.ringCog, spinAxis, spinAngle, light);
        negativeSpiderCog.renderInto(ms, vc);
    };
    
};
