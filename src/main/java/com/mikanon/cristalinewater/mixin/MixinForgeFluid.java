package com.mikanon.cristalinewater.mixin;

import com.mikanon.cristalinewater.Config;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Fluid.class)
public abstract class MixinForgeFluid {

    @Inject(method = "getColor(Lnet/minecraftforge/fluids/FluidStack;)I", at = @At("HEAD"), cancellable = true, remap = false)
    private void onGetColor(FluidStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack != null && (Object) this == FluidRegistry.WATER) {
            cir.setReturnValue(Config.DEFAULT_WATER);
        }
    }

}
