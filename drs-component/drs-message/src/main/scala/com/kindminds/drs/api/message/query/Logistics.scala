package com.kindminds.drs.api.message.query

package ivs {

  import com.kindminds.drs.api.message.Query


  case class GetIvsProductVerifyInfo(ivsName:String , sku:String, destination:String) extends Query

  case class GetIvsNumbers(kcode:String) extends Query

  case class GetIvsLineitemSku(ivsNumber:String) extends  Query

  case class GetBoxNumbers(ivsNumber:String , sku :String) extends  Query

  case class GetIvsLineitemProdVerificationStatus(ivsNumber:String , boxNum:Int, mixedBoxLineSeq: Int) extends Query


  case class GetIvsProdDocRequirement(ivsNumber:String , boxNum:Int, mixedBoxLineSeq: Int ) extends Query

  case class GetShipmentLineItem(ivsName:String , boxNum:Int, mixedBoxLineSeq: Int) extends Query

  case class GetExpectedShippingDate(ivsName:String) extends Query

  case class GetIvsShippingCosts(ivsName:String) extends Query



}