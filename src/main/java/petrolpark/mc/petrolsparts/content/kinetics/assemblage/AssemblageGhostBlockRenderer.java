package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.engine_room.flywheel.lib.model.baked.EmptyVirtualBlockGetter;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.client.render.model.BakedModelBufferer;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.compat.create.core.client.ghostBlocks.PetrolparkGhostBlockRenderer;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public class AssemblageGhostBlockRenderer extends PetrolparkGhostBlockRenderer {

    protected final AssemblageClientSet set;

    public AssemblageGhostBlockRenderer(AssemblageClientSet set) {
        this.set = set;
    };

    @Override
    public void render(BlockState state, BlockPos pos, PoseStack ms, SuperRenderTypeBuffer buffer, VertexConsumer vertexConsumer, Vec3 camera) {
        if (!(state.getBlock() instanceof IAssemblageBlock assemblageBlock)) return;
        final Direction facing = Direction.get(AxisDirection.POSITIVE, state.getValue(IAssemblageBlock.AXIS));
        final PoseTransformStack mts = TransformStack.of(ms);

        // Assume the state we are rendering will only have one thing in it
        final BakedModel model;
        if (!state.getValue(IAssemblageBlock.TOP_COG).isNone()) {
            model = set.getModel(state.getValue(IAssemblageBlock.TOP_COG)).get();
            mts.translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(5 / 16d));
        } else if (!state.getValue(IAssemblageBlock.MIDDLE_COG).isNone()) {
            model = set.getModel(state.getValue(IAssemblageBlock.MIDDLE_COG)).get();
        } else if (!state.getValue(IAssemblageBlock.BOTTOM_COG).isNone()) {
            model = set.getModel(state.getValue(IAssemblageBlock.BOTTOM_COG)).get();
            mts.translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(-5 / 16d));
        } else if (assemblageBlock.hasTopShaft(state)) {
            model = PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_HALF_TOP.get();
        } else if (assemblageBlock.hasBottomShaft(state)) {
            model = PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_HALF_BOTTOM.get();
        } else {
            return;
        };

        mts.center();
        ms.scale(0.85f, 0.85f, 0.85f);
        mts.rotateTo(Direction.UP, facing);
        mts.uncenter();
        BakedModelBufferer.bufferModel(model, pos, EmptyVirtualBlockGetter.FULL_BRIGHT, state, ms, (layer, shade) -> vertexConsumer);
    };
    
};
