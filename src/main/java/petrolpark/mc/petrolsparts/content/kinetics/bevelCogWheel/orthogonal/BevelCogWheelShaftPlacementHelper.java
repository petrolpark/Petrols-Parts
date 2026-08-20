package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;

public class BevelCogWheelShaftPlacementHelper implements IPlacementHelper {

    protected final Supplier<BevelCogWheelSet> set;

    public BevelCogWheelShaftPlacementHelper(Supplier<BevelCogWheelSet> set) {
        this.set = set;
    };

    public BevelCogWheelSet getSet() {
        return set.get();
    };

    public boolean canPlaceShaft(BlockState state) {
        return state.getBlock() instanceof IOrthogonalBevelCogWheelBlock block
            && block.getSet() == getSet()
            && block.getShaftAxis(state) == null;
    };

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return getSet().shaftBlock()::isIn;
    };

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return this::canPlaceShaft;
    };

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
        if (switch (((IOrthogonalBevelCogWheelBlock)state.getBlock()).getTargetedPart(state, pos, player)) {
            case BevelCogWheelPart.Cog cog -> cog.face == ray.getDirection();
            case BevelCogWheelPart.Shaft shaft -> shaft.axis == ray.getDirection().getAxis();
            case null -> false;
        }) return PlacementOffset.fail(); // Don't place on ends

        for (final Direction dir : IPlacementHelper.orderedByDistance(pos, ray.getLocation())) {
            if (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock block && block.withPart(state, getSet().shaftParts().get(dir.getAxis())) != null) {
                return PlacementOffset.success(pos, s -> s.setValue(ShaftBlock.AXIS, dir.getAxis()));
            };
        };
        return PlacementOffset.fail();
    };

};
