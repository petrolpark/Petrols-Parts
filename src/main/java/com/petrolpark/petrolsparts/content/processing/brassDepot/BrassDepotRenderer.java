package com.petrolpark.petrolsparts.content.processing.brassDepot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BrassDepotRenderer extends DepotRenderer {

    public BrassDepotRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(DepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);
    };
    
};
