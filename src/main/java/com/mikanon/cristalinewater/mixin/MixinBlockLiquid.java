package com.mikanon.cristalinewater.mixin;

import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructor(CallbackInfo info) {
        BlockLiquid blockLiquid = (BlockLiquid) (Object) this;
        blockLiquid.setLightOpacity(0);
        //((MixinBlockAccessor) blockLiquid).setUseNeighborBrightness(true);
    }

}

