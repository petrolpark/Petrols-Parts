package com.petrolpark.petrolsparts.core.advancement;

import java.util.stream.Stream;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.util.Lang;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public enum PetrolsPartsAdvancementTriggers {

    COAXIAL_GEAR,
    COLOSSAL_COGWHEEL_POWER_MANY,
    DIFFERENTIAL,
    PNEUMATIC_TUBE,
    ;

    private final String triggerId;
    private final String[] advancementIds;
    private final PetrolsPartsAdvancementTrigger trigger;

    PetrolsPartsAdvancementTriggers() {
        triggerId = Lang.asId(name());
        advancementIds = new String[]{Lang.asId(name())};
        trigger = new PetrolsPartsAdvancementTrigger();
    };

    public void award(Level level, Player player) {
        if (level.isClientSide()) return;
        if (player instanceof ServerPlayer serverPlayer) {
            trigger.trigger(serverPlayer);
        } else {
            PetrolsParts.LOGGER.warn("Could not award Petrol's Parts Advancement "+triggerId+" to client-side Player.");
        };
    };

    public boolean isAlreadyAwardedTo(LivingEntity player) {
		if (!(player instanceof ServerPlayer sp)) return true;
        for (String advancementId : advancementIds) {
            AdvancementHolder advancement = sp.getServer().getAdvancements().get(PetrolsParts.asResource(advancementId));
            if (advancement == null || sp.getAdvancements().getOrStartProgress(advancement).isDone()) return true;
        };
        return false;
	};

    public static void register() {
        Stream.of(values()).forEach(trigger -> {
			Registry.register(BuiltInRegistries.TRIGGER_TYPES, PetrolsParts.asResource(trigger.triggerId), trigger.trigger);
		});
    };
}
