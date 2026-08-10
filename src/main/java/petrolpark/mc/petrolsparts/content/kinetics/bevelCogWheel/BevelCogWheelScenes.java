package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

public class BevelCogWheelScenes {
    
    public static final void orthogonal(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
    };

    public static final void diagonal(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel.diagonal", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
    };

    public static final void encasing(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel.encasing", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
    };
};
