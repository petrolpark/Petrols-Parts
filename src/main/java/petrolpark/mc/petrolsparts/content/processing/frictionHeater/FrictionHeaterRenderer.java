package petrolpark.mc.petrolsparts.content.processing.frictionHeater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FrictionHeaterRenderer extends SafeBlockEntityRenderer<FrictionHeaterBlockEntity> {

    public FrictionHeaterRenderer(BlockEntityRendererProvider.Context context) {};

    @Override
    protected void renderSafe(FrictionHeaterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderSafe'");
    };
    
};
