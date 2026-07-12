package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

public class StraightCornerShaftBlock extends ShaftBlock {

    public StraightCornerShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public Item asItem() {
        return PetrolsPartsBlocks.CORNER_SHAFT.asItem();
    };

    @Override
    public String getDescriptionId() {
        return PetrolsPartsBlocks.CORNER_SHAFT.get().getDescriptionId();
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return tryEncase(state, level, pos, stack, player, hand, hitResult);
    };

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.STRAIGHT_CORNER_SHAFT.get();
    };
    
};
