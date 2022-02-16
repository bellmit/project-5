package com.kindminds.drs.api.message.command

package notification {

  import com.kindminds.drs.Notification
  import com.kindminds.drs.api.message.Command

  case class SendNotification(topic:String ,content:String , refUrl:String,userId:Int) extends Command

  case class SendNotificationToWebFE(content:String , userId:Int) extends Command

  case class SendUnreadNotificationCount(count:Int) extends Command

  case class MarkNotificationAsRead(id:String) extends Command

  case class MarkNotificationAsUnRead(id:String) extends Command

  case class MarkAllNotificationsAsRead(userId:Int) extends Command

  case class DeleteNotification(id:String) extends Command


}
