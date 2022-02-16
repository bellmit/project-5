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
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.2.2/css/responsive.bootstrap4.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<link href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css"/>" type="text/css" rel="stylesheet">	
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.2/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.2/js/responsive.bootstrap4.min.js"></script>
<script src="https://cdn.datatables.net/plug-ins/1.10.16/sorting/absolute.js"></script>
<script>
var numbersType = $.fn.dataTable.absoluteOrderNumber( [
	{ value: 'N/A', position: 'bottom' }
] );
$(function() {	
	$('[data-toggle="tooltip"]').tooltip();				
	var report = $('#skuAdEvaluation').DataTable({
		searching: false,
	    paging: false,
	    info: false,
	    responsive: true,
	    fixedHeader: true,
	    autoWidth: false,
        columnDefs: [{
    		"targets":0,
    	},{
    		"targets":1,
    	},{
    		"type":numbersType,
    		"targets":2,
    	},{
    		"type":numbersType,
    		"targets":3,
    	},{
    		"targets":4,
    	},{
    		"targets":5,
    	},{
    		"targets":6,
    	},{
    		"targets":7,
    	},{
    		"targets":8,
    	},{
    		"targets":9,
    	},{
    		"type":numbersType,
    		"targets":10,
    	},{
    		"type":numbersType,
    		"targets":11,
    	}],
        buttons: [{
        	extend: 'csv',
            text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="skuAdvertisingEvaluationReport.export" />'
        }]
	});	
	report.column(4).visible(true);
	report.column(5).visible(true);
	report.column(6).visible(true);
	report.column(7).visible(true);
	report.column(8).visible(true);
	report.column(9).visible(true);
	report.column(10).visible(true);
	report.column(11).visible(true);
	report.buttons().container().appendTo($('#btnExport'));
	$('#btnExport > .dt-buttons > a').addClass('btn btn-default');
	$('input[type="checkbox"]').click(function(){
		report.column($(this).val()).visible($(this).prop('checked'));
	});
	$('#supplierSelector').select2({
	    theme: "bootstrap"
	});	
});
</script>
<style>

