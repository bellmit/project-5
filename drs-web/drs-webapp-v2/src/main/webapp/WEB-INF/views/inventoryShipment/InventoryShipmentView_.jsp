<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@	taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
	<title>
		<c:choose>
			<c:when test="${InventoryShipment.status eq 'SHPT_DRAFT' || InventoryShipment.status eq 'SHPT_AWAIT_PLAN' || InventoryShipment.status eq 'SHPT_PLANNING'}">
				<spring:message code='inventoryShipment.inventoryShipment' />: ${InventoryShipment.name} - DRS		
			</c:when>
			<c:otherwise>
				<spring:message code='inventoryShipment.inventoryPurchaseOrder' />: ${InventoryShipment.name} - DRS					
			</c:otherwise>
		</c:choose>
	</title>	

<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>

<script>
	var app = angular.module('inventoryShipmentView', []);
	app.controller('inventoryShipmentViewCtrl', function($scope) {
		
		var InventoryShipmentJson = ${InventoryShipmentJson};	
		var lineItems = InventoryShipmentJson.lineItems;			 					
		var totalQuantity = 0 ;				
		var totalCartonCounts = 0;
				
		for(i = 0; i < lineItems.length; i++){			
			var quantity = Number(lineItems[i].quantity);
			var cartonCounts = Number(lineItems[i].cartonCounts);
			totalQuantity += quantity;
			totalCartonCounts += cartonCounts; 
		}		
		$("#totalQuantity").html(totalQuantity);
		$("#totalCartonCounts").html(totalCartonCounts);
				
		$scope.lineItems = [];
		$scope.mixedContentBoxes = [];
				
		for(i = 0; i < lineItems.length; i++){		
			if(lineItems[i].mixedBoxLineSeq == 0) $scope.lineItems.push(lineItems[i]);				
		}	
								
		for(i = 0; i < lineItems.length; i++){
			if(lineItems[i].mixedBoxLineSeq != 0){
				if(typeof $scope.mixedContentBoxes[lineItems[i].boxNum] == "undefined") $scope.mixedContentBoxes[lineItems[i].boxNum] = [];
				$scope.mixedContentBoxes[lineItems[i].boxNum].push(lineItems[i]);
			}
		}
								
		$scope.mixedContentBoxes = $scope.mixedContentBoxes.filter(function(el) { return el; });

		$scope.redirectIvsVp = function(lineSeq , boxNum , mixedBoxLineSeq , skuCode , productVerificationStatus , qty){
            $("#lineSeq").val(lineSeq)
            $("#boxNum").val(boxNum)
            $("#mixedBoxLineSeq").val(mixedBoxLineSeq)
            $("#sku").val(skuCode)
            $("#productVerificationStatus").val(productVerificationStatus)
            $("#qty").val(qty)
            $("#ivsVp").submit();

        };
														
	});
	
	function submitConfirm(shipmentId){		
		$("#dialog-confirm").html('<spring:message code="inventoryShipment.submitISWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
				{text:"<spring:message code='inventoryShipment.yes' /> ",
				name:"yes",	
				click: function() {
					$(this).dialog('close');
						location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/submit";				      					
					}					
				},				
				{text:"<spring:message code='inventoryShipment.no' /> ",
				name:"no",	
				click: function() {
					$(this).dialog('close');										      					
					}						
				}				           				           				           
			]
		});		
	}

	function acceptConfirm(shipmentId) {		
		$("#dialog-confirm").html('<spring:message code="inventoryShipment.acceptISWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
				{text:"<spring:message code='inventoryShipment.yes' /> ",
				name:"yes",	
				click: function() {
					$(this).dialog('close');
						location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/accept";				      					
						}					
				},				
				{text:"<spring:message code='inventoryShipment.no' /> ",
				name:"no",	
				click: function() {
					$(this).dialog('close');										      					
					}						
				}				           				           				           
			]
		});				
	}
		
	function confirmConfirm(shipmentId) {	
		$("#dialog-confirm").html('<spring:message code="inventoryShipment.confirmISWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
				{text:"<spring:message code='inventoryShipment.yes' /> ",
				name:"yes",	
				click: function() {
					$(this).dialog('close');
						location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/confirm";				      					
					}					
				},				
				{text:"<spring:message code='inventoryShipment.no' /> ",
				name:"no",	
				click: function() {
					$(this).dialog('close');										      					
					}						
				}				           				           				           
			]
		});			
	}
	
	function deleteConfirm(shipmentId) {		
		$("#dialog-confirm").html('<spring:message code="inventoryShipment.deleteISWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
				{text:"<spring:message code='inventoryShipment.yes' /> ",
				name:"yes",	
				click: function() {
					$(this).dialog('close');
						location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/delete";				      					
					}					
				},				
				{text:"<spring:message code='inventoryShipment.no' /> ",
				name:"no",	
				click: function() {
					$(this).dialog('close');										      					
					}
						
				}				           				           				           
			]
		});
	}	
