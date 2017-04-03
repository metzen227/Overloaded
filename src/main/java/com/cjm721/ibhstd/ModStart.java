package com.cjm721.ibhstd;

import com.cjm721.ibhstd.common.block.ModBlocks;
import com.cjm721.ibhstd.common.block.compressed.BakedModelLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CJ on 4/2/2017.
 */
@Mod(modid = ModStart.MODID, version = ModStart.VERSION)
public class ModStart {

    public static final String MODID = "ibhstd";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Loader.isModLoaded("botania");

        ModBlocks.init();

        ModelLoaderRegistry.registerLoader(new BakedModelLoader());
        ModBlocks.registerModels();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModBlocks.addRecipes();
    }
}