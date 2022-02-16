<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code="header.companies" /> - DRS</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		$('#company').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});s
		$('#company').on("floatThead", function(e, isFloated, $floatContainer) {
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
	<div class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code='header.companies' />
				</div>
			</div>
			
		</div>
		<div class="row">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/Companies?page=1" />
						<c:url var="lastUrl" value="/Companies?page=${totalPages}" />
						<c:url var="prevUrl" value="/Companies?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/Companies?page=${currentPageIndex + 1}" />
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li  class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
                                            <li  class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/Companies?page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li  class="page-item active"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li  class="page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li  class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
                                            <li  class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
				<div style="padding-bottom: 10px"></div>
				<table id="company" class="table table-floated">
					<thead>
						<tr>
							<th><spring:message code='company.title' /></th>
							<th>SKU</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${CompanyList}" var="company">
							<c:choose>
								<c:when test="${company.skus.size() eq 0}">
									<tr>
										<td>
											<a href="${pageContext.request.contextPath}/Companies/${company.kcode}">
												${company.kcode} ${company.shortNameLocal}
											</a>
										</td>
										<td></td>
										<td>
											<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/Companies/${company.kcode}/edit">
												<spring:message code='company.edit' />
											</a>
										</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${company.skus}" var="sku" varStatus="status">
										<c:choose>
											<c:when test="${status.count==1}">
												<tr>
													<td rowspan="${company.skus.size()}">
														<a href="${pageContext.request.contextPath}/Companies/${company.kcode}">
															${company.kcode} ${company.shortNameLocal}
														</a>
													</td>
													<td>
														<a href="${pageContext.request.contextPath}/SKUs/${sku.codeByDrs}">
															${sku.codeByDrs} ${sku.nameByDrs}
														</a>
													</td>
													<td rowspan="${company.skus.size()}">
														<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/Companies/${company.kcode}/edit">
															<spring:message code='company.edit' />
														</a>
													</td>
												</tr>
											</c:when>
											<c:otherwise>
												<tr>
													<td>
														<a href="${pageContext.request.contextPath}/SKUs/${sku.codeByDrs}">
															${sku.codeByDrs} ${sku.nameByDrs}
														</a>
													</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</tbody>
				</table>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/Companies?page=1" />
						<c:url var="lastUrl" value="/Companies?page=${totalPages}" />
						<c:url var="prevUrl" value="/Companies?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/Companies?page=${currentPageIndex + 1}" />
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li  class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
                                            <li  class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/Companies?page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li  class="page-item active"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li  class="page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li  class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
                                            <li  class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
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