package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

@EventBusSubscriber
public class EncasedCornerShaftBlock extends AbstractCornerShaftBlock implements SpecialBlockItemRequirement, EncasedBlock {

    public static final NonNullFunction<BlockBehaviour.Properties, EncasedCornerShaftBlock> andesite(Supplier<CornerShaftSet> set) {
        return p -> new EncasedCornerShaftBlock(set, p, AllBlocks.ANDESITE_CASING::get);
    };

    public static final NonNullFunction<BlockBehaviour.Properties, EncasedCornerShaftBlock> brass(Supplier<CornerShaftSet> set) {
        return p -> new EncasedCornerShaftBlock(set, p, AllBlocks.BRASS_CASING::get);
    };

    private final Supplier<Block> casing;

    public EncasedCornerShaftBlock(Supplier<CornerShaftSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing) {
        super(set, properties);
        this.casing = casing;
    };

    @Override
    public Block getCasing() {
        return casing.get();
    };

    @Override
    public ItemRequirement getRequiredItems(BlockState state, @Nullable BlockEntity blockEntity) {
        return ItemRequirement.of(getSet().cornerShaftBlock().getDefaultState(), blockEntity);
    };

    @Override
    public BlockEntityType<? extends CornerShaftBlockEntity> getBlockEntityType() {
        return getSet().encasedCornerShaftBlockEntity().get();
    };

    @Override
	public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
		KineticBlockEntity.switchToBlockState(level, pos, defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(AXIS_ALONG_FIRST_COORDINATE, state.getValue(AXIS_ALONG_FIRST_COORDINATE)));
	};

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(state));
		KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), getSet().cornerShaftBlock().getDefaultState().setValue(FACING, state.getValue(FACING)).setValue(AXIS_ALONG_FIRST_COORDINATE, state.getValue(AXIS_ALONG_FIRST_COORDINATE)));
		return InteractionResult.SUCCESS;
    };

    @SubscribeEvent
    public static final void onRegister(RegisterEvent event) {
        if (event.getRegistry().key() == Registries.BLOCK) {
            EncasingRegistry.addVariant(PetrolsPartsBlocks.CORNER_SHAFT.get(), PetrolsPartsBlocks.ANDESITE_ENCASED_CORNER_SHAFT.get());
            EncasingRegistry.addVariant(PetrolsPartsBlocks.CORNER_SHAFT.get(), PetrolsPartsBlocks.BRASS_ENCASED_CORNER_SHAFT.get());
        };
    };
    
};
