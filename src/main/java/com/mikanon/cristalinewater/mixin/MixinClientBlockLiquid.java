package com.mikanon.cristalinewater.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinClientBlockLiquid {

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void onShouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        Block adjacentBlock = world.getBlock(x, y, z);
        if (adjacentBlock.getMaterial() == Material.glass) {
            //hacer transparente el agua que toca el cristal, se ve bien dependiendo del caso.
            cir.setReturnValue(false);
        }
    }

}
