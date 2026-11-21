package petrolpark.mc.petrolsparts.compat.bitsnbobs;

import javax.annotation.Nullable;

import com.kipti.bnb.content.kinetics.cogwheel_chain.behaviour.CogwheelChainBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblagePart;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;

public class AssemblageCogwheelChains {

    public static void register() {
        AssemblageBlockEntity.MIDDLE_COG_ONLY_BEHAVIOURS.add(CogwheelChainBehaviour.TYPE);
        // Azimuth only forwards BreakEvents to Behaviours of the Block Entity actually in the Level, which for Assemblages has none
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, BlockEvent.BreakEvent.class, AssemblageCogwheelChains::onBlockBroken);
    };

    @Nullable
    public static BlockEntity redirectToMiddleCogPart(@Nullable BlockEntity be) {
        return be instanceof AssemblageBlockEntity assemblage ? assemblage.getMiddleCogPart() : be;
    };

    @Nullable
    public static CogwheelChainBehaviour getChainBehaviour(@Nullable AssemblageBlockEntityPart part) {
        return part == null ? null : part.getBehaviour(CogwheelChainBehaviour.TYPE);
    };

    @Nullable
    public static AABB getRenderBoundingBox(AssemblageBlockEntity assemblage) {
        final CogwheelChainBehaviour behaviour = getChainBehaviour(assemblage.getMiddleCogPart());
        return behaviour == null ? null : behaviour.getRenderBoundingBox();
    };

    public static void onMiddleCogLost(AssemblageBlockEntityPart oldMiddleCogPart) {
        final CogwheelChainBehaviour behaviour = getChainBehaviour(oldMiddleCogPart);
        if (behaviour != null && behaviour.isPartOfChain()) behaviour.removeFromLevel(false);
    };

    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide()) return;
        final BlockPos pos = event.getPos();
        if (!(level.getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage)) return;
        final CogwheelChainBehaviour behaviour = getChainBehaviour(assemblage.getMiddleCogPart());
        if (behaviour == null || !behaviour.isPartOfChain()) return;
        if (!breakingRemovesMiddleCog(event.getState(), pos, event.getPlayer())) return; // Chain will get transferred to the new middle cog Part
        behaviour.onBlockBroken(event);
    };

    protected static boolean breakingRemovesMiddleCog(BlockState state, BlockPos pos, Player player) {
        if (state.getBlock() instanceof AssemblageBlock block) {
            final AssemblagePart part = block.getTargetedPart(state, pos, player);
            if (part != null) {
                final BlockState newState = block.withoutPart(state, part);
                return !(newState.getBlock() instanceof IAssemblageBlock) || newState.getValue(IAssemblageBlock.MIDDLE_COG).isNone();
            };
        };
        return true; // whole Block is broken
    };
};
