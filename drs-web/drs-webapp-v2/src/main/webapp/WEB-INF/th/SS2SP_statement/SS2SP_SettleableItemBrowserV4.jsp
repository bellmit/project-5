<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='${report.settleableItemName}' /> -
	DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<!-- [todo] MOVE TO Nav bar in the future -->	
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code='${report.settleableItemName}' />
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					<p class="text-right statement-info">
						<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd} <br>
						<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[report.isurKcode]}<br>
						<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[report.rcvrKcode]}
					</p>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table dataTable table-striped table-floated">
						<thead id="tableHeader" class="border-top border-bottom">
							<tr>
								<th><spring:message code='ss2spStatement.time' /></th>
								<th><spring:message code='ss2spStatement.SKU' /></th>
								<th><spring:message code='ss2spStatement.skuName' /></th>
								<th><spring:message code='ss2spStatement.itemName' /></th>
								<th><spring:message code='ss2spStatement.sourceItem' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
								<th class="text-right"><spring:message code='ss2spStatement.sourceAmount' /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${report.itemList}" var="disp">
								<tr>
									<td>${disp.transactionTimeUtc}</td>
									<td>${disp.sku}</td>
									<td>${disp.skuName}</td>
									<td><spring:message code="${disp.name}" /></td>
									<td>${disp.sourceName}</td>
									<td class="text-center"><spring:message code='${disp.currency}' /></td>
									<td class="text-right">${disp.amount}</td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="5"></td>
								<td class="text-right">
									<b><spring:message code='ss2spStatement.totalAmount' /> 
									<spring:message code='${report.currency}' /></b>
								</td>
								<td class="text-right"><b>${report.total}</b></td>
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
</div>