package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockEntity;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {
    
    /**
     * Allow Kinetic Blocks to rotate the Gear end of a Long Shaft, and Cogwheels to connect to Colossal Cogwheel
     * @param from
     * @param to
     * @param cir
     */
    @Inject(
        method = "getRotationSpeedModifier(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;)F",
        at = @At("TAIL"),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILSOFT,
        remap = false
    )
    private static void inGetRotationSpeedModifier(KineticBlockEntity from, KineticBlockEntity to, CallbackInfoReturnable<Float> cir, BlockState stateFrom, BlockState stateTo, Block fromBlock, Block toBlock, IRotate definitionFrom, IRotate definitionTo, BlockPos diff, Direction direction, Level world) {
        // Colossal Cogwheel <-> Cogwheels
        if (to.getBlockState().getBlock() instanceof ColossalCogwheelBlock) {
            float ratio = ColossalCogwheelBlockEntity.propagateFromColossalCogwheel(to, stateTo, stateFrom, BlockPos.ZERO.subtract(diff));
            if (ratio != 0f) cir.setReturnValue(1 / ratio);
        };
    };
};
