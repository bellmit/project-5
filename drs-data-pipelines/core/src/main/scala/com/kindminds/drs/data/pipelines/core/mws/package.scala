package com.kindminds.drs.data.pipelines.core

import com.kindminds.drs.Marketplace

package object mws {


  val manageFBAInventoryPath = Map(
    Marketplace.AMAZON_COM -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Manage FBA Inventory",
    Marketplace.AMAZON_CA -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Manage FBA Inventory",
    Marketplace.AMAZON_CO_UK -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Manage FBA Inventory",
    Marketplace.AMAZON_DE -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Manage FBA Inventory",
    Marketplace.AMAZON_FR -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Manage FBA Inventory",
    Marketplace.AMAZON_IT -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Manage FBA Inventory",
    Marketplace.AMAZON_ES -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Manage FBA Inventory" )



  val monthlyStorageFeePath = Map(Marketplace.AMAZON_COM -> "hdfs://kindminds01:9000/user/Amazon/US/Monthly Storage Fee",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Monthly Storage Fee",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Monthly Storage Fee")

  val longTermStorageFeePath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Long Term Storage Fee",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Long Term Storage Fee",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Long Term Storage Fee" )

  val allStatementsPath = Map(Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/All Statements",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/All Statements",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/All Statements",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/All Statements",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/All Statements",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/All Statements",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/All Statements")

  val paymentsDateRangePath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Payments Date Range",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Payments Date Range",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Payments Date Range",
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Payments Date Range",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Payments Date Range",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Payments Date Range",
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Payments Date Range")

  val customerReturnPath = Map(Marketplace.AMAZON_COM -> "hdfs://kindminds01:9000/user/Amazon/US/Customer Return",
    Marketplace.AMAZON_CA -> "hdfs://kindminds01:9000/user/Amazon/CA/Customer Return",
    Marketplace.AMAZON_CO_UK -> "hdfs://kindminds01:9000/user/Amazon/UK/Customer Return" )



  val inventoryRptPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Inventory Report",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Inventory Report",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Inventory Report",
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Inventory Report",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Inventory Report",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Inventory Report",
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Inventory Report" )

  val inventoryEventDetailRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Inventory Event Detail",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Inventory Event Detail",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Inventory Event Detail",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Inventory Event Detail",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Inventory Event Detail",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Inventory Event Detail",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Inventory Event Detail" )

  val dailyInventoryRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Daily Inventory History",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Daily Inventory History",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Daily Inventory History",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Daily Inventory History",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Daily Inventory History",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Daily Inventory History",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Daily Inventory History" )

  val customerShipmentSalesRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Shipment/Customer Shipment Sales",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Shipment/Customer Shipment Sales",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Shipment/Customer Shipment Sales",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/Shipment/Customer Shipment Sales",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/Shipment/Customer Shipment Sales",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/Shipment/Customer Shipment Sales",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/Shipment/Customer Shipment Sales" )


  val countryFbaReportsPath = Map(
    Marketplace.AMAZON_COM -> "C:/Users/HyperionFive/Desktop/user/Amazon/US/FBA_Reports/",
//    Marketplace.AMAZON_CA -> "C:/Users/HyperionFive/Desktop/user/Amazon/CA/FBA_Reports/",
//    Marketplace.AMAZON_DE -> "C:/Users/HyperionFive/Desktop/user/Amazon/DE/FBA_Reports/",
    Marketplace.AMAZON_CO_UK -> "C:/Users/HyperionFive/Desktop/user/Amazon/UK/FBA_Reports/"
  )


  val countryInventoryReportsPath = Map(
    Marketplace.AMAZON_COM -> "C:/Users/HyperionFive/Desktop/user/Amazon/US/Inventory_Reports/",
//    Marketplace.AMAZON_CA -> "C:/Users/HyperionFive/Desktop/user/Amazon/CA/Inventory_Reports/",
//    Marketplace.AMAZON_DE -> "C:/Users/HyperionFive/Desktop/user/Amazon/DE/Inventory_Reports/",
    Marketplace.AMAZON_CO_UK -> "C:/Users/HyperionFive/Desktop/user/Amazon/UK/Inventory_Reports/"
  )

  val receivedInventoryRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Received Inventory",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Received Inventory",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Received Inventory",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Received Inventory",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Received Inventory",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Received Inventory",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Received Inventory" )

  val reservedInventoryRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Reserved Inventory",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Reserved Inventory",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Reserved Inventory",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Reserved Inventory",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Reserved Inventory",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Reserved Inventory",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Reserved Inventory" )

  val replacementsRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Replacements",
    Marketplace.AMAZON_CA  -> "hdfs://kindminds01:9000/user/Amazon/CA/Replacements",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Replacements",
    Marketplace.AMAZON_DE  -> "hdfs://kindminds01:9000/user/Amazon/DE/Replacements",
    Marketplace.AMAZON_FR  -> "hdfs://kindminds01:9000/user/Amazon/FR/Replacements",
    Marketplace.AMAZON_IT  -> "hdfs://kindminds01:9000/user/Amazon/IT/Replacements",
    Marketplace.AMAZON_ES  -> "hdfs://kindminds01:9000/user/Amazon/ES/Replacements" )

  val reimbursementsRptPath = Map(
    Marketplace.AMAZON_COM  -> "hdfs://kindminds01:9000/user/Amazon/US/Reimbursements",
    Marketplace.AMAZON_CO_UK  -> "hdfs://kindminds01:9000/user/Amazon/UK/Reimbursements" )

  val submitProductListingPath = "C:/Users/HyperionFive/Desktop/user/"







}
