<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>


<title> 
<c:choose>
	<c:when test="${type eq 'source'}">
		<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="productMarketingMaterial.source" /> - <spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> - DRS						
	</c:when>
	<c:otherwise>
		<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="${marketSideRegion}" /> - <spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> - DRS			
	</c:otherwise>
</c:choose>
</title>


<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">


<script>

	console.log( ${product} );

	angular.module('productMarketingMaterialView', []).controller('productMarketingMaterialViewCtrl', function($scope) {
		var product = ${product};
		var productMarketingMaterialVersion = JSON.parse(product["data"]);
		var baseProductCode = '${baseProductCode}';
		var supplierKcode = '${supplierKcode}';
		var variationCode = '${variationCode}';
		$scope.status = product["status"];
		$scope.baseProductCode = baseProductCode;
		$scope.supplierKcode = supplierKcode;
		$scope.productName = productMarketingMaterialVersion["name"];
		$scope.productShortName = productMarketingMaterialVersion["shortName"];
		$scope.feature1 = productMarketingMaterialVersion["feature1"];
		$scope.feature2 = productMarketingMaterialVersion["feature2"];
		$scope.feature3 = productMarketingMaterialVersion["feature3"];
		$scope.feature4 = productMarketingMaterialVersion["feature4"];
		$scope.feature5 = productMarketingMaterialVersion["feature5"];
		$scope.description = productMarketingMaterialVersion["description"];				
		$("#description").append($scope.description);		
		$scope.searchTerms1 = productMarketingMaterialVersion["searchTerms1"];
		$scope.searchTerms2 = productMarketingMaterialVersion["searchTerms2"];
		$scope.searchTerms3 = productMarketingMaterialVersion["searchTerms3"]; 
		$scope.searchTerms4 = productMarketingMaterialVersion["searchTerms4"];
		$scope.searchTerms5 = productMarketingMaterialVersion["searchTerms5"];																				
		$scope.note = productMarketingMaterialVersion["note"];			
		$("#note").append($scope.note);
		$scope.productLineItems = JSON.parse(productMarketingMaterialVersion["products"]);
		$scope.productWithVariation = productMarketingMaterialVersion["productWithVariation"];		
	});
</script>

<style type="text/css">
.hover_img a { position:relative; }
.hover_img a span { position:absolute; display:none; z-index:99;}
.hover_img a:hover span { display:block; top:-400px;  width: 50%;} 
.hover_img img{width:400px; height:400px;}
.disabledRow {
	opacity: 0.5;	
	pointer-events: none;	
}
</style>


</head>


<div class="row">
    <div class="col-md-12 max-width">
    	<div class="breadcrumb">
        	<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
        		<a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>	
        		<span>${breadcrumbProduct}</span>
        		<div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
        		<span>
        		<c:choose>
					<c:when test="${type eq 'source'}">
						<spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> [<spring:message code="productMarketingMaterial.source" />]
					</c:when>
					<c:otherwise>
						<spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> @<spring:message code="${marketSideRegion}" />
					</c:otherwise>
				</c:choose>        
        		</span>
        	</sec:authorize>
        	<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
        		<a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>
        		<a href="${pageContext.request.contextPath}/NewDevelopingProductList?kcode=${supplierKcode}">${supplierKcode} <spring:message code="productVersion.supplierBaseProductList" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>
        		<span>${breadcrumbProduct}</span>
        		<div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
        		<span>
        		<c:choose>
					<c:when test="${type eq 'source'}">
						<spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> [<spring:message code="productMarketingMaterial.source" />]
					</c:when>
					<c:otherwise>
						<spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> @<spring:message code="${marketSideRegion}" />
					</c:otherwise>
				</c:choose>        
        		</span>
        	</sec:authorize>	         
    	</div>
    </div>
</div>

