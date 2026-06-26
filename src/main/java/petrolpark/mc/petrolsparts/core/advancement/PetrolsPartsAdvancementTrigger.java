package petrolpark.mc.petrolsparts.core.advancement;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.advancement.CriterionTriggerBase;
import com.simibubi.create.foundation.advancement.SimpleCreateTrigger;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class PetrolsPartsAdvancementTrigger extends SimpleCriterionTrigger<PetrolsPartsAdvancementTrigger.Instance> {

	public void trigger(ServerPlayer player) {
		super.trigger(player, null);
	};

	public SimpleCreateTrigger.Instance instance() {
		return new SimpleCreateTrigger.Instance();
	};
    
    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    };
    
    public static class Instance extends CriterionTriggerBase.Instance {

		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player)
		).apply(instance, Instance::new));

		private final Optional<ContextAwarePredicate> player;

		public Instance() {
			player = Optional.empty();
		};

		public Instance(Optional<ContextAwarePredicate> player) {
			this.player = player;
		};

		@Override
		protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
			return true;
		};

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		};
	}
};
