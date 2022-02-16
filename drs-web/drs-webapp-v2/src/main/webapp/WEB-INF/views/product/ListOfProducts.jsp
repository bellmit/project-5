<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="header.products" /> - DRS
</title>
<!-- <script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>-->
<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		$('#product').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});

		$('#product').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});
	});
</script>
<style>

</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='header.products' />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-center text-md-right my-2">
					<sec:authorize access="hasAnyRole('${auth['BASE_PRODUCT_CREATE']}')">
						<a class="btn btn-primary" href="${pageContext.request.contextPath}/BaseProduct/create">
							<i class="fas fa-plus"></i> <spring:message code="product.createBaseProduct" />
						</a>
					</sec:authorize>
					 <sec:authorize access="hasAnyRole('${auth['SKU_CREATE']}')">
						<a class="btn btn-default" href="${pageContext.request.contextPath}/SKUs/create">
							<i class="fas fa-plus"></i> <spring:message code="product.createSku" />
						</a>
					</sec:authorize> 
				</div>
			</div>			
		</div>		
		<div class="row">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/Products?page=1" />
						<c:url var="lastUrl" value="/Products?page=${totalPages}" />
						<c:url var="prevUrl" value="/Products?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/Products?page=${currentPageIndex + 1}" />
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item" ><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item" ><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/Products?page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item" ><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item" ><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item" ><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
				<div class="table-responsive">
				<table id="product" class="table table-floated">
					<thead>
						<tr>
							<th><spring:message code="product.drsBaseProductCode" /></th>
							<th><spring:message code="product.drsSkuCode" /></th>
							<th><spring:message code="product.supplierProductName" /></th>
							<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
								<th><spring:message code="product.drsProductName" /></th>
							</sec:authorize>
							<th><spring:message code="product.status" /></th>
							<th><spring:message code="product.nextAction" /></th>
						</tr>
					</thead>
					<c:forEach items="${BaseProductList}" var="baseProduct">
						<c:choose>
							<c:when test="${baseProduct.skuList.size() eq 0}">
								<tr>
									<td>
										<a href="${pageContext.request.contextPath}/BaseProduct/${baseProduct.codeByDrs}">
											${baseProduct.codeByDrs}
										</a>
									</td>
									<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
										<td colspan="4"></td>
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['SKU_DRS_NO_INTERNAL_NAME']}')">
										<td colspan="3"></td>
									</sec:authorize>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${baseProduct.skuList}" var="sku" varStatus="status">
									<c:choose>
										<c:when test="${status.count==1}">
											<tr>
												<td rowspan="${baseProduct.skuList.size()}">
													<a href="${pageContext.request.contextPath}/BaseProduct/${baseProduct.codeByDrs}">
														${baseProduct.codeByDrs}
													</a>
												</td>
												<td><a href="${pageContext.request.contextPath}/SKUs/${sku.codeByDrs}">${sku.codeByDrs}</a></td>
												<td>${sku.nameBySupplier}</td>
												<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
													<td>${sku.nameByDrs}</td>
												</sec:authorize>
												<td><spring:message code="${sku.status}" /></td>
												<td><spring:message code="${sku.status}_NEXT_ACTION" /></td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td>
													<a href="${pageContext.request.contextPath}/SKUs/${sku.codeByDrs}">
														${sku.codeByDrs}
													</a>
												</td>
												<td>${sku.nameBySupplier}</td>
												<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
													<td>${sku.nameByDrs}</td>
												</sec:authorize>
												<td><spring:message code="${sku.status}" /></td>
												<td><spring:message code="${sku.status}_NEXT_ACTION" /></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</table>
				</div>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/Products?page=1" />
						<c:url var="lastUrl" value="/Products?page=${totalPages}" />
						<c:url var="prevUrl" value="/Products?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/Products?page=${currentPageIndex + 1}" />
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item" ><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item" ><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/Products?page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active page-item"><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item" ><a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item" ><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item" ><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
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