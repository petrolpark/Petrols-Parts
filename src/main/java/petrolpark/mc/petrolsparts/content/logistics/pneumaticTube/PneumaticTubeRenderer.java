package petrolpark.mc.petrolsparts.content.logistics.pneumaticTube;

import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.tube.ITubeRenderer;
import petrolpark.mc.library.util.KineticsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlockEntity.Input.StackTransporting;

public class PneumaticTubeRenderer extends KineticBlockEntityRenderer<PneumaticTubeBlockEntity> implements ITubeRenderer<PneumaticTubeBlockEntity> {

    public final PartialModel[] segmentModels;

    public PneumaticTubeRenderer(Context context) {
        super(context);
        segmentModels = new PartialModel[]{PetrolsPartsPartialModels.PNEUMATIC_TUBE_SEGMENT, PetrolsPartsPartialModels.PNEUMATIC_TUBE_SEGMENT, PetrolsPartsPartialModels.PNEUMATIC_TUBE_SEGMENT_STICHED};
    };

    @Override
    protected void renderSafe(PneumaticTubeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();
        Direction facing = state.getValue(PneumaticTubeBlock.FACING);
        VertexConsumer vc = buffer.getBuffer(RenderType.solid());

        renderTube(be, ms, buffer, light);

        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);

        if (be.isInput() || be.isOutput()) {
            ms.pushPose();
            TransformStack.of(ms)
                .center()
                .rotateToFace(facing.getOpposite())
                .rotateXDegrees(90f)
                .uncenter();
            final SuperByteBuffer arrowBuffer = CachedBuffers.partial(PetrolsPartsPartialModels.PNEUMATIC_TUBE_ARROWS, state)
                .translateY(3.5f / 16f);
            if (be.isOutput()) arrowBuffer.translateY(1f / 16f).center().rotateXDegrees(180f).uncenter();
            arrowBuffer.light(light)
                .renderInto(ms, vc);
            ms.popPose();
        };

        //if (VisualizationManager.supportsVisualization(be.getLevel())) return;
		renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(getRenderType(be, state)), light);
    };

    @Override
    public PartialModel[] getTubeSegmentModels(PneumaticTubeBlockEntity be) {
        return segmentModels;
    };

    protected static final int PADDING_SEGMENTS = 2;
    protected static final float BULGE_WIDTH = 8f;
    protected static final float BULGE_HEIGHT = 0.15f;

    @Override
    public void modifySegmentScales(PneumaticTubeBlockEntity be, float[] segmentScales, float partialTicks) {
        final Optional<PneumaticTubeBlockEntity.Input> inputOp = be.getInput();
        if (inputOp.isEmpty()) return;
        final PneumaticTubeBlockEntity.Input input = inputOp.get();
        final boolean controllerIsOutput = !be.isInput();
        final float pathLength = (float)(segmentScales.length + 2 * PADDING_SEGMENTS);
        for (StackTransporting stackTransporting : input.getStacksTransporting()) {
            float value = stackTransporting.animation.getValue(partialTicks);
            if (controllerIsOutput) value = 1f - value;
            int centerSegment = (int)(value * pathLength) - PADDING_SEGMENTS;
            eachSegment: for (int segment = centerSegment - 5; segment <= centerSegment + 5; segment++) {
                if (!growSegment(segmentScales, segment, bulge((((segment + PADDING_SEGMENTS) / pathLength) - value) * BULGE_WIDTH) * BULGE_HEIGHT)) break eachSegment;
            };
        };
    };

    protected boolean growSegment(float[] segmentScales, int index, float scaleBoost) {
        if (index < 0) return true;
        if (index >= segmentScales.length) return false;
        segmentScales[index] = Math.max(segmentScales[index], 1f + scaleBoost);
        return true;
    };

    protected float bulge(float x) {
        if (Math.abs(x) > 1f) return 0f;
        float xx = (1 - x * x);
        return xx * xx;
    };

    @Override
    protected SuperByteBuffer getRotatedModel(PneumaticTubeBlockEntity be, BlockState state) {
        Direction face = state.getValue(PneumaticTubeBlock.FACING);
        return CachedBuffers.partialDirectional(AllPartialModels.SHAFTLESS_COGWHEEL, state, face, () -> KineticsHelper.rotateToFace(face.getOpposite()));
    };

    @Override
    public boolean shouldRenderOffScreen(PneumaticTubeBlockEntity pBlockEntity) {
        return true;
    };
    
};
