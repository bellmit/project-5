<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="productInfoSourceVersion.productGeneralInformationTitle" />  - DRS											
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">

<script src="<c:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
<link href="<c:url value="/resources/css/bootstrap-4.1.3.min.css"/>" type="text/css" rel="stylesheet">



<style>

body{
	font-size: 1.3rem;
}

.limit-text{
	width: 250px;
	white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

#mainContainer{
	padding-top: 4.8rem;
}

#title_row{
	height: 52px;
	line-height: 52px;
	margin-bottom: 1.2rem;
}

#kcode{
	background-color: #1779ba;
	border: 0;
	color: white;
	font-size: 22px;
	font-weight: bold;
	text-align: center;
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

.region-btn{
	margin:3px;
	font-size:9pt;
	padding:2px;
	border: 1px solid #87929d;

}

</style>


<script>
	angular.module('productInfoSourceView', []).controller('productInfoSourceViewCtrl', function($scope) {		
		var baseProduct = ${baseProduct};		
		var jsonData = JSON.parse(baseProduct["productInfoSource"]["data"]);	
		console.log(jsonData);
		$scope.status = baseProduct["productInfoSource"]["status"];
		$scope.applicationSN = jsonData["applicationSN"];
		$scope.supplierKcode = jsonData["supplierKcode"];
		$scope.productNameEnglish = jsonData["productNameEnglish"];
		$scope.productNameLocal = jsonData["productNameLocal"];
		$scope.proposalProductCategory = jsonData["proposalProductCategory"];
		$scope.brand = jsonData["brand"];
		$scope.brandEng = jsonData["brandEng"];
		$scope.supplierBaseProductCode = jsonData["baseProductCode"].replace("BP-"+$scope.supplierKcode+"-","");		
		$scope.baseProductCode = jsonData["baseProductCode"];
		$scope.manufacturerLeadTime = jsonData["manufacturerLeadTime"];
		$scope.allergy = jsonData["allergy"];
		$scope.allergyName = jsonData["allergyName"];
		if ($scope.allergy && $scope.allergyName != 'latex') $scope.allergyOther = jsonData["allergyName"];
		$scope.materialLineItems = JSON.parse(jsonData["materials"]);
		$scope.referenceLinkLineItems = JSON.parse(jsonData["referenceLinks"]);
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
		$scope.otherWirelessTechLineItems = JSON.parse(jsonData["otherWirelessTechs"]);
		$scope.hsCode = jsonData["hsCode"];
		$scope.referenceFileLineItems = JSON.parse(jsonData["referenceFiles"]);
		$scope.containedMaterial = jsonData["containedMaterial"];
		$scope.hazardousMaterialLineItems = JSON.parse(jsonData["hazardousMaterials"]);
		$scope.batteryLineItems = JSON.parse(jsonData["batteries"]);		
		$scope.note = jsonData["note"];
		$scope.innerNote = jsonData["innerNote"];
		$("#note").append($scope.note);
		$("#innerNote").append($scope.innerNote);
		$scope.marketSideRegionList = jsonData["applicableRegionBP"];
	});
	

	$(document).ready(function() {
		var popTarget = $('#indicator');
		var baseProduct = ${baseProduct};
		var status = baseProduct["productInfoSource"]["status"];						
		popTarget.popover({
			html:true,
			title: '提醒您：',
			placement: 'left',
			container: 'body',
			trigger: 'manual',
			content: function(){return '資料遞交後會依據勾選的銷售地區產生各國資料表，請務必點選右方圖示，填寫相關資料。 <p>'+$('#regions').html().replace(/\pull-right/g,'') +' </p>';}			
			// template:  	'<div class="popover" role="tooltip" style="width:270px;max-width:270px;"><div class="arrow"></div><h3 class="popover-title" style="font-weight:bold;font-size:13pt;"></h3><div class="popover-content" style="font-size:13pt;"></div></div>'
		});
		if(status != 'Finalized'){
			popTarget.popover('show');
		}
		$('body').click(function(){
			popTarget.popover('hide');
		});
	});
</script>

</head>


