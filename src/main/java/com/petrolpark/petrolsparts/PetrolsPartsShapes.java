package com.petrolpark.petrolsparts;

import com.simibubi.create.AllShapes;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.world.level.block.Block;

public class PetrolsPartsShapes {
    
    public static final VoxelShaper

    PLANETARY_GEARSET = shape(0, 4.5, 0, 16, 11.5, 16)
        .add(5, 0, 5, 11, 16, 11)
        .forAxis(),
    
    SHAFT_HALF = shape(5, 8, 5, 11, 16, 11).forDirectional(),
    FACIAL_COGWHEEL = shape(2, 11, 2, 14, 15, 14).forDirectional(),
    MIDDLE_COGWHEEL = shape(2, 6, 2, 14, 10, 14).forAxis(),
    FACIAL_LARGE_COGWHEEL = shape(0, 11, 0, 16, 15, 16).forDirectional(),
    MIDDLE_LARGE_COGWHEEL = shape(0, 6, 0, 16, 10, 16).forAxis(),
    
    TRANSMISSION_SHAFT = shape(5d, 0d, 5d, 11d, 16d, 11d).forAxis();

    public static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AllShapes.Builder(Block.box(x1, y1, z1, x2, y2, z2));
    };
};
