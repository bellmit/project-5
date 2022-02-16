<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="header.unifiedShipments" /> - DRS
</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		$('#UnifiedShipments').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});
		$('#UnifiedShipments').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});
	});
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code="header.unifiedShipments" />
				</div>
			</div>
			<div class="col-md-6 text-right">
				<div class="page-heading">
				<sec:authorize access="hasAnyRole('${auth['UNIFIED_SHIPMENTS_CREATE']}')">
					<a class="btn btn-primary" href="${pageContext.request.contextPath}/UnifiedShipments/create">
						<i class="fas fa-plus"></i> <spring:message code="unifiedShipment.create" />
					</a>
				</sec:authorize>	
				</div>
			</div>			
		</div>		
		<div class="row">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/UnifiedShipments?page=1" />
						<c:url var="lastUrl"  value="/UnifiedShipments?page=${totalPages}" />
						<c:url var="prevUrl"  value="/UnifiedShipments?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl"  value="/UnifiedShipments?page=${currentPageIndex + 1}" />
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/UnifiedShipments?page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class=" page-item active"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
				<div style="padding-bottom: 10px"></div>
				<div class="table-responsive">
				<table id="UnifiedShipments" class="table table-floated">
					<thead>
						<tr>
							<th><spring:message code="unifiedShipment.id" /></th>
							<th><spring:message code="unifiedShipment.invoiceNumber" /></th>
							<th><spring:message code="unifiedShipment.seller" /></th>
							<th><spring:message code="unifiedShipment.buyer" /></th>
							<th class="text-right" ><spring:message code="unifiedShipment.totalAmount" /></th>
							<th class="text-center"><spring:message code="unifiedShipment.destination" /></th>
							<th class="text-center"><spring:message code="unifiedShipment.shippingMethod" /></th>
							<th class="text-center"><spring:message code="unifiedShipment.exportDate" /></th>
							<th class="text-center"><spring:message code="unifiedShipment.expectArrivalDate" /></th>
							<th class="text-center"><spring:message code="unifiedShipment.status" /></th>
							<th><spring:message code="unifiedShipment.trackingNumber" /></th>
							<th><spring:message code="unifiedShipment.fbaId" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${UnifiedShipmentList}" var="unifiedShipment">
							<tr>
								<td>
									<a href="${pageContext.request.contextPath}/UnifiedShipments/${unifiedShipment.name}/view">${unifiedShipment.name}</a>									
								</td>
								<td>${unifiedShipment.invoiceNumber}</td>
								<td>${unifiedShipment.sellerCompanyKcode} ${DrsCompanyKcodeToNameMap[unifiedShipment.sellerCompanyKcode]}</td>
								<td>${unifiedShipment.buyerCompanyKcode} ${DrsCompanyKcodeToNameMap[unifiedShipment.buyerCompanyKcode]}</td>
								<td class="text-right">${unifiedShipment.amountTotal}</td>
								<td class="text-center"><spring:message code="${unifiedShipment.destinationCountry}" /></td>
								<td class="text-center"><spring:message code="${unifiedShipment.shippingMethod}" /></td>
								<td class="text-center">${unifiedShipment.exportDate}</td>
								<td class="text-center">${unifiedShipment.expectArrivalDate}</td>
								<td class="text-center"><spring:message code="${unifiedShipment.status}" /></td>
								<td>						
								<c:choose>
									<c:when test="${unifiedShipment.forwarder.value == null}">
								    	${unifiedShipment.trackingNumber}									
								    </c:when>
									<c:otherwise>
										<a href="${unifiedShipment.forwarder.value}" target="_blank">${unifiedShipment.trackingNumber}</a>																	
									</c:otherwise>
								</c:choose>																								
								</td>
								<td>${unifiedShipment.fbaId}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/UnifiedShipments?page=1" />
						<c:url var="lastUrl" value="/UnifiedShipments?page=${totalPages}" />
						<c:url var="prevUrl" value="/UnifiedShipments?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/UnifiedShipments?page=${currentPageIndex + 1}" />
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/UnifiedShipments?page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class=" page-item active"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</div>