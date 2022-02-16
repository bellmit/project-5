package com.kindminds.drs.rt

import com.kindminds.drs.api.message.Command
import java.util.List

import com.kindminds.drs.api.v1.model.product.AmazonAsin
import org.openqa.selenium.WebDriver


  case class ImportAmazonReviews(amazonReviewList: String) extends Command

  case class ScrapeAmazonReviews(amazonAsinSnip : List[AmazonAsin] , masterDriver: WebDriver,
                                 itemDriver: WebDriver) extends Command

  case class ScrapeAmazonReview(amazonAsi : AmazonAsin , masterDriver: WebDriver,
                                itemDriver: WebDriver) extends Command

  case class Complete(masterDriver: WebDriver,
                      itemDriver: WebDriver) extends Command

  case class Start() extends Command