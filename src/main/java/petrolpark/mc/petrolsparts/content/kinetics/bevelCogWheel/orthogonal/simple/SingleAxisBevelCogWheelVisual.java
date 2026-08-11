package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelClientSet;

public class SingleAxisBevelCogWheelVisual extends KineticBlockEntityVisual<SingleAxisBevelCogWheelBlockEntity> implements SimpleTickableVisual {
  
    public static final SingleAxisBevelCogWheelVisual vanilla(VisualizationContext context, SingleAxisBevelCogWheelBlockEntity blockEntity, float partialTicks) {
        return new SingleAxisBevelCogWheelVisual(BevelCogWheelClientSet.vanilla(), context, blockEntity, partialTicks);
    };

    protected final BevelCogWheelClientSet set;
    protected final List<RotatingInstance> instances;

    public SingleAxisBevelCogWheelVisual(BevelCogWheelClientSet set, VisualizationContext context, SingleAxisBevelCogWheelBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        this.set = set;
        this.instances = new ArrayList<>();

        final SingleAxisBevelCogWheelBlock.Type type = blockEntity.getBlockState().getValue(SingleAxisBevelCogWheelBlock.TYPE);
        if (type.hasBottomCog()) {
            add(set.fourTeeth(), false);
            if (type.hasShaft()) add(set.cogCap(), false);
        };
        if (type.hasTopCog()) {
            add(set.fourTeeth(), true);
            if (type.hasShaft()) add(set.cogCap(), true);
        };
        if (type.hasShaft()) add(set.shaft(), true);
    };

    protected void add(PartialModel model, boolean top) {
        final RotatingInstance instance = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(model))
            .createInstance()
            .rotateToFace(Direction.DOWN, Direction.get(top ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, rotationAxis()))
            .setup(blockEntity)
            .setPosition(getVisualPosition());
        instances.add(instance);
        instance.setChanged();
    };

    @Override
    public void update(float partialTick) {
        instances.forEach(instance -> instance.setup(blockEntity).setChanged());
    };

    @Override
    public void tick(Context context) {
        instances.forEach(instance -> applyOverstressEffect(blockEntity, instance));
    };

    @Override
    public void updateLight(float partialTick) {
        instances.forEach(this::relight);
    };

    @Override
    protected void _delete() {
        instances.forEach(RotatingInstance::delete);
    };

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        instances.forEach(consumer);
    };
};
