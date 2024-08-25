package com.petrolpark.petrolsparts.core.advancement;

import com.petrolpark.advancement.SimpleAdvancementTrigger;
import com.petrolpark.petrolsparts.PetrolsParts;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public enum PetrolsPartsAdvancementTrigger {

    COAXIAL_GEAR,
    COLOSSAL_COGWHEEL_POWER_MANY,
    DIFFERENTIAL,
    ;

    private String triggerId;
    private String[] advancementIds;
    private SimpleAdvancementTrigger trigger;

    PetrolsPartsAdvancementTrigger() {
        triggerId = Lang.asId(name());
        advancementIds = new String[]{Lang.asId(name())};
        trigger = new SimpleAdvancementTrigger(PetrolsParts.asResource(triggerId));
    };

    public void award(Level level, Player player) {
        if (level.isClientSide()) return;
        if (player instanceof ServerPlayer serverPlayer) {
            trigger.trigger(serverPlayer);
        } else {
            PetrolsParts.LOGGER.warn("Could not award Destroy Advancement "+triggerId+" to client-side Player.");
        };
    };

    public boolean isAlreadyAwardedTo(LivingEntity player) {
		if (!(player instanceof ServerPlayer sp)) return true;
        for (String advancementId : advancementIds) {
            Advancement advancement = sp.getServer().getAdvancements().getAdvancement(PetrolsParts.asResource(advancementId));
            if (advancement == null || sp.getAdvancements().getOrStartProgress(advancement).isDone()) return true;
        };
        return false;
	};

    public static void register() {
        for (PetrolsPartsAdvancementTrigger e : values()) {
            CriteriaTriggers.register(e.trigger);
        };
    };
}
