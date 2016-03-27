package com.torumekia

import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.{EntityRegistry, GameRegistry}
import net.minecraftforge.fml.common.{Mod, SidedProxy}


@Mod(modid = Torumekia.MODID, version = Torumekia.VERSION, modLanguage = "scala", name = "Torumekia")
object Torumekia {
  final val MODID = "torumekia"
  final val VERSION = "1.0"

  val itemSpore = new ItemSpore
  val blockFungus = (new BlockFungus).setCreativeTab(CreativeTabs.tabDecorations)

  val itemWheatAndSteel = (new ItemWheatAndSteel).setUnlocalizedName("wheat_and_steel")

  def registerBlocks() = {
    val cool = new BlockCool
    cool.setUnlocalizedName("cool")
    cool.setCreativeTab(CreativeTabs.tabDecorations)
    GameRegistry.registerBlock(cool, "cool")

    GameRegistry.registerBlock(blockFungus, "fungus")
    GameRegistry.registerItem(itemSpore, "spore")
    EntityRegistry.registerModEntity(classOf[EntitySpore], classOf[EntitySpore].getSimpleName, 1, this, 16, 4, true)

    GameRegistry.registerItem(itemWheatAndSteel, "wheat_and_steel")
  }

  def registerItemModels(): Unit = {
    val coolItem = GameRegistry.findItem(MODID, "cool")
    ModelLoader.setCustomModelResourceLocation(coolItem, 0, new ModelResourceLocation(coolItem.getRegistryName))
    ModelLoader.setCustomModelResourceLocation(itemSpore, 0, new ModelResourceLocation(itemSpore.getRegistryName))
    ModelLoader.setCustomModelResourceLocation(itemWheatAndSteel, 0, new ModelResourceLocation(itemWheatAndSteel.getRegistryName))

  }

  @SidedProxy(clientSide = "com.torumekia.ClientOnlyProxy", serverSide = "com.torumekia.CommonProxy")
  var proxy: CommonProxy = null

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    proxy.preInit()
  }
}

class CommonProxy {
  def preInit(): Unit = {
    Torumekia.registerBlocks()
  }
}

class ClientOnlyProxy extends CommonProxy {
  override def preInit(): Unit = {
    super.preInit()
    Torumekia.registerItemModels()
  }
}
