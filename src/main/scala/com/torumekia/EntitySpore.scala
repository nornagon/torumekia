package com.torumekia

import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{BlockPos, MathHelper}
import net.minecraft.world.World


class EntitySpore(world: World) extends Entity(world) {
  def this(world: World, x: Double, y: Double, z: Double) = {
    this(world)

    this.setPosition(x, y, z)
    this.rotationYaw = (Math.random * 360.0D).toFloat
    this.motionX = ((Math.random * 0.20000000298023224D - 0.10000000149011612D).toFloat).toDouble
    this.motionY = 0.20000000298023224D
    this.motionZ = ((Math.random * 0.20000000298023224D - 0.10000000149011612D).toFloat).toDouble
  }

  setSize(1.0f / 16, 1.0f / 16)
  this.renderDistanceWeight = 10d

  def germinate(): Unit = {
    println("Germinating...")
    val pos = getPosition
    if (world.isAirBlock(pos) && canGrowAt(world, pos)) {
      world.setBlockState(pos, Torumekia.blockFungus.getDefaultState)
    }
    world.removeEntity(this)
  }

  def canGrowAt(world: World, pos: BlockPos): Boolean = {
    val block = world.getBlockState(pos.down).getBlock
    block == Blocks.grass || block == Blocks.dirt
  }

  override def onUpdate(): Unit = {
    if (!world.isRemote && this.onGround && world.rand.nextFloat() < 0.001) {
      germinate()
    } else {
      super.onUpdate()
      this.motionY -= 0.03999999910593033D*0.1
      this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox.minY + this.getEntityBoundingBox.maxY)
        / 2.0D, this.posZ)
      this.moveEntity(this.motionX, this.motionY, this.motionZ)
      val flag: Boolean = this.prevPosX.toInt != this.posX.toInt || this.prevPosY.toInt != this.posY.toInt || this
        .prevPosZ.toInt != this.posZ.toInt

      if (flag || this.ticksExisted % 25 == 0) {
        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock.getMaterial eq Material.lava) {
          this.motionY = 0.20000000298023224D
          this.motionX = ((this.rand.nextFloat - this.rand.nextFloat) * 0.2F).toDouble
          this.motionZ = ((this.rand.nextFloat - this.rand.nextFloat) * 0.2F).toDouble
          this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat * 0.4F)
        }
      }

      var f: Float = 0.98F

      if (this.onGround) {
        f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this
          .getEntityBoundingBox.minY) - 1, MathHelper.floor_double(this.posZ))).getBlock.slipperiness * 0.98F
      }

      this.motionX *= f.toDouble
      this.motionY *= 0.9800000190734863D
      this.motionZ *= f.toDouble

      if (this.onGround) {
        this.motionY *= -0.5D
      }

      this.handleWaterMovement()
    }
  }

  override def writeEntityToNBT(tagCompound: NBTTagCompound): Unit = {}

  override def entityInit(): Unit = {}

  override def readEntityFromNBT(tagCompund: NBTTagCompound): Unit = {}
}
