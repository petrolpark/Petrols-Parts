package petrolpark.mc.petrolsparts;

import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import petrolpark.mc.library.Petrolpark;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.PlaceCornerShaftsPacket;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeItemTransportPacket;

public enum PetrolsPartsPackets implements BasePacketPayload.PacketTypeProvider {

	PLACE_VANILLA_CORNER_SHAFTS(PlaceCornerShaftsPacket.class, PlaceCornerShaftsPacket.VANILLA_STREAM_CODEC),
    PNEUMATIC_TUBE_ITEM_TRANSPORT(PneumaticTubeItemTransportPacket.class, PneumaticTubeItemTransportPacket.STREAM_CODEC)
    ;

    private final CatnipPacketRegistry.PacketType<?> type;

	<T extends BasePacketPayload> PetrolsPartsPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		type = new CatnipPacketRegistry.PacketType<>(
			new CustomPacketPayload.Type<>(PetrolsParts.asResource(Lang.asId(name()))),
			clazz, codec
		);
	};

	@Override
	@SuppressWarnings("unchecked")
	public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
		return (CustomPacketPayload.Type<T>) type.type();
	};

	public static void register() {
		CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(Petrolpark.MOD_ID, 1);
		for (PetrolsPartsPackets packet : PetrolsPartsPackets.values()) {
			packetRegistry.registerPacket(packet.type);
		};
		packetRegistry.registerAllPackets();
	};
};
