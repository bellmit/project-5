package com.kindminds.drs.api.message.query

package customer {

  import com.kindminds.drs.api.message.Query

  case class GetReturns(companyKcode :  Option[String] , marketPlace :  Option[String] ,
                        productBaseCode :  Option[String] , productSkuCode : Option[String] )
    extends Query

}
