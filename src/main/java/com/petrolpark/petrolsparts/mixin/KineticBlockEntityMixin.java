package com.petrolpark.petrolsparts.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.CoaxialGearBlock;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.LongShaftBlock;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin extends SmartBlockEntity {
    
    public KineticBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        throw new AssertionError();
    };

    // /**
    //  * Search for connecting Long Shafts.
    //  * @param block
    //  * @param state
    //  * @param neighbours
    //  */
    // @Inject(
    //     method = "Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;addPropagationLocations(Lcom/simibubi/create/content/kinetics/base/IRotate;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/List;)Ljava/util/List;",
    //     at = @At("HEAD"),
    //     remap = false
    // )
    // public void inAddPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours, CallbackInfoReturnable<List<BlockPos>> cir) {
    //     if (!hasLevel()) return;
    //     BlockPos pos = getBlockPos();
    //     Level level = getLevel();
    //     if (level == null) return;
    //     for (Direction direction : Direction.values()) {
    //         BlockState coaxialGearState = level.getBlockState(pos.relative(direction));
    //         if (CoaxialGearBlock.isCoaxialGear(coaxialGearState) && coaxialGearState.getValue(RotatedPillarKineticBlock.AXIS) == direction.getAxis()) {
    //             BlockPos longShaftPos = pos.relative(direction, 2);
    //             BlockState longShaftState = level.getBlockState(longShaftPos);
    //             if (longShaftState.getBlock() instanceof LongShaftBlock && DirectionalRotatedPillarKineticBlock.getDirection(longShaftState) == direction.getOpposite()) neighbours.add(longShaftPos);
    //         };
    //     };
    // };

    @ModifyReturnValue(
        method = "Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;addPropagationLocations(Lcom/simibubi/create/content/kinetics/base/IRotate;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/List;)Ljava/util/List;",
        at = @At("RETURN"),
        remap = false
    )
    public List<BlockPos> modifyAddPropagationLocations(List<BlockPos> original, IRotate block, BlockState state, List<BlockPos> neighbors) {
        if (!hasLevel()) return original;
        BlockPos pos = getBlockPos();
        Level level = getLevel();
        if (level == null) return original;
        for (Direction direction : Direction.values()) {
            BlockState coaxialGearState = level.getBlockState(pos.relative(direction));
            if (CoaxialGearBlock.isCoaxialGear(coaxialGearState) && coaxialGearState.getValue(RotatedPillarKineticBlock.AXIS) == direction.getAxis()) {
                BlockPos longShaftPos = pos.relative(direction, 2);
                BlockState longShaftState = level.getBlockState(longShaftPos);
                if (longShaftState.getBlock() instanceof LongShaftBlock && DirectionalRotatedPillarKineticBlock.getDirection(longShaftState) == direction.getOpposite()) original.add(longShaftPos);
            };
        };
        return original;
    };

};
