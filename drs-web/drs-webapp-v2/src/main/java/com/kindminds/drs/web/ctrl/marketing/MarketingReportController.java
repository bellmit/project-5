package com.kindminds.drs.web.ctrl.marketing;

import static akka.pattern.Patterns.ask;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetAmazonSponsoredBrandsCampaignReport;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetCampaignNames;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetMarketplaceNames;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetSupplierKcodeToShortEnNameMap;
import com.kindminds.drs.web.config.DrsActorSystem;
import com.kindminds.drs.web.view.marketing.AmazonHsaCampaignReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewSkuAdvertisingEvaluationReportUco;
import com.kindminds.drs.api.usecase.ViewSkuAdvertisingPerformanceReportUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReport;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.view.marketing.AmazonSponsoredBrandsCampaignReport;
import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")
public class MarketingReportController {
	
	@Autowired private ViewSkuAdvertisingPerformanceReportUco viewSkuAdvPerformanceReportUco;

	ActorRef drsQueryBus  = DrsActorSystem.drsQueryBus();
	
	private ViewSkuAdvertisingEvaluationReportUco getAdvEvalUco() {
		return (ViewSkuAdvertisingEvaluationReportUco)(SpringAppCtx.get().getBean("viewSkuAdvertisingEvaluationReportUcoImpl"));
	}
	
	private static final String rootUrl = "MarketingReports";
		
	@RequestMapping(value=rootUrl)
	public String listSettlementLinks() {
		return "MarketingReports";
	}
	
