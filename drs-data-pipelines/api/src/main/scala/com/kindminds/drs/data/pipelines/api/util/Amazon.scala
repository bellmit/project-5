package com.kindminds.drs.data.pipelines.api.util

object Amazon {

  val marketPlaceDn = Map(
    //"us" -> "www.amazon.com - TrueToSource US",
    "us" -> "www.amazon.com",
    "k619-us" -> "www.amazon.com - LP SUPPORT",
    "k621-us" -> "www.amazon.com - SecuX Tech",
    "k620-us" -> "www.amazon.com - ikloo",
    "k626-us" -> "www.amazon.com - Alpha Autotect",
    "k627-us" -> "www.amazon.com - V-LAB",
  "ca" -> "www.amazon.ca",
  "uk" -> "www.amazon.co.uk" ,
  "de" -> "www.amazon.de",
  "fr" -> "www.amazon.fr",
  "it" -> "www.amazon.it" ,
  "es" -> "www.amazon.es" )

  val drsMarketPlaceId = Map("us" -> "1",
  "ca" -> "5",
  "uk" -> "4" ,
  "de" -> "6",
  "fr" -> "7",
  "it" -> "8" ,
  "es" -> "9" )

  val drsWarehouseId = Map("us" -> "101",
  "ca" -> "103",
  "uk" -> "102")

}

