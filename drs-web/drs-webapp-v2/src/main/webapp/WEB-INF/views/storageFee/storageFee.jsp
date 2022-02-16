<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="storageFee.title" /> - DRS
</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script>
$(function() {
	if($(window).width() >= 1024){
	$('#storageFee').floatThead({
		scrollingTop : $("#s5_menu_wrap").height(),
		zIndex : 100
	});
	$('#storageFee').on("floatThead", function(e, isFloated, $floatContainer) {
		if (isFloated) {
			$floatContainer.addClass("table-floated");
		} else {
			$floatContainer.removeClass("table-floated");
		}
	})};
	
	$( "#supplierSelector" ).select2({
	    theme: "bootstrap"
	});		
	$('[data-toggle="tooltip"]').tooltip();
	
	$("#search").click(function() {
		var supplierKcode = $("#supplierSelector").val();
		if (supplierKcode === undefined) {
			supplierKcode = $("#userCode").val();
		}
		var country = $("#currency").val();
		var year = $("#year").val();
		var month = $("#month").val();
		//alert( supplierKcode + country + year + month);
		console.log("${pageContext.request.contextPath}");
		window.location.href = "${pageContext.request.contextPath}/storage-fee/"+ supplierKcode 
			+ "/" + country + "/" + year + "/" + month;
	});
});
</script>
<style>
	.select2-container--default .select2-selection--single {
  border: 1px solid #ccc;
  font-size: 12px;
}		

.select2-container {
    display: block;
}

</style>	
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="storageFee.title" />						
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
			<form action="storage-fee" class="form-inline" method="post">
			    <input type="hidden" id="userCode" value="${supplierKcode}" />
				
				<sec:authorize access="hasAnyRole('${auth['MONTHLY_STOREAGE_FEE_SUPPLIER_SELECTOR']}')">
				<label class="control-label"><spring:message code="storageFee.supplier" /></label>
				<select id="supplierSelector" class="form-control" name="supplierKcode" style="display: inline;">
					<option value="all">--- All ---</option>							
					<c:forEach items="${supplierKcodeToShortEnNameMap}" var="kcodeAndName">
					<option value="${kcodeAndName.key}" ${kcodeAndName.key==supplierKcode?'selected="selected"':''}>
						${kcodeAndName.key} ${kcodeAndName.value}
					</option>
					</c:forEach>
				</select>						
				</sec:authorize>

				<label for="currency" class="mr-2 ml-md-2 mt-2 mt-md-0"><spring:message code="storageFee.country" /></label>
				<select id="currency" class="form-control mr-md-2 pr-4" name="country" >					

					<c:forEach items="${countryList}" var="country">
						<option value="${country}" ${country==countryInput?'selected="selected"':''}>${country}</option>
					</c:forEach>				
				</select>

				
				<label class="mr-2 mt-2 mt-md-0" for="year"><spring:message code="storageFee.year" /></label>
				<select id="year" class="form-control mr-md-2 pr-4" name="year">
					<c:forEach items="${yearList}" var="year">
					<option value="${year}" ${year==yearInput?'selected="selected"':''}>${year}</option>
					</c:forEach>
				</select>

				<label class="mr-2 mt-2 mt-md-0" for="month"><spring:message code="storageFee.month" /></label>
				<select id="month" class="form-control mr-md-2 pr-4" name="month">
					<c:forEach items="${monthList}" var="month">
					<option value="${month}" ${month==monthInput?'selected="selected"':''}>${month}</option>
					</c:forEach>							
				</select>

				<a id="search" class="btn btn-primary mt-2 mt-md-0"><i class="fas fa-search"></i> <spring:message code="storageFee.search" /></a>

			</form>
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
				<label class="control-label enhancement">
					<spring:message code="storageFee.description" />															
				</label>
			</div>
		</div>
		<div style="padding-bottom: 40px"></div>
		<div class="row">
			<div class="col-md-12">
			<div class="table-responsive">
				<table id="storageFee" class="table">
					<thead>	
						<tr>
							<th><spring:message code="storageFee.sku" /></th>
							<th><spring:message code="storageFee.productName" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.itemVolume_hint" />"><spring:message code="storageFee.itemVolume" /></th>
							<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.volumeUnit_hint" />"><spring:message code="storageFee.volumeUnit" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.averageQuantityOnHand_hint" />"><spring:message code="storageFee.averageQuantityOnHand" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.averageQuantityPendingRemoval_hint" />"><spring:message code="storageFee.averageQuantityPendingRemoval" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.estimatedTotalItemVolume_hint" />"><spring:message code="storageFee.estimatedTotalItemVolume" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.monthOfCharge_hint" />"><spring:message code="storageFee.monthOfCharge" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.storageRate_hint" />"><spring:message code="storageFee.storageRate" /></th>
							<th data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.currency_hint" />"><spring:message code="storageFee.currency" /></th>
							<th class="text-right" data-toggle="tooltip" data-placement="top" data-container="body" data-html="true" title="<spring:message code="storageFee.estimatedMonthlyStorageFee_hint" />"><spring:message code="storageFee.estimatedMonthlyStorageFee" /></th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty report.lineItems}">
							<c:forEach var="item" items="${report.lineItems}" >
								<tr>
									<td>${item.sku}</td>
									<td>${item.productName}</td>
									<td class="text-right">${item.itemVolume}</td>
									<td>${item.volumeUnit}</td>
									<td class="text-right">${item.totalAverageQuantityOnHand}</td>
									<td class="text-right">${item.totalAverageQuantityPendingRemoval}</td>
									<td class="text-right">${item.totalEstimatedTotalItemVolume}</td>
									<td class="text-right">${item.monthOfCharge}</td>
									<td class="text-right">${item.storageRate}</td>
									<td>${item.currency}</td>
									<td class="text-right">${item.totalEstimatedMonthlyStorageFee}</td>
								</tr>
							</c:forEach>
								<tr>
									<td colspan="9"></td>
									<td><b><spring:message code="storageFee.total" /></b></td>
									<td class="text-right">${report.sumOfTotalEstimatedMonthlyStorageFee}</td>
								</tr>
						</c:if>					
					</tbody>
				</table>
				</div>
			</div>
		</div>		
	</div>
</div>