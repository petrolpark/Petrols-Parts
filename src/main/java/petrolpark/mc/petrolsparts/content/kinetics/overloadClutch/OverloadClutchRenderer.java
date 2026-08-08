package petrolpark.mc.petrolsparts.content.kinetics.overloadClutch;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class OverloadClutchRenderer extends SafeBlockEntityRenderer<OverloadClutchBlockEntity> {

    public OverloadClutchRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(OverloadClutchBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, bufferSource, light, overlay);
        final Direction facing = be.getBlockState().getValue(OverloadClutchBlock.FACING);
        final VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());
        KineticBlockEntityRenderer.renderRotatingBuffer(be.generatingPart, CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), facing), ms, vc, light);
        KineticBlockEntityRenderer.renderRotatingBuffer(be.impactPart, CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), facing.getOpposite()), ms, vc, light);
    };
    
};