<div id="app" class="max-width" ng-app="productMarketingMaterialView" ng-controller="productMarketingMaterialViewCtrl">
	<div class="container-fluid">
		<div class="row" style="padding-top:10px">
			<div class="col-md-12">


				<div class="productname">
					${breadcrumbProduct}
					<c:if test="${type ne 'source'}">
						@<spring:message code="${marketSideRegion}" />
					</c:if>
				</div>								 

			 	<c:choose>
					<c:when test="${type eq 'source'}">
						<div class="col-md-2 col-md-push-5 prod-status" style="position:absolute;text-align:center;color:red;font-size:13pt;">
							<i class="far fa-check-square"></i> <spring:message code="productOnBoardingStatus.${status}" />
						</div>
			 		</c:when>
			 	</c:choose>


			 	<div class="change_product">



			 		<!-- help -->
			 		<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
							<i class="fas fa-question-circle"></i>
							<spring:message code="detailsOfBaseProduct.help" />
						</a>
	  				</sec:authorize>


	  				<!-- back -->
			 		<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}">
	  						<spring:message code="detailsOfBaseProduct.backToList" />
	  					</a>
	  				</sec:authorize>

	  				<!-- back -->
	  				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
	  					<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}">
	  					<spring:message code="detailsOfBaseProduct.backToList" /></a>
	  				</sec:authorize>




			 		<c:if test="${type eq 'marketSide'}">

						<!-- notify supplier -->
			 			<sec:authorize access="hasAnyRole('${auth['GENERAL_DRS_STAFF']}')">
				 			<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
								 <spring:message code="detailsOfBaseProduct.sendMailToSupplier" />
							</button>
						</sec:authorize>
			 			 
			 			<!-- sales copy discussion -->
			 			<a href="${pageContext.request.contextPath}/ConfirmMarketingMaterial/${baseProductCode}/${variationCode}/${marketSideRegion}?supplierKcode=${supplierKcode}" class="btn btn-info">
			 				<spring:message code="detailsOfBaseProduct.salesCopyDiscussion" />
			 			</a>


			 		</c:if>
			 		
			 		
			 		<!-- Confirmation Modal -->
					<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
					  <div class="modal-dialog" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <h4 class="modal-title" style="text-align: center;"><spring:message code="detailsOfBaseProduct.sendMailConfirmation" /></h4>
					        
					      </div>
					      <div class="modal-body" style="text-align: center;">
					     
					        <a href="${pageContext.request.contextPath}/sendDefaultEmail/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}" class="btn btn-primary">
					        	<spring:message code="detailsOfBaseProduct.yes" />
					        </a>
					        <button type="button" class="btn btn-danger" data-dismiss="modal">
					        	<spring:message code="detailsOfBaseProduct.no" />
					        </button>
					      </div>
					    </div>
					  </div>
					</div>
								 		
			 		
			 		<c:choose>

						<c:when test="${type eq 'source'}">

							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_EDIT']}')">

								<!-- edit -->
								<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
									<a class="btn btn-primary" ng-show="status =='Pending supplier action'"
									href="${pageContext.request.contextPath}/EditSourceProductMarketingMaterial/${baseProductCode}/${variationCode}?supplierKcode=${supplierKcode}">
									<i class="fas fa-pencil-alt" aria-hidden="true"></i>
									<spring:message code="detailsOfBaseProduct.edit" /></a>
								</sec:authorize>

								<!-- edit -->
								<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
									<a class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'"
									href="${pageContext.request.contextPath}/EditSourceProductMarketingMaterial/${baseProductCode}/${variationCode}?supplierKcode=${supplierKcode}">
									<i class="fas fa-pencil-alt" aria-hidden="true"></i>
									<spring:message code="detailsOfBaseProduct.edit" /></a>
								</sec:authorize>

							</sec:authorize>

						</c:when>

						<c:otherwise>

							<!-- edit -->
							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_EDIT_MARKETING_MARKET_SIDE']}')">
								<a class="btn btn-primary" href="${pageContext.request.contextPath}/EditMarketSideProductMarketingMaterial/${baseProductCode}/${variationCode}/${marketSideRegion}?supplierKcode=${supplierKcode}">
								<i class="fas fa-pencil-alt" aria-hidden="true"></i>
								 <spring:message code="detailsOfBaseProduct.edit" /></a>
							</sec:authorize>


						</c:otherwise>
					</c:choose>

				</div>
			</div>
		</div>	


		<!-- nav bars -->
		<ul class="nav nav-tabs" role="tablist" style="margin-top:20px;">
    		<li role="presentation" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
    		<li role="presentation"><a href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}" aria-controls="home" role="tab">
    		<spring:message code="detailsOfBaseProduct.productInformation" /></a></li>
    		<li role="presentation"  class="active" ><a href="#"  role="tab"><spring:message code="detailsOfBaseProduct.productMarketingMaterial" /></a></li>
  		</ul>

		<div class="row region" style="padding:5px;">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${type eq 'source'}">
						<span  style="margin:3px;background-color:#cfcfcf;font-size:9pt;padding:2px;" class="btn btn-default disabled pull-right">
							<a href="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}?supplierKcode={{supplierKcode}}">
								<img style="width:16px;" src="<c:url value="/resources/images/UN.png"/>"> <spring:message code="detailsOfBaseProduct.productGeneralInformation" /> 
							</a>
						</span>		
						<c:forEach items="${marketSideList}" var="marketSide">
							<span  style="margin:3px;font-size:9pt;padding:2px;" class="btn btn-default pull-right">
								<a href="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}/${marketSide}?supplierKcode={{supplierKcode}}">
									<img style="width:16px;" src="<c:url value="/resources/images/${marketSide}.png"/>"> ${marketSide}
								</a>
							</span>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<span  style="margin:3px;font-size:9pt;padding:2px;" class="btn btn-default pull-right">
							<a href="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}?supplierKcode={{supplierKcode}}">
								<img style="width:16px;" src="<c:url value="/resources/images/UN.png"/>"> <spring:message code="detailsOfBaseProduct.productGeneralInformation" />
							</a>
						</span>		
						<c:forEach items="${marketSideList}" var="marketSide">		
							<c:if test="${marketSide eq marketSideRegion}">
								<span  style="margin:3px;background-color:#cfcfcf;font-size:9pt;padding:2px;" class="btn btn-default disabled pull-right">
									<a href="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}/${marketSide}?supplierKcode={{supplierKcode}}">
										<img style="width:16px;" src="<c:url value="/resources/images/${marketSide}.png"/>"> ${marketSide} 
									</a>
								</span>
							</c:if>
							<c:if test="${marketSide ne marketSideRegion}">
								<span  style="margin:3px;font-size:9pt;padding:2px;" class="btn btn-default pull-right">
									<a href="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}/${marketSide}?supplierKcode={{supplierKcode}}">
										<img style="width:16px;" src="<c:url value="/resources/images/${marketSide}.png"/>"> ${marketSide}
									</a>
								</span>
							</c:if>		
						</c:forEach>	
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productMarketingMaterial.productName" />
				</div>				
			</div>
		</div>
		<div class="row">
			<div class="col-md-10">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right" style="width:150px;vertical-align:top;">
							<b><spring:message code="productMarketingMaterial.productName" /></b>
						</td>
						<td style="vertical-align:top;">
							<span ng-if="productName === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="productName != ''">{{productName}}</span>										
						</td>				
					</tr>
					<c:if test="${type ne 'source'}">
					<tr>
						<td class="text-right" style="width:150px;vertical-align:top;">
							<b><spring:message code="productMarketingMaterial.productShortName" /></b>
						</td>
						<td style="vertical-align:top;">
							<span ng-if="productShortName === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="productShortName != ''">{{productShortName}}</span>												
						</td>				
					</tr>
					</c:if>				
				</table>
			</div>
		</div>	
		<c:if test="${type ne 'source'}">
			<div class="row"  ng-show="productWithVariation=='1'">
				<div class="col-md-12">
					<div class="page-heading"><spring:message code="productMarketingMaterial.variationProductName" /></div>				
				</div>
			</div>
			<div class="row"  ng-show="productWithVariation=='1'">
				<div class="col-md-10">
					<table class="table table-withoutBorder">
						<tr ng-class="{'disabledRow':productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td class="text-right" style="width:300px;">
							<b>
								{{supplierKcode}}-{{productLineItem.SKU}}							
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</b>
							</td>
							<td>
								<span ng-if="productLineItem.name === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.name != ''">{{productLineItem.name}}</span>												
							</td>							
						</tr>			
					</table>
				</div>
			</div>	
		</c:if>		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productMarketingMaterial.productFeaturesTitle" /></div>				
			</div>
		</div>
		<div class="row">
			<div class="col-md-10">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right" style="width:10%">
							<b><spring:message code="productMarketingMaterial.feature1" /></b>
						</td>
						<td>
							<span ng-if="feature1 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="feature1 != ''">{{feature1}}</span>										
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:10%">
							<b><spring:message code="productMarketingMaterial.feature2" /></b>
						</td>
						<td>
							<span ng-if="feature2 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="feature2 != ''">{{feature2}}</span>																			
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:10%">
							<b><spring:message code="productMarketingMaterial.feature3" /></b>
						</td>
						<td>
							<span ng-if="feature3 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="feature3 != ''">{{feature3}}</span>									
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:10%">
							<b><spring:message code="productMarketingMaterial.feature4" /></b>
						</td>
						<td>
							<span ng-if="feature4 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="feature4 != ''">{{feature4}}</span>										
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:10%">
							<b><spring:message code="productMarketingMaterial.feature5" /></b>
						</td>
						<td>
							<span ng-if="feature5 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="feature5 != ''">{{feature5}}</span>										
						</td>
					</tr>			
				</table>							
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productMarketingMaterial.productDescriptionTitle" /></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-body">							
						<div id="description" ></div>						
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productMarketingMaterial.searchTermsTitle" /></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-10">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right" style="width:12%">
							<b><spring:message code="productMarketingMaterial.searchTerms1" /></b>
						</td>
						<td>
							<span ng-if="searchTerms1 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="searchTerms1 != ''">{{searchTerms1}}</span>										
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:12%">
							<b><spring:message code="productMarketingMaterial.searchTerms2" /></b>
						</td>
						<td>
							<span ng-if="searchTerms2 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="searchTerms2 != ''">{{searchTerms2}}</span>					
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:12%">
							<b><spring:message code="productMarketingMaterial.searchTerms3" /></b>
						</td>
						<td>
							<span ng-if="searchTerms3 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="searchTerms3 != ''">{{searchTerms3}}</span>					
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:12%">
							<b><spring:message code="productMarketingMaterial.searchTerms4" /></b>
						</td>
						<td>
							<span ng-if="searchTerms4 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="searchTerms4 != ''">{{searchTerms4}}</span>					
						</td>
					</tr>
					<tr>
						<td class="text-right" style="width:12%">
							<b><spring:message code="productMarketingMaterial.searchTerms5" /></b>
						</td>
						<td>
							<span ng-if="searchTerms5 === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="searchTerms5 != ''">{{searchTerms5}}</span>					
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productMarketingMaterial.pictureOfVariationTitle" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th><spring:message code="productMarketingMaterial.variationProduct" /></th>
							<th><spring:message code="productMarketingMaterial.mainImages" /></th>
							<th><spring:message code="productMarketingMaterial.variationImages" /></th>
							<th><spring:message code="productMarketingMaterial.otherImages" /></th>					
						</tr>
					</thead>
					<tbody>
						<tr ng-class="{'disabledRow':productLineItem.status=='unallowed'||productLineItem.status=='aborted'}" ng-repeat="productLineItem in productLineItems">
							<td>
								{{supplierKcode}}-{{productLineItem.SKU}}						
								<span ng-if="productWithVariation=='1'">
									(<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span><span ng-if="productLineItem.type2!=''">-<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span><span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span></span>)
								</span>
							</td>
							<td>
								<div class="hover_img" ng-repeat="mainImageLineItem in productLineItem.mainImageLineItems">
									<a href="${pageContext.request.contextPath}/MainImageFile?fileName={{mainImageLineItem.file}}&region=${region}">{{mainImageLineItem.file}}
										<span><img src="${pageContext.request.contextPath}/MainImageFile?fileName={{mainImageLineItem.file}}&region=${region}" alt="image"/></span>						
									</a>
								</div>
							</td>	
							<td>
								<div class="hover_img" ng-repeat="variationImageLineItem in productLineItem.variationImageLineItems">
									<a href="${pageContext.request.contextPath}/VariationImageFile?fileName={{variationImageLineItem.file}}&region=${region}">{{variationImageLineItem.file}}
										<span><img src="${pageContext.request.contextPath}/VariationImageFile?fileName={{variationImageLineItem.file}}&region=${region}" alt="image"/></span>							
									</a>						
								</div>
							</td>	
							<td>							
								<div class="hover_img" ng-repeat="otherImageLineItem in productLineItem.otherImageLineItems">
									<a href="${pageContext.request.contextPath}/OtherImageFile?fileName={{otherImageLineItem.file}}&region=${region}">{{otherImageLineItem.file}}
										<span><img src="${pageContext.request.contextPath}/OtherImageFile?fileName={{otherImageLineItem.file}}&region=${region}" alt="image"/></span>						
									</a>
								</div>
							</td>												
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productMarketingMaterial.note" />
				</div>
			</div>
		</div>	
		<div class="row">
			<div class="col-md-12">
				<div id="note" ></div>							
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>			
		<div class="row">
			<div class="col-md-12 text-right">
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
						<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />								   						
					</a>  				
	  			</sec:authorize>

				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}"><spring:message code="detailsOfBaseProduct.backToList" /></a>
	  			</sec:authorize>
	  			<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
	  				<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}"><spring:message code="detailsOfBaseProduct.backToList" /></a>
	  			</sec:authorize>
				<c:choose>
					<c:when test="${type eq 'source'}">
						<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_EDIT']}')">															    								    								    
							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">							
								<a class="btn btn-primary" ng-show="status =='Pending supplier action'" href="${pageContext.request.contextPath}/EditSourceProductMarketingMaterial/${baseProductCode}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>																									
							</sec:authorize>
							<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
								<a class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" href="${pageContext.request.contextPath}/EditSourceProductMarketingMaterial/${baseProductCode}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>																										
							</sec:authorize>
						</sec:authorize>				
					</c:when>
					<c:otherwise>
						<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_EDIT_MARKETING_MARKET_SIDE']}')">															    								    								    
							<a class="btn btn-primary" href="${pageContext.request.contextPath}/EditMarketSideProductMarketingMaterial/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>								
						</sec:authorize>
					</c:otherwise>
				</c:choose>								
			</div>
		</div>		
	</div>
</div>