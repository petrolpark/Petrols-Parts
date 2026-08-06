package petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission;

import java.util.BitSet;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

public class TransmissionBlockEntity extends KineticBlockEntity implements IFaceAlignedCogWheelBlockEntity {

    protected final BitSet cogs = new BitSet();
    protected int displacement = 0; // How many spaces the Cogs have moved since last tick

    public TransmissionBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);  
    };

    @Override
    public CogType getCogType(Direction face) {
        final Direction facing = getBlockState().getValue(TransmissionBlock.FACING);
        if (face == facing) return CogType.small(getBlockState().getValue(TransmissionBlock.UPPER_COG));
        if (face == facing.getOpposite()) return CogType.small(getBlockState().getValue(TransmissionBlock.LOWER_COG));
        return CogType.NONE;
    };

    @Override
    public void tick() {
        super.tick();
        displacement = 0;
    };

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if (compound.contains("Cogs")) {
            cogs.clear();
            ExtraCodecs.BIT_SET.parse(NbtOps.INSTANCE, compound.get("Cogs")).result()
                .ifPresent(cogs::or);
            displacement = compound.getInt("Displacement");
        };
    };

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if (!cogs.isEmpty()) {
            compound.put("Cogs", ExtraCodecs.BIT_SET.encodeStart(NbtOps.INSTANCE, cogs).getOrThrow());
            if (displacement != 0) compound.putInt("Displacement", displacement);
        };
    };
    
};
