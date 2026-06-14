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
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MovementRenderer extends SafeBlockEntityRenderer<MovementBlockEntity> {

    public MovementRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(MovementBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        final BlockState state = be.getBlockState();
        final Direction facing = state.getValue(MovementBlock.FACING).getOpposite();
        final VertexConsumer vc = bufferSource.getBuffer(RenderType.cutout());

        KineticBlockEntityRenderer.standardKineticRotationTransform(CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_SHAFT, state, facing), be.generatingPart, light)
            .renderInto(ms, vc);
        KineticBlockEntityRenderer.standardKineticRotationTransform(CachedBuffers.partialFacing(PetrolsPartsPartialModels.MOVEMENT_SHAFT, state, facing.getOpposite()), be.windingPart, light)
            .renderInto(ms, vc);

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
    };
    
};
