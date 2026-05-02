package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;

public class SingleShaftAssemblageRenderer extends AssemblageRenderer {

    public SingleShaftAssemblageRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(AssemblageBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);

        if (be.getParts().isEmpty()) return;

        KineticBlockEntityRenderer.renderRotatingBuffer(
            be.topShaftPart,
            CachedBuffers.partialFacingVertical(AllPartialModels.SHAFT, be.topShaftPart.getBlockState(), Direction.fromAxisAndDirection(be.getBlockState().getValue(IAssemblageBlock.AXIS), AxisDirection.POSITIVE)),
            ms, bufferSource.getBuffer(RenderType.solid()), light
        );
    };
    
};
