package petrolpark.mc.petrolsparts.core.block;

import com.simibubi.create.content.kinetics.base.IRotate;

import net.minecraft.world.level.block.state.BlockState;

public interface IStateDependentCogWheelBlock extends IRotate {
    
    public CogType getCogType(BlockState state);
};
