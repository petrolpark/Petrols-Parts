package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.function.Consumer;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.util.Lang;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

public enum AssemblageCog implements StringRepresentable {
    
    //TODO Item Requirements
    NONE(() -> ItemRequirement.NONE),
    SMALL(() -> ItemRequirement.NONE),
    LARGE(() -> ItemRequirement.NONE),
    SMALL_COAXIAL(() -> ItemRequirement.NONE),
    LARGE_COAXIAL(() -> ItemRequirement.NONE);

    protected final String name;
    protected final Supplier<ItemRequirement> itemRequirement;

    AssemblageCog(Supplier<ItemRequirement> itemRequirement) {
        name = Lang.asId(name());
        this.itemRequirement = Suppliers.memoize(itemRequirement);
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

    public ItemRequirement itemRequirement() {
        return itemRequirement.get();
    };

    public AssemblageCogWheelBlockItem item(Item.Properties properties) {
        return new AssemblageCogWheelBlockItem(this, properties);
    };
};
