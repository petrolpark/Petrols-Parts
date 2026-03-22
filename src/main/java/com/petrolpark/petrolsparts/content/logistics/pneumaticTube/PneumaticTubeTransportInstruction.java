package com.petrolpark.petrolsparts.content.logistics.pneumaticTube;

import java.util.Optional;

import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.instruction.PonderInstruction;
import net.minecraft.core.BlockPos;

public class PneumaticTubeTransportInstruction extends PonderInstruction {

    public final BlockPos pos;
    public final boolean blocking;

    protected int ticksRemaining = -1;
    protected Runnable removeCallback = () -> {};

    public PneumaticTubeTransportInstruction(BlockPos tubeInputPos, boolean blocking) {
        pos = tubeInputPos;
        this.blocking = blocking;
    };

    @Override
    public boolean isBlocking() {
        return blocking;
    };

    @Override
    public boolean isComplete() {
        return ticksRemaining == 0;
    };

    @Override
    public void onScheduled(PonderScene scene) {
        super.onScheduled(scene);
        if (isBlocking()) scene.addToSceneTime(getDuration(scene));
    };

    @Override
    public void tick(PonderScene scene) {
        if (ticksRemaining == -1) { // First tick
            ticksRemaining = getDuration(scene);
            removeCallback = getInput(scene).map(PneumaticTubeBlockEntity.Input::transportAnimationOnly).orElse(() -> {});
        };
        if (ticksRemaining > 0) ticksRemaining--;
    };

    @Override
    public void reset(PonderScene scene) {
        super.reset(scene);
        ticksRemaining = -1;
        removeCallback.run();
    };

    public Optional<PneumaticTubeBlockEntity.Input> getInput(PonderScene scene) {
        if (scene.getWorld().getBlockEntity(pos) instanceof PneumaticTubeBlockEntity tube) return tube.asInput();
        return Optional.empty();
    };

    protected int getDuration(PonderScene scene) {
        return getInput(scene).map(input -> {
            input.updateFromSpeed();
            return input.enclosing().getItemTransportDistance() / input.distanceMovedPerTick;
        }).orElse(0);
    };
    
};
