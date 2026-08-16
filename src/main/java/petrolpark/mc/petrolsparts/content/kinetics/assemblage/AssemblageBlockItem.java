package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.tterrag.registrate.util.RegistrateDistExecutor;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlocks;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class AssemblageBlockItem extends ItemNameBlockItem {

    private final Supplier<AssemblageSet> set;

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected AssemblageGhostBlockRenderer ghostBlockRenderer;

    public AssemblageBlockItem(Supplier<AssemblageSet> set, Item.Properties properties) {
        super(Blocks.AIR, properties);
        this.set = set;
    };

    public AssemblageSet getSet() {
        return set.get();
    };

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public AssemblageGhostBlockRenderer getGhostBlockRenderer() {
        return ghostBlockRenderer;
    };

    @Override
    public SeparateShaftHalvesAssemblageBlock getBlock() {
        return getSet().separateShaftsAssemblageBlock().get();
    };

    @Override
    public AssemblageBlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        if (!(context instanceof AssemblageBlockPlaceContext assemblageContext)) return null; // Cast should always succeed
        final AssemblagePart part = getSet().getTargetedPart(context);
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
            if (!replaceClicked) replaceClicked = getSet().getEquivalent(getLevel().getBlockState(context.getHitResult().getBlockPos())).canBeReplaced(this);
        };

        public void dontReplaceClicked() {
            this.replaceClicked = false;
        };

        @Override
        public boolean canPlace() {
            return replacingClickedOnBlock() || getSet().getEquivalent(getLevel().getBlockState(getClickedPos())).canBeReplaced(this);
        };

        public AssemblageSet getSet() {
            return AssemblageBlockItem.this.getSet();
        };

    };

    public abstract class PlacementHelper implements IPlacementHelper {
        
        @Override
        public void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
            if (!offset.hasGhostState() || getGhostBlockRenderer() == null) return;

            GhostBlocks.getInstance().showGhost(this, getGhostBlockRenderer(), GhostBlockParams.of(offset.getTransform().apply(offset.getGhostState())), 1)
                .at(offset.getBlockPos())
                .breathingAlpha();
        };
    };

    public static final <I extends AssemblageBlockItem> NonNullConsumer<I> registerClientSet(NonNullSupplier<AssemblageClientSet> clientSet) {
        return item -> RegistrateDistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> item.ghostBlockRenderer = new AssemblageGhostBlockRenderer(clientSet.get()));  
    };
    
};
