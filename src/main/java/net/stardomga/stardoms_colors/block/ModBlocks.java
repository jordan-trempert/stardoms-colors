package net.stardomga.stardoms_colors.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.stardomga.stardoms_colors.block.entity.ConcreteSlabBlockEntity;

import java.util.function.Function;

public class ModBlocks {
    public static final Block WOOL = register("wool", WoolBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL));
    public static final Block CONCRETE_POWDER = register("concrete_powder", ConcretePowderBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE_POWDER));
    public static final Block CONCRETE = register("concrete", ConcreteBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE));
    public static final Block CONCRETE_SLAB = register("concrete_slab", ConcreteSlabBlock::new, Block.Settings.copy(Blocks.WHITE_CONCRETE));
    public static final Block DYED_GLASS = register("dyed_glass", DyedGlassBlock::new, Block.Settings.copy(Blocks.GLASS).nonOpaque());
    public static final Block DYED_GLASS_PANE = register("dyed_glass_pane", DyedGlassPaneBlock::new, Block.Settings.copy(Blocks.GLASS).nonOpaque());
    public static final Block BRICKS = register("bricks", BricksBlock::new, Block.Settings.copy(Blocks.BRICKS));
    public static final Block BRICK_SLAB = register("brick_slab", BrickSlabBlock::new, Block.Settings.copy(Blocks.BRICKS));
    public static final Block BED = register("bed", BedBlock::new, Block.Settings.copy(Blocks.WHITE_BED));
    public static final Block TERRACOTTA = register("terracotta", TerracottaBlock::new, Block.Settings.copy(Blocks.TERRACOTTA));
    public static final Block TERRACOTTA_SLAB = register("terracotta_slab", TerracottaSlabBlock::new, Block.Settings.copy(Blocks.TERRACOTTA));
    public static final Block CANDLE = register("candle", CandleBlock::new, Block.Settings.copy(Blocks.WHITE_CANDLE));


    private static Block register(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of("stardoms_colors", path);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        return block;
    }

    public static void registerBlocks(){

    }

}
