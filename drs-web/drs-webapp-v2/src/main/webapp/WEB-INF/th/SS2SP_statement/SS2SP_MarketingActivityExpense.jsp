<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
<title><spring:message code="ss2spStatement.marketingActivityExpense" /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
   		 <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare"><spring:message code='ss2spStatement.profitShare' /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <span><spring:message code="ss2spStatement.profitShareSubtraction" />: <spring:message code="ss2spStatement.marketingActivityExpense" />@${country}</span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="ss2spStatement.profitShareSubtraction" />: <spring:message code="ss2spStatement.marketingActivityExpense" />@${country}
				</div>
			</div>
		</div>		
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					<spring:message code='ss2spStatement.period' />
					${marketingActivityExpense.dateStart} ~ ${marketingActivityExpense.dateEnd}<br>
					<spring:message code='ss2spStatement.issuer' />: ${companyKcodeToNameMap[marketingActivityExpense.isurKcode]}<br>
					<spring:message code='ss2spStatement.receiver' />: ${companyKcodeToNameMap[marketingActivityExpense.rcvrKcode]}
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center"><spring:message
									code='ss2spStatement.itemName' /></th>
							<th class="text-center"><spring:message
									code='ss2spStatement.notes' /></th>
							<th class="text-center"><spring:message
									code='ss2spStatement.sourceCurrency' /></th>		
							<th class="text-right"><spring:message
									code='ss2spStatement.amount' /></th>									
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${marketingActivityExpense.lineItems}" var="lineItem">
							<tr>
								<td class="text-center"><spring:message code="ss2spStatement.profitShareSubtraction" />: <spring:message code="ss2spStatement.marketingActivityExpense" /></td>
								<td class="text-center">${lineItem.itemNote}</td>
								<td class="text-center">${lineItem.currency}</td>
								<td class="text-right"><fmt:formatNumber type="number" maxFractionDigits="2" value="${lineItem.amount}"/></td>
							</tr>		
						</c:forEach>					
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-7"></div>
			<div class="col-md-3 text-right">
				<strong><spring:message code='ss2spStatement.totalAmount' /></strong></div>
			<div class="col-md-2 text-right" style="padding-right:20px">
			 <strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${marketingActivityExpense.total}"/></strong>
			</div>					
		</div>
	</div>
</div>
