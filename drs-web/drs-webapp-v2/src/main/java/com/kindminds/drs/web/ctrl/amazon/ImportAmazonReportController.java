package com.kindminds.drs.web.ctrl.amazon;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('IMPORT_AMAZON_REPORT'))")
public class ImportAmazonReportController {
	
	@RequestMapping(value="ImportAmazonReport")
	public String listLinks() {
		return "importAmazonReport";
	}
		
}