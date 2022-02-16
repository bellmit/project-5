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

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>


<script src="<c:url value="/resources/js/views/productMarketingMaterialIndex.js"/>"></script>



<!-- css -->
<style type="text/css">
#page_content_container{
	margin-top: 40px;
	padding-left: 30px;
	min-height: 700px;
}
/*===============================================*/
#title_row{
	height: 52px;
	line-height: 52px;
}
#kcode{
	font-size: 1.3rem;
	text-align: center;
	font-weight: bold;
	
}
#menu_header{
	font-size: 1.3rem;
	text-align:center;
	border: 1px solid black !important;
}
#dropdown_col{
	padding-left: 0;
}
#info_type_dropdown{
	margin-top: -4px;
	background-color: white;
	border: 1px solid #1779ba;
	border-radius: 0;
	height: 52px;
	min-width: 250px;
	font-size: 18px;
	font-family: arial;
	color: black;
}
#info_type_dropdown_list{
	margin: 0;
	padding: 0;
}
.info_type_dropdown_item{
	border: 1px solid black !important;
	height: 50px;
	line-height: 42px;
	min-width: 250px;
	font-size: 16px;
	/*font-weight: bold !important;*/
}
#backToListBtn{
	margin-top: .8rem;
	font-size: 1.7rem;
	background-color: #3d8c3d !important;
}
/*===============================================*/
#sku_table{
	margin-top: 40px;
}
thead tr{
	height: 45px;
	font-size: 15px;
}
thead tr th{
	padding-left: 12px !important;
}
tbody tr{
	height: 55px;
}
tbody tr th, tbody tr td{
	padding-left: 14px !important;
}
#marketplaceCol{
	width: 350px;
}
.table-badge{
	height: 30px;
	border: 1px solid #9e9e9e;
	line-height: 20px !important;
	min-width: 60px;
	font-size: 1rem !important;
	cursor: pointer;
	padding-left: .6rem !important;
	padding-right: .6rem !important;
	margin-bottom: .2rem;
	margin-top: .3rem;
}
.flag{
	height: 13px;
}
/*=====================================================*/
/* modal */
.modal-content{
	padding: 5px 10px 5px 10px;
}
.modal-title{
	font-size: 20px;
}
#modalSkuCodeRow{
	margin-top: 1rem;
}
#modalSkuCode{
	font-size: 1.2rem;
}
#detailModal .modal-dialog{
	margin: auto;
	margin-top: 4rem;
	display: block;
	width: 190%;
	max-width: 1200px !important;
	font-size: 14px !important;
}
.tab-pane{
  height:500px;
  overflow-y:scroll;
  width:100%;
}
.modal-badge{
	margin-left: 3rem;
	font-size: 1.2rem;
	border: 1px solid grey;
	
}
#modalMarketingMaterialStatus{
	background-color: #d34519;
	color: white;
	padding: .5rem 1rem;
	font-size: 18px;
	font-weight: bold;
	margin-bottom: 2rem;
	
}
#titleRow{
	margin-bottom: 1.3rem;	
}
#skuTitle{
	font-size: 1.5rem;
	margin-bottom: 1.2rem;
}
#skuShortName{
	font-size: 1.5rem;
	margin-bottom: 1.2rem;
}
#pic_col, #detail_col{
	min-height: 400px;
	/*background-color: #ffcdd2;*/
	/*border: 1px solid #eee;*/
}
#display_img{
	width: 95%;
	margin-bottom: 1.8rem;
}
.product_img{
	height: 60px;
	/*width: 150px;*/
	border: 1px solid #424242;
	margin-right: 6px;
	margin-bottom: 8px;
	cursor: pointer;
}
.sectionTitle{
	font-weight: bold;
	font-size: 22px;
}
ol{
	padding-left: 20px;
}
#modalXBtn{
	font-size: 2.3rem;
}
.modal-function-buttons{
}
#modalCloseBtn{
}
</style>  


