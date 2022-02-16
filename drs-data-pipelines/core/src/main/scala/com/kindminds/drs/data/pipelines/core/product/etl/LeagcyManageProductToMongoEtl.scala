package com.kindminds.drs.data.pipelines.core.product.etl


import com.kindminds.drs.{Country, Currency}
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl
import com.kindminds.drs.api.data.access.rdb.CompanyDao
import com.kindminds.drs.api.data.cqrs.store.read.queries.{ProductViewQueries, QuoteRequestViewQueries}
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.api.v2.biz.domain.model.product.Product
import java.{lang, util}
import java.util.{ArrayList, List, Map, Optional}

import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
//import org.mongodb.scala.bson.{BsonDocument, BsonString, Document}
import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Failure, Success}

object LeagcyManageProductToMongoEtl  {

  val springCtx = BizCoreCtx.get

  val productDao = new ProductDao

  val companyDao = springCtx.getBean(classOf[CompanyDao])
    .asInstanceOf[CompanyDao]

  val productViewQueries = springCtx.getBean(classOf[ProductViewQueries])
    .asInstanceOf[ProductViewQueries]

  val pd = springCtx.getBean(classOf[com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao])
    .asInstanceOf[com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao]


  def main(args: Array[String]): Unit = {


    val repo = new ProductRepoImpl

    var currentIndex = 1

    val exception = new Array[String](100)

    var exceptionIndex = 0

    var saveBPjson = ""

    val allCompanyKcodeList = companyDao.queryAllCompanyKcodeList

//    allCompanyKcodeList.forEach(kcode => {

      val getallbaseproducts = productViewQueries.getBaseProductOnboardingList(1, 1000, "K598")

//      System.out.println("------------------------" + kcode + "------------------------")

      System.out.println("---------Total number of baseproducts = " + getallbaseproducts.size + "---------")

      getallbaseproducts.forEach(k => {
        val result: Optional[String] = pd.getId(k.getProductBaseCode, Country.CORE)
        val product_Id: String = result.get
        val p: Product = repo.findById(product_Id).get

        var data = p.getData.substring(1, p.getData.length - 1)

        var kcode = p.getSupplierKcode

        var productorsku = data.split("\"products\":\"")

        var productdatas = productorsku(0).replace("\\\"", "")

        productdatas = productdatas.replace("\"", "")

        var productdata = productdatas.split(",")

        var productList = new Array[Array[String]](productdata.length)

        //var supplierId = companyDao.queryIdFromKcode(kcode)
        var category = ""
        var brandNameCH = ""
        var brandNameEN = ""
        var productNameCH = ""
        var productNameEN = ""
        var variationType1 = ""
        var variationType2 = ""
        var variationTheme = ""
        var multiTheme = ""


        try {

          for (i <- 0 until productdata.length) {
            productList(i) = productdata(i).split(":")
            if (productList(i)(0) == "proposalProductCategory") category = productList(i)(1)
            else if (productList(i)(0) == "brand") brandNameCH = productList(i)(1)
            else if (productList(i)(0) == "brandEng") brandNameEN = productList(i)(1)
            else if (productList(i)(0) == "productNameLocal") productNameCH = productList(i)(1)
            else if (productList(i)(0) == "productNameEnglish") productNameEN = productList(i)(1)
            else if (productList(i)(0) == "variationType1") if (productList(i).length == 1) variationType1 = ""
            else variationType1 = productList(i)(1)
            else if (productList(i)(0) == "variationType2") if (productList(i).length == 1) variationType2 = ""
            else variationType2 = productList(i)(1)
          }

          System.out.println("kcode : " + kcode)
          System.out.println("ProductBaseCode : " + k.getProductBaseCode)
          System.out.println("supplierId : " + kcode)
          System.out.println("category : " + category)
          System.out.println("brandNameCH : " + brandNameCH)
          System.out.println("brandNameEN : " + brandNameEN)
          System.out.println("productNameCH : " + productNameCH)
          System.out.println("productNameEN : " + productNameEN)
          System.out.println("variationType1 : " + variationType1)
          System.out.println("variationType2 : " + variationType2)

          if (!("" == variationType1) && variationType2 == "") variationTheme = variationType1
          else if (!("" == variationType1) && !("" == variationType2)) variationTheme = variationType1 + "&" + variationType2
          else if (variationType1 == "") variationTheme = ""

          System.out.println("variationTheme : " + variationTheme)

          val originalPlace = "\",\"originalPlace\":"

          var allskudatas = ""

          if (productorsku(1).indexOf(originalPlace) > 0) {
            val allskudata = productorsku(1).split("\",\"originalPlace\":")

            allskudatas = allskudata(0).substring(2, allskudata(0).length - 2)
          }
          else {
            val allskudata = productorsku(1).split("\",\"medical\":")

            allskudatas = allskudata(0).substring(2, allskudata(0).length - 2)
          }

          allskudatas = allskudatas.replace("\\\"", "")

          val skudata = allskudatas.split("},\\{")
          val skuList = new Array[Array[String]](skudata.length)
          val SKU = new Array[Array[String]](skuList.length)
          val gtinValue = new Array[Array[String]](skuList.length)
          val gtinType = new Array[Array[String]](skuList.length)
          val type1value = new Array[Array[String]](skuList.length)
          val type2value = new Array[Array[String]](skuList.length)
          val fcaPrice = new Array[Array[String]](skuList.length)

          for (i <- 0 until skudata.length) {
            skuList(i) = skudata(i).split(",")
          }

          System.out.println("Total number of SKUs = " + skuList.length)

          System.out.println("********************************************")


          var sellerSku = ""
          var productId = ""
          var productIdType = ""
          var retailPrice = ""
          var variablevalue1 = ""
          var variablevalue2 = ""
          var variable = ""

          var allvariablevalue1 = ""
          var allvariablevalue2 = ""

          var sku = ""

          var allsku = ""

          for (i <- 0 until skuList.length) {

            SKU(i) = skuList(i)(8).split(":")
            gtinValue(i) = skuList(i)(6).split(":")
            gtinType(i) = skuList(i)(4).split(":")
            type1value(i) = skuList(i)(1).split(":")
            type2value(i) = skuList(i)(3).split(":")
            fcaPrice(i) = skuList(i)(skuList(i).length - 8).split(":")

            if (SKU(i).length == 1) sellerSku = ""
            else sellerSku = SKU(i)(1)

            if (gtinValue(i).length == 1) productId = ""
            else productId = gtinValue(i)(1)

            if (gtinType(i).length == 1) productIdType = ""
            else productIdType = gtinType(i)(1)

            if (fcaPrice(i).length == 1) retailPrice = ""
            else retailPrice = fcaPrice(i)(1)

            if (type1value(i).length == 1) variablevalue1 = ""
            else {
              variablevalue1 = type1value(i)(1)
              allvariablevalue1 = allvariablevalue1 + variablevalue1 + ","
            }

            if (type2value(i).length == 1) variablevalue2 = ""
            else {
              variablevalue2 = type2value(i)(1)
              allvariablevalue2 = allvariablevalue2 + variablevalue2 + ","
            }

            if (!("" == variablevalue1) && variablevalue2 == "") variable = variablevalue1
            else if (!("" == variablevalue1) && !("" == variablevalue2)) variable = variablevalue1 + "&" + variablevalue2
            else if (variablevalue1 == "") variable = ""

            System.out.println("sellerSku : " + SKU(i)(1))
            System.out.println("productId : " + gtinValue(i)(1))
            System.out.println("productIdType : " + gtinType(i)(1))
            System.out.println("retailPrice : " + fcaPrice(i)(1))
            System.out.println("sellerSku : " + sellerSku)
            System.out.println("productId : " + productId)
            System.out.println("productIdType : " + productIdType)
            System.out.println("variable : " + variable)
            System.out.println("retailPrice : " + retailPrice)
            System.out.println("++++++++++++++++++++++++++++++")


            sku = "{ \"actions\": [\"a\",\"b\"], \"sellerSku\": \"" + sellerSku + "\", " +
              "\"productId\": {\"name\": \"Product ID\", \"value\": \"" + productId + "\"}, " +
              "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType + "\"}, " +
              "\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
              "\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
              "\"pageIndex\": 1,\"retailPrice\": \"" + retailPrice + "\", \"fbaQuantity\": 0, " +
              "\"salesVolume\":55, \"openPosition\":20, \"editable\": false, " +
              "\"status\": \"applied\" },"

            allsku = allsku + sku

          }

          allsku = allsku.substring(0, allsku.length - 1)

          var index1 = ""

          var index2 = ""

          var variables1 = ""

          var variables2 = ""

          var allvariables1 = ""

          var allvariables2 = ""

          if (!("" == allvariablevalue1)) {

            allvariablevalue1 = allvariablevalue1.substring(0, allvariablevalue1.length - 1)

            val allvariablevalue1list = allvariablevalue1.split(",")

            val list = new util.ArrayList[String]

            for (i <- 0 until allvariablevalue1list.length) {
              if (!list.contains(allvariablevalue1list(i))) list.add(allvariablevalue1list(i))
            }

            val newStr = list.toArray(new Array[String](1))

            for (i <- 0 until newStr.length) {

              for (q <- 0 until allvariablevalue1list.length) {
                if (newStr(i) == allvariablevalue1list(q)) index1 = index1 + q + ","
              }

              index1 = index1.substring(0, index1.length - 1)

              variables1 = "{ \"value\": \"" + newStr(i) + "\",\n" + "\"index\": [" + index1 + "]\n" + "},"

              allvariables1 = allvariables1 + variables1

              index1 = ""

            }

            allvariables1 = allvariables1.substring(0, allvariables1.length - 1)

          }

          if (!("" == allvariablevalue2)) {

            allvariablevalue2 = allvariablevalue2.substring(0, allvariablevalue2.length - 1)

            val allvariablevalue2list = allvariablevalue2.split(",")

            val list = new util.ArrayList[String]

            for (i <- 0 until allvariablevalue2list.length) {
              if (!list.contains(allvariablevalue2list(i))) list.add(allvariablevalue2list(i))
            }

            val newStr = list.toArray(new Array[String](1))

            for (i <- 0 until newStr.length) {

              for (q <- 0 until allvariablevalue2list.length) {
                if (newStr(i) == allvariablevalue2list(q)) index2 = index2 + q + ","
              }

              index2 = index2.substring(0, index2.length - 1)

              variables2 = "{ \"value\": \"" + newStr(i) + "\",\n" + "\"index\": [" + index2 + "]\n" + "},"

              allvariables2 = allvariables2 + variables2

              index2 = ""

            }

            allvariables2 = allvariables2.substring(0, allvariables2.length - 1)

          }

          if (!("" == variationType1) && variationType2 == "") {
          }
          else if (!("" == variationType1) && !("" == variationType2)) multiTheme = "{ \"theme\": \"" + variationType1 + "\", " + "\"variables\": [\n" + allvariables1 + "  ]" + " }," + "{ \"theme\": \"" + variationType2 + "\", " + "\"variables\": [\n" + allvariables2 + "  ]" + " }"
          else if (variationType1 == "") {
          }

          saveBPjson = "{\"supplierId\": \"488\",\n" +
            "  \"category\": \"" + category + "\",\n" +
            "  \"brandNameCH\": \"" + brandNameCH + "\",\n" +
            "  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
            "  \"productNameCH\": \"" + productNameCH + "\",\n" +
            "  \"productNameEN\": \"" + productNameEN + "\",\n" +
            "  \"variationTheme\": \"" + variationTheme + "\",\n" +
            "  \"multiTheme\": [" + multiTheme + "],\n" +
            "  \"totalSkus\": " + skuList.length + ",\n" +
            "  \"pageSize\": \"1\",\n" +
            "  \"currentIndex\": \"" + currentIndex + "\",\n" +
            "  \"bpStatus\": \"applied\",\n" +
            "\"skus\": [" + allsku + "]" +
            "}"

          System.out.println(saveBPjson)

//          productDao.productintoMongodb(saveBPjson)

          productDao.createProduct(org.bson.Document.parse(saveBPjson))

          currentIndex = currentIndex + 1

        } catch {

          case a: java.lang.ArrayIndexOutOfBoundsException => {

//            println("ArrayIndexOutOfBoundsException")
//
//            exception(exceptionIndex) = p.getData
//
//            data = p.getData.substring(1, p.getData.length - 1)
//
//            kcode = p.getSupplierKcode
//
//            productorsku = data.split("\"products\":\"")
//
//            productdatas = productorsku(0).replace("\"", "")
//
//            productdata = productdatas.split(",")
//
//            productList = new Array[Array[String]](productdata.length)
//
//            supplierId = companyDao.queryIdFromKcode(kcode)
//
//            for (i <- 0 until productdata.length) { //				System.out.println(productdata[i]);
//              productList(i) = productdata(i).split(":")
//              if (productList(i)(0) == "proposalProductCategory") category = productList(i)(1)
//              else if (productList(i)(0) == "brand") brandNameCH = productList(i)(1)
//              else if (productList(i)(0) == "brandEng") brandNameEN = productList(i)(1)
//              else if (productList(i)(0) == "productNameLocal") productNameCH = productList(i)(1)
//              else if (productList(i)(0) == "productNameEnglish") productNameEN = productList(i)(1)
//              else if (productList(i)(0) == "variationType1") if (productList(i).length == 1) variationType1 = ""
//              else variationType1 = productList(i)(1)
//              else if (productList(i)(0) == "variationType2") if (productList(i).length == 1) variationType2 = ""
//              else variationType2 = productList(i)(1)
//            }
//
//            if (!("" == variationType1) && variationType2 == "") variationTheme = variationType1
//            else if (!("" == variationType1) && !("" == variationType2)) {
//              variationTheme = variationType1 + "&" + variationType2
//              multiTheme = "{ \"theme\": \"" + variationType1 + "\", " + "\"variables\": [\n" + "{ \"value\": \"pink\",\n" + "\"index\": [0,1,2]\n" + "},\n" + "{ \"value\": \"pink\",\n" + "\"index\": [0,1,2]\n" + "}\n" + "  ]" + " }"
//            }
//            else if (variationType1 == "") variationTheme = ""

//            val originalPlace = "\",\"originalPlace\":"
//
//            var allskudatas = ""
//
//            if (productorsku(1).indexOf(originalPlace) > 0) {
//              val allskudata = productorsku(1).split("\",\"originalPlace\":")
//
//              allskudatas = allskudata(0).substring(2, allskudata(0).length - 2)
//            }
//            else {
//              val allskudata = productorsku(1).split("\",\"medical\":")
//
//              allskudatas = allskudata(0).substring(2, allskudata(0).length - 2)
//            }
//
//            allskudatas = allskudatas.replace("\\\"", "")
//
//            val skudata = allskudatas.split("},\\{")
//            val skuList = new Array[Array[String]](skudata.length)
//            val SKU = new Array[Array[String]](skuList.length)
//            val gtinValue = new Array[Array[String]](skuList.length)
//            val gtinType = new Array[Array[String]](skuList.length)
//            val type1value = new Array[Array[String]](skuList.length)
//            val type2value = new Array[Array[String]](skuList.length)
//            val fcaPrice = new Array[Array[String]](skuList.length)
//
//            for (i <- 0 until skudata.length) {
//              skuList(i) = skudata(i).split(",")
//            }

           // System.out.println("Total number of SKUs = " + skuList.length)

            System.out.println("********************************************")

//            var sellerSku = ""
//            var productId = ""
//            var productIdType = ""
//            var retailPrice = ""
//            var variablevalue1 = ""
//            var variablevalue2 = ""
//            var variable = ""
//
//            var allvariablevalue1 = ""
//            var allvariablevalue2 = ""
//
//            var sku = ""
//
//            var allsku = ""

//            for (i <- 0 until skuList.length) {
//              SKU(i) = skuList(i)(8).split(":")
//              gtinValue(i) = skuList(i)(6).split(":")
//              gtinType(i) = skuList(i)(4).split(":")
//              type1value(i) = skuList(i)(1).split(":")
//              type2value(i) = skuList(i)(3).split(":")
//              fcaPrice(i) = skuList(i)(skuList(i).length - 8).split(":")
//
//              if (SKU(i).length == 1) sellerSku = ""
//              else sellerSku = SKU(i)(1)
//
//              if (gtinValue(i).length == 1) productId = ""
//              else productId = gtinValue(i)(1)
//
//              if (gtinType(i).length == 1) productIdType = ""
//              else productIdType = gtinType(i)(1)
//
//              if (fcaPrice(i).length == 1) retailPrice = ""
//              else retailPrice = fcaPrice(i)(1)
//
//              if (type1value(i).length == 1) variablevalue1 = ""
//              else {
//                variablevalue1 = type1value(i)(1)
//                allvariablevalue1 = allvariablevalue1 + variablevalue1 + ","
//              }
//
//              if (type2value(i).length == 1) variablevalue2 = ""
//              else {
//                variablevalue2 = type2value(i)(1)
//                allvariablevalue2 = allvariablevalue2 + variablevalue2 + ","
//              }
//
//              if (!("" == variablevalue1) && variablevalue2 == "") variable = variablevalue1
//              else if (!("" == variablevalue1) && !("" == variablevalue2)) variable = variablevalue1 + "&" + variablevalue2
//              else if (variablevalue1 == "") variable = ""
//
//              sku = "{ \"actions\": [\"a\",\"b\"], \"sellerSku\": \"" + sellerSku + "\", " +
//                "\"productId\": {\"name\": \"Product ID\", \"value\": \"" + productId + "\"}, " +
//                "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType +
//                "\"}, " + "\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
//                "\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
//                "\"pageIndex\": 1,\"retailPrice\": \"" + retailPrice + "\", \"fbaQuantity\": 0, " +
//                "\"salesVolume\":55, \"openPosition\":20, \"editable\": false, " +
//                "\"status\": \"applied\" },"
//
//              allsku = allsku + sku
//
//
//            }
//
//            allsku = allsku.substring(0, allsku.length - 1)
//
//            var index1 = ""
//
//            var index2 = ""
//
//            var variables1 = ""
//
//            var variables2 = ""
//
//            var allvariables1 = ""
//
//            var allvariables2 = ""

//            if (!("" == allvariablevalue1)) {
//              allvariablevalue1 = allvariablevalue1.substring(0, allvariablevalue1.length - 1)
//              val allvariablevalue1list = allvariablevalue1.split(",")
//              val list = new util.ArrayList[String]
//              for (i <- 0 until allvariablevalue1list.length) {
//                if (!list.contains(allvariablevalue1list(i))) list.add(allvariablevalue1list(i))
//              }
//              val newStr = list.toArray(new Array[String](1))
//              for (i <- 0 until newStr.length) {
//                for (q <- 0 until allvariablevalue1list.length) {
//                  if (newStr(i) == allvariablevalue1list(q)) index1 = index1 + q + ","
//                }
//                index1 = index1.substring(0, index1.length - 1)
//                variables1 = "{ \"value\": \"" + newStr(i) + "\",\n" + "\"index\": [" + index1 + "]\n" + "},"
//                allvariables1 = allvariables1 + variables1
//                index1 = ""
//              }
//              allvariables1 = allvariables1.substring(0, allvariables1.length - 1)
//            }

//            if (!("" == allvariablevalue2)) {
//              allvariablevalue2 = allvariablevalue2.substring(0, allvariablevalue2.length - 1)
//              val allvariablevalue2list = allvariablevalue2.split(",")
//              val list = new util.ArrayList[String]
//              for (i <- 0 until allvariablevalue2list.length) {
//                if (!list.contains(allvariablevalue2list(i))) list.add(allvariablevalue2list(i))
//              }
//              val newStr = list.toArray(new Array[String](1))
//              for (i <- 0 until newStr.length) {
//                for (q <- 0 until allvariablevalue2list.length) {
//                  if (newStr(i) == allvariablevalue2list(q)) index2 = index2 + q + ","
//                }
//                index2 = index2.substring(0, index2.length - 1)
//                variables2 = "{ \"value\": \"" + newStr(i) + "\",\n" + "\"index\": [" + index2 + "]\n" + "},"
//                allvariables2 = allvariables2 + variables2
//                index2 = ""
//              }
//              allvariables2 = allvariables2.substring(0, allvariables2.length - 1)
//            }
//
//            if (!("" == variationType1) && variationType2 == "") {
//            }
//            else if (!("" == variationType1) && !("" == variationType2)) multiTheme = "{ \"theme\": \"" + variationType1 + "\", " + "\"variables\": [\n" + allvariables1 + "  ]" + " }," + "{ \"theme\": \"" + variationType2 + "\", " + "\"variables\": [\n" + allvariables2 + "  ]" + " }"
//            else if (variationType1 == "") {
//            }

//            saveBPjson = "{\"supplierId\": \"" + supplierId + "\",\n" +
//              "  \"category\": \"" + category + "\",\n" +
//              "  \"brandNameCH\": \"" + brandNameCH + "\",\n" +
//              "  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
//              "  \"productNameCH\": \"" + productNameCH + "\",\n" +
//              "  \"productNameEN\": \"" + productNameEN + "\",\n" +
//              "  \"variationTheme\": \"" + variationTheme + "\",\n" +
//              "  \"multiTheme\": [" + multiTheme + "],\n" +
//              "  \"totalSkus\": " + skuList.length + ",\n" +
//              "  \"pageSize\": \"1\",\n" +
//              "  \"currentIndex\": \"" + currentIndex + "\",\n" +
//              "  \"bpStatus\": \"applied\",\n" +
//              "  \"categoryVersion\": 1.0,\n" +
//              "\"skus\": [" + allsku + "]" +
//              "}"


//            productDao.productintoMongodb(saveBPjson)

//            productDao.productExceptionintoMongodb(saveBPjson)

            //exceptionIndex += 1

          }

        //end catch
        }
//        for (i <- 0 until exceptionIndex) {
//          System.out.println(exception(i))
//        }
      })

//    })

  }







}
