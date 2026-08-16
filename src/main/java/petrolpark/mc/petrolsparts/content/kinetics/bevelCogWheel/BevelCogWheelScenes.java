package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
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
    };

    public static final void encasing(SceneBuilder sceneIn, SceneBuildingUtil util) {
        final CreateSceneBuilder scene = new CreateSceneBuilder(sceneIn);
        scene.title("bevel_cogwheel.encasing", "This text is defined in a language file");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
    };
};
