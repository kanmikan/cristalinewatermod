package com.mikanon.cristalinewater.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {

    @Inject(method = "onUpdate", at = @At("TAIL"))
    public void onPlayerUpdate(CallbackInfo ci) {
        EntityPlayer player = (EntityPlayer) (Object) this;

        float time = player.getEntityData().getFloat("timeUnderwater");
        if (player.isInsideOfMaterial(Material.water)) {
            time += 1.0F;
        } else {
            time = 0.0F;
        }
        player.getEntityData().setFloat("timeUnderwater", time);
    }

}
