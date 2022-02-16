<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<sec:authorize access="hasAnyRole('${auth['HOME_NONE_CUSTOMER_CARE']}')">	
	<title>
		<spring:message code="header.home" /> - DRS
	</title>
</sec:authorize>
<sec:authorize access="hasAnyRole('${auth['HOME_CUSTOMER_CARE']}')">	
	<title>
		<spring:message code="home.title" /> - DRS
	</title>
</sec:authorize>
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script type='text/javascript' src='https://us-west-2b.online.tableau.com/javascripts/api/viz_v1.js'></script>
<script>	
	
	$(function() {			
		if($(window).width() >= 1024){
		$('#statement').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});
		$('#statement').on("floatThead", function(e, isFloated,$floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated"); // the div containing the table containing the thead	        
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});		
		}
		$('[data-toggle="tooltip"]').tooltip();			
		$('#singlePage').prop('checked', true);					
	});
		
	function checkLayout() {
		var val = $('input:radio[name="layout"]:checked').val();
			if (val == "single") {
				$("#tab").hide();
				$("#single").show();
				$("#treemap").hide();
			} else if(val == "tab"){
				$("#single").hide();
				$("#tab").show();
				$("#treemap").hide();
			}else if(val == "treemap"){
				$("#single").hide();
				$("#tab").hide();
				$("#treemap").show();
			}
	}
</script>
<style>
#treemap {
	display:none;
}
@media only screen and (min-width: 1024px) {
 #treemap {
 display: block;
 }
}

