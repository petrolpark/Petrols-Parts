package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.createmod.catnip.net.base.BasePacketPayload.PacketTypeProvider;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsPackets;

public record CornerShaftSet(
    BlockEntry<? extends ShaftBlock> shaftBlock,
    BlockEntry<? extends CornerShaftBlock> cornerShaftBlock, BlockEntry<? extends ShaftBlock> straightCornerShaftBlock,
    BlockEntityEntry<? extends CornerShaftBlockEntity> cornerBlockEntity, BlockEntityEntry<? extends KineticBlockEntity> straightBlockEntity, BlockEntityEntry<? extends CornerShaftBlockEntity> encasedCornerShaftBlockEntity,
    PacketTypeProvider packetType, PlaceCornerShaftsPacket.Factory packetFactory
) {
  
    public static final CornerShaftSet VANILLA = new CornerShaftSet(
        AllBlocks.SHAFT,
        PetrolsPartsBlocks.CORNER_SHAFT, PetrolsPartsBlocks.STRAIGHT_CORNER_SHAFT,
        PetrolsPartsBlockEntityTypes.CORNER_SHAFT, PetrolsPartsBlockEntityTypes.STRAIGHT_CORNER_SHAFT,
        PetrolsPartsBlockEntityTypes.ENCASED_CORNER_SHAFT,
        PetrolsPartsPackets.PLACE_VANILLA_CORNER_SHAFTS, PlaceCornerShaftsPacket::vanilla
    );

    public static CornerShaftSet vanilla() {
        return VANILLA;
    };
};
