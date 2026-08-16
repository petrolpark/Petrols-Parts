package petrolpark.mc.petrolsparts;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class PetrolsPartsSpriteShifts {

    public static final CTSpriteShiftEntry

    CLOSED_ANDESITE_ENCASED_ASSEMBLAGE_SIDE_VERTICAL = vertical("assemblage/encased/andesite/closed"),
	CLOSED_ANDESITE_ENCASED_ASSEMBLAGE_SIDE_HORIZONTAL = horizontal("assemblage/encased/andesite/closed"),
    OPEN_ANDESITE_ENCASED_ASSEMBLAGE_SIDE_VERTICAL = vertical("assemblage/encased/andesite/open"),
	OPEN_ANDESITE_ENCASED_ASSEMBLAGE_SIDE_HORIZONTAL = horizontal("assemblage/encased/andesite/open"),
	
	CLOSED_BRASS_ENCASED_ASSEMBLAGE_SIDE_VERTICAL = vertical("assemblage/encased/brass/closed"),
	CLOSED_BRASS_ENCASED_ASSEMBLAGE_SIDE_HORIZONTAL = horizontal("assemblage/encased/brass/closed"),
    OPEN_BRASS_ENCASED_ASSEMBLAGE_SIDE_VERTICAL = vertical("assemblage/encased/brass/open"),
	OPEN_BRASS_ENCASED_ASSEMBLAGE_SIDE_HORIZONTAL = horizontal("assemblage/encased/brass/open");

	public static final SpriteShiftEntry

	MOVEMENT_CHAIN = SpriteShifter.get(PetrolsParts.asResource("movement_chain"), PetrolsParts.asResource("movement_chain"));
    
    // private static final CTSpriteShiftEntry omni(String name) {
	// 	return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
	// };

	private static final CTSpriteShiftEntry horizontal(String name) {
		return getCT(AllCTTypes.HORIZONTAL, name);
	};

	private static final CTSpriteShiftEntry vertical(String name) {
		return getCT(AllCTTypes.VERTICAL, name);
	};

	private static final CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
		return CTSpriteShifter.getCT(type, PetrolsParts.asResource("block/" + blockTextureName), PetrolsParts.asResource("block/" + connectedTextureName + "_connected"));
	};

	private static final CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
		return getCT(type, blockTextureName, blockTextureName);
	};
};
