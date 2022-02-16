package com.kindminds.drs.api.message.command


  import com.kindminds.drs.api.message.Command


  //  returns the _id of the created Document as a String
  case class CreateMarketingActivity(activityJson: String) extends Command

  //  returns the _id of the created Document as a String
  case class UpdateMarketingActivity(activityJson : String) extends Command

  //  returns the number of records deleted
  case class DeleteMarketingActivity(activityId : String) extends Command


case class SendBuyBoxReminder() extends Command