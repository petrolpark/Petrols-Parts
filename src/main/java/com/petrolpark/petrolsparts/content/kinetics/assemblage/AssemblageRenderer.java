package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.petrolsparts.PetrolsPartsPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AssemblageRenderer extends SafeBlockEntityRenderer<AssemblageBlockEntity> {

    public AssemblageRenderer(BlockEntityRendererProvider.Context context) {
        
    };

    @Override
    protected void renderSafe(AssemblageBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        // if (VisualizationManager.supportsVisualization(be.getLevel())) return;

        if (be.getParts().isEmpty()) return;

        final BlockState state = be.getBlockState();
        final Direction facing = Direction.get(AxisDirection.POSITIVE, state.getValue(IAssemblageBlock.AXIS));
        final AssemblageCog topCog = state.getValue(IAssemblageBlock.TOP_COG);
        final AssemblageCog middleCog = state.getValue(IAssemblageBlock.MIDDLE_COG);
        final AssemblageCog bottomCog = state.getValue(IAssemblageBlock.BOTTOM_COG);
        final VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutoutMipped());

        if (!topCog.isNone()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                be.topCogPart,
                CachedBuffers.partialFacingVertical(getModel(topCog), be.topCogPart.getBlockState(), facing)
                    .translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(6 / 16d)),
                ms, buffer, light
            );
        };

        if (!middleCog.isNone()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                be.middleCogPart,
                CachedBuffers.partialFacingVertical(getModel(middleCog), be.middleCogPart.getBlockState(), facing),
                ms, buffer, light
            );
        };

        if (!bottomCog.isNone()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                be.bottomCogPart,
                CachedBuffers.partialFacingVertical(getModel(bottomCog), be.bottomCogPart.getBlockState(), facing)
                    .translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(-6 / 16d)),
                ms, buffer, light
            );
        };

        //TODO shafts
    };

    public static final PartialModel getModel(AssemblageCog cog) {
        return switch (cog) {
            case LARGE -> AllPartialModels.SHAFTLESS_LARGE_COGWHEEL;
            case SMALL_COAXIAL -> PetrolsPartsPartialModels.COAXIAL_GEAR;
            case LARGE_COAXIAL -> PetrolsPartsPartialModels.LARGE_COAXIAL_GEAR;
            default -> AllPartialModels.SHAFTLESS_COGWHEEL;
        };
    };
    
};
