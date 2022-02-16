package com.kindminds.drs.web.ctrl.accounting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.kindminds.drs.v1.model.impl.statement.ProfitCostShareListReportItemImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kindminds.drs.api.usecase.ProfitCostShareStatementsUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

import com.kindminds.drs.web.data.dto.ProfitCostShareListReportImpl;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROFIT_COST_SHARE_STATEMENTS'))")
@RequestMapping(value = "/ProfitCostShareStatements")
@SessionAttributes({"settlementPeriodId", "supplierKcode"})
public class ProfitCostShareStatementsController {

	@Autowired
	private ProfitCostShareStatementsUco uco;

	private static final BillStatementType billStatementType = BillStatementType.OFFICIAL;

	public static final String rootUrl = "ProfitCostShareStatements";

	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
	public String listProfitCostShareStatements(Model model,
			@RequestParam(value = "supplierKcode", defaultValue = "") String supplierKcode,
			@RequestParam(value = "settlementPeriodId", defaultValue = "") String settlementPeriodId) {

		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("rootUrlStatements", Ss2spStatementController.rootUrl);
		model.addAttribute("supplierKcodeNameMap", this.uco.getSupplierKcodeToNameMap());
		List<SettlementPeriod> settlementPeriodList = this.uco.getSettlementPeriodList();
		model.addAttribute("settlementPeriodList", settlementPeriodList);
		// Defaults to latest settlement period
		if( (settlementPeriodId == null || StringUtils.isEmpty(settlementPeriodId)) && (supplierKcode == null || StringUtils.isEmpty(supplierKcode)) &&
				settlementPeriodList != null && settlementPeriodList.size() > 0) {
			settlementPeriodId = String.valueOf(settlementPeriodList.get(0).getId());
		}
		model.addAttribute("settlementPeriodId", settlementPeriodId);
		model.addAttribute("supplierKcode", supplierKcode);
//		ProfitCostShareListReport pcsrl = this.uco.getProfitCostShareStatementListReport(billStatementType, supplierKcode, settlementPeriodId);
		model.addAttribute("report",this.uco.getProfitCostShareStatementListReport(billStatementType, supplierKcode, settlementPeriodId));

		return "ProfitCostShareStatements";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROFIT_COST_SHARE_STATEMENTS_EDIT'))")
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String bulkEditProfitCostShareStatementGet(Model model,
			@RequestParam(value = "supplierKcode", defaultValue = "") String supplierKcode,
			@RequestParam(value = "settlementPeriodId", defaultValue = "") String settlementPeriodId) {

		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("rootUrlStatements", Ss2spStatementController.rootUrl);
		model.addAttribute("supplierKcodeNameMap", this.uco.getSupplierKcodeToNameMap());
		model.addAttribute("settlementPeriodList", this.uco.getSettlementPeriodList());
		model.addAttribute("settlementPeriodId", settlementPeriodId);
		if(supplierKcode != null && !supplierKcode.equals(""))
		{
			model.addAttribute("supplierKcode", supplierKcode);
			model.addAttribute("supplierKcodeName", this.uco.getSupplierKcodeToNameMap().get(supplierKcode));
		}
		ProfitCostShareListReportImpl profitCostShareReportCmd =
				new ProfitCostShareListReportImpl(
				this.uco.getProfitCostShareStatementListReport(billStatementType, supplierKcode, settlementPeriodId).getPcsStatements());
		model.addAttribute("report", profitCostShareReportCmd);
		model.addAttribute("reportList", new ProfitCostShareListReportItemImpl());
		
		return "BulkEditProfitCostShareStatements";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROFIT_COST_SHARE_STATEMENTS_EDIT'))")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView bulkEditProfitCostShareStatementPost(Model model,
			@RequestParam(value = "supplierKcode", defaultValue = "") String supplierKcode,
			@RequestParam(value = "settlementPeriodId", defaultValue = "") String settlementPeriodId,
			@ModelAttribute("report") ProfitCostShareListReportImpl profitCostShareReport) {

		model.addAttribute("rootUrl", rootUrl);
		this.uco.bulkUpdateProfitCostShareStatement(billStatementType, profitCostShareReport);
		Map<String, String> mapModel = new HashMap<String, String>();
		mapModel.put("settlementPeriodId", settlementPeriodId);
		if(supplierKcode != null && !supplierKcode.equals(""))
			mapModel.put("supplierKcode", supplierKcode);

		return new ModelAndView("redirect:/" + rootUrl, mapModel);
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROFIT_COST_SHARE_STATEMENTS'))")
	@RequestMapping(value = "{statementName}")
	public String showProfitCostShareStatement(Model model, @PathVariable String statementName,
			@ModelAttribute("settlementPeriodId") String settlementPeriodId, @ModelAttribute("supplierKcode") String supplierKcode) {

		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("statementName", statementName);
		model.addAttribute("settlementPeriodId", settlementPeriodId);
		model.addAttribute("supplierKcode", supplierKcode);
		model.addAttribute("statement", this.uco.getProfitCostShareStatement(billStatementType, statementName));

		return "EditProfitCostShareStatements";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROFIT_COST_SHARE_STATEMENTS_EDIT'))")
	@RequestMapping(value = "{statementName}", method = RequestMethod.POST)
	public String editProfitCostShareStatement(Model model, @ModelAttribute("statement") ProfitCostShareListReportItemImpl statement,
			@ModelAttribute("settlementPeriodId") String settlementPeriodId, @ModelAttribute("supplierKcode") String supplierKcode) {

		model.addAttribute("rootUrl", rootUrl);
		boolean successful = this.uco.updateProfitCostShareStatement(billStatementType, statement);
		model.addAttribute("settlementPeriodId", settlementPeriodId);
		model.addAttribute("supplierKcode", supplierKcode);
		model.addAttribute("successfulUpdate", successful);
		model.addAttribute("statement", statement);

		return "EditProfitCostShareStatements";
	}
}
