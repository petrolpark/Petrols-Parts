package petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission;
// package petrolpark.mc.petrolsparts.content.hydraulic_transmission;

// import petrolpark.mc.petrolsparts.PetrolsPartsPartials;
// import petrolpark.mc.util.KineticsHelper;
// import com.simibubi.create.content.kinetics.base.RotatingInstance;
// import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;

// import dev.engine_room.flywheel.api.instance.Instancer;
// import dev.engine_room.flywheel.api.visualization.VisualizationContext;
// import net.minecraft.core.Direction;

// public class HydraulicTransmissionVisual extends SingleAxisRotatingVisual<HydraulicTransmissionBlockEntity> {
    
//     public HydraulicTransmissionVisual(VisualizationContext visualizationContext, HydraulicTransmissionBlockEntity blockEntity, float partialTick) {
//         super(visualizationContext, blockEntity, partialTick);
//     };

//     @Override
//     protected Instancer<RotatingInstance> getModel() {
//         Direction facing = blockEntity.getBlockState().getValue(HydraulicTransmissionBlock.FACING);
//         return getRotatingMaterial().getModel(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_INNER, blockEntity.getBlockState(), facing, () -> KineticsHelper.rotateToFace(facing.getOpposite()));
//     };
    
// };
