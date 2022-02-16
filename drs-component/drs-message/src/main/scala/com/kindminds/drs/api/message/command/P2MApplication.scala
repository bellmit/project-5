package com.kindminds.drs.api.message.command

package p2m {

  import com.kindminds.drs.api.message.Command

  case class ChangeP2MApplication(p2mId:String , userId:Int) extends Command

  case class ReApplyP2MApplication(p2mId:String , userId:Int) extends Command

  case class UpdateP2MApplication(p2mId:String , p2mApplication:String , skuInfo:String , userId:Int) extends Command

  case class CreateP2MApplication(p2mApplication:String , userId:Int) extends Command

  case class SubmitP2MApplication(p2mApplication:String,userId:Int , kcode :String) extends Command

  case class DeleteP2MApplication(p2mId:String , userId:Int , kcode :String) extends Command

  case class ApplyToRemoveP2MApplication(p2mId:String , userId:Int , kcode :String) extends Command

  case class ApproveToRemoveP2MApplication(p2mApplication:String,userId:Int) extends Command

  case class RejectToRemoveP2MApplication(p2mApplication:String , userId:Int) extends Command

  case class CreateApplicationNumber() extends Command

  case class SaveMarketplaceInfo(marketplaceInfo:String) extends Command

  case class SaveInsurance(insurance:String) extends Command

  case class SaveRegional(regional:String) extends Command

  case class SaveShipping(shipping:String) extends Command

  case class SaveP2MProductInfo(p2mProductInfo:String) extends Command

  case class SaveImageFilePath() extends Command

  case class ApproveP2MApplication(p2mApplication:String,userId:Int) extends Command

  case class RejectP2MApplication(p2mApplication:String , userId:Int) extends Command




}
