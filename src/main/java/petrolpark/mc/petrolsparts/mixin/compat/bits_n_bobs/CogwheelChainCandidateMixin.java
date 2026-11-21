package petrolpark.mc.petrolsparts.mixin.compat.bits_n_bobs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kipti.bnb.content.kinetics.cogwheel_chain.graph.CogwheelChainCandidate;
import com.kipti.bnb.registry.core.BnbFeatureFlag;
import com.kipti.bnb.registry.core.BnbTags.BnbBlockTags;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;

@Mixin(CogwheelChainCandidate.class)
public class CogwheelChainCandidateMixin {

    @Inject(
        method = "isValidCandidate(Lnet/minecraft/world/level/block/state/BlockState;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void petrolsParts$assemblageCandidatesAreValid(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof IAssemblageBlock) cir.setReturnValue(petrolsParts$isValidAssemblage(state));
    };

    @Inject(
        method = "getForBlock(Lnet/minecraft/world/level/block/state/BlockState;)Lcom/kipti/bnb/content/kinetics/cogwheel_chain/graph/CogwheelChainCandidate;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void petrolsParts$getAssemblageCandidate(BlockState state, CallbackInfoReturnable<CogwheelChainCandidate> cir) {
        if (state != null && state.getBlock() instanceof IAssemblageBlock) {
            if (!petrolsParts$isValidAssemblage(state)) {
                cir.setReturnValue(null);
            } else {
                final AssemblageCog cog = state.getValue(IAssemblageBlock.MIDDLE_COG);
                cir.setReturnValue(new CogwheelChainCandidate(state.getValue(IAssemblageBlock.AXIS), cog.isLarge(), !cog.isLarge() && !BnbBlockTags.COGWHEEL_CHAIN_NO_SMALL_OFFSET.matches(state)));
            };
        };
    };

    /**
     * Mirrors the gating in {@link CogwheelChainCandidate#isValidCandidate(Block)}, but only Assemblages with a middle cog are valid.
     */
    @Unique
    private static boolean petrolsParts$isValidAssemblage(BlockState state) {
        if (state.getValue(IAssemblageBlock.MIDDLE_COG).isNone()) return false;
        final Block block = state.getBlock();
        if (BnbBlockTags.FORBIDDEN_COGWHEEL_CHAIN_COMPONENT.matches(block)) return false;
        return BnbBlockTags.DEDICATED_COGWHEEL_CHAIN_COMPONENT.matches(block) || BnbFeatureFlag.UNDEDICATED_COGWHEEL_CHAIN_DRIVES.isEnabled();
    };
};
