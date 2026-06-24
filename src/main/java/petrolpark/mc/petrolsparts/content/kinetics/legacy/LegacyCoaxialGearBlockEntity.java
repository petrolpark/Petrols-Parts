package petrolpark.mc.petrolsparts.content.kinetics.legacy;

import java.util.List;

import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated
public class LegacyCoaxialGearBlockEntity extends KineticBlockEntity {

    public LegacyCoaxialGearBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {};

    @Override
    public void tick() {
        super.tick();
        getLevel().setBlockAndUpdate(getBlockPos(), 
            (getBlockState().getValue(LegacyCoaxialGearBlock.HAS_SHAFT)
                ? PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE
                : PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE
            ).getDefaultState()
                .setValue(IAssemblageBlock.AXIS, getBlockState().getValue(LegacyCoaxialGearBlock.AXIS))
                .setValue(IAssemblageBlock.MIDDLE_COG, getBlockState().getBlock() instanceof LegacyCoaxialGearBlock legacyBlock
                    ? legacyBlock.isLargeCog()
                        ? AssemblageCog.LARGE
                        : AssemblageCog.SMALL
                    : AssemblageCog.NONE
                )
            );
    };
};
