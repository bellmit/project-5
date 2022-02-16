<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title><spring:message code='profitCostShareStatement.statements' /> - DRS</title>
<script type='text/javascript'
	src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>	
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script type="text/javascript">
	$(document).ready(function() {

		$('#Profit-Cost-Share-Statements').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});

		$('#Profit-Cost-Share-Statements').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});

		var supplierSelector =  $('#supplierSelector').select2();
		supplierSelector.val('${supplierKcode}').trigger("change")		
		
	});
</script>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
        <span><spring:message code='profitCostShareStatement.statements' /></span> 
        </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='profitCostShareStatement.statements' />
				</div>
			</div>
		</div>
		<c:if test="${not empty supplierKcodeNameMap}">
		<div class="row">
			<div class="col-md-11">
			<form method="POST" class="form-inline" action="${pageContext.request.contextPath}/ProfitCostShareStatements">
				<label class="control-label"><spring:message code="profitCostShareStatement.period" /></label>
				<select class="form-control" name="settlementPeriodId" style="width:180px; display: inline;">
					<option value="">--- Select ---</option>
					<c:forEach items="${settlementPeriodList}" var="settlementPeriod">
						<option value="${settlementPeriod.id}" ${settlementPeriod.id==settlementPeriodId?'selected="selected"':''}>
							${settlementPeriod.startDate} ~ ${settlementPeriod.endDate}
						</option>
					</c:forEach>
				</select>
				<label class="control-label"><spring:message code="profitCostShareStatement.supplier" /></label>
				<select id="supplierSelector" class="form-control" name="supplierKcode" style="width:25%;">
					<option value="">--- Select ---</option>
					<c:forEach items="${supplierKcodeNameMap}" var="supplierKcodeName">
						<option value="${supplierKcodeName.key}" ${supplierKcodeName.key==supplierKcode?'selected="selected"':''}>
							${supplierKcodeName.key} ${supplierKcodeName.value}
						</option>
					</c:forEach>
				</select>
				<input class="btn btn-primary" type="submit" value="<spring:message code="profitCostShareStatement.search" />">
			</form>
			</div>
			<div class="col-md-1">	
			<c:if test="${not empty report.pcsStatements}">
				<sec:authorize access="hasAnyRole('${auth['PROFIT_COST_SHARE_STATEMENTS_EDIT']}')">
					<form method="GET" class="form-inline" action="${pageContext.request.contextPath}/ProfitCostShareStatements/edit">
						<input type="hidden" name="settlementPeriodId" value="${settlementPeriodId}"/>
						<c:if test="${not empty supplierKcode}">
							<input type="hidden" name="supplierKcode" value="${supplierKcode}"/>
						</c:if>
						<button class="btn btn-success" type="submit"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="profitCostShareStatement.edit" /></button>
					</form>
				</sec:authorize>
			</c:if>
			</div>
		</div>
		</c:if>
		<div class="row">
			<div class="col-md-12">
				<table id="Profit-Cost-Share-Statements" class="table table-hover table-floated">
					<thead>
						<tr>
							<th class="text-center"><spring:message code='profitCostShareStatement.statementId' /></th>
							<th class="text-center"><spring:message code='profitCostShareStatement.amount' /></th>
							<th class="text-center"><spring:message code='profitCostShareStatement.invoiceFromSsdc' /></th>
							<th class="text-center"><spring:message code='profitCostShareStatement.invoiceFromSupplier' /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.pcsStatements }" var="s">
							<tr class="clickableRow ${not empty s.invoiceFromSsdc or not empty s.invoiceFromSupplier ? 'alert-success' : 'alert-danger'}" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${s.statementId}'">
								<td class="text-center"><a href="${pageContext.request.contextPath}/${rootUrlStatements}/${s.statementId}">${s.statementId}</a></td>
								<td class="text-center">${s.amountStr}</td>
								<td class="text-center">${s.invoiceFromSsdc}</td>
								<td class="text-center">${s.invoiceFromSupplier}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>