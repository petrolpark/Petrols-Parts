package petrolpark.mc.petrolsparts.core;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.createmod.catnip.lang.FontHelper.Palette;
import net.minecraft.world.item.Item;
import petrolpark.mc.library.compat.create.core.registrate.AbstractPetrolparkCreateRegistrate;
import petrolpark.mc.library.core.registrate.builder.PetrolparkItemBuilder;
import petrolpark.mc.petrolsparts.PetrolsParts;

public final class PetrolsPartsRegistrate extends AbstractPetrolparkCreateRegistrate<PetrolsPartsRegistrate> {

    public PetrolsPartsRegistrate() {
        super(PetrolsParts.MOD_ID);
    };

    @Override
    public <T extends Item> PetrolparkItemBuilder<T, PetrolsPartsRegistrate> item(String name, NonNullFunction<Item.Properties, T> factory) {
        final PetrolparkItemBuilder<T, PetrolsPartsRegistrate> builder = super.item(name, factory);
        builder.onRegister(item -> TooltipModifier.REGISTRY.register(item, new ItemDescription.Modifier(item, Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(KineticStats.create(item)))));
        return builder;
    };
    
};
