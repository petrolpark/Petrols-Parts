package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@Mixin(CogWheelBlock.class)
public class CogWheelBlockMixin {
    
    @Inject(
        method = "isValidCogWheelPosition",
        at = @At(
            value = "INVOKE",
            target = "isLargeCog"
        ),
        cancellable = true
    )
    public void petrolsparts$forbidOverlappingFaceAlignedCogwheels(boolean large, LevelReader worldIn, BlockPos pos, Axis cogAxis, CallbackInfoReturnable<Boolean> cir, @Local Direction facing, @Local BlockPos offsetPos, @Local BlockState offsetState) {
        if (offsetState.hasProperty(BlockStateProperties.AXIS) && worldIn.getBlockEntity(offsetPos) instanceof KineticBlockEntity kbe) {
            final Axis axis = offsetState.getValue(BlockStateProperties.AXIS);
            if (axis != cogAxis) {
                final CogType rightAngleUpperCogType = IFaceAlignedCogWheelBlockEntity.getCogType(kbe, Direction.get(AxisDirection.POSITIVE, axis));
                final CogType rightAngleLowerCogType = IFaceAlignedCogWheelBlockEntity.getCogType(kbe, Direction.get(AxisDirection.NEGATIVE, axis));
                if (rightAngleUpperCogType.isLarge() || rightAngleLowerCogType.isLarge() || ICogWheel.isLargeCog(offsetState)) cir.setReturnValue(false);
                if (large && rightAngleUpperCogType.isSmall() || rightAngleLowerCogType.isSmall() || ICogWheel.isSmallCog(offsetState)) cir.setReturnValue(false);
            };
        };
    };
};
