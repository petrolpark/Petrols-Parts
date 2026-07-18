package petrolpark.mc.petrolsparts;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class PetrolsPartsSpriteShifts {

    public static final CTSpriteShiftEntry

    CLOSED_ANDESITE_ENCASED_COGWHEEL_SIDE_VERTICAL = vertical("closed_andesite_encased_assemblage_side"),
	CLOSED_ANDESITE_ENCASED_COGWHEEL_SIDE_HORIZONTAL = horizontal("closed_andesite_encased_assemblage_side"),
    OPEN_ANDESITE_ENCASED_COGWHEEL_SIDE_VERTICAL = vertical("open_andesite_encased_assemblage_side"),
	OPEN_ANDESITE_ENCASED_COGWHEEL_SIDE_HORIZONTAL = horizontal("open_andesite_encased_assemblage_side"),
	
	CLOSED_BRASS_ENCASED_COGWHEEL_SIDE_VERTICAL = vertical("closed_brass_encased_assemblage_side"),
	CLOSED_BRASS_ENCASED_COGWHEEL_SIDE_HORIZONTAL = horizontal("closed_brass_encased_assemblage_side"),
    OPEN_BRASS_ENCASED_COGWHEEL_SIDE_VERTICAL = vertical("open_brass_encased_assemblage_side"),
	OPEN_BRASS_ENCASED_COGWHEEL_SIDE_HORIZONTAL = horizontal("open_brass_encased_assemblage_side");

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
