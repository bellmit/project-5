package com.kindminds.drs.data.pipelines.core.product.etl

import com.kindminds.drs.Country
import com.kindminds.drs.core.biz.repo.product.ProductRepoImpl
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import java.util
import java.util.{NoSuchElementException, Optional}

import com.kindminds.drs.api.data.access.rdb.CompanyDao
import com.kindminds.drs.api.data.access.rdb.product.ProductDao
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao
//import org.mongodb.scala.bson.{BsonDocument, BsonString, Document}

object LeagcyP2mApplicationToMongoEtl  {

  val springCtx = BizCoreCtx.get

  val productDao = springCtx.getBean(classOf[ProductDao])
    .asInstanceOf[ProductDao]

  val companyDao = springCtx.getBean(classOf[CompanyDao])
    .asInstanceOf[CompanyDao]

  val productViewQueries = springCtx.getBean(classOf[ProductViewQueries])
    .asInstanceOf[ProductViewQueries]

  val pd = springCtx.getBean(classOf[com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao])
    .asInstanceOf[com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao]

  val p2MApplicationDao = springCtx.getBean(classOf[P2MApplicationDao])
    .asInstanceOf[P2MApplicationDao]


  def main(args: Array[String]): Unit = {


    val repo = new ProductRepoImpl

    var currentIndex = 1

    val perDataOfCountry = new Array[String](100)

    val perProductSKU =  Array.ofDim[String](100, 100)

    val perSKUCountry = new Array[String](100)

    val allCompanyKcodeList = companyDao.queryAllCompanyKcodeList

//    allCompanyKcodeList.forEach(kcode => {

      val getallbaseproducts = productViewQueries.getBaseProductOnboardingList(1, 1000, "K488")

//      System.out.println("------------------------" + kcode + "------------------------")

      System.out.println("---------Total number of baseproducts = " + getallbaseproducts.size + "---------")

      getallbaseproducts.forEach(k => {
        val result: Optional[String] = pd.getId(k.getProductBaseCode, Country.CORE)
        val product_Id: String = result.get
        val p = repo.findById(product_Id).get

        var data = p.getData.substring(1, p.getData.length - 1)

        var kcode = p.getSupplierKcode

        val basecode = k.getProductBaseCode

        var productorsku = data.split("\"products\":\"")

        var productdatas = productorsku(0).replace("\\\"", "")

        productdatas = productdatas.replace("\"", "")

        var productdata = productdatas.split(",")

        var productList = new Array[Array[String]](productdata.length)

        var supplierId = companyDao.queryIdFromKcode(kcode)
        var brandNameEN = ""
        var productNameEN = ""
        var selectedCountry = ""
        var selectedPlatform = ""
        var variationType1 = ""
        var variationType2 = ""
        var variationTheme = ""

        for (i <- 0 until productdata.length) { //				System.out.println(productdata[i]);
          productList(i) = productdata(i).split(":")
          if (productList(i)(0) == "brandEng") brandNameEN = productList(i)(1)
          else if (productList(i)(0) == "productNameEnglish") productNameEN = productList(i)(1)
          else if (productList(i)(0) == "variationType1") if (productList(i).length == 1) variationType1 = ""
          else variationType1 = productList(i)(1)
          else if (productList(i)(0) == "variationType2") if (productList(i).length == 1) variationType2 = ""
          else variationType2 = productList(i)(1)
        }

        if (!("" == variationType1) && variationType2 == "") variationTheme = variationType1
        else if (!("" == variationType1) && !("" == variationType2)) variationTheme = variationType1 + "&" + variationType2
        else if (variationType1 == "") variationTheme = ""

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

        System.out.println(allskudatas)

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

        System.out.println("ProductBaseCode : " + k.getProductBaseCode)

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

        var perProductSKUIndex = 0

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

          sku = "{ \"actions\": [\"a\",\"b\"], \"sellerSku\": \"" + sellerSku + "\", " +
            "\"productId\": {\"name\": \"Product ID\", \"value\": \"" + productId + "\"}, " +
            "\"productIdType\": {\"name\": \"Product ID Type\", \"value\": \"" + productIdType + "\"}, " +
            "\"variable\": {\"name\": \"Variable\", \"value\": \"" + variable + "\"}, " +
            "\"variationTheme\": {\"name\": \"variationTheme\", \"value\": \"" + variationTheme + "\"}, " +
            "\"pageIndex\": 1,\"retailPrice\": \"" + retailPrice + "\", \"fbaQuantity\": 0, " +
            "\"salesVolume\":55, \"openPosition\":20, \"editable\": false, " +
            "\"status\": \"applied\" },"

          perProductSKU(perProductSKUIndex)(0) = sellerSku
          perProductSKU(perProductSKUIndex)(1) = sku
          perProductSKUIndex += 1

          allsku = allsku + sku

        }

        var savejson = ""

        var country = ""

        var index = 0

        for (i <- 1 until 12) {
          try {
            val pc = repo.find(product_Id, Country.fromKey(i)).get
            perDataOfCountry(index) = pc.getData.substring(1, pc.getData.length - 1)
            index += 1
          } catch {
            case ex: NoSuchElementException =>

          }
        }

        try {

          for (l <- 0 until index) {

            System.out.println(perDataOfCountry(l))
            val countrydata = perDataOfCountry(l)
            val splitdata = countrydata.split("\"products\":\"")
            var datas = splitdata(0).replace("\\\"", "")
            datas = datas.replace("\"", "")
            val basedata = datas.split(",")
            val dataList = new Array[Array[String]](basedata.length)

            for (j <- 0 until basedata.length) {

              dataList(j) = basedata(j).split(":")

              if (dataList(j)(0) == "country") country = dataList(j)(1)

              if (country == "TW") {
                selectedCountry = "台灣"
                selectedPlatform = "Amazon.tw"
              }
              else if (country == "US") {
                selectedCountry = "美國"
                selectedPlatform = "Amazon.us"
              }
              else if (country == "UK") {
                selectedCountry = "英國"
                selectedPlatform = "Amazon.uk"
              }
              else if (country == "CA") {
                selectedCountry = "加拿大"
                selectedPlatform = "Amazon.ca"
              }
              else if (country == "DE") {
                selectedCountry = "德國"
                selectedPlatform = "Amazon.de"
              }
              else if (country == "FR") {
                selectedCountry = "法國"
                selectedPlatform = "Amazon.fr"
              }
              else if (country == "IT") {
                selectedCountry = "義大利"
                selectedPlatform = "Amazon.it"
              }
              else if (country == "ES") {
                selectedCountry = "西班牙"
                selectedPlatform = "Amazon.es"
              }
              else if (country == "EU") {
                selectedCountry = "歐盟"
                selectedPlatform = "Amazon.eu"
              }
              else if (country == "NA") {
                selectedCountry = "納米比亞"
                selectedPlatform = "Amazon.na"
              }
              else if (country == "MX") {
                selectedCountry = "墨西哥"
                selectedPlatform = "Amazon.mx"
              }
            }

            val countryskudata = splitdata(1).split("}]\"")

            System.out.println(countryskudata(0))

            var countryskudatas = countryskudata(0).substring(2)

            countryskudatas = countryskudatas.replace("\\\"", "")

            System.out.println(countryskudatas)

            val perskudata = countryskudatas.split("},\\{")

            val perskuList = new Array[Array[String]](perskudata.length)

            val perSKU = new Array[Array[String]](perskuList.length)

            var SkuOfCountry = ""

            var appliedSkuCode = ""

            var appliedSkuData = ""

            var perSKUCountryIndex = 0

            for (k <- 0 until perskudata.length) {
              perskuList(k) = perskudata(k).split(",")
            }

            for (i <- 0 until perskuList.length) {
              perSKU(i) = perskuList(i)(0).split(":")
              if (perSKU(i).length == 1) SkuOfCountry = ""
              else SkuOfCountry = perSKU(i)(1)
              perSKUCountry(perSKUCountryIndex) = SkuOfCountry
              perSKUCountryIndex += 1
            }
            for (m <- 0 until perProductSKUIndex) {
              for (n <- 0 until perSKUCountryIndex) {
                if (perProductSKU(m)(0) == perSKUCountry(n)) {
                  appliedSkuCode = appliedSkuCode + "\"" + perProductSKU(m)(0) + "\","
                  appliedSkuData = appliedSkuData + perProductSKU(m)(1)
                }
              }
            }

            appliedSkuCode = appliedSkuCode.substring(0, appliedSkuCode.length - 1)
            appliedSkuData = appliedSkuData.substring(0, appliedSkuData.length - 1)


            System.out.println(appliedSkuCode)
            System.out.println(appliedSkuData)

            savejson = "{\"name\": \"\",\n" +
              "  \"type\": \"new\",\n" +
              "  \"supplierId\": \"" + supplierId + "\",\n" +
              "  \"selectedProduct\": \"" + basecode + "\",\n" +
              "  \"selectedCountry\": \"" + selectedCountry + "\",\n" +
              "  \"selectedPlatform\": \"" + selectedPlatform + "\",\n" +
              "  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
              "  \"productNameEN\": \"" + productNameEN + "\",\n" +
              "  \"bpId\": \"" + product_Id + "\",\n" +
              "  \"appliedSkuCode\": [" + appliedSkuCode + "],\n" +
              "  \"appliedSku\": [" + appliedSkuData + "]," +
              "  \"subApplication\": {\n" +
              "      \"marketPlaceInfo\":{\n" +
              "			      \"type\":\"MarketPlace Information\",\n" +
              "	      		\"appliedSku\": [" + appliedSkuData + "]\n" +
              "		      	},\n" +
              "      \"insurance\":{\n" +
              "		      	\"type\":\"Insurance\",\n" +
              "		      	\"appliedSku\": [" + appliedSkuData + "]\n" +
              "		      	},\n" +
              "      \"regional\":{\n" +
              "     			\"type\":\"Regional\",\n" +
              "     			\"appliedSku\": [" + appliedSkuData + "]\n" +
              "     			},\n" +
              "      \"shipping\":{\n" +
              "     			\"type\":\"Shipping\",\n" +
              "   	  		\"appliedSku\": [" + appliedSkuData + "]\n" +
              "     			},\n" +
              "      \"productInfo\":{\n" +
              "     			\"type\":\"Product Advanced Information\"" +
              "     			\"appliedSku\": [" + appliedSkuData + "]\n" +
              "     			}\n" +
              "  	   },\n" +
              "  \"status\": \"Approved\",\n" +
              "}"

            System.out.println(country)

            System.out.println(savejson)

//            p2MApplicationDao.intoMongodb(savejson)

            country = ""


          }
        } catch {
          case ex: StringIndexOutOfBoundsException => {
            for (l <- 0 until index) {
              System.out.println(perDataOfCountry(l))
              val countrydata = perDataOfCountry(l)
              val splitdata = countrydata.split("\"products\":\"")
              var datas = splitdata(0).replace("\\\"", "")
              datas = datas.replace("\"", "")
              val basedata = datas.split(",")
              val dataList = new Array[Array[String]](basedata.length)

              for (j <- 0 until basedata.length) {
                dataList(j) = basedata(j).split(":")
                if (dataList(j)(0) == "country") country = dataList(j)(1)
                if (country == "TW") {
                  selectedCountry = "台灣"
                  selectedPlatform = "Amazon.tw"
                }
                else if (country == "US") {
                  selectedCountry = "美國"
                  selectedPlatform = "Amazon.us"
                }
                else if (country == "UK") {
                  selectedCountry = "英國"
                  selectedPlatform = "Amazon.uk"
                }
                else if (country == "CA") {
                  selectedCountry = "加拿大"
                  selectedPlatform = "Amazon.ca"
                }
                else if (country == "DE") {
                  selectedCountry = "德國"
                  selectedPlatform = "Amazon.de"
                }
                else if (country == "FR") {
                  selectedCountry = "法國"
                  selectedPlatform = "Amazon.fr"
                }
                else if (country == "IT") {
                  selectedCountry = "義大利"
                  selectedPlatform = "Amazon.it"
                }
                else if (country == "ES") {
                  selectedCountry = "西班牙"
                  selectedPlatform = "Amazon.es"
                }
                else if (country == "EU") {
                  selectedCountry = "歐盟"
                  selectedPlatform = "Amazon.eu"
                }
                else if (country == "NA") {
                  selectedCountry = "納米比亞"
                  selectedPlatform = "Amazon.na"
                }
                else if (country == "MX") {
                  selectedCountry = "墨西哥"
                  selectedPlatform = "Amazon.mx"
                }
              }

              val countryskudata = splitdata(1).split("}]\"")

              System.out.println(countryskudata(0))

              var countryskudatas = countryskudata(0).substring(2)

              countryskudatas = countryskudatas.replace("\\\"", "")

              System.out.println(countryskudatas)

              val perskudata = countryskudatas.split("},\\{")

              val perskuList = new Array[Array[String]](perskudata.length)

              val perSKU = new Array[Array[String]](perskuList.length)

              var SkuOfCountry = ""

              var appliedSkuCode = ""

              var appliedSkuData = ""

              var perSKUCountryIndex = 0

              for (k <- 0 until perskudata.length) {
                perskuList(k) = perskudata(k).split(",")
              }

              for (i <- 0 until perskuList.length) {
                perSKU(i) = perskuList(i)(0).split(":")
                if (perSKU(i).length == 1) SkuOfCountry = ""
                else SkuOfCountry = perSKU(i)(1)
                perSKUCountry(perSKUCountryIndex) = SkuOfCountry
                perSKUCountryIndex += 1
              }

              for (m <- 0 until perProductSKUIndex) {
                for (n <- 0 until perSKUCountryIndex) {
                  if (perProductSKU(m)(0) == perSKUCountry(n)) {
                    appliedSkuCode = appliedSkuCode + "\"" + perProductSKU(m)(0) + "\","
                    appliedSkuData = appliedSkuData + perProductSKU(m)(1)
                  }
                }
              }

              if (appliedSkuCode.length > 0) {
                appliedSkuCode = appliedSkuCode.substring(0, appliedSkuCode.length - 1)
                appliedSkuData = appliedSkuData.substring(0, appliedSkuData.length - 1)
              }

              System.out.println(appliedSkuCode)
              System.out.println(appliedSkuData)

              savejson = "{\"name\": \"\",\n" +
                "  \"type\": \"new\",\n" +
                "  \"supplierId\": \"" + supplierId + "\",\n" +
                "  \"selectedProduct\": \"" + basecode + "\",\n" +
                "  \"selectedCountry\": \"" + selectedCountry + "\",\n" +
                "  \"selectedPlatform\": \"" + selectedPlatform + "\",\n" +
                "  \"brandNameEN\": \"" + brandNameEN + "\",\n" +
                "  \"productNameEN\": \"" + productNameEN + "\",\n" +
                "  \"bpId\": \"" + product_Id + "\",\n" +
                "  \"appliedSkuCode\": [" + appliedSkuCode + "],\n" +
                "  \"appliedSku\": [" + appliedSkuData + "]," +
                "  \"subApplication\": {\n" +
                "      \"marketPlaceInfo\":{\n" +
                "		      	\"type\":\"MarketPlace Information\",\n" +
                "		      	\"appliedSku\": [" + appliedSkuData + "]\n" +
                "		      	},\n" +
                "      \"insurance\":{\n" +
                "		      	\"type\":\"Insurance\",\n" +
                "		      	\"appliedSku\": [" + appliedSkuData + "]\n" +
                "	      		},\n" +
                "      \"regional\":{\n" +
                "			      \"type\":\"Regional\",\n" +
                "		      	\"appliedSku\": [" + appliedSkuData + "]\n" +
                "			      },\n" +
                "      \"shipping\":{\n" +
                "     			\"type\":\"Shipping\",\n" +
                "     			\"appliedSku\": [" + appliedSkuData + "]\n" +
                "     			},\n" +
                "      \"productInfo\":{\n" +
                "	      		\"type\":\"Product Advanced Information\"" +
                "     			\"appliedSku\": [" + appliedSkuData + "]\n" +
                "	      		}\n" +
                "  	   },\n" +
                "  \"status\": \"Approved\",\n" +
                "}"

              System.out.println(country)

              System.out.println(savejson)

//              p2MApplicationDao.intoMongodb(savejson)

//              p2MApplicationDao.intoMongodbException(savejson)

              country = ""

            }
          }
        }
      })

//    })

  }







}
