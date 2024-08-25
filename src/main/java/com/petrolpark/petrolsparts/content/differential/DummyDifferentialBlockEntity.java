package com.petrolpark.petrolsparts.content.differential;

import java.util.List;

import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementBehaviour;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTrigger;
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
        advancementBehaviour = new PetrolsPartsAdvancementBehaviour(this, PetrolsPartsAdvancementTrigger.DIFFERENTIAL);
        behaviours.add(advancementBehaviour);
    };
    
};
