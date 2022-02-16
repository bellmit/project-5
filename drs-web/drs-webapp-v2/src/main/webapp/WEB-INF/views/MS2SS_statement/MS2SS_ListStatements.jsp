<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<head>
<title>Intercompany statements - DRS</title>
<script type='text/javascript'
	src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script language="javascript" type="text/javascript">
	$(document).ready(function() {

		$('#MS2SS-Statements').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});

		$('#MS2SS-Statements').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});

	});
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Intercompany statements ${type}</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="MS2SS-Statements" class="table table-floated">
					<thead>
						<tr>
							<th class="text-center">Period start UTC</th>
							<th class="text-center">Period end UTC</th>
							<th class="text-center">Statement ID</th>
							<th class="text-right">Total(${report.currency})</th>
							<th class="text-right">Balance(${report.currency})</th>
							<sec:authorize access="hasAnyRole('${auth['DEBIT_NOTE_VIEW']}')">
								<th class="text-center">Debit/Credit note</th>
							</sec:authorize>							
							<sec:authorize access="hasAnyRole('${auth['DEBIT_NOTE_DOWNLOAD']}')">
								<th class="text-center">Debit/Credit note download</th>
							</sec:authorize>							
							<sec:authorize access="hasAnyRole('${auth['UNS_RECOGNIZE_REVENUE_REPORT']}')">
								<th class="text-center">UNS RR report</th>
							</sec:authorize>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.items}" var="s">
							<tr>
								<td class="text-center">${s.periodStartUtc}</td>
								<td class="text-center">${s.periodEndUtc}</td>
								<td class="text-center"><a href="${pageContext.request.contextPath}/MS2SS-Statements/${s.statementId}">${s.statementId}</a></td>
								<td class="text-right">${s.total}</td>
								<td class="text-right">${s.balance}</td>
								<sec:authorize access="hasAnyRole('${auth['DEBIT_NOTE_VIEW']}')">
									<td class="text-center">
									<c:if test="${s.total ne '0.00'}">
    								<a href="${pageContext.request.contextPath}/MS2SS-Statements/DebitCreditNote/${s.statementId}" target="_blank">Link</a>
									</c:if>
									</td>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['DEBIT_NOTE_DOWNLOAD']}')">
									<td class="text-center">
    									<a href="${pageContext.request.contextPath}/MS2SS-Statements/DebitCreditNoteReport/${s.statementId}">Download</a>
									</td>
								</sec:authorize>																
								<sec:authorize access="hasAnyRole('${auth['UNS_RECOGNIZE_REVENUE_REPORT']}')">
									<td class="text-center">
    									<a href="${pageContext.request.contextPath}/MS2SS-Statements/revenue-recognize-report/${s.statementId}">Download</a>
									</td>
								</sec:authorize>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>