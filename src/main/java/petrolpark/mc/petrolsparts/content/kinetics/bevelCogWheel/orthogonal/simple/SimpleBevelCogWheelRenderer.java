package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelClientSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;

public class SimpleBevelCogWheelRenderer extends KineticBlockEntityRenderer<KineticBlockEntity> {

    public static final SimpleBevelCogWheelRenderer vanilla(BlockEntityRendererProvider.Context context) {
        return new SimpleBevelCogWheelRenderer(BevelCogWheelClientSet.vanilla(), context);
    };

    protected final BevelCogWheelClientSet set;

    public SimpleBevelCogWheelRenderer(BevelCogWheelClientSet set, BlockEntityRendererProvider.Context context) {
        super(context);
        this.set = set;
    };

    @Override
    protected void renderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        final BlockState state = be.getBlockState();
        final IRotate block = (IRotate)state.getBlock();
        final VertexConsumer vc = buffer.getBuffer(RenderType.solid());

        final Axis shaftAxis;

        switch (block) {
            case IOrthogonalBevelCogWheelBlock bevel: {
                shaftAxis = bevel.getShaftAxis(state);
                break;
            } case ShaftBlock shaft: {
                shaftAxis = state.getValue(ShaftBlock.AXIS);
                break;
            } default: return;
        };

        if (shaftAxis != null) {
            final Direction positiveShaftFace = Direction.get(AxisDirection.POSITIVE, shaftAxis);
            final float shaftAngle = getAngleForBe(be, be.getBlockPos(), shaftAxis) * SimpleBevelCogWheelBlockEntity.getRotationRatio(be, state, positiveShaftFace);
            kineticRotationTransform(CachedBuffers.partialFacingVertical(set.shaft(), state, positiveShaftFace), be, shaftAxis, shaftAngle, light)
                .renderInto(ms, vc);
        };

        if (!(block instanceof SimpleBevelCogWheelBlock bevelBlock)) return;
        final Direction primaryCogFace = bevelBlock.getPrimaryCogFace(state);

        for (Direction face : Iterate.directions) {
            if (!block.hasShaftTowards(be.getLevel(), be.getBlockPos(), state, face)) continue;
            if (shaftAxis != null && face.getOpposite() == primaryCogFace) continue;

            // getAngleForBe's position-based checkerboard offset (see KineticBlockEntityVisual#rotationOffset) is only
            // meaningful relative to a real Shaft axis; with none, it degrades to an arbitrary, inconsistent offset,
            // so fall back to our own fixed 0/45 degree convention (primaryCogFace) instead
            float angle = shaftAxis != null ? getAngleForBe(be, be.getBlockPos(), shaftAxis) : getAngleForBeWithoutPositionOffset(be);
            angle *= SimpleBevelCogWheelBlockEntity.getRotationRatio(be, state, face);
            if (face.getAxis() != primaryCogFace.getAxis()) angle += Mth.PI / 4f;

            kineticRotationTransform(CachedBuffers.partialFacingVertical(set.fourTeeth(), state, face.getOpposite()), be, face.getAxis(), angle, light)
                .renderInto(ms, vc);

            if (Objects.equals(face.getAxis(), shaftAxis)) kineticRotationTransform(CachedBuffers.partialFacingVertical(set.cogCap(), state, face.getOpposite()), be, face.getAxis(), angle, light)
                .renderInto(ms, vc);
        };
    };

    /**
     * Like {@link #getAngleForBe}, but without {@link com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual#rotationOffset}'s
     * position-based checkerboard offset, which is only meaningful relative to a real Shaft axis.
     */
    private static float getAngleForBeWithoutPositionOffset(KineticBlockEntity be) {
        final float time = AnimationTickHolder.getRenderTime(be.getLevel());
        return ((time * be.getSpeed() * 3f / 10f) % 360f) / 180f * Mth.PI;
    };

};
