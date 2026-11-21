package petrolpark.mc.petrolsparts.mixin.compat.bits_n_bobs;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.cake.azimuth.behaviour.SuperBlockEntityBehaviour;
import com.kipti.bnb.content.kinetics.cogwheel_chain.behaviour.CogwheelChainBehaviour;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;

@Mixin(CogwheelChainBehaviour.class)
public abstract class CogwheelChainBehaviourMixin extends SuperBlockEntityBehaviour {
    
    public CogwheelChainBehaviourMixin(SmartBlockEntity be) {
        super(be);
        throw new AssertionError();
    };

    @WrapOperation(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "getSameBehaviourOptional(Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;"
        )
    )
    public Optional<SuperBlockEntityBehaviour> petrolsParts$getAssemblageControllerBehaviourFromPosOptional(CogwheelChainBehaviour behaviour, BlockPos pos, Operation<Optional<SuperBlockEntityBehaviour>> original) {
        if (getLevel().getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage)
            return Optional.ofNullable(assemblage.getMiddleCogPart()).map(part -> part.getBehaviour(CogwheelChainBehaviour.TYPE));

        return original.call(behaviour, pos);
    };

    @WrapOperation(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "getSameBehaviourOptional(Lnet/minecraft/world/level/block/entity/BlockEntity;)Ljava/util/Optional;"
        )
    )
    public Optional<SuperBlockEntityBehaviour> petrolsParts$getAssemblageControllerBehaviourFromBEOptional(CogwheelChainBehaviour behaviour, BlockEntity be, Operation<Optional<SuperBlockEntityBehaviour>> original) {
        if (be instanceof AssemblageBlockEntity assemblage)
            return Optional.ofNullable(assemblage.getMiddleCogPart()).map(part -> part.getBehaviour(CogwheelChainBehaviour.TYPE));

        return original.call(behaviour, be);
    };

    @WrapOperation(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "getSameBehaviour(Lnet/minecraft/core/BlockPos;)Lcom/cake/azimuth/behaviour/SuperBlockEntityBehaviour;"
        )
    )
    public SuperBlockEntityBehaviour petrolsParts$getAssemblageControllerBehaviourFromPos(CogwheelChainBehaviour behaviour, BlockPos pos, Operation<SuperBlockEntityBehaviour> original) {
        if (getLevel().getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage)
            return Optional.ofNullable(assemblage.getMiddleCogPart()).map(be -> be.getBehaviour(CogwheelChainBehaviour.TYPE)).orElse(null);

        return original.call(behaviour, pos);
    };

    @WrapOperation(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "getSameBehaviour(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lcom/cake/azimuth/behaviour/SuperBlockEntityBehaviour;"
        )
    )
    public SuperBlockEntityBehaviour petrolsParts$getAssemblageControllerBehaviourFromBE(CogwheelChainBehaviour behaviour, BlockEntity be, Operation<SuperBlockEntityBehaviour> original) {
        if (be instanceof AssemblageBlockEntity assemblage)
            return Optional.ofNullable(assemblage.getMiddleCogPart()).map(part -> part.getBehaviour(CogwheelChainBehaviour.TYPE)).orElse(null);

        return original.call(behaviour, be);
    };

    @ModifyExpressionValue(
        method = "tryTransferOnRemoval",
        at = @At(
            value = "INVOKE",
            target = "getBlockEntity"
        )
    )
    public BlockEntity petrolsParts$getAssemblageMiddleCogBlockEntity(BlockEntity original) {
        return original instanceof AssemblageBlockEntity assemblage ? assemblage.getMiddleCogPart() : original;
    };
};
