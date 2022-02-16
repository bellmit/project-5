<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="header.remittance" /> - DRS
</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		if($(window).width() >= 1024) {
			$('#Remittance').floatThead({
				scrollingTop : $("#s5_menu_wrap").height(),
				zIndex : 100
			});
			$('#Remittance').on("floatThead", function(e, isFloated, $floatContainer) {
				if (isFloated) {
					$floatContainer.addClass("table-floated");
				} else {
					$floatContainer.removeClass("table-floated");
				}
			});
		}
		$("#sender,#receiver").select2({ theme: "bootstrap"});

			$(".custom-file-input").on("change", function() {

              var fileName = $(this).val().split("\\").pop();
              $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
            });
	});


</script>

<style>
.importButton {
	float: right;
}
</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code="header.remittance" />
				</div>
			</div>
			<div class="col-md-6 text-right">
				<div class="page-heading">
				<sec:authorize access="hasAnyRole('${auth['REMITTANCE_CREATE']}')">
					<a class="btn btn-primary" href="${pageContext.request.contextPath}/Remittance/create"> 
						<spring:message code='remittance.create' />
					</a>
				</sec:authorize>	
				</div>
			</div>			
		</div>
		<div class="row mb-3">
			<div class="col-md-6">
			</div>
			<div class="col-md-6 text-right">
				<div class="importButton">
				<form accept-charset="UTF-8" method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/Remittance/import">
					<table class="no-head">
						<tr>
							<td>
							 <div class="custom-file"">
                                                <input type="file" class="custom-file-input" name="file">
                                                <label class="custom-file-label" for="customFile"></label>
                                              </div>
							</td>
							<td><input class="btn btn-primary" type="submit" value="Import" ></td>
						</tr>
					</table>
				</form>
				</div>


			</div>
		</div>
		<c:url var="action" value="/Remittance"></c:url>
		<form:form class="form-inline mb-3"  action="${action}" method="GET" name="RemittanceListSearch"  modelAttribute="RemittanceListSearchCondition">
				<label class="mr-2" for="sender"><spring:message code='remittance.sender' /></label>
				<form:select id="sender" path="sndrKcode" class="form-control">
					<form:option value="">All</form:option>
					<c:forEach var="SenderAndReceiverCompany" items="${SenderAndReceiverCompanies}">
						<form:option value="${SenderAndReceiverCompany.key}" label="${SenderAndReceiverCompany.key} ${SenderAndReceiverCompany.value}" />
					</c:forEach>				
				</form:select>
				<label class="mx-2" for="reciever"><spring:message code='remittance.receiver' /></label>
				<form:select id="receiver" path="rcvrKcode" class="form-control">
					<form:option value="">All</form:option>
					<c:forEach var="SenderAndReceiverCompany" items="${SenderAndReceiverCompanies}">
						<form:option value="${SenderAndReceiverCompany.key}" label="${SenderAndReceiverCompany.key} ${SenderAndReceiverCompany.value}" />
					</c:forEach>				
				</form:select>
				<input class="btn btn-primary ml-2" type="submit" value="<spring:message code="remittance.search" />" >
		</form:form>
		<div class="row">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url value="Remittance" var="URL">
							<c:forEach var="RemittanceListSearchCondition" items="${RemittanceListSearchConditions}">
								<c:param name="${RemittanceListSearchCondition.key}" value="${RemittanceListSearchCondition.value}" />
							</c:forEach>
						</c:url>
						<c:url var="firstUrl" value="${URL}&page=1" />
						<c:url var="lastUrl" value="${URL}&page=${totalPages}" />
						<c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
						<div class="text-center">
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}" data-pager="${currentPageIndex - 1}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="${URL}&page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}" data-pager="${currentPageIndex + 1}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}" data-pager="${totalPages}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</div>
					</c:when>
				</c:choose>
				<table id="Remittance" class="table table-floated">
					<thead>
						<tr>
							<th class="text-center"><spring:message code='remittance.id' /></th>
							<th><spring:message code='remittance.dateSent' /></th>
							<th><spring:message code='remittance.dateReceived' /></th>
							<th><spring:message code='remittance.sender' /></th>
							<th><spring:message code='remittance.receiver' /></th>
							<th class="text-center"><spring:message code='remittance.currency' /></th>
							<th class="text-right"><spring:message code='remittance.amount' /></th>
							<th><spring:message code='remittance.reference' /></th>
							<sec:authorize access="hasAnyRole('${auth['REMITTANCE_EDIT']}')">
								<th></th>
							</sec:authorize>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${RemittanceList}" var="remittance">
							<tr>
								<td class="text-center"><a href="${pageContext.request.contextPath}/Remittance/${remittance.remittanceId}">${remittance.remittanceId}</a></td>
								<td>${remittance.utcDateSent}</td>
								<td>${remittance.utcDateReceived}</td>
								<td>${remittance.sender} ${SenderAndReceiverCompanies[remittance.sender]}</td>
								<td>${remittance.receiver} ${SenderAndReceiverCompanies[remittance.receiver]}</td>
								<td class="text-center">${remittance.currency}</td>
								<td class="text-right">${remittance.amount}</td>
								<td>${remittance.reference}</td>
								<sec:authorize access="hasAnyRole('${auth['REMITTANCE_EDIT']}')">
									<td class="text-center">
										<a href="${pageContext.request.contextPath}/Remittance/${remittance.remittanceId}/edit">
											<spring:message code='remittance.edit' var='edit' /> 
											<input class="btn btn-default btn-sm" style="padding:1px 12px;" type="button" value="${edit}" />
										</a>
									</td>
								</sec:authorize>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url value="Remittance" var="URL">
							<c:forEach var="RemittanceListSearchCondition" items="${RemittanceListSearchConditions}">
								<c:param name="${RemittanceListSearchCondition.key}" value="${RemittanceListSearchCondition.value}" />
							</c:forEach>
						</c:url>
						<c:url var="firstUrl" value="${URL}&page=1" />
						<c:url var="lastUrl" value="${URL}&page=${totalPages}" />
						<c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
						<div class="text-center">
						<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}" data-pager="${currentPageIndex - 1}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="${URL}&page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}" data-pager="${currentPageIndex + 1}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}" data-pager="${totalPages}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</div>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</div>