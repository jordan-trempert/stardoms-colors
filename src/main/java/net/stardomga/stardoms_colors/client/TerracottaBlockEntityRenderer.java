package net.stardomga.stardoms_colors.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.stardomga.stardoms_colors.block.entity.BricksBlockEntity;
import net.stardomga.stardoms_colors.block.entity.TerracottaBlockEntity;

public class TerracottaBlockEntityRenderer implements BlockEntityRenderer<TerracottaBlockEntity> {

    public TerracottaBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}


    @Override
    public void render(TerracottaBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {

    }

    @Override
    public boolean rendersOutsideBoundingBox(TerracottaBlockEntity blockEntity) {
        return true; // Important for color updates!
    }
}