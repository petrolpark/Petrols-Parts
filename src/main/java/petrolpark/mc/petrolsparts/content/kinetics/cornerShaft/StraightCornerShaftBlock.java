package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.function.Supplier;

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

public class StraightCornerShaftBlock extends ShaftBlock {

    public static StraightCornerShaftBlock vanilla(BlockBehaviour.Properties properties) {
        return new StraightCornerShaftBlock(CornerShaftSet::vanilla, properties);
    };

    protected final Supplier<CornerShaftSet> set;

    public StraightCornerShaftBlock(Supplier<CornerShaftSet> set, BlockBehaviour.Properties properties) {
        super(properties);
        this.set = set;
    };

    public CornerShaftSet getSet() {
        return set.get();
    };

    @Override
    public Item asItem() {
        return getSet().cornerShaftBlock().asItem();
    };

    @Override
    public String getDescriptionId() {
        return getSet().cornerShaftBlock().get().getDescriptionId();
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return tryEncase(state, level, pos, stack, player, hand, hitResult);
    };

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return getSet().straightBlockEntity().get();
    };
    
};
