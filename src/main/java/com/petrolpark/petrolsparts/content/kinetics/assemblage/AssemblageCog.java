package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.function.Consumer;

import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.util.Lang;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.StringRepresentable;

public enum AssemblageCog implements StringRepresentable {
    
    NONE,
    SMALL,
    LARGE,
    SMALL_COAXIAL,
    LARGE_COAXIAL;

    protected final String name;

    AssemblageCog() {
        name = Lang.asId(name());
    };

    @Override
    public String getSerializedName() {
        return name;
    };

    public void addTopPart(Axis axis, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: partAdder.accept(AssemblagePart.COGWHEELS.get(Direction.get(AxisDirection.POSITIVE, axis)));
            case LARGE: partAdder.accept(AssemblagePart.LARGE_COGWHEELS.get(Direction.get(AxisDirection.POSITIVE, axis)));
            case SMALL_COAXIAL: partAdder.accept(AssemblagePart.COAXIAL_COGWHEELS.get(Direction.get(AxisDirection.POSITIVE, axis)));
            case LARGE_COAXIAL: partAdder.accept(AssemblagePart.LARGE_COAXIAL_COGWHEELS.get(Direction.get(AxisDirection.POSITIVE, axis)));
        };
    };

    public void addMiddlePart(Axis axis, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: partAdder.accept(AssemblagePart.MIDDLE_COGWHEELS.get(axis));
            case LARGE: partAdder.accept(AssemblagePart.LARGE_MIDDLE_COGWHEELS.get(axis));
            case SMALL_COAXIAL: partAdder.accept(AssemblagePart.MIDDLE_COAXIAL_COGWHEELS.get(axis));
            case LARGE_COAXIAL: partAdder.accept(AssemblagePart.LARGE_MIDDLE_COAXIAL_COGWHEELS.get(axis));
        };
    };

    public void addBottomPart(Axis axis, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: partAdder.accept(AssemblagePart.COGWHEELS.get(Direction.get(AxisDirection.NEGATIVE, axis)));
            case LARGE: partAdder.accept(AssemblagePart.LARGE_COGWHEELS.get(Direction.get(AxisDirection.NEGATIVE, axis)));
            case SMALL_COAXIAL: partAdder.accept(AssemblagePart.COAXIAL_COGWHEELS.get(Direction.get(AxisDirection.NEGATIVE, axis)));
            case LARGE_COAXIAL: partAdder.accept(AssemblagePart.LARGE_COAXIAL_COGWHEELS.get(Direction.get(AxisDirection.NEGATIVE, axis)));
        };
    };

    public CogType getCogType() {
        return switch (this) {
            case NONE -> CogType.NONE;
            case SMALL -> CogType.SMALL;
            case LARGE -> CogType.LARGE;
            case SMALL_COAXIAL -> CogType.SMALL;
            case LARGE_COAXIAL -> CogType.LARGE;
        };
    };

    public boolean hasShaftConnection() {
        return this == SMALL || this == LARGE;
    };
};
