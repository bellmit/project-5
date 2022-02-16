package com.kindminds.drs.web.ctrl.accounting;

import static akka.pattern.Patterns.ask;


import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.util.JsonHelper;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROCESS_AD_SPEND'))")
@RequestMapping(value = "/ProcessAdSpend")

public class ProcessAdSpendController {


	ActorRef drsQueryBus  = DrsActorSystem.drsQueryBus();
	ActorRef drsCmdBus   = DrsActorSystem.drsCmdBus();
		
	@RequestMapping(value = "")	
	public String getLatestSettlementPeriod(Model model) {
				
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));			
		
		final Future<Object> latestSettlementPeriodQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
				
		try {			
			ObjectMapper mapper = new ObjectMapper();
			String latestSettlementPeriodJson = (String) Await.result(latestSettlementPeriodQuery, timeout.duration());
			
			//serializing JSON to object
			SettlementPeriod latestSettlementPeriod = mapper.readValue(latestSettlementPeriodJson,
					SettlementPeriodImpl.class);
					
					
			model.addAttribute("latestSettlementPeriod", latestSettlementPeriod);
			model.addAttribute("processing", 0);
			
		} catch (Exception e) {		
			e.printStackTrace();
		}	
		return "ProcessAdSpend";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROCESS_AD_SPEND'))")
	@RequestMapping(value = "/Process")
	public String processAdSpend(Model model,
			@RequestParam(value="periodId",defaultValue="") String periodId,
			@RequestParam(value="action",defaultValue="") String action) {
		
		Timeout timeout = new Timeout(Duration.create(120, "seconds"));	
		
		//Get latest settlement period
		final Future<Object> latestSettlementPeriodQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
		
		//process ad spend
		final Future<Object> processAdSpend = ask(drsCmdBus,
				new com.kindminds.drs.api.message.ProcessAdSpendTransaction(Context.getCurrentUser().getCompanyKcode())
				,timeout );
		
		try {		
			
			//getting latest settlement JSON
			ObjectMapper mapper = new ObjectMapper();
			String latestSettlementPeriodJson = (String) Await.result(latestSettlementPeriodQuery, timeout.duration());
			SettlementPeriod latestSettlementPeriod = mapper.readValue(latestSettlementPeriodJson,
					SettlementPeriodImpl.class);
			
			//get transaction JSON
			String transactionsJSON = 
				 (String) Await.result(processAdSpend, timeout.duration());
			
			
			//add attributes to model
			model.addAttribute("latestSettlementPeriod", latestSettlementPeriod);
			model.addAttribute("transactionsJSON", JsonHelper.toJson(transactionsJSON));
			model.addAttribute("processing", 1);
	
		} catch (Exception e) {		
			e.printStackTrace();
		}	
		
		return "ProcessAdSpend";
	}

	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROCESS_AD_SPEND'))")
	@RequestMapping(value = "/ViewCampaignDetail")
	public String viewCampaignDetail(Model model) {
		
		Timeout timeout = new Timeout(Duration.create(30, "seconds"));			
		
		//get latest settlement
		final Future<Object> latestSettlementPeriodQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
		
		//get campaign report detail query
		final Future<Object> campaignReportTransactionsQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.CampaignReport.GetCampaignReportDetail(), timeout);
		
		try {			
			ObjectMapper mapper = new ObjectMapper();
			
			//serialize latest settlement period
			String latestSettlementPeriodJson = (String) Await.result(latestSettlementPeriodQuery, timeout.duration());
			SettlementPeriod latestSettlementPeriod = mapper.readValue(latestSettlementPeriodJson,
					SettlementPeriodImpl.class);
			
			//get campaign report detail JSON
			String campaignReportTransactionsJson = (String) Await.result(campaignReportTransactionsQuery, timeout.duration());
			
			model.addAttribute("latestSettlementPeriod", latestSettlementPeriod);
			model.addAttribute("transactionsJSON", JsonHelper.toJson(campaignReportTransactionsJson));
			
		// catch error
		} catch (Exception e) {		
			e.printStackTrace();
		}	
		
		return "ViewCampaignDetail";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROCESS_AD_SPEND'))")
	@RequestMapping(value = "/ViewHSAReport")
	public String viewHSAReport(Model model) {
		
		Timeout timeout = new Timeout(Duration.create(30, "seconds"));			
		
		//get latest settlement
		final Future<Object> latestSettlementPeriodQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
		
		//get campaign report detail query
		final Future<Object> HsaCampaignReportQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.query.marketing.hsaCampaignReport.GetHsaCampaignReportDetail(), timeout);
		
		try {			
			ObjectMapper mapper = new ObjectMapper();
			
			//serialize latest settlement period
			String latestSettlementPeriodJson = (String) Await.result(latestSettlementPeriodQuery, timeout.duration());
			SettlementPeriod latestSettlementPeriod = mapper.readValue(latestSettlementPeriodJson, SettlementPeriodImpl.class);
			
			//get campaign report detail JSON
			String HsaCampaignReportJson = (String) Await.result(HsaCampaignReportQuery, timeout.duration());
			
			model.addAttribute("latestSettlementPeriod", latestSettlementPeriod);
			model.addAttribute("transactionsJSON", JsonHelper.toJson(HsaCampaignReportJson));
			
		// catch error
		} catch (Exception e) {		
			e.printStackTrace();
		}	
		
		return "ViewHSAReport";
	}
	
}
