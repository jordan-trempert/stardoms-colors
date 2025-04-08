package net.stardomga.stardoms_colors.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.stardomga.stardoms_colors.block.CandleBlock;
import net.stardomga.stardoms_colors.block.ModBlocks;

public class StardomsColorsBlockEntityTypes {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("stardoms_colors", path), blockEntityType);
    }

    public static final BlockEntityType<ColoredBlockEntity> WOOL = register(
            "wool",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(ColoredBlockEntity::new, ModBlocks.WOOL).build()
    );

    public static final BlockEntityType<ConcretePowderBlockEntity> CONCRETE_POWDER = register(
            "concrete_powder",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(ConcretePowderBlockEntity::new, ModBlocks.CONCRETE_POWDER).build()
    );

    public static final BlockEntityType<ConcreteBlockEntity> CONCRETE = register(
            "concrete",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(ConcreteBlockEntity::new, ModBlocks.CONCRETE).build()
    );

    public static final BlockEntityType<DyedGlassBlockEntity> DYED_GLASS = register(
            "dyed_glass",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(DyedGlassBlockEntity::new, ModBlocks.DYED_GLASS).build()
    );

    public static final BlockEntityType<DyedGlassPaneBlockEntity> DYED_GLASS_PANE = register(
            "dyed_glass_pane",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(DyedGlassPaneBlockEntity::new, ModBlocks.DYED_GLASS_PANE).build()
    );

    public static final BlockEntityType<BricksBlockEntity> BRICKS = register(
            "bricks",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(BricksBlockEntity::new, ModBlocks.BRICKS).build()
    );

    public static final BlockEntityType<net.stardomga.stardoms_colors.block.entity.BedBlockEntity> BED = register(
            "bed",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(BedBlockEntity::new, ModBlocks.BED).build()
    );

    public static final BlockEntityType<TerracottaBlockEntity> TERRACOTTA = register(
            "terracotta",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(TerracottaBlockEntity::new, ModBlocks.TERRACOTTA).build()
    );

    public static final BlockEntityType<CandleBlockEntity> CANDLE = register(
            "candle",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(CandleBlockEntity::new, ModBlocks.CANDLE).build()
    );

    public static final BlockEntityType<ConcreteSlabBlockEntity> CONCRETE_SLAB = register(
            "concrete_slab",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(ConcreteSlabBlockEntity::new, ModBlocks.CONCRETE_SLAB).build()
    );

    public static final BlockEntityType<BrickSlabBlockEntity> BRICK_SLAB = register(
            "brick_slab",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(BrickSlabBlockEntity::new, ModBlocks.BRICK_SLAB).build()
    );

    public static final BlockEntityType<TerracottaSlabBlockEntity> TERRACOTTA_SLAB = register(
                "terracotta_slab",
                // For versions before 1.21.2, please use BlockEntityType.Builder.
                FabricBlockEntityTypeBuilder.create(TerracottaSlabBlockEntity::new, ModBlocks.TERRACOTTA_SLAB).build()
        );


    public static void initialize() {
    }
}