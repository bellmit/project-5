<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<spring:message code="inventoryHealthReport.title" /> - DRS
</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>	
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script>
$(document).ready(function() {
	document.addEventListener("scroll", (e) => {
		const $table = $("#inventoryHealthReport");
		const os = window.navigator.userAgent;
		if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
			if ($table.offset().top >= 0) {
				$table.floatThead();
			} else {
				$table.floatThead("destroy");
			}
		} else {
			if ($table.offset().top <= 0) {
				$table.floatThead();
			} else {
				$table.floatThead("destroy");
			}
		}
	}, true);
	$('#supplierSelector').select2({
	    theme: "bootstrap"
	});
});
</script>

</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class="card-title"><spring:message code="inventoryHealthReport.title" /></h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="card-body">
						<div class="mb-1">
							<label class="control-label enhancement">
								<spring:message code="inventoryHealthReport.note" /><br>
								<spring:message code="inventoryHealthReport.noteUS" /><br>
								<spring:message code="inventoryHealthReport.noteUKCA" /><br>
								<c:choose>
									<c:when test="${currency eq 'CAD'}">
										<spring:message code="inventoryHealthReport.moreInfoCA" /><br>
									</c:when>
									<c:when test="${currency eq 'GBP'}">
										<spring:message code="inventoryHealthReport.moreInfoUK" /><br>
									</c:when>
									<c:otherwise>
										<spring:message code="inventoryHealthReport.moreInfoUS" /><br>
									</c:otherwise>
								</c:choose>
								<sec:authorize access="hasAnyRole('${auth['EMAIL_REMINDER']}')">
									<a href="${pageContext.request.contextPath}/emailReminder"><spring:message code='inventoryHealthReport.emailReminder' /></a><br>
								</sec:authorize>
							</label>
						</div>
						<form method="POST" class="form-inline" action="${pageContext.request.contextPath}/inventory-health-report">
							<sec:authorize access="hasAnyRole('${auth['SUPPLIER_FILTER']}')">
							<div class="form-inline-wrapper">
								<label class="mt-2 mt-md-0 mr-md-2" for="supplierSelector"><spring:message code="campaignPerformance.supplier" /></label>
								<select id="supplierSelector" class="form-control" name="supplierKcode">
									<option value="">All</option>
									<c:forEach var="supplierKcodeName" items="${supplierKcodeNames}" >
										<option value="${supplierKcodeName.key}" ${supplierKcodeName.key==report.supplierKcode?'selected="selected"':''}>
											${supplierKcodeName.key} ${supplierKcodeName.value}
										</option>
									</c:forEach>
								</select>
							</div>
							</sec:authorize>
							<div class="form-inline-wrapper">
								<label class="px-md-2 pt-2 pt-md-0" for="marketplaceId"><spring:message code="common.marketplace" /></label>
								<select id="marketplaceSelector" class="form-control" name="marketplaceId">
									<c:forEach items="${marketplaces}" var="marketplace">
										<option value="${marketplace.key}" ${marketplace.key==report.marketplace.key?'selected="selected"':''} >${marketplace.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-inline-wrapper">
								<button id="searchBtn" class="btn-align btn btn-primary mx-md-2 mt-2 mt-md-0 searchBtn" type="submit">
									<i class="fas fa-search"></i> <spring:message code="campaignPerformance.search"/>
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 text-right">
					<spring:message code="inventoryHealthReport.snapshotDate" /> ${report.snapshotDate} <spring:message code="inventoryHealthReport.weeklyUpdate" />		
				</div>
			</div>		
			<div style="padding-bottom: 10px"></div>
			<div class="row">
				<div class="col-md-12">
				<div class="table-responsive" style="overflow: hidden;">
					<table id="inventoryHealthReport" class="table table-floated table-striped">
						<thead id="tableHeader" class="border-top border-bottom">
							<tr style="height: 50px;">
								<th><spring:message code="inventoryHealthReport.SKU" /></th>
								<th><spring:message code="inventoryHealthReport.productName" /></th>
								<th><spring:message code="inventoryHealthReport.condition" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.totalUnsellableQty" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.totalSellableQty" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.estLongTermStorageUnits6+" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.estLongTermStorageUnits12+" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.estLongTermStorageFee6+" arguments="${currency}" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.estLongTermStorageFee12+" arguments="${currency}" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.unitsShippedLast30days" /></th>
								<th class="text-right"><spring:message code="inventoryHealthReport.weeksOfCoverTrailing30days" /></th>
							</tr>
						</thead>
						<tbody class="border-bottom">
						<c:forEach var="item" items="${report.lineItems}">
							<tr>
								<td>${item.sku}</td>
								<td>${item.productName}</td>
								<td>${item.condition}</td>
								<td class="text-right">${item.unsellableQuantity}</td>
								<td class="text-right">${item.sellableQuantity}</td>
								<td class="text-right">${item.qtyToBeChargedLtsf6Mo}</td>
								<td class="text-right">${item.qtyToBeChargedLtsf12Mo}</td>
								<td class="text-right">${item.projectedLtsf6Mo}</td>
								<td class="text-right">${item.projectedLtsf12Mo}</td>
								<td class="text-right">${item.unitsShippedLast30Days}</td>
								<td class="text-right">${item.weeksOfCoverT30}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					</div>
				</div>
			</div>
		</div>

	</div>
</div>