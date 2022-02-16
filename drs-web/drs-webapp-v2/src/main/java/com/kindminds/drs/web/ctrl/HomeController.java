package com.kindminds.drs.web.ctrl;

import static akka.pattern.Patterns.ask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kindminds.drs.web.config.DrsActorSystem;
import com.kindminds.drs.web.view.product.KeyProductStatsReportLineItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.ctrl.accounting.Ss2spStatementController;
import com.kindminds.drs.web.view.product.KeyProductStatsReport;
import com.kindminds.drs.web.view.statement.StatementListReport;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
	
	//@Autowired private ViewKeyProductStatsUco viewKeyProductStatsUco;

	ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('HOME_ACCESS'))")	
	@RequestMapping(value="/")
	public String home(Model model, HttpServletRequest request, HttpServletResponse response) {

		//ViewHomePageUco uco = (ViewHomePageUco) SpringAppCtx.get().getBean("viewHomePageUco"); 
		//KeyProductStatsReport report = this.viewKeyProductStatsUco.getKeyProductStatsReport();		
		Boolean isSupplier = Context.getCurrentUser().isSupplier();

		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
			
		 final Future<Object> f1 =
	                ask(drsQueryBus,
	                		new com.kindminds.drs.api.message.viewKeyProductStatsUco.
	                		GetKeyProductStatsReport(Context.getCurrentUser().isSupplier() ,
	                				Context.getCurrentUser().getCompanyKcode()),
	                		timeout);
		  
		 
	    final Future<Object> f2 =
	                ask(drsQueryBus, new com.kindminds.drs.api.message.viewHomePageUco
	                		.GetSs2spStatementListReport(Context.getCurrentUser().getCompanyKcode()),
	                		timeout);
	    
		 
	  KeyProductStatsReport report = null;
	  StatementListReport statementListReport = null;
	    ObjectMapper mapper = new ObjectMapper();

	    try {
	    	
	    	String kReportStr  = (String)  Await.result(f1, timeout.duration());
	    	report = mapper.readValue(kReportStr,
	    			com.kindminds.drs.web.view.product.KeyProductStatsReport.class);


	    	String statementListReportStr  = (String) Await.result(f2, timeout.duration());
	    	statementListReport = mapper.readValue(statementListReportStr,
	    			com.kindminds.drs.web.view.statement.StatementListReport.class);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		model.addAttribute("statementRootUrl",Ss2spStatementController.rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementListReport", statementListReport);

		return "home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/sdb")	
	public String salesDashboard(){
		

		return "salesDashboard";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/cpdb")	
	public String campaignDashboard(){
		

		return "campaignDashboard";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/cci")
	public String ccIssues(){


		return "ccIssues";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/re")
	public String amazonReturn(){


		return "return";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/rv")
	public String review(){

		return "review";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/afi")
	public String amzFBAInv(){


		return "amzfbainv";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/amzse")
	public String amzsettlement(){


		return "amzsettlement";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/accinv")
	public String accinv(){

		return "acctinvoice";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/niinv")
	public String niinv(){

		return "th/invoiceStatement/nonIssuedInvoice";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/sab")
	public String salesboard(){

		return "salesboard";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/araging")
	public String araging(){

		return "araging";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/bwinv")
	public String biweeklyInv(){

		return "biwinv";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/sks")
	public String supplierKeyStats(){

		return "sks";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/marketingSalesDashboard")	
	public String marketingSalesDashboard(Model model){
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));			

		final Future<Object> listOfSkusQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.viewExternalMarketingActivityUco.GetListOfSkus(), timeout);
				
		try {			
			
			String listOfSkus = (String) Await.result(listOfSkusQuery, timeout.duration());
			
			//System.out.println(listOfSkus);
			
			model.addAttribute("listOfSkus", JsonHelper.toJson(listOfSkus));
			
		} catch (Exception e) {		
			e.printStackTrace();
		}	
		
		return "marketingSalesDashboard";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DASH_BOARD_DRS_STAFF'))")
	@RequestMapping(value="/i4")
	public String indexV4(){


		return "indexV4";
	}


	

	
	

	
						
}

