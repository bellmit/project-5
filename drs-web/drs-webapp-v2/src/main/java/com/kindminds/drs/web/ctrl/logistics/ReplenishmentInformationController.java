package com.kindminds.drs.web.ctrl.logistics;

import java.util.List;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kindminds.drs.api.usecase.logistics.MaintainReplenishmentTimeSpentInfoUco;

import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.ReplenishmentInfoWeb;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REPLENISHMENT_INFORMATION'))")
@RequestMapping(value = "/replenishment-information")
public class ReplenishmentInformationController {

	private MaintainReplenishmentTimeSpentInfoUco getUco(){

		return (MaintainReplenishmentTimeSpentInfoUco)(SpringAppCtx.get().getBean("maintainReplenishmentTimeSpentInfoUcoImpl"));		
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)			
	public String editReplenishmentInformationPage(Model model){		
		List<ReplenishmentTimeSpentInfo> list = this.getUco().getList();
		ReplenishmentInfoWeb replenishmentInfo = new ReplenishmentInfoWeb(list); 
		model.addAttribute("ReplenishmentInfo",replenishmentInfo);
		model.addAttribute("ReplenishmentInfoJson",JsonHelper.toJson(replenishmentInfo));							
		return "replenishmentInformation";		
	}
		
	@RequestMapping(value = "/save", method = RequestMethod.POST)	
	public String saveReplenishmentInformation(@ModelAttribute("ReplenishmentInfo") ReplenishmentInfoWeb replenishmentInfo){		
		this.getUco().save(replenishmentInfo.getLineItems());
		return "redirect:/replenishment-information";				
	}
		
}