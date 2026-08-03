package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelClientSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelRenderer;

public class CompositeBevelCogWheelRenderer extends SafeBlockEntityRenderer<CompositeBevelCogWheelBlockEntity> {

    public static final CompositeBevelCogWheelRenderer vanilla(BlockEntityRendererProvider.Context context) {
        return new CompositeBevelCogWheelRenderer(BevelCogWheelClientSet.vanilla(), context);
    };

    protected final SimpleBevelCogWheelRenderer simpleRenderer;

    public CompositeBevelCogWheelRenderer(BevelCogWheelClientSet set, BlockEntityRendererProvider.Context context) {
        this.simpleRenderer = new SimpleBevelCogWheelRenderer(set, context);
    };

    @Override
    protected void renderSafe(CompositeBevelCogWheelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        be.getParts().forEach(part -> simpleRenderer.render(part, partialTicks, ms, buffer, light, overlay));
    };
    
};
