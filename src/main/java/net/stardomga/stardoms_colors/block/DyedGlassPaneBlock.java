package net.stardomga.stardoms_colors.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.stardomga.stardoms_colors.block.entity.DyedGlassPaneBlockEntity;
import org.jetbrains.annotations.Nullable;

public class DyedGlassPaneBlock extends PaneBlock implements BlockEntityProvider {

    public static final MapCodec<DyedGlassPaneBlock> CODEC = createCodec(DyedGlassPaneBlock::new);

    public DyedGlassPaneBlock(Settings settings) {
        super(settings);
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyedGlassPaneBlockEntity(pos, state);
    }


    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return direction.getAxis().isHorizontal()
                ? state.with(FACING_PROPERTIES.get(direction),
                connectsTo(neighborState, neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())))
                : super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }







    // Color behavior

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            if (world.getBlockEntity(pos) instanceof DyedGlassPaneBlockEntity colorBlockEntity) {
                int newColor = dyeItem.getColor().getEntityColor();
                int originalColor = colorBlockEntity.color;
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
        if (world.getBlockEntity(pos) instanceof DyedGlassPaneBlockEntity be) {
            return be.getColoredDrop();
        }
        return super.getPickStack(world, pos, state, includeData);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof DyedGlassPaneBlockEntity coloredBE) {
            ItemStack drop = coloredBE.getColoredDrop();
            ItemEntity itemEntity = new ItemEntity(world,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
            itemEntity.setVelocity(world.random.nextFloat() * 0.1f - 0.05f, 0.2f, world.random.nextFloat() * 0.1f - 0.05f);
            if (!player.isCreative()) {
                world.spawnEntity(itemEntity);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof DyedGlassPaneBlockEntity be) {
            DyedColorComponent colorComponent = itemStack.get(DataComponentTypes.DYED_COLOR);
            if (colorComponent != null) {
                be.setColor(colorComponent.rgb());
            }
        }
    }
}
