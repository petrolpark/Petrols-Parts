package petrolpark.mc.petrolsparts.compat.bitsnbobs;

import java.util.Collections;

import com.cake.azimuth.registration.BehaviourApplicators;
import com.kipti.bnb.content.kinetics.cogwheel_chain.behaviour.CogwheelChainBehaviour;

import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class PetrolsPartsBehaviourApplicators {
    
    public static final void register() {
        BehaviourApplicators.registerForType(PetrolsPartsBlockEntityTypes.ASSEMBLAGE_PART, be -> Collections.singletonList(new CogwheelChainBehaviour(be)));
        AssemblageCogwheelChains.register();
    };
};
