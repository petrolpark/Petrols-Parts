package com.petrolpark.petrolsparts.content.kinetics.movement;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class MovementRenderer extends SafeBlockEntityRenderer<MovementBlockEntity> {

    public MovementRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(MovementBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        
    };
    
};
