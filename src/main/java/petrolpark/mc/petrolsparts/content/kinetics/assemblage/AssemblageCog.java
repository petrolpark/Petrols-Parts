package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Consumer;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.core.block.CogType;

public enum AssemblageCog implements StringRepresentable {
    
    NONE(),
    SMALL(PetrolsPartsItems.SHAFTLESS_COGWHEEL),
    LARGE(PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL),
    SMALL_COAXIAL(PetrolsPartsItems.COAXIAL_COGWHEEL),
    LARGE_COAXIAL(PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL);

    protected final String name;
    protected final ItemEntry<? extends AssemblageBlockItem> item;
    protected final ResourceKey<LootTable> lootTable;

    AssemblageCog() {
        name = Lang.asId(name());
        this.item = null;
        this.lootTable = BuiltInLootTables.EMPTY;
    };

    AssemblageCog(ItemEntry<? extends AssemblageBlockItem> item) {
        name = Lang.asId(name());
        this.item = item;
        this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, item.getId().withPrefix("blocks/"));
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

    public ResourceKey<LootTable> getLootTable() {
        return lootTable;  
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
        if (isNone()) return ItemRequirement.NONE;
        return new ItemRequirement(ItemUseType.CONSUME, item.get());
    };
};