<div class="max-width" ng-app="productInfoSourceView" ng-controller="productInfoSourceViewCtrl">
	<div id="mainContainer" class="container-fluid">

		<div id="title_row" class="row">
	        <div id='kcode' class="col-md-3">

	            ${baseProductCode}

	        </div>

	        <div id="dropdown_col" class="col-md-3">

	          <div id="dropdown_container" class="dropdown">
	            <button class="btn dropdown-toggle" type="button" id="info_type_dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	              <spring:message code="detailsOfBaseProduct.productInformation" />
	            </button>
	            <div id="info_type_dropdown_list" class="dropdown-menu" aria-labelledby="info_type_dropdown">
	              <a class="dropdown-item info_type_dropdown_item" href="#">
	              	<spring:message code="detailsOfBaseProduct.productInformation" />
	              </a>
	              <a class="dropdown-item info_type_dropdown_item" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?supplierKcode={{supplierKcode}}">
	              	<spring:message code="detailsOfBaseProduct.productMarketingMaterial" />
	              </a>

	            </div>
	          </div>

	        </div>

	        <div class="col-md-4">
	          <!-- column holder -->
	        </div>

	        <div class="col-md-2">


	          <a id='backToListBtn' type="button" class="btn btn-success float-right btn-lg"
	          href="${pageContext.request.contextPath}/NewDevelopingProductList">
					 <spring:message code="detailsOfBaseProduct.back" />
			   </a>



	        </div>

	      </div>



 		<!-- regions row -->
		<div class="row" style="padding:5px;">
			<div class="col-md-12">
				<span style="margin:3px;background-color:#cfcfcf;font-size:9pt;padding:2px;" class="btn btn-default disabled pull-right"><a href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><img style="width:16px;" src="<c:url value="/resources/images/UN.png"/>"> <spring:message code="detailsOfBaseProduct.productGeneralInformation" /> </a></span>		
					<span id="regions">
						<span ng-repeat="region in marketSideRegionList">
							<span class="btn btn-default region-btn pull-right">
								<a href="${pageContext.request.contextPath}/MarketSideProductInformation/${baseProductCode}/{{region}}?supplierKcode={{supplierKcode}}">
									<img style="width:16px;" src="<c:url value="/resources/images/{{region}}.png"/>"> {{region}}
								</a>
							</span>
						</span>	
					</span>
				<span id="indicator" style="margin:3px;font-size:9pt;padding:2px;" class="pull-right" data-container="body" data-toggle="popover" data-placement="left" >&nbsp;</span>				
			</div>
		</div>		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoSourceVersion.basicInfoTitle" />
				</div>
			</div>
		</div>
		<div class="row rowframe">
			<div class="col-md-8">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right vertical_th" style="width:350px;">
							<b><spring:message code="productInfoSourceVersion.ApplicationSN" /></b>
						</td>
						<td>{{applicationSN}}</td>
					</tr>
					<tr>
						<td class="text-right vertical_th" style="width:350px;">
							<b><spring:message code="productInfoSourceVersion.supplier" /></b>
						</td>
						<td>{{supplierKcode}}</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.productNameEn" /></b>
						</td>
						<td>
							<span ng-if="productNameEnglish === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="productNameEnglish != ''">{{productNameEnglish}}</span>						
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th"><b><spring:message code="productInfoSourceVersion.productNameLocal" /></b></td>
						<td>
							<span ng-if="productNameLocal === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="productNameLocal != ''">{{productNameLocal}}</span>							
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th"><b><spring:message code="productInfoSourceVersion.proposalProductCategory" /></b></td>
						<td>
							<span ng-if="proposalProductCategory === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="proposalProductCategory != ''">
								<span ng-if="proposalProductCategory=='3DPrintedProducts'"><spring:message code="ProposalProductCategory.3DPrintedProducts" /></span>												
								<span ng-if="proposalProductCategory=='amazonDeviceAccessories'"><spring:message code="ProposalProductCategory.amazonDeviceAccessories" /></span>						
								<span ng-if="proposalProductCategory=='amazonKindle'"><spring:message code="ProposalProductCategory.amazonKindle" /></span>												
								<span ng-if="proposalProductCategory=='automotivePowersports'"><spring:message code="ProposalProductCategory.automotivePowersports" /></span>												
								<span ng-if="proposalProductCategory=='beauty'"><spring:message code="ProposalProductCategory.beauty" /></span>						
								<span ng-if="proposalProductCategory=='books'"><spring:message code="ProposalProductCategory.books" /></span>						
								<span ng-if="proposalProductCategory=='cameraPhoto'"><spring:message code="ProposalProductCategory.cameraPhoto" /></span>						
								<span ng-if="proposalProductCategory=='cellPhoneDevices'"><spring:message code="ProposalProductCategory.cellPhoneDevices" /></span>						
								<span ng-if="proposalProductCategory=='clothingAccessories'"><spring:message code="ProposalProductCategory.clothingAccessories" /></span>						
								<span ng-if="proposalProductCategory=='collectibleCoins'"><spring:message code="ProposalProductCategory.collectibleCoins" /></span>												
								<span ng-if="proposalProductCategory=='consumerElectronics'"><spring:message code="ProposalProductCategory.consumerElectronics" /></span>						
								<span ng-if="proposalProductCategory=='electronicsAccessories'"><spring:message code="ProposalProductCategory.electronicsAccessories" /></span>												
								<span ng-if="proposalProductCategory=='entertainmentCollectibles'"><spring:message code="ProposalProductCategory.entertainmentCollectibles" /></span>						
								<span ng-if="proposalProductCategory=='healthPersonalCare'"><spring:message code="ProposalProductCategory.healthPersonalCare" /></span>						
								<span ng-if="proposalProductCategory=='homeGarden'"><spring:message code="ProposalProductCategory.homeGarden" /></span>						
								<span ng-if="proposalProductCategory=='independentDesign'"><spring:message code="ProposalProductCategory.independentDesign" /></span>						
						 		<span ng-if="proposalProductCategory=='industrialScientific'"><spring:message code="ProposalProductCategory.industrialScientific" /></span>						
								<span ng-if="proposalProductCategory=='jewelry'"><spring:message code="ProposalProductCategory.jewelry" /></span>						
								<span ng-if="proposalProductCategory=='kitchen'"><spring:message code="ProposalProductCategory.kitchen" /></span>						
								<span ng-if="proposalProductCategory=='luggageTravelAccessories'"><spring:message code="ProposalProductCategory.luggageTravelAccessories" /></span>						
								<span ng-if="proposalProductCategory=='majorAppliances'"><spring:message code="ProposalProductCategory.majorAppliances" /></span>						
								<span ng-if="proposalProductCategory=='music'"><spring:message code="ProposalProductCategory.music" /></span>						
								<span ng-if="proposalProductCategory=='musicalInstruments'"><spring:message code="ProposalProductCategory.musicalInstruments" /></span>						
								<span ng-if="proposalProductCategory=='officeProducts'"><spring:message code="ProposalProductCategory.officeProducts" /></span>						
								<span ng-if="proposalProductCategory=='outdoors'"><spring:message code="ProposalProductCategory.outdoors" /></span>						
								<span ng-if="proposalProductCategory=='personalComputers'"><spring:message code="ProposalProductCategory.personalComputers" /></span>												
								<span ng-if="proposalProductCategory=='shoesHandbagsSunglasses'"><spring:message code="ProposalProductCategory.shoesHandbagsSunglasses" /></span>						
								<span ng-if="proposalProductCategory=='softwareComputerVideoGames'"><spring:message code="ProposalProductCategory.softwareComputerVideoGames" /></span>						
								<span ng-if="proposalProductCategory=='sports'"><spring:message code="ProposalProductCategory.sports" /></span>						
								<span ng-if="proposalProductCategory=='sportsCollectibles'"><spring:message code="ProposalProductCategory.sportsCollectibles" /></span>						
								<span ng-if="proposalProductCategory=='toolsHomeImprovement'"><spring:message code="ProposalProductCategory.toolsHomeImprovement" /></span>						
								<span ng-if="proposalProductCategory=='toysGames'"><spring:message code="ProposalProductCategory.toysGames" /></span>						
								<span ng-if="proposalProductCategory=='videoDVD'"><spring:message code="ProposalProductCategory.videoDVD" /></span>						
								<span ng-if="proposalProductCategory=='videoGames'"><spring:message code="ProposalProductCategory.videoGames" /></span>						
								<span ng-if="proposalProductCategory=='videoGameConsoles'"><spring:message code="ProposalProductCategory.videoGameConsoles" /></span>						
								<span ng-if="proposalProductCategory=='watches'"><spring:message code="ProposalProductCategory.watches" /></span>																																				
							</span>						
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.brandEnglish" /></b>
						</td>
						<td>
							<span ng-if="brandEng === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="brandEng != ''">{{brandEng}}</span>													
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.brandlocal" /></b>
						</td>
						<td>
							<span ng-if="brand === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="brand != ''">{{brand}}</span>													
						</td>
					</tr>				
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.supplierBaseProductCode" /></b>
						</td>
						<td>{{supplierBaseProductCode}}</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.baseProductCode" /></b>
						</td>
						<td>{{baseProductCode}}</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.manufacturerLeadTime" /></b>
						</td>
						<td>
							<span ng-if="manufacturerLeadTime === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="manufacturerLeadTime != ''">{{manufacturerLeadTime}}</span>															
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.allergy" /></b>
						</td>
						<td>
							<span ng-if="allergy === true"><spring:message code="productVersion.yes" /> {{allergyName}}</span>
							<span ng-if="allergy != true"><spring:message code="productVersion.no" /> {{allergyName}}</span>
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.materialAndPercentage" /></b>
						</td>
						<td>
							<div ng-repeat="materialLineItem in materialLineItems">
								{{materialLineItem.material}}
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.productReferenceLink" /></b>
						</td>
						<td>
						<div class="limit-text" ng-repeat="referenceLinkLineItem in referenceLinkLineItems">
							<a href="{{referenceLinkLineItem.referenceLink}}" target="_blank">{{referenceLinkLineItem.referenceLink}}</a>
						</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoSourceVersion.variationProductTitle" />
				</div>
			</div>
		</div>
		<div class="row rowframe">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th ng-if="productWithVariation=='1'&&type1!=''">
								<span ng-if="type1Column=='color'"><spring:message code="VariationType.color" /></span>												
								<span ng-if="type1Column=='sizeCapacity'"><spring:message code="VariationType.sizeCapacity" /></span>												
								<span ng-if="type1Column=='packageQuantity'"><spring:message code="VariationType.packageQuantity" /></span>				
								<span ng-if="type1Column=='material'"><spring:message code="VariationType.material" /></span>																		
								<span ng-if="type1Column=='regionalSpecification'"><spring:message code="VariationType.regionalSpecification" /></span>																				
							</th>
							<th ng-if="productWithVariation=='1'&&type2!=''">
								<span ng-if="type2Column=='color'"><spring:message code="VariationType.color" /></span>												
								<span ng-if="type2Column=='sizeCapacity'"><spring:message code="VariationType.sizeCapacity" /></span>												
								<span ng-if="type2Column=='packageQuantity'"><spring:message code="VariationType.packageQuantity" /></span>				
								<span ng-if="type2Column=='material'"><spring:message code="VariationType.material" /></span>							
								<span ng-if="type2Column=='regionalSpecification'"><spring:message code="VariationType.regionalSpecification" /></span>
							</th>
							<th style="width: 15%;"><spring:message code="productInfoSourceVersion.SKUcode" /></th>							
							<th><spring:message code="productInfoSourceVersion.GTIN" /></th>
							<th><spring:message code="productInfoSourceVersion.applicableRegion" /></th>
							<th><spring:message code="productInfoSourceVersion.productDimension" /></th>
							<th><spring:message code="productInfoSourceVersion.productWeight" /></th>
							<th class="text-right"><spring:message code="productInfoSourceVersion.FCAPrice" /></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="productLineItem in productLineItems">
							<td ng-if="productWithVariation=='1'&&type1!=''">
								<span ng-if="productLineItem.type1Value === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.type1Value != ''">{{productLineItem.type1Value}}</span>								
							</td>
							<td ng-if="productWithVariation=='1'&&type2!=''">
								<span ng-if="productLineItem.type2Value === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.type2Value != ''">{{productLineItem.type2Value}}</span>															
							</td>
							<td>{{productLineItem.SKU}}</td>
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
							<td>
								<span ng-repeat="(region, applicableRegion) in productLineItem.applicableRegion">	
									<span ng-if="applicableRegion.status =='selected'">
										<span ng-if="region == 'CA'"><spring:message code="CA" /></span>
										<span ng-if="region == 'DE'"><spring:message code="DE" /></span>
										<span ng-if="region == 'IT'"><spring:message code="IT" /></span>
										<span ng-if="region == 'FR'"><spring:message code="FR" /></span>
										<span ng-if="region == 'ES'"><spring:message code="ES" /></span>															
										<span ng-if="region == 'UK'"><spring:message code="UK" /></span>
										<span ng-if="region == 'US'"><spring:message code="US" /></span>																
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
							<td class="text-right">
								<span ng-if="productLineItem.FCAPrice === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="productLineItem.FCAPrice != ''">{{productLineItem.FCAPrice}} <spring:message code="TWD" /></span>																																																							
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>				
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoSourceVersion.complianceInfoTitle" />
				</div>
			</div>
		</div>
		<div class="row rowframe">
			<div class="col-md-12">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right vertical_th" style="width:350px;">
							<b><spring:message code="productInfoSourceVersion.originalPlace" /></b>
						</td>
						<td>
							<span ng-if="originalPlace === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="originalPlace != ''">
								<span ng-if="originalPlace == 'Taiwan'"><spring:message code="OriginalPlace.Taiwan" /></span>
								<span ng-if="originalPlace == 'China'"><spring:message code="OriginalPlace.China" /></span>
								<span ng-if="originalPlace == 'US'"><spring:message code="OriginalPlace.US" /></span>
								<span ng-if="originalPlace == 'UK'"><spring:message code="OriginalPlace.UK" /></span>						
							</span>																																																																												
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.medicalDevice" /></b>
						</td>
						<td>
							<span ng-if="medical === true"><spring:message code="productVersion.yes" /></span>
							<span ng-if="medical != true"><spring:message code="productVersion.no" /></span>												
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.wirelessTechnology" /></b>
						</td>
						<td>
							<span ng-repeat="tech in wirelessTech">
								<span ng-if="tech == 'None'"><spring:message code="WirelessTechnology.None" /></span>						
								<span ng-if="tech == 'bluetooth'"><spring:message code="WirelessTechnology.bluetooth" /></span>
								<span ng-if="tech == 'Wifi'"><spring:message code="WirelessTechnology.Wifi" /></span>
								<span ng-if="tech == 'RF'"><spring:message code="WirelessTechnology.RF" /></span>
								<span ng-if="tech == 'NFC'"><spring:message code="WirelessTechnology.NFC" /></span>
								<span ng-if="tech == 'Ultrasonic'"><spring:message code="WirelessTechnology.Ultrasonic" /></span>
								<span ng-if="tech == 'FIRIR'"><spring:message code="WirelessTechnology.FIRIR" /></span>						
							</span>												
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th"><b><spring:message code="productInfoSourceVersion.otherWirelessTechnology" /></b></td>
						<td>
							<div ng-repeat="otherWirelessTechLineItem in otherWirelessTechLineItems">
								{{otherWirelessTechLineItem.otherWirelessTech}}
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.hSCodeForSupplySideCountry" /></b>
						</td>
						<td>
							<span ng-if="hsCode === ''"><spring:message code="productVersion.none" /></span>
							<span ng-if="hsCode != ''">{{hsCode}}</span>										
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.documentationForSupplierSideCountry" /></b>
						</td>
						<td>
							<div ng-repeat="doc in documentationForSupplierSideCountry">						
								<span ng-if="doc == 'none'"><spring:message code="DocumentationForSupplierSideCountry.none" /></span>
								<span ng-if="doc == 'unsure'"><spring:message code="DocumentationForSupplierSideCountry.unsure" /></span>
								<span ng-if="doc == 'C02Required'"><spring:message code="DocumentationForSupplierSideCountry.C02Required" /></span>
								<span ng-if="doc == 'C02NotRequired'"><spring:message code="DocumentationForSupplierSideCountry.C02NotRequired" /></span>
								<span ng-if="doc == '504'"><spring:message code="DocumentationForSupplierSideCountry.504" /></span>
								<span ng-if="doc == '602'"><spring:message code="DocumentationForSupplierSideCountry.602" /></span>
								<span ng-if="doc == 'mp1'"><spring:message code="DocumentationForSupplierSideCountry.mp1" /></span>						
								<span ng-if="doc == 'mw0'"><spring:message code="DocumentationForSupplierSideCountry.mw0" /></span>
								<span ng-if="doc == 'other'"><spring:message code="DocumentationForSupplierSideCountry.other" /></span>				
							</div>						
						</td>					
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.containMaterial" /></b>
						</td>
						<td>
							<span ng-repeat="cm in containedMaterial"> 
								<span ng-if="cm == 'none'"><spring:message code="ContainedMaterial.none" /></span>						
								<span ng-if="cm == 'powder'"><spring:message code="ContainedMaterial.powder" /></span>
								<span ng-if="cm == 'liquid'"><spring:message code="ContainedMaterial.liquid" /></span>
								<span ng-if="cm == 'gel'"><spring:message code="ContainedMaterial.gel" /></span>
								<span ng-if="cm == 'battery'"><spring:message code="ContainedMaterial.battery" /></span>
								<span ng-if="cm == 'gas'"><spring:message code="ContainedMaterial.gas" /></span>						
							</span>												
						</td>
					</tr>
					<tr>
						<td class="text-right vertical_th">
							<b><spring:message code="productInfoSourceVersion.hazardousMaterials" /></b>
						</td>
						<td>
							<div ng-repeat="hazardousMaterialLineItem in hazardousMaterialLineItems">
								{{hazardousMaterialLineItem.hazardousMaterial}}
							</div>
						</td>
					</tr>					
				</table>
			</div>
		</div>
		<div class="row" ng-hide="batteryLineItems.length==0">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoSourceVersion.batteryInfoTitle" />
				</div>
			</div>				
		</div>
		<div class="row rowframe" ng-hide="batteryLineItems.length==0">
			<div class="col-md-12">
				<table class="table">
					<colgroup>
 						<col style="width:60px;" />
 						<col style="width:120px;" />
 						<col style="width:100px;" />
 						<col style="width:120px;" />
 						<col style="width:60px;" />
 						<col style="width:265px;" />
 						<col style="width:360px;" />
 						<col style="width:360px;" />
 						<col style="width:360px;" />
 					</colgroup>
					<thead>
						<tr>
							<th class="text-center"><spring:message code="productInfoSourceVersion.batteryNo" /></th>
							<th class="text-center"><spring:message code="productInfoSourceVersion.batteryType" /></th>
							<th class="text-center"><spring:message code="productInfoSourceVersion.rechargeable" /></th>
							<th class="text-right"><spring:message code="productInfoSourceVersion.batterySpecification" /></th>														
							<th></th>
							<th><spring:message code="productInfoSourceVersion.appliedVariationProduct" /></th>	
							<th><spring:message code="productInfoSourceVersion.batteryFile" /></th>
							<th><spring:message code="productInfoSourceVersion.lithiumBatteryPackingWay" /></th>
							<th><spring:message code="productInfoSourceVersion.dangerousGoodsCode" /></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="batteryLineItem in batteryLineItems">
							<td class="text-center" style="vertical-align:top;">{{$index+1}}</td>
							<td class="text-center" style="vertical-align:top;">
								<span ng-if="batteryLineItem.batteryType === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="batteryLineItem.batteryType != ''">
									<span ng-if="batteryLineItem.batteryType == '12V'"><spring:message code="BatteryType.12V" /></span>
									<span ng-if="batteryLineItem.batteryType == '9V'"><spring:message code="BatteryType.9V" /></span>
									<span ng-if="batteryLineItem.batteryType == 'A'"><spring:message code="BatteryType.A" /></span>
									<span ng-if="batteryLineItem.batteryType == 'AA'"><spring:message code="BatteryType.AA" /></span>
									<span ng-if="batteryLineItem.batteryType == 'AAA'"><spring:message code="BatteryType.AAA" /></span>
									<span ng-if="batteryLineItem.batteryType == 'AAAA'"><spring:message code="BatteryType.AAAA" /></span>
									<span ng-if="batteryLineItem.batteryType == 'C'"><spring:message code="BatteryType.C" /></span>
									<span ng-if="batteryLineItem.batteryType == 'CR123A'"><spring:message code="BatteryType.CR123A" /></span>
									<span ng-if="batteryLineItem.batteryType == 'CR2'"><spring:message code="BatteryType.CR2" /></span>
									<span ng-if="batteryLineItem.batteryType == 'CR5'"><spring:message code="BatteryType.CR5" /></span>
									<span ng-if="batteryLineItem.batteryType == 'D'"><spring:message code="BatteryType.D" /></span>
									<span ng-if="batteryLineItem.batteryType == 'LithiumIon'"><spring:message code="BatteryType.LithiumIon" /></span>
									<span ng-if="batteryLineItem.batteryType == 'LithiumMetal'"><spring:message code="BatteryType.LithiumMetal" /></span>
									<span ng-if="batteryLineItem.batteryType == 'nimh'"><spring:message code="BatteryType.nimh" /></span>							
									<span ng-if="batteryLineItem.batteryType == 'P76'"><spring:message code="BatteryType.P76" /></span>
									<span ng-if="batteryLineItem.batteryType == 'productSpecific'"><spring:message code="BatteryType.productSpecific" /></span>																								
								</span>																				
							</td>							
							<td class="text-center" style="vertical-align:top;">
								<span ng-if="batteryLineItem.rechargeable === true"><spring:message code="productVersion.yes" /></span>
								<span ng-if="batteryLineItem.rechargeable != true"><spring:message code="productVersion.no" /></span>																																
							</td>
							<td class="text-right">
								<span ng-if="batteryLineItem.cellsNumber != ''">{{batteryLineItem.cellsNumber}} [Cells]</span>																																									
								<br>
								<span ng-if="batteryLineItem.votage != ''">{{batteryLineItem.votage}} [V]</span>																											
								<br>
								<span ng-if="batteryLineItem.capacity != ''">{{batteryLineItem.capacity}} [mAh]</span>																																									
								<br>
								<span ng-if="batteryLineItem.weight != ''">{{batteryLineItem.weight}} [g]</span>										
							</td>
							<td></td>
							<td style="vertical-align:top;">
								<div ng-repeat="appliedVariationProduct in batteryLineItem.appliedVariationProduct">
									{{supplierKcode}}-{{appliedVariationProduct}}						
								</div>							
							</td>		
							<td style="vertical-align:top;">
								<div ng-repeat="batteryLineItem in batteryLineItem.batteryFileLineItems">
									<a href="${pageContext.request.contextPath}/BatteryFile?fileName={{batteryLineItem.file}}">{{batteryLineItem.file}}</a>						
								</div>
							</td>
							<td style="vertical-align:top;">
								<span ng-if="batteryLineItem.packingWay === ''"><spring:message code="productVersion.none" /></span>
								<span ng-if="batteryLineItem.packingWay != ''">
									<span ng-if="batteryLineItem.packingWay == 'packedByThemselves'"><spring:message code="LithiumBattery.PackedByThemselves" /></span>
									<span ng-if="batteryLineItem.packingWay == 'packedWithEquipment'"><spring:message code="LithiumBattery.PackedWithEquipment" /></span>
									<span ng-if="batteryLineItem.packingWay == 'packedInEquipment'"><spring:message code="LithiumBattery.PackedInEquipment" /></span>
								</span>
							</td>
							<td style="vertical-align:top;">{{batteryLineItem.dangerousGoodsCode}}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row" ng-hide="referenceFileLineItems.length==0">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoSourceVersion.referenceInformation" />
				</div>
			</div>
		</div>
		<div class="row rowframe" ng-hide="referenceFileLineItems.length==0">
			<div class="col-md-12">
				<table class="table table-fixed-width">
					<colgroup>
 						<col style="width:60px;" />
 						<col style="width:320px;" />
 						<col style="width:140px;" />
 						<col style="width:200px;" />
 						<col style="width:65px;" />
 						<col style="width:300px;" />
 					</colgroup>
					<thead>	
						<tr>
							<th class="text-center"><spring:message code="productInfoSourceVersion.referenceNo" /></th>
							<th><spring:message code="productInfoSourceVersion.referenceFileName" /> / <spring:message code="productInfoSourceVersion.referenceLink" /></th>
							<th><spring:message code="productInfoSourceVersion.referenceType" /></th>
							<th><spring:message code="productInfoSourceVersion.appliedVariationProducts" /></th>
							<th><spring:message code="productInfoSourceVersion.applicableRegions" /></th>															   																
							<th><spring:message code="productInfoSourceVersion.referenceLinkDescription" /></th>
						</tr>	
					</thead>
					<tbody>
						<tr ng-repeat="referenceFileLineItem in referenceFileLineItems" style="padding-bottom: 10px">
							<td class="text-center" style="vertical-align:top;">{{$index+1}}</td>
							<td style="vertical-align:top;">
								<a ng-show="referenceFileLineItem.file" href="${pageContext.request.contextPath}/ReferenceFile?fileName={{referenceFileLineItem.file}}">{{referenceFileLineItem.file}}</a>
								<span ng-show="referenceFileLineItem.link">{{referenceFileLineItem.link}}</span>
							</td>
							<td style="vertical-align:top;">
								<div ng-repeat="type in referenceFileLineItem.type">
									<span ng-if="type == 'ce'"><spring:message code="referenceType.ce" /></span>
									<span ng-if="type == 'reach'"><spring:message code="referenceType.reach" /></span>
									<span ng-if="type == 'rohs'"><spring:message code="referenceType.rohs" /></span>
									<span ng-if="type == 'weee'"><spring:message code="referenceType.weee" /></span>
									<span ng-if="type == 'fcc'"><spring:message code="referenceType.fcc" /></span>
									<span ng-if="type == 'fda'"><spring:message code="referenceType.fda" /></span>									
									<span ng-if="type == 'msds'"><spring:message code="referenceType.msds" /></span>
									<span ng-if="type == 'ncc'"><spring:message code="referenceType.ncc" /></span>
									<span ng-if="type == 'bluetooth'"><spring:message code="referenceType.bluetooth" /></span>																		
									<span ng-if="type == 'cp65'"><spring:message code="referenceType.cp65" /></span>
									<span ng-if="type == 'complianceOther'"><spring:message code="referenceType.complianceOther" /></span>
									<span ng-if="type == 'umFile'"><spring:message code="referenceType.umFile" /></span>
									<span ng-if="type == 'umLink'"><spring:message code="referenceType.umLink" /></span>
									<span ng-if="type == 'operationVideo'"><spring:message code="referenceType.operationVideo" /></span>
									<span ng-if="type == 'specificationTable'"><spring:message code="referenceType.specificationTable" /></span>
									<span ng-if="type == 'marketingVideo'"><spring:message code="referenceType.marketingVideo" /></span>
									<span ng-if="type == 'productPackageDesign'"><spring:message code="referenceType.productPackageDesign" /></span>
									<span ng-if="type == 'insurance'"><spring:message code="referenceType.insurance" /></span>									
									<span ng-if="type == 'OtherReferenceMaterialOther'"><spring:message code="referenceType.OtherReferenceMaterialOther" /></span>																		
								</div>
							</td>						
							<td style="vertical-align:top;">
							<div ng-repeat="appliedVariationProduct in referenceFileLineItem.appliedVariationProduct">{{supplierKcode}}-{{appliedVariationProduct}} </div>
							</td>
							<td style="vertical-align:top;">
								<span ng-repeat="applicableRegion in referenceFileLineItem.applicableRegion">									 
									<div ng-if="applicableRegion == 'CA'"><spring:message code="CA" /></div>
									<div ng-if="applicableRegion == 'DE'"><spring:message code="DE" /></div>
									<div ng-if="applicableRegion == 'ES'"><spring:message code="ES" /></div>
									<div ng-if="applicableRegion == 'FR'"><spring:message code="FR" /></div>
									<div ng-if="applicableRegion == 'IT'"><spring:message code="IT" /></div>
									<div ng-if="applicableRegion == 'TW'"><spring:message code="TW" /></div>									
									<div ng-if="applicableRegion == 'UK'"><spring:message code="UK" /></div>																		
									<div ng-if="applicableRegion == 'US'"><spring:message code="US" /></div>									
								</span>
							</td>
							<td style="vertical-align:top;">
								{{referenceFileLineItem.description}}
							</td>										
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row" ng-show="note">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productInfoSourceVersion.note" />
				</div>
			</div>
		</div>	
		<div class="row" >
			<div class="col-md-12">															
				<div id="note" ></div>															
			</div>
		</div>
		<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
		<div class="row" ng-show="innerNote">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="productInfoSourceVersion.innerNote" /></div>
			</div>
		</div>	
		<div class="row" >
			<div class="col-md-12">															
				<div id="innerNote" ></div>									
			</div>
		</div>
		</sec:authorize>
		<div style="padding-bottom: 20px"></div>			
		<div class="row">
			<div class="col-md-12 text-right">
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  				<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
						<i class="fas fa-question-circle"></i> <spring:message code="detailsOfBaseProduct.help" />								   						
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
						<a class="btn btn-info" ng-show="status =='Pending supplier action'" href="${pageContext.request.contextPath}/CoreProductInformation/SKUs/${baseProductCode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="productVersion.changeSKU" /></a>																									
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
						<a class="btn btn-info" ng-show="status =='Pending DRS review'" href="${pageContext.request.contextPath}/CoreProductInformation/SKUs/${baseProductCode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="productVersion.changeSKU" /></a>																										
					</sec:authorize>							
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">							
						<a class="btn btn-primary" ng-show="status =='Pending supplier action'" href="${pageContext.request.contextPath}/ep/${baseProductCode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
						<a class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" href="${pageContext.request.contextPath}/ep/${baseProductCode}"><i class="fas fa-pencil-alt" aria-hidden="true"></i> <spring:message code="detailsOfBaseProduct.edit" /></a>
					</sec:authorize>				
				</sec:authorize>									
			</div>
		</div>		
	</div>
</div>