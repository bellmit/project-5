package com.kindminds.drs.web.ctrl.sales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.v1.model.report.HistoryLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco;
import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco.ChartData;

import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco.TimeScale;
import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReport;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.SalesAndPageTrafficReportCondition;

/*
 * UC059 View Sales And Page Traffic Report
 */
@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SALES_AND_PAGE_TRAFFIC_REPORT'))")
@RequestMapping(value = "/SalesAndPageTrafficReport")
public class SalesAndPageTrafficReportController {

	@Autowired private ViewSalesAndPageTrafficReportUco uco;
	
	@RequestMapping(value="", method = RequestMethod.GET)
	public String showSalesAndPageTrafficReport(Model model,
			@ModelAttribute("SalesAndPageTrafficReport") SalesAndPageTrafficReportCondition condition,
			@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex
			){

		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(condition);
			model.addAttribute("conditionsJson", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		model.addAttribute("supplierKcodeNames", uco.getSupplierKcodeNames());
		model.addAttribute("productBases",uco.getProductBases(Context.getCurrentUser().getCompanyKcode()));								
		model.addAttribute("marketplaces",uco.getMarketplaces());
		model.addAttribute("timeScales", uco.getTimeScales());
		model.addAttribute("trafficTypes",uco.getTrafficTypes());
		model.addAttribute("dateRanges",uco.getDateRanges());
//		model.addAttribute("conditionsJson",JsonHelper.toJson(condition));
//		model.addAttribute("conditionsJson",condition);
		model.addAttribute("conditions", this.getSalesAndPageTrafficReportConditions(condition));
		List<String> selectedSkus = uco.getSelectedSkusList(condition.getBaseProduct(), condition.getSku());
//		model.addAttribute("selectedSkus", JsonHelper.toJson(selectedSkus));
		model.addAttribute("selectedSkus", selectedSkus);
		List<String> selectedProducts = uco.getSelectedProductList(condition.getBaseProduct());
//		model.addAttribute("selectedProducts", JsonHelper.toJson(selectedProducts));
		model.addAttribute("selectedProducts", selectedProducts);
		SalesAndPageTrafficReport report = uco.getReport(
				condition.getMarketplaceId(), condition.getBaseProduct(),
				condition.getSku(), condition.getDateRange(),
				convertStringtoDate(condition.getStartDate()),
				convertStringtoDate(condition.getEndDate()));
		if(report != null) {
			DtoList<HistoryLine> historyLines = uco.getSalesHistory(pageIndex,
							condition.getTimeScale(), condition.getTotalOrAverage());
			if (historyLines.getPager() != null) {
				Pager page = historyLines.getPager();
				List<ChartData> chartData = uco.getChartDatas(condition.getTimeScale(),
							condition.getTrafficType());



				model.addAttribute("historyLines",historyLines.getItems());
				model.addAttribute("chartData",JsonHelper.toJson(chartData));
//				model.addAttribute("chartData", chartData);
				model.addAttribute("totalPages",page.getTotalPages());
				model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
				model.addAttribute("startPage",page.getStartPage());
				model.addAttribute("endPage",page.getEndPage());
			}
		}
		model.addAttribute("report", report);
		return "th/salesAndPageTrafficReport/salesAndPageTrafficReport";
	}

	@RequestMapping(value="/v4", method = RequestMethod.GET)
	public String showSalesAndPageTrafficReportV4(Model model,
												@ModelAttribute("SalesAndPageTrafficReport") SalesAndPageTrafficReportCondition condition,
												@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex
	){
		model.addAttribute("supplierKcodeNames", uco.getSupplierKcodeNames());
		model.addAttribute("productBases",uco.getProductBases(Context.getCurrentUser().getCompanyKcode()));
		model.addAttribute("marketplaces",uco.getMarketplaces());
		model.addAttribute("timeScales", uco.getTimeScales());
		model.addAttribute("trafficTypes",uco.getTrafficTypes());
		model.addAttribute("dateRanges",uco.getDateRanges());
		model.addAttribute("conditionsJson",JsonHelper.toJson(condition));
		model.addAttribute("conditions", this.getSalesAndPageTrafficReportConditions(condition));
		List<String> selectedSkus = uco.getSelectedSkusList(condition.getBaseProduct(), condition.getSku());
		model.addAttribute("selectedSkus", JsonHelper.toJson(selectedSkus));
		List<String> selectedProducts = uco.getSelectedProductList(condition.getBaseProduct());
		model.addAttribute("selectedProducts", JsonHelper.toJson(selectedProducts));

		SalesAndPageTrafficReport report = uco.getReport(
				condition.getMarketplaceId(), condition.getBaseProduct(),
				condition.getSku(), condition.getDateRange(),
				convertStringtoDate(condition.getStartDate()),
				convertStringtoDate(condition.getEndDate()));
		if(report != null) {
			DtoList<HistoryLine> historyLines = uco.getSalesHistory(pageIndex,
					condition.getTimeScale(), condition.getTotalOrAverage());
			if (historyLines.getPager() != null) {
				Pager page = historyLines.getPager();
				List<ChartData> chartData = uco.getChartDatas(condition.getTimeScale(),
						condition.getTrafficType());
				model.addAttribute("historyLines",historyLines.getItems());
				model.addAttribute("chartData",JsonHelper.toJson(chartData));
				model.addAttribute("totalPages",page.getTotalPages());
				model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
				model.addAttribute("startPage",page.getStartPage());
				model.addAttribute("endPage",page.getEndPage());
			}
		}
		model.addAttribute("report", report);
		return "salesAndPageTrafficReportV4";
	}
	
	private static Date convertStringtoDate(String date) {
		if (!StringUtils.hasText(date)) return null;
		Date parsedDate = null;
		try {
			parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parsedDate;
	}
	
	@RequestMapping(value = "/getProductBases", method = RequestMethod.GET)	
	public @ResponseBody String getProductBases(@RequestParam("supplierKcode") String supplierKcode){		
		return JsonHelper.toJson(uco.getProductBases(supplierKcode));
	}
	
	@RequestMapping(value = "/getProductSkus", method = RequestMethod.GET)
	public @ResponseBody String getProductSkus(@RequestParam("productBase") String productBase){
		return JsonHelper.toJson(uco.getProductSkus(productBase));
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)	
	public String exportSalesAndPageTrafficReport(@ModelAttribute("SalesAndPageTrafficReport") SalesAndPageTrafficReportCondition condition, Model model){	
		List<HistoryLine> salesHistoryLines = uco.exportSalesHistory();
		model.addAttribute("salesAndPageTrafficHistoryLines", salesHistoryLines);
		model.addAttribute("kcode", condition.getSupplierKcode());
		model.addAttribute("timescale", TimeScale.getDisplayName(condition.getTimeScale()));
		model.addAttribute("totalOrAverage", condition.getTotalOrAverage());
		if (salesHistoryLines != null) {
			model.addAttribute("startDate", salesHistoryLines.get(0).getDate().substring(0, 10));
			model.addAttribute("endDate", salesHistoryLines.get(salesHistoryLines.size() - 1).getDate().substring(0, 10));
		}
		return "salesAndPageTrafficToExport";		
	}

	@RequestMapping(value = "/exportV4", method = RequestMethod.GET)
	public String exportSalesAndPageTrafficReportV4(@ModelAttribute("SalesAndPageTrafficReport") SalesAndPageTrafficReportCondition condition, Model model){
		List<HistoryLine> salesHistoryLines = uco.exportSalesHistory();
		model.addAttribute("salesAndPageTrafficHistoryLines", salesHistoryLines);
		model.addAttribute("kcode", condition.getSupplierKcode());
		model.addAttribute("timescale", TimeScale.getDisplayName(condition.getTimeScale()));
		model.addAttribute("totalOrAverage", condition.getTotalOrAverage());
		if (salesHistoryLines != null) {
			model.addAttribute("startDate", salesHistoryLines.get(0).getDate().substring(0, 10));
			model.addAttribute("endDate", salesHistoryLines.get(salesHistoryLines.size() - 1).getDate().substring(0, 10));
		}
		return "salesAndPageTrafficToExportV4";
	}

		
	private Map<String,String> getSalesAndPageTrafficReportConditions(SalesAndPageTrafficReportCondition condition){	
		Map<String,String> salesAndPageTrafficReportConditions = new TreeMap<String,String>();		
		salesAndPageTrafficReportConditions.put("supplierKcode", condition.getSupplierKcode());
		salesAndPageTrafficReportConditions.put("baseProduct", condition.getBaseProduct());
		salesAndPageTrafficReportConditions.put("sku", condition.getSku());
		salesAndPageTrafficReportConditions.put("marketplaceId", condition.getMarketplaceId());
		salesAndPageTrafficReportConditions.put("timeScale", condition.getTimeScale());
		salesAndPageTrafficReportConditions.put("trafficType", condition.getTrafficType());
		salesAndPageTrafficReportConditions.put("dateRange", condition.getDateRange());
		salesAndPageTrafficReportConditions.put("startDate", condition.getStartDate());
		salesAndPageTrafficReportConditions.put("endDate", condition.getEndDate());
		salesAndPageTrafficReportConditions.put("totalOrAverage", Integer.toString(condition.getTotalOrAverage()));
		return salesAndPageTrafficReportConditions;		
	}
		
}