<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="${type}"/><spring:message code="unifiedShipment.name"/> - DRS
</title>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jquery-ui-timepicker-addon.css"/>"></link>
<script type='text/javascript' src="<c:url value="/resources/js/jquery-ui-timepicker-addon.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css"/>" type="text/css" rel="stylesheet">	
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">	
<script>
	
	function onSubmit(){
		switch(document.pressed){	 
			case 'saveDraft':
				document.UnifiedShipment.action = "${pageContext.request.contextPath}/UnifiedShipments/saveDraft";		   
				break;
			case 'updateDraft':
				document.UnifiedShipment.action ="${pageContext.request.contextPath}/UnifiedShipments/updateDraft";
				break;
	
			default:		
		}	 
		return true;	  	  
	}
	
	angular.module('unifiedShipment', []).controller('unifiedShipmentCtrl', function($scope) {
		
		var control = {create : function(tp_inst, obj, unit, val, min,max, step) {
			$('<input class="ui-timepicker-input" value="'	+ val + '" style="width:50%">')
				.appendTo(obj).spinner({
									min : min,
									max : max,
									step : step,
									change : function(e, ui) {														
										if (e.originalEvent !== undefined)
										tp_inst._onTimeChange();
										tp_inst._onSelectHandler();
									},
									spin : function(e, ui) {
										tp_inst.control.value(tp_inst, obj,unit, ui.value);
										tp_inst._onTimeChange();
										tp_inst._onSelectHandler();
									}
									});
					return obj;
				},
				options : function(tp_inst, obj, unit, opts, val) {
					if (typeof (opts) == 'string' && val !== undefined){
					    return obj.find('.ui-timepicker-input').spinner(opts, val);
					}else{
						return obj.find('.ui-timepicker-input').spinner(opts);
					}
				},
				value : function(tp_inst, obj, unit, val) {
					if (val !== undefined){
					    return obj.find('.ui-timepicker-input').spinner('value', val);
					}else{
						return obj.find('.ui-timepicker-input').spinner('value');
					}
				}
			};			
			$('#destinationReceivedDate,#expectArrivalDate,#exportDate').attr('readonly', true);			
			jQuery("#destinationReceivedDate,#expectArrivalDate").datetimepicker({
				beforeShow: function() {
			        setTimeout(function(){
			            $('.ui-datepicker').css('z-index', 200);
			        }, 0);
			    },
				controlType : control,
				dateFormat : "yy-mm-dd",
				timeFormat : 'HH:mm z',
				timezoneList : [ {
					value : +480,
					label : 'Taipei'
				}, {
					value : -480,
					label : 'Pacific'
				} ]
			});			
			jQuery("#exportDate").datepicker({
				beforeShow : function() {
					setTimeout(function() {
						$('.ui-datepicker').css('z-index', 200);
					}, 0);
				},
				dateFormat : 'yy-mm-dd'				
			});
				
		$scope.dstCountry = [];
		$scope.dstCountry['US'] = "<spring:message code='US' />";
		$scope.dstCountry['CA'] = "<spring:message code='CA' />";
		$scope.dstCountry['UK'] = "<spring:message code='UK' />";
		$scope.dstCountry['DE'] = "<spring:message code='DE' />";
		$scope.dstCountry['FR'] = "<spring:message code='FR' />";
		$scope.dstCountry['EU'] = "<spring:message code='EU' />";
		
		$scope.addLineItem = function(id){
			$scope.lineItems.push({
				sourceInventoryShipmentName:'',
				skuCode:'',
				cartonNumberStart:0,
				cartonNumberEnd:0,
				cartonDimensionCm1:0,
				cartonDimensionCm2:0,
				cartonDimensionCm3:0,
				perCartonGrossWeightKg:0,
				perCartonUnits:0,
				quantity:0,
				subtotal:0,
				cifSubtotal:0});
		};
		 				
		function removeAllOptions(id) {
			 $("#"+id).empty();
		}
				 
		$scope.getDestinationCountryCodeList = function(){																													
			$("#buyerCompany").text("");
			$scope.buyerCompanyKcode = "";				
			$("#exchangeRateContent").html("");
			removeAllOptions("destinationCountry");				
			$scope.getAvailableInventoryShipmentNameList();				
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/UnifiedShipments/getDestinationCountryCodeList',			
				contentType : "application/json; charset=utf-8",
				data:  {sellerKcode: $scope.sellerCompanyKcode},
				dataType : "json",
				success : function(destinationCountryCodeList) {										
					$('<option>').val("").text("--- Select ---").appendTo('#destinationCountry');						
					for(var key in destinationCountryCodeList){						
						if (destinationCountryCodeList.hasOwnProperty(key)) {								
							$('<option>').val(destinationCountryCodeList[key]).text($scope.dstCountry[destinationCountryCodeList[key]]).appendTo('#destinationCountry');																															
						}														
					}						
					$scope.$apply();						
				}					
			});				
		};

		$scope.getActualDestinationCountryCodeList = function(){
			$("#buyerCompany").text("");
			$scope.buyerCompanyKcode = "";
			$("#exchangeRateContent").html("");
			$scope.getAvailableInventoryShipmentNameList();
			removeAllOptions("actualDestination");
			$scope.emptyLineItems();
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/UnifiedShipments/getActualDestinationCountryCodeList',
				contentType : "application/json; charset=utf-8",
				data:  {destinationCountry: $scope.destinationCountry},
				dataType : "json",
				success : function(actualDestinationCountryList) {
					$('<option>').val("").text("--- Select ---").appendTo('#actualDestination');
					for(var key in actualDestinationCountryList){
						if (actualDestinationCountryList.hasOwnProperty(key)) {
							$('<option>').val(actualDestinationCountryList[key]).text($scope.dstCountry[actualDestinationCountryList[key]]).appendTo('#actualDestination');
						}
					}
					$scope.$apply();
				}
			});
		};
		 
		$scope.getDrsCompanyKcode = function(){								
			var drsCompany = [];	
			drsCompany['K3'] = "Beanworthy";
			drsCompany['K4'] = "BW UK";
			drsCompany['K5'] = "BW CA";
			drsCompany['K6'] = "BW DE";
			drsCompany['K7'] = "BW FR";
			$("#buyerCompany").text("");
			$scope.buyerCompanyKcode = "";
			$("#exchangeRateContent").html("");
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/UnifiedShipments/getDrsCompanyKcode',			
				contentType : "application/json; charset=utf-8",
				data:  {countryCode: $scope.actualDestination},
				dataType : "json",
				success : function(drsCompanyKcode) {															
					$scope.getExchangeRateInfo($scope.actualDestination);
					$("#buyerCompany").text(drsCompanyKcode+" "+drsCompany[drsCompanyKcode]);							
					$scope.buyerCompanyKcode = drsCompanyKcode;
				}					
			});				
		};
		 
		$scope.getExchangeRateInfo = function(destinationCountryCode){		 		
			var currencyInfo = [];	
		 	currencyInfo['TW'] = "<spring:message code='TWD' />";
		 	currencyInfo['US'] = "<spring:message code='USD' />";								
		 	currencyInfo['UK'] = "<spring:message code='GBP' />";
		 	currencyInfo['DE'] = "<spring:message code='EUR' />";
		 	currencyInfo['FR'] = "<spring:message code='EUR' />";
		 	currencyInfo['CA'] = "<spring:message code='CAD' />";
		 	currencyInfo['EU'] = "<spring:message code='EUR' />";
		 	$("#exchangeRateContent").html(currencyInfo['TW']+"<spring:message code='To' />"+currencyInfo[destinationCountryCode]+" ");		 					 
		};
		 
		$scope.getAvailableInventoryShipmentNameList = function(){						 		
			$.ajax({
				type : 'get',
		 		url : '${pageContext.request.contextPath}/UnifiedShipments/getAvailableInventoryShipmentNameList',			
		 		contentType : "application/json; charset=utf-8",
		 		data:  {sellerKcode: $scope.sellerCompanyKcode,destinationCountryCode:$scope.destinationCountry},
		 		dataType : "json",
		 		success : function(availableInventoryShipmentNameList) {

		 		    console.log("BBBBBBBBBBBBBBB")
					$scope.amountTotal = 0;
 					var rows = $('#InventoryShipments > tbody > tr');
 					rows.hide();
 					$('input[type=checkbox]').prop('checked',false);
 					var nodata = true; 						
						for(var item in $scope.lineItems){		 							 							 					
		 					if ($scope.lineItems.hasOwnProperty(item)) {		 						
		 						$scope.lineItems[item]['sourceInventoryShipmentName'] = '';
		 						$scope.lineItems[item]['skuCode'] = '';
		 						$scope.lineItems[item]['skuName'] = '';
		 						$scope.lineItems[item]['boxNum'] = null;
		 						$scope.lineItems[item]['mixedBoxLineSeq'] = null;
					 			$scope.lineItems[item]['cartonNumberStart'] = 0;
					 			$scope.lineItems[item]['cartonNumberEnd'] = 0;
		 						$scope.lineItems[item]['cartonDimensionCm1'] = 0;
		 						$scope.lineItems[item]['cartonDimensionCm2'] = 0;
		 						$scope.lineItems[item]['cartonDimensionCm3'] = 0;
		 						$scope.lineItems[item]['perCartonGrossWeightKg'] = 0;
		 						$scope.lineItems[item]['perCartonUnits'] = 0;
		 						$scope.lineItems[item]['cartonCounts'] = "";		 						
		 						$scope.lineItems[item]['quantity'] = 0;		 						
		 						$scope.lineItems[item]['unitAmount'] = "";		 						
		 						$scope.lineItems[item]['amountUntaxed'] = 0;
		 						$scope.$apply();		 								 								 						
		 						for(var key in availableInventoryShipmentNameList){		 							
		 							if (availableInventoryShipmentNameList.hasOwnProperty(key)) {		 									 						    		
										rows.each(function(){
											if($(this).children().eq(1).find('a').text()==availableInventoryShipmentNameList[key]) {
												$(this).show();
												nodata = false;
											}
										});										
		 							}		 						    
		 						}		 					
		 					}		 							 							 					
		 				}
		 				nodata?$('#InventoryShipments > tfoot > tr').show():$('#InventoryShipments > tfoot > tr').hide();					 					 				
		 			}		 					 			
		 		});		 				
		 };
			 			 	
		$scope.getShipmentLineItem = function(scope){		 				 		
			$.ajax({
				type : 'get',
		 		url : '${pageContext.request.contextPath}/UnifiedShipments/getShipmentLineItem/',			
		 		contentType : "application/json; charset=utf-8",
		 		data:  {inventoryShipmentName:scope.item.sourceInventoryShipmentName},
		 		dataType : "json",
		 		success : function(ShipmentLineItems) {
			 		$.each(ShipmentLineItems, function(index, ShipmentLineItem) {
			 			scope.item.boxNum = ShipmentLineItem['boxNum'];
			 			scope.item.mixedBoxLineSeq = ShipmentLineItem['mixedBoxLineSeq'];
			 			scope.item.cartonNumberStart = ShipmentLineItem['cartonNumberStart'];
			 			scope.item.cartonNumberEnd = ShipmentLineItem['cartonNumberEnd'];
			 			scope.item.cartonDimensionCm1 = ShipmentLineItem['cartonDimensionCm1'];
			 			scope.item.cartonDimensionCm2 = ShipmentLineItem['cartonDimensionCm2'];
			 			scope.item.cartonDimensionCm3 = ShipmentLineItem['cartonDimensionCm3'];
			 			scope.item.perCartonGrossWeightKg = ShipmentLineItem['perCartonGrossWeightKg'];
			 			scope.item.perCartonUnits = ShipmentLineItem['perCartonUnits'];
			 			scope.item.cartonCounts = ShipmentLineItem['cartonCounts'];
			 			scope.item.quantity = ShipmentLineItem['quantity'];
			 			scope.calculateAmount(scope);
			 			scope.calculateTotalAmount();
			 			scope.$apply();
		 			});
		 		}		 			
		 	});		 			
		 };
		 		 		 
		 $scope.calculateAmount = function(scope){		 		
			var amount = Math.round(Number(scope.item.quantity)*Number(scope.item.unitAmount)* Math.pow(10, 6)) / Math.pow(10, 6);
		 	amount = amount.toFixed(2);		 		
		 	scope.item.subtotal = amount.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
		 	scope.calculateTotalAmount();		 				 				 			 				 				 		
		 };

		 $scope.calculateCifAmount = function(scope){
			var cifAmount = Math.round(Number(scope.item.quantity)*Number(scope.item.unitCifAmount)* Math.pow(10, 6)) / Math.pow(10, 6);
			cifAmount = cifAmount.toFixed(2);
			scope.item.cifSubtotal = cifAmount.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
			scope.calculateCifTotalAmount();
		 };

		 $scope.calculateTotalQuantity = function(){		 		
		 	var totalQuantity = 0;		 		
		 	for(var i = 0; i < $scope.lineItems.length;i++){	 				
		 		var quantity = Number($scope.lineItems[i].quantity);
		 		totalQuantity += Number(quantity);		 			 							 		
		 	}		 		
		 	totalQuantity = totalQuantity.toFixed(0);
		 	$scope.quantityTotal = totalQuantity.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");		 		
		 };
		 	
		 $scope.calculateTotalAmount = function(){		 	
			var totalAmount = 0;		 		
		 		for(var i = 0; i < $scope.lineItems.length;i++){	 				
		 			var subtotal = $scope.lineItems[i].subtotal;
		 			subtotal = subtotal.toString().replace(",","");		 			
		 			totalAmount += Number(subtotal);			 			 							 		
		 		}		 			 		
		 	var tAmount = Math.round(totalAmount * Math.pow(10, 2)) / Math.pow(10, 2);
		 	tAmount = tAmount.toFixed(2);		 		
		 	$scope.amountTotal = tAmount.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");		 		
		 };

		 $scope.calculateCifTotalAmount = function(){
			 var cifTotalAmount = 0;
			 for(var i = 0; i < $scope.lineItems.length;i++){
		 			var cifSubtotal = $scope.lineItems[i].cifSubtotal;
		 			cifSubtotal = cifSubtotal.toString().replace(",","");
		 			cifTotalAmount += Number(cifSubtotal);
		 		}
			 var cifTAmount = Math.round(cifTotalAmount * Math.pow(10, 2)) / Math.pow(10, 2);
			 cifTAmount = cifTAmount.toFixed(2);
			 $scope.cifAmountTotal = cifTAmount.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
		 };

		if(document.URL.indexOf("edit") > -1){			
			var UnifiedShipmentJson = ${UnifiedShipmentJson};				
			var lineItems = UnifiedShipmentJson.lineItems;	
			var drsCompanyKcodeToNameJson = ${drsCompanyKcodeToNameJson};
			$scope.sellerCompanyKcode = UnifiedShipmentJson['sellerCompanyKcode'];
			$scope.buyerCompanyKcode = UnifiedShipmentJson['buyerCompanyKcode'];
			$scope.destinationCountry = UnifiedShipmentJson['destinationCountry'];
			$scope.actualDestination = UnifiedShipmentJson['actualDestination'];
			$scope.shippingMethod = UnifiedShipmentJson['shippingMethod']; 
			$scope.fbaId = UnifiedShipmentJson['fbaId'];
			$scope.exportCurrencyExchangeRate = UnifiedShipmentJson['exportCurrencyExchangeRate'];
			$scope.exportFxRateToEur = UnifiedShipmentJson['exportFxRateToEur'];
			$scope.exportDate = UnifiedShipmentJson['exportDate'];
			$scope.expectArrivalDate = UnifiedShipmentJson['expectArrivalDate'];
			$scope.destinationReceivedDate = UnifiedShipmentJson['destinationReceivedDate'];								
			$("#buyerCompany").text($scope.buyerCompanyKcode+" "+drsCompanyKcodeToNameJson[$scope.buyerCompanyKcode]);				
			$('#'+$scope.shippingMethod).prop('checked',true);			
			$scope.lineItems = [];
				for(var lineItem in lineItems){					
					if (lineItems.hasOwnProperty(lineItem)) {					
						var cartonDimensionCm1 = Math.round(lineItems[lineItem].cartonDimensionCm1 * Math.pow(10, 1)) / Math.pow(10, 1);
		 				var cartonDimensionCm2 = Math.round(lineItems[lineItem].cartonDimensionCm2 * Math.pow(10, 1)) / Math.pow(10, 1);
		 				var cartonDimensionCm3 = Math.round(lineItems[lineItem].cartonDimensionCm3 * Math.pow(10, 1)) / Math.pow(10, 1);
		 				var perCartonGrossWeightKg = Math.round(lineItems[lineItem].perCartonGrossWeightKg * Math.pow(10, 2)) / Math.pow(10, 2);						
		 				$scope.lineItems.push({
		 					sourceInventoryShipmentName:lineItems[lineItem].sourceInventoryShipmentName,
							skuCode:lineItems[lineItem].skuCode,
							skuName:lineItems[lineItem].nameBySupplier,
							boxNum:lineItems[lineItem].boxNum,
			 				mixedBoxLineSeq:lineItems[lineItem].mixedBoxLineSeq,
							cartonNumberStart:lineItems[lineItem].cartonNumberStart,
							cartonNumberEnd:lineItems[lineItem].cartonNumberEnd,
							cartonDimensionCm1:	cartonDimensionCm1.toFixed(1).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
							cartonDimensionCm2:	cartonDimensionCm2.toFixed(1).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
							cartonDimensionCm3: cartonDimensionCm3.toFixed(1).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
							perCartonGrossWeightKg: perCartonGrossWeightKg.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
							perCartonUnits: lineItems[lineItem].perCartonUnits,
							cartonCounts: lineItems[lineItem].cartonCounts,
							quantity: lineItems[lineItem].quantity,
							unitAmount:Number(lineItems[lineItem].unitAmount.replace(/[^0-9\.-]+/g,"")),
							unitCifAmount:lineItems[lineItem].unitCifAmount,
							subtotal: lineItems[lineItem].subtotal,
							cifSubtotal: lineItems[lineItem].cifSubtotal
						});					
					}								
				};				
				$scope.calculateTotalQuantity();
				$scope.amountTotal = UnifiedShipmentJson['amountTotal'];				
				$scope.cifAmountTotal = UnifiedShipmentJson['cifAmountTotal'];
				getDestinationCountry();
				getActualDestinationCountry();
				$scope.actualDestination = UnifiedShipmentJson['actualDestination'];
				$scope.getExchangeRateInfo($scope.actualDestination);
				$.ajax({
					type : 'get',
					url : '${pageContext.request.contextPath}/UnifiedShipments/getAvailableInventoryShipmentNameList',			
					contentType : "application/json; charset=utf-8",
					data:  {sellerKcode: $scope.sellerCompanyKcode,destinationCountryCode:$scope.destinationCountry},
					dataType : "json",
					success : function(availableInventoryShipmentNameList) {

					console.log("AAAAAAAAAAAAAAAAAAAAA")
 						var rows = $('#InventoryShipments > tbody > tr');
 						rows.hide();
 						$('input[type=checkbox]').prop('checked',false);
 						var nodata = true;
 						for(var key in availableInventoryShipmentNameList){							
 							if (availableInventoryShipmentNameList.hasOwnProperty(key)) {							
 						    	rows.each(function(){
									if($(this).children().eq(1).find('a').text()==availableInventoryShipmentNameList[key]) {
										$(this).show();										
										nodata = false;
									}
								});
 							}						    
 						};
 						nodata?$('#InventoryShipments > tfoot > tr').show():$('#InventoryShipments > tfoot > tr').hide(); 						
						for(var item in $scope.lineItems){			 					
		 					if ($scope.lineItems.hasOwnProperty(item)) {
								$("#"+$scope.lineItems[item]['sourceInventoryShipmentName']).prop('checked',true);										
		 					}		 							 							 					
		 				}																												
					}						
				});																								
		}else{
			$scope.shippingMethod = 'EXPRESS';
			$scope.lineItems = [{
				sourceInventoryShipmentName:'',
				skuCode:'',
				cartonNumberStart:0,
				cartonNumberEnd:0,
				cartonDimensionCm1:0,
				cartonDimensionCm2:0,
				cartonDimensionCm3:0,
				perCartonGrossWeightKg:0,
				perCartonUnits:0,
				cartonCounts:0,
				quantity:0,
				subtotal:0,
				cifSubtotal:0}];
			$scope.quantityTotal = 0;
			$scope.amountTotal = 0;
			$scope.cifAmountTotal = 0;
		}	
		 					
	function getDestinationCountry(){												
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/UnifiedShipments/getDestinationCountryCodeList',			
			contentType : "application/json; charset=utf-8",
			data:  {sellerKcode: $scope.sellerCompanyKcode},
			dataType : "json",
			success : function(destinationCountryCodeList) {				
				removeAllOptions("destinationCountry");						
					$('<option>').val("").text("--- Select ---").appendTo('#destinationCountry');						
						for(var key in destinationCountryCodeList){						
							if (destinationCountryCodeList.hasOwnProperty(key)) {								
								$('<option>').val(destinationCountryCodeList[key]).text($scope.dstCountry[destinationCountryCodeList[key]]).appendTo('#destinationCountry');																															
							}														
						}						
				$scope.$apply();
				$("#destinationCountry").val($scope.destinationCountry);						
			}					
		});									
	}

	function getActualDestinationCountry(){
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/UnifiedShipments/getActualDestinationCountryCodeList',
			contentType : "application/json; charset=utf-8",
			data:  {destinationCountry: $scope.destinationCountry},
			dataType : "json",
			success : function(actualDestinationCountryList) {
				removeAllOptions("actualDestination");
					$('<option>').val("").text("--- Select ---").appendTo('#actualDestination');
						for(var key in actualDestinationCountryList){
							if (actualDestinationCountryList.hasOwnProperty(key)) {
								$('<option>').val(actualDestinationCountryList[key]).text($scope.dstCountry[actualDestinationCountryList[key]]).appendTo('#actualDestination');
							}
						}
				$scope.$apply();
				$("#actualDestination").val($scope.actualDestination);
			}
		});
	}
					
	$scope.getAvailableSkus = function(sourceInventoryShipmentName){			 		
		$.ajax({
			type : 'get',
		 	url : '${pageContext.request.contextPath}/UnifiedShipments/getAvailableSkusInInventoryShipment/',			
		 	contentType : "application/json; charset=utf-8",
		 	data:  {shipmentName: sourceInventoryShipmentName},
		 	dataType : "json",
		 	success : function(availableSkusInInventoryShipment) {
 				$scope.addShipmentLineItem(sourceInventoryShipmentName);								
		 	}		 			
		});	 			
	}
		 	
	$scope.addShipmentLineItem = function(sourceInventoryShipmentName){			 		
		$.ajax({
			type : 'get',
		 	url : '${pageContext.request.contextPath}/UnifiedShipments/getShipmentLineItem/',			
		 	contentType : "application/json; charset=utf-8",
		 	data:  {inventoryShipmentName:sourceInventoryShipmentName},
		 	dataType : "json",
		 	success : function(ShipmentLineItems) {
		 		$.each(ShipmentLineItems, function(index, ShipmentLineItem) {
					if(( $scope.lineItems.length==1) & ($scope.lineItems[0]['sourceInventoryShipmentName']=='')) $scope.lineItems.splice(0,1);
						var cartonDimensionCm1 = Math.round(ShipmentLineItem['cartonDimensionCm1'] * Math.pow(10, 1)) / Math.pow(10, 1);
			 			var cartonDimensionCm2 = Math.round(ShipmentLineItem['cartonDimensionCm2'] * Math.pow(10, 1)) / Math.pow(10, 1);
			 			var cartonDimensionCm3 = Math.round(ShipmentLineItem['cartonDimensionCm3'] * Math.pow(10, 1)) / Math.pow(10, 1);
			 			var perCartonGrossWeightKg = Math.round(ShipmentLineItem['perCartonGrossWeightKg'] * Math.pow(10, 2)) / Math.pow(10, 2);
			 			$scope.lineItems.push({
			 				sourceInventoryShipmentName:sourceInventoryShipmentName,
			 				skuCode:ShipmentLineItem['skuCode'],
			 				skuName:ShipmentLineItem['nameBySupplier'],
			 				boxNum:ShipmentLineItem['boxNum'],
			 				mixedBoxLineSeq:ShipmentLineItem['mixedBoxLineSeq'],
				 			cartonNumberStart:ShipmentLineItem['cartonNumberStart'],
				 			cartonNumberEnd:ShipmentLineItem['cartonNumberEnd'],
			 				cartonDimensionCm1:cartonDimensionCm1.toFixed(1).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
			 				cartonDimensionCm2:cartonDimensionCm2.toFixed(1).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
			 				cartonDimensionCm3:cartonDimensionCm3.toFixed(1).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
			 				perCartonGrossWeightKg:perCartonGrossWeightKg.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
			 				perCartonUnits:ShipmentLineItem['perCartonUnits'],
			 				cartonCounts:ShipmentLineItem['cartonCounts'],
			 				quantity:ShipmentLineItem['quantity'],
			 				subtotal:0,
			 				cifSubtotal:0});
			 				$scope.calculateTotalAmount();
		 				    $scope.calculateCifTotalAmount();
			 				$scope.calculateTotalQuantity();
			 				$scope.$apply();
		 		});
		 	}		 			
		});		 			
	}		 	

	$scope.removeShipmentLineItem = function(sourceInventoryShipmentName){
		for(var i = $scope.lineItems.length-1; i >-1; i--){
			if($scope.lineItems[i]['sourceInventoryShipmentName']==sourceInventoryShipmentName) {						 	 				 
				$scope.lineItems.splice(i,1);					 
				$scope.calculateTotalQuantity();
				$scope.calculateTotalAmount();
				$scope.calculateCifTotalAmount();
				$scope.$apply();
			 }						 							 					
 		}
		if($scope.lineItems.length==0) {
			$scope.addLineItem(0);
			$scope.$apply();
		}
	};

	$scope.emptyLineItems = function() {
	    $scope.lineItems = [{
            sourceInventoryShipmentName:'',
            skuCode:'',
            cartonNumberStart:0,
            cartonNumberEnd:0,
            cartonDimensionCm1:0,
            cartonDimensionCm2:0,
            cartonDimensionCm3:0,
            perCartonGrossWeightKg:0,
            perCartonUnits:0,
            cartonCounts:0,
            quantity:0,
            subtotal:0,
            cifSubtotal:0}];
        $scope.quantityTotal = 0;
        $scope.amountTotal = 0;
        $scope.cifAmountTotal = 0;
	}
	
	});
