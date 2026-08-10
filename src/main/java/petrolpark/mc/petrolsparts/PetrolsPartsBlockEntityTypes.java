package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import dev.engine_room.flywheel.lib.model.Models;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.DualDiagonalBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.DualDiagonalBevelCogWheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.CompositeBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.CompositeBevelCogWheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SingleAxisBevelCogWheelVisual;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.CornerShaftBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.CornerShaftRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.EncasedCornerShaftRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DifferentialBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DifferentialRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.overloadClutch.OverloadClutchBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.overloadClutch.OverloadClutchRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission.TransmissionBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission.TransmissionRenderer;
import petrolpark.mc.petrolsparts.content.legacy.LegacyCoaxialGearBlockEntity;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlockEntity;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeRenderer;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotBlockEntity;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotRenderer;
import petrolpark.mc.petrolsparts.content.processing.frictionHeater.FrictionHeaterBlockEntity;
import petrolpark.mc.petrolsparts.content.processing.frictionHeater.FrictionHeaterRenderer;

public class PetrolsPartsBlockEntityTypes {
    
    // Assemblage

    public static final BlockEntityEntry<AssemblageBlockEntity> ASSEMBLAGE = REGISTRATE
        .createBlockEntity("assemblage", AssemblageBlockEntity::new)
        .validBlocks(
            PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE, PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE,
            PetrolsPartsBlocks.ANDESITE_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE, PetrolsPartsBlocks.ANDESITE_ENCASED_SINGLE_SHAFT_ASSEMBLAGE,
            PetrolsPartsBlocks.BRASS_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE, PetrolsPartsBlocks.BRASS_ENCASED_SINGLE_SHAFT_ASSEMBLAGE
        )
        .renderer(() -> AssemblageRenderer::vanilla)
        .register();

    public static final BlockEntityEntry<AssemblageBlockEntityPart> ASSEMBLAGE_PART = REGISTRATE
        .<AssemblageBlockEntityPart>uninstantiableBlockEntity("assemblage_part")
        .register();

    // Bevel Cogwheel

    public static final BlockEntityEntry<KineticBlockEntity> SINGLE_AXIS_BEVEL_COGWHEEL = REGISTRATE
        .createBlockEntity("bevel_cogwheel/single_axis", KineticBlockEntity::new)
        .visual(() -> SingleAxisBevelCogWheelVisual::vanilla, false)
        .validBlocks(PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL)
        .renderer(() -> KineticBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<SimpleBevelCogWheelBlockEntity> SIMPLE_BEVEL_COGWHEEL = REGISTRATE
        .createBlockEntity("bevel_cogwheel/simple", SimpleBevelCogWheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS, PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS, PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS)
        .renderer(() -> SimpleBevelCogWheelRenderer::vanilla)
        .register();

    public static final BlockEntityEntry<CompositeBevelCogWheelBlockEntity> COMPOSITE_BEVEL_COGWHEEL = REGISTRATE
        .createBlockEntity("bevel_cogwheel/composite", CompositeBevelCogWheelBlockEntity::new)
        .validBlocks(
            PetrolsPartsBlocks.SINGLE_BEVEL_COGWHEEL_AND_SHAFT,
            PetrolsPartsBlocks.OPPOSITE_BEVEL_COGWHEELS, PetrolsPartsBlocks.OPPOSITE_BEVEL_COGWHEELS_AND_SHAFT,
            PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS_AND_SHAFT, PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS_AND_SHAFT, PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS_AND_SHAFT
        ).renderer(() -> CompositeBevelCogWheelRenderer::vanilla)
        .register();

    public static final BlockEntityEntry<CompositeBevelCogWheelBlockEntity.Part> COMPOSITE_BEVEL_COGWHEEL_PART = REGISTRATE
        .<CompositeBevelCogWheelBlockEntity.Part>uninstantiableBlockEntity("bevel_cogwheel/composite_part")
        .register();

    public static final BlockEntityEntry<SingleDiagonalBevelCogWheelBlockEntity> SINGLE_DIAGONAL_BEVEL_COGWHEEL = REGISTRATE
        .blockEntity("bevel_cogwheel/single_diagonal", SingleDiagonalBevelCogWheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL)
        .renderer(() -> SingleDiagonalBevelCogWheelRenderer::vanilla)
        .register();

    public static final BlockEntityEntry<DualDiagonalBevelCogWheelBlockEntity> DUAL_DIAGONAL_BEVEL_COGWHEEL = REGISTRATE
        .blockEntity("bevel_cogwheel/dual_diagonal", DualDiagonalBevelCogWheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.DUAL_DIAGONAL_BEVEL_COGWHEEL, PetrolsPartsBlocks.ANDESITE_ENCASED_DUAL_DIAGONAL_BEVEL_COGWHEEL, PetrolsPartsBlocks.BRASS_ENCASED_DUAL_DIAGONAL_BEVEL_COGWHEEL)
        .renderer(() -> DualDiagonalBevelCogWheelRenderer::create)
        .register();

    public static final BlockEntityEntry<DualDiagonalBevelCogWheelBlockEntity.Part> DUAL_DIAGONAL_BEVEL_COGWHEEL_PART = REGISTRATE
        .<DualDiagonalBevelCogWheelBlockEntity.Part>uninstantiableBlockEntity("bevel_cogwheel/dual_diagonal_part")
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

    public static final BlockEntityEntry<DifferentialBlockEntity.Part> DIFFERENTIAL_PART = REGISTRATE
        .<DifferentialBlockEntity.Part>uninstantiableBlockEntity("differential_part")
        .register();

    public static final BlockEntityEntry<FrictionHeaterBlockEntity> FRICTION_HEATER = REGISTRATE
        .createBlockEntity("friction_heater", FrictionHeaterBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.FRICTION_HEATER)
        .renderer(() -> FrictionHeaterRenderer::new)
        .register();

    public static final BlockEntityEntry<FrictionHeaterBlockEntity.Part> FRICTION_HEATER_PART = REGISTRATE
        .<FrictionHeaterBlockEntity.Part>uninstantiableBlockEntity("friction_heater_part")
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
        .<MovementBlockEntity.GeneratingPart>uninstantiableBlockEntity("movement/generating_part")
        .register();

    public static final BlockEntityEntry<MovementBlockEntity.WindingPart> MOVEMENT_WINDING_PART = REGISTRATE
        .<MovementBlockEntity.WindingPart>uninstantiableBlockEntity("movement/winding_part")
        .register();

    public static final BlockEntityEntry<OverloadClutchBlockEntity> OVERLOAD_CLUTCH = REGISTRATE
        .createBlockEntity("overload_clutch", OverloadClutchBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.OVERLOAD_CLUTCH)
        .renderer(() -> OverloadClutchRenderer::new)
        .register();

    public static final BlockEntityEntry<OverloadClutchBlockEntity.GeneratingPart> OVERLOAD_CLUTCH_GENERATING_PART = REGISTRATE
        .<OverloadClutchBlockEntity.GeneratingPart>uninstantiableBlockEntity("overload_clutch/generating_part")
        .register();

    public static final BlockEntityEntry<OverloadClutchBlockEntity.ImpactPart> OVERLOAD_CLUTCH_IMPACT_PART = REGISTRATE
        .<OverloadClutchBlockEntity.ImpactPart>uninstantiableBlockEntity("overload_clutch/impact_part")
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
