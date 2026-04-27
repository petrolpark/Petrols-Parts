package com.petrolpark.petrolsparts.content.kinetics.bevelGear;

import java.util.Collection;

import com.petrolpark.core.world.block.multiPart.MultiPartBlock;
import com.petrolpark.petrolsparts.PetrolsParts;
import com.simibubi.create.AllShapes;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

public abstract class BevelGearBlock extends MultiPartBlock<BevelGearPart> {

    public static final VoxelShaper GEAR_SHAPE = new AllShapes.Builder(Block.box(1, 11, 1, 15, 16, 15)).forDirectional();
    public static final ResourceKey<LootTable> LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("bevel_gear"));

    public BevelGearBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public Collection<BevelGearPart> getParts(BlockState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getParts'");
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelGearPart part) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withoutPart'");
    };
    
};