</style>
</head>
<div class="max-width">
	<div class="container-fluid">


		<sec:authorize access="hasAnyRole('${auth['HOME_NONE_CUSTOMER_CARE']}')">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code='product.keyProductStats' />
					</div>
				</div>
			</div>		  
			<div class="row">
				<div class="col-md-12">
				<div class="form-check form-check-inline">
					<input id="singlePage" class="form-check-input" type="radio" name="layout" value="single" onclick="checkLayout()" checked>
					<label class="form-check-label" for="singlePage"> <spring:message code='product.singlePage' /></label>
					</div>
					<div class="form-check form-check-inline">
					<input id="tabbedByRegion" class="form-check-input"  type="radio" name="layout" value="tab" onclick="checkLayout()">
					<label class="form-check-label" for="tabbedByRegion"><spring:message code='product.tabbedByRegion' /></label>
					</div>
					<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
					<div class="form-check form-check-inline">
					<input id="tabbedByRegion" class="form-check-input"  type="radio" name="layout" value="treemap" onclick="checkLayout()">
					<label class="form-check-label" for="tabbedByRegion">Treemap (beta - internal)</label>
					</div>
					</sec:authorize>
				</div>
			</div>

			<div style="padding-bottom: 10px"></div>
			<div id="single" class="row">
				<div class="col-md-12">
					<c:if test="${not empty report}">
						<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
							<h3>${statsByCountry.country}</h3>
							<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
								<span style="float: left">
									<spring:message code='product.noteForEstimatedRevenue' />
									${statsByCountry.estimatedSevenDayRevenue}
									${statsByCountry.currency} 
								</span>
							</c:if>
							<script>
							if($(window).width() >= 1024){
							$(document).ready(function() {
								var country = '${statsByCountry.country}';									
								$('#product-vital-stats-'+country).floatThead({
									scrollingTop : $("#s5_menu_wrap").height(),
									zIndex : 100
								});
								$('#product-vital-stats-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
									if (isFloated) {
										$floatContainer.addClass("table-floated");
									} else {
										$floatContainer.removeClass("table-floated");
									}
								});
							});	}					
							</script>
							<span style="float: right">
								<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
							
							</span>
							<div class="table-responsive">
							<table id="product-vital-stats-${statsByCountry.country}" class="table table-floated">
								<thead>
									<tr>
										<th><spring:message code="product.BaseProduct" /></th>
										<th><spring:message code="product.SKU" /></th>
										<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
											<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
										</th>									
										<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
										<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
											<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
												<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
											</th>														
										</sec:authorize>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />">
											<spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)
										</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />">
											<spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)
										</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />">
											<spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)
										</th>						
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
										<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
											<tr>
												<c:if test="${status.count==1}">
													<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
												</c:if>
												<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
												<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
												<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
												<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
												<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
													<td class="text-right">${keyStats.qtyToReceive}</td>
													<td class="text-right">${keyStats.qtyInStock}</td>
												</sec:authorize>
												<td class="text-right">${keyStats.fbaQtyInBound}</td>
												<td class="text-right">${keyStats.fbaQtyInStock}</td>
												<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
											</tr>
										</c:forEach>
									</c:forEach>
									</tbody>
							</table>
							</div>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
				</c:if>
			</div>
		</div>
		<div id="tab" class="row" style="display: none;">
			<div class="col-md-12">
				<ul class="nav nav-tabs mb-2">
					<c:if test="${not empty report}">
						<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
							<c:choose>
							<c:when test="${statsByCountry.country == 'US'}">
								<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#${statsByCountry.country}">${statsByCountry.country}</a></li>
							</c:when>
							</c:choose>
							<c:choose>
							<c:when test="${statsByCountry.country ne 'US'}">
								<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#${statsByCountry.country}">${statsByCountry.country}</a></li>
							</c:when>
							</c:choose>	
						</c:forEach>
					</c:if>
				</ul>
				<div class="tab-content">
					<div id="US" class="tab-pane fade in active">
						<c:if test="${not empty report}">
						<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
						<c:choose>
								<c:when test="${statsByCountry.country == 'US'}">
								<h3>${statsByCountry.country}</h3>
								<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
									<span style="float: left">
										<spring:message code='product.noteForEstimatedRevenue' />
										${statsByCountry.estimatedSevenDayRevenue}
										${statsByCountry.currency} 
									</span>
								</c:if>
						<script>
						if($(window).width() >= 1024){
						$(document).ready(function() {									
							var country = '${statsByCountry.country}';									
							$(window).scroll(function(){										
								$('#product-vital-stats-tab-'+country).floatThead({
									scrollingTop : $("#s5_menu_wrap").height(),
									zIndex : 100
								});										
							});
							$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated, $floatContainer) {
								if (isFloated) {
									$floatContainer.addClass("table-floated");
								} else {
									$floatContainer.removeClass("table-floated");
								}
							});
						});		
						}
						</script>
						<span style="float: right">
							<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
						</span>
						<div class="table-responsive">
						<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
							<thead>
								<tr>
									<th><spring:message code="product.BaseProduct" /></th>
									<th><spring:message code="product.SKU" /></th>
									<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
										<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
									</th>									
									<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
										<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
											<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
										</th>														
									</sec:authorize>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />">
										<spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)
									</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />">
										<spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)
									</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />">
										<spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)
									</th>						
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
								<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
									<tr>
										<c:if test="${status.count==1}">
											<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
										</c:if>
										<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
										<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
										<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
										<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
										<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<td class="text-right">${keyStats.qtyToReceive}</td>
											<td class="text-right">${keyStats.qtyInStock}</td>
										</sec:authorize>
										<td class="text-right">${keyStats.fbaQtyInBound}</td>
										<td class="text-right">${keyStats.fbaQtyInStock}</td>
										<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
									</tr>
								</c:forEach>
							</c:forEach>
							</tbody>
						</table>
						</div>
						</c:when>
						</c:choose>
					</c:forEach>
					
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>
					</div>
					<div id="UK" class="tab-pane fade">
						<c:if test="${not empty report}">
						<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
							<c:choose>
								<c:when test="${statsByCountry.country == 'UK'}">
									<h3>${statsByCountry.country}</h3>
									<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
							<span style="float: left">
								<spring:message code='product.noteForEstimatedRevenue' />
								${statsByCountry.estimatedSevenDayRevenue}
								${statsByCountry.currency} 
							</span>
						</c:if>
						<script>
						if($(window).width() >= 1024){
						$(document).ready(function() {									
							var country = '${statsByCountry.country}';									
							$(window).scroll(function(){										
								$('#product-vital-stats-tab-'+country).floatThead({
									scrollingTop : $("#s5_menu_wrap").height(),
									zIndex : 100
								});										
							});
							$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
								if (isFloated) {
									$floatContainer.addClass("table-floated");
								} else {
									$floatContainer.removeClass("table-floated");
								}
							});
						});		
						}
						</script>
						<span style="float: right">
							<spring:message code='product.lastUpdate' /> ${statsByCountry.lastUpdateDateUTC}
						</span>
						<div class="table-responsive">
						<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
							<thead>
								<tr>
									<th><spring:message code="product.BaseProduct" /></th>
									<th><spring:message code="product.SKU" /></th>
									<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
										<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
									</th>									
									<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
										<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
											<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
										</th>														
									</sec:authorize>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />">
										<spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)
									</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />">
										<spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)
									</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />">
										<spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)
									</th>						
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
									<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
										<tr>
											<c:if test="${status.count==1}">
												<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
											</c:if>
											<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
											<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
											<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
											<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
											<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
												<td class="text-right">${keyStats.qtyToReceive}</td>
												<td class="text-right">${keyStats.qtyInStock}</td>
											</sec:authorize>
											<td class="text-right">${keyStats.fbaQtyInBound}</td>
											<td class="text-right">${keyStats.fbaQtyInStock}</td>
											<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						</div>
						</c:when>
						</c:choose>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>
					</div>
					<div id="CA" class="tab-pane fade">
					<c:if test="${not empty report}">
					<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
						<c:choose>
							<c:when test="${statsByCountry.country == 'CA'}">
								<h3>${statsByCountry.country}</h3>
							<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
							<span style="float: left">
								<spring:message code='product.noteForEstimatedRevenue' />
								${statsByCountry.estimatedSevenDayRevenue}
								${statsByCountry.currency} 
							</span>
							</c:if>
						<script>
						if($(window).width() >= 1024){
						$(document).ready( function() {									
							var country = '${statsByCountry.country}';									
							$(window).scroll(function(){										
								$('#product-vital-stats-tab-'+country).floatThead({
									scrollingTop : $("#s5_menu_wrap").height(),
									zIndex : 100
								});										
							});
							$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
								if (isFloated) {
									$floatContainer.addClass("table-floated");
								} else {
									$floatContainer.removeClass("table-floated");
								}
							});
						});		
						}
						</script>
						<span style="float: right">
							<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
						</span>
						<div class="table-responsive">
						<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
							<thead>
								<tr>
									<th><spring:message code="product.BaseProduct" /></th>
									<th><spring:message code="product.SKU" /></th>
									<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
										<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
									</th>									
									<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
										<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
											<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
										</th>														
									</sec:authorize>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />">
										<spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)
									</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />">
										<spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)
									</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />">
										<spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)
									</th>						
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
									<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
										<tr>
											<c:if test="${status.count==1}">
												<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
											</c:if>
											<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
											<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
											<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
											<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
											<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
												<td class="text-right">${keyStats.qtyToReceive}</td>
												<td class="text-right">${keyStats.qtyInStock}</td>
											</sec:authorize>
											<td class="text-right">${keyStats.fbaQtyInBound}</td>
											<td class="text-right">${keyStats.fbaQtyInStock}</td>
											<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						</div>
						</c:when>
						</c:choose>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>					
					</div>
					<div id="DE" class="tab-pane fade">
					<c:if test="${not empty report}">
					<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
						<c:choose>
							<c:when test="${statsByCountry.country == 'DE'}">
							<h3>${statsByCountry.country}</h3>
							<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
								<span style="float: left">
									<spring:message code='product.noteForEstimatedRevenue' />
									${statsByCountry.estimatedSevenDayRevenue}
									${statsByCountry.currency} 
								</span>
							</c:if>
						<script>
						if($(window).width() >= 1024){
						$(document).ready( function() {									
							var country = '${statsByCountry.country}';									
							$(window).scroll(function(){										
								$('#product-vital-stats-tab-'+country).floatThead({
									scrollingTop : $("#s5_menu_wrap").height(),
									zIndex : 100
								});										
							});
							$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
								if (isFloated) {
									$floatContainer.addClass("table-floated");
								} else {
									$floatContainer.removeClass("table-floated");
								}
							});
						});	
						}
						</script>
						<span style="float: right">
							<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
						</span>
						<div class="table-responsive">
						<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
							<thead>
								<tr>
									<th><spring:message code="product.BaseProduct" /></th>
									<th><spring:message code="product.SKU" /></th>
									<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
										<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
									</th>									
									<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
									<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
										<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
									</th>														
									</sec:authorize>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />"><spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />"><spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)</th>
									<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />"><spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)</th>						
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
									<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
										<tr>
											<c:if test="${status.count==1}">
												<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
											</c:if>
											<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
											<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
											<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
											<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
											<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
												<td class="text-right">${keyStats.qtyToReceive}</td>
												<td class="text-right">${keyStats.qtyInStock}</td>
											</sec:authorize>
											<td class="text-right">${keyStats.fbaQtyInBound}</td>
											<td class="text-right">${keyStats.fbaQtyInStock}</td>
											<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						</div>
						</c:when>
						</c:choose>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>
					</div>
					<div id="FR" class="tab-pane fade">
					<c:if test="${not empty report}">
					<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
						<c:choose>
							<c:when test="${statsByCountry.country == 'FR'}">
								<h3>${statsByCountry.country}</h3>
								<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
									<span style="float: left">
										<spring:message code='product.noteForEstimatedRevenue' />
										${statsByCountry.estimatedSevenDayRevenue}
										${statsByCountry.currency} 
									</span>
								</c:if>
							<script>
							if($(window).width() >= 1024){
							$(document).ready(function() {									
								var country = '${statsByCountry.country}';									
								$(window).scroll(function(){										
									$('#product-vital-stats-tab-'+country).floatThead({
										scrollingTop : $("#s5_menu_wrap").height(),
										zIndex : 100
									});										
								});
								$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
									if (isFloated) {
										$floatContainer.addClass("table-floated");
									} else {
										$floatContainer.removeClass("table-floated");
									}
								});
							});		
							}
							</script>
							<span style="float: right">
								<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
							</span>
							<div class="table-responsive">
							<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
								<thead>
									<tr>
										<th><spring:message code="product.BaseProduct" /></th>
										<th><spring:message code="product.SKU" /></th>
										<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
											<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
										</th>									
										<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
										<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
											<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
												<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
											</th>														
										</sec:authorize>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />"><spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />"><spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />"><spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)</th>						
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
									<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
										<tr>
											<c:if test="${status.count==1}">
												<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
											</c:if>
											<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
											<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
											<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
											<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
											<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
												<td class="text-right">${keyStats.qtyToReceive}</td>
												<td class="text-right">${keyStats.qtyInStock}</td>
											</sec:authorize>
											<td class="text-right">${keyStats.fbaQtyInBound}</td>
											<td class="text-right">${keyStats.fbaQtyInStock}</td>
											<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						</div>
						</c:when>
						</c:choose>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>
					</div>
					<div id="IT" class="tab-pane fade">
					<c:if test="${not empty report}">
					<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
						<c:choose>
							<c:when test="${statsByCountry.country == 'IT'}">
								<h3>${statsByCountry.country}</h3>
								<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
									<span style="float: left">
										<spring:message code='product.noteForEstimatedRevenue' />
										${statsByCountry.estimatedSevenDayRevenue}
										${statsByCountry.currency} 
									</span>
								</c:if>
							<script>
							if($(window).width() >= 1024){
							$(document).ready( function() {									
								var country = '${statsByCountry.country}';									
								$(window).scroll(function(){										
									$('#product-vital-stats-tab-'+country).floatThead({
										scrollingTop : $("#s5_menu_wrap").height(),
										zIndex : 100
									});
										
								});
								$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
									if (isFloated) {
										$floatContainer.addClass("table-floated");
									} else {
										$floatContainer.removeClass("table-floated");
									}
								});
							});		
							}
							</script>
							<span style="float: right">
								<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
							</span>
							<div class="table-responsive">
							<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
								<thead>
									<tr>
										<th><spring:message code="product.BaseProduct" /></th>
										<th><spring:message code="product.SKU" /></th>
										<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
											<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
										</th>									
										<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
										<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
											<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
												<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
											</th>														
										</sec:authorize>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />">
											<spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)
										</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />">
											<spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)
										</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />">
											<spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)
										</th>						
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
									<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
										<tr>
											<c:if test="${status.count==1}">
												<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
											</c:if>
											<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
											<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
											<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
											<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
											<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
												<td class="text-right">${keyStats.qtyToReceive}</td>
												<td class="text-right">${keyStats.qtyInStock}</td>
											</sec:authorize>
											<td class="text-right">${keyStats.fbaQtyInBound}</td>
											<td class="text-right">${keyStats.fbaQtyInStock}</td>
											<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
										</tr>
									</c:forEach>
								</c:forEach>
								</tbody>
							</table>
							</div>
						</c:when>
						</c:choose>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>
					</div>
					<div id="ES" class="tab-pane fade">
					<c:if test="${not empty report}">
					<c:forEach items="${report.keyProductStatsByCountryList}" var="statsByCountry">
						<c:choose>
							<c:when test="${statsByCountry.country == 'ES'}">
								<h3>${statsByCountry.country}</h3>
								<c:if test="${not empty statsByCountry.estimatedSevenDayRevenue}">
									<span style="float: left">
										<spring:message code='product.noteForEstimatedRevenue' />
										${statsByCountry.estimatedSevenDayRevenue}
										${statsByCountry.currency} 
									</span>
								</c:if>
							<script>
							if($(window).width() >= 1024){
							$(document).ready(function() {									
								var country = '${statsByCountry.country}';									
								$(window).scroll(function(){										
									$('#product-vital-stats-tab-'+country).floatThead({
										scrollingTop : $("#s5_menu_wrap").height(),
										zIndex : 100
									});										
								});
								$('#product-vital-stats-tab-'+country).on("floatThead", function(e, isFloated,$floatContainer) {
									if (isFloated) {
										$floatContainer.addClass("table-floated");
									} else {
										$floatContainer.removeClass("table-floated");
									}
								});
							});		
							}
							</script>
							<span style="float: right">
								<spring:message code='product.lastUpdate' />${statsByCountry.lastUpdateDateUTC}
							</span>
							<div class="table-responsive">
							<table id="product-vital-stats-tab-${statsByCountry.country}" class="table table-floated">
								<thead>
									<tr>
										<th><spring:message code="product.BaseProduct" /></th>
										<th><spring:message code="product.SKU" /></th>
										<th class="text-right"><spring:message code="product.currentBaseRetailPrice" /> (${statsByCountry.currency})</th>									
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForStatementPeriod" arguments="${report.nextSettlementPeriod}" /> ">
											<spring:message code="product.qtyOrderedInThisSettlementPeriod" /> (<spring:message code="product.unit" />)
										</th>									
										<th class="text-right"><spring:message code="product.qtyOrderedInLastSevenDays" /> (<spring:message code="product.unit" />)</th>
										<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<th class="text-right"><spring:message code="product.qtyToReceive" /> (<spring:message code="product.unit" />)</th>
											<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForQtyInStock" />">
												<spring:message code="product.sellableQty" /> (<spring:message code="product.unit" />)
											</th>														
										</sec:authorize>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInbound" />">
											<spring:message code="product.fbaInbound" /> (<spring:message code="product.unit" />)
										</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaInStock" />">
											<spring:message code="product.fbaInStock" /> (<spring:message code="product.unit" />)
										</th>
										<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="product.noteForFbaTransfer" />">
											<spring:message code="product.fbaTransfer" /> (<spring:message code="product.unit" />)
										</th>						
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${statsByCountry.baseToKeySkuStatsListMap}" var="entry">
									<c:forEach items="${entry.value}" var="keyStats" varStatus="status">
										<tr>
											<c:if test="${status.count==1}">
												<td rowspan="${entry.value.size()}"><a href="${pageContext.request.contextPath}/BaseProduct/${keyStats.baseCodeByDrs}">${entry.key}</a></td>
											</c:if>
											<td><a href="${pageContext.request.contextPath}/SKUs/${keyStats.skuCodeByDrs}">${keyStats.skuCode}</a></td>
											<td class="text-right">${keyStats.currentBaseRetailPrice}</td>											
											<td class="text-right">${keyStats.qtyOrderedInCurrentSettlementPeriod}</td>											
											<td class="text-right">${keyStats.qtyOrderedInLastSevenDays}</td>
											<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
												<td class="text-right">${keyStats.qtyToReceive}</td>
												<td class="text-right">${keyStats.qtyInStock}</td>
											</sec:authorize>
											<td class="text-right">${keyStats.fbaQtyInBound}</td>
											<td class="text-right">${keyStats.fbaQtyInStock}</td>
											<td class="text-right">${keyStats.fbaQtyTransfer}</td>		
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						</div>
						</c:when>
						</c:choose>
					</c:forEach>
					<p>
						<spring:message code="product.notes" />
						<spring:message code='product.noteForRef' />
					</p>
					</c:if>
					</div>												
				</div>
			</div>
		</div>
	
	   <div id="treemap" class="row">
			<div class="col-md-12">
			<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
			<div class='tableauPlaceholder' style='width: 1260px; height: 2827px;'><object class='tableauViz' width='1260' height='2827' style='display:none;'><param name='host_url' value='https%3A%2F%2Fus-west-2b.online.tableau.com%2F' /> <param name='embed_code_version' value='3' /> <param name='site_root' value='&#47;t&#47;drs' /><param name='name' value='KeyStatsDashboardMaster&#47;Dashboard1' /><param name='tabs' value='no' /><param name='toolbar' value='yes' /><param name='showAppBanner' value='false' /><param name='filter' value='iframeSizedToWindow=true' /></object></div>
			</sec:authorize>
			</div>
		</div>
		
		
		<c:if test="${not empty statementListReport}">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code='header.ss2spStatements' />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive">
					<table id="statement" class="table table-floated">
						<thead>
							<tr>
								<th class="text-center"><spring:message code='ss2spStatement.periodStart' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.periodEnd' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.statementId' /></th>
								<th class="text-right"><spring:message code='ss2spStatement.total' />(<spring:message code='${statementListReport.currency}' />)</th>
								<th class="text-right"><spring:message code='ss2spStatement.stmntTotalAmount' />(<spring:message code='${statementListReport.currency}' />)</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${statementListReport.items}" var="s">
								<tr>
									<td class="text-center">${s.periodStartUtc}</td>
									<td class="text-center">${s.periodEndUtc}</td>
									<td class="text-center">
										<a href="${pageContext.request.contextPath}/${statementRootUrl}/${s.statementId }">${s.statementId }</a>
									</td>
									<td class="text-right">${s.total}</td>
									<td class="text-right">${s.balance}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
				</div>
			</div>
			<div class="text-right">
				<a class="btn btn-link" href="${pageContext.request.contextPath}/statements">more</a>
			</div>
		</c:if>
		</sec:authorize>
		<sec:authorize access="hasAnyRole('${auth['HOME_CUSTOMER_CARE']}')">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='home.title' />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p><spring:message code='home.content1' /></p>
				<br><br>
				<p><spring:message code='home.content2' /></p>
				<p><spring:message code='home.content3' /></p>				
			</div>
		</div>		
		</sec:authorize>		
	</div>
</div>		