package net.stardomga.stardoms_colors.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.stardomga.stardoms_colors.block.DyedGlassBlock;
import net.stardomga.stardoms_colors.block.entity.ConcreteBlockEntity;
import net.stardomga.stardoms_colors.block.entity.DyedGlassBlockEntity;

public class DyedGlassBlockEntityRenderer implements BlockEntityRenderer<DyedGlassBlockEntity> {

    public DyedGlassBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}


    @Override
    public void render(DyedGlassBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {

    }

    @Override
    public boolean rendersOutsideBoundingBox(DyedGlassBlockEntity blockEntity) {
        return true; // Important for color updates!
    }
}