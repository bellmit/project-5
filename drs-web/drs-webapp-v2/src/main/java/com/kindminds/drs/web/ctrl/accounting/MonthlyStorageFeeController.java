package com.kindminds.drs.web.ctrl.accounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.ViewMonthlyStorageFeeReportUco;
import com.kindminds.drs.api.v1.model.report.MonthlyStorageFeeReport;
import com.kindminds.drs.UserInfo;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('VIEW_MONTHLY_STOREAGE_FEE_REPORT'))")
@RequestMapping(value = "/storage-fee")
public class MonthlyStorageFeeController {
	
	@Autowired private ViewMonthlyStorageFeeReportUco uco;
	
	@RequestMapping(value="")
	public String showMonthlyStorageFeeReport(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(value="country",defaultValue="US") String country,
			@RequestParam(value="year",defaultValue="") String year,
			@RequestParam(value="month",defaultValue="") String month){

		String actualSupplierKcode = this.uco.getActualSupplierKcode(supplierKcode);

		String actualCountry = StringUtils.hasText(country)?country:this.uco.getDefaultCountry();
		String yearInput = StringUtils.hasText(year)?year:this.uco.getDefaultYear();
		String monthInput = StringUtils.hasText(month)?month:this.uco.getDefaultMonth();
		
		MonthlyStorageFeeReport report = this.uco.getReport(actualSupplierKcode,actualCountry,yearInput,monthInput);
		
		model.addAttribute("supplierKcode",actualSupplierKcode);
		model.addAttribute("countryList",this.uco.getCountries());
		model.addAttribute("yearList", this.uco.getYears());
		model.addAttribute("monthList", this.uco.getMonths());
		model.addAttribute("countryInput",actualCountry);
		model.addAttribute("yearInput",yearInput);
		model.addAttribute("monthInput",monthInput);		
		model.addAttribute("supplierKcodeToShortEnNameMap",this.uco.getSupplierKcodeToShortEnNameMap());
		model.addAttribute("report",report);
		
		return "th/storageFee/storageFee";
	}
	
	@RequestMapping(value = "/{supplierKcode}/{country}/{year}/{month}")
	public String showMonthlyStorageFeeReport_test(Model model,
			@PathVariable String supplierKcode,
			@PathVariable String country,
			@PathVariable String year,
			@PathVariable String month){
		
		String actualSupplierKcode = this.uco.getActualSupplierKcode(supplierKcode);
		MonthlyStorageFeeReport report = this.uco.getReport(actualSupplierKcode,country,year,month);
		
		model.addAttribute("supplierKcode",actualSupplierKcode);
		model.addAttribute("countryList",this.uco.getCountries());
		model.addAttribute("yearList", this.uco.getYears());
		model.addAttribute("monthList", this.uco.getMonths());
		model.addAttribute("countryInput",country);
		model.addAttribute("yearInput",year);
		model.addAttribute("monthInput",month);
		model.addAttribute("supplierKcodeToShortEnNameMap",this.uco.getSupplierKcodeToShortEnNameMap());
		UserInfo user = Context.getCurrentUser();
		if (user.isDrsUser() ||	
				(user.isSupplier() && user.getCompanyKcode().equals(supplierKcode))) {
			model.addAttribute("report",report);
		}
		
		return "th/storageFee/storageFee";
	}



	@RequestMapping(value="/v4")
	public String showMonthlyStorageFeeReportV4(Model model,
											  @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
											  @RequestParam(value="country",defaultValue="US") String country,
											  @RequestParam(value="year",defaultValue="") String year,
											  @RequestParam(value="month",defaultValue="") String month){

		String actualSupplierKcode = this.uco.getActualSupplierKcode(supplierKcode);

		String actualCountry = StringUtils.hasText(country)?country:this.uco.getDefaultCountry();
		String yearInput = StringUtils.hasText(year)?year:this.uco.getDefaultYear();
		String monthInput = StringUtils.hasText(month)?month:this.uco.getDefaultMonth();

		MonthlyStorageFeeReport report = this.uco.getReport(actualSupplierKcode,actualCountry,yearInput,monthInput);

		model.addAttribute("supplierKcode",actualSupplierKcode);
		model.addAttribute("countryList",this.uco.getCountries());
		model.addAttribute("yearList", this.uco.getYears());
		model.addAttribute("monthList", this.uco.getMonths());
		model.addAttribute("countryInput",actualCountry);
		model.addAttribute("yearInput",yearInput);
		model.addAttribute("monthInput",monthInput);
		model.addAttribute("supplierKcodeToShortEnNameMap",this.uco.getSupplierKcodeToShortEnNameMap());
		model.addAttribute("report",report);

		return "storageFeeV4";
	}

	@RequestMapping(value = "/v4/{supplierKcode}/{country}/{year}/{month}")
	public String showMonthlyStorageFeeReport_testV4(Model model,
												   @PathVariable String supplierKcode,
												   @PathVariable String country,
												   @PathVariable String year,
												   @PathVariable String month){

		String actualSupplierKcode = this.uco.getActualSupplierKcode(supplierKcode);
		MonthlyStorageFeeReport report = this.uco.getReport(actualSupplierKcode,country,year,month);

		model.addAttribute("supplierKcode",actualSupplierKcode);
		model.addAttribute("countryList",this.uco.getCountries());
		model.addAttribute("yearList", this.uco.getYears());
		model.addAttribute("monthList", this.uco.getMonths());
		model.addAttribute("countryInput",country);
		model.addAttribute("yearInput",year);
		model.addAttribute("monthInput",month);
		model.addAttribute("supplierKcodeToShortEnNameMap",this.uco.getSupplierKcodeToShortEnNameMap());
		UserInfo user = Context.getCurrentUser();
		if (user.isDrsUser() ||
				(user.isSupplier() && user.getCompanyKcode().equals(supplierKcode))) {
			model.addAttribute("report",report);
		}

		return "storageFeeV4";
	}
}
