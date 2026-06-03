package com.petrolpark.petrolsparts.content.kinetics.transmission;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Either;
import com.petrolpark.compat.create.core.block.CreateMultiPartBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.PetrolsPartsItems;
import com.petrolpark.petrolsparts.PetrolsPartsShapes;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;

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
    
    NORTH_END_CASING(false, Direction.NORTH),
    SOUTH_END_CASING(false, Direction.SOUTH),
    EAST_END_CASING(false, Direction.EAST),
    WEST_END_CASING(false, Direction.WEST),
    UP_END_CASING(false, Direction.UP),
    DOWN_END_CASING(false, Direction.DOWN),

    X_MIDDLE_CASING(false, Axis.X),
    Y_MIDDLE_CASING(false, Axis.Y),
    Z_MIDDLE_CASING(false, Axis.Z),

    X_FULL_CASING(Axis.X),
    Y_FULL_CASING(Axis.Y),
    Z_FULL_CASING(Axis.Z);

    public static final Map<Direction, TransmissionPart> FACIAL_COGS = Stream.of(NORTH_COG, SOUTH_COG, EAST_COG, WEST_COG, UP_COG, DOWN_COG).collect(Collectors.toMap(part -> part.place.left().get(), Function.identity()));
    public static final Map<Axis, TransmissionPart> AXIAL_COGS = Stream.of(X_COG, Y_COG, Z_COG).collect(Collectors.toMap(part -> part.place.right().get(), Function.identity()));
    public static final Map<Direction, TransmissionPart> END_CASINGS = Stream.of(NORTH_END_CASING, SOUTH_END_CASING, EAST_END_CASING, WEST_END_CASING, UP_END_CASING, DOWN_END_CASING).collect(Collectors.toMap(part -> part.place.left().get(), Function.identity()));
    public static final Map<Axis, TransmissionPart> MIDDLE_CASINGS = Stream.of(X_MIDDLE_CASING, Y_MIDDLE_CASING, Z_MIDDLE_CASING).collect(Collectors.toMap(part -> part.place.right().get(), Function.identity()));
    public static final Map<Axis, TransmissionPart> WHOLE_CASINGS = Map.of(Axis.X, X_FULL_CASING, Axis.Y, Y_FULL_CASING, Axis.Z, Z_FULL_CASING);

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
        shape = cog ? place.map(PetrolsPartsShapes.FACIAL_COGWHEEL::get, PetrolsPartsShapes.MIDDLE_COGWHEEL::get) : place.map(PetrolsPartsShapes.TRANSMISSION_END_CASING::get, PetrolsPartsShapes.TRANSMISSION_MIDDLE_CASING::get);
    };

    TransmissionPart(Axis axis) {
        this.cog = false;
        this.place = Either.right(axis);
        shape = PetrolsPartsShapes.TRANSMISSION_WHOLE_CASING.get(axis);
    };

    @Override
    public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
        return (cog ? PetrolsPartsItems.SHAFTLESS_COGWHEEL : PetrolsPartsBlocks.TRANSMISSION).asStack();
    };

    @Override
    public VoxelShape shape() {
        return shape;
    };

    @Override
    public ResourceKey<LootTable> loot() {
        return AssemblageCog.SMALL.getLootTable(); //TODO
    };

    @Override
    public ItemRequirement itemRequirement() {
        return new ItemRequirement(ItemUseType.CONSUME, (cog ? PetrolsPartsItems.SHAFTLESS_COGWHEEL : PetrolsPartsBlocks.TRANSMISSION).asItem());
    };
    

};
