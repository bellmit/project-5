<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='${report.title}' /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <span><spring:message code='${report.title}' /></span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='${report.title}' />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd}<br>
					<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[report.isurKcode]}<br>
					<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[report.rcvrKcode]}
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th><spring:message code='ss2spStatement.ivsName' /></th>
							<th>SKU</th>
							<th><spring:message code='ss2spStatement.skuName' /></th>
							<th><spring:message code='ss2spStatement.itemType' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.amountPerUnit' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.quantity' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.amount' /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.lineItems}" var="item">
							<tr>
								<td>${item.ivsName}</td>
								<td>${item.sku}</td>
								<td>${item.skuName}</td>
								<td><spring:message code='${item.itemType}' /></td>
								<td class="text-right">${item.unitAmount}</td>
								<td class="text-right">${item.quantity}</td>
								<td class="text-right">${item.subtotal}</td>
							</tr>
						</c:forEach>
                        <c:if test="${not report.includedTax}">
	                     <tr>
							<td colspan="5"></td>
							<td class="text-right"><strong><spring:message code='ss2spStatement.totalAmount' /> <spring:message code='${report.currency.name()}' /></strong></td>
							<td class="text-right"><strong>${report.total}</strong></td>
						</tr>
						</c:if>

                        <c:if test="${report.includedTax}">
                        <tr>
							<td colspan="5"></td>
							<td class="text-right"><strong><spring:message code='ss2spStatement.subtotal' />
							<td class="text-right"><strong>${report.total}</strong></td>
						</tr>

	                     <tr>
							<td colspan="5"></td>
							<td class="text-right"><strong><spring:message code='ss2spStatement.tax' />
							<td class="text-right"><strong>${report.totalTaxAmount}</strong></td>
						</tr>
                        <tr>
							<td colspan="5"></td>
							<td class="text-right"><strong><spring:message code='ss2spStatement.totalAmount' />
							<spring:message code='${report.currency.name()}' /></strong></td>
							<td class="text-right"><strong>${report.totalWithTax}</strong></td>
						</tr>
						</c:if>

					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<spring:message code="ss2spStatement.reference" />
			</div>
		</div>
	</div>
</div>