package com.petrolpark.petrolsparts.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(
    value = MechanicalPressBlockEntity.class,
    priority = 2000
)
public abstract class MechanicalPressBlockEntityMixin extends BasinOperatingBlockEntity {
    
    public MechanicalPressBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        throw new AssertionError();
    };

    @ModifyReturnValue(
        method = "Lcom/simibubi/create/content/kinetics/press/MechanicalPressBlockEntity;getRecipe(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;",
        at = @At("RETURN")
    )
    public Optional<RecipeHolder<PressingRecipe>> petrolsparts$filterRecipes(Optional<RecipeHolder<PressingRecipe>> original, ItemStack item) {
        if (original.isEmpty()) return original;
        final SingleRecipeInput input = new SingleRecipeInput(item);
        return getLevel().getBlockEntity(getBlockPos().below(2), PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get())
            .filter(depot -> depot.getHeldItem() == item) // Ensure we are actually Pressing the Depot, not an Item Entity above it
            .flatMap(depot ->
                original.filter(depot::matches) // Check if the existing Recipe (if added by another mixin) is acceptable
                    .or(() -> depot.pick(SequencedAssemblyRecipe.getRecipes(level, item, AllRecipeTypes.PRESSING.getType(), PressingRecipe.class, depot::matches)))
                    .or(() -> depot.pick(getLevel().getRecipeManager().getRecipesFor(AllRecipeTypes.PRESSING.getType(), input, level)
                        .stream()
                        .map(rh -> new RecipeHolder<>(rh.id(), (PressingRecipe)rh.value()))
                        .filter(depot::matches)
                        .toList()
                    ))
            );
    };
};
