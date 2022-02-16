<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="customerSatisfaction.title" /> - DRS
</title>
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/dataTables.bootstrap.min.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>	
<script>
$(function() {	
	
	$( "#supplierSelector" ).select2({
	    theme: "bootstrap"
	});
	if($(window).width() >= 1024){
	$('#customerSatisfaction').floatThead({
		scrollingTop : $("#s5_menu_wrap").height(),
		zIndex : 100
	});
	$('#customerSatisfaction').on("floatThead", function(e, isFloated, $floatContainer) {
		if (isFloated) {
			$floatContainer.addClass("table-floated");
		} else {
			$floatContainer.removeClass("table-floated");
		}
	})};				
});	
</script>
<style>

th, td { 
	min-width: 100px; 
}
.satisfactionGood {
    background: #1155cc;
    width: 10px;
    height: 10px;
   	border-radius: 50%;
   	display: inline-block;
}
.satisfactionOkay {
    background: #38761d;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    display: inline-block;
}
.satisfactionBad {
    background: #ff9900;
   	width: 10px;
    height: 10px;
    border-radius: 50%;
    display: inline-block;
}
.satisfactionUnacceptable {
    background: #cc0000;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    display: inline-block;
}
.satisfactionNA {
    background: #BBBBBB;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    display: inline-block;
}		
</style>
</head>
<div class="max-width" >
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="customerSatisfaction.title" /></div>
			</div>
			<div class="col-md-12">
				<spring:message code="customerSatisfaction.description" /><br><br>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<form class="form-inline" method="POST" action="${pageContext.request.contextPath}/customer-satisfaction">
					<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT_FILTER']}')">
						<c:if test="${not empty supplierKcodeNameMap}">
							<label for="supplierSelector" class="pr-md-2"><spring:message code="campaignPerformance.supplier" /></label>
							<select id="supplierSelector" class="form-control" name="supplierKcode">
								<option value="">--- All ---</option>
								<c:forEach items="${supplierKcodeNameMap}" var="supplierKcodeName">
									<option value="${supplierKcodeName.key}" ${supplierKcodeName.key==supplierKcode?'selected="selected"':''}>
										${supplierKcodeName.key} ${supplierKcodeName.value}
									</option>
								</c:forEach>
							</select>
						</c:if>
					</sec:authorize>
					<label for="mkid" class="px-md-2 pt-2 pt-md-0"> <spring:message code="common.marketplace" /> </label>
					<select class="form-control" id="mkid" name="assignedMarketplaceId">
						<option value="">--- Select ---</option>
						<c:forEach items="${marketplaces}" var="marketplace">
							<option value="${marketplace.key}" ${marketplace.key==assignedMarketplaceId?'selected="selected"':''} >${marketplace.name}</option>
						</c:forEach>
					</select>
					
					<input class="btn btn-primary mx-md-2 mt-2 mt-md-0" type="submit" value="View">
					
				</form>
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
			<div class="table-responsive">
				<table id="customerSatisfaction" class="table table-floated">
					<thead>
						<tr>
							<th class="th-gray" style="width:15%">SKU</th>
							<th class="th-gray" style="width:25%"><spring:message code="customerSatisfaction.productName" /></th>
							<th class="th-gray" style="width:20%"><spring:message code="customerSatisfaction.last1" /><br>${report.last1PeriodStart} ~ ${report.end}</th>
							<th class="th-gray" style="width:20%"><spring:message code="customerSatisfaction.last2" /><br>${report.last2PeriodStart} ~ ${report.end}</th>
							<th class="th-gray" style="width:20%"><spring:message code="customerSatisfaction.last6" /><br>${report.last6PeriodStart} ~ ${report.end}</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${not empty report}">
						<c:forEach var="lineItem" items="${report.lineItems}" >
							<tr>
								<td class="td-gray">${lineItem.productSku}</td>
								<td class="td-gray">${lineItem.productName}</td>
								<td><div class="${statusColorMap[lineItem.lastOnePeriodData.statisticsDescription]}"></div> ${lineItem.lastOnePeriodData.percentage} (${lineItem.lastOnePeriodData.returnQuantity}/${lineItem.lastOnePeriodData.orderQuantity})<br></td>
								<td><div class="${statusColorMap[lineItem.lastTwoPeriodData.statisticsDescription]}"></div> ${lineItem.lastTwoPeriodData.percentage} (${lineItem.lastTwoPeriodData.returnQuantity}/${lineItem.lastTwoPeriodData.orderQuantity})<br></td>
								<td><div class="${statusColorMap[lineItem.lastSixPeriodData.statisticsDescription]}"></div> ${lineItem.lastSixPeriodData.percentage} (${lineItem.lastSixPeriodData.returnQuantity}/${lineItem.lastSixPeriodData.orderQuantity})<br></td>
							</tr>
						</c:forEach>
					</c:if>
					</tbody>
				</table>				  	
			</div>
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
				<b><spring:message code="customerSatisfaction.satisfactionScoreFormula" /></b>
			</div>
			<div style="padding-bottom: 10px"></div>
			<div class="col-md-12">
			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<th style="width:10%"></th>
							<th style="width:40%"><spring:message code="customerSatisfaction.satisfaction" /></th>
							<th style="width:50%"><spring:message code="customerSatisfaction.drsAction" /></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td></td>
							<td><div class="satisfactionGood"></div> 93-100%</td>
							<td><spring:message code="customerSatisfaction.drsActionForGood" /></td>
						</tr>
						<tr>
							<td></td>
							<td><div class="satisfactionOkay"></div> 86-92%</td>
							<td><spring:message code="customerSatisfaction.drsActionForOkay" /></td>
						</tr>
						<tr>
							<td></td>
							<td><div class="satisfactionBad"></div> 81-85%</td>
							<td><spring:message code="customerSatisfaction.drsActionForBad" /></td>
						</tr>
						<tr>
							<td></td>
							<td><div class="satisfactionUnacceptable"></div> 0-80%</td>
							<td><spring:message code="customerSatisfaction.drsActionForUnacceptable" /></td>
						</tr>						
					</tbody>
				</table>
				</div>	 
				</div>			
			</div>
			<small class="col-md-12 text-muted">
				<spring:message code="customerSatisfaction.hint" />
			</small>
</div>