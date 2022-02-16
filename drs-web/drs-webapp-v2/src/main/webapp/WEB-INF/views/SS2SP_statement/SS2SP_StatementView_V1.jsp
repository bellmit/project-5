<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='ss2spStatement.statement' />:
	${statementName} - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='ss2spStatement.statement' />
					: ${statementName}
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd} <br>
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
							<th><spring:message code='ss2spStatement.itemType' /></th>
							<th class="text-right" style="width: 15%;"><spring:message
									code='ss2spStatement.statementAmountT' /></th>
							<th><spring:message code='ss2spStatement.notes' /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.profitShareItems}" var="item">
							<tr>
								<td><spring:message code='${item.displayName}' /></td>
								<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare">${item.amountStr}</a></td>
								<td>請開立並寄送三聯式統一發票予本公司。品名：利潤分享，金額：${item.amountUntaxStr}，營業稅：${item.amountTaxStr}，總計：${item.amountStr}。公司名稱：善恩創新股份有限公司，統一編號：80698253，公司地址：11148
									台北市士林區忠誠路一段179號3樓。感謝您！</td>
							</tr>
						</c:forEach>
						<c:forEach items="${report.shipmentRelatedItems}" var="item">
							<tr>
								<td><spring:message code='${item.itemName}' /> ${item.sourceShipmentName}</td>
								<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/shipment/${item.sourceShipmentName}">${item.amountStr}</a></td>
								<td><spring:message code='${item.noteText}' />${item.sourceShipmentInvoiceNumber}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${report.statementItemsServiceExpense}"
							var="item">
						    <c:choose>
						    <c:when test="${empty fn:trim(item.invoiceNumber)}">
                                <tr>
                                    <td>-<spring:message code='${item.displayName}' /></td>
                                    <td class="text-right">${item.amountStr}</td>
                                    <td>尚未開立</td>
                                </tr>
						    </c:when>
						    <c:otherwise>
                                <tr>
                                    <td>-<spring:message code='${item.displayName}' /></td>
                                    <td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/ServiceExpense/${item.invoiceNumber}">${item.amountStr}</a></td>
                                    <td>${item.invoiceNumber}</td>
                                </tr>
							</c:otherwise>
						    </c:choose>
						</c:forEach>
						<tr>
							<td><strong><em><spring:message code='ss2spStatement.totalForThisPeriod' /></em></strong></td>
							<td class="text-right"><strong><em>${report.total}</em></strong></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="3"></td>
						</tr>
						<tr>
							<td><spring:message code='ss2spStatement.balanceFromPreviousPeriod' /></td>
							<td class="text-right">${report.previousBalance}</td>
							<td></td>
						</tr>
						<tr>
							<td>-<spring:message code='ss2spRemittanceFrom' /></td>
							<c:choose>
								<c:when test="${report.remittanceIsurToRcvr == '0'}">
									<td class="text-right">-${report.remittanceIsurToRcvr}</td>
								</c:when>
								<c:otherwise>
									<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/RemittanceReceived">-${report.remittanceIsurToRcvr}</a></td>
								</c:otherwise>
							</c:choose>
							<td></td>
						</tr>
						<tr>
							<td><spring:message code='ss2spRemittanceTo' /></td>
							<c:choose>
								<c:when test="${report.remittanceRcvrToIsur == '0'}">
									<td class="text-right">${report.remittanceRcvrToIsur}</td>
								</c:when>
								<c:otherwise>
									<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/RemittanceSent">${report.remittanceRcvrToIsur}</a></td>
								</c:otherwise>
							</c:choose>
							<td></td>
						</tr>
						<tr>
							<td colspan="3"></td>
						</tr>
						<tr>
							<td style="background-color: #F2F5A9;"><strong><spring:message code='ss2spStatement.balance' /></strong></td>
							<td class="text-right" style="background-color: #F2F5A9"><strong><spring:message code='${report.currency}' /> ${report.balance} </strong></td>
							<td>${balanceNote}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>