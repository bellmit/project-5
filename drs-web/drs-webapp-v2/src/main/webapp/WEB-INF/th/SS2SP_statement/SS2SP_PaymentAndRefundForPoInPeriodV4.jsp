<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='${report.title}' /> ${sourcePoId} - DRS</title>

<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<style>
/* table.dataTable {    
    margin-top: 0px !important;
    margin-bottom: 0px !important;
	}
table.dataTable.no-footer {
    border-bottom: 0px solid #111;
}
table.dataTable thead > tr > th.sorting_asc, table.dataTable thead > tr > th.sorting_desc, table.dataTable thead > tr > th.sorting, table.dataTable thead > tr > td.sorting_asc, table.dataTable thead > tr > td.sorting_desc, table.dataTable thead > tr > td.sorting {
    padding-right: 15px;
}
table.fixedHeader-floating {
 top:48px !important;
}
table.dataTable thead th{
	border-bottom: 1px solid #eeeded;
}
.table > tbody > tr > td.dataTables_empty {
	border-top:0px;
} */

table.dataTable tbody td {
	padding: 8px 18px !important;
}
</style>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                                   
         <span><spring:message code='${report.title}' /> ${sourcePoId}</span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class='card-title'><spring:message code='${report.title}' /> ${sourcePoId}</h4>
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					<p class="text-right statement-info">
						<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd}<br>
						<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[report.isurKcode]}<br>
						<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[report.rcvrKcode]}
					</p>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12" style="padding-bottom: 12px;">
					<a class="btn btn-outline-default btn-color btn-inside-table" target="_blank" href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/shipment/${sourcePoId}/export">
						<spring:message code='ss2spStatement.reviewExport' />															    
					</a>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="report" class="table">
						<thead>
							<tr class="border-top border-bottom bg" style="color: #535353;">
								<th></th>
								<th>SKU</th>
								<th><spring:message code='ss2spStatement.skuName' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.itemType' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.amountPerUnit' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.quantity' /></th>
								<th class="text-right" style="padding-right: 31px;"><spring:message code='ss2spStatement.amount' /></th>
							</tr>
						</thead>
						<tbody class="border-b">
							<c:forEach items="${lineItems}" var="item">
								<tr class="padding-reset">
									<td>		
									<a id="collapse${item.sku}${item.settleableItemId}" class="btn btn-outline-default btn-color btn-inside-table">
										<spring:message code='ss2spStatement.expand' />															    
									</a>								
									<script>							
									$(document).ready(function() {
										
										$( "#collapse"+'${item.sku}'+'${item.settleableItemId}').click(function() {
																			  
											if ($("#panel"+'${item.sku}'+'${item.settleableItemId}').css('display') === "table-row") {
												$("#panel"+'${item.sku}'+'${item.settleableItemId}').css('display','none');	
												$( "#collapse"+'${item.sku}'+'${item.settleableItemId}').text("<spring:message code='ss2spStatement.expand' />");											
											} else {
												$("#panel"+'${item.sku}'+'${item.settleableItemId}').css('display','table-row');											    
												$( "#collapse"+'${item.sku}'+'${item.settleableItemId}').text("<spring:message code='ss2spStatement.collapse' />");										       
											}
																															
										});	
																			
										$('#report'+'${item.sku}'+'${item.settleableItemId}').DataTable( {
											searching: false,
											ordering: false,
											paging: false,
											info: false,
											responsive: true,
											fixedHeader: true,
											columnDefs: [{
												"targets": 0,
												"width": "100px"
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
											}],
											dom: 'Bfrtip'
											// scrollY: '50vh',									    
											// scrollCollapse: true
										});
										
									});									
									</script>
									</td>
									<td>${item.sku}</td>
									<td>${item.skuName}</td>
									<td class="text-center"><spring:message code='${item.itemType}' /></td>
									<td class="text-center">${item.unitAmount}</td>
									<td class="text-center">${item.quantity}</td>
									<td class="text-right" style="padding-right: 31px;">
										<c:choose>
											<c:when test="${report.title eq 'ss2spStatement.SellBack'}">
												${item.totalAmount}
											</c:when>
											<c:otherwise>
												${item.totalAmount}	
											</c:otherwise>								
										</c:choose>
									</td>
								</tr>
								<!--  
								<tr id="collapse${item.sku}${item.settleableItemId}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${item.sku}${item.settleableItemId}">
								-->
								<tr style="padding-top: 20px;"></tr>
								<tr id="panel${item.sku}${item.settleableItemId}">	
									<c:set var="id" value="${item.sku}${item.settleableItemId}" />
									<td colspan="7" class="tb-with-space tb-with-bg">
										<div class="table-responsive tb-pb-reset tb-border-around tb-radius" style="overflow: hidden;">																	
											<table id="report${item.sku}${item.settleableItemId}" class="table success">
												<thead id="tableHeader" class="border-bottom border-top">
												<tr class="success bg" style="height: 50px;">
													<th><spring:message code='ss2spStatement.time' /></th>											
													<th><spring:message code='ss2spStatement.SKU' /></th>
													<th><spring:message code='ss2spStatement.skuName' /></th>
													<th><spring:message code='ss2spStatement.itemName' /></th>										
													<th><spring:message code='ss2spStatement.sourceItem' /></th>
													<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
													<th class="text-right"><spring:message code='ss2spStatement.sourceAmount' /></th>
												</tr>
												</thead>
												<tbody class="border-b">
													<c:forEach items="${ss2spSettleableItemReport[id].itemList}" var="disp">
													<tr class="success padding-reset">
														<td>${disp.transactionTimeUtc}</td>										
														<td>${disp.sku}</td>
														<td>${disp.skuName}</td>
														<td><spring:message code="${disp.name}" /></td>
														<td>${disp.sourceName}</td>
														<td class="text-center"><spring:message code='${disp.currency}' /></td>
														<td class="text-right">${disp.amount}</td>
													</tr>
													</c:forEach>
													<tr class="success padding-reset">
														<!-- <td colspan="5"></td> -->
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>												
														<td class="text-center"><b><spring:message code='ss2spStatement.totalAmount' />
															<spring:message code='${ss2spSettleableItemReport[id].currency}' /></b></td>
														<td class="text-right"><b>${ss2spSettleableItemReport[id].total}</b></td>
													</tr>
												</tbody>								
											</table>
										</div>
									</td>
								</tr>
								<script>
									$(document).ready(function() {  
										$("#panel"+'${item.sku}'+'${item.settleableItemId}').css('display','none');																																																					
									});		
								</script>
							</c:forEach>
							<tr style="border-bottom: none !important;">
								<td colspan="5"></td>
								<td class="text-right" style="padding-top: 25px;">
									<strong>
										<spring:message code='ss2spStatement.totalAmount' />
										<spring:message code='${report.currency.name()}' />
									</strong>
								</td>
								<td class="text-right" style="padding-right: 31px; padding-top: 25px;"><strong>${report.total}</strong></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>