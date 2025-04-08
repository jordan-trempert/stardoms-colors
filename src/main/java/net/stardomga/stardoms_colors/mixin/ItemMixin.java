package net.stardomga.stardoms_colors.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.stardomga.stardoms_colors.block.ModBlocks;
import net.stardomga.stardoms_colors.block.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract Item asItem();

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!(this.asItem() instanceof DyeItem)) {
            return;
        }

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();
        String blockName = block.toString().toLowerCase();
        DyeColor dyeColor = ((DyeItem) this.asItem()).getColor();

        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        if (blockName.contains("_wool")) {
            handleWool(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (blockName.contains("_concrete_powder")) {
            handleConcretePowder(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (blockName.contains("_stained_glass") && !blockName.contains("pane")) {
            handleStainedGlass(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (blockName.contains("_stained_glass_pane")) {
            handleStainedGlassPane(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (blockName.contains("_bed")) {
            handleBed(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (block.getDefaultState().isOf(Blocks.BRICKS)) {
            handleBricks(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (blockName.contains("_terracotta")) {
            handleTerracotta(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (blockName.contains("_candle")) {
            handleCandle(world, pos, block, dyeColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    private void handleWool(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.WOOL.getDefaultState());
        if (world.getBlockEntity(pos) instanceof ColoredBlockEntity woolEntity) {
            woolEntity.color =  getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private void handleCandle(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.CANDLE.getDefaultState());
        if (world.getBlockEntity(pos) instanceof CandleBlockEntity woolEntity) {
            woolEntity.color =  getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private void handleTerracotta(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.TERRACOTTA.getDefaultState());
        if (world.getBlockEntity(pos) instanceof TerracottaBlockEntity woolEntity) {
            woolEntity.color =  getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private void handleBricks(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.BRICKS.getDefaultState());
        if (world.getBlockEntity(pos) instanceof BricksBlockEntity woolEntity) {
            woolEntity.color = getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private static void killClosestItemEntity(World world, Vec3d center, double radius) {
        // Create a bounding box around the center
        Box box = new Box(center.subtract(radius, radius, radius), center.add(radius, radius, radius));

        // Find closest item entity
        ItemEntity closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : world.getEntitiesByClass(ItemEntity.class, box, e -> true)) {
            double distance = entity.squaredDistanceTo(center);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = (ItemEntity) entity;
            }
        }

        if (closest != null) {
            closest.discard(); // kills the entity (removes it from world)
        }
    }

    private void handleBed(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.BED.getDefaultState());
        killClosestItemEntity(world, pos.toCenterPos(), 2);

        // Choose a direction for the bed (e.g., north)
        Direction direction = Direction.NORTH;

        // Calculate the position of the head part of the bed
        BlockPos headPos = pos.offset(direction);

        // Set the foot part of the bed
        world.setBlockState(pos, ModBlocks.BED.getDefaultState()
                .with(BedBlock.FACING, direction)
                .with(BedBlock.PART, BedPart.FOOT));

        // Set the head part of the bed
        world.setBlockState(headPos, ModBlocks.BED.getDefaultState()
                .with(BedBlock.FACING, direction)
                .with(BedBlock.PART, BedPart.HEAD));

        // Set color for the block entity if it's a BedBlockEntity
        if (world.getBlockEntity(pos) instanceof BedBlockEntity footEntity) {
            footEntity.color = getDyeColorValue(block) + dyeColor.getEntityColor();
        }
        if (world.getBlockEntity(headPos) instanceof BedBlockEntity headEntity) {
            headEntity.color = getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }




    private void handleConcretePowder(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.CONCRETE_POWDER.getDefaultState());
        if (world.getBlockEntity(pos) instanceof ConcretePowderBlockEntity powderEntity) {
            powderEntity.color = getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private void handleStainedGlass(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.DYED_GLASS.getDefaultState());
        if (world.getBlockEntity(pos) instanceof DyedGlassBlockEntity glassEntity) {
            glassEntity.color = getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private void handleStainedGlassPane(World world, BlockPos pos, Block block, DyeColor dyeColor) {
        world.setBlockState(pos, ModBlocks.DYED_GLASS_PANE.getDefaultState());
        if (world.getBlockEntity(pos) instanceof DyedGlassPaneBlockEntity paneEntity) {
            paneEntity.color = getDyeColorValue(block) + dyeColor.getEntityColor();
        }
    }

    private int getDyeColorValue(Block block) {
        if (block.getDefaultState().isOf(Blocks.WHITE_WOOL))return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_WOOL)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_WOOL)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_WOOL)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_WOOL)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_WOOL)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_WOOL)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_WOOL)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_WOOL)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_WOOL)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_WOOL)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_WOOL)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_WOOL)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_WOOL)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_WOOL)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_WOOL)) return DyeColor.BROWN.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.WHITE_TERRACOTTA))return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_TERRACOTTA)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_TERRACOTTA)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_TERRACOTTA)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_TERRACOTTA)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_TERRACOTTA)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_TERRACOTTA)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_TERRACOTTA)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_TERRACOTTA)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_TERRACOTTA)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_TERRACOTTA)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_TERRACOTTA)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_TERRACOTTA)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_TERRACOTTA)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_TERRACOTTA)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_TERRACOTTA)) return DyeColor.BROWN.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.WHITE_CANDLE))return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_CANDLE)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_CANDLE)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_CANDLE)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_CANDLE)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_CANDLE)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_CANDLE)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_CANDLE)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_CANDLE)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_CANDLE)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_CANDLE)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_CANDLE)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_CANDLE)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_CANDLE)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_CANDLE)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_CANDLE)) return DyeColor.BROWN.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.BRICKS)) return DyeColor.RED.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.WHITE_CONCRETE_POWDER)) return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_CONCRETE_POWDER)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_CONCRETE_POWDER)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_CONCRETE_POWDER)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_CONCRETE_POWDER)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_CONCRETE_POWDER)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_CONCRETE_POWDER)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_CONCRETE_POWDER)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_CONCRETE_POWDER)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_CONCRETE_POWDER)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_CONCRETE_POWDER)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_CONCRETE_POWDER)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_CONCRETE_POWDER)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_CONCRETE_POWDER)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_CONCRETE_POWDER)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_CONCRETE_POWDER)) return DyeColor.BROWN.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.WHITE_STAINED_GLASS)) return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_STAINED_GLASS)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_STAINED_GLASS)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_STAINED_GLASS)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_STAINED_GLASS)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_STAINED_GLASS)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_STAINED_GLASS)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_STAINED_GLASS)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_STAINED_GLASS)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_STAINED_GLASS)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_STAINED_GLASS)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_STAINED_GLASS)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_STAINED_GLASS)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_STAINED_GLASS)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_STAINED_GLASS)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_STAINED_GLASS)) return DyeColor.BROWN.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.WHITE_STAINED_GLASS_PANE)) return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_STAINED_GLASS_PANE)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_STAINED_GLASS_PANE)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_STAINED_GLASS_PANE)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_STAINED_GLASS_PANE)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_STAINED_GLASS_PANE)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_STAINED_GLASS_PANE)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_STAINED_GLASS_PANE)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_STAINED_GLASS_PANE)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_STAINED_GLASS_PANE)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_STAINED_GLASS_PANE)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_STAINED_GLASS_PANE)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_STAINED_GLASS_PANE)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_STAINED_GLASS_PANE)) return DyeColor.BROWN.getEntityColor();

        if (block.getDefaultState().isOf(Blocks.WHITE_BED)) return DyeColor.WHITE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.RED_BED)) return DyeColor.RED.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.ORANGE_BED)) return DyeColor.ORANGE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.YELLOW_BED)) return DyeColor.YELLOW.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GREEN_BED)) return DyeColor.GREEN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLUE_BED)) return DyeColor.BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PURPLE_BED)) return DyeColor.PURPLE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.PINK_BED)) return DyeColor.PINK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.MAGENTA_BED)) return DyeColor.MAGENTA.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIME_BED)) return DyeColor.LIME.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_BLUE_BED)) return DyeColor.LIGHT_BLUE.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.LIGHT_GRAY_BED)) return DyeColor.LIGHT_GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.GRAY_BED)) return DyeColor.GRAY.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.CYAN_BED)) return DyeColor.CYAN.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BLACK_BED)) return DyeColor.BLACK.getEntityColor();
        if (block.getDefaultState().isOf(Blocks.BROWN_BED)) return DyeColor.BROWN.getEntityColor();

        return 0; // Default color if none matched
    }
}