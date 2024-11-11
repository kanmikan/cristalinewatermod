package com.mikanon.cristalinewater.biome;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeColors {

    private static int defaultColor = 0x3F76E4;

    public static int getColorForBiome(BiomeGenBase biome) {
        int biomeColor;

        if (biome == BiomeGenBase.ocean) {
            biomeColor = 0x3F76E4;
        } else if (biome == BiomeGenBase.frozenOcean) {
            biomeColor = 0x4F98B1;
        } else if (biome == BiomeGenBase.swampland) {
            biomeColor = 0x3E8000;
        } else if (biome == BiomeGenBase.river) {
            biomeColor = 0x2D5E77;
        } else {
            biomeColor = 0x2D5E77; //4159204;
        }

        return blend(defaultColor, biomeColor, 0.3f);
    }

    private static int blend(int color1, int color2, float ratio) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int) (r1 * (1 - ratio) + r2 * ratio);
        int g = (int) (g1 * (1 - ratio) + g2 * ratio);
        int b = (int) (b1 * (1 - ratio) + b2 * ratio);

        return (r << 16) | (g << 8) | b;
    }

    public static float[] color2FogTint(int color, float factor) {
        float r = ((color >> 16) & 0xFF) / 255.0f * factor;
        float g = ((color >> 8) & 0xFF) / 255.0f * factor;
        float b = (color & 0xFF) / 255.0f * factor;
        return new float[]{r, g, b};
    }

}
