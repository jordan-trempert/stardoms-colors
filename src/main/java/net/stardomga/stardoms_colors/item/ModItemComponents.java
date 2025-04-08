package net.stardomga.stardoms_colors.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ModItemComponents {

    public static final Codec<BLOCK_COLOR> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("color").forGetter(BLOCK_COLOR::color)
        ).apply(builder, BLOCK_COLOR::new);
    });

    static public record BLOCK_COLOR(int color) {
    }
}
