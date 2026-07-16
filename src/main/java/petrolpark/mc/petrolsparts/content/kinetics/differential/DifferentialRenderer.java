package petrolpark.mc.petrolsparts.content.kinetics.differential;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.util.KineticsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;
import petrolpark.mc.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;

public class DifferentialRenderer extends KineticBlockEntityRenderer<LegacyDifferentialBlockEntity> {

    public DifferentialRenderer(Context context) {
        super(context);
    };

    @Override
    @SuppressWarnings("null") // It thinks getLevel() might be null
    protected void renderSafe(LegacyDifferentialBlockEntity differential, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //if (Backend.canUseInstancing(planetaryGearsetBlockEntity.getLevel())) return;
        if (!differential.hasLevel()) return;

		BlockState state = getRenderedBlockState(differential);
        Direction face = DirectionalRotatedPillarKineticBlock.getDirection(state);
        Axis axis = face.getAxis();
		VertexConsumer vbSolid = buffer.getBuffer(RenderType.solid());

        float time = AnimationTickHolder.getRenderTime(differential.getLevel());
		float ringGearOffset = Mth.PI * getRotationOffsetForPosition(differential, differential.getBlockPos(), axis) / 180f;
		float ringGearAngle = ((time * differential.getSpeed() * 3f / 10 + ringGearOffset) % 360) / 180 * Mth.PI;

        BlockPos inputPos = differential.getBlockPos().relative(face);
        BlockPos controlPos = differential.getBlockPos().relative(face.getOpposite());

        BlockEntity inputBE = differential.getLevel().getBlockEntity(inputPos);
        BlockEntity controlBE = differential.getLevel().getBlockEntity(controlPos);

        float inputShaftOffset = Mth.PI * BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, inputPos) / 180f;
        float controlShaftOffset = Mth.PI * BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, controlPos) / 180f;

        float inputCogAngle = 0f;
        float controlCogAngle = 0f;

        if (differential.propagatesToMe(inputPos, face.getOpposite()) && inputBE instanceof KineticBlockEntity inputKBE) inputCogAngle = (time * differential.getPropagatedSpeed(inputKBE, face) * 3f / 10 % 360) / 180 * Mth.PI;
        if (differential.propagatesToMe(controlPos, face) && controlBE instanceof KineticBlockEntity controlKBE) controlCogAngle = (time * differential.getPropagatedSpeed(controlKBE, face.getOpposite()) * 3f / 10 % 360) / 180 * Mth.PI;

        SuperByteBuffer ringGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_RING_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(ringGear, differential, axis, ringGearAngle + ringGearOffset, light);
        ringGear.renderInto(ms, vbSolid);

        SuperByteBuffer eastGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_EAST_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(eastGear, differential, axis, ringGearAngle + ringGearOffset, light);
        kineticRotationTransform(eastGear, differential, axis == Axis.X ? Axis.Z : Axis.X, ((controlCogAngle - inputCogAngle) / 2) * (axis == Axis.Z ? -1 : 1), light);
        eastGear.renderInto(ms, vbSolid);

        SuperByteBuffer westGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_WEST_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(westGear, differential, axis, ringGearAngle + ringGearOffset, light);
        kineticRotationTransform(westGear, differential, axis == Axis.X ? Axis.Z : Axis.X, ((inputCogAngle - controlCogAngle) / 2) * (axis == Axis.Z ? -1 : 1), light);
        westGear.renderInto(ms, vbSolid);

        SuperByteBuffer topGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_CONTROL_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(topGear, differential, axis, controlCogAngle + ringGearOffset, light);
        topGear.renderInto(ms, vbSolid);

        SuperByteBuffer topShaft = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_CONTROL_SHAFT, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(topShaft, differential, axis, controlCogAngle + controlShaftOffset, light);
        topShaft.renderInto(ms, vbSolid);

        SuperByteBuffer bottomGear = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_INPUT_GEAR, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(bottomGear, differential, axis, inputCogAngle + ringGearOffset, light);
        bottomGear.renderInto(ms, vbSolid);

        SuperByteBuffer bottomShaft = CachedBuffers.partialDirectional(PetrolsPartsPartialModels.DIFFERENTIAL_INPUT_SHAFT, state, face, () -> KineticsHelper.rotateToFace(face));
        kineticRotationTransform(bottomShaft, differential, axis, inputCogAngle + inputShaftOffset, light);
        bottomShaft.renderInto(ms, vbSolid);
    };
    
};
