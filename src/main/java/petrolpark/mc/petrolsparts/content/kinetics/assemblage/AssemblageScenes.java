package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction;
import net.createmod.ponder.foundation.instruction.FadeOutOfSceneInstruction;
import net.minecraft.core.Direction;
import petrolpark.mc.library.core.client.ponder.instruction.SetElementVisibilityInstruction;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;

public class AssemblageScenes {
    
    public static final void shaftlessCogwheels(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.shaftless_cogwheel", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

        scene.world().showSection(util.select().position(1, 0, 5), Direction.NORTH);
        scene.idle(5);
        final ElementLink<WorldSectionElement> plainBig = scene.world().showIndependentSection(util.select().position(1, 2, 4), Direction.DOWN);
        scene.world().moveSection(plainBig, util.vector().of(0d, -1d, 0d), 0);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 3), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 3), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(2, 1, 3, 3, 1, 3), 100)
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(40);
        scene.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, "shaft", util.select().fromTo(3, 1, 3, 3, 2, 3), 40);
        scene.overlay().showText(40)
            .attachKeyFrame()
            .independent(120)
            .colored(PonderPalette.RED)
            .text("This text is defined in a language file");
        scene.idle(40);
        scene.world().hideSection(util.select().position(3, 2, 3), Direction.UP);
        scene.idle(20);

        final ElementLink<WorldSectionElement> showSmall = scene.world().showIndependentSection(util.select().position(1, 3, 4), Direction.DOWN);
        scene.world().moveSection(showSmall, util.vector().of(0, -2d, 0), 0);
        scene.idle(15);
        scene.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.DOWN, plainBig));
        scene.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.DOWN, showSmall));
        scene.addInstruction(new DisplayWorldSectionInstruction(0, Direction.DOWN, util.select().position(1, 1, 4), scene.getScene()::getBaseWorldSection));
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 1, 3), Direction.EAST);
        scene.idle(5);
        final ElementLink<WorldSectionElement> topSmall = scene.world().showIndependentSection(util.select().position(0, 1, 3), Direction.EAST);
        scene.world().moveSection(topSmall, util.vector().of(1d, 0d, -1d), 0);
        scene.idle(20);
        scene.rotateCameraY(-45);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(1, 1, 2, 1, 1, 4), 40)
            .colored(PonderPalette.GREEN)
            .attachKeyFrame()
            .text("This text is defined in a language file");
        scene.idle(50);
        
        scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);
        scene.idle(20);
        scene.overlay().showOutlineWithText(util.select().fromTo(1, 1, 2, 1, 2, 2), 40)
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(60);

        scene.world().showSection(util.select().position(1, 1, 1), Direction.SOUTH);
        scene.idle(10);
        scene.overlay().showOutlineWithText(util.select().fromTo(1, 1, 1, 1, 1, 2), 40)
            .attachKeyFrame()
            .colored(PonderPalette.RED)
            .text("This text is defined in a language file");
        scene.idle(40);
        scene.world().hideSection(util.select().position(1, 1, 1), Direction.NORTH);
        scene.idle(20);

        scene.rotateCameraY(45);
        scene.world().showSection(util.select().position(5, 0, 1), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(4, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 1), Direction.SOUTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 2), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 2), Direction.NORTH);
        scene.idle(5);
        final ElementLink<WorldSectionElement> bottomSmall = scene.world().showIndependentSection(util.select().position(0, 1, 1), Direction.EAST);
        scene.world().moveSection(bottomSmall, util.vector().of(1d, 0d, 1d), 0);
        scene.idle(15);
        scene.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.DOWN, topSmall));
        scene.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.DOWN, bottomSmall));
        scene.addInstruction(new DisplayWorldSectionInstruction(0, Direction.DOWN, util.select().position(1, 1, 2), scene.getScene()::getBaseWorldSection));
        scene.idle(20);

        scene.overlay().showOutlineWithText(util.select().position(1, 1, 2), 60)
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.markAsFinished();
    };

    public static final void shafts(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.shaft", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 3);
		scene.showBasePlate();

        scene.idle(10);
        scene.world().showSection(util.select().position(2, 1, 1), Direction.WEST);
        scene.idle(10);
        scene.world().showSection(util.select().position(2, 2, 1), Direction.DOWN);
        scene.idle(10);
        final ElementLink<WorldSectionElement> plainBig = scene.world().showIndependentSection(util.select().position(1, 2, 1), Direction.EAST);
        scene.idle(10);

        final ElementLink<WorldSectionElement> middleSmall = scene.world().showIndependentSection(util.select().position(0, 3, 1), Direction.EAST);
        scene.world().moveSection(middleSmall, util.vector().of(1d, -1d, 0d), 0);
        scene.idle(15);

        scene.overlay().showOutline(PonderPalette.RED, "not connecting", util.select().position(1, 2, 1), 30);
        scene.idle(40);

        scene.world().moveSection(middleSmall, util.vector().of(-1d, 0d, 0d), 15);
        scene.idle(15);
        final ElementLink<WorldSectionElement> shaftHalf = scene.world().showIndependentSection(util.select().position(0, 2, 1), Direction.NORTH);
        scene.world().moveSection(shaftHalf, util.vector().of(0.5d, 0d, 0d), 0);
        scene.idle(10);
        scene.overlay().showControls(util.vector().of(1.25d, 2.5d, 1.5d), Pointing.DOWN, 40)
            .withItem(PetrolsPartsItems.SHAFT_HALF.asStack());
        scene.idle(50);
        scene.world().moveSection(middleSmall, util.vector().of(1d, 0d, 0d), 10);
        scene.world().moveSection(shaftHalf, util.vector().of(0.5d, 0d, 0d), 10);
        scene.idle(10);
        scene.addInstruction(new SetElementVisibilityInstruction(plainBig, false));
        scene.addInstruction(new SetElementVisibilityInstruction(middleSmall, false));
        scene.addInstruction(new SetElementVisibilityInstruction(shaftHalf, false));
        final ElementLink<WorldSectionElement> bigAndSmall = scene.world().showIndependentSectionImmediately(util.select().position(1, 6, 1));
        scene.world().moveSection(bigAndSmall, util.vector().of(0d, -4d, 0d), 0);

        scene.idle(10);
        scene.overlay().showOutlineWithText(util.select().position(1, 2, 1), 60)
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(80);

        scene.addInstruction(new SetElementVisibilityInstruction(bigAndSmall, false));
        scene.addInstruction(new SetElementVisibilityInstruction(plainBig, true));
        scene.addInstruction(new SetElementVisibilityInstruction(middleSmall, true));
        scene.addInstruction(new SetElementVisibilityInstruction(shaftHalf, true));
        scene.world().moveSection(middleSmall, util.vector().of(-2d, 0d, 0d), 20);
        scene.world().moveSection(shaftHalf, util.vector().of(-1d, 0d, 0d), 20);
        scene.idle(30);
        scene.world().hideIndependentSection(shaftHalf, Direction.NORTH);
        scene.idle(5);

        final ElementLink<WorldSectionElement> shaft = scene.world().showIndependentSection(util.select().position(0, 4, 1), Direction.NORTH);
        scene.world().moveSection(shaft, util.vector().of(0d, -2d, 0d), 0);
        scene.idle(20);
        final ElementLink<WorldSectionElement> endSmall = scene.world().showIndependentSection(util.select().position(0, 5, 1), Direction.EAST);
        scene.world().moveSection(endSmall, util.vector().of(-1.5d, -3d, 0d), 0);
        scene.idle(20);

        scene.world().moveSection(shaft, util.vector().of(1d, 0d, 0d), 15);
        scene.world().moveSection(middleSmall, util.vector().of(2d, 0d, 0d), 15);
        scene.world().moveSection(endSmall, util.vector().of(2.5d, 0d, 0d), 15);
        scene.idle(15);
        scene.addInstruction(new SetElementVisibilityInstruction(plainBig, false));
        scene.addInstruction(new SetElementVisibilityInstruction(middleSmall, false));
        scene.addInstruction(new SetElementVisibilityInstruction(shaft, false));
        scene.addInstruction(new SetElementVisibilityInstruction(endSmall, false));
        final ElementLink<WorldSectionElement> all = scene.world().showIndependentSectionImmediately(util.select().position(1, 10, 1));
        scene.world().moveSection(all, util.vector().of(0d, -8d, 0d), 0);

        scene.idle(10);
        scene.overlay().showOutlineWithText(util.select().position(1, 2, 1), 60)
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(80);
        
        scene.markAsFinished();
    };

    public static final void coaxialCogwheels(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.coaxial_cogwheel", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
        
        scene.markAsFinished();
    };

    public static final void encasing(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.coaxial_cogwheel", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
        
        scene.markAsFinished();
    };
};
