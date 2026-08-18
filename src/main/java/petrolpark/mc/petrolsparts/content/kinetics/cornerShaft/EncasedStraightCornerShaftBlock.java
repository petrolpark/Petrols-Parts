package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.function.Supplier;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

@EventBusSubscriber
public class EncasedStraightCornerShaftBlock extends EncasedShaftBlock {

	public static NonNullFunction<BlockBehaviour.Properties, EncasedStraightCornerShaftBlock> andesite(Supplier<CornerShaftSet> set) {
		return p -> new EncasedStraightCornerShaftBlock(set, p, AllBlocks.ANDESITE_CASING::get);
	};

	public static NonNullFunction<BlockBehaviour.Properties, EncasedStraightCornerShaftBlock> brass(Supplier<CornerShaftSet> set) {
		return p -> new EncasedStraightCornerShaftBlock(set, p, AllBlocks.BRASS_CASING::get);
	};

	protected final Supplier<CornerShaftSet> set;

    public EncasedStraightCornerShaftBlock(Supplier<CornerShaftSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing) {
        super(properties, casing);
		this.set = set;
    };

	public CornerShaftSet getSet() {
		return set.get();
	};

    @Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, context.getClickedPos(), Block.getId(state));
		KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), getSet().straightCornerShaftBlock().getDefaultState().setValue(AXIS, state.getValue(AXIS)));
		return InteractionResult.SUCCESS;
	};

    @Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		if (target instanceof BlockHitResult) return ((BlockHitResult) target).getDirection().getAxis() == getRotationAxis(state) ? getSet().cornerShaftBlock().asStack() : getCasing().asItem().getDefaultInstance();
		return super.getCloneItemStack(state, target, level, pos, player);
	};

	@Override
	public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
		return ItemRequirement.of(getSet().straightCornerShaftBlock().getDefaultState(), be);
	};

    @SubscribeEvent
    public static final void onBlockEntityTypeAddBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(AllBlockEntityTypes.ENCASED_SHAFT.getKey(), PetrolsPartsBlocks.ANDESITE_ENCASED_STRAIGHT_CORNER_SHAFT.get(), PetrolsPartsBlocks.BRASS_ENCASED_STRAIGHT_CORNER_SHAFT.get());
    };
    
};
