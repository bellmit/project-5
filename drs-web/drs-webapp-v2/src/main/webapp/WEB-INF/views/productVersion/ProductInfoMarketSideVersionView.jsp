<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="${marketSideRegion}" /> - <spring:message code="detailsOfBaseProduct.productInformation" /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	angular.module('productInfoMarketSideView', []).controller('productInfoMarketSideViewCtrl', function($scope) {
		var product = ${product};
		var productInfoMarketSideVersion = JSON.parse(product["data"]);
		var baseProductCode = '${baseProductCode}';
		var supplierKcode = '${supplierKcode}';
		var marketSideRegion = '${marketSideRegion}';
		$scope.status = product["status"];
		$scope.baseProductCode = baseProductCode;
		$scope.supplierKcode = supplierKcode;
		$scope.marketSideRegion = marketSideRegion;
		$scope.hsCode = productInfoMarketSideVersion["hsCode"];
		$scope.dutyRate = productInfoMarketSideVersion["dutyRate"];
		$scope.note = productInfoMarketSideVersion["note"];
		$("#note").append($scope.note);
		$scope.productLineItems = JSON.parse(productInfoMarketSideVersion["products"]);
		$scope.productWithVariation = productInfoMarketSideVersion["productWithVariation"];
	});
</script>

<style type="text/css">
.disabledRow {
	opacity: 0.5;
	pointer-events: none;
}
</style>
</head>
<div class="row">
    <div class="col-md-12 max-width">
    	<ol class="breadcrumb">
        	<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
        		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a></li>
        		<li class="breadcrumb-item"><span>${breadcrumbProduct}</span></li>
        		<li class="breadcrumb-item active"><span><spring:message code="detailsOfBaseProduct.productInformation" /> @<spring:message code="${marketSideRegion}" /></span></li>
        	</sec:authorize>
        	<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
        		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a></li>
        		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList?kcode=${supplierKcode}">${supplierKcode} <spring:message code="productVersion.supplierBaseProductList" /></a></li>
        		<li class="breadcrumb-item"><span>${breadcrumbProduct}</span></li>
        		<li class="breadcrumb-item active"><span><spring:message code="detailsOfBaseProduct.productInformation" /> @<spring:message code="${marketSideRegion}" /></span></li>
        	</sec:authorize>
    	</ol>
    </div>
