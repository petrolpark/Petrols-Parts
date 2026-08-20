package petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class RedstoneTransmissionRenderer extends KineticBlockEntityRenderer<RedstoneTransmissionBlockEntity> {

    public RedstoneTransmissionRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(RedstoneTransmissionBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);

        final BlockState state = be.getBlockState();
        final Direction axisFacing = Direction.get(AxisDirection.POSITIVE, state.getValue(RedstoneTransmissionBlock.FACING).getAxis());
        final VertexConsumer vc = bufferSource.getBuffer(getRenderType(be, state));

        renderRotatingKineticBlock(be, state, ms, vc, light);
        
        if (be.cogPositions.isEmpty()) return;
        
        final Vec3 vec = Vec3.atLowerCornerOf(state.getValue(RedstoneTransmissionBlock.FACING).getNormal());

        for (LerpedFloat cogPosition : be.cogPositions) {
            renderRotatingBuffer(
                be,
                CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.COAXIAL_COGWHEEL, state, axisFacing)
                    .translate(vec.scale(cogPosition.getValue(partialTicks))),
                ms, vc, light
            );
        };
    };
    
};
