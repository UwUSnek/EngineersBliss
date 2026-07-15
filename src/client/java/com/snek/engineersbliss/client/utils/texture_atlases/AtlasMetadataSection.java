package com.snek.engineersbliss.client.utils.texture_atlases;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.packs.metadata.MetadataSectionType;




public record AtlasMetadataSection(
    int atlasCols,  int atlasRows,
    int sheetWidth, int sheetHeight,
    int frameWidth, int frameHeight,
    int frameCount, int fps
) {
    public static final MetadataSectionType<AtlasMetadataSection> TYPE =
        new MetadataSectionType<>("engineers-bliss.atlas", RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("atlas_cols"  ).forGetter(AtlasMetadataSection::atlasCols),
            Codec.INT.fieldOf("atlas_rows"  ).forGetter(AtlasMetadataSection::atlasRows),
            Codec.INT.fieldOf("sheet_width" ).forGetter(AtlasMetadataSection::sheetWidth),
            Codec.INT.fieldOf("sheet_height").forGetter(AtlasMetadataSection::sheetHeight),
            Codec.INT.fieldOf("frame_width" ).forGetter(AtlasMetadataSection::frameWidth),
            Codec.INT.fieldOf("frame_height").forGetter(AtlasMetadataSection::frameHeight),
            Codec.INT.fieldOf("frame_count" ).forGetter(AtlasMetadataSection::frameCount),
            Codec.INT.fieldOf("fps"         ).forGetter(AtlasMetadataSection::fps)
        ).apply(inst, AtlasMetadataSection::new)));
}