package net.stardomga.stardoms_colors.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.model.special.BedModelRenderer;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.stardomga.stardoms_colors.Stardoms_colors;
import net.stardomga.stardoms_colors.block.ModBlocks;
import net.stardomga.stardoms_colors.block.entity.ConcretePowderBlockEntity;
import net.stardomga.stardoms_colors.block.entity.StardomsColorsBlockEntityTypes;
import net.stardomga.stardoms_colors.util.ModBlockColorProvider;

public class Stardoms_colorsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.WOOL,
                ColoredBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.CONCRETE_POWDER,
                ConcretePowderBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.CONCRETE,
                ConcreteBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.DYED_GLASS,
                DyedGlassBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.DYED_GLASS_PANE,
                DyedGlassPaneBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.BRICKS,
                BricksBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.BED,
                BedBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.TERRACOTTA,
                TerracottaBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.CANDLE,
                CandleBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.CONCRETE_SLAB,
                ConcreteSlabBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.BRICK_SLAB,
                BrickSlabBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                StardomsColorsBlockEntityTypes.TERRACOTTA_SLAB,
                TerracottaSlabBlockEntityRenderer::new
        );






        ModBlockColorProvider.registerBlockColors();
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYED_GLASS_PANE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BRICKS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CANDLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BRICK_SLAB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BRICK_STAIR, RenderLayer.getCutout());


    }
}
