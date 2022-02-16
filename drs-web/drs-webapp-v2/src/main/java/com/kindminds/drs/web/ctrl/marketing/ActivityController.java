package com.kindminds.drs.web.ctrl.marketing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ms")
public class ActivityController {



	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value ="" , method = RequestMethod.GET)
    public String marketingActivityTable() {

        return "th/reactIndex";

    }
    
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value ="/create" , method = RequestMethod.GET)
    public String createMarketingActivity()  {

        return "th/reactIndex";

    }
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
    @RequestMapping(value ="/edit/{id}" , method = RequestMethod.GET)
    public String editMarketingActivity()  {
		
//		println(id)

        return "th/reactIndex";

    }
	



}


