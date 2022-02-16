package com.kindminds.drs.web.ctrl.productV2.onboarding;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oai")
public class OnboaringLineitemController {
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/tlp")
	public String trailListProduct(@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/es")
	public String evalSample (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/p")
	public String presentSample (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/pc")
	public String provideComment (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/gf")
	public String giveFeedback (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/aps")
	public String approveSample (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/ckc")
	public String checkCompliance (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/cki")
	public String checkInsurance (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping("/ckp")
	public String checkProfitability (@RequestParam() String i )  {
		
		return "th/reactIndex";
		
	}
	

	
	
	
	
}


