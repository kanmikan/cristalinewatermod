package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.Config;
import com.mikanon.cristalinewater.CristalineWater;
import com.mikanon.cristalinewater.utils.MovingSoundUnderwater;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    private boolean wasInWater = false;
    private MovingSoundUnderwater underwaterSound = new MovingSoundUnderwater(Minecraft.getMinecraft().thePlayer);

    @Inject(method = "handleWaterMovement", at = @At("HEAD"))
    public void onHandleWaterMovement(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof EntityPlayer && !entity.isRiding() && Config.UNDERWATER_SOUNDS) {

            EntityPlayer player = (EntityPlayer) entity;

            int eyePosX = MathHelper.floor_double(player.posX);
            int eyePosY = MathHelper.floor_double(player.posY + player.getEyeHeight());
            int eyePosZ = MathHelper.floor_double(player.posZ);

            boolean isInWater = (player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) == Blocks.water);
            boolean isInAir = (player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) == Blocks.air);

            //al entrar
            if (isInWater && !wasInWater) {
                entity.playSound(CristalineWater.MODID + ":water.enter", 1.0F, 1.0F);
                wasInWater = true;
                if (underwaterSound.isDonePlaying()) {
                    try {
                        Minecraft.getMinecraft().getSoundHandler().playSound(underwaterSound);
                    } catch (Exception e) {
                        e.printStackTrace(); //no debería suceder mas.
                    }
                }
            }

            //al salir
            if (!isInWater && isInAir && wasInWater) {
                entity.playSound(CristalineWater.MODID + ":water.exit", 1.0F, 1.0F);
                wasInWater = false;
            }

            //bajo el agua
            if (isInWater && wasInWater) {
                if (player.worldObj.rand.nextInt(50) == 0) {
                    entity.playSound(CristalineWater.MODID + ":underwater.bubbles", 1.0F, 1.0F);
                }
            }

        }

    }

}
