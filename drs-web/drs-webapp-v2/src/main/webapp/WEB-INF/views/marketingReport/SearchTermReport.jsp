<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
	<title><spring:message code="marketingReport.searchTermReport" /> - DRS
</title>	
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<link href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css"/>" type="text/css" rel="stylesheet">		
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">	
<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">	
<script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script>	
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>	
<script src="https://cdn.datatables.net/plug-ins/1.10.16/sorting/absolute.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.2.2/css/responsive.bootstrap4.min.css">
<script src="https://cdn.datatables.net/responsive/2.2.2/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.2/js/responsive.bootstrap4.min.js"></script>
<script>

var numbersType = $.fn.dataTable.absoluteOrderNumber( [
	{ value: 'N/A', position: 'bottom' }
] );
$(document).ready(function() {
	$('[data-toggle="tooltip"]').tooltip();			
	var report = $('#searchTermReport').DataTable({
		searching: true,
		paging: true,
		lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
		info: false,
		responsive: true,
		fixedHeader: true,
		language: {
		      "emptyTable": "<spring:message code='searchTermReport.noData' />"
		},		
		drawCallback: function() {
	        var pageInfo = this.api().page.info();
			$('.total-displayed').text((pageInfo.start+1) + ' - ' + pageInfo.end + ' of ' + pageInfo.recordsDisplay);
	    },
		dom: "<'row'<'col-sm-1'><'col-sm-2'l><'col-sm-2 total-displayed'><'col-sm-6'p>>" +
		"<'row'<'col-sm-12'tr>>" +
		"<'row'<'col-sm-5'i><'col-sm-6'p>>",
		buttons: [{
			extend: 'csv',
		    text: '<i class="fas fa-cloud-download-alt"></i> <spring:message code="searchTermReport.export" />'
		}],
		columnDefs: [ {
				"targets": [0,1,2,3,4,5,6,7,8,9,10,11,12,13],
		    	"visible": false
		    },{
		    	"targets":0,
		    	"width":"80px"
		    },{
		        "targets":1,
		        "width":"90px"
		    },{
		       	"targets":2,
		        "width":"70px"
		    },{
		       	"targets":3,
		       	"width":"70px"
		    },{
		        "targets":4,
		       	"width":"70px"
		    },{
		       	"targets":5,
		       	"width":"70px"
		    },{
		       	"targets":6,
		        "width":"70px"
		    },{
		       	"targets":7,
		        "width":"70px"
		    },{
	    		"type":numbersType,
		        "targets":8,
		       	"width":"70px"
		    },{
		        "targets":9,
		        "width":"70px"
		    },{
	    		"type":numbersType,
		    	"targets":10,
		        "width":"80px"
		    },{
		       	"targets":11,
		       	"width":"80px"
		    },{
		       	"targets":12,
		       	"width":"80px"
		    },{
	    		"type":numbersType,
		       	"targets":13,
		        "width":"80px"
		    }]
	});			
	$('#searchTermReport_filter').hide();			
	report.column(0).visible(true);
	report.column(1).visible(true);
	report.column(2).visible(true);
	report.column(6).visible(true);
	report.column(7).visible(true);
	report.column(8).visible(true);
	report.column(9).visible(true);
	report.column(10).visible(true);
	report.column(11).visible(true);
	report.column(12).visible(true);
	report.column(13).visible(true);			
	report.buttons().container().appendTo($('#btnExport'));
	$('#btnExport > .dt-buttons > a').addClass('btn btn-default');			
	$('input[type="checkbox"]').click(function(){					
		for(i=1;i<14;i++){
			report.column(i).visible(false);
		}
		$('input[type="checkbox"]').each(function(){
			report.column($(this).val()).visible($(this).prop('checked'));
		});
	});			
	$('input[type="checkbox"]').each(function(){
		$(this).prop('checked',(($(this).val()!=3)&&($(this).val()!=4)&&($(this).val()!=5)));
	});
	
	var campaignNameList = ${campaignNameList};
	
	$.each(campaignNameList,function(index,value){
		var c = '<span class="btn btn-pill btn-default">'+value+'</span>&nbsp;';				
		$('#campaignList').append(c);
	});
	
	if(campaignNameList.length>0){
		$('span.btn-pill').eq(0).removeClass('btn-default').addClass('btn-success');	
	}
	
	$('span.btn-pill').click(function(){
		$('span.btn-pill').removeClass('btn-success').addClass('btn-default');
		$(this).removeClass('btn-default').addClass('btn-success');
			
		var supplierKcode = '${supplierKcode}';
		var assignedMarketplace = '${assignedMarketplace}';		
		var campaignName = $(this).text();
		
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/SearchTermReport/getReportByCampaignName',
			contentType : "application/json; charset=utf-8",
			data : {
				supplierKcode : supplierKcode,
				assignedMarketplace : assignedMarketplace,
				campaignName : campaignName										
			},
			dataType : "json",
			success : function(data) {

				var campaignReport = data;																								
				report.rows().remove().draw();																																									
				var items = campaignReport.lineItems					
													
				$('#totalSpendDescCurrency').text("("+items[0].currency+")");
				$('#averageCpcDescCurrency').text("("+items[0].currency+")");
				$('#productSalesWithinOneWeekOfAClickDescCurrency').text("("+items[0].currency+")");
								
				var itemArray = new Array();
				
				items.forEach(function(item){
					
					itemArray.push(		                    
		                    [
		                    item.adGroupName,
		                    item.customerSearchTerm,
		                    item.keyword,
		                    item.matchType,
		                    item.firstDayOfImpression,
		                    item.lastDayOfImpression,
		                    item.impressions,
		                    item.clicks,
		                    item.ctr,
		                    item.totalSpend,
		                    item.averageCpc,
		                    item.ordersPlacedWithinOneWeekOfAClick,
		                    item.productSalesWithinOneWeekOfAClick,
		                    item.acos]		                    
		            );
																
				});
								
				report.rows.add(itemArray).draw();				
				$('#searchTermReport tbody tr').find('td:eq(0), td:eq(1), td:eq(2), td:eq(3), td:eq(4), td:eq(5)').addClass('text-center');								
				$('#searchTermReport tbody tr').find('td:eq(6), td:eq(7), td:eq(8), td:eq(9), td:eq(10), td:eq(11), td:eq(12), td:eq(13)').addClass('text-right');
								
			}															
		});
			
	});
			
	$('#supplierSelector').select2();
	$('.dataTables_empty').attr('colspan',11);

});
</script>
<style>

