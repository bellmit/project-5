<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title><spring:message code='profitCostShareStatement.statement' />: ${statementName} - DRS</title>
<script type='text/javascript'
	src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>	
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script type="text/javascript">
	$(document).ready(function() {
		$(document).on('click', '#btnBack',function(){
			document.getElementById('frmBack').submit()
	    });
	});
</script>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    	<div class="breadcrumb">
	        <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
	        <a href="${pageContext.request.contextPath}/ProfitCostShareStatements"><spring:message code="profitCostShareStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>
	        <span>${statement.statementId}</span>  
        </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='profitCostShareStatement.statement' />: ${statementName}
				</div>
			</div>
		</div>
		<c:if test="${not empty successfulUpdate}">
			<div class="row">
				<div class="col-md-12 max-width" >
				<c:choose>
					<c:when test="${successfulUpdate == true}">
						<div class="alert alert-success" role="alert">
			 				 Successfully updated invoice details
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-danger" role="alert">
			 				 An error occurred while updating invoice details
						</div>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</c:if>
		<c:choose>
			<c:when test="${not empty statement.statementId}">
				<div class="row">
					<div class="col-md-12">
						<p class="text-right">
							<spring:message code="profitCostShareStatement.period" />：${statement.periodStartUtc} ~ ${statement.periodEndUtc} <br>
							<spring:message code="profitCostShareStatement.statementId" />：${statement.statementId}<br>
						</p>
					</div>
				</div>
				<div class="row rowframe">
					<div class="col-md-12">
					<sec:authorize access="hasAnyRole('${auth['PROFIT_COST_SHARE_STATEMENTS_EDIT']}')" var="isEditAllowed" />
					<form:form method="POST" modelAttribute="statement" class="form-inline" onsubmit="return onSubmit();">
						<table class="table table-withoutBorder">
						<tbody>
							<tr>
								<td class="text-right" style="width:300px;">
									<label class="control-label"><spring:message code="profitCostShareStatement.invoiceFromSsdc" /></label>
								</td>
								<td>
									<c:choose>
										<c:when test="${isEditAllowed}">
											<form:input id="invoiceFromSsdc" class="form-control width-600" style="display: inline;" path="invoiceFromSsdc" value="${statement.invoiceFromSsdc}"/>
										</c:when>
										<c:otherwise>
											<p style="margin-bottom: 5px;">${statement.invoiceFromSsdc}</p>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td class="text-right" style="width:300px;">
									<label class="control-label"><spring:message code="profitCostShareStatement.invoiceFromSupplier" /></label>
								</td>
								<td>
									<c:choose>
										<c:when test="${isEditAllowed}">
											<form:input id="invoiceFromSupplier" class="form-control width-600" style="display: inline;" path="invoiceFromSupplier" value="${statement.invoiceFromSupplier}"/>
										</c:when>
										<c:otherwise>
											<p style="margin-bottom: 5px;" style="margin-bottom: 5px;">${statement.invoiceFromSupplier}</p>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td class="text-right" style="width:300px;">
									<label class="control-label"><spring:message code="profitCostShareStatement.invoiceNotes" /></label>
								</td>
								<td>
									<c:choose>
										<c:when test="${isEditAllowed}">
											<form:textarea id="invoiceNotes" class="form-control width-600" style="display: inline;" path="invoiceNotes" value="${statement.invoiceNotes}"/>
										</c:when>
										<c:otherwise>
											<span style="white-space: pre-wrap;">${statement.invoiceNotes}</span>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td>
									<form:hidden id="statementId" path="statementId" value="${statement.statementId}"/>
									<form:hidden id="periodStartUtc" path="periodStartUtc" value="${statement.periodStartUtc}"/>
									<form:hidden id="periodEndUtc" path="periodEndUtc" value="${statement.periodEndUtc}"/>
								</td>
								<td class="text-right">
									<span id="btnBack" class="btn btn-success"><spring:message code="profitCostShareStatement.back" /></span>
									<c:choose>
										<c:when test="${isEditAllowed}">
											<input class="btn btn-primary" type="submit" value="<spring:message code="profitCostShareStatement.save" />"/>
										</c:when>
									</c:choose>
								</td>
							</tr>
							</tbody>
						</table>
					</form:form>
					<form id="frmBack" method="POST" class="form-inline" action="${pageContext.request.contextPath}/${rootUrl}">
						<input type="hidden" name="settlementPeriodId" value="${settlementPeriodId}"/>
						<input type="hidden" name="supplierKcode" value="${supplierKcode}"/>
					</form>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="alert alert-danger" role="alert">
	  				<spring:message code="profitCostShareStatement.unableToRetrieveStatement" />
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>