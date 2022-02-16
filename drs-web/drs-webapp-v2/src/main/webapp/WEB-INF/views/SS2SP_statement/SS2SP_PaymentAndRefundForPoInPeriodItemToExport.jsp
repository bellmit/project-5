<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='ss2spStatement.PaymentAndRefund' /> ${ivsName} <spring:message code='ss2spStatement.reviewExport' /> - DRS</title>
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
		
		var report = $('#paymentAndRefundForPoInPeriodReport').DataTable({			 
		 	searching: false,
		    paging: false,
		    info: false,		    
		    buttons: [{
                 extend: 'csv',
                 text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="ss2spStatement.export" />'
        }]
		 			 			 
	 });
			
	report.buttons().container().appendTo($('#btnExport'));
	$('#btnExport > .dt-buttons > a').addClass('btn btn-primary');
		
	});
</script>
<div class="max-width">
	<div class="container-fluid">
	<div class="row">
		<div class="col-md-12">
			<div class="page-heading">
				<spring:message code='ss2spStatement.PaymentAndRefund' /> ${ivsName} <spring:message code='ss2spStatement.reviewExport' />
			</div>
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
			<table id="paymentAndRefundForPoInPeriodReport" class="table">
				<thead>
					<tr>
						<th><spring:message code="ss2spStatement.statements" /></th>
						<th class="text-center">Period start</th>
						<th class="text-center">Period end</th>
						<th><spring:message code="ss2spStatement.ivs" /></th>
						<th class="text-center"><spring:message code='ss2spStatement.time' /></th>
						<th><spring:message code='ss2spStatement.SKU' /></th>
						<th><spring:message code='ss2spStatement.skuName' /></th>
						<th><spring:message code='ss2spStatement.itemName' /></th>
						<th><spring:message code='ss2spStatement.sourceItem' /></th>
						<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
						<th class="text-right"><spring:message code='ss2spStatement.amountPerUnit' /></th>
						<th class="text-right"><spring:message code='ss2spStatement.quantity' /></th>
						<th class="text-right"><spring:message code='ss2spStatement.sourceAmount' /></th>		
					</tr>
				</thead>				
				<tbody>
					<c:forEach items="${itemList}" var="item">
						<tr>
							<td>${item.statementName}</td>
							<td class="text-center">${item.dateStart}</td>
							<td class="text-center">${item.dateEnd}</td>
							<td>${item.shipmentName}</td>
							<td class="text-center">${item.transactionTimeUtc}</td>
							<td>${item.sku}</td>
							<td>${item.skuName}</td>
							<td><spring:message code="${item.itemType}" /></td>
							<td>${item.sourceName}</td>
							<td class="text-center"><spring:message code="${item.currency}" /></td>
							<td class="text-right">${item.unitAmount}</td>
							<td class="text-right">${item.quantity}</td>
							<td class="text-right">${item.totalAmount}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>	
	</div>
</div>