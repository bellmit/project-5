<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Purchase allowance for ${shipmentName} - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Purchase Allowance for ${shipmentName}</div>
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
							<th>Related SKU</th>
							<th>Item</th>
							<th class="text-right">Amount</th>
							<th class="text-right">Sum for SKU</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.itemsGroupBySku}" var="itemGroupBySku">
							<c:forEach items="${itemGroupBySku.rawItems}" var="rawItem"
								varStatus="status">
								<c:choose>
									<c:when test="${status.count==1}">
										<tr>
											<td rowspan="${itemGroupBySku.rawItems.size()}">${itemGroupBySku.sku}</td>
											<td>${rawItem.itemName}</td>
											<td class="text-right"><a
												href="${pageContext.request.contextPath}/MS2SS-Statements/${statementName}/PurchaseAllowance/${shipmentName}/${rawItem.sku}/items?sid=${rawItem.settleableItemId}">${rawItem.statementAmountStr }</a></td>
											<td class="text-right"
												rowspan="${itemGroupBySku.rawItems.size()}">${itemGroupBySku.getSubtotal()}</td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr>
											<td>${rawItem.itemName}</td>
											<td class="text-right"><a
												href="${pageContext.request.contextPath}/MS2SS-Statements/${statementName}/PurchaseAllowance/${shipmentName}/${rawItem.sku}/items?sid=${rawItem.settleableItemId}">${rawItem.statementAmountStr }</a></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>
						<c:forEach items="${report.otherItemAmounts}" var="itemAmount">
						<tr>
							<td></td>
							<td>${itemAmount.key}</td>
							<td></td>
							<td class="text-right">${itemAmount.value}</td>
						</tr>
						</c:forEach>
						<tr>
							<td colspan="3"></td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<th class="text-right">Total ${currency}</th>
							<th class="text-right">${report.subTotal}</th>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">For your reference only.</div>
		</div>
	</div>
</div>