package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.petrolpark.core.world.block.multiPart.MultiPartBlock;
import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsShapes;
import com.simibubi.create.Create;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AssemblagePart implements MultiPartBlock.IPart {

    public static final VoxelShaper SHAFT_HALF_SHAPER = PetrolsPartsShapes.shape(5, 8, 5, 11, 16, 11).forDirectional();
    public static final VoxelShaper COGWHEEL_SHAPER = PetrolsPartsShapes.shape(2, 12, 2, 14, 16, 14).forDirectional();
    public static final VoxelShaper MIDDLE_COGWHEEL_SHAPER = PetrolsPartsShapes.shape(2, 6, 2, 14, 10, 14).forAxis();
    public static final VoxelShaper LARGE_COGWHEEL_SHAPER = PetrolsPartsShapes.shape(0, 12, 0, 116, 16, 16).forDirectional();
    public static final VoxelShaper LARGE_MIDDLE_COGWHEEL_SHAPER = PetrolsPartsShapes.shape(0, 6, 0, 16, 10, 16).forAxis();

    public static final ResourceKey<LootTable> SHAFT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Create.asResource("block/shaft"));
    public static final ResourceKey<LootTable> SHAFT_HALF_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/shaft_half"));
    public static final ResourceKey<LootTable> COGWHEEL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/shaftless_cogwheel"));
    public static final ResourceKey<LootTable> LARGE_COGWHEEL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/large_shaftless_cogwheel"));
    public static final ResourceKey<LootTable> COAXIAL_COGWHEEL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/coaxial_cogwheel"));
    public static final ResourceKey<LootTable> LARGE_COAXIAL_COGWHEEL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/large_coaxial_cogwheel"));

    public static final UnaryOperator<BlockState> REMOVE_TOP_COG = state -> state.setValue(IAssemblageBlock.TOP_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> REMOVE_BOTTOM_COG = state -> state.setValue(IAssemblageBlock.BOTTOM_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> REMOVE_MIDDLE_COG = state -> state.setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.NONE);

    public static final UnaryOperator<BlockState> removeCog(Direction direction) {
        return direction.getAxisDirection() == AxisDirection.POSITIVE ? REMOVE_TOP_COG : REMOVE_BOTTOM_COG;
    };

    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;
    protected final UnaryOperator<BlockState> remover;

    public static final Map<Direction, AssemblagePart> SHAFT_HALVES = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(SHAFT_HALF_SHAPER.get(dir), SHAFT_HALF_LOOT, state -> state.setValue(dir.getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, false))));
    public static final Map<Direction, AssemblagePart> COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(COGWHEEL_SHAPER.get(dir), COGWHEEL_LOOT, removeCog(dir))));
    public static final Map<Axis, AssemblagePart> MIDDLE_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(MIDDLE_COGWHEEL_SHAPER.get(axis), COGWHEEL_LOOT, REMOVE_MIDDLE_COG)));
    public static final Map<Direction, AssemblagePart> LARGE_COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(LARGE_COGWHEEL_SHAPER.get(dir), LARGE_COGWHEEL_LOOT, removeCog(dir))));
    public static final Map<Axis, AssemblagePart> LARGE_MIDDLE_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(LARGE_MIDDLE_COGWHEEL_SHAPER.get(axis), LARGE_COGWHEEL_LOOT, REMOVE_MIDDLE_COG)));
    public static final Map<Direction, AssemblagePart> COAXIAL_COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(COGWHEEL_SHAPER.get(dir), COAXIAL_COGWHEEL_LOOT, removeCog(dir))));
    public static final Map<Axis, AssemblagePart> MIDDLE_COAXIAL_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(MIDDLE_COGWHEEL_SHAPER.get(axis), COAXIAL_COGWHEEL_LOOT, REMOVE_MIDDLE_COG)));
    public static final Map<Direction, AssemblagePart> LARGE_COAXIAL_COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(LARGE_COGWHEEL_SHAPER.get(dir), LARGE_COAXIAL_COGWHEEL_LOOT, removeCog(dir))));
    public static final Map<Axis, AssemblagePart> LARGE_MIDDLE_COAXIAL_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(LARGE_MIDDLE_COGWHEEL_SHAPER.get(axis), LARGE_COAXIAL_COGWHEEL_LOOT, REMOVE_MIDDLE_COG)));

    AssemblagePart(VoxelShape shape, ResourceKey<LootTable> loot, UnaryOperator<BlockState> remover) {
        this.shape = shape;
        this.loot = loot;
        this.remover = remover;
    };

    @Override
    public VoxelShape shape() {
        return shape;
    };

    @Override
    public ResourceKey<LootTable> loot() {
        return loot;
    };
    
};
