package com.torumekia

import net.minecraft.entity.Entity
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.world.World


class ItemSpore extends Item {
  override def hasCustomEntity(stack: ItemStack): Boolean = true

  override def createEntity(world: World, location: Entity, itemstack: ItemStack): Entity = {
    println("ItemSpore.createEntity")
    val entity = new EntityItemSpore(world, location.posX, location.posY, location.posZ, itemstack)
    entity.motionX = location.motionX
    entity.motionY = location.motionY
    entity.motionZ = location.motionZ
    entity
  }
}
