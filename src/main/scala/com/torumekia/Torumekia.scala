package com.torumekia

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent}
import net.minecraftforge.fml.common.registry.{EntityRegistry, GameRegistry}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}


@Mod(modid = "torumekia", version = "1.0", modLanguage = "scala", name = "Torumekia")
object Torumekia {
  val itemSpore = new ItemSpore
  val blockFungus = new BlockFungus

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    println("Hello!")
    GameRegistry.registerBlock(blockFungus, "fungus")
    GameRegistry.registerItem(itemSpore, "spore")
    EntityRegistry.registerModEntity(classOf[EntityItemSpore], classOf[EntityItemSpore].getSimpleName, 1, this, 16, 4, true)

    val res = new ModelResourceLocation("torumekia:spore", "inventory")
    Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(itemSpore, 0, res)
  }

  @SideOnly(Side.CLIENT)
  def postInit(event: FMLPostInitializationEvent): Unit = {
    println("Client-side postinit")
  }
}
