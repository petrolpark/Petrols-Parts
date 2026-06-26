package petrolpark.mc.petrolsparts.content.kinetics.movement;

import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.library.util.codec.CodecHelper;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsDataComponentTypes;

@EventBusSubscriber
public record MovementItemComponent(ItemStack weightStack, float storedRotations) implements TooltipProvider {
    
    public static final Codec<MovementItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.SINGLE_ITEM_CODEC.fieldOf("weight").forGetter(MovementItemComponent::weightStack),
        CodecHelper.POS_FLOAT.fieldOf("rotations").forGetter(MovementItemComponent::storedRotations)
    ).apply(instance, MovementItemComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MovementItemComponent> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, MovementItemComponent::weightStack,
        ByteBufCodecs.FLOAT, MovementItemComponent::storedRotations,
        MovementItemComponent::new
    );

    @Override
    public void addToTooltip(TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        tooltipAdder.accept(PetrolsParts.translate("tooltip.movement.weight", weightStack().getHoverName().copy().withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        tooltipAdder.accept(PetrolsParts.translate("tooltip.stored_rotations", Component.literal(Lang.INT_DF.format(storedRotations())).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
    };

    @SubscribeEvent
    public static final void onAddToTooltip(ItemTooltipEvent event) {
        event.getItemStack().addToTooltip(PetrolsPartsDataComponentTypes.MOVEMENT_DATA, event.getContext(), event.getToolTip()::add, event.getFlags());
    };
};