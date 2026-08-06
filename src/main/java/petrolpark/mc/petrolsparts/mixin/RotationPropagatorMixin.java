package petrolpark.mc.petrolsparts.mixin;

import java.util.Iterator;
import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DifferentialBlockEntity;
import petrolpark.mc.petrolsparts.mixin.accessor.RotationPropagatorAccessor;

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

    //TODO not working!
    @Inject(
        method = "propagateNewSource",
        at = @At(
            value = "INVOKE",
            target = "destroyBlock",
            ordinal = 1,
            shift = Shift.BY,
            by = -7
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void petrolsParts$overrideDifferential(KineticBlockEntity currentTE, CallbackInfo ci, BlockPos pos, Level world, Iterator<KineticBlockEntity> iterator, KineticBlockEntity neighbourTE) {
        PetrolsParts.LOGGER.info("helllloooooooooooooooooooooooooooooo shithead");
        // Other BEs always overpower otherwise unpowered Differential parts
        if (neighbourTE instanceof DifferentialBlockEntity.Part && neighbourTE.hasSource() && Objects.equals(neighbourTE.source, neighbourTE.getBlockPos())) {
            
            final float prevSpeed = neighbourTE.getSpeed();
            neighbourTE.setSource(currentTE.getBlockPos());
            neighbourTE.setSpeed(RotationPropagatorAccessor.invokeGetConveyedSpeed(currentTE, neighbourTE));
            neighbourTE.onSpeedChanged(prevSpeed);
            neighbourTE.sendData();
            ci.cancel();
        } else if (currentTE instanceof DifferentialBlockEntity.Part && currentTE.hasSource() && Objects.equals(currentTE.source, currentTE.getBlockPos())) {
            final float prevSpeed = currentTE.getSpeed();
            currentTE.setSource(neighbourTE.getBlockPos());
            currentTE.setSpeed(RotationPropagatorAccessor.invokeGetConveyedSpeed(neighbourTE, currentTE));
            currentTE.onSpeedChanged(prevSpeed);
            currentTE.sendData();
            ci.cancel();
        };
    };
};
