<%@ page session="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
<title><spring:message code='ss2spStatement.profitShare' /> (<spring:message code="${country}" />) <spring:message code='ss2spStatement.reviewExport' /> - DRS</title>
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
	 		
	 		var report = $('#skuCountryProfitShare').DataTable({			 
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
<div class="max-width" style="max-width:1640px !important;padding-left:20px;padding-right:20px;">
	<div class="max-width">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code='ss2spStatement.profitShare' /> (<spring:message code="${country}" />) <spring:message code='ss2spStatement.reviewExport' />
					</div>
				</div>
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
				<table id ="skuCountryProfitShare" class="table">
					<thead>
						<tr>
							<th><spring:message code="ss2spStatement.statements" /></th>
							<th>Period start</th>
							<th>Period end</th>
							<th class="text-center"><spring:message code='ss2spStatement.sourceCountry' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.exchangeRate' /></th>
							<th><spring:message code='ss2spStatement.relatedSKU' /></th>
							<th><spring:message code='ss2spStatement.skuName' /></th>
							<th><spring:message code='ss2spStatement.items' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.utcTime'/></th>
							<th class="text-center"c><spring:message code='ss2spStatement.marketplace'/></th>
							<th><spring:message code='ss2spStatement.orderId'/></th>
							<th class="text-right"><spring:message code='ss2spStatement.quantity' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.pretaxPrice'/></th>
							<th class="text-right"><spring:message code='ss2spStatement.fcaPrice'/></th>
							<th class="text-right"><spring:message code='ss2spStatement.marketplaceFee'/> / <spring:message code='ss2spStatement.marketplaceFeeRefund'/></th>
							<th class="text-right"><spring:message code='ss2spStatement.netLogisticsExpenditure'/> / <spring:message code='ss2spStatement.reverseLogisticsExpenditure'/></th>
							<th class="text-right"><spring:message code='ss2spStatement.drsRetainment'/> / <spring:message code='ss2spStatement.drsRetainmentReversal'/></th>
							<th class="text-right"><spring:message code='ss2spStatement.profitShare' /></th>					
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${itemList}" var="item">
							<tr>
								<td>${item.statementName}</td>
								<td>${item.dateStart}</td>
								<td>${item.dateEnd}</td>
								<td class="text-center"><spring:message code='${item.country}' /></td>
								<td class="text-center"><spring:message code='${item.sourceCurrency}' /></td>
								<td class="text-right">${item.exchangeRate}</td>
								<td>${item.sku}</td>
								<td>${item.skuName}</td>
								<td><spring:message code='${item.name}' /></td>
								<td class="text-center">${item.utcDate}</td>
								<td class="text-center">${item.marketplace.name}</td>
								<td>${item.orderId}</td>
								<td class="text-right">${item.quantity}</td>
								<td class="text-right">${item.pretaxPrincipalPrice}</td>
								<td class="text-right">${item.fcaInMarketSideCurrency}</td>
								<td class="text-right">${item.marketplaceFee}</td>
								<td class="text-right">${item.fulfillmentFee}</td>
								<td class="text-right">${item.drsRetainment}</td>
								<td class="text-right">${item.profitShare}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
</div>	