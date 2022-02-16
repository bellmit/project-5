<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="marketingReport.skuAdvertisingEvaluationReport" /> - DRS
</title>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
	
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">

<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>

<!-- may not being used -->
<!-- <script src="https://cdn.datatables.net/responsive/2.2.2/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.2/js/responsive.bootstrap4.min.js"></script> -->

<!-- <link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script> -->

<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">
<!-- <link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css"> -->
<!-- <script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script> -->
<script src="https://cdn.datatables.net/plug-ins/1.10.16/sorting/absolute.js"></script>

<link href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css"/>" type="text/css" rel="stylesheet">	
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>


<script>
var numbersType = $.fn.dataTable.absoluteOrderNumber( [
	{ value: 'N/A', position: 'bottom' }
] );

$(function() {
	$('#supplierSelector').select2({
	    theme: "bootstrap"
	});

	$('[data-toggle="tooltip"]').tooltip();				
	var report = $('#skuAdEvaluation').DataTable({
		searching: false,
	    paging: false,
	    info: false,
	    responsive: true,
	    fixedHeader: true,
		// autoWidth: false,
		language: {
			"emptyTable": "<spring:message code='campaignPerformance.noData'/>"
		},
        columnDefs: [{
			"targets":0,
    	},{
			"targets":1,
			"width":"100px"
    	},{
    		"type":numbersType,
			"targets":2,
			"width":"100px"
    	},{
    		"type":numbersType,
			"targets":3,
			"width":"100px"
    	},{
			"targets":4,
			"width":"100px"
    	},{
			"targets":5,
			"width":"100px"
    	},{
			"targets":6,
			"width":"120px"
    	},{
			"targets":7,
			"width":"120px"
    	},{
			"targets":8,
			"width":"100px"
    	},{
			"targets":9,
			"width":"100px"
    	},{
    		"type":numbersType,
			"targets":10,
			"width":"80px"
    	},{
    		"type":numbersType,
			"targets":11,
			"width":"100px"
		}],
		dom: 'Bfrtip',
        buttons: [{
        	extend: 'csv',
            text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="skuAdvertisingEvaluationReport.export" />'
        }]
	});	
	$('#skuAdPerformance_filter').hide();
	// report.column(0).visible(true);
	report.column(1).visible(true);
	report.column(2).visible(true);
	report.column(3).visible(true);
	report.column(4).visible(true);
	report.column(5).visible(true);
	report.column(6).visible(true);
	report.column(7).visible(true);
	report.column(8).visible(true);
	report.column(9).visible(true);
	report.column(10).visible(true);
	report.column(11).visible(true);
	report.buttons().container().appendTo($('#btnExport'));
	$('#btnExport > .dt-buttons > a').addClass('btn btn-outline-default');
	$('input[type="checkbox"]').click(function(){
		report.column($(this).val()).visible($(this).prop('checked'));
	});
});
</script>
<style>

