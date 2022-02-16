package com.kindminds.drs.api.message.command

package productCategory {

  import com.kindminds.drs.api.message.Command

  case class SaveProductCategory(productCategory:String) extends Command

}