package petrolpark.mc.petrolsparts;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public class PetrolsPartsDatagen {
  
    public static void gatherData(GatherDataEvent event) {
		if (!event.getMods().contains(PetrolsParts.MOD_ID)) return;

		//event.getGenerator().addProvider(true, PetrolsParts.REGISTRATE.setDataProvider(new RegistrateDataProvider(PetrolsParts.REGISTRATE, PetrolsParts.MOD_ID, event)));
	};
};
