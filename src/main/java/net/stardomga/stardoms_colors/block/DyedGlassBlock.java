package net.stardomga.stardoms_colors.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.stardomga.stardoms_colors.block.entity.ColoredBlockEntity;
import net.stardomga.stardoms_colors.block.entity.DyedGlassBlockEntity;
import net.stardomga.stardoms_colors.util.DyeColorHelper;
import org.jetbrains.annotations.Nullable;

public class DyedGlassBlock extends BlockWithEntity implements Stainable {
    public DyedGlassBlock(Settings settings) {
        super(settings);
    }


    @Override
    protected MapCodec<? extends DyedGlassBlock> getCodec() {
        return createCodec(DyedGlassBlock::new);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyedGlassBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /*
        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
            // Make sure to check world.isClient if you only want to tick only on serverside.
            return validateTicker(type, StardomsColorsBlockEntityTypes.WOOL, ColoredBlockEntity::tick);
        }
    */
    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            if (world.getBlockEntity(pos) instanceof DyedGlassBlockEntity colorBlockEntity) {
                final int newColor = dyeItem.getColor().getEntityColor();
                final int originalColor = colorBlockEntity.color;
                colorBlockEntity.color = ColorHelper.average(newColor, originalColor);
                stack.decrementUnlessCreative(1, player);
                colorBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }


    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        if (world.getBlockEntity(pos) instanceof DyedGlassBlockEntity be) {
            return be.getColoredDrop();
        }
        return super.getPickStack(world, pos, state, true);
    }


    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof DyedGlassBlockEntity coloredBE) {
            // Create the item stack with the correct color component
            ItemStack coloredWool = coloredBE.getColoredDrop();


            // Spawn the item in the world
            ItemEntity itemEntity = new ItemEntity(
                    world,
                    pos.getX() + 0.5,  // Center of block
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    coloredWool
            );

            // Optional: Add slight random velocity like normal block drops
            itemEntity.setVelocity(
                    world.random.nextFloat() * 0.1f - 0.05f,
                    0.2f,
                    world.random.nextFloat() * 0.1f - 0.05f
            );

            if(!player.isCreative()){
                world.spawnEntity(itemEntity);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        // Get the block entity and set its color from the item
        if (world.getBlockEntity(pos) instanceof DyedGlassBlockEntity coloredBE) {
            DyedColorComponent colorComponent = itemStack.get(DataComponentTypes.DYED_COLOR);
            if (colorComponent != null) {
                coloredBE.setColor(colorComponent.rgb());
            }
        }
    }


    @Override
    public DyeColor getColor() {
        return DyeColor.PURPLE;  // Fallback color if no block entity found
    }


}