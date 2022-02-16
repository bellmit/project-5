package com.kindminds.drs.core.actors.handlers.query.product

import java.time.{LocalDateTime, OffsetDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs._
import com.kindminds.drs.api.message.query.prdocut.dashboard._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.core.biz.customercare.repo.CustomerCareCaseRepoImpl
import com.kindminds.drs.core.biz.marketing.repo.CampaignSetRepoImpl
import com.kindminds.drs.core.biz.sales.repo.DailySalesSetRepoImpl
import java.util.{Date, Optional}

import com.kindminds.drs.api.data.access.rdb.order.DailyOrderDao
import com.kindminds.drs.api.data.access.rdb.product.ViewKeyProductStatsDaoV2
import com.kindminds.drs.api.data.access.rdb.sales.DetailPageSalesTrafficDao
import com.kindminds.drs.api.message.viewKeyProductStatsUco.{GetKeyProductBaseRetailPrice, GetKeyProductInventoryStats, GetKeyProductLastSevenDaysOrder, GetKeyProductSinceLastSettlementOrder}
import com.kindminds.drs.util.DateHelper



object ProductDashboardViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new ProductDashboardViewHandler(drsQueryBus))

}


class ProductDashboardViewHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val vkpsDao = BizCoreCtx.get().getBean(classOf[ViewKeyProductStatsDaoV2])
  val dpstDao = BizCoreCtx.get().getBean(classOf[DetailPageSalesTrafficDao])
  val om = new ObjectMapper();
  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetDailySalesQtyAndRev].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetMTDCustomerCareCases].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetMTDCampaignSpendAndAcos].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetDailyOrder].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetKeyProductInventoryStats].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetKeyProductSinceLastSettlementOrder].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetKeyProductLastSevenDaysOrder].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetKeyProductBaseRetailPrice].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetDetailPageSalesTraffic].getName, self)


  def receive = {

    case d : GetDailySalesQtyAndRev =>


      val f = new Filter
      f.addCriteria(new Criteria(DailySalesQueryField.KCode , d.companyKcode.getOrElse("All")))

      val today = LocalDateTime.now().minusDays(1)
        .withHour(0).withMinute(0).
        withSecond(0).withNano(0)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

      //println(today)

        f.addCriteria(new Criteria(DailySalesQueryField.SalesDate ,today))


      if(d.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(DailySalesQueryField.BpCode , d.productBaseCode.get))
      }

      if(d.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(DailySalesQueryField.SkuCode , d.productSkuCode.get))
      }

      if(d.marketPlace.getOrElse("")!= ""){
        if(d.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(DailySalesQueryField.Marketplace ,
            SalesChannel.fromKey(d.marketPlace.get.toInt).getDisplayName))
        }
      }


      val repo = new DailySalesSetRepoImpl
      val dsSet = repo.findOne(f)

      val qty = dsSet.get().sumTotalQty()
      val rev = dsSet.get().sumTotalRevenue()

      //println(qty)

      this.sender() ! "{\"qty\":\""+ qty +"\" , \"rev\":\""+ rev.setScale(2, BigDecimal.RoundingMode.HALF_UP) +"\"}"

      //println("{\"qty\":\""+ qty +"\" , \"rev\":\""+ rev.setScale(2, BigDecimal.RoundingMode.HALF_UP) +"\"}")


    case dr : GetDailyReviews =>

    case ccc: GetMTDCustomerCareCases =>

      val repo = new CustomerCareCaseRepoImpl
      val f = new Filter
      f.addCriteria(new Criteria(CustomerCareCaseQueryField.KCode , ccc.companyKcode.getOrElse("All")))

      val today =  LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)

      f.addCriteria(new Criteria(CustomerCareCaseQueryField.EndDate ,today.plusDays(1).format(DateTimeFormatter.
        ofPattern("yyyy-MM-dd HH:mm:ss"))))

      f.addCriteria(new Criteria(CustomerCareCaseQueryField.StartDate ,today
        .withDayOfMonth(1).format(DateTimeFormatter.
        ofPattern("yyyy-MM-dd HH:mm:ss"))))

      val cList = repo.find(f)

    case drq: GetDailyReturnsQty =>

    case ca : GetMTDCampaignSpendAndAcos =>

      val repo = new CampaignSetRepoImpl
      val f = new Filter

      f.addCriteria(new Criteria(CampaignQueryField.KCode , ca.companyKcode.getOrElse("All")))


      val today = LocalDateTime.now().withHour(0).withMinute(0).
        withSecond(0).withNano(0)

      f.addCriteria(new Criteria(CampaignQueryField.EndDate
        ,today.plusDays(1).format(DateTimeFormatter.
        ofPattern("yyyy-MM-dd HH:mm:ss"))))

      f.addCriteria(new Criteria(CampaignQueryField.StartDate ,today
        .withDayOfMonth(1).format(DateTimeFormatter.
        ofPattern("yyyy-MM-dd HH:mm:ss"))))


      if(ca.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(CampaignQueryField.BpCode , ca.productBaseCode.get))
      }

      if(ca.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(CampaignQueryField.SkuCode , ca.productSkuCode.get))
      }

      if(ca.marketPlace.getOrElse("")!= ""){
        if(ca.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(CampaignQueryField.Marketplace , ca.marketPlace.get))
        }

      }



      val caSet = repo.findOne(f).get()

      val totalSpend = caSet.sumTotalSpend()
      caSet.sumTotalOrderedProductSales()
      val acos = caSet.calcTotalACos()

      this.sender() ! "{\"totalSpend\":\""+ totalSpend.setScale(2, BigDecimal.RoundingMode.HALF_UP) +"\" , \"acos\":\""+ acos +"\"}"


    case o : GetDailyOrder =>


      val dao = BizCoreCtx.get().getBean(classOf[DailyOrderDao])
      val f = new Filter
      f.addCriteria(new Criteria(DailyOrderQueryField.KCode , o.companyKcode.getOrElse("All")))

      val today = LocalDateTime.now().minusDays(1)
        .withHour(0).withMinute(0).
        withSecond(0).withNano(0)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      //println("today : "+today)

      val tomorrow = LocalDateTime.now()
        .withHour(0).withMinute(0).
        withSecond(0).withNano(0)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      //println("tomorrow : "+tomorrow)

      f.addCriteria(new Criteria(DailyOrderQueryField.StartDate ,today))

      f.addCriteria(new Criteria(DailyOrderQueryField.EndDate ,tomorrow))


      if(o.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(DailyOrderQueryField.BpCode , o.productBaseCode.get))
      }

      if(o.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(DailyOrderQueryField.SkuCode , o.productSkuCode.get))
      }

      if(o.marketPlace.getOrElse("")!= ""){
        if(o.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(DailyOrderQueryField.Marketplace ,
            SalesChannel.fromKey(o.marketPlace.get.toInt).getDisplayName))
        }
      }
      val Orders = dao.queryDailyOrderByFilter(f)
      this.sender() ! "{\"Orders\":\""+Orders+"\"}"
      //println("{\"Orders\":\""+Orders+"\"}")
      //println(Orders)

    case kpis : GetKeyProductInventoryStats =>


      val f = new Filter

      f.addCriteria(new Criteria(KeyProductStatsQueryField.KCode , kpis.companyKcode.getOrElse("All")))
      if(kpis.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.BpCode , kpis.productBaseCode.get))
      }

      if(kpis.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.SkuCode , kpis.productSkuCode.get))
      }

      if(kpis.marketPlace.getOrElse("")!= ""){
        if(kpis.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(KeyProductStatsQueryField.Marketplace ,
            kpis.marketPlace.get))
        }
      }

      val inventorySupplyStats = vkpsDao.queryInventorySupplyStatsByFilter(f);

      val result = om.writeValueAsString(inventorySupplyStats);

      this.sender() ! result;

    case kpslso : GetKeyProductSinceLastSettlementOrder =>

      val f = new Filter

      f.addCriteria(new Criteria(KeyProductStatsQueryField.KCode , kpslso.companyKcode.getOrElse("All")))
      if(kpslso.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.BpCode , kpslso.productBaseCode.get))
      }

      if(kpslso.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.SkuCode , kpslso.productSkuCode.get))
      }

      if(kpslso.marketPlace.getOrElse("")!= ""){
        if(kpslso.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(KeyProductStatsQueryField.Marketplace ,
            kpslso.marketPlace.get))
        }
      }

      val lastPeriodEnd = vkpsDao.queryLastPeriodEnd();
      val nextSettlementStart = DateHelper.addDays(lastPeriodEnd.clone.asInstanceOf[Date], 14);

      val sinceLastSettlementOrder = vkpsDao.queryTotalOrder(f,lastPeriodEnd,nextSettlementStart);
      val result = om.writeValueAsString(sinceLastSettlementOrder);

      this.sender() ! result;

      //println(result);

    case kplsdo : GetKeyProductLastSevenDaysOrder =>

      val f = new Filter

      f.addCriteria(new Criteria(KeyProductStatsQueryField.KCode , kplsdo.companyKcode.getOrElse("All")))
      if(kplsdo.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.BpCode , kplsdo.productBaseCode.get))
      }

      if(kplsdo.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.SkuCode , kplsdo.productSkuCode.get))
      }

      if(kplsdo.marketPlace.getOrElse("")!= ""){
        if(kplsdo.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(KeyProductStatsQueryField.Marketplace ,
            kplsdo.marketPlace.get))
        }
      }

      val today = new Date();
      val sevenDaysAgo =DateHelper.addDays( new Date(),-7);

      val lastSevenDaysOrder = vkpsDao.queryTotalOrder(f,sevenDaysAgo,today);
      val result = om.writeValueAsString(lastSevenDaysOrder);

      this.sender() ! result;

      //println(result);

    case kpbrp : GetKeyProductBaseRetailPrice =>
      val f = new Filter

      f.addCriteria(new Criteria(KeyProductStatsQueryField.KCode , kpbrp.companyKcode.getOrElse("All")))
      if(kpbrp.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.BpCode , kpbrp.productBaseCode.get))
      }

      if(kpbrp.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.SkuCode , kpbrp.productSkuCode.get))
      }

      if(kpbrp.marketPlace.getOrElse("")!= ""){
        if(kpbrp.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(KeyProductStatsQueryField.Marketplace ,
            kpbrp.marketPlace.get))
        }
      }

      val baseRetailPrice = vkpsDao.queryBaseRetailPrice(f);
      val result = om.writeValueAsString(baseRetailPrice);

      this.sender() ! result;

      //println(result);

    case dpst :GetDetailPageSalesTraffic =>

      val f = new Filter

      f.addCriteria(new Criteria(KeyProductStatsQueryField.KCode , dpst.companyKcode.getOrElse("All")))
      if(dpst.productBaseCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.BpCode , dpst.productBaseCode.get))
      }

      if(dpst.productSkuCode.getOrElse("") != ""){
        f.addCriteria(new Criteria(KeyProductStatsQueryField.SkuCode , dpst.productSkuCode.get))
      }

      if(dpst.marketPlace.getOrElse("")!= ""){
        if(dpst.marketPlace.getOrElse("")!= "All"){
          f.addCriteria(new Criteria(KeyProductStatsQueryField.Marketplace , dpst.marketPlace.get))
        }
      }

      val today = DateHelper.addDays( new Date(),-12);
      val sevenDaysAgo =DateHelper.addDays( new Date(),-19);
      //println(today)
      //println(sevenDaysAgo)

      val detailPageSalesTraffic = dpstDao.queryPageSalesTraffic(f,sevenDaysAgo,today)
      val result =om.writeValueAsString(detailPageSalesTraffic)

      this.sender() ! result
      //println(result)

  }


}
