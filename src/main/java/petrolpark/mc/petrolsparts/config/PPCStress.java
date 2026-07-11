package petrolpark.mc.petrolsparts.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.Create;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

public class PPCStress extends ConfigBase {
    
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_IMPACTS = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_CAPACITIES = new Object2DoubleOpenHashMap<>();

    protected final Map<ResourceLocation, ConfigValue<Double>> capacities = new HashMap<>();
	protected final Map<ResourceLocation, ConfigValue<Double>> impacts = new HashMap<>();

	@Override
	public void registerAll(ModConfigSpec.Builder builder) {
		builder.comment(".", Comments.su, Comments.impact)
			.push("impact");
		DEFAULT_IMPACTS.forEach((id, value) -> this.impacts.put(id, builder.define(id.getPath(), value)));
		builder.pop();

		builder.comment(".", Comments.su, Comments.capacity)
			.push("capacity");
		DEFAULT_CAPACITIES.forEach((id, value) -> this.capacities.put(id, builder.define(id.getPath(), value)));
		builder.pop();
	};

    @Override
    public String getName() {
        return "stressValues";
    };

    @Nullable
	public DoubleSupplier getImpact(Block block) {
		ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
		ConfigValue<Double> value = this.impacts.get(id);
		return value == null ? null : value::get;
	};

	@Nullable
	public DoubleSupplier getCapacity(Block block) {
		ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
		ConfigValue<Double> value = this.capacities.get(id);
		return value == null ? null : value::get;
	};

	public static <B extends AbstractBuilder<?,?, ?, ?>> NonNullUnaryOperator<B> setNoImpact() {
		return setImpact(0);
	};

	public static <B extends AbstractBuilder<?,?, ?, ?>> NonNullUnaryOperator<B> setImpact(double value) {
		return builder -> {
			ResourceLocation id = Create.asResource(builder.getName());
			DEFAULT_IMPACTS.put(id, value);
			return builder;
		};
	};

	public static <B extends AbstractBuilder<?,?, ?, ?>> NonNullUnaryOperator<B> setCapacity(double value) {
		return builder -> {
			ResourceLocation id = Create.asResource(builder.getName());
			DEFAULT_CAPACITIES.put(id, value);
			return builder;
		};
	};

	private static class Comments {
		static String su = "[in Stress Units]";
		static String impact = "Individual coefficients of stress impact of kinetic blocks.";
		static String capacity = "Individual stress capacities of kinetic blocks.";
	};
};
