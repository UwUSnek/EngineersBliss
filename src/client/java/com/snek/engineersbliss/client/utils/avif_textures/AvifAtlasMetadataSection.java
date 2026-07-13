package com.snek.engineersbliss.client.utils.avif_textures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.packs.metadata.MetadataSectionType;




public record AvifAtlasMetadataSection(
    int atlasCols,  int atlasRows,
    int sheetWidth, int sheetHeight,
    int frameWidth, int frameHeight,
    int frameCount, int fps
) {
    public static final MetadataSectionType<AvifAtlasMetadataSection> TYPE =
        new MetadataSectionType<>("avif_atlas", RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("atlas_cols"  ).forGetter(AvifAtlasMetadataSection::atlasCols),
            Codec.INT.fieldOf("atlas_rows"  ).forGetter(AvifAtlasMetadataSection::atlasRows),
            Codec.INT.fieldOf("sheet_width" ).forGetter(AvifAtlasMetadataSection::sheetWidth),
            Codec.INT.fieldOf("sheet_height").forGetter(AvifAtlasMetadataSection::sheetHeight),
            Codec.INT.fieldOf("frame_width" ).forGetter(AvifAtlasMetadataSection::frameWidth),
            Codec.INT.fieldOf("frame_height").forGetter(AvifAtlasMetadataSection::frameHeight),
            Codec.INT.fieldOf("frame_count" ).forGetter(AvifAtlasMetadataSection::frameCount),
            Codec.INT.fieldOf("fps"         ).forGetter(AvifAtlasMetadataSection::fps)
        ).apply(inst, AvifAtlasMetadataSection::new)));
}