</script>
<style>
	.non-padding {
		padding: 0px !important;
	}
</style>
</head>
<div class="max-width" style="max-width:1600px !important;padding-left:20px;padding-right:20px;" ng-app="inventoryShipmentView" ng-controller="inventoryShipmentViewCtrl">
	<div class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid">
		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
						<c:when test="${InventoryShipment.status eq 'SHPT_DRAFT' || InventoryShipment.status eq 'SHPT_AWAIT_PLAN' || InventoryShipment.status eq 'SHPT_PLANNING'}">
							<spring:message code='inventoryShipment.inventoryShipment' />		
						</c:when>
						<c:otherwise>
							<spring:message code='inventoryShipment.inventoryPurchaseOrder' />					
						</c:otherwise>
					</c:choose>
					
	  					<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpIVS.pdf"/>" target="_blank">
							 <i class="fas fa-question-circle"></i> 	<spring:message code="inventoryShipment.help" />								   						
						</a>  				
	  			
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-5">
				  <table class="table table-withoutBorder no-head">
					<tr>
						<td>
							<b><spring:message code='inventoryShipment.inventoryShipmentId' /></b>
						</td>
						<td>${InventoryShipment.name}</td>
					</tr>
					<tr>
						<td>
							<b><spring:message code='inventoryShipment.FCADeliveryLocation' /></b></td>
						<td>
							${FCADeliveryLocationIdToLocationMap[InventoryShipment.fcaDeliveryLocationId]}
						</td>
					</tr>				
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.destination' /></b>
						</td>
						<td>
							<spring:message code="${InventoryShipment.destinationCountry}" />
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.shippingMethod' /></b>
						</td>
						<td>
							<spring:message code="${InventoryShipment.shippingMethod}" />
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.expectedExportDate' /></b>
						</td>
						<td>
							${InventoryShipment.expectedExportDate}
						</td>
					</tr>								
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.FCADeliveryDate' /></b>
						</td>
						<td>
							${InventoryShipment.fcaDeliveryDate}
						</td>
					</tr>					
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.specialRequest' /></b>
							<label data-toggle="tooltip" data-placement="top" title="<spring:message code="inventoryShipment.special_request_hint" />">
								<span class="glyphicon glyphicon-info-sign" style="color:#428bca;"></span>
							</label>
						</td>
						<td>
							${InventoryShipment.specialRequest}
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.boxesNeedRepackaging' /></b>
						</td>
						<td>
							${InventoryShipment.boxesNeedRepackaging}
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.repackagingFee' /></b>
						</td>
						<td>
							${InventoryShipment.repackagingFee}
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.requiredPO' /></b>
						</td>
						<td>
							<spring:message code='inventoryShipment.${InventoryShipment.requiredPO}' />
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.PONumber' /></b>
						</td>
						<td>
							${InventoryShipment.PONumber}
						</td>
					</tr>															
				</table>
			</div>
			<div class="col-md-5">
				 <table class="table table-withoutBorder no-head">
					<tr>
						<td>
							<b><spring:message code='inventoryShipment.status' /></b>
						</td>
						<td>
							<spring:message code="${InventoryShipment.status}" />
						</td>
					</tr>
					<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
					<tr>
						<td >
							<b><spring:message code='inventoryShipment.seller' /></b>
						</td>
						<td>
							${InventoryShipment.sellerCompanyKcode} ${AllCompanyKcodeToNameMap[InventoryShipment.sellerCompanyKcode]}
						</td>
					</tr>
					<tr>
						<td >
							<b><spring:message code='inventoryShipment.buyer' /></b>
						</td>
						<td>
							${InventoryShipment.buyerCompanyKcode} ${AllCompanyKcodeToNameMap[InventoryShipment.buyerCompanyKcode]}
						</td>
					</tr>
					</sec:authorize>
					<tr>
						<td >
							<b><spring:message code='inventoryShipment.dateCreated' /></b>
						</td>
						<td>${InventoryShipment.dateCreated}</td>
					</tr>
					<tr>
						<td >
							<b><spring:message code='inventoryShipment.invoiceNumber' /></b>
						</td>
						<td>${InventoryShipment.invoiceNumber}</td>
					</tr>
					<sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
						<c:choose>
							<c:when test="${InventoryShipment.status eq 'SHPT_CONFIRMED'}">								
								<tr>
									<td></td>
									<td style="color: #2A6496;">
										<spring:message code='inventoryShipment.invoiceNumber_hint'/>
									</td>
								</tr>
							</c:when>		
						</c:choose>
				    </sec:authorize>
				    <tr>
				    	<td >
				    		<b><spring:message code='inventoryShipment.datePurchased'/></b>
				    	</td>
				    	<td>${InventoryShipment.datePurchased}</td>
				    </tr>
				    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
				    <tr>
				    	<td >
				    		<b><spring:message code='inventoryShipment.internalNote'/></b>
				    	</td>
				    	<td>${InventoryShipment.internalNote}</td>
				    </tr>
				    </sec:authorize>
				     <sec:authorize access="hasAnyRole('${auth['ADMIN_ONLY']}')">
				    <c:choose>
				    	<c:when test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
				    		<tr>
				    			<td class="text-right">
				    				<b><spring:message code='inventoryShipment.shippingCost'/></b>
				    			</td>
				    			<td>${shippingCost}</td>
				    		</tr>
				    	</c:when>
				    </c:choose>			
				      </sec:authorize>	    
				</table>
			</div>
		</div>

		<br>		
		<div class="row">
			<div class="col-md-12">
				<table class="table" style="width: 100%" colspan="15">
					<thead>
						<tr>
							<th style="width: 12%">
								<spring:message code='inventoryShipment.sku' />
							</th>							
							<th style="width: 18%;">
								<spring:message code='inventoryShipment.skuName' />
							</th>
							<th style="width: 3%;">
								<spring:message code='inventoryShipment.requireRepackaging' />
							</th>
							<th class="text-center" style="width: 5%;">
								<spring:message code='inventoryShipment.cartonNumber' />
							</th>
							<th class="text-center" style="width: 12%;">
								<spring:message code='inventoryShipment.cartonDimensions' />
							</th>
							<th class="text-right" style="width: 4.8%;">
								<spring:message code='inventoryShipment.grossWeightPerCarton' />
							</th>
							<th class="text-right" style="width: 5%;">
								<spring:message code='inventoryShipment.unitsPerCarton' />
							</th>
							<th class="text-right" style="width: 3%;">
								<spring:message code='inventoryShipment.numberOfCartons' />
							</th>
							<th class="text-right" style="width: 5%;">
								<spring:message code='inventoryShipment.quantity' />
							</th>
							<th class="text-right" style="width: 5.5%;">
								<spring:message code='inventoryShipment.uniPrice' />
							</th>
							<th class="text-right" style="width: 5.5%;">
								<spring:message code='inventoryShipment.amount' />
							</th>
							<th class="text-right" style="width: 7.8%;">
								<spring:message code='inventoryShipment.GUIInvoiceNumber' />
							</th>
							<th class="text-right" style="width: 5%;">
								<spring:message code='inventoryShipment.GUIInvoice' />
							</th>

							 <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
							<th class="text-right" style="width: 4.2%;">
								<span>產品狀態</span>
							</th>
							<th class="text-right" style="width: 4.2%">
								<span>確認產品</span>
							</th>
							</sec:authorize>

						</tr>
					</thead>
					<!-- non mixedContentBox -->
					<tr ng-repeat="lineItem in lineItems">
						<td>{{lineItem.skuCode}}</td>
						<td>{{lineItem.nameBySupplier}}</td>
						<td ng-if="lineItem.requireRepackaging == true" class="text-center">
							<spring:message code='inventoryShipment.true' />						
						</td>
						<td ng-if="lineItem.requireRepackaging == false" class="text-center">
							<spring:message code='inventoryShipment.false' />
						</td>
						<td class="text-center">{{lineItem.cartonNumberStart}} ~ {{lineItem.cartonNumberEnd}}</td>
						<td class="text-center">{{lineItem.cartonDimensionCm1}} x {{lineItem.cartonDimensionCm2}} x {{lineItem.cartonDimensionCm3}}</td>
						<td class="text-right">{{lineItem.perCartonGrossWeightKg}}</td>
						<td class="text-right">{{lineItem.perCartonUnits}}</td>
						<td class="text-right">{{lineItem.cartonCounts}}</td>
						<td class="text-right">{{lineItem.quantity}}</td>
						<td class="text-right">{{lineItem.unitAmount}}</td>
						<td class="text-right">{{lineItem.amountUntaxed}}</td>
						<td class="text-right">{{lineItem.guiinvoiceNumber}}</td >
						<td class="text-right">
							<a href="${pageContext.request.contextPath}/InventoryShipments/downloadGUIInvoiceFile?shipmentName=${shipmentId}&GUIInvoiceFileName={{lineItem.guifileName}}">
								{{lineItem.guifileName}}</a>
						</td>


						 <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
						<td class="text-right">
							<span class="verify-status">{{lineItem.productVerificationStatus}}</span>

						</td>
						<td class="text-right">
							<input name="verify" class="btn btn-default btn-sm" type="button"
							value="Go"
							 ng-click="redirectIvsVp(lineItem.lineSeq,lineItem.boxNum,lineItem.mixedBoxLineSeq , lineItem.skuCode,lineItem.productVerificationStatus,lineItem.quantity)">
						</td>
						</sec:authorize>


					</tr>						
					<tr ng-show="mixedContentBoxes.length > 0">
						<td class="text-info" colspan="15">
							<!-- 以下為混箱產品 -->
							---<spring:message code='inventoryShipment.mixedContentBox' />---
						</td>
					</tr>
					<tr ng-repeat="mixedContentBox in mixedContentBoxes">		
						<td class="non-padding" colspan="15">							
							<table class="table" style="width: 100%">
								<thead>
									<tr>
										<th style="width: 12%;">
											<spring:message code='inventoryShipment.sku' />
										</th>							
										<th style="width: 18%;">
											<spring:message code='inventoryShipment.skuName' />
										</th>
										<th style="width: 3%;">
											<spring:message code='inventoryShipment.requireRepackaging' />
										</th>
										<th class="text-center" style="width: 5%;">
											<spring:message code='inventoryShipment.cartonNumber' />
										</th>
										<th class="text-center" style="width: 12%;">
											<spring:message code='inventoryShipment.cartonDimensions' />
										</th>
										<th class="text-right" style="width: 4.8%;">
											<spring:message code='inventoryShipment.grossWeightPerCarton' />
										</th>
										<th class="text-right" style="width: 5%;">
											<spring:message code='inventoryShipment.unitsPerCarton' />
										</th>
										<th class="text-right" style="width: 3%;">
											<spring:message code='inventoryShipment.numberOfCartons' />
										</th>
										<th class="text-right" style="width: 5%;">
											<spring:message code='inventoryShipment.quantity' />
										</th>
										<th class="text-right" style="width: 5.5%;">
											<spring:message code='inventoryShipment.uniPrice' />
										</th>
										<th class="text-right" style="width: 5.5%;">
											<spring:message code='inventoryShipment.amount' />
										</th>
										<th class="text-right" style="width: 7.8%;">
											<spring:message code='inventoryShipment.GUIInvoiceNumber' />
										</th>
										<th class="text-right" style="width: 5%;">
											<spring:message code='inventoryShipment.GUIInvoice' />
										</th>


									    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
										<th class="text-right" style="width: 4.2%;">
											<span>產品狀態</span>
										</th>
										<th class="text-right" style="width: 4.2%">
											<span>確認產品</span>
										</th>
										</sec:authorize>

									</tr>
								</thead>
								<tbody>
									<tr ng-repeat="mixedContentBoxItem in mixedContentBox">
										<td>{{mixedContentBoxItem.skuCode}}</td>
										<td>{{mixedContentBoxItem.nameBySupplier}}</td>
										<td ng-if="mixedContentBoxItem.requireRepackaging == true" class="text-center">
											<spring:message code='inventoryShipment.true' />						
										</td>
										<td ng-if="mixedContentBoxItem.requireRepackaging == false" class="text-center">
											<spring:message code='inventoryShipment.false' />
										</td>				
										<td class="text-center">{{mixedContentBoxItem.cartonNumberStart}} ~ {{mixedContentBoxItem.cartonNumberEnd}}</td>
										<td class="text-center">{{mixedContentBoxItem.cartonDimensionCm1}} x {{mixedContentBoxItem.cartonDimensionCm2}} x {{mixedContentBoxItem.cartonDimensionCm3}}</td>
										<td class="text-right">{{mixedContentBoxItem.perCartonGrossWeightKg}}</td>
										<td class="text-right">{{mixedContentBoxItem.perCartonUnits}}</td>
										<td class="text-right">{{mixedContentBoxItem.cartonCounts}}</td>
										<td class="text-right">{{mixedContentBoxItem.quantity}}</td>
										<td class="text-right">{{mixedContentBoxItem.unitAmount}}</td>
										<td class="text-right">{{mixedContentBoxItem.amountUntaxed}}</td>
										<td class="text-right">{{mixedContentBoxItem.guiinvoiceNumber}}</td>
										<td class="text-right">
											<a href="${pageContext.request.contextPath}/InventoryShipments/downloadGUIInvoiceFile?shipmentName=${shipmentId}&GUIInvoiceFileName={{mixedContentBoxItem.guifileName}}">
											{{mixedContentBoxItem.guifileName}}</a>
										</td>


										<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
										<td class="text-right">
											<span class="verify-status">{{mixedContentBoxItem.productVerificationStatus}}</span>
										</td>
										<td class="text-right">
											<span>
											<input name="verify" class="btn btn-default btn-sm" type="button"value="Go"
                                                  ng-click="redirectIvsVp(mixedContentBoxItem.lineSeq,mixedContentBoxItem.boxNum,mixedContentBoxItem.mixedBoxLineSeq,mixedContentBoxItem.skuCode,mixedContentBoxItem.productVerificationStatus,mixedContentBoxItem.quantity)">
											</span>
										</td>
										</sec:authorize>

									</tr>
								</tbody>
							</table>
						</td>	
					</tr>											
					<tr>
						<td colspan="15"></td>
					</tr>
					<!-- TOTAL AMOUNT -->
					<tr>
						<td colspan="9"></td>							
						<td class="text-right">
							<b><spring:message code='inventoryShipment.subtotal' /></b>
						</td>
						<td class="text-right">${InventoryShipment.subtotal}</td>
					</tr>
					<tr>
						<td colspan="8"></td>
						<td colspan="2" class="text-right">
							<b><spring:message code='inventoryShipment.taxAmount' />(${InventoryShipment.salesTaxPercentage}%)</b>
						</td>
						<td class="text-right">${InventoryShipment.salesTax}</td>
					</tr>
					<tr>
						<td colspan="6"></td>
						<td class="text-right">
							<b><spring:message code='inventoryShipment.total' /></b>
						</td>
						<td id="totalCartonCounts" class="text-right"></td>
						<td id="totalQuantity" class="text-right"></td>							
						<td>								
						</td>
						<td class="text-right">
							<b>${InventoryShipment.total}</b>
						</td>
					</tr>
					<tr>
						<td colspan="8"></td>
						<td colspan="2" class="text-right">
							<b><spring:message code='inventoryShipment.paid' /></b>
						</td>
						<td class="text-right">								
							<a target="_blank" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/InventoryPayment">
									<b>${InventoryShipment.paidTotal}</b>
							</a>																
						</td>
					</tr>						
				</table>				
				<div id="dialog-confirm"></div>							
			</div>
		</div>
		<div style="padding-bottom: 20px"></div>
		<c:choose>
					<c:when test="${InventoryShipment.status eq 'SHPT_DRAFT'}">
						<sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
							<div style="float: right">
								<input class="btn btn-success" type="button" value="<spring:message code="inventoryShipment.requestPickup" />" onclick="submitConfirm('${InventoryShipment.name}');" />
								<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/editDraft">
									<spring:message code="inventoryShipment.editInventoryShipment" />
								</a>
								<input class="btn btn-link" type="button" value="<spring:message code="inventoryShipment.deleteInventoryShipment" />" onclick="deleteConfirm('${shipmentId}');" />
								<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
									<spring:message code="inventoryShipment.backToInventoryShipment" />
								</a>			
							</div>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
							<div style="float: right">
								<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
									<spring:message code="inventoryShipment.backToInventoryShipment" />
								</a>
							</div>
						</sec:authorize>				
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${InventoryShipment.status eq 'SHPT_AWAIT_PLAN'}">
								<sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
									<div style="float: right">
										<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
											<spring:message code="inventoryShipment.editInventoryShipment" />
										</a>
										<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
											<spring:message code="inventoryShipment.backToInventoryShipment" />
										</a>
									</div>							
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
									<div style="float: right">

                                        <!-- <input class="btn btn-success" type="button" value="<spring:message code="inventoryShipment.accept" />" onclick="acceptConfirm('${InventoryShipment.name}');" />-->

										<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
											<spring:message code="inventoryShipment.editInventoryShipment" />
										</a>
										<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
											<spring:message code="inventoryShipment.backToInventoryShipment" />
										</a>		
									</div>																
								</sec:authorize>
							</c:when>
							<c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
								<sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
									<div style="float: right">
										<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
											<spring:message code="inventoryShipment.editInventoryShipment" />
										</a>
										<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
											<spring:message code="inventoryShipment.backToInventoryShipment" />
										</a>
									</div>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
									<div style="float: right">
										<input class="btn btn-success" type="button" value="<spring:message code="inventoryShipment.confirmInventoryShipment" />" onclick="confirmConfirm('${InventoryShipment.name}');" />
										<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
											<spring:message code="inventoryShipment.editInventoryShipment" />
										</a>
										<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
											<spring:message code="inventoryShipment.backToInventoryShipment" />
										</a>		
									</div>
								</sec:authorize>
							</c:when>							  
							<c:otherwise>
								<sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
									<div style="float: right">
										<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
											<spring:message code="inventoryShipment.backToInventoryShipment" />
										</a>
									</div>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
									<div style="float: right">
										<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
											<spring:message code="inventoryShipment.editInventoryShipment" />
										</a>
										<a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
											<spring:message code="inventoryShipment.backToInventoryShipment" />
										</a>									
									</div>
								</sec:authorize>
							</c:otherwise>								
						</c:choose>	
					</c:otherwise>
				</c:choose>		
		<div class="row">
			<div class="col-md-12 text-danger">
				<spring:message code='inventoryShipment.note' />
			</div>
		</div>
	</div>
</div>

<form id="ivsVp" action="/shipment/ivp" method="post">
   <input type="hidden" id="ivs" name="ivs" value=${InventoryShipment.name}>
   <input type="hidden" id="dc" name="dc" value=${InventoryShipment.destinationCountry}>
   <input type="hidden" id="lineSeq" name="lineSeq" value="">
   <input type="hidden" id="boxNum" name="boxNum" value="">
   <input type="hidden" id="mixedBoxLineSeq" name="mixedBoxLineSeq" value="">
   <input type="hidden" id="sku" name="sku" value="">
   <input type="hidden" id="productVerificationStatus" name="productVerificationStatus" value="">
   <input type="hidden" id="qty" name="qty" value="">

</form>