</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="marketingReport.skuAdvertisingEvaluationReport" /></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
			<form method="POST" class="form-inline" action="${pageContext.request.contextPath}/sku-adv-eval-report">
			<div class="form-inline">
				<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT_FILTER']}')">
				<label class="mt-2 mt-md-0 mr-md-2"><spring:message code="campaignPerformance.supplier" /></label>
				<select id="supplierSelector" class="form-control" name="supplierKcode">
					<option value="">--- Select ---</option>
					<c:forEach items="${supplierKcodeNameMap}" var="supplierKcodeName">
						<option value="${supplierKcodeName.key}" ${supplierKcodeName.key==supplierKcode?'selected="selected"':''}>
							${supplierKcodeName.key} ${supplierKcodeName.value}
						</option>
					</c:forEach>
				</select>
				</sec:authorize>
				<label class="mt-2 mt-md-0 mr-md-2 ml-md-2"><spring:message code="skuAdvertisingEvaluationReport.marketPlace" /></label>
					<select class="form-control" name="marketplaceId" >
					<c:forEach items="${marketplaces}" var="marketplace">
						<option value="${marketplace.key}" ${marketplace.key==marketplaceId?'selected="selected"':''} >${marketplace.name}</option>
					</c:forEach>
				</select>
				<label class="mt-2 mt-md-0 mr-2 ml-md-2"><spring:message code="skuAdvertisingEvaluationReport.period" /></label>
				<select class="form-control mt-2-md mt-md-0" name="periodId">
					<c:forEach items="${settlementPeriodList}" var="settlementPeriod">
						<option value="${settlementPeriod.id}" ${settlementPeriod.id==settlementPeriodId?'selected="selected"':''}>
							${settlementPeriod.startDate} ~ ${settlementPeriod.endDate}
						</option>
					</c:forEach>
				</select>
				<button class="btn btn-primary mt-2 mt-md-0 ml-md-2" type="submit"><i class="fas fa-search"></i> <spring:message code="skuAdvertisingEvaluationReport.search" /></button>
				</div>
			</form>
			</div>
		</div>		
		<div style="padding-bottom: 10px"></div>		
		<div class="row">
			<div class="col-md-12">
				<label class="control-label enhancement">
					<spring:message code="skuAdvertisingEvaluationReport.description" />
				</label>
			</div>
		</div>				
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12 text-right mb-3">
				<button type="button" class="btn btn-default" style="margin-left:10px;float:right;" data-toggle="collapse" data-target="#ConfigColumns" aria-expanded="false" >
					<i class="fas fa-th-list"></i> <spring:message code="campaignPerformance.configColumns" />
				</button>
				<span id="btnExport" style="float:right;"></span>
			</div>
		</div>
		<div class="row">	
			<div class="col-md-12 text-center">
				<div id="ConfigColumns" class="collapse">
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="adClicks" style="display: inline;" value="2"  id="adClicks" />
						<label class="form-check-label" for="adClicks" ><spring:message code="skuAdvertisingEvaluationReport.adClicks" /></label>
					</div>
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="adSpend" style="display: inline;" value="3"  id="adSpend" />
						<label class="form-check-label" for="adSpend" ><spring:message code="skuAdvertisingEvaluationReport.adSpend" /></label>
					</div>
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="sessions" style="display: inline;" value="4"  id="sessions"  checked />
						<label class="form-check-label" for="sessions" ><spring:message code="skuAdvertisingEvaluationReport.sessions" /></label>
					</div>
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="pageViews" style="display: inline;" value="5"  id="pageViews"  checked />
						<label class="form-check-label" for="pageViews" ><spring:message code="skuAdvertisingEvaluationReport.pageViews" /></label>
					</div>	
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="buyBoxPercentage" style="display: inline;" value="6"  id="buyBoxPercentage"  checked />
						<label class="form-check-label" for="buyBoxPercentage" ><spring:message code="skuAdvertisingEvaluationReport.buyBoxPercentage" /></label>
					</div>																									
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="unitsOrdered" style="display: inline;" value="7"  id="unitsOrdered"  checked />
						<label class="form-check-label" for="unitsOrdered" ><spring:message code="skuAdvertisingEvaluationReport.unitsOrdered" /></label>
					</div>									
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="orderedProductSales" style="display: inline;" value="8"  id="orderedProductSales" checked  />
						<label class="form-check-label" for="orderedProductSales" ><spring:message code="skuAdvertisingEvaluationReport.orderedProductSales" /></label>
					</div>		
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="unitSessionPercentage" style="display: inline;" value="9"  id="unitSessionPercentage"  checked />
						<label class="form-check-label" for="unitSessionPercentage" ><spring:message code="skuAdvertisingEvaluationReport.unitSessionPercentage" /></label>
					</div>
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="acos" style="display: inline;" value="10"  id="acos" checked />
						<label class="form-check-label" for="acos" ><spring:message code="skuAdvertisingEvaluationReport.acos" /></label>
					</div>
					<div class="form-check form-check-inline"">
						<input class="form-check-input" type="checkbox" name="pageTrafficThroughAdvertising" style="display: inline;" value="11"  id="pageTrafficThroughAdvertising" checked />
						<label class="form-check-label" for="pageTrafficThroughAdvertising" ><spring:message code="skuAdvertisingEvaluationReport.pageTrafficThroughAdvertising" /></label>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table" id="skuAdEvaluation" style="border-spacing:0;width:100%">
					<thead>
						<tr>
							<th><spring:message code="skuAdvertisingEvaluationReport.skuCode" /></th>
							<th data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.skuNameDesc" />"><spring:message code="skuAdvertisingEvaluationReport.skuName" /></th>
							<th class="text-right"><spring:message code="skuAdvertisingEvaluationReport.adClicks" /></th>
							<th class="text-right"><spring:message code="skuAdvertisingEvaluationReport.adSpend" /> (${report.currency})</th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.sessionsDesc" />"><spring:message code="skuAdvertisingEvaluationReport.sessions" /></th>
							<th class="text-right"><spring:message code="skuAdvertisingEvaluationReport.pageViews" /></th>
							<th class="text-right"><spring:message code="skuAdvertisingEvaluationReport.buyBoxPercentage" /></th>
							<th class="text-right"><spring:message code="skuAdvertisingEvaluationReport.unitsOrdered" /></th>
							<th class="text-right"><spring:message code="skuAdvertisingEvaluationReport.orderedProductSales" /> (${report.currency})</th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.unitSessionPercentageDesc" />"><spring:message code="skuAdvertisingEvaluationReport.unitSessionPercentage" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.acosDesc" />"><spring:message code="skuAdvertisingEvaluationReport.acos" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="skuAdvertisingEvaluationReport.pageTrafficThroughAdvertisingDesc" />"><spring:message code="skuAdvertisingEvaluationReport.pageTrafficThroughAdvertising" /></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="item" items="${report.lineItems}">
						<tr>
							<td>${item.sku}</td>
							<td>${item.skuName}</td>
							<td class="text-right">${item.totalAdClicks}</td>
							<td class="text-right">${item.totalAdSpend}</td>
							<td class="text-right">${item.totalSessions}</td>
							<td class="text-right">${item.totalPageViews}</td>
							<td class="text-right">${item.buyBoxPercentage}</td>
							<td class="text-right">${item.totalUnitOrdered}</td>
							<td class="text-right">${item.totalOrderedProductSales}</td>
							<td class="text-right">${item.conversionRate}</td>
							<td class="text-right">${item.acos}</td>
							<td class="text-right">${item.totalClicksOverTotalPageViews}</td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td class="text-center"><b><spring:message code="skuAdvertisingEvaluationReport.total" /></b></td>
							<td class="text-right"></td>
							<td class="text-right">${report.grandTotalAdClicks}</td>
							<td class="text-right">${report.grandTotalAdSpend}</td>
							<td class="text-right">${report.grandTotalSessions}</td>
							<td class="text-right">${report.grandTotalPageViews}</td>
							<td class="text-right">${report.grandBuyBoxPercentage}</td>
							<td class="text-right">${report.grandTotalUnitOrdered}</td>
							<td class="text-right">${report.grandTotalOrderedProductSales}</td>
							<td class="text-right">${report.grandConversionRate}</td>
							<td class="text-right">${report.grandAcos}</td>
							<td class="text-right">${report.grandTotalClicksOverTotalPageViews}</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</div>