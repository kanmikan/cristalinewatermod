package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.CristalineWater;
import com.mikanon.cristalinewater.biome.BiomeColors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public abstract class MixinClientBlockLiquid {

    @Accessor("field_149806_a")
    abstract void setField_149806_a(IIcon[] icons);

    @Inject(method = "colorMultiplier", at = @At("HEAD"), cancellable = true)
    private void onColorMultiplier(IBlockAccess world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        Block block = (Block) (Object) this;
        if (block.getMaterial() == Material.water) {

            int[] average = BiomeColors.averageColorBlend(world, x, z, BiomeColors.DEFAULT_BIOME_BLEND_RADIUS);
            int result = ((average[0] / average[3]) << 16) | ((average[1] / average[3]) << 8) | (average[2] / average[3]);
            cir.setReturnValue(result);

        }
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void onShouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        Block adjacentBlock = world.getBlock(x, y, z);
        if (adjacentBlock.getMaterial() == Material.glass) {
            //hacer transparente el agua que toca el cristal, se ve bien dependiendo del caso.
            cir.setReturnValue(false);
        }
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

}
