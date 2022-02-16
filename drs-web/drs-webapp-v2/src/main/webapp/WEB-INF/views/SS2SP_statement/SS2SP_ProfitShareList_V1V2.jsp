<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='ss2spStatement.profitShare' /> -
	DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='ss2spStatement.profitShare' />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					<spring:message code='ss2spStatement.period' />:${report.dateStart} ~ ${report.dateEnd}<br>
					<spring:message code='ss2spStatement.issuer' />:${companyKcodeToNameMap[report.isurKcode]}<br>
					<spring:message code='ss2spStatement.receiver' />:${companyKcodeToNameMap[report.rcvrKcode]}
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center"><spring:message code='ss2spStatement.sourceCountry' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.sourceAmount' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.amount' /> [<spring:message code="${report.statementCurrency}" />]</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.lineItems}" var="item">
							<tr>
								<td class="text-center"><spring:message code='${item.sourceCountry}' /></td>
								<td class="text-center"><spring:message code='${item.sourceCurrency}' /></td>
								<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}">${item.sourceAmount}</a></td>
								<td class="text-right">${item.statementAmount}</td>
							</tr>
						</c:forEach>
						<c:if test="${not empty report.amountSubTotal}">
							<tr>
								<td colspan="2"></td>
								<td class="text-right"><strong><spring:message code='ss2spStatement.subtotal' /></strong></td>
								<td class="text-right"><strong>${report.amountSubTotal}</strong></td>
							</tr>
						</c:if>
						<c:if test="${not empty report.amountTax}">
							<tr>
								<td colspan="2"></td>
								<td class="text-right"><strong><spring:message code='ss2spStatement.tax' /></strong></td>
								<td class="text-right"><strong>${report.amountTax}</strong></td>
							</tr>
						</c:if>
						<tr>
							<td colspan="2"></td>
							<td class="text-right">
								<strong>
									<spring:message code='ss2spStatement.totalStatementAmount' />
									<spring:message code='${report.statementCurrency}' />
								</strong>
							</td>
							<td class="text-right"><strong>${report.amountTotal}</strong></td>
						</tr>
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