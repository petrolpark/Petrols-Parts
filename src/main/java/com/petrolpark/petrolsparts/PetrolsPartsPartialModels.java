package com.petrolpark.petrolsparts;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class PetrolsPartsPartialModels {

    public static final PartialModel

    CHAIN_LINK = block("chain_link"),

    // Coaxial Gears
    COAXIAL_GEAR = block("coaxial_gear"),
    LARGE_COAXIAL_GEAR = block("large_coaxial_gear"),
    LONG_SHAFT = block("long_shaft"),

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
    DIFFERENTIAL_CONTROL_SHAFT = block("differential/control_shaft"),

    // Hydraulic Transmission
    HYDRAULIC_TRANSMISSION_INNER = block("hydraulic_transmission/inner"),
    HYDRAULIC_TRANSMISSION_PISTON = block("hydraulic_transmission/piston"),
    HYDRAULIC_TRANSMISSION_SEGMENT = block("hydraulic_transmission/segment"),

    // Pneumatic Tube
    PNEUMATIC_TUBE_SEGMENT = block("pneumatic_tube/segment"),
    PNEUMATIC_TUBE_SEGMENT_STICHED = block("pneumatic_tube/segment_stitched"),
    PNEUMATIC_TUBE_ARROWS = block("pneumatic_tube/arrows");

    private static PartialModel block(String path) {
        return PartialModel.of(PetrolsParts.asResource("block/"+path));
    };

    public static final void init() {};
};
