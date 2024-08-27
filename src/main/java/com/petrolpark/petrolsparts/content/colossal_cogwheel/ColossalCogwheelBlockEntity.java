package com.petrolpark.petrolsparts.content.colossal_cogwheel;

import java.util.List;

import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlock.Connection;
import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlock.Position;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementBehaviour;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTrigger;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ColossalCogwheelBlockEntity extends KineticBlockEntity {

    private PetrolsPartsAdvancementBehaviour advancementBehaviour;

    private int checkAdvancementTimer = 100;

    public ColossalCogwheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        checkAdvancementTimer = 100;
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        advancementBehaviour = new PetrolsPartsAdvancementBehaviour(this, PetrolsPartsAdvancementTrigger.COLOSSAL_COGWHEEL_POWER_MANY);
        behaviours.add(advancementBehaviour);
    };

    @Override
    protected AABB createRenderBoundingBox() {
        if (ColossalCogwheelBlock.isController(getBlockState())) {
            AABB aabb = new AABB(getBlockPos().offset(ColossalCogwheelBlock.getRelativeCenterPosition(getBlockState())));
            switch(getBlockState().getValue(RotatedPillarKineticBlock.AXIS)) {
                case X: return aabb.inflate(0d, 2d, 2d);
                case Y: return aabb.inflate(2d, 0d, 2d);
                case Z: return aabb.inflate(2d, 2d, 0d);
            };
        };
        return super.createRenderBoundingBox();
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (stateTo.getBlock() instanceof ColossalCogwheelBlock && ColossalCogwheelBlock.getRelativeCenterPosition(stateFrom).equals(diff.offset(ColossalCogwheelBlock.getRelativeCenterPosition(stateTo)))) return 1f;
        return propagateFromColossalCogwheel(stateFrom, stateTo, diff);
    };

    public static float propagateFromColossalCogwheel(BlockState colossalState, BlockState otherCogState, BlockPos diff) {
        BlockPos relCenter = ColossalCogwheelBlock.getRelativeCenterPosition(colossalState);
        boolean toLargeCog = ICogWheel.isLargeCog(otherCogState);
        if (toLargeCog || ICogWheel.isSmallCog(otherCogState)) {
            Axis axis = colossalState.getValue(RotatedPillarKineticBlock.AXIS);
            Position.Clock posClock = colossalState.getValue(ColossalCogwheelBlock.POSITION_CLOCK);
            for (Connection.Type connectionType : Connection.Type.values()) {
                if (relCenter.subtract(connectionType.relativeCenterPos.apply(axis, posClock.getDirection(axis))).equals(diff)) {
                    Connection connection = connectionType.connection;
                    if (connection.toLargeCog() == toLargeCog) return connection.ratio();
                };
            };
        };
        return 0f;
    };

    @Override
    public void tick() {
        super.tick();
        if (checkAdvancementTimer > 0) checkAdvancementTimer--;
        if (checkAdvancementTimer <= 0) {
            tryAwardCogsPoweringAdvancement();
            checkAdvancementTimer = 100;
        };
    };

    public void tryAwardCogsPoweringAdvancement() {
        BlockPos center = getBlockPos().offset(ColossalCogwheelBlock.getRelativeCenterPosition(getBlockState()));
        advancementBehaviour.awardAdvancementIf(PetrolsPartsAdvancementTrigger.COLOSSAL_COGWHEEL_POWER_MANY, () -> Connection.getAll(center, getBlockState().getValue(RotatedPillarKineticBlock.AXIS)).stream()
            .filter(pair -> {
                BlockPos pos = pair.getFirst();
                BlockEntity be = getLevel().getBlockEntity(pos);
                if (be instanceof KineticBlockEntity kbe && kbe.hasSource()) {
                    BlockState sourceState = getLevel().getBlockState(kbe.source);
                    return (sourceState.getBlock() instanceof ColossalCogwheelBlock && kbe.source.offset(ColossalCogwheelBlock.getRelativeCenterPosition(sourceState)).equals(center));
                };
                return false;
            }).count() >= 6
        );
    };
    
};
