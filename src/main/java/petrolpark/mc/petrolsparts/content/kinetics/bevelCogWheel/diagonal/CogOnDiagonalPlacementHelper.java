package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import java.util.function.Predicate;

import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CogOnDiagonalPlacementHelper implements IPlacementHelper {

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return ICogWheel::isSmallCogItem;
    };

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return state -> state.getBlock() instanceof IDiagonalBevelCogWheelBlock;
    };

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
        if (!(state.getBlock() instanceof IDiagonalBevelCogWheelBlock diagonalBevel)) return PlacementOffset.fail();
        return IPlacementHelper.orderedByDistance(pos, ray.getLocation()).stream()
            .<PlacementOffset>mapMulti((dir, consumer) -> {
                final Axis axis = diagonalBevel.getCogRotationAxisConnectedToFace(state, dir);
                if (axis != null && world.getBlockState(pos.relative(dir)).canBeReplaced()) consumer.accept(PlacementOffset.success(pos.relative(dir), s -> s.setValue(CogWheelBlock.AXIS, axis)));
            }).findFirst()
            .orElseGet(PlacementOffset::fail);
    };

};
