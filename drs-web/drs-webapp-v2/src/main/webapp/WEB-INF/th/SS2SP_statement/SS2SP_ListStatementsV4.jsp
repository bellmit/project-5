<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<head>
<title><spring:message code='ss2spStatement.statements' /> - DRS</title>

<script src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script language="javascript" type='text/javascript'>
	$(document).ready(function() {
		document.addEventListener("scroll", (e) => {
			console.log('scroll');
			const $table = $("#SS2SP-Statements");
			const os = window.navigator.userAgent;
			if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
				if ($table.offset().top >= 0) {
					$table.floatThead();
				} else {
					$table.floatThead("destroy");
				}
				} else {
				if ($table.offset().top <= 0) {
					$table.floatThead();
				} else {
					$table.floatThead("destroy");
				}
			}
		}, true);
		
		var supplierSelector = $("#supplierSelector").select2({
		    theme: "bootstrap"
		});

		//this is for initial loading
		// for future usage: <spring:message code='ss2spStatement.statements' />
		var supplierKcode = ${supplierKcode};
		supplierSelector.val(supplierKcode).trigger("change");

        //url for page back function
        var pageURL = $(location).attr("href");

        if( (supplierKcode != '') && (pageURL.indexOf("supplierKcode") < 0) ) {
            pageURL = pageURL + "?supplierKcode=" + supplierKcode;
            window.history.pushState("", "", pageURL);
        }
	});
</script>
</head>

<div class="max-width">
	<div class="container-fluid">
		<div class='card'>
			<!-- [todo] MOVE TO Nav bar in the future -->
			
			<sec:authorize access="hasAnyRole('${auth['LIST_SS2SP_STATEMENTS_FOR_A_SUPPLIER']}')">
			<c:if test="${not empty supplierKcodeNameMap}">
				
			<div class="row">
				
				<div class='card-body' style="padding-left: 20px;">
					<form class="form-inline" method="POST" action="${pageContext.request.contextPath}/statements/v4">
						<div class='form-inline-wrapper'>
							<label for="supplierSelector" class='mt-2 mt-md-0 mr-md-2'>
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
						<div class='form-inline-wrapper'>
							<button class="btn btn-primary mt-1 mt-md-0 ml-2 searchBtn" type="submit">
								<i class="fas fa-filter"></i> Filter
							</button>
						</div>
					</form>
				</div>
				
			</div>
			</c:if>
			</sec:authorize>
			<div style="padding-bottom: 30px"></div>
			<div class="row">
				<div class="col-md-12">
				<div class="table-responsive" style='overflow: hidden;'>
					<table id="SS2SP-Statements" class="table table-floated">
						<thead id='tableHeader' class='border-top border-bottom'>
							<tr class='bg' stlye='height:50px;'>
								<th class="text-left"><spring:message code='ss2spStatement.periodStart' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.periodEnd' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.statementId' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.total' />(<spring:message code='${report.currency}' />)</th>
								<th class="text-center"><spring:message code='ss2spStatement.stmntTotalAmount' />(<spring:message code='${report.currency}' />)</th>
								<th class="text-center"><spring:message code='ss2spStatement.invoiceFromSsdc' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.invoiceFromSupplier' /></th>
							</tr>
						</thead>
						<tbody class='border-bottom border-b'>
							<c:forEach items="${report.items }" var="s">
								<tr class="clickableRow padding-reset" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${s.statementId}'">
									<td class="text-left">${s.periodStartUtc}</td>
									<td class="text-center">${s.periodEndUtc}</td>
									<td class="text-center"><a href="${pageContext.request.contextPath}/${rootUrl}/${s.statementId}">${s.statementId}</a></td>
									<td class="text-center">${s.total}</td>
									<td class="text-center">${s.balance}</td>
									<td class="text-center">${fn:replace(s.invoiceFromSsdc,', ','<br/>')}</td>
									<td class="text-center">${fn:replace(s.invoiceFromSupplier,', ','<br/>')}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>