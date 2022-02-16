package com.kindminds.drs.web.ctrl.accounting;

import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import akka.actor.ActorRef;
import akka.util.Timeout;

import static akka.pattern.Patterns.ask;

import java.util.List;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CALCULATE_MONTHLY_STOREAGE_FEE'))")
public class CalculateMonthlyStorageFeeController {

	ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();
	
	/*
	private CalculateMonthlyStorageFeeUco getUco(){
		return (CalculateMonthlyStorageFeeUco)(SpringAppCtx.get().getBean("calculateMonthlyStorageFeeUcoImpl"));		
	}*/
		
	@RequestMapping(value = "/CalculateMonthlyStorageFee")
	public String showCalculateMonthlyStorageFeePage(Model model){

			
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	    final Future<Object> f1 =
	                ask(drsCmdBus, new com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco.GetYears(),
	                		timeout);
	    
	    final Future<Object> f2 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco.GetMonths(),
                		timeout);

	    try {
			List<String> years = (List<String>) Await.result(f1, timeout.duration());
			List<String> monthList = (List<String>) Await.result(f2, timeout.duration());
			
			model.addAttribute("yearList", years);
			model.addAttribute("monthList", monthList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "calculateMonthlyStorageFee";
	}
		
	@RequestMapping(value="/CalculateMonthlyStorageFee/calculate",method=RequestMethod.POST)
	public String calculateMonthlyStorageFee(final RedirectAttributes redirectAttributes,
			@RequestParam(value="year",defaultValue="") String year,
			@RequestParam(value="month",defaultValue="") String month){
		
		Timeout timeout = new Timeout(Duration.create(120, "seconds"));
		
		  final Future<Object> f1 =
	                ask(drsCmdBus, new com.kindminds.drs.api.message.calculateMonthlyStorageFeeUco
	                		.Calculate(year, month),
	                		timeout);
		
		  String message = "";
		try {
			message = (String) Await.result(f1, timeout.duration());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		redirectAttributes.addFlashAttribute("message",message);	
		
		return "redirect:/CalculateMonthlyStorageFee";
	}
		
}