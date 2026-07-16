package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.compat.create.core.client.ghostBlocks.PetrolparkGhostBlockRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.ISingleDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelRenderer;

public class DiagonalBevelCogWheelGhostBlockRenderer extends PetrolparkGhostBlockRenderer {
    
    public static final DiagonalBevelCogWheelGhostBlockRenderer INSTANCE = new DiagonalBevelCogWheelGhostBlockRenderer();

    @Override
    public void render(BlockState state, BlockPos pos, PoseStack ms, SuperRenderTypeBuffer buffer, VertexConsumer vertexConsumer, Vec3 camera) {
        SingleDiagonalBevelCogWheelRenderer.renderCog(Minecraft.getInstance().level, 0f, pos, state, state.getValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION), ms, vertexConsumer, LightTexture.FULL_BRIGHT);
    };


};
