package com.kindminds.drs.web.ctrl.accounting;

import java.util.List;

import akka.actor.ActorRef;
import akka.util.Timeout;
import com.kindminds.drs.Context;
import com.kindminds.drs.api.message.command.Settlement.CollectMarketSideTransactions;
import com.kindminds.drs.api.message.command.Settlement.StartCollectingMarketSideTransactions;
import com.kindminds.drs.api.message.command.Settlement.StartProcessingMarketSideTransactions;
import com.kindminds.drs.api.message.query.product.GetProductList;
import com.kindminds.drs.api.message.query.product.GetProductList4DRS;
import com.kindminds.drs.api.v1.model.accounting.NonProcessedMarketSideTransaction;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.accounting.ProcessMarketSideTransactionUco;

import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static akka.pattern.Patterns.ask;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('MARKET_SIDE_TRANSACTION'))")
@RequestMapping(value = "/MarketSideTransaction")
public class MarketSideTransactionController {

	@Autowired private ProcessMarketSideTransactionUco processMarketSideTransactionUco;

	ActorRef drsCmdBus =  DrsActorSystem.drsCmdBus();

	ActorRef drsQueryBus =  DrsActorSystem.drsQueryBus();
	
	@RequestMapping(value = "")	
	public String listMarketSideTransactions(Model model) {
		List<SettlementPeriod> settlementPeriodList = this.processMarketSideTransactionUco.getSettlementPeriodList();
		List<NonProcessedMarketSideTransaction> nonProcessedTransactionList = processMarketSideTransactionUco.getNonProcessedTransactionList();
		model.addAttribute("settlementPeriodList",settlementPeriodList);
		model.addAttribute("nonProcessedTransactionList",nonProcessedTransactionList);		
		return "MarketSideTransactionsList";
	}
	
	@RequestMapping(value = "/actions", method = RequestMethod.POST)		
	public String findMarketSideEvents( final RedirectAttributes redirectAttributes,
			@RequestParam String action,
			@RequestParam(value="periodId",defaultValue="") String periodId) {
		String resultMessage=null;

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));

		if(action.equals("find")){
			//resultMessage = this.processMarketSideTransactionUco.collectMarketSideTransactions(periodId);

			final Future<Object> futureResult =
					ask(drsCmdBus, new StartCollectingMarketSideTransactions(periodId),timeout);

			resultMessage= "collect";
		} else 
		if(action.equals("delete")){
			this.processMarketSideTransactionUco.deleteNonProcessedTransactions();
		} else
		if(action.equals("process")){
			//resultMessage = this.processMarketSideTransactionUco.processMarketSideTransactions(periodId);

			final Future<Object> futureResult =
					ask(drsCmdBus, new StartProcessingMarketSideTransactions(periodId),timeout);

			resultMessage= "process";
		}
		redirectAttributes.addFlashAttribute("message",resultMessage);
		return "redirect:/MarketSideTransaction";
	}

}