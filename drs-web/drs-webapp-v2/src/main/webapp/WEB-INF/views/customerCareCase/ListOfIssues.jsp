<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code='issue.maintainIssues' /> - DRS
</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>	
	$(document).ready(function() {				
		$('#issue').floatThead({
			scrollingTop:$("#s5_menu_wrap").height(),zIndex:100
		});
		$('#issue').on("floatThead", function(e, isFloated,$floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});
	});
	
</script>
<style>
#searchDescription {
	 color: #46a046;
	 padding-top: 10px;
	 font-size: 14px;
}
</style>	
</header>
<div class="max-width">
	<div class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid" ng-app="issueList"
		ng-controller="issueListCtrl">
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code='issue.maintainIssues' />
				</div>
			</div>
			<div class="col-md-6 text-right">
				<div class="page-heading">
					<sec:authorize access="hasAnyRole('${auth['ISSUES_ISSUE_TYPE']}')">
						<a class="btn btn-default"
							href="${pageContext.request.contextPath}/Issues/IssueTypes">
							<spring:message code='issue.maintainIssueType' />
						</a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
						<a class="btn btn-primary"
							href="${pageContext.request.contextPath}/Issues/create">
							<i class="fas fa-plus"></i>
							<spring:message code='issue.createIssue' />
							</a>
					</sec:authorize>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 form-inline">
				<c:url var="apply" value="/Issues/search"></c:url>
				<form:form id="IssueApply" action="${apply}" name="IssueApply"
					class="form-horizontal text-left">
					<input id="searchWords" name="searchWords" class="form-control"
						value="${searchWords}" />
					<button class="btn btn-primary" type="submit">
						<i class="fas fa-search"></i>
						<spring:message code='issue.search' />
					</button>
				</form:form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-11" id="searchDescription">
				<b><spring:message code="issue.searchDescription" /></b> <br>
				<br>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="text-right">
					<spring:message code='issue.dateTimeNowUtc'
						arguments="${dateTimeNowUtc}" />
				</div>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:choose>
							<c:when test="${type eq 'search'}">
								<c:url var="firstUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="1" />
								</c:url>
								<c:url var="lastUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${totalPages}" />
								</c:url>
								<c:url var="prevUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex - 1}" />
								</c:url>
								<c:url var="nextUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex + 1}" />
								</c:url>
							</c:when>
							<c:otherwise>
								<c:url var="firstUrl" value="/Issues?page=1" />
								<c:url var="lastUrl" value="/Issues?page=${totalPages}" />
								<c:url var="prevUrl"
									value="/Issues?page=${currentPageIndex - 1}" />
								<c:url var="nextUrl"
									value="/Issues?page=${currentPageIndex + 1}" />
							</c:otherwise>
						</c:choose>
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link"
												href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link"
												href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:choose>
											<c:when test="${type eq 'search'}">
												<c:url var="pageUrl" value="/Issues/search">
													<c:param name="searchWords" value="${searchWords}" />
													<c:param name="page" value="${i}" />
												</c:url>
											</c:when>
											<c:otherwise>
												<c:url var="pageUrl" value="/Issues?page=${i}" />
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="page-item active"><a class="page-link"
													href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link"
													href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link"
												href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link"
												href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
				<table id="issue" class="table table-hover table-floated">
					<thead>
						<tr>
							<th><spring:message code='issue.issueType' /></th>
							<th><spring:message code='issue.issueName' /></th>
							<sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
								<th><spring:message code='issue.supplier' /></th>
							</sec:authorize>
							<th><spring:message code='issue.relatedProducts' /></th>
							<th><spring:message code='issue.status' /></th>
							<th class="text-right"><spring:message
									code='issue.templateOccurrences' /></th>
							<th class="text-center"><spring:message
									code='issue.latestActivity' /></th>
							<sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
								<th></th>
							</sec:authorize>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${issueList}" var="issue"
							varStatus="issueStatus">
							<sec:authorize
								access="hasAnyRole('${auth['ISSUES_LIST_VIEW_DRS_STAFF']}')">
								<c:choose>
									<c:when test="${issue.status == 'PENDING_DRS_ACTION'}">
										<tr class="clickableRow" style="font-weight: bold;"
											onclick="document.location.href='${pageContext.request.contextPath}/Issues/${issue.id}'">
									</c:when>
									<c:otherwise>
										<tr class="clickableRow"
											onclick="document.location.href='${pageContext.request.contextPath}/Issues/${issue.id}'">
									</c:otherwise>
								</c:choose>
							</sec:authorize>
							<sec:authorize
								access="hasAnyRole('${auth['ISSUES_LIST_VIEW_SUPPLIER']}')">
								<c:choose>
									<c:when test="${issue.status == 'PENDING_SUPPLIER_ACTION'}">
										<tr class="clickableRow" style="font-weight: bold;"
											onclick="document.location.href='${pageContext.request.contextPath}/Issues/${issue.id}'">
									</c:when>
									<c:otherwise>
										<tr class="clickableRow"
											onclick="document.location.href='${pageContext.request.contextPath}/Issues/${issue.id}'">
									</c:otherwise>
								</c:choose>
							</sec:authorize>
							<td>${issue.issueTypeName}</td>
							<td>${issue.name}</td>
							<sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
								<td><c:choose>
										<c:when test="${!empty issue.supplierKcode}">
									${issue.supplierKcode} ${supplierKcodeToShortEnUsNameMap[issue.supplierKcode]}							
								</c:when>
										<c:otherwise>
											<spring:message code="issue.anyForSupplier" />
										</c:otherwise>
									</c:choose></td>
							</sec:authorize>
							<td><c:if test="${empty issue.relatedProductSkuCodeList}">
									<c:forEach items="${issue.relatedProductBaseCodeList}"
										var="ProductBase">
										${ProductBase} ${relatedProduct[issueStatus.count-1][ProductBase]}<br>
									</c:forEach>
								</c:if> <c:if test="${empty issue.relatedProductBaseCodeList}">
									<c:forEach items="${issue.relatedProductSkuCodeList}" var="sku">
										${sku} ${relatedProduct[issueStatus.count-1][sku]}<br>
									</c:forEach>

								</c:if>
							</td>
							<td><spring:message code="issue.${issue.status}"/></td>
							<td class="text-right">
							<a href="${pageContext.request.contextPath}/CustomerCareCases/search?searchWords=issueId:${issue.id}" >
								${issue.templateOccurrences}
							</a>
							</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${issue.daysFromLastUpdate >= 3}">
										${issue.daysFromLastUpdate} <spring:message code="ccc.days" />
										<spring:message code="ccc.ago" />
									</c:when>
									<c:otherwise>
										<c:if test="${issue.daysFromLastUpdate ne '0'}">
											<c:choose>
												<c:when test="${issue.daysFromLastUpdate > 1}">
													${issue.daysFromLastUpdate} <spring:message code="ccc.days" />
												</c:when>
												<c:when test="${issue.daysFromLastUpdate eq 1}">
													${issue.daysFromLastUpdate} <spring:message code="ccc.day" />
												</c:when>
											</c:choose>
											<c:choose>
												<c:when test="${issue.hoursFromLastUpdate > 1}">
													${issue.hoursFromLastUpdate} <spring:message
														code="ccc.hours" />
													<spring:message code="ccc.ago" />
												</c:when>
												<c:when test="${issue.hoursFromLastUpdate <= 1}">
													${issue.hoursFromLastUpdate} <spring:message
														code="ccc.hour" />
													<spring:message code="ccc.ago" />
												</c:when>
											</c:choose>
										</c:if>
										<c:if test="${issue.daysFromLastUpdate eq '0'}">
											<c:choose>
												<c:when test="${issue.hoursFromLastUpdate > 1}">
													${issue.hoursFromLastUpdate} <spring:message
														code="ccc.hours" />
												</c:when>
												<c:when test="${issue.hoursFromLastUpdate eq 1}">
													${issue.hoursFromLastUpdate} <spring:message
														code="ccc.hour" />
												</c:when>
											</c:choose>
											<c:choose>
												<c:when test="${issue.minsFromLastUpdate > 1}">
													${issue.minsFromLastUpdate} <spring:message
														code="ccc.minutes" />
													<spring:message code="ccc.ago" />
												</c:when>
												<c:when test="${issue.minsFromLastUpdate <= 1}">
													${issue.minsFromLastUpdate} <spring:message
														code="ccc.minute" />
													<spring:message code="ccc.ago" />
												</c:when>
											</c:choose>
										</c:if>
									</c:otherwise>
								</c:choose></td>
							<sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
								<td><a class="btn btn-default"
									href="${pageContext.request.contextPath}/Issues/${issue.id}/edit">
										<spring:message code='issue.edit' />
								</a></td>
							</sec:authorize>
						</c:forEach>
					</tbody>
				</table>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:choose>
							<c:when test="${type eq 'search'}">
								<c:url var="firstUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="1" />
								</c:url>
								<c:url var="lastUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${totalPages}" />
								</c:url>
								<c:url var="prevUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex - 1}" />
								</c:url>
								<c:url var="nextUrl" value="/Issues/search">
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex + 1}" />
								</c:url>
							</c:when>
							<c:otherwise>
								<c:url var="firstUrl" value="/Issues?page=1" />
								<c:url var="lastUrl" value="/Issues?page=${totalPages}" />
								<c:url var="prevUrl"
									value="/Issues?page=${currentPageIndex - 1}" />
								<c:url var="nextUrl"
									value="/Issues?page=${currentPageIndex + 1}" />
							</c:otherwise>
						</c:choose>
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link"
												href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link"
												href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:choose>
											<c:when test="${type eq 'search'}">
												<c:url var="pageUrl" value="/Issues/search">
													<c:param name="searchWords" value="${searchWords}" />
													<c:param name="page" value="${i}" />
												</c:url>
											</c:when>
											<c:otherwise>
												<c:url var="pageUrl" value="/Issues?page=${i}" />
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="page-item active"><a class="page-link"
													href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link"
													href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link"
												href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link"
												href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</div>