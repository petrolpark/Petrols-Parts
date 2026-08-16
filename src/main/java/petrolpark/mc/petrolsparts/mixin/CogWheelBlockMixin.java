package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

@Mixin(CogWheelBlock.class)
public class CogWheelBlockMixin {

    @Inject(
        method = "isValidCogWheelPosition",
        at = @At(
            value = "INVOKE",
            target = "hasProperty"
        ),
        cancellable = true
    )
    public void petrolsparts$forbidLargeCogWheelsOverlappingFaceAlignedCogWheels(boolean large, LevelReader worldIn, BlockPos pos, Axis cogAxis, CallbackInfoReturnable<Boolean> cir, @Local Direction facing, @Local BlockPos offsetPos, @Local BlockState offsetState) {
        if (large && CompositeKineticBlockEntity.streamAny(worldIn, offsetPos).anyMatch(kbe -> IFaceAlignedCogWheelBlockEntity.getCogType(kbe, facing.getOpposite()) != CogType.NONE)) cir.setReturnValue(false);
        //if (large && offsetState.getBlock() instanceof IDiagonalBevelCogWheelBlock bevelBlock && bevelBlock.getCogRotationAxisConnectedToFace(offsetState, facing.getOpposite()) != null) cir.setReturnValue(false);
    };
    
    @Inject(
        method = "isValidCogWheelPosition",
        at = @At(
            value = "INVOKE",
            target = "isLargeCog"
        ),
        cancellable = true
    )
    public void petrolsparts$forbidOverlappingFaceAlignedCogwheels(boolean large, LevelReader worldIn, BlockPos pos, Axis cogAxis, CallbackInfoReturnable<Boolean> cir, @Local Direction facing, @Local BlockPos offsetPos, @Local BlockState offsetState) {
        if (offsetState.hasProperty(BlockStateProperties.AXIS) && CompositeKineticBlockEntity.streamAny(worldIn, offsetPos).anyMatch(kbe -> {
            final Axis axis = offsetState.getValue(BlockStateProperties.AXIS);
            if (axis != cogAxis) {
                final CogType rightAngleUpperCogType = IFaceAlignedCogWheelBlockEntity.getCogType(kbe, Direction.get(AxisDirection.POSITIVE, axis));
                final CogType rightAngleLowerCogType = IFaceAlignedCogWheelBlockEntity.getCogType(kbe, Direction.get(AxisDirection.NEGATIVE, axis));
                if (rightAngleUpperCogType.isLarge() || rightAngleLowerCogType.isLarge() || ICogWheel.isLargeCog(offsetState)) return true;
                if (large && rightAngleUpperCogType.isSmall() || rightAngleLowerCogType.isSmall() || ICogWheel.isSmallCog(offsetState)) return true;
            };
            return false;
        })) cir.setReturnValue(false);
    };
};
