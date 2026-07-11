package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import petrolpark.mc.library.compat.create.core.world.block.MultiPartKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;

public abstract class SimpleBevelCogWheelBlock extends MultiPartKineticBlock<BevelCogWheelPart> implements IBevelCogWheelBlock {

    public SimpleBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public Item asItem() {
        return PetrolsPartsItems.BEVEL_COGWHEEL.get();
    };

    @Override
    public String getDescriptionId() {
        return TRANSLATION_KEY;
    };
    
};
