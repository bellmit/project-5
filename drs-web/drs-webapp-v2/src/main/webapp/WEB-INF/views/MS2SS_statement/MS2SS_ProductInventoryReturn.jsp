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
					Period: ${report.dateStart} ~ ${report.dateEnd} <br> 
					Issued by ${kcodeToNameMap[report.isurKcode]} to ${kcodeToNameMap[report.rcvrKcode]}
				</p>

			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th>Shipment Name</th>
							<th>SKU</th>
							<th class="text-right">Quantity</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.lineItems}" var="item">
							<tr>
								<td>${item.shipmentName}</td>
								<td>${item.productSku}</td>
								<td class="text-right">${item.quantity}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">For your reference only.</div>
		</div>
	</div>
</div>