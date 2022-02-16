package com.kindminds.drs.web.ctrl.util;

import static akka.pattern.Patterns.ask;

import java.util.Map;

import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kindminds.drs.Context;


import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('EMAIL_REMINDER'))")
public class EmailReminderController {

	ActorRef drsCmdBus =  DrsActorSystem.drsCmdBus();

//	@Autowired private EmailReminderUco uco;
	
	@RequestMapping(value = "/emailReminder")		
	public String showEmailReminderPage(Model model) {
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	    final Future<Object> f1 =
	                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.GetFeeToSendReminder(),
	                		timeout);
	    
	    final Future<Object> f2 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.GetIncludedSuppliers(),
                		timeout);
	    
	    final Future<Object> f3 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.GetExcludedSuppliers(),
                		timeout);

		try {
			Double feeLimit = (Double) Await.result(f1, timeout.duration());
			Map<String,String> includedSuppliers = (Map<String,String>) Await.result(f2, timeout.duration());
			Map<String,String> excludedSuppliers = (Map<String,String>) Await.result(f3, timeout.duration());
			
			model.addAttribute("limit", feeLimit);
			model.addAttribute("includeSuppliers", includedSuppliers);
			model.addAttribute("excludeSuppliers", excludedSuppliers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "emailReminder";
	}
	
	@RequestMapping(value = "/update")		
	public String updateLongTermStorageFeeSuppliers(Model model,
			@RequestParam(value="includeSuppliers", required=false) String includedKCodes, 
			@RequestParam(value="excludeSuppliers", required=false) String excludedKCodes,
			@RequestParam(value="limit") Double limit) {
		if (includedKCodes != null) {
			updateLongTermStorageList(includedKCodes);
		}
		if (excludedKCodes != null) {
			updateLongTermStorageList(excludedKCodes);
		}

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	    final Future<Object> f1 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.GetIncludedSuppliers(),
                		timeout);
	    
	    final Future<Object> f2 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.GetExcludedSuppliers(),
                		timeout);
	    try {
			Map<String,String> includedSuppliers = (Map<String,String>) Await.result(f1, timeout.duration());
			Map<String,String> excludedSuppliers = (Map<String,String>) Await.result(f2, timeout.duration());
			
			model.addAttribute("includeSuppliers", includedSuppliers);
			model.addAttribute("excludeSuppliers", excludedSuppliers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("limit", limit);
		return "emailReminder";
	}
	
	private void updateLongTermStorageList(String kCodes) {
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	    final Future<Object> f1 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.UpdateLongTermStorageReminder(kCodes),
                		timeout);
	}
	
	@RequestMapping(value = "/sendEmail")		
	public void sendEmailReminder(Model model) {
		int currentUserID = Context.getCurrentUser().getUserId();
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	    final Future<Object> f1 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.SendLTSFReminderToCurrentUser(currentUserID),
                		timeout);
	}
	
	@RequestMapping(value = "/updateFeeLimit")		
	public void updateFeeToSendReminder(Model model,
			@RequestParam(value="limit") Double limit) {
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	    final Future<Object> f1 =
                ask(drsCmdBus, new com.kindminds.drs.api.message.emailReminderUco.UpdateFeeToSendReminder(limit),
                		timeout);
	}
}