package petrolpark.mc.petrolsparts.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.compat.jei.category.MovementWeightRecipeCategory;

@JeiPlugin
public class PetrolsPartsJEI implements IModPlugin {

    public static final ResourceLocation ID = PetrolsParts.asResource("jei_plugin");

    // @Override
    // public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    //     final List<ItemStack> ingredientsToRemove = PetrolsPartsRemaps.COMPAT_ITEM_REMOVALS.stream()
    //         .filter(removal -> removal.condition().get())
    //         .map(CompatRemoval::id)
    //         .map(BuiltInRegistries.ITEM::getOptional)
    //         .flatMap(Optional::stream)
    //         .map(ItemStack::new)
    //         .toList();
    //     if (!ingredientsToRemove.isEmpty()) jeiRuntime.getJeiHelpers().getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ingredientsToRemove);
    // };

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
