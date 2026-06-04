package com.petrolpark.petrolsparts.content.kinetics.cornerShaft;

import java.util.List;
import java.util.Objects;

import com.simibubi.create.content.kinetics.base.IRotate;

import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.BlockFace;
import net.createmod.catnip.placement.IPlacementHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public class AutoShaftRoutingClientHelper {
  
    protected static BlockFace startFace = null;
    protected static BlockFace goalFace = null;
    protected static List<Pair<BlockPos, BlockState>> statesToPlace = null;

    public static final void cancel() {
        startFace = null;
        goalFace = null;
    };

    @SubscribeEvent
    public static final void tick(ClientTickEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        final ClientLevel level = mc.level;
        final LocalPlayer player = mc.player;
        if (level == null || player == null) return;

        if (startFace == null) return;

        // Check what face we should connect to
        final BlockFace targetedFace;
        if (mc.hitResult instanceof BlockHitResult bhr) {
            final BlockPos pos = bhr.getBlockPos();
            final BlockState state = level.getBlockState(pos);
            if (!player.isShiftKeyDown() && state.getBlock() instanceof IRotate rotate) {
                final List<Direction> facesWithShafts = IPlacementHelper.orderedByDistance(pos, bhr.getLocation(), dir -> rotate.hasShaftTowards(level, pos, state, dir));
                targetedFace = new BlockFace(pos, facesWithShafts.isEmpty() ? bhr.getDirection() : facesWithShafts.getFirst()).getOpposite();
            } else {
                targetedFace = new BlockFace(pos, bhr.getDirection()).getOpposite();
            };
        } else {
            targetedFace = null;
        };
        if (Objects.equals(goalFace, targetedFace)) return; // Already matching, nevermind
    };
};
