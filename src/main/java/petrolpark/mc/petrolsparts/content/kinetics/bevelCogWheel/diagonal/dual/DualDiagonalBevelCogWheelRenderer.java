package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelClientSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelRenderer;

public class DualDiagonalBevelCogWheelRenderer extends SafeBlockEntityRenderer<DualDiagonalBevelCogWheelBlockEntity> {

    public static final DualDiagonalBevelCogWheelRenderer create(BlockEntityRendererProvider.Context context) {
        return new DualDiagonalBevelCogWheelRenderer(BevelCogWheelClientSet.CREATE, context);
    };

    public final BevelCogWheelClientSet set;

    public DualDiagonalBevelCogWheelRenderer(BevelCogWheelClientSet set, BlockEntityRendererProvider.Context context) {
        this.set = set;
    };

    @Override
    protected void renderSafe(DualDiagonalBevelCogWheelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        be.getParts().forEach(part -> SingleDiagonalBevelCogWheelRenderer.renderCog(set, part, part.getOrientation(), ms, buffer, light));
    };
    
};
