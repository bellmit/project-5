<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>${title} - DRS</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	function onSubmit() {
		switch (document.pressed) {
		case 'createCompany':
			document.Company.action = "${pageContext.request.contextPath}/Companies/save";
			break;
		case 'updateCompany':
			document.Company.action = "${pageContext.request.contextPath}/Companies/update";
			break;
		default:
		}
		return true;
	}
	angular.module('company', []).controller('companyCtrl',function($scope) {												
		$scope.OfficialRegistrationNoTypeArray = new Array();
		$scope.OfficialRegistrationNoTypeArray["TW"] = "<spring:message code="company.TW_officialRegistrationNoType" />";
		$scope.OfficialRegistrationNoTypeArray["CN"] = "<spring:message code="company.CN_officialRegistrationNoType" />";
		$scope.OfficialRegistrationNoTypeArray["US"] = "<spring:message code="company.US_officialRegistrationNoType" />";
		$scope.OfficialRegistrationNoTypeArray["UK"] = "<spring:message code="company.UK_officialRegistrationNoType" />";
		if (document.URL.indexOf("edit") > -1) {
			var CompanyJson = ${CompanyJson};
			$scope.serviceEmailDuplicated = false;
			$scope.serviceEmailAtLeastOne = false;						
			$scope.serviceEmailLineItems = [];							
			$scope.couponKeywordDuplicated = false;
			$scope.innerCouponKeywordDuplicated = false;
			$scope.couponKeywordLineItems = [];
			$scope.productContactEmailDuplicated = false;
			$scope.productContactEmailAtLeastOne = false;
			$scope.productContactEmailLineItems = [];
			$scope.kcode = CompanyJson.kcode;
			$scope.nameLocal = CompanyJson.nameLocal;
			$scope.nameEnUs = CompanyJson.nameEnUs;
			$scope.shortNameLocal = CompanyJson.shortNameLocal;
			$scope.countryCode = CompanyJson.countryCode;
			$scope.currency = CompanyJson.currency;
			$scope.address = CompanyJson.address;
		    $scope.phoneNumber = CompanyJson.phoneNumber;
			$scope.serviceMailAddress = CompanyJson.serviceMailAddress;
			$scope.officialRegistrationNo = CompanyJson.officialRegistrationNo;
			$scope.bankName = CompanyJson.bankName;
			$scope.bankBranchName = CompanyJson.bankBranchName;
			$scope.accountNumber = CompanyJson.accountNumber;
			$scope.accountName = CompanyJson.accountName;
			var serviceEmailList = CompanyJson.serviceEmailList;							
				for ( var key in serviceEmailList) {
					if (serviceEmailList.hasOwnProperty(key)) {								
						$scope.serviceEmailLineItems.push({
							serviceEmail : serviceEmailList[key]
						});
					}
				}
			if(CompanyJson.couponList == null){
				$scope.couponKeywordLineItems = [{couponKeyword:''}]				
			}else{
				var couponKeywordList = CompanyJson.couponList;	
				for ( var key in couponKeywordList) {
					if (couponKeywordList.hasOwnProperty(key)) {								
						$scope.couponKeywordLineItems.push({
							couponKeyword : couponKeywordList[key]
						});
					}
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
			$('#officialRegistrationNoType').html($scope.OfficialRegistrationNoTypeArray[CompanyJson.countryCode]);
		}
		$scope.retrieveOfficialRegistrationNoType = function(scope) {
			if (typeof scope.countryCode != 'undefined') {
				$('#officialRegistrationNoType').html($scope.OfficialRegistrationNoTypeArray[scope.countryCode]);
			} else {
				$('#officialRegistrationNoType').html("<spring:message code="company.officialRegistrationNo" />");
			}
		};
						
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

		$scope.addCouponKeywordLineItem = function() {
			$scope.couponKeywordLineItems.push({
				couponKeyword : ''
			});		
		};
		
		$scope.removeCouponKeywordLineItem = function(couponKeywordLineItem) {
			var idx = $scope.couponKeywordLineItems.indexOf(couponKeywordLineItem);
			$scope.couponKeywordLineItems.splice(idx, 1);
			$scope.updateCouponKeywordList();
		};
		$scope.updateCouponKeywordList = function() {
			var CouponKeywordArray = new Array();
			for ( var key in $scope.couponKeywordLineItems) {
				if ($scope.couponKeywordLineItems.hasOwnProperty(key)) {
					if ($scope.couponKeywordLineItems[key]['couponKeyword'] !== "") {
						CouponKeywordArray.push($scope.couponKeywordLineItems[key]['couponKeyword']);
					}
				}
			}
			$scope.innerCouponKeywordDuplicated = hasDuplicatesCaseInsensitive(CouponKeywordArray);
			$('#couponKeywordList').val(CouponKeywordArray.toString());
						
			$scope.couponKeywordDuplicated = false;
			for ( var key in $scope.couponKeywordLineItems) {
				if ($scope.couponKeywordLineItems.hasOwnProperty(key)) {
					if ($scope.couponKeywordLineItems[key]['couponKeyword'] !== "") {
						$scope.isCouponUnique($scope.couponKeywordLineItems[key]['couponKeyword']);
						if($scope.couponKeywordDuplicated) break;
					}
				}
			}						
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
		

		$scope.isCouponUnique = function(couponKeyword) {
			var isCouponUnique = null;
			$.ajax({	
				type : 'get',
				async : false,
				url : '${pageContext.request.contextPath}/Companies/isCouponUnique/',
				contentType : "application/json; charset=utf-8",
				data : {					
					couponKeyword : couponKeyword,
					companyKCode : $scope.kcode
				},
				dataType : "json",
				success : function(data) {
					isCouponUnique = data;
					if(isCouponUnique){
						$scope.couponKeywordDuplicated = false;
					}else{
						$scope.couponKeywordDuplicated = true;
					}										
				}												
			});			
		};
		
		function hasDuplicatesCaseInsensitive(array) {			
		 	var x;
		    var length=array.length;
		    var arrayCheck=[];
		    var obj={};		 
		  	for (x=0; x<length; x++) {
		    	obj[array[x].toLowerCase()]=0;
		  	}		  
		  	for (x in obj) {
		    	arrayCheck.push(x);
		  	}		  		  
		  	return length !== arrayCheck.length;						
		}
				

		function hasDuplicates(array) {
			return (new Set(array)).size !== array.length;
		}
						
	});
</script>
</head>
<div class="max-width">
	<div class="container-fluid" ng-app="company" ng-controller="companyCtrl">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">${title}</div>
			</div>
		</div>
		<form:form name="Company"  modelAttribute="Company" onsubmit="return onSubmit();" novalidate="novalidate">
			<div class="row">
				<div class="col-md-6">
					<table class="table table-withoutBorder">
						<tr>
							<td class="text-right">
								<b><spring:message code='company.k_code' /></b>
							</td>
							<td>
								<c:choose>
									<c:when test="${type eq 'Create'}">
										<form:input id="kcode" class="form-control" style="width:95%;display: inline;" path="kcode" ng-model="kcode" required="required" />
										<span class="text-danger">*</span>
										<div class="text-danger" ng-show="Company.kcode.$error.required && Company.kcode.$dirty">
											<spring:message code='company.k_code_req' />
										</div>
									</c:when>
									<c:otherwise>
										<form:input id="kcode" class="form-control" style="width:95%;display: inline;" path="kcode" readonly="true" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.companyName' /></b>
							</td>
							<td>
								<form:input id="nameLocal" class="form-control" style="width:95%;display: inline;" path="nameLocal" ng-model="nameLocal" required="required" />
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.nameLocal.$error.required && Company.nameLocal.$dirty">
									<spring:message code='company.companyName_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.companyNameEng' /></b>
							</td>
							<td>
								<form:input id="nameEnUs" class="form-control" style="width:95%;display: inline;" path="nameEnUs" ng-model="nameEnUs" required="required" />
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.nameEnUs.$error.required && Company.nameEnUs.$dirty">
									<spring:message code='company.companyNameEng_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.shortName' /></b>
							</td>
							<td>
								<form:input id="shortNameLocal" class="form-control" style="width:95%;display: inline;" path="shortNameLocal" ng-model="shortNameLocal" required="required" />
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.shortNameLocal.$error.required && Company.shortNameLocal.$dirty">
									<spring:message code='company.shortName_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.shortNameEng' /></b>
							</td>
							<td>
								<form:input id="shortNameEnUs" class="form-control" style="width:95%;display: inline;" path="shortNameEnUs" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.address' /></b>
							</td>
							<td>
								<form:input id="address" class="form-control" style="width:95%;display: inline;" path="address" ng-model="address" required="required" />
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
							<td>
								<form:input id="phoneNumber" class="form-control" style="width:95%;display: inline;" path="phoneNumber" ng-model="phoneNumber" required="required" />
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.phoneNumber.$error.required && Company.phoneNumber.$dirty">
									<spring:message code='company.phoneNumber_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right"><b><spring:message
										code='company.serviceMailAddress' /></b></td>
							<td>
								<div ng-repeat="serviceEmailLineItem in serviceEmailLineItems" style="padding-bottom: 10px">
									<div class="row">
										<div class="col-sm-10">
											<select name="serviceMailAddress{{$id}}" class="form-control" style="width: 95%; display: inline;" ng-model="serviceEmailLineItem.serviceEmail" ng-change="updateServiceEmailList()" required>
												<option value="">--- Select ---</option>
												<c:forEach items="${supplierUserEmailList}" var="supplierUserEmail">
												<option value="${supplierUserEmail}">${supplierUserEmail}</option>
												</c:forEach>
											</select>
											<span style="color: #FF0000">*</span>
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
							<td class="text-right"><b><spring:message
										code='company.couponKeyword' /></b></td>
							<td>
								<div ng-repeat="couponKeywordLineItem in couponKeywordLineItems" style="padding-bottom: 10px">
									<div class="row">
										<div class="col-sm-10">
											<input name="couponKeyword{{$id}}" class="form-control" style="width: 95%; display: inline;" ng-model="couponKeywordLineItem.couponKeyword" ng-change="updateCouponKeywordList()">
										</div>
										<div class="col-sm-2">
											<button type="button" class="btn btn-default" ng-click="removeCouponKeywordLineItem(couponKeywordLineItem)">
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
										<div class="text-danger" ng-if="innerCouponKeywordDuplicated || couponKeywordDuplicated">
											<spring:message code='company.couponKeyword_dup' />
										</div>
									</div>
									<div class="col-sm-2">
										<button type="button" class="btn btn-default" ng-click="addCouponKeywordLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										<form:hidden id="couponKeywordList" path="couponList" />
									</div>
								</div>
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
							<td>
								<form:select
									id="countryCode" class="form-control"
									style="width:95%;display: inline;" path="countryCode"
									ng-model="countryCode"
									ng-change="retrieveOfficialRegistrationNoType(this)"
									required="required">
									<form:option value="">--- Select ---</form:option>
									<form:option value="TW">
										<spring:message code="TW" />
									</form:option>
									<form:option value="CN">
										<spring:message code="CN" />
									</form:option>
									<form:option value="US">
										<spring:message code="US" />
									</form:option>
									<form:option value="UK">
										<spring:message code="UK" />
									</form:option>
								</form:select>
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.countryCode.$error.required && Company.countryCode.$dirty">
									<spring:message code='company.country_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.currency' /></b>
							</td>
							<td>
								<form:select
									id="currency" class="form-control"
									style="width:95%;display: inline;" path="currency"
									ng-model="currency" required="required">
									<form:option value="" label="--- Select ---" />
									<spring:message code="TWD" var="TWD" />
									<form:option value="TWD" label="${TWD}" />
									<spring:message code="USD" var="USD" />
									<form:option value="USD" label="${USD}" />
								</form:select>
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.currency.$error.required && Company.currency.$dirty">
									<spring:message code='company.currency_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.officialRegistrationType' /></b>
							</td>
							<td></td>
						</tr>
						<tr>
							<td class="text-right">
								<b><span id="officialRegistrationNoType"> <spring:message code='company.officialRegistrationNo'/></span></b>
							</td>
							<td>
								<form:input
									id="officialRegistrationNo" class="form-control"
									style="width:95%;display: inline;"
									path="officialRegistrationNo" ng-model="officialRegistrationNo"
									required="required" />
									<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.officialRegistrationNo.$error.required && Company.officialRegistrationNo.$dirty">
									<spring:message code='company.officialRegistrationNo_req' />
								</div>
							</td>
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
							<td>
								<form:input
									id="bankName" class="form-control"
									style="width:95%;display: inline;" path="bankName"
									ng-model="bankName" required="required" />
									<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.bankName.$error.required && Company.bankName.$dirty">
									<spring:message code='company.bankName_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankBranchName' /></b>
							</td>
							<td>
								<form:input
									id="bankBranchName" class="form-control"
									style="width:95%;display: inline;" path="bankBranchName"
									ng-model="bankBranchName" required="required" />
									<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.bankBranchName.$error.required && Company.bankBranchName.$dirty">
									<spring:message code='company.bankBranchName_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankAccountNumber' /></b>
							</td>
							<td>
								<form:input
									id="accountNumber" class="form-control"
									style="width:95%;display: inline;" path="accountNumber"
									ng-model="accountNumber" required="required" />
									<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.accountNumber.$error.required && Company.accountNumber.$dirty ">
									<spring:message code='company.bankAccountNumber_req' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code='company.bankAccount' /></b>
							</td>
							<td>
								<form:input
									id="accountName" class="form-control"
									style="width:95%;display: inline;" path="accountName"
									ng-model="accountName" required="required" />
									<span class="text-danger">*</span>
								<div class="text-danger" ng-show="Company.accountName.$error.required && Company.accountName.$dirty">
									<spring:message code='company.bankAccount_req' />
								</div>
							</td>
						</tr>
                        <tr>
                            <td class ="text-right">
                            <b><spring:message code='company.activated' /></b>
                            </td>
                            <td>
                                yes &nbsp;<form:radiobutton  id="activated" path="activated" value="true" />
                                &ensp;
                                no &nbsp;<form:radiobutton   id="activated" path="activated" value="false" />


							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-withoutBorder">
						<tr>
							<td class="text-left">
								<label class="control-label">
									<spring:message code='company.notes' />
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<form:textarea id="notes" class="form-control" path="notes" rows="4" cols="150" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-12 text-right">
					<c:choose>
						<c:when test="${type eq 'Create'}">
							<input class="btn btn-primary" name="createCompany"
								type="submit" value="<spring:message code='company.submit' />"
								style="float: right" onclick="document.pressed=this.name"
								ng-disabled='serviceEmailDuplicated || innerCouponKeywordDuplicated || couponKeywordDuplicated || Company.$invalid' />
						</c:when>
						<c:otherwise>
							<input class="btn btn-primary" name="updateCompany"
								type="submit" value="<spring:message code='company.submit' />"
								style="float: right" onclick="document.pressed=this.name"
								ng-disabled='serviceEmailDuplicated || innerCouponKeywordDuplicated || couponKeywordDuplicated || Company.$invalid' />
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</div>
</div>