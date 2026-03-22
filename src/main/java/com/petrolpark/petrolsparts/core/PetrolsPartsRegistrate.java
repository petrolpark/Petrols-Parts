package com.petrolpark.petrolsparts.core;

import com.petrolpark.PetrolparkRegistrate;
import com.petrolpark.core.registrate.builder.PetrolparkItemBuilder;
import com.petrolpark.petrolsparts.PetrolsParts;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.builders.BlockEntityBuilder.BlockEntityFactory;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.createmod.catnip.lang.FontHelper.Palette;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class PetrolsPartsRegistrate extends PetrolparkRegistrate {

    public PetrolsPartsRegistrate() {
        super(PetrolsParts.MOD_ID);
    };

    @Override
    public <T extends Item> PetrolparkItemBuilder<T, PetrolparkRegistrate> item(String name, NonNullFunction<Properties, T> factory) {
        final PetrolparkItemBuilder<T, PetrolparkRegistrate> builder = super.item(name, factory);
        builder.onRegister(item -> TooltipModifier.REGISTRY.register(item, new ItemDescription.Modifier(item, Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(KineticStats.create(item)))));
        return builder;
    };

	public <T extends BlockEntity, P> CreateBlockEntityBuilder<T, PetrolsPartsRegistrate> createBlockEntity(String name, BlockEntityFactory<T> factory) {
		return (CreateBlockEntityBuilder<T, PetrolsPartsRegistrate>) entry(name, (callback) -> CreateBlockEntityBuilder.create(this, this, name, callback, factory));
	};
    
};
