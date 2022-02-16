<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
<title><spring:message code="ss2spStatement.otherRefundAndAllowance" /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
		<div class="breadcrumb">
			<a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
			<a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
			<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
			<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare"><spring:message code='ss2spStatement.profitShare' /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
			<span><spring:message code="ss2spStatement.profitShareSubtraction" />: <spring:message code="ss2spStatement.otherRefundAndAllowance" />@${country}</span>  
		</div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code="ss2spStatement.profitShareSubtraction" />: <spring:message code="ss2spStatement.otherRefundAndAllowanceFromMarketSideToCustomer" />@${country}
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					<p class="text-right statement-info">
						<spring:message code='ss2spStatement.period' />: ${otherRefundAndAllowance.dateStart} ~ ${otherRefundAndAllowance.dateEnd}<br>
						<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[otherRefundAndAllowance.isurKcode]}<br>
						<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[otherRefundAndAllowance.rcvrKcode]}
					</p>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table dataTable table-striped table-floated">
						<thead id="tableHeader" class="border-top border-bottom">
							<tr>
								<th class="text-left"><spring:message code='ss2spStatement.itemName' /></th>
								<th class="text-left"><spring:message code='ss2spStatement.notes' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>		
								<th class="text-right"><spring:message code='ss2spStatement.amount' /></th>									
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${otherRefundAndAllowance.lineItems}" var="lineItem">
								<tr>
									<td class="text-left pl-20"><spring:message code="ss2spStatement.otherRefundAndAllowanceFromMarketSideToCustomer" /></td>
									<td class="text-left pl-20">${lineItem.itemNote}</td>
									<td class="text-center">${lineItem.currency}</td>
									<td class="text-right pr-18"><fmt:formatNumber type="number" maxFractionDigits="2" value="${lineItem.amount}"/></td>
								</tr>
							</c:forEach>
							<!-- <tr style="background-color: transparent !important; border-top: 1px solid #dee2e6;">
								<td colspan="3"></td>
								<td class="text-center pt-15"><b><spring:message code='ss2spStatement.totalAmount' /></b></td>
								<td class="text-right pr-18 pt-15"><b><fmt:formatNumber type="number" maxFractionDigits="2" value="${otherRefundAndAllowance.total}"/></b></td>
							</tr> -->
						</tbody>
					</table>
				</div>
			</div>
			<div class="row" style="padding-top: 15px;"></div>
			<div class="row">
				<div class="col-md-8"></div>
				<div class="col-md-2 text-right" style="padding: 5px .86% 0 0; font-size: 14px;">
					<strong><spring:message code='ss2spStatement.totalAmount' /></strong></div>
				<div class="col-md-2 text-right" style="padding: 5px 32px 0 0; font-size: 14px;">
				 	<strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${otherRefundAndAllowance.total}"/></strong>
				</div>					
			</div>
		</div>
	</div>
</div>
