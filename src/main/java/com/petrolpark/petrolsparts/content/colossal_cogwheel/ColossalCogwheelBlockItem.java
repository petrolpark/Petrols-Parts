package com.petrolpark.petrolsparts.content.colossal_cogwheel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlock.Connection;
import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlock.Position;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class ColossalCogwheelBlockItem extends BlockItem {

    public final Couple<Integer> onOtherPlacementHelpers = Couple.create(-1, -1);
    public final Couple<Integer> onColossalPlacementHelpers = Couple.create(-1, -1);

    public ColossalCogwheelBlockItem(ColossalCogwheelBlock block, Properties properties) {
        super(block, properties);

        for (boolean large : Iterate.trueAndFalse) {
            onOtherPlacementHelpers.set(large, PlacementHelpers.register(new ColossalOnOtherPlacementHelper(large)));
            onColossalPlacementHelpers.set(large, PlacementHelpers.register(new OtherOnColossalPlacementHelper(large)));
        };
    };

    @Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);

        if (ICogWheel.isLargeCog(state.getBlock()) || ICogWheel.isSmallCog(state.getBlock())) {
            IPlacementHelper helper = PlacementHelpers.get(onOtherPlacementHelpers.get(ICogWheel.isLargeCog(state.getBlock())));
            Player player = context.getPlayer();
            BlockHitResult ray = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, true);
            if (helper.matchesState(state) && player != null && !player.isShiftKeyDown()) {
                PlacementOffset offset = helper.getOffset(player, world, state, pos, ray);
                Axis axis = offset.getTransform().apply(getBlock().defaultBlockState()).getValue(RotatedPillarKineticBlock.AXIS);
                return offset.at(offset.getPos().offset(Position.getRelativeControllerPosition(axis))).placeInWorld(world, this, player, context.getHand(), ray);
            };
        };

		return super.onItemUseFirst(stack, context);
	};

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Axis axis = context.getNearestLookingDirection().getAxis();
        BlockPos controllerPos = pos.offset(Position.getRelativeControllerPosition(axis));
        InteractionResult result;
        if (!isValidCenterPosition(context.getLevel(), pos, axis)) {
            result = InteractionResult.FAIL;
        } else {
            result = super.place(BlockPlaceContext.at(context, controllerPos, context.getNearestLookingDirection()));;
        };
        if (result == InteractionResult.FAIL) DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ()-> showBounds(pos, axis, context.getPlayer()));
        return result;
    };

    public static boolean isValidCenterPosition(Level level, BlockPos pos, Axis axis) {
        for (Position.Clock posClock : Position.Clock.values()) {
            for (Position.Type posType : Position.Type.values()) {
                BlockPos targetPos = pos.subtract(posType.relativeCenterPos.apply(axis, posClock.getDirection(axis)));
                //if (!level.getEntities((Entity)null, new AABB(targetPos), e -> e.blocksBuilding).isEmpty()) return false;
                if (!level.getBlockState(targetPos).canBeReplaced()) return false;
            };
        };
        return true;
    };

    @OnlyIn(Dist.CLIENT)
	public void showBounds(BlockPos centerPos, Axis axis, Player player) {
		if (!(player instanceof LocalPlayer localPlayer)) return;
		CreateClient.OUTLINER.showCluster(Pair.of("colossal_cogwheel", centerPos), Stream.of(Position.Clock.values()).map(c -> Stream.of(Position.Type.values()).map(t -> centerPos.offset(t.relativeCenterPos.apply(axis, c.getDirection(axis)))).toList()).collect(ArrayList::new, List::addAll, List::addAll))
            .colored(0xFF_ff5d6c);
		Lang.translate("large_water_wheel.not_enough_space")
			.color(0xFF_ff5d6c)
			.sendStatus(localPlayer);
	};

    public class OtherOnColossalPlacementHelper implements IPlacementHelper {

        public final boolean large;

        public OtherOnColossalPlacementHelper(boolean large) {
            this.large = large;
        };

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return ((Predicate<ItemStack>) ICogWheel::isDedicatedCogItem).and(stack -> ICogWheel.isLargeCogItem(stack) == large);
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return state -> state.is(ColossalCogwheelBlockItem.this.getBlock());
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
            BlockPos center = pos.offset(ColossalCogwheelBlock.getRelativeCenterPosition(state));
            return Connection.getAll(center, axis).stream()
                .filter(pair -> pair.getSecond().toLargeCog() == large)
                .filter(pair -> world.getBlockState(pair.getFirst()).canBeReplaced())
                .map(pair -> Pair.of(pair.getFirst(), Vec3.atCenterOf(pair.getFirst()).distanceTo(ray.getLocation())))
                .sorted(Comparator.comparingDouble(Pair::getSecond))
                .map(pair -> PlacementOffset.success(pair.getFirst(), s -> s.setValue(CogWheelBlock.AXIS, axis)))
                .findFirst()
                .orElse(PlacementOffset.fail());
        };

    };

    public class ColossalOnOtherPlacementHelper implements IPlacementHelper {

        public final boolean large;

        public ColossalOnOtherPlacementHelper(boolean large) {
            this.large = large;
        };

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (stack) -> stack.getItem().equals(ColossalCogwheelBlockItem.this);
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return (state) -> ICogWheel.isLargeCog(state) == large && ICogWheel.isSmallCog(state) != large;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Axis axis = ((IRotate)state.getBlock()).getRotationAxis(state);
            return Connection.getAll(BlockPos.ZERO, axis).stream()
                .filter(
                    pair -> pair.getSecond().toLargeCog() == large)
                .map(
                    pair -> Pair.of(pos.subtract(pair.getFirst()), pair.getSecond()))
                .filter(
                    pair -> isValidCenterPosition(world, pair.getFirst(), axis))
                .map(
                    pair -> Pair.of(pair.getFirst(), Vec3.atCenterOf(pair.getFirst()).distanceTo(ray.getLocation())))
                .sorted((Comparator.comparingDouble(Pair::getSecond)))
                .map(
                    pair -> PlacementOffset.success(pair.getFirst(), s -> s.setValue(RotatedPillarKineticBlock.AXIS, axis)))
                .findFirst()
                .orElse(PlacementOffset.fail());
        };

    };  
    
};
