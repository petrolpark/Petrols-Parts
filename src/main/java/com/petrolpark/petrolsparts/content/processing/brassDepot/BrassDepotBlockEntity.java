package com.petrolpark.petrolsparts.content.processing.brassDepot;

import java.util.List;
import java.util.Optional;

import com.petrolpark.core.recipe.FilteredRecipeManager;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.mixin.accessor.DeployerRecipeSearchEventAccessor;
import com.petrolpark.util.RandomHelper;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;

import net.createmod.catnip.levelWrappers.WrappedLevel;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

@EventBusSubscriber
public class BrassDepotBlockEntity extends DepotBlockEntity {

    public FilteringBehaviour filtering;

    public BrassDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @SubscribeEvent
    public static final void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get(), BrassDepotBlockEntity::getItemCapability);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(filtering = new FilteringBehaviour(this, new BrassDepotValueBoxTransform()).forRecipes());
    };

    public IItemHandler getItemCapability(Direction side) {
        return getBehaviour(DepotBehaviour.TYPE).itemHandler;
    };

    public boolean matches(RecipeHolder<? extends Recipe<?>> recipeHolder) {
        if (recipeHolder == null) return false;
        if (recipeHolder.value() instanceof ProcessingRecipe<?, ?> processingRecipe) return processingRecipe.getRollableResults().stream().map(ProcessingOutput::getStack).anyMatch(filtering::test);
        return filtering.test(recipeHolder.value().getResultItem(getLevel().registryAccess()));
    };

    public class BrassDepotValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8d, 8d, 16.05d);
        };
        
        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis() != Axis.Y;
        };

    };

    @SubscribeEvent
    public static final void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        final DeployerBlockEntity deployer = event.getBlockEntity();
        final Level level = deployer.getLevel();
        if (level == null) return;

        level.getBlockEntity(deployer.getBlockPos().below(2), PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get())
            .filter(depot -> event.getInventory().getItem(0) == depot.getHeldItem()) // Check we are Deploying on the Depot and not an Item Entity above it
            .ifPresent(depot -> {
                if (!depot.matches(event.getRecipe())) { // Get rid of existing Recipe
                    ((DeployerRecipeSearchEventAccessor)event).setRecipe(null);
                    ((DeployerRecipeSearchEventAccessor)event).setMaxPriority(0);
                };
                event.addRecipe(() -> depot.pick(SequencedAssemblyRecipe.getRecipes(level, event.getInventory().getItem(0), AllRecipeTypes.DEPLOYING.getType(), DeployerApplicationRecipe.class, AllRecipeTypes.CAN_BE_AUTOMATED.and(depot::matches))), 105);
                event.addRecipe(() -> depot.pick(level.getRecipeManager().getRecipesFor(AllRecipeTypes.DEPLOYING.getType(), event.getInventory(), level).stream().filter(AllRecipeTypes.CAN_BE_AUTOMATED.and(depot::matches)).toList()), 55);
                event.addRecipe(() -> depot.pick(level.getRecipeManager().getRecipesFor(AllRecipeTypes.ITEM_APPLICATION.getType(), event.getInventory(), level).stream().filter(AllRecipeTypes.CAN_BE_AUTOMATED.and(depot::matches)).toList()), 55);
            });
    };

    public <T> Optional<T> pick(List<T> recipes) {
        return Optional.ofNullable(RandomHelper.pick(getLevel().getRandom(), recipes));
    };

    public FanProcessingWorld getFilteredRecipeManagerWorld() {
        return new FanProcessingWorld(level, this);
    };

    public static class FanProcessingWorld extends WrappedLevel {
      
        protected final FilteredRecipeManager recipeManager = new FilteredRecipeManager(super.getRecipeManager());

        public FanProcessingWorld(Level level, BrassDepotBlockEntity depot) {
            super(level);
            recipeManager.filter = depot::matches;
        };

        @Override
        public RecipeManager getRecipeManager() {
            return recipeManager;
        };
    };
    
};
