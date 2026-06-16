package com.petrolpark.petrolsparts.compat.jei;

import java.util.Optional;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.PetrolsPartsRemaps;
import com.petrolpark.petrolsparts.PetrolsPartsRemaps.CompatRemoval;
import com.petrolpark.petrolsparts.compat.jei.category.MovementWeightRecipeCategory;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class PetrolsPartsJEI implements IModPlugin {

    public static final ResourceLocation ID = PetrolsParts.asResource("jei_plugin");

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        jeiRuntime.getJeiHelpers().getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, PetrolsPartsRemaps.COMPAT_ITEM_REMOVALS.stream()
            .filter(removal -> removal.condition().get())
            .map(CompatRemoval::id)
            .map(BuiltInRegistries.ITEM::getOptional)
            .flatMap(Optional::stream)
            .map(ItemStack::new)
            .toList()
        );
    };

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MovementWeightRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    };

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MovementWeightRecipeCategory.RECIPE_TYPE, MovementWeightRecipeCategory.getRecipes(registration.getIngredientManager()));
    };

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(PetrolsPartsBlocks.MOVEMENT, MovementWeightRecipeCategory.RECIPE_TYPE);
    };

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    };
    
};
