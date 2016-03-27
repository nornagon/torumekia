package com.torumekia

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.util.{EnumFacing, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import net.minecraftforge.fml.common.registry.GameRegistry


class ItemWheatAndSteel extends Item {
  maxStackSize = 1
  setMaxDamage(64)
  setCreativeTab(CreativeTabs.tabTools)

  override def onItemUse(
    stack: ItemStack,      // The ItemStack the player is using
    player: EntityPlayer,  // The player using the item
    world: World,          // The world in which the block exists
    pos: BlockPos,         // The position of the block the item is being used on
    side: EnumFacing,      // The side of the block being pointed at
    hitX: Float,           // Exact coordinates the mouse is pointing at
    hitY: Float,
    hitZ: Float
  ): Boolean = {
    // If the player is pointing at the top face of a block, and there's
    // nothing above it...
    if (side == EnumFacing.UP && world.isAirBlock(pos.up)) {
      // ... get the Minecraft item called "wheat_seeds" ...
      val wheat_seeds =
        GameRegistry.findItem("minecraft", "wheat_seeds")
          // ... which we know implements IPlantable ...
          .asInstanceOf[Item with IPlantable]
      // ... check if the block the player is pointing at can grow wheat (i.e.
      // it's farmland)
      val block = world.getBlockState(pos).getBlock
      val canGrow = block.canSustainPlant(world, pos, EnumFacing.UP, wheat_seeds)
      if (canGrow) {
        // ... if it is, set the block above the pointed-at block to wheat ...
        world.setBlockState(pos.up, wheat_seeds.getPlant(world, pos))
        // ... and add one point of damage to the wheat & steel.
        stack.damageItem(1, player)
        // The return value of onItemUse indicates whether the use was successful,
        // which is used to determine whether to play the "swing item" character
        // animation.
        return true
      }
    }
    false
  }
}
