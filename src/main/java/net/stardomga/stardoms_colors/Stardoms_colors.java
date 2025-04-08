package net.stardomga.stardoms_colors;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.stardomga.stardoms_colors.block.ModBlocks;
import net.stardomga.stardoms_colors.block.entity.StardomsColorsBlockEntityTypes;
import net.stardomga.stardoms_colors.item.ModItems;
import net.stardomga.stardoms_colors.util.ModBlockColorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stardoms_colors implements ModInitializer {
    public static final String MOD_ID = "stardoms_colors";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        StardomsColorsBlockEntityTypes.initialize();
        ModBlocks.registerBlocks();
        ModItems.registerModItems();
        //ModBlockColorProvider.registerBlockColors();
    }



}
