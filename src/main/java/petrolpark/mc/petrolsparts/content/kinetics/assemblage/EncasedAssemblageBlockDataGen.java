package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import net.createmod.catnip.data.Iterate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

public class EncasedAssemblageBlockDataGen {
    
    public static final <B extends EncasedSeparateShaftHalvesAssemblageBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> separateShaftHalvesBlockState(String prefix) {
        return (ctx, prov) -> {
        
            final MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

            final String templateModelPath = ctx.get().getSet().id().withPrefix("block/").withSuffix("/encased/" + prefix + "/").getPath();

            for (final Axis axis : Iterate.axes) {

                final int rotX = axis == Axis.Y ? 0 : 90;
                final int rotY = axis == Axis.X ? 90 : axis == Axis.Y ? 0 : 180;

                builder.part()
                    .modelFile(prov.models().getExistingFile(prov.modLoc(templateModelPath + "core")))
                    .rotationX(rotX)
                    .rotationY(rotY)
                    .addModel()
                    .condition(IAssemblageBlock.AXIS, axis)
                    .end();

                for (boolean open : Iterate.trueAndFalse) {

                    final String middleSuffix = open ? "open" : "closed";

                    builder.part()
                        .modelFile(prov.models().getExistingFile(prov.modLoc(templateModelPath + "middle_" + middleSuffix)))
                        .rotationX(rotX)
                        .rotationY(rotY)
                        .addModel()
                        .condition(IAssemblageBlock.AXIS, axis)
                        .condition(IAssemblageBlock.MIDDLE_COG, open ? new AssemblageCog[]{AssemblageCog.SMALL, AssemblageCog.LARGE, AssemblageCog.SMALL_COAXIAL, AssemblageCog.LARGE_COAXIAL} : new AssemblageCog[]{AssemblageCog.NONE})
                        .end();

                    for (boolean shaft : Iterate.trueAndFalse) {

                        for (boolean top : Iterate.trueAndFalse) {

                            final BooleanProperty shaftProperty = top ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF;
                            final EnumProperty<AssemblageCog> cogProperty = top ? IAssemblageBlock.TOP_COG : IAssemblageBlock.BOTTOM_COG;

                            final MultiPartBlockStateBuilder.PartBuilder endPartBuilder = builder.part()
                                .modelFile(prov.models().getExistingFile(prov.modLoc(templateModelPath + (top ? "top_" : "bottom_") + middleSuffix + (shaft ? "" : "_shaftless"))))
                                .rotationX(rotX)
                                .rotationY(rotY)
                                .addModel();
                                    
                                if (shaft && open) { // Shaft, cog
                                    endPartBuilder
                                        .nestedGroup()
                                            .condition(IAssemblageBlock.AXIS, axis)
                                        .end()
                                        .nestedGroup()
                                            .useOr()
                                            .nestedGroup()
                                                .condition(shaftProperty, true)
                                                .condition(cogProperty, AssemblageCog.SMALL_COAXIAL, AssemblageCog.LARGE_COAXIAL)
                                            .endNestedGroup()
                                            .nestedGroup()
                                                .condition(cogProperty, AssemblageCog.SMALL, AssemblageCog.LARGE)
                                            .endNestedGroup()
                                        .end();
                                } else {
                                    endPartBuilder.condition(IAssemblageBlock.AXIS, axis);
                                    if (!open) { // Shaft or shaftess, no cog
                                        endPartBuilder
                                            .condition(shaftProperty, shaft)
                                            .condition(cogProperty, AssemblageCog.NONE);
                                    } else { // Shaftless, cog
                                        endPartBuilder
                                            .condition(shaftProperty, false)
                                            .condition(cogProperty, AssemblageCog.SMALL_COAXIAL, AssemblageCog.LARGE_COAXIAL);
                                    };
                                };

                                endPartBuilder.end();
                        };

                        
                    };
                    
                };
            };
        };
    };

    public static final <B extends EncasedSingleShaftAssemblageBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> singleShaftBlockState(String prefix) {
        return (ctx, prov) -> {
        
            final MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

            final String templateModelPath = ctx.get().getSet().id().withPrefix("block/").withSuffix("/encased/" + prefix + "/").getPath();

            for (final Axis axis : Iterate.axes) {

                final int rotX = axis == Axis.Y ? 0 : 90;
                final int rotY = axis == Axis.X ? 90 : axis == Axis.Y ? 0 : 180;

                // Core
                builder.part()
                    .modelFile(prov.models().getExistingFile(prov.modLoc(templateModelPath + "core")))
                    .rotationX(rotX)
                    .rotationY(rotY)
                    .addModel()
                    .condition(IAssemblageBlock.AXIS, axis)
                    .end();

                for (boolean open : Iterate.trueAndFalse) {

                    final String suffix = open ? "open" : "closed";
                    final AssemblageCog[] cogPropertyValues = open ? new AssemblageCog[]{AssemblageCog.SMALL, AssemblageCog.LARGE, AssemblageCog.SMALL_COAXIAL, AssemblageCog.LARGE_COAXIAL} : new AssemblageCog[]{AssemblageCog.NONE};

                    builder.part()
                        .modelFile(prov.models().getExistingFile(prov.modLoc(templateModelPath + "middle_" + suffix)))
                        .rotationX(rotX)
                        .rotationY(rotY)
                        .addModel()
                        .condition(IAssemblageBlock.AXIS, axis)
                        .condition(IAssemblageBlock.MIDDLE_COG, cogPropertyValues)
                        .end();

                    for (boolean top : Iterate.trueAndFalse) {

                        builder.part()
                            .modelFile(prov.models().getExistingFile(prov.modLoc(templateModelPath + (top ? "top_" : "bottom_") + suffix)))
                            .rotationX(rotX)
                            .rotationY(rotY)
                            .addModel()
                            .condition(IAssemblageBlock.AXIS, axis)
                            .condition(top ? IAssemblageBlock.TOP_COG : IAssemblageBlock.BOTTOM_COG, cogPropertyValues)
                            .end();
                    };
                };
            };
        };
    };

    // public static final void models(RegistrateBlockstateProvider prov, String casing, ResourceLocation closedSideTexture, ResourceLocation openSideTexture, ResourceLocation endTexture, ResourceLocation insideTexture, ResourceLocation gearboxTexture, ResourceLocation particleTexture) {

    //     for (String model : List.of("bottom_closed_shaftless", "bottom_closed", "bottom_open_shaftless", "bottom_open", "core", "middle_closed", "middle_open", "top_closed_shaftless", "top_closed", "top_open_shaftless", "top_open")) {
    //         prov.models().withExistingParent("block/assemblage/encased/" + casing + "/" + model, PetrolsParts.asResource("block/assemblage/encased/andesite/" + model))
    //             .texture("open_side", openSideTexture)
    //             .texture("closed_side", closedSideTexture)
    //             .texture("end", endTexture)
    //             .texture("inside", insideTexture)
    //             .texture("gearbox", gearboxTexture)
    //             .texture("particle", particleTexture);
    //     };
    // };

    // public static final void brassModels(RegistrateBlockstateProvider prov) {
    //     models(prov, "brass", PetrolsParts.asResource("block/closed_brass_encased_assemblage_side"), PetrolsParts.asResource("block/open_brass_encased_assemblage_side"), AllBlocks.BRASS_CASING.getId().withPrefix("block/"), ResourceLocation.withDefaultNamespace("block/stripped_dark_oak_log_end"), Create.asResource("block/brass_gearbox"), AllBlocks.BRASS_CASING.getId().withPrefix("block/"));
    // };

    public static final <B extends EncasedAssemblageBlock> LootTable.Builder lootTableBuilder(B block) {
        final LootTable.Builder builder = lootTable();
        
        for (final AssemblageCog cog : AssemblageCog.values()) {
            if (cog.isNone()) continue;

            for (final EnumProperty<AssemblageCog> cogProperty : IAssemblageBlock.COG_PROPERTIES) {
                builder.withPool(lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(cogProperty, cog.getSerializedName())
                        )
                    ).setRolls(ConstantValue.exactly(1f))
                    .add(NestedLootTable.lootTableReference(cog.getLootTable(block.getSet())))
                );
            };
        };

        return builder;
    };

    public static final <B extends EncasedSeparateShaftHalvesAssemblageBlock> void separateShaftHalvesLoot(RegistrateBlockLootTables lt, B block) {
        final LootTable.Builder builder = lootTableBuilder(block);

        for (BooleanProperty shaftHalfProperty : IAssemblageBlock.SHAFT_HALF_PROPERTIES) {
            builder.withPool(lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(shaftHalfProperty, true)
                    )  
                ).setRolls(ConstantValue.exactly(1f))
                .add(NestedLootTable.lootTableReference(block.getSet().shaftHalfLoot()))
            );
        };

        lt.add(block, builder);
    };

    public static final <B extends EncasedSingleShaftAssemblageBlock> void singleShaftLoot(RegistrateBlockLootTables lt, B block) {
        lt.add(block, lootTableBuilder(block)
            .withPool(lootPool()
                .setRolls(ConstantValue.exactly(1f))
                .add(NestedLootTable.lootTableReference(block.getSet().shaftLoot()))
            )
        );
    };
};
