package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.Config;
import com.mikanon.cristalinewater.biome.BiomeColors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderBlocks.class)
public abstract class MixinRenderBlocks {

    @Shadow
    public IBlockAccess blockAccess;

    @Redirect(method = "renderBlockCauldron", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockCauldron;colorMultiplier(Lnet/minecraft/world/IBlockAccess;III)I"))
    private int onCauldronColorMultiplier(BlockCauldron block, IBlockAccess world, int x, int y, int z) {
        //TODO this is assuming the cauldron can only have normal water, not modded liquids.
        int[] average = BiomeColors.averageColorBlend(world, x, z, Config.DEFAULT_BIOME_BLEND_RADIUS);
        return ((average[0] / average[3]) << 16) | ((average[1] / average[3]) << 8) | (average[2] / average[3]);
    }

    @Redirect(method = "renderBlockLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;colorMultiplier(Lnet/minecraft/world/IBlockAccess;III)I"))
    private int onBlockLiquidColorMultiplier(Block block, IBlockAccess world, int x, int y, int z) {
        if (block.getMaterial() == Material.water) {
            int[] avg = BiomeColors.averageColorBlend(world, x, z, Config.DEFAULT_BIOME_BLEND_RADIUS);
            return ((avg[0] / avg[3]) << 16) | ((avg[1] / avg[3]) << 8) |  (avg[2] / avg[3]);
        }
        return block.colorMultiplier(world, x, y, z);
    }

}