</style>
</head>
<div class="max-width">
	<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading"><spring:message code="marketingReport.searchTermReport" /></div>
				</div>						
			</div>
			<div class="row">
				<div class="col-md-12">
				<form method="POST" action="${pageContext.request.contextPath}/SearchTermReport">
				<div class="form-inline">
					<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT_FILTER']}')">
						<c:if test="${not empty supplierKcodeNameMap}">
							<label class="mt-2 mt-md-0 mr-md-2"><spring:message code="campaignPerformance.supplier" /></label>
							<select id="supplierSelector" class="form-control" name="supplierKcode" >
								<option value="">--- Select ---</option>
								<c:forEach items="${supplierKcodeNameMap}" var="supplierKcodeName">
									<option value="${supplierKcodeName.key}" ${supplierKcodeName.key==supplierKcode?'selected="selected"':''}>
										${supplierKcodeName.key} ${supplierKcodeName.value}
									</option>
								</c:forEach>
							</select>
						</c:if>
					</sec:authorize>
					<label class="mt-2 mt-md-0 mr-md-2 ml-md-2"><spring:message code="searchTermReport.marketPlace" /></label>
					<select class="form-control" name="assignedMarketplace">
						<option value="">--- Select ---</option>
						<c:forEach items="${marketplaces}" var="marketplace">
							<option value="${marketplace.name()}" ${marketplace.name()==assignedMarketplace?'selected="selected"':''} >
								${marketplace.name}
							</option>
						</c:forEach>
					</select>
					<input class="btn btn-primary mt-2 mt-md-0 ml-md-2" type="submit" value="Search">
					</div>
				</form>
				</div>
			</div>						
		<div class="row">
			<div class="col-md-12 my-1">
				<label class="control-label enhancement">
					<spring:message code="searchTermReport.description" />
				</label>
			</div>
		</div>		
		<div class="row">
			<div class="col-md-12 mb-1" id="campaignList">
				<label class="control-label"><i class="fas fa-tags"></i> <spring:message code="searchTermReport.campaignName" />: </span>&nbsp;&nbsp;</label>
			</div>
		</div>				
		<div class="row">
			<div class="col-md-12 text-right mb-1">
				<button type="button" class="btn btn-default" style="margin-left:10px;float:right;" data-toggle="collapse" data-target="#ConfigColumns" aria-expanded="false" ><i class="fas fa-th-list"></i> <spring:message code="searchTermReport.configColumns" /></button>	
				<span id="btnExport" style="float:right;"></span>
			</div>
		</div>

		<div class="row">	
			<div class="col-md-5">
			</div>
			<div class="col-md-7">
				<div id="ConfigColumns" class="collapse" style="border-top:1px dotted #333; border-bottom:1px dotted #333;padding:5px 5px 10px 5px;margin-top:5px;"">
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="adGroupName" style="display: inline;" value="0"  id="adGroupName"  checked />
						<label for="adGroupName" ><spring:message code="searchTermReport.adGroupName" /></label>
					</span>									
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="customerSearchTerm" style="display: inline;" value="1"  id="customerSearchTerm"  checked />
						<label for="customerSearchTerm" ><spring:message code="searchTermReport.customerSearchTerm" /></label>
					</span>																								
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="keyword" style="display: inline;" value="2"  id="keyword"  checked />
						<label for="keyword" ><spring:message code="searchTermReport.keyword" /></label>
					</span>																	
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="matchType" style="display: inline;" value="3"  id="matchType" />
						<label for="matchType" ><spring:message code="searchTermReport.matchType" /></label>
					</span>	
					<br>
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="firstDayOfImpression" style="display: inline;" value="4"  id="firstDayOfImpression" />
						<label for="firstDayOfImpression" ><spring:message code="searchTermReport.firstDayOfImpression" /></label>
					</span>									
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="lastDayOfImpression" style="display: inline;" value="5"  id="lastDayOfImpression" />
						<label for="lastDayOfImpression" ><spring:message code="searchTermReport.lastDayOfImpression" /></label>
					</span>																									
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="impressions" style="display: inline;" value="6"  id="impressions"  checked />
						<label for="impressions" ><spring:message code="searchTermReport.impressions" /></label>
					</span>									
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="clicks" style="display: inline;" value="7"  id="clicks" checked  />
						<label for="clicks" ><spring:message code="searchTermReport.clicks" /></label>
					</span>		
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="ctr" style="display: inline;" value="8"  id="ctr"  checked />
						<label for="ctr" ><spring:message code="searchTermReport.ctr" /></label>
					</span>	
					<br>
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="totalSpend" style="display: inline;" value="9"  id="totalSpend" checked  />
						<label for="totalSpend" ><spring:message code="searchTermReport.totalSpend" /></label>
					</span>		
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="averageCpc" style="display: inline;" value="10"  id="averageCpc" checked  />
						<label for="averageCpc" ><spring:message code="searchTermReport.averageCpc" /></label>
					</span>	
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="ordersPlacedWithinOneWeekOfAClick" style="display: inline;" value="11"  id="ordersPlacedWithinOneWeekOfAClick"  checked />
						<label for="ordersPlacedWithinOneWeekOfAClick" ><spring:message code="searchTermReport.ordersPlacedWithinOneWeekOfAClick" /></label>
					</span>								
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="productSalesWithinOneWeekOfAClick" style="display: inline;" value="12"  id="productSalesWithinOneWeekOfAClick" checked  />
						<label for="productSalesWithinOneWeekOfAClick" ><spring:message code="searchTermReport.productSalesWithinOneWeekOfAClick" /></label>
					</span>								
					<span class="checkbox checkbox-success checkbox-inline" style="padding-top:15px;">
						<input type="checkbox" name="acos" style="display: inline;" value="13"  id="acos"  checked />
						<label for="acos" ><spring:message code="searchTermReport.acos" /></label>
					</span>

				</div>
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row text-center" style="">
			<div class="col-md-12">			 
				<div class="search-table-outter wrapper">			
					<table id="searchTermReport" class="table" style="border-spacing:0;diaplay:inline-block; width:100%">
						<thead>
							<tr>						
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.adGroupNameDesc" />"><spring:message code="searchTermReport.adGroupName" /></th>
								<th class="text-center"><spring:message code="searchTermReport.customerSearchTerm" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.keywordDesc" />"><spring:message code="searchTermReport.keyword" /></th>					
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.matchTypeDesc" />"><spring:message code="searchTermReport.matchType" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.firstDayOfImpressionDesc" />"><spring:message code="searchTermReport.firstDayOfImpression" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.lastDayOfImpressionDesc" />"><spring:message code="searchTermReport.lastDayOfImpression" /></th>														
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.impressionsDesc" />"><spring:message code="searchTermReport.impressions" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.clicksDesc" />"><spring:message code="searchTermReport.clicks" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.ctrDesc" />"><spring:message code="searchTermReport.ctr" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.totalSpendDesc" />"><spring:message code="searchTermReport.totalSpend" /> <span id="totalSpendDescCurrency">(${report.lineItems[0].currency})</span></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.averageCpcDesc" />"><spring:message code="searchTermReport.averageCpc" /> <span id="averageCpcDescCurrency">(${report.lineItems[0].currency})</span></th>						
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.ordersPlacedWithinOneWeekOfAClickDesc" />"><spring:message code="searchTermReport.ordersPlacedWithinOneWeekOfAClick" /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.productSalesWithinOneWeekOfAClickDesc" />"><spring:message code="searchTermReport.productSalesWithinOneWeekOfAClick" /> <span id="productSalesWithinOneWeekOfAClickDescCurrency">(${report.lineItems[0].currency})</span></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code="searchTermReport.acosDesc" />"><spring:message code="searchTermReport.acos" /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${not empty report}">
							<c:forEach items="${report.lineItems}" var="lineItem">
								<tr>
									<td class="text-center">${lineItem.adGroupName}</td>
									<td class="text-center">${lineItem.customerSearchTerm}</td>
									<td class="text-center">${lineItem.keyword}</td>
									<td class="text-center">${lineItem.matchType}</td>
							    	<td class="text-center">${lineItem.firstDayOfImpression}</td>
							    	<td class="text-center">${lineItem.lastDayOfImpression}</td>
									<td class="text-right">${lineItem.impressions}</td>
									<td class="text-right">${lineItem.clicks}</td>
									<td class="text-right">${lineItem.ctr}</td>
									<td class="text-right">${lineItem.totalSpend}</td>
									<td class="text-right">${lineItem.averageCpc}</td>														
									<td class="text-right">${lineItem.ordersPlacedWithinOneWeekOfAClick}</td>
									<td class="text-right">${lineItem.productSalesWithinOneWeekOfAClick}</td>
									<td class="text-right">${lineItem.acos}</td>
								</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>			  	
				</div>			
			</div>
		</div>
		</div>
	</div>