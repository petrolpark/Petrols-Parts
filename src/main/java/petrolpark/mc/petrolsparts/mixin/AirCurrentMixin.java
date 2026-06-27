package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.AirCurrent;

import net.minecraft.world.level.Level;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotBlockEntity;

@Mixin(AirCurrent.class)
public class AirCurrentMixin {
    
    @WrapOperation(
        method = "Lcom/simibubi/create/content/kinetics/fan/AirCurrent;tickAffectedHandlers()V",
        at = @At(
            value = "INVOKE",
            target = "getWorld()Lnet/minecraft/world/level/Level;"
        )
    )
    public Level petrolsParts$filteredRecipeManagerWorld(TransportedItemStackHandlerBehaviour behaviour, Operation<Level> original) {
        final Level level = original.call(behaviour);
        if (behaviour.blockEntity instanceof BrassDepotBlockEntity depot) return new BrassDepotBlockEntity.FanProcessingWorld(level, depot);
        return level;
    };
};
