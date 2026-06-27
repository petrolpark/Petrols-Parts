package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

@Mixin(AssemblyOperatorBlockItem.class)
public class AssemblyOperatorBlockItemMixin {
    
    @ModifyReturnValue(
        method = "Lcom/simibubi/create/content/processing/AssemblyOperatorBlockItem;operatesOn",
        at = @At("TAIL")
    )
    protected boolean petrolsParts$placeOnBrassDepots(boolean original, LevelReader world, BlockPos pos, BlockState placedOnState) {
        return original || PetrolsPartsBlocks.BRASS_DEPOT.has(placedOnState);
    };
};
