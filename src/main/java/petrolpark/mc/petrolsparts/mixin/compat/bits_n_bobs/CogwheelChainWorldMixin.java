package petrolpark.mc.petrolsparts.mixin.compat.bits_n_bobs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.kipti.bnb.content.kinetics.cogwheel_chain.world.CogwheelChainWorld;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;

@Mixin(CogwheelChainWorld.class)
public class CogwheelChainWorldMixin {
    
    @WrapOperation(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lcom/cake/azimuth/behaviour/SuperBlockEntityBehaviour;get(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lcom/simibubi/create/foundation/blockEntity/behaviour/BehaviourType;)Lcom/simibubi/create/foundation/blockEntity/behaviour/BlockEntityBehaviour;"
        )
    )
    // Must be static: wildcard selectors skip static targets for non-static handlers, and CogwheelChainWorld#validate does its lookup in a static lambda
    private static BlockEntityBehaviour petrolsParts$getAssemblageBehaviour(BlockGetter level, BlockPos pos, BehaviourType<?> type, Operation<BlockEntityBehaviour> original) {
        if (level.getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage)
            return BlockEntityBehaviour.get(assemblage.getMiddleCogPart(), type);

        return original.call(level, pos, type);
    };
};
