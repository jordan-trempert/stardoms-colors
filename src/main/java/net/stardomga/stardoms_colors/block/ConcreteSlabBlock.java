package net.stardomga.stardoms_colors.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.stardomga.stardoms_colors.block.entity.ConcreteBlockEntity;
import net.stardomga.stardoms_colors.block.entity.ConcreteSlabBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ConcreteSlabBlock extends SlabBlock implements BlockEntityProvider {
    public ConcreteSlabBlock(Settings settings) {
        super(settings);
    }



    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConcreteSlabBlockEntity(pos, state);
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
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        if (world.getBlockEntity(pos) instanceof ConcreteSlabBlockEntity be) {
            return be.getColoredDrop(1);
        }
        return super.getPickStack(world, pos, state, true);
    }


    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof ConcreteSlabBlockEntity coloredBE && player.getMainHandStack().isSuitableFor(Blocks.STONE.getDefaultState())) {
            // Check if the slab is a double slab
            int amnt = state.get(TYPE) == SlabType.DOUBLE ? 2 : 1; // 2 if double, 1 otherwise

            ItemStack coloredWool = coloredBE.getColoredDrop(amnt);

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
        if (world.getBlockEntity(pos) instanceof ConcreteSlabBlockEntity coloredBE) {
            DyedColorComponent colorComponent = itemStack.get(DataComponentTypes.DYED_COLOR);
            if (colorComponent != null) {
                coloredBE.setColor(colorComponent.rgb());
            }

        }
    }

    
}