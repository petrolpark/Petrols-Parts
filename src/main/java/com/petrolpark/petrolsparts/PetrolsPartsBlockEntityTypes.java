package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageRenderer;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.SingleShaftAssemblageRenderer;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.CoaxialGearBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.CoaxialGearVisual;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.LongShaftBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.LongShaftVisual;
import com.petrolpark.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelRenderer;
import com.petrolpark.petrolsparts.content.kinetics.cornerShaft.CornerShaftBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.cornerShaft.CornerShaftRenderer;
import com.petrolpark.petrolsparts.content.kinetics.cornerShaft.EncasedCornerShaftRenderer;
import com.petrolpark.petrolsparts.content.kinetics.differential.DifferentialBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.differential.DifferentialRenderer;
import com.petrolpark.petrolsparts.content.kinetics.differential.DummyDifferentialBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionRenderer;
import com.petrolpark.petrolsparts.content.kinetics.movement.MovementBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetBlockEntity;
import com.petrolpark.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetRenderer;
import com.petrolpark.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlockEntity;
import com.petrolpark.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeRenderer;
import com.petrolpark.petrolsparts.content.processing.brassDepot.BrassDepotBlockEntity;
import com.petrolpark.petrolsparts.content.processing.brassDepot.BrassDepotRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class PetrolsPartsBlockEntityTypes {

    public static final BlockEntityEntry<AssemblageBlockEntity> SEPARATE_SHAFT_HALVES_ASSEMBLAGE = REGISTRATE
        .createBlockEntity("separate_shaft_halves_assemblage", AssemblageBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE)
        .renderer(() -> AssemblageRenderer::new)
        .register();

    public static final BlockEntityEntry<AssemblageBlockEntity> SINGLE_SHAFT_ASSEMBLAGE = REGISTRATE
        .createBlockEntity("single_shaft_assemblage", AssemblageBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE)
        .renderer(() -> SingleShaftAssemblageRenderer::new)
        .register();

    public static final BlockEntityEntry<AssemblageBlockEntityPart> ASSEMBLAGE_PART = REGISTRATE
        .<AssemblageBlockEntityPart>blockEntity("assemblage_part", (t, p, s) -> new AssemblageBlockEntity(SEPARATE_SHAFT_HALVES_ASSEMBLAGE.get(), p, s).new AssemblageBlockEntityPart())
        .register();
    
    public static final BlockEntityEntry<BrassDepotBlockEntity> BRASS_DEPOT = REGISTRATE
        .createBlockEntity("brass_depot", BrassDepotBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.BRASS_DEPOT)
        .renderer(() -> BrassDepotRenderer::new)
        .register();

    public static final BlockEntityEntry<CoaxialGearBlockEntity> COAXIAL_GEAR = REGISTRATE
        .createBlockEntity("coaxial_gear", CoaxialGearBlockEntity::new)
        .visual(() -> CoaxialGearVisual::create, false)
        .validBlocks(PetrolsPartsBlocks.COAXIAL_GEAR, PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
        .renderer(() -> BracketedKineticBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<ColossalCogwheelBlockEntity> COLOSSAL_COGWHEEL = REGISTRATE
        .createBlockEntity("colossal_cogwheel", ColossalCogwheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
        .renderer(() -> ColossalCogwheelRenderer::new)
        .register();

    public static final BlockEntityEntry<CornerShaftBlockEntity> CORNER_SHAFT = REGISTRATE
        .createBlockEntity("corner_shaft", CornerShaftBlockEntity::new)
        //.visual(() -> CornerShaftvisual::new) //TODO fix
        .validBlock(PetrolsPartsBlocks.CORNER_SHAFT)
        .renderer(() -> CornerShaftRenderer::new)
        .register();

    public static final BlockEntityEntry<CornerShaftBlockEntity> ENCASED_CORNER_SHAFT = REGISTRATE
        .createBlockEntity("encased_corner_shaft", CornerShaftBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.ANDESITE_ENCASED_CORNER_SHAFT, PetrolsPartsBlocks.BRASS_ENCASED_CORNER_SHAFT)
        .renderer(() -> EncasedCornerShaftRenderer::new)
        .register();

    public static final BlockEntityEntry<DifferentialBlockEntity> DIFFERENTIAL = REGISTRATE
        .createBlockEntity("differential", DifferentialBlockEntity::new)
        //TODO visual
        .validBlock(PetrolsPartsBlocks.DIFFERENTIAL)
        .renderer(() -> DifferentialRenderer::new)
        .register();

    public static final BlockEntityEntry<DummyDifferentialBlockEntity> DUMMY_DIFFERENTIAL = REGISTRATE
        .createBlockEntity("dummy_differential", DummyDifferentialBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.DUMMY_DIFFERENTIAL)
        .register();

    public static final BlockEntityEntry<HydraulicTransmissionBlockEntity> HYDRAULIC_TRANSMISSION = REGISTRATE
        .createBlockEntity("hydraulic_transmission", HydraulicTransmissionBlockEntity::new)
        //.visual(() -> HydraulicTransmissionVisual::new)
        .validBlock(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
        .renderer(() -> HydraulicTransmissionRenderer::new)
        .register();

    public static final BlockEntityEntry<LongShaftBlockEntity> LONG_SHAFT = REGISTRATE
        .createBlockEntity("long_shaft", LongShaftBlockEntity::new)
        .visual(() -> LongShaftVisual::new, false)
        .validBlocks(PetrolsPartsBlocks.LONG_SHAFT)
        .renderer(() -> BracketedKineticBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<MovementBlockEntity> MOVEMENT = REGISTRATE
        .createBlockEntity("movement", MovementBlockEntity::new)
        .register();

    public static final BlockEntityEntry<MovementBlockEntity.GeneratingPart> MOVEMENT_GENERATING_PART = REGISTRATE
        .<MovementBlockEntity.GeneratingPart>blockEntity("movement_part", (t, p, s) -> new MovementBlockEntity(MOVEMENT.get(), p, s).new GeneratingPart())
        .register();

    public static final BlockEntityEntry<MovementBlockEntity.GeneratingPart> MOVEMENT_WINDING_PART = REGISTRATE
        .<MovementBlockEntity.GeneratingPart>blockEntity("winding_part", (t, p, s) -> new MovementBlockEntity(MOVEMENT.get(), p, s).new GeneratingPart())
        .register();

    public static final BlockEntityEntry<PlanetaryGearsetBlockEntity> PLANETARY_GEARSET = REGISTRATE
        .createBlockEntity("planetary_gearset", PlanetaryGearsetBlockEntity::new)
        //.visual(() -> PlanetaryGearsetVisual::new, false)
        .validBlocks(PetrolsPartsBlocks.PLANETARY_GEARSET)
        .renderer(() -> PlanetaryGearsetRenderer::new)
        .register();

    public static final BlockEntityEntry<PneumaticTubeBlockEntity> PNEUMATIC_TUBE = REGISTRATE
        .createBlockEntity("pneumatic_tube", PneumaticTubeBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.PNEUMATIC_TUBE)
        .renderer(() -> PneumaticTubeRenderer::new)
        .register();

    public static final void register() {};
};
