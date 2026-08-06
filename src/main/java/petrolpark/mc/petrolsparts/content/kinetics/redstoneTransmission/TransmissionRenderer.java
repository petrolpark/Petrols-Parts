package petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class TransmissionRenderer extends KineticBlockEntityRenderer<TransmissionBlockEntity> {

    public TransmissionRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(TransmissionBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);

        //TODO visual
        if (be.cogs.isEmpty()) return;

        final BlockState state = be.getBlockState();
        final Direction axisFacing = Direction.get(AxisDirection.POSITIVE, state.getValue(TransmissionBlock.FACING).getAxis());
        final VertexConsumer buffer = bufferSource.getBuffer(getRenderType(be, state));

        for (int cog = 0; cog < be.cogs.length(); cog++) {
            if (!be.cogs.get(cog)) continue;

            double offset = getOffset(cog);
            if (be.displacement != 0) offset = Mth.lerp(partialTicks, offset, getOffset(cog + be.displacement)); //TODO smooth lerp

            renderRotatingBuffer(
                be,
                CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.COAXIAL_COGWHEEL, state, axisFacing)
                    .translate(Vec3.atLowerCornerOf(state.getValue(TransmissionBlock.FACING).getNormal()).scale(offset)),
                ms, buffer, light
            );
        };
    };

    public static final double getOffset(int cogIndex) {
        return (double)(cogIndex / 3) + switch (cogIndex % 3) {
            case 2 -> 13 / 16d;
            case 1 -> 8 / 16d;
            default -> 3 / 16d;
        };
    };
    
};
