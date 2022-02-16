package com.kindminds.drs.web.ctrl.accounting;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.InternationalTransactionImpl;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INTERNATIONAL_TRANSACTION'))")
public class InternationalTransactionController {

	private MaintainInternationalTransactionUco getUco(){
		return (MaintainInternationalTransactionUco)(SpringAppCtx.get().getBean("maintainInternationalTransactionUcoImpl"));
	}
	
	@RequestMapping(value = "/InternationalTransactions", method = RequestMethod.GET)		
	public String listInternationalTransactions(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){		
		DtoList<InternationalTransaction> transactionList = this.getUco().getList(pageIndex);
		Pager page = transactionList.getPager();
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());			
		model.addAttribute("TransactionList", transactionList.getItems());
		model.addAttribute("keyToCashFlowDirectionMap", this.createKeyToCashFlowDirectionMap());
		return "listOfInternationalTransactions";
	}
	
	private Map<Integer,CashFlowDirection> createKeyToCashFlowDirectionMap(){
		CashFlowDirection [] allDirections = CashFlowDirection.values();
		Map<Integer,CashFlowDirection> result = new HashMap<>(allDirections.length);
		for(CashFlowDirection direction:allDirections){
			result.put(direction.getKey(), direction);
		}
		return result;
	}
	
	@RequestMapping(value = "/InternationalTransaction/create", method = RequestMethod.GET)			
	public String createInternationalTransactionPage(@ModelAttribute("InternationalTransaction") InternationalTransactionImpl internationalTransaction,Model model){		
		model.addAttribute("InternationalTransaction",internationalTransaction);
		model.addAttribute("InternationalTransactionJson",JsonHelper.toJson(null));
		model.addAttribute("msdcKcodeNameMap",this.getUco().getMsdcKcodeNameMap());
		model.addAttribute("ssdcKcodeNameMap",this.getUco().getSsdcKcodeNameMap());
		model.addAttribute("earliestAvailableUtcDate",this.getUco().getEarliestAvailableUtcDate());
		model.addAttribute("cashFlowDirections", this.getUco().getCashFlowDirections());
		model.addAttribute("type", "Create");	
		return "internationalTransaction";	
	}

	@RequestMapping(value = "/InternationalTransaction/save", method = RequestMethod.POST)	
	public String saveInternationalTransaction(@ModelAttribute("InternationalTransaction") InternationalTransactionImpl internationalTransaction,Model model){		
		Integer id = this.getUco().create(internationalTransaction);
		return "redirect:/InternationalTransaction/"+id;			
	}
	
	@RequestMapping(value = "/InternationalTransaction/edit/{id}", method = RequestMethod.GET)				
	public String editInternationalTransactionPage(@PathVariable Integer id,Model model){		
		InternationalTransaction transaction = this.getUco().get(id);
		if(!transaction.isEditable()) return "accessDeny";
		InternationalTransactionImpl t = new InternationalTransactionImpl(transaction);	
		model.addAttribute("InternationalTransaction",t);
		model.addAttribute("InternationalTransactionJson",JsonHelper.toJson(t));			
		model.addAttribute("msdcKcodeNameMap",this.getUco().getMsdcKcodeNameMap());
		model.addAttribute("ssdcKcodeNameMap",this.getUco().getSsdcKcodeNameMap());
		model.addAttribute("earliestAvailableUtcDate",this.getUco().getEarliestAvailableUtcDate());
		model.addAttribute("cashFlowDirections", this.getUco().getCashFlowDirections());
		model.addAttribute("type", "Edit");
		return "internationalTransaction";
	}

	@RequestMapping(value = "/InternationalTransaction/update", method = RequestMethod.POST)		
	public String updateInternationalTransaction(@ModelAttribute("InternationalTransaction") InternationalTransactionImpl internationalTransaction,Model model){
		Integer id = this.getUco().update(internationalTransaction);
		return "redirect:/InternationalTransaction/"+id;
	}
	
	@RequestMapping(value = "/InternationalTransaction/{id}")		
	public String showInternationalTransaction(@PathVariable int id,Model model){
		InternationalTransaction transaction = this.getUco().get(id);
		model.addAttribute("t",transaction);
		model.addAttribute("cashFlowDirections", this.getUco().getCashFlowDirections());	
		return "internationalTransactionView";
	}
	
	@RequestMapping(value = "/InternationalTransaction/{id}/delete")			
	public String deleteInternationalTransaction(@PathVariable Integer id,final RedirectAttributes redirectAttributes){
		this.getUco().delete(id);
		redirectAttributes.addFlashAttribute("message", "This transaction has been deleted.");
		return "redirect:/InternationalTransactions";
	}
	
	@RequestMapping(value = "/InternationalTransaction/getCurrencyByMsdcKcode", method = RequestMethod.GET)		
	public @ResponseBody String getCurrencyByMsdcKcode(@RequestParam("msdcKcode") String msdcKcode){		
		return JsonHelper.toJson(this.getUco().getCurrency(msdcKcode));
	}
		
	@RequestMapping(value = "/InternationalTransaction/getSplrKcodeNameMap", method = RequestMethod.GET)	
	public @ResponseBody String getSplrKcodeNameMap(@RequestParam("ssdcKcode") String ssdcKcode){
		return JsonHelper.toJson(this.getUco().getSplrKcodeNameMap(ssdcKcode));		
	}
	
	@RequestMapping(value = "/InternationalTransaction/getLineItemKeyNameMap", method = RequestMethod.GET)		
	public @ResponseBody String getLineItemKeyNameMap(@RequestParam("cashFlowDirectionKey") int cashFlowDirectionKey){
		return JsonHelper.toJson(this.getUco().getLineItemKeyNameMap(cashFlowDirectionKey));
	}
		
}