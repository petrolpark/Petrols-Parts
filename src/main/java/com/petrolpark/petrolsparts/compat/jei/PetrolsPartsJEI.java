package com.petrolpark.petrolsparts.compat.jei;

import java.util.Optional;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsRemaps;
import com.petrolpark.petrolsparts.PetrolsPartsRemaps.CompatRemoval;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
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
    public ResourceLocation getPluginUid() {
        return ID;
    };
    
};
