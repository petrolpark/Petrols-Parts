package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class EncasedCornerShaftRenderer extends KineticBlockEntityRenderer<CornerShaftBlockEntity> {

    public static EncasedCornerShaftRenderer vanilla(BlockEntityRendererProvider.Context context) {
        return new EncasedCornerShaftRenderer(PetrolsPartsPartialModels.CORNER_SHAFT_SIDE, context);
    };

    protected final PartialModel side;

    public EncasedCornerShaftRenderer(PartialModel side, BlockEntityRendererProvider.Context context) {
        super(context);
        this.side = side;
    };

    @Override
    protected void renderSafe(CornerShaftBlockEntity cornerShaftBlockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //if (Backend.canUseInstancing(cornerShaftBlockEntity.getLevel())) return; //TODO fix

        float time = AnimationTickHolder.getRenderTime(cornerShaftBlockEntity.getLevel());
        BlockState state = cornerShaftBlockEntity.getBlockState();
        VertexConsumer vbSolid = buffer.getBuffer(RenderType.solid());

        Direction[] directions = CornerShaftBlock.getDirectionsConnectedByState(state);
        Direction shaft1Direction = directions[0];
        Direction shaft2Direction = directions[1];

        float gimbal1Angle = Mth.PI * ((time * getSpeed(cornerShaftBlockEntity, shaft1Direction) * 3f / 10) % 360) / 180f;
        float gimbal2Angle = Mth.PI * ((time * getSpeed(cornerShaftBlockEntity, shaft2Direction) * 3f / 10) % 360) / 180f;
        
        float offset1 = Mth.PI * getRotationOffsetForPosition(cornerShaftBlockEntity, cornerShaftBlockEntity.getBlockPos(), shaft1Direction.getAxis()) / 180f;
        float offset2 = Mth.PI * getRotationOffsetForPosition(cornerShaftBlockEntity, cornerShaftBlockEntity.getBlockPos(), shaft2Direction.getAxis()) / 180f;

        SuperByteBuffer shaft1 = CachedBuffers.partialFacing(side, state, shaft1Direction);
        kineticRotationTransform(shaft1, cornerShaftBlockEntity, shaft1Direction.getAxis(), gimbal1Angle + offset1, light);
        shaft1.renderInto(ms, vbSolid);

        SuperByteBuffer shaft2 = CachedBuffers.partialFacing(side, state, shaft2Direction);
        kineticRotationTransform(shaft2, cornerShaftBlockEntity, shaft2Direction.getAxis(), gimbal2Angle + offset2, light);
        shaft2.renderInto(ms, vbSolid);
        
        renderMiddle(cornerShaftBlockEntity, state, ms, vbSolid, light, shaft1Direction, shaft2Direction, gimbal1Angle, gimbal2Angle);
    };

    protected void renderMiddle(CornerShaftBlockEntity be, BlockState state, PoseStack ms, VertexConsumer vc, int light, Direction shaft1Direction, Direction shaft2Direction, float gimbal1Angle, float gimbal2Angle) {
    };
    
    protected float getSpeed(CornerShaftBlockEntity blockEntity, Direction face) {
        Direction sourceFacing = null;
        if (blockEntity.hasSource()) {
            BlockPos source = blockEntity.source.subtract(blockEntity.getBlockPos()); // It thinks source can be null (it can't)
            sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
        };
        float speed = blockEntity.getSpeed();
        if (speed != 0f && sourceFacing != null) {
            if (sourceFacing.getAxisDirection() == face.getAxisDirection() && face != sourceFacing) speed *= -1;
        };
        return speed;
    };

    protected Direction getSourceFacing(CornerShaftBlockEntity blockEntity) {
        if (blockEntity.hasSource()) {
            BlockPos source = blockEntity.source.subtract(blockEntity.getBlockPos()); // It thinks source can be null (it can't)
            return Direction.getNearest(source.getX(), source.getY(), source.getZ());
        } else {
            return null;
        }
    };

    protected Axis getAxis(Direction shaft1Direction, Direction shaft2Direction) {
        List<Axis> axes = new ArrayList<>();
        axes.addAll(List.of(Axis.values()));
        axes.remove(shaft1Direction.getAxis());
        axes.remove(shaft2Direction.getAxis());
        return axes.get(0);
    };
    
};
