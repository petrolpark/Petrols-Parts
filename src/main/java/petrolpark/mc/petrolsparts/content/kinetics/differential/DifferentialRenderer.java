package petrolpark.mc.petrolsparts.content.kinetics.differential;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class DifferentialRenderer extends SafeBlockEntityRenderer<DifferentialBlockEntity> {

    public DifferentialRenderer(BlockEntityRendererProvider.Context context) {

    };

    @Override
    protected void renderSafe(DifferentialBlockEntity differential, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //if (Backend.canUseInstancing(planetaryGearsetBlockEntity.getLevel())) return;

        final VertexConsumer vc = buffer.getBuffer(RenderType.solid());

        final BlockState state = differential.getBlockState();
        final Axis axis = differential.getBlockState().getValue(DifferentialBlock.AXIS);
        final Direction facing = Direction.get(AxisDirection.POSITIVE, axis);

        KineticBlockEntityRenderer.renderRotatingBuffer(differential.ringCog, CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_RING_COG, state, facing), ms, vc, light);
        KineticBlockEntityRenderer.renderRotatingBuffer(differential.topCog, CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_INNER_COG, state, facing), ms, vc, light);
        KineticBlockEntityRenderer.renderRotatingBuffer(differential.bottomCog, CachedBuffers.partialFacingVertical(PetrolsPartsPartialModels.DIFFERENTIAL_INNER_COG, state, facing.getOpposite()), ms, vc, light);

		// BlockState state = getRenderedBlockState(differential);
        // Direction face = DirectionalRotatedPillarKineticBlock.getDirection(state);
        // Axis axis = face.getAxis();
		// VertexConsumer vbSolid = buffer.getBuffer(RenderType.solid());

        // float time = AnimationTickHolder.getRenderTime(differential.getLevel());
		// float ringGearOffset = Mth.PI * getRotationOffsetForPosition(differential, differential.getBlockPos(), axis) / 180f;
		// float ringGearAngle = ((time * differential.getSpeed() * 3f / 10 + ringGearOffset) % 360) / 180 * Mth.PI;

        // BlockPos inputPos = differential.getBlockPos().relative(face);
        // BlockPos controlPos = differential.getBlockPos().relative(face.getOpposite());

        // BlockEntity inputBE = differential.getLevel().getBlockEntity(inputPos);
        // BlockEntity controlBE = differential.getLevel().getBlockEntity(controlPos);

        // float inputShaftOffset = Mth.PI * BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, inputPos) / 180f;
        // float controlShaftOffset = Mth.PI * BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, controlPos) / 180f;

        // float inputCogAngle = 0f;
        // float controlCogAngle = 0f;

        // if (differential.propagatesToMe(inputPos, face.getOpposite()) && inputBE instanceof KineticBlockEntity inputKBE) inputCogAngle = (time * differential.getPropagatedSpeed(inputKBE, face) * 3f / 10 % 360) / 180 * Mth.PI;
        // if (differential.propagatesToMe(controlPos, face) && controlBE instanceof KineticBlockEntity controlKBE) controlCogAngle = (time * differential.getPropagatedSpeed(controlKBE, face.getOpposite()) * 3f / 10 % 360) / 180 * Mth.PI;

        // SuperByteBuffer ringGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_RING_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(ringGear, differential, axis, ringGearAngle + ringGearOffset, light);
        // ringGear.renderInto(ms, vbSolid);

        // SuperByteBuffer eastGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_EAST_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(eastGear, differential, axis, ringGearAngle + ringGearOffset, light);
        // kineticRotationTransform(eastGear, differential, axis == Axis.X ? Axis.Z : Axis.X, ((controlCogAngle - inputCogAngle) / 2) * (axis == Axis.Z ? -1 : 1), light);
        // eastGear.renderInto(ms, vbSolid);

        // SuperByteBuffer westGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_WEST_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(westGear, differential, axis, ringGearAngle + ringGearOffset, light);
        // kineticRotationTransform(westGear, differential, axis == Axis.X ? Axis.Z : Axis.X, ((inputCogAngle - controlCogAngle) / 2) * (axis == Axis.Z ? -1 : 1), light);
        // westGear.renderInto(ms, vbSolid);

        // SuperByteBuffer topGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_CONTROL_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(topGear, differential, axis, controlCogAngle + ringGearOffset, light);
        // topGear.renderInto(ms, vbSolid);

        // SuperByteBuffer topShaft = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_CONTROL_SHAFT, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(topShaft, differential, axis, controlCogAngle + controlShaftOffset, light);
        // topShaft.renderInto(ms, vbSolid);

        // SuperByteBuffer bottomGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_INPUT_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(bottomGear, differential, axis, inputCogAngle + ringGearOffset, light);
        // bottomGear.renderInto(ms, vbSolid);

        // SuperByteBuffer bottomShaft = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_INPUT_SHAFT, state, face, () -> KineticsHelper.rotateToFace(face));
        // kineticRotationTransform(bottomShaft, differential, axis, inputCogAngle + inputShaftOffset, light);
        // bottomShaft.renderInto(ms, vbSolid);
    };
    
};
