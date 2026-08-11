package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SingleAxisBevelCogWheelBlockEntity extends KineticBlockEntity {

    public SingleAxisBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public float calculateStressApplied() {
        if (!(getBlockState().getBlock() instanceof SimpleBevelCogWheelBlock block)) return super.calculateStressApplied();
        return lastStressApplied = block.getStressImpact(getBlockState());
    };

    @Override
    public float calculateAddedStressCapacity() {
        if (!(getBlockState().getBlock() instanceof SimpleBevelCogWheelBlock block)) return super.calculateAddedStressCapacity();
        return lastStressApplied = block.getStressCapacity(getBlockState());
    };
    
};
