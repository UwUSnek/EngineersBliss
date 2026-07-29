package com.snek.engineersbliss.client.utils.textures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.packs.metadata.MetadataSectionType;




public record SvgMetadataSection(
    int width,  // Target pixel size at 1x gui scale
    int height
) {
    public static final MetadataSectionType<SvgMetadataSection> TYPE =
        new MetadataSectionType<>("engineers-bliss.svg", RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("width").forGetter(SvgMetadataSection::width),
            Codec.INT.fieldOf("height").forGetter(SvgMetadataSection::height)
        ).apply(inst, SvgMetadataSection::new)))
    ;
}