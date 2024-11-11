package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.CristalineWater;
import com.mikanon.cristalinewater.biome.BiomeColors;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid {

    @Accessor("field_149806_a")
    abstract void setField_149806_a(IIcon[] icons);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructor(CallbackInfo info) {
        BlockLiquid blockLiquid = (BlockLiquid) (Object) this;
        blockLiquid.setLightOpacity(0);
    }

    @Inject(method = "registerBlockIcons", at = @At("HEAD"), cancellable = true)
    private void onRegisterBlockIcons(IIconRegister registry, CallbackInfo ci) {
        IIcon[] custom = new IIcon[2];
        Block block = (Block) (Object) this;
        if (block.getMaterial() == Material.water) {
            custom[0] = registry.registerIcon(CristalineWater.MODID + ":water_still");
            custom[1] = registry.registerIcon(CristalineWater.MODID + ":water_flow");
            setField_149806_a(custom);
            ci.cancel();
        }
    }

    @Inject(method = "colorMultiplier", at = @At("HEAD"), cancellable = true)
    private void onColorMultiplier(IBlockAccess world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        Block block = (Block) (Object) this;
        if (block.getMaterial() == Material.water) {
            BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
            cir.setReturnValue(BiomeColors.getColorForBiome(biome));
        }
    }

}

