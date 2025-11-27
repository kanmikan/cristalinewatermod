package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.Config;
import com.mikanon.cristalinewater.biome.BiomeColors;
import com.mikanon.cristalinewater.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    private final FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(4);

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void onRenderSky(float partialTicks, CallbackInfo ci) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player != null) {

            int eyePosX = MathHelper.floor_double(player.posX);
            int eyePosY = MathHelper.floor_double(player.posY + player.getEyeHeight());
            int eyePosZ = MathHelper.floor_double(player.posZ);

            if ((player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) == Blocks.water) || Utils.underWaterVolume(player.worldObj, eyePosX, eyePosY, eyePosZ)) {

                int[] average = BiomeColors.averageColorBlend(player.worldObj, eyePosX, eyePosZ, Config.DEFAULT_BIOME_BLEND_RADIUS);
                float skyR = (float) (average[0] / average[3]) / 255.0F;
                float skyG = (float) (average[1] / average[3]) / 255.0F;
                float skyB = (float) (average[2] / average[3]) / 255.0F;

                float brightnessFactor = Utils.getBrightnessFactor(player.worldObj.getWorldTime() % 24000);
                skyR *= brightnessFactor + 0.128F;
                skyG *= brightnessFactor + 0.128F;
                skyB *= brightnessFactor + 0.128F;

                //fog del horizonte
                GL11.glClearColor(skyR, skyG, skyB, 1.0F);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                GL11.glEnable(GL11.GL_FOG);
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
                GL11.glFogf(GL11.GL_FOG_START, 10.0F);
                GL11.glFogf(GL11.GL_FOG_END, 100.0F);

                fogColorBuffer.clear();
                fogColorBuffer.put(new float[]{skyR, skyG, skyB, 1.0F}).flip();

                GL11.glFog(GL11.GL_FOG_COLOR, fogColorBuffer);
                GL11.glDisable(GL11.GL_FOG);

                ci.cancel();
            }
        }
    }

}