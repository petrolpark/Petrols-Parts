package com.petrolpark.petrolsparts;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.petrolpark.compat.GetPetrolparkSharedFeatures;
import com.petrolpark.compat.SharedFeatureFlag;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlockItem.GearOnShaftPlacementHelper;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlockItem.ShaftOnGearPlacementHelper;
import com.petrolpark.petrolsparts.core.PetrolsPartsRegistrate;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTriggers;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.createmod.catnip.lang.FontHelper.Palette;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(PetrolsParts.MOD_ID)
public class PetrolsParts {

    public static final String MOD_ID = "petrolsparts";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final PetrolsPartsRegistrate REGISTRATE = new PetrolsPartsRegistrate(MOD_ID);

    static {
		REGISTRATE.setTooltipModifierFactory(item -> {
			return new ItemDescription.Modifier(item, Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
		});
	};

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    };

    static {
        // Placement Helpers which need to come before Create's
        PlacementHelpers.register(new GearOnShaftPlacementHelper());
        PlacementHelpers.register(new ShaftOnGearPlacementHelper());
    };

    public PetrolsParts(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        PetrolsPartsPackets.register();
        PetrolsPartCreativeModeTab.register(modEventBus);
        PetrolsPartsBlocks.register();
        PetrolsPartsBlockEntityTypes.register();

        PetrolsPartsConfigs.register(modLoadingContext, modContainer);
    
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(EventPriority.LOWEST, PetrolsPartsDatagen::gatherData);
    };

    @GetPetrolparkSharedFeatures
    public static final SharedFeatureFlag[] getEnabledSharedFeatureFlags() {
        return new SharedFeatureFlag[]{SharedFeatureFlag.REDSTONE_PROGRAMMER};
    };

    private void onRegister(final RegisterEvent event) {
		if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {
			PetrolsPartsAdvancementTriggers.register();
		};
	};

};
