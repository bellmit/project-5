<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<head>
<title><c:choose>
		<c:when test="${type eq 'Create'}">
			<spring:message code='issue.createIssueType' /> - DRS
							<c:url var="action" value="/Issues/saveIssueType"></c:url>
		</c:when>
		<c:otherwise>
			<spring:message code='issue.issueType' /> - DRS
							<c:url var="action" value="/Issues/updateIssueType"></c:url>
		</c:otherwise>
	</c:choose></title>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	var app = angular.module('issueType', []);
	app.controller('issueTypeCtrl', function($scope) {
		
		$scope.type = '${type}';
				
		var currentURL = document.URL;
		
		if(currentURL.indexOf("edit") > -1) $scope.name = '${name}';
						
	});
</script>
</head>
<div class="max-width">
	<div class="container-fluid" ng-app="issueType"
		ng-controller="issueTypeCtrl">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
						<c:when test="${type eq 'Create'}">
							<spring:message code='issue.createIssueType' />
							<c:url var="action" value="/Issues/saveIssueType"></c:url>
						</c:when>
						<c:otherwise>
							<spring:message code='issue.issueType' />
							<c:url var="action" value="/Issues/updateIssueType"></c:url>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6 offset-md-3">
				<form:form action="${action}" name="IssueType"
					class="form-horizontal text-left"
					modelAttribute="IssueType">
					
					<div class="form-group">
					<label><spring:message code='issue.issueCategory' /></label>
					<form:select class="form-control" path="categoryId" ng-disabled="type == 'Edit'">
									<c:forEach items="${categoryIdToNameMap}"
										var="categoryIdToName">
										<form:option value="${categoryIdToName.key}"><spring:message
												code="issue.${categoryIdToName.value}" /></form:option>
									</c:forEach>
							</form:select>
					</div>
					
					<div class="form-group">
					<label><spring:message code='issue.issueType' /><span class="text-danger">*</span></label>
					<form:hidden path="typeId" />
							    <form:input class="form-control"
									path="name" ng-model="name"
									required="required" /> 
								<small class="text-danger"
									ng-show="IssueType.name.$error.required && IssueType.name.$dirty">
									<spring:message code='issue.issueType_req' />
								</small>
					</div>
					<div>
					<input class="btn btn-primary" type="submit"
								ng-disabled='IssueType.$invalid'
								value="<spring:message code='issue.submit'/>"
								style="float: right" />
					</div>
					
				</form:form>
			</div>
		</div>
	</div>
</div>