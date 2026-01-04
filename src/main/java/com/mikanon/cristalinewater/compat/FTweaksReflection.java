package com.mikanon.cristalinewater.compat;

import cpw.mods.fml.common.Loader;
import net.minecraft.client.renderer.Tessellator;

import java.lang.reflect.Method;

public final class FTweaksReflection {

    public static final boolean LOADED = Loader.isModLoaded("falsetweaks");

    private static Method getThreadTessellator;
    private static Method isEnabled;

    static {
        if (LOADED) {
            try {
                Class<?> cls = Class.forName("com.falsepattern.falsetweaks.api.ThreadedChunkUpdates");
                getThreadTessellator = cls.getMethod("getThreadTessellator");
                isEnabled = cls.getMethod("isEnabled");
            } catch (Throwable t) {
                getThreadTessellator = null;
                isEnabled = null;
            }
        }
    }

    public static boolean canUse() {
        if (!LOADED || isEnabled == null) return false;
        try {
            return (Boolean) isEnabled.invoke(null);
        } catch (Throwable t) {
            return false;
        }
    }

    public static Tessellator getThreadTessellator() {
        try {
            return (Tessellator) getThreadTessellator.invoke(null);
        } catch (Throwable t) {
            return Tessellator.instance;
        }
    }
}
