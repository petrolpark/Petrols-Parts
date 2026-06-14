package com.petrolpark.petrolsparts;

import com.petrolpark.Petrolpark;
import com.petrolpark.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeItemTransportPacket;

import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;

public enum PetrolsPartsPackets implements BasePacketPayload.PacketTypeProvider {

    PNEUMATIC_TUBE_ITEM_TRANSPORT(PneumaticTubeItemTransportPacket.class, PneumaticTubeItemTransportPacket.STREAM_CODEC)
    ;

    private final CatnipPacketRegistry.PacketType<?> type;

	<T extends BasePacketPayload> PetrolsPartsPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		type = new CatnipPacketRegistry.PacketType<>(
			new CustomPacketPayload.Type<>(PetrolsParts.asResource(name().toLowerCase(Locale.ROOT))),
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
