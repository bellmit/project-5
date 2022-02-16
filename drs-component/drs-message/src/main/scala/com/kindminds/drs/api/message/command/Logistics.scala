package com.kindminds.drs.api.message.command


package ivs {

  import com.kindminds.drs.api.message.Command
  import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus


  case class InitVerifyIvsProductInfo(ivsNumber:String , boxNum:Int, mixedBoxLineSeq: Int) extends Command

  case class ConfirmIvsProductVerifyInfo(ivsNumber:String , boxNum:Int, mixedBoxLineSeq: Int , docReq : String) extends Command

  case class RejectIvsProductVerifyInfo(ivsNumber:String , boxNum:Int, mixedBoxLineSeq: Int , docReq : String) extends Command

  case class UpdateIvsStatus(ivsNumber:String , status : Option[ShipmentStatus],
                             sku:String, boxNum:Int) extends  Command


}