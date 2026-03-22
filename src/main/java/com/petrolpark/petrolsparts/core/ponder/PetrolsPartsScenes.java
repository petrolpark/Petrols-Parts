package com.petrolpark.petrolsparts.core.ponder;

import com.petrolpark.client.ponder.instruction.CameraShakeInstruction;
import com.petrolpark.client.ponder.particle.PetrolparkEmitters;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.CoaxialGearBlock;
import com.petrolpark.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlockEntity;
import com.petrolpark.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeTransportInstruction;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PetrolsPartsScenes {

    public static final void brassDepot(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("brass_depot", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
        scene.world().setBlock(util.grid().at(3, 2, 2), Blocks.WATER.defaultBlockState(), false);
        scene.idle(10);

        final BlockPos depotPos = util.grid().at(2, 1, 2);
        final BlockPos abovePos = depotPos.above(2);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(10);
		final Vec3 filterPos = util.vector().blockSurface(depotPos, Direction.WEST);
        scene.overlay().showFilterSlotInput(filterPos, Direction.WEST, 60);
		scene.overlay().showText(60)
			.attachKeyFrame()
			.text("This text is defined in a language file")
			.placeNearTarget()
			.pointAt(filterPos);
		scene.idle(70);

        scene.world().showSection(util.select().position(abovePos), Direction.SOUTH);
		scene.world().createItemOnBeltLike(depotPos, Direction.NORTH, new ItemStack(Items.BUCKET));
		scene.idle(20);
		scene.world().modifyBlockEntityNBT(util.select().position(abovePos), SpoutBlockEntity.class, nbt -> nbt.putInt("ProcessingTicks", 20));
		scene.idle(20);
		scene.world().removeItemsFromBelt(depotPos);
		scene.world().createItemOnBeltLike(depotPos, Direction.UP, new ItemStack(Items.WATER_BUCKET));
		scene.world().modifyBlockEntityNBT(util.select().position(abovePos), SpoutBlockEntity.class, nbt -> nbt.putBoolean("Splash", true));
		scene.idle(30);
		scene.world().removeItemsFromBelt(depotPos);
		scene.world().hideSection(util.select().position(abovePos), Direction.SOUTH);
		scene.idle(20);
		final ElementLink<WorldSectionElement> pressLink = scene.world().showIndependentSection(util.select().position(abovePos.south()), Direction.SOUTH);
		scene.world().moveSection(pressLink, util.vector().of(0d, 0d, -1d), 0);

        scene.overlay().showText(60)
            .placeNearTarget()
            .pointAt(util.vector().blockSurface(abovePos, Direction.WEST))
            .text("This text is defined in a language file");

		final BlockPos pressPos = abovePos.south();
		final ItemStack copper = new ItemStack(Items.COPPER_INGOT);
		scene.world().createItemOnBeltLike(depotPos, Direction.NORTH, copper);
		final Vec3 depotCenter = util.vector().centerOf(depotPos);
		scene.idle(10);
		scene.world().modifyBlockEntity(pressPos, MechanicalPressBlockEntity.class, pte -> pte.getPressingBehaviour().start(PressingBehaviour.Mode.BELT));
		scene.idle(15);
		scene.world().modifyBlockEntity(pressPos, MechanicalPressBlockEntity.class, pte -> pte.getPressingBehaviour().makePressingParticleEffect(depotCenter.add(0, 8 / 16f, 0), copper));
		scene.world().removeItemsFromBelt(depotPos);
		scene.world().createItemOnBeltLike(depotPos, Direction.UP, AllItems.COPPER_SHEET.asStack());
		scene.idle(20);
		scene.world().hideIndependentSection(pressLink, Direction.SOUTH);
        scene.world().removeItemsFromBelt(depotPos);
		scene.idle(10);

        
        final BlockPos deployerPos = pressPos.south();
        final ElementLink<WorldSectionElement> deployerLink = scene.world().showIndependentSection(util.select().position(deployerPos), Direction.SOUTH);
		scene.world().moveSection(deployerLink, util.vector().of(0d, 0d, -2d), 0);
		final ItemStack strippedWood = new ItemStack(Items.STRIPPED_SPRUCE_LOG);
		scene.world().createItemOnBeltLike(depotPos, Direction.NORTH, strippedWood);
		scene.idle(10);
		scene.world().moveDeployer(deployerPos, 1f, 10);
		scene.idle(15);
		scene.world().removeItemsFromBelt(depotPos);
		scene.world().createItemOnBeltLike(depotPos, Direction.UP, AllBlocks.ANDESITE_CASING.asStack());
        scene.world().moveDeployer(deployerPos, -1f, 10);
		scene.idle(20);
		scene.world().hideIndependentSection(deployerLink, Direction.SOUTH);
		scene.idle(10);

		final Selection fanSelect = util.select().fromTo(4, 1, 3, 5, 2, 2)
			.add(util.select().position(3, 1, 2))
			.add(util.select().position(5, 0, 2));
		scene.world().showSection(fanSelect, Direction.SOUTH);
        final ElementLink<WorldSectionElement> waterLink = scene.world().showIndependentSection(util.select().position(3, 1, 0), Direction.SOUTH);
		scene.world().moveSection(waterLink, util.vector().of(0, 1, 2), 0);
		scene.idle(30);

		scene.world().hideSection(fanSelect, Direction.SOUTH);
        scene.world().hideIndependentSection(waterLink, Direction.SOUTH);
    };

    public static void coaxialGearShaftless(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("coaxial_gear_shaftless", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.idle(5);
        scene.world().showSection(util.select().position(2, 0, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(3, 1, 2, 3, 1, 5), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 2), Direction.EAST);
        scene.idle(5);
        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.WEST));
        scene.idle(80);

        scene.world().hideSection(util.select().position(3, 1, 4), Direction.EAST);
        scene.idle(15);
        ElementLink<WorldSectionElement> belt = scene.world().showIndependentSection(util.select().fromTo(3, 3, 4, 4, 3, 4), Direction.DOWN);
        scene.world().moveSection(belt, new Vec3(0d, -2d, 0d), 10);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(4, 1, 1, 4, 1, 4), Direction.SOUTH);
        scene.idle(5);

        int[][] cogs = new int[][]{new int[]{3, 1}, new int[]{2, 1}, new int[]{1, 1}, new int[]{1, 2}};
        for (int[] cog : cogs) {
            scene.idle(5);
            scene.world().showSection(util.select().position(cog[0], 1, cog[1]), Direction.EAST);
        };

        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(util.grid().at(1, 1, 2), Direction.UP));
        scene.idle(20);

        scene.effects().rotationDirectionIndicator(util.grid().at(1, 1, 1));
		scene.effects().rotationDirectionIndicator(util.grid().at(1, 1, 2));
        scene.idle(100);

        scene.markAsFinished();
    };

    public static void coaxialGearThrough(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("coaxial_gear_through", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        Selection verticalShaft1 = util.select().fromTo(3, 2, 2, 3, 3, 2);
        scene.world().setKineticSpeed(verticalShaft1, 0);
        scene.world().showSection(verticalShaft1, Direction.DOWN);
        scene.idle(30);

        BlockPos coaxialGear1 = util.grid().at(3, 2, 2);
        BlockPos longShaft1 = util.grid().at(3, 3, 2);

        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(coaxialGear1, Direction.UP));
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(coaxialGear1, Direction.NORTH), Pointing.RIGHT, 60)
            .withItem(PetrolsPartsBlocks.COAXIAL_GEAR.asStack());
        scene.idle(5);
        scene.world().setBlock(coaxialGear1, PetrolsPartsBlocks.COAXIAL_GEAR.getDefaultState().setValue(CoaxialGearBlock.HAS_SHAFT, true), false);
        scene.world().setBlock(longShaft1, PetrolsPartsBlocks.LONG_SHAFT.getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, Axis.Y).setValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION, false), false);
        scene.idle(55);

        scene.world().showSection(util.select().fromTo(1, 2, 2, 1, 3, 2), Direction.DOWN);
        scene.idle(20);

        BlockPos coaxialGear2 = util.grid().at(1, 2, 2);
        BlockPos longShaft2 = util.grid().at(1, 3, 2);

        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().blockSurface(coaxialGear2, Direction.UP));
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(coaxialGear2, Direction.NORTH), Pointing.RIGHT, 60)
            .withItem(AllBlocks.SHAFT.asStack());
        scene.idle(5);
        scene.world().setBlock(coaxialGear2, PetrolsPartsBlocks.COAXIAL_GEAR.getDefaultState().setValue(CoaxialGearBlock.HAS_SHAFT, true), false);
        scene.world().setBlock(longShaft2, PetrolsPartsBlocks.LONG_SHAFT.getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, Axis.Y).setValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION, false), false);
        scene.idle(65);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .placeNearTarget()
            .attachKeyFrame()
            .colored(PonderPalette.RED);
        scene.idle(100);

        scene.world().showSection(util.select().fromTo(1, 0, 5, 3, 1, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(1, 1, 4, 3, 1, 4), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(1, 1, 3, 3, 1, 3), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(1, 1, 2, 3, 1, 2), Direction.SOUTH);
        scene.world().setKineticSpeed(util.select().position(longShaft1), -32);
        scene.world().setKineticSpeed(util.select().position(longShaft2), -32);
        scene.idle(10);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(coaxialGear2, Direction.UP));
        scene.idle(100);

        BlockPos cogwheel = util.grid().at(2, 2, 2);

        scene.world().showSection(util.select().position(5, 0, 2), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(4, 1, 2, 4, 2, 2), Direction.DOWN);
        scene.world().setKineticSpeed(util.select().position(coaxialGear1), 8);
        scene.idle(5);
        scene.world().setKineticSpeed(util.select().position(cogwheel), -8);
        scene.world().showSection(util.select().position(cogwheel), Direction.DOWN);
        scene.world().setKineticSpeed(util.select().position(coaxialGear2), 8);
        scene.idle(25);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(coaxialGear2, Direction.EAST));
        scene.idle(20);

        scene.effects().rotationDirectionIndicator(coaxialGear1);
		scene.effects().rotationDirectionIndicator(coaxialGear2);
        scene.effects().rotationDirectionIndicator(longShaft1);
		scene.effects().rotationDirectionIndicator(longShaft2);

        scene.idle(80);

        scene.markAsFinished();
    };

    public static void colossalCogwheel(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("colossal_cogwheel", "This text is defined in a language file.");
        scene.configureBasePlate(1, 1, 5);
        scene.scaleSceneView(0.75f);
        scene.showBasePlate();
        scene.idle(20);

        Selection innerCogs = util.select().fromTo(2, 1, 2, 4, 1, 4);
        Selection colossalCog = util.select().fromTo(1, 1, 1, 5, 1, 5).substract(innerCogs);
        BlockPos largeCog = util.grid().at(2, 1, 6);
        Selection firstLargeCog = util.select().position(largeCog);
        Selection otherLargeCogs = util.select().fromTo(0, 1, 0, 6, 1, 6).substract(colossalCog).substract(innerCogs).substract(firstLargeCog);
        Selection keepingLargeCogs = util.select().position(4, 1, 0).add(util.select().position(6, 1, 2));
        //BlockPos controller = util.grid().at(3, 1, 5);
        //BlockPos center = util.grid().at(3, 1, 3);
        BlockPos smallCog = util.grid().at(1, 1, 1);
        BlockPos innerCog = util.grid().at(2, 1, 3);

        scene.overlay().showText(40)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(60);
        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(80);
        scene.world().showSection(util.select().fromTo(0, 0, 0, 6, 0, 6).substract(util.select().fromTo(1, 0, 1, 5, 0, 5)), Direction.UP);
        scene.idle(20);
        scene.world().showSection(util.select().position(2, 0, 7), Direction.NORTH);
        scene.idle(20);
        scene.world().showSection(firstLargeCog, Direction.DOWN);
        scene.idle(54);

        ElementLink<WorldSectionElement> cogwheel = scene.world().showIndependentSection(colossalCog, Direction.DOWN);
        scene.world().moveSection(cogwheel, util.vector().of(0d, 10, 0d), 0);
        scene.idle(1);
        scene.addKeyframe();
        scene.world().moveSection(cogwheel, util.vector().of(0d, -10d, 0d), 5);
        scene.idle(5);
        scene.effects().emitParticles(util.vector().of(1d, 1d, 1d), PetrolparkEmitters.inAABB(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, new AABB(0d, 0.1d, 0d, 5d, 0.2d, 5d), util.vector().of(0d, 0.1d, 0d)), 20f, 1);
        scene.addInstruction(new CameraShakeInstruction());
        scene.idle(40);

        scene.overlay().showText(160)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().topOf(1, 1, 3));
        scene.idle(40);
        //scene.effects().rotationDirectionIndicator(controller, center.above());
        scene.effects().rotationDirectionIndicator(largeCog);
        scene.idle(80);
        scene.world().showSection(otherLargeCogs, Direction.DOWN);
        scene.idle(60);

        scene.world().hideSection(otherLargeCogs.copy().substract(keepingLargeCogs), Direction.UP);
        scene.idle(20);
        ElementLink<WorldSectionElement> smallCogs = scene.world().showIndependentSection(util.select().position(1, 2, 1).add(util.select().position(5, 2, 5)), Direction.DOWN);
        scene.world().moveSection(smallCogs, util.vector().of(0d, -1d, 0d), 0);
        scene.idle(20);
        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().topOf(smallCog));
        scene.idle(40);
        //scene.effects().rotationDirectionIndicator(controller.above(), center);
        scene.effects().rotationDirectionIndicator(smallCog);
        scene.idle(80);

        scene.world().showSection(innerCogs, Direction.DOWN);
        scene.idle(20);
        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().topOf(innerCog));
        scene.idle(40);
        scene.effects().rotationDirectionIndicator(innerCog);
        scene.idle(80);


        scene.markAsFinished();
    };

    public static void differential(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("differential", "This text is defined in a language file.");
        scene.configureBasePlate(1, 0, 5);
        scene.showBasePlate();
        
        BlockPos westBigGear = util.grid().at(0, 0, 3);
        BlockPos eastBigGear = util.grid().at(6, 0, 1);
        BlockPos westBottomSmallGear = util.grid().at(0, 1, 2);
        BlockPos westTopSmallGear = util.grid().at(0, 2, 2);
        BlockPos eastBottomSmallGear = util.grid().at(6, 1, 2);
        BlockPos eastTopSmallGear = util.grid().at(6, 2, 2);
        BlockPos westOuterShaft = util.grid().at(1, 2, 2);
        BlockPos westInnerShaft = util.grid().at(2, 2, 2);
        BlockPos eastOuterShaft = util.grid().at(5, 2, 2);
        BlockPos eastInnerShaft = util.grid().at(4, 2, 2);
        BlockPos differential = util.grid().at(3, 2, 2);
        BlockPos westSpeedometer = util.grid().at(1, 1, 2);
        BlockPos eastSpeedometer = util.grid().at(5, 1, 2);
        BlockPos middleSmallGear = util.grid().at(3, 3, 3);
        BlockPos middleSpeedometer = util.grid().at(2, 3, 3);

        Selection west = util.select().position(westBottomSmallGear)
            .add(util.select().position(westTopSmallGear))
            .add(util.select().position(westOuterShaft))
            .add(util.select().position(westInnerShaft))
            .add(util.select().position(westSpeedometer));

        Selection east = util.select().position(eastBigGear)
            .add(util.select().position(eastBottomSmallGear))
            .add(util.select().position(eastTopSmallGear))
            .add(util.select().position(eastOuterShaft))
            .add(util.select().position(eastInnerShaft))
            .add(util.select().position(eastSpeedometer));

        Selection center = util.select().position(differential)
            .add(util.select().position(middleSmallGear))
            .add(util.select().position(middleSpeedometer));

        Selection back = util.select().fromTo(3, 0, 5, 4, 2, 5);

        scene.idle(10);
        ElementLink<WorldSectionElement> bigGearElement = scene.world().showIndependentSection(util.select().position(westBigGear), Direction.EAST);
        scene.world().showSection(util.select().position(eastBigGear), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(westBottomSmallGear), Direction.EAST);
        scene.world().showSection(util.select().position(eastBottomSmallGear), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(westTopSmallGear), Direction.EAST);
        scene.world().showSection(util.select().position(eastTopSmallGear), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(westOuterShaft), Direction.DOWN);
        scene.world().showSection(util.select().position(eastOuterShaft), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(westInnerShaft), Direction.DOWN);
        scene.world().showSection(util.select().position(eastInnerShaft), Direction.DOWN);
        scene.idle(5);
        ElementLink<WorldSectionElement> differentialElement = scene.world().showIndependentSection(util.select().position(differential), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().topOf(differential))
            .attachKeyFrame();
        scene.idle(20);
        scene.effects().rotationSpeedIndicator(westOuterShaft);
        scene.effects().rotationSpeedIndicator(eastOuterShaft);
        scene.idle(20);
        scene.effects().rotationSpeedIndicator(differential);
        scene.idle(60);

        scene.world().showSection(util.select().position(eastSpeedometer), Direction.EAST);
        scene.idle(5);
        scene.world().showSection(util.select().position(westSpeedometer), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(middleSmallGear), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(middleSpeedometer), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(120)
            .text("This text is defined in a language file.")
            .attachKeyFrame();
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.BLUE, "east", util.select().position(eastSpeedometer), 100);
        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.BLUE)
            .independent(40);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, "west", util.select().position(westSpeedometer), 80);
        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.RED)
            .independent(60);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.FAST, "total", util.select().position(middleSpeedometer), 60);
        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.FAST)
            .independent(80);
        scene.idle(80);

        scene.overlay().showText(170)
            .text("This text is defined in a language file.")
            .attachKeyFrame();
        scene.idle(10);
        scene.world().multiplyKineticSpeed(center, 8 / 14f);
        scene.world().multiplyKineticSpeed(west, 1 / 1000f);
        scene.world().moveSection(bigGearElement, util.vector().of(-1d, 0d, 0d), 10);
        scene.idle(15);
        scene.world().rotateSection(bigGearElement, 0d, 0d, 180d, 10);
        scene.idle(15);
        scene.world().moveSection(bigGearElement, util.vector().of(1d, 0d, 0d), 10);
        scene.idle(10);
        scene.world().rotateSection(bigGearElement, 0d, 0d, 180d, 0);
        scene.world().setKineticSpeed(util.select().position(westBigGear), -3f);
        scene.world().multiplyKineticSpeed(center, 2 / 8f);
        scene.world().multiplyKineticSpeed(west, -1000f);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.BLUE, "east", util.select().position(eastSpeedometer), 100);
        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.BLUE)
            .independent(40);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, "west", util.select().position(westSpeedometer), 80);
        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.RED)
            .independent(60);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.FAST, "total", util.select().position(middleSpeedometer), 60);
        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.FAST)
            .independent(80);
        scene.idle(70);
        scene.world().hideSection(util.select().position(middleSmallGear).add(util.select().position(middleSpeedometer)), Direction.SOUTH);
        scene.idle(10);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.RED)
            .attachKeyFrame();
        scene.idle(100);

        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.GREEN)
            .pointAt(util.vector().blockSurface(differential, Direction.WEST));
        scene.idle(80);
        scene.world().setKineticSpeed(util.select().position(differential), 0f);
        scene.world().hideSection(east, Direction.EAST);
        scene.world().hideSection(west, Direction.WEST);
        scene.world().hideIndependentSection(bigGearElement, Direction.WEST);
        scene.idle(10);
        scene.world().setKineticSpeed(east, 0f);
        scene.world().setKineticSpeed(west, 0f);
        scene.idle(10);
        scene.world().rotateSection(differentialElement, 0d, 90d, 0d, 10);
        scene.idle(10);
        scene.overlay().showText(60)
            .text("This text is defined in a language file.")
            .colored(PonderPalette.RED)
            .pointAt(util.vector().blockSurface(differential, Direction.NORTH));
        scene.idle(80);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().blockSurface(differential, Direction.UP))
            .attachKeyFrame();
        scene.idle(20);
        scene.world().showSection(back, Direction.NORTH);
        scene.idle(5);
        for (int z = 5; z >= 2; z--) {
            scene.world().showSection(util.select().position(util.grid().at(4, 3, z)), Direction.DOWN);
            scene.idle(5);
        };
        scene.world().destroyBlock(differential);
        scene.idle(60);

        scene.markAsFinished();
    };

    public static void cornerShaft(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("corner_shaft", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos dcs = util.grid().at(2, 1, 2);

        scene.world().showSection(util.select().position(1, 0, 5), Direction.NORTH);
        int[][] shafts = new int[][]{new int[]{2, 5}, new int[]{2, 4}, new int[]{2, 3}, new int[]{2, 2}, new int[]{3, 2}, new int[]{4, 2}};
        for (int[] shaft : shafts) {
            scene.idle(5);
            scene.world().showSection(util.select().position(shaft[0], 1, shaft[1]), Direction.DOWN);
        };

        scene.idle(10);
        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().centerOf(dcs))
            .attachKeyFrame();
        scene.idle(120);

        Selection secondShaft = util.select().fromTo(0, 1, 2, 1, 1, 2);
        scene.world().showSection(secondShaft, Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame();
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(dcs, Direction.NORTH), Pointing.RIGHT, 20)
            .withItem(AllItems.WRENCH.asStack());
        scene.idle(5);
        scene.world().setBlock(dcs, PetrolsPartsBlocks.CORNER_SHAFT.get().getBlockstateConnectingDirections(Direction.SOUTH, Direction.UP), false);
        scene.world().setKineticSpeed(util.select().fromTo(3, 1, 2, 4, 1, 2), 0);
        scene.idle(25);
        scene.overlay().showControls(util.vector().blockSurface(dcs, Direction.NORTH), Pointing.RIGHT, 20)
            .withItem(AllItems.WRENCH.asStack());
        scene.idle(5);
        scene.world().setBlock(dcs, PetrolsPartsBlocks.CORNER_SHAFT.get().getBlockstateConnectingDirections(Direction.SOUTH, Direction.WEST), false);
        scene.world().setKineticSpeed(secondShaft, 16);
        scene.idle(15);

        scene.markAsFinished();
    };  

    public static void hydraulicTransmission(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("hydraulic_transmission", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        Selection gauge = util.select().position(1, 4, 4);
        scene.world().setKineticSpeed(gauge, 0f);

        scene.idle(10);
        scene.world().showSection(util.select().position(5, 0, 2), Direction.WEST);
        scene.idle(10);
        scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(1, 1, 4, 1, 4, 4), Direction.DOWN);
        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(100);

        scene.world().showSection(util.select().fromTo(1, 1, 1, 3, 4, 3), Direction.EAST);
        scene.idle(10);
        scene.world().setKineticSpeed(gauge, 16f);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().blockSurface(util.grid().at(1, 4, 3), Direction.EAST))
            .attachKeyFrame();
        scene.idle(100);

        scene.markAsFinished();
    };
    
    public static void planetaryGearset(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("planetary_gearset", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 3);
        scene.showBasePlate();

        scene.world().showSection(util.select().position(1, 0, 3), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(1, 2, 0, 1, 2, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 3, 1), Direction.DOWN);
        scene.idle(5);

        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(util.grid().at(1, 2, 1), Direction.WEST));
        scene.idle(120);

        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(util.grid().at(1, 2, 0), Direction.NORTH));
        scene.idle(20);

        scene.effects().rotationDirectionIndicator(util.grid().at(1, 2, 1));
		scene.effects().rotationDirectionIndicator(util.grid().at(1, 2, 0));
        scene.idle(100);

        scene.markAsFinished();
    };

    public static void pneumaticTube(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("pneumatic_tube", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(0.75f);
        scene.showBasePlate();

        ItemStack ingots = new ItemStack(Items.IRON_INGOT, 64);

        Selection depotTower = util.select().fromTo(3, 1, 4, 3, 4, 4);
        BlockPos bottomDepot = util.grid().at(0, 1, 1);
        BlockPos topDepot = util.grid().at(3, 4, 4);
        Selection shaft = util.select().fromTo(1, 1, 2, 5, 1, 2);
        Selection tube = util.select().fromTo(1, 1, 1, 3, 4, 2).substract(shaft);
        BlockPos bottomEnd = util.grid().at(1, 1, 1);
        BlockPos topEnd = util.grid().at(3, 4, 3);

        scene.world().setKineticSpeed(util.select().position(bottomEnd), 0f);

        scene.idle(10);
        scene.world().showSection(util.select().position(topEnd), Direction.SOUTH);
        scene.idle(10);
        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().blockSurface(topEnd, Direction.UP))
            .placeNearTarget();
        scene.idle(40);
        scene.world().showSection(tube, Direction.WEST);
        scene.idle(60);

        scene.world().showSection(util.select().position(bottomDepot), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(depotTower, Direction.DOWN);
        scene.idle(20);
        scene.world().createItemOnBeltLike(bottomDepot, Direction.WEST, ingots);
        scene.idle(10);
        scene.overlay().showControls(util.vector().topOf(bottomDepot), Pointing.DOWN, 40)
            .withItem(ingots);
        scene.idle(50);

        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(bottomEnd, Direction.UP))
            .placeNearTarget();
        scene.idle(20);
        scene.world().showSection(util.select().position(5, 0, 3), Direction.WEST);
        scene.idle(10);
        scene.world().showSection(shaft, Direction.EAST);
        scene.idle(10);
        scene.world().setKineticSpeed(util.select().position(bottomEnd), -32f);
        scene.world().removeItemsFromBelt(bottomDepot);
        scene.addInstruction(new PneumaticTubeTransportInstruction(bottomEnd, true));
        scene.world().createItemOnBeltLike(topDepot, Direction.NORTH, ingots);
        scene.idle(10);
        scene.overlay().showControls(util.vector().topOf(topDepot), Pointing.DOWN, 40)
            .withItem(ingots);
        scene.idle(50);
        scene.world().removeItemsFromBelt(topDepot);
        scene.idle(20);

        ItemStack goldIngots = new ItemStack(Items.GOLD_INGOT, 64);

        Vec3 filter = util.vector().blockSurface(bottomEnd, Direction.UP).add(-5 / 16d, 0d, 0d);
        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showFilterSlotInput(filter, Direction.UP, 60);
        scene.overlay().showControls(filter, Pointing.DOWN, 40)
            .rightClick()
			.withItem(goldIngots);
        scene.idle(10);
        scene.world().setFilterData(util.select().position(bottomEnd), PneumaticTubeBlockEntity.class, goldIngots);
        scene.idle(30);
        scene.overlay().showText(120)
            .text("This text is defined in a language file.")
            .pointAt(util.vector().blockSurface(bottomEnd, Direction.NORTH));
        scene.idle(30);
        scene.world().createItemOnBeltLike(bottomDepot, Direction.WEST, ingots);
        scene.idle(10);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.RED, filter, new AABB(bottomDepot).deflate(3 / 16d).move(0d, 4 / 16d, 0d), 40);
        scene.idle(50);
        scene.world().removeItemsFromBelt(bottomDepot);
        scene.idle(20);
        scene.world().createItemOnBeltLike(bottomDepot, Direction.WEST, goldIngots);
        scene.idle(10);
        scene.world().removeItemsFromBelt(bottomDepot);
        scene.addInstruction(new PneumaticTubeTransportInstruction(bottomEnd, true));
        scene.world().createItemOnBeltLike(topDepot, Direction.NORTH, goldIngots);
        scene.idle(50);

        scene.world().hideSection(depotTower, Direction.UP);
        scene.idle(10);
        scene.overlay().showText(100)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(topEnd, Direction.WEST))
            .placeNearTarget();
        scene.idle(40);
        scene.world().createItemOnBeltLike(bottomDepot, Direction.WEST, goldIngots);
        scene.idle(10);
        scene.world().removeItemsFromBelt(bottomDepot);
        scene.addInstruction(new PneumaticTubeTransportInstruction(bottomEnd, true));
        scene.world().createItemEntity(util.vector().blockSurface(topDepot, Direction.SOUTH), util.vector().of(0d, 0d, 1d), goldIngots);
        scene.idle(50);

        scene.overlay().showText(80)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(bottomEnd, Direction.WEST))
            .placeNearTarget();
        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(bottomEnd), Pointing.DOWN, 40)
            .withItem(AllItems.WRENCH.asStack());
        scene.idle(10);
        scene.world().modifyBlockEntity(bottomEnd, PneumaticTubeBlockEntity.class, be -> be.flip(null));
        scene.idle(70);
    };

};
