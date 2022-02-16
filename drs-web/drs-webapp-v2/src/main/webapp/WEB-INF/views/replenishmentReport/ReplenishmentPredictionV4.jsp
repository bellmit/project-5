<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="replenishmentPrediction.Title" /> - DRS
</title>
<script>
	$(function() {
		var report = ${reportJson};
		$("#courier").on("change", function(e) {	
			var getArrVal = $('input[type="checkbox"][name="shippingMethod"]:checked').map(function(){
			    return this.value;
			}).toArray();
			if(getArrVal.length){				
				$('#shippingMethodAtLeastOne').html("");				   
			}else{
				$(this).prop("checked",true);
				$('#shippingMethodAtLeastOne').html('<spring:message code="replenishmentPrediction.shippingMethodAtLeastOne" />');
				return false;				    
			};						
			for(var o in report){			
				var colepd = $("#"+o+"-epd").attr('colspan');        		
				var colesd = $("#"+o+"-esd").attr('colspan');  		        	       		
				var colepdCourier = $("#"+o+ "-epd-courier, "+"#"+o+ " tr td:nth-child(10)");
				var colesdCourier = $("#"+o+ "-esd-courier, "+"#"+o+ " tr td:nth-child(13)");	        									
				if($(this).is(":checked")){														
					if($("#"+o+"-epd").is(':visible')){	
						if(colepd != 3){    			        	
			        		$("#"+o+"-epd").attr('colspan',parseInt(colepd)+1);			        			
			        	}
						colepdCourier.show();												
					}										
					if(colesd != 3){	        			
						$("#"+o+"-esd").attr('colspan',parseInt(colesd)+1);       							
		        	}		        			        		
					colesdCourier.show();																							
				}else{					
					if($("#"+o+"-epd").is(':visible')){						
						if(colepd == 1)$("#"+o+"-epd").hide();						
						$("#"+o+"-epd").attr('colspan',parseInt(colepd)-1);					
						colepdCourier.hide();						
					}															
	        		$("#"+o+"-esd").attr('colspan',parseInt(colesd)-1);       							        		
	        		colesdCourier.hide();										
				}															
			}			
		}).prop("checked", true).change();
										
		$("#airFreight").on("change", function(e) {		
			var getArrVal = $('input[type="checkbox"][name="shippingMethod"]:checked').map(function(){
				return this.value;
			}).toArray();			
			if(getArrVal.length){				
				$('#shippingMethodAtLeastOne').html("");				   
			}else{
				$(this).prop("checked",true);
				$('#shippingMethodAtLeastOne').html('<spring:message code="replenishmentPrediction.shippingMethodAtLeastOne" />');
				return false;				    
			};			
			for(var o in report){				
				var colepd = $("#"+o+"-epd").attr('colspan');        		
				var colesd = $("#"+o+"-esd").attr('colspan');  		        	       		
				var colepdAirFreight = $("#"+o+ "-epd-airFreight, "+"#"+o+ " tr td:nth-child(11)");
				var colesdAirFreight = $("#"+o+ "-esd-airFreight, "+"#"+o+ " tr td:nth-child(14)");	        									
				if($(this).is(":checked")){															
					if($("#"+o+"-epd").is(':visible')){						
						if(colepd != 3){   			        	
			        		$("#"+o+"-epd").attr('colspan',parseInt(colepd)+1);			        			
			        	}
						colepdAirFreight.show();												
					}															
					if(colesd != 3){	        			
						$("#"+o+"-esd").attr('colspan',parseInt(colesd)+1);       							
		        	}		        			        		
					colesdAirFreight.show();																							
				}else{					
					if($("#"+o+"-epd").is(':visible')){						
						if(colepd == 1)$("#"+o+"-epd").hide();						
						$("#"+o+"-epd").attr('colspan',parseInt(colepd)-1);					
						colepdAirFreight.hide();						
					}															
	        		$("#"+o+"-esd").attr('colspan',parseInt(colesd)-1);       							        		
	        		colesdAirFreight.hide();										
				}															
			}
		}).prop("checked", true).change();	
						
		$("#surfaceFreight").on("change", function(e) {
			var getArrVal = $('input[type="checkbox"][name="shippingMethod"]:checked').map(function(){
				return this.value;
			}).toArray();			
			if(getArrVal.length){				
				$('#shippingMethodAtLeastOne').html("");				   
			}else{
				$(this).prop("checked",true);
				$('#shippingMethodAtLeastOne').html('<spring:message code="replenishmentPrediction.shippingMethodAtLeastOne" />');
				return false;				    
			};						
			for(var o in report){				
				var colepd = $("#"+o+"-epd").attr('colspan');        		
				var colesd = $("#"+o+"-esd").attr('colspan');  		        	       		
				var colepdSurfaceFreight = $("#"+o+ "-epd-surfaceFreight, "+"#"+o+ " tr td:nth-child(12)");
				var colesdSurfaceFreight = $("#"+o+ "-esd-surfaceFreight, "+"#"+o+ " tr td:nth-child(15)");	        									
				if($(this).is(":checked")){															
					if($("#"+o+"-epd").is(':visible')){						
						if(colepd != 3){    			        	
			        		$("#"+o+"-epd").attr('colspan',parseInt(colepd)+1);			        			
			        	}
						colepdSurfaceFreight.show();												
					}										
					if(colesd != 3){	        			
						$("#"+o+"-esd").attr('colspan',parseInt(colesd)+1);							
		        	}		        			        		
					colesdSurfaceFreight.show();																							
				}else{					
					if($("#"+o+"-epd").is(':visible')){						
						if(colepd == 1)$("#"+o+"-epd").hide();						
						$("#"+o+"-epd").attr('colspan',parseInt(colepd)-1);					
						colepdSurfaceFreight.hide();						
					}															
	        		$("#"+o+"-esd").attr('colspan',parseInt(colesd)-1);							        		
	        		colesdSurfaceFreight.hide();										
				}															
			}		
		}).prop("checked", true).change();
								
		$("#expectedProductionDate").on("change", function(e) {
			for(var o in report){			
				var col = $("#"+o+ "-epd-courier, "+"#"+o+ "-epd-airFreight, "+"#"+o+ "-epd-surfaceFreight, "+"#"+o+ " tr td:nth-child(10), "+"#"+o+ " tr td:nth-child(11), "+"#"+o+ " tr td:nth-child(12)");	        	
	        	var colspanCount = 0				
				if($(this).is(":checked")) {					
					if($("#courier").is(":checked")) colspanCount+=1;
					if($("#airFreight").is(":checked")) colspanCount+=1;
					if($("#surfaceFreight").is(":checked")) colspanCount+=1;						
					if(colspanCount!=0)$("#"+o+"-epd").show();					
					$("#"+o+"-epd").attr('colspan',colspanCount);  		
					if($("#courier").is(":checked")) $("#"+o+ "-epd-courier, "+"#"+o+ " tr td:nth-child(10)").show();
					if($("#airFreight").is(":checked")) $("#"+o+ "-epd-airFreight, "+"#"+o+ " tr td:nth-child(11)").show();
					if($("#surfaceFreight").is(":checked")) $("#"+o+ "-epd-surfaceFreight, "+"#"+o+ " tr td:nth-child(12)").show();									    							
				} else {					
					$("#"+o+"-epd").hide();
		    		col.hide();					
				}																
			}						
		}).prop("checked", false).change();
				
		$( "input[name$='mlt'], input[name$='mQty']" ).keyup(function(){		
			calManually( $(this).parent().parent(),$.trim($(this).parent().parent().find("td:first-child").text()),$("#assignedWarehouseId").val(),$("#spwCalPeriod").val(),$(this).parent().parent().find("td:nth-child(8) input[name$='mQty']").val(),$(this).parent().parent().find("td:nth-child(2) input[name$='mlt']").val());									
		})
		
		$("#assignedWarehouseId").change(function(){
			initDes( $(this).val());
		});
		
		function initDes(wid){
			var ajaxUrl = '${pageContext.request.contextPath}/rpd';
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {assignedWarehouseId :wid},
				dataType : "json",
				success : function(data) {			
					$("#arp").text(data.receivingDays);
					$("#spwCalPeriod").val(data.calculationDays);						
					$("#courierDays").text("("+ data.courierDays+" <spring:message code='replenishmentPrediction.days' />)");
					$("#airFreightDays").text("("+ data.airFreightDays+" <spring:message code='replenishmentPrediction.days' />)");
					$("#surfaceFreightDays").text("("+ data.surfaceFreightDays+" <spring:message code='replenishmentPrediction.days' />)");						
				}
			});			
		}
		
		function calManually(tr,sku,wid,period,qty,mlt){			
			var ajaxUrl = '${pageContext.request.contextPath}/rpm';
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {						
					supplierKcode : sku,
					assignedWarehouseId : wid,
					calculatePeriod : period,
					quantity : qty,
					MLT : mlt
				},
				dataType : "json",
				success : function(data) {
					$(tr).find("td").eq(8).text(data.expectedStockDepletionDate);																							
					$(tr).find("td").eq(9).text(data.expectedProductionDate.courierDate);						
					$(tr).find("td").eq(10).text(data.expectedProductionDate.airFreightDate);						
					$(tr).find("td").eq(11).text(data.expectedProductionDate.surfaceFreightDate);												
					
					if(data.expectedShippingDate.courierDate != null){
						if(data.expectedShippingDate.courierSelloutRisk == true){
							if(data.expectedShippingDate.courierMinOutOfStockDays == data.expectedShippingDate.courierMaxOutOfStockDays){
								$(tr).find("td").eq(12).html(data.expectedShippingDate.courierDate+" "+"<span class='text-danger fas fa-exclamation-circle' aria-hidden='true' data-toggle='tooltip' data-placement='top' title='<spring:message code='replenishmentPrediction.outOfStockDays' arguments='"+data.expectedShippingDate.courierMinOutOfStockDays+"'/>'></span>");								
							}else{
								$(tr).find("td").eq(12).html(data.expectedShippingDate.courierDate+" "+"<span class='text-danger fas fa-exclamation-circle' aria-hidden='true' data-toggle='tooltip' data-placement='top' title='<spring:message code='replenishmentPrediction.outOfStockDaysDuring' arguments='"+data.expectedShippingDate.courierMinOutOfStockDays+","+data.expectedShippingDate.courierMaxOutOfStockDays+"'/>'></span>");														
							}																					
						}else{
							$(tr).find("td").eq(12).html(data.expectedShippingDate.courierDate);								
						}												
					}										
					$(tr).find("td").eq(12).attr('class', '');
					$(tr).find("td").eq(12).attr('class', 'status_'+data.expectedShippingDate.courierStatus+' text-center');													
					
					if(data.expectedShippingDate.airFreightDate != null){
						if(data.expectedShippingDate.airFreightSelloutRisk == true){
							if(data.expectedShippingDate.airFreightMinOutOfStockDays == data.expectedShippingDate.airFreightMaxOutOfStockDays){
								$(tr).find("td").eq(13).html(data.expectedShippingDate.airFreightDate+" "+"<span class='text-danger fas fa-exclamation-circle' aria-hidden='true' data-toggle='tooltip' data-placement='top' title='<spring:message code='replenishmentPrediction.outOfStockDays' arguments='"+data.expectedShippingDate.airFreightMinOutOfStockDays+"'/>'></span>");								
							}else{
								$(tr).find("td").eq(13).html(data.expectedShippingDate.airFreightDate+" "+"<span class='text-danger fas fa-exclamation-circle' aria-hidden='true' data-toggle='tooltip' data-placement='top' title='<spring:message code='replenishmentPrediction.outOfStockDaysDuring' arguments='"+data.expectedShippingDate.airFreightMinOutOfStockDays+","+data.expectedShippingDate.airFreightMaxOutOfStockDays+"'/>'></span>");														
							}																					
						}else{
							$(tr).find("td").eq(13).html(data.expectedShippingDate.airFreightDate);								
						}												
					}										
					$(tr).find("td").eq(13).attr('class', '');
					$(tr).find("td").eq(13).attr('class', 'status_'+data.expectedShippingDate.airFreightStatus+' text-center');												
					
					if(data.expectedShippingDate.surfaceFreightDate != null){						
						if(data.expectedShippingDate.surfaceFreightSelloutRisk == true){
							if(data.expectedShippingDate.surfaceFreightMinOutOfStockDays == data.expectedShippingDate.surfaceFreightMaxOutOfStockDays){
								$(tr).find("td").eq(14).html(data.expectedShippingDate.surfaceFreightDate+" "+"<span class='text-danger fas fa-exclamation-circle' aria-hidden='true' data-toggle='tooltip' data-placement='top' title='<spring:message code='replenishmentPrediction.outOfStockDays' arguments='"+data.expectedShippingDate.surfaceFreightMinOutOfStockDays+"'/>'></span>");								
							}else{
								$(tr).find("td").eq(14).html(data.expectedShippingDate.surfaceFreightDate+" "+"<span class='text-danger fas fa-exclamation-circle' aria-hidden='true' data-toggle='tooltip' data-placement='top' title='<spring:message code='replenishmentPrediction.outOfStockDaysDuring' arguments='"+data.expectedShippingDate.surfaceFreightMinOutOfStockDays+","+data.expectedShippingDate.surfaceFreightMaxOutOfStockDays+"'/>'></span>");														
							}																					
						}else{
							$(tr).find("td").eq(14).html(data.expectedShippingDate.surfaceFreightDate);								
						}												
					}					
					$(tr).find("td").eq(14).attr('class', '');
					$(tr).find("td").eq(14).attr('class', 'status_'+data.expectedShippingDate.surfaceFreightStatus+' text-center');																		
					
					$(tr).find("td").eq(15).text(data.expectedEightWeeksReplenishmentQuantity);
					$('[data-toggle="tooltip"]').tooltip();
				}
			 });						
		}				
		$('[data-toggle="tooltip"]').tooltip();		
	});
