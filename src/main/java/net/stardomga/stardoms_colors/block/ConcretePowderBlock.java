package net.stardomga.stardoms_colors.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import net.stardomga.stardoms_colors.block.entity.ColoredBlockEntity;
import net.stardomga.stardoms_colors.block.entity.ConcreteBlockEntity;
import net.stardomga.stardoms_colors.block.entity.ConcretePowderBlockEntity;
import net.stardomga.stardoms_colors.util.BlockEntityDataCache;
import org.jetbrains.annotations.Nullable;

public class ConcretePowderBlock extends FallingBlock implements BlockEntityProvider {
    public ConcretePowderBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends ConcretePowderBlock> getCodec() {
        return createCodec(ConcretePowderBlock::new);
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return 0;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            if (world.getBlockEntity(pos) instanceof ConcretePowderBlockEntity colorBlockEntity) {
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
        if (world.getBlockEntity(pos) instanceof ConcretePowderBlockEntity be) {
            return be.getColoredDrop();
        }
        return super.getPickStack(world, pos, state, true);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof ConcretePowderBlockEntity coloredBE) {
            ItemStack coloredWool = coloredBE.getColoredDrop();

            ItemEntity itemEntity = new ItemEntity(
                    world,
                    pos.getX() + 0.5,  // Center of block
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    coloredWool
            );

            itemEntity.setVelocity(
                    world.random.nextFloat() * 0.1f - 0.05f,
                    0.2f,
                    world.random.nextFloat() * 0.1f - 0.05f
            );

            if (!player.isCreative()) {
                world.spawnEntity(itemEntity);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
        super.onLanding(world, pos, fallingBlockState, currentStateInPos, fallingBlockEntity);

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighbor);
            if (neighborState.getFluidState().isIn(FluidTags.WATER)) {
                // Before replacing, capture the color from the old block entity
                BlockEntity oldEntity = world.getBlockEntity(pos);
                if (oldEntity instanceof ConcretePowderBlockEntity concreteEntity) {
                    int color = concreteEntity.color;
                    // Store the color in the cache keyed by a copy of the block position
                    BlockEntityDataCache.COLOR_CACHE.put(pos.toImmutable(), color);
                }
                // Replace with the new block (Concrete or Special block)
                world.setBlockState(pos, ModBlocks.CONCRETE.getDefaultState(), Block.NOTIFY_ALL);
                return;
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.getBlockEntity(pos) instanceof ConcretePowderBlockEntity coloredBE) {
            DyedColorComponent colorComponent = itemStack.get(DataComponentTypes.DYED_COLOR);
            if (colorComponent != null) {
                coloredBE.setColor(colorComponent.rgb());
            }
        }

        int oldColor = 0;
        // Check if any neighboring block is water
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighbor);
            if (neighborState.getFluidState().isIn(FluidTags.WATER)) {
                // Before replacing, capture the color from the old block entity
                BlockEntity oldEntity = world.getBlockEntity(pos);
                if (oldEntity instanceof ConcretePowderBlockEntity concreteEntity) {
                    oldColor = concreteEntity.color;
                }
                // Replace with the new block (Concrete or Special block)
                world.setBlockState(pos, ModBlocks.CONCRETE.getDefaultState(), Block.NOTIFY_ALL);
                if(world.getBlockEntity(pos) instanceof ConcreteBlockEntity blockEntity){
                    blockEntity.color = oldColor;
                }
                return;
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConcretePowderBlockEntity(pos, state);
    }

    // This method checks if water is adjacent to the concrete powder and turns it into a diamond block.


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
        int oldColor = 0;
        // Check if any neighboring block is water
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighbor);
            if (neighborState.getFluidState().isIn(FluidTags.WATER)) {
                // Before replacing, capture the color from the old block entity
                BlockEntity oldEntity = world.getBlockEntity(pos);
                if (oldEntity instanceof ConcretePowderBlockEntity concreteEntity) {
                    oldColor = concreteEntity.color;
                }
                // Replace with the new block (Concrete or Special block)
                world.setBlockState(pos, ModBlocks.CONCRETE.getDefaultState(), Block.NOTIFY_ALL);
                if(world.getBlockEntity(pos) instanceof ConcreteBlockEntity blockEntity){
                    blockEntity.color = oldColor;
                }
                return;
            }
        }
    }




}