package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.entity.IOverridableKineticBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockEntity;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {
    
    /**
     * Allow Cogwheels to connect to Colossal Cogwheel
     * TODO check if still necessary after other mixin that checks propagation both ways
     * @param from
     * @param to
     * @param cir
     */
    @Inject(
        method = "getRotationSpeedModifier(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;)F",
        at = @At("TAIL"),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
        remap = false
    )
    private static void inGetRotationSpeedModifier(KineticBlockEntity from, KineticBlockEntity to, CallbackInfoReturnable<Float> cir, BlockState stateFrom, BlockState stateTo, Block fromBlock, Block toBlock, IRotate definitionFrom, IRotate definitionTo, BlockPos diff, Direction direction, Level world) {
        // Colossal Cogwheel <-> Cogwheels
        if (to.getBlockState().getBlock() instanceof ColossalCogwheelBlock) {
            float ratio = ColossalCogwheelBlockEntity.propagateFromColossalCogwheel(to, stateTo, stateFrom, BlockPos.ZERO.subtract(diff));
            if (ratio != 0f) cir.setReturnValue(1 / ratio);
        };
    };

    //TODO MOVE BELOW TO LIBRARY

    /**
     * Trick definition of incompatible {@link RotationPropagator#propagateNewSource} line 232
     */
    @ModifyExpressionValue(
        method = "propagateNewSource",
        at = @At(
            value = "INVOKE",
            target = "signum",
            ordinal = 1
        )
    )
    private static float petrolsParts$overrideDifferentialSignum(float original, KineticBlockEntity currentTE, @Local(ordinal = 1) KineticBlockEntity neighbourTE, @Local(ordinal = 2) float newSpeed) {
        if (
            IOverridableKineticBlockEntity.isSourceOverridable(currentTE)
            || IOverridableKineticBlockEntity.isSourceOverridable(neighbourTE)
        ) return Math.signum(newSpeed);
        return original;
    };

    @ModifyExpressionValue(
        method = "propagateNewSource",
        at = @At(
            value = "INVOKE",
            target = "abs",
            ordinal = 3
        )
    )
    private static float petrolsParts$overrideDifferentialCurrentSpeed(float original, KineticBlockEntity currentTE, @Local(ordinal = 0) float speedOfCurrent, @Local(ordinal = 3) float oppositeSpeed) {
        if (IOverridableKineticBlockEntity.isSourceOverridable(currentTE)
            && Mth.abs(speedOfCurrent) < Mth.abs(oppositeSpeed) // Prevent recursion if speeds already match
        )
            return 0f;
        else 
            return original;
    };

    @ModifyExpressionValue(
        method = "propagateNewSource",
        at = @At(
            value = "INVOKE",
            target = "abs",
            ordinal = 5
        )
    )
    private static float petrolsParts$overrideDifferentialNeighbourSpeed(float original, KineticBlockEntity currentTE, @Local(ordinal = 1) KineticBlockEntity neighbourTE) {
        if (IOverridableKineticBlockEntity.isSourceOverridable(neighbourTE)) return 0f;
        return original;
    };

};
