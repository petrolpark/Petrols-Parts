package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import petrolpark.mc.petrolsparts.PetrolsPartsRemaps;
import petrolpark.mc.petrolsparts.PetrolsPartsRemaps.CompatRemoval;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    
    //TODO do this in a less invasive way
    @ModifyExpressionValue(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "startsWith"
        )
    )
    public boolean petrolsparts$disableConfiguredCompatRecipes(boolean original, @Local(ordinal = 0) ResourceLocation resourcelocation) {
        return original || (PetrolsPartsRemaps.COMPAT_RECIPE_REMOVALS.get(resourcelocation) instanceof CompatRemoval removal && removal != null && removal.condition().get());
    };
};
