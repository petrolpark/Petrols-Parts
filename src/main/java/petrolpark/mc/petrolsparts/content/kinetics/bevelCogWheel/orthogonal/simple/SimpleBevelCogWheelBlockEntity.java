package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.entity.ISplitShaftKineticBlockEntity;

public class SimpleBevelCogWheelBlockEntity extends SingleAxisBevelCogWheelBlockEntity implements ISplitShaftKineticBlockEntity {

    public SimpleBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public float getRotationSpeedModifier(Direction face) {
        return getRotationRatio(getBlockState(), face);
    };

    public static float getRotationRatio(BlockState state, Direction face) {
        if (!(state.getBlock() instanceof SimpleBevelCogWheelBlock bevelBlock)) return 1f; // e.g. a plain Shaft: always coaxial with its own source, never inverts

        final Direction primaryCogFace = bevelBlock.getPrimaryCogFace(state);
        final Axis shaftAxis = bevelBlock.getShaftAxis(state);
        if (shaftAxis != null && face.getAxis() == shaftAxis && face != bevelBlock.getPrimaryCogFace(state)) face = face.getOpposite();

        if (face.getAxis() != primaryCogFace.getAxis()) return face.getAxisDirection() == primaryCogFace.getAxisDirection() ? -1f : 1f;
        return face == primaryCogFace ? 1f : -1f;
    };

};
