<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>International transactions</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<script>
	$(document).ready(function() {
		$('#internationalTransaction').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});
		$('#internationalTransaction').on("floatThead", function(e, isFloated, $floatContainer) {
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
				<div class="page-heading">International transactions</div>
		</div>
		<div class="col-md-6 text-right">
			<div class="page-heading">
				<a class="btn btn-primary" href="${pageContext.request.contextPath}/InternationalTransaction/create"><i class="fas fa-plus"></i> Add transaction</a>																				
			</div>
		</div>		
	</div>
		<div class="row">
			<div class="col-md-12">
				<div style="padding-bottom: 10px"></div>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/InternationalTransactions?page=1" />
						<c:url var="lastUrl" value="/InternationalTransactions?page=${totalPages}" />
						<c:url var="prevUrl" value="/InternationalTransactions?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/InternationalTransactions?page=${currentPageIndex + 1}" />
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
										<c:url var="pageUrl" value="/InternationalTransactions?page=${i}" />
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
					<table id="internationalTransaction" class="table table-hover table-floated">
						<thead>	
							<tr>
								<th>id</th>
								<th>Date</th>
								<th>MSDC</th>
								<th>SSDC</th>
								<th>Supplier</th>
								<th>Cash flow direction</th>
								<th class="text-center">Currency</th>
								<th class="text-right">Total</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${TransactionList}" var="transaction">
							<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/InternationalTransaction/${transaction.id}'">								
								<td>${transaction.id}</td>								
								<td>${transaction.utcDate}</td>
								<td>${transaction.msdcKcode} ${transaction.msdcName}</td>
								<td>${transaction.ssdcKcode} ${transaction.ssdcName}</td>
								<td>${transaction.splrKcode} ${transaction.splrName}</td>
								<td>${keyToCashFlowDirectionMap[transaction.cashFlowDirectionKey].displayName}</td>
								<td class="text-center">${transaction.currency}</td>
								<td class="text-right">${transaction.total}</td>
								<td class="text-center">
								<c:if test="${transaction.isEditable() eq true}">
									<a style="padding:0px 12px;" class="btn btn-primary" href="${pageContext.request.contextPath}/InternationalTransaction/edit/${transaction.id}">Edit</a>																				
								</c:if>
								</td>
							</tr>					
						</c:forEach>							
						</tbody>
					</table>
					<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/InternationalTransactions?page=1" />
						<c:url var="lastUrl" value="/InternationalTransactions?page=${totalPages}" />
						<c:url var="prevUrl" value="/InternationalTransactions?page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/InternationalTransactions?page=${currentPageIndex + 1}" />
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
										<c:url var="pageUrl" value="/InternationalTransactions?page=${i}" />
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