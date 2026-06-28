package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.joml.Vector3f;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractShaftBlock;

import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.BlockFace;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.platform.CatnipClientServices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import petrolpark.mc.library.compat.create.util.BlueprintOverlayHelper;
import petrolpark.mc.library.util.BigItemStack;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public class AutoShaftRoutingClientHelper {

    private static final Vector3f PARTICLE_COLOR = new Vector3f(0.3f, 0.9f, 0.5f);
  
    protected static BlockFace startFace = null;
    protected static BlockFace goalFace = null;
    protected static List<Pair<BlockPos, BlockState>> statesToPlace = null;
    protected static List<BigItemStack> itemRequirements = null;
    protected static List<Vec3> particleLocations = null;

    public static final void cancel() {
        startFace = null;
        goalFace = null;
        statesToPlace = null;
        itemRequirements = null;
        particleLocations = null;
    };

    @SubscribeEvent
    public static final void tick(ClientTickEvent.Post event) {
        final Minecraft mc = Minecraft.getInstance();
        final ClientLevel level = mc.level;
        final LocalPlayer player = mc.player;
        if (level == null || player == null) return;

        if (startFace == null) return;

        // Maybe cancel
        if (!PetrolsPartsBlocks.CORNER_SHAFT.isIn(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            cancel();
            return;
        };

        // Show path
        if (particleLocations != null) for (Vec3 loc : particleLocations) {
            if (level.getRandom().nextInt(10) == 0) level.addParticle(new DustParticleOptions(PARTICLE_COLOR, 1), loc.x(), loc.y(), loc.z(), 0, 0, 0);
        };

        // Show Item requirements
        if (itemRequirements != null) BlueprintOverlayHelper.displayRequiredItems(itemRequirements, true);

        // Check what face we should connect to
        final BlockFace targetedFace;
        if (mc.hitResult instanceof BlockHitResult bhr && bhr.getBlockPos().distSqr(startFace.getPos()) <= 256) {
            targetedFace = getFace(level, bhr).getOpposite();
        } else {
            targetedFace = null;
        };
        if (Objects.equals(goalFace, targetedFace)) return; // Already matching, don't change anything

        goalFace = targetedFace;
        if (goalFace == null) return;
        statesToPlace = AutoShaftRouting.getPath(level, startFace, goalFace);
        particleLocations = new ArrayList<>(statesToPlace.size() * 5);
        int straightShafts = 0;
        int cornerShafts = 0;
        for (Pair<BlockPos, BlockState> posAndState : statesToPlace) {
            if (posAndState.getSecond().getBlock() instanceof AbstractShaftBlock) straightShafts++;
            if (posAndState.getSecond().getBlock() instanceof AbstractCornerShaftBlock) cornerShafts++;
            final Vec3 center = Vec3.atCenterOf(posAndState.getFirst());
            particleLocations.add(center);
            for (Direction dir : AbstractCornerShaftBlock.getDirectionsConnectedByState(posAndState.getSecond())) {
                particleLocations.add(center.relative(dir, 0.4f));
                particleLocations.add(center.relative(dir, 0.2f));
            };
        };
        itemRequirements = List.of(new BigItemStack(AllBlocks.SHAFT, straightShafts), new BigItemStack(PetrolsPartsBlocks.CORNER_SHAFT, cornerShafts));
    };

    public static final BlockFace getFace(Level level, BlockHitResult bhr) {
        final BlockPos pos = bhr.getBlockPos();
        final BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof IRotate rotate) {
            final List<Direction> facesWithShafts = IPlacementHelper.orderedByDistance(pos, bhr.getLocation(), dir -> rotate.hasShaftTowards(level, pos, state, dir));
            for (Direction face : facesWithShafts) {
                if (level.getBlockState(pos.relative(face)).canBeReplaced()) return new BlockFace(pos, face);
            };
        };
        return new BlockFace(pos, bhr.getDirection());
    };

    public static final void tryPlace(BlockPlaceContext context) {
        if (startFace == null) {
            startFace = getFace(context.getLevel(), context.getHitResult());
        } else {
            if (statesToPlace != null && !statesToPlace.isEmpty()) {
                CatnipClientServices.NETWORK.sendToServer(new PlaceCornerShaftsPacket(statesToPlace));
                cancel();
            };
        };
    };
};
