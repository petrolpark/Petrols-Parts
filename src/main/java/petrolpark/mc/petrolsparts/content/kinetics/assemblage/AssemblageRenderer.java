package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class AssemblageRenderer extends SafeBlockEntityRenderer<AssemblageBlockEntity> {

    public AssemblageRenderer(BlockEntityRendererProvider.Context context) {
        
    };

    @Override
    protected void renderSafe(AssemblageBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        // if (VisualizationManager.supportsVisualization(be.getLevel())) return;

        if (be.getParts().isEmpty()) return;

        final BlockState state = be.getBlockState();
        final Axis axis = state.getValue(IAssemblageBlock.AXIS);
        final Direction facing = Direction.get(AxisDirection.POSITIVE, axis);

        final AssemblageCog topCog = state.getValue(IAssemblageBlock.TOP_COG);
        final AssemblageCog middleCog = state.getValue(IAssemblageBlock.MIDDLE_COG);
        final AssemblageCog bottomCog = state.getValue(IAssemblageBlock.BOTTOM_COG);
        final boolean hasTopShaft;
        final boolean hasBottomShaft;
        if (state.getBlock() instanceof IAssemblageBlock asssemblage) {
            hasTopShaft = asssemblage.hasTopShaft(state);
            hasBottomShaft = asssemblage.hasBottomShaft(state);
        } else { // Should never be called
            hasTopShaft = hasBottomShaft = false;
        };

        final VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutoutMipped());

        if (!topCog.isNone()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                be.topCogPart,
                CachedBuffers.partialFacingVertical(getModel(topCog), be.topCogPart.getBlockState(), facing)
                    .translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(5 / 16d))
                    .rotateCenteredDegrees(!KineticBlockEntityVisual.shouldOffset(axis, be.getBlockPos()) && be.topCogPart.topCogType.isLarge() && !be.topCogPart.middleCogType.isLarge() ? 11.25f : 0f, facing),
                ms, buffer, light
            );
            if (topCog.hasShaftConnection() && !hasTopShaft) KineticBlockEntityRenderer.renderRotatingBuffer(
                be.topCogPart,
                CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.COGWHEEL_SHAFT, be.topCogPart.getBlockState(), facing),
                ms, buffer, light
            );
        };

        if (!middleCog.isNone()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                be.middleCogPart,
                CachedBuffers.partialFacingVertical(getModel(middleCog), be.middleCogPart.getBlockState(), facing),
                // Large Cog offset already applied
                ms, buffer, light
            );
        };

        if (!bottomCog.isNone()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                be.bottomCogPart,
                CachedBuffers.partialFacingVertical(getModel(bottomCog), be.bottomCogPart.getBlockState(), facing)
                    .translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(-5 / 16d))
                    .rotateCenteredDegrees(!KineticBlockEntityVisual.shouldOffset(axis, be.getBlockPos()) && be.bottomCogPart.bottomCogType.isLarge() && !be.bottomCogPart.middleCogType.isLarge() ? 11.25f : 0f, facing),
                ms, buffer, light
            );
            if (bottomCog.hasShaftConnection() && !hasBottomShaft) KineticBlockEntityRenderer.renderRotatingBuffer(
                be.bottomCogPart,
                CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.COGWHEEL_SHAFT, be.bottomCogPart.getBlockState(), facing.getOpposite()),
                ms, buffer, light
            );
        };

        if (!hasBottomShaft && !hasTopShaft) return;
        final PartialModel shaftModel;
        if (!hasBottomShaft) {
            shaftModel = PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_HALF_TOP;
        } else if (!hasTopShaft) {
            shaftModel = PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_HALF_BOTTOM;
        } else if (middleCog.hasShaftConnection()) {
            if (topCog.hasShaftConnection()) {
                shaftModel = bottomCog.hasShaftConnection() ? PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_ALL : PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_NO_BOTTOM;
            } else if (bottomCog.hasShaftConnection()) {
                shaftModel = PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_NO_TOP;
            } else {
                shaftModel = AllPartialModels.COGWHEEL_SHAFT;
            };
        } else if (topCog.hasShaftConnection()) {
            shaftModel = bottomCog.hasShaftConnection() ? PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_ALL : PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_TOP;
        } else if (bottomCog.hasShaftConnection()) {
            shaftModel = PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_BOTTOM;  
        } else {
            shaftModel = AllPartialModels.SHAFT;
        };

        KineticBlockEntityRenderer.kineticRotationTransform(CachedBuffers.partialFacingVertical(shaftModel, be.shaftPart.getBlockState(), facing), be.shaftPart, axis, getAngle(be.shaftPart, be.getBlockPos(), axis), light)
            .renderInto(ms, buffer);
    };

    public static final PartialModel getModel(AssemblageCog cog) {
        return switch (cog) {
            case LARGE -> AllPartialModels.SHAFTLESS_LARGE_COGWHEEL;
            case SMALL_COAXIAL -> PetrolsPartsPartialModels.COAXIAL_COGWHEEL;
            case LARGE_COAXIAL -> PetrolsPartsPartialModels.LARGE_COAXIAL_COGWHEEL;
            default -> AllPartialModels.SHAFTLESS_COGWHEEL;
        };
    };

    public static float getAngle(KineticBlockEntity be, final BlockPos pos, Axis axis) {
		float time = AnimationTickHolder.getRenderTime(be.getLevel());
		float angle = ((time * be.getSpeed() * 3f / 10 + BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)) % 360) / 180 * (float) Math.PI;
		return angle;
	};
    
};