</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class="card-title"><spring:message code="marketingReport.skuAdvertisingEvaluationReport"/></h4>
					</div>
				</div>
			</div>
							
			<div class="row">
				<div class="col-md-12">
					<div class="card-body">
						<form method="POST" class="form-inline mb-1" action="${pageContext.request.contextPath}/sku-adv-eval-reportV4">
							<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT_FILTER']}')">
								<div class="form-inline-wrapper">
									<label class="mt-2 mt-md-0 mr-md-2"><spring:message code="campaignPerformance.supplier" /></label>
									<select id="supplierSelector" class="form-control" name="supplierKcode">
										<option value="">--- Select ---</option>
										<c:forEach items="${supplierKcodeNameMap}" var="supplierKcodeName">
											<option value="${supplierKcodeName.key}" ${supplierKcodeName.key==supplierKcode?'selected="selected"':''}>
												${supplierKcodeName.key} ${supplierKcodeName.value}
											</option>
										</c:forEach>
									</select>
								</div>
							</sec:authorize>
							<div class="form-inline-wrapper">
								<label class="mt-2 mt-md-0 mr-md-2 ml-md-2"><spring:message code="skuAdvertisingEvaluationReport.marketPlace" /></label>
									<select class="form-control" name="marketplaceId" >
									<c:forEach items="${marketplaces}" var="marketplace">
										<option value="${marketplace.key}" ${marketplace.key==marketplaceId?'selected="selected"':''} >${marketplace.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-inline-wrapper">
								<label class="mt-2 mt-md-0 mr-2 ml-md-2"><spring:message code="skuAdvertisingEvaluationReport.period" /></label>
								<select class="form-control mt-2-md mt-md-0" name="periodId">
									<c:forEach items="${settlementPeriodList}" var="settlementPeriod">
										<option value="${settlementPeriod.id}" ${settlementPeriod.id==settlementPeriodId?'selected="selected"':''}>
											${settlementPeriod.startDate} ~ ${settlementPeriod.endDate}
										</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-inline-wrapper">
								<button class="btn btn-primary mt-2 mt-md-0 ml-md-2 searchBtn" type="submit">
									<i class="fas fa-search"></i> <spring:message code="skuAdvertisingEvaluationReport.search"/>
								</button>
							</div>
						</form>		
						<div class="m-1">
							<label class="control-label enhancement">
								<spring:message code="skuAdvertisingEvaluationReport.description"/>
							</label>
						</div>
					</div>
				</div>
			</div>	
			<div class="row">
				<div class="col-md-12 text-right mb-3">
					<button type="button" class="btn btn-outline-default" style="margin-left:10px;float:right;" data-toggle="collapse" data-target="#ConfigColumns" aria-expanded="false" >
						<i class="fas fa-th-list"></i> <spring:message code="campaignPerformance.configColumns"/>
					</button>
					<span id="btnExport" style="float:right;"></span>
				</div>
			</div>
			<div class="row">	
				<div class="col-md-12 text-center">
					<div id="ConfigColumns" class="collapse">
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="adClicks">
								<input class="form-check-input" type="checkbox" name="adClicks" value="0" id="adClicks" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.adClicks"/>
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="adSpend">
								<input class="form-check-input" type="checkbox" name="adSpend" value="1" id="adSpend" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.adSpend"/>
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="sessions">
								<input class="form-check-input" type="checkbox" name="sessions" value="2" id="sessions" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.sessions"/>
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="pageViews">
								<input class="form-check-input" type="checkbox" name="pageViews" value="3" id="pageViews" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.pageViews"/>
							</label>
						</div>	
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="buyBoxPercentage">
								<input class="form-check-input" type="checkbox" name="buyBoxPercentage" value="4" id="buyBoxPercentage" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.buyBoxPercentage"/>
							</label>
						</div>																									
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="unitsOrdered">
								<input class="form-check-input" type="checkbox" name="unitsOrdered" value="5" id="unitsOrdered" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.unitsOrdered"/>
							</label>
						</div>									
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="orderedProductSales">
								<input class="form-check-input" type="checkbox" name="orderedProductSales" value="6" id="orderedProductSales" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.orderedProductSales"/>
							</label>
						</div>		
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="unitSessionPercentage">
								<input class="form-check-input" type="checkbox" name="unitSessionPercentage" value="7" id="unitSessionPercentage" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.unitSessionPercentage"/>
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="acos">
								<input class="form-check-input" type="checkbox" name="acos" value="8" id="acos" checked />
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.acos"/>
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="pageTrafficThroughAdvertising">
								<input class="form-check-input" type="checkbox" name="pageTrafficThroughAdvertising" value="9"  id="pageTrafficThroughAdvertising" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="skuAdvertisingEvaluationReport.pageTrafficThroughAdvertising"/>
							</label>
						</div>
					</div>
				</div>
			</div>
			<div stlye="padding-bottom: 10px"></div>
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive" style="overflow:hidden;">	
					<table id="skuAdEvaluation" class="table dataTable table-striped table-floated table-width" >
						<thead id="tableHeader" class="border-top border-bottom">
							<tr role="row" style="height: 50px;">
								<th><spring:message code="skuAdvertisingEvaluationReport.skuCode" /></th>
								<th data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.skuNameDesc" />">
									<spring:message code="skuAdvertisingEvaluationReport.skuName" /></th>
								<th class="text-right">
									<spring:message code="skuAdvertisingEvaluationReport.adClicks" /></th>
								<th class="text-right">
									<spring:message code="skuAdvertisingEvaluationReport.adSpend" /> (${report.currency})</th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.sessionsDesc" />">
									<spring:message code="skuAdvertisingEvaluationReport.sessions" /></th>
								<th class="text-right">
									<spring:message code="skuAdvertisingEvaluationReport.pageViews" /></th>
								<th class="text-right">
									<spring:message code="skuAdvertisingEvaluationReport.buyBoxPercentage" /></th>
								<th class="text-right">
									<spring:message code="skuAdvertisingEvaluationReport.unitsOrdered" /></th>
								<th class="text-right">
									<spring:message code="skuAdvertisingEvaluationReport.orderedProductSales" /> (${report.currency})</th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.unitSessionPercentageDesc" />">
									<spring:message code="skuAdvertisingEvaluationReport.unitSessionPercentage" /></th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.acosDesc" />">
									<spring:message code="skuAdvertisingEvaluationReport.acos" /></th>
								<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.pageTrafficThroughAdvertisingDesc" />">
									<spring:message code="skuAdvertisingEvaluationReport.pageTrafficThroughAdvertising" /></th>
							</tr>
						</thead>
						<tbody class="border-bottom">
							<c:forEach var="item" items="${report.lineItems}">
								<tr>
									<td>${item.sku}</td>
									<td>${item.skuName}</td>
									<td class="text-center">${item.totalAdClicks}</td>
									<td class="text-center">${item.totalAdSpend}</td>
									<td class="text-center">${item.totalSessions}</td>
									<td class="text-center">${item.totalPageViews}</td>
									<td class="text-center">${item.buyBoxPercentage}</td>
									<td class="text-center">${item.totalUnitOrdered}</td>
									<td class="text-center">${item.totalOrderedProductSales}</td>
									<td class="text-center">${item.conversionRate}</td>
									<td class="text-center">${item.acos}</td>
									<td class="text-center">${item.totalClicksOverTotalPageViews}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
							<tr>
								<td class="text-left"><b><spring:message code="skuAdvertisingEvaluationReport.total" /></b></td>
								<td class="text-center"></td>
								<td class="text-center">${report.grandTotalAdClicks}</td>
								<td class="text-center">${report.grandTotalAdSpend}</td>
								<td class="text-center">${report.grandTotalSessions}</td>
								<td class="text-center">${report.grandTotalPageViews}</td>
								<td class="text-center">${report.grandBuyBoxPercentage}</td>
								<td class="text-center">${report.grandTotalUnitOrdered}</td>
								<td class="text-center">${report.grandTotalOrderedProductSales}</td>
								<td class="text-center">${report.grandConversionRate}</td>
								<td class="text-center">${report.grandAcos}</td>
								<td class="text-center">${report.grandTotalClicksOverTotalPageViews}</td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>