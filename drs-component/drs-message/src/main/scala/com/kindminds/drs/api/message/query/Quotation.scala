package com.kindminds.drs.api.message.query

import com.kindminds.drs.api.message.Query

package  quotation {

  case class GetQuotation(companyKcode : String ,pageIndex:Int) extends Query

  case class GetQuoteRequestView(requestId : String) extends Query

}

