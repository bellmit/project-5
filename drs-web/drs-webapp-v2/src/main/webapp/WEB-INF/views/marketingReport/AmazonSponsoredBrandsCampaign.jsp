<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="sponsoredBrandsCampaign.title" /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<script src="<c:url value="/resources/js/accounting.min.js"/>"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.2.2/css/responsive.bootstrap4.min.css">
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
		jQuery("#startDateInput").datepicker({
			beforeShow: function() {
		        setTimeout(function(){
		            $('.ui-datepicker').css('z-index', 200);
		        }, 0);
		    },
			dateFormat : 'yy-mm-dd'	
		});
		jQuery("#endDateInput").datepicker({
			beforeShow: function() {
		        setTimeout(function(){
		            $('.ui-datepicker').css('z-index', 200);
		        }, 0);
		    },
			dateFormat : 'yy-mm-dd'
		});						
		jQuery("#startDateInput").datepicker('setDate','${defaultUtcDateStart}');		
		jQuery("#endDateInput").datepicker('setDate','${defaultUtcDateEnd}');								
		$("#startDateInput,#endDateInput").attr('readonly', true);	
		$("#supplierSelector").select2({
		    theme: "bootstrap"
		});

		$('[data-toggle="tooltip"]').tooltip();
		var report = $('#sponsoredBrandsCampaign').DataTable({
			searching: false,
		    paging: false,
		    info: false,
		    responsive: true,
		    fixedHeader: true,
	        columnDefs: [{
	        	"targets":0,
				"width": "100px"
	        },{
	        	"targets":1,
				"width": "50px"
	        },{
	        	"targets":2,
				"width": "50px"
	        },{
	        	"targets":3,
				"width": "50px"
	        },{
	        	"type":numbersType,
	        	"targets":4,
				"width": "100px"
	        },{	
	        	"type":numbersType,
	        	"targets":5,
				"width": "100px"
	        },{
	        	"targets":6,
				"width": "100px"
	        },{
	        	"targets":7,
				"width": "100px"
	        },{
	        	"targets":8,
				"width": "100px"
	        },{
	        	"targets":9,
				"width": "100px"
	        }],
			dom: 'Bfrtip',
	        buttons: [{
	        	extend: 'csv',
	            text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="campaignPerformance.export" />'
	        }]
		});
		$('#sponsoredBrandsCampaign_filter').hide();

		report.column(0).visible(true);
		report.column(1).visible(true);
		report.column(2).visible(true);
		report.column(3).visible(true);
		report.column(4).visible(true);
		report.column(5).visible(true);
		report.column(6).visible(true);
		report.column(7).visible(true);	
		report.column(9).visible(true);		
		report.buttons().container().appendTo($('#btnExport'));
		$('#btnExport > .dt-buttons > a').addClass('btn btn-default');		
		$('input[type="checkbox"]').click(function(){
			for(i=0;i<10;i++){
				report.column(i).visible(false);
			}
			report.column(0).visible(true);
			report.column(1).visible(true);
			$('input[type="checkbox"]').each(function(){
				report.column($(this).val()).visible($(this).prop('checked'));
			});
		});
		$('input[type="checkbox"]').each(function(){
			$(this).prop('checked');
		});		
		var campaignNameList = ${campaignNameList};

		// console.log(campaignNameList);
				
		$.each(campaignNameList,function(index,value){
			var c = '<span class="btn btn-pill btn-default">'+value+'</span>&nbsp;&nbsp;';		
			$('#campaignList').append(c);
		});
		if(campaignNameList.length>0){
			$('span.btn-pill').eq(0).removeClass('btn-default').addClass('btn-success');			
		}
		$('span.btn-pill').click(function(){
			$('span.btn-pill').removeClass('btn-success').addClass('btn-default');
			$(this).removeClass('btn-default').addClass('btn-success');
						
			var supplierKcode = '${supplierKcode}';
			var marketplaceId = '${marketplaceId}';		
			var campaignName = $(this).text();
			var startDate = $('#startDateInput').val();
			var endDate = $('#endDateInput').val();
			
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/AmazonSponsoredBrandsCampaign/getAmazonSponsoredBrandsCampaignByCampaignName',
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
					var campaignReport = data;
																												
					report.rows().remove().draw();																																									
					var items = campaignReport.hsaCampaignReportItems;
					// console.log(campaignReport);
										
					$('#cpcCurrency').text("("+campaignReport.currency+")");
					$('#spendCurrency').text("("+campaignReport.currency+")");
					$('#14dayTotalSalesCurrency').text("("+campaignReport.currency+")");
					
					items.forEach(function(item){
																											
						report.rows.add(
			                    [
			                    [item.reportDate,
			                    item.impressions,
			                    item.clicks,
			                    item.clickThruRate,
			                    item.costPerClick,
			                    item.totalSpend,
			                    item.totalSales14days,
			                    item.totalOrders14days,
			                    item.totalUnits14days,
			                    item.conversionRate14days]
			                    ]
			            ).draw();
																	
					});

					$('#reportDate').text(campaignReport.reportDate);																				
					$('#totalImpressions').text(campaignReport.totalImpressions);
					$('#totalClicks').text(campaignReport.totalClicks);
					$('#totalClickThruRate').text(campaignReport.totalClickThruRate);
					$('#totalCostPerClick').text(campaignReport.totalCostPerClick);
					$('#totalSpend').text(campaignReport.totalSpend);
					$('#totalSales14days').text(campaignReport.totalSales14days);
					$('#totalOrders14days').text(campaignReport.totalOrders14days);
					$('#totalUnits14days').text(campaignReport.totalUnits14days);
					$('#totalConversionRate14days').text(campaignReport.totalConversionRate14days);
									
					$('#sponsoredBrandsCampaign tbody tr').find('td:eq(2), td:eq(3), td:eq(4), td:eq(5), td:eq(6), td:eq(7), td:eq(8), td:eq(9), td:eq(10)').addClass('text-right');
																			
				}															
			});
		});
		$('.dataTables_empty').attr('colspan',8);
												
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

