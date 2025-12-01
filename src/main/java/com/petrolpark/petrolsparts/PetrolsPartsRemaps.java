package com.petrolpark.petrolsparts;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class PetrolsPartsRemaps {
    
    @SubscribeEvent
    public static final void onRegister(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();
        ResourceKey<?> key = registry.key();
        
        if (key == Registries.BLOCK || key == Registries.ITEM || key == Registries.BLOCK_ENTITY_TYPE) {
            registry.addAlias(PetrolsParts.asResource("double_cardan_shaft"), PetrolsPartsBlocks.CORNER_SHAFT.getId());
        };
    };
};
