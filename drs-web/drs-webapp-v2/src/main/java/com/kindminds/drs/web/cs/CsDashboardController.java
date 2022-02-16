package com.kindminds.drs.web.cs;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kindminds.drs.service.util.SpringAppCtx;

@Controller
public class CsDashboardController {
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/cs")	
	public String salesDashboard(){
		
		return "constructionSchedule";
		
	}

}
