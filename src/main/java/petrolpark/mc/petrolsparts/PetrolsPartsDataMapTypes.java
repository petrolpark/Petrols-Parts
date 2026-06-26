package petrolpark.mc.petrolsparts;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementWeightData;

@EventBusSubscriber
public class PetrolsPartsDataMapTypes {
    
    public static final DataMapType<Item, MovementWeightData> MOVEMENT_WEIGHT = DataMapType.builder(
        PetrolsParts.asResource("movement_weight"),
        Registries.ITEM,
        MovementWeightData.CODEC
    ).build();

    @SubscribeEvent
    public static final void onRegisterDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(MOVEMENT_WEIGHT);
    };
};
