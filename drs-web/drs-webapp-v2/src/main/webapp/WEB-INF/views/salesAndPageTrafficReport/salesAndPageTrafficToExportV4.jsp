<%@ page session="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
<title><spring:message code="salesAndPageTrafficReport.title" /> - DRS</title>
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
	var report = $('#salesAndPageTrafficHistory').DataTable({			 
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
	if ("${totalOrAverage}" === "2") {
		document.title = '${kcode} ${timescale} Daily Averages ${startDate} to ${endDate}';
	} else {
		document.title = '${kcode} ${timescale} Totals ${startDate} to ${endDate}';
	}
			
});

jQuery(window).on("load", function(e) {	 		
	$("#exportButton").click();		
});
</script>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="salesAndPageTrafficReport.title" /></div>
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
				<table id="salesAndPageTrafficHistory" class="table table-striped">
					<thead>
						<tr>
							<th><spring:message code="salesAndPageTrafficReport.date" /></th>
							<th class="text-right"><spring:message code="salesAndPageTrafficReport.Sessions" /></th>
							<th class="text-right"><spring:message code="salesAndPageTrafficReport.pageView" /></th>
							<th class="text-right"><spring:message code="salesAndPageTrafficReport.buybox" /></th>
							<th class="text-right"><spring:message code="salesAndPageTrafficReport.unitSession" /></th>
							<th class="text-right"><spring:message code="salesAndPageTrafficReport.orderedProductSales" /></th>
							<th class="text-right"><spring:message code="salesAndPageTrafficReport.orderItems" /></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${salesAndPageTrafficHistoryLines}" var="historyLine">	
						<tr>
							<td>${historyLine.date}</td>
							<td class="text-right">${historyLine.sessions}</td>
							<td class="text-right">${historyLine.pageViews}</td>
							<td class="text-right">${historyLine.buyBoxPercentage}</td>
							<td class="text-right">${historyLine.unitSessionPercentage}</td>
							<td class="text-right">${historyLine.orderedProductSales}</td>
							<td class="text-right">${historyLine.orderItems}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>