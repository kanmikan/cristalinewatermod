package com.mikanon.cristalinewater.biome;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeColors {

    private static final int DEFAULT_WATER = 0x3F76E4;
    public static final int DEFAULT_BIOME_BLEND_RADIUS = 2;

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

        return blend(DEFAULT_WATER, biomeColor, 0.6f);
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

    public static int[] averageColorBlend(IBlockAccess world, int x, int z, int radio) {
        int r = 0, g = 0, b = 0;

        int count = 0;
        for (int dx = -radio; dx <= radio; dx++) {
            for (int dz = -radio; dz <= radio; dz++) {
                BiomeGenBase biome = world.getBiomeGenForCoords(x + dx, z + dz);
                int color = BiomeColors.getColorForBiome(biome);
                r += (color >> 16) & 0xFF;
                g += (color >> 8) & 0xFF;
                b += color & 0xFF;
                count++;
            }
        }

        return new int[]{r, g, b, count};
    }

}
