<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="header.links" /> - DRS
</title>

<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js "/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script>
	$(document).ready(function() {

		$('#supplementalLink').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});

		$('#supplementalLink').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});

		$('#supplierKcodeToFilter').select2();
		
	});

	function deleteConfirm() {

		$("#dialog-confirm").html("Delete selected supplemental link?");

		$("#dialog-confirm").dialog({
			open : function() {
				$('.ui-dialog-buttonset button[name="no"]').focus();
			},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [ {
				text : "Yes",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					$("#deleteSupplementalLinks").submit();
				}
			}, {
				text : "No",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});

	}

	var app = angular.module('supplementalLink', []);
	app.controller('supplementalLinkCtrl',function($scope) {

						$('#idList').val("");
						$scope.idArray = [];
						$("#deleteId").attr("disabled", true);
						$scope.filter = "All";

						$scope.supplementalLinkLineItems = [];
						var supplementalLinkList = ${SupplementalLinkList};
						
						for ( var supplementalLink in supplementalLinkList) {

							if (supplementalLinkList
									.hasOwnProperty(supplementalLink)) {

								$scope.supplementalLinkLineItems
										.push({
											id : supplementalLinkList[supplementalLink].id,
											name : supplementalLinkList[supplementalLink].name,
											url : supplementalLinkList[supplementalLink].url,
											description : supplementalLinkList[supplementalLink].description
										});

							}

						}

						$scope.filterSupplementalLink = function() {

							var supplierKcode = $("#supplierKcodeToFilter").val();
							
							if (supplierKcode == "All") {

								$scope.getAllSupplementalLinks();

							} else {

								$scope.getListOfSupplementalLinksBySupplierKcode(supplierKcode);

							}

						};

						$scope.getListOfSupplementalLinksBySupplierKcode = function(
								supplierKcode) {

							var ajaxUrl = '${pageContext.request.contextPath}/SupplementalLinks/getListOfSupplementalLinksBySupplierKcode/';
							var supplementalLinkList = null;

							$.ajax({
										type : 'get',
										url : ajaxUrl,
										contentType : "application/json; charset=utf-8",
										data : {
											supplierKcode : supplierKcode
										},
										dataType : "json",
										success : function(data) {
											supplementalLinkList = data;
											
											$scope.supplementalLinkLineItems
													.splice(
															0,
															$scope.supplementalLinkLineItems.length)
											$scope.$apply();

											for ( var supplementalLink in supplementalLinkList) {

												if (supplementalLinkList
														.hasOwnProperty(supplementalLink)) {

													$scope.supplementalLinkLineItems
															.push({
																id : supplementalLinkList[supplementalLink].id,
																name : supplementalLinkList[supplementalLink].name,
																url : supplementalLinkList[supplementalLink].url,
																description : supplementalLinkList[supplementalLink].description
															});

												}

											}
											
											$scope.$apply();

										}

									});

						};

						$scope.getAllSupplementalLinks = function() {

							var ajaxUrl = '${pageContext.request.contextPath}/SupplementalLinks/getAllSupplementalLinks/';
							var supplementalLinkList = null;

							$.ajax({
										type : 'get',
										url : ajaxUrl,
										contentType : "application/json; charset=utf-8",
										dataType : "json",
										success : function(data) {
											supplementalLinkList = data;
											
											$scope.supplementalLinkLineItems
													.splice(
															0,
															$scope.supplementalLinkLineItems.length)
											$scope.$apply();

											for ( var supplementalLink in supplementalLinkList) {

												if (supplementalLinkList
														.hasOwnProperty(supplementalLink)) {

													$scope.supplementalLinkLineItems
															.push({
																id : supplementalLinkList[supplementalLink].id,
																name : supplementalLinkList[supplementalLink].name,
																url : supplementalLinkList[supplementalLink].url,
																description : supplementalLinkList[supplementalLink].description
															});

												}

											}
											
											$scope.$apply();

										}

									});

						};

						$scope.checkSupplementatLinkToDelete = function(id) {

							var isChecked = $('#link' + id).prop('checked') ? true
									: false;

							if (isChecked === true) {

								$scope.idArray.push(id);

							} else {

								$scope.idArray = jQuery.grep($scope.idArray,
										function(value) {
											return value != id;
										});

							}

							$('#idList').val($scope.idArray.toString());

							if ($('#idList').val() == "") {
								$("#deleteId").attr("disabled", true);
							} else {
								$("#deleteId").attr("disabled", false);
							}

						};

					});
</script>
</head>
<div class="max-width" ng-app="supplementalLink" ng-controller="supplementalLinkCtrl">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row">							
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code="header.links" />
				</div>
			</div>
			<div class="col-md-6 text-right">
				<div class="page-heading">
					<sec:authorize access="hasAnyRole('${auth['SUPPLEMENTAL_LINKS_CREATE']}')">
						<a class="btn btn-primary" href="${pageContext.request.contextPath}/SupplementalLinks/create">Create supplemental link</a>
					</sec:authorize>
		   		</div>
			</div>								
		</div>
		<sec:authorize access="hasAnyRole('${auth['SUPPLEMENTAL_LINKS_PREVIEW']}')">
		<div class="row">
			<div class="col-md-4 col-md-offset-8">
				<div id="preview" style="float:right">
					<spring:message code="links.preview" />
					<select id="supplierKcodeToFilter" class="form-control" style="width: 250px; display: inline;" ng-model="filter" ng-change="filterSupplementalLink();">
						<option value="All">All</option>
						<c:forEach var="supplierKcodeToNameMap" items="${supplierKcodeToNameMap}">
						<option value="${supplierKcodeToNameMap.key}">${supplierKcodeToNameMap.key} ${supplierKcodeToNameMap.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		</sec:authorize>
		<div class="row">
			<div class="col-md-12">
				<table id="supplementalLink" class="table table-floated">
					<thead>
						<tr>
							<sec:authorize access="hasAnyRole('${auth['SUPPLEMENTAL_LINKS_DELETE']}')">
								<c:if test="${!empty SupplementalLinkList}">
									<th class="text-center">
										<c:url var="action" value="/SupplementalLinks/delete"></c:url>
										<form id="deleteSupplementalLinks" action="${action}" method="POST">
											<input id="idList" type="hidden" name="idList">
										</form> 
										<input id="deleteId" class="btn btn-default btn-sm" type="button" value="Delete" onclick="deleteConfirm();">
										<div id="dialog-confirm"></div>
									</th>
								</c:if>
							</sec:authorize>
							<th><spring:message code="links.item" /></th>
							<th><spring:message code="links.description" /></th>
							<sec:authorize access="hasAnyRole('${auth['SUPPLEMENTAL_LINKS_EDIT']}')">
								<c:if test="${!empty SupplementalLinkList}">
									<th></th>
								</c:if>
							</sec:authorize>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="supplementalLinkLineItem in supplementalLinkLineItems">
							<sec:authorize access="hasAnyRole('${auth['SUPPLEMENTAL_LINKS_DELETE']}')">
								<td class="text-center">
									<input id="link{{supplementalLinkLineItem.id}}" type="checkbox" name="link" value="{{supplementalLinkLineItem.id}}" ng-model="checkToDelete" ng-change="checkSupplementatLinkToDelete('{{supplementalLinkLineItem.id}}');">
								</td>
							</sec:authorize>
								<td>
									<a target="_blank" href="{{supplementalLinkLineItem.url}}">{{supplementalLinkLineItem.name}}</a>
								</td>
								<td>{{supplementalLinkLineItem.description}}</td>
							<sec:authorize access="hasAnyRole('${auth['SUPPLEMENTAL_LINKS_EDIT']}')">
								<td>
									<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/SupplementalLinks/edit/{{supplementalLinkLineItem.id}}">Edit</a>
								</td>
							</sec:authorize>
						</tr>
					</tbody>
				</table>
			</div>
		</div>		
	</div>
</div>