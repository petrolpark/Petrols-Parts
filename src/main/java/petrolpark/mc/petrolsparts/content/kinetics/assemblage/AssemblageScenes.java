package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction;
import net.createmod.ponder.foundation.instruction.FadeOutOfSceneInstruction;
import net.minecraft.core.Direction;

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
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("This text is defined in a language file");
        scene.idle(40);
        scene.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, "shaft", util.select().fromTo(3, 1, 3, 3, 2, 3), 40);
        scene.overlay().showText(40)
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
        scene.world().showSection(util.select().position(1, 1, 2), Direction.EAST);
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
        
        scene.markAsFinished();
    };

    public static final void shafts(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.shaft", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
        
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
