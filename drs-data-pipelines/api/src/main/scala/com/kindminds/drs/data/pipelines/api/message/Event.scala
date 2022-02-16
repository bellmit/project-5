package com.kindminds.drs.data.pipelines.api.message

import com.kindminds.drs.data.pipelines.api.common.ReportType

trait Event{
  val topic : Events.Topic
}

object Events extends Enumeration {
  type Topic = Value
  val AutoProcess , AutoProcessFailed = Value
}


case class ProcessReportStarted(marketPlaceId:Int ,
                                scheduledDate:java.time.LocalDate,
                                reportType : ReportType.Val ,
                                topic:Events.Topic = Events.AutoProcess) extends Event

case class ProcessReportStartFailed(marketPlaceId:Int ,
                                    scheduledDate:java.time.LocalDate,
                                     reportType : ReportType.Val ,
                                    topic:Events.Topic = Events.AutoProcessFailed) extends Event

case class ReportDownloaded(marketPlaceId:Int ,
                                scheduledDate:java.time.LocalDate,
                                reportType : ReportType.Val ,
                                topic:Events.Topic = Events.AutoProcess) extends Event

case class ReportDownloadFailed(marketPlaceId:Int ,
                            scheduledDate:java.time.LocalDate,
                            reportType : ReportType.Val ,
                            topic:Events.Topic = Events.AutoProcessFailed) extends Event



case class ReportValidated(marketPlaceId:Int ,
                            scheduledDate:java.time.LocalDate,
                            reportType : ReportType.Val ,
                            topic:Events.Topic = Events.AutoProcess) extends Event

case class ReportValidateFailed(marketPlaceId:Int ,
                                scheduledDate:java.time.LocalDate,
                                reportType : ReportType.Val ,
                                topic:Events.Topic = Events.AutoProcessFailed) extends Event



case class ReportSavedToHDFS(marketPlaceId:Int ,
                           scheduledDate:java.time.LocalDate,
                           reportType : ReportType.Val ,
                           topic:Events.Topic = Events.AutoProcess) extends Event

case class ReportSaveToHDFSFailed(marketPlaceId:Int ,
                                scheduledDate:java.time.LocalDate,
                                reportType : ReportType.Val ,
                                topic:Events.Topic = Events.AutoProcessFailed) extends Event



case class ReportImportedToDRSDB(marketPlaceId:Int ,
                             scheduledDate:java.time.LocalDate,
                             reportType : ReportType.Val ,
                             topic:Events.Topic = Events.AutoProcess) extends Event

case class ReportImportToDRSDBFailed(marketPlaceId:Int ,
                                  scheduledDate:java.time.LocalDate,
                                  reportType : ReportType.Val ,
                                  topic:Events.Topic = Events.AutoProcessFailed) extends Event


case class ReportImportedToHBASE(marketPlaceId:Int ,
                                 scheduledDate:java.time.LocalDate,
                                 reportType : ReportType.Val ,
                                 topic:Events.Topic = Events.AutoProcess) extends Event

case class ReportImportToHBASEFailed(marketPlaceId:Int ,
                                     scheduledDate:java.time.LocalDate,
                                     reportType : ReportType.Val ,
                                     topic:Events.Topic = Events.AutoProcessFailed) extends Event



