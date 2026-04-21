package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.content.kinetics.base.IRotate;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IAssemblageBlock extends IRotate {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty TOP_SHAFT_HALF = BooleanProperty.create("top_shaft_half");
    public static final BooleanProperty BOTTOM_SHAFT_HALF = BooleanProperty.create("bottom_half_shaft");
    public static final EnumProperty<AssemblageCog> TOP_COG = EnumProperty.create("top_gear", AssemblageCog.class);
    public static final EnumProperty<AssemblageCog> MIDDLE_COG = EnumProperty.create("middle_gear", AssemblageCog.class);
    public static final EnumProperty<AssemblageCog> BOTTOM_COG = EnumProperty.create("bottom_gear", AssemblageCog.class);
    
    public boolean hasTopShaft(BlockState state);

    public boolean hasBottomShaft(BlockState state);
};
