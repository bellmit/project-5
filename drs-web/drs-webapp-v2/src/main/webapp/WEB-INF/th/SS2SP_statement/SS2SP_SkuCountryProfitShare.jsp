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
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
				SKU <spring:message code='ss2spStatement.profitShare' /> (<spring:message code="${country}" />) : ${report.sku}								
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">				
				<span style="float:right"><spring:message code='ss2spStatement.period' />: ${report.dateStart} ~ ${report.dateEnd}</span><br>
				<span style="float:right"><spring:message code='ss2spStatement.issuer' />: ${companyKcodeToNameMap[report.isurKcode]}</span><br>				
				<span style="float:right"><spring:message code='ss2spStatement.receiver' />: ${companyKcodeToNameMap[report.rcvrKcode]}</span><br>
				<span style="float:right"><spring:message code='ss2spStatement.currency' />:<spring:message code='${currency.name()}' /></span>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
				<c:if test="${not empty report.shippedItems}">
					<thead>
					<tr>
						<td colspan="10">
							<div class="page-heading">
								<spring:message code='ss2spStatement.shipped' />
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-center"><spring:message code='ss2spStatement.utcTime'/></th>
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
					<c:forEach var="item" items="${report.shippedItems}" >
					<tr>
						<td class="text-center">${item.utcDate}</td>
						<td class="text-center">${item.marketplace.name}</td>
						<td class="text-center">${item.orderId}</td>
						<td class="text-right" style="border-left: 1px solid #DDD">${item.pretaxPrincipalPrice}</td>
						<td class="text-right">${item.fcaInMarketSideCurrency}</td>
						<td class="text-right">${item.marketplaceFee}</td>
						<td class="text-right">${item.fulfillmentFee}</td>
						<td class="text-right">${item.drsRetainment}</td>
						<td class="text-right">=</td>
						<td class="text-right">${item.profitShare}</td>
					</tr>
					</c:forEach>
				</c:if>
				<c:if test="${not empty report.refundedItems}">
					<thead>
					<tr>
						<td colspan="10">
							<div class="page-heading">
								<spring:message code='ss2spStatement.returned' />
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-center"><spring:message code='ss2spStatement.utcTime'/></th>
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
					<c:forEach var="item" items="${report.refundedItems}" >
					<tr>
						<td class="text-center">${item.utcDate}</td>
						<td class="text-center">${item.marketplace.name}</td>
						<td class="text-center">${item.orderId}</td>
						<td class="text-right" style="border-left: 1px solid #DDD">${item.pretaxPrincipalPrice}</td>
						<td class="text-right">${item.fcaInMarketSideCurrency}</td>
						<td class="text-right">${item.marketplaceFee}</td>
						<td class="text-right">${item.refundFee}</td>						
						<td class="text-right">${item.drsRetainment}</td>
						<td class="text-right">=</td>
						<td class="text-right">${item.profitShare}</td>
					</tr>
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="7"></td>
					<td class="text-right"><b>Total</b></td>
					<td class="text-right">=</td>
					<td class="text-right">${report.total}</td>
				</tr>
				</table>
			</div>
		</div>
	</div>
</div>