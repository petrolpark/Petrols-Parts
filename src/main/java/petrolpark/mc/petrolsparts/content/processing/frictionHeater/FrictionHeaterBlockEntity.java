package petrolpark.mc.petrolsparts.content.processing.frictionHeater;

import java.util.List;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsConfigs;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

public class FrictionHeaterBlockEntity extends CompositeKineticBlockEntity {

    public final FrictionHeaterBlockEntity.Part topPart, bottomPart;
    protected final List<FrictionHeaterBlockEntity.Part> parts;

    public FrictionHeaterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        parts = List.of(
            topPart = new FrictionHeaterBlockEntity.Part(true),
            bottomPart = new FrictionHeaterBlockEntity.Part(false)
        );
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {};

    @Override
    public List<FrictionHeaterBlockEntity.Part> getParts() {
        return parts;
    };

    public void updateHeat() {
        final float diff = Math.abs(topPart.getSpeed() - bottomPart.getSpeed());
        if (diff >= PetrolsPartsConfigs.server().superHeatedMinSpeedDifference.get()) {
            getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.SEETHING));
        } else if (diff >= PetrolsPartsConfigs.server().heatedMinSpeedDifference.get()) {
            getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.KINDLED));
        } else {
            getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.NONE));
        };
    };

    //TODO particles

    public class Part extends CompositeKineticBlockEntityPart implements IFaceAlignedCogWheelBlockEntity {

        public final boolean top;
        private final BlockState effectiveState = new DummyHalfBlock().defaultBlockState();

        Part(boolean top) {
            super(null);
            this.top = top;
        };

        @Override
        public CogType getCogType(Direction face) {
            return face.getAxis() == FrictionHeaterBlockEntity.super.getBlockState().getValue(FrictionHeaterBlock.HORIZONTAL_AXIS)
                && (face.getAxisDirection() == AxisDirection.POSITIVE == top)
                ? CogType.SMALL
                : CogType.NONE;
        };
        
        @Override
        protected Block getStressConfigKey() {
            return FrictionHeaterBlockEntity.super.getBlockState().getBlock();
        };

        @Override
        public void onSpeedChanged(float previousSpeed) {
            super.onSpeedChanged(previousSpeed);
            updateHeat();
        };

        @Override
        public BlockState getBlockState() {
            return effectiveState == null ? FrictionHeaterBlockEntity.super.getBlockState() : effectiveState; // Effective state can be null during initialization
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return oldState.getValue(FrictionHeaterBlock.HORIZONTAL_AXIS) == state.getValue(FrictionHeaterBlock.HORIZONTAL_AXIS);
        };

        @Override
        public int getIndex() {
            return top ? 0 : 1;
        };

        class DummyHalfBlock extends DummyBlock implements IRotate {

            DummyHalfBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return FrictionHeaterBlockEntity.super.getBlockState().getValue(FrictionHeaterBlock.HORIZONTAL_AXIS);
            };

            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                return face.getAxis() == getRotationAxis(state) && (face.getAxisDirection() == AxisDirection.POSITIVE == top);
            };

        };
    };
    
};
