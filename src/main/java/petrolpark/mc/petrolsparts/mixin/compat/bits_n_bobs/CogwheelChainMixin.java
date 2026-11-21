package petrolpark.mc.petrolsparts.mixin.compat.bits_n_bobs;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.cake.azimuth.behaviour.SuperBlockEntityBehaviour;
import com.kipti.bnb.content.kinetics.cogwheel_chain.behaviour.CogwheelChainBehaviour;
import com.kipti.bnb.content.kinetics.cogwheel_chain.graph.CogwheelChain;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;

@Mixin(CogwheelChain.class)
public class CogwheelChainMixin {
    
    @ModifyExpressionValue(
        method = "placeInLevel",
        at = @At(
            value = "INVOKE",
            target = "getBlockEntity"
        )
    )
    public BlockEntity petrolsParts$detachAssembages(BlockEntity original) {
        return original instanceof AssemblageBlockEntity assemblage ? assemblage.getMiddleCogPart() : original;
    };

    @WrapOperation(
        method = "placeChainCogwheelInLevel",
        at = @At(
            value = "INVOKE",
            target = "getOrThrow"
        )
    )
    public BlockEntityBehaviour petrolsParts$getAssemblageBehaviour(Level level, BlockPos pos, BehaviourType<CogwheelChainBehaviour> type, Operation<CogwheelChainBehaviour> original) {
        if (level.getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage && assemblage.getMiddleCogPart() != null) return SuperBlockEntityBehaviour.getOrThrow(assemblage.getMiddleCogPart(), type);
        return original.call(level, pos, type); // If no middle cog, this throws the usual descriptive exception
    };

    @WrapOperation(
        method = "destroy",
        at = @At(
            value = "INVOKE",
            target = "getOptional"
        )
    )
    public Optional<? extends BlockEntityBehaviour> petrolsParts$destroyAssemblage(Level level, BlockPos pos, BehaviourType<CogwheelChainBehaviour> type, Operation<Optional<CogwheelChainBehaviour>> original) {
        if (level.getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage) return SuperBlockEntityBehaviour.getOptional(assemblage.getMiddleCogPart(), type);
        return original.call(level, pos, type);
    };
};
