package petrolpark.mc.petrolsparts.util;

import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import petrolpark.mc.library.core.world.block.multiPart.MultiPartBlock;
import petrolpark.mc.library.core.world.block.multiPart.MultiPartBlock.IPart;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblagePart;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;

public class ShaftHelper {

    public static boolean isShaftLikeAssemblage(BlockState state) {
        return state.getBlock() instanceof IAssemblageBlock assemblageBlock 
            && assemblageBlock.hasTopShaft(state)
            && assemblageBlock.hasBottomShaft(state);
            // && state.getValue(IAssemblageBlock.TOP_COG).isNone()
            // && state.getValue(IAssemblageBlock.MIDDLE_COG).isNone()
            // && state.getValue(IAssemblageBlock.BOTTOM_COG).isNone();
    };

    public static boolean isTargetingShaftPart(BlockPos pos, MultiPartBlock<?> block, BlockState state, Player player) {
        final IPart part = block.getTargetedPart(state, pos, player);
        if (part instanceof AssemblagePart assemblagePart) return assemblagePart.isShaft();
        if (part instanceof BevelCogWheelPart) return part instanceof BevelCogWheelPart.Shaft;
        return false;
    };
    
    /**
     * @param state
     * @return {@code true} if the state is functionally just a Shaft, and has the property {@link BlockStateProperties#AXIS}
     */
    public static boolean isShaftLike(BlockState state) {
        return ShaftBlock.isShaft(state)
            || AllBlocks.POWERED_SHAFT.has(state)
            || PetrolsPartsBlocks.STRAIGHT_CORNER_SHAFT.has(state)
            || isShaftLikeAssemblage(state)
            || (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelCogWheelBlock && bevelCogWheelBlock.getShaftAxis(state) != null);
    };

    public static final Predicate<BlockState> SHAFT_PLACEMENT_HELPER_ACTIVATION_STATE_PREDICATE = ShaftHelper::isShaftLike;

    /**
     * @param state
     * @return {@code true} if the state can be expanded upon by the Shaft block Placement Helper
     * The state is <strong>not</strong> guaranteed to have the property {@link BlockStateProperties#AXIS}
     */
    public static boolean isStateForShaftPlacementHelper(BlockState state) {
        return state.getBlock() instanceof AbstractSimpleShaftBlock
            || state.getBlock() instanceof PoweredShaftBlock
            || isShaftLikeAssemblage(state)
            || (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelCogWheelBlock && bevelCogWheelBlock.getShaftAxis(state) != null);
    };

    public static final Predicate<BlockState> SHAFT_PLACEMENT_HELPER_STATE_PREDICATE = ShaftHelper::isStateForShaftPlacementHelper;

    @Nullable
    public static Axis getAxisForShaftPlacementHelper(BlockState state) {
        if (state.hasProperty(BlockStateProperties.AXIS)) return state.getValue(BlockStateProperties.AXIS);
        if (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelCogWheelBlock) return bevelCogWheelBlock.getShaftAxis(state);
        return null;
    };

    public static final Function<BlockState, Axis> SHAFT_PLACEMENT_HELPER_AXIS_FUNCTION = ShaftHelper::getAxisForShaftPlacementHelper;
};
