package petrolpark.mc.petrolsparts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(SpoutBlockEntity.class)
public abstract class SpoutBlockEntityMixin extends SmartBlockEntity {
    
    public SpoutBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @ModifyArg(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/fluids/spout/FillingBySpout;canItemBeFilled(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Z"
        ),
        index = 0
    )
    public Level petrolsParts$filterCanBeFilled(Level level) {
        return level.getBlockEntity(getBlockPos().below(2), PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get())
            .<Level>map(BrassDepotBlockEntity::getFilteredRecipeManagerWorld)
            .orElse(level);
    };

    @ModifyArg(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/fluids/spout/FillingBySpout;getRequiredAmountForItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/neoforged/neoforge/fluids/FluidStack;)I"
        ),
        index = 0
    )
    public Level petrolsParts$filterGetRequiredAmount(Level level) {
        return level.getBlockEntity(getBlockPos().below(2), PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get())
            .<Level>map(BrassDepotBlockEntity::getFilteredRecipeManagerWorld)
            .orElse(level);
    };

    @ModifyArg(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/fluids/spout/FillingBySpout;fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/neoforged/neoforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;"
        ),
        index = 0
    )
    public Level petrolsParts$filterFillItem(Level level) {
        return level.getBlockEntity(getBlockPos().below(2), PetrolsPartsBlockEntityTypes.BRASS_DEPOT.get())
            .<Level>map(BrassDepotBlockEntity::getFilteredRecipeManagerWorld)
            .orElse(level);
    };

    
};
