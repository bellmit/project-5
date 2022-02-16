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
			<spring:message code="product.addToNewMarketRegion" /> : ${productCodeByDrs} - DRS 
		</c:when>
		<c:otherwise>
			<spring:message code="product.editMarketRegion" /> : ${ProductSkuRegionInfo.productCodeByDrs} - DRS 
		</c:otherwise>
	</c:choose></title>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	angular.module('ProductSkuRegionInfo', []).controller('ProductSkuRegionInfoCtrl',function($scope) {
		var currentURL = document.URL;						
		$scope.vat = [];						
		$scope.vat["AMAZON_COM"] = 0;
		$scope.vat["TRUETOSOURCE"] = 0;
		$scope.vat["AMAZON_CO_UK"] = 0.2;
		$scope.vat["AMAZON_CA"] = 0;
		$scope.vat["AMAZON_DE"] = 0.19;
		$scope.vat["AMAZON_ES"] = 0.21;
		$scope.vat["AMAZON_FR"] = 0.2;
		$scope.vat["AMAZON_IT"] = 0.22;												
		if (currentURL.indexOf("edit") > -1) {
			var ProductSkuRegionInfoJson = ${ProductSkuRegionInfoJson};
			$scope.marketplace = ProductSkuRegionInfoJson.marketplace;
			$scope.currentBaseRetailPrice = ProductSkuRegionInfoJson.currentBaseRetailPrice;
			$scope.estimatedDrsRetainment = ProductSkuRegionInfoJson.estimatedDrsRetainment;
			$scope.referralRate = ProductSkuRegionInfoJson.referralRate;
			$scope.estimatedMarketplaceFees = ProductSkuRegionInfoJson.estimatedMarketplaceFees;
			$scope.estimatedFulfillmentFees = ProductSkuRegionInfoJson.estimatedFulfillmentFees;
			$scope.estimatedImportDuty = ProductSkuRegionInfoJson.estimatedImportDuty;
			$scope.estimatedFreightCharge = ProductSkuRegionInfoJson.estimatedFreightCharge;
			$scope.approxSupplierNetRevenue = ProductSkuRegionInfoJson.approxSupplierNetRevenue;
			$("#MSRP-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#supplierSuggestedBaseRetailPrice-currency").text("["+ ProductSkuRegionInfoJson.currency+ "]");
			$("#supplierMandatedMinRetailPrice-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#currentBaseRetailPrice-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#estimatedDrsSRetainment-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#estimatedMarketplaceFees-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#estimatedFulfillmentFees-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#estimatedImportDuty-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#estimatedFreightCharge-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
			$("#approxSupplierNetRevenue-currency").text("[" + ProductSkuRegionInfoJson.currency+ "]");
		}

		$scope.calculateEstimatedDrsRetainment = function(scope) {														
			var estimatedDrsRetainment = Math.round(Number(scope.currentBaseRetailPrice/(1+scope.vat[scope.marketplace]) * 0.15)* Math.pow(10, 6))/ Math.pow(10, 6);														
			scope.estimatedDrsRetainment = Math.round(Number(estimatedDrsRetainment)* Math.pow(10, 2))/ Math.pow(10, 2);
			scope.calculateApproxSupplierNetRevenue(scope);
		};

		$scope.calculateEstimatedMarketplaceFees = function(scope) {
			var estimatedMarketplaceFees = Math.round(Number(scope.currentBaseRetailPrice * (scope.referralRate / 100)) * Math.pow(10, 6)) / Math.pow(10, 6);
			scope.estimatedMarketplaceFees = Math.round(Number(estimatedMarketplaceFees) * Math.pow(10, 2))/ Math.pow(10, 2);
			scope.calculateApproxSupplierNetRevenue(scope);
		};

		$scope.calculateApproxSupplierNetRevenue = function(scope) {														
			var baseRetailPriceBeforeTax = Math.round(Number(scope.currentBaseRetailPrice/(1+scope.vat[scope.marketplace]))* Math.pow(10, 6))/ Math.pow(10, 6);							
		    var estimatedDrsRetainment = Math.round(Number(scope.currentBaseRetailPrice/(1+scope.vat[scope.marketplace]) * 0.15)* Math.pow(10, 6))/ Math.pow(10, 6);							
		    var estimatedMarketplaceFees = Math.round(Number(scope.estimatedMarketplaceFees)* Math.pow(10, 6))/ Math.pow(10, 6);							
		    var estimatedFulfillmentFees = Math.round(Number(scope.estimatedFulfillmentFees)* Math.pow(10, 6))/ Math.pow(10, 6);
			var estimatedImportDuty = Math.round(Number(scope.estimatedImportDuty)* Math.pow(10, 6))/ Math.pow(10, 6);
			var estimatedFreightCharge = Math.round(Number(scope.estimatedFreightCharge)* Math.pow(10, 6))/ Math.pow(10, 6);							
			var approxSupplierNetRevenue = baseRetailPriceBeforeTax - estimatedDrsRetainment - estimatedMarketplaceFees - estimatedFulfillmentFees - estimatedImportDuty - estimatedFreightCharge;
			scope.approxSupplierNetRevenue = Math.round(approxSupplierNetRevenue* Math.pow(10, 2))/ Math.pow(10, 2);
		};

		$scope.getMarketplaceCurrency = function(scope) {
			var marketplace = $('#marketplace').val();
			var ajaxUrl = '${pageContext.request.contextPath}/ProductSkuRegionInfo/getMarketplaceCurrency/';
			var marketplaceCurrency = "";
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : { marketplace : marketplace },
				dataType : "json",
				success : function(data) {
					marketplaceCurrency = data;
					$("#MSRP-currency").text("[" + marketplaceCurrency + "]");
					$("#supplierSuggestedBaseRetailPrice-currency").text("["+ marketplaceCurrency + "]");
					$("#currentBaseRetailPrice-currency").text("["+ marketplaceCurrency + "]");
					$("#estimatedDrsSRetainment-currency").text("["+ marketplaceCurrency + "]");
					$("#estimatedMarketplaceFees-currency").text("["+ marketplaceCurrency + "]");
					$("#estimatedFulfillmentFees-currency").text("["+ marketplaceCurrency + "]");
					$("#estimatedImportDuty-currency").text("[" + marketplaceCurrency + "]");
					$("#estimatedFreightCharge-currency").text("["+ marketplaceCurrency + "]");
					$("#approxSupplierNetRevenue-currency").text("[" + marketplaceCurrency + "]");											
					scope.calculateEstimatedDrsRetainment(scope);																						
				},
				error : function(jqXHR, textStatus,errorThrown) {
					$("#MSRP-currency").text("[" + marketplaceCurrency + "]");
					$("#supplierSuggestedActualRetailPrice-currency").text("["+ marketplaceCurrency + "]");
					$("#currentActualRetailPrice-currency").text("[" + marketplaceCurrency + "]");
					$("#estimatedDrsSRetainment-currency").text("[" + marketplaceCurrency + "]");
					$("#estimatedMarketplaceFees-currency").text("[" + marketplaceCurrency+ "]");
					$("#estimatedFulfillmentFees-currency").text("[" + marketplaceCurrency + "]");
					$("#estimatedImportDuty-currency").text("[" + marketplaceCurrency + "]");
					$("#estimatedFreightCharge-currency").text("[" + marketplaceCurrency+ "]");
					$("#approxSupplierNetRevenue-currency").text("[" + marketplaceCurrency + "]");
				}
			});
		}
	});
