<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title><spring:message code="ccc.title" /> - DRS</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		$('#customer-care-case').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});
		$('#customer-care-case').on("floatThead", function(e, isFloated, $floatContainer) {
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
</head>
<div class="max-width">
	<div class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code="ccc.title" />
				</div>
			</div>
			<div class="col-md-6 text-right">
				<div class="page-heading">
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_CREATE']}')">
						<a class="btn btn-primary" href="${pageContext.request.contextPath}/CustomerCareCases/create">
							<i class="fas fa-plus"></i> <spring:message code="ccc.create" />
						</a> 
					</sec:authorize>	
				</div>
			</div>			
		</div>		
		<c:url var="searchAction" value="/CustomerCareCases/search"></c:url>
		<form:form id="customerCaseSearch" action="${searchAction}" name="customerCaseSearch" class="form-horizontal text-left">
			<div class="row">


				<div class="col-md-12 form-inline">
					<input id="searchWords" name="searchWords"
							class="form-control mr-2"
							value="${searchWords}"/>
					<button class="btn btn-primary"
							type="submit"><i class="fas fa-search"></i> <spring:message code='ccc.search' /></button>   
				</div>

			</div>

			<div class="row">
				<div class="col-md-11" id="searchDescription">
					<b><spring:message code="ccc.searchDescription" /></b>
					<br><br>
				</div>
			</div>
		</form:form>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
			<c:choose>
					<c:when test="${totalPages > 1}">
						<c:choose>
							<c:when test="${type eq 'search'}">
								<c:url var="firstUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="1" />
								</c:url>
								<c:url var="lastUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${totalPages}" />
								</c:url>
								<c:url var="prevUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex - 1}" />
								</c:url>
								<c:url var="nextUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex + 1}" />
								</c:url>
							</c:when>
							<c:otherwise>
								<c:url var="firstUrl" value="/CustomerCareCases?page=1" />
								<c:url var="lastUrl" value="/CustomerCareCases?page=${totalPages}" />
								<c:url var="prevUrl" value="/CustomerCareCases?page=${currentPageIndex - 1}" />
								<c:url var="nextUrl" value="/CustomerCareCases?page=${currentPageIndex + 1}" />
							</c:otherwise>
						</c:choose>
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center"> 
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:choose>
											<c:when test="${type eq 'search'}">
												<c:url var="pageUrl" value="/CustomerCareCases/search" >
													<c:param name="searchWords" value="${searchWords}" />
													<c:param name="page" value="${i}" />
												</c:url>
											</c:when>
											<c:otherwise>
												<c:url var="pageUrl" value="/CustomerCareCases?page=${i}" />
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="page-item active"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
				<table id="customer-care-case" class="table table-hover table-floated">
					<thead>
						<tr>
							<th><spring:message code="ccc.id" /></th>
							<th><spring:message code="ccc.customerName" /></th>
							<th><spring:message code="ccc.caseType" /></th>
							<th><spring:message code="ccc.issue" /></th>
							<th><spring:message code="ccc.relatedProduct" /></th>
							<th><spring:message code="common.marketplace" /></th>
							<th><spring:message code="ccc.status" /></th>
							<th><spring:message code="ccc.customerContactTime" /></th>
							<th><spring:message code="ccc.latestActivity" /></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${CustomerCareCaseList}" var="customerCareCase" varStatus="customerCareCaseStatus">
						<c:choose>
							<c:when test="${customerCareCase.status == 'processing'}">
								<tr class="clickableRow" style="font-weight: bold;" onclick="document.location.href='${pageContext.request.contextPath}/CustomerCareCases/${customerCareCase.caseId}'">
							</c:when>
							<c:otherwise>
								<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/CustomerCareCases/${customerCareCase.caseId}'">
							</c:otherwise>
						</c:choose>
							<td>${customerCareCase.caseId}</td>
							<td>${customerCareCase.customerName}</td>
							<td><spring:message code="ccc.${customerCareCase.caseType}" /></td>
							<td>
								<c:forEach items="${customerCareCase.relatedIssueIds}" var="relatedIssueIds" varStatus="status">
									${issueNameMap[relatedIssueIds]}
									<c:if test="${status.count != customerCareCase.relatedIssueIds.size()}">,</c:if>
								</c:forEach>
							</td>
							<td>
								<c:if test="${empty customerCareCase.relatedProductSkuCodeList}">
									<c:forEach items="${customerCareCase.relatedProductBaseCodeList}" var="ProductBase">
										${ProductBase} ${relatedProduct[customerCareCaseStatus.count-1][ProductBase]}<br>
									</c:forEach>
								</c:if>
								<c:if test="${empty customerCareCase.relatedProductBaseCodeList}">
									<c:forEach items="${customerCareCase.relatedProductSkuCodeList}" var="sku">
										${sku} ${relatedProduct[customerCareCaseStatus.count-1][sku]}<br>
									</c:forEach>
								</c:if>
							</td>
							<td>${customerCareCase.marketplace.name}</td>
							<td><spring:message code="ccc.${customerCareCase.status}" /></td>
							<td>${customerCareCase.dateCreated}</td>
							<td>
								<c:choose>
									<c:when test="${customerCareCase.latestActivityDays >= 3}">
										${customerCareCase.latestActivityDays} <spring:message code="ccc.days" />
									</c:when>
									<c:when test="${customerCareCase.latestActivityDays >= 1}">
										${customerCareCase.latestActivityDays}
										<c:choose>
											<c:when test="${customerCareCase.latestActivityDays >= 2}"><spring:message code="ccc.days" /></c:when>
											<c:when test="${customerCareCase.latestActivityDays == 1}"><spring:message code="ccc.day" /></c:when>
										</c:choose>
										${customerCareCase.latestActivityHours}
										<c:choose>
											<c:when test="${customerCareCase.latestActivityHours >= 2}"><spring:message code="ccc.hours" /> </c:when>
											<c:when test="${customerCareCase.latestActivityHours <= 1}"><spring:message code="ccc.hour" /> </c:when>
										</c:choose>
									</c:when>
									<c:when test="${customerCareCase.latestActivityDays == 0}">
										<c:if test="${customerCareCase.latestActivityHours >= 1 }">
											${customerCareCase.latestActivityHours}
											<c:choose>
												<c:when test="${customerCareCase.latestActivityHours >= 2}"><spring:message code="ccc.hours" /></c:when>
												<c:when test="${customerCareCase.latestActivityHours == 1}"><spring:message code="ccc.hour" /></c:when>
											</c:choose>
										</c:if>
										${customerCareCase.latestActivityMinutes}
										<c:choose>
											<c:when test="${customerCareCase.latestActivityMinutes >= 2}"><spring:message code="ccc.minutes" /> </c:when>
											<c:when test="${customerCareCase.latestActivityMinutes <= 1}"><spring:message code="ccc.minute" /> </c:when>
										</c:choose>
									</c:when>
								</c:choose>
								<spring:message code="ccc.ago" />
							</td>
						</c:forEach>
					</tbody>
				</table>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:choose>
							<c:when test="${type eq 'search'}">
								<c:url var="firstUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="1" />
								</c:url>
								<c:url var="lastUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${totalPages}" />
								</c:url>
								<c:url var="prevUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex - 1}" />
								</c:url>
								<c:url var="nextUrl" value="/CustomerCareCases/search" >
									<c:param name="searchWords" value="${searchWords}" />
									<c:param name="page" value="${currentPageIndex + 1}" />
								</c:url>
							</c:when>
							<c:otherwise>
								<c:url var="firstUrl" value="/CustomerCareCases?page=1" />
								<c:url var="lastUrl" value="/CustomerCareCases?page=${totalPages}" />
								<c:url var="prevUrl" value="/CustomerCareCases?page=${currentPageIndex - 1}" />
								<c:url var="nextUrl" value="/CustomerCareCases?page=${currentPageIndex + 1}" />
							</c:otherwise>
						</c:choose>
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center"> 
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:choose>
											<c:when test="${type eq 'search'}">
												<c:url var="pageUrl" value="/CustomerCareCases/search" >
													<c:param name="searchWords" value="${searchWords}" />
													<c:param name="page" value="${i}" />
												</c:url>
											</c:when>
											<c:otherwise>
												<c:url var="pageUrl" value="/CustomerCareCases?page=${i}" />
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="page-item active"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
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