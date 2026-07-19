package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;

public class CompositeBevelCogWheelRenderer extends SafeBlockEntityRenderer<CompositeBevelCogWheelBlockEntity> {

    public final BevelCogWheelSet.Client set;

    public CompositeBevelCogWheelRenderer(BevelCogWheelSet.Client set, BlockEntityRendererProvider.Context context) {
        this.set = set;
    };

    @Override
    protected void renderSafe(CompositeBevelCogWheelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // TODO Auto-generated method stub
        
    };
    
};
