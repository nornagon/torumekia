package com.torumekia

import net.minecraft.block.BlockBush
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.world.{ColorizerGrass, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}


class BlockFungus extends BlockBush(Material.vine) {
  setUnlocalizedName("fungus")
  setDefaultState(blockState.getBaseState)

  @SideOnly(Side.CLIENT) override def getBlockColor: Int = {
    ColorizerGrass.getGrassColor(0.5D, 1.0D)
  }

  override def onBlockDestroyedByPlayer(worldIn: World, pos: BlockPos, state: IBlockState): Unit = {
    println("Hello! Block destroyed.")

    if (!worldIn.isRemote) {
      val numToSpawn = 1 + worldIn.rand.nextInt(4)
      for (i <- 1 to numToSpawn) {
        val f: Float = 0.5F
        val dx = (worldIn.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
        val dy = (worldIn.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
        val dz = (worldIn.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
        val x = pos.getX.toDouble + dx
        val y = pos.getY.toDouble + dy
        val z = pos.getZ.toDouble + dz
        val stack = new ItemStack(Torumekia.itemSpore, 1)
        val entityitem = new EntityItemSpore(worldIn, x, y, z, stack)
        entityitem.motionX *= 3
        entityitem.motionY *= 3
        entityitem.motionZ *= 3
        worldIn.spawnEntityInWorld(entityitem)
      }
    }
  }
}
