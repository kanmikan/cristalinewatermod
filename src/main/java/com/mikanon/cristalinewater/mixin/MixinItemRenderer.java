package com.mikanon.cristalinewater.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(at = @At("HEAD"), method = "renderWarpedTextureOverlay", cancellable = true)
    private void onRenderWarpedTextureOverlay(float p_78448_1_, CallbackInfo ci) {

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null /*&& player.isInWater()*/) {
            ci.cancel();
        }

    }

}
