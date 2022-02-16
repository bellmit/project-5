<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<spring:message code='company.companyInfoMaintenance' /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	$(function() {	  
		$("#flashMessage").delay(5000).fadeOut();	  
	});
	angular.module('companyViewBySupplier', []).controller('companyViewBySupplierCtrl',function($scope) {
		$scope.serviceEmailLineItems = [];
		$scope.serviceEmailDuplicated = false;
		$scope.serviceEmailAtLeastOne = false;
		$scope.productContactEmailLineItems = [];
		$scope.productContactEmailDuplicated = false;
		$scope.productContactEmailAtLeastOne = false;
		var CompanyJson = ${CompanyJson};
		$scope.address = CompanyJson.address;FCompanyEditByOne.jsp
		$scope.phoneNumber = CompanyJson.phoneNumber;
		var serviceEmailList = CompanyJson.serviceEmailList;						
		for (var key in serviceEmailList) {
			if (serviceEmailList.hasOwnProperty(key)) {							
				$scope.serviceEmailLineItems.push({
					serviceEmail : serviceEmailList[key]
				});
			}
		}
		if(CompanyJson.productEmailList == null){
			$scope.productContactEmailLineItems = [{productContactEmail:''}];
		}else{
			var productContactEmailList = CompanyJson.productEmailList; 	
			for ( var key in productContactEmailList) {
				if (productContactEmailList.hasOwnProperty(key)) {								
					$scope.productContactEmailLineItems.push({
						productContactEmail : productContactEmailList[key]
					});
				}
			}				
		}
				
		$scope.addServiceEmailLineItem = function() {
			$scope.serviceEmailAtLeastOne = false;							
				$scope.serviceEmailLineItems.push({
					serviceEmail : ''
				});
		};
		$scope.removeServiceEmailLineItem = function(serviceEmailLineItem) {
			if(Number($scope.serviceEmailLineItems.length)-1 == 0){
				$scope.serviceEmailAtLeastOne = true;
			}
			if ($scope.serviceEmailLineItems.length > 1) {
				var idx = $scope.serviceEmailLineItems.indexOf(serviceEmailLineItem);
				$scope.serviceEmailLineItems.splice(idx, 1);
			}
			$scope.updateServiceEmailList();
		};
		$scope.updateServiceEmailList = function() {
			var ServiceEmailArray = new Array();
			for ( var key in $scope.serviceEmailLineItems) {
				if ($scope.serviceEmailLineItems.hasOwnProperty(key)) {
					if ($scope.serviceEmailLineItems[key]['serviceEmail'] !== "") {
						ServiceEmailArray.push($scope.serviceEmailLineItems[key]['serviceEmail']);
					}
				}
			}
			$scope.serviceEmailDuplicated = hasDuplicates(ServiceEmailArray);
			$('#serviceEmailList').val(ServiceEmailArray.toString());
		};

		$scope.addProductContactEmailLineItem = function() {
			$scope.productContactEmailAtLeastOne = false;
			$scope.productContactEmailLineItems.push({
				productContactEmail : ''
			});
		};

		$scope.removeProductContactEmailLineItem = function(productContactEmailLineItem) {
			if(Number($scope.productContactEmailLineItems.length)-1 == 0){
				$scope.productContactEmailAtLeastOne = true;
			}
			if ($scope.productContactEmailLineItems.length > 1) {
				var idx = $scope.productContactEmailLineItems.indexOf(productContactEmailLineItem);
				$scope.productContactEmailLineItems.splice(idx, 1);
			}
			$scope.updateProductContactEmailList();
		};

		$scope.updateProductContactEmailList = function() {
			var ProductContactEmailArray = new Array();
			for ( var key in $scope.productContactEmailLineItems) {
				if ($scope.productContactEmailLineItems.hasOwnProperty(key)) {
					if ($scope.productContactEmailLineItems[key]['productContactEmail'] !== "") {
						ProductContactEmailArray.push($scope.productContactEmailLineItems[key]['productContactEmail']);
					}
				}
			}
			$scope.productContactEmailDuplicated = hasDuplicates(ProductContactEmailArray);
			$('#productEmailList').val(ProductContactEmailArray.toString());
		};
		function hasDuplicates(array) {
			return (new Set(array)).size !== array.length;
		}
	});
