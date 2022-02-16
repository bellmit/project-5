package com.kindminds.drs.web.ctrl.accounting;

import com.kindminds.drs.api.v2.biz.domain.model.SettlementHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindminds.drs.api.usecase.settlement.AddSettlementPeriodUco;

import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SETTLEMENT'))")
@RequestMapping(value="settlement")
public class SettlementController {
	
	@Autowired
	SettlementHandler handler;

	@Autowired AddSettlementPeriodUco addSettlementPeriodUco; 

	@RequestMapping(value="")
	public String listSettlementLinks(Model model) {
		model.addAttribute("amazonMarketplaces",this.handler.getAmazonMarketplaces());
		return "Settlement";
	}
	
	@ResponseBody @RequestMapping(value="getPeriods", method = RequestMethod.GET)
	public String getPeriods() {
		return JsonHelper.toJson(this.addSettlementPeriodUco.getRecentPeriods());
	}
	
	@ResponseBody @RequestMapping(value="tryAddPeriod", method = RequestMethod.GET)
	public String tryAddPeriod() {
		return this.addSettlementPeriodUco.addPeriod();
	}
	
	@ResponseBody @RequestMapping(value="getAmazonSettlementReportReadyStatus", method = RequestMethod.GET)
	public String getAmazonSettlementReportReadyStatus() {
		return JsonHelper.toJson(this.handler.getAmazonSettlementReportPeriodReadyMarketplaces());
	}

	/*
	@ResponseBody @RequestMapping(value="doAllSettlement", method = RequestMethod.GET)
	public String doAllSettlement() {
		this.handler.doAllSettlement();
		return "Done.";
	}
	*/

	
	@ResponseBody @RequestMapping(value="createMs2ssStatements", method = RequestMethod.GET)
	public String doMs2ssSettlement() {
		this.handler.doMs2ssSettlement();
		return "Done.";
	}
	
	@ResponseBody @RequestMapping(value="createSs2spDraftStatements", method = RequestMethod.GET)
	public String doSs2spSettlementInDraft() {
		this.handler.doSs2spSettlementInDraft();
		return "Done.";
	}
	
}