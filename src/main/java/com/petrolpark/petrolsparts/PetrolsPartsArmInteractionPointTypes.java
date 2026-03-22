package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.petrolsparts.content.processing.brassDepot.BrassDepotArmInteractionPointType;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class PetrolsPartsArmInteractionPointTypes {
    
    public static final RegistryEntry<ArmInteractionPointType, BrassDepotArmInteractionPointType> BRASS_DEPOT = REGISTRATE.simple("brass_depot", CreateRegistries.ARM_INTERACTION_POINT_TYPE, BrassDepotArmInteractionPointType::new);

    public static final void register() {};
};
