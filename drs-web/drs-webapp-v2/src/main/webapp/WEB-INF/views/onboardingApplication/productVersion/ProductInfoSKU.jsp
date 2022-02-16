<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<spring:message code="productVersion.changeSKU" /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	angular.module('baseProductOnboardingWithSKU', []).controller('baseProductOnboardingWithSKUCtrl', function($scope) {
		var BaseProductOnboardingWithSKUJson = ${BaseProductOnboardingWithSKUJson};
		$scope.variationType1 = BaseProductOnboardingWithSKUJson.variationType1;
		$scope.variationType2 = BaseProductOnboardingWithSKUJson.variationType2;
		$scope.productWithVariation = BaseProductOnboardingWithSKUJson.productWithVariation;
		$scope.SKUValueDuplicated = false;		
		var lineItems = BaseProductOnboardingWithSKUJson.skulineItems;
		$scope.SKULineItems = [];		
		for (i = 0; i < lineItems.length; i++) { 
			$scope.SKULineItems.push({
				index:lineItems[i].index,
				originalSKU:lineItems[i].originalSKU,
				updatedSKU:lineItems[i].updatedSKU,
				type1:lineItems[i].type1,
				type1Value:lineItems[i].type1Value,
				type2:lineItems[i].type2,
				type2Value:lineItems[i].type2Value,
				GTINValue:lineItems[i].gtinvalue,
				applicableRegionList:lineItems[i].applicableRegionList				
			});				
		}
		
		$scope.checkSKUValueDuplicated = function(){			
			var SKUValueArray = [];			
			for (var SKULineItem in $scope.SKULineItems) {
				if ($scope.SKULineItems.hasOwnProperty(SKULineItem)) {															
					if($scope.SKULineItems[SKULineItem]["updatedSKU"] != ""){						
						SKUValueArray.push($scope.SKULineItems[SKULineItem]["updatedSKU"]);						
					}																								
				}																						
			}			
			$scope.SKUValueDuplicated = hasDuplicates(SKUValueArray);						
		};

		function hasDuplicates(array) {
		    return (new Set(array)).size !== array.length;
		}
		
	});
</script>
</head>
<div class="max-width" ng-app="baseProductOnboardingWithSKU" ng-controller="baseProductOnboardingWithSKUCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
				<spring:message code="productVersion.changeSKU" />
				</div>
			</div>
		</div>	
		<form:form action="${pageContext.request.contextPath}/CoreProductInformation/SKUs/update" name="BaseProductOnboardingWithSKU" modelAttribute="BaseProductOnboardingWithSKU">							
		<div class="row">
			<div class="col-md-12">	
				<label class="control-label"><spring:message code="productInfoSourceVersion.supplier" /></label>
				${BaseProductOnboardingWithSKU.supplierKcode}		
				<form:hidden path="supplierKcode"/>			
				<label class="control-label"><spring:message code="productInfoSourceVersion.baseProductCode" /></label>
				${BaseProductOnboardingWithSKU.productBaseCode}
				<form:hidden path="productBaseCode"/>
				<table class="table">
					<thead>
						<tr>
							<th ng-if='productWithVariation== "1" && variationType1!=""'>						
								<span ng-if='variationType1== "color" '><spring:message code="VariationType.color" /></span>												
								<span ng-if='variationType1=="sizeCapacity"'><spring:message code="VariationType.sizeCapacity" /></span>												
								<span ng-if='variationType1=="packageQuantity"'><spring:message code="VariationType.packageQuantity" /></span>				
								<span ng-if='variationType1=="material"'><spring:message code="VariationType.material" /></span>																		
								<span ng-if='variationType1=="regionalSpecification"'><spring:message code="VariationType.regionalSpecification" /></span>
							</th>
							<th ng-if='productWithVariation== "1" && variationType2!=""'>
								<span ng-if='variationType2== "color" '><spring:message code="VariationType.color" /></span>												
								<span ng-if='variationType2=="sizeCapacity"'><spring:message code="VariationType.sizeCapacity" /></span>												
								<span ng-if='variationType2=="packageQuantity"'><spring:message code="VariationType.packageQuantity" /></span>				
								<span ng-if='variationType2=="material"'><spring:message code="VariationType.material" /></span>																		
								<span ng-if='variationType2=="regionalSpecification"'><spring:message code="VariationType.regionalSpecification" /></span>						
							</th>
							<th><spring:message code="productInfoSourceVersion.GTIN" /></th>
							<th><spring:message code="productInfoSourceVersion.applicableRegion" /></th>
							<th><spring:message code="productVersion.oldSupplierSKUcode" /></th>
							<th>
								<spring:message code="productVersion.newSupplierSKUcode" />
								<div class="text-danger" ng-show="SKUValueDuplicated">									
									<spring:message code="productInfoSourceVersion.SKU_duplicated" />
								</div>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr id="item{{$index}}" ng-repeat="SKULineItem in SKULineItems">
							<td ng-if='productWithVariation== "1" && variationType1!=""'>
								{{SKULineItem.type1Value}}
							</td>
							<td ng-if='productWithVariation== "1" && variationType2!=""'>
								{{SKULineItem.type2Value}}
							</td>
							<td>{{SKULineItem.GTINValue}}</td>
							<td>
								<span ng-repeat="region in SKULineItem.applicableRegionList">				
									<span ng-if='region == "CA"'><spring:message code="CA" /></span>
									<span ng-if='region == "DE"'><spring:message code="DE" /></span>
									<span ng-if='region == "IT"'><spring:message code="IT" /></span>
									<span ng-if='region == "FR"'><spring:message code="FR" /></span>
									<span ng-if='region == "ES"'><spring:message code="ES" /></span>															
									<span ng-if='region == "UK"'><spring:message code="UK" /></span>
			    					<span ng-if='region == "US"'><spring:message code="US" /></span>								
								</span>
							</td>
							<td>
								<b>{{SKULineItem.originalSKU}}</b>
								<input type="hidden" name="sKULineItem[{{$id}}].originalSKU" value="{{SKULineItem.originalSKU}}">												
								<input type="hidden" name="sKULineItem[{{$id}}].index" value="{{SKULineItem.index}}">																			
							</td>
							<td>
								<input name="sKULineItem[{{$id}}].updatedSKU" class="form-control" style="width:90%;display: inline;" ng-model="SKULineItem.updatedSKU" ng-change="checkSKUValueDuplicated()" required>
								<span class="text-danger">*</span>
								<div class="text-danger" ng-show="BaseProductOnboardingWithSKU['sKULineItem[{{$id}}].updatedSKU'].$error.required && BaseProductOnboardingWithSKU['sKULineItem[{{$id}}].updatedSKU'].$dirty">
									<spring:message code="productInfoSourceVersion.sku_req" />									
								</div>												
							</td>				
						</tr>
					</tbody>
				</table>		
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-right">
				<a class="btn btn-success" href="${pageContext.request.contextPath}/CoreProductInformation/${BaseProductOnboardingWithSKU.productBaseCode}"><spring:message code="detailsOfBaseProduct.back" /></a>																
				<input class="btn btn-primary" type="submit" value="<spring:message code="detailsOfBaseProduct.update" />" ng-disabled="BaseProductOnboardingWithSKU.$invalid||SKUValueDuplicated" />										
			</div>
		</div>						
		</form:form>
	</div>
</div>