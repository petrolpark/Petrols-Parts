package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import dev.engine_room.flywheel.lib.model.Models;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.DiagonalBevelCogWheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.SingleDiagonalBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.CornerShaftBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.CornerShaftRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.EncasedCornerShaftRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DifferentialBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DifferentialRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DummyDifferentialBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.legacy.LegacyCoaxialGearBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.transmission.TransmissionBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.transmission.TransmissionRenderer;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlockEntity;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeRenderer;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotBlockEntity;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotRenderer;

public class PetrolsPartsBlockEntityTypes {
    
    // Assemblage

    public static final BlockEntityEntry<AssemblageBlockEntity> ASSEMBLAGE = REGISTRATE
        .createBlockEntity("assemblage", AssemblageBlockEntity::new)
        .validBlocks(
            PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE, PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE,
            PetrolsPartsBlocks.ANDESITE_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE, PetrolsPartsBlocks.ANDESITE_ENCASED_SINGLE_SHAFT_ASSEMBLAGE,
            PetrolsPartsBlocks.BRASS_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE, PetrolsPartsBlocks.BRASS_ENCASED_SINGLE_SHAF_ASSEMBLAGE
        )
        .renderer(() -> AssemblageRenderer::new)
        .register();

    public static final BlockEntityEntry<AssemblageBlockEntityPart> ASSEMBLAGE_PART = REGISTRATE
        .<AssemblageBlockEntityPart>blockEntity("assemblage_part", (t, p, s) -> new AssemblageBlockEntity(ASSEMBLAGE.get(), p, s).new AssemblageBlockEntityPart())
        .register();

    // Bevel Cogwheel

    public static final BlockEntityEntry<SingleDiagonalBevelCogWheelBlockEntity> SINGLE_DIAGONAL_BEVEL_COGWHEEL = REGISTRATE
        .blockEntity("single_diagonal_bevel_cogwheel", SingleDiagonalBevelCogWheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL)
        .renderer(() -> DiagonalBevelCogWheelRenderer::new)
        .register();
    
    public static final BlockEntityEntry<BrassDepotBlockEntity> BRASS_DEPOT = REGISTRATE
        .createBlockEntity("brass_depot", BrassDepotBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.BRASS_DEPOT)
        .renderer(() -> BrassDepotRenderer::new)
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

    public static final BlockEntityEntry<BracketedKineticBlockEntity> STRAIGHT_CORNER_SHAFT = REGISTRATE
        .createBlockEntity("straight_corner_shaft", BracketedKineticBlockEntity::new)
        .visual(() -> (ctx, be, pt) -> new SingleAxisRotatingVisual<>(ctx, be, pt, Models.partial(PetrolsPartsPartialModels.STRAIGHT_CORNER_SHAFT)))
        .validBlock(PetrolsPartsBlocks.STRAIGHT_CORNER_SHAFT)
        .renderer(() -> BracketedKineticBlockEntityRenderer::new)
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

    public static final BlockEntityEntry<MovementBlockEntity> MOVEMENT = REGISTRATE
        .createBlockEntity("movement", MovementBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.MOVEMENT)
        .renderer(() -> MovementRenderer::new)
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

    public static final BlockEntityEntry<TransmissionBlockEntity> TRANSMISSION = REGISTRATE
        .createBlockEntity("transmission", TransmissionBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.REDSTONE_TRANSMISSION)
        .renderer(() -> TransmissionRenderer::new)
        .register();

    //OLD

    @Deprecated
    public static final BlockEntityEntry<LegacyCoaxialGearBlockEntity> LEGACY_COAXIAL_GEAR = REGISTRATE
        .createBlockEntity("coaxial_gear", LegacyCoaxialGearBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.LEGACY_COAXIAL_GEAR, PetrolsPartsBlocks.LEGACY_LARGE_COAXIAL_GEAR)
        .register();

    public static final void register() {};
};
