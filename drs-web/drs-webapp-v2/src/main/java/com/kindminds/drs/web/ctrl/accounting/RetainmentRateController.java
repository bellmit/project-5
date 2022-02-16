package com.kindminds.drs.web.ctrl.accounting;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.CalculateRetainmentRateUco;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('RETAINMENT_RATE'))")
@RequestMapping(value = "/RetainmentRate")
public class RetainmentRateController {
	
	private CalculateRetainmentRateUco getCalculateRetainmentRateUco(){
		return (CalculateRetainmentRateUco)(SpringAppCtx.get().getBean("calculateRetainmentRateUcoImpl"));				
	}
	
	@RequestMapping(value = "")
	public String listRetainmentRates(Model model) {
		List<RetainmentRate> retainmentRateList = this.getCalculateRetainmentRateUco().getList();
		List<SettlementPeriod> settlementPeriodList = this.getCalculateRetainmentRateUco().getAvailableSettlementPeriodList();
		model.addAttribute("availableSettlementPeriodList",settlementPeriodList);
		model.addAttribute("retainmentRateList",retainmentRateList);
		return "RetainmentRateList";
	}
	
	@ResponseBody @RequestMapping(value="rates.json", method = RequestMethod.GET)
	public String getList(){
		return JsonHelper.toJson(this.getCalculateRetainmentRateUco().getList());
	}

	@ResponseBody @RequestMapping(value="calculateAnyway", method = RequestMethod.GET)
	public String calculateAnyway(){
		return this.getCalculateRetainmentRateUco().calculate();
	}
	
	@RequestMapping(value = "/calculate",method = RequestMethod.POST)
	public String calculateRetainmentRate(@RequestParam(value="periodId",defaultValue="") String periodId){
		this.getCalculateRetainmentRateUco().calculate(periodId);
		return "redirect:/RetainmentRate";
	}
	
	@RequestMapping(value = "/delete/{rateId}")
	public String deleteRetainmentRate(@PathVariable int rateId,final RedirectAttributes redirectAttributes){
		this.getCalculateRetainmentRateUco().delete(rateId);
		redirectAttributes.addFlashAttribute("message","DRS retainment rate has been deleted.");
		return "redirect:/RetainmentRate";
	}
			
}