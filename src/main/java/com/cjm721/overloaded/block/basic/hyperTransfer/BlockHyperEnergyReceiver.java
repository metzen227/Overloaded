package com.cjm721.overloaded.block.basic.hyperTransfer;

import com.cjm721.overloaded.block.basic.hyperTransfer.base.AbstractBlockHyperReceiver;
import com.cjm721.overloaded.block.tile.hyperTransfer.TileHyperEnergyReceiver;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

import static com.cjm721.overloaded.Overloaded.MODID;

public class BlockHyperEnergyReceiver extends AbstractBlockHyperReceiver {

    public BlockHyperEnergyReceiver() {
        super(Material.ROCK);
    }

    @Override
    public void baseInit() {
        setRegistryName("hyper_energy_receiver");
        setUnlocalizedName("hyper_energy_receiver");

        GameRegistry.registerTileEntity(TileHyperEnergyReceiver.class, MODID + ":hyper_energy_receiver");
    }

    @Override
    @Nonnull
    protected String getType() {
        return "Energy";
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileHyperEnergyReceiver();
    }

}
