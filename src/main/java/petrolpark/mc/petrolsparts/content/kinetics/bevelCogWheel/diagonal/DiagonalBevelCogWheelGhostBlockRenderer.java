package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlockRenderer;
import net.createmod.catnip.impl.client.render.ColoringVertexConsumer;
import net.createmod.catnip.placement.PlacementClient;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.mixin.compat.create.accessor.GhostBlockParamsAccessor;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.ISingleDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelRenderer;

public class DiagonalBevelCogWheelGhostBlockRenderer extends GhostBlockRenderer {
    
    public static final DiagonalBevelCogWheelGhostBlockRenderer INSTANCE = new DiagonalBevelCogWheelGhostBlockRenderer();

    @Override
    public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, GhostBlockParams params) {
        final BlockState state = ((GhostBlockParamsAccessor)params).getState();
        if (!(state.getBlock() instanceof SingleDiagonalBevelCogWheelBlock)) return;
        final BlockPos pos = ((GhostBlockParamsAccessor)params).getPos();
        final float alpha = ((GhostBlockParamsAccessor)params).getAlphaSupplier().get() * 0.75f * PlacementClient.getCurrentAlpha();
        final VertexConsumer vb = new ColoringVertexConsumer(buffer.getEarlyBuffer(RenderType.translucent()), 1, 1, 1, alpha);

        ms.pushPose();
        // final PoseTransformStack mts = TransformStack.of(ms);

        ms.translate(pos.getX() - camera.x(), pos.getY() - camera.y(), pos.getZ() - camera.z());
        ms.pushPose();
        // mts.center();
        //ms.scale(0.85f, 0.85f, 0.85f);
        // mts.uncenter();
        SingleDiagonalBevelCogWheelRenderer.renderCog(Minecraft.getInstance().level, 0f, pos, state, state.getValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION), ms, vb, LightTexture.FULL_BRIGHT);
        ms.popPose();
        ms.popPose();
    };


};
