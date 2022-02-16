package com.kindminds.drs.api.message.command

import com.kindminds.drs.api.message.Command


package fee {

  case class ActivateNewServiceFee(quotationId:String) extends Command

  case class ActivateServiceFee(feeId:String) extends Command

  case class DeactivateServiceFee(feeId:String) extends Command

}

package quotation{

  //bpSummary format example: 5 BPs x 6 Selling Regions
  case class RequestServiceFeeQuotation4OnboardingProd(onboardingApplicationId:String) extends Command

  case class CreateServiceFeeQuotation(quoteRequestId : String,
                                       reducedPrice : String,
                                       currentUser : String) extends Command

  case class ModifyServiceFeeQuotation(quoteRequestId : String,
                                       reducedPrice : String,
                                       currentUser : String) extends Command

  case class RejectServiceFeeQuotation(quoteRequestId : String) extends Command

  case class AcceptServiceFeeQuotation(quoteRequestId : String) extends Command

  case class ConfirmServiceFeeQuotation(quoteRequestId : String) extends Command

}