<!-- js section -->
<script>
$(document).ready(function() {
	isDrsUser = ${isDrsUser};
	
    // when core information badge is clicked
	$('.coreInformationBadge').click(function(){
		
		// reset display img
		//$("#display_img").attr("src", $('#mainImage')[0].src);
		
		
		// console.log('core information clicked');
		// console.log( "${supplierKcode}" );
		// console.log( "${baseProductCode}" );
		// console.log($(this).attr('sku-code'))
		
		supplierKcode = "${supplierKcode}";
		baseProductCode = "${baseProductCode}";
		skuCode = $(this).attr('sku-code');
		region = $(this).attr('region');
		
		
		$.ajax({
			
			type : 'get',
			url : '${pageContext.request.contextPath}/getSkuCoreInformation/${supplierKcode}/${baseProductCode}/' + skuCode,
			contentType : "application/json; charset=utf-8",
			// data : {baseProductCode: baseProductCode, variationCode: skuCode, supplierKcode: supplierKcode},
			dataType : "text",
			success : function(data) {	
				console.log('success');
				updateModal(JSON.parse(data), '${pageContext.request.contextPath}');
				
			}
		});
			
	
		
	});
	
	
	
	
	// Sku region marketing material
	$('.skuRegionMarketingMaterial').click(function(){
		// console.log('skuRegionMarketingMaterial clicked');
		
		// reset display img
		//$("#display_img").attr("src", $('#mainImage')[0].src);
		
		region = $(this).attr('region');
		supplierKcode = "${supplierKcode}";
		baseProductCode = "${baseProductCode}";
		skuCode = $(this).attr('sku-code');
		// console.log(region);
		// console.log(supplierKcode);
		// console.log(baseProductCode);
		// console.log(skuCode);
		
		$.ajax({
			
			type : 'get',
			url : '${pageContext.request.contextPath}/getSkuMarketplaceInformation/' + region + '/' + supplierKcode + '/' +
				baseProductCode + '/' + skuCode,
			contentType : "application/json; charset=utf-8",
			// data : {baseProductCode: baseProductCode, variationCode: skuCode, supplierKcode: supplierKcode},
			dataType : "text",
			success : function(data) {	
				//console.log('success');
				updateModal(JSON.parse(data), '${pageContext.request.contextPath}');
				
			}
		});
		
	});
	
	
	
	
	
	$('#notifySupplierBtn').click(function(){
		
		region = $(this).attr('region');
		supplierKcode = "${supplierKcode}";
		baseProductCode = "${baseProductCode}";
		
		//console.log(region);
		//console.log(supplierKcode);
		//console.log(baseProductCode);
		
		
		$.ajax({
			
			type : 'get',
			url : '${pageContext.request.contextPath}/sendDefaultEmail/' + baseProductCode + '/' + region +
				'?supplierKcode=' + supplierKcode,
			contentType : "application/json; charset=utf-8",
			dataType : "text",
			success : function(data) {	
				console.log(data);
				
			}
		});
		
	});
	
	
	
	// when there is prev referral
	var prevSkuCode =  "${prevSkuCode}";
	var prevType = "${prevType}";
	var id = prevSkuCode + '-' + prevType;
	
	if(prevSkuCode != ''){
		
		//click on the prev btn
		$('#' + id).simulateClick();
		
		console.log(id);
				
	}
	
});
	
	
	
	
	
	
	angular.module('baseProductOnboardingWithSKU', []).controller('baseProductOnboardingWithSKUCtrl', function($scope) {
		
		
		var baseProduct = ${baseProduct};	
	
		//var jsonData = JSON.parse(baseProduct["productInfoSource"]["data"]);
		var jsonData = JSON.parse(baseProduct["data"]);
		$scope.status = baseProduct["status"];
		$scope.supplierKcode = jsonData["supplierKcode"];
		$scope.productNameEnglish = jsonData["productNameEnglish"];
		$scope.productNameLocal = jsonData["productNameLocal"];
		$scope.proposalProductCategory = jsonData["proposalProductCategory"];
		$scope.brand = jsonData["brand"];
		$scope.brandEng = jsonData["brandEng"];
		
		$scope.supplierBaseProductCode = baseProduct["productBaseCode"].replace("BP-"+$scope.supplierKcode+"-","");
		
		$scope.baseProductCode = jsonData["baseProductCode"];
		$scope.manufacturerLeadTime = jsonData["manufacturerLeadTime"];
		$scope.allergy = jsonData["allergy"];
		$scope.allergyName = jsonData["allergyName"];
		if ($scope.allergy && $scope.allergyName != 'latex') $scope.allergyOther = jsonData["allergyName"];
		
		
		$scope.productWithVariation = jsonData["productWithVariation"];
		$scope.type1 = jsonData["variationType1"];
		$scope.type2 = jsonData["variationType2"];
		$scope.type1Column = jsonData["variationType1"];
		$scope.type2Column = jsonData["variationType2"];
		$scope.productLineItems = JSON.parse(jsonData["products"]);
		$scope.originalPlace = jsonData["originalPlace"];
		$scope.medical = jsonData["medical"];
		$scope.wirelessTech = jsonData["wirelessTech"];
		$scope.documentationForSupplierSideCountry = jsonData["documentationForSupplierSideCountry"];
		
		//$scope.otherWirelessTechLineItems = JSON.parse(jsonData["otherWirelessTechs"]);
		
		$scope.hsCode = jsonData["hsCode"];
		
		//$scope.referenceFileLineItems = JSON.parse(jsonData["referenceFiles"]);
		//$scope.containedMaterial = jsonData["containedMaterial"];
		//$scope.hazardousMaterialLineItems = JSON.parse(jsonData["hazardousMaterials"]);
		//$scope.batteryLineItems = JSON.parse(jsonData["batteries"]);		
		//$scope.note = jsonData["note"];
		//$scope.innerNote = jsonData["innerNote"];
		//$("#note").append($scope.note);
		//$("#innerNote").append($scope.innerNote);
		
		$scope.marketSideRegionList = jsonData["applicableRegionBP"];
	});
	
	
	
	
