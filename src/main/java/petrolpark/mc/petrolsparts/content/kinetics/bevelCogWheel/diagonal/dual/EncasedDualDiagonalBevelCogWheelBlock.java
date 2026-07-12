package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual;

import java.util.function.Supplier;

import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
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
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlock;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IEncasedBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.core.PetrolsPartsRegistrate;

public class EncasedDualDiagonalBevelCogWheelBlock extends CompositeKineticBlock implements IDualDiagonalBevelCogWheelBlock, IEncasedBevelCogWheelBlock {

    protected final Supplier<Block> casing;
    protected final String descriptionId;

    public EncasedDualDiagonalBevelCogWheelBlock(BlockBehaviour.Properties properties, Supplier<Block> casing, String descriptionId) {
        super(properties);
        this.casing = casing;
        this.descriptionId = descriptionId;
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXCLUDED_AXIS, FACE_PARITY));
    };

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
        context.getLevel().setBlockAndUpdate(context.getClickedPos(), BlockHelper.copyAll(PetrolsPartsBlocks.DUAL_DIAGONAL_BEVEL_COGWHEEL.getDefaultState(), state));
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
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y; // Unused
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
        return PetrolsPartsItems.BEVEL_COGWHEEL.get();
    };

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return IDualDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return IDualDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };

    public static final NonNullUnaryOperator<BlockBuilder<EncasedDualDiagonalBevelCogWheelBlock, PetrolsPartsRegistrate>> builderTransformer(CTSpriteShiftEntry spriteShiftEntry, String casing) {
        return builder -> builder
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> {
                final ModelFile model = prov.models().getExistingFile(PetrolsParts.asResource("block/bevel_cogwheel/encased/dual_diagonal/" + casing));
                prov.getVariantBuilder(ctx.get())
                    .partialState().with(EXCLUDED_AXIS, Axis.Y)
                    .modelForState().modelFile(model).uvLock(true).addModel()
                    .partialState().with(EXCLUDED_AXIS, Axis.Z)
                    .modelForState().modelFile(model).rotationX(90).uvLock(true).addModel()
                    .partialState().with(EXCLUDED_AXIS, Axis.X)
                    .modelForState().modelFile(model).rotationX(90).rotationY(90).uvLock(true).addModel();
            }).loot((lt, b) -> lt.add(b, lt.createSingleItemTable(PetrolsPartsItems.BEVEL_COGWHEEL, ConstantValue.exactly(2))))
            .transform(EncasingRegistry.addVariantTo(PetrolsPartsBlocks.DUAL_DIAGONAL_BEVEL_COGWHEEL))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(spriteShiftEntry)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, spriteShiftEntry, (s, f) -> f.getAxis() == s.getValue(IDualDiagonalBevelCogWheelBlock.EXCLUDED_AXIS))))
            .transform(TagGen.axeOrPickaxe());
    };
};