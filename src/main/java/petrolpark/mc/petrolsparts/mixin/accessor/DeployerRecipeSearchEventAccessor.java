package petrolpark.mc.petrolsparts.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;

@Mixin(DeployerRecipeSearchEvent.class)
public interface DeployerRecipeSearchEventAccessor {
    
    @Accessor("recipe")
    public void setRecipe(RecipeHolder<? extends Recipe<? extends RecipeInput>> recipe);

    @Accessor("maxPriority")
    public void setMaxPriority(int maxPriority);
};
