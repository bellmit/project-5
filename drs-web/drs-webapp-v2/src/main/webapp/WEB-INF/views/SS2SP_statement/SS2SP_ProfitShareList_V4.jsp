<%@ page session="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
<title><spring:message code='ss2spStatement.profitShare' /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">		
<script>
	$(function() {
		$('[data-toggle="tooltip"]').tooltip();
	});						
</script>
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
	table.dataTable thead th {
		border-bottom: 1px solid #eeeded;
	}
	.table > tbody > tr > td.dataTables_empty {
		border-top:0px;
	}
	.dataTables_wrapper.no-footer .dataTables_scrollBody {
		border-bottom: 0px solid #111;
	} */
</style>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
		 <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a>
		 <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
		 <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a>
		 <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
		 <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a>
		 <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <span><spring:message code='ss2spStatement.profitShare' /></span>
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class='card'>
			<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class='card-title'><spring:message code='ss2spStatement.profitShare' /></h4>
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
				<div class="col-md-12">
					<table class="table success">
						<thead>
							<tr class='border-top border-bottom bg' style="color: #535353;">
								<th></th>
								<th class="text-left"><spring:message code='ss2spStatement.sourceCountry' /></th>							
								<th class="text-center"><spring:message code='ss2spStatement.revenueInUsd'/></th>
								<th class="text-center"><spring:message code='ss2spStatement.retainmentRateTier'/></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code='ss2spStatement.noteForEffectiveRetainmentRate' />"><spring:message code='ss2spStatement.effectiveRetainmentRate'/></th>
								<th class="text-center"><spring:message code='ss2spStatement.profitShare' /></th>
								<th class="text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="<spring:message code='ss2spStatement.noteForExchangeRate' />"><spring:message code='ss2spStatement.exchangeRate' /></th>
								<th class="text-right pr-reset-td"><spring:message code='ss2spStatement.amount' /> [<spring:message code="${report.statementCurrency}" />]</th>
							</tr>
						</thead>
						<tbody class="border-b">
							<c:forEach items="${report.lineItems}" var="item">
							<tr class="padding-reset">
								<td style="width: 196px;">
									<a id="collapse${item.sourceCountry}" class="btn btn-outline-default btn-color btn-inside-table"><spring:message code='ss2spStatement.expand' /></a>
									<a class="btn btn-outline-default btn-inside-table" target="_blank" href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/export"><spring:message code='ss2spStatement.reviewExport' /></a>					
									<script>
										$(function () {
											$("#collapse"+'${item.sourceCountry}').click(function() {
												if ($("#panel"+'${item.sourceCountry}').css('display') === "table-row") {											
													$("#panel"+'${item.sourceCountry}').css('display','none');											
													$("#collapse"+'${item.sourceCountry}').text("<spring:message code='ss2spStatement.expand' />");
												} else {								        	
													$("#panel"+'${item.sourceCountry}').css('display','table-row');											    
													$("#collapse"+'${item.sourceCountry}').text("<spring:message code='ss2spStatement.collapse' />");
												}																
											});

											// document.addEventListener("scroll", (e) => {
											// 	// console.log('scroll');
											// 	const $table = $("#report"+"${item.sourceCountry}");
											// 	const os = window.navigator.userAgent;
											// 	if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
											// 		if ($table.offset().top >= 0) {
											// 			$table.floatThead();
											// 		} else {
											// 			$table.floatThead("destroy");
											// 		}
											// 	} else {
											// 		if ($table.offset().top <= 0) {
											// 			$table.floatThead();
											// 		} else {
											// 			$table.floatThead("destroy");
											// 		}
											// 	}
											// }, true);

											$('#report'+'${item.sourceCountry}').DataTable( {
												searching: false,
												ordering: false,
												paging: false,
												info: false,
												// scrollY: '50vh',							    
												// scrollCollapse: true
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
													"width": "150px"
												},{
													"targets": 4,
													"width": "100px"
												},{
													"targets": 5,
													"width": "100px"
												},{
													"targets": 6,
													"width": "120px"
												},{
													"targets": 7,
													"width": "120px"
												}],
												dom: 'Bfrtip',
											});

											$('#report'+'${item.sourceCountry}'+'_filter').hide();
										});
									</script>
								</td>
								<td class="text-left"><spring:message code='${item.sourceCountry}'/></td>							
								<td class="text-center">${item.revenueInUsd}</td>																								
								<td class="text-center">${item.achievedRetainmentRateGrade}</td>
								<td class="text-center">${item.effectiveRetainmentRate}</td>																								
								<td class="text-center">${item.sourceAmount} ${item.sourceCurrency}</td>
								<td class="text-center">${item.exchangeRate}</td>
								<td class="text-right pr-reset-td">${item.statementAmount}</td>								
							</tr>
							<tr style="padding-top: 20px;"></tr>
							<tr id="panel${item.sourceCountry}">
								<td colspan="9" class='tb-with-space tb-with-bg'>
									<div class='table-responsive tb-pb-reset tb-border-around tb-radius' style="overflow: hidden;">
										<table id="report${item.sourceCountry}" class="table success" >
											<thead id='tableHeader' class="border-bottom border-top">
												<tr role="row" class="bg" style="height: 50px;">
													<th style="width: 196px;"><spring:message code='ss2spStatement.relatedSKU' /></th>
													<th style="width: 48px;" class="text-center"><spring:message code='ss2spStatement.sourceCountry' /></th>
													<th style="width: 93px;" class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
													<th style="width: 250px; padding-right: 40px;" class="text-center"><spring:message code='ss2spStatement.skuName' /></th>
													<th style="width: 80px;" class="text-center"><spring:message code='ss2spStatement.shippedQty' /></th>
													<th style="width: 80px;" class="text-center"><spring:message code='ss2spStatement.refundedQty' /></th>
													<th style="width: 250px; padding-right: 40px;" class="text-center"><spring:message code='ss2spStatement.items' /></th>
													<th style="width: 102px;" class="text-right"><spring:message code='ss2spStatement.profitShareAmount' /></th> 
												</tr>
											</thead>
											<tbody class="border-b">
												<c:forEach items="${profitShareDetailReport[item.sourceCountry].skuProfitShareItems}" var="skuItem">
													<tr id="${skuItem.sku}" class="success clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/${skuItem.sku}'">
														<td>${skuItem.sku}</td>
														<td class="text-center"><spring:message code='${item.sourceCountry}' /></td>
														<td class="text-center"><spring:message code='${item.sourceCurrency}' /></td>
														<td>${skuItem.name}</td>
														<td class="text-center">${skuItem.shippedQty}</td>
														<td class="text-center">${skuItem.refundedQty}</td>
														<td class="text-left">SKU <spring:message code='ss2spStatement.profitShare' /></td>
														<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/${skuItem.sku}">${skuItem.subtotal}</a></td>												  	
													</tr>										
												</c:forEach>			
												<c:forEach items="${profitShareDetailReport[item.sourceCountry].detailListOfProductSku}" var="detail">
													<c:forEach items="${detail.itemNameToAmountMap}" var="itemNameAmount" varStatus="status">
													<tr class="success clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/${detail.productSkuCode}/${itemNameAmount.key}'">
														<c:if test="${status.count==1}">
														<td class="text-center" rowspan="${detail.itemNameToAmountMap.size()}">${detail.productSkuCode}</td>
														<td class="text-center" rowspan="${detail.itemNameToAmountMap.size()}">${detail.productSkuName}</td>
														</c:if>
														<td><spring:message code="${itemNameAmount.key}" /></td>
														<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/${detail.productSkuCode}/${itemNameAmount.key}">${itemNameAmount.value}</a></td>
														<c:if test="${status.count==1}">												
															<td rowspan="${detail.itemNameToAmountMap.size()}" class="text-right" >${detail.subtotal}</td>											
														</c:if>
													</tr>
													</c:forEach>										
												</c:forEach>								
												
												<%-- total section --%>
												<c:forEach items="${profitShareDetailReport[item.sourceCountry].otherItemAmounts}" var="itemAmount">											
												<c:choose>											
													<%-- customer care case --%>
													<c:when test="${itemAmount.key == 'MS2SS_PURCHASE_ALWNC_CUSTOMERCARE'}">
														<tr id="customerCareCase" class="success clickableRow" onclick="window.open('${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/${itemAmount.key}')">												
													</c:when>
													
													<%-- monthly storage fee --%>
													<c:when test="${itemAmount.key == 'FBA_MONTHLY_STORAGE'}">
														<tr id="monthlyStorageFee" class="success clickableRow" onclick="window.open('${pageContext.request.contextPath}/storage-fee/${report.rcvrKcode}/${item.sourceCountry}/${monthInfoForMonthStorageFee.split('-')[0]}/${storageFeeMonth}')">												
													</c:when>
													
													<%-- advertising cost --%>
													<c:when test="${itemAmount.key == 'MARKET_SIDE_ADVERTISING_COST'}">
														<tr id="advertisingCost" class="success clickableRow" onclick="window.open('${pageContext.request.contextPath}/statements/${statementName}/profitshare/advertising-cost/${item.sourceCountry}')">												
													</c:when>
													
													<%-- other refund and allowance --%>
													<c:when test="${itemAmount.key == 'OTHER_REFUND_AND_ALLOWANCE_FROM_MARKET_SIDE_TO_CUSTOMER'}">
														<tr id="otherRefundAndAllowance" class="success clickableRow" onclick="window.open('${pageContext.request.contextPath}/statements/${statementName}/profitshare/other-refund-and-allowance/${item.sourceCountry}')">												
													</c:when>
													
													<%-- marketing activity expense --%>
													<%-- MARKET_SIDE_MARKETING_ACTIVITY --%>
													<c:when test="${itemAmount.key == 'MARKET_SIDE_MARKETING_ACTIVITY'}">
														<tr id="marketingActivityExpense" class="success clickableRow" onclick="window.open('${pageContext.request.contextPath}/statements/${statementName}/profitshare/marketing-activity-expense/${item.sourceCountry}')">												
													</c:when>
													
													<c:otherwise>
														<tr class="success"></tr>
													</c:otherwise>
												</c:choose>																				
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td class="text-right"><spring:message code="${itemAmount.key}" /> <c:if test="${itemAmount.key == 'FBA_MONTHLY_STORAGE'}"> (${monthInfoForMonthStorageFee})</c:if></td>
												<td class="text-right">
												<c:choose>
													<c:when test="${itemAmount.key == 'MS2SS_PURCHASE_ALWNC_CUSTOMERCARE'}">
														<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare/${item.sourceCountry}/${itemAmount.key}" target="_blank">${itemAmount.value}</a>
													</c:when>
													<c:when test="${itemAmount.key == 'FBA_MONTHLY_STORAGE'}">
														<a href="${pageContext.request.contextPath}/storage-fee/${report.rcvrKcode}/${item.sourceCountry}/${monthInfoForMonthStorageFee.split('-')[0]}/${storageFeeMonth}" target="_blank">${itemAmount.value}</a>
													</c:when>
													
													<%-- advertising cost --%>
													<c:when test="${itemAmount.key == 'MARKET_SIDE_ADVERTISING_COST'}">
														<a href="${pageContext.request.contextPath}/statements/${statementName}/profitshare/advertising-cost/${item.sourceCountry}" target="_blank">${itemAmount.value}</a>
													</c:when>
													
													<%-- other refund and allowances --%>
													<c:when test="${itemAmount.key == 'OTHER_REFUND_AND_ALLOWANCE_FROM_MARKET_SIDE_TO_CUSTOMER'}">
														<a href="${pageContext.request.contextPath}/statements/${statementName}/profitshare/other-refund-and-allowance/${item.sourceCountry}" target="_blank">${itemAmount.value}</a>
													</c:when>
													
													<%-- marketing activity expense --%>
													<c:when test="${itemAmount.key == 'MARKET_SIDE_MARKETING_ACTIVITY'}">
														<a href="${pageContext.request.contextPath}/statements/${statementName}/profitshare/marketing-activity-expense/${item.sourceCountry}" target="_blank">${itemAmount.value}</a>
													</c:when>
													
													<c:otherwise>
														${itemAmount.value}
													</c:otherwise>
												</c:choose>
												</td>
												<!-- </tr> -->
												</c:forEach>
												<tr class="success">
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<th class="text-right">
														<spring:message code='ss2spStatement.totalSourceAmount' />
														<spring:message code='${profitShareDetailReport[item.sourceCountry].currency.name()}' />
													</th>
													<th class="text-right">${profitShareDetailReport[item.sourceCountry].subTotal}</th>
												</tr>								
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<script>
								$(function() {
									$("#panel"+'${item.sourceCountry}').css('display','none');
									
									function getParameterByName(name, url) {
									    if (!url) {
									      url = window.location.href;
									    }
									    name = name.replace(/[\[\]]/g, "\\$&");
									    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
									        results = regex.exec(url);
									    if (!results) return null;
									    if (!results[2]) return '';
									    return decodeURIComponent(results[2].replace(/\+/g, " "));
									}
									
									var country = getParameterByName('country'); 
									
									if(country != null){
										$("#panel"+country).css('display','table-row');	
										$("#collapse"+country).text("<spring:message code='ss2spStatement.collapse' />");
									}
								})
							</script>
								
							</c:forEach>
							<tr style="border-bottom: none !important;">
								<td colspan="6"></td>
								<td class="text-right" style="padding-top: 15px; padding-bottom: 5px;"><strong><spring:message code='ss2spStatement.subtotal' /></strong></td>
								<td class="text-right pr-reset-td" style="width: 132px;padding-top: 15px; padding-bottom: 5px;""><strong>${report.amountSubTotal}</strong></td>
							</tr>
							<tr style="border-bottom: none !important;">
								<td colspan="6"></td>
								<td class="text-right" style="padding-top: 5px; padding-bottom: 5px;"><strong><spring:message code='ss2spStatement.tax' /></strong></td>
								<td class="text-right pr-reset-td" style="width: 132px;padding-top: 5px; padding-bottom: 5px;""><strong>${report.amountTax}</strong></td>
							</tr>
							<tr style="border-bottom: none !important;">
								<td colspan="6"></td>
								<td class="text-right" style="padding-top: 5px; padding-bottom: 5px;">
									<strong>
										<spring:message code='ss2spStatement.totalStatementAmount' />
										<spring:message code='${report.statementCurrency}' />
									</strong>
								</td>
								<td class="text-right pr-reset-td" style="width: 132px;padding-top: 5px; padding-bottom: 5px;""><strong>${report.amountTotal}</strong></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
