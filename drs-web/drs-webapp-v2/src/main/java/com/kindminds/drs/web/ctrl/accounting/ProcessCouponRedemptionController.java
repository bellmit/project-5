package com.kindminds.drs.web.ctrl.accounting;

import static akka.pattern.Patterns.ask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;

import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.view.settlement.CouponRedemption;
import com.kindminds.drs.web.view.settlement.CouponRedemptionStatus;
import com.kindminds.drs.web.view.settlement.InternationalTransaction;
import com.kindminds.drs.web.view.settlement.MessageCode;
import com.kindminds.drs.web.view.statement.SettlementPeriod;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PROCESS_COUPON_REDEMPTION'))")
@RequestMapping(value = "/ProcessCouponRedemptionFee")
public class ProcessCouponRedemptionController {


	ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

	ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();
		
	@RequestMapping(value = "")	
	public String listCouponRedemptionFeeTransactions(Model model, @RequestParam(value="periodId",defaultValue="") String periodId) {						
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		ObjectMapper mapper = new ObjectMapper();
		SettlementPeriod settlementPeriod = null;
		List<InternationalTransaction> processedCouponRedemptions = null;
		List<CouponRedemption> failedCouponRedemptions = null;
		String resultMessage = "No data found";
							
		try {
			final Future<Object> settlementPeriodObj = ask(drsQueryBus,new com.kindminds.drs.api.message.GetLatestSettlementPeriod(), timeout);
			final Future<Object> isSettledObj = ask(drsQueryBus,new com.kindminds.drs.api.message.IsLatestSettlementPeriodSettled(), timeout);
			String settlementPeriodStr = (String) Await.result(settlementPeriodObj, timeout.duration());									
			settlementPeriod = mapper.readValue(settlementPeriodStr,com.kindminds.drs.web.view.statement.SettlementPeriod.class);
			String isSettled = (String) Await.result(isSettledObj, timeout.duration());			
			if(isSettled.equals("false")){												
				if(!periodId.isEmpty()){
					final Future<Object> couponRedemptionObj = ask(drsCmdBus,new com.kindminds.drs.api.message.coupon.ProcessCouponRedemptionBySettlementPeriod(Integer.parseInt(periodId)), timeout);
					String couponRedemptionStr = (String) Await.result(couponRedemptionObj, timeout.duration());
					if(couponRedemptionStr.contains("Coupons already have been processed") || couponRedemptionStr.contains("No coupons in the system to process")){
						MessageCode messageCode = mapper.readValue(couponRedemptionStr,com.kindminds.drs.web.view.settlement.MessageCode.class);					
						resultMessage = messageCode.getDescription();
					}else{
						CouponRedemptionStatus couponRedemptions = mapper.readValue(couponRedemptionStr,com.kindminds.drs.web.view.settlement.CouponRedemptionStatus.class);
						processedCouponRedemptions = couponRedemptions.getProcessedCouponRedemptions();																		
						failedCouponRedemptions = couponRedemptions.getUnprocessedCouponRedemptions();
					}																										
				}else{
					final Future<Object> failedCouponRedemptionObj = ask(drsQueryBus,new com.kindminds.drs.api.message.coupon.GetFailedCouponRedemptions(), timeout);					 
					String failedCouponRedemptionStr = (String) Await.result(failedCouponRedemptionObj, timeout.duration());
					failedCouponRedemptions = mapper.readValue(failedCouponRedemptionStr,new TypeReference<List<CouponRedemption>>(){});
				}
			}			
			model.addAttribute("settlementPeriod", settlementPeriod);
			model.addAttribute("isSettled", JsonHelper.toJson(isSettled));
			model.addAttribute("resultMessage", resultMessage);
			model.addAttribute("processedCouponRedemptions", processedCouponRedemptions);
			model.addAttribute("failedCouponRedemptions", failedCouponRedemptions);
			model.addAttribute("keyToCashFlowDirectionMap", this.createKeyToCashFlowDirectionMap());
		} catch (Exception e) {		
			e.printStackTrace();
		}				
		return "ListOfCouponRedemptionTransactions";
	}
	
	private Map<Integer,CashFlowDirection> createKeyToCashFlowDirectionMap(){
		CashFlowDirection [] allDirections = CashFlowDirection.values();
		Map<Integer,CashFlowDirection> result = new HashMap<>(allDirections.length);
		for(CashFlowDirection direction:allDirections){
			result.put(direction.getKey(), direction);
		}
		return result;
	}
	
}