</style>
</head>
	<div class="max-width">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code="sponsoredBrandsCampaignCP.title" />
					</div>
				</div>
			</div>		
			<div class="row">
				<div class="col-md-12">
					<form action="AmazonSponsoredBrandsCampaign" method="post" class="form-inline mb-1">
						<sec:authorize access="hasAnyRole('${auth['SPONSORED_BRANDS_CAMPAIGN_REPORT_FILTER']}')">					
							<label class="mt-2 mt-md-0 mr-md-2" for="supplierSelector"><spring:message code="sponsoredBrandsCampaign.supplier" /></label>
							<select id="supplierSelector" class="form-control" name="supplierKcode">
								<option value="">--- Select ---</option>
								<c:forEach items="${supplierKcodeNameMap}" var="kcodeAndName">
									<option value="${kcodeAndName.key}" ${kcodeAndName.key==supplierKcode?'selected="selected"':''}>
										${kcodeAndName.key} ${kcodeAndName.value}
									</option>
								</c:forEach>							
							</select>
						</sec:authorize>
						
							<label for="marketplaceSelector" class="mt-2 mt-md-0 mr-md-2 ml-md-2" ><spring:message code="sponsoredBrandsCampaign.marketplace" /></label>
							<select id="marketplaceSelector" class="form-control mb-3 mb-md-0" name="marketplaceId">
								<c:forEach items="${marketplacesMap}" var="marketplace">
									<option value="${marketplace.key}" ${marketplace.key==marketplaceId?'selected="selected"':''}>${marketplace.value}</option>
								</c:forEach>						
							</select>
							
							<label for="startDateInput" class="mt-2 mt-md-0 mr-2 ml-md-2"><spring:message code="sponsoredBrandsCampaign.startDateInput" /></label>
							<input class="form-control mt-2-md mt-md-0" id="startDateInput" name="start" style="width:100px;cursor:default;background-color:white;">
							
							<label for="endDateInput" class="mt-2 mt-md-0 mr-2 ml-2"><spring:message code="sponsoredBrandsCampaign.endDateInput" /></label>
							<input class="form-control mt-2-md mt-md-0 mr-md-2" id="endDateInput" name="end" style="width:100px;cursor:default;background-color:white;">			
							
							<button class="btn btn-primary mt-2 mt-md-0 mr-md-2 " id="submitButton" type="submit" onclick="this.disabled=true;this.form.submit();"><i class="fas fa-search"></i> <spring:message code="sponsoredBrandsCampaign.search" /></button>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 mb-1">
					<label class="control-label enhancement">
						<spring:message code="sponsoredBrandsCampaignCP.description" />
					</label>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 mb-1" id="campaignList">
					<label class="control-label"><i class="fas fa-tags"></i> <spring:message code="sponsoredBrandsCampaign.campaignName" />: </span>&nbsp;&nbsp;</label>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 text-right">
					<button type="button" class="btn btn-default" style="margin-left:10px;float:right;" data-toggle="collapse" data-target="#ConfigColumns" aria-expanded="false" ><i class="fas fa-th-list"></i> <spring:message code="sponsoredBrandsCampaign.configColumns" /></button>	
					<span id="btnExport" style="float:right;"></span>
					</div>
			</div>
			<div class="row my-2">	
				<div class="col-md-12 text-center ">
					<div id="ConfigColumns" class="collapse">
						<div class="form-check form-check-inline" >
							<input class="form-check-input" type="checkbox" name="reportDate" style="display: inline;" value="1" id="reportDate" checked />
							<label class="form-check-label" for="reportDate" ><spring:message code="sponsoredBrandsCampaignCP.rptDate" /></label>
						</div>							
						<div class="form-check form-check-inline" >
							<input class="form-check-input" type="checkbox" name="impressions" style="display: inline;" value="2" id="impressions" checked />
							<label class="form-check-label" for="impressions" ><spring:message code="sponsoredBrandsCampaign.impressions" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="clicks" style="display: inline;" value="3" id="clicks" checked />
							<label class="form-check-label" for="clicks" ><spring:message code="sponsoredBrandsCampaign.clicks" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="ctr" style="display: inline;" value="4" id="ctr" checked />
							<label class="form-check-label" for="ctr" ><spring:message code="sponsoredBrandsCampaign.ctr" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="cpc" style="display: inline;" value="5" id="cpc" checked />
							<label class="form-check-label" for="cpc" ><spring:message code="sponsoredBrandsCampaign.cpc" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="spend" style="display: inline;" value="6" id="spend" checked />
							<label class="form-check-label" for="spend" ><spring:message code="sponsoredBrandsCampaign.spend" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="14dayTotalSales" style="display: inline;" value="7" id="14dayTotalSales" checked />
							<label class="form-check-label" for="14dayTotalSales" ><spring:message code="sponsoredBrandsCampaign.14dayTotalSales" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="14dayTotalOrders" style="display: inline;" value="8" id="14dayTotalOrders" checked />
							<label class="form-check-label" for="14dayTotalOrders" ><spring:message code="sponsoredBrandsCampaign.14dayTotalOrders" /></label>
						</div>	
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="14daytotalUnits" style="display: inline;" value="9" id="14daytotalUnits" checked />
							<label class="form-check-label" for="14daytotalUnits" ><spring:message code="sponsoredBrandsCampaign.14daytotalUnits" /></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="14dayConversionRate" style="display: inline;" value="10" id="14dayConversionRate" checked />
							<label class="form-check-label" for="14dayConversionRate" ><spring:message code="sponsoredBrandsCampaign.14dayConversionRate" /></label>
						</div>
					</div>
				</div>
			</div>

	<div style="padding-bottom: 10px"></div>
	<div class="row">
		<div class="col-md-12">
			<div class="wrapper">
				<table class="table dataTable dtr-inline" id="sponsoredBrandsCampaign" style="border-spacing:0;width:100%;">
					<thead>
						<tr>
							<th style="width: 6%;  max-width:80px; padding-left: 25px;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="reportDate"><span>日期</span></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="sponsoredBrandsCampaign.impressionsDesc" />"><spring:message code="sponsoredBrandsCampaign.impressions" /></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="sponsoredBrandsCampaign.clicksDesc" />"><spring:message code="sponsoredBrandsCampaign.clicks" /></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="sponsoredBrandsCampaign.ctrDesc" />"><spring:message code="sponsoredBrandsCampaign.ctr" /></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="sponsoredBrandsCampaign.cpcDesc" />"><spring:message code="sponsoredBrandsCampaign.cpc" /> <span id="cpcCurrency">(${hsaCampaignReport.currency})</span></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="sponsoredBrandsCampaign.spendDesc" />"><spring:message code="sponsoredBrandsCampaign.spend" /> <span id="spendCurrency">(${hsaCampaignReport.currency})</span></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body"> <spring:message code="sponsoredBrandsCampaign.14dayTotalSales" /><span id="14dayTotalSalesCurrency">(${hsaCampaignReport.currency})</span></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body"><spring:message code="sponsoredBrandsCampaign.14dayTotalOrders" /></th>
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body"><spring:message code="sponsoredBrandsCampaign.14daytotalUnits" /></th>					
							<th style="width: 10%;" class="text-left" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="sponsoredBrandsCampaign.14dayConversionRateDesc" />"><spring:message code="sponsoredBrandsCampaign.14dayConversionRate" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${hsaCampaignReport.hsaCampaignReportItems}">
							<tr>
								<td class="text-center">${item.reportDate}</td>
								<td class="text-right">${item.impressions}</td>
								<td class="text-right">${item.clicks}</td>
								<td class="text-right">${item.clickThruRate}</td>
								<td class="text-right">${item.costPerClick}</td>
								<td class="text-right">${item.totalSpend}</td>
								<td class="text-right">${item.totalSales14days}</td>
								<td class="text-right">${item.totalOrders14days}</td>
								<td class="text-right">${item.totalUnits14days}</td>
								<td class="text-right">${item.conversionRate14days}</td>
							</tr>						
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td style="padding-left: 28px;"><b><spring:message code="sponsoredBrandsCampaign.total" /></b></td>
							
							<td id="totalImpressions" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalImpressions}</td>
							<td id="totalClicks" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalClicks}</td>
							<td id="totalClickThruRate" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalClickThruRate}</td>
							<td id="totalCostPerClick" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalCostPerClick}</td>
							<td id="totalSpend" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalSpend}</td>
							<td id="totalSales14days" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalSales14days}</td>
							<td id="totalUnits14days" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalUnits14days}</td>
							<td id="totalOrders14days" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalOrders14days}</td>
							<td id="totalConversionRate14days" class="text-right" style="padding: 8px 10px;">${hsaCampaignReport.totalConversionRate14days}</td>
						</tr>
					</tfoot>
				</table>
				</div>
			</div>
		</div>
</div>
</div>