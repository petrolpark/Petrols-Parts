package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CornerShaftBlockItem extends BlockItem {

    public CornerShaftBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    };

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);
        final BlockState defaultPlacementState = getPlacementState(blockPlaceContext);
        if (!context.getPlayer().isShiftKeyDown() && defaultPlacementState != null && canPlace(blockPlaceContext, defaultPlacementState)) {
            if (context.getLevel().isClientSide()) {
                CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> AutoShaftRoutingClientHelper.tryPlace(blockPlaceContext));
            };
            return InteractionResult.FAIL;
        };
        return super.useOn(context);
    };
    
};
