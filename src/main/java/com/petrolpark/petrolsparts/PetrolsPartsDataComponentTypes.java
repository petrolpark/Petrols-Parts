package com.petrolpark.petrolsparts;

import java.util.function.UnaryOperator;

import org.jetbrains.annotations.ApiStatus;

import com.petrolpark.petrolsparts.content.kinetics.movement.MovementItemComponent;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PetrolsPartsDataComponentTypes {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PetrolsParts.MOD_ID);

    public static final DataComponentType<MovementItemComponent> MOVEMENT_DATA = register("movement", builder -> builder
        .persistent(MovementItemComponent.CODEC)
        .networkSynchronized(MovementItemComponent.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
		DATA_COMPONENTS.register(name, () -> type);
		return type;
	};

	@ApiStatus.Internal
	public static final void register(IEventBus modEventBus) {
		DATA_COMPONENTS.register(modEventBus);
	};
};
