<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<title>SKU <spring:message code='ss2spStatement.profitShare' /> (<spring:message code="${country}" />): ${report.sku} - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script>
	$(document).ready(function() {
			
		$('[data-toggle="tooltip"]').tooltip();	
					
	});
</script>
<style>
	.card-subtitle {
		margin: 0;
		padding: 0;
	}
	.border-right {
		border-right: 1px solid #dee2e6;
	}
	.border-left {
		border-left: 1px solid #dee2e6;
	}
	tr.padding-reset td {
		padding-top: 8px !important;
		padding-bottom: 8px !important;
	}
</style>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
    <c:set var="requestURL" value="${requestScope['javax.servlet.forward.servlet_path']}" />               
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                                   
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare?country=${country}#${report.sku}"><spring:message code='ss2spStatement.profitShare' /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>   	      	 
         <span><spring:message code="${country}" /> : ${report.sku}</span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class="card-title">
							SKU <spring:message code='ss2spStatement.profitShare' /> (<spring:message code="${country}" />) : ${report.sku}
						</h4>		
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					
					<p class="text-right statement-info">
						<spring:message code='ss2spStatement.period' />: ${report.dateStart} ~ ${report.dateEnd}<br>
						<spring:message code='ss2spStatement.issuer' />: ${companyKcodeToNameMap[report.isurKcode]}<br>				
						<spring:message code='ss2spStatement.receiver' />: ${companyKcodeToNameMap[report.rcvrKcode]}<br>
						<spring:message code='ss2spStatement.currency' />:<spring:message code='${currency.name()}' />
					</p>
					
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-striped">
					<c:if test="${not empty report.shippedItems}">
						<thead id="tableHeader" class="border-bottom border-top border-right border-left">
							<tr role='row' style="height: 50px; background-color: #f8f8f8;">
								<td colspan="10">
									<div class="card-header">
										<h5 class="card-subtitle"><spring:message code='ss2spStatement.shipped'/></h5>
									</div>
								</td>
							</tr>
							<tr class="border-top border-bottom">
								<th class="text-left" style="padding-left: 20px;"><spring:message code='ss2spStatement.utcTime'/></th>
								<th class="text-center"><spring:message code='ss2spStatement.marketplace'/></th>
								<th class="text-center"><spring:message code='ss2spStatement.orderId'/></th>
								<th class="text-right"><spring:message code='ss2spStatement.additionalItem'/> <spring:message code='ss2spStatement.pretaxPrice'/></th>
								<th class="text-right"><spring:message code='ss2spStatement.subtractionItem'/> <spring:message code='ss2spStatement.fcaPrice'/></th>
								<th class="text-right"><spring:message code='ss2spStatement.subtractionItem'/> <spring:message code='ss2spStatement.marketplaceFee'/></th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code='ss2spStatement.netLogisticsExpenditure_hint'/>"><spring:message code='ss2spStatement.subtractionItem'/> <spring:message code='ss2spStatement.netLogisticsExpenditure'/></th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code='ss2spStatement.noteForDrsRetainment'/>"><spring:message code='ss2spStatement.subtractionItem'/> <spring:message code='ss2spStatement.drsRetainment'/></th>
								<th class="text-right"></th>
								<th class="text-right"><spring:message code='ss2spStatement.profitShare' /></th>
							</tr>
						</thead>
						<tbody class="border-bottom border-right border-left">
							<c:forEach var="item" items="${report.shippedItems}" >
								<tr class="padding-reset">
									<td class="text-center">${item.utcDate}</td>
									<td class="text-center">${item.marketplace.name}</td>
									<td class="text-center">${item.orderId}</td>
									<td class="text-right" style="border-left: 1px solid #DDD !important;">${item.pretaxPrincipalPrice}</td>
									<td class="text-right">${item.fcaInMarketSideCurrency}</td>
									<td class="text-right">${item.marketplaceFee}</td>
									<td class="text-right">${item.fulfillmentFee}</td>
									<td class="text-right">${item.drsRetainment}</td>
									<td class="text-right">=</td>
									<td class="text-right" style="padding-right: 20px;">${item.profitShare}</td>
								</tr>
							</c:forEach>
						</tbody>
					</c:if>
					<c:if test="${not empty report.refundedItems}">
						
						<thead id="tableHeader" class="border-top border-bottom border-right border-left">
							<tr role='row' style="height: 50px; background-color: #f8f8f8;">
								<td colspan="10">
									<div class="card-header">
										<h5 class="card-subtitle"><spring:message code='ss2spStatement.returned'/></h5>
									</div>
								</td>
							</tr>
							<tr class="border-top border-bottom">
								<th class="text-left" style="padding-left: 20px;"><spring:message code='ss2spStatement.utcTime'/></th>
								<th class="text-center"><spring:message code='ss2spStatement.marketplace'/></th>
								<th class="text-center"><spring:message code='ss2spStatement.orderId'/></th>
								<th class="text-right"><spring:message code='ss2spStatement.subtractionItem'/> <spring:message code='ss2spStatement.pretaxPrice'/></th>
								<th class="text-right"><spring:message code='ss2spStatement.additionalItem'/> <spring:message code='ss2spStatement.fcaPrice'/></th>
								<th class="text-right"><spring:message code='ss2spStatement.additionalItem'/> <spring:message code='ss2spStatement.marketplaceFeeRefund'/></th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code='ss2spStatement.reverseLogisticsExpenditure_hint'/>"><spring:message code='ss2spStatement.subtractionItem'/> <spring:message code='ss2spStatement.reverseLogisticsExpenditure'/></th>						
								<th class="text-right"><spring:message code='ss2spStatement.additionalItem'/> <spring:message code='ss2spStatement.drsRetainmentReversal'/></th>
								<th class="text-right"></th>
								<th class="text-right"><spring:message code='ss2spStatement.profitShare' /></th>
							</tr>
						</thead>
						<tbody class="border-bottom border-right border-left">
							<c:forEach var="item" items="${report.refundedItems}" >
							<tr class="padding-reset">
								<td class="text-center">${item.utcDate}</td>
								<td class="text-center">${item.marketplace.name}</td>
								<td class="text-center">${item.orderId}</td>
								<td class="text-right" style="border-left: 1px solid #DDD !important;">${item.pretaxPrincipalPrice}</td>
								<td class="text-right">${item.fcaInMarketSideCurrency}</td>
								<td class="text-right">${item.marketplaceFee}</td>
								<td class="text-right">${item.refundFee}</td>						
								<td class="text-right">${item.drsRetainment}</td>
								<td class="text-right">=</td>
								<td class="text-right" style="padding-right: 20px;">${item.profitShare}</td>
							</tr>
							</c:forEach>
						</tbody>
					</c:if>
					<tr style="background-color: transparent;">
						<td colspan="7"></td>
						<td class="text-right"><b>Total</b></td>
						<td class="text-right">=</td>
						<td class="text-right" style="padding-right: 20px;">${report.total}</td>
					</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>