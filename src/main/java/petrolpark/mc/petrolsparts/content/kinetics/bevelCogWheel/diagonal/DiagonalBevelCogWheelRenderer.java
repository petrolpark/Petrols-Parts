package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class DiagonalBevelCogWheelRenderer extends KineticBlockEntityRenderer<SingleDiagonalBevelCogWheelBlockEntity> {

    public DiagonalBevelCogWheelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(SingleDiagonalBevelCogWheelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        final SuperByteBuffer sbb = CachedBuffers.partial(PetrolsPartsPartialModels.BEVEL_COGWHEEL, be.getBlockState())
            .translateY( 6/ 16f)
            .center()
            .rotateXDegrees(45)
            .uncenter()
            .center()
            .rotateY(getAngleForBe(be, BlockPos.ZERO, Axis.Y))
            .uncenter()
            .light(light);
        sbb.renderInto(ms, buffer.getBuffer(RenderType.solid()));
    };
    
    
};
