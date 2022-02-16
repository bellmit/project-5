package com.kindminds.drs.core.actors.handlers

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.query.manageProduct.GetBaseProducts
import com.kindminds.drs.core.actors.handlers.query.product.ManageProductQueryHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class TestProductMongoHandler extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val name = self.path.name
  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
//  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
//  val drsEventBus = new DrsEventBus

  val queryActor = TestActorRef(new ManageProductQueryHandler(drsQueryBus))

  private val testBPBedSheet = "{\n" + "  \"supplierId\": 190,\n" + "  \"category\": \"bedding\",\n" +
    "  \"brandNameCH\": \"CH\",\n" + "  \"brandNameEN\": \"SuperComfy\",\n" + "  \"productNameCH\": \"無\",\n" + "  \"productNameEN\": \"Nicolas Cage BedSheets2\",\n" + "  \"totalSkus\": 2,\n" + "  \"pageSize\": 3,\n" + "  \"currentIndex\": 1,\n" + "\"skus\": [\n" + "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " + "\"productId\": {\"name\": \"Product ID\", \"value\": \"123456789\"}, " + "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " + //            "\"hscode\": {\"name\": \"CCC Code\", \"value\": \"7001000200\"}, " +
    //            "\"variationTheme\": {\"name\": \"Variation Theme\", \"value\": \"Package Quantity\"}, " +
    "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " + "\"retailPrice\": \"\", \"fbaQuantity\": \"\"}, \n" + "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " + "\"productId\": {\"name\": \"Product ID\", \"value\": \"500000000\"}, " + "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " + "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " + "\"retailPrice\": \"\", \"fbaQuantity\": \"\"}\n" + "  ]," + "  \"batteryCategory\": \"100000V\",\n" + "  \"chargeable\": \"no\",\n" + "  \"un38\": \"yes\",\n" + "  \"msds\": \"yes\",\n" + "  \"droptest\": \"yes\",\n" + "  \"allergic\": \"no\",\n" + "  \"dangerous\": \"no\",\n" + "  \"medical\": \"no\",\n" + "  \"battery\": \"no\",\n" + "  \"materialSelected\": \"無(None)\",\n" + "  \"aircargo\": \"yes\",\n" + "  \"seafreight\": \"yes\",\n" + "  \"certification\": \"yes\",\n" + "  \"formatSelected\": \"Select...\",\n" + "  \"packageSelected\": \"Select...\",\n" + "  \"wirelessSelected\": \"Select...\",\n" + "  \"showOthersInput\": false,\n" + "  \"startDate\":\"new Date()\",\n" + "  \"allFiles\": 0,\n" + "  \"batteryQuantity\": \"無電池\",\n" + "  \"batteryVoltage\": \"電池電壓\",\n" + "  \"batteryCapacitance\": \"電池組電容量\",\n" + "  \"batteryNetWeight\": \"電池組淨重\"\n" + "}"

  private val updateBlanket = "{\n" + "  \"supplierId\": 190,\n" + "  \"category\": \"bedding\",\n" +
    "  \"brandNameCH\": \"CH\",\n" + "  \"brandNameEN\": \"SuperComfy\",\n" +
    "  \"productNameCH\": \"無\",\n" + "  \"productNameEN\": \"Nicolas Cage Blanket Thick\",\n" +
    "  \"totalSkus\": 1,\n" + "  \"pageSize\": 3,\n" + "  \"currentIndex\": 3,\n" + "\"skus\": [\n" +
    "    { \"actions\": [\"a\",\"b\"], \"sellerSku\": \"K618\", " + "\"productId\": {\"name\": \"Product ID\", \"value\": \"500000000\"}, " + "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"EAN\"}, " + "\"variable\": {\"name\": \"Variable\", \"value\": \"small\"}, " + "\"retailPrice\": \"\", \"fbaQuantity\": \"\"}\n" + "  ]," + "  \"batteryCategory\": \"100000V\",\n" + "  \"chargeable\": \"no\",\n" + "  \"un38\": \"yes\",\n" + "  \"msds\": \"yes\",\n" + "  \"droptest\": \"yes\",\n" + "  \"allergic\": \"no\",\n" + "  \"dangerous\": \"no\",\n" + "  \"medical\": \"no\",\n" + "  \"battery\": \"no\",\n" + "  \"materialSelected\": \"無(None)\",\n" + "  \"aircargo\": \"yes\",\n" + "  \"seafreight\": \"yes\",\n" + "  \"certification\": \"yes\",\n" + "  \"formatSelected\": \"Select...\",\n" + "  \"packageSelected\": \"Select...\",\n" + "  \"wirelessSelected\": \"Select...\",\n" + "  \"showOthersInput\": false,\n" + "  \"startDate\":\"new Date()\",\n" + "  \"allFiles\": 0,\n" + "  \"batteryQuantity\": \"無電池\",\n" + "  \"batteryVoltage\": \"電池電壓\",\n" + "  \"batteryCapacitance\": \"電池組電容量\",\n" + "  \"batteryNetWeight\": \"電池組淨重\"\n" + "}"

  private val productId = "5ff2d26b3a882d52d14449f5"


  "Actor" must {

    "get base product json in correct format" in {
      queryActor ! GetBaseProducts("190", 1)

      Thread.sleep(1000)
    }
  }

//  "Actor" must {
//
//    "create base product json" in {
//
//      val result = CreateBaseProduct(190, testBPBedSheet)
//
//      queryActor ! result
//
//      Thread.sleep(1000)
//    }
//  }

//  "Actor" must {
//
//    "Update base product json" in {
//
//      val result = UpdateBaseProduct(190, productId, updateBlanket)
//
//      queryActor ! result
//
//      Thread.sleep(1000)
//    }
//  }

//  "Actor" must {
//
//    "Delete base product json" in {
//
//      val result = DeleteBaseProduct(190, "5ff56522f324730a14ddc042")
//
//      queryActor ! result
//
//      Thread.sleep(1000)
//    }
//  }


}
