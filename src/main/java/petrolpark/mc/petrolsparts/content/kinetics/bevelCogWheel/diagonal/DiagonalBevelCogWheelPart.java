package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;
import petrolpark.mc.library.compat.create.core.world.block.CreateMultiPartBlock.ICreatePart;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.library.util.Orientation.OrientedVoxelShaper;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.PetrolsPartsShapes;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblagePart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.SimpleBevelCogWheelBlock;

public sealed interface DiagonalBevelCogWheelPart extends ICreatePart permits DiagonalBevelCogWheelPart.Cog, DiagonalBevelCogWheelPart.ShaftHalf {

    public static final Map<Orientation, DiagonalBevelCogWheelPart.Cog> COGS = Stream.of(Orientation.EDGE_ORIENTATIONS).collect(Collectors.toMap(Function.identity(), DiagonalBevelCogWheelPart.Cog::new));
    public static final Map<Direction, DiagonalBevelCogWheelPart.ShaftHalf> SHAFT_HALVES = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), ShaftHalf::new));

    public final class Cog implements DiagonalBevelCogWheelPart {

        public static final Orientation.OrientedVoxelShaper SHAPER = new OrientedVoxelShaper(Block.box(3d, 7d, 7d, 13d, 15d, 15d));

        public final Orientation orientation;
        private final VoxelShape shape;

        private Cog(Orientation orientation) {
            this.orientation = orientation;
            this.shape = SHAPER.get(orientation);
        };

        @Override
        public ItemRequirement itemRequirement() {
            return new ItemRequirement(ItemUseType.CONSUME, PetrolsPartsItems.BEVEL_COGWHEEL.asStack());
        };

        @Override
        public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
            return PetrolsPartsItems.BEVEL_COGWHEEL.asStack();
        };

        @Override
        public VoxelShape shape() {
            return shape;
        };

        @Override
        public ResourceKey<LootTable> loot() {
            return SimpleBevelCogWheelBlock.LOOT;
        };

    };

    public final class ShaftHalf implements DiagonalBevelCogWheelPart {

        public final Direction face;
        private final VoxelShape shape;

        private ShaftHalf(Direction face) {
            this.face = face;
            this.shape = PetrolsPartsShapes.SHAFT_HALF.get(face);
        };

        @Override
        public ItemRequirement itemRequirement() {
            return new ItemRequirement(ItemUseType.CONSUME, PetrolsPartsItems.SHAFT_HALF.asStack());
        };

        @Override
        public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
            return PetrolsPartsItems.SHAFT_HALF.asStack();
        };

        @Override
        public VoxelShape shape() {
            return shape;
        };

        @Override
        public ResourceKey<LootTable> loot() {
            return AssemblagePart.SHAFT_HALF_LOOT;
        };
    };
    
};
