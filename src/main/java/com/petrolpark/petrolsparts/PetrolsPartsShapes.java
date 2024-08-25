package com.petrolpark.petrolsparts;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.world.level.block.Block;

public class PetrolsPartsShapes {
    
    public static final VoxelShaper

    COAXIAL_GEAR = shape(2, 6, 2, 14, 10, 14)
        .forAxis(),

    PLANETARY_GEARSET = shape(0, 4.5, 0, 16, 11.5, 16)
        .add(5, 0, 5, 11, 16, 11)
        .forAxis();

    public static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AllShapes.Builder(Block.box(x1, y1, z1, x2, y2, z2));
    };
};
