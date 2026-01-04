package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.Config;
import com.mikanon.cristalinewater.biome.BiomeColors;
import com.mikanon.cristalinewater.compat.FTweaksReflection;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class MixinRenderBlocks {

    @Shadow
    public IBlockAccess blockAccess;

    /*
    @Redirect(method = "renderBlockCauldron", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockCauldron;colorMultiplier(Lnet/minecraft/world/IBlockAccess;III)I"))
    private int onCauldronColorMultiplier(BlockCauldron block, IBlockAccess world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == 0) return block.colorMultiplier(world, x, y, z);
        //TODO this is assuming the cauldron can only have normal water, not modded liquids.
        int[] average = BiomeColors.averageColorBlend(world, x, z, Config.DEFAULT_BIOME_BLEND_RADIUS);
        return ((average[0] / average[3]) << 16) | ((average[1] / average[3]) << 8) | (average[2] / average[3]);
    }
    */

    @Inject(method = "renderBlockCauldron", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderBlocks;renderFaceYPos(Lnet/minecraft/block/Block;DDDLnet/minecraft/util/IIcon;)V", shift = At.Shift.BEFORE),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockLiquid;getLiquidIcon(Ljava/lang/String;)Lnet/minecraft/util/IIcon;")))
    private void cw_onBeforeCauldronWater(BlockCauldron block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        int meta = this.blockAccess.getBlockMetadata(x, y, z);
        if (meta <= 0) return;
        if (Config.TINT_CAULDRON_WATER) {
            int[] avg = BiomeColors.averageColorBlend(this.blockAccess, x, z, Config.DEFAULT_BIOME_BLEND_RADIUS);
            setColorOpaque((avg[0] / avg[3]), (avg[1] / avg[3]), (avg[2] / avg[3]));
        } else {
            int def = Config.DEFAULT_WATER;
            setColorOpaque((def >> 16) & 0xFF, (def >> 8) & 0xFF, def & 0xFF);
        }
    }

    @Inject(method = "renderBlockCauldron", at = @At("RETURN"))
    private void cw_onAfterCauldronWater(BlockCauldron block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        setColorOpaque(255, 255, 255);
    }

    @Redirect(method = "renderBlockLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;colorMultiplier(Lnet/minecraft/world/IBlockAccess;III)I"))
    private int onBlockLiquidColorMultiplier(Block block, IBlockAccess world, int x, int y, int z) {
        if (block.getMaterial() == Material.water) {
            int[] avg = BiomeColors.averageColorBlend(world, x, z, Config.DEFAULT_BIOME_BLEND_RADIUS);
            return ((avg[0] / avg[3]) << 16) | ((avg[1] / avg[3]) << 8) |  (avg[2] / avg[3]);
        }
        return block.colorMultiplier(world, x, y, z);
    }

    private void setColorOpaque(int r, int g, int b) {
        if (FTweaksReflection.canUse()) {
            FTweaksReflection.getThreadTessellator().setColorOpaque(r, g, b);
        } else {
            Tessellator.instance.setColorOpaque(r, g, b);
        }
    }

}
