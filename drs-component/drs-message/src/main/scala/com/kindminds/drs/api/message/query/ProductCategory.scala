package com.kindminds.drs.api.message.query

package productCategory {

  import com.kindminds.drs.api.message.Query

  case class GetProductCategory(parent:String) extends Query

  case class GetListByParent(parent:String) extends Query

}