package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import com.simibubi.create.AllShapes;
import com.simibubi.create.api.contraption.transformable.TransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public interface IBevelCogWheelBlock extends TransformableBlock, IWrenchable {

    public static final VoxelShaper COG_SHAPE = new AllShapes.Builder(Block.box(1, 11, 1, 15, 16, 15)).forDirectional();
    //public static final ResourceKey<LootTable> LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/bevel_cogwheel"));

    @Override
    public default BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return transform(originalState, new StructureTransform(BlockPos.ZERO, targetedFace.getAxis(), Rotation.CLOCKWISE_90, Mirror.NONE));
    };
};
