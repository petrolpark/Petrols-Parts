package petrolpark.mc.petrolsparts.mixin.compat.bits_n_bobs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.kipti.bnb.content.kinetics.cogwheel_chain.attachment.CogwheelChainAttachment;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import petrolpark.mc.petrolsparts.compat.bitsnbobs.AssemblageCogwheelChains;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;

@Mixin(CogwheelChainAttachment.class)
public class CogwheelChainAttachmentMixin {

    @WrapOperation(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lcom/cake/azimuth/behaviour/SuperBlockEntityBehaviour;get(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lcom/simibubi/create/foundation/blockEntity/behaviour/BehaviourType;)Lcom/simibubi/create/foundation/blockEntity/behaviour/BlockEntityBehaviour;"
        )
    )
    public BlockEntityBehaviour petrolsParts$getAssemblageBehaviour(BlockGetter level, BlockPos pos, BehaviourType<?> type, Operation<BlockEntityBehaviour> original) {
        if (level.getBlockEntity(pos) instanceof AssemblageBlockEntity assemblage)
            return BlockEntityBehaviour.get(assemblage.getMiddleCogPart(), type);

        return original.call(level, pos, type);
    };

    /**
     * Attachments get the speed of the controller, which needs to be a {@link com.simibubi.create.content.kinetics.base.KineticBlockEntity}
     */
    @ModifyExpressionValue(
        method = {"tick", "getDistPerTick(Lnet/minecraft/world/level/Level;)F"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"
        ),
        require = 2
    )
    public BlockEntity petrolsParts$getAssemblageMiddleCogPart(BlockEntity original) {
        return AssemblageCogwheelChains.redirectToMiddleCogPart(original);
    };
};
