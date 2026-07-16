package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Consumer;

import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import petrolpark.mc.petrolsparts.core.block.CogType;

public enum AssemblageCog implements StringRepresentable {
    
    NONE,
    SMALL,
    LARGE,
    SMALL_COAXIAL,
    LARGE_COAXIAL;

    protected final String name;

    AssemblageCog() {
        this.name = Lang.asId(name());
    };

    public boolean isNone() {
        return this == NONE;
    };

    public boolean isSmall() {
        return this == SMALL || this == SMALL_COAXIAL;
    };

    public boolean isLarge() {
        return this == LARGE || this == LARGE_COAXIAL;
    };

    @Override
    public String getSerializedName() {
        return name;
    };

    public void addTopPart(AssemblageSet set, Axis axis, Consumer<AssemblagePart> partAdder) {
        addFaceAlignedPart(set, axis, AxisDirection.POSITIVE, partAdder);
    };

    public void addMiddlePart(AssemblageSet set, Axis axis, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: {
                partAdder.accept(set.middleCogWheelParts().get(axis));
                return;
            } case LARGE: {
                partAdder.accept(set.middleLargeCogWheelParts().get(axis));
                return;
            } case SMALL_COAXIAL: {
                partAdder.accept(set.middleCoaxialCogWheelParts().get(axis));
                return;
            } case LARGE_COAXIAL: {
                partAdder.accept(set.middleLargeCoaxialCogWheelParts().get(axis));
                return;
            }
        };
    };

    public void addBottomPart(AssemblageSet set, Axis axis, Consumer<AssemblagePart> partAdder) {
        addFaceAlignedPart(set, axis, AxisDirection.NEGATIVE, partAdder);
    };

    public void addFaceAlignedPart(AssemblageSet set, Axis axis, AxisDirection direction, Consumer<AssemblagePart> partAdder) {
        switch (this) {
            case NONE: return;
            case SMALL: {
                partAdder.accept(set.cogWheelParts().get(Direction.get(direction, axis)));
                return;
            } case LARGE: {
                partAdder.accept(set.largeCogWheelParts().get(Direction.get(direction, axis)));
                return;
            } case SMALL_COAXIAL: {
                partAdder.accept(set.coaxialCogWheelParts().get(Direction.get(direction, axis)));
                return;
            } case LARGE_COAXIAL: {
                partAdder.accept(set.largeCoaxialCogWheelParts().get(Direction.get(direction, axis)));
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

    public ResourceKey<LootTable> getLootTable(AssemblageSet set) {
        return switch (this) {
            case NONE -> BuiltInLootTables.EMPTY;
            case SMALL -> set.smallCogLoot();
            case LARGE -> set.largeCogLoot();
            case SMALL_COAXIAL -> set.coaxialCogLoot();
            case LARGE_COAXIAL -> set.largeCoaxialCogLoot();
        };
    };
};
