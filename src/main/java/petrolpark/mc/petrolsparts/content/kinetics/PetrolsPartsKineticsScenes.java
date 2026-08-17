package petrolpark.mc.petrolsparts.content.kinetics;

import java.util.function.UnaryOperator;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.content.redstone.analogLever.AnalogLeverBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.instruction.FadeOutOfSceneInstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.core.client.ponder.instruction.CameraShakeInstruction;
import petrolpark.mc.library.core.client.ponder.particle.PetrolparkEmitters;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementBlockEntity;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlockEntity;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeTransportInstruction;

public class PetrolsPartsKineticsScenes {

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
        scene.title("differential", "This text is defined in a language file");
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

        // Selection east = util.select().position(eastBigGear)
        //     .add(util.select().position(eastBottomSmallGear))
        //     .add(util.select().position(eastTopSmallGear))
        //     .add(util.select().position(eastOuterShaft))
        //     .add(util.select().position(eastInnerShaft))
        //     .add(util.select().position(eastSpeedometer));

        Selection center = util.select().position(differential)
            .add(util.select().position(middleSmallGear))
            .add(util.select().position(middleSpeedometer));

        // Selection back = util.select().fromTo(3, 0, 5, 4, 2, 5);

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
        //ElementLink<WorldSectionElement> differentialElement =
            scene.world().showIndependentSection(util.select().position(differential), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(80)
            .text("This text is defined in a language file")
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
            .text("This text is defined in a language file")
            .attachKeyFrame();
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.BLUE, "east", util.select().position(eastSpeedometer), 100);
        scene.overlay().showText(100)
            .text("This text is defined in a language file")
            .colored(PonderPalette.BLUE)
            .independent(40);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, "west", util.select().position(westSpeedometer), 80);
        scene.overlay().showText(80)
            .text("This text is defined in a language file")
            .colored(PonderPalette.RED)
            .independent(60);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.FAST, "total", util.select().position(middleSpeedometer), 60);
        scene.overlay().showText(60)
            .text("This text is defined in a language file")
            .colored(PonderPalette.FAST)
            .independent(80);
        scene.idle(80);

        scene.overlay().showText(170)
            .text("This text is defined in a language file")
            .attachKeyFrame();
        scene.idle(10);
        scene.world().multiplyKineticSpeed(center, 8 / 22f);
        scene.world().multiplyKineticSpeed(west, 1 / 1000f);
        multiplyCompositeKBESpeed(scene, center, 1,  1 / 1000f);
        multiplyCompositeKBESpeed(scene, center, 2, 8 / 22f);
        scene.world().moveSection(bigGearElement, util.vector().of(-1d, 0d, 0d), 10);
        scene.idle(15);
        scene.world().rotateSection(bigGearElement, 0d, 0d, 180d, 10);
        scene.idle(15);
        scene.world().moveSection(bigGearElement, util.vector().of(1d, 0d, 0d), 10);
        scene.idle(10);
        scene.world().rotateSection(bigGearElement, 0d, 0d, 180d, 0);
        scene.world().setKineticSpeed(util.select().position(westBigGear), -3f);
        scene.world().multiplyKineticSpeed(center, 10 / 8f);
        scene.world().multiplyKineticSpeed(west, -1000f);
        multiplyCompositeKBESpeed(scene, center, 1,  -1000f);
        multiplyCompositeKBESpeed(scene, center, 2, 10 / 8f);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.BLUE, "east", util.select().position(eastSpeedometer), 100);
        scene.overlay().showText(100)
            .text("This text is defined in a language file")
            .colored(PonderPalette.BLUE)
            .independent(40);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, "west", util.select().position(westSpeedometer), 80);
        scene.overlay().showText(80)
            .text("This text is defined in a language file")
            .colored(PonderPalette.RED)
            .independent(60);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.FAST, "total", util.select().position(middleSpeedometer), 60);
        scene.overlay().showText(60)
            .text("This text is defined in a language file")
            .colored(PonderPalette.FAST)
            .independent(80);
        scene.idle(80);

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

    public static final void movement(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("movement", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        final BlockPos movement = util.grid().at(2, 2, 2);

        scene.idle(5);
        final ElementLink<WorldSectionElement> movementLink = scene.world().showIndependentSection(util.select().position(movement), Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(100)
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(movement, Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(30);
        scene.overlay().showControls(util.vector().topOf(movement), Pointing.DOWN, 70)
            .rightClick()
            .withItem(new ItemStack(Items.IRON_BLOCK));
        scene.idle(10);
        scene.world().modifyBlockEntity(movement, MovementBlockEntity.class, be -> be.setWeightStack(new ItemStack(Items.IRON_BLOCK)));
        scene.idle(20);
        scene.overlay().showText(40)
            .independent(120)
            .text("This text is defined in a language file");
        scene.idle(50);

        scene.idle(10);
        scene.world().showSection(util.select().position(3, 0, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 5), Direction.NORTH);
        for (int z = 5; z >= 3; z--) {
            scene.idle(5);
            scene.world().showSection(util.select().position(2, 2, z), Direction.DOWN);
        };
        scene.overlay().showText(100)
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(movement, Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(40);
        scene.overlay().showText(60)
            .independent(115)
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.world().showSection(util.select().fromTo(2, 1, 0, 2, 2, 1), Direction.SOUTH);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(0, 1, 2, 1, 2, 2), Direction.EAST);
        scene.idle(10);
        scene.overlay().showText(120)
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(movement, Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(20);
        scene.world().toggleRedstonePower(util.select().fromTo(0, 2, 2, 1, 2, 2));
        scene.effects().indicateRedstone(util.grid().at(0, 2, 2));
        scene.world().setKineticSpeed(util.select().fromTo(2, 2, 0, 2, 2, 1), 16f);
        scene.world().modifyBlockEntity(movement, MovementBlockEntity.class, be -> be.generatingPart.setSpeed(16f));
        scene.idle(40);
        scene.overlay().showText(60)
            .independent(120)
            .text("This text is defined in a language file");
        scene.idle(80);

        final BlockPos clock = util.grid().at(2, 3, 2);

        scene.world().showSection(util.select().position(clock), Direction.DOWN);
        scene.idle(20);
        scene.overlay().showText(80)
            .pointAt(util.vector().blockSurface(clock, Direction.SOUTH))
            .attachKeyFrame()
            .text("This text is defined in a language file");
        scene.idle(30);
        scene.rotateCameraY(-90);
        scene.idle(50);
        scene.rotateCameraY(90);
        scene.idle(60);


        scene.overlay().showText(80)
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(movement, Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(10);
        scene.overlay().showControls(util.vector().topOf(movement), Pointing.DOWN, 20)
            .rightClick()
            .withItem(AllItems.WRENCH.asStack());
        scene.idle(10);
        scene.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.EAST, movementLink));
        scene.world().setKineticSpeed(util.select().fromTo(2, 2, 0, 2, 2, 1), 0f);
        scene.idle(60);
        scene.world().showIndependentSectionImmediately(util.select().position(movement));
        scene.world().setKineticSpeed(util.select().fromTo(2, 2, 0, 2, 2, 1), 16f);
        scene.idle(40);

        scene.overlay().showText(90)
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(movement, Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(10);
        scene.overlay().showControls(util.vector().topOf(movement), Pointing.DOWN, 50)
            .rightClick();
        scene.idle(10);
        scene.world().modifyBlockEntity(movement, MovementBlockEntity.class, be -> {
            be.setWeightStack(ItemStack.EMPTY);
            be.generatingPart.setSpeed(0f);
        });
        scene.world().setKineticSpeed(util.select().fromTo(2, 2, 0, 2, 2, 1), 0f);
    };

    public static final void movementBattery(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("movement_battery", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.DOWN);
        scene.idle(10);

        for (int y = 1; y <= 3; y++) {
            scene.world().showSection(util.select().layer(y), Direction.DOWN);
            scene.idle(15);
            scene.addKeyframe();
            scene.idle(15);
        };

        scene.rotateCameraY(-90);
        scene.idle(20);
        scene.addKeyframe();
        scene.idle(20);
        scene.rotateCameraY(90);
        scene.idle(20);

        scene.overlay().showText(80)
            .independent(40)
            .text("This text is defined in a language file");
    };
    
    public static final void overloadClutch(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("overload_clutch", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        final Selection clutch = util.select().position(2, 2, 3);
        final Selection gauge = util.select().position(2, 2, 1);
        final Vec3 clutchSide = util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.WEST);

        scene.idle(5);
        scene.world().showSection(util.select().position(1, 0, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 2, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 2, 4), Direction.WEST);
        scene.idle(10);
        scene.world().showSection(clutch, Direction.SOUTH);
        scene.idle(10);
        scene.world().showSection(util.select().position(2, 2, 2), Direction.SOUTH);
        scene.idle(10);
        scene.world().showSection(gauge, Direction.SOUTH);
        setStress(scene, gauge, 0f);
        scene.idle(10);

        scene.overlay().showText(60)
            .pointAt(clutchSide)
            .attachKeyFrame()
            .placeNearTarget()
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.world().showSection(util.select().position(2, 2, 0), Direction.SOUTH);
        scene.idle(15);
        final Selection subnet = util.select().fromTo(2, 2, 0, 2, 2, 2);
        scene.world().multiplyKineticSpeed(subnet, 1 / 256f / 256f);
        multiplyCompositeKBESpeed(scene, clutch, 0, 1 / 256f / 256f);
        setStress(scene, gauge, 1.125f);
        scene.effects().emitParticles(Vec3.ZERO, PetrolparkEmitters.inAABB(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, new AABB(1.75d, 3.5d, -0.25d, 3.25d, 3.6d, 3.25d), util.vector().of(0d, 0.1d, 0d)), 10f, 1);
        scene.idle(20);

        scene.overlay().showOutline(PonderPalette.RED, "overdstressed", subnet, 70);
        scene.overlay().showOutlineWithText(util.select().fromTo(1, 0, 5, 2, 2, 5).add(util.select().position(2, 2, 4)), 70)
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(90);
        
        scene.rotateCameraY(-45f);
        scene.idle(20);
        scene.overlay().showFilterSlotInput(util.vector().of(1.9d, 2.5d, 3.5d), Direction.WEST, 80);
        scene.overlay().showText(80)
            .attachKeyFrame()
            .pointAt(clutchSide)
            .text("This text is defined in a language file");
        scene.idle(40);
        scene.world().multiplyKineticSpeed(subnet, 256f * 256f);
        multiplyCompositeKBESpeed(scene, clutch, 0, 256f * 256f);
        setStress(scene, gauge, 0.3f);
        scene.effects().emitParticles(Vec3.ZERO, PetrolparkEmitters.inAABB(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, new AABB(1.75d, 3.5d, -0.25d, 3.25d, 3.6d, 3.25d), util.vector().of(0d, 0.1d, 0d)), 10f, 1);
        scene.idle(60);

        final Selection redstone = util.select().fromTo(0, 1, 3, 1, 2, 3);
        scene.world().showSection(redstone, Direction.EAST);
        scene.idle(10);

        scene.overlay().showText(180)
            .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.UP))
            .text("This text was defined in a language file");
        
        final BlockPos dust = util.grid().at(1, 2, 3);
        for (int i = 2; i < 14; i += 2) {
            scene.idle(20);
            scene.world().cycleBlockProperty(dust, RedStoneWireBlock.POWER);
            scene.world().cycleBlockProperty(dust, RedStoneWireBlock.POWER);
            scene.effects().indicateRedstone(dust);
            final int power = i;
            scene.world().modifyBlockEntityNBT(util.select().position(0, 2, 3), AnalogLeverBlockEntity.class, nbt -> nbt.putInt("State", power));
            setStress(scene, gauge, 0.3f + power / 20f);
        };

        scene.overlay().showText(60)
            .independent(100)
            .text("This text is defined in a lamguage file")
            .attachKeyFrame();
        scene.idle(20);
        scene.world().modifyBlockEntityNBT(util.select().position(0, 2, 3), AnalogLeverBlockEntity.class, nbt -> nbt.putInt("State", 15));
        scene.world().multiplyKineticSpeed(subnet, 1 / 256f / 256f);
        multiplyCompositeKBESpeed(scene, clutch, 0, 1 / 256f / 256f);
        setStress(scene, gauge, 1.125f);
        scene.effects().emitParticles(Vec3.ZERO, PetrolparkEmitters.inAABB(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, new AABB(1.75d, 3.5d, -0.25d, 3.25d, 3.6d, 3.25d), util.vector().of(0d, 0.1d, 0d)), 10f, 1);
        scene.idle(60);

        scene.world().showSection(util.select().fromTo(2, 1, 2, 2, 1, 4), Direction.WEST);
        scene.idle(10);
        scene.world().multiplyKineticSpeed(subnet, 256f * 256f);
        multiplyCompositeKBESpeed(scene, clutch, 0, 256f * 256f);
        setStress(scene, gauge, 0.8f);
        scene.effects().emitParticles(Vec3.ZERO, PetrolparkEmitters.inAABB(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, new AABB(1.75d, 3.5d, -0.25d, 3.25d, 3.6d, 3.25d), util.vector().of(0d, 0.1d, 0d)), 10f, 1);
        scene.idle(15);
        scene.world().showSection(util.select().fromTo(2, 3, 2, 2, 3, 4), Direction.WEST);
        scene.idle(15);
        setStress(scene, gauge, 0.1f);

        scene.overlay().showText(80)
            .pointAt(util.vector().blockSurface(util.grid().at(2, 3, 3), Direction.WEST))
            .attachKeyFrame()
            .text("This text is defined in a language file");
        scene.idle(100);

        scene.markAsFinished();
    };

    private static final void setStress(SceneBuilder scene, Selection selection, float stress) {
        scene.world().modifyBlockEntityNBT(selection, StressGaugeBlockEntity.class, nbt -> nbt.putFloat("Value", stress));
    };

    public static final void planetaryGearset(SceneBuilder baseScene, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(baseScene);
        scene.title("planetary_gearset", "This text is defined in a language file");
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

    public static final void modifyCompositeKBESpeed(SceneBuilder scene, Selection selection, int partIndex, UnaryOperator<Float> speedFunc) {
        scene.world().modifyBlockEntityNBT(selection, CompositeKineticBlockEntity.class, tag -> {
            final CompoundTag partTag = tag.getList("Parts", Tag.TAG_COMPOUND).getCompound(partIndex);
            partTag.putFloat("Speed", speedFunc.apply(partTag.getFloat("Speed")));
        });
    };

    public static final void multiplyCompositeKBESpeed(SceneBuilder scene, Selection selection, int partIndex, float factor) {
        modifyCompositeKBESpeed(scene, selection, partIndex, s -> s * factor);
    };
};
