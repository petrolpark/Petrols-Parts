package petrolpark.mc.petrolsparts;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class PetrolsPartsPartialModels {

    public static final PartialModel

    //CHAIN_LINK = block("chain_link"),

    // Assemblage
    ASSEMBLAGE_SHAFT_ALL = block("assemblage/shaft/all"),
    ASSEMBLAGE_SHAFT_HALF_BOTTOM = block("assemblage/shaft/bottom_half"),
    ASSEMBLAGE_SHAFT_HALF_TOP = block("assemblage/shaft/top_half"),
    ASSEMBLAGE_SHAFT_TOP = block("assemblage/shaft/top"),
    ASSEMBLAGE_SHAFT_BOTTOM = block("assemblage/shaft/bottom"),
    ASSEMBLAGE_SHAFT_NO_TOP = block("assemblage/shaft/no_top"),
    ASSEMBLAGE_SHAFT_NO_BOTTOM = block("assemblage/shaft/no_bottom"),
    COAXIAL_COGWHEEL = block("assemblage/coaxial_cogwheel"),
    LARGE_COAXIAL_COGWHEEL = block("assemblage/large_coaxial_cogwheel"),
    COGWHEEL_SHAFT = block("assemblage/cogwheel_shaft"),

    // Bevel Cogwheel
    BEVEL_COGWHEEL = block("bevel_cogwheel/four_teeth"),
    BEVEL_COGWHEEL_FIVE_TEETH = block("bevel_cogwheel/five_teeth"),
    BEVEL_COGWHEEL_CAP = block("bevel_cogwheel/cog_cap"),

    // Corner Shaft
    CORNER_SHAFT_CENTER = block("corner_shaft/center_shaft"),
    CORNER_SHAFT_SIDE = block("corner_shaft/side_shaft"),
    CORNER_SHAFT_SIDE_GRIP = block("corner_shaft/side_grip"),
    CORNER_SHAFT_GIMBAL = block("corner_shaft/gimbal"),
    STRAIGHT_CORNER_SHAFT = block("corner_shaft/straight"),

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

    // Movement
    MOVEMENT_PENDULUM = block("movement/pendulum"),
    MOVEMENT_PENDULUM_WEIGHT = block("movement/pendulum_weight"),
    MOVEMENT_ESCAPEMENT_COG = block("movement/escapement_cog"),
    MOVEMENT_COIL = block("movement/coil"),
    MOVEMENT_CHAIN = block("movement/chain"),
    MOVEMENT_SHAFT = block("movement/shaft"),

    MOVEMENT_WEIGHT_IRON = movementWeight("iron"),
    MOVEMENT_WEIGHT_BRASS = movementWeight("brass"),
    MOVEMENT_WEIGHT_ANVIL = movementWeight("anvil"),
    MOVEMENT_WEIGHT_NETHERITE = movementWeight("netherite"),
    MOVEMENT_WEIGHT_HEAVY_CORE = movementWeight("heavy_core"),

    // Pneumatic Tube
    PNEUMATIC_TUBE_SEGMENT = block("pneumatic_tube/segment"),
    PNEUMATIC_TUBE_SEGMENT_STICHED = block("pneumatic_tube/segment_stitched"),
    PNEUMATIC_TUBE_ARROWS = block("pneumatic_tube/arrows");

    private static PartialModel block(String path) {
        return PartialModel.of(PetrolsParts.asResource("block/"+path));
    };

    private static PartialModel movementWeight(String path) {
        return block("movement/weight/" + path);
    };

    public static final void init() {};
};
