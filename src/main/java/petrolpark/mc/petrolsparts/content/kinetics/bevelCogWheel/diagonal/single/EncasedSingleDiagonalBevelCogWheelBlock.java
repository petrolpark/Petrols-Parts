package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;

import java.util.function.Supplier;

import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.BlockStateProviderHelper;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IEncasedBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;

public class EncasedSingleDiagonalBevelCogWheelBlock extends KineticBlock implements ISingleDiagonalBevelCogWheelBlock, IEncasedBevelCogWheelBlock {

    private final Supplier<BevelCogWheelSet> set;
    protected final Supplier<Block> casing;
    protected final String descriptionId;

    public EncasedSingleDiagonalBevelCogWheelBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(properties);
        this.set = set;
        this.casing = casing;
        this.descriptionId = Util.makeDescriptionId("block", Lang.prependLocation(casingName + "_encased_", getSet().id()));
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, SHAFT));
    };

    @Override
    public BevelCogWheelSet getSet() {
        return set.get();
    };

    @Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().setBlockAndUpdate(context.getClickedPos(), BlockHelper.copyAll(getSet().singleDiagonalBlock().getDefaultState(), state));
		return InteractionResult.SUCCESS;
	};

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return placeCogOrEncase(stack, state, level, pos, player, hand, hitResult);
    };

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canDiagonalBevelCogWheelSurvive(state, level, pos);
    };

    @Override
    public Block getCasing() {
        return casing.get();
    };

    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        level.setBlock(pos, BlockHelper.copyAll(defaultBlockState(), state), Block.UPDATE_ALL);
    };

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(getCasing());
    };

    @Override
    public String getDescriptionId() {
        return descriptionId;
    };

    @Override
    public Item asItem() {
        return getSet().item().get();
    };

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return ISingleDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return ISingleDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };

    public boolean isCasingSide(BlockState state, Direction face) {
        final Orientation orientation = state.getValue(ORIENTATION);
        if (face.getAxis() == orientation.right.getAxis()) return true;
        if (face == orientation.top || face == orientation.front) return false;
        else return switch (state.getValue(SHAFT)) {
            case NONE -> true;
            case FIRST_AXIS -> face != orientation.top.getOpposite();
            case SECOND_AXIS -> face != orientation.front.getOpposite();
        };
    };

    public static final <B extends EncasedSingleDiagonalBevelCogWheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> builderTransformer(CTSpriteShiftEntry spriteShiftEntry, String casing) {
        return builder -> builder
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> {
                final ResourceLocation modelsFolder = ctx.get().getSet().id().withPrefix("block/").withSuffix("/encased/diagonal/single/" + casing);
                final ModelFile noShaftModel = prov.models().getExistingFile(modelsFolder.withSuffix("/no_shaft"));
                final ModelFile upSouthShaftModel = prov.models().getExistingFile(modelsFolder.withSuffix("/shaft_up_south"));
                final ModelFile eastSouthShaftModel = prov.models().getExistingFile(modelsFolder.withSuffix("/shaft_east_south"));
                final VariantBlockStateBuilder stateBuilder = prov.getVariantBuilder(ctx.get());
                BlockStateProviderHelper.edgeOrientedBlock(stateBuilder, b -> b.with(ISingleDiagonalBevelCogWheelBlock.SHAFT, CornerBevelCogWheelsBlock.ShaftType.NONE), noShaftModel);
                for (Orientation orientation : Orientation.values()) {
                    final Orientation edge = orientation.asEdge();
                    stateBuilder.partialState()
                        .with(ISingleDiagonalBevelCogWheelBlock.ORIENTATION, edge)
                        .with(ISingleDiagonalBevelCogWheelBlock.SHAFT, orientation == edge ? CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS : CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS)
                        .modelForState()
                            .modelFile(orientation.isTopVertical() ? upSouthShaftModel : eastSouthShaftModel)
                            .rotationX(orientation.blockStateXRotation)
                            .rotationY(orientation.blockStateYRotation)
                        .addModel();
                };
            }).loot((lt, b) -> lt.add(b, lootTable()
                .withPool(lootPool()
                    .add(NestedLootTable.lootTableReference(b.getSet().cogLoot()))
                    .setRolls(ConstantValue.exactly(1f))
                ).withPool(lootPool()
                    .when(InvertedLootItemCondition.invert(
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(SHAFT, CornerBevelCogWheelsBlock.ShaftType.NONE)
                            )
                    )).add(NestedLootTable.lootTableReference(b.getSet().shaftHalfLoot()))
                    .setRolls(ConstantValue.exactly(1f))
                )
            )).onRegisterAfter(Registries.BLOCK, b -> EncasingRegistry.addVariant(b.getSet().singleDiagonalBlock().get(), b))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedSingleDiagonalBevelCogWheelCTBehaviour(spriteShiftEntry)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, spriteShiftEntry, block::isCasingSide)))
            .transform(TagGen.axeOrPickaxe());
    };
    
};
