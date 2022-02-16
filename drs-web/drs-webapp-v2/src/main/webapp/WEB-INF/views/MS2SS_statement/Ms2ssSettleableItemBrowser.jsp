<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>${report.title} - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">${report.title}</div>
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
							<th>Transaction time UTC</th>
							<th>SKU</th>
							<th>Item name</th>
							<th>Source item</th>
							<th class="text-center">Source currency</th>
							<th class="text-right">Source amount</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.itemList}" var="disp">
							<tr>
								<td>${disp.transactionTimeUtc}</td>
								<td>${disp.sku}</td>
								<td>${disp.name}</td>
								<td>${disp.sourceName}</td>
								<td class="text-center">${disp.currency}</td>
								<td class="text-right">${disp.amount}</td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="4"></td>
							<td class="text-right"><b>Total ${report.currency}</b></td>
							<td class="text-right"><b>${report.total}</b></td>
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