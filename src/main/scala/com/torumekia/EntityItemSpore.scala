package com.torumekia

import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World


class EntityItemSpore(world: World) extends EntityItem(world) {
  def this(world: World, x: Double, y: Double, z: Double, itemStack: ItemStack) = {
    this(world)
    this.lifespan = 6000

    this.setPosition(x, y, z)
    this.rotationYaw = (Math.random * 360.0D).toFloat
    this.motionX = ((Math.random * 0.20000000298023224D - 0.10000000149011612D).toFloat).toDouble
    this.motionY = 0.20000000298023224D
    this.motionZ = ((Math.random * 0.20000000298023224D - 0.10000000149011612D).toFloat).toDouble
    this.setInfinitePickupDelay()
    this.setEntityItemStack(itemStack)
  }

  def germinate(): Unit = {
    println("Germinating...")
    val pos = getPosition
    if (world.isAirBlock(pos)) {
      world.setBlockState(pos, Torumekia.blockFungus.getDefaultState)
    }
    world.removeEntity(this)
  }

  override def onUpdate(): Unit = {
    if (!world.isRemote && world.rand.nextFloat() < 0.001) {
      germinate()
    } else {
      super.onUpdate()
    }
  }
}