</script>
<style>
.status_Normal {
	background-color: white;
}
.status_ExpiredIn21Days {
	color: #47a447;	
}
.status_ExpiredIn14Days {
	color: #ec971f;	
}
.status_ExpiredIn7Days {
	color: #c9302c;	
}
.status_ExpiredIn21DaysLogo {
	background-color: #47a447;
	width: 10px;
    height: 10px;
   	border-radius: 50%;
   	display: inline-block;	
}
.status_ExpiredIn14DaysLogo {
	background-color: #ec971f;
	width: 10px;
    height: 10px;
   	border-radius: 50%;
   	display: inline-block;	
}
.status_ExpiredIn7DaysLogo {
	background-color: #c9302c;
	width: 10px;
    height: 10px;
   	border-radius: 50%;
   	display: inline-block;	
}
.form-wrapper {
	display: flex;
	align-items: center;
	margin-right: 22px;
}
.form-section {
	display: flex;
	align-items: center;
	margin-right: 12px;
}
.supplier-name {
	font-size: 16px;
	color: #535353;
	margin-left: 16px;
	letter-spacing: .5px;
	font-weight: 600;
}
.supplier-tb-padding-reset thead tr th {
	padding: 2px 4px;
}
.supplier-tb-padding-reset tbody tr td {
	padding: 4px;
}
.table-wrapper {
	background-color: rgba(237,237,237, .36);
	padding: 2px 15px;
	border-right: 1px solid #dee2e6;
	border-left: 1px solid #dee2e6;
	border-bottom: 1px solid #dee2e6;
}
.border-t tr {
	border-top: 1px solid rgba(128,128,128,.1);
}