</script>
</head>
<div class="max-width">
	<div id="flashMessage" class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid" ng-app="companyViewBySupplier" ng-controller="companyViewBySupplierCtrl">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='company.companyInfoMaintenance' />
				</div>
			</div>
		</div>
		<c:url var="updateAction" value="/Company/update"></c:url>
		<form:form name="Company" action="${updateAction}"  modelAttribute="Company" novalidate="novalidate">
			<div class="row">
				<div class="col-md-6">
					<table class="table table-withoutBorder">
						<tr>
							<td class="text-right">
								<b><spring:message code='company.k_code' /></b>
							</td>
							<td>
								${Company.kcode}
								<form:hidden path="kcode" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.companyName' /></b>
							</td>
							<td>${Company.nameLocal}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.companyNameEng' /></b>
							</td>
							<td>${Company.nameEnUs}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.shortName' /></b>
							</td>
							<td>${Company.shortNameLocal}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.shortNameEng' /></b>
							</td>
							<td>${Company.shortNameEnUs}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.address' /></b>
							</td>
							<td><form:input id="address"
									class="form-control" style="width:95%;display: inline;"
									path="address" ng-model="address" required="required" />
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.address.$error.required && Company.address.$dirty">
									<spring:message code='company.address_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.phoneNumber' /></b>
							</td>
							<td><form:input
									id="phoneNumber" class="form-control"
									style="width:95%;display: inline;" path="phoneNumber"
									ng-model="phoneNumber" required="required" />
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.phoneNumber.$error.required && Company.phoneNumber.$dirty">
									<spring:message code='company.phoneNumber_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.serviceMailAddress' /></b>
							</td>
							<td>
								<div ng-repeat="serviceEmailLineItem in serviceEmailLineItems" style="padding-bottom: 10px">
									<div class="row">
										<div class="col-sm-10">
											<select name="serviceMailAddress{{$id}}"
												class="form-control" style="width: 95%; display: inline;"
												ng-model="serviceEmailLineItem.serviceEmail"
												ng-change="updateServiceEmailList()" required>
												<option value="">--- Select ---</option>
												<c:forEach items="${supplierUserEmailList}" var="supplierUserEmail">
												<option value="${supplierUserEmail}">${supplierUserEmail}</option>
												</c:forEach>
											</select>
											<span class="text-danger">*</span>
											<div class="text-danger" ng-show="Company.serviceMailAddress{{$id}}.$error.required && Company.serviceMailAddress{{$id}}.$dirty">
												<spring:message code='company.serviceMailAddress_req' />
											</div>
										</div>
										<div class="col-sm-2">
											<button type="button" class="btn btn-default" ng-click="removeServiceEmailLineItem(serviceEmailLineItem)">
												<i class="fas fa-trash-alt"></i>
											</button>
										</div>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div class="row">
									<div class="col-sm-10">
										<div class="text-danger" ng-if="serviceEmailDuplicated">
											<spring:message code='company.serviceMailAddress_dup' />
										</div>
										<div class="text-danger" ng-if="serviceEmailAtLeastOne">
											<spring:message code='company.serviceMailAddress_atLeastOne' />
										</div>
									</div>
									<div class="col-sm-2">
										<button type="button" class="btn btn-default" ng-click="addServiceEmailLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										<form:hidden id="serviceEmailList" path="serviceEmailList" />
									</div>
								</div>
							</td>
						</tr>
						<tr>	
							<td class="text-right">	
								<b><spring:message code='company.productContactEmailAddress' /></b>
							</td>
							<td>
								<div ng-repeat="productContactEmailLineItem in productContactEmailLineItems" style="padding-bottom: 10px">
									<div class="row">
										<div class="col-sm-10">
											<select name="productContactEmailAddress{{$id}}" class="form-control" style="width: 95%; display: inline;" ng-model="productContactEmailLineItem.productContactEmail" ng-change="updateProductContactEmailList()" required>
												<option value="">--- Select ---</option>
												<c:forEach items="${supplierUserEmailList}" var="supplierUserEmail">
												<option value="${supplierUserEmail}">${supplierUserEmail}</option>
												</c:forEach>
											</select>
											<span style="color: #FF0000">*</span>
											<div class="text-danger" ng-show="Company.productContactEmailAddress{{$id}}.$error.required && Company.productContactEmailAddress{{$id}}.$dirty">
												<spring:message code='company.productContactEmailAddress_req' />
											</div>
										</div>
										<div class="col-sm-2">
											<button type="button" class="btn btn-default" ng-click="removeProductContactEmailLineItem(productContactEmailLineItem)">
												<i class="fas fa-trash-alt"></i>
											</button>
										</div>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div class="row">
									<div class="col-sm-10">
										<div class="text-danger" ng-if="productContactEmailDuplicated">
											<spring:message code='company.productContactEmailAddress_dup' />
										</div>
										<div class="text-danger" ng-if="productContactEmailAtLeastOne">
											<spring:message code='company.productContactEmailAddress_atLeastOne' />
										</div>
									</div>
									<div class="col-sm-2">
										<button type="button" class="btn btn-default" ng-click="addProductContactEmailLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										<form:hidden id="productEmailList" path="productEmailList" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.couponKeyword' /></b>
							</td>
							<td>
								<c:choose>
									<c:when test="${Company.couponList.size() ne null}">
										<c:forEach items="${Company.couponList}" var="couponKeyword" varStatus="couponKeywordStatus">
											<c:choose>
												<c:when test="${Company.couponList.size() ne couponKeywordStatus.count}">
													${couponKeyword}, 																
												</c:when>
												<c:otherwise>
													${couponKeyword}
												</c:otherwise>
											</c:choose>
										</c:forEach>		 																
									</c:when>
									<c:otherwise>
										-		
									</c:otherwise>																																				
								</c:choose>																
							</td>
						</tr>
					</table>
				</div>
				<div class="col-md-6">
					<table class="table table-withoutBorder">
						<tr>
							<td class="text-right">
								<b><spring:message code='company.country' /></b>
							</td>
							<td>${Company.countryCode}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.currency' /></b>
							</td>
							<td>${Company.currency}</td>
						</tr>						
						<tr>
							<td class="text-right">
								<b><spring:message code='company.${Company.countryCode}_officialRegistrationNoType' /></b>
							</td>
							<td>${Company.officialRegistrationNo}</td>
						</tr>
						<tr>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankAccountInfo' /></b>
							</td>
							<td></td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankName' /></b>
							</td>
							<td>${Company.bankName}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankBranchName' /></b>
							</td>
							<td>${Company.bankBranchName}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankAccountNumber' /></b>
							</td>
							<td>${Company.accountNumber}</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankAccount' /></b>
							</td>
							<td>${Company.accountName}</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<spring:message code='company.note' />
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-12 text-right">
					<input class="btn btn-primary" type="submit"
						value="<spring:message code='company.save' />"
						ng-disabled='serviceEmailDuplicated || Company.$invalid' />
				</div>
			</div>
		</form:form>
	</div>
</div>