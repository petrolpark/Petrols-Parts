package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;

public abstract class EncasedAssemblageBlock extends Block implements IBE<AssemblageBlockEntity>, IAssemblageBlock, EncasedBlock {

    public static final <B extends EncasedAssemblageBlock> NonNullFunction<BlockBehaviour.Properties, B> andesite(Supplier<AssemblageSet> set, EncasedAssemblageBlock.Factory<B> factory) {
        return p -> factory.create(set, p, AllBlocks.ANDESITE_CASING::get, "andesite");
    };

    public static final <B extends EncasedAssemblageBlock> NonNullFunction<BlockBehaviour.Properties, B> brass(Supplier<AssemblageSet> set, EncasedAssemblageBlock.Factory<B> factory) {
        return p -> factory.create(set, p, AllBlocks.BRASS_CASING::get, "brass");
    };

    protected final Supplier<AssemblageSet> set;
    protected final Supplier<Block> casing;
    protected final String descriptionId;

    public EncasedAssemblageBlock(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(properties);
        this.set = set;
        this.casing = casing;
        this.descriptionId = Util.makeDescriptionId("block", Lang.prependLocation(casingName + "_encased_", getSet().id()));
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS, TOP_COG, MIDDLE_COG, BOTTOM_COG);
    };

    @Override
    public AssemblageSet getSet() {
        return set.get();
    };

    public abstract BlockState getUnencasedDefaultState();

    @Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().setBlockAndUpdate(context.getClickedPos(), BlockHelper.copyAll(getUnencasedDefaultState(), state));
		return InteractionResult.SUCCESS;
	};

    @Override
    public boolean canDiagonalBevelCogWheelSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return IAssemblageBlock.super.canDiagonalBevelCogWheelSurvive(state, level, pos);
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        // Deferred update of usual KineticBlockEntity/CompositeKineticBlockEntity is no good
        withBlockEntityDo(level, pos, AssemblageBlockEntity::invalidateParts);
    };

	@Override
	public void onRemove(@Nonnull BlockState pState, @Nonnull Level pLevel, @Nonnull BlockPos pPos, @Nonnull BlockState pNewState, boolean pIsMoving) {
		IBE.onRemove(pState, pLevel, pPos, pNewState);
	};

	@Override
	public void updateIndirectNeighbourShapes(@Nonnull BlockState stateIn, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, int flags, int count) {
		IAssemblageBlock.super.updateIndirectNeighbourShapes(stateIn, level, pos, flags, count);
	};

	@Override
	public void setPlacedBy(@Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
		IAssemblageBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
	};

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS) && (face.getAxisDirection() == AxisDirection.POSITIVE ? hasTopShaft(state) || state.getValue(TOP_COG).hasShaftConnection() : hasBottomShaft(state) || state.getValue(BOTTOM_COG).hasShaftConnection());
    };

    public boolean hasShaftExposed(BlockState state, boolean top) {
        return top
            ? hasTopShaft(state) || state.getValue(IAssemblageBlock.TOP_COG).hasShaftConnection()
            : hasBottomShaft(state) || state.getValue(IAssemblageBlock.BOTTOM_COG).hasShaftConnection();
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
	public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
		return pAdjacentBlockState.getBlock() instanceof EncasedAssemblageBlock encasedAssemblage && encasedAssemblage.getCasing() == getCasing()
			&& pState.getValue(AXIS) == pAdjacentBlockState.getValue(AXIS);
	};

    @Override
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public AssemblageBlockEntityPart getTargetedKineticPart(AssemblageBlockEntity be, Player player) {
        final Minecraft mc = Minecraft.getInstance();
        if (!(mc.hitResult instanceof BlockHitResult bhr)) return null;
        if (bhr.getDirection().getAxis() == be.getBlockState().getValue(AXIS)) {
            if (bhr.getDirection().getAxisDirection() == AxisDirection.POSITIVE) {
                if (hasTopShaft(be.getBlockState())) return be.shaftPart;
                else if (be.getBlockState().getValue(TOP_COG).hasShaftConnection()) return be.topCogPart;
                else return null;
            } else {
                if (hasBottomShaft(be.getBlockState())) return be.shaftPart;
                else if (be.getBlockState().getValue(BOTTOM_COG).hasShaftConnection()) return be.bottomCogPart;
                else return null;
            }
        } else {
            final EnumProperty<AssemblageCog> property = AssemblageCogWheelBlockItem.getClosestTargetedCog(be.getBlockPos(), be.getBlockState(), bhr.getLocation());
            if (property == TOP_COG) return be.topCogPart;
            else if (property == MIDDLE_COG) return be.middleCogPart;
            else return be.bottomCogPart;
        }
    };

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return IAssemblageBlock.rotate(state, Axis.Y, rotation);
    };

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return IAssemblageBlock.mirror(state, mirror);
    };

    @Override
    public Class<AssemblageBlockEntity> getBlockEntityClass() {
        return AssemblageBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends AssemblageBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.ASSEMBLAGE.get();
    };

    @FunctionalInterface
    public interface Factory<B extends EncasedAssemblageBlock> {

        public B create(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName);
    };

    public static final <B extends EncasedAssemblageBlock> NonNullConsumer<B> registerCTs(Supplier<EncasedAssemblageCTBehaviour> ctBehaviour) {
        return b -> {
            CreateRegistrate.connectedTextures(() -> ctBehaviour.get()).accept(b);
            CreateRegistrate.casingConnectivity((b1, cc) -> cc.make(b1, ctBehaviour.get().getEndShift(), (s, f) -> s.getValue(IAssemblageBlock.AXIS) == f.getAxis() && !b.hasShaftExposed(s, f.getAxisDirection() == AxisDirection.POSITIVE))).accept(b);
        };
    };
    
};
