package net.stardomga.stardoms_colors.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import net.stardomga.stardoms_colors.block.DyedGlassBlock;
import net.stardomga.stardoms_colors.block.DyedGlassPaneBlock;
import net.stardomga.stardoms_colors.block.entity.DyedGlassBlockEntity;
import net.stardomga.stardoms_colors.block.entity.DyedGlassPaneBlockEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DyeColor;getEntityColor()I")
    )
    private static int changeColor(
            int originalColorInt,
            World world,
            BlockPos pos,
            BlockState state,
            BeaconBlockEntity beacon
    ) {
        int r = 0;
        int g = 0;
        int b = 0;
        int count = 0;

        int buildHeight = world.getHeight();

        for (int y = pos.getY() + 1; y < buildHeight; y++) { // Start above the beacon
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState bs = world.getBlockState(checkPos);

            // Check for opaque blocks to stop the beam
            if (bs.getOpacity() >= 15) {
                break;
            }

            int color = 0;
            if (bs.getBlock() instanceof DyedGlassBlock) {
                // Get color from custom DyedGlassBlock
                BlockEntity be = world.getBlockEntity(checkPos);
                if (be instanceof DyedGlassBlockEntity dyedBE) {
                    color = dyedBE.getColor();
                } else {
                    continue; // Skip if no block entity
                }
            }
            else if (bs.getBlock() instanceof DyedGlassPaneBlock) {
                // Get color from custom DyedGlassBlock
                BlockEntity be = world.getBlockEntity(checkPos);
                if (be instanceof DyedGlassPaneBlockEntity dyedBE) {
                    color = dyedBE.getColor();
                } else {
                    continue; // Skip if no block entity
                }
            }
            else if (bs.getBlock() instanceof Stainable) {
                // Get color from vanilla stained glass
                DyeColor dyeColor = ((Stainable) bs.getBlock()).getColor();
                color = dyeColor.getSignColor();
            }
            else {
                continue; // Skip non-relevant blocks
            }

            // Accumulate color components
            r += (color >> 16) & 0xFF;
            g += (color >> 8) & 0xFF;
            b += color & 0xFF;
            count++;
        }

        if (count == 0) {
            return 0xFFFFFFFF; // White beam if no colored blocks
        }

        // Average the color components
        r /= count;
        g /= count;
        b /= count;

        // Combine into ARGB format (alpha set to 0xFF for full opacity)
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}