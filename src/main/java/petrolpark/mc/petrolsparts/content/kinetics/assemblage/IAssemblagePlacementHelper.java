package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlocks;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IAssemblagePlacementHelper extends IPlacementHelper {
    
    @Override
    public default void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
        if (!offset.hasGhostState()) return;

        GhostBlocks.getInstance().showGhost(this, AssemblageGhostBlockRenderer.INSTANCE, GhostBlockParams.of(offset.getTransform().apply(offset.getGhostState())), 1)
            .at(offset.getBlockPos())
            .breathingAlpha();
    };
};