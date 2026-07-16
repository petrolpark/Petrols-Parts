package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Predicate;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class ShaftInAssemblagePlacementHelper implements IPlacementHelper {

    protected final AssemblageSet set;

    public ShaftInAssemblagePlacementHelper(AssemblageSet set) {
        this.set = set;
    };

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return set.shaft()::isIn;
    };

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return state -> set.separateShaftsAssemblage().has(state) && !state.getValue(IAssemblageBlock.TOP_SHAFT_HALF) && !state.getValue(IAssemblageBlock.BOTTOM_SHAFT_HALF);
    };

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
        return PlacementOffset.success(pos, s -> s.setValue(BlockStateProperties.AXIS, state.getValue(IAssemblageBlock.AXIS)));
    };
    
};
