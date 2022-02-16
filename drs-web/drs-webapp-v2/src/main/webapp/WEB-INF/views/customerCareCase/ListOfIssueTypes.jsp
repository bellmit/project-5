<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<header>
	<title><spring:message code='issue.issueType' /> - DRS</title>
</header>
<div class="max-width">
	<div class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code='issue.issueType' />
				</div>
			</div>
			<div class="col-md-6 text-right">
				<div class="page-heading">
					<a class="btn btn-primary"
						href="${pageContext.request.contextPath}/Issues/createIssueType">
						<spring:message code='issue.createIssueType' />
					</a>
				</div>
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th><spring:message code='issue.issueCategory' /></th>
							<th><spring:message code='issue.issueType' /></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${IssueCategoryToIssueTypeAndIdMap}"
							var="IssueCategoryToIssueType">
							<c:forEach items="${IssueCategoryToIssueType.value}"
								var="IssueType">
								<tr>
									<td><spring:message
												code='issue.${categoryIdToNameMap[IssueCategoryToIssueType.key]}' /></td>
									<td>${IssueType.value}</td>
									<td><a
										class="btn btn-default" href="${pageContext.request.contextPath}/Issues/editIssueType?categoryId=${IssueCategoryToIssueType.key}&typeId=${IssueType.key}"><spring:message
												code='issue.edit' /></a></td>
								</tr>
							</c:forEach>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>