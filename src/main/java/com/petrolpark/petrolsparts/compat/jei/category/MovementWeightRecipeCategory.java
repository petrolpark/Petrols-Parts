package com.petrolpark.petrolsparts.compat.jei.category;

import java.util.Comparator;
import java.util.List;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.PetrolsPartsDataMapTypes;
import com.petrolpark.util.Lang;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class MovementWeightRecipeCategory extends AbstractRecipeCategory<MovementWeightRecipeCategory.Recipe> {

    public static final RecipeType<MovementWeightRecipeCategory.Recipe> RECIPE_TYPE = new RecipeType<>(PetrolsParts.asResource("movement_weight"), MovementWeightRecipeCategory.Recipe.class);

    public MovementWeightRecipeCategory(IGuiHelper guiHelper) {
        super(
			RECIPE_TYPE,
			PetrolsParts.translate("gui.jei.category.movement_weight"),
			guiHelper.createDrawableItemLike(PetrolsPartsBlocks.MOVEMENT),
			120,
			18
		);
    };

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MovementWeightRecipeCategory.Recipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 1)
			.setStandardSlotBackground()
			.addItemStack(recipe.item());
    };

    @Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, MovementWeightRecipeCategory.Recipe recipe, IFocusGroup focuses) {
		builder.addText(PetrolsParts.translate("gui.jei.category.movement_weight.capacity", Lang.INT_DF.format(recipe.stressCapacity())), getWidth() - 24, getHeight())
			.setPosition(24, 0)
			.setTextAlignment(HorizontalAlignment.CENTER)
			.setTextAlignment(VerticalAlignment.CENTER)
			.setColor(0xFF808080);
	};

    public record Recipe(ItemStack item, float stressCapacity) {

    };

    public static final List<MovementWeightRecipeCategory.Recipe> getRecipes(IIngredientManager ingredientManager) {
        return BuiltInRegistries.ITEM.getDataMap(PetrolsPartsDataMapTypes.MOVEMENT_WEIGHT).entrySet().stream()
            .map(entry -> new MovementWeightRecipeCategory.Recipe(new ItemStack(BuiltInRegistries.ITEM.get(entry.getKey())), entry.getValue().stressCapacity()))
            .sorted(Comparator.comparingDouble(MovementWeightRecipeCategory.Recipe::stressCapacity))
            .toList();
	};
};
