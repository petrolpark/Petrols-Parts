package petrolpark.mc.petrolsparts.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.core.world.block.multiPart.MultiPartBlock;
import petrolpark.mc.library.util.FunctionHelper;
import petrolpark.mc.petrolsparts.util.ShaftHelper;

@Mixin(targets = "com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock$PlacementHelper")
public class ShaftBlockPlacementHelperMixin {
    
    @ModifyArgs(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "<init>(Ljava/util/function/Predicate;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/properties/Property;)V"
        )
    )
    private static void petrolsParts$moreShafts(Args args) {
        args.set(0, ShaftHelper.SHAFT_PLACEMENT_HELPER_STATE_PREDICATE.or(args.get(0)));
        args.set(1, FunctionHelper.withFallback(ShaftHelper.SHAFT_PLACEMENT_HELPER_AXIS_FUNCTION, args.get(1)));
    };

    @ModifyReturnValue(
        method = "getStatePredicate",
        at = @At("RETURN")
    )
    private Predicate<BlockState> petrolsParts$moreShafts(Predicate<BlockState> predicate) {
        return ShaftHelper.SHAFT_PLACEMENT_HELPER_ACTIVATION_STATE_PREDICATE.or(predicate);
    };

    @Inject(
        method = "getOffset",
        at = @At("HEAD"),
        cancellable = true
    )
    public void petrolsParts$ensureSelectingShaftPart(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, CallbackInfoReturnable<PlacementOffset> cir) {
        if (state.getBlock() instanceof MultiPartBlock<?> block && !ShaftHelper.isTargetingShaftPart(pos, block, state, player)) cir.setReturnValue(PlacementOffset.fail());
    };

};
