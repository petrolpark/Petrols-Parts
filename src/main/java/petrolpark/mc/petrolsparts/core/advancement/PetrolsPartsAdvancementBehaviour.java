package petrolpark.mc.petrolsparts.core.advancement;

import java.util.Set;
import java.util.function.Supplier;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import petrolpark.mc.library.compat.create.core.world.block.entity.behaviour.AbstractRememberPlacerBehaviour;

public class PetrolsPartsAdvancementBehaviour extends AbstractRememberPlacerBehaviour {

    public static final BehaviourType<PetrolsPartsAdvancementBehaviour> TYPE = new BehaviourType<>();

    private final Set<PetrolsPartsAdvancementTriggers> advancements;

    public PetrolsPartsAdvancementBehaviour(SmartBlockEntity be, PetrolsPartsAdvancementTriggers ...advancements) {
        super(be);
        this.advancements = Set.of(advancements);
    };

    public void awardAdvancement(PetrolsPartsAdvancementTriggers advancement) {
		awardAdvancementIf(advancement, () -> true);
	};

    /**
     * Trigger the given Destroy Advancement trigger conditionally.
     * @param advancement
     * @param condition Computation of this is saved until after we have checked whether the Player actually exists and doesn't already have the Advancement
     */
    public void awardAdvancementIf(PetrolsPartsAdvancementTriggers advancement, Supplier<Boolean> condition) {
        Player placer = getPlayer();
        if (placer == null || !(placer instanceof ServerPlayer player) || advancement.isAlreadyAwardedTo(player)) return;
        if (condition.get()) advancement.award(getWorld(), player);
    };

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    };

    @Override
    public boolean shouldRememberPlacer(Player placer) {
        return placer instanceof ServerPlayer player && (advancements.size() == 0 || !advancements.stream().allMatch(advancement -> advancement.isAlreadyAwardedTo(player)));
    };
};
