package petrolpark.mc.petrolsparts.content.processing.brassDepot;

import org.jetbrains.annotations.Nullable;

import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes.DepotPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BrassDepotArmInteractionPointType extends ArmInteractionPointType {

    @Override
    public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
        return PetrolsPartsBlocks.BRASS_DEPOT.has(state);
    };

    @Override
    public @Nullable ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
        return new DepotPoint(this, level, pos, state);
    };
    
};
