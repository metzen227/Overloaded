package com.cjm721.overloaded.common.block.basic;

import com.cjm721.overloaded.common.OverloadedCreativeTabs;
import com.cjm721.overloaded.common.block.ModBlock;
import com.cjm721.overloaded.common.block.tile.infinity.TileInfiniteCapacitor;
import com.cjm721.overloaded.common.storage.LongEnergyStack;
import mcjty.lib.tools.ChatTools;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.cjm721.overloaded.Overloaded.MODID;

public class BlockInfiniteCapacitor extends ModBlock implements ITileEntityProvider {

    public BlockInfiniteCapacitor() {
        super(Material.ROCK);

        defaultRegistry();

        setHardness(10);
        setLightOpacity(0);
        setCreativeTab(OverloadedCreativeTabs.ENERGY_BLOCKS);
        register();
        GameRegistry.registerTileEntity(TileInfiniteCapacitor.class, MODID + ":infiniteCapacitor");
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileInfiniteCapacitor();
    }

    @Override
    public void registerRecipe() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(MODID, "infinite_capacitor"), null);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, location);

        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState iBlockState) {
                return location;
            }
        };
        ModelLoader.setCustomStateMapper(this, ignoreState);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if(ItemStackTools.isEmpty(heldItem) && hand == EnumHand.MAIN_HAND) {
                LongEnergyStack stack = ((TileInfiniteCapacitor) worldIn.getTileEntity(pos)).getStorage().status();

                        // TODO Make the exact number show in a tooltip so it can be easier to read at a glance
                double percent = (double) stack.getAmount() / (double) Long.MAX_VALUE;
                ChatTools.addChatMessage(playerIn, new TextComponentString(String.format("Emergy Amount: %,d  %,.4f%%", stack.getAmount(), percent)));
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }
}
