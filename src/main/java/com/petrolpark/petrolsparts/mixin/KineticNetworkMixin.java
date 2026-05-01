package com.petrolpark.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.petrolpark.petrolsparts.content.kinetics.differential.DifferentialSink;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(KineticNetwork.class)
public class KineticNetworkMixin {

    // KineticNetwork.calculateStress() evicts any KBE where level.getBlockEntity(pos) != be.
    // DifferentialSink is not in the world, so it always fails this check.
    // Return `be` itself when it's a DifferentialSink so the identity check passes (be == be).

    @WrapOperation(
        method = "calculateStress",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"),
        remap = false
    )
    private BlockEntity wrapGetBlockEntityCalculateStress(
            Level level, BlockPos pos, Operation<BlockEntity> original,
            @Local KineticBlockEntity be) {
        if (be instanceof DifferentialSink) return be;
        return original.call(level, pos);
    }

    @WrapOperation(
        method = "calculateCapacity",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"),
        remap = false
    )
    private BlockEntity wrapGetBlockEntityCalculateCapacity(
            Level level, BlockPos pos, Operation<BlockEntity> original,
            @Local KineticBlockEntity be) {
        if (be instanceof DifferentialSink) return be;
        return original.call(level, pos);
    }
};