</script>

</head>


<div id="page_content_container" class="container"  ng-app="baseProductOnboardingWithSKU" ng-controller="baseProductOnboardingWithSKUCtrl">
      
      <!--  
      <div id="title_row" class="row">
        <div id='kcode' class="col col-3">
          
            ${baseProductCode}
          
        </div>
        <div id="dropdown_col" class="col col-3">
          
          <div id="dropdown_container" class="dropdown">
            <button class="btn dropdown-toggle" type="button" id="info_type_dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <spring:message code="detailsOfBaseProduct.productMarketingMaterial" />
            </button>
            <div id="info_type_dropdown_list" class="dropdown-menu" aria-labelledby="info_type_dropdown">
              <a class="dropdown-item info_type_dropdown_item" href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}">
              	<spring:message code="detailsOfBaseProduct.productInformation" />
              </a>
              <a class="dropdown-item info_type_dropdown_item" href="#">
              	<spring:message code="detailsOfBaseProduct.productMarketingMaterial" />
              </a>
              
            </div>
          </div>
        </div>
        <div class="col col-4">
         
        </div>
        <div class="col col-2">
       
          
          <a id='backToListBtn' type="button" class="btn btn-success float-right btn-lg" 
          href="${pageContext.request.contextPath}/NewDevelopingProductList">
				 <spring:message code="detailsOfBaseProduct.back" />
		   </a> 
		
			
        </div>
        
      </div>
      -->
      
       <div class="row mb-3">
	        <div class="col-md-12">

			<div class="btn-group">
			  <button id="kcode" type="button" class="btn btn-primary">${baseProductCode} </button>
			  <button id="menu_header" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    <spring:message code="detailsOfBaseProduct.productMarketingMaterial" />
			  </button>
			  <div class="dropdown-menu">
			    <a class="dropdown-item menu-item" href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"> <spring:message code="detailsOfBaseProduct.productInformation" /></a>
			    <a class="dropdown-item menu-item" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}"><spring:message code="detailsOfBaseProduct.productMarketingMaterial" /></a>
			  </div>
			</div>
	          <a type="button" class="btn btn-default float-right"
	          href="${pageContext.request.contextPath}/NewDevelopingProductList">
					 <spring:message code="detailsOfBaseProduct.back" />
			   </a>
			</div>
	      </div>
      


      <div class="row">
      
		<div class="table-responsive">
        <table>

          <thead>
            <tr>
              <th scope="col"><spring:message code="productInfoSourceVersion.SKUcode" /></th>
              <th scope="col"><spring:message code="productInfoSourceVersion.GTIN" /></th>
              <th scope="col"><spring:message code="productInfoSourceVersion.applicableRegion" /></th>
              <th scope="col"><spring:message code="productInfoSourceVersion.productDimension" /></th>
              <th scope="col"><spring:message code="productInfoSourceVersion.productWeight" /></th>
              <th scope="col"><spring:message code="productInfoSourceVersion.FCAPrice" /></th>
            </tr>
          </thead>

          <tbody>
          
            <tr ng-repeat="productLineItem in productLineItems">
										
				<th scope="row">
					<c:if test="${region eq 'source'}">
						{{productLineItem.SKU}}
					</c:if>
					<c:if test="${marketSide ne 'source'}">
						{{productLineItem.SKU}}
					</c:if>
				</th>
				
				<td>
					<sec:authorize access="hasAnyRole('${auth['GTIN_PROVIDER_DRS']}')">
						<span ng-if="productLineItem.GTINValue === ''"><spring:message code="productVersion.none" /></span>
						<span ng-if="productLineItem.GTINValue != ''">{{productLineItem.GTINValue}}</span>																																				
						<span ng-if="productLineItem.providedByDRS == true">
							<spring:message code="GTIN.providerDRS_HINT" />
						</span> 	
						<span ng-if="productLineItem.providedByDRS == false">
							<spring:message code="GTIN.providerSupplier_HINT" />
						</span>
					</sec:authorize>
					
					<sec:authorize access="hasAnyRole('${auth['GTIN_PROVIDER_SUPPLIER']}')">
						<span ng-if="productLineItem.providedByDRS == true">
							<spring:message code="GTIN.providerSupplier_view" />
						</span> 	
						<span ng-if="productLineItem.providedByDRS == false">
							<span ng-if="productLineItem.GTINValue === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="productLineItem.GTINValue != ''">{{productLineItem.GTINValue}}</span>																																								
							<spring:message code="GTIN.providerSupplier_HINT" />
						</span>							
					</sec:authorize>	
												
				</td>			
				
				<td id=marketplaceCol>
				
					<a id="{{productLineItem.SKU}}-core" class="coreInformationBadge badge badge-light table-badge" data-toggle="modal" data-target="#detailModal" 
						sku-code="{{productLineItem.SKU}}" region="core">
						<spring:message code="detailsOfBaseProduct.productGeneralInformation" />
					</a>


					<span ng-repeat="(region, applicableRegion) in productLineItem.applicableRegion">	
						<span ng-if="applicableRegion.status =='selected'">
						
							<a id="{{productLineItem.SKU}}-{{region}}" class="skuRegionMarketingMaterial badge badge-light table-badge" data-toggle="modal" data-target="#detailModal"
								sku-code="{{productLineItem.SKU}}" region="{{region}}">
							
			               		{{region}} <img class="flag" src="<c:url value="/resources/images/svg_flags/{{region}}.svg"/>">
			               	</a>
			            																	
						</span>
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
					<span ng-if="productLineItem.packageWeight === ''"><spring:message code="productVersion.none" /> {{productLineItem.packageWeightUnit}}</span>
					<span ng-if="productLineItem.packageWeight != ''">{{productLineItem.packageWeight}} {{productLineItem.packageWeightUnit}}</span>																																																																											
				</td>
				<td>
					<span ng-if="productLineItem.FCAPrice === ''"><spring:message code="productVersion.none" /></span>
					<span ng-if="productLineItem.FCAPrice != ''">{{productLineItem.FCAPrice}} <spring:message code="TWD" /></span>																																																							
				</td>
			</tr>
			
          </tbody>
        </table>  
	</div>

   </div>

