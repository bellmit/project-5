<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='ss2spStatement.PaymentAndRefund' /> ${ivsName} <spring:message code='ss2spStatement.reviewExport' /> - DRS</title>
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
			const $table = $("#paymentAndRefundForPoInPeriodReport");
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
		var report = $('#paymentAndRefundForPoInPeriodReport').DataTable({			 
		 	searching: false,
		    paging: false,
			info: false,
			responsive: true,
			fixedHeader: true,
			columnDefs: [{
				"targets": 0,
				"width": "120px"
			},{
				"targets": 1,
				"width": "140px"
			},{
				"targets": 2,
				"width": "120px"
			},{
				"targets": 3,
				"width": "150px"
			},{
				"targets": 4,
				"width": "160px"
			},{
				"targets": 5,
				"width": "160px"
			},{
				"targets": 6,
				"width": "160px"
			},{
				"targets": 7,
				"width": "90px"
			},{
				"targets": 8,
				"width": "140px"
			},{
				"targets": 9,
				"width": "60px"
			},{
				"targets": 10,
				"width": "100px"
			},{
				"targets": 11,
				"width": "60px"
			},{
				"targets": 12,
				"width": "100px"
			}],
			dom:'Bfrtip',	    
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
	#paymentAndRefundForPoInPeriodReport {
		margin: 1px 1px 1px 15px !important;
		width: 98% !important;
		border-bottom: 1px solid #dee2e6 !important;
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
<div class="max-width" style="max-width: 1640px !important;padding-left: 20px;padding-right: 20px;">
	<div class="max-width">
		<div class="container-fluid">
			<div class="card">
				<!-- [todo] MOVE TO Nav bar in the future -->
				<!-- <div class="row">
					<div class="col-md-12">
						<div class="card-header">
							<h4 class="card-title">
								<spring:message code='ss2spStatement.PaymentAndRefund' /> ${ivsName} 
								<spring:message code='ss2spStatement.reviewExport' />
							</h4>
						</div>
					</div>
				</div> -->
				<div class="row">
					<div class="col-md-12 text-right">
						<div class="card-body">
							<span id="btnExport"></span>
						</div>
					</div>
				</div>
				<div style="padding-bottom: 10px"></div>
				<div class="row">
					<div class="col-md-12 no-padding">
						<div class="table-responsive" style="overflow: hidden; padding-left: 20px;">
							<table id="paymentAndRefundForPoInPeriodReport" class="table dataTable table-striped table-floated">
								<thead id="tableHeader" class="border-top border-bottom pd-reset">
									<tr role="row" style="height: 50px;">
										<th class="text-left ts-s" style="width: 60px;" ><spring:message code="ss2spStatement.statements" /></th>
										<th class="text-left ts-s" stlye="width: 70px;">Period start</th>
										<th class="text-left ts-s" style="width: 60px;">Period end</th>
										<th class="text-left ts-s" style="width: 85px;"><spring:message code="ss2spStatement.ivs" /></th>
										<th class="text-center ts-s" style="width: 85px;"><spring:message code='ss2spStatement.time' /></th>
										<th class="text-left ts-s" style="width: 80px;"><spring:message code='ss2spStatement.SKU' /></th>
										<th class="text-left ts-s" style="width: 80px;"><spring:message code='ss2spStatement.skuName' /></th>
										<th class="text-center ts-s" style="width: 73px;padding-right: 20px;"><spring:message code='ss2spStatement.itemName' /></th>
										<th class="text-left ts-s" style="width: 73px;"><spring:message code='ss2spStatement.sourceItem' /></th>
										<th class="text-center ts-s" style="width: 50px;"><spring:message code='ss2spStatement.sourceCurrency' /></th>
										<th class="text-center ts-s" style="width: 80px;"><spring:message code='ss2spStatement.amountPerUnit' /></th>
										<th class="text-center ts-s" style="width: 50px;"><spring:message code='ss2spStatement.quantity' /></th>
										<th class="text-left ts-s" style="width: 75px;"><spring:message code='ss2spStatement.sourceAmount' /></th>		
									</tr>
								</thead>				
								<tbody class="bb-reset">
									<c:forEach items="${itemList}" var="item">
										<tr>
											<td class="text-left ts-s">${item.statementName}</td>
											<td class="text-left ts-s">${item.dateStart}</td>
											<td class="text-left ts-s">${item.dateEnd}</td>
											<td class='text-left ts-s'>${item.shipmentName}</td>
											<td class="text-center ts-s">${item.transactionTimeUtc}</td>
											<td class="text-left ts-s">${item.sku}</td>
											<td class="text-left ts-s">${item.skuName}</td>
											<td class="text-center ts-s"><spring:message code="${item.itemType}" /></td>
											<td class="text-left ts-s">${item.sourceName}</td>
											<td class="text-center ts-s"><spring:message code="${item.currency}" /></td>
											<td class="text-center ts-s">${item.unitAmount}</td>
											<td class="text-center ts-s">${item.quantity}</td>
											<td class="text-left ts-s">${item.totalAmount}</td>
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