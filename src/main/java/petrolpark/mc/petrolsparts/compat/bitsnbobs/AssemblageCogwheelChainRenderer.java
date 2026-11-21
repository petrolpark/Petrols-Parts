package petrolpark.mc.petrolsparts.compat.bitsnbobs;

import com.kipti.bnb.content.kinetics.cogwheel_chain.behaviour.CogwheelChainBehaviour;
import com.kipti.bnb.content.kinetics.cogwheel_chain.behaviour.CogwheelChainBehaviourRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;

public class AssemblageCogwheelChainRenderer {

    private static CogwheelChainBehaviourRenderer renderer = null;

    public static void render(AssemblageBlockEntity assemblage, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        final AssemblageBlockEntityPart part = assemblage.getMiddleCogPart();
        final CogwheelChainBehaviour behaviour = AssemblageCogwheelChains.getChainBehaviour(part);
        if (behaviour == null || behaviour.getControlledChain() == null) return;
        if (renderer == null) renderer = new CogwheelChainBehaviourRenderer();
        renderer.renderSafe(behaviour, part, partialTicks, ms, bufferSource, light, overlay);
    };
};
