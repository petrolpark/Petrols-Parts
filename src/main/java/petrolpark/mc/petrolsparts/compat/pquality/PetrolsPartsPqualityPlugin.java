package petrolpark.mc.petrolsparts.compat.pquality;

import java.util.Collections;
import java.util.function.Consumer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.pquality.core.client.effectDescription.IQualityEffectDescription;
import petrolpark.mc.pquality.core.client.effectDescription.SimpleQualityEffectDescription;
import petrolpark.mc.pquality.core.plugin.IPqualityPlugin;
import petrolpark.mc.pquality.core.plugin.PQualityPlugin;

@PQualityPlugin
public class PetrolsPartsPqualityPlugin implements IPqualityPlugin {
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerEffectDescriptions(Consumer<IQualityEffectDescription> adder) {
        adder.accept(new SimpleQualityEffectDescription(PetrolsParts.asResource("movement"), () -> true, Collections.singletonList(PetrolsPartsBlocks.MOVEMENT.asStack())));
    };
};
