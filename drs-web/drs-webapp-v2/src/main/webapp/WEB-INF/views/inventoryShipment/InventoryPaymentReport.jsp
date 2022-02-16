<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@	taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
	<title>Payment Record For ${report.shipmentName} - DRS
</title>
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script type="text/javascript">
	$(function() {
		$('[data-toggle="tooltip"]').tooltip();	
	});
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="inventoryPaymentReport.TITLE" />: 
					<a href="${pageContext.request.contextPath}/InventoryShipments/${report.shipmentName}">${report.shipmentName}</a>
				</div>
			</div>
		</div>
		<div style="font-size:16px;color:#333333;">
			<spring:message code="inventoryPaymentReport.QUANTITY_SUMMARY" />
		</div>
		<div class="row rowframe">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center">SKU</th>
							<th class="text-right"><spring:message code="inventoryPaymentReport.QUANTITY_ORIGINAL_INVOICE" /></th>
							<!--<th class="text-right"><spring:message code="inventoryPaymentReport.QUANTITY_RETURNED" /></th>-->
							<!--<th class="text-right"><spring:message code="inventoryPaymentReport.QUANTITY_DRS_ACTUAL_PURCHASE" /></th>-->
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_MARKET_SIDE_FULFILLED" />" ><spring:message code="inventoryPaymentReport.QUANTITY_MARKET_SIDE_FULFILLED" /></th>
							<th class="text-right"><spring:message code="inventoryPaymentReport.QUANTITY_SOLD_BACK" /></th>
							<th class="text-right"><spring:message code="inventoryPaymentReport.QUANTITY_SOLD_BACK_RECOVERY" /></th>
							<th class="text-right"><spring:message code="inventoryPaymentReport.QUANTITY_OTHER" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_PAID" />"><spring:message code="inventoryPaymentReport.QUANTITY_PAID" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_REFUNDED" />"><spring:message code="inventoryPaymentReport.QUANTITY_REFUNDED" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_NET_PAID" />"><spring:message code="inventoryPaymentReport.QUANTITY_NET_PAID" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_OUTSTANDING" />"><spring:message code="inventoryPaymentReport.QUANTITY_OUTSTANDING" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.quantitySummaryLineItems}" var="item">
							<tr>
								<td class="text-left">${item.sku}</td>
								<td class="text-right">${item.quantityOriginalInvoice}</td>
								<!--<td class="text-right">${item.quantityReturned}</td>-->
								<!--<td class="text-right">${item.quantityActualPurchase}</td>-->
								<td class="text-right">${item.quantityMarketSideFulfilled}</td>
								<td class="text-right">${item.quantitySoldBack}</td>
								<td class="text-right">${item.quantitySoldBackRecovery}</td>
								<td class="text-right">${item.quantityOther}</td>
								<td class="text-right">${item.quantityPaid}</td>
								<td class="text-right">${item.quantityRefunded}</td>
								<td class="text-right">${item.quantityNetPaid}</td>
								<td class="text-right">${item.quantityOutstanding}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div style="padding-bottom: 20px"></div>	
		<div style="font-size:16px;color:#333333;">
			<spring:message code="inventoryPaymentReport.AMOUNT_DETAILS" />
		</div>
		<div class="row rowframe">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center">SKU</th>
							<th class="text-center"><spring:message code="ss2spStatement.statementId" /></th>
							<th class="text-center"><spring:message code="ss2spStatement.itemName" /></th>
							<th class="text-right"><spring:message code="QUANTITY"/></th>
							<th class="text-right"><spring:message code="inventoryPaymentReport.AMOUNT"/> (<spring:message code="${report.currency}"/>)</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.amountDetailLineItems}" var="item">
							<tr>
								<td class="text-center">${item.sku}</td>
								<td class="text-center"><a href="${pageContext.request.contextPath}/${statementRootUrl}/${item.statementName}">${item.statementName}</a></td>
								<td class="text-center"><spring:message code="${item.itemName}"/></td>
								<td class="text-right">${item.quantity}</td>
								<td class="text-right">${item.amount}</td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="4"></td>
							<td class="text-right"><b><spring:message code="inventoryPaymentReport.TOTAL"/> (<spring:message code="${report.currency}"/>): ${report.amountSubtotal}</b></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>