package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelPlacementHelper;

public class BevelCogWheelItem extends BlockItem {

    protected final int[] placementHelperIds = new int[]{
        PlacementHelpers.register(new IDiagonalBevelCogWheelPlacementHelper.BevelOnCog())
    };

    public BevelCogWheelItem(Item.Properties properties) {
        super(Blocks.AIR, properties);
    };

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = level.getBlockState(pos);
        final Player player = context.getPlayer();
        if (!player.isShiftKeyDown()) {
            for (final int placementHelperId : placementHelperIds) {
                final IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
                if (helper.matchesState(state)) {
                    final InteractionResult result = helper.getOffset(player, level, state, pos, context.getHitResult()).placeInWorld(level, this, player, context.getHand(), context.getHitResult()).result();
                    if (result.consumesAction()) return result;
                };
            };
        };
        
        return super.onItemUseFirst(stack, context);
    };

    @Override
    public String getDescriptionId() {
        return IBevelCogWheelBlock.TRANSLATION_KEY;
    };
    
};
