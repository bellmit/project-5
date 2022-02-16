package com.kindminds.drs.api.message


trait Command



package calculateMonthlyStorageFeeUco{

  case class GetYears() extends Command

  case class GetMonths() extends Command

  case class Calculate (year:String , month :String ) extends Command

  case class CalculateSumOfTotalEstimatedMonthlyStorageFee (supplierKcode: String, country: String, year: String, month: String) extends Command

}


package importAmazonDateRangeReportUco {

  case class GetMarketplaces() extends Command

  case class GetImportStatus() extends Command

  case class ImportReport(marketplaceId: Integer, fileBytes: Array[Byte]) extends Command

}

package AddProductSkuAsinUco{

  // return : List<Marketplace>
  case class GetMarketplaces() extends Command

  // return : Map<String, String>
  case class GetKcodeToSupplierName() extends Command

  // return : List<SkuFnskuAsin>
  case class GetSkuToAsin(marketplaceIdText: String, splrKcode: String) extends Command

  // return : List<SkuWithoutAsin>
  case class GetMarketplaceToSku() extends Command

  // return : String
  case class AddFbaData(fileData: Array[Byte], marketplaceId: Int) extends Command

  // return : String
  case class AddInventoryData(fileData: Array[Byte], marketplaceId: Int) extends Command

  // return : void
  case class ToggleStorageFeeFlag(marketplaceId: Int, marketplaceSku: String) extends Command

}

package manageFbaInventoryFileImporter {

  case class ImportFbaInventoryFile(fileData: Array[Byte], fileName: String) extends Command

}


package emailReminderUco {

  case class GetIncludedSuppliers() extends Command

  case class GetExcludedSuppliers() extends Command

  case class UpdateLongTermStorageReminder(kCodes: String) extends Command

  case class GetSuppliersOverFeeLimit(kCodes: String) extends Command

  case class GetFeeToSendReminder() extends Command

  case class UpdateFeeToSendReminder(limit: Double) extends Command

  case class SendLTSFReminderToCurrentUser(currentUserID: Integer) extends Command

  case class AutomateSendLTSFReminder() extends Command
}

package confirmMarketingMaterialUco {

  case class AutoRenotifySuppliers() extends Command


  case class SendEmail(emailType: Int, marketplace: String, productBaseCode: String, productCodeName: String, supplierKCode: String) extends Command

  case class AddComment(userId: Int, marketplace: String, productBaseCode: String, supplierKcode: String, contents: String, baseCodeAndName: String) extends Command

  case class GetComments(marketplaceId: Int, productBaseCode: String) extends Command

}

package importSellback {

  case class ImportSellbackTransactions() extends Command

}

package logistics {

  import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs
  import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs


  //todo need to refactor here arthur

  case class VerifyProduct(shipment : Ivs, sku : String,
                                    boxNum: Integer, mixedBoxLineSeq: Integer) extends Command

  case class RejectProduct(shipment : Ivs, sku : String,
                           boxNum: Integer, mixedBoxLineSeq: Integer) extends Command

  case class ConfirmProduct(shipment : Ivs, sku : String,
                           boxNum: Integer, mixedBoxLineSeq: Integer) extends Command

}

package calculateRetainmentRateUco{



}

package  viewKeyProductStatsUco{

  case class TransformData() extends Command

}

package util {
  case class MailMessage(to: String, subject: String, body: String) extends Command

  case class MailMessageWithBCC(to: String, cc: String, subject: String, body: String) extends Command
}

package process{

  import com.kindminds.drs.UserTaskType

  case class CreateUserTask(userTaskType:UserTaskType , refId : String , kcode : String) extends Command

}
















