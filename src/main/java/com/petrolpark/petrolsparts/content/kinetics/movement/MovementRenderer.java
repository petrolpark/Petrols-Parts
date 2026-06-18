package com.petrolpark.petrolsparts.content.kinetics.movement;

import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.petrolsparts.PetrolsPartsPartialModels;
import com.petrolpark.petrolsparts.PetrolsPartsSpriteShifts;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MovementRenderer extends SafeBlockEntityRenderer<MovementBlockEntity> {

    public MovementRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(MovementBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        final BlockState state = be.getBlockState();
        final Direction facing = state.getValue(MovementBlock.HORIZONTAL_FACING).getOpposite();
        final VertexConsumer vc = bufferSource.getBuffer(RenderType.cutout());

        // Shafts

        KineticBlockEntityRenderer.standardKineticRotationTransform(CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_SHAFT, state, facing), be.generatingPart, light)
            .renderInto(ms, vc);
        KineticBlockEntityRenderer.standardKineticRotationTransform(CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_SHAFT, state, facing.getOpposite()), be.windingPart, light)
            .renderInto(ms, vc);

        // Chain

        final float charge = be.rotationsCharge / be.getMaxRotationsCharge();
        final float weightOffset = -1f + charge * 11 / 16f;

        Optional.ofNullable(be.weightData)
            .map(MovementWeightData::model)
            .map(PartialModel::of)
            .ifPresent(model -> CachedBuffers.partialFacing(model, state, facing)
                .translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(3.5 / 16f))
                .translateY(weightOffset)
                .light(light)
                .renderInto(ms, vc)
            );

        for (int i = 0; i < 4; i++) {
            final float chainSegmentOffset = weightOffset + i * 6 / 16f;
            if (chainSegmentOffset > 5 / 16f) break;
            CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_CHAIN, state, facing)
                .translateY(chainSegmentOffset)
                .light(light)
                .renderInto(ms, vc);
        };

        CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_CHAIN, state, facing);

        AbstractPulleyRenderer.scrollCoil(CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_COIL, state, facing), PetrolsPartsSpriteShifts.MOVEMENT_CHAIN, charge, 8f)
            .light(light)
            .renderInto(ms, vc);

        // Escapement

        final float targetAngle = be.shouldGenerate()
            ? ((AnimationTickHolder.getRenderTime(be.getLevel()) * be.generatingPart.getSpeed() * 6f / 150f) % 360) / 180 * (float) Math.PI
            : Mth.PI / 32;
        final Vec3 escapementCogOffset = new Vec3(facing.getAxis() == Axis.Z ? 8 / 16f : 0f, 5 / 16f, facing.getAxis() == Axis.X ? 8 / 16f : 0f);
        final Vec3 pendulumOffset = escapementCogOffset.add(0f, 8.5f / 16f, 0f);

        CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_PENDULUM, state, facing)
            .translate(pendulumOffset)
            .rotate(Mth.PI * Mth.cos(16 * targetAngle) / 12f, facing)
            .translateBack(pendulumOffset)
            .light(light)
            .renderInto(ms, vc);

        CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_PENDULUM_WEIGHT, state, facing)
            .translate(pendulumOffset)
            .rotate(Mth.PI * Mth.cos(16 * targetAngle) / 12f, facing)
            .translateBack(pendulumOffset)
            .light(light)
            .renderInto(ms, vc);

        final float turnProgress = targetAngle % (Mth.PI / 8f) / (Mth.PI / 8f);
        final float escapementCogAngle;
        if (turnProgress < 0.0625f) {
            escapementCogAngle = 0f;
        } else if (turnProgress < 0.5f) {
            escapementCogAngle = ((turnProgress - 0.0625f) / 0.4375f) * Mth.PI / 8f;
        } else if (turnProgress < 0.615f) {
            escapementCogAngle = Mth.PI / 8f;
        } else {
            escapementCogAngle = (Mth.PI / 8f) + (turnProgress - 0.625f) * Mth.PI / (8f * 0.375f);
        };

        CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_ESCAPEMENT_COG, state, facing)
            .translate(escapementCogOffset)
            .rotate(escapementCogAngle + Mth.floor(targetAngle / (Mth.PI / 4f)) * Mth.PI / 4f, facing)
            .translateBack(escapementCogOffset)
            .light(light)
            .renderInto(ms, vc);
    };
    
};
