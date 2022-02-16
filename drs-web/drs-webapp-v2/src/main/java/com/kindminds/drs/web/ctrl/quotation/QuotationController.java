package com.kindminds.drs.web.ctrl.quotation;


import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class QuotationController {

	//	confirm quotation
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value="/q/cq")
	public String trailListProduct(Model model) {				
		
		model.addAttribute("foo", "bar");
		
		return "th/reactIndex";
		
	}
	


}




