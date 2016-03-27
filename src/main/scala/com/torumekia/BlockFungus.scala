package com.torumekia

import java.util.Random
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.util.{AxisAlignedBB, BlockPos, EnumWorldBlockLayer}
import net.minecraft.world.{ColorizerGrass, IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}


class BlockFungus extends Block(Material.vine) {
  setUnlocalizedName("fungus")
  setDefaultState(blockState.getBaseState)
  setCreativeTab(CreativeTabs.tabDecorations)
  setTickRandomly(true)
  setHardness(0.5f)
  setStepSound(Block.soundTypePiston)
  val f = 0.2F
  setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f)

  override def tickRate(worldIn: World): Int = 20


  override def updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random): Unit = {
    super.updateTick(worldIn, pos, state, rand)
    //if (rand.nextFloat() < 0.1) {
      makeSpore(worldIn, pos)
    //}
  }

  override def isOpaqueCube: Boolean = false

  override def isFullCube: Boolean = false

  override def isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean = true

  override def getCollisionBoundingBox(worldIn: World, pos: BlockPos, state: IBlockState): AxisAlignedBB = null

  @SideOnly(Side.CLIENT) override def getBlockColor: Int = {
    ColorizerGrass.getGrassColor(0.5D, 1.0D)
  }

  @SideOnly(Side.CLIENT) override def getBlockLayer: EnumWorldBlockLayer = EnumWorldBlockLayer.CUTOUT

  override def onBlockDestroyedByPlayer(worldIn: World, pos: BlockPos, state: IBlockState): Unit = {
    if (!worldIn.isRemote) {
      asplode(worldIn, pos)
    }
  }

  def asplode(worldIn: World, pos: BlockPos): Unit = {
    val numToSpawn = 1 + worldIn.rand.nextInt(4)
    for (i <- 1 to numToSpawn) {
      makeSpore(worldIn, pos)
    }
  }

  def makeSpore(worldIn: World, pos: BlockPos): Boolean = {
    val f: Float = 0.5F
    val dx = (worldIn.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
    val dy = (worldIn.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
    val dz = (worldIn.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
    val x = pos.getX.toDouble + dx
    val y = pos.getY.toDouble + dy
    val z = pos.getZ.toDouble + dz
    val entityitem = new EntitySpore(worldIn, x, y, z)
    entityitem.motionX *= 3
    entityitem.motionY *= 3
    entityitem.motionZ *= 3
    worldIn.spawnEntityInWorld(entityitem)
  }

  override def onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity): Unit = {
    if (!worldIn.isRemote) {
      val dx = entityIn.posX - entityIn.prevPosX
      val dy = entityIn.posY - entityIn.prevPosY
      val dz = entityIn.posZ - entityIn.prevPosZ
      val d = math.sqrt((dx*dx) + (dy*dy) + (dz*dz))
      if (d >= 0.2) {
        if (worldIn.rand.nextFloat() < 0.1) {
          worldIn.destroyBlock(pos, false)
          asplode(worldIn, pos)
        }
      }
    }
  }

  override def onBlockEventReceived(worldIn: World, pos: BlockPos, state: IBlockState, eventID: Int, eventParam: Int): Boolean = {
    println(s"Block event received: $eventID, $eventParam")
    false
  }
}
