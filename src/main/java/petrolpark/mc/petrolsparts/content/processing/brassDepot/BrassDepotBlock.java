package petrolpark.mc.petrolsparts.content.processing.brassDepot;

import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BrassDepotBlock extends DepotBlock {

    public BrassDepotBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };
    
    @Override
    public BlockEntityType<? extends DepotBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get();
    };
    
};
