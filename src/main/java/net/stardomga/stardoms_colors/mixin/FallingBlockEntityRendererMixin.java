package net.stardomga.stardoms_colors.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.state.FallingBlockEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.FallingBlockEntity;
import net.stardomga.stardoms_colors.block.ConcretePowderBlock;
import net.stardomga.stardoms_colors.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;


@Mixin(FallingBlockEntityRenderer.class)
public class FallingBlockEntityRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(
            FallingBlockEntityRenderState state,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        // Use reflection to get the falling entity from the render state.
        FallingBlockEntity entity = null;
        try {
            Field entityField = state.getClass().getDeclaredField("entity");
            entityField.setAccessible(true);
            entity = (FallingBlockEntity) entityField.get(state);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        if (entity != null && entity.blockEntityData != null &&
                entity.getBlockState().getBlock() == ModBlocks.CONCRETE_POWDER) {
            int color = entity.blockEntityData.getInt("color", 0);
            // Now you have the color for rendering while falling.
            // For example, store it in a ThreadLocal, or apply it directly to the render state.
        }
    }
}