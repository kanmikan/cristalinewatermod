package com.mikanon.cristalinewater;

import com.mikanon.cristalinewater.biome.BiomeColors;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static Configuration config;

    public static int DEFAULT_WATER = 0x3F76E4;
    public static int DEFAULT_BIOME_BLEND_RADIUS = 1;
    public static int DEFAULT_FOG_BLEND_RADIUS = 2;

    public static boolean GRAYSCALE_WATER = true;
    public static boolean TRANSPARENT_WATER_SIDES = true;
    public static boolean UNDERWATER_SOUNDS = true;
    public static boolean TINT_CAULDRON_WATER = true;

    public static boolean UNDERWATER_AIR_POCKETS = true;
    public static int UNDERWATER_AIR_VOLUME_CHECK_AREA = 10;
    public static int UNDERWATER_AIR_VOLUME_MIN_AREA = 4;

    public static Map<String, Integer> BIOME_COLORS = new HashMap<>();

    public static void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    public static void syncConfig() {
        config.load();

        GRAYSCALE_WATER = config.get("graphics", "GrayscaleWater", true, "If true, a grayscale texture will be used instead of the vanilla one.").getBoolean();
        TRANSPARENT_WATER_SIDES = config.get("graphics", "TransparentWaterSides", true, "If true, water touching glass will not render its side.").getBoolean();
        UNDERWATER_SOUNDS = config.get("graphics", "UnderwaterSounds", true, "If true, it uses underwater sound effects.").getBoolean();
        TINT_CAULDRON_WATER = config.get("graphics", "TintCauldronWater", true, "If true, cauldron water will be tinted based on the biome").getBoolean();

        DEFAULT_WATER = config.get("graphics", "BaseWaterColor", DEFAULT_WATER, "Default water color").getInt();
        DEFAULT_BIOME_BLEND_RADIUS = config.get("graphics", "BiomeBlendRadius", DEFAULT_BIOME_BLEND_RADIUS, "Water biome blending radius (in blocks), bigger value = smoother and slower").getInt();
        DEFAULT_FOG_BLEND_RADIUS = config.get("graphics", "FogBlendRadius", DEFAULT_FOG_BLEND_RADIUS, "The blending radius of the fog transition between biomes").getInt();

        UNDERWATER_AIR_POCKETS = config.get("graphics", "UnderwaterAirPockets", true, "If true, glass structures underwater under certain conditions, should render the custom fog and lighting").getBoolean();
        UNDERWATER_AIR_VOLUME_CHECK_AREA = config.get("graphics", "UnderwaterAirVolumeCheckArea", UNDERWATER_AIR_VOLUME_CHECK_AREA, "The Y distance check for any water block above the air pocket").getInt();
        UNDERWATER_AIR_VOLUME_MIN_AREA = config.get("graphics", "UnderwaterAirVolumeMinArea", UNDERWATER_AIR_VOLUME_MIN_AREA, "The minimal number of water blocks that should be detected above the air pocket to enable the underwater fog and lighting").getInt();

        Property biomeProp = config.get("biomeColors", "BiomeColorOverrides", BiomeColors.DEFAULT_BIOME_COLORS, "Format: biomeName:hexColor");
        parseBiomeColorMap(biomeProp.getStringList());

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void parseBiomeColorMap(String[] entries) {
        BIOME_COLORS.clear();
        for (String entry : entries) {
            if (!entry.contains(":")) continue;

            String[] split = entry.split(":");
            String biomeName = split[0].trim();
            String hex = split[1].trim();

            try {
                int color = Integer.parseInt(hex, 16);
                BIOME_COLORS.put(biomeName, color);
            } catch (Exception ignored) {}
        }
    }

    public static void reload() {
        syncConfig();
    }
}

