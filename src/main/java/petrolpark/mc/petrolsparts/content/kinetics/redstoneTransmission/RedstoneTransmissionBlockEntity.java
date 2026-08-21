package petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.animation.LerpedFloat.Chaser;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

public class RedstoneTransmissionBlockEntity extends KineticBlockEntity implements IFaceAlignedCogWheelBlockEntity {

    protected boolean updateBoundingBox = false;
    protected boolean forceUpdate = false;
    protected List<LerpedFloat> cogPositions = new ArrayList<>();

    public RedstoneTransmissionBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    protected AABB createRenderBoundingBox() {
        AABB aabb = super.createRenderBoundingBox();
        BlockPos pos = getBlockPos();
        BlockState state = getBlockState();
        final Direction facing  = state.getValue(RedstoneTransmissionBlock.FACING);
        final Vec3 vec = Vec3.atLowerCornerOf(state.getValue(RedstoneTransmissionBlock.FACING).getNormal());
        while (state.getValue(RedstoneTransmissionBlock.UPPER_CONNECTION)) {
            aabb = aabb.expandTowards(vec);
            pos = pos.relative(facing); 
            state = getLevel().getBlockState(pos);
            if (state.getBlock() != getBlockState().getBlock() || state.getValue(RedstoneTransmissionBlock.FACING) != facing || !state.getValue(RedstoneTransmissionBlock.LOWER_CONNECTION)) return aabb; // Badly formatted BlockStates
        };
        return aabb;
    };

    @Override
    public CogType getCogType(Direction face) {
        final Direction facing = getBlockState().getValue(RedstoneTransmissionBlock.FACING);
        if (face == facing) return CogType.small(getBlockState().getValue(RedstoneTransmissionBlock.UPPER_COG));
        if (face == facing.getOpposite()) return CogType.small(getBlockState().getValue(RedstoneTransmissionBlock.LOWER_COG));
        return CogType.NONE;
    };

    @Override
    public boolean tryToPlaceOnOtherFaces() {
        return false;
    };

    @Override
    public void tick() {
        super.tick();
        if (updateBoundingBox && getLevel().isClientSide()) {
            invalidateRenderBoundingBox();
            updateBoundingBox = false;
        };
        if (getBlockState().getValue(RedstoneTransmissionBlock.LOWER_CONNECTION)) { // Not the controller
            cogPositions.clear();
            return;
        };
        for (LerpedFloat cogPosition : cogPositions)
            cogPosition.tickChaser();
    };

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        updateBoundingBox = compound.getBoolean("UpdateBoundingBox");
        forceUpdate = compound.getBoolean("ForceUpdate");
        if (compound.contains("Cogs")) {
            final ListTag list = compound.getList("Cogs", Tag.TAG_COMPOUND);
            if (list.size() == cogPositions.size()) {
                for (int i = 0; i < list.size(); i++) cogPositions.get(i).readNBT(list.getCompound(i), clientPacket);
            } else {
                invalidateRenderBoundingBox();
                cogPositions = NBTHelper.readCompoundList(list, tag -> {
                    final LerpedFloat cogPosition = LerpedFloat.linear().chase(0d, 0d, Chaser.EXP); // Need to set Chaser function here
                    cogPosition.readNBT(tag, clientPacket);
                    return cogPosition;
                });
            }
        };
    };

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putBoolean("UpdateBoundingBox", updateBoundingBox);
        if (clientPacket) updateBoundingBox = false;
        compound.putBoolean("ForceUpdate", forceUpdate);
        compound.put("Cogs", NBTHelper.writeCompoundList(cogPositions, LerpedFloat::writeNBT));
    };
    
};
