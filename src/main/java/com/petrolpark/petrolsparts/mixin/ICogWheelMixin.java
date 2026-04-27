package com.petrolpark.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.petrolpark.petrolsparts.core.block.IStateDependentCogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.minecraft.world.level.block.state.BlockState;

@Mixin(ICogWheel.class)
public interface ICogWheelMixin {
    
    @ModifyReturnValue(
        method = "Lcom/simibubi/create/content/kinetics/simpleRelays/ICogWheel;isSmallCog(Lnet/minecraft/world/level/block/state/BlockState;)Z",
        at = @At("RETURN")
    )
    private static boolean petrolsParts$stateDependentSmallCogWheels(boolean original, BlockState state) {
        return (state.getBlock() instanceof IStateDependentCogWheelBlock cogwheel && cogwheel.getCogType(state).isSmall()) || original;
    };

    @ModifyReturnValue(
        method = "Lcom/simibubi/create/content/kinetics/simpleRelays/ICogWheel;isLargeCog(Lnet/minecraft/world/level/block/state/BlockState;)Z",
        at = @At("RETURN")
    )
    private static boolean petrolsParts$stateDependentLargeCogWheels(boolean original, BlockState state) {
        return (state.getBlock() instanceof IStateDependentCogWheelBlock cogwheel && cogwheel.getCogType(state).isLarge()) || original;
    };
};
