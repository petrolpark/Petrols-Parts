package petrolpark.mc.petrolsparts.content.processing;

import static petrolpark.mc.petrolsparts.content.kinetics.PetrolsPartsKineticsScenes.multiplyCompositeKBESpeed;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.processing.frictionHeater.FrictionHeaterBlock;

public class PetrolsPartsProcessingScenes {
    
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

    public static final void frictionHeater(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("friction_heater", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		final float lots = 256f;
		final BlockPos heater = util.grid().at(3, 1, 2);
		final Selection heaterS = util.select().position(heater);
		final Vec3 heaterSide = util.vector().blockSurface(heater, Direction.WEST);

		scene.idle(5);
		scene.world().showSection(util.select().position(5, 0, 1), Direction.WEST);
		scene.idle(5);
		scene.world().showSection(util.select().position(5, 1, 2), Direction.WEST);
		scene.idle(5);
		scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
		scene.idle(10);
		scene.world().setBlock(heater, PetrolsPartsBlocks.FRICTION_HEATER.getDefaultState(), false);
		multiplyCompositeKBESpeed(scene, heaterS, 1, 1 / lots);
		scene.world().showSection(heaterS, Direction.DOWN);
		scene.idle(10);
		scene.world().showSection(util.select().position(heater.above()), Direction.DOWN);
		scene.idle(20);

		scene.overlay().showText(50)
			.pointAt(heaterSide)
			.attachKeyFrame()
			.placeNearTarget()
			.text("This text is defined in a language file");
		scene.idle(60);
		scene.world().hideSection(util.select().position(heater.above()), Direction.UP);
		scene.idle(20);

		final Selection kineticsS = util.select().fromTo(0, 0, 3, 3, 3, 5);
		scene.world().multiplyKineticSpeed(kineticsS, 0.25f);
		multiplyCompositeKBESpeed(scene, kineticsS, 0, 0.25f);
		final Selection cogsS = util.select().fromTo(0, 0, 5, 1, 1, 5);
		final ElementLink<WorldSectionElement> cogs = scene.world().showIndependentSection(cogsS, Direction.NORTH);
		scene.world().rotateSection(cogs, 0, 0, 180, 0);
		scene.world().multiplyKineticSpeed(cogsS, -1f);
		scene.idle(5);
		scene.world().showSection(util.select().position(0, 1, 4), Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(util.select().position(0, 1, 3), Direction.DOWN);
		scene.idle(5);
		final ElementLink<WorldSectionElement> shaft = scene.world().showIndependentSection(util.select().position(1, 3, 3), Direction.DOWN);
		scene.world().moveSection(shaft, util.vector().of(0d, -2d, 0d), 0);
		scene.idle(5);
		scene.world().showSection(util.select().position(2, 1, 3), Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(util.select().position(3, 1, 3), Direction.DOWN);
		scene.idle(10);
		multiplyCompositeKBESpeed(scene, heaterS, 1, lots * 0.25f);
		scene.world().setBlock(heater, PetrolsPartsBlocks.FRICTION_HEATER.getDefaultState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.KINDLED), false);
		scene.idle(10);

		scene.overlay().showText(60)
			.pointAt(heaterSide)
			.attachKeyFrame()
			.colored(PonderPalette.RED)
			.text("This text is defined in a language file");
		scene.idle(70);

		scene.world().moveSection(cogs, util.vector().of(0d, 1d, 0d), 10);
		scene.world().multiplyKineticSpeed(kineticsS, 1 / lots);
		multiplyCompositeKBESpeed(scene, kineticsS, 0, 1 / lots);
		multiplyCompositeKBESpeed(scene, heaterS, 1, 1 / lots);
		scene.idle(10);
		scene.world().rotateSection(cogs, 0, 0, 180, 10);
		scene.world().multiplyKineticSpeed(cogsS, -1f);
		scene.idle(10);
		scene.world().moveSection(cogs, util.vector().of(0d, -1d, 0d), 10);
		scene.idle(10);
		scene.world().multiplyKineticSpeed(kineticsS, lots * 4f);
		multiplyCompositeKBESpeed(scene, kineticsS, 0, lots * 4f);
		multiplyCompositeKBESpeed(scene, heaterS, 1, lots * 4f);
		scene.world().setBlock(heater, PetrolsPartsBlocks.FRICTION_HEATER.getDefaultState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.SEETHING), false);
		scene.idle(15);

		scene.overlay().showText(80)
			.pointAt(heaterSide)
			.attachKeyFrame()
			.colored(PonderPalette.GREEN)
			.text("This text is defined in a language file");
		scene.idle(100);

		final Selection gearboxS = util.select().fromTo(1, 1, 1, 1, 1, 3);
		scene.world().hideIndependentSection(shaft, Direction.SOUTH);
		scene.idle(5);
		scene.world().showSection(gearboxS, Direction.SOUTH);
		scene.idle(40);

		scene.world().toggleRedstonePower(gearboxS);
		scene.effects().indicateRedstone(util.grid().at(1, 1, 1));
		scene.world().multiplyKineticSpeed(util.select().position(2, 1, 3), -1f);
		multiplyCompositeKBESpeed(scene, kineticsS, 0, -1f);
		multiplyCompositeKBESpeed(scene, heaterS, 1, -1f);
		scene.world().setBlock(heater, PetrolsPartsBlocks.FRICTION_HEATER.getDefaultState(), false);
		scene.idle(30);

		scene.overlay().showText(80)
			.pointAt(heaterSide)
			.attachKeyFrame()
			.colored(PonderPalette.RED)
			.text("This text is defined in a language file");
		scene.idle(100);

		scene.world().toggleRedstonePower(gearboxS);
		scene.effects().indicateRedstone(util.grid().at(1, 1, 1));
		scene.world().multiplyKineticSpeed(util.select().position(2, 1, 3), -1f);
		multiplyCompositeKBESpeed(scene, kineticsS, 0, -1f);
		multiplyCompositeKBESpeed(scene, heaterS, 1, -1f);
		scene.world().setBlock(heater, PetrolsPartsBlocks.FRICTION_HEATER.getDefaultState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.KINDLED), false);
		scene.idle(20);

		final Selection mixer = util.select().fromTo(3, 2, 2, 5, 4, 3);
		scene.world().showSection(mixer, Direction.WEST);
		scene.idle(20);
		scene.overlay().showText(120)
			.pointAt(util.vector().blockSurface(util.grid().at(3, 4, 2), Direction.NORTH))
			.attachKeyFrame()
			.text("This text is defined in a language file");
		scene.idle(60);
		scene.world().hideSection(mixer, Direction.SOUTH);
		scene.idle(20);
		final ElementLink<WorldSectionElement> boiler = scene.world().showIndependentSection(util.select().fromTo(2, 5, 1, 3, 9, 2), Direction.SOUTH);
		scene.world().moveSection(boiler, util.vector().of(0d, -3d, 0d), 0);
		scene.world().showSection(util.select().fromTo(2, 1, 1, 3, 1, 2).substract(heaterS), Direction.SOUTH);
		scene.idle(60);
	};
};
