package com.mikanon.cristalinewater.utils;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class Utils {

    private static final int VOLUME_CHECK = 10; //rango de deteccion
    private static final int VOLUME_MINIMAL = 4; //minimo de 4 bloques de agua sobre el jugador

    //si el jugador se encuentra debajo de un volumen de agua, pero sin estar dentro del agua
    public static boolean underWaterVolume(World world, int eyePosX, int eyePosY, int eyePosZ) {
        int count = 0;
        for (int y = eyePosY + 1; y <= eyePosY + VOLUME_CHECK; y++) {
            if (world.getBlock(eyePosX, y, eyePosZ) == Blocks.water) {
                count++;
            }
        }
        return count >= VOLUME_MINIMAL;
    }

    public static float getBrightnessFactor(float time) {
        if (time < 6000) {
            return 0.3F + (time / 12000.0F);
        } else if (time < 18000) {
            return 1.0F - ((time - 6000.0F) / 12000.0F);
        } else {
            return 0.5F * ((time - 18000.0F) / 12000.0F);
        }
    }

}
