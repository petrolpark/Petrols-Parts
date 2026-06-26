package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.tterrag.registrate.util.entry.RegistryEntry;

import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotArmInteractionPointType;

public class PetrolsPartsArmInteractionPointTypes {
    
    public static final RegistryEntry<ArmInteractionPointType, BrassDepotArmInteractionPointType> BRASS_DEPOT = REGISTRATE.simple("brass_depot", CreateRegistries.ARM_INTERACTION_POINT_TYPE, BrassDepotArmInteractionPointType::new);

    public static final void register() {};
};
