package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;

public class AssemblageScenes {
    
    public static final void shaftlessCogwheels(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.shaftless_cogwheel", "This text is defined in a language file");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
        
        scene.markAsFinished();
    };

    public static final void shafts(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("assemblage.shaft", "This text is defined in a language file");
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