</style>
</head>
	<div class="max-width">
		<div class="container-fluid">
			<div class="card">
				<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code="replenishmentPrediction.Title" />
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					<div class="card-body">
						<form class="form-inline" method="POST" action="${pageContext.request.contextPath}/ReplenishmentPlanning">
							<div class="form-wrapper" style="color: #535353;">
								<div class="form-section" style='margin-right: 40px;'>
									<label class="control-label mr-3">
										<spring:message code="replenishmentPrediction.ReportingDate"/>
									</label>
									<span><b>${reportDate}</b></span>
								</div>
								<div class="form-section">
									<label class="control-label mr-3">
										<span class="control-label" data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.AmazonReceivingPeriod_hint" />">
											<spring:message code="replenishmentPrediction.AmazonReceivingPeriod" />
										</span>
									</label>
									<span id="arp"><b>${receivingDays}</b></span>
								</div>
							</div>
							<div class='form-wrapper'>
								<div class='form-section'>
									<label class="control-label mr-2">
										<span class="control-label" data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.Warehouse_hint" />">
											<spring:message code="replenishmentPrediction.Warehouse" />
										</span>
									</label>
									<span>
										<select class="form-control" name="assignedWarehouseId" id="assignedWarehouseId" style="height: 34px !important;">
											<option value="">--- Select ---</option>
											<c:forEach items="${warehouses}" var="warehouses">
												<option value="${warehouses.key}" ${warehouses.key==assignedWarehouseId?'selected="selected"':''}>${warehouses.name}</option>
											</c:forEach>
										</select>
									</span>
								</div>
								<div class='form-section'>
									<label class="control-label mr-2">
										<span class="control-label" data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.SPWCalculationPeriod_hint" />">
											<spring:message code="replenishmentPrediction.SPWCalculationPeriod" />
										</span>
									</label>
									<span><input id="spwCalPeriod" name="spwCalPeriod" class="form-control" size="5" value="${cperiods}"></input></span>
									<input class="btn btn-primary mt-2 mt-md-0 mr-md-2 ml-2 searchBtn" type="submit" value="<spring:message code='replenishmentPrediction.viewButton'/>">
								</div>
							</div>	
						</form>
						<hr>
					</div>
				</div>			
			</div>
			
			<div class="row" style='margin-right: 5px;'>
				<div class="col-md-12" style="display: flex; justify-content: center;">
					<div class="form-check form-check-inline">	
						<label class="form-check-label" style="padding-left: 0px !important;">
							<spring:message code="replenishmentPrediction.view" />
							<spring:message code="replenishmentPrediction.shippingMethod" />
						</label>			
					</div>	
					<div class="form-check form-check-inline">
						<label class="form-check-label" for="courier">
							<input class="form-check-input" id="courier" type="checkbox" name="shippingMethod" value="1" checked /> 
							<span class="form-check-sign"></span>
							<spring:message code="replenishmentPrediction.Courier"/> 
							<span id="courierDays">(${courierDays}<spring:message code='replenishmentPrediction.days' />)</span>  
						</label>
					</div>		
					<div class="form-check form-check-inline">					  			
						<label class="form-check-label" for="airFreight">
						<input class="form-check-input" id="airFreight" type="checkbox" name="shippingMethod" value="2" checked /> 
						<span class="form-check-sign"></span>
							<spring:message code="replenishmentPrediction.AirFreight"/>
							<span id="airFreightDays">(${airFreightDays}<spring:message code='replenishmentPrediction.days' />)</span>  
						</label>
					</div>			
					<div class="form-check form-check-inline">							  	
						<label class="form-check-label" for="surfaceFreight">
						<input class="form-check-input" id="surfaceFreight" type="checkbox" name="shippingMethod" value="3" checked /> 
						<span class="form-check-sign"></span>
							<spring:message code="replenishmentPrediction.SurfaceFreight"/>
							<span id="surfaceFreightDays">(${surfaceFreightDays}<spring:message code='replenishmentPrediction.days' />)</span>				
						</label>
					</div>
					<div class="form-check form-check-inline" style="margin-right: 0;">							  	
						<label class="form-check-label" for="expectedProductionDate">
						<input class="form-check-input" id="expectedProductionDate" type="checkbox" name="expectedProductionDate" value="4" />
						<span class="form-check-sign"></span>
							<spring:message code="replenishmentPrediction.ExpectedProductionDate"/>
						</label>
					</div>
				</div>
			</div>
			<div class="row mb-2">
				<div class="col-md-12" style="display: flex; justify-content: center;">
					<label class="control-label" style="padding-right: 17px;">
						<span id="shippingMethodAtLeastOne" class="text-danger"></span>
					</label>
				</div>
			</div>
						
			<div class="row mb-2" style='margin-right: 5px; padding-bottom: 12px; color: #535353; font-size: 13.5px;'>
				<div class="col-md-12" style="display: flex; justify-content: center; align-items: center;">
					<div class="status_ExpiredIn7DaysLogo mr-2 ml-2"></div>
					<spring:message code="replenishmentPrediction.expiredIn7Days" />				
					<div class="status_ExpiredIn14DaysLogo mr-2 ml-2"></div>
					<spring:message code="replenishmentPrediction.expiredIn14Days" />				
					<div class="status_ExpiredIn21DaysLogo mr-2 ml-2"></div>
					<spring:message code="replenishmentPrediction.expiredIn21Days" />
				</div>
			</div>	
		
			<div class="row">
				<div class="col-md-12">						
					<c:forEach var="entry" items="${report}">
						<div className='card-title title-space-mb title-space-mt'>
                            <div className="title-wrapper" style="border: 1px solid #dee2e6; padding-top: 10px; margin-top: 20px;">
                                <h6 style="padding-left: 20px;">${entry.key.supplierName}</h6>
                            </div>
                        </div>
						<!-- <span class='supplier-name'>${entry.key.supplierName}</span> -->
						<div class="table-wrapper mb-2">
							<div class="table-responsive-lg" style="background-color:#fff;">
								<table id="${entry.key.supplierKcode}" class="table supplier-tb-padding-reset" style="border-right: 1px solid #dee2e6; border-left: 1px solid #dee2e6; border-bottom: 1px solid #dee2e6; margin-top: 12px;">
									<thead id="tableHeader" class="border-top border-bottom">		
										<tr class="bg" style="color: #535353;">
											<th rowspan="2" class="text-left" style="padding-left: 15px;border-bottom: 1px solid #dee2e6;">SKU</th>
											<th rowspan="2" class="text-right" style="padding-right:18px; border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.ProductionPeriods_hint" />">
													<spring:message code="replenishmentPrediction.ProductionPeriods" />
												</span>
											</th>
											<th rowspan="2" class="text-right" style="border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.SellableInventory_hint" />">
													<spring:message code="replenishmentPrediction.SellableInventory" />
												</span>
											</th>							
											<th rowspan="2" class="text-right" style="border-left: 1px solid #ddd;border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.PlannedQty_hint" />">
													<spring:message code="replenishmentPrediction.PlannedQty" />
												</span>
											</th>							
											<th rowspan="2" class="text-right" style="border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.InboundQty_hint" />">
													<spring:message code="replenishmentPrediction.InboundQty" />
												</span>
											</th>
											<th rowspan="2" class="text-center" style="border-right: 1px solid #ddd; border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.ExpectedArrivalWarehouseDate_hint" />">
													<spring:message code="replenishmentPrediction.ExpectedArrivalWarehouseDate" />
												</span>
											</th>																														
											<th rowspan="2"  class="text-right" style="border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.AverageWeeklySalesQty_hint" />">
													<spring:message code="replenishmentPrediction.AverageWeeklySalesQty" />
												</span>
											</th>
											<th rowspan="2" class="text-right" style="width: 100px;border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.ModifiedWeeklySalesQty_hint" />">
													<spring:message code="replenishmentPrediction.ModifiedWeeklySalesQty" />
												</span>
											</th>
											<th rowspan="2" class="text-center" style="border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.ExpectedStockDepletionDate_hint" />">
													<spring:message code="replenishmentPrediction.ExpectedStockDepletionDate" />
												</span>
											</th>							
											<th id="${entry.key.supplierKcode}-epd" colspan="3" class="text-center" style="border-left: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.ExpectedProductionDate_hint" />">
													<spring:message code="replenishmentPrediction.ExpectedProductionDate" />
												</span>
											</th>									
											<th id="${entry.key.supplierKcode}-esd" colspan="3" class="text-center" style="border-left: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.RecommendedFcaDeliveryDate_hint" />">
													<spring:message code="replenishmentPrediction.RecommendedFcaDeliveryDate" />
												</span>
											</th>							
											<th rowspan="2" class="text-right" style="border-left: 1px solid #ddd; width: 7%;border-bottom: 1px solid #dee2e6;">
												<span data-toggle="tooltip" data-placement="top" title="<spring:message code="replenishmentPrediction.Expected8WeeksReplenishmentQty_hint" />">
													<spring:message code="replenishmentPrediction.Expected8WeeksReplenishmentQty" />
												</span>
											</th>
										</tr>
										<tr>
											<th id="${entry.key.supplierKcode}-epd-courier" class="text-center" style="background-color: #f9f9f9; width: 6%;border-bottom: 1px solid #dee2e6;">
												<spring:message code="replenishmentPrediction.Courier" />
											</th>
											<th id="${entry.key.supplierKcode}-epd-airFreight" class="text-center" style="background-color: #f9f9f9; width: 6%;border-bottom: 1px solid #dee2e6;">
												<spring:message code="replenishmentPrediction.AirFreight" />
											</th>
											<th id="${entry.key.supplierKcode}-epd-surfaceFreight" class="text-center" style="border-right: 1px solid #ddd; background-color: #f9f9f9; width: 6%;border-bottom: 1px solid #dee2e6;">
												<spring:message code="replenishmentPrediction.SurfaceFreight" />
											</th>
											<th id="${entry.key.supplierKcode}-esd-courier" class="text-center" style="border-left: 1px solid #ddd; background-color: #f9f9f9; width: 6%;border-bottom: 1px solid #dee2e6;">
												<spring:message code="replenishmentPrediction.Courier" />
											</th>
											<th id="${entry.key.supplierKcode}-esd-airFreight" class="text-center" style="background-color: #f9f9f9;width: 6%;border-bottom: 1px solid #dee2e6;">
												<spring:message code="replenishmentPrediction.AirFreight" />
											</th>
											<th id="${entry.key.supplierKcode}-esd-surfaceFreight" class="text-center" style="border-right: 1px solid #ddd; background-color: #f9f9f9; width: 6%;border-bottom: 1px solid #dee2e6;">
												<spring:message code="replenishmentPrediction.SurfaceFreight" />
											</th>
										</tr>
									</thead>
									<tbody class="border-bottom border-t" style="border-bottom: 1px solid #dee2e6;">
									<c:if test="${not empty report}">				
										<c:forEach var="items" items="${entry.value}">
											<tr>
												<td class="text-left" style="padding-left: 15px;">
													<span class="control-label" data-toggle="tooltip" data-placement="top" title="${items.skuName}">
														${items.skuNumber}
													</span>
												</td>
												<td class="text-right">
													<input class="form-control" type="text" style="text-align:right; width: 100px;" value="${items.productionDays}" name="mlt"  />
												</td>
												<td class="text-right" style="padding-right: 15px;">${items.sellableInventory}</td>
												<td class="text-right" style="border-left: 1px solid #ddd;">${items.planninQuantity} </td>									
												<td class="text-right">${items.inboundQuantity} </td>
												<td class="text-center" style="border-right: 1px solid #ddd;">${items.expectedArrivalDate}</td>
												<td class="text-right" style="padding-right: 8px;">${items.averageWeeklySalesQuantity}</td>
												<td class="text-left">																		  
													<input class="form-control" type="text" style="text-align:right; width: 100px;" value="" name="mQty"  />									
												</td>
												<td class="text-center" style="border-right: 1px solid #ddd;">${items.expectedStockDepletionDate}</td>									
												<td class="text-center">${items.expectedProductionDate.courierDate}</td>
												<td class="text-center">${items.expectedProductionDate.airFreightDate}</td>
												<td class="text-center">${items.expectedProductionDate.surfaceFreightDate}</td>
												<td class="status_${items.expectedShippingDate.courierStatus} text-center">																																					
													<c:if test = "${not empty items.expectedShippingDate.courierDate}">
														<c:choose>
															<c:when test="${items.expectedShippingDate.courierSelloutRisk eq true}">
																<c:choose>
																	<c:when test="${items.expectedShippingDate.courierMinOutOfStockDays eq items.expectedShippingDate.courierMaxOutOfStockDays}">
																		${items.expectedShippingDate.courierDate} <span class="text-danger fas fa-exclamation-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="<spring:message code='replenishmentPrediction.outOfStockDays' arguments='${items.expectedShippingDate.courierMinOutOfStockDays}'/>"></span>														
																	</c:when>
																	<c:otherwise>
																		${items.expectedShippingDate.courierDate} <span class="text-danger fas fa-exclamation-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="<spring:message code='replenishmentPrediction.outOfStockDaysDuring' arguments='${items.expectedShippingDate.courierMinOutOfStockDays},${items.expectedShippingDate.courierMaxOutOfStockDays}'/>"></span>															
																	</c:otherwise>
																</c:choose>									
															</c:when>
															<c:otherwise>
																${items.expectedShippingDate.courierDate}
															</c:otherwise>
														</c:choose>       														        									
													</c:if>      									      									      									
												</td>
												<td class="status_${items.expectedShippingDate.airFreightStatus} text-center">
													<c:if test = "${not empty items.expectedShippingDate.airFreightDate}">
														<c:choose>
															<c:when test="${items.expectedShippingDate.airFreightSelloutRisk eq true}">
																<c:choose>
																	<c:when test="${items.expectedShippingDate.airFreightMinOutOfStockDays eq items.expectedShippingDate.airFreightMaxOutOfStockDays}">
																		${items.expectedShippingDate.airFreightDate} <span class="text-danger fas fa-exclamation-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="<spring:message code='replenishmentPrediction.outOfStockDays' arguments='${items.expectedShippingDate.airFreightMinOutOfStockDays}'/>"></span>														
																	</c:when>
																	<c:otherwise>
																		${items.expectedShippingDate.airFreightDate} <span class="text-danger fas fa-exclamation-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="<spring:message code='replenishmentPrediction.outOfStockDaysDuring' arguments='${items.expectedShippingDate.airFreightMinOutOfStockDays},${items.expectedShippingDate.airFreightMaxOutOfStockDays}'/>"></span>															
																	</c:otherwise>
																</c:choose>									
															</c:when>
															<c:otherwise>
																${items.expectedShippingDate.airFreightDate}
															</c:otherwise>
														</c:choose>       														        									
													</c:if>										
												</td>
												<td class="status_${items.expectedShippingDate.surfaceFreightStatus} text-center">
													<c:if test = "${not empty items.expectedShippingDate.surfaceFreightDate}">
														<c:choose>
															<c:when test="${items.expectedShippingDate.surfaceFreightSelloutRisk eq true}">
																<c:choose>
																	<c:when test="${items.expectedShippingDate.surfaceFreightMinOutOfStockDays eq items.expectedShippingDate.surfaceFreightMaxOutOfStockDays}">
																		${items.expectedShippingDate.surfaceFreightDate} <span class="text-danger fas fa-exclamation-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="<spring:message code='replenishmentPrediction.outOfStockDays' arguments='${items.expectedShippingDate.surfaceFreightMinOutOfStockDays}'/>"></span>														
																	</c:when>
																	<c:otherwise>
																		${items.expectedShippingDate.surfaceFreightDate} <span class="text-danger fas fa-exclamation-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="<spring:message code='replenishmentPrediction.outOfStockDaysDuring' arguments='${items.expectedShippingDate.surfaceFreightMinOutOfStockDays},${items.expectedShippingDate.surfaceFreightMaxOutOfStockDays}'/>"></span>															
																	</c:otherwise>
																</c:choose>									
															</c:when>
															<c:otherwise>
																${items.expectedShippingDate.surfaceFreightDate}
															</c:otherwise>
														</c:choose>       														        									
													</c:if>																												
												</td>
												<td class="text-right" style="border-left: 1px solid #ddd; padding-right: 15px;">
													${items.expectedEightWeeksReplenishmentQuantity}
												</td>								
											</tr>
										</c:forEach>
									</c:if>
								</tbody>
								</table>
							</div>
						</div>
				</c:forEach>		
			</div>
			</div>
			</div>
			
		</div>
	</div>	
<script>
	$(function() {
		// var report = ${reportJson};
		// for(var o in report) {
		// 	var col = $("#"+o+ "-epd-courier, "+"#"+o+ "-epd-airFreight, "+"#"+o+ "-epd-surfaceFreight, "+"#"+o+ " tr td:nth-child(10), "+"#"+o+ " tr td:nth-child(11), "+"#"+o+ " tr td:nth-child(12)");	        	
        // 	$("#"+o+"-epd").hide();
    	// 	col.hide();		    		    		
		// }
	});
</script>