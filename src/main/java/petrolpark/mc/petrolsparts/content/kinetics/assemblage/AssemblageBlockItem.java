package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public abstract class AssemblageBlockItem extends ItemNameBlockItem {

    protected final AssemblageSet set;

    public AssemblageBlockItem(AssemblageSet set, Item.Properties properties) {
        super(Blocks.AIR, properties);
        this.set = set;
    };

    @Override
    public SeparateShaftHalvesAssemblageBlock getBlock() {
        return set.separateShaftsAssemblage().get();
    };

    @Override
    public AssemblageBlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        if (!(context instanceof AssemblageBlockPlaceContext assemblageContext)) return null; // Cast should always succeed
        final AssemblagePart part = set.getTargetedPart(context);
        if (part != null && (context.getClickedFace().getAxis() == context.getLevel().getBlockState(context.getClickedPos()).getValue(IAssemblageBlock.AXIS) ? part.isOnEnd(context.getClickedFace()) : !part.isShaft())) {
            assemblageContext.dontReplaceClicked(); // Don't replace this Block, place in the next one
        };
        return assemblageContext.canPlace() ? assemblageContext : null;
    };

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return super.place(new AssemblageBlockPlaceContext(context));
    };

    public class AssemblageBlockPlaceContext extends BlockPlaceContext {

        public AssemblageBlockPlaceContext(UseOnContext context) {
            super(context);
            if (!replaceClicked) replaceClicked = set.getEquivalent(getLevel().getBlockState(context.getHitResult().getBlockPos())).canBeReplaced(this);
        };

        public void dontReplaceClicked() {
            this.replaceClicked = false;
        };

        @Override
        public boolean canPlace() {
            return replacingClickedOnBlock() || set.getEquivalent(getLevel().getBlockState(getClickedPos())).canBeReplaced(this);
        };

    };
    
};