</div>
<div class="max-width" ng-app="productInfoMarketSideView" ng-controller="productInfoMarketSideViewCtrl">
	<div class="container-fluid">
			<div class="col-md-12">

				<div class="page-heading">
					${breadcrumbProduct} @<spring:message code="${marketSideRegion}" />
				</div>

				<div class="change_product float-right">
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
							<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />
						</a>
	  				</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="detailsOfBaseProduct.backToList" /></a>
	  				</sec:authorize>
	  				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
	  					<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList?kcode={{supplierKcode}}"><spring:message code="detailsOfBaseProduct.backToList" /></a>
	  				</sec:authorize>
   					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_EDIT']}')">
						<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
							<a class="btn btn-primary" ng-show="status =='Pending supplier action'" href="${pageContext.request.contextPath}/EditMarketSideProductInformation/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
							<a class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" href="${pageContext.request.contextPath}/EditMarketSideProductInformation/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>
						</sec:authorize>
					</sec:authorize>
				</div>
			</div>
		</div>
		<div class="row">
		 <div class="col-md-12">
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
			  <span id="pStatus"><spring:message code="productOnBoardingStatus.${status}" /></span>
			  </div>
			  </div>

		<div class="row" style="padding:5px;">
			<div class="col-md-12">
				<span style="margin:3px;font-size:9pt;padding:2px;" class="btn btn-default pull-right">
					<a href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><img style="width:16px;" src="<c:url value="/resources/images/UN.png"/>"> <spring:message code="detailsOfBaseProduct.productGeneralInformation" /> </a>
				</span>
				<c:forEach items="${marketSideList}" var="marketSide">
				<c:if test="${marketSide eq marketSideRegion}">
					<span  style="margin:3px;background-color:#cfcfcf;font-size:9pt;padding:2px;" class="btn btn-default disabled pull-right">
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
		<div class="row">
			<div class="col-md-12">
				<table class="table no-head" style="width:50%">
					<tr>
						<td class="text-right">
							<b><spring:message code="productInfoMarketSide.hSCodeForMarketSideRegion" /></b>
						</td>
						<td>
							<span ng-if="hsCode === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="hsCode != ''">{{hsCode}}</span>
						</td>
					</tr>
					<tr>
					<td class="text-right">
						<b><spring:message code="productInfoMarketSide.dutyRate" /></b>
					</td>
					<td>
						<span ng-if="dutyRate === ''"><spring:message code="productVersion.none" /></span>
						<span ng-if="dutyRate != ''">{{dutyRate}}</span>
					</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoMarketSide.marketplaceOfVariationProductTitle" />
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
							<td>
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</td>
							<td>
								<span ng-repeat="marketplace in productLineItem.marketplace">
									<span ng-if="marketplace.applied != false">{{marketplace.name}} </span>
									<span ng-if="marketplace.applied == false"><spring:message code="productVersion.none" /></span>
								</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoMarketSide.marketSidePriceTitle" />
				</div>
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
							<td style="width:200px">{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</td>
							<td class="text-center" style="width:150px">
								<span ng-if="productLineItem.MSRP === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.MSRP != ''">{{productLineItem.MSRP}} ${regionToCurrencyMap[marketSideRegion]}</span>
							</td>
							<td class="text-center">
								<span ng-if="productLineItem.MSRPTax === '' || productLineItem.MSRPTax == null">-</span>
								<span ng-if="productLineItem.MSRPTax != '' && productLineItem.MSRPTax != null">{{productLineItem.MSRPTax}} ${regionToCurrencyMap[marketSideRegion]}</span>
							</td>
							<td class="text-center">
								<span ng-repeat="marketplace in productLineItem.marketplace">
									<span ng-if="marketplace.SSBP === ''"><spring:message code="productVersion.none" /></span>
									<span ng-if="marketplace.SSBP != ''">{{marketplace.SSBP}} ${regionToCurrencyMap[marketSideRegion]}</span>
								</span>
							</td>
							<td class="text-center">
								<span ng-repeat="marketplace in productLineItem.marketplace">
									<span ng-if="marketplace.SSBPTax === '' || marketplace.SSBPTax == null">-</span>
									<span ng-if="marketplace.SSBPTax != '' && marketplace.SSBPTax != null">{{marketplace.SSBPTax}} ${regionToCurrencyMap[marketSideRegion]}</span>
								</span>
							</td>
							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DDP_PRICE']}')">
								<td class="text-right">
									{{productLineItem.DDPprice}} ${regionToCurrencyMap[marketSideRegion]}
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
						</tr>
					</thead>
					<tbody>
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td style="width:200px">{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</td>
							<td>
								<span ng-if="productLineItem.packageDimension1 === ''"><spring:message code="productVersion.none" /> X</span>
								<span ng-if="productLineItem.packageDimension1 != ''">{{productLineItem.packageDimension1}} X</span>
								<span ng-if="productLineItem.packageDimension2 === ''"><spring:message code="productVersion.none" /> X</span>
								<span ng-if="productLineItem.packageDimension2 != ''">{{productLineItem.packageDimension2}} X</span>
								<span ng-if="productLineItem.packageDimension3 === ''"><spring:message code="productVersion.none" /> {{productLineItem.packageDimensionUnit}}</span>
								<span ng-if="productLineItem.packageDimension3 != ''">{{productLineItem.packageDimension3}} {{productLineItem.packageDimensionUnit}}</span>
							</td>
							<td>
								<span ng-if="productLineItem.packageWeight === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.packageWeight != ''">{{productLineItem.packageWeight}} {{productLineItem.packageWeightUnit}}</span>
							</td>
							<td>
								<span ng-if="productLineItem.plasticBagWarningLabel === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.plasticBagWarningLabel != ''">
									<span ng-if="productLineItem.plasticBagWarningLabel == 'No'"><spring:message code="productInfoMarketSide.no" /></span>
									<span ng-if="productLineItem.plasticBagWarningLabel == 'smaller12'"><spring:message code="productInfoMarketSide.smaller12" /></span>
									<span ng-if="productLineItem.plasticBagWarningLabel == 'larger12'"><spring:message code="productInfoMarketSide.larger12" /> <span class="text-danger"><spring:message code="productInfoMarketSide.plasticBagWarningLabelHint" /></span></span>
								</span>
							</td>
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
							<th style="width: 60px;"><spring:message code="productInfoMarketSide.variationProductQuantity"/></th>
							<th><spring:message code="productInfoMarketSide.cartonDimension" /></th>
							<th><spring:message code="productInfoMarketSide.cartonWeight" /></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td td style="width:200px">
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</td>
							<td style="width: 60px;">
								<span ng-if="productLineItem.variationProductQuantity === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.variationProductQuantity != ''">{{productLineItem.variationProductQuantity}}</span>
							</td>
							<td>
								<span ng-if="productLineItem.cartonPackageDimension1 === ''"><spring:message code="productVersion.none" /> X</span>
								<span ng-if="productLineItem.cartonPackageDimension1 != ''">{{productLineItem.cartonPackageDimension1}} X</span>
								<span ng-if="productLineItem.cartonPackageDimension2 === ''"><spring:message code="productVersion.none" /> X</span>
								<span ng-if="productLineItem.cartonPackageDimension2 != ''">{{productLineItem.cartonPackageDimension2}} X</span>
								<span ng-if="productLineItem.cartonPackageDimension3 === ''"><spring:message code="productVersion.none" /> {{productLineItem.cartonPackageDimensionUnit}}</span>
								<span ng-if="productLineItem.cartonPackageDimension3 != ''">{{productLineItem.cartonPackageDimension3}} {{productLineItem.cartonPackageDimensionUnit}}</span>
							</td>
							<td>
								<span ng-if="productLineItem.cartonPackageWeight === ''"><spring:message code="productVersion.none" /> {{productLineItem.cartonPackageWeightUnit}}</span>
								<span ng-if="productLineItem.cartonPackageWeight != ''">{{productLineItem.cartonPackageWeight}} {{productLineItem.cartonPackageWeightUnit}}</span>
							</td>
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
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td style="width:200px">
								{{supplierKcode}}-{{productLineItem.SKU}}
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</td>
							<td>
								<span ng-if="productLineItem.disposeOfUnfulfillableInventory =='remove'"><spring:message code="productInfoMarketSide.remove" /></span>
								<span ng-if="productLineItem.disposeOfUnfulfillableInventory == 'dispose'"><spring:message code="productInfoMarketSide.dispose" /></span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productInfoMarketSide.note" /></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div id="note" ></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-right">
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
						<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />
					</a>
	  			</sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="detailsOfBaseProduct.backToList" /></a>
	  			</sec:authorize>
	  			<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
	  				<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList?kcode={{supplierKcode}}"><spring:message code="detailsOfBaseProduct.backToList" /></a>
	  			</sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_EDIT']}')">
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
						<a class="btn btn-primary" ng-show="status =='Pending supplier action'" href="${pageContext.request.contextPath}/EditMarketSideProductInformation/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
						<a class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" href="${pageContext.request.contextPath}/EditMarketSideProductInformation/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>
					</sec:authorize>
				</sec:authorize>
			</div>
		</div>		
	</div>
</div>