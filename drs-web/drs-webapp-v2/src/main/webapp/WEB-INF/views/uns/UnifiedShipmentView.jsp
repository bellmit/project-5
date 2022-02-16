<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="unifiedShipment.title" />: ${UnifiedShipment.name} - DRS
</title>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jquery-ui-timepicker-addon.css"/>"></link>
<script type='text/javascript' src="<c:url value="/resources/js/jquery-ui-timepicker-addon.js"/>"></script>
<script>
	$(document).ready(function() {	
		var UnifiedShipmentJson = ${UnifiedShipmentJson};				
		var lineItems = UnifiedShipmentJson.lineItems;	
		var totalQuantity = 0;		
		for (i = 0; i < lineItems.length; i++) { 		
			var quantity = lineItems[i].quantity;
			totalQuantity += Number(quantity);			
		}	
		$("#totalQuantity b").html(totalQuantity);
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
	});	
	
	function freezeConfirm(shipmentId) {		
		$("#dialog-confirm").html('<spring:message code="unifiedShipment.freezeUSWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [{
				text : "<spring:message code='unifiedShipment.yes' /> ",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/UnifiedShipments/" + shipmentId + "/freeze";
				}
			},{
				text:"<spring:message code='unifiedShipment.no' />",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			}]
		});		
	}
		
	function deleteConfirm(shipmentId) {
		$("#dialog-confirm").html('<spring:message code="unifiedShipment.deleteUSWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [{
				text : "<spring:message code='unifiedShipment.yes' /> ",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/UnifiedShipments/" + shipmentId + "/delete";
				}
			},{
				text:"<spring:message code='unifiedShipment.no' />",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			}]
		});
	}		
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="unifiedShipment.title" />
				</div>
			</div>
		</div>
		<c:url var="updateAction" value="/UnifiedShipments/updateStatus"></c:url>
		<form:form action="${updateAction}"  modelAttribute="UnifiedShipment">
			<div class="row">
				<div class="col-md-12">
					<table class="table table-withoutBorder no-head">
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.id" /></b>
							</td>
							<td>
								${UnifiedShipment.name}<form:hidden path="name" />
							</td>
							<td></td>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.status" /></b>
							</td>
							<c:choose>
  								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										<spring:message code="${UnifiedShipment.status}"/> <form:hidden path="status" />
									</td>	    						
  								</c:when> 
  								<c:otherwise>
    								<td class="danger">								
										<form:select id="status" class="form-control" path="status">
											<form:option value="SHPT_FROZEN"><spring:message code="SHPT_FROZEN"/></form:option>
											<form:option value="SHPT_AWAIT_PICK_UP"><spring:message code="SHPT_AWAIT_PICK_UP"/></form:option>
											<form:option value="SHPT_IN_TRANSIT"><spring:message code="SHPT_IN_TRANSIT"/></form:option>
											<form:option value="SHPT_RECEIVING"><spring:message code="SHPT_RECEIVING"/></form:option>
											<form:option value="SHPT_RECEIVED"><spring:message code="SHPT_RECEIVED"/></form:option>
											<form:option value="SHPT_EXCEPTION"><spring:message code="SHPT_EXCEPTION"/></form:option>																								
										</form:select>
									</td>
  								</c:otherwise>
							</c:choose>																					
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.seller" /></b>
							</td>
							<td>
								${UnifiedShipment.sellerCompanyKcode} ${DrsCompanyKcodeToNameMap[UnifiedShipment.sellerCompanyKcode]}
								<form:hidden path="sellerCompanyKcode" />
							</td>
							<td></td>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.buyer" /></b>
							</td>
							<td>
								${UnifiedShipment.buyerCompanyKcode} ${DrsCompanyKcodeToNameMap[UnifiedShipment.buyerCompanyKcode]}
								<form:hidden path="buyerCompanyKcode" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.destination" /></b>
							</td>
							<td>
								<spring:message code="${UnifiedShipment.destinationCountry}" /> 
								<form:hidden path="destinationCountry" />
							</td>
							<td></td>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.actualDestination" /></b>
							</td>
							<td>
								${UnifiedShipment.actualDestination}
								<form:hidden path="destinationCountry" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.shippingMethod" /></b>
							</td>
							<td>
								<spring:message code="${UnifiedShipment.shippingMethod}" /> 
								<form:hidden path="shippingMethod" />
							</td>
							<td></td>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.invoiceNumber" /></b>
							</td>
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										${UnifiedShipment.invoiceNumber}
										<form:hidden path="invoiceNumber" />
									</td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:input id="invoiceNumber" class="form-control" path="invoiceNumber" />
									</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.fbaId" /></b>
							</td>
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										${UnifiedShipment.fbaId} <form:hidden path="fbaId" />
								    </td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:input id="fbaId" class="form-control" path="fbaId" />														
									</td>	
								</c:otherwise>												
							</c:choose>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr>							
							<td class="text-right">
								<b><spring:message code="unifiedShipment.exportDate" /></b>
							</td>							
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										${UnifiedShipment.exportDate} <form:hidden path="exportDate" />
								    </td>
								</c:when>
								<c:otherwise>
									<td class="danger">						
										<form:input id="exportDate" class="form-control" path="exportDate" style="cursor:default;background-color:white;" />							
									</td>	
								</c:otherwise>
							</c:choose>																																			
							<td></td>
							<td class="text-right">
								<b>
									<c:choose>
										<c:when test="${UnifiedShipment.name=='UNS-K2-2' || UnifiedShipment.name=='UNS-K2-3' || UnifiedShipment.name=='UNS-K2-4' || UnifiedShipment.name=='UNS-K2-5' || UnifiedShipment.name=='UNS-K2-6' || UnifiedShipment.name=='UNS-K2-7' || UnifiedShipment.name=='UNS-K2-8'}">
											<spring:message code="${UnifiedShipment.exportDstCurrency}" /><spring:message code='To' /><spring:message code="${UnifiedShipment.exportSrcCurrency}" /> <spring:message code="unifiedShipment.exchangeRate" />
										</c:when>
										<c:otherwise>
											<spring:message code="${UnifiedShipment.exportSrcCurrency}" /><spring:message code='To' /><spring:message code="${UnifiedShipment.exportDstCurrency}" /> <spring:message code="unifiedShipment.exchangeRate" />
										</c:otherwise>
									</c:choose>
								</b>
							</td>
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">	
									<td>
										${UnifiedShipment.exportCurrencyExchangeRate} 
										<form:hidden path="exportCurrencyExchangeRate" />
									</td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:input id="exportCurrencyExchangeRate" class="form-control" path="exportCurrencyExchangeRate" />								
									</td>									
								</c:otherwise>
							</c:choose>																												
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.trackingNumber" /></b>
							</td>
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										${UnifiedShipment.trackingNumber} 
										<form:hidden path="trackingNumber" />
								    </td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:input id="trackingNumber" class="form-control" path="trackingNumber" />														
									</td>	
								</c:otherwise>
							</c:choose>
							<td></td>
							<c:choose>
								<c:when test="${UnifiedShipment.destinationCountry=='UK'}">
									<td class="text-right">
										<b><spring:message code="unifiedShipment.exportFxRateToEur" /></b>
									</td>
									<c:choose>
										<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">	
											<td>
												${UnifiedShipment.exportFxRateToEur} <form:hidden path="exportFxRateToEur" />
											</td>
										</c:when>
										<c:otherwise>
											<td class="danger">
												<form:input id="exportFxRateToEur" class="form-control" path="exportFxRateToEur" />									
											</td>									
										</c:otherwise>
									</c:choose>																										
								</c:when>
								<c:otherwise>
									<td class="text-right">
										<b><spring:message code="unifiedShipment.dateCreated" /></b>
									</td>
									<td>${UnifiedShipment.dateCreated}</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.forwarder" /></b>
							</td>
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										<a href="${UnifiedShipment.forwarder.value}" target="_blank">${UnifiedShipment.forwarder}</a> <form:hidden path="forwarder" />
								    </td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:select id="forwarder" class="form-control" path="forwarder" >
											<form:option value="">--- Select ---</form:option>
											<c:forEach var="forwarder" items="${forwarderList}">
											<form:option value="${forwarder}">${forwarder}</form:option>										
											</c:forEach>
										</form:select>
									</td>	
								</c:otherwise>												
							</c:choose>
							<td></td>							  
							<c:choose>
								<c:when test="${UnifiedShipment.destinationCountry=='UK'}">
									<td class="text-right">
										<b><spring:message code="unifiedShipment.dateCreated" /></b>
									</td>
									<td>${UnifiedShipment.dateCreated}</td>
								</c:when>
								<c:otherwise>
									<td></td>
									<td></td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.expectArrivalDate" /></b>
							</td>							
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										${UnifiedShipment.expectArrivalDate} 
										<form:hidden path="expectArrivalDate" />
								    </td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:input id="expectArrivalDate" class="form-control" path="expectArrivalDate" style="cursor:default;background-color:white;"/>														
									</td>	
								</c:otherwise>
							</c:choose>																																					
							<td></td>
							<td></td>
							<td></td>																					
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.destReceivedDate" /></b>
							</td>							
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">
									<td>
										${UnifiedShipment.destinationReceivedDate} 
										<form:hidden path="destinationReceivedDate" />
								    </td>
								</c:when>
								<c:otherwise>
									<td class="danger">
										<form:input id="destinationReceivedDate" class="form-control" style="cursor:default;background-color:white;" path="destinationReceivedDate" />
									</td>	
								</c:otherwise>												
							</c:choose>																																										
							<td></td>
							<td></td>
							<td></td>
						</tr>																		
					</table>
					<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th><spring:message code="unifiedShipment.isId" /></th>
								<th><spring:message code="unifiedShipment.sku" /></th>
								<th><spring:message code="unifiedShipment.skuName" /></th>
								<th class="text-center"><spring:message code="unifiedShipment.cartonNumber"/></th>
								<th class="text-center"><spring:message code="unifiedShipment.cartonDimensions" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.grossWeightPerCarton" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.unitsPerCarton" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.numberOfCartons" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.quantity" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.unitPrice" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.unitCifAmount" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.DDPamount" /></th>
								<th class="text-right"><spring:message code="unifiedShipment.CIFamount" /></th>
							</tr>
						</thead>
						<c:forEach items="${UnifiedShipment.lineItems}" var="line" varStatus="lineItem">
							<tr>
								<td>									
									<form:hidden path="lineItem[${lineItem.index}].lineSeq" value="${lineItem.count}"/>
									${line.sourceInventoryShipmentName}<form:hidden path="lineItem[${lineItem.index}].sourceInventoryShipmentName" />
								</td>
								<td>
									${line.skuCode}
									<form:hidden path="lineItem[${lineItem.index}].skuCode" /> 
									<form:hidden path="lineItem[${lineItem.index}].boxNum" />
									<form:hidden path="lineItem[${lineItem.index}].mixedBoxLineSeq" />
								</td>
								<td>${line.nameBySupplier}</td>
								<td class="text-center">
									${line.cartonNumberStart} ~ ${line.cartonNumberEnd}
				   					<form:hidden path="lineItem[${lineItem.index}].cartonNumberStart" />
				   					<form:hidden path="lineItem[${lineItem.index}].cartonNumberEnd" />
								</td>
								<td class="text-center">
									${line.cartonDimensionCm1} x ${line.cartonDimensionCm2} x ${line.cartonDimensionCm3}
									<form:hidden path="lineItem[${lineItem.index}].cartonDimensionCm1" />
									<form:hidden path="lineItem[${lineItem.index}].cartonDimensionCm2" />
									<form:hidden path="lineItem[${lineItem.index}].cartonDimensionCm3" />
								</td>
								<td class="text-right">${line.perCartonGrossWeightKg}<form:hidden path="lineItem[${lineItem.index}].perCartonGrossWeightKg" /></td>
								<td class="text-right">${line.perCartonUnits}<form:hidden path="lineItem[${lineItem.index}].perCartonUnits" /></td>
								<td class="text-right">${line.cartonCounts}<form:hidden path="lineItem[${lineItem.index}].cartonCounts" /></td>
								<td class="text-right">${line.quantity}<form:hidden path="lineItem[${lineItem.index}].quantity" /></td>
								<td class="text-right">${line.unitAmount}<form:hidden path="lineItem[${lineItem.index}].unitAmount" /></td>
								<td class="text-right">${line.unitCifAmount}<form:hidden path="lineItem[${lineItem.index}].unitCifAmount" /></td>
								<td class="text-right">${line.subtotal}<form:hidden path="lineItem[${lineItem.index}].subtotal" /></td>
								<td class="text-right">${line.cifSubtotal}<form:hidden path="lineItem[${lineItem.index}].cifSubtotal" /></td>
							</tr>
						</c:forEach>						
						<tr>
							<td colspan="7"></td>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.totalQuantity" /></b>
							</td>
							<td id="totalQuantity" class="text-right"><b></b></td>
							<td></td>
							<td class="text-right">
								<b><spring:message code="unifiedShipment.totalAmount" /></b>
							</td>
							<td class="text-right">
								<b>${UnifiedShipment.amountTotal}</b><form:hidden path="amountTotal" />
							</td>
							<td class="text-right">
								<b>${UnifiedShipment.cifAmountTotal}</b><form:hidden path="cifAmountTotal" />
							</td>
						</tr>
					</table>
					</div>
					<sec:authorize access="hasAnyRole('${auth['UNIFIED_SHIPMENTS_EDIT']}')">
						<div style="float: right">							
							<c:choose>
								<c:when test="${UnifiedShipment.status eq 'SHPT_DRAFT'}">	
									<input class="btn btn-success" type="button"
											value="<spring:message 
											code="unifiedShipment.freeze" />"
											onclick="freezeConfirm('${UnifiedShipment.name}');" />								
									<a class="btn btn-primary" href="${pageContext.request.contextPath}/UnifiedShipments/${UnifiedShipment.name}/edit">
										<spring:message code="unifiedShipment.edit" />
									</a>
									<input class="btn btn-link" type="button" value="<spring:message code="unifiedShipment.delete" />" onclick="deleteConfirm('${UnifiedShipment.name}');" />
									<a class="btn btn-link" href="${pageContext.request.contextPath}/UnifiedShipments">
										<spring:message code="unifiedShipment.back" />
									</a>
									<div id="dialog-confirm"></div>																							
								</c:when>
								<c:otherwise>
									<input class="btn btn-primary" type="submit" value="<spring:message code="unifiedShipment.updateUNS" />" />							
									<a class="btn btn-link" href="${pageContext.request.contextPath}/UnifiedShipments">
										<spring:message code="unifiedShipment.back" />
									</a>
								</c:otherwise>
							</c:choose>														
						</div>
					</sec:authorize>
				</div>
			</div>
		</form:form>
	</div>
</div>