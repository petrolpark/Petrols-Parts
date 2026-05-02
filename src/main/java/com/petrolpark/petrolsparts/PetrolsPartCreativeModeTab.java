package com.petrolpark.petrolsparts;

import java.util.function.Supplier;

import com.petrolpark.client.creativemodetab.CustomTab;
import com.petrolpark.compat.create.PetrolparkCreateBlocks;
import com.simibubi.create.AllCreativeModeTabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PetrolsPartCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PetrolsParts.MOD_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TABS.register(
		"base",
		() -> new CustomTab.Builder(CreativeModeTab.Row.TOP, 0)
			.add(

                i(PetrolsPartsBlocks.BRASS_DEPOT::asStack), i(PetrolsPartsItems.SHAFTLESS_COGWHEEL::asStack), i(PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL::asStack), i(PetrolsPartsItems.COAXIAL_COGWHEEL::asStack), i(PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL::asStack), i(PetrolsPartsBlocks.CORNER_SHAFT::asStack), i(PetrolsPartsBlocks.DIFFERENTIAL::asStack), i(PetrolsPartsBlocks.PLANETARY_GEARSET::asStack), i(PetrolsPartsBlocks.COLOSSAL_COGWHEEL::asStack), i(PetrolsPartsBlocks.PNEUMATIC_TUBE::asStack), i(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION::asStack), i(PetrolparkCreateBlocks.REDSTONE_PROGRAMMER::asStack)
			
			).title(Component.translatable("itemGroup.petrolsparts.base"))
			.withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
			.icon(PetrolsPartsBlocks.CORNER_SHAFT::asStack)
			.build()
	);

    private static CustomTab.ITabEntry i(Supplier<ItemStack> item) {
        return new CustomTab.ITabEntry.SingleItem(item);
    };

	public static final void register(IEventBus bus) {
		TABS.register(bus);
	};
};
