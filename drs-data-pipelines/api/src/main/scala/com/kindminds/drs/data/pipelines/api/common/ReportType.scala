package com.kindminds.drs.data.pipelines.api.common

object ReportType extends Enumeration {

  case class Val(override val id: Int, name: String) extends super.Val {
    //def surfaceGravity: Double = Planet.G * mass / (radius * radius)
    //def surfaceWeight(otherMass: Double): Double = otherMass * surfaceGravity
  }

  implicit def valueToRTVal(x: Value): Val = x.asInstanceOf[Val]


  val DailyCampaignPerformance= Val(1 ,"Daily Campaign Performance")
  val SalesNumCampaignPerformance= Val(2 ,"Sales Num Campaign Performance")

  val DailySearchTerm= Val(3 ,"Daily SearchTerm")
  val TotalSearchTerm= Val(4 ,"Total SearchTerm")

  val HSAKeyword= Val(5 ,"HSA Keyword")
  val HSACampaign= Val(6 ,"HSA Campaign")

  val DailyPageTraffic= Val(7 ,"Daily Page Traffic")
  val BiWeeklyPageTraffic= Val(8 ,"BiWeekly Page Traffic")

  val PaymentsAllStatementsV2= Val(9 ,"Payments AllStatements V2")
  val CustomerReturn= Val(10 ,"Customer Return")

  val ManageFBAInventory= Val(11 ,"Manage FBA Inventory")
  val Inventory= Val(12 ,"Inventory")
  val PaymentsDateRange= Val(13 ,"Payments Date Range")

  val MonthlyStorageFees= Val(14 ,"Monthly StorageFees")
  val LongTermStorageFee= Val(15 ,"Long Term StorageFee")

  val PurchasedProduct= Val(16 ,"Purchased Product")
  val SPKeyword= Val(17 ,"SP Keyword")

  //All orders?


}





