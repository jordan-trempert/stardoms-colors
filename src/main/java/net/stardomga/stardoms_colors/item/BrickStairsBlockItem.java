package net.stardomga.stardoms_colors.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stardomga.stardoms_colors.block.entity.BrickSlabBlockEntity;
import net.stardomga.stardoms_colors.block.entity.BrickStairBlockEntity;
import org.jetbrains.annotations.Nullable;

public class BrickStairsBlockItem extends BlockItem {

    public BrickStairsBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        // Transfer color from item to block entity
        if (world.getBlockEntity(pos) instanceof BrickStairBlockEntity coloredBE) {
            DyedColorComponent colorComponent = stack.get(DataComponentTypes.DYED_COLOR);
            if (colorComponent != null) {
                coloredBE.setColor(colorComponent.rgb());
                coloredBE.markDirty();
            }
        }
        return super.postPlacement(pos, world, player, stack, state);
    }
}
