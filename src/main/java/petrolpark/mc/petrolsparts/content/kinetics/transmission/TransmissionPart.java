package petrolpark.mc.petrolsparts.content.kinetics.transmission;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;
import petrolpark.mc.library.compat.create.core.world.block.CreateMultiPartBlock;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.PetrolsPartsShapes;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;

public enum TransmissionPart implements CreateMultiPartBlock.ICreatePart {
    
    NORTH_COG(true, Direction.NORTH),
    SOUTH_COG(true, Direction.SOUTH),
    EAST_COG(true, Direction.EAST),
    WEST_COG(true, Direction.WEST),
    UP_COG(true, Direction.UP),
    DOWN_COG(true, Direction.DOWN),

    X_COG(true, Axis.X),
    Y_COG(true, Axis.Y),
    Z_COG(true, Axis.Z),

    X_SHAFT(false, Axis.X),
    Y_SHAFT(false, Axis.Y),
    Z_SHAFT(false, Axis.Z);

    public static final ResourceKey<LootTable> TRANSMISSION_SHAFT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("transmission_shaft"));

    public static final Map<Direction, TransmissionPart> FACIAL_COGS = Stream.of(NORTH_COG, SOUTH_COG, EAST_COG, WEST_COG, UP_COG, DOWN_COG).collect(Collectors.toMap(part -> part.place.left().get(), Function.identity()));
    public static final Map<Axis, TransmissionPart> AXIAL_COGS = Stream.of(X_COG, Y_COG, Z_COG).collect(Collectors.toMap(part -> part.place.right().get(), Function.identity()));
    public static final Map<Axis, TransmissionPart> SHAFTS = Stream.of(X_SHAFT, Y_SHAFT, Z_SHAFT).collect(Collectors.toMap(part -> part.place.right().get(), Function.identity()));

    protected final boolean cog;
    protected final Either<Direction, Axis> place;
    protected final VoxelShape shape;

    TransmissionPart(boolean cog, Direction face) {
        this(cog, Either.left(face));
    };

    TransmissionPart(boolean cog, Axis axis) {
        this(cog, Either.right(axis));
    };

    TransmissionPart(boolean cog, Either<Direction, Axis> place) {
        this.cog = cog;
        this.place = place;
        shape = cog ? place.map(PetrolsPartsShapes.FACIAL_COGWHEEL::get, PetrolsPartsShapes.MIDDLE_COGWHEEL::get) : place.map(PetrolsPartsShapes.TRANSMISSION_SHAFT::get, PetrolsPartsShapes.TRANSMISSION_SHAFT::get);
    };

    @Override
    public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
        return (cog ? PetrolsPartsItems.COAXIAL_COGWHEEL : PetrolsPartsBlocks.REDSTONE_TRANSMISSION).asStack();
    };

    @Override
    public VoxelShape shape() {
        return shape;
    };

    @Override
    public ResourceKey<LootTable> loot() {
        return cog ? AssemblageCog.SMALL_COAXIAL.getLootTable() : TRANSMISSION_SHAFT_LOOT; //TODO
    };

    @Override
    public ItemRequirement itemRequirement() {
        return new ItemRequirement(ItemUseType.CONSUME, (cog ? PetrolsPartsItems.COAXIAL_COGWHEEL : PetrolsPartsBlocks.REDSTONE_TRANSMISSION).asItem());
    };
    

};
