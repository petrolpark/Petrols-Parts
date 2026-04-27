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

    public boolean isNone() {
        return this == NONE;
    };

    @Override
    public String getSerializedName() {
        return name;
    };

    public void addTopPart(Axis axis, Consumer<AssemblagePart> partAdder) {
        addFaceAlignedPart(axis, AxisDirection.POSITIVE, partAdder);
    };

    public void addMiddlePart(Axis axis, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: {
                partAdder.accept(AssemblagePart.MIDDLE_COGWHEELS.get(axis));
                return;
            } case LARGE: {
                partAdder.accept(AssemblagePart.LARGE_MIDDLE_COGWHEELS.get(axis));
                return;
            } case SMALL_COAXIAL: {
                partAdder.accept(AssemblagePart.MIDDLE_COAXIAL_COGWHEELS.get(axis));
                return;
            } case LARGE_COAXIAL: {
                partAdder.accept(AssemblagePart.LARGE_MIDDLE_COAXIAL_COGWHEELS.get(axis));
                return;
            }
        };
    };

    public void addBottomPart(Axis axis, Consumer<AssemblagePart> partAdder) {
        addFaceAlignedPart(axis, AxisDirection.NEGATIVE, partAdder);
    };

    public void addFaceAlignedPart(Axis axis, AxisDirection direction, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: {
                partAdder.accept(AssemblagePart.COGWHEELS.get(Direction.get(direction, axis)));
                return;
            } case LARGE: {
                partAdder.accept(AssemblagePart.LARGE_COGWHEELS.get(Direction.get(direction, axis)));
                return;
            } case SMALL_COAXIAL: {
                partAdder.accept(AssemblagePart.COAXIAL_COGWHEELS.get(Direction.get(direction, axis)));
                return;
            } case LARGE_COAXIAL: {
                partAdder.accept(AssemblagePart.LARGE_COAXIAL_COGWHEELS.get(Direction.get(direction, axis)));
                return;
            }
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
