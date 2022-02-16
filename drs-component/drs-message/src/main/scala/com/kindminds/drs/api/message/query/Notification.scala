package com.kindminds.drs.api.message.query

package notification {

  import com.kindminds.drs.api.message.Query

  case class GetNotifications(userId: Int ,pageIndex:Int) extends Query


}