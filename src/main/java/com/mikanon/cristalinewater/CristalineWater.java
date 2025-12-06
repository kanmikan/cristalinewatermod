package com.mikanon.cristalinewater;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CristalineWater.MODID, version = CristalineWater.VERSION)
public class CristalineWater {
	
	public static final String MODID = "cristalinewater";
	public static final String VERSION = "1.2";

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event){
        Config.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }
	
	@EventHandler
    public void PostInit(FMLPostInitializationEvent event){
    }
	
}
