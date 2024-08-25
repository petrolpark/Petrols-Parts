package com.petrolpark.petrolsparts;

import com.jozufozu.flywheel.core.PartialModel;

public class PetrolsPartsPartials {

    public static final PartialModel

    CHAIN_LINK = block("chain_link"),

    // Double Cardan Shaft
    DCS_CENTER_SHAFT = block("double_cardan_shaft/center_shaft"),
    DCS_SIDE_SHAFT = block("double_cardan_shaft/side_shaft"),
    DCS_SIDE_GRIP = block("double_cardan_shaft/side_grip"),
    DCS_GIMBAL = block("double_cardan_shaft/gimbal"),

    // Planetary Gearset
    PG_SUN_GEAR = block("planetary_gearset/sun_gear"),
    PG_PLANET_GEAR = block("planetary_gearset/planet_gear"),
    PG_RING_GEAR = block("planetary_gearset/ring_gear"),

    // Differential
    DIFFERENTIAL_RING_GEAR = block("differential/ring_gear"),
    DIFFERENTIAL_INPUT_GEAR = block("differential/input_gear"),
    DIFFERENTIAL_CONTROL_GEAR = block("differential/control_gear"),
    DIFFERENTIAL_EAST_GEAR = block("differential/east_gear"),
    DIFFERENTIAL_WEST_GEAR = block("differential/west_gear"),
    DIFFERENTIAL_INPUT_SHAFT = block("differential/input_shaft"),
    DIFFERENTIAL_CONTROL_SHAFT = block("differential/control_shaft");
    

    private static PartialModel block(String path) {
        return new PartialModel(PetrolsParts.asResource("block/"+path));
    };
};
