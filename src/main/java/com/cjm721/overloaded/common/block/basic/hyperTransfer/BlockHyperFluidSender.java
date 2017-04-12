package com.cjm721.overloaded.common.block.basic.hyperTransfer;

import com.cjm721.overloaded.common.OverloadedCreativeTabs;
import com.cjm721.overloaded.common.block.basic.hyperTransfer.base.AbstractBlockHyperSender;
import com.cjm721.overloaded.common.block.tile.hyperTransfer.TileHyperFluidSender;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.cjm721.overloaded.Overloaded.MODID;

public class BlockHyperFluidSender extends AbstractBlockHyperSender {


    public BlockHyperFluidSender() {
        super(Material.ROCK);

        setHardness(10);
        setLightOpacity(0);
        setCreativeTab(OverloadedCreativeTabs.UTILITY);
        register();

        GameRegistry.registerTileEntity(TileHyperFluidSender.class, MODID + ":hyperFluidSender");
    }

    @NotNull
    @Override
    public String getType() {
        return "Fluid";
    }

    @Override
    public void registerRecipe() {

    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileHyperFluidSender();
    }
}