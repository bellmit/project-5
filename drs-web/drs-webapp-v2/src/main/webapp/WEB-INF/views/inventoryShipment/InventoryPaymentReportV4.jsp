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
<style>
	table thead tr th,
	table tbody tr td {
		text-align: center;
	}

	table thead tr th:first-child,
	table tbody tr td:first-child {
		text-align: left;
		padding-left: 1.5rem;
	}

	table thead tr th:last-child,
	table tbody tr td:last-child {
		text-align: right;
		padding-right: 1.5rem;
	}

	.card {
		padding: 2rem;
	}
</style>
<script type="text/javascript">
	$(function() {
		$('[data-toggle="tooltip"]').tooltip();
	});
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<div class="card-body">
				<!-- <div class="row">
					<div class="col-md-12">
						<div class="page-heading">
							<spring:message code="inventoryPaymentReport.TITLE" />:
							<a href="${pageContext.request.contextPath}/InventoryShipments/${report.shipmentName}">${report.shipmentName}</a>
						</div>
					</div>
				</div> -->

				<h5 class="mb-3 font-weight-bold">
					<spring:message code="inventoryPaymentReport.QUANTITY_SUMMARY" />
				</h5>

				<!-- 數量一覽 -->
				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>SKU</th>
									<th><spring:message code="inventoryPaymentReport.QUANTITY_ORIGINAL_INVOICE" /></th>
									<!-- <th><spring:message code="inventoryPaymentReport.QUANTITY_RETURNED" /></th> -->
									<!-- <th><spring:message code="inventoryPaymentReport.QUANTITY_DRS_ACTUAL_PURCHASE" /></th> -->
									<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_MARKET_SIDE_FULFILLED" />" ><spring:message code="inventoryPaymentReport.QUANTITY_MARKET_SIDE_FULFILLED" /></th>
									<th><spring:message code="inventoryPaymentReport.QUANTITY_SOLD_BACK" /></th>
									<th><spring:message code="inventoryPaymentReport.QUANTITY_OTHER" /></th>
									<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_PAID" />"><spring:message code="inventoryPaymentReport.QUANTITY_PAID" /></th>
									<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_REFUNDED" />"><spring:message code="inventoryPaymentReport.QUANTITY_REFUNDED" /></th>
									<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_NET_PAID" />"><spring:message code="inventoryPaymentReport.QUANTITY_NET_PAID" /></th>
									<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="inventoryPaymentReport.TOOLTIP_TEXT_QUANTITY_OUTSTANDING" />"><spring:message code="inventoryPaymentReport.QUANTITY_OUTSTANDING" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${report.quantitySummaryLineItems}" var="item">
									<tr>
										<td class="text-left">${item.sku}</td>
										<td>${item.quantityOriginalInvoice}</td>
										<!-- <td>${item.quantityReturned}</td> -->
										<!-- <td>${item.quantityActualPurchase}</td> -->
										<td>${item.quantityMarketSideFulfilled}</td>
										<td>${item.quantitySoldBack}</td>
										<td>${item.quantityOther}</td>
										<td>${item.quantityPaid}</td>
										<td>${item.quantityRefunded}</td>
										<td>${item.quantityNetPaid}</td>
										<td>${item.quantityOutstanding}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>

				<hr class="my-4">

				<!-- 收付明細 -->
				<h5 class="mb-3 font-weight-bold">
					<spring:message code="inventoryPaymentReport.AMOUNT_DETAILS" />
				</h5>

				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>SKU</th>
									<th><spring:message code="ss2spStatement.statementId" /></th>
									<th><spring:message code="ss2spStatement.itemName" /></th>
									<th><spring:message code="QUANTITY"/></th>
									<th><spring:message code="inventoryPaymentReport.AMOUNT"/> (<spring:message code="${report.currency}"/>)</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${report.amountDetailLineItems}" var="item">
									<tr>
										<td>${item.sku}</td>
										<td><a href="${pageContext.request.contextPath}/${statementRootUrl}/${item.statementName}">${item.statementName}</a></td>
										<td><spring:message code="${item.itemName}"/></td>
										<td>${item.quantity}</td>
										<td>${item.amount}</td>
									</tr>
								</c:forEach>
								<tr>
									<td class="pt-5" colspan="4"></td>
									<td class="font-weight-bold pt-4 pb-0"><spring:message code="inventoryPaymentReport.TOTAL"/> (<spring:message code="${report.currency}"/>): ${report.amountSubtotal}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
