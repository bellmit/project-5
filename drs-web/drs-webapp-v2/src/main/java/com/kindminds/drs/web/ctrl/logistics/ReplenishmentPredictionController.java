package com.kindminds.drs.web.ctrl.logistics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.api.usecase.logistics.PredictReplenishmentUco;


@Controller
public class ReplenishmentPredictionController {

	private PredictReplenishmentUco getUcoForView(){
		return (PredictReplenishmentUco)(SpringAppCtx.get().getBean("predictReplenishmentUcoImpl"));
	}
	
	@RequestMapping(value = "/rpm", method = RequestMethod.GET)			
	public @ResponseBody String calculateManually(@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
		   @RequestParam(value="assignedWarehouseId",defaultValue="1") String assignedWarehouseId ,
		   @RequestParam(value="calculatePeriod",defaultValue="1") String calculatePeriod,
		   @RequestParam(value="quantity",defaultValue="0") BigDecimal quantity,
		   @RequestParam(value="MLT",defaultValue="0") String MLT){								
		return  JsonHelper.toJson(this.getUcoForView().calculateManually(supplierKcode,
				Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)), Integer.parseInt(calculatePeriod), 
				quantity,Integer.parseInt(MLT)));		
	} 
	
	@RequestMapping(value = "/rpd", method = RequestMethod.GET)			
	public @ResponseBody String initCalculate(@RequestParam(value="assignedWarehouseId",defaultValue="1") String assignedWarehouseId ){						
		return  JsonHelper.toJson(this.getUcoForView().retrieveWarehouseDescription(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId))));		
	} 
	
//	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REPLENISHMENT_PREDICTION'))")
//	@RequestMapping(value = "/ReplenishmentPlanning")
//	public String showReplenishmentPlanning(Model model,
//			@RequestParam(value="spwCalPeriod",defaultValue="") String spwCalPeriod,
//			@RequestParam(value="assignedWarehouseId",defaultValue="101") String assignedWarehouseId ){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println("wareHouseId: " + assignedWarehouseId);
//		ReplenishmentTimeSpent info = this.getUcoForView().retrieveWarehouseDescription(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)));
//		if(spwCalPeriod.isEmpty()) spwCalPeriod = String.valueOf(info.getCalculationDays());
//		model.addAttribute("reportDate",sdf.format(Calendar.getInstance().getTime()));
//		model.addAttribute("report",this.getUcoForView().calculate(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)), Integer.parseInt(spwCalPeriod)));
//		model.addAttribute("reportJson", JsonHelper.toJson(this.getUcoForView().calculate(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)), Integer.parseInt(spwCalPeriod))));
//		model.addAttribute("warehouses",this.getUcoForView().getMarketplaceList());
//		model.addAttribute("receivingDays",info.getReceivingDays());
//		model.addAttribute("cperiods",spwCalPeriod);
//		model.addAttribute("courierDays",info.getCourierDays());
//		model.addAttribute("airFreightDays",info.getAirFreightDays());
//		model.addAttribute("surfaceFreightDays",info.getSurfaceFreightDays());
//		model.addAttribute("assignedWarehouseId", assignedWarehouseId );
//		return "ReplenishmentPrediction";
//	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REPLENISHMENT_PREDICTION'))")
	@RequestMapping(value = "/ReplenishmentPlanning")
	public String showReplenishmentPlanningV4(Model model,
											@RequestParam(value="spwCalPeriod",defaultValue="") String spwCalPeriod,
											@RequestParam(value="assignedWarehouseId",defaultValue="101") String assignedWarehouseId ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("wareHouseId: " + assignedWarehouseId);
		ReplenishmentTimeSpent info = this.getUcoForView().retrieveWarehouseDescription(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)));
		if(spwCalPeriod.isEmpty()) spwCalPeriod = String.valueOf(info.getCalculationDays());
		model.addAttribute("reportDate",sdf.format(Calendar.getInstance().getTime()));
		model.addAttribute("report",this.getUcoForView().calculate(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)), Integer.parseInt(spwCalPeriod)));
		//model.addAttribute("reportJson", JsonHelper.toJson(this.getUcoForView().calculate(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)), Integer.parseInt(spwCalPeriod))));
		model.addAttribute("warehouses",this.getUcoForView().getMarketplaceList());
		model.addAttribute("receivingDays",info.getReceivingDays());
		model.addAttribute("cperiods",spwCalPeriod);
		model.addAttribute("courierDays",info.getCourierDays());
		model.addAttribute("airFreightDays",info.getAirFreightDays());
		model.addAttribute("surfaceFreightDays",info.getSurfaceFreightDays());
		model.addAttribute("assignedWarehouseId", assignedWarehouseId );

		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(this.getUcoForView().calculate(Warehouse.fromKey(Integer.parseInt(assignedWarehouseId)), Integer.parseInt(spwCalPeriod)));
			//System.out.println(j);
			model.addAttribute("reportJson", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "th/replenishmentReport/ReplenishmentPrediction";
	}


}