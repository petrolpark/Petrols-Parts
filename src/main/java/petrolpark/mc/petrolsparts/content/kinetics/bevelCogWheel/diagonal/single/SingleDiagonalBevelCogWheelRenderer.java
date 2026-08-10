package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelClientSet;

public class SingleDiagonalBevelCogWheelRenderer extends KineticBlockEntityRenderer<SingleDiagonalBevelCogWheelBlockEntity> {

    public static final SingleDiagonalBevelCogWheelRenderer vanilla(BlockEntityRendererProvider.Context context) {
        return new SingleDiagonalBevelCogWheelRenderer(BevelCogWheelClientSet.vanilla(), context);  
    };

    public final BevelCogWheelClientSet set;

    public SingleDiagonalBevelCogWheelRenderer(BevelCogWheelClientSet set, BlockEntityRendererProvider.Context context) {
        super(context);
        this.set = set;
    };

    @Override
    protected void renderSafe(SingleDiagonalBevelCogWheelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        final Orientation orientation = be.getBlockState().getValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION);
        renderCog(set, be, orientation, ms, buffer, light);
    };

    public static final void renderCog(BevelCogWheelClientSet set, KineticBlockEntity be, Orientation orientation, PoseStack ms, MultiBufferSource buffer, int light) {
        renderCog(set, be.getLevel(), be.getSpeed(), be.getBlockPos(), be.getBlockState(), orientation, ms, buffer.getBuffer(RenderType.solid()), light);
    };

    public static final void renderCog(BevelCogWheelClientSet set, Level level, float speed, BlockPos pos, BlockState state, Orientation orientation, PoseStack ms, VertexConsumer vc, int light) {
        final boolean topOffset = KineticBlockEntityVisual.shouldOffset(orientation.front.getAxis(), pos);
        final boolean frontOffset = KineticBlockEntityVisual.shouldOffset(orientation.top.getAxis(), pos);
        final boolean fiveTeeth = topOffset != frontOffset;
        final float spinOffset = frontOffset ? Mth.DEG_TO_RAD * (fiveTeeth ? 36f : 45f) : 0f;
        final float spinSpeedMultiplier = fiveTeeth ? 1.6f : 2f;
        
        CachedBuffers.partial(fiveTeeth ? set.fiveTeeth() : set.fourTeeth(), state)
            .center()
            .rotate(orientation.rotationFromUpSouth)
            .translate(0f, 1.25 / 16f, 1.25 / 16f)
            .rotateXDegrees(45)
            .rotateY(getAngle(level, speed * spinSpeedMultiplier) + spinOffset)
            .translate(fiveTeeth ? 0f : -8 / 16f, 0f, fiveTeeth ? 0f : -8 / 16f)
            .light(light)
            .renderInto(ms, vc);
    };
    
    public static final float getAngle(Level level, float speed) {
        float time = AnimationTickHolder.getRenderTime(level);
        return ((time * speed * 3f / 10) % 360) / 180 * (float) Math.PI;
    };
};
