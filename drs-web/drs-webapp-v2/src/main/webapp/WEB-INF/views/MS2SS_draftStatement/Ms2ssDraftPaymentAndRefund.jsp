<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
	<title>Payment and refund for ${sourcePoId} - DRS</title>
</head>
<div class="container">
	<h2>
		Payment and refund for ${sourcePoId}
	</h2>
	<p style="text-align: right">
		Period: ${report.dateStart} ~ ${report.dateEnd} <br>
		Issued by ${kcodeToNameMap[report.isurKcode]} to ${kcodeToNameMap[report.rcvrKcode]}
	</p>	
	<table class="table">
		<thead>
			<tr>
				<th>SKU</th>
				<th>Item type</th>
				<th class="text-right">Amount per unit</th>
				<th class="text-right">Quantity</th>
				<th class="text-right">Amount</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${report.lineItems}" var="item">
				<tr>
					<td>${item.sku}</td>
					<td>${item.itemType}</td>
					<td class="text-right">${item.unitAmount}</td>
					<td class="text-right">${item.quantity}</td>
					<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementId}/PaymentRefund/${sourcePoId}/${item.sku}/items?sid=${item.settleableItemId}">${item.totalAmount}</a></td>
				</tr>
			</c:forEach>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td class="text-right"><strong>Total ${currency}</strong></td>
				<td class="text-right"><strong>${report.total}</strong></td>
			</tr>
		</tbody>
	</table>
	<p>For your reference only.</p>
</div>