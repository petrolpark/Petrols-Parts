package com.petrolpark.petrolsparts;

import com.petrolpark.client.creativemodetab.CustomTab;
import com.simibubi.create.AllCreativeModeTabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PetrolsPartCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PetrolsParts.MOD_ID);

	public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register(
		"base",
		() -> new CustomTab.Builder(CreativeModeTab.Row.TOP, 0)
			.add(

                i(PetrolsPartsBlocks.COAXIAL_GEAR::asStack), i(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR::asStack), i(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT::asStack), i(PetrolsPartsBlocks.DIFFERENTIAL::asStack), i(PetrolsPartsBlocks.PLANETARY_GEARSET::asStack), i(PetrolsPartsBlocks.COLOSSAL_COGWHEEL::asStack)
			
			).title(Component.translatable("itemGroup.petrolsparts.base"))
			.withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
			.icon(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT::asStack)
			.build()
	);

    private static CustomTab.ITabEntry i(Supplier<ItemStack> item) {
        return new CustomTab.ITabEntry.Item(item);
    };

	public static final void register(IEventBus bus) {
		TABS.register(bus);
	};
};
