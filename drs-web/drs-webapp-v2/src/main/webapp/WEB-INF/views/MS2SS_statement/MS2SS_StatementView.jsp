<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Intercompany statement: ${statementName} - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Statement: ${statementName}</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					Period: ${report.dateStart} ~ ${report.dateEnd} <br> Issued by
					${kcodeToNameMap[report.isurKcode]} to
					${kcodeToNameMap[report.rcvrKcode]}
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th>Item type</th>
							<th class="text-right">Amount</th>
							<th>Notes</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.statementItems}" var="item">
							<tr>
								<td>${item.displayName}</td>
								<td class="text-right"><a
									href="${pageContext.request.contextPath}/MS2SS-Statements/${statementName}/${item.urlPath}">${item.amountStr }</a></td>
								<td>${item.note}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${report.statementItemsPaymentOnBehalf}"
							var="item">
							<tr>
								<td>${item.displayName}</td>
								<td class="text-right"><a
									href="${pageContext.request.contextPath}/MS2SS-Statements/${statementName}/${item.urlPath}">${item.amountStr }</a></td>
								<td></td>
							</tr>
						</c:forEach>
						<c:if test="${not empty report.ms2ssStatementItemMsdcPaymentOnBehalf}">
							<tr>
								<td>${report.ms2ssStatementItemMsdcPaymentOnBehalf.name}</td>
								<td class="text-right">
									<a href="${pageContext.request.contextPath}/MS2SS-Statements/${statementName}/msdc-payment-on-behalf">${report.ms2ssStatementItemMsdcPaymentOnBehalf.amount}</a>
								</td>
								<td></td>
							</tr>
						</c:if>
						<c:if test="${not empty report.productInventoryReturnItem}">
							<tr>
								<td>${report.productInventoryReturnItem.displayName}</td>
								<td class="text-right"><a
									href="${pageContext.request.contextPath}/MS2SS-Statements/${statementName}/productinventoryreturn">${report.productInventoryReturnItem.amountStr }</a></td>
								<td></td>
							</tr>
						</c:if>
						<tr>
							<td><strong><em>Total for this period</em></strong></td>
							<td class="text-right"><strong><em>${report.total}</em></strong></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="3"></td>
						</tr>
						<tr>
							<td>Balance from previous period</td>
							<td class="text-right">${report.previousBalance}</td>
							<td></td>
						</tr>
						<tr>
							<td>Remittance received from
								${kcodeToNameMap[report.isurKcode]}</td>
							<td class="text-right">-${report.remittanceIsurToRcvr}</a></td>
							<td></td>
						</tr>
						<tr>
							<td>Remittance made to ${kcodeToNameMap[report.isurKcode]}</td>
							<td class="text-right">${report.remittanceRcvrToIsur}</a></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="3"></td>
						</tr>
						<tr>
							<td style="background-color: #F2F5A9;"><strong>Balance</strong></td>
							<td class="text-right" style="background-color: #F2F5A9"><strong>USD
									${report.balance}</strong></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>