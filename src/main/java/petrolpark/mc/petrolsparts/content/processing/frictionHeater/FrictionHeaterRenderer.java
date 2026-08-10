package petrolpark.mc.petrolsparts.content.processing.frictionHeater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class FrictionHeaterRenderer extends SafeBlockEntityRenderer<FrictionHeaterBlockEntity> {

    public FrictionHeaterRenderer(BlockEntityRendererProvider.Context context) {};

    @Override
    protected void renderSafe(FrictionHeaterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        final Axis axis = be.getBlockState().getValue(FrictionHeaterBlock.HORIZONTAL_AXIS);
        final VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());
        KineticBlockEntityRenderer.renderRotatingBuffer(be.topPart, CachedBuffers.partialFacing(PetrolsPartsPartialModels.FRICTION_HEATER_COG, be.getBlockState(), Direction.get(AxisDirection.NEGATIVE, axis)), ms, vc, light);
        KineticBlockEntityRenderer.renderRotatingBuffer(be.bottomPart, CachedBuffers.partialFacing(PetrolsPartsPartialModels.FRICTION_HEATER_COG, be.getBlockState(), Direction.get(AxisDirection.POSITIVE, axis)), ms, vc, light);
    };
    
};