</script>
<style>
	.form-group {
		margin-top: 10px;
	}
	.form-group label {
		margin-bottom: 8px;
	}
	.text-danger {
		font-size: 16px;
	}
	input, select {
    	height: 37px !important;
    	padding: .375rem .75rem !important;
	}
	.btn {
		padding: 11px 22px !important;
	}
</style>
</head>
<div class="max-width" ng-app="ProductSkuRegionInfo"
	ng-controller="ProductSkuRegionInfoCtrl">
	<div class="container-fluid">
		<div class="card">
			<div class="row" style="margin-bottom: 22px;">
				<div class="col-md-12">
					<c:choose>
						<c:when test="${type eq 'Create'}">
							<div class="page-heading">
								<h4 class="card-title">${productCodeByDrs} <spring:message code="product.addToNewMarketRegion" /></h4>
								
							</div>
							<c:url var="addAction" value="/ProductSkuRegionInfo/save"></c:url>
						</c:when>
						<c:otherwise>
							<div class="page-heading">
								<h4 class="card-title"><spring:message code="product.editMarketRegion" />
									:${ProductSkuRegionInfo.marketplace.name}
									${ProductSkuRegionInfo.productCodeByDrs}
									${ProductSkuRegionInfo.productNameBySupplier}
								</h4>
							</div>
							<c:url var="addAction" value="/ProductSkuRegionInfo/update"></c:url>
						</c:otherwise>
					</c:choose>
					<span class="text-danger"><spring:message code='product.tempValidation' /></span>
				</div>
			</div>
			<form:form action="${addAction}" class="form-horizontal text-left" modelAttribute="ProductSkuRegionInfo">
			<div class="row">
				<div class="col-md-6">

						<div class="form-group">
							<label for="marketplace"><spring:message
									code="common.marketplace" /></label>
							<c:choose>
								<c:when test="${type eq 'Create'}">
									<form:select id="marketplace" class="form-control"
										path="marketplace" ng-model="marketplace"
										ng-change="getMarketplaceCurrency(this)">
										<form:option value="">--- Select ---</form:option>
										<c:forEach var="marketplace" items="${marketplaceList}">
											<form:option value="${marketplace.name()}">${marketplace.name}</form:option>
										</c:forEach>
									</form:select>
								</c:when>
								<c:otherwise>
									<form:select id="marketplace" class="form-control"
										path="marketplace" ng-model="marketplace"
										ng-change="getMarketplaceCurrency(this)" disabled="true">
										<form:option value="">--- Select ---</form:option>
										<c:forEach var="marketplace" items="${marketplaceList}">
											<form:option value="${marketplace.name()}">${marketplace.name}</form:option>
										</c:forEach>
									</form:select>
									<form:hidden id="marketplace" path="marketplace" />
								</c:otherwise>
							</c:choose>
							<form:hidden id="currency" path="currency" />
							<form:hidden id="productCodeByDrs" path="productCodeByDrs"
								value="${productCodeByDrs}" />
						</div>
						<div class="form-group">
							<label for="status"><spring:message
									code="product.marketplaceStatus" /></label>
							<form:select id="status" class="form-control" path="status">
								<form:option name="" value="" label="--- Select ---" />
								<c:forEach var="productSkuRegionStatus"
									items="${productSkuRegionStatusList}">
									<form:option value="${productSkuRegionStatus}">
										<spring:message code="${productSkuRegionStatus}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group">
							<label for="marketSku"><spring:message
									code="product.marketplaceSKU" /></label>
							<c:choose>
								<c:when test="${type eq 'Create'}">
									<form:input id="marketSku" class="form-control"
										path="marketplaceSku" />
								</c:when>
								<c:otherwise>
									<form:input id="marketSku" class="form-control"
										path="marketplaceSku" disabled="true" />
									<form:hidden path="marketplaceSku" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="form-group">
							<label for="MSRP"><spring:message code="product.MSRP" />
								<span id="MSRP-currency"></span></label>
							<form:input id="" class="form-control" path="MSRP" />
						</div>
						<div class="form-group">
							<label for="supplierSuggestedBaseRetailPrice"><spring:message
									code="product.supplierSuggestedBaseRetailPrice" /><span
								id="supplierSuggestedBaseRetailPrice-currency"></span></label>
							<form:input id="supplierSuggestedBaseRetailPrice"
								class="form-control" path="supplierSuggestedBaseRetailPrice" />
						</div>
						<div class="form-group">
							<label for="currentBaseRetailPrice"> <spring:message
									code="product.currentBaseRetailPrice" /><span
								id="currentBaseRetailPrice-currency"></span></label>
							<form:input id="currentBaseRetailPrice" class="form-control"
								path="currentBaseRetailPrice" ng-model="currentBaseRetailPrice"
								ng-change="calculateEstimatedDrsRetainment(this)" />
						</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label for="estimatedDrsRetainment"><spring:message
								code="product.estimatedDrsSRetainment" /><span
							id="estimatedDrsSRetainment-currency"></span></label>
						<form:input id="estimatedDrsRetainment" class="form-control"
							path="estimatedDrsRetainment" ng-model="estimatedDrsRetainment"
							readonly="true" />
					</div>
					<div class="form-group">
						<label for="referralRate"><spring:message
								code="product.estimatedMarketplaceCommissionRate" /></label>
						<form:input id="referralRate" class="form-control"
							path="referralRate" ng-model="referralRate"
							ng-change="calculateEstimatedMarketplaceFees(this)" />
					</div>
					<div class="form-group">
						<label for="estimatedMarketplaceFees"><spring:message
								code="product.estimatedMarketplaceFees" /><span
							id="estimatedMarketplaceFees-currency"></span></label>
						<form:input class="form-control" path="estimatedMarketplaceFees"
							ng-model="estimatedMarketplaceFees" readonly="true" />
					</div>
					<div class="form-group">
						<spring:message code="product.estimatedFulfillmentFees" />
						<span id="estimatedFulfillmentFees-currency"></span>

						<form:input class="form-control" path="estimatedFulfillmentFees"
							ng-model="estimatedFulfillmentFees"
							ng-change="calculateApproxSupplierNetRevenue(this)" />

					</div>
					<div class="form-group">
						<label for="estimatedImportDuty"><spring:message
								code="product.estimatedImportDuty" /> <span
							id="estimatedImportDuty-currency"></span></label>

						<form:input id="estimatedImportDuty" class="form-control"
							path="estimatedImportDuty" ng-model="estimatedImportDuty"
							ng-change="calculateApproxSupplierNetRevenue(this)" />
					</div>
					<div class="form-group">
						<label for="estimatedFreightCharge"><spring:message
								code="product.estimatedFreightCharge" /> <span
							id="estimatedFreightCharge-currency"></span></label>
						<form:input class="form-control" path="estimatedFreightCharge"
							ng-model="estimatedFreightCharge"
							ng-change="calculateApproxSupplierNetRevenue(this)" />
					</div>
					<div class="form-group">
						<label for="approxSupplierNetRevenue"><spring:message
								code="product.approxSupplierNetRevenue" /> <span
							id="approxSupplierNetRevenue-currency"></span> </label>
						<form:input id="approxSupplierNetRevenue" class="form-control"
							path="approxSupplierNetRevenue"
							ng-model="approxSupplierNetRevenue" readonly="true" />
					</div>

					<div style="text-align:right;">
						<!-- <a id="cancel" href="${pageContext.request.contextPath}/Products" class="btn btn-default mx-1">
							<spring:message code="product.cancel"/>
						</a> -->
						<a id="cancel" href="javascript:history.back()" class="btn btn-default mx-1">
							<spring:message code="product.cancel"/>
						</a> 
						<input style="height: 39px!important;" class="btn btn-primary" type="submit" value="<spring:message code="product.submit" />" />
					</div>
				</div>
			</form:form>
			</div>
		</div>
	</div>
</div>