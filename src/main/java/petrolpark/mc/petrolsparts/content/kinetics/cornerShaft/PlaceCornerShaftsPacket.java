package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.List;

import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import petrolpark.mc.library.util.ItemHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsPackets;

public record PlaceCornerShaftsPacket(List<Pair<BlockPos, BlockState>> statesToPlace) implements ServerboundPacketPayload {

    public static final StreamCodec<ByteBuf, PlaceCornerShaftsPacket> STREAM_CODEC = Pair.streamCodec(BlockPos.STREAM_CODEC, CatnipStreamCodecs.BLOCK_STATE).apply(ByteBufCodecs.list()).map(PlaceCornerShaftsPacket::new, PlaceCornerShaftsPacket::statesToPlace);

    @Override
    public PacketTypeProvider getTypeProvider() {
        return PetrolsPartsPackets.PLACE_CORNER_SHAFTS;
    };

    @Override
    public void handle(ServerPlayer player) {
        final InvWrapper inv = new InvWrapper(player.getInventory());
        for (Pair<BlockPos, BlockState> posAndState : statesToPlace()) {
            final BlockPos pos = posAndState.getFirst();
            if (!player.level().getBlockState(pos).canBeReplaced()) return;
            BlockState state = posAndState.getSecond();
            final ItemStack stack = ItemHelper.removeItem(inv, s -> s.is(state.getBlock().asItem()), player.hasInfiniteMaterials());
            if (!stack.isEmpty()) {
                place(player, pos, state, stack);
                continue;
            } else if (state.getBlock() instanceof ShaftBlock) { // Try substitute regular Shafts for Straight Corner Shafts
                final ItemStack straightCornerShaftStack = ItemHelper.removeItem(inv, PetrolsPartsBlocks.CORNER_SHAFT::isIn, player.hasInfiniteMaterials());
                if (!straightCornerShaftStack.isEmpty()) {
                    place(player, pos, PetrolsPartsBlocks.STRAIGHT_CORNER_SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, state.getValue(ShaftBlock.AXIS)), straightCornerShaftStack);
                    continue;
                };
            };
            return;
        };
    };

    private static final void place(Player player, BlockPos pos, BlockState state, ItemStack stack) {
        player.level().setBlock(pos, state, 11);
        BlockItem.updateCustomBlockEntityTag(player.level(), player, pos, stack);
        BlockItem.updateBlockEntityComponents(player.level(), pos, stack);
        state.getBlock().setPlacedBy(player.level(), pos, state, player, stack);
    };
    
};