</script>
</head>
<div id="unifiedShipment" class="max-width" ng-app="unifiedShipment" ng-controller="unifiedShipmentCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">		
				<div class="page-heading">
					<c:choose>
						<c:when test="${status eq 'Draft'}">
							<spring:message code="Edit"/><spring:message code="unifiedShipment.title"/>
						</c:when>
						<c:otherwise>
							<spring:message code="unifiedShipment.create"/>
						</c:otherwise>
					</c:choose>
				</div>
				<span class="text-danger"><spring:message code='unifiedShipment.required' /></span>
			</div>
		</div>
		<form:form name="UnifiedShipment"  modelAttribute="UnifiedShipment" onsubmit="return onSubmit();" novalidate="novalidate">			
			<div class="row">
				<div class="col-md-12">			
					<table class="table table-withoutBorder table-autoWidth">
						<c:choose>
							<c:when test="${status eq 'Draft'}">
								<tr>
									<td class="text-right">
										<b><spring:message code="unifiedShipment.id"/></b>
									</td>
									<td>${UnifiedShipment.name}<form:hidden path="name" /></td>
									<td></td>
									<td class="text-right"><b><spring:message code="unifiedShipment.status"/></b></td>
									<td class="active"><spring:message code="${UnifiedShipment.status}" /></td>
								</tr>
							</c:when>
						</c:choose>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.seller"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:select id="sellerCompanyKcode" class="form-control" path="sellerCompanyKcode" ng-model="sellerCompanyKcode" ng-change="getDestinationCountryCodeList();" required="required">
										<form:option value="" label="--- Select ---" />
										<form:option value="K2" label="K2 KindMinds" />														
									</form:select>
									<div class="text-danger" ng-show="UnifiedShipment.sellerCompanyKcode.$error.required && UnifiedShipment.sellerCompanyKcode.$dirty">
      	 								<spring:message code='unifiedShipment.seller_req'/>
									</div>
								</td>
								<td></td>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.buyer"/></b>
								</td>
								<td>
									<span id="buyerCompany"></span>
									<form:hidden id="buyerCompanyKcode" path="buyerCompanyKcode" ng-model="buyerCompanyKcode" value="{{buyerCompanyKcode}}"/>
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.destination"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:select id="destinationCountry" class="form-control" path="destinationCountry" ng-model="destinationCountry" ng-change= "getActualDestinationCountryCodeList();" required="required">
									</form:select>
									<div class="text-danger" ng-show="UnifiedShipment.destinationCountry.$error.required && UnifiedShipment.destinationCountry.$dirty">
      	 								<spring:message code='unifiedShipment.destination_req'/>
									</div>
								</td>
								<td></td>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.actualDestination"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:select id="actualDestination" class="form-control" path="actualDestination" ng-model="actualDestination" ng-change="getDrsCompanyKcode();" required="required">
									</form:select>
									<div class="text-danger" ng-show="UnifiedShipment.actualDestination.$error.required && UnifiedShipment.actualDestination.$dirty">
      	 								<spring:message code='unifiedShipment.destination_req'/>
									</div>
								</td>
							</tr>				
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.shippingMethod"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<span class="radio radio-success radio-inline" style="margin-top:15px;">
										<input type="radio" class="form-control" name="shippingMethod"  path="shippingMethod" ng-model="shippingMethod" style="width: 5%; display: inline;" value="EXPRESS"  id="EXPRESS"/>
										<label for="EXPRESS" ><spring:message code="EXPRESS"/></label>
									</span>
									<span class="radio radio-success radio-inline" style="margin-top:15px;">
										<input type="radio" class="form-control" name="shippingMethod"  path="shippingMethod" ng-model="shippingMethod" style="width: 5%; display: inline;" value="AIR_CARGO"  id="AIR_CARGO"/>
										<label for="AIR_CARGO" ><spring:message code="AIR_CARGO"/></label>
									</span>
									<span class="radio radio-success radio-inline" style="margin-top:15px;">
										<input type="radio" class="form-control" name="shippingMethod" path="shippingMethod"  ng-model="shippingMethod" style="width: 5%; display: inline;" value="SEA_FREIGHT"  id="SEA_FREIGHT"/>
										<label for="SEA_FREIGHT" ><spring:message code="SEA_FREIGHT"/></label>
									</span>						
								</td>
								<td></td>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.invoiceNumber"/></b>
								</td>
								<td>
									${UnifiedShipment.invoiceNumber}
									<form:hidden path="invoiceNumber" ng-model="invoiceNumber" />
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.fbaId"/><span class="text-danger">*</span></b>
								</td>					
								<td>
									<form:input id="fbaId" class="form-control" path="fbaId" ng-model="fbaId" required="required" />
									<div class="text-danger" ng-show="UnifiedShipment.fbaId.$error.required && UnifiedShipment.fbaId.$dirty">
      	 								<spring:message code='unifiedShipment.fbaId_req'/>
									</div>
								</td>
								<td colspan="3"></td>
							</tr>				
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.exportDate"/></b>
								</td>
								<td>
									<form:input id="exportDate" class="form-control" path="exportDate" style="cursor:default;background-color:white;" ng-model="exportDate"/>					
								</td>
								<td></td>
								<td class="text-right">
									<b><span id="exchangeRateContent"></span><spring:message code="unifiedShipment.exchangeRate"/></b>
								</td>
								<td>
									<form:input id="exportCurrencyExchangeRate" class="form-control" path="exportCurrencyExchangeRate" ng-model="exportCurrencyExchangeRate" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" />					
									<div class="text-danger" ng-show="UnifiedShipment.exportCurrencyExchangeRate.$error.pattern">
      	 								<spring:message code='unifiedShipment.exchangeRate_format'/>
									</div>				
								</td>				
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.trackingNumber"/></b>
								</td>					
								<td><form:input id="trackingNumber" class="form-control" path="trackingNumber" /></td>
								<td></td>
								<td ng-show="destinationCountry=='UK'" class="text-right">
									<b><spring:message code="unifiedShipment.exportFxRateToEur"/></b>
								</td>
								<td ng-show="destinationCountry=='UK'">
									<form:input id="exportFxRateToEur" class="form-control" path="exportFxRateToEur" ng-model="exportFxRateToEur" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" />							
								</td>
							<c:choose>
								<c:when test="${status eq 'Draft' && UnifiedShipment.destinationCountry ne 'UK'}">
									<td class="text-right">
										<b><spring:message code="unifiedShipment.dateCreated"/></b>
									</td>
									<td>${UnifiedShipment.dateCreated}</td>				
								</c:when>
							</c:choose>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.forwarder"/></b>
								</td>					
								<td>
									<form:select id="forwarder" class="form-control" path="forwarder" >
										<form:option value="">--- Select ---</form:option>
										<c:forEach var="forwarder" items="${forwarderList}">
										<form:option value="${forwarder}">${forwarder}</form:option>										
										</c:forEach>
									</form:select>
								</td>
								<td></td>
							<c:choose>
								<c:when test="${status eq 'Draft' && UnifiedShipment.destinationCountry eq 'UK'}">
									<td class="text-right">
										<b><spring:message code="unifiedShipment.dateCreated"/></b>
									</td>
									<td>${UnifiedShipment.dateCreated}</td>				
								</c:when>
							</c:choose>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.expectArrivalDate"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:input id="expectArrivalDate" class="form-control" path="expectArrivalDate" style="cursor:default;background-color:white;" ng-model="expectArrivalDate" required="required"/>					
									<div class="text-danger" ng-show="UnifiedShipment.expectArrivalDate.$error.required && UnifiedShipment.expectArrivalDate.$dirty">
      	 								<spring:message code='unifiedShipment.expectArrivalDate_req'/>
									</div>
								</td>
								<td colspan="3"></td>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.destReceivedDate"/></b>
								</td>
								<td>
									<form:input id="destinationReceivedDate" class="form-control" style="cursor:default;background-color:white;" path="destinationReceivedDate" ng-model="destinationReceivedDate"/>
								</td>						
								<td colspan="3"></td>				
							</tr>																
					</table>
				</div>
			</div>						
			<div style="padding-bottom: 40px"></div>
			<div class="row">
				<div class="col-md-10">
						<table id="InventoryShipments" class="table table-floated" style="border-collapse:separate;">
							<thead>
								<tr>
									<th>Select</th>
									<th><spring:message code='inventoryShipment.inventoryShipmentId' /></th>							
									<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
										<th>Supplier</th>
									</sec:authorize>
									<th class="text-center"><spring:message code='inventoryShipment.FCADeliveryDate' /></th>
									<th class="text-center"><spring:message code='inventoryShipment.expectedExportDate' /></th>
									<th class="text-center"><spring:message code='inventoryShipment.destination' /></th>
									<th class="text-center"><spring:message code='inventoryShipment.shippingMethod' /></th>
									<th class="text-center"><spring:message code='inventoryShipment.status' /></th>							
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${ShipmentOfInventoryList}" var="shipmentOfInventory">
								<c:if test="${shipmentOfInventory.status eq 'SHPT_CONFIRMED' or shipmentOfInventory.status eq 'SHPT_FC_BOOKING_CONFIRM'}">
								<tr style="display:none;">
									<td>
										<span class="checkbox checkbox-success" style="margin-top:0px;">
											<input type="checkbox" class="form-control" name="${shipmentOfInventory.name}" style="width: 5%; display: inline;" value="false"  id="${shipmentOfInventory.name}">
											<label for="${shipmentOfInventory.name}" ></label>
										</span>
									</td>
									<td>
										<c:choose>
											<c:when test="${shipmentOfInventory.status eq 'SHPT_DRAFT'}">
											<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
												<a href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}">${shipmentOfInventory.name}</a>
											</sec:authorize>
											<sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
												<a href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}/editDraft">${shipmentOfInventory.name}</a>
											</sec:authorize>
											</c:when>
											<c:otherwise>
												<a href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}">${shipmentOfInventory.name}</a>
											</c:otherwise>
										</c:choose>
									</td>								
								<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
									<td>${shipmentOfInventory.sellerCompanyKcode} ${AllCompanyKcodeToNameMap[shipmentOfInventory.sellerCompanyKcode]}</td>
								</sec:authorize>
									<td class="text-center">${shipmentOfInventory.fcaDeliveryDate}</td>
									<td class="text-center">${shipmentOfInventory.expectedExportDate}</td>
									<td class="text-center"><spring:message code="${shipmentOfInventory.destinationCountry}" /></td>
									<td class="text-center"><spring:message code="${shipmentOfInventory.shippingMethod}" /></td>
									<td class="text-center"><spring:message code="${shipmentOfInventory.status}" /></td>
								</tr>
								</c:if>
							</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="8">no data</td>
								</tr>
							</tfoot>
					</table>
				</div>
			</div>									
		<div style="padding-bottom: 40px"></div>
			<div class="row">
				<div class="col-md-12">
					<table class="table">
						<thead>
							<tr id="newShipment">
								<th style="width:100px;"><spring:message code="unifiedShipment.isId"/></th>
								<th style="width:170px;"><spring:message code="unifiedShipment.sku"/></th>
								<th style="width:170px;"><spring:message code="unifiedShipment.skuName"/></th>
								<th class="text-center"><spring:message code="unifiedShipment.cartonNumber"/></th>
								<th class="text-left"  style="width:180px;"><spring:message code="unifiedShipment.cartonDimensions"/></th>
								<th class="text-right"><spring:message code="unifiedShipment.grossWeightPerCarton"/></th>
								<th class="text-right"><spring:message code="unifiedShipment.unitsPerCarton"/></th>
								<th class="text-center"><spring:message code="unifiedShipment.numberOfCartons"/></th>
								<th class="text-right"><spring:message code="unifiedShipment.quantity"/></th>
								<th class="text-right"><spring:message code="unifiedShipment.unitPrice"/><span class="text-danger">*</span></th>
								<th class="text-right"><spring:message code="unifiedShipment.unitCifAmount"/></th>
								<th class="text-right"><spring:message code="unifiedShipment.DDPamount"/></th>
								<th class="text-right"><spring:message code="unifiedShipment.CIFamount"/></th>
							</tr>
						</thead>
						<tbody>
							<tr id="newShipment{{$id}}" ng-repeat="item in lineItems">
								<td>
									<input type="hidden" name="lineItem[{{$id}}].lineSeq" ng-model="item.lineSeq" value="{{$index+1}}">						
									<span>{{item.sourceInventoryShipmentName}}</span>
									<input type="hidden" id="sourceInventoryShipmentName{{$index}}" name="lineItem[{{$id}}].sourceInventoryShipmentName" ng-model="item.sourceInventoryShipmentName" value="{{item.sourceInventoryShipmentName}}" />
								</td>
								<td>
									<span>{{item.skuCode}}</span>
									<input type="hidden" id="skuCode{{$index}}" name="lineItem[{{$id}}].skuCode" ng-model="item.skuCode" value="{{item.skuCode}}" />
									<input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum" ng-model="item.boxNum" value="{{item.boxNum}}" />
				   					<input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq" ng-model="item.mixedBoxLineSeq" value="{{item.mixedBoxLineSeq}}" />
								</td>
								<td>{{item.skuName}}</td>
								<td class="text-center active">
									{{item.cartonNumberStart}} ~ {{item.cartonNumberEnd}}
				   					<input type="hidden" id="cartonNumberStart{{$id}}" name="lineItem[{{$id}}].cartonNumberStart" ng-model="item.cartonNumberStart" value="{{item.cartonNumberStart}}" />
				   					<input type="hidden" id="cartonNumberEnd{{$id}}" name="lineItem[{{$id}}].cartonNumberEnd" ng-model="item.cartonNumberEnd" value="{{item.cartonNumberEnd}}" />
								</td>
								<td class="text-center active">
									{{item.cartonDimensionCm1}} X {{item.cartonDimensionCm2}} X {{item.cartonDimensionCm3}}
									<input type="hidden" id="cartonDimensionCm1-{{$id}}" name="lineItem[{{$id}}].cartonDimensionCm1" ng-model="item.cartonDimensionCm1" size="2" value="{{item.cartonDimensionCm1}}" />
									<input type="hidden" id="cartonDimensionCm2-{{$id}}" name="lineItem[{{$id}}].cartonDimensionCm2" ng-model="item.cartonDimensionCm2" size="2" value="{{item.cartonDimensionCm2}}" />
									<input type="hidden" id="cartonDimensionCm3-{{$id}}" name="lineItem[{{$id}}].cartonDimensionCm3" ng-model="item.cartonDimensionCm3" size="2" value="{{item.cartonDimensionCm3}}" />																								
								</td>
								<td class="text-right active">
									{{item.perCartonGrossWeightKg}}
									<input type="hidden" id="perCartonGrossWeightKg{{$id}}" name="lineItem[{{$id}}].perCartonGrossWeightKg" ng-model="item.perCartonGrossWeightKg" size="5" value="{{item.perCartonGrossWeightKg}}" />
								</td>
								<td class="text-right active">
									{{item.perCartonUnits}}
									<input type="hidden" id="units{{$id}}" name="lineItem[{{$id}}].perCartonUnits" ng-model="item.perCartonUnits" size="7" value="{{item.perCartonUnits}}" />
								</td>
								<td class="text-center active">
									{{item.cartonCounts}}
									<input type="hidden" id="number{{$id}}" name="lineItem[{{$id}}].cartonCounts" ng-model="item.cartonCounts"  value="{{item.cartonCounts}}" />	
								</td>	
								<td class="text-right active">
									{{item.quantity}}
									<input type="hidden" id="quantity{{$id}}" name="lineItem[{{$id}}].quantity" ng-model="item.quantity" size="7" value="{{item.quantity}}" />											
								</td>
								<td class="text-center">
									<input id="unitPrice{{$id}}" class="form-control" style="text-align:right;" name="lineItem[{{$id}}].unitAmount" ng-model="item.unitAmount" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" size="7" ng-change="calculateAmount(this);" required/>
									<div class="text-danger" ng-show="UnifiedShipment['lineItem[{{$id}}].unitAmount'].$error.required && UnifiedShipment['lineItem[{{$id}}].unitAmount'].$dirty">
      	 								<spring:message code='unifiedShipment.unitPrice_req'/>
									</div>			
									<div class="text-danger" ng-show="UnifiedShipment['lineItem[{{$id}}].unitAmount'].$error.pattern">
      	 								<spring:message code='unifiedShipment.unitPrice_format'/>
									</div>																											
								</td>
								<td class="text-center">
									<input id="unitCifAmount{{$id}}" class="form-control" style="text-align:right;" name="lineItem[{{$id}}].unitCifAmount" ng-model="item.unitCifAmount" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" ng-change="calculateCifAmount(this);" size="7" />
								</td>
								<td class="text-right active">
									{{item.subtotal}}
									<input type="hidden" id="amount{{$id}}" name="lineItem[{{$id}}].subtotal" ng-model="item.subtotal" size="10" value="{{item.subtotal}}" />
								</td>
								<td class="text-right active">
									{{item.cifSubtotal}}
									<input type="hidden" id="cifAmount{{$id}}" name="lineItem[{{$id}}].cifSubtotal" ng-model="item.cifSubtotal" size="10" value="{{item.cifSubtotal}}" />
								</td>
							</tr>				
						</tbody>				
							<tr>
								<td colspan="7"></td>
									<td class="text-right">
										<b><spring:message code="unifiedShipment.totalQuantity" /></b>
								</td>
								<td class="text-right">{{quantityTotal}}</td>
								<td></td>
								<td class="text-right">
									<b><spring:message code="unifiedShipment.totalAmount"/></b>
								</td>
								<td class="text-right active">
									{{amountTotal}}
								<form:hidden id="totalAmount" path="amountTotal" size="10" ng-model="amountTotal" value="{{amountTotal}}" />					
								</td>
								<td class="text-right active">
									{{cifAmountTotal}}
								<form:hidden id="cifAmountTotal" path="cifAmountTotal" size="10" ng-model="cifAmountTotal" value="{{cifAmountTotal}}" />
								</td>
							</tr>
					</table>
				</div>			
			</div>			
			<div style="padding-bottom: 20px"></div>
			<div style="float: right">
				<sec:authorize access="hasAnyRole('${auth['UNIFIED_SHIPMENTS_CREATE']}')">	
					<c:choose>
						<c:when test="${type eq 'Create'}">						
							<input class="btn btn-primary" type="submit" onclick="document.pressed=this.name" name="saveDraft" value="<spring:message code="unifiedShipment.saveDraft" />" ng-disabled="UnifiedShipment.$invalid" />
							<a class="btn btn-link" href="${pageContext.request.contextPath}/UnifiedShipments">
								<spring:message code="unifiedShipment.back" />
							</a>
						</c:when>
						<c:otherwise>							
							<input class="btn btn-primary" type="submit" onclick="document.pressed=this.name" name="updateDraft" value="<spring:message code="unifiedShipment.saveDraft" />" ng-disabled="UnifiedShipment.$invalid" />
							<a class="btn btn-link" href="${pageContext.request.contextPath}/UnifiedShipments">
								<spring:message code="unifiedShipment.back" />
							</a>
						</c:otherwise>
					</c:choose>					
				</sec:authorize>
			</div>									
		</form:form>		
	</div>
</div>
<script>
$('input[type=checkbox]').change(function(){
	var ang = angular.element(document.getElementById('unifiedShipment'));
	if($(this).prop('checked')) {
		ang.scope().getAvailableSkus($(this).attr('id'));
	} else {
		ang.scope().removeShipmentLineItem($(this).attr('id'));
	}
});
</script>