</div>



<!-- Main Modal -->
<div class="modal fade" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="detailModal" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">

      <div class="modal-header">
      
      	<div class="modal-title">
      		<span id="modalMarketingMaterialStatus"></span>
      		<div id="modalSkuCodeRow">
	      		<span id="modalSkuCode"></span>
				<span id="modalMarketingMaterialRegion"></span>
			</div>
      	</div>
      		
     	<button id="modalXBtn" type="button" class="close float-right" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      	
      </div>

      <div class="modal-body">
        <div id="titleRow" class="row">
          <div class="col col-5">
				 
				<span class="sectionTitle">
					<spring:message code="productMarketingMaterial.variationProductName" />: 
				</span> 
				<span id="skuTitle"></span>
				
				<span id="skuShortNamePlaceholder"></span>

          </div>
          
          <div class="col-7">
          
          		<span id="buttonGroupPlaceholder"></span>
          
          </div>
        </div>

        <div class="row">

          <!-- img col -->
          <div id='pic_col' class="col col-5">

            <img id="display_img">

            <h4 class="sectionTitle"><spring:message code="productMarketingMaterial.mainImages" /></h4>
            <span id="mainImagePlaceholder"></span>
            

			<h4 class="sectionTitle"><spring:message code="productMarketingMaterial.variationImages" /></h4>
			<span id="variationImagePlaceholder"></span>
			
			
            <h4 class="sectionTitle"><spring:message code="productMarketingMaterial.otherImages" /></h4>
            <span id="otherImagesPlaceholder"></span>


          </div>

          <!-- detail col -->
          <div id='detail_col' class="col col-7">
            
          <ul class="nav nav-tabs" id="myTab" role="tablist">
		  <li class="nav-item">
		    <a class="nav-link active" id="features-tab" data-toggle="tab" href="#featureContainer" role="tab" aria-controls="features" aria-selected="true">Features</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="description-tab" data-toggle="tab" href="#descriptionContainer" role="tab" aria-controls="description" aria-selected="false">Description</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="searchterms-tab" data-toggle="tab" href="#searchTermContainer" role="tab" aria-controls="search" aria-selected="false">Search Terms</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="notes-tab" data-toggle="tab" href="#noteContainer" role="tab" aria-controls="notes" aria-selected="false">Notes</a>
		  </li>
		  </ul>
            
            <div class="tab-content mt-3" id="MarketingInfoCategories">
            
            <div id="featureContainer" class="tab-pane fade show active" role="tabpanel" aria-labelledby="home-tab">
              <h4 class="sectionTitle"><spring:message code="productMarketingMaterial.productFeaturesTitle" /></h4>
              <ol id="featureList">
                
              </ol>
            </div>

            <div id="descriptionContainer" class="tab-pane fade" role="tabpanel" aria-labelledby="home-tab">
              <h4 class="sectionTitle"><spring:message code="productMarketingMaterial.productDescriptionTitle" /></h4>
				
				<p id="skuDescription"></p>
              
            </div>

            <div id="searchTermContainer" class="tab-pane fade" role="tabpanel" aria-labelledby="home-tab">
              <h4 class="sectionTitle"><spring:message code="productMarketingMaterial.searchTermsTitle" /></h4>
              <ol id="searchTermList">
                
              </ol>

            </div>

            <div id="noteContainer" class="tab-pane fade" role="tabpanel" aria-labelledby="home-tab">
              <h4 class="sectionTitle"><spring:message code="productMarketingMaterial.note" /></h4>
              <p id="skuNotes"></p>
			
			</div>

            </div>
          </div>
        </div>
      </div>

      <div class="modal-footer">
		
        <button id="modalCloseBtn" type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>

      </div>

    </div>
  </div>
</div>





<!-- Confirmation Modal -->
<div class="modal fade" id="notifySupplierModal" tabindex="-1" role="dialog" aria-labelledby="notifySupplierModal" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" style="text-align: center;"><spring:message code="detailsOfBaseProduct.sendMailConfirmation" /></h4>
        
      </div>
      <div class="modal-body" style="text-align: center;">
		<!-- yes btn -->
		<a id=notifySupplierBtn class="btn btn-primary" data-dismiss="modal"> 
			<spring:message code="detailsOfBaseProduct.yes" />
		</a>
		
		<!-- No btn -->
		<button type="button" class="btn btn-danger" data-dismiss="modal">
        	<spring:message code="detailsOfBaseProduct.no" />
        </button>
      </div>
    </div>
  </div>
</div>
