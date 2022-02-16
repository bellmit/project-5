<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="marketingReport.skuAdvertisingPerformanceReport" /> - DRS
</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>

<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">

<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
<!-- 
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>-->
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">

<!--<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">
<script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script> -->
<script src="https://cdn.datatables.net/plug-ins/1.10.16/sorting/absolute.js"></script>

<script src="<c:url value="/resources/js/accounting.min.js"/>"></script>
<link href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css"/>" type="text/css" rel="stylesheet">	
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script>
var numbersType = $.fn.dataTable.absoluteOrderNumber( [
	{ value: 'N/A', position: 'bottom' }
] );

$(function() {
	document.addEventListener("scroll", (e) => {
		const $table = $("#skuAdPerformance");
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

	$("#supplierSelector").select2({
	    theme: "bootstrap"
	});

	$(function () {
		var startDate = '${defaultUtcDateStart}'; 
		var endDate = '${defaultUtcDateEnd}';
      	/* New datepicker */
		  $("#startDateInput").datetimepicker({
			format: 'YYYY-MM-DD', // Originally'MM/DD/YYYY', changed to comply with DRS's date format
			defaultDate: startDate,
			icons: {
				time: "fa fa-clock-o",
				date: "fa fa-calendar",
				up: "fa fa-chevron-up",
				down: "fa fa-chevron-down",
				previous: 'fa fa-chevron-left',
				next: 'fa fa-chevron-right',
				today: 'fa fa-screenshot',
				clear: 'fa fa-trash',
				close: 'fa fa-remove'
			}
		});

		$("#endDateInput").datetimepicker({
			format: 'YYYY-MM-DD', // Originally'MM/DD/YYYY', changed to comply with DRS's date format
			defaultDate: endDate,
			icons: {
				time: "fa fa-clock-o",
				date: "fa fa-calendar",
				up: "fa fa-chevron-up",
				down: "fa fa-chevron-down",
				previous: 'fa fa-chevron-left',
				next: 'fa fa-chevron-right',
				today: 'fa fa-screenshot',
				clear: 'fa fa-trash',
				close: 'fa fa-remove'
			}
		});

		$('[data-toggle="tooltip"]').tooltip();	
		var report = $('#skuAdPerformance').DataTable({
			searching: true,
			paging: false,
			info: false,
			responsive: true,
			fixedHeader: true,
			language: {
				"emptyTable": "<spring:message code='campaignPerformance.noData' />"
			},
			columnDefs: [{
				"targets":0,
				"visible":false,
				"width":"10px"
			},{
				"targets":1,
				"visible":false,
				"width":"100px"
			},{
				"targets":2,
				"visible":false,
				"width":"100px"
			},{
				"targets":3,
				"visible":false,
				"width":"100px"
			},{
				"targets":4,
				"visible":false,
				"width":"100px"
			},{
				"type":numbersType,
				"targets":5,
				"visible":false,
				"width":"100px"
			},{
				"targets":6,
				"visible":false,
				"width":"100px"
			},{
				"type":numbersType,
				"targets":7,
				"visible":false,
				"width":"100px"
			},{
				"targets":8,
				"visible":false,
				"width":"100px"
			},{
				"targets":9,
				"visible":false,
				"width":"100px"
			},{
				"type":numbersType,
				"targets":10,
				"visible":false,
				"width":"100px",
				"className":"text-right"
			}],
			dom: 'Bfrtip',
			buttons: [{
				extend: 'csv',
				text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="campaignPerformance.export" />'
			}]
		});	
	
		$('#skuAdPerformance_filter').hide();	
		report.column(1).visible(true);
		report.column(3).visible(true);
		report.column(4).visible(true);
		report.column(5).visible(true);
		report.column(6).visible(true);
		report.column(7).visible(true);
		report.column(8).visible(true);
		report.column(9).visible(true);
		report.buttons().container().appendTo($('#btnExport'));
		$('#btnExport > .dt-buttons > a').addClass('btn btn-outline-default');

		// try to make a function of when clicking toggle input to show or hide the columns
		// $('#btnShowColumns').click(function() { 
		// 	var myClass = this.className;
   		// 	console.log(myClass);
			
		// 	var ariaExpanded = $("#btnShowColumns").attr("aria-expanded");
		// 	console.log('aria-expanded is' + ariaExpanded);

		// 	if(ariaExpanded === 'false') {
		// 		$("#btnShowColumns").attr("aria-expanded", 'true');
		// 		$("#btnShowColumns").removeClass("collapsed");
		// 		$("#ConfigColumns").removeClass('show');
		// 	} else {
		// 		$("#btnShowColumns").attr("aria-expanded", 'false');
		// 		$("#btnShowColumns").addClass("collapsed");
		// 		$("#ConfigColumns").addClass('show');
		// 	}
		// });

		// click checkbox to select what will show 
		$('input[type="checkbox"]').click(function() {		
			for(i=1;i<11;i++){
				report.column(i).visible(false);
			}
			report.column(1).visible(true);				
			$('input[type="checkbox"]').each(function() {
				report.column($(this).val()).visible($(this).prop('checked'));
			});
			// $("span.btn-success").click();
		});
		$('input[type="checkbox"]').each(function() {				
			$(this).prop('checked',($(this).val()!=2 && $(this).val()!=10));
		});

		// show campaignList
		var campaignNameList = ${campaignNameList};
		$.each(campaignNameList,function(index,value) {
			var c = '<span class="btn btn-pill btn-outline-info">'+value+'</span>&nbsp;&nbsp;';		
			$('#campaignList').append(c);
		});
		if(campaignNameList.length>0) {
			$('span.btn-pill').eq(0).removeClass('btn-outline-info').addClass('btn-info');		
		}
		// if click the btn change the stlye 
		$('span.btn-pill, span.btn-primary').click(function(){
			$('span.btn-pill').removeClass('btn-primary').addClass('btn-outline-info');
			$(this).removeClass('btn-outline-info').addClass('btn-info');
					
			var supplierKcode = '${supplierKcode}';
			var marketplaceId = '${marketplaceId}';	
			var startDate = '${defaultUtcDateStart}'; 
			var endDate = '${defaultUtcDateEnd}';
			var campaignName = $(this).text();
									
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/sku-adv-performance-report/getReportByCampaignName',
				contentType : "application/json; charset=utf-8",
				data : {
					supplierKcode : supplierKcode,
					marketplaceId : marketplaceId,
					startDate : startDate,
					endDate : endDate,
					campaignName : campaignName										
				},
				dataType : "json",
				success : function(data) {
					var skuAdvPerformanceReport = data;																								
					report.rows().remove().draw();																																									
					var items = skuAdvPerformanceReport.campaignItems;
										
					$('#totalSpendCurrency').text("("+skuAdvPerformanceReport.currency+")");
					$('#averageCpcCurrency').text("("+skuAdvPerformanceReport.currency+")");
					$('#productSalesWithinOneWeekOfAClickCurrency').text("("+skuAdvPerformanceReport.currency+")");			
					
					var itemArray = new Array();
					
					items[campaignName].forEach(function(item){
												
						itemArray.push(		                    
							[campaignName,
								item.sku,
								"[ad group]",
								item.totalImpression,
								item.totalClicks,
								item.clickThroughRate,
								item.totalSpend,
								item.costPerClick,
								item.totalOneWeekOrdersPlaced,
								item.totalOneWeekOrderedProductSales,
								item.acos]      
						);
																		
					});
										
					report.rows.add(itemArray).draw();
					$('#grandTotalImpression').text(skuAdvPerformanceReport.grandTotalImpression);
					$('#grandTotalClick').text(skuAdvPerformanceReport.grandTotalClick);
					$('#grandClickThroughRate').text(skuAdvPerformanceReport.grandClickThroughRate);
					$('#grandTotalSpend').text(skuAdvPerformanceReport.grandTotalSpend);
					$('#grandCostPerClick').text(skuAdvPerformanceReport.grandCostPerClick);
					$('#grandTotalOneWeekOrdersPlaced').text(skuAdvPerformanceReport.grandTotalOneWeekOrdersPlaced);
					$('#grandTotalOneWeekOrderedProductSales').text(skuAdvPerformanceReport.grandTotalOneWeekOrderedProductSales);
					$('#grandAcos').text(skuAdvPerformanceReport.grandAcos);							
					$('#skuAdPerformance tbody tr').find('td:eq(1),td:eq(2),td:eq(3),td:eq(4),td:eq(5),td:eq(6),td:eq(7),td:eq(8)').addClass('text-right');																							
				}															
			});
		});	
		
		$('.dataTables_empty').attr('colspan',11);
	});	
});
function getDefaultStartDate(){
	var d = new Date();		
	d.setDate(d.getDate() - 5);		
	var curr_date = d.getDate();
	var curr_month = d.getMonth() + 1;
	var curr_year = d.getFullYear();		
	var defaultStartDate = curr_year + "-" + curr_month + "-" + curr_date;
	return defaultStartDate;						
}

