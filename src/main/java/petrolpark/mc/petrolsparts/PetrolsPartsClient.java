package petrolpark.mc.petrolsparts;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import petrolpark.mc.petrolsparts.core.ponder.PetrolsPartsPonderPlugin;
import petrolpark.mc.petrolsparts.core.ponder.PetrolsPartsPonderPlugin.PetrolsPartsCreatePonderPlugin;

@Mod(value = PetrolsParts.MOD_ID, dist = Dist.CLIENT)
public class PetrolsPartsClient {

    public PetrolsPartsClient(IEventBus modEventBus) {
        clientCtor(modEventBus);
    };

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PetrolsPartsCreatePonderPlugin());
        PonderIndex.addPlugin(new PetrolsPartsPonderPlugin());
    };

    public static final void clientCtor(IEventBus modEventBus) {
        modEventBus.addListener(PetrolsPartsClient::clientInit);
        PetrolsPartsPartialModels.init();
    };
};
