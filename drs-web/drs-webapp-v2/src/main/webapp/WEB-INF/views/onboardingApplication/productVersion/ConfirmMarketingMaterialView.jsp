<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<title>
	<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="${marketSideRegion}" /> - <spring:message code="detailsOfBaseProduct.confirmMarketingMaterial" /> - DRS			
</title>
<link href="<c:url value="/resources/css/bootstrap3-wysihtml5.css"/>" type="text/css" rel="stylesheet">
<script type='text/javascript' src="<c:url value="/resources/js/bootstrap3-wysihtml5.all.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">		
<script>
	angular.module('productMarketingMaterialView', []).controller('productMarketingMaterialViewCtrl', function($scope) {		
		var product = ${product};		
		var productMarketingMaterialVersion = JSON.parse(product["data"]);				
		var baseProductCode = '${baseProductCode}';
		var supplierKcode = '${supplierKcode}';		
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
    <div id="breadcrumbBar" class="col-md-12 max-width">
    	<div class="breadcrumb">
        	<a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
        	<a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.product" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
        	<span>${breadcrumbProduct}</span>
        	<div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>
        	<span>
        	<c:choose>
				<c:when test="${type eq 'source'}">
					<spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> [<spring:message code="productMarketingMaterial.source" />]
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/MarketSideProductMarketingMaterial/${baseProductCode}/${marketSideRegion}?supplierKcode=${supplierKcode}" >
						<spring:message code="detailsOfBaseProduct.productMarketingMaterial" /> @<spring:message code="${marketSideRegion}" />
					</a>
				</c:otherwise>
			</c:choose>        
        	</span>
        	<div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>
        	<span><spring:message code="detailsOfBaseProduct.confirmMarketingMaterial" /></span>
    	</div>
    </div>
</div>
<div id="app" class="max-width" ng-app="productMarketingMaterialView" ng-controller="productMarketingMaterialViewCtrl">
	<div class="container-fluid">
		<div class="row" style="padding-top:10px">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productMarketingMaterial.productName" />
				</div>				
			</div>
		</div>
		<div class="row">
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-body">
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
					<div class="panel panel-default">
						<div class="panel-body">
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
				<div class="panel panel-default">
					<div class="panel-body">
					
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
			</div>
		</div>
		
		<!-- Product Description Section -->
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
		
		<!-- Search Term Section -->
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productMarketingMaterial.searchTermsTitle" /></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-body">
						<table class="table	 table-withoutBorder">
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
		
		<sec:authorize access="hasAnyRole('${auth['GENERAL_DRS_STAFF']}')">
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
		</sec:authorize>
		
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12 text-right">
			<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="detailsOfBaseProduct.backToList" /></a>
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
		<div style="padding-bottom: 10px"></div>
		
		
		<!-- Comment Form Section -->
		
		<sec:authorize access="hasAnyRole('${auth['ISSUES_POST_COMMENT']}')">  
		<c:url var="postCommentAction" value="/ConfirmMarketingMaterial/${baseProductCode}/${marketSideRegion}/postComment?supplierKcode=${supplierKcode}"></c:url>
			<form:form action="${postCommentAction}" name="Comment" class="form-horizontal text-left" modelAttribute="Comment">
				
				<div id="contentsRow" class="row">
					<div class="col-md-12">
						<textarea id="contents" name="contents" ng-model="contents" placeholder="Leave a comment" class="form-control"></textarea>					
					</div>					
				</div>
				
				<div style="padding-bottom: 10px"></div>	
						
				<div class="row text-right">					
					<div class="col-md-12">
						<input class="btn btn-success" type="submit" value="<spring:message code='productMarketingMaterial.replyNotify'/>" />
					</div>
					
				</div>			
			</form:form>
		</sec:authorize>
		
		<div style="padding-bottom: 15px"></div>
		
		<c:forEach items="${comments}" var="comment" varStatus="commentStatus">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<span class="text-muted text-right">${comment.creatorName} ${comment.dateCreated}</span>
						</div>
						<div class="panel-body">
				  			<% pageContext.setAttribute("newLineChar", "\n"); %>
				 	 		<p>${fn:replace(comment.contents, newLineChar,  "<br>")}</p>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
		 
		
		<!-- Textbox plugin -->
		<script type="text/javascript">
		$().ready(function() {
			$('#contents').wysihtml5({
				"events": {
			    	"load": function() {
			        	$("#contentsRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");        	               
			        }
			    },toolbar: {		   
			    	 "html": true, //Button which allows you to edit the generated HTML. Default false   		     		   
					 "link": false, //Button to insert a link. Default true
					 "image": false, //Button to insert an image. Default true,
					 "blockquote": false //Blockquote  		   
				}
			});	
		});
		</script>
		
		
	</div>
</div>