package com.petrolpark.petrolsparts;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.petrolpark.compat.GetPetrolparkSharedFeatures;
import com.petrolpark.compat.SharedFeatureFlag;
import com.petrolpark.petrolsparts.core.PetrolsPartsRegistrate;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTriggers;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
    public static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static final PetrolsPartsRegistrate REGISTRATE = new PetrolsPartsRegistrate();

    public static final ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    };

    public static final MutableComponent translate(String key, Object... args) {
        return Component.translatable(MOD_ID + "." + key, args);
    };

    @OnlyIn(Dist.CLIENT)
    public static final LangBuilder langBuilder() {
        return new LangBuilder(MOD_ID);
    };

    public PetrolsParts(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        PetrolsPartsArmInteractionPointTypes.register();
        PetrolsPartsDataComponentTypes.register(modEventBus);
        PetrolsPartsPackets.register();
        PetrolsPartCreativeModeTab.register(modEventBus);
        PetrolsPartsBlocks.register();
        PetrolsPartsBlockEntityTypes.register();
        PetrolsPartsItems.register();

        PetrolsPartsConfigs.register(modLoadingContext, modContainer);
    
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(EventPriority.LOWEST, PetrolsPartsDatagen::gatherData);
    };

    @GetPetrolparkSharedFeatures
    public static final SharedFeatureFlag[] getEnabledSharedFeatureFlags() {
        return new SharedFeatureFlag[]{SharedFeatureFlag.HORSE_MILL, SharedFeatureFlag.REDSTONE_PROGRAMMER};
    };

    private void onRegister(final RegisterEvent event) {
		if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {
			PetrolsPartsAdvancementTriggers.register();
		};
	};

};
