<%@ page session="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
<title><spring:message code='ss2spStatement.profitShare' /> (<spring:message code="${country}" />) <spring:message code='ss2spStatement.reviewExport' /> - DRS</title>

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
	document.addEventListener("scroll", (e) => {
		const $table = $("#skuCountryProfitShare");
		const os = window.navigator.userAgent;
		if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
			if ($table.offset().top >= 0) {
				$table.floatThead();
			} else {
				$table.floatThead("destroy");
			}
			} else {
			if ($table.offset().top <= 0) {
				$table.floatThead();
			} else {
				$table.floatThead("destroy");
			}
		}
	}, true);

	var report = $('#skuCountryProfitShare').DataTable({			 
		searching: false,
		paging: false,
		info: false,
		responsive: true,
		fixedHeader: true,
		columnDefs: [{
			"targets": 0,
		},{
			"targets": 1,
			"width": "100px"
		},{
			"targets": 2,
			"width": "100px"
		},{
			"targets": 3,
			"width": "100px"
		},{
			"targets": 4,
			"width": "100px"
		},{
			"targets": 5,
			"width": "100px"
		},{
			"targets": 6,
			"width": "100px"
		},{
			"targets": 7,
			"width": "100px"
		},{
			"targets": 8,
			"width": "100px"
		},{
			"targets": 9,
			"width": "100px"
		},{
			"targets": 10,
			"width": "100px"
		},{
			"targets": 11,
			"width": "100px"
		},{
			"targets": 12,
			"width": "100px"
		},{
			"targets": 13,
			"width": "100px"
		},{
			"targets": 14,
			"width": "100px"
		},{
			"targets": 15,
			"width": "100px"
		},{
			"targets": 16,
			"width": "100px"
		},{
			"targets": 17,
			"width": "100px"
		},{
			"targets": 18,
			"width": "100px"
		}],
		dom: 'Bfrtip',	    
		buttons: [{
			extend: 'csv',
			text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="ss2spStatement.export" />'
		}]
	});
			
	report.buttons().container().appendTo($('#btnExport'));
	$('#btnExport > .dt-buttons > a').addClass('btn btn-outline-default');	
});

</script>
<style>
	#skuCountryProfitShare {
		margin: 1px 1px 1px 15px !important;
		width: 98% !important;
	}
	.no-padding {
		padding: 0 1px !important;
	}
	tbody tr td:first-child:before {
		left: -24px !important;
	}
	td.child {
		padding-left: 10px !important;
	}
	td.child ul {
		font-size: 12px;
		color: #141414;
	}
</style>
</head>
<div class="max-width" style="max-width:1780px !important;padding-left:20px;padding-right:20px;">
	<div class="max-width">
		<div class="container-fluid">
			<div class='card'>
				<!-- [todo] MOVE TO Nav bar in the future -->
				<!-- <div class="row">
					<div class="col-md-12">
						<div class="card-header">
							<h4 class="card-title"><spring:message code='ss2spStatement.profitShare'/> (<spring:message code="${country}" />) <spring:message code='ss2spStatement.reviewExport' /></h4>
						</div>
					</div>
				</div> -->
				<div class="row">
					<div class="col-md-12 text-right mb-3">
						<span id="btnExport"></span>
					</div>
				</div>
				<div style="padding-bottom: 10px"></div>
				<div class="row">
					<div class="col-md-12 no-padding">
						<div class="table-responsive" style="overflow: hidden; padding-left: 20px;">
							<table id="skuCountryProfitShare" class="table dataTable table-striped table-floated">
								<thead id="tableHeader" class="border-top border-bottom">
									<tr role="row" style="height: 50px;">
										<th class="text-left pd-reset ts-s"><spring:message code="ss2spStatement.statements" /></th>
										<th class="text-left pd-reset ts-s">Period start</th>
										<th class="text-left pd-reset ts-s">Period end</th>
										<th class="text-center pd-reset ts-s"><spring:message code='ss2spStatement.sourceCountry' /></th>
										<th class="text-center pd-reset ts-s"><spring:message code='ss2spStatement.sourceCurrency' /></th>
										<th class="text-center pd-reset ts-s"><spring:message code='ss2spStatement.exchangeRate' /></th>
										<th class="text-left pd-reset ts-s"><spring:message code='ss2spStatement.relatedSKU' /></th>
										<th class="text-left pd-reset ts-s"><spring:message code='ss2spStatement.skuName' /></th>
										<th class="text-left pd-reset ts-s"><spring:message code='ss2spStatement.items' /></th>
										<th class="text-left pd-reset ts-s" style="min-width: 85px"><spring:message code='ss2spStatement.utcTime'/></th>
										<th class="text-left pd-reset ts-s"><spring:message code='ss2spStatement.marketplace'/></th>
										<th class="text-left pd-reset ts-s"><spring:message code='ss2spStatement.orderId'/></th>
										<th class="text-center pd-reset ts-s"><spring:message code='ss2spStatement.quantity' /></th>
										<th class="text-center pd-reset ts-s"><spring:message code='ss2spStatement.pretaxPrice'/></th>
										<th class="text-center pd-reset ts-s"><spring:message code='ss2spStatement.fcaPrice'/></th>
										<th class="text-right pd-reset ts-s"><spring:message code='ss2spStatement.marketplaceFee'/> / <spring:message code='ss2spStatement.marketplaceFeeRefund'/></th>
										<th class="text-right pd-reset ts-s"><spring:message code='ss2spStatement.netLogisticsExpenditure'/> / <spring:message code='ss2spStatement.reverseLogisticsExpenditure'/></th>
										<th class="text-right pd-reset ts-s"><spring:message code='ss2spStatement.drsRetainment'/> / <spring:message code='ss2spStatement.drsRetainmentReversal'/></th>
										<th class="text-right pd-reset ts-s"><spring:message code='ss2spStatement.profitShare' /></th>					
									</tr>
								</thead>
								<tbody class="border-bottom">
									<c:forEach items="${itemList}" var="item">
										<tr>
											<td class="text-left pd-reset ts-s td-more">${item.statementName}</td>
											<td class="text-left pd-reset ts-s">${item.dateStart}</td>
											<td class="text-left pd-reset ts-s">${item.dateEnd}</td>
											<td class="text-center pd-reset ts-s"><spring:message code='${item.country}' /></td>
											<td class="text-center pd-reset ts-s"><spring:message code='${item.sourceCurrency}' /></td>
											<td class="text-center pd-reset ts-s">${item.exchangeRate}</td>
											<td class="text-left pd-reset ts-s">${item.sku}</td>
											<td class="text-left pd-reset-wider ts-s">${item.skuName}</td>
											<td class="text-left pd-reset ts-s"><spring:message code='${item.name}' /></td>
											<td class="text-left pd-reset ts-s">${item.utcDate}</td>
											<td class="text-left pd-reset ts-s">${item.marketplace.name}</td>
											<td class="text-left pd-reset ts-s">${item.orderId}</td>
											<td class="text-center pd-reset ts-s">${item.quantity}</td>
											<td class="text-left pd-reset ts-s">${item.pretaxPrincipalPrice}</td>
											<td class="text-right pd-reset ts-s">${item.fcaInMarketSideCurrency}</td>
											<td class="text-right pd-reset ts-s">${item.marketplaceFee}</td>
											<td class="text-right pd-reset ts-s">${item.fulfillmentFee}</td>
											<td class="text-right pd-reset ts-s">${item.drsRetainment}</td>
											<td class="text-right pd-reset ts-s">${item.profitShare}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	