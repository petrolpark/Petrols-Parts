package petrolpark.mc.petrolsparts.content.processing.brassDepot;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;

import net.createmod.catnip.levelWrappers.WrappedLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import petrolpark.mc.library.core.data.recipe.FilteredRecipeManager;

public class FilteredFanProcessing {
    
    public static class World extends WrappedLevel {

        protected final FilteredRecipeManager recipeManager = new FilteredRecipeManager(super.getRecipeManager());

        public World(Level level, TransportedItemStackHandlerBehaviour behaviour) {
            super(level);
            if (behaviour.blockEntity instanceof BrassDepotBlockEntity depot) recipeManager.filter = depot::matches;
        };

        @Override
        public RecipeManager getRecipeManager() {
            return recipeManager;
        };

    };
};