function getDefaultEndDate(){
	var d = new Date();		
	d.setDate(d.getDate() - 4);
	var curr_date = d.getDate();
	var curr_month = d.getMonth() + 1;
	var curr_year = d.getFullYear();
	var defaultEndDate = curr_year + "-" + curr_month + "-" + curr_date;
	return defaultEndDate;		
}
</script>
<style>
	#campaignList{
		margin: 5px 0 5px 0;
	}
</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class="card-title"><spring:message code="marketingReport.skuAdvertisingPerformanceReport" /></h4>
					</div>
				</div>	
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="card-body">
						<form action="sku-adv-performance-reportv4" method="post" class="form-inline mb-1">
							<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT_FILTER']}')">
								<div class="form-inline-wrapper">
									<label class="mt-2 mt-md-0 mr-md-2" for="supplierSelector"><spring:message code="campaignPerformance.supplier" /></label>
									<select id="supplierSelector" class="form-control" name="supplierKcode">
										<option value="">--- Select ---</option>
										<c:forEach items="${supplierKcodeToShortEnNameMap}" var="kcodeAndName">
										<option value="${kcodeAndName.key}" ${kcodeAndName.key==supplierKcode?'selected="selected"':''}>
											${kcodeAndName.key} ${kcodeAndName.value}
										</option>
										</c:forEach>
									</select>
								</div>
							</sec:authorize>
							<div class="form-inline-wrapper">
								<label for="marketplaceSelector" class="mt-2 mt-md-0 mr-md-2 ml-md-2"><spring:message code="campaignPerformance.marketplace" /></label>
								<select id="marketplaceSelector" class="form-control mb-3 mb-md-0" name="marketplaceId" >
									<c:forEach items="${marketplaces}" var="marketplace">
										<option value="${marketplace.key}" ${marketplace.key==marketplaceId?'selected="selected"':''} >${marketplace.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-inline-wrapper">
								<label for="startDateInput" class="mt-2 mt-md-0 mr-1 ml-md-2"><spring:message code="campaignPerformance.startDateInput" /></label>
								<input class="form-control mt-2-md mt-md-0" id="startDateInput" name="start" style="width:106px;cursor:default;background-color:white;">
							</div>	
							<div class="form-inline-wrapper">
								<label for="endDateInput" class="mt-2 mt-md-0 mr-1 ml-1"><spring:message code="campaignPerformance.endDateInput" /></label>
								<input class="form-control mt-1-md mt-md-0 mr-md-2" id="endDateInput" name="end" style="width:106px;cursor:default;background-color:white;">
							</div>
							<div class="form-inline-wrapper">
								<button class="btn btn-primary mt-1 mt-md-0 mr-md-2 searchBtn" type="submit" onclick="this.disabled=true;this.form.submit();">
									<i class="fas fa-search"></i> <spring:message code="campaignPerformance.search" />
								</button>
							</div>
						</form>
						<div class="mb-1">
							<label class="control-label enhancement">
								<spring:message code="campaignPerformance.description" />															
							</label>
						</div>
						<div class="mb-1" id="campaignList">
							<label class="control-label"><i class="fas fa-tags"></i> <spring:message code="campaignPerformance.campaignName" />: </span>&nbsp;&nbsp;</label>
						</div>
					</div>
				</div>						
			</div>
			<div class="row">
				<div class="col-md-12 text-right">
					<button id="btnShowColumns" class="btn btn-outline-default" style="margin-left:10px;float:right;" type="button" data-toggle="collapse" data-target="#ConfigColumns" aria-expanded="false">
						<i class="fas fa-th-list"></i> <spring:message code="campaignPerformance.configColumns" />
					</button>
					<span id="btnExport" style="float:right;"></span>
				</div> 
			</div>	
			<div class="row my-2">
				<div class="col-md-12 text-center">
					<div id="ConfigColumns" class="collapse">	
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="adGroupName" >
								<input class="form-check-input" type="checkbox" name="adGroupName" value="2"  id="adGroupName"/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.adGroupName" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="impressions" >
								<input class="form-check-input" type="checkbox" name="impressions" value="3"  id="impressions" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.impressions" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="clicks" >
								<input class="form-check-input" type="checkbox" name="clicks" value="4"  id="clicks" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.clicks" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="ctr" >
								<input class="form-check-input" type="checkbox" name="ctr" value="5"  id="ctr" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.ctr" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="totalSpend" >
								<input class="form-check-input" type="checkbox" name="totalSpend" value="6"  id="totalSpend" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.totalSpend" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="averageCpc" >
								<input class="form-check-input" type="checkbox" name="averageCpc" value="7"  id="averageCpc" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.averageCpc" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="ordersPlacedWithinOneWeekOfAClick" >
								<input class="form-check-input" type="checkbox" name="ordersPlacedWithinOneWeekOfAClick" value="8"  id="ordersPlacedWithinOneWeekOfAClick" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.ordersPlacedWithinOneWeekOfAClick" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="productSalesWithinOneWeekOfAClick" >
								<input class="form-check-input" type="checkbox" name="productSalesWithinOneWeekOfAClick" value="9"  id="productSalesWithinOneWeekOfAClick" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.productSalesWithinOneWeekOfAClick" />
							</label>
						</div>
						<div class="form-check form-check-inline">
							<label class="form-check-label" for="acos" >
								<input class="form-check-input" type="checkbox" name="acos" value="10"  id="acos" checked/>
								<span class="form-check-sign"></span>
								<spring:message code="campaignPerformance.acos" />
							</label>
						</div>
					</div>
				</div>
			</div>
			<div style="padding-bottom: 10px"></div>
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive" style="overflow:hidden;">		
						<table id="skuAdPerformance" class="table dataTable table-striped table-floated table-width">
							<thead id="tableHeader" class="border-top border-bottom">
								<tr role="row" style="height: 50px;">
									<th class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.campaignNameDesc" />">
										<spring:message code="campaignPerformance.campaignName" /></th>
									<th class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.skuCodeDesc" />">
										<spring:message code="campaignPerformance.skuCode" /></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.adGroupNameDesc" />">
										<spring:message code="campaignPerformance.adGroupName" /></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.impressionsDesc" />">
										<spring:message code="campaignPerformance.impressions" /></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.clicksDesc" />">
										<spring:message code="campaignPerformance.clicks" /></th>							
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.ctrDesc" />">
										<spring:message code="campaignPerformance.ctr" /></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.totalSpendDesc" />">
										<spring:message code="campaignPerformance.totalSpend" /> <span id="totalSpendCurrency">(${report.currency})</span></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.averageCpcDesc" />">
										<spring:message code="campaignPerformance.averageCpc" /> <span id="averageCpcCurrency">(${report.currency})</span></th>							
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.ordersPlacedWithinOneWeekOfAClickDesc" />">
										<spring:message code="campaignPerformance.ordersPlacedWithinOneWeekOfAClick" /></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.productSalesWithinOneWeekOfAClickDesc" />">
										<spring:message code="campaignPerformance.productSalesWithinOneWeekOfAClick" /> <span id="productSalesWithinOneWeekOfAClickCurrency">(${report.currency})</span></th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="campaignPerformance.acosDesc" />">
										<spring:message code="campaignPerformance.acos" /></th>
								</tr>
							</thead>
							<tbody class="border-bottom">
								<c:forEach var="campaignItems" items="${report.campaignItems}">
									<c:forEach var="lineItem" items="${campaignItems.value}" varStatus="status">
										<tr>							
											<td class="text-left">${campaignItems.key}</td>							
											<td class="text-left">${lineItem.sku}</td>
											<td class="text-left">[ad group]</td>
											<td class="text-right">${lineItem.totalImpression}</td>
											<td class="text-right">${lineItem.totalClicks}</td>							
											<td class="text-right">${lineItem.clickThroughRate}</td>
											<td class="text-right">${lineItem.totalSpend}</td>
											<td class="text-right">${lineItem.costPerClick}</td>							
											<td class="text-right">${lineItem.totalOneWeekOrdersPlaced}</td>
											<td class="text-right">${lineItem.totalOneWeekOrderedProductSales}</td>							
											<td class="text-right">${lineItem.acos}</td>
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td></td>
									<td class="text-left"><b><spring:message code="campaignPerformance.total" /></b><input type="hidden" id="reportCurrency" value="${report.currency}"/></td>
									<td></td>
									<td id="grandTotalImpression" class="text-right">${report.grandTotalImpression}</td>
									<td id="grandTotalClick" class="text-right">${report.grandTotalClick}</td>						
									<td id="grandClickThroughRate" class="text-right">${report.grandClickThroughRate}</td>
									<td id="grandTotalSpend" class="text-right">${report.grandTotalSpend}</td>
									<td id="grandCostPerClick" class="text-right">${report.grandCostPerClick}</td>						
									<td id="grandTotalOneWeekOrdersPlaced" class="text-right">${report.grandTotalOneWeekOrdersPlaced}</td>
									<td id="grandTotalOneWeekOrderedProductSales" class="text-right">${report.grandTotalOneWeekOrderedProductSales}</td>						
									<td id="grandAcos" class="text-right">${report.grandAcos}</td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>			