package com.petrolpark.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

@Mixin(Block.class)
public class BlockMixin {
    
    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "createIntrusiveHolder"
        )
    )
    public Holder.Reference<Block> petrolsParts$dontCreateUnregisteredBlockHolder(DefaultedRegistry<Block> registry, Object block, Operation<Holder.Reference<Block>> original) {
        if (block instanceof AssemblageBlockEntityPart.DummyCogWheelBlock) return null;
        return original.call(registry, block);
    };
};
