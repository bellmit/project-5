<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
			<div class="col-md-10">
				<c:if test="${not empty supplierKcode}">
					<label class="control-label"><spring:message code="profitCostShareStatement.supplier" /></label>
					<label class="control-label">${supplierKcode} ~ ${supplierKcodeName}</label>
				</c:if>
			</div>
			<c:if test="${not empty report}">
				<div class="col-md-1">
					<form method="POST" class="form-inline" action="${pageContext.request.contextPath}/ProfitCostShareStatements">
						<input type="hidden" name="settlementPeriodId" value="${settlementPeriodId}"/>
						<input type="hidden" name="supplierKcode" value="${supplierKcode}"/>
						<input class="btn btn-danger" type="submit" value="<spring:message code="profitCostShareStatement.cancel" />">
					</form>
				</div>
				<div class="col-md-1">
					<input type="submit" class="btn btn-success" form="updatePcsItems"
							value="<spring:message code="profitCostShareStatement.save" />" />
				</div>
			</c:if>
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
					<form:form method="POST" id="updatePcsItems" modelAttribute="report" class="form-inline" onsubmit="return onSubmit();">
						<c:forEach items="${report.profitCostShareList}" var="s" varStatus="vs">
							<tr class="${not empty s.invoiceFromSsdc or not empty s.invoiceFromSupplier ? 'alert-success' : 'alert-danger'}">
								<form:hidden id="statementId" path="profitCostShareList[${vs.index}].statementId"/>
								<td class="text-center"><a href="${pageContext.request.contextPath}/${rootUrlStatements}/${s.statementId}">${s.statementId}</a></td>
								<td class="text-center">${s.amountStr}</td>
								<td class="text-center"><form:input path="profitCostShareList[${vs.index}].invoiceFromSsdc"/></td>
								<td class="text-center"><form:input path="profitCostShareList[${vs.index}].invoiceFromSupplier"/></td>
							</tr>
						</c:forEach>
					</form:form>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>