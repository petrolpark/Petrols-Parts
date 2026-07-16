package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CogWheelInAssemblagePlacementHelper extends ShaftInAssemblagePlacementHelper {

    private final Predicate<ItemStack> itemPredicate = stack -> set.equivalentSmallCogWheel().map(entry -> entry.isIn(stack))
        .or(() -> set.equivalentLargeCogWheel().map(entry -> entry.isIn(stack))).orElse(false);

    public CogWheelInAssemblagePlacementHelper(AssemblageSet set) {
        super(set);
    };
    
    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return itemPredicate;
    };

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return super.getStatePredicate().and(state -> state.getValue(IAssemblageBlock.MIDDLE_COG).isNone());
    };
};
