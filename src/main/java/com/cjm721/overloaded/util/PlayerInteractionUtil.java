package com.cjm721.overloaded.util;

import com.cjm721.overloaded.Overloaded;
import com.cjm721.overloaded.config.OverloadedConfig;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class PlayerInteractionUtil {

  public static boolean tryHarvestBlock(ServerPlayerEntity player, World world, BlockPos pos) {
    int exp =
        net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(
            world, player.interactionManager.getGameType(), player, pos);
    if (exp == -1) {
      return false;
    } else {
      BlockState iblockstate = world.getBlockState(pos);
      TileEntity tileentity = world.getTileEntity(pos);
      Block block = iblockstate.getBlock();

      if ((block instanceof CommandBlockBlock || block instanceof StructureBlock)
          && !player.canUseCommandBlock()) {
        world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
        return false;
      } else {
        world.playEvent(null, 2001, pos, Block.getStateId(iblockstate));
        boolean flag1;

        if (player.abilities.isCreativeMode) {
          flag1 = removeBlock(world, pos, player, false);
          player.connection.sendPacket(new SChangeBlockPacket(world, pos));
        } else {
          ItemStack itemstack1 = player.getHeldItemMainhand();
          ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy();
          boolean flag = iblockstate.canHarvestBlock(world, pos, player);

          itemstack1.onBlockDestroyed(world, iblockstate, pos, player);

          flag1 = removeBlock(world, pos, player, flag);
          if (flag1 && flag) {
            iblockstate
                .getBlock()
                .harvestBlock(world, player, pos, iblockstate, tileentity, itemstack2);
          }
        }

        // Drop experience
        if (!player.isCreative() && flag1 && exp > 0) {
          iblockstate.getBlock().dropXpOnBlockBreak(world, player.getPosition(), exp);
        }
        return flag1;
      }
    }
  }

  private static boolean removeBlock(
      World world, BlockPos pos, PlayerEntity player, boolean canHarvest) {
    BlockState iblockstate = world.getBlockState(pos);
    boolean flag =
        iblockstate.getBlock().removedByPlayer(iblockstate, world, pos, player, canHarvest, iblockstate.getFluidState());

    if (flag) {
      iblockstate.getBlock().onPlayerDestroy(world, pos, iblockstate);
    }

    return flag;
  }

  @Nonnull
  public static BlockPlaceResult placeBlock(
      @Nonnull ItemStack searchStack,
      @Nonnull ServerPlayerEntity player,
      @Nonnull World worldIn,
      @Nonnull BlockPos newPosition,
      @Nonnull Direction facing,
      @Nonnull IEnergyStorage energy,
      float hitX,
      float hitY,
      float hitZ) {

    // Can we place a block at this Pos
    BlockItem itemBlock = ((BlockItem) searchStack.getItem());
//    if (worldIn.func_217400_a(newPosition, player)) {
//      return BlockPlaceResult.FAIL_DENY;
//    }

    BlockSnapshot blockSnapshot =
        new BlockSnapshot(worldIn, newPosition, worldIn.getBlockState(newPosition));
    BlockState placedAgainst =
        blockSnapshot.getWorld().getBlockState(blockSnapshot.getPos().offset(facing.getOpposite()));
    BlockEvent.EntityPlaceEvent event =
        new BlockEvent.EntityPlaceEvent(blockSnapshot, placedAgainst, player);
    MinecraftForge.EVENT_BUS.post(event);

    if (event.isCanceled()) {
      return BlockPlaceResult.FAIL_DENY;
    }

    long distance = Math.round(Math.sqrt(player.getPosition().distanceSq(newPosition)));

    long cost =
        OverloadedConfig.INSTANCE.multiToolConfig.placeBaseCost
            + OverloadedConfig.INSTANCE.multiToolConfig.costPerMeterAway * distance;
    if (!player.abilities.isCreativeMode
        && (cost > Integer.MAX_VALUE || cost < 0 || energy.getEnergyStored() < cost))
      return BlockPlaceResult.FAIL_ENERGY;

    LazyOptional<IItemHandler> opInventory =
        player.getCapability(ITEM_HANDLER_CAPABILITY, Direction.UP);
    if (!opInventory.isPresent()) {
      Overloaded.logger.warn("Player has no ItemHandler Capability? NBT: " + player.serializeNBT());
      return BlockPlaceResult.FAIL_PREREQUISITE;
    }
    IItemHandler inventory =
        opInventory.orElseThrow(() -> new RuntimeException("Impossible Condition"));

    int foundStackSlot = findItemStackSlot(searchStack, inventory);
    if (foundStackSlot == -1) {
      return BlockPlaceResult.FAIL_PREREQUISITE;
    }
    ItemStack foundStack =
        inventory.extractItem(foundStackSlot, 1, player.abilities.isCreativeMode);

    BlockItemUseContext context =
        new BlockItemUseContextPublic(
            worldIn,
            player,
            Hand.MAIN_HAND,
            foundStack,
            new BlockRayTraceResult(
                new Vec3d(
                    hitX + newPosition.getX(),
                    hitY + newPosition.getY(),
                    hitZ + newPosition.getZ()),
                facing,
                newPosition,
                false));

    ActionResultType result = ForgeHooks.onPlaceItemIntoWorld(context);

    switch (result) {
      case SUCCESS:
        SoundType soundtype =
            worldIn
                .getBlockState(newPosition)
                .getBlock()
                .getSoundType(worldIn.getBlockState(newPosition), worldIn, newPosition, player);
        worldIn.playSound(
            null,
            newPosition,
            soundtype.getPlaceSound(),
            SoundCategory.BLOCKS,
            (soundtype.getVolume() + 1.0F) / 2.0F,
            soundtype.getPitch() * 0.8F);
        if (!player.abilities.isCreativeMode) {
          energy.extractEnergy((int) cost, false);
        }
        return BlockPlaceResult.SUCCESS;
      case PASS:
      case FAIL:
      default:
        inventory.insertItem(foundStackSlot, foundStack, player.abilities.isCreativeMode);
        return BlockPlaceResult.FAIL_DENY;
    }
  }

  private static int findItemStackSlot(@Nonnull ItemStack item, @Nonnull IItemHandler inventory) {
    int size = inventory.getSlots();
    for (int i = 0; i < size; i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if (!stack.isEmpty() && stack.isItemEqual(item)) {
        return i;
      }
    }

    return -1;
  }

  @Nonnull
  @OnlyIn(Dist.CLIENT)
  public static BlockRayTraceResult getBlockPlayerLookingAtClient(
      PlayerEntity player, float partialTicks) {
    return player
        .getEntityWorld()
        .rayTraceBlocks(
            new RayTraceContext(
                player.getEyePosition(partialTicks),
                player
                    .getLook(partialTicks)
                    .scale(OverloadedConfig.INSTANCE.multiToolConfig.reach)
                    .add(player.getEyePosition(partialTicks)),
                RayTraceContext.BlockMode.COLLIDER,
                RayTraceContext.FluidMode.NONE,
                player));
  }
}
