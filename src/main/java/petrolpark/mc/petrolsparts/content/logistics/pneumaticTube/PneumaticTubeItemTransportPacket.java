package petrolpark.mc.petrolsparts.content.logistics.pneumaticTube;

import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsPackets;

import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record PneumaticTubeItemTransportPacket(BlockPos inputPos, ItemStack stack) implements ClientboundPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, PneumaticTubeItemTransportPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, PneumaticTubeItemTransportPacket::inputPos,
        ItemStack.STREAM_CODEC, PneumaticTubeItemTransportPacket::stack,
        PneumaticTubeItemTransportPacket::new
    );

    @Override
    public PacketTypeProvider getTypeProvider() {
        return PetrolsPartsPackets.PNEUMATIC_TUBE_ITEM_TRANSPORT;
    };

    @Override
    public void handle(LocalPlayer player) {
        player.level().getBlockEntity(inputPos, PetrolsPartsBlockEntityTypes.PNEUMATIC_TUBE.get())
            .flatMap(PneumaticTubeBlockEntity::asInput)
            .ifPresent(input -> input.transport(stack));
    };
    
};
