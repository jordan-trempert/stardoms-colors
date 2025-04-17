package net.stardomga.stardoms_colors.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.stardomga.stardoms_colors.block.entity.TerracottaSlabBlockEntity;
import net.stardomga.stardoms_colors.block.entity.TerracottaStairBlockEntity;

public class TerracottaStairBlockEntityRenderer implements BlockEntityRenderer<TerracottaStairBlockEntity> {

    public TerracottaStairBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}


    @Override
    public void render(TerracottaStairBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {

    }

    @Override
    public boolean rendersOutsideBoundingBox(TerracottaStairBlockEntity blockEntity) {
        return true; // Important for color updates!
    }
}