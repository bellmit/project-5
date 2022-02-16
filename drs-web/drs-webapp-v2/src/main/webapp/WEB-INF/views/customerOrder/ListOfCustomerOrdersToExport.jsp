<%@ page session="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
<title><spring:message code="customerOrder.title" /> - DRS</title>
</head>

<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<link href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css"/>"type="text/css" rel="stylesheet">		
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">	
<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">	
<script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script>

<script>
$(document).ready(function() {	 		
	var report = $('#customerOrder').DataTable({			 
		searching: false,
	 	paging: false,
	 	info: false,	    
	 	buttons: [{
	    	extend: 'csv',
	        text: '<i id="exportButton" class="fas fa-cloud-download-alt"></i> Export'
	    }]	 		 			 			 
	});	 			
	report.buttons().container().appendTo($('#btnExport'));
	$('#btnExport > .dt-buttons > a').addClass('btn btn-primary');	 		
});

jQuery(window).on("load", function(e) {	 	
	setTimeout(function () {
		$("#exportButton").click();		
    }, 1000);
		
});
</script>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="customerOrder.searchCustomerOrders" /></div>
			</div>
		</div>
		<div class="row">
 			<div class="col-md-12 text-right">
 				<span id="btnExport"></span>
 			</div>
 		</div>
 		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
				<table id="customerOrder" class="table table-striped">
					<thead>
						<tr>
							<th><spring:message code="customerOrder.orderTimeLocal" /></th>
							<th><spring:message code="customerOrder.orderTimeUTC" /></th>
							<th><spring:message code="customerOrder.transactionTimeUTC" /></th>
							<th><spring:message code="customerOrder.marketplaceOrderId" /></th>
							<th><spring:message code="customerOrder.buyer" /></th>
							<th><spring:message code="customerOrder.SKUCode" /></th>
							<th><spring:message code="customerOrder.productName" /></th>
							<th><spring:message code="customerOrder.orderStatus" /></th>
							<th><spring:message code="common.marketplace" /></th>
							<th><spring:message code="customerOrder.promotionId" /></th>
							<th><spring:message code="customerOrder.fulfillmentCenter" /></th>
							<th><spring:message code="customerOrder.qty" /></th>
							<th><spring:message code="customerOrder.actualRetailPrice" /></th>
							<th><spring:message code="customerOrder.city" /></th>
							<th><spring:message code="customerOrder.state" /></th>
							<th><spring:message code="customerOrder.country" /></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${customerOrders}" var="customerOrder" varStatus="loop">	
						<tr>
							<td>${customerOrder.orderTimeLocal}</td>
							<td>${customerOrder.orderTimeUTC}</td>
							<td>${customerOrder.transactionTimeUTC}</td>
							<td>${customerOrder.marketplaceOrderId}</td>
							<td>${customerOrder.buyer}</td>
							<td>${customerOrder.SKUCode}</td>
							<td>${customerOrder.productName}</td>
							<td>${customerOrder.orderStatus}</td>
							<td>${customerOrder.salesChannel}</td>
							<td>${customerOrder.promotionId}</td>
							<td>${customerOrder.fulfillmentCenter}</td>
							<td>${customerOrder.qty}</td>
							<td>${customerOrder.actualRetailPrice}</td>
							<td>${customerOrder.city}</td>
							<td>${customerOrder.state}</td>
							<td>${customerOrder.country}</td>
						</tr>
						
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>