package com.kindminds.drs.data.pipelines

import java.time.{OffsetDateTime, ZoneId, ZoneOffset}

import com.kindminds.drs.data.pipelines.core.dto._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import slick.jdbc.GetResult
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

package object core {

  val DrsConfig = com.typesafe.config.ConfigFactory.load("application.conf")

  lazy val hadoopConf : Configuration ={

    val hadoopConf = new Configuration
    val confPath = DrsConfig.getString("drs.hadoop.conf") + "/hadoop/conf/core-site.xml"
    hadoopConf.addResource(new Path(confPath))

    hadoopConf
  }

  implicit val getOrderResult = GetResult(r => {

    val id:Int =r.nextInt
    val source_id:Int =r.nextInt

    val lud = r.nextTimestamp()
    val last_update_date : Long = (
      if(lud == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
        lud.toInstant.toEpochMilli
       // OffsetDateTime.ofInstant((lud).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
      }
      )


    val ot = r.nextTimestamp()
    val order_time: Long= (
      if(ot == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
        ot.toInstant.toEpochMilli
       // OffsetDateTime.ofInstant((ot).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
      }
      )

    val lot = r.nextTimestamp()
    val local_order_time: Long= (
      if(lot == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
        lot.toInstant.toEpochMilli
        //OffsetDateTime.ofInstant((lot).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
      }
      )

    val tt = r.nextTimestamp()
    val transaction_time:  Long= (
      if(tt == null){
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      }
      else{
        tt.toInstant.toEpochMilli
        //OffsetDateTime.ofInstant((tt).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
      }
      )

    val marketplace_order_id: String=  r.nextString()
    val shopify_order_id: String=  r.nextString()
    val promotion_id: String=  r.nextString()
    val order_status: String=  r.nextString()
    val asin: String=  r.nextString()
    val com_code: String=  r.nextString()
    val base_code: String =  r.nextString()
    val sku_code: String =  r.nextString()
    val sku_code_by_drs: String =  r.nextString()
    val sku_code_by_supplier: String =  r.nextString()

    val product_name: String=  r.nextString()
    val item_price: BigDecimal=  r.nextBigDecimal()
    val actual_retail_price: BigDecimal=  r.nextBigDecimal()
    val actual_shipping_price: BigDecimal=  r.nextBigDecimal()
    val actual_total_shipping_price: BigDecimal=  r.nextBigDecimal()
    val  qty: Int=  r.nextInt()
    val  buyer: String=  r.nextString()
    val buyer_email: String=  r.nextString()
    val sales_channel: String=  r.nextString()
    val fulfillment_center: String=  r.nextString()
    val refund_dt_id: String =  r.nextString()
    val city: String =  r.nextString()
    val state: String=  r.nextString()
    val country: String =  r.nextString()

    Order(id,
      source_id,
      last_update_date ,
      order_time,
      local_order_time,
      transaction_time,
      if(marketplace_order_id== null) "" else marketplace_order_id,
      if( shopify_order_id== null) "" else shopify_order_id,
      if(promotion_id== null) "" else promotion_id,
      if(order_status== null) "" else order_status,
      if(asin== null) "" else asin,
      if(com_code== null) "" else com_code,
      if( base_code== null) "" else base_code,
      if( sku_code== null) "" else sku_code,
      if( sku_code_by_drs== null) "" else sku_code_by_drs,
      if( sku_code_by_supplier == null) "" else sku_code_by_supplier,
      if( product_name== null) "" else product_name.replace("'" , "''"),
      if(item_price == null ) 0 else  item_price,
      if(actual_retail_price == null ) 0 else  actual_retail_price,
      if(actual_shipping_price == null ) 0 else actual_shipping_price,
      if(actual_total_shipping_price == null ) 0 else actual_total_shipping_price,
      if(qty == null ) 0 else  qty,
      if(buyer== null) "" else buyer,
      if(buyer_email== null) "" else buyer_email,
      if(sales_channel== null) "" else sales_channel,
      if(fulfillment_center== null) "" else fulfillment_center,
      if(refund_dt_id== null)  null else refund_dt_id,
      if(city== null) "" else city,
      if(state== null) "" else state,
      if(country== null) "" else country
    )
    //r.nextString, r.nextString)
  })



  implicit val getRetainmentRateResult = GetResult(r => {

    val id:Int =r.nextInt


    val lud = r.nextTimestamp()
    val st : OffsetDateTime = (
      if(lud == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC)
      else{
       // OffsetDateTime.ofInstant((lud).toInstant, ZoneId.of("Asia/Taipei"))
        OffsetDateTime.ofInstant((lud).toInstant, ZoneOffset.UTC)
      }
      )


    val ot = r.nextTimestamp()
    val et: OffsetDateTime= (
      if(ot == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC)
      else{
       // OffsetDateTime.ofInstant((ot).toInstant, ZoneId.of("Asia/Taipei"))
        OffsetDateTime.ofInstant((ot).toInstant, ZoneOffset.UTC)
      }
      )

    /*
       date_start : OffsetDateTime,
                          date_end : OffsetDateTime,
                          country_id: Int,
                          supplier_company_id: Int,
                          revenue_in_original_currency: BigDecimal,
                          currency_exchange_rate_to_usd: BigDecimal,
                          revenue_in_usd: BigDecimal,
                          rate: BigDecimal
     */


    val country_id: Int=  r.nextInt()
    val supplier_company_id: Int=  r.nextInt()
    val original_currency_id: Int=  r.nextInt()
    val revenue_in_original_currency: BigDecimal=  r.nextBigDecimal()
    val currency_exchange_rate_to_usd: BigDecimal=  r.nextBigDecimal()
    val revenue_in_usd: BigDecimal=  r.nextBigDecimal()
    val rate: BigDecimal=  r.nextBigDecimal()


    RetainmentRate(id,
      st ,
      et,
      country_id,
      supplier_company_id,
      original_currency_id,
      revenue_in_original_currency,
      currency_exchange_rate_to_usd,
      revenue_in_usd,
      rate

    )
    //r.nextString, r.nextString)
  })


  implicit val getDailySalesResult = GetResult(r => {


    val id:Int =r.nextInt

    val sd = r.nextTimestamp()
    val sales_date : OffsetDateTime = (
      if(sd == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC)
      else{
        //OffsetDateTime.ofInstant((sd).toInstant, ZoneId.of("Asia/Taipei"))
        OffsetDateTime.ofInstant((sd).toInstant, ZoneOffset.UTC)
      }
      )



    val sales_channel: String =  r.nextString()
    val k_code: String =  r.nextString()
    val product_base_code: String =  r.nextString()
    val product_sku_code: String =  r.nextString()
    val product_name: String =  r.nextString()
    val asin: String  =  r.nextString()
    val revenue: BigDecimal =  r.nextBigDecimal()
    val revenue_usd: BigDecimal  =  r.nextBigDecimal()
    val gross_profit: BigDecimal =  r.nextBigDecimal()
    val gross_profit_usd: BigDecimal =  r.nextBigDecimal()
    val qty: Int =r.nextInt

    val cd = r.nextTimestamp()
    val create_time : OffsetDateTime = (
      if(cd == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC)
      else{
        //OffsetDateTime.ofInstant((cd).toInstant, ZoneId.of("Asia/Taipei"))
        OffsetDateTime.ofInstant((cd).toInstant, ZoneOffset.UTC)
      }
      )

    val ud = r.nextTimestamp()
    val update_time : OffsetDateTime = (
      if(ud == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC)
      else{
        //OffsetDateTime.ofInstant((ud).toInstant, ZoneId.of("Asia/Taipei"))
        OffsetDateTime.ofInstant((ud).toInstant, ZoneOffset.UTC)
      }
      )


    val revenue_excl_pndg: BigDecimal =  r.nextBigDecimal()
    val revenue_usd_excl_pndg: BigDecimal  =  r.nextBigDecimal()
    val gross_profit_excl_pndg: BigDecimal =  r.nextBigDecimal()
    val gross_profit_usd_excl_pndg: BigDecimal =  r.nextBigDecimal()
    val qty_excl_pndg: Int =r.nextInt

    val revenue_excl_refd: BigDecimal =  r.nextBigDecimal()
    val revenue_usd_excl_refd: BigDecimal  =  r.nextBigDecimal()
    val gross_profit_excl_refd: BigDecimal =  r.nextBigDecimal()
    val gross_profit_usd_excl_refd: BigDecimal =  r.nextBigDecimal()
    val qty_excl_refd: Int =r.nextInt


    DailySale(id,
      sales_date ,
      if(sales_channel== null) "" else sales_channel,
      if( k_code== null) "" else k_code,
      if(product_base_code== null) "" else product_base_code,
      if(product_sku_code== null) "" else product_sku_code,
      if(product_name== null) "" else product_name,
      if(asin== null) "" else asin,

      if(revenue == null ) 0 else  revenue,
      if(revenue_usd == null ) 0 else  revenue_usd,
      if(gross_profit == null ) 0 else gross_profit,
      if(gross_profit_usd == null ) 0 else gross_profit_usd,
      if(qty == null ) 0 else  qty,
      create_time,
      update_time,
      if(revenue_excl_pndg == null ) 0 else  revenue_excl_pndg,
      if(revenue_usd_excl_pndg == null ) 0 else  revenue_usd_excl_pndg,
      if(gross_profit_excl_pndg == null ) 0 else gross_profit_excl_pndg,
      if(gross_profit_usd_excl_pndg == null ) 0 else gross_profit_usd_excl_pndg,
      if(qty_excl_pndg == null ) 0 else  qty_excl_pndg,
      if(revenue_excl_refd == null ) 0 else  revenue_excl_refd,
      if(revenue_usd_excl_refd == null ) 0 else  revenue_usd_excl_refd,
      if(gross_profit_excl_refd == null ) 0 else gross_profit_excl_refd,
      if(gross_profit_usd_excl_refd == null ) 0 else gross_profit_usd_excl_refd,
      if(qty_excl_refd == null ) 0 else  qty_excl_refd
    )

  })


  implicit val getCustomercareCaseResult = GetResult(r => {

    val id:Int =r.nextInt
    val issue_id:Int =r.nextInt
    val marketplace_name: String=  r.nextString()
    val marketplace_order_id: String=  r.nextString()

    val _type: String=  r.nextString()
    val status: String=  r.nextString()

    val dc = r.nextTimestamp()
    val date_create : Long = (
      if(dc == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
       // OffsetDateTime.ofInstant((dc).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
        dc.toInstant.toEpochMilli
      }
      )

    val seconds_from_last_activity:Int =r.nextInt

    val lut = r.nextTimestamp()
    val last_update_time : Long = (
      if(dc == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
       // OffsetDateTime.ofInstant((lut).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
        lut.toInstant.toEpochMilli
      }
      )



    val supplier_en_name: String=  r.nextString()
    val supplier_local_name: String=  r.nextString()

    val product_base_code: String=  r.nextString()
    val base_product_name: String=  r.nextString()
    val product_sku_code: String=  r.nextString()
    val sku_product_name: String=  r.nextString()
    val issue_type_name: String =  r.nextString()
    val issue_type_category_name: String =  r.nextString()
    val issue_name: String=  r.nextString()
    val customer_name: String=  r.nextString()

    CustomercareCase(id,
      issue_id,
      if( marketplace_name== null) "" else marketplace_name,
      if(marketplace_order_id== null) "" else marketplace_order_id,
      if( _type== null) "" else _type,
      if( status== null) "" else status,
      date_create,
      seconds_from_last_activity,
      last_update_time,
      if( supplier_en_name== null) "" else supplier_en_name,
      if(supplier_local_name== null) "" else supplier_local_name,
      if(product_base_code== null) "" else product_base_code,
      if(base_product_name== null) "" else base_product_name,
      if(product_sku_code== null) "" else product_sku_code,
      if( sku_product_name== null) "" else sku_product_name,
      if( issue_type_name== null) "" else issue_type_name,
      if( issue_type_category_name== null) "" else issue_type_category_name,
      if(issue_name == null ) "" else  issue_name,
      if(customer_name == null ) "" else  customer_name
    )
    //r.nextString, r.nextString)
  })



  implicit val getCustomercareCaseIssueResult = GetResult(r => {

    val id:Int =r.nextInt
    val status: String=  r.nextString()
    val locale_id:Int =r.nextInt
    val name: String=  r.nextString()
    val issue_type_name: String =  r.nextString()
    val issue_type_category_name: String =  r.nextString()
    val supplier_kcode: String=  r.nextString()
    val supplier_en_name: String=  r.nextString()
    val supplier_local_name: String=  r.nextString()
    val product_base_code: String=  r.nextString()
    val base_product_name: String=  r.nextString()
    val product_sku_code: String=  r.nextString()
    val sku_product_name: String=  r.nextString()


    val dc = r.nextTimestamp()
    val date_create : Long = (
      if(dc == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
       // OffsetDateTime.ofInstant((dc).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
        dc.toInstant.toEpochMilli

      }
      )

    val seconds_from_last_update:Int =r.nextInt

    val lut = r.nextTimestamp()
    val last_update_time : Long = (
      if(dc == null)
        OffsetDateTime.of(1900,1,1 ,0,0,0,0,ZoneOffset.UTC).toInstant.toEpochMilli
      else{
        //OffsetDateTime.ofInstant((lut).toInstant, ZoneId.of("Asia/Taipei")).toInstant.toEpochMilli
        lut.toInstant.toEpochMilli
      }
      )



    CustomercareCaseIssue(id,
      if( status== null) "" else status,
      locale_id  ,
      if( name== null) "" else name,
      if( issue_type_name== null) "" else issue_type_name,
      if( issue_type_category_name== null) "" else issue_type_category_name,
      if( supplier_kcode== null) "" else supplier_kcode,
      if( supplier_en_name== null) "" else supplier_en_name,
      if(supplier_local_name== null) "" else supplier_local_name,
      if(product_base_code== null) "" else product_base_code,
      if(base_product_name== null) "" else base_product_name,
      if(product_sku_code== null) "" else product_sku_code,
      if( sku_product_name== null) "" else sku_product_name,
      date_create,
      seconds_from_last_update,
      last_update_time

    )
    //r.nextString, r.nextString)
  })

  implicit val getOrderViewResult = GetResult(r => {

    val id:Int =r.nextInt

    val last_update_date  =  r.nextTimestamp()
    val order_time = r.nextTimestamp()
    val transaction_time =  r.nextTimestamp()

    val marketplace_order_id: String=  r.nextString()
    val shopify_order_id: String=  r.nextString()
    val promotion_id: String=  r.nextString()

    val order_status: String=  r.nextString()
    val asin: String=  r.nextString()
    val com_code: String=  r.nextString()
    val base_code: String =  r.nextString()

    val sku_code: String =  r.nextString()
    val sku_code_by_drs: String =  r.nextString()
    val sku_code_by_supplier :String =  r.nextString()

    val product_name: String=  r.nextString()
    val item_price: BigDecimal=  r.nextBigDecimal()
    val actual_retail_price: BigDecimal=  r.nextBigDecimal()

    val actual_shipping_price: BigDecimal=  r.nextBigDecimal()
    val actual_total_shipping_price: BigDecimal=  r.nextBigDecimal()

    val  qty: Int=  r.nextInt()
    val  buyer: String=  r.nextString()
    val buyer_email: String=  r.nextString()

    val sales_channel: String=  r.nextString()

    val fulfillment_center: String=  r.nextString()

    val refund_dt_id: Int=  r.nextInt()
    val city: String =  r.nextString()
    val state: String=  r.nextString()

    val country: String =  r.nextString()

    //println(country)

    OrderView(id,
      last_update_date ,
      order_time,
      transaction_time,
      if(marketplace_order_id== null) "" else marketplace_order_id,
      if( shopify_order_id== null) "" else shopify_order_id,
      if(promotion_id== null) "" else promotion_id,
      if(order_status== null) "" else order_status,
      if(asin== null) "" else asin,
      if(com_code== null) "" else com_code,
      if( base_code== null) "" else base_code,
      if( sku_code== null) "" else sku_code,
      if( sku_code_by_drs== null) "" else sku_code_by_drs,
      if( sku_code_by_supplier == null) "" else sku_code_by_supplier,
      if( product_name== null) "" else product_name,
      if(item_price == null ) 0 else  item_price,
      if(actual_retail_price == null ) 0 else  actual_retail_price,
      if(actual_shipping_price == null ) 0 else actual_shipping_price,
      if(actual_total_shipping_price == null ) 0 else actual_total_shipping_price,
      if(qty == null ) 0 else  qty,
      if(buyer== null) "" else buyer,
      if(buyer_email== null) "" else buyer_email,
      if(sales_channel== null) "" else sales_channel,
      if(fulfillment_center== null) "" else fulfillment_center,
      if(refund_dt_id== null) 0 else refund_dt_id,
      if(city== null) "" else city,
      if(state== null) "" else state,
      if(country== null) "" else country
    )
    //r.nextString, r.nextString)
  })

  implicit val getEarlyReviewIntTranResult = GetResult(r => {

    val id:Int = r.nextInt
    val marketplace_id:Int = r.nextInt
    val inserted_on = r.nextTimestamp()
    val date_time = r.nextTimestamp()
    val description: String = r.nextString
    val marketplace: String = r.nextString
    val other_transaction_fees: BigDecimal = r.nextBigDecimal()
    val total: BigDecimal = r.nextBigDecimal()

    EarlyReviewIntTran(
      id,
      marketplace_id,
      inserted_on,
      date_time,
      description,
      marketplace,
      other_transaction_fees,
      total
    )
  })

  implicit val getSettlementDatesResult = GetResult(r => {

    val id:Int = r.nextInt
    val period_start = r.nextTimestamp()
    val period_end = r.nextTimestamp()

    SettlementDates(
      id,
      period_start,
      period_end
    )
  })


  object OrderJsonProtocol extends DefaultJsonProtocol {
    implicit object OrderJsonFormat extends RootJsonFormat[Order] {
      def write(o: Order) = JsObject(

        "id" ->   JsNumber(o.id),
        "source_id" ->   JsNumber(o.source_id),
        "last_update_date" ->  JsNumber(o.last_update_date),
        "order_time" ->   JsNumber(o.order_time),
        "local_order_time" ->   JsNumber(o.local_order_time),
        "transaction_time" -> JsNumber(o.transaction_time),
        //JsNumber(o.transaction_time.toInstant.toEpochMilli),
        "marketplace_order_id" -> JsString(o.marketplace_order_id),
        "shopify_order_id" ->  JsString(o.shopify_order_id),
        "promotion_id" ->    JsString(o.promotion_id),
        "order_status" ->  JsString(o.order_status),
        "asin" ->  JsString(o.asin),
        "com_code" ->  JsString(o.com_code),
        "base_code" ->  JsString(o.base_code),
        "sku_code" ->   JsString(o.sku_code),
        "sku_code_by_drs" ->   JsString(o.sku_code_by_drs),
        "sku_code_by_supplier" ->   JsString(o.sku_code_by_supplier),
        "product_name" ->   JsString(o.product_name),
        "item_price" ->  JsNumber(o.item_price),
        "actual_retail_price" ->  JsNumber(o.actual_retail_price),
        "actual_shipping_price" ->  JsNumber(o.actual_shipping_price),
        "actual_total_shipping_price" ->  JsNumber(o.actual_total_shipping_price),
        "qty" ->  JsNumber(o.qty),
        "buyer" ->  JsString(o.buyer),
        "buyer_email" -> JsString(o.buyer_email),
        "sales_channel" ->  JsString(o.sales_channel),
        "fulfillment_center" -> JsString(o.fulfillment_center),
        "refund_dt_id" -> JsString(if(o.refund_dt_id==null)""else o.refund_dt_id),
        "city" -> JsString(o.city),
        "state" -> JsString(o.state),
        "country" -> JsString(o.country),
      )

      def read(value: JsValue) = {
        value.asJsObject.getFields("name", "red", "green", "blue") match {
          case Seq(JsString(name), JsNumber(red), JsNumber(green), JsNumber(blue)) =>
            //new Color(name, red.toInt, green.toInt, blue.toInt)
            null
          /*
          *    def read(value: JsValue) = value match {
      case
        JsArray(Vector
          (
          JsNumber(id),
          JsString(last_update_date)
          ,JsString(order_time)
          ,JsString(transaction_time)
          ,JsString(marketplace_order_id)
          ,JsString(shopify_order_id)
          ,JsString(promotion_id)
          ,JsString(order_status)
          ,JsString(asin)
          ,JsString(com_code)
          ,JsString(base_code)
          ,JsString(sku_code)
          ,JsString(product_name)
          ,JsNumber(item_price)
          ,JsNumber(actual_retail_price)
          ,JsNumber(qty)
          ,JsString(buyer)
          ,JsString(buyer_email)
          ,JsString(sales_channel)
          ,JsString(fulfillment_center)
          ,JsString(refund_dt_id)
          ,JsString(city)
        )) => {

        println("BBBBBBBBBBBBB")
        new Order(id.toInt, last_update_date, order_time, transaction_time ,
          marketplace_order_id, shopify_order_id, promotion_id ,order_status,
          asin, com_code ,  base_code, sku_code, product_name ,item_price, actual_retail_price, qty.toInt ,
          buyer, buyer_email, sales_channel ,fulfillment_center,refund_dt_id,city            )

      }*/
          case _ => throw new DeserializationException("Color expected")
        }
      }
    }
  }


}
