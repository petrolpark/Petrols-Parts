package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction;
import net.createmod.ponder.foundation.instruction.FadeOutOfSceneInstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.library.util.PonderHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.FourBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.ThreeBevelCogWheelsBlock;

public class BevelCogWheelScenes {
    
    public static final void orthogonal(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        final BlockPos middle = util.grid().at(2, 1, 2);

        scene.idle(5);
        scene.world().showSection(util.select().position(1, 0, 5), Direction.NORTH);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 1, 5), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 1, 4), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 1, 3), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(middle), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(3, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(2, 1, 1), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(0, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(2, 1, 0), Direction.DOWN);
        scene.idle(20);

        scene.world().setBlock(middle, PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS.getDefaultState().setValue(FourBevelCogWheelsBlock.EXCLUDED_AXIS, Axis.Y), true);
        scene.world().setKineticSpeed(util.select().position(middle), 32f);
        scene.idle(20);

        scene.overlay().showText(60)
            .attachKeyFrame()
            .pointAt(util.vector().topOf(middle))
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.world().setBlock(middle, PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState().setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, Direction.NORTH), true);
        scene.world().multiplyKineticSpeed(util.select().position(middle), -1f);
        final Selection northShaft = util.select().fromTo(2, 1, 0, 2, 1, 1);
        scene.world().setKineticSpeed(northShaft, 0f);
        scene.idle(20);

        scene.overlay().showText(60)
            .attachKeyFrame()
            .pointAt(util.vector().topOf(middle))
            .text("This text is defined in a language file");
        scene.idle(30);

        scene.world().setBlock(middle, PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS.getDefaultState().setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.WEST_SOUTH), true);
        final Selection eastShaft = util.select().fromTo(3, 1, 2, 4, 1, 2);
        scene.world().setKineticSpeed(eastShaft, 0f);
        scene.idle(50);

        scene.overlay().showControls(util.vector().topOf(middle), Pointing.DOWN, 30)
            .rightClick()
            .withItem(AllBlocks.SHAFT.asStack());
        scene.idle(20);
        scene.world().cycleBlockProperty(middle, CornerBevelCogWheelsBlock.SHAFT);
        scene.world().setKineticSpeed(eastShaft, -32f);
        scene.idle(20);

        scene.overlay().showText(60)
            .attachKeyFrame()
            .pointAt(util.vector().topOf(middle))
            .text("This text is defined in a language file");
        scene.idle(20);
        scene.effects().rotationDirectionIndicator(util.grid().at(3, 1, 2));
        scene.idle(60);

        scene.world().destroyBlock(util.grid().at(4, 1, 2));
        scene.idle(5);
        scene.world().destroyBlock(util.grid().at(3, 1, 2));
        scene.idle(5);
        scene.world().setBlock(middle, PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS.getDefaultState().setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.WEST_SOUTH), true);
        scene.idle(5);
        scene.world().destroyBlock(util.grid().at(2, 1, 1));
        scene.idle(5);
        scene.world().destroyBlock(util.grid().at(2, 1, 0));
        scene.idle(20);

        scene.world().showSection(util.select().position(5, 0, 1), Direction.WEST);
        scene.idle(5);
        final ElementLink<WorldSectionElement> cog = scene.world().showIndependentSection(util.select().position(6, 1, 2), Direction.WEST);
        scene.world().moveSection(cog, util.vector().of(-1d, 0d, 0d), 0);
        scene.idle(5);
        for (int i = 5; i >= 2; i--) {
            scene.world().showSection(util.select().position(i, 2, 2), Direction.DOWN);
            scene.idle(5);
        };
        scene.idle(5);
        final ElementLink<WorldSectionElement> shaft = scene.world().showIndependentSection(util.select().position(2, 3, 2), Direction.SOUTH);
        scene.world().moveSection(shaft, util.vector().of(0d, -2d, 0d), 0);
        scene.idle(10);

        scene.overlay().showText(60)
            .attachKeyFrame()
            .pointAt(util.vector().topOf(middle))
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.markAsFinished();
    };

    public static final void diagonal(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel.diagonal", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.idle(5);
        scene.world().showSection(util.select().position(0, 0, 5), Direction.NORTH);
        for (int z = 5; z >= 2; z--) {
            scene.idle(5);
            scene.world().showSection(util.select().position(1, 1, z), Direction.DOWN);
        };
        scene.idle(15);
        final ElementLink<WorldSectionElement> westNorth = scene.world().showIndependentSection(util.select().position(2, 3, 2), Direction.DOWN);
        scene.world().moveSection(westNorth, util.vector().of(0d, -2d, 0d), 0);
        scene.idle(15);

        final Vec3 side = util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.WEST);

        scene.overlay().showText(60)
            .attachKeyFrame()
            .pointAt(side)
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.world().showSection(util.select().position(2, 1, 1), Direction.DOWN);
        scene.idle(15);
        scene.overlay().showText(60)
            .pointAt(side)
            .text("This text is defined in a language file");
        scene.idle(20);
        scene.effects().rotationSpeedIndicator(util.grid().at(1, 1, 2));
        scene.effects().rotationSpeedIndicator(util.grid().at(2, 1, 1));
        scene.idle(60);

        scene.rotateCameraY(180);
        scene.idle(30);

        scene.world().showSection(util.select().position(3, 1, 1), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().position(3, 1, 2), Direction.DOWN);
        scene.idle(10);
        final ElementLink<WorldSectionElement> eastSouth = scene.world().showIndependentSection(util.select().position(2, 5, 2), Direction.DOWN);
        scene.world().moveSection(eastSouth, util.vector().of(0d, -4d, 0d), 0);
        scene.idle(10);
        scene.world().showSection(util.select().position(2, 1, 3), Direction.DOWN);
        scene.idle(20);

        scene.overlay().showOutlineWithText(util.select().position(2, 1, 2), 80)
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(70);

        scene.rotateCameraY(-180);
        scene.idle(30);

        final ElementLink<WorldSectionElement> eastDown = scene.world().showIndependentSection(util.select().position(2, 4, 1), Direction.DOWN);
        scene.world().moveSection(eastDown, util.vector().of(0d, -2d, 0d), 0);
        scene.idle(20);

        scene.overlay().showControls(util.vector().centerOf(util.grid().at(2, 2, 1)), Pointing.DOWN, 40)
            .rightClick()
            .withItem(PetrolsPartsItems.SHAFT_HALF.asStack());
        scene.idle(30);

        scene.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.DOWN, eastDown));
        scene.addInstruction(new DisplayWorldSectionInstruction(0, Direction.DOWN, util.select().position(2, 2, 1), scene.getScene()::getBaseWorldSection));
        scene.idle(20);

        scene.overlay().showText(80)
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 1), Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(60);
        scene.world().showSection(util.select().position(1, 2, 1), Direction.EAST);
        scene.idle(40);

        scene.overlay().showText(60)
            .pointAt(util.vector().blockSurface(util.grid().at(1, 2, 1), Direction.WEST))
            .text("This text is defined in a language file");
        scene.idle(20);
        scene.effects().rotationSpeedIndicator(util.grid().at(1, 2, 1));
        scene.effects().rotationSpeedIndicator(util.grid().at(2, 1, 1));
        scene.idle(60);

        scene.markAsFinished();
    };

    public static final void encasing(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel.encasing", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);

        scene.world().showSection(util.select().layer(0), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().layer(1), Direction.DOWN);
        scene.idle(20);

        scene.overlay().showControls(util.vector().topOf(2, 1, 2), Pointing.DOWN, 20)
            .rightClick()
            .withItem(AllBlocks.ANDESITE_CASING.asStack());
        scene.idle(7);
        PonderHelper.swapBlockState(scene, util.grid().at(2, 1, 2), PetrolsPartsBlocks.ANDESITE_ENCASED_SINGLE_DIAGONAL_BEVEL_COGWHEEL.getDefaultState(), true);
        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(1, 1, 3), Pointing.DOWN, 20)
            .rightClick()
            .withItem(AllBlocks.BRASS_CASING.asStack());
        scene.idle(7);
        PonderHelper.swapBlockState(scene, util.grid().at(1, 1, 3), PetrolsPartsBlocks.BRASS_ENCASED_SINGLE_DIAGONAL_BEVEL_COGWHEEL.getDefaultState(), true);
        scene.idle(20);

        scene.overlay().showText(80)
            .attachKeyFrame()
            .pointAt(util.vector().centerOf(1, 1, 3))
            .text("This text is defined in a language file");
        scene.idle(40);

        PonderHelper.swapBlockState(scene, util.grid().at(1, 1, 1), PetrolsPartsBlocks.BRASS_ENCASED_SINGLE_DIAGONAL_BEVEL_COGWHEEL.getDefaultState(), true);
        scene.idle(20);
        PonderHelper.swapBlockState(scene, util.grid().at(0, 1, 2), PetrolsPartsBlocks.ANDESITE_ENCASED_DUAL_DIAGONAL_BEVEL_COGWHEEL.getDefaultState(), true);
        scene.idle(20);

        scene.rotateCameraY(180);
        scene.idle(20);
    
        scene.overlay().showOutlineWithText(util.select().position(3, 1, 3), 60)
            .colored(PonderPalette.RED)
            .attachKeyFrame()
            .text("This text is defined in a language file");
        scene.idle(80);
        scene.markAsFinished();
    };
};