	@RequestMapping(value="/sku-adv-performance-report")	
	public String showSkuAdvertisingPerformanceReport(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(defaultValue="") String marketplaceId,
			@RequestParam(value="start",defaultValue="") String start,
			@RequestParam(value="end",defaultValue="") String end){
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:this.viewSkuAdvPerformanceReportUco.getDefaultMarketplaceId();
		String dateStart = StringUtils.hasText(start)?start:this.viewSkuAdvPerformanceReportUco.getDefaultUtcDateStart();
		String dateEnd = StringUtils.hasText(end)?end:this.viewSkuAdvPerformanceReportUco.getDefaultUtcDateEnd();
		String actualSupplierKcode = this.viewSkuAdvPerformanceReportUco.getActualSupplierKcode(supplierKcode);		
		List<String> campaignNameList = this.viewSkuAdvPerformanceReportUco.getCampaignNameList(actualSupplierKcode, actualMarketplaceId, dateStart, dateEnd);
		String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);		
		SkuAdvertisingPerformanceReport report = this.viewSkuAdvPerformanceReportUco.getReport(actualSupplierKcode,actualMarketplaceId,dateStart,dateEnd,campaignName);
		model.addAttribute("report",report);
		model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));
		model.addAttribute("supplierKcode",actualSupplierKcode);
		model.addAttribute("defaultUtcDateStart",dateStart);
		model.addAttribute("defaultUtcDateEnd",dateEnd);
		model.addAttribute("marketplaceId",actualMarketplaceId);
		model.addAttribute("marketplaces",this.viewSkuAdvPerformanceReportUco.getMarketplaces());
		model.addAttribute("supplierKcodeToShortEnNameMap",this.viewSkuAdvPerformanceReportUco.getSupplierKcodeToShortEnNameMap());
		//return "SkuAdvertisingPerformanceReport";

		return "th/marketingReport/SkuAdvertisingPerformanceReport";
	}

	@RequestMapping(value="/sku-adv-performance-reportv4")
	public String showSkuAdvertisingPerformanceReportV4(Model model,
													  @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
													  @RequestParam(defaultValue="") String marketplaceId,
													  @RequestParam(value="start",defaultValue="") String start,
													  @RequestParam(value="end",defaultValue="") String end){
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:this.viewSkuAdvPerformanceReportUco.getDefaultMarketplaceId();
		String dateStart = StringUtils.hasText(start)?start:this.viewSkuAdvPerformanceReportUco.getDefaultUtcDateStart();
		String dateEnd = StringUtils.hasText(end)?end:this.viewSkuAdvPerformanceReportUco.getDefaultUtcDateEnd();
		String actualSupplierKcode = this.viewSkuAdvPerformanceReportUco.getActualSupplierKcode(supplierKcode);
		List<String> campaignNameList = this.viewSkuAdvPerformanceReportUco.getCampaignNameList(actualSupplierKcode, actualMarketplaceId, dateStart, dateEnd);
		String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);
		SkuAdvertisingPerformanceReport report = this.viewSkuAdvPerformanceReportUco.getReport(actualSupplierKcode,actualMarketplaceId,dateStart,dateEnd,campaignName);
		model.addAttribute("report",report);
		model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));
		model.addAttribute("supplierKcode",actualSupplierKcode);
		model.addAttribute("defaultUtcDateStart",dateStart);
		model.addAttribute("defaultUtcDateEnd",dateEnd);
		model.addAttribute("marketplaceId",actualMarketplaceId);
		model.addAttribute("marketplaces",this.viewSkuAdvPerformanceReportUco.getMarketplaces());
		model.addAttribute("supplierKcodeToShortEnNameMap",this.viewSkuAdvPerformanceReportUco.getSupplierKcodeToShortEnNameMap());
		return "SkuAdvertisingPerformanceReportV4";
	}

	@RequestMapping(value="/ms/cp")
	public String showHbaseCPAnalytics(){
			return "hbaseCp";
	}

	@RequestMapping(value="/ms/st")
	public String showHbaseStAnalytics(){
		return "hbaseSt";
	}
	
	@RequestMapping(value="/sku-adv-performance-report/getReportByCampaignName")
	public @ResponseBody String getSkuAdvertisingPerformanceReportByCampaignName(
			@RequestParam(value="supplierKcode") String supplierKcode,
			@RequestParam(value="marketplaceId") String marketplaceId,
			@RequestParam(value="startDate") String startDate,
			@RequestParam(value="endDate") String endDate,
			@RequestParam("campaignName") String campaignName
			){
		return JsonHelper.toJson(this.viewSkuAdvPerformanceReportUco.getReport(supplierKcode, marketplaceId, startDate, endDate, campaignName));		
	}
		
	@RequestMapping(value="/sku-adv-eval-report")
	public String showSkuAdvertisingEvaluationReport(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(value="marketplaceId",defaultValue="") String marketplaceId,
			@RequestParam(value="periodId",defaultValue="") String periodId) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String actuslSupplierKcode = isDrsUser?supplierKcode:userCompanyKcode;
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:this.viewSkuAdvPerformanceReportUco.getDefaultMarketplaceId();

		model.addAttribute("marketplaces",this.getAdvEvalUco().getMarketplaces());
		model.addAttribute("marketplaceId",marketplaceId);
		List<SettlementPeriod> periodList = this.getAdvEvalUco().getSettlementPeriodList();
		model.addAttribute("settlementPeriodList",periodList);
		String actualPeriodId = periodId;
		if(!StringUtils.hasText(periodId) && periodList != null && !periodList.isEmpty())
			actualPeriodId = String.valueOf(periodList.get(0).getId());
		model.addAttribute("settlementPeriodId",periodId);
		model.addAttribute("report",this.getAdvEvalUco().getReport(actuslSupplierKcode,actualMarketplaceId,actualPeriodId));
		if(isDrsUser){
			model.addAttribute("supplierKcode",supplierKcode);
			model.addAttribute("supplierKcodeNameMap",this.getAdvEvalUco().getSupplierKcodeNameMap());
		}
		//return "SkuAdvertisingEvaluationReport";
		return "th/marketingReport/SkuAdvertisingEvaluationReport";
	}

	@RequestMapping(value="/sku-adv-eval-reportV4")
	public String showSkuAdvertisingEvaluationReportV4(Model model,
													 @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
													 @RequestParam(value="marketplaceId",defaultValue="") String marketplaceId,
													 @RequestParam(value="periodId",defaultValue="") String periodId) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String actuslSupplierKcode = isDrsUser?supplierKcode:userCompanyKcode;
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:this.viewSkuAdvPerformanceReportUco.getDefaultMarketplaceId();
		model.addAttribute("marketplaces",this.getAdvEvalUco().getMarketplaces());
		model.addAttribute("marketplaceId",marketplaceId);
		List<SettlementPeriod> periodList = this.getAdvEvalUco().getSettlementPeriodList();
		model.addAttribute("settlementPeriodList",periodList);
		String actualPeriodId = periodId;
		if(!StringUtils.hasText(periodId) && periodList != null && !periodList.isEmpty())
			actualPeriodId = String.valueOf(periodList.get(0).getId());
		model.addAttribute("settlementPeriodId",periodId);
		model.addAttribute("report",this.getAdvEvalUco().getReport(actuslSupplierKcode,actualMarketplaceId,actualPeriodId));
		if(isDrsUser){
			model.addAttribute("supplierKcode",supplierKcode);
			model.addAttribute("supplierKcodeNameMap",this.getAdvEvalUco().getSupplierKcodeNameMap());
		}
		return "SkuAdvertisingEvaluationReportV4";
	}

	@RequestMapping(value="/AmazonSponsoredBrandsCampaignReport")
	public String showAmazonSponsoredBrandsCampaignReport(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(defaultValue="") String marketplaceId,
			@RequestParam(value="start",defaultValue="") String start,
			@RequestParam(value="end",defaultValue="") String end){
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();		
		String actuslSupplierKcode = isDrsUser?supplierKcode:userCompanyKcode;
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:String.valueOf(Marketplace.AMAZON_COM.getKey());
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> supplierKcodeNameMap = null;
		Map<String,String> marketplacesMap = null;
		com.kindminds.drs.web.view.statement.SettlementPeriod settlementPeriod = null;
		List<String> campaignNameList = null;
		AmazonSponsoredBrandsCampaignReport report = null;
		try {
			final Future<Object> supplierKcodeToShortEnNameMapObj = ask(drsQueryBus,
					new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetSupplierKcodeToShortEnNameMap(), timeout);
			final Future<Object> marketplacesObj = ask(drsQueryBus,
					new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetMarketplaceNames(), timeout);
			final Future<Object> settlementPeriodObj = ask(drsQueryBus,new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
			String supplierKcodeNameStr =  (String) Await.result(supplierKcodeToShortEnNameMapObj, timeout.duration());
			supplierKcodeNameMap = mapper.readValue(supplierKcodeNameStr,new TypeReference<Map<String, String>>(){});			
			String marketplacesStr = (String) Await.result(marketplacesObj, timeout.duration());
			marketplacesMap = mapper.readValue(marketplacesStr,new TypeReference<Map<String, String>>(){});														
			String settlementPeriodStr = (String) Await.result(settlementPeriodObj, timeout.duration());									
			settlementPeriod = mapper.readValue(settlementPeriodStr,com.kindminds.drs.web.view.statement.SettlementPeriod.class);
			String dateStart = StringUtils.hasText(start)?start:settlementPeriod.getStartDate();
			String dateEnd = StringUtils.hasText(end)?end:settlementPeriod.getEndDate();
			if(!actuslSupplierKcode.equals("") && !dateStart.equals("") && !dateEnd.equals("")){
				final Future<Object> campaignNamesObj = ask(drsQueryBus,
						new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetCampaignNames(
						actuslSupplierKcode, Integer.parseInt(actualMarketplaceId), dateStart, dateEnd), timeout);
				
				String campaignNamesStr = (String) Await.result(campaignNamesObj, timeout.duration());
				campaignNameList = mapper.readValue(campaignNamesStr,new TypeReference<List<String>>(){});
				String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);
				final Future<Object> reportObj = ask(drsQueryBus,
						new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetAmazonSponsoredBrandsCampaignReport(actuslSupplierKcode, Integer.parseInt(actualMarketplaceId), dateStart, dateEnd, campaignName), timeout);
				String reportStr = (String) Await.result(reportObj, timeout.duration());
				report = mapper.readValue(reportStr,com.kindminds.drs.web.view.marketing.AmazonSponsoredBrandsCampaignReport.class);
			}
			model.addAttribute("defaultUtcDateStart",dateStart);
			model.addAttribute("defaultUtcDateEnd",dateEnd);
			model.addAttribute("marketplaceId",actualMarketplaceId);
			model.addAttribute("marketplacesMap",marketplacesMap);						
			model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));
			model.addAttribute("amazonSponsoredBrandsCampaignReport",report);
			model.addAttribute("supplierKcode",actuslSupplierKcode);
			model.addAttribute("supplierKcodeNameMap",supplierKcodeNameMap);
		} catch (Exception e) {
			e.printStackTrace();
		}						
		//return "AmazonSponsoredBrandsCampaignReport";
		return "th/marketingReport/AmazonSponsoredBrandsCampaignReport";
	}


	@RequestMapping(value="/AmazonSponsoredBrandsCampaignReportV4")
	public String showAmazonSponsoredBrandsCampaignReportV4(Model model,
														  @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
														  @RequestParam(defaultValue="") String marketplaceId,
														  @RequestParam(value="start",defaultValue="") String start,
														  @RequestParam(value="end",defaultValue="") String end){
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String actuslSupplierKcode = isDrsUser?supplierKcode:userCompanyKcode;
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:String.valueOf(Marketplace.AMAZON_COM.getKey());
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> supplierKcodeNameMap = null;
		Map<String,String> marketplacesMap = null;
		com.kindminds.drs.web.view.statement.SettlementPeriod settlementPeriod = null;
		List<String> campaignNameList = null;
		AmazonSponsoredBrandsCampaignReport report = null;
		try {
			final Future<Object> supplierKcodeToShortEnNameMapObj = ask(drsQueryBus, new GetSupplierKcodeToShortEnNameMap(), timeout);
			final Future<Object> marketplacesObj = ask(drsQueryBus, new GetMarketplaceNames(), timeout);
			final Future<Object> settlementPeriodObj = ask(drsQueryBus,new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
			String supplierKcodeNameStr =  (String) Await.result(supplierKcodeToShortEnNameMapObj, timeout.duration());
			supplierKcodeNameMap = mapper.readValue(supplierKcodeNameStr,new TypeReference<Map<String, String>>(){});
			String marketplacesStr = (String) Await.result(marketplacesObj, timeout.duration());
			marketplacesMap = mapper.readValue(marketplacesStr,new TypeReference<Map<String, String>>(){});
			String settlementPeriodStr = (String) Await.result(settlementPeriodObj, timeout.duration());
			settlementPeriod = mapper.readValue(settlementPeriodStr,com.kindminds.drs.web.view.statement.SettlementPeriod.class);
			String dateStart = StringUtils.hasText(start)?start:settlementPeriod.getStartDate();
			String dateEnd = StringUtils.hasText(end)?end:settlementPeriod.getEndDate();
			if(!actuslSupplierKcode.equals("") && !dateStart.equals("") && !dateEnd.equals("")){
				final Future<Object> campaignNamesObj = ask(drsQueryBus, new GetCampaignNames(
						actuslSupplierKcode, Integer.parseInt(actualMarketplaceId), dateStart, dateEnd), timeout);

				String campaignNamesStr = (String) Await.result(campaignNamesObj, timeout.duration());
				campaignNameList = mapper.readValue(campaignNamesStr,new TypeReference<List<String>>(){});
				String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);
				final Future<Object> reportObj = ask(drsQueryBus, new GetAmazonSponsoredBrandsCampaignReport(actuslSupplierKcode, Integer.parseInt(actualMarketplaceId), dateStart, dateEnd, campaignName), timeout);
				String reportStr = (String) Await.result(reportObj, timeout.duration());
				report = mapper.readValue(reportStr,com.kindminds.drs.web.view.marketing.AmazonSponsoredBrandsCampaignReport.class);
			}
			model.addAttribute("defaultUtcDateStart",dateStart);
			model.addAttribute("defaultUtcDateEnd",dateEnd);
			model.addAttribute("marketplaceId",actualMarketplaceId);
			model.addAttribute("marketplacesMap",marketplacesMap);
			model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));
			model.addAttribute("amazonSponsoredBrandsCampaignReport",report);
			model.addAttribute("supplierKcode",actuslSupplierKcode);
			model.addAttribute("supplierKcodeNameMap",supplierKcodeNameMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "AmazonSponsoredBrandsCampaignReportV4";
	}
	
	
	@RequestMapping(value = "/AmazonSponsoredBrandsCampaignReport/getAmazonSponsoredBrandsCampaignReportByCampaignName", method = RequestMethod.GET)
	public @ResponseBody String getAmazonSponsoredBrandsCampaignReportByCampaignName(
			@RequestParam("supplierKcode") String supplierKcode, 
			@RequestParam("marketplaceId") String marketplaceId, 
			@RequestParam("startDate") String startDate, 
			@RequestParam("endDate") String endDate, 
			@RequestParam("campaignName") String campaignName			
			){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ObjectMapper mapper = new ObjectMapper();
		AmazonSponsoredBrandsCampaignReport report = null;
		try {
			final Future<Object> reportObj = ask(drsQueryBus,
					new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetAmazonSponsoredBrandsCampaignReport(supplierKcode, Integer.parseInt(marketplaceId), startDate, endDate, campaignName), timeout);
			String reportStr = (String) Await.result(reportObj, timeout.duration());
			report = mapper.readValue(reportStr,com.kindminds.drs.web.view.marketing.AmazonSponsoredBrandsCampaignReport.class);
		} catch (Exception e) {
			e.printStackTrace();
		}						
		return JsonHelper.toJson(report);		
	}



	@RequestMapping(value="/AmazonSponsoredBrandsCampaign")
	public String showAmazonSponsoredBrandsCampaign(Model model,
														  @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
														  @RequestParam(defaultValue="") String marketplaceId,
														  @RequestParam(value="start",defaultValue="") String start,
														  @RequestParam(value="end",defaultValue="") String end){

		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String actuslSupplierKcode = isDrsUser?supplierKcode:userCompanyKcode;
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?marketplaceId:String.valueOf(Marketplace.AMAZON_COM.getKey());
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> supplierKcodeNameMap = null;
		Map<String,String> marketplacesMap = null;
		com.kindminds.drs.web.view.statement.SettlementPeriod settlementPeriod = null;
		List<String> campaignNameList = null;
		AmazonHsaCampaignReport report = null;
		try {

			final Future<Object> supplierKcodeToShortEnNameMapObj = ask(drsQueryBus,
					new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetSupplierKcodeToShortEnNameMap(), timeout);
			final Future<Object> marketplacesObj = ask(drsQueryBus,
					new com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetMarketplaceNames(), timeout);
			final Future<Object> settlementPeriodObj = ask(drsQueryBus,new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);

			String supplierKcodeNameStr =  (String) Await.result(supplierKcodeToShortEnNameMapObj, timeout.duration());
			supplierKcodeNameMap = mapper.readValue(supplierKcodeNameStr,new TypeReference<Map<String, String>>(){});

			String marketplacesStr = (String) Await.result(marketplacesObj, timeout.duration());
			marketplacesMap = mapper.readValue(marketplacesStr,new TypeReference<Map<String, String>>(){});

			String settlementPeriodStr = (String) Await.result(settlementPeriodObj, timeout.duration());
			settlementPeriod = mapper.readValue(settlementPeriodStr,com.kindminds.drs.web.view.statement.SettlementPeriod.class);

			String dateStart = StringUtils.hasText(start)?start:settlementPeriod.getStartDate();
			String dateEnd = StringUtils.hasText(end)?end:settlementPeriod.getEndDate();


			if(!actuslSupplierKcode.equals("") && !dateStart.equals("") && !dateEnd.equals("")){
				final Future<Object> campaignNamesObj = ask(drsQueryBus,
						new com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetCampaignNames(
						actuslSupplierKcode, Integer.parseInt(actualMarketplaceId), dateStart, dateEnd), timeout);

				String campaignNamesStr = (String) Await.result(campaignNamesObj, timeout.duration());
				campaignNameList = mapper.readValue(campaignNamesStr,new TypeReference<List<String>>(){});

				String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);


				final Future<Object> reportObj = ask(drsQueryBus,
						new com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReport(actuslSupplierKcode, Integer.parseInt(actualMarketplaceId), dateStart, dateEnd, campaignName), timeout);
				String reportStr = (String) Await.result(reportObj, timeout.duration());


				report = mapper.readValue(reportStr,com.kindminds.drs.web.view.marketing.AmazonHsaCampaignReport.class);
			}



			model.addAttribute("defaultUtcDateStart",dateStart);
			model.addAttribute("defaultUtcDateEnd",dateEnd);
			model.addAttribute("marketplaceId",actualMarketplaceId);
			model.addAttribute("marketplacesMap",marketplacesMap);
			model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));

			model.addAttribute("hsaCampaignReport",report);

			model.addAttribute("supplierKcode",actuslSupplierKcode);
			model.addAttribute("supplierKcodeNameMap",supplierKcodeNameMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//return "AmazonSponsoredBrandsCampaign";
		return "th/marketingReport/AmazonSponsoredBrandsCampaign";
	}

	@RequestMapping(value = "/AmazonSponsoredBrandsCampaign/getAmazonSponsoredBrandsCampaignByCampaignName", method = RequestMethod.GET)
	public @ResponseBody String getAmazonSponsoredBrandsCampaignByCampaignName(
			@RequestParam("supplierKcode") String supplierKcode,
			@RequestParam("marketplaceId") String marketplaceId,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("campaignName") String campaignName
	){
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ObjectMapper mapper = new ObjectMapper();
		AmazonHsaCampaignReport report = null;
		try {
			final Future<Object> reportObj = ask(drsQueryBus,
					new com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReport(supplierKcode, Integer.parseInt(marketplaceId), startDate, endDate, campaignName), timeout);
			String reportStr = (String) Await.result(reportObj, timeout.duration());
			report = mapper.readValue(reportStr,com.kindminds.drs.web.view.marketing.AmazonHsaCampaignReport.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonHelper.toJson(report);
	}


}