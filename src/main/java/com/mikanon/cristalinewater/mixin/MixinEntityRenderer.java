package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.biome.BiomeColors;
import com.mikanon.cristalinewater.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    public abstract void updateLightmap(float partialTicks);

    @Shadow
    public abstract float getNightVisionBrightness(EntityPlayer p_getNightVisionBrightness_1_, float p_getNightVisionBrightness_2_);

    @Shadow
    private int[] lightmapColors;

    @Shadow
    private DynamicTexture lightmapTexture;

    @Shadow
    private boolean lightmapUpdateNeeded;

    @Shadow
    private float torchFlickerX;

    @Shadow
    private float bossColorModifier;

    @Shadow
    private float bossColorModifierPrev;

    private float FOG_DENSITY_MIN = 0.025F; //0.03F
    private float FOG_DENSITY_MAX = 0.2F;
    private float FOG_CHANGE_SPEED = 90.0F;

    private final FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(4);

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private void onSetupFog(int pass, float partialTicks, CallbackInfo ci) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player != null) {
            int eyePosX = MathHelper.floor_double(player.posX);
            int eyePosY = MathHelper.floor_double(player.posY + player.getEyeHeight());
            int eyePosZ = MathHelper.floor_double(player.posZ);

            boolean underwaterVolume = Utils.underWaterVolume(player.worldObj, eyePosX, eyePosY, eyePosZ);
            if ((player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) == Blocks.water) || underwaterVolume) {
                int[] averageColor = BiomeColors.averageColorBlend(player.worldObj, eyePosX, eyePosZ, BiomeColors.DEFAULT_FOG_BLEND_RADIUS);

                float fogR = (float) (averageColor[0] / averageColor[3]) / 255.0F;
                float fogG = (float) (averageColor[1] / averageColor[3]) / 255.0F;
                float fogB = (float) (averageColor[2] / averageColor[3]) / 255.0F;

                float brightnessFactor = Utils.getBrightnessFactor(player.worldObj.getWorldTime() % 24000);

                fogR *= brightnessFactor + 0.15F;
                fogG *= brightnessFactor + 0.15F;
                fogB *= brightnessFactor + 0.15F;

                //dynamic fog
                float fogDensity = 0.1F * (1.0F - Math.min(1.0F, player.getEntityData().getFloat("timeUnderwater") / FOG_CHANGE_SPEED));
                double playerY = player.posY + (player.posY - player.lastTickPosY) * partialTicks;
                float depthFactor = (float) Math.max(0.0, 1.0 - (playerY / 32.0));
                fogDensity += depthFactor * 0.1F;

                BiomeGenBase biome = player.worldObj.getBiomeGenForCoords(eyePosX, eyePosZ);
                if (biome != null && biome.biomeName.toLowerCase().contains("swamp")) {
                    fogDensity += 0.05F;
                }
                if (player.isPotionActive(Potion.waterBreathing)) {
                    fogDensity *= 0.7F;
                }
                if (player.isPotionActive(Potion.blindness)) {
                    fogDensity += 0.1F;
                }

                fogDensity = (underwaterVolume && !player.isInWater()) ? FOG_DENSITY_MIN : MathHelper.clamp_float(fogDensity, FOG_DENSITY_MIN, FOG_DENSITY_MAX);

                fogColorBuffer.clear();
                fogColorBuffer.put(new float[]{fogR, fogG, fogB, 1.0F}).flip();
                GL11.glFog(GL11.GL_FOG_COLOR, fogColorBuffer);
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP2);
                GL11.glFogf(GL11.GL_FOG_DENSITY, fogDensity);

                ci.cancel();
            }
        }
    }

    @Inject(method = "updateLightmap", at = @At("HEAD"), cancellable = true)
    private void onUpdateLightmap(float partialTick, CallbackInfo ci) {

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            int eyePosX = MathHelper.floor_double(player.posX);
            int eyePosY = MathHelper.floor_double(player.posY + player.getEyeHeight());
            int eyePosZ = MathHelper.floor_double(player.posZ);

            if ((player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) == Blocks.water) || Utils.underWaterVolume(player.worldObj, eyePosX, eyePosY, eyePosZ)) {
                updateLightmapUnderwater(partialTick);
                ci.cancel();
            }
        }

    }

    private void updateLightmapUnderwater(float p_updateLightmap_1_) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (world != null) {
            for (int i = 0; i < 256; i++) {

                float sun = world.getSunBrightness(1.0F) * 0.95F + 0.05F;
                float f2 = world.provider.lightBrightnessTable[i / 16] * sun;
                float f3 = world.provider.lightBrightnessTable[i % 16] * (torchFlickerX * 0.1F + 1.5F);

                if (world.lastLightningBolt > 0) {
                    f2 = world.provider.lightBrightnessTable[i / 16]; // rayo
                }

                sun = sun * 0.2F; // factor de 20% de la luz solar
                f2 = f2 + sun; //sumado a todos los bloques
                f3 = f3 * 1.0F; //y mayor intensidad de las lamparas bajo el agua

                float sunA = world.getSunBrightness(1.0F) * 0.65F + 0.35F;
                float f3A = f3 * 0.6F + 0.4F;

                float f8 = (f2 * sunA + f3) * 0.96F + 0.03F;
                float f9 = (f2 * sunA + f3 * (f3A * 0.6F + 0.4F)) * 0.96F + 0.03F;
                float f10 = (f2 + f3 * (f3 * 0.6F + 0.4F)) * 0.96F + 0.03F;

                //boss color
                if (bossColorModifier > 0.0F) {
                    float bossColorInterp = bossColorModifierPrev + (bossColorModifier - bossColorModifierPrev) * p_updateLightmap_1_;
                    f8 = f8 * (1.0F - bossColorInterp) + f8 * 0.7F * bossColorInterp;
                    f9 = f9 * (1.0F - bossColorInterp) + f9 * 0.6F * bossColorInterp;
                    f10 = f10 * (1.0F - bossColorInterp) + f10 * 0.6F * bossColorInterp;
                }

                //end
                if (world.provider.dimensionId == 1) {
                    f8 = 0.22F + f3 * 0.75F;
                    f9 = 0.28F + f3 * (f3A * 0.6F + 0.4F) * 0.75F;
                    f10 = 0.25F + f3 * (f3 * 0.6F + 0.4F) * 0.75F;
                }

                //pocion de vision nocturna
                if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.nightVision)) {
                    float nightVisionBrightness = getNightVisionBrightness(Minecraft.getMinecraft().thePlayer, p_updateLightmap_1_);
                    float f12 = 1.0F / Math.min(f8, Math.min(f9, f10));
                    f8 = f8 * (1.0F - nightVisionBrightness) + f8 * f12 * nightVisionBrightness;
                    f9 = f9 * (1.0F - nightVisionBrightness) + f9 * f12 * nightVisionBrightness;
                    f10 = f10 * (1.0F - nightVisionBrightness) + f10 * f12 * nightVisionBrightness;
                }

                f8 = Math.max(0.0F, Math.min(1.0F, f8));
                f9 = Math.max(0.0F, Math.min(1.0F, f9));
                f10 = Math.max(0.0F, Math.min(1.0F, f10));

                //gamma
                float gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
                f8 = f8 * (1.0F - gamma) + (1.0F - (float) Math.pow(1.0F - f8, 4)) * gamma;
                f9 = f9 * (1.0F - gamma) + (1.0F - (float) Math.pow(1.0F - f9, 4)) * gamma;
                f10 = f10 * (1.0F - gamma) + (1.0F - (float) Math.pow(1.0F - f10, 4)) * gamma;

                f8 = Math.max(0.0F, Math.min(1.0F, (f8 * 0.96F + 0.03F)));
                f9 = Math.max(0.0F, Math.min(1.0F, (f9 * 0.96F + 0.03F)));
                f10 = Math.max(0.0F, Math.min(1.0F, (f10 * 0.96F + 0.03F)));

                int r = (int) (f8 * 255F);
                int g = (int) (f9 * 255F);
                int b = (int) (f10 * 255F);
                lightmapColors[i] = (255 << 24) | (r << 16) | (g << 8) | b;
            }

            lightmapTexture.updateDynamicTexture();
            lightmapUpdateNeeded = false;
        }
    }

}

