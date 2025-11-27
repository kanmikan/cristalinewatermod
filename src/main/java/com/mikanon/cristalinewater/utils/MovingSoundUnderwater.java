package com.mikanon.cristalinewater.utils;

import com.mikanon.cristalinewater.CristalineWater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.BlockBush;

@SideOnly(Side.CLIENT)
public class MovingSoundUnderwater extends MovingSound {

    private final EntityPlayer player;
    private boolean inWater, inPlant;

    public MovingSoundUnderwater(EntityPlayer player) {
        super(new ResourceLocation(CristalineWater.MODID, "underwater.ambience"));
        this.player = player;
        this.repeat = true;
        this.field_147665_h = 10;
        this.field_147666_i = ISound.AttenuationType.NONE;
        this.volume = 0.6F;
    }

    @Override
    public void update() {
        if (player == null || player.isDead) {
            this.donePlaying = true;
            return;
        }

        int eyePosX = MathHelper.floor_double(player.posX);
        int eyePosY = MathHelper.floor_double(player.posY + player.getEyeHeight());
        int eyePosZ = MathHelper.floor_double(player.posZ);

        inWater = (player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) == Blocks.water);
		inPlant = (player.worldObj.getBlock(eyePosX, eyePosY, eyePosZ) instanceof BlockBush);
		

        if ((!inWater && !inPlant) || player.isDead) {
            this.donePlaying = true;
        }
    }
}
