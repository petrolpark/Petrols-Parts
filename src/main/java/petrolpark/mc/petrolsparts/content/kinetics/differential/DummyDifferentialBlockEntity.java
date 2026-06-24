package petrolpark.mc.petrolsparts.content.kinetics.differential;

import java.util.List;

import petrolpark.mc.petrolsparts.core.advancement.PetrolsPartsAdvancementBehaviour;
import petrolpark.mc.petrolsparts.core.advancement.PetrolsPartsAdvancementTriggers;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DummyDifferentialBlockEntity extends SmartBlockEntity {

    public PetrolsPartsAdvancementBehaviour advancementBehaviour;

    public DummyDifferentialBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        advancementBehaviour = new PetrolsPartsAdvancementBehaviour(this, PetrolsPartsAdvancementTriggers.DIFFERENTIAL);
        behaviours.add(advancementBehaviour);
    };
    
};
