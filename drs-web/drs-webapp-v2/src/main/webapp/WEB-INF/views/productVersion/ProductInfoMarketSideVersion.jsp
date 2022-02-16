<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="${marketSideRegion}" /> - <spring:message code="detailsOfBaseProduct.productInformation" /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	$(document).ready( function() {
		var editing = false;
		$('input,textarea,select').each(function(){
			$(this).on('change',function(){
				editing = true;
			})
		});
		$('ul.nav > li > a, span.btn > a, .breadcrumb > a, a.btn-success,.menu a,.S5_submenu_item a,footer a').on('click',function(e){
			if(editing) {
				e.preventDefault();
				$('#confirmOK').attr('href',$(this).attr('href'));
				$('#confirmLink').modal('show');
			}
		});
	});
</script>
<script>
	angular.module('productInfoMarketSideVersion', []).controller('productInfoMarketSideVersionCtrl', [ '$scope','$http',function($scope, $http) {
		var product  = ${product};
		var productInfoMarketSideVersion = JSON.parse(product["data"]);
		var baseProductCode = '${baseProductCode}';
		var supplierKcode = '${supplierKcode}';
		var marketSideRegion = '${marketSideRegion}';
		$scope.baseProductCode = baseProductCode;
		$scope.supplierKcode = supplierKcode;
		$scope.marketSideRegion = marketSideRegion;
		$scope.hsCode = productInfoMarketSideVersion["hsCode"];
		$scope.dutyRate = productInfoMarketSideVersion["dutyRate"];
		$scope.note = productInfoMarketSideVersion["note"];
		$scope.productLineItems = JSON.parse(productInfoMarketSideVersion["products"]);
		$scope.productWithVariation = productInfoMarketSideVersion["productWithVariation"];
		$scope.status = product["status"];

		$scope.applyProductSizePackageToAll = function(){
			var statusDisable= ["aborted"];
			if($("#packageDimension1ToApply").val().length == "" ||$.trim($("#packageDimension1ToApply").val()) === ""
				||$("#packageDimension2ToApply").val().length == "" ||$.trim($("#packageDimension2ToApply").val()) === ""
				||$("#packageDimension3ToApply").val().length == "" ||$.trim($("#packageDimension3ToApply").val()) === ""
				||$("#packageWeightToApply").val().length == "" ||$.trim($("#packageWeightToApply").val()) === ""
				|| $("#packageDimensionUnitToApply").val() ==""
				|| $("#packageWeightUnitToApply").val() ==""
			){
				alert("<spring:message code="ErrorMessage.CheckingTextBox" />")
			}else{
				for(var productLineItem in $scope.productLineItems) {
					if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
						if(statusDisable.indexOf($scope.productLineItems[productLineItem]["status"]) == -1 ){
							$scope.productLineItems[productLineItem]["packageDimension1"] = $("#packageDimension1ToApply").val();
							$scope.productLineItems[productLineItem]["packageDimension2"] = $("#packageDimension2ToApply").val();
							$scope.productLineItems[productLineItem]["packageDimension3"] = $("#packageDimension3ToApply").val();
							$scope.productLineItems[productLineItem]["packageDimensionUnit"] = $("#packageDimensionUnitToApply").val();
							$scope.productLineItems[productLineItem]["packageWeight"] = $("#packageWeightToApply").val();
							$scope.productLineItems[productLineItem]["packageWeightUnit"] = $("#packageWeightUnitToApply").val();
							$scope.productLineItems[productLineItem]["plasticBagWarningLabel"] = $("#packagePlasticBagWarningLabelToApply").val();
						}
					}
				}
				$("#packageDimension1ToApply").val("");
				$("#packageDimension2ToApply").val("");
				$("#packageDimension3ToApply").val("");
				$("#packageDimensionUnitToApply").val("");
				$("#packageWeightToApply").val("");
				$("#packageWeightUnitToApply").val("");
				$("#packagePlasticBagWarningLabelToApply").val("");
			}
		};

		$scope.applyProductSizeCartonToAll = function(){
			var statusDisable= ["aborted"];
			if($("#cartonVariationProductQuantityToApply").val().length == "" ||$.trim($("#cartonVariationProductQuantityToApply").val()) === ""
				||$("#cartonPackageDimension1ToApply").val().length == "" ||$.trim($("#cartonPackageDimension1ToApply").val()) === ""
				||$("#cartonPackageDimension2ToApply").val().length == "" ||$.trim($("#cartonPackageDimension2ToApply").val()) === ""
				||$("#cartonPackageDimension3ToApply").val().length == "" ||$.trim($("#cartonPackageDimension3ToApply").val()) === ""
				||$("#cartonPackageWeightToApply").val().length == "" ||$.trim($("#cartonPackageWeightToApply").val()) === ""
				|| $("#cartonPackageDimensionUnitToApply").val() ==""
				|| $("#cartonPackageWeightUnitToApply").val() ==""
			){
				alert("<spring:message code="ErrorMessage.CheckingTextBox" />")
			}else{
				for(var productLineItem in $scope.productLineItems) {
					if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
						if(statusDisable.indexOf($scope.productLineItems[productLineItem]["status"]) == -1 ){
							$scope.productLineItems[productLineItem]["variationProductQuantity"] = $("#cartonVariationProductQuantityToApply").val();
							$scope.productLineItems[productLineItem]["cartonPackageDimension1"] = $("#cartonPackageDimension1ToApply").val();
							$scope.productLineItems[productLineItem]["cartonPackageDimension2"] = $("#cartonPackageDimension2ToApply").val();
							$scope.productLineItems[productLineItem]["cartonPackageDimension3"] = $("#cartonPackageDimension3ToApply").val();
							$scope.productLineItems[productLineItem]["cartonPackageDimensionUnit"] = $("#cartonPackageDimensionUnitToApply").val();
							$scope.productLineItems[productLineItem]["cartonPackageWeight"] = $("#cartonPackageWeightToApply").val();
							$scope.productLineItems[productLineItem]["cartonPackageWeightUnit"] = $("#cartonPackageWeightUnitToApply").val();
						}
					}
				}
				$("#cartonVariationProductQuantityToApply").val("");
				$("#cartonPackageDimension1ToApply").val("");
				$("#cartonPackageDimension2ToApply").val("");
				$("#cartonPackageDimension3ToApply").val("");
				$("#cartonPackageDimensionUnitToApply").val("");
				$("#cartonPackageWeightToApply").val("");
				$("#cartonPackageWeightUnitToApply").val("");
			}
		};

		$scope.getMSRPTax = function(country,item){
			var taxableCountryArray = ["UK","DE","IT","FR","ES"];
			$.ajax({
				url : "${pageContext.request.contextPath}/ProductInfoMarketSide/getPriceWithTax",
				type : 'get',
				data : {
					country : country,
					price : item.MSRP
				},
				contentType : 'application/json; charset=utf-8',
				success : function(MSRPTax) {
					if(taxableCountryArray.indexOf(country) != -1){
						var idxOfProductLineItems = $scope.productLineItems.indexOf(item);
						$scope.productLineItems[idxOfProductLineItems]['MSRPTax'] = MSRPTax;
						$scope.$apply();
					}
				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};

		$scope.getSSBPTax = function(country,item,marketplaceItem){
			var taxableCountryArray = ["UK","DE","IT","FR","ES"];
			$.ajax({
				url : "${pageContext.request.contextPath}/ProductInfoMarketSide/getPriceWithTax",
				type : 'get',
				data : {
					country : country,
					price : marketplaceItem.SSBP
				},
				contentType : 'application/json; charset=utf-8',
				success : function(SSBPTax) {
					if(taxableCountryArray.indexOf(country) != -1){
						var idxOfProductLineItems = $scope.productLineItems.indexOf(item);
						var idxOfMarketplaceItems = $scope.productLineItems[idxOfProductLineItems]['marketplace'].indexOf(marketplaceItem);
						$scope.productLineItems[idxOfProductLineItems]['marketplace'][idxOfMarketplaceItems]['SSBPTax'] = SSBPTax;
						$scope.$apply();
					}
				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};

		$scope.saveDraftForProductInfoMarketSide = function() {
			$scope.note = $('#note').val();
			var jsonData = {
				country : $scope.marketSideRegion,
				hsCode : $scope.hsCode,
				dutyRate : $scope.dutyRate,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};
			var formData = {
				productBaseCode : $scope.baseProductCode,
				supplierKcode: $scope.supplierKcode,
				country : $scope.marketSideRegion,
				jsonData : JSON.stringify(jsonData)
			};
			$.ajax({
				url : "${pageContext.request.contextPath}/saveDraftForMarketSideProductInformation",
				type : 'POST',
				data : JSON.stringify(formData),
				contentType : 'application/json',
				success : function(data) {
					window.location.href = "${pageContext.request.contextPath}"+ data;
				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};

		$scope.submitProductInfoMarketSide = function() {

			$scope.note = $('#note').val();

			var jsonData = {
				country : $scope.marketSideRegion,
				hsCode : $scope.hsCode,
				dutyRate : $scope.dutyRate,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};

			var formData = {
				productBaseCode : $scope.baseProductCode,
				supplierKcode: $scope.supplierKcode,
				country : $scope.marketSideRegion,
				jsonData : JSON.stringify(jsonData)
			};

			$.ajax({
				url : "${pageContext.request.contextPath}/submitMarketSideProductInformation",
				type : 'POST',
				data : JSON.stringify(formData),
				contentType : 'application/json',
				success : function(data) {

					window.location.href = "${pageContext.request.contextPath}"+ data;

				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};

		$scope.approveProductInfoMarketSide = function() {
			$scope.note = $('#note').val();
			var jsonData = {
				country : $scope.marketSideRegion,
				hsCode : $scope.hsCode,
				dutyRate : $scope.dutyRate,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};
			var formData = {
				productBaseCode : $scope.baseProductCode,
				supplierKcode: $scope.supplierKcode,
				country : $scope.marketSideRegion,
				jsonData : JSON.stringify(jsonData)
			};
			$.ajax({
				url : "${pageContext.request.contextPath}/approveMarketSideProductInformation",
				type : 'POST',
				data : JSON.stringify(formData),
				contentType : 'application/json',
				success : function(data) {
					window.location.href = "${pageContext.request.contextPath}"+ data;
				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};

		$scope.returnProductInfoMarketSide = function() {
			$scope.note = $('#note').val();
			var jsonData = {
				country : $scope.marketSideRegion,
				hsCode : $scope.hsCode,
				dutyRate : $scope.dutyRate,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};
			var formData = {
				productBaseCode : $scope.baseProductCode,
				supplierKcode: $scope.supplierKcode,
				country : $scope.marketSideRegion,
				jsonData : JSON.stringify(jsonData)
			};
			$.ajax({
				url : "${pageContext.request.contextPath}/returnMarketSideProductInformation",
				type : 'POST',
				data : JSON.stringify(formData),
				contentType : 'application/json',
				success : function(data) {
					window.location.href = "${pageContext.request.contextPath}"+ data;
				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};

		$scope.updateProductInfoMarketSide = function() {
			$scope.note = $('#note').val();
			var jsonData = {
				country : $scope.marketSideRegion,
				hsCode : $scope.hsCode,
				dutyRate : $scope.dutyRate,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
					productWithVariation : $scope.productWithVariation
			};
			var formData = {
				productBaseCode : $scope.baseProductCode,
				supplierKcode: $scope.supplierKcode,
				country : $scope.marketSideRegion,
				jsonData : JSON.stringify(jsonData)
			};
			$.ajax({
				url : "${pageContext.request.contextPath}/updateMarketSideProductInformation",
				type : 'POST',
				data : JSON.stringify(formData),
				contentType : 'application/json',
				success : function(data) {
					window.location.href = "${pageContext.request.contextPath}"+ data;
				},
				error : function(data, status,er) {
					console.log("error: "
						+ data
						+ " status: "
						+ status + " er:"
						+ er);
				}
			});
		};
	}]);
</script>
<!-- <link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">-->
<style type="text/css">
.disabledRow {
	opacity: 0.5;
	pointer-events: none;
}
</style>
<script type='text/javascript' src="<c:url value="/resources/js/bootstrap3-wysihtml5.all.js"/>"></script>
<link href="<c:url value="/resources/css/bootstrap3-wysihtml5.css"/>" type="text/css" rel="stylesheet">
</head>
<div class="row">
    <div class="col-md-12 max-width" >
    	<ol class="breadcrumb">
        	<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
        		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a></li>
        		<li class="breadcrumb-item"><span>${breadcrumbProduct}</span></li>
        		<li class="breadcrumb-item active"><span> [<spring:message code="detailsOfBaseProduct.edit" />] <spring:message code="detailsOfBaseProduct.productInformation" /> @<spring:message code="${marketSideRegion}" /></span></li>
        	</sec:authorize>
        	<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
        		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a></li>
        		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList?kcode=${supplierKcode}">${supplierKcode} <spring:message code="productVersion.supplierBaseProductList" /></a></li>
        		<li class="breadcrumb-item"><span>${breadcrumbProduct}</span></li>
        		<li class="breadcrumb-item active"><span> [<spring:message code="detailsOfBaseProduct.edit" />] <spring:message code="detailsOfBaseProduct.productInformation" /> @<spring:message code="${marketSideRegion}" /></span></li>
        	</sec:authorize>
    	</ol>
    </div>
</div>
<div class="max-width" ng-app="productInfoMarketSideVersion" ng-controller="productInfoMarketSideVersionCtrl">
	<div class="row">
		<div class="col-md-12">

			<div class="page-heading">
				${breadcrumbProduct} @<spring:message code="${marketSideRegion}" />
			</div>
			<div class="change_product float-right">
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
						<i class="fas fa-question-circle"></i> <spring:message code="detailsOfBaseProduct.help" />
					</a>
	  			</sec:authorize>
				<a class="btn btn-success" href="${pageContext.request.contextPath}/MarketSideProductInformation/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><spring:message code="detailsOfBaseProduct.back" /></a>
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
					<input type="button" class="btn btn-primary" ng-click="saveDraftForProductInfoMarketSide()" value="<spring:message code="detailsOfBaseProduct.saveDraft" />" />
					<button class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="submitProductInfoMarketSide()">
						<spring:message code="detailsOfBaseProduct.submit" />  <i class="fas fa-forward"></i>
					</button>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
					<input type="button" ng-show="status =='Pending DRS review' || status =='Finalized'" class="btn btn-primary" ng-click="updateProductInfoMarketSide()" value="<spring:message code="detailsOfBaseProduct.update" />" />
					<button ng-show="status =='Pending DRS review'" class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="returnProductInfoMarketSide()">
						<i class="fas fa-backward"></i>  <spring:message code="detailsOfBaseProduct.return" />
					</button>
					<button ng-show="status =='Pending DRS review'" class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="approveProductInfoMarketSide()">
						<spring:message code="detailsOfBaseProduct.approve" />  <i class="fas fa-forward"></i>
					</button>
				</sec:authorize>
			</div>

			<div class="btn-group">
			  <button id="kcode" type="button" class="btn btn-primary">${baseProductCode} </button>
			  <button id="menu_header" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    <spring:message code="detailsOfBaseProduct.productInformation" />
			  </button>
			  <div class="dropdown-menu">
			    <a class="dropdown-item menu-item" href="#"> <spring:message code="detailsOfBaseProduct.productInformation" /></a>
			    <a class="dropdown-item menu-item" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}"><spring:message code="detailsOfBaseProduct.productMarketingMaterial" /></a>
			  </div>
			  </div>

		</div>
	</div>


	<div class="row" style="padding:5px;">
		<div class="col-md-12">
			<span  style="margin:3px;font-size:9pt;padding:2px;" class="btn btn-default pull-right">
				<a href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}">
					<img style="width:16px;" src="<c:url value="/resources/images/UN.png"/>"> <spring:message code="detailsOfBaseProduct.productGeneralInformation" />
				</a>
			</span>
			<c:forEach items="${marketSideList}" var="marketSide">
				<c:if test="${marketSide eq marketSideRegion}">
					<span style="margin:3px;background-color:#cfcfcf;font-size:9pt;padding:2px;" class="btn btn-default disabled pull-right">
						<a href="${pageContext.request.contextPath}/MarketSideProductInformation/${baseProductCode}/${marketSide}?supplierKcode={{supplierKcode}}">
							<img style="width:16px;" src="<c:url value="/resources/images/${marketSide}.png"/>"> ${marketSide}
						</a>
					</span>
				</c:if>
				<c:if test="${marketSide ne marketSideRegion}">
					<span  style="margin:3px;font-size:9pt;padding:2px;" class="btn btn-default pull-right">
						<a href="${pageContext.request.contextPath}/MarketSideProductInformation/${baseProductCode}/${marketSide}?supplierKcode={{supplierKcode}}">
							<img style="width:16px;" src="<c:url value="/resources/images/${marketSide}.png"/>"> ${marketSide}
						</a>
					</span>
				</c:if>
			</c:forEach>
		</div>
	</div>
	<form name="productInfoMarketSide">
	<div class="row">
		<div class="col-md-12">
			<table class="table table-withoutBorder no-head" style="width:50%">
				<tr>
					<td class="text-right">
						<b><spring:message code="productInfoMarketSide.hSCodeForMarketSideRegion" /></b>
					</td>
					<td><input class="form-control" ng-model="hsCode"></td>
				</tr>
				<tr>
					<td class="text-right">
						<b><spring:message code="productInfoMarketSide.dutyRate" /></b>
					</td>
					<td>
						<input class="form-control" style="width: 25%;" name="dutyRate" ng-model="dutyRate" ng-pattern="/^(?:[0-9]\d*)?(?:\.\d+)?$/">
						<div class="text-danger" ng-show="productInfoMarketSide.dutyRate.$error.pattern">
							<spring:message code="productInfoMarketSide.dutyRate_format" />
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="page-heading">
				<spring:message code="productInfoMarketSide.editMarketplaceOfVariationProductTitle" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<table class="table">
				<thead>
					<tr>
						<th style="width:200px"><spring:message code="productInfoMarketSide.variationProduct" /></th>
						<th><spring:message code="productInfoMarketSide.marketplace" /></th>
					</tr>
				</thead>
				<tbody>
					<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
						<td>{{supplierKcode}}-{{productLineItem.SKU}}
							<span ng-if="productWithVariation=='1'"> ({{productLineItem.type1Value}}<span
										ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)</span>
						</td>
						<td>
							<span ng-repeat="marketplace in productLineItem.marketplace">
								<input type="checkbox" name="marketplace[]" ng-model="marketplace.applied" value="{{marketplace.name}}" ng-checked="marketplace.applied"> {{marketplace.name}}
							</span>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productInfoMarketSide.marketSidePriceTitle" /></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th rowspan="2" style="width:200px"><spring:message code="productInfoMarketSide.variationProduct" /></th>
							<th colspan="2" class="text-center"><spring:message code="productInfoMarketSide.MSRP" /></th>
							<th colspan="2" class="text-center"><spring:message code="productInfoMarketSide.SSBP" /></th>
							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DDP_PRICE']}')">
								<th rowspan="2" class="text-right" style="width:100px"><spring:message code="productInfoMarketSide.DDPUnitPrice" /></th>
							</sec:authorize>
						</tr>
						<tr class="success">
							<th class="text-center" style="width:150px"><spring:message code="productInfoMarketSide.withoutTax" /></th>
							<th class="text-center" style="width:150px"><spring:message code="productInfoMarketSide.withTax" /></th>
							<th class="text-center" style="width:200px"><spring:message code="productInfoMarketSide.withoutTax" /></th>
							<th class="text-center" style="width:150px"><spring:message code="productInfoMarketSide.withTax" /></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td style="width:200px">
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'"> ({{productLineItem.type1Value}}<span ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)</span>
							</td>
							<td class="text-center" style="width:150px">
								<input id="MSRP{{$id}}" class="form-control" name="MSRP{{$id}}" style="width: 50px; display: inline;"
									ng-model="productLineItem.MSRP" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" ng-change="getMSRPTax('${marketSideRegion}',productLineItem)"> ${regionToCurrencyMap[marketSideRegion]}
								<div class="text-danger" ng-show="productInfoMarketSide.MSRP{{$id}}.$error.pattern">
									<spring:message code="productInfoMarketSide.MSRP_format" />
								</div>
							</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${marketSideRegion == 'US' || marketSideRegion == 'CA'}">
										-
									</c:when>
									<c:otherwise>
										<input id="MSRPtax{{$id}}" class="form-control" style="width: 50px; display: inline;text-align:right;" ng-model="productLineItem.MSRPTax" readonly> ${regionToCurrencyMap[marketSideRegion]}
									</c:otherwise>
								</c:choose>
							</td>
							<td class="text-center">
								<span ng-repeat="marketplace in productLineItem.marketplace">
									{{marketplace.name}} <input id="SSBP{{$id}}" class="form-control" name="SSBP{{$id}}" style="width: 50px; display: inline;text-align:right;" ng-model="marketplace.SSBP" ng-disabled="!marketplace.applied" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" ng-change="getSSBPTax('${marketSideRegion}',productLineItem,marketplace)"> ${regionToCurrencyMap[marketSideRegion]}
									<div class="text-danger" ng-show="productInfoMarketSide.SSBP{{$id}}.$error.pattern">
										<spring:message code="productInfoMarketSide.SSBP_format" />
									</div>
								</span>
							</td>
							<td class="text-center">
							<c:choose>
								<c:when test="${marketSideRegion == 'US' || marketSideRegion == 'CA'}">
									<span ng-repeat="marketplace in productLineItem.marketplace">
										 -
									</span>
								</c:when>
								<c:otherwise>
									<span ng-repeat="marketplace in productLineItem.marketplace">
										{{marketplace.name}} <input id="SSBPtax{{$id-1}}" class="form-control" style="width: 50px; display: inline;text-align:right;" ng-model="marketplace.SSBPTax" readonly> ${regionToCurrencyMap[marketSideRegion]}
									</span>
								</c:otherwise>
							</c:choose>
							</td>
							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DDP_PRICE']}')">
								<td class="text-right">
									<input id="DDPprice{{$id}}" class="form-control" name="DDPprice{{$id}}" style="width: 50px; display: inline;text-align:right;"
										ng-model="productLineItem.DDPprice" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" > ${regionToCurrencyMap[marketSideRegion]}
									<div class="text-danger" ng-show="productInfoMarketSide.DDPprice{{$id}}.$error.pattern">
										<spring:message code="productInfoMarketSide.DDPPrice_format" />
									</div>
								</td>
							</sec:authorize>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoMarketSide.packageTitle" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 200px;"><spring:message code="productInfoMarketSide.variationProduct" /></th>
							<th><spring:message code="productInfoMarketSide.packageDimension" /></th>
							<th><spring:message code="productInfoMarketSide.packageWeight" /></th>
							<th><spring:message code="productInfoMarketSide.plasticBagWarningLabel" /></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr class="success">
							<td style="width: 200px;"></td>
							<td>
								<input id="packageDimension1ToApply" name="packageDimension1ToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="packageDimension1ToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									id="packageDimension2ToApply" name="packageDimension2ToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="packageDimension2ToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									id="packageDimension3ToApply" name="packageDimension3ToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="packageDimension3ToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select id="packageDimensionUnitToApply" class="form-control" style="width: 120px; display: inline;">
									<option value="">--- Select ---</option>
									<option value="cm"><spring:message code="Dimension.cm" /></option>
									<option value="m"><spring:message code="Dimension.m" /></option>
									<option value="in"><spring:message code="Dimension.in" /></option>
									<option value="ft"><spring:message code="Dimension.ft" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.packageDimension1ToApply.$error.pattern || productInfoMarketSide.packageDimension2ToApply.$error.pattern || productInfoMarketSide.packageDimension3ToApply.$error.pattern">
									<spring:message code="productInfoMarketSide.packageDimension_format" />
								</div>
							</td>
							<td>
								<input id="packageWeightToApply" name="packageWeightToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="packageWeightToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select id="packageWeightUnitToApply" class="form-control" style="width: 120px; display: inline;">
									<option value="">--- Select ---</option>
									<option value="g"><spring:message code="Weight.g" /></option>
									<option value="kg"><spring:message code="Weight.kg" /></option>
									<option value="oz"><spring:message code="Weight.oz" /></option>
									<option value="lb"><spring:message code="Weight.lb" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.packageWeightToApply.$error.pattern">
									<spring:message code="productInfoMarketSide.packageWeight_format" />
								</div>
							</td>
							<td>
								<select id="packagePlasticBagWarningLabelToApply" class="form-control" style= "width:120px">
									<option value="No"><spring:message code="productInfoMarketSide.no" /></option>
									<option value="smaller12"><spring:message code="productInfoMarketSide.smaller12" /></option>
									<option value="larger12"><spring:message code="productInfoMarketSide.larger12" /></option>
								</select>
							</td>
							<td>
								<button type="button" class="btn btn-default" ng-click="applyProductSizePackageToAll()" ng-disabled="productInfoMarketSide.packageDimension1ToApply.$error.pattern || productInfoMarketSide.packageDimension2ToApply.$error.pattern || productInfoMarketSide.packageDimension3ToApply.$error.pattern || productInfoMarketSide.packageWeightToApply.$error.pattern">
									<span><spring:message code="productInfoMarketSide.applyToAll" /></span>
								</button>
							</td>
						</tr>
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td style="width:200px">
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'"> ({{productLineItem.type1Value}}<span ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)</span>
							</td>
							<td>
								<input
									class="form-control" name="packageDimension1{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.packageDimension1" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									class="form-control" name="packageDimension2{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.packageDimension2" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									class="form-control" name="packageDimension3{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.packageDimension3" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select
									class="form-control" style="width: 120px; display: inline;"
									ng-model="productLineItem.packageDimensionUnit">
										<option value="">--- Select ---</option>
										<option value="cm"><spring:message code="Dimension.cm" /></option>
										<option value="m"><spring:message code="Dimension.m" /></option>
										<option value="in"><spring:message code="Dimension.in" /></option>
										<option value="ft"><spring:message code="Dimension.ft" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.packageDimension1{{$id}}.$error.pattern || productInfoMarketSide.packageDimension2{{$id}}.$error.pattern || productInfoMarketSide.packageDimension3{{$id}}.$error.pattern">
									<spring:message code="productInfoMarketSide.packageDimension_format" />
								</div>
							</td>
							<td>
								<input
									class="form-control" name="packageWeight{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.packageWeight" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select
									class="form-control" style="width: 120px; display: inline;"
									ng-model="productLineItem.packageWeightUnit">
										<option value="">--- Select ---</option>
										<option value="g"><spring:message code="Weight.g" /></option>
										<option value="kg"><spring:message code="Weight.kg" /></option>
										<option value="oz"><spring:message code="Weight.oz" /></option>
										<option value="lb"><spring:message code="Weight.lb" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.packageWeight{{$id}}.$error.pattern">
									<spring:message code="productInfoMarketSide.packageWeight_format" />
								</div>
							</td>
							<td>
								<select
									class="form-control"
									ng-model="productLineItem.plasticBagWarningLabel" style="width:120px">
										<option value="No"><spring:message code="productInfoMarketSide.no" /></option>
										<option value="smaller12"><spring:message code="productInfoMarketSide.smaller12" /></option>
										<option value="larger12"><spring:message code="productInfoMarketSide.larger12" /></option>
								</select>
								<div class="text-danger" ng-show="productLineItem.plasticBagWarningLabel =='larger12'">
									<spring:message code="productInfoMarketSide.plasticBagWarningLabelHint" />
								</div>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoMarketSide.cartonTitle" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 200px;"><spring:message code="productInfoMarketSide.variationProduct" /></th>
							<th style="width: 60px;"><spring:message code="productInfoMarketSide.variationProductQuantity" /></th>
							<th><spring:message code="productInfoMarketSide.cartonDimension" /></th>
							<th><spring:message code="productInfoMarketSide.cartonWeight" /></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr class="success">
							<td></td>
							<td>
								<input id="cartonVariationProductQuantityToApply" name="cartonVariationProductQuantityToApply" class="form-control" style="width: 45px;" ng-model="cartonVariationProductQuantityToApply" ng-pattern="/^[1-9][0-9]*$/">
								<div class="text-danger" ng-show="productInfoMarketSide.cartonVariationProductQuantityToApply.$error.pattern">
									<spring:message code="productInfoMarketSide.variationProductQuantity_format" />
								</div>
							</td>
							<td>
								<input id="cartonPackageDimension1ToApply" name="cartonPackageDimension1ToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="cartonPackageDimension1ToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									id="cartonPackageDimension2ToApply" name="cartonPackageDimension2ToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="cartonPackageDimension2ToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									id="cartonPackageDimension3ToApply" name="cartonPackageDimension3ToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="cartonPackageDimension3ToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select id="cartonPackageDimensionUnitToApply" class="form-control"
									style="width: 120px; display: inline;">
										<option value="">--- Select ---</option>
										<option value="cm"><spring:message code="Dimension.cm" /></option>
										<option value="m"><spring:message code="Dimension.m" /></option>
										<option value="in"><spring:message code="Dimension.in" /></option>
										<option value="ft"><spring:message code="Dimension.ft" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.cartonPackageDimension1ToApply.$error.pattern || productInfoMarketSide.cartonPackageDimension2ToApply.$error.pattern || productInfoMarketSide.cartonPackageDimension3ToApply.$error.pattern">
									<spring:message code="productInfoMarketSide.cartonDimension_format" />
								</div>
							</td>
							<td>
								<input id="cartonPackageWeightToApply" name="cartonPackageWeightToApply" class="form-control"
									style="width: 45px; display: inline;" ng-model="cartonPackageWeightToApply" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select id="cartonPackageWeightUnitToApply" class="form-control"
									style="width: 120px; display: inline;">
										<option value="">--- Select ---</option>
										<option value="g"><spring:message code="Weight.g" /></option>
										<option value="kg"><spring:message code="Weight.kg" /></option>
										<option value="oz"><spring:message code="Weight.oz" /></option>
										<option value="lb"><spring:message code="Weight.lb" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.cartonPackageWeightToApply.$error.pattern">
									<spring:message code="productInfoMarketSide.cartonWeight_format" />
								</div>
							</td>
							<td>
								<button type="button" class="btn btn-default" ng-click="applyProductSizeCartonToAll()" ng-disabled="productInfoMarketSide.cartonVariationProductQuantityToApply.$error.pattern || productInfoMarketSide.cartonPackageDimension1ToApply.$error.pattern || productInfoMarketSide.cartonPackageDimension2ToApply.$error.pattern || productInfoMarketSide.cartonPackageDimension3ToApply.$error.pattern || productInfoMarketSide.cartonPackageWeightToApply.$error.pattern">
									<span><spring:message code="productInfoMarketSide.applyToAll" /></span>
								</button>
							</td>
						</tr>
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td style="width:200px">
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'"> ({{productLineItem.type1Value}}<span ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)</span>
							</td>
							<td>
								<input class="form-control" style="width: 45px;" name="variationProductQuantity{{$id}}" ng-model="productLineItem.variationProductQuantity" ng-pattern="/^[1-9][0-9]*$/">
								<div class="text-danger" ng-show="productInfoMarketSide.variationProductQuantity{{$id}}.$error.pattern">
									<spring:message code="productInfoMarketSide.variationProductQuantity_format" />
								</div>
							</td>
							<td>
								<input
									class="form-control" name="cartonPackageDimension1{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.cartonPackageDimension1" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									class="form-control" name="cartonPackageDimension2{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.cartonPackageDimension2" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									class="form-control" name="cartonPackageDimension3{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.cartonPackageDimension3" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select
									class="form-control" style="width: 120px; display: inline;"
									ng-model="productLineItem.cartonPackageDimensionUnit">
										<option value="">--- Select ---</option>
										<option value="cm"><spring:message code="Dimension.cm" /></option>
										<option value="m"><spring:message code="Dimension.m" /></option>
										<option value="in"><spring:message code="Dimension.in" /></option>
										<option value="ft"><spring:message code="Dimension.ft" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.cartonPackageDimension1{{$id}}.$error.pattern || productInfoMarketSide.cartonPackageDimension2{{$id}}.$error.pattern || productInfoMarketSide.cartonPackageDimension3{{$id}}.$error.pattern">
									<spring:message code="productInfoMarketSide.cartonDimension_format" />
								</div>
							</td>
							<td>
								<input
									class="form-control" name="cartonPackageWeight{{$id}}" style="width: 45px; display: inline;"
									ng-model="productLineItem.cartonPackageWeight" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">
								<select
									class="form-control" style="width: 120px; display: inline;"
									ng-model="productLineItem.cartonPackageWeightUnit">
										<option value="">--- Select ---</option>
										<option value="g"><spring:message code="Weight.g" /></option>
										<option value="kg"><spring:message code="Weight.kg" /></option>
										<option value="oz"><spring:message code="Weight.oz" /></option>
										<option value="lb"><spring:message code="Weight.lb" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoMarketSide.cartonPackageWeight{{$id}}.$error.pattern">
									<spring:message code="productInfoMarketSide.cartonWeight_format" />
								</div>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoMarketSide.unfulfillableInventoryTitle" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th style="width:200px"><spring:message code="productInfoMarketSide.variationProduct" /></th>
							<th><spring:message code="productInfoMarketSide.disposeOfUnfulfillableInventory" /></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-class="{'disabledRow':productLineItem.status=='unallowed'||productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td style="width:200px">
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'"> ({{productLineItem.type1Value}}<span ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)</span>
							</td>
							<td>
								<input type="radio" name="unfulfillableInventory{{$index}}" ng-model="productLineItem.disposeOfUnfulfillableInventory" ng-checked="productLineItem.disposeOfUnfulfillableInventory == 'remove'" value="remove"> <spring:message code="productInfoMarketSide.remove" />
								<input type="radio" name="unfulfillableInventory{{$index}}" ng-model="productLineItem.disposeOfUnfulfillableInventory" ng-checked="productLineItem.disposeOfUnfulfillableInventory == 'dispose'" value="dispose"> <spring:message code="productInfoMarketSide.dispose" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoMarketSide.note" />
				</div>
			</div>
		</div>
		<div id="noteRow" class="row">
			<div class="col-md-12">
				<textarea id="note" class="form-control" rows="10" ng-model="note">${comment}</textarea>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12 text-right">
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
						<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />
					</a>
	  			</sec:authorize>
				<a class="btn btn-success" href="${pageContext.request.contextPath}/MarketSideProductInformation/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><spring:message code="detailsOfBaseProduct.back" /></a>
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
					<input type="button" class="btn btn-primary" ng-click="saveDraftForProductInfoMarketSide()"
						value="<spring:message code="detailsOfBaseProduct.saveDraft" />" />
					<button class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="submitProductInfoMarketSide()">
						<spring:message code="detailsOfBaseProduct.submit" />  <i class="fas fa-forward"></i>
					</button>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
					<input type="button" ng-show="status =='Pending DRS review' || status =='Finalized'" class="btn btn-primary" ng-click="updateProductInfoMarketSide()"
						value="<spring:message code="detailsOfBaseProduct.update" />" />
					<button ng-show="status =='Pending DRS review'" class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="returnProductInfoMarketSide()">
						<i class="fas fa-backward"></i>  <spring:message code="detailsOfBaseProduct.return" />
					</button>
					<button ng-show="status =='Pending DRS review'" class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="approveProductInfoMarketSide()">
						<spring:message code="detailsOfBaseProduct.approve" />  <i class="fas fa-forward"></i>
					</button>
				</sec:authorize>
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
	$('#note').wysihtml5({
    	"events": {
        	"load": function() {
            	$("#noteRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");
        	}
    	},toolbar: {
	  		"html": true, //Button which allows you to edit the generated HTML. Default false
    		"link": true, //Button to insert a link. Default true
    		"image": false, //Button to insert an image. Default true,
    		"blockquote": false, //Blockquote
    		"font-styles": false, //Font styling, e.g. h1, h2, etc. Default true
    		"lists": false, //(Un)ordered lists, e.g. Bullets, Numbers. Default true
    		"emphasis": false, // Italics, bold, etc.
    		"fa": true
	  	}
	});
</script>
<div class="modal fade" id="confirmLink" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="background-color:#3d85c6;color:#fff;font-size:14pt;">
                <spring:message code="productInfo.notSavedReminder.title" />
            </div>
            <div class="modal-body" style="font-size:12pt;">
               <spring:message code="productInfo.notSavedReminder.msg" />
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="productInfo.notSavedReminder.cancel" /></button>
                <a id="confirmOK" class="btn btn-danger btn-ok"><spring:message code="productInfo.notSavedReminder.discard" /></a>
            </div>
        </div>
    </div>
</div>