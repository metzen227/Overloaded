package com.cjm721.overloaded.client.render.entity;

import codechicken.lib.texture.TextureUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.cjm721.overloaded.Overloaded.MODID;

@SideOnly(Side.CLIENT)
public class ArmorModelHelper implements TextureUtils.IIconRegister {

    @Override
    public void registerIcons(TextureMap textureMap) {
        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_helmet"));

        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_body"));
        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_right_arm"));
        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_left_arm"));

        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_belt"));
        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_right_leg"));
        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_left_leg"));

        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_right_boot"));
        textureMap.registerSprite(new ResourceLocation(MODID,"armors/custom_left_boot"));
    }
}