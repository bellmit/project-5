<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<head>

<title><spring:message code='ss2spStatement.statements' /> - DRS</title>

<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>


<script language="javascript" type="text/javascript">

	$(document).ready(function() {

		if($(window).width() >= 1024){
		$('#SS2SP-Statements').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});

		$('#SS2SP-Statements').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		})};
		
		var supplierSelector = $("#supplierSelector").select2({
		    theme: "bootstrap"
		});

        //this is for initial loading
		var supplierKcode = ${supplierKcode};
		supplierSelector.val(supplierKcode).trigger("change")


        //url for page back function
        var pageURL = $(location).attr("href");

        if( (supplierKcode != '') && (pageURL.indexOf("supplierKcode") < 0) ){

            pageURL = pageURL + "?supplierKcode=" + supplierKcode;
            window.history.pushState("", "", pageURL);

        }


	});

</script>

<style>

#search {
	margin: 10px 0 30px 0;
}

</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">

				<div class="page-heading">
					<spring:message code='ss2spStatement.statements' />
				</div>
			</div>
		</div>

		<sec:authorize access="hasAnyRole('${auth['LIST_SS2SP_STATEMENTS_FOR_A_SUPPLIER']}')">
            <c:if test="${not empty supplierKcodeNameMap}">
            <div class="row" id="search">

                <div class="col-md-12">
                <form class="form-inline" method="POST" action="${pageContext.request.contextPath}/statements">

                    <div class="form-group px-2">
                        <label class="px-2" for="supplierSelector">
                            <spring:message code="customerOrder.supplier" />
                        </label>

                        <select id="supplierSelector" class="form-control" name="supplierKcode" >

                            <option value="">All</option>

                            <c:forEach items="${supplierKcodeNameMap}" var="supplierKcodeName">

                                <option value="${supplierKcodeName.key}" ${supplierKcodeName.key == supplierKcode ? 'selected="selected"' :''}>
                                    ${supplierKcodeName.key} ${supplierKcodeName.value}
                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <button class="btn btn-primary" type="submit"><i class="fas fa-filter"></i> Filter</button>

                </form>
                </div>
            </div>
            </c:if>
		</sec:authorize>


		<div class="row">
			<div class="col-md-12">
			<div class="table-responsive">
				<table id="SS2SP-Statements" class="table table-hover table-floated">
					<thead>
						<tr>
							<th class="text-center"><spring:message code='ss2spStatement.periodStart' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.periodEnd' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.statementId' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.total' />(<spring:message code='${report.currency}' />)</th>
							<th class="text-right"><spring:message code='ss2spStatement.stmntTotalAmount' />(<spring:message code='${report.currency}' />)</th>
							<th class="text-right"><spring:message code='ss2spStatement.invoiceFromSsdc' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.invoiceFromSupplier' /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.items }" var="s">
							<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${s.statementId}'">
								<td class="text-center">${s.periodStartUtc}</td>
								<td class="text-center">${s.periodEndUtc}</td>
								<td class="text-center"><a href="${pageContext.request.contextPath}/${rootUrl}/${s.statementId}">${s.statementId}</a></td>
								<td class="text-right">${s.total}</td>
								<td class="text-right">${s.balance}</td>
								<td class="text-right">${fn:replace(s.invoiceFromSsdc,', ','<br/>')}</td>
								<td class="text-right">${fn:replace(s.invoiceFromSupplier,', ','<br/>')}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
			</div>
		</div>
	</div>
</div>