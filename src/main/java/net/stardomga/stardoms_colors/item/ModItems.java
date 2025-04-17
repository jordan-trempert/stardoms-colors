package net.stardomga.stardoms_colors.item;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.stardomga.stardoms_colors.Stardoms_colors;
import net.stardomga.stardoms_colors.block.ConcreteSlabBlock;
import net.stardomga.stardoms_colors.block.DyedGlassBlock;
import net.stardomga.stardoms_colors.block.ModBlocks;
import net.stardomga.stardoms_colors.block.WoolBlock;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ModItems {
    // Default teal color (0x008080)
    public static final Item WOOL = register(ModBlocks.WOOL, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 0);
    public static final Item CONCRETE_POWDER = register(ModBlocks.CONCRETE_POWDER, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 1);
    public static final Item CONCRETE = register(ModBlocks.CONCRETE, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 2);
    public static final Item DYED_GLASS = register(ModBlocks.DYED_GLASS, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 3);
    public static final Item DYED_GLASS_PANE = register(ModBlocks.DYED_GLASS_PANE, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 4);
    public static final Item BRICKS = register(ModBlocks.BRICKS, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 5);
    public static final Item BRICK_SLAB = register(ModBlocks.BRICK_SLAB, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 10);
    public static final Item BRICK_STAIR = register(ModBlocks.BRICK_STAIR, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 12);
    public static final Item BRICK_WALL = register(ModBlocks.BRICK_WALL, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 12);
    public static final Item BED = register(ModBlocks.BED, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 6);
    public static final Item TERRACOTTA = register(ModBlocks.TERRACOTTA, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 7);
    public static final Item TERRACOTTA_SLAB = register(ModBlocks.TERRACOTTA_SLAB, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 11);
    public static final Item TERRACOTTA_STAIR = register(ModBlocks.TERRACOTTA_STAIR, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 14);
    public static final Item TERRACOTTA_WALL = register(ModBlocks.TERRACOTTA_WALL, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 14);
    public static final Item BUNDLE = register("bundle", (settings) -> {return new BundleItem(settings.component(DataComponentTypes.DYED_COLOR, new DyedColorComponent(9722929)).maxCount(1).component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT));});
    public static final Item CANDLE = register(ModBlocks.CANDLE, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 8);
    public static final Item CONCRETE_SLAB = register(ModBlocks.CONCRETE_SLAB, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 9);
    public static final Item CONCRETE_STAIR = register(ModBlocks.CONCRETE_STAIR, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 13);
    public static final Item CONCRETE_WALL = register(ModBlocks.CONCRETE_WALL, new Item.Settings().component(DataComponentTypes.DYED_COLOR,new DyedColorComponent(0xFFFFFF)), 13);

    // Helper method to register items
    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Stardoms_colors.MOD_ID, id));
    }
    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    public static Item register(Block block) {
        return register(block, BlockItem::new);
    }

    public static Item register(Block block, Item.Settings settings) {
        return register(block, BlockItem::new, settings);
    }

    public static Item register(Block block, Item.Settings settings, int in) {
        if(in == 0){
            return register(block, WoolBlockItem::new, settings);
        }
        else if(in == 1){
            return register(block, ConcretePowderBlockItem::new, settings);
        }
        else if(in == 2){
            return register(block, ConcreteBlockItem::new, settings);
        }
        else if(in == 3){
            return register(block, DyedGlassBlockItem::new, settings);
        }
        else if(in == 4){
            return register(block, DyedGlassPaneBlockItem::new, settings);
        }
        else if(in == 5){
            return register(block,BricksBlockItem::new, settings);
        }
        else if(in == 6){
            return register(block,BedBlockItem::new, settings);
        }
        else if(in == 7){
            return register(block,TerracottaBlockItem::new, settings);
        }
        else if(in == 8){
            return register(block, CandleBlockItem::new, settings);
        }
        else if(in == 9){
            return register(block, ConcreteSlabBlockItem::new, settings);
        }
        else if(in == 10){
            return register(block, BrickSlabBlockItem::new, settings);
        }
        else if(in == 11){
            return register(block, TerracottaSlabBlockItem::new, settings);
        }
        else if(in == 12){
            return register(block, BrickStairsBlockItem::new, settings);
        }
        else if(in == 13){
            return register(block, ConcreteStairsBlockItem::new, settings);
        }
        else if(in == 14){
            return register(block, TerracottaStairsBlockItem::new, settings);
        }
        return register(block, WoolBlockItem::new, settings);
    }

    public static Item register(Block block, UnaryOperator<Item.Settings> settingsOperator) {
        return register(block, (blockx, settings) -> {
            return new BlockItem(blockx, (Item.Settings)settingsOperator.apply(settings));
        });
    }

    public static Item register(Block block, Block... blocks) {
        Item item = register(block);
        Block[] var3 = blocks;
        int var4 = blocks.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Block block2 = var3[var5];
            Item.BLOCK_ITEMS.put(block2, item);
        }

        return item;
    }

    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
        return register(block, factory, new Item.Settings());
    }

    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return register(keyOf(block.getRegistryEntry().registryKey()), (itemSettings) -> {
            return (Item)factory.apply(block, itemSettings);
        }, settings.useBlockPrefixedTranslationKey());
    }

    public static Item register(String id, Function<Item.Settings, Item> factory) {
        return register(keyOf(id), factory, new Item.Settings());
    }

    public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return register(keyOf(id), factory, settings);
    }

    public static Item register(String id, Item.Settings settings) {
        return register(keyOf(id), Item::new, settings);
    }

    public static Item register(String id) {
        return register(keyOf(id), Item::new, new Item.Settings());
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory) {
        return register(key, factory, new Item.Settings());
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return (Item) Registry.register(Registries.ITEM, key, item);
    }

    // Method to create a colored wool item stack
    public static ItemStack createColoredWool(int color) {
        ItemStack stack = new ItemStack(WOOL);
        stack.set(DataComponentTypes.DYED_COLOR,
                new DyedColorComponent(color));
        return stack;
    }

    public static ItemStack createColoredConcretePowder(int color) {
        ItemStack stack = new ItemStack(CONCRETE_POWDER);
        stack.set(DataComponentTypes.DYED_COLOR,
                new DyedColorComponent(color));
        return stack;
    }

    // Call this in your mod initializer
    public static void registerModItems() {
        Stardoms_colors.LOGGER.info("Registering ModItems for " + Stardoms_colors.MOD_ID);
    }
}