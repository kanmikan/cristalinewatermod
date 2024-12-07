package com.mikanon.cristalinewater.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface MixinBlockAccessor {
    @Accessor("useNeighborBrightness")
    void setUseNeighborBrightness(boolean useNeighborBrightness);
}