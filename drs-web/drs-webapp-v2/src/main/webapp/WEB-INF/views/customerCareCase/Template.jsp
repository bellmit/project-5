<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<title>
	<c:choose>
		<c:when test="${type eq 'Create'}">
			<spring:message code="template.createResponseTemplate"/>: ${Template.issueName} - DRS
		</c:when>
		<c:otherwise>
			<spring:message code="template.editResponse"/>: ${Template.issueName} - DRS
		</c:otherwise>
	</c:choose>
</title>
<script type='text/javascript' src="<c:url value="/resources/js/Countable.js"/>"></script>	
<link href="<c:url value="/resources/css/bootstrap3-wysihtml5.css"/>" type="text/css" rel="stylesheet">	
<script type='text/javascript' src="<c:url value="/resources/js/bootstrap3-wysihtml5.all.js"/>"></script>	
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	angular.module('template', []).controller('templateCtrl',function($scope) {
						
		$scope.checkedAllApplicableCaseTypes = function() {				
			$(':checkbox.applicableCaseTypes').prop('checked', true);   			
		};
			
		$scope.checkedAllApplicableMarketRegions = function() {			
			 $(':checkbox.applicableMarketRegions').prop('checked', true);   			
		};
		
		$scope.checkedAllApplicableMarketplaces = function() {			
			$(':checkbox.applicableMarketplaces').prop('checked', true);   	  			
		};
		
	});	
</script>		
</head>
<div class="max-width" ng-app="template" ng-controller="templateCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
						<c:when test="${type eq 'Create'}">
							<spring:message code="template.createResponseTemplate" />
							<c:url var="action" value="/Issues/saveResponseTemplate"></c:url>
						</c:when>
						<c:otherwise>
							<spring:message code="template.editResponse" />
							<c:url var="action" value="/Issues/updateResponseTemplate"></c:url>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<form:form action="${action}" name="Template"
			class="form-horizontal text-left" modelAttribute="Template">
			<div class="row">
				<div class="col-md-12">
					<div class="form-group row">
						<div class="col-12">
							<label><spring:message code="template.issueName" /></label>
						</div>
						<div class="col-12">
							<b>${Template.issueName}</b>
							<form:hidden path="id" />
							<form:hidden path="issueId" value="${Template.issueId}" />
						</div>
					</div>

					<div class="form-group row">
						<div class="col-12">

							<label><spring:message
									code="template.applicableLanguages" /></label>
						</div>

						<div class="col-12">

							<c:forEach items="${localeCodeList}" var="localeCode">

								<div class="form-check form-check-inline">
									<form:checkbox class="form-check-input"
										path="applicableLanguages" value="${localeCode.name()}" />
									<label class="form-check-label">${localeCode.fullName}</label>
								</div>
							</c:forEach>

						</div>

					</div>
					<div class="form-group row">
						<div class="col-12">
							<label><spring:message code="template.applicableCase" /></label>
						</div>
						<div class="col-12">
							<input class="btn btn-default" type="button"
								value="<spring:message code='template.selectAll'/>"
								ng-click="checkedAllApplicableCaseTypes()" />
							<c:forEach items="${caseTypeList}" var="caseType">

								<div class="form-check form-check-inline">
									<form:checkbox class="form-check-input applicableCaseTypes"
										path="applicableCaseTypes" value="${caseType}" />
									<label class="form-check-label"><spring:message
											code="ccc.${caseType}" /></label>
								</div>

							</c:forEach>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-12">

							<label><spring:message code="template.applicableMarket" /></label>
						</div>

						<div class="col-12">
							<input class="btn btn-default" type="button"
								value="<spring:message code='template.selectAll'/>"
								ng-click="checkedAllApplicableMarketRegions()" />
							<c:forEach items="${marketRegionList}" var="marketRegion">

								<div class="form-check form-check-inline">
									<form:checkbox class="form-check-input applicableMarketRegions"
										path="applicableMarketRegions" value="${marketRegion}" />
									<label class="form-check-label"><spring:message
											code="${marketRegion}" /></label>
								</div>

							</c:forEach>
						</div>
					</div>

					<div class="form-group row">
						<div class="col-12">
							<label><spring:message
									code="template.applicableMarketplaces" /></label>
						</div>
						<div class="col-12">
							<input class="btn btn-default" type="button"
								value="<spring:message code='template.selectAll'/>"
								ng-click="checkedAllApplicableMarketplaces()" />
							<c:forEach items="${marketplaceList}" var="marketplace">
								<div class="form-check form-check-inline">
									<form:checkbox class="form-check-input applicableMarketplaces"
										path="applicableMarketplaceList" value="${marketplace.name()}" />
									<label class="form-check-label">${marketplace.name}</label>
								</div>
							</c:forEach>
						</div>
					</div>



					<div id="contentsRow">
						<form:textarea id="contents" class="form-control" path="contents"
							rows="8" />
						<div>
							<div class=" mt-2">
								<input class="btn btn-primary float-right ml-2" type="submit"
									value="<spring:message code='template.submit'/>" />
								<c:choose>
									<c:when test="${type eq 'Create'}">
										<a
											href="${pageContext.request.contextPath}/Issues/${Template.issueId}">
											<input class="btn btn-link float-right" type="button"
											value="<spring:message code='template.cancel'/>" />
										</a>
									</c:when>
									<c:otherwise>
										<a
											href="${pageContext.request.contextPath}/Issues/${Template.issueId}">
											<input class="btn btn-link float-right" type="button"
											value="<spring:message code='template.cancel'/>" />
										</a>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
$('#contents').wysihtml5({
	"events": {
    	"load": function() {
        	$("#contentsRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");        	               
		}
    },toolbar: {		   
   		"html": true, //Button which allows you to edit the generated HTML. Default false   		     		   
		"link": false, //Button to insert a link. Default true
		"image": false, //Button to insert an image. Default true,
		"blockquote": false, //Blockquote 
		"fa": true,
		
	}
});		
</script>