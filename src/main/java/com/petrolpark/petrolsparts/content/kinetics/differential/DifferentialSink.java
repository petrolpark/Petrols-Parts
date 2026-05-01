package com.petrolpark.petrolsparts.content.kinetics.differential;

import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class DifferentialSink extends KineticBlockEntity {

    public DifferentialSink(DifferentialBlockEntity owner) {
        super(PetrolsPartsBlockEntityTypes.DIFFERENTIAL.get(), owner.getBlockPos(), owner.getBlockState());
        this.level = owner.getLevel();
    }

    // getActualStressOf(sink) = members.get(sink) * |getTheoreticalSpeed()| = lastStressApplied * 1f
    @Override public float getTheoreticalSpeed() { return 1f; }
    @Override public float getGeneratedSpeed() { return 0f; }
    @Override public float calculateStressApplied() { return lastStressApplied; }

    public void setStressImpact(float impact) { lastStressApplied = impact; }
    public float getStressImpact() { return lastStressApplied; }

    public float lastNetworkCapacity = 0f;
    public float lastNetworkStress = 0f;

    @Override
    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        lastNetworkCapacity = maxStress;
        lastNetworkStress = currentStress;
    }
    @Override public void setChanged() {}
    @Override public void tick() {}
    @Override public void initialize() {}
    @Override public void remove() {}
    @Override protected void write(CompoundTag tag, HolderLookup.Provider r, boolean c) {}
    @Override protected void read(CompoundTag tag, HolderLookup.Provider r, boolean c) {}
};
