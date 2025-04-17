package net.stardomga.stardoms_colors.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Items;
import net.minecraft.item.ProjectileItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.stardomga.stardoms_colors.Stardoms_colors;
import net.stardomga.stardoms_colors.block.entity.ConcreteSlabBlockEntity;
import net.stardomga.stardoms_colors.block.entity.ConcreteWallBlockEntity;

import java.util.function.Function;

public class ModBlocks {
    public static final Block WOOL = register("wool", WoolBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL));
    public static final Block CONCRETE_POWDER = register("concrete_powder", ConcretePowderBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE_POWDER));
    public static final Block CONCRETE = register("concrete", ConcreteBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE));
    public static final Block CONCRETE_SLAB = register("concrete_slab", ConcreteSlabBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE));
    public static final Block CONCRETE_WALL = register("concrete_wall", ConcreteWallBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE));
    public static final Block CONCRETE_STAIR = registerConcreteStairsBlock("concrete_stair", ModBlocks.CONCRETE);
    public static final Block DYED_GLASS = register("dyed_glass", DyedGlassBlock::new, Block.Settings.copy(Blocks.GLASS).nonOpaque());
    public static final Block DYED_GLASS_PANE = register("dyed_glass_pane", DyedGlassPaneBlock::new, Block.Settings.copy(Blocks.GLASS).nonOpaque());
    public static final Block BRICKS = register("bricks", BricksBlock::new, Block.Settings.copy(Blocks.BRICKS));
    public static final Block BRICK_SLAB = register("brick_slab", BrickSlabBlock::new, Block.Settings.copy(Blocks.BRICKS));
    public static final Block BRICK_WALL = register("brick_wall", BrickWallBlock::new, Block.Settings.copy(Blocks.BRICKS));
    public static final Block BRICK_STAIR = registerBrickStairsBlock("brick_stair", ModBlocks.BRICKS);
    public static final Block BED = register("bed", BedBlock::new, Block.Settings.copy(Blocks.WHITE_BED));
    public static final Block TERRACOTTA = register("terracotta", TerracottaBlock::new, Block.Settings.copy(Blocks.TERRACOTTA));
    public static final Block TERRACOTTA_SLAB = register("terracotta_slab", TerracottaSlabBlock::new, Block.Settings.copy(Blocks.TERRACOTTA));
    public static final Block TERRACOTTA_WALL = register("terracotta_wall", TerracottaWallBlock::new, Block.Settings.copy(Blocks.TERRACOTTA));
    public static final Block TERRACOTTA_STAIR = registerTerracottaStairsBlock("terracotta_stair", ModBlocks.TERRACOTTA);
    public static final Block CANDLE = register("candle", CandleBlock::new, Block.Settings.copy(Blocks.WHITE_CANDLE));


    private static Block register(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of("stardoms_colors", path);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        return block;
    }

    private static Block registerBrickStairsBlock(String id, Block base) {
        return register(id, settings -> new BrickStairBlock(base.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(base));
    }
    private static Block registerConcreteStairsBlock(String id, Block base) {
        return register(id, settings -> new ConcreteStairBlock(base.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(base));
    }
    private static Block registerTerracottaStairsBlock(String id, Block base) {
        return register(id, settings -> new TerracottaStairBlock(base.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(base));
    }

    public static void registerBlocks(){

    }

}
