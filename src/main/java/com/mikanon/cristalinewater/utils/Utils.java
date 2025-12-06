package com.mikanon.cristalinewater.utils;

import com.mikanon.cristalinewater.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class Utils {

    //private static final int VOLUME_CHECK = 10; //rango de deteccion
    //private static final int VOLUME_MINIMAL = 4; //minimo de 4 bloques de agua sobre el jugador

    //si el jugador se encuentra debajo de un volumen de agua, pero sin estar dentro del agua
    public static boolean underWaterVolume(World world, int eyePosX, int eyePosY, int eyePosZ) {
        if (!Config.UNDERWATER_AIR_POCKETS) return false;
        int count = 0;
        for (int y = eyePosY + 1; y <= eyePosY + Config.UNDERWATER_AIR_VOLUME_CHECK_AREA; y++) {
            if (world.getBlock(eyePosX, y, eyePosZ) == Blocks.water) {
                count++;
            }
        }
        return count >= Config.UNDERWATER_AIR_VOLUME_MIN_AREA;
    }

    public static boolean isWaterPlant(World world, int x, int y, int z) {

        if (!(world.getBlock(x, y, z) instanceof BlockBush)) return false;

        int adjacent = 0;

        //4 lados
        if (world.getBlock(x + 1, y, z) == Blocks.water) adjacent++;
        if (world.getBlock(x - 1, y, z) == Blocks.water) adjacent++;
        if (world.getBlock(x, y, z + 1) == Blocks.water) adjacent++;
        if (world.getBlock(x, y, z - 1) == Blocks.water) adjacent++;

        boolean waterAbove = false;
        boolean waterBelow = false;

        //y+
        for (int i = 1; i <= 4; i++) {
            Block b = world.getBlock(x, y + i, z);
            if (b == Blocks.water) {
                waterAbove = true;
                break;
            }
            if (!(b instanceof BlockBush)) break;
        }

        //y-
        for (int i = 1; i <= 2; i++) {
            Block b = world.getBlock(x, y - i, z);
            if (b == Blocks.water) {
                waterBelow = true;
                break;
            }
            if (!(b instanceof BlockBush) && b != Blocks.water) break;
        }

        return adjacent >= 3 || (adjacent >= 1 && (waterAbove || waterBelow));

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
