<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<head>
<title> 
<c:choose>
<c:when test="${type eq 'Create'}">
<spring:message code="productInfoSourceVersion.productGeneralInformationTitle" /> - DRS
</c:when>
<c:otherwise>
<spring:message code="productInfoMarketSide.baseProductTitle" /> ${baseProductCode} <spring:message code="productInfoSourceVersion.productGeneralInformationTitle" />  - DRS											
</c:otherwise>
</c:choose>	
</title>
<link href="<c:url value="/resources/css/bootstrap-multiselect.css"/>"
	type="text/css" rel="stylesheet">

<link href="<c:url value="/resources/css/prettify.css"/>"
	type="text/css" rel="stylesheet">


 <link href="<c:url value="/resources/css/newstyle.css"/>"
	type="text/css" rel="stylesheet"> 
		
<script type='text/javascript'
	src="<c:url value="/resources/js/bootstrap-multiselect.js"/>"></script>

<script type='text/javascript'
	src="<c:url value="/resources/js/prettify.js"/>"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
	<script type='text/javascript' src="<c:url value="/resources/js/bootstrap3-wysihtml5.all.js"/>"></script>
	<link href="<c:url value="/resources/css/bootstrap3-wysihtml5.css"/>" type="text/css" rel="stylesheet">
<script>


	$(document).ready(function() {
		$('#wirelessTech').multiselect({buttonWidth:'100%'});
    	$('#documentationForSupplierSideCountry').multiselect({buttonWidth:'100%'});
    	$('#containedMaterial').multiselect({buttonWidth:'100%'});      	
    	
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

    $('#process-step li').on('click',function(){
    	 $('#process-step li').each(function(){
    		 $(this).removeClass('current');
    	 });
    	 $(this).addClass('current');
    });

    $('[data-toggle="tooltip"]').tooltip();
    
	});
	
	var app = angular.module('developingProduct', []);

	app.controller(
					'developingProductCtrl',
					[
							'$scope',
							'$http',
							function($scope, $http) {
								
								$scope.formValidated = true;
								$scope.operatorType = ${typeJson};
								$scope.marketSideRegionList = ${marketSideRegionList};	

								$scope.regionToMarketplaceMap = ${regionToMarketplaceMap};
								$scope.BaseProductCodeExisting = false;
								$scope.withVariation = "1";
								$scope.allergy = false;
								$scope.allergyName = "";
								$scope.medical = true;
								$scope.type1 = "color";
								$scope.type2 = "";
								$scope.type1Column = "color";
								$scope.type2Column = "";
								$scope.variationTypeDuplicated = false;
								$scope.SKUValueDuplicated = false;
								$scope.GTINValueDuplicated = false;
								
								
								$scope.materialLineItems = [ {
									material : ""
								} ];
								$scope.referenceLinkLineItems = [ {
									referenceLink : ""
								} ];

								$scope.productWithVariation = "1";

								$scope.productLineItems = [];
								$scope.referenceFileLineItems = [];

								$scope.otherWirelessTechLineItems = [ {
									otherWirelessTech : ""
								} ];
								$scope.hazardousMaterialLineItems = [ {
									hazardousMaterial : ""
								} ];
																
								$scope.batteryLineItems = [];

								var currentURL = document.URL;

								if (currentURL.indexOf("ep") > -1) {

								//prev update url
								//if (currentURL.indexOf("EditCoreProductInformation") > -1) {
								
									var baseProduct = ${baseProduct}; 		


									$scope.applicableRegionRecords = [];
																										 										
									var productInfoMarketSideArray = [];
									
									for(var productInfoMarketSideItem in  baseProduct["productInfoMarketSide"]){
									
										if(baseProduct["productInfoMarketSide"].hasOwnProperty(productInfoMarketSideItem)){
											productInfoMarketSideArray.push(baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["data"]);
										}
									}
									
									$scope.productInfoMarketSideItems = productInfoMarketSideArray;
																											
									var productMarketingMaterialMarketSideArray = [];
									
									for(var productMarketingMaterialMarketSideItem in baseProduct["productMarketingMaterialMarketSide"]){
										
										if(baseProduct["productMarketingMaterialMarketSide"].hasOwnProperty(productMarketingMaterialMarketSideItem)){
																						
											productMarketingMaterialMarketSideArray.push(baseProduct["productMarketingMaterialMarketSide"][productMarketingMaterialMarketSideItem]["data"]);
											
										}
																													
									}
																		
									$scope.productMarketingMaterialMarketSideItems = productMarketingMaterialMarketSideArray;
																		

									$scope.productMarketingMaterialSource =
										angular.toJson(JSON.parse(baseProduct["productMarketingMaterialSource"]["data"]));

									var jsonData = JSON.parse(baseProduct["productInfoSource"]["data"]);

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
									if($scope.allergy && $scope.allergyName!='latex')$scope.allergyOther = jsonData["allergyName"];
									$scope.materialLineItems = JSON
											.parse(jsonData["materials"]);
									$scope.referenceLinkLineItems = JSON
											.parse(jsonData["referenceLinks"]);
									$scope.productWithVariation = jsonData["productWithVariation"];
									$scope.type1 = jsonData["variationType1"];
									$scope.type2 = jsonData["variationType2"];
									$("#type1").val(jsonData["variationType1"]);
									$("#type2").val(jsonData["variationType2"]);
									
									$scope.type1Column = $("#type1 option:selected").text();
									$scope.type2Column = $("#type2 option:selected").text();
									
									$scope.productLineItems = JSON.parse(jsonData["products"]);


									$scope.originalPlace = jsonData["originalPlace"];
									$scope.medical = jsonData["medical"];
									$scope.wirelessTech = jsonData["wirelessTech"];
									$scope.documentationForSupplierSideCountry = jsonData["documentationForSupplierSideCountry"];
									$scope.otherWirelessTechLineItems = JSON
											.parse(jsonData["otherWirelessTechs"]);
									$scope.hsCode = jsonData["hsCode"];
									$scope.referenceFileLineItems = JSON
											.parse(jsonData["referenceFiles"]);
									$scope.containedMaterial = jsonData["containedMaterial"];
									$scope.hazardousMaterialLineItems = JSON
											.parse(jsonData["hazardousMaterials"]);
									$scope.batteryLineItems = JSON
											.parse(jsonData["batteries"]);
									$scope.note = jsonData["note"];
									$scope.innerNote = jsonData["innerNote"];
									$scope.status = baseProduct["productInfoSource"]["status"];

									//console.log(jsonData["containedMaterial"]);

									if(typeof($scope.containedMaterial) == "undefined"){
										$scope.batteryRequired = false;
									}else{
										if($scope.containedMaterial.indexOf('battery') == -1){
											$scope.batteryRequired = false;
										}else{
											$scope.batteryRequired = true;
										}
									}

									/*
									if(typeof(jsonData["containedMaterial"]) != "undefined" || jsonData["containedMaterial"].indexOf('battery') == -1){
										$scope.batteryRequired = false;
									}else{
										$scope.batteryRequired = true;
									}
									*/

								}else{
									
									var isDrsUser = ${isDrsUserJson};
																		
									if(!isDrsUser){
										
										$scope.supplierKcode = ${userCompanyKcodeJson};
										
									}
									
									$scope.type1Column = $("#type1 option:selected").text();
									$scope.type2Column = $("#type2 option:selected").text();
																				
									$scope.productLineItems.push({
										type1 : $scope.type1,
										type1Value : "",
										type2 : $scope.type2,
										type2Value : "",
										GTINType : "EAN",
										GTINTypeLength : 13,
										GTINValue : "",
										providedByDRS : false,
										SKU : "",
										DRSSKU : "",
										applicableRegion : {},
										applicableRegionList : [],
										FCAPrice : "",
										packageDimension1 : "",
										packageDimension2 : "",
										packageDimension3 : "",
										packageDimensionUnit : "",
										packageWeight : "",
										packageWeightUnit : "",
										status : "create"
									});
									
									$scope.productWithVariation = "1";
									$scope.batteryRequired = false;

								}

								$scope.getDRSBaseProductCode = function() {
									
									$scope.baseProductCode = "BP-"+$scope.supplierKcode+"-";
																		
								};
								
								$scope.isBaseProductCodeExist = function() {
									
									$scope.baseProductCode = "BP-"+$scope.supplierKcode+"-"+$scope.supplierBaseProductCode;
									
									var baseProductCodeExist = null;
									
									$.ajax({
										type : 'get',
										async : false,
										url : '${pageContext.request.contextPath}/ProductInfoSourceVersion/isBaseProductCodeExist/',
										contentType : "application/json; charset=utf-8",
										data : {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode
										},
										dataType : "json",
										success : function(data) {
											baseProductCodeExist = data;
											$scope.BaseProductCodeExisting = baseProductCodeExist;											
										}
									});
																		
								};
																
								$scope.checkAllergy = function() {

									if ($('#allergyNo').is(':checked')) {

										$scope.allergy = false;
										$scope.allergyName = "";

										$("#allergyOther").val("");
										$("#allergyOther").prop("disabled",
												true);

									}

									if ($('#allergyYes').is(':checked')) {

										$scope.allergy = true;
										$scope.allergyName = "latex";

										$("#allergyOther").val("");
										$("#allergyOther").prop("disabled",
												true);

									}

									if ($('#allergyYesOther').is(':checked')) {

										$scope.allergy = true;
										$scope.allergyName = $("#allergyOther")
												.val();

										$("#allergyOther").prop("disabled",
												false);

									}
									
								};

								$scope.changeAllergyName = function() {

									$scope.allergyName = $("#allergyOther")
											.val();

								};
								
								$scope.addOtherWirelessTechLineItem = function() {

									$scope.otherWirelessTechLineItems.push({
										otherWirelessTech : ''
									});

								};

								$scope.removeOtherWirelessTechLineItem = function(
										otherWirelessTechLineItem) {

									if ($scope.otherWirelessTechLineItems.length > 1) {

										var idx = $scope.otherWirelessTechLineItems
												.indexOf(otherWirelessTechLineItem);
										$scope.otherWirelessTechLineItems
												.splice(idx, 1);

									}

								};

								$scope.isPackingWayRequired = function (scope){

									var batteryType = scope.batteryLineItem.batteryType;
									if(batteryType == "LithiumIon" || batteryType == "LithiumMetal"){
										scope.batteryLineItem.isPackingWayRequired = true;
									}else{
										scope.batteryLineItem.isPackingWayRequired = false;
									}

								};

								$scope.checkContainedMaterial = function(){

									if($scope.containedMaterial.indexOf('battery') == -1){
										$scope.batteryRequired = false;
									}else{
										$scope.batteryRequired = true;
									}

								};

								$scope.addHazardousMaterialLineItem = function() {

									$scope.hazardousMaterialLineItems.push({
										hazardousMaterial : ''
									});

								};

								$scope.removeHazardousMaterialLineItem = function(
										hazardousMaterialLineItem) {

									if ($scope.hazardousMaterialLineItems.length > 1) {

										var idx = $scope.hazardousMaterialLineItems
												.indexOf(hazardousMaterialLineItem);
										$scope.hazardousMaterialLineItems
												.splice(idx, 1);

									}

								};

								$scope.addReferenceLinkLineItem = function() {

									$scope.referenceLinkLineItems.push({
										referenceLink : ''
									});

								};

								$scope.removeReferenceLinkLineItem = function(
										referenceLinkLineItem) {

									if ($scope.referenceLinkLineItems.length > 1) {

										var idx = $scope.referenceLinkLineItems
												.indexOf(referenceLinkLineItem);
										$scope.referenceLinkLineItems.splice(
												idx, 1);

									}

								};

								$scope.checkVariationTypes = function() {

									if ($('#withVariation').is(':checked')) {

										$("#type1").prop("disabled", false);
										$("#type2").prop("disabled", false);										
										$scope.withVariation = "1";
										
									}

									if ($('#noVariation').is(':checked')) {

										$("#type1").prop("disabled", true);
										$("#type2").prop("disabled", true);
										$scope.withVariation = "0";
										
									}

								};

								$scope.createProduct = function(withVariation){
									
									$scope.productLineItems.length = 0;
									
									if(withVariation == "1"){
																				
										$scope.type1Column = $("#type1 option:selected").text();
										$scope.type2Column = $("#type2 option:selected").text();
																					
										$scope.productLineItems.push({
											type1 : $scope.type1,
											type1Value : "",
											type2 : $scope.type2,
											type2Value : "",
											GTINType : "EAN",
											GTINTypeLength : 13,
											GTINValue : "",
											providedByDRS : false,
											SKU : "",
											DRSSKU : "",
											applicableRegion : {},
											applicableRegionList : [],
											FCAPrice : "",
											packageDimension1 : "",
											packageDimension2 : "",
											packageDimension3 : "",
											packageDimensionUnit : "",
											packageWeight : "",
											packageWeightUnit : "",
											status : "create"
										});
										
										$scope.productWithVariation = "1";
										
										
									}else{
																														
										$scope.productLineItems.push({
											type1 : "",
											type1Value : "",
											type2 : "",
											type2Value : "",
											GTINType : "EAN",
											GTINTypeLength : 13,
											GTINValue : "",
											providedByDRS : false,
											SKU : "",
											DRSSKU : "",
											applicableRegion : {},
											applicableRegionList : [],
											FCAPrice : "",
											packageDimension1 : "",
											packageDimension2 : "",
											packageDimension3 : "",
											packageDimensionUnit : "",
											packageWeight : "",
											packageWeightUnit : "",
											status : "create"
										});

										$scope.productWithVariation = "0";
																														
									}
																				
								};
								
								$scope.resetProduct = function() {
									
									$scope.productLineItems.length = 0;
																		
								};
																							
								$scope.addProductLineItem = function() {

									$scope.productLineItems.push({
										type1 : $scope.type1,
										type1Value : "",
										type2 : $scope.type2,
										type2Value : "",
										GTINType : "EAN",
										GTINTypeLength : 13,
										GTINValue : "",
										providedByDRS : false,
										SKU : "",
										DRSSKU:"",
										applicableRegion : {},
										applicableRegionList : [],
										FCAPrice : "",
										packageDimension1 : "",
										packageDimension2 : "",
										packageDimension3 : "",
										packageDimensionUnit : "",
										packageWeight : "",
										packageWeightUnit : "",
										status : "create"
									});

								};

								$scope.removeProductLineItem = function(
										productLineItem) {

									if ($scope.productLineItems.length > 1) {

										if (currentURL.indexOf("EditCoreProductInformation") > -1) {
											
											$scope.removeApplicableRegionRecords(productLineItem["SKU"]);
																						
										}
																														
										var idx = $scope.productLineItems
												.indexOf(productLineItem);
										$scope.productLineItems.splice(idx, 1);

									}

								};

								$scope.removeApplicableRegionRecords = function(SKU) {

									for(var applicableRegionRecord in $scope.applicableRegionRecords){
										
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
											
											if($scope.applicableRegionRecords[applicableRegionRecord]["skuCode"] == SKU){
												
												$scope.applicableRegionRecords[applicableRegionRecord]["type"] = "remove";												
												
											}
																						
										}
										
									}
									
								};
																
								$scope.checkVariationTypeDuplicated = function(){
									
									if($scope.type1 == $scope.type2){
									
										$scope.variationTypeDuplicated = true
																					
									}else{
										
										$scope.variationTypeDuplicated = false;
																			
									}
																																				
								};
								
								
								$scope.checkSKUValueDuplicated = function(item){
									
									var SKUValueArray = [];
									
									for (var productLineItem in $scope.productLineItems) {

										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
											if($scope.productLineItems[productLineItem]["SKU"] != ""){
												
												SKUValueArray.push($scope.productLineItems[productLineItem]["SKU"]);
												
											}
																															
										}
																												
									}
									
									$scope.SKUValueDuplicated = hasDuplicates(SKUValueArray);
																		
								};
																
								$scope.checkGTINValueDuplicated = function(){
										
									var GTINValueArray = [];
									
									for (var productLineItem in $scope.productLineItems) {

										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
											if($scope.productLineItems[productLineItem]["GTINValue"] != ""){
												
												GTINValueArray.push($scope.productLineItems[productLineItem]["GTINValue"]);
												
											}
																															
										}
																												
									}

										$scope.GTINValueDuplicated = hasDuplicates(GTINValueArray);
																				
								};
								
								function hasDuplicates(array) {
								    return (new Set(array)).size !== array.length;
								}
								
								$scope.setGTINTypeLength = function(GTINType,productLineItem){
								
									var GTINTypeLength = [];
									GTINTypeLength['EAN'] = 13;
									GTINTypeLength['UPC'] = 12;
																											
									var idxOfProductLineItems = $scope.productLineItems.indexOf(productLineItem);																	
									$scope.productLineItems[idxOfProductLineItems]['GTINTypeLength'] = GTINTypeLength[GTINType];
																																													
								};
																
								$scope.setApplicable = function (id,region,productLineItem,applicableRegionList) {
									
									var checked = $("#"+id+region).prop('checked');									
									var idxOfProductLineItems = $scope.productLineItems.indexOf(productLineItem);
									var idx = $scope.productLineItems[idxOfProductLineItems]['applicableRegionList'].indexOf(region);
																	
									if (currentURL.indexOf("CreateCoreProductInformation") > -1) {
									
										if(checked){
										
											$scope.productLineItems[idxOfProductLineItems]['applicableRegion'][region] = {"applicable":true,"status":"selected"};											
										    $scope.productLineItems[idxOfProductLineItems]['applicableRegionList'].push(region);
																			    
										}else{
											
											$scope.productLineItems[idxOfProductLineItems]['applicableRegion'][region] = {"applicable":false,"status":"unselected"};										
										    $scope.productLineItems[idxOfProductLineItems]['applicableRegionList'].splice(idx, 1);
																					    
										}
										
									}
									
									
									if (currentURL.indexOf("EditCoreProductInformation") > -1) {
																		
									if(checked){
										
										if (idx > -1) {
																						
											$scope.productLineItems[idxOfProductLineItems]['applicableRegion'][region] = {"applicable":true,"status":"selected"};
																					    
										}else{
																						
											$scope.productLineItems[idxOfProductLineItems]['applicableRegion'][region] = {"applicable":true,"status":"selected"};											
										    $scope.setNewApplicableRegionRecords(productLineItem,region,"add");
																					    
										}
																			
									}else{
										
										if (idx > -1) {
																						
											$scope.productLineItems[idxOfProductLineItems]['applicableRegion'][region] = {"applicable":false,"status":"aborted"};
																					    
										}else{
																						
											$scope.productLineItems[idxOfProductLineItems]['applicableRegion'][region] = {"applicable":false,"status":"unselected"};											
										    $scope.setNewApplicableRegionRecords(productLineItem,region,"remove");
																					    
										}
																				
									}
									
									}
																			
								};
																
								$scope.checkedAllApplicableRegions = function(id,referenceFileLineItem) {
									
									  $(':checkbox.applicableRegion'+id).prop('checked', true); 
									  var idxOfReferenceFileLineItems = $scope.referenceFileLineItems
										.indexOf(referenceFileLineItem);
									 
									  $scope.referenceFileLineItems[idxOfReferenceFileLineItems]['applicableRegion']=["CA","DE","ES","FR","IT","TW","UK","US"];
																		 							  
								};	
									
								$scope.setApplicableRegionForReferenceFile = function(region,
										referenceFileLineItem) {
									
									var idxOfReferenceFileLineItems = $scope.referenceFileLineItems
										.indexOf(referenceFileLineItem);
									var idx = $scope.referenceFileLineItems[idxOfReferenceFileLineItems]['applicableRegion']
										.indexOf(region);
									
									if (idx > -1) {
										$scope.referenceFileLineItems[idxOfReferenceFileLineItems]['applicableRegion']
										.splice(idx, 1);
																		
									} else {
										$scope.referenceFileLineItems[idxOfReferenceFileLineItems]['applicableRegion']
										.push(region);
																				
									}
																				
								};
								
								$scope.setNewApplicableRegionRecords = function(productLineItem,region,type) {
									
									var idxOfProductLineItems = $scope.productLineItems.indexOf(productLineItem);
									
									for(var applicableRegionRecord in $scope.applicableRegionRecords){
										
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
											
											if($scope.applicableRegionRecords[applicableRegionRecord]["skuCode"] == $scope.productLineItems[idxOfProductLineItems]['SKU'] &&
													$scope.applicableRegionRecords[applicableRegionRecord]["applicableRegion"] == region){
												
												var idxOfapplicableRegionRecords = $scope.applicableRegionRecords.indexOf($scope.applicableRegionRecords[applicableRegionRecord]);												
												$scope.applicableRegionRecords.splice(idxOfapplicableRegionRecords, 1);
																								
											}
																						
										}
										
									}
									
									$scope.applicableRegionRecords.push({
										skuCode : $scope.productLineItems[idxOfProductLineItems]['SKU'],
										applicableRegion : region,
										type : type,
										content : productLineItem
									});
																		
								};
																																																																																							
								$scope.applyProductSizeToAll = function() {

									if(
											$("#packageDimension1ToApply").val().length == "" ||$.trim($("#packageDimension1ToApply").val()) === "" 
											||$("#packageDimension2ToApply").val().length == "" ||$.trim($("#packageDimension2ToApply").val()) === ""
												||$("#packageDimension3ToApply").val().length == "" ||$.trim($("#packageDimension3ToApply").val()) === ""
													||$("#packageWeightToApply").val().length == "" ||$.trim($("#packageWeightToApply").val()) === ""
														|| $("#packageDimensionUnitToApply").val() ==""
															|| $("#packageWeightUnitToApply").val() ==""
									    )
								
								{
										alert("<spring:message code="ErrorMessage.CheckingTextBox" />")
								}else{
									
									for ( var productLineItem in $scope.productLineItems) {
										
										if ($scope.productLineItems
												.hasOwnProperty(productLineItem)) {

											$scope.productLineItems[productLineItem]["packageDimension1"] = $(
													"#packageDimension1ToApply")
													.val();
											$scope.productLineItems[productLineItem]["packageDimension2"] = $(
													"#packageDimension2ToApply")
													.val();
											$scope.productLineItems[productLineItem]["packageDimension3"] = $(
													"#packageDimension3ToApply")
													.val();
											$scope.productLineItems[productLineItem]["packageDimensionUnit"] = $(
													"#packageDimensionUnitToApply")
													.val();
											$scope.productLineItems[productLineItem]["packageWeight"] = $(
													"#packageWeightToApply")
													.val();
											$scope.productLineItems[productLineItem]["packageWeightUnit"] = $(
													"#packageWeightUnitToApply")
													.val();

										}

									}

									$("#packageDimension1ToApply").val("");
									$("#packageDimension2ToApply").val("");
									$("#packageDimension3ToApply").val("");									
									$("#packageDimensionUnitToApply").val("");									
									$("#packageWeightToApply").val("");
									$("#packageWeightUnitToApply").val("");
									
								}
									
								};

								$scope.checkMedical = function() {

									if ($('#medicalYes').is(':checked')) {

										$scope.medical = true;
										$("#MEDICAL_DEVICE").val("yes");

									}

									if ($('#medicalNo').is(':checked')) {

										$scope.medical = false;
										$("#MEDICAL_DEVICE").val("no");

									}

								};

								$scope.addReferenceInfo = function() {
									
									var appliedVar = [];
									for(var product in $scope.productLineItems){
										if ($scope.productLineItems.hasOwnProperty(product)) {
											appliedVar.push($scope.productLineItems[product]["SKU"]);
										}
									}
																		
									$scope.referenceFileLineItems.push({
										file : "",
										link : "",
										type : "",
										appliedVariationProduct : appliedVar,
										applicableRegion : [],
										description : ""
									});
																										
								};
										
								$scope.uploadReferenceFile = function(referenceLineItem, index) {
																		
									var fileData = $("#referenceFile"+index).prop("files")[0];
									var form_data = new FormData();
									form_data.append("file", fileData);
																		
									$.ajax({
										url : "${pageContext.request.contextPath}/ProductInfoSourceVersion/uploadReferenceFile",
										dataType : 'script',
										cache : false,
										contentType : false,
										processData : false,
										data : form_data,
										type : 'POST',
										success : function(data) {										
											var result = JSON.parse(data)
											if(result == "fail"){
												$("#ReferenceFileUploadFail"+index).text('<spring:message code="productVersion.fileUploadDuplication" />');	
											}else{
												$("#ReferenceFileUploadFail"+index).text("");
												var idx = $scope.referenceFileLineItems.indexOf(referenceLineItem);											
												$scope.referenceFileLineItems[idx]['file'] = fileData['name'];
												$scope.$apply();																								
											}																																	
										}

									});
																	
								};
																								
								$scope.removeReferenceFileLineItem = function(referenceFileLineItem){
								
									$.ajax({
										type : 'POST',
										url : '${pageContext.request.contextPath}/ProductInfoSourceVersion/removeReferenceFile',										
										data : {
											referenceFile : referenceFileLineItem["file"]											
										},								
										success : function(data) {
																																	
											var idx = $scope.referenceFileLineItems.indexOf(referenceFileLineItem);
									    	$scope.referenceFileLineItems.splice(idx, 1);
									    	$scope.$apply();
											
										}
														
									});
									
								};
									
								$scope.removeReferenceFileLineItemWithoutFile = function(referenceFileLineItem){
																		
									var idx = $scope.referenceFileLineItems.indexOf(referenceFileLineItem);
							    	$scope.referenceFileLineItems.splice(idx, 1);
							    	
								};
								
								$scope.uploadBatteryFile = function(batteryLineItem, index) {

									var fileData = $("#batteryFile" + index).prop("files")[0];
									var form_data = new FormData();
									form_data.append("file", fileData)
								
									$.ajax({
												url : "${pageContext.request.contextPath}/ProductInfoSourceVersion/uploadBatteryFile",
												dataType : 'script',
												cache : false,
												contentType : false,
												processData : false,
												data : form_data,
												type : 'POST',
												success : function(data) {
													var result = JSON.parse(data)
													if(result == "fail"){
														$("#BatteryFileUploadFail"+index).text('<spring:message code="productVersion.fileUploadDuplication" />');	
													}else{
														$("#BatteryFileUploadFail"+index).text("");
														var idx = $scope.batteryLineItems.indexOf(batteryLineItem);
														if(!Array.isArray($scope.batteryLineItems[idx]['batteryFileLineItems'])) $scope.batteryLineItems[idx]['batteryFileLineItems'] = [];
														$scope.batteryLineItems[idx]['batteryFileLineItems'].push({file : fileData['name']});
														$scope.$apply();																								
													}																																																																									
												}

											});

								};

								$scope.removeBatteryFile = function(batteryFile,batteryLineItem, batteryFileLineItem) {

									$.ajax({
										type : 'POST',
										url : '${pageContext.request.contextPath}/ProductInfoSourceVersion/removeBatteryFile',
										data : {
											batteryFile : batteryFile											
										},
										success : function(data) {
																		
											var idxOfBatteryLineItems = $scope.batteryLineItems.indexOf(batteryLineItem);
											var idxOfBatteryFileLineItems = $scope.batteryLineItems[idxOfBatteryLineItems]['batteryFileLineItems'].indexOf(batteryFileLineItem);
											$scope.batteryLineItems[idxOfBatteryLineItems]['batteryFileLineItems'].splice(idxOfBatteryFileLineItems,1);
											if($scope.batteryLineItems[idxOfBatteryLineItems]['batteryFileLineItems'].length == 0) $scope.batteryLineItems[idxOfBatteryLineItems]['batteryFileLineItems'] = "";
											$scope.$apply();
											
										}
														
									});									
																																				
								};

								$scope.addBatteryLineItem = function() {

									var appliedVar = [];
								for(	var product in $scope.productLineItems){
									if ($scope.productLineItems.hasOwnProperty(product)) {
										appliedVar.push($scope.productLineItems[product]["SKU"]);
									}
								}
									
									$scope.batteryLineItems.push({
										batteryType : "12V",
										rechargeable : false,
										cellsNumber : "",
										votage : "",
										capacity : "",
										weight : "",
										batteryFileLineItems : "",
										appliedVariationProduct : appliedVar,
										packingWay : "",
										isPackingWayRequired : false,
										dangerousGoodsCode : "",
									});

								};

								$scope.removeBatteryLineItem = function(
										batteryLineItem) {
									if($scope.batteryLineItems.length > 1){
										var idx = $scope.batteryLineItems.indexOf(batteryLineItem);
										$scope.batteryLineItems.splice(idx, 1);
									}
								};

								$scope.setAppliedVariationProduct = function(
										sku, batteryLineItem) {

									var idxOfBatteryLineItems = $scope.batteryLineItems
											.indexOf(batteryLineItem);
									var idx = $scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct']
											.indexOf(sku);

									if (idx > -1) {

										$scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct']
												.splice(idx, 1);
										if($scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct'].length == 0) $scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct'] = "";
									} else {
										if(!Array.isArray($scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct'])) $scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct'] = [];
										$scope.batteryLineItems[idxOfBatteryLineItems]['appliedVariationProduct']
												.push(sku);

									}

								};
								
								$scope.setAppliedVariationProductForReferenceFile = function(
										sku, referenceFileLineItem) {
								
									var idxOfReferenceFileLineItems = $scope.referenceFileLineItems
											.indexOf(referenceFileLineItem);
									var idx = $scope.referenceFileLineItems[idxOfReferenceFileLineItems]['appliedVariationProduct']
											.indexOf(sku);
									
									if (idx > -1) {
										
										$scope.referenceFileLineItems[idxOfReferenceFileLineItems]['appliedVariationProduct']
										.splice(idx, 1);
																		
									} else {
										
										$scope.referenceFileLineItems[idxOfReferenceFileLineItems]['appliedVariationProduct']
										.push(sku);
																				
									}
																		
								};
									
								$scope.getProductMarketingMaterialSourceProduct = function (productMarketingMaterialSourceProducts,sku){
									
									var result = null;
									
									for(var productMarketingMaterialSourceProduct in productMarketingMaterialSourceProducts){
										
										if (productMarketingMaterialSourceProducts.hasOwnProperty(productMarketingMaterialSourceProduct)) {
																					
										if(productMarketingMaterialSourceProducts[productMarketingMaterialSourceProduct]["SKU"]==sku){
											result = productMarketingMaterialSourceProducts[productMarketingMaterialSourceProduct];
											break;
										}	
																				
										}
										
									}
									
									return result;
									
								};
									
								//start saveDraftForProductInfoSource
								$scope.saveDraftForProductInfoSource = function(form){
									
									//generateApplicableRegionBP
									var applicableRegionBP = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
											$scope.productLineItems[productLineItem]['applicableRegionList'] = []; 
											
											for(var marketSideRegion in $scope.marketSideRegionList){

												if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																										
													var idx = $scope.productLineItems[productLineItem]['applicableRegionList'].indexOf($scope.marketSideRegionList[marketSideRegion]);
													
													var idxForApplicableRegion = $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]];
													
													if (typeof idxForApplicableRegion == 'undefined'){
													
														$scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]] = {"applicable":false,"status":"unselected"};
														
													}
																																							
													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status'] == "selected"){
														
														if(idx == -1)$scope.productLineItems[productLineItem]['applicableRegionList'].push($scope.marketSideRegionList[marketSideRegion]);
														
													}
																																						
												}

											}
																																												
										}
																			
									}
													
									for(var product in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(product)) {
																		
											applicableRegionBP = applicableRegionBP.concat($scope.productLineItems[product]['applicableRegionList']);
											$scope.productLineItems[product]['status'] = "edit";											
											
										}
										
									}
										
									//check for batteryLineItems
									for (var i = $scope.batteryLineItems.length-1 ; i >= 0; i--){									
										if($scope.batteryLineItems[i]['batteryType']=='') {
											$scope.batteryLineItems.splice(i, 1);
										}
									}
												
									//check for referenceFileLineItems
									for (var i = $scope.referenceFileLineItems.length-1 ; i >= 0; i--){									
										if(($scope.referenceFileLineItems[i]['link']=='')&&($scope.referenceFileLineItems[i]['file']=='')) {
											$scope.referenceFileLineItems.splice(i, 1);
										}
									}
																		
									//productInfoSource
									$scope.note = $('#note').val();
									$scope.innerNote = $('#innerNote').val();
									var productInfoSource = {
											applicationSN : $scope.applicationSN,
											supplierKcode : $scope.supplierKcode,
											productNameEnglish : $scope.productNameEnglish,
											productNameLocal : $scope.productNameLocal,
											proposalProductCategory : $scope.proposalProductCategory,
											brand : $scope.brand,
											brandEng: $scope.brandEng,
											baseProductCode : $scope.baseProductCode,
											manufacturerLeadTime : $scope.manufacturerLeadTime,
											allergy : $scope.allergy,
											allergyName : $scope.allergyName,
											materials : angular
													.toJson($scope.materialLineItems),
											referenceLinks : angular
													.toJson($scope.referenceLinkLineItems),
											applicableRegionBP : unique(applicableRegionBP),
											productWithVariation : $scope.productWithVariation,
											variationType1 : $scope.type1,
											variationType2 : $scope.type2,
											products : angular
													.toJson($scope.productLineItems),
											originalPlace : $scope.originalPlace,
											medical : $scope.medical,
											wirelessTech : $scope.wirelessTech,
											documentationForSupplierSideCountry : $scope.documentationForSupplierSideCountry,
											otherWirelessTechs : angular
													.toJson($scope.otherWirelessTechLineItems),
											hsCode : $scope.hsCode,
											referenceFiles : angular
													.toJson($scope.referenceFileLineItems),
											containedMaterial : $scope.containedMaterial,
											hazardousMaterials : angular
													.toJson($scope.hazardousMaterialLineItems),
											batteries : angular
													.toJson($scope.batteryLineItems),
											note : $scope.note,
											innerNote : $scope.innerNote,
											status : $scope.status
										};

									
									var productMarketingMaterialSource =
										JSON.parse($scope.productMarketingMaterialSource);

									var productMarketingMaterialSourceProducts =
										JSON.parse(productMarketingMaterialSource["products"]);

									//productMarketingMaterialSource
									var productLineItemsForMarketingMaterialSource = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
																							
											var product = $scope.getProductMarketingMaterialSourceProduct(productMarketingMaterialSourceProducts,$scope.productLineItems[productLineItem]["SKU"]);
											
											if(product){
																								
												productLineItemsForMarketingMaterialSource.push({
													SKU : product['SKU'],
													DRSSKU:product['DRSSKU'],
													type1 : product['type1'],
													type1Value : product['type1Value'],
													type2 : product['type2'],
													type2Value : product['type2Value'],
													name : product["name"],
													character : product["character"],													
													mainImageLineItems : product["mainImageLineItems"],
													variationImageLineItems : product["variationImageLineItems"],
													otherImageLineItems : product["otherImageLineItems"]													
												});
												
											}else{
																		
												productLineItemsForMarketingMaterialSource.push({
													SKU : $scope.productLineItems[productLineItem]['SKU'],
													DRSSKU : $scope.productLineItems[productLineItem]['DRSSKU'],
													type1 : $scope.productLineItems[productLineItem]['type1'],
													type1Value : $scope.productLineItems[productLineItem]['type1Value'],
													type2 : $scope.productLineItems[productLineItem]['type2'],
													type2Value : $scope.productLineItems[productLineItem]['type2Value'],
													name : "",
													character : 0,													
													mainImageLineItems : [],
													variationImageLineItems : [],
													otherImageLineItems : []													
												});
																								
											}
																															
										}										
									
									}
									
									var jsonDataForMarketingMaterialSource = {											
											name : productMarketingMaterialSource["name"],
											shortName : productMarketingMaterialSource["shortName"],
											feature1 : productMarketingMaterialSource["feature1"],
											feature2 : productMarketingMaterialSource["feature2"],
											feature3 : productMarketingMaterialSource["feature3"],
											feature4 : productMarketingMaterialSource["feature4"],
											feature5 : productMarketingMaterialSource["feature5"],
											description : productMarketingMaterialSource["description"],
											searchTerms1 : productMarketingMaterialSource["searchTerms1"],
											searchTerms2 : productMarketingMaterialSource["searchTerms2"],
											searchTerms3 : productMarketingMaterialSource["searchTerms3"],
											searchTerms4 : productMarketingMaterialSource["searchTerms4"],
											searchTerms5 : productMarketingMaterialSource["searchTerms5"],
											note : productMarketingMaterialSource["note"],
											products : angular.toJson(productLineItemsForMarketingMaterialSource),
											productWithVariation : $scope.productWithVariation	
									};
									
									
									var productMarketingMaterialSource = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											jsonData : JSON.stringify(jsonDataForMarketingMaterialSource),
											status : baseProduct["productMarketingMaterialSource"]["status"]
									};
																		
									//productInfoMarketSide
									//productMarketingMaterialMarketSide																	
									var productInfoMarketSideProducts = [];
									var productInfoMarketSideWhole = [];
									var productMarketingMaterialMarketSideProducts = [];									
									var productMarketingMaterialMarketSideWhole = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
										
											productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											
											for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems){
												                    
												if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse($scope.productInfoMarketSideItems[productInfoMarketSideItem]);
													
													if($scope.marketSideRegionList[marketSideRegion]==productInfoMarketSideProduct["country"]){
														
														var productsInfoMarketSide = $scope.getProductInfoMarketSideProducts(productInfoMarketSideProduct["products"],
																$scope.marketSideRegionList[marketSideRegion]);
														productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productsInfoMarketSide);														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productInfoMarketSideProduct;
													
													}
																																										
												}//end if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem))
												
											}//end for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems)	
											
											for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems){
												
												if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem)){
													
													var productMarketingMaterialMarketSideProduct = JSON.parse($scope.productMarketingMaterialMarketSideItems[productMarketingMaterialMarketSideItem]);  		
													
													if($scope.marketSideRegionList[marketSideRegion]==productMarketingMaterialMarketSideProduct["country"]){
														
														var productsMarketingMaterialMarketSide = $scope.getProductInfoMarketSideProducts(productMarketingMaterialMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productMarketingMaterialMarketSideProduct["products"] = productsMarketingMaterialMarketSide;
														productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productMarketingMaterialMarketSideProduct["products"]);
														productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productMarketingMaterialMarketSideProduct;
														
													}
																		
												}//end if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem))
																							
											}//end for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems)
																																		
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
										
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																																
											for(var productInfoMarketSide in productInfoMarketSideWhole){
																							
												if(productInfoMarketSideWhole.hasOwnProperty(productInfoMarketSide)){
																								
													if(typeof productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = {
																country : $scope.marketSideRegionList[marketSideRegion],
																hsCode : "",
																dutyRate : "",
																note : "",
																products : angular.toJson([]),	
																productWithVariation : $scope.productWithVariation																	
														};			
																												
													}
																																																				
												}
														
											}
																															
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)		
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
									
											for(var productMarketingMaterialMarketSide in productMarketingMaterialMarketSideWhole){
												
												if(productMarketingMaterialMarketSideWhole.hasOwnProperty(productMarketingMaterialMarketSide)){
													
												if(typeof productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
													productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]= {
															name : "",
															shortName : "",
															feature1 : "",
															feature2 : "",
															feature3 : "",
															feature4 : "",
															feature5 : "",
															description : "",
															searchTerms1 : "",
															searchTerms2 : "",
															searchTerms3 : "",
															searchTerms4 : "",
															searchTerms5 : "",
															note : "",
															country : $scope.marketSideRegionList[marketSideRegion],
															products : angular.toJson([]),
															productWithVariation : $scope.productWithVariation
													};			
																											
													}
																										
												}
																							
											}
																						
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)	
																													
									for(var applicableRegionRecord in $scope.applicableRegionRecords){
									
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
								
											var applicableRegionRecord = $scope.applicableRegionRecords[applicableRegionRecord];
											
											if(applicableRegionRecord["type"] == "add"){
											
												var marketplace = [];
												
												for(var regionToMarketplace in $scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]]){
																					
												if ($scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]].hasOwnProperty(regionToMarketplace)) {
																																												
													marketplace.push({name:$scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]][regionToMarketplace],
															 applied:false,
															 SSBP:"",
															 SSBPtax:""
													});
																						
												}
																					
												}	
																																				
											productInfoMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]["SKU"],
												DRSSKU : applicableRegionRecord["content"]["DRSSKU"],
												type1 : applicableRegionRecord["content"]["type1"],
												type1Value : applicableRegionRecord["content"]["type1Value"],
												type2 : applicableRegionRecord["content"]["type2"],
												type2Value : applicableRegionRecord["content"]["type2Value"],
												marketplace : marketplace,
												MSRP:"",
												MSRPtax:"",
												DDPprice:"",
												variationProductQuantity:"",
												packageDimension1 : "",
												packageDimension2 : "",
												packageDimension3 : "",
												packageDimensionUnit : "",
												packageWeight : "",
												packageWeightUnit : "",
												cartonPackageDimension1 : "",
												cartonPackageDimension2 : "",
												cartonPackageDimension3 : "",
												cartonPackageDimensionUnit : "",
												cartonPackageWeight : "",
												cartonPackageWeightUnit : "",												
												plasticBagWarningLabel: "No",
												disposeOfUnfulfillableInventory: "remove",
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});	
										
											productMarketingMaterialMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]['SKU'],
												DRSSKU : applicableRegionRecord["content"]['DRSSKU'],
												type1 : applicableRegionRecord["content"]['type1'],
												type1Value : applicableRegionRecord["content"]['type1Value'],
												type2 : applicableRegionRecord["content"]['type2'],
												type2Value : applicableRegionRecord["content"]['type2Value'],
												name : "",
												character : 0,												
												mainImageLineItems : [],
												variationImageLineItems : [],
												otherImageLineItems : [],
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});
											
											}
											
										}
										
									}
																		
									var productInfoMarketSide = [];
									var productMarketingMaterialMarketSide = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
									
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																						
											var productLineItemsForMarketSide = [];
											var productLineItemsForMarketingMaterialMarketSide = [];
											
											for(var productInfoMarketSideProduct in productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productInfoMarketSideProduct)){
													
													productLineItemsForMarketSide.push(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productInfoMarketSideProduct]);												
													
												}
																																																
											}
										
											for(var productMarketingMaterialMarketSideProduct in productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productMarketingMaterialMarketSideProduct)){
													
													productLineItemsForMarketingMaterialMarketSide.push(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productMarketingMaterialMarketSideProduct]);
													
												}
																							
											}
											
											var jsonDataForMarketSide = {
													country : $scope.marketSideRegionList[marketSideRegion],
													hsCode : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["hsCode"], 
													dutyRate : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["dutyRate"],
													note : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],
													products : angular.toJson(productLineItemsForMarketSide),	
													productWithVariation : $scope.productWithVariation																	
											};
																																								
											var jsonDataForMarketingMaterialMarketSide = {
													name : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["name"],
													shortName : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["shortName"],
													feature1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature1"],
													feature2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature2"],
													feature3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature3"],
													feature4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature4"],
													feature5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature5"],
													description : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["description"],
													searchTerms1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms1"],
													searchTerms2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms2"],
													searchTerms3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms3"],
													searchTerms4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms4"],
													searchTerms5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms5"],
													note : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],												
													country : $scope.marketSideRegionList[marketSideRegion],
													products : angular.toJson(productLineItemsForMarketingMaterialMarketSide),
													productWithVariation : $scope.productWithVariation
											};
																						
											var statusForMarketSide = "Pending supplier action";
											
											for(var productInfoMarketSideItem in  baseProduct["productInfoMarketSide"]){
												
												if(baseProduct["productInfoMarketSide"].hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse(baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["data"]);
													
													if(productInfoMarketSideProduct["country"] == $scope.marketSideRegionList[marketSideRegion]){
													
														statusForMarketSide = baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["status"];
														break;
													}
													
												}
																									
											}
											
											var marketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketSide),
													status : statusForMarketSide
											};
											
											var marketingMaterialMarketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketingMaterialMarketSide)													
											};	
																					
											productInfoMarketSide.push(marketSide);
											productMarketingMaterialMarketSide.push(marketingMaterialMarketSide);	
																						
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
															
									var developingProduct = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											productInfoSource : JSON.stringify(productInfoSource),
											productInfoMarketSide : JSON.stringify(productInfoMarketSide),
											productMarketingMaterialSource : JSON.stringify(productMarketingMaterialSource),
											productMarketingMaterialMarketSide : JSON.stringify(productMarketingMaterialMarketSide),
											serialNumber : $scope.applicationSN
										};
									
									$scope.SKUInvalid = false;

									for(var productLineItem in $scope.productLineItems){

										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {

											if($scope.productLineItems[productLineItem]['SKU'] == ''){
												$scope.SKUInvalid = true;
												break;
											}
											if($scope.productLineItems[productLineItem]['type1'] != '' && $scope.productLineItems[productLineItem]['type1Value'] == ''){
												$scope.SKUInvalid = true;
												break;
											}
											if($scope.productLineItems[productLineItem]['type2'] != '' && $scope.productLineItems[productLineItem]['type2Value'] == ''){
												$scope.SKUInvalid = true;
												break;
											}

										}

									}

									//=======================================================
									if($scope.SKUInvalid || $scope.SKUValueDuplicated || $scope.GTINValueDuplicated ){

										$scope.formValidated = false;
										$('#validation_message').show();
										scrollToAnchor('s5_body_padding');

									}else{

										$scope.formValidated = true;
										$("#save,#saveDraft,#submit,#update,#return,#approve").prop('disabled', true);
										$('#validation_message').hide();

										$.ajax({
											url : "${pageContext.request.contextPath}/saveDraftForCoreProductInformation",
											type : 'POST',
											data : JSON.stringify(developingProduct),
											contentType : 'application/json',
											success : function(data) {

												window.location.href = "${pageContext.request.contextPath}"
														+ data;

											},
											error : function(data, status,
													er) {
												console.log("error: "
														+ data
														+ " status: "
														+ status + " er:"
														+ er);
											}
										});

									}

								};
								//end saveDraftForProductInfoSource

								//start submitProductInfoSource
								$scope.submitProductInfoSource = function(form){
									
									//generateApplicableRegionBP
									var applicableRegionBP = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {

											$scope.productLineItems[productLineItem]['applicableRegionList'] = []; 

											for(var marketSideRegion in $scope.marketSideRegionList){

												if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {

													var idx = $scope.productLineItems[productLineItem]['applicableRegionList'].indexOf($scope.marketSideRegionList[marketSideRegion]);

													var idxForApplicableRegion = $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]];
													
													if (typeof idxForApplicableRegion == 'undefined'){

														$scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]] = {"applicable":false,"status":"unselected"};
													}


													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status'] == "selected"){

														if(idx == -1)$scope.productLineItems[productLineItem]['applicableRegionList'].push($scope.marketSideRegionList[marketSideRegion]);
														
													}
																									
												}

											}
																																												
										}
																			
									}
														
									for(var product in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(product)) {
																		
											applicableRegionBP = applicableRegionBP.concat($scope.productLineItems[product]['applicableRegionList']);
											$scope.productLineItems[product]['status'] = "edit";											
											
										}
										
									}
										
									//check for batteryLineItems
									for (var i = $scope.batteryLineItems.length-1 ; i >= 0; i--){									
										if($scope.batteryLineItems[i]['batteryType']=='') {
											$scope.batteryLineItems.splice(i, 1);
										}
									}
												
									//check for referenceFileLineItems
									for (var i = $scope.referenceFileLineItems.length-1 ; i >= 0; i--){									
										if(($scope.referenceFileLineItems[i]['link']=='')&&($scope.referenceFileLineItems[i]['file']=='')) {
											$scope.referenceFileLineItems.splice(i, 1);
										}
									}	
									
									//productInfoSource
									$scope.note = $('#note').val();
									$scope.innerNote = $('#innerNote').val();
									var productInfoSource = {
											applicationSN : $scope.applicationSN,
											supplierKcode : $scope.supplierKcode,
											productNameEnglish : $scope.productNameEnglish,
											productNameLocal : $scope.productNameLocal,
											proposalProductCategory : $scope.proposalProductCategory,
											brand : $scope.brand,
											brandEng: $scope.brandEng,
											baseProductCode : $scope.baseProductCode,
											manufacturerLeadTime : $scope.manufacturerLeadTime,
											allergy : $scope.allergy,
											allergyName : $scope.allergyName,
											materials : angular
													.toJson($scope.materialLineItems),
											referenceLinks : angular
													.toJson($scope.referenceLinkLineItems),
											applicableRegionBP : unique(applicableRegionBP),
											productWithVariation : $scope.productWithVariation,
											variationType1 : $scope.type1,
											variationType2 : $scope.type2,
											products : angular
													.toJson($scope.productLineItems),
											originalPlace : $scope.originalPlace,
											medical : $scope.medical,
											wirelessTech : $scope.wirelessTech,
											documentationForSupplierSideCountry : $scope.documentationForSupplierSideCountry,
											otherWirelessTechs : angular
													.toJson($scope.otherWirelessTechLineItems),
											hsCode : $scope.hsCode,
											referenceFiles : angular
													.toJson($scope.referenceFileLineItems),
											containedMaterial : $scope.containedMaterial,
											hazardousMaterials : angular
													.toJson($scope.hazardousMaterialLineItems),
											batteries : angular
													.toJson($scope.batteryLineItems),
											note : $scope.note,
											innerNote : $scope.innerNote,
											status : $scope.status
										};
																		
									var productMarketingMaterialSource = JSON.parse($scope.productMarketingMaterialSource);									
									var productMarketingMaterialSourceProducts = JSON.parse(productMarketingMaterialSource["products"]);
									
									//productMarketingMaterialSource									
									var productLineItemsForMarketingMaterialSource = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
																							
											var product = $scope.getProductMarketingMaterialSourceProduct(productMarketingMaterialSourceProducts,$scope.productLineItems[productLineItem]["SKU"]);
											
											if(product){
																								
												productLineItemsForMarketingMaterialSource.push({
													SKU : product['SKU'],
													DRSSKU : product['DRSSKU'],
													type1 : product['type1'],
													type1Value : product['type1Value'],
													type2 : product['type2'],
													type2Value : product['type2Value'],
													name : product["name"],
													character : product["character"],													
													mainImageLineItems : product["mainImageLineItems"],
													variationImageLineItems : product["variationImageLineItems"],
													otherImageLineItems : product["otherImageLineItems"]													
												});
												
											}else{
																		
												productLineItemsForMarketingMaterialSource.push({
													SKU : $scope.productLineItems[productLineItem]['SKU'],
													DRSSKU : $scope.productLineItems[productLineItem]['DRSSKU'],
													type1 : $scope.productLineItems[productLineItem]['type1'],
													type1Value : $scope.productLineItems[productLineItem]['type1Value'],
													type2 : $scope.productLineItems[productLineItem]['type2'],
													type2Value : $scope.productLineItems[productLineItem]['type2Value'],
													name : "",
													character : 0,													
													mainImageLineItems : [],
													variationImageLineItems : [],
													otherImageLineItems : []													
												});
																								
											}
																															
										}										
									
									}
									
									var jsonDataForMarketingMaterialSource = {											
											name : productMarketingMaterialSource["name"],
											shortName : productMarketingMaterialSource["shortName"],
											feature1 : productMarketingMaterialSource["feature1"],
											feature2 : productMarketingMaterialSource["feature2"],
											feature3 : productMarketingMaterialSource["feature3"],
											feature4 : productMarketingMaterialSource["feature4"],
											feature5 : productMarketingMaterialSource["feature5"],
											description : productMarketingMaterialSource["description"],
											searchTerms1 : productMarketingMaterialSource["searchTerms1"],
											searchTerms2 : productMarketingMaterialSource["searchTerms2"],
											searchTerms3 : productMarketingMaterialSource["searchTerms3"],
											searchTerms4 : productMarketingMaterialSource["searchTerms4"],
											searchTerms5 : productMarketingMaterialSource["searchTerms5"],
											note : productMarketingMaterialSource["note"],
											products : angular.toJson(productLineItemsForMarketingMaterialSource),
											productWithVariation : $scope.productWithVariation
									};
									
									var productMarketingMaterialSource = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											jsonData : JSON.stringify(jsonDataForMarketingMaterialSource),
											status : baseProduct["productMarketingMaterialSource"]["status"]
									};
																		
									//productInfoMarketSide
									//productMarketingMaterialMarketSide																	
									var productInfoMarketSideProducts = [];
									var productInfoMarketSideWhole = [];
									var productMarketingMaterialMarketSideProducts = [];									
									var productMarketingMaterialMarketSideWhole = [];
								
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
										
											productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											
											for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems){
												                    
												if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse($scope.productInfoMarketSideItems[productInfoMarketSideItem]);
													
													if($scope.marketSideRegionList[marketSideRegion]==productInfoMarketSideProduct["country"]){
														
														var productsInfoMarketSide = $scope.getProductInfoMarketSideProducts(productInfoMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productsInfoMarketSide);														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productInfoMarketSideProduct;
													
													}
																																										
												}//end if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem))
												
											}//end for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems)	
											
											for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems){
												
												if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem)){
													
													var productMarketingMaterialMarketSideProduct = JSON.parse($scope.productMarketingMaterialMarketSideItems[productMarketingMaterialMarketSideItem]);  		
													
													if($scope.marketSideRegionList[marketSideRegion]==productMarketingMaterialMarketSideProduct["country"]){
														
														var productsMarketingMaterialMarketSide = $scope.getProductInfoMarketSideProducts(productMarketingMaterialMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productMarketingMaterialMarketSideProduct["products"] = productsMarketingMaterialMarketSide;
														productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productMarketingMaterialMarketSideProduct["products"]);
														productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productMarketingMaterialMarketSideProduct;
														
													}
																		
												}//end if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem))
																							
											}//end for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems)
																																		
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																																
											for(var productInfoMarketSide in productInfoMarketSideWhole){
																							
												if(productInfoMarketSideWhole.hasOwnProperty(productInfoMarketSide)){
																								
													if(typeof productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = {
																country : $scope.marketSideRegionList[marketSideRegion],
																hsCode : "",
																dutyRate : "",
																note : "",
																products : angular.toJson([]),	
																productWithVariation : $scope.productWithVariation																	
														};			
																												
													}
																																																				
												}
														
											}
																															
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)		
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
									
											for(var productMarketingMaterialMarketSide in productMarketingMaterialMarketSideWhole){
												
												if(productMarketingMaterialMarketSideWhole.hasOwnProperty(productMarketingMaterialMarketSide)){
													
												if(typeof productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
													productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]= {
															name : "",
															shortName : "",
															feature1 : "",
															feature2 : "",
															feature3 : "",
															feature4 : "",
															feature5 : "",
															description : "",
															searchTerms1 : "",
															searchTerms2 : "",
															searchTerms3 : "",
															searchTerms4 : "",
															searchTerms5 : "",
															note : "",
															country : $scope.marketSideRegionList[marketSideRegion],
															products : angular.toJson([]),
															productWithVariation : $scope.productWithVariation
													};			
																											
													}
																										
												}
																							
											}
																						
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)	
																														
									for(var applicableRegionRecord in $scope.applicableRegionRecords){
									
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
								
											var applicableRegionRecord = $scope.applicableRegionRecords[applicableRegionRecord];
											
											if(applicableRegionRecord["type"] == "add"){
											
												var marketplace = [];
												
												for(var regionToMarketplace in $scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]]){
																					
												if ($scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]].hasOwnProperty(regionToMarketplace)) {
																																												
													marketplace.push({name:$scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]][regionToMarketplace],
															 applied:false,
															 SSBP:"",
															 SSBPtax:""
													});
																						
												}
																					
												}	
																																				
											productInfoMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]["SKU"],
												DRSSKU : applicableRegionRecord["content"]["DRSSKU"],
												type1 : applicableRegionRecord["content"]["type1"],
												type1Value : applicableRegionRecord["content"]["type1Value"],
												type2 : applicableRegionRecord["content"]["type2"],
												type2Value : applicableRegionRecord["content"]["type2Value"],
												marketplace : marketplace,
												MSRP:"",
												MSRPtax:"",
												DDPprice:"",
												variationProductQuantity:"",
												packageDimension1 : "",
												packageDimension2 : "",
												packageDimension3 : "",
												packageDimensionUnit : "",
												packageWeight : "",
												packageWeightUnit : "",
												cartonPackageDimension1 : "",
												cartonPackageDimension2 : "",
												cartonPackageDimension3 : "",
												cartonPackageDimensionUnit : "",
												cartonPackageWeight : "",
												cartonPackageWeightUnit : "",												
												plasticBagWarningLabel: "No",
												disposeOfUnfulfillableInventory: "remove",
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});	
										
											productMarketingMaterialMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]['SKU'],
												DRSSKU : applicableRegionRecord["content"]['DRSSKU'],
												type1 : applicableRegionRecord["content"]['type1'],
												type1Value : applicableRegionRecord["content"]['type1Value'],
												type2 : applicableRegionRecord["content"]['type2'],
												type2Value : applicableRegionRecord["content"]['type2Value'],
												name : "",
												character : 0,												
												mainImageLineItems : [],
												variationImageLineItems : [],
												otherImageLineItems : [],
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});
											
											}
											
										}
										
									}
																		
									var productInfoMarketSide = [];
									var productMarketingMaterialMarketSide = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
									
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																																	
											var productLineItemsForMarketSide = [];
											var productLineItemsForMarketingMaterialMarketSide = [];
											
											for(var productInfoMarketSideProduct in productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productInfoMarketSideProduct)){
													
													productLineItemsForMarketSide.push(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productInfoMarketSideProduct]);												
													
												}
																																																
											}
											
											for(var productMarketingMaterialMarketSideProduct in productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productMarketingMaterialMarketSideProduct)){
													
													productLineItemsForMarketingMaterialMarketSide.push(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productMarketingMaterialMarketSideProduct]);
													
												}
																							
											}
											
											var jsonDataForMarketSide = {
													country : $scope.marketSideRegionList[marketSideRegion],
													hsCode : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["hsCode"], 
													dutyRate : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["dutyRate"],
													note : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],
													products : angular.toJson(productLineItemsForMarketSide),	
													productWithVariation : $scope.productWithVariation																	
											};
																																						
											var jsonDataForMarketingMaterialMarketSide = {
													name : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["name"],
													shortName : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["shortName"],
													feature1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature1"],
													feature2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature2"],
													feature3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature3"],
													feature4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature4"],
													feature5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature5"],
													description : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["description"],
													searchTerms1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms1"],
													searchTerms2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms2"],
													searchTerms3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms3"],
													searchTerms4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms4"],
													searchTerms5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms5"],
													note : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],												
													country : $scope.marketSideRegionList[marketSideRegion],
													products : angular.toJson(productLineItemsForMarketingMaterialMarketSide),
													productWithVariation : $scope.productWithVariation
											};
																						
											var statusForMarketSide = "Pending supplier action";
											
											for(var productInfoMarketSideItem in  baseProduct["productInfoMarketSide"]){
												
												if(baseProduct["productInfoMarketSide"].hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse(baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["data"]);
													
													if(productInfoMarketSideProduct["country"] == $scope.marketSideRegionList[marketSideRegion]){
													
														statusForMarketSide = baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["status"];
														break;
													}
													
												}
																									
											}
											
											var marketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketSide),
													status : statusForMarketSide
											};
											
											var marketingMaterialMarketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketingMaterialMarketSide)													
											};	
																					
											productInfoMarketSide.push(marketSide);
											productMarketingMaterialMarketSide.push(marketingMaterialMarketSide);	
																																
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
																	
									var developingProduct = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											productInfoSource : JSON.stringify(productInfoSource),
											productInfoMarketSide : JSON.stringify(productInfoMarketSide),
											productMarketingMaterialSource : JSON.stringify(productMarketingMaterialSource),
											productMarketingMaterialMarketSide : JSON.stringify(productMarketingMaterialMarketSide),
											serialNumber : $scope.applicationSN
										};

									if(form.$invalid || $scope.BaseProductCodeExisting
											|| $scope.SKUValueDuplicated || $scope.GTINValueDuplicated || $scope.productLineItems.length == 0){

											$scope.formValidated = false;
											$('#validation_message').show();
											scrollToAnchor('s5_body_padding');

									}else{

											$scope.formValidated = true;
											$("#save,#saveDraft,#submit,#update,#return,#approve").prop('disabled', true);
											$('#validation_message').hide();

										$.ajax({
											url : "${pageContext.request.contextPath}/submitCoreProductInformation",
											type : 'POST',
											data : JSON.stringify(developingProduct),
											contentType : 'application/json',
											success : function(data) {

												window.location.href = "${pageContext.request.contextPath}"
														+ data;

											},
											error : function(data, status,
													er) {
												console.log("error: "
														+ data
														+ " status: "
														+ status + " er:"
														+ er);
											}
										});

									}

								};
								//end submitProductInfoSource

								$scope.approveProductInfoSource = function(form){
									
									//generateApplicableRegionBP
									var applicableRegionBP = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
										
											$scope.productLineItems[productLineItem]['applicableRegionList'] = []; 
											
											for(var marketSideRegion in $scope.marketSideRegionList){

												if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																										
													var idx = $scope.productLineItems[productLineItem]['applicableRegionList'].indexOf($scope.marketSideRegionList[marketSideRegion]);
													
													var idxForApplicableRegion = $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]];
													
													if (typeof idxForApplicableRegion == 'undefined'){
	 													
														$scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]] = {"applicable":false,"status":"unselected"};
														 														
													}
													
													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status'] == "selected"){
														
														if(idx == -1)$scope.productLineItems[productLineItem]['applicableRegionList'].push($scope.marketSideRegionList[marketSideRegion]);
														
													}
																																							
												}

											}
																																												
										}
																			
									}
														
									for(var product in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(product)) {
																		
											applicableRegionBP = applicableRegionBP.concat($scope.productLineItems[product]['applicableRegionList']);
											$scope.productLineItems[product]['status'] = "edit";											
											
										}
										
									}

									//check for batteryLineItems
									for (var i = $scope.batteryLineItems.length-1 ; i >= 0; i--){									
										if($scope.batteryLineItems[i]['batteryType']=='') {
											$scope.batteryLineItems.splice(i, 1);
										}
									}
												
									//check for referenceFileLineItems
									for (var i = $scope.referenceFileLineItems.length-1 ; i >= 0; i--){									
										if(($scope.referenceFileLineItems[i]['link']=='')&&($scope.referenceFileLineItems[i]['file']=='')) {
											$scope.referenceFileLineItems.splice(i, 1);
										}
									}									
									
									//productInfoSource
									$scope.note = $('#note').val();
									$scope.innerNote = $('#innerNote').val();
									var productInfoSource = {
											applicationSN : $scope.applicationSN,
											supplierKcode : $scope.supplierKcode,
											productNameEnglish : $scope.productNameEnglish,
											productNameLocal : $scope.productNameLocal,
											proposalProductCategory : $scope.proposalProductCategory,
											brand : $scope.brand,
											brandEng: $scope.brandEng,
											baseProductCode : $scope.baseProductCode,
											manufacturerLeadTime : $scope.manufacturerLeadTime,
											allergy : $scope.allergy,
											allergyName : $scope.allergyName,
											materials : angular
													.toJson($scope.materialLineItems),
											referenceLinks : angular
													.toJson($scope.referenceLinkLineItems),
											applicableRegionBP : unique(applicableRegionBP),
											productWithVariation : $scope.productWithVariation,
											variationType1 : $scope.type1,
											variationType2 : $scope.type2,
											products : angular
													.toJson($scope.productLineItems),
											originalPlace : $scope.originalPlace,
											medical : $scope.medical,
											wirelessTech : $scope.wirelessTech,
											documentationForSupplierSideCountry : $scope.documentationForSupplierSideCountry,
											otherWirelessTechs : angular
													.toJson($scope.otherWirelessTechLineItems),
											hsCode : $scope.hsCode,
											referenceFiles : angular
													.toJson($scope.referenceFileLineItems),
											containedMaterial : $scope.containedMaterial,
											hazardousMaterials : angular
													.toJson($scope.hazardousMaterialLineItems),
											batteries : angular
													.toJson($scope.batteryLineItems),
											note : $scope.note,
											innerNote : $scope.innerNote,
											status : $scope.status
										};
																		
									var productMarketingMaterialSource = JSON.parse($scope.productMarketingMaterialSource);									
									var productMarketingMaterialSourceProducts = JSON.parse(productMarketingMaterialSource["products"]);
									
									//productMarketingMaterialSource									
									var productLineItemsForMarketingMaterialSource = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
																							
											var product = $scope.getProductMarketingMaterialSourceProduct(productMarketingMaterialSourceProducts,$scope.productLineItems[productLineItem]["SKU"]);
											
											if(product){
																								
												productLineItemsForMarketingMaterialSource.push({
													SKU : product['SKU'],
													DRSSKU : product['DRSSKU'],
													type1 : product['type1'],
													type1Value : product['type1Value'],
													type2 : product['type2'],
													type2Value : product['type2Value'],
													name : product["name"],
													character : product["character"],													
													mainImageLineItems : product["mainImageLineItems"],
													variationImageLineItems : product["variationImageLineItems"],
													otherImageLineItems : product["otherImageLineItems"]													
												});
												
											}else{
																		
												productLineItemsForMarketingMaterialSource.push({
													SKU : $scope.productLineItems[productLineItem]['SKU'],
													DRSSKU : $scope.productLineItems[productLineItem]['DRSSKU'],
													type1 : $scope.productLineItems[productLineItem]['type1'],
													type1Value : $scope.productLineItems[productLineItem]['type1Value'],
													type2 : $scope.productLineItems[productLineItem]['type2'],
													type2Value : $scope.productLineItems[productLineItem]['type2Value'],
													name : "",
													character : 0,													
													mainImageLineItems : [],
													variationImageLineItems : [],
													otherImageLineItems : []													
												});
																								
											}
																															
										}										
									
									}
											
									var jsonDataForMarketingMaterialSource = {											
											name : productMarketingMaterialSource["name"],
											shortName : productMarketingMaterialSource["shortName"],
											feature1 : productMarketingMaterialSource["feature1"],
											feature2 : productMarketingMaterialSource["feature2"],
											feature3 : productMarketingMaterialSource["feature3"],
											feature4 : productMarketingMaterialSource["feature4"],
											feature5 : productMarketingMaterialSource["feature5"],
											description : productMarketingMaterialSource["description"],
											searchTerms1 : productMarketingMaterialSource["searchTerms1"],
											searchTerms2 : productMarketingMaterialSource["searchTerms2"],
											searchTerms3 : productMarketingMaterialSource["searchTerms3"],
											searchTerms4 : productMarketingMaterialSource["searchTerms4"],
											searchTerms5 : productMarketingMaterialSource["searchTerms5"],
											note : productMarketingMaterialSource["note"],
											products : angular.toJson(productLineItemsForMarketingMaterialSource),
											productWithVariation : $scope.productWithVariation
									};
									
									var productMarketingMaterialSource = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											jsonData : JSON.stringify(jsonDataForMarketingMaterialSource),
											status : baseProduct["productMarketingMaterialSource"]["status"]
									};
																		
									//productInfoMarketSide
									//productMarketingMaterialMarketSide																	
									var productInfoMarketSideProducts = [];
									var productInfoMarketSideWhole = [];
									var productMarketingMaterialMarketSideProducts = [];									
									var productMarketingMaterialMarketSideWhole = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
										
											productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											
											for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems){
												                    
												if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse($scope.productInfoMarketSideItems[productInfoMarketSideItem]);
													
													if($scope.marketSideRegionList[marketSideRegion]==productInfoMarketSideProduct["country"]){
														
														var productsInfoMarketSide = $scope.getProductInfoMarketSideProducts(productInfoMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productsInfoMarketSide);														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productInfoMarketSideProduct;
													
													}
																																																																				
												}//end if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem))
												
											}//end for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems)	
											
											for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems){
												
												if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem)){
													
													var productMarketingMaterialMarketSideProduct = JSON.parse($scope.productMarketingMaterialMarketSideItems[productMarketingMaterialMarketSideItem]);  		
													
													if($scope.marketSideRegionList[marketSideRegion]==productMarketingMaterialMarketSideProduct["country"]){
														
														var productsMarketingMaterialMarketSide = $scope.getProductInfoMarketSideProducts(productMarketingMaterialMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productMarketingMaterialMarketSideProduct["products"] = productsMarketingMaterialMarketSide;
														productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productMarketingMaterialMarketSideProduct["products"]);
														productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productMarketingMaterialMarketSideProduct;
														
													}
																		
												}//end if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem))
																							
											}//end for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems)
																																		
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																																
											for(var productInfoMarketSide in productInfoMarketSideWhole){
																							
												if(productInfoMarketSideWhole.hasOwnProperty(productInfoMarketSide)){
																								
													if(typeof productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = {
																country : $scope.marketSideRegionList[marketSideRegion],
																hsCode : "",
																dutyRate : "",
																note : "",
																products : angular.toJson([]),	
																productWithVariation : $scope.productWithVariation																	
														};			
																												
													}
																																																				
												}
														
											}
																															
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)		
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
									
											for(var productMarketingMaterialMarketSide in productMarketingMaterialMarketSideWhole){
												
												if(productMarketingMaterialMarketSideWhole.hasOwnProperty(productMarketingMaterialMarketSide)){
													
												if(typeof productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
													productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]= {
															name : "",
															shortName : "",
															feature1 : "",
															feature2 : "",
															feature3 : "",
															feature4 : "",
															feature5 : "",
															description : "",
															searchTerms1 : "",
															searchTerms2 : "",
															searchTerms3 : "",
															searchTerms4 : "",
															searchTerms5 : "",
															note : "",
															country : $scope.marketSideRegionList[marketSideRegion],
															products : angular.toJson([]),
															productWithVariation : $scope.productWithVariation
													};			
																											
													}
																										
												}
																							
											}
																						
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)	
																				
									for(var applicableRegionRecord in $scope.applicableRegionRecords){
									
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
								
											var applicableRegionRecord = $scope.applicableRegionRecords[applicableRegionRecord];
											
											if(applicableRegionRecord["type"] == "add"){
											
												var marketplace = [];
												
												for(var regionToMarketplace in $scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]]){
																					
												if ($scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]].hasOwnProperty(regionToMarketplace)) {
																																												
													marketplace.push({name:$scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]][regionToMarketplace],
															 applied:false,
															 SSBP:"",
															 SSBPtax:""
													});
																						
												}
																					
												}	
																																				
											productInfoMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]["SKU"],
												DRSSKU : applicableRegionRecord["content"]["DRSSKU"],
												type1 : applicableRegionRecord["content"]["type1"],
												type1Value : applicableRegionRecord["content"]["type1Value"],
												type2 : applicableRegionRecord["content"]["type2"],
												type2Value : applicableRegionRecord["content"]["type2Value"],
												marketplace : marketplace,
												MSRP:"",
												MSRPtax:"",
												DDPprice:"",
												variationProductQuantity:"",
												packageDimension1 : "",
												packageDimension2 : "",
												packageDimension3 : "",
												packageDimensionUnit : "",
												packageWeight : "",
												packageWeightUnit : "",
												cartonPackageDimension1 : "",
												cartonPackageDimension2 : "",
												cartonPackageDimension3 : "",
												cartonPackageDimensionUnit : "",
												cartonPackageWeight : "",
												cartonPackageWeightUnit : "",												
												plasticBagWarningLabel: "No",
												disposeOfUnfulfillableInventory: "remove",
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});	
										
											productMarketingMaterialMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]['SKU'],
												DRSSKU : applicableRegionRecord["content"]['DRSSKU'],
												type1 : applicableRegionRecord["content"]['type1'],
												type1Value : applicableRegionRecord["content"]['type1Value'],
												type2 : applicableRegionRecord["content"]['type2'],
												type2Value : applicableRegionRecord["content"]['type2Value'],
												name : "",
												character : 0,												
												mainImageLineItems : [],
												variationImageLineItems : [],
												otherImageLineItems : [],
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});
											
											}
											
										}
										
									}
																		
									var productInfoMarketSide = [];
									var productMarketingMaterialMarketSide = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
									
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																														
											var productLineItemsForMarketSide = [];
											var productLineItemsForMarketingMaterialMarketSide = [];
											
											for(var productInfoMarketSideProduct in productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productInfoMarketSideProduct)){
													
													productLineItemsForMarketSide.push(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productInfoMarketSideProduct]);												
													
												}
																																																
											}
											
											for(var productMarketingMaterialMarketSideProduct in productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productMarketingMaterialMarketSideProduct)){
													
													productLineItemsForMarketingMaterialMarketSide.push(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productMarketingMaterialMarketSideProduct]);
													
												}
																							
											}
											
											var jsonDataForMarketSide = {
													country : $scope.marketSideRegionList[marketSideRegion],
													hsCode : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["hsCode"], 
													dutyRate : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["dutyRate"],
													note : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],
													products : angular.toJson(productLineItemsForMarketSide),	
													productWithVariation : $scope.productWithVariation																	
											};
																																	
											var jsonDataForMarketingMaterialMarketSide = {
													name : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["name"],
													shortName : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["shortName"],
													feature1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature1"],
													feature2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature2"],
													feature3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature3"],
													feature4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature4"],
													feature5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature5"],
													description : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["description"],
													searchTerms1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms1"],
													searchTerms2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms2"],
													searchTerms3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms3"],
													searchTerms4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms4"],
													searchTerms5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms5"],
													note : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],												
													country : $scope.marketSideRegionList[marketSideRegion],
													products : angular.toJson(productLineItemsForMarketingMaterialMarketSide),
													productWithVariation : $scope.productWithVariation
											};
																						
											var statusForMarketSide = "Pending supplier action";
											
											for(var productInfoMarketSideItem in  baseProduct["productInfoMarketSide"]){
												
												if(baseProduct["productInfoMarketSide"].hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse(baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["data"]);
													
													if(productInfoMarketSideProduct["country"] == $scope.marketSideRegionList[marketSideRegion]){
													
														statusForMarketSide = baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["status"];
														break;
													}
													
												}
																									
											}
											
											var marketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketSide),
													status : statusForMarketSide
											};
											
											var marketingMaterialMarketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketingMaterialMarketSide)													
											};	
																					
											productInfoMarketSide.push(marketSide);
											productMarketingMaterialMarketSide.push(marketingMaterialMarketSide);	
																																
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
																	
									var developingProduct = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											productInfoSource : JSON.stringify(productInfoSource),
											productInfoMarketSide : JSON.stringify(productInfoMarketSide),
											productMarketingMaterialSource : JSON.stringify(productMarketingMaterialSource),
											productMarketingMaterialMarketSide : JSON.stringify(productMarketingMaterialMarketSide)
										};

									if(form.$invalid || $scope.BaseProductCodeExisting
											|| $scope.SKUValueDuplicated || $scope.GTINValueDuplicated || $scope.productLineItems.length == 0){

											$scope.formValidated = false;
											$('#validation_message').show();
											scrollToAnchor('s5_body_padding');

									}else{

											$scope.formValidated = true;
											$("#save,#saveDraft,#submit,#update,#return,#approve").prop('disabled', true);
											$('#validation_message').hide();

										$.ajax({
											url : "${pageContext.request.contextPath}/approveCoreProductInformation",
											type : 'POST',
											data : JSON.stringify(developingProduct),
											contentType : 'application/json',
											success : function(data) {

												window.location.href = "${pageContext.request.contextPath}"
														+ data;

											},
											error : function(data, status,
													er) {
												console.log("error: "
														+ data
														+ " status: "
														+ status + " er:"
														+ er);
											}
										});

									}

								};
								
								$scope.returnProductInfoSource = function(form){
									
									//generateApplicableRegionBP
									var applicableRegionBP = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
										
											$scope.productLineItems[productLineItem]['applicableRegionList'] = []; 
											
											for(var marketSideRegion in $scope.marketSideRegionList){

												if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																										
													var idx = $scope.productLineItems[productLineItem]['applicableRegionList'].indexOf($scope.marketSideRegionList[marketSideRegion]);
													
													var idxForApplicableRegion = $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]];
													
													if (typeof idxForApplicableRegion == 'undefined'){
	 													
														$scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]] = {"applicable":false,"status":"unselected"};
														 														
													}
													
													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status'] == "selected"){
														
														if(idx == -1)$scope.productLineItems[productLineItem]['applicableRegionList'].push($scope.marketSideRegionList[marketSideRegion]);
														
													}
																																							
												}

											}
																																												
										}
																			
									}
														
									for(var product in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(product)) {
																		
											applicableRegionBP = applicableRegionBP.concat($scope.productLineItems[product]['applicableRegionList']);
											$scope.productLineItems[product]['status'] = "edit";											
											
										}
										
									}
									
									//check for batteryLineItems
									for (var i = $scope.batteryLineItems.length-1 ; i >= 0; i--){									
										if($scope.batteryLineItems[i]['batteryType']=='') {
											$scope.batteryLineItems.splice(i, 1);
										}
									}
												
									//check for referenceFileLineItems
									for (var i = $scope.referenceFileLineItems.length-1 ; i >= 0; i--){									
										if(($scope.referenceFileLineItems[i]['link']=='')&&($scope.referenceFileLineItems[i]['file']=='')) {
											$scope.referenceFileLineItems.splice(i, 1);
										}
									}									
																						
									//productInfoSource
									$scope.note = $('#note').val();
									$scope.innerNote = $('#innerNote').val();
									var productInfoSource = {
											applicationSN : $scope.applicationSN,
											supplierKcode : $scope.supplierKcode,
											productNameEnglish : $scope.productNameEnglish,
											productNameLocal : $scope.productNameLocal,
											proposalProductCategory : $scope.proposalProductCategory,
											brand : $scope.brand,
											brandEng: $scope.brandEng,
											baseProductCode : $scope.baseProductCode,
											manufacturerLeadTime : $scope.manufacturerLeadTime,
											allergy : $scope.allergy,
											allergyName : $scope.allergyName,
											materials : angular
													.toJson($scope.materialLineItems),
											referenceLinks : angular
													.toJson($scope.referenceLinkLineItems),
											applicableRegionBP : unique(applicableRegionBP),
											productWithVariation : $scope.productWithVariation,
											variationType1 : $scope.type1,
											variationType2 : $scope.type2,
											products : angular
													.toJson($scope.productLineItems),
											originalPlace : $scope.originalPlace,
											medical : $scope.medical,
											wirelessTech : $scope.wirelessTech,
											documentationForSupplierSideCountry : $scope.documentationForSupplierSideCountry,
											otherWirelessTechs : angular
													.toJson($scope.otherWirelessTechLineItems),
											hsCode : $scope.hsCode,
											referenceFiles : angular
													.toJson($scope.referenceFileLineItems),
											containedMaterial : $scope.containedMaterial,
											hazardousMaterials : angular
													.toJson($scope.hazardousMaterialLineItems),
											batteries : angular
													.toJson($scope.batteryLineItems),
											note : $scope.note,
											innerNote : $scope.innerNote,
											status : $scope.status
										};
																		
									var productMarketingMaterialSource = JSON.parse($scope.productMarketingMaterialSource);									
									var productMarketingMaterialSourceProducts = JSON.parse(productMarketingMaterialSource["products"]);
									
									//productMarketingMaterialSource									
									var productLineItemsForMarketingMaterialSource = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
																							
											var product = $scope.getProductMarketingMaterialSourceProduct(productMarketingMaterialSourceProducts,$scope.productLineItems[productLineItem]["SKU"]);
											
											if(product){
																								
												productLineItemsForMarketingMaterialSource.push({
													SKU : product['SKU'],
													type1 : product['type1'],
													type1Value : product['type1Value'],
													type2 : product['type2'],
													type2Value : product['type2Value'],
													name : product["name"],
													character : product["character"],													
													mainImageLineItems : product["mainImageLineItems"],
													variationImageLineItems : product["variationImageLineItems"],
													otherImageLineItems : product["otherImageLineItems"]													
												});
												
											}else{
																		
												productLineItemsForMarketingMaterialSource.push({
													SKU : $scope.productLineItems[productLineItem]['SKU'],
													type1 : $scope.productLineItems[productLineItem]['type1'],
													type1Value : $scope.productLineItems[productLineItem]['type1Value'],
													type2 : $scope.productLineItems[productLineItem]['type2'],
													type2Value : $scope.productLineItems[productLineItem]['type2Value'],
													name : "",
													character : 0,													
													mainImageLineItems : [],
													variationImageLineItems : [],
													otherImageLineItems : []													
												});
																								
											}
																															
										}										
									
									}
									
									var jsonDataForMarketingMaterialSource = {											
											name : productMarketingMaterialSource["name"],
											shortName : productMarketingMaterialSource["shortName"],
											feature1 : productMarketingMaterialSource["feature1"],
											feature2 : productMarketingMaterialSource["feature2"],
											feature3 : productMarketingMaterialSource["feature3"],
											feature4 : productMarketingMaterialSource["feature4"],
											feature5 : productMarketingMaterialSource["feature5"],
											description : productMarketingMaterialSource["description"],
											searchTerms1 : productMarketingMaterialSource["searchTerms1"],
											searchTerms2 : productMarketingMaterialSource["searchTerms2"],
											searchTerms3 : productMarketingMaterialSource["searchTerms3"],
											searchTerms4 : productMarketingMaterialSource["searchTerms4"],
											searchTerms5 : productMarketingMaterialSource["searchTerms5"],
											note : productMarketingMaterialSource["note"],
											products : angular.toJson(productLineItemsForMarketingMaterialSource),
											productWithVariation : $scope.productWithVariation
									};
									
									var productMarketingMaterialSource = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											jsonData : JSON.stringify(jsonDataForMarketingMaterialSource),
											status : baseProduct["productMarketingMaterialSource"]["status"]
									};
																		
									//productInfoMarketSide
									//productMarketingMaterialMarketSide																	
									var productInfoMarketSideProducts = [];
									var productInfoMarketSideWhole = [];
									var productMarketingMaterialMarketSideProducts = [];									
									var productMarketingMaterialMarketSideWhole = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
										
											productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											
											for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems){
												                    
												if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse($scope.productInfoMarketSideItems[productInfoMarketSideItem]);
													
													if($scope.marketSideRegionList[marketSideRegion]==productInfoMarketSideProduct["country"]){
														
														var productsInfoMarketSide = $scope.getProductInfoMarketSideProducts(productInfoMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productsInfoMarketSide);														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productInfoMarketSideProduct;
													
													}
																																																																				
												}//end if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem))
												
											}//end for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems)	
											
											for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems){
												
												if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem)){
													
													var productMarketingMaterialMarketSideProduct = JSON.parse($scope.productMarketingMaterialMarketSideItems[productMarketingMaterialMarketSideItem]);  		
													
													if($scope.marketSideRegionList[marketSideRegion]==productMarketingMaterialMarketSideProduct["country"]){
														
														var productsMarketingMaterialMarketSide = $scope.getProductInfoMarketSideProducts(productMarketingMaterialMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productMarketingMaterialMarketSideProduct["products"] = productsMarketingMaterialMarketSide;
														productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productMarketingMaterialMarketSideProduct["products"]);
														productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productMarketingMaterialMarketSideProduct;
														
													}
																		
												}//end if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem))
																							
											}//end for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems)
																																		
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																																
											for(var productInfoMarketSide in productInfoMarketSideWhole){
																							
												if(productInfoMarketSideWhole.hasOwnProperty(productInfoMarketSide)){
																								
													if(typeof productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = {
																country : $scope.marketSideRegionList[marketSideRegion],
																hsCode : "",
																dutyRate : "",
																note : "",
																products : angular.toJson([]),	
																productWithVariation : $scope.productWithVariation																	
														};			
																												
													}
																																																				
												}
														
											}
																															
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)		
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
									
											for(var productMarketingMaterialMarketSide in productMarketingMaterialMarketSideWhole){
												
												if(productMarketingMaterialMarketSideWhole.hasOwnProperty(productMarketingMaterialMarketSide)){
													
												if(typeof productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
													productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]= {
															name : "",
															shortName : "",
															feature1 : "",
															feature2 : "",
															feature3 : "",
															feature4 : "",
															feature5 : "",
															description : "",
															searchTerms1 : "",
															searchTerms2 : "",
															searchTerms3 : "",
															searchTerms4 : "",
															searchTerms5 : "",
															note : "",
															country : $scope.marketSideRegionList[marketSideRegion],
															products : angular.toJson([]),
															productWithVariation : $scope.productWithVariation
													};			
																											
													}
																										
												}
																							
											}
																						
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)	
																				
									for(var applicableRegionRecord in $scope.applicableRegionRecords){
									
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
								
											var applicableRegionRecord = $scope.applicableRegionRecords[applicableRegionRecord];
											
											if(applicableRegionRecord["type"] == "add"){
											
												var marketplace = [];
												
												for(var regionToMarketplace in $scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]]){
																					
												if ($scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]].hasOwnProperty(regionToMarketplace)) {
																																												
													marketplace.push({name:$scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]][regionToMarketplace],
															 applied:false,
															 SSBP:"",
															 SSBPtax:""
													});
																						
												}
																					
												}	
																																				
											productInfoMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]["SKU"],
												type1 : applicableRegionRecord["content"]["type1"],
												type1Value : applicableRegionRecord["content"]["type1Value"],
												type2 : applicableRegionRecord["content"]["type2"],
												type2Value : applicableRegionRecord["content"]["type2Value"],
												marketplace : marketplace,
												MSRP:"",
												MSRPtax:"",
												DDPprice:"",
												variationProductQuantity:"",
												packageDimension1 : "",
												packageDimension2 : "",
												packageDimension3 : "",
												packageDimensionUnit : "",
												packageWeight : "",
												packageWeightUnit : "",
												cartonPackageDimension1 : "",
												cartonPackageDimension2 : "",
												cartonPackageDimension3 : "",
												cartonPackageDimensionUnit : "",
												cartonPackageWeight : "",
												cartonPackageWeightUnit : "",												
												plasticBagWarningLabel: "No",
												disposeOfUnfulfillableInventory: "remove",
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});	
										
											productMarketingMaterialMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]['SKU'],
												type1 : applicableRegionRecord["content"]['type1'],
												type1Value : applicableRegionRecord["content"]['type1Value'],
												type2 : applicableRegionRecord["content"]['type2'],
												type2Value : applicableRegionRecord["content"]['type2Value'],
												name : "",
												character : 0,												
												mainImageLineItems : [],
												variationImageLineItems : [],
												otherImageLineItems : [],
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});
											
											}
											
										}
										
									}
																		
									var productInfoMarketSide = [];
									var productMarketingMaterialMarketSide = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
									
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																														
											var productLineItemsForMarketSide = [];
											var productLineItemsForMarketingMaterialMarketSide = [];
											
											for(var productInfoMarketSideProduct in productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productInfoMarketSideProduct)){
													
													productLineItemsForMarketSide.push(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productInfoMarketSideProduct]);												
													
												}
																																																
											}
											
											for(var productMarketingMaterialMarketSideProduct in productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productMarketingMaterialMarketSideProduct)){
													
													productLineItemsForMarketingMaterialMarketSide.push(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productMarketingMaterialMarketSideProduct]);
													
												}
																							
											}
												
											var jsonDataForMarketSide = {
													country : $scope.marketSideRegionList[marketSideRegion],
													hsCode : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["hsCode"], 
													dutyRate : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["dutyRate"],
													note : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],
													products : angular.toJson(productLineItemsForMarketSide),	
													productWithVariation : $scope.productWithVariation																	
											};
																																				
											var jsonDataForMarketingMaterialMarketSide = {
													name : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["name"],
													shortName : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["shortName"],
													feature1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature1"],
													feature2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature2"],
													feature3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature3"],
													feature4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature4"],
													feature5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature5"],
													description : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["description"],
													searchTerms1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms1"],
													searchTerms2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms2"],
													searchTerms3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms3"],
													searchTerms4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms4"],
													searchTerms5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms5"],
													note : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],												
													country : $scope.marketSideRegionList[marketSideRegion],
													products : angular.toJson(productLineItemsForMarketingMaterialMarketSide),
													productWithVariation : $scope.productWithVariation
											};
																						
											var statusForMarketSide = "Pending supplier action";
											
											for(var productInfoMarketSideItem in  baseProduct["productInfoMarketSide"]){
												
												if(baseProduct["productInfoMarketSide"].hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse(baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["data"]);
													
													if(productInfoMarketSideProduct["country"] == $scope.marketSideRegionList[marketSideRegion]){
													
														statusForMarketSide = baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["status"];
														break;
													}
													
												}
																									
											}
											
											var marketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketSide),
													status : statusForMarketSide
											};
											
											var marketingMaterialMarketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketingMaterialMarketSide)													
											};	
																					
											productInfoMarketSide.push(marketSide);
											productMarketingMaterialMarketSide.push(marketingMaterialMarketSide);	
																																
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
																	
									var developingProduct = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											productInfoSource : JSON.stringify(productInfoSource),
											productInfoMarketSide : JSON.stringify(productInfoMarketSide),
											productMarketingMaterialSource : JSON.stringify(productMarketingMaterialSource),
											productMarketingMaterialMarketSide : JSON.stringify(productMarketingMaterialMarketSide)
										};

									if(form.$invalid || $scope.BaseProductCodeExisting
											|| $scope.SKUValueDuplicated || $scope.GTINValueDuplicated || $scope.productLineItems.length == 0){

											$scope.formValidated = false;
											$('#validation_message').show();
											scrollToAnchor('s5_body_padding');

									}else{

											$scope.formValidated = true;
											$("#save,#saveDraft,#submit,#update,#return,#approve").prop('disabled', true);
											$('#validation_message').hide();

										$.ajax({
											url : "${pageContext.request.contextPath}/returnCoreProductInformation",
											type : 'POST',
											data : JSON.stringify(developingProduct),
											contentType : 'application/json',
											success : function(data) {

												window.location.href = "${pageContext.request.contextPath}"
														+ data;

											},
											error : function(data, status,
													er) {
												console.log("error: "
														+ data
														+ " status: "
														+ status + " er:"
														+ er);
											}
										});

									}

								};
								
								$scope.validationForm = function(form){

									if(form.$invalid || $scope.BaseProductCodeExisting
											 || $scope.SKUValueDuplicated || $scope.GTINValueDuplicated || $scope.productLineItems.length == 0){

										$('#validationForm').modal('show');

									}

								};
																
								$scope.updateProductInfoSource = function(form){

									//generateApplicableRegionBP
									var applicableRegionBP = [];
								
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
											$scope.productLineItems[productLineItem]['applicableRegionList'] = []; 
											
											for(var marketSideRegion in $scope.marketSideRegionList){

												if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																										
													var idx = $scope.productLineItems[productLineItem]['applicableRegionList'].indexOf($scope.marketSideRegionList[marketSideRegion]);
													
													var idxForApplicableRegion = $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]];
													
													if (typeof idxForApplicableRegion == 'undefined'){
	 													
														$scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]] = {"applicable":false,"status":"unselected"};
														 														
													}
													
													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status'] == "selected"){
														
														if(idx == -1)$scope.productLineItems[productLineItem]['applicableRegionList'].push($scope.marketSideRegionList[marketSideRegion]);
														
													}
													
												}

											}
																																												
										}
																			
									}
														
									for(var product in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(product)) {
																		
											applicableRegionBP = applicableRegionBP.concat($scope.productLineItems[product]['applicableRegionList']);
											$scope.productLineItems[product]['status'] = "edit";											
											
										}
										
									}
																	    
									//check for batteryLineItems
									for (var i = $scope.batteryLineItems.length-1 ; i >= 0; i--){									
										if($scope.batteryLineItems[i]['batteryType']=='') {
											$scope.batteryLineItems.splice(i, 1);
										}
									}
												
									//check for referenceFileLineItems
									for (var i = $scope.referenceFileLineItems.length-1 ; i >= 0; i--){									
										if(($scope.referenceFileLineItems[i]['link']=='')&&($scope.referenceFileLineItems[i]['file']=='')) {
											$scope.referenceFileLineItems.splice(i, 1);
										}
									}
														
									//productInfoSource
									$scope.note = $('#note').val();
									$scope.innerNote = $('#innerNote').val();
									var productInfoSource = {
											applicationSN : $scope.applicationSN,
											supplierKcode : $scope.supplierKcode,
											productNameEnglish : $scope.productNameEnglish,
											productNameLocal : $scope.productNameLocal,
											proposalProductCategory : $scope.proposalProductCategory,
											brand : $scope.brand,
											brandEng: $scope.brandEng,
											baseProductCode : $scope.baseProductCode,
											manufacturerLeadTime : $scope.manufacturerLeadTime,
											allergy : $scope.allergy,
											allergyName : $scope.allergyName,
											materials : angular
													.toJson($scope.materialLineItems),
											referenceLinks : angular
													.toJson($scope.referenceLinkLineItems),
											applicableRegionBP : unique(applicableRegionBP),
											productWithVariation : $scope.productWithVariation,
											variationType1 : $scope.type1,
											variationType2 : $scope.type2,
											products : angular
													.toJson($scope.productLineItems),
											originalPlace : $scope.originalPlace,
											medical : $scope.medical,
											wirelessTech : $scope.wirelessTech,
											documentationForSupplierSideCountry : $scope.documentationForSupplierSideCountry,
											otherWirelessTechs : angular
													.toJson($scope.otherWirelessTechLineItems),
											hsCode : $scope.hsCode,
											referenceFiles : angular
													.toJson($scope.referenceFileLineItems),
											containedMaterial : $scope.containedMaterial,
											hazardousMaterials : angular
													.toJson($scope.hazardousMaterialLineItems),
											batteries : angular
													.toJson($scope.batteryLineItems),
											note : $scope.note,	
											innerNote : $scope.innerNote,
											status : $scope.status
										};
									
									var productMarketingMaterialSource = JSON.parse($scope.productMarketingMaterialSource);									
									var productMarketingMaterialSourceProducts = JSON.parse(productMarketingMaterialSource["products"]);
									
									//productMarketingMaterialSource									
									var productLineItemsForMarketingMaterialSource = [];
									
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
																							
											var product = $scope.getProductMarketingMaterialSourceProduct(productMarketingMaterialSourceProducts,$scope.productLineItems[productLineItem]["SKU"]);
											
											if(product){
																								
												productLineItemsForMarketingMaterialSource.push({
													SKU : product['SKU'],
													DRSSKU : product['DRSSKU'],
													type1 : product['type1'],
													type1Value : product['type1Value'],
													type2 : product['type2'],
													type2Value : product['type2Value'],
													name : product["name"],
													character : product["character"],													
													mainImageLineItems : product["mainImageLineItems"],
													variationImageLineItems : product["variationImageLineItems"],
													otherImageLineItems : product["otherImageLineItems"]													
												});
												
											}else{
																		
												productLineItemsForMarketingMaterialSource.push({
													SKU : $scope.productLineItems[productLineItem]['SKU'],
													DRSSKU : $scope.productLineItems[productLineItem]['DRSSKU'],
													type1 : $scope.productLineItems[productLineItem]['type1'],
													type1Value : $scope.productLineItems[productLineItem]['type1Value'],
													type2 : $scope.productLineItems[productLineItem]['type2'],
													type2Value : $scope.productLineItems[productLineItem]['type2Value'],
													name : "",
													character : 0,													
													mainImageLineItems : [],
													variationImageLineItems : [],
													otherImageLineItems : []													
												});
																								
											}
																															
										}										
									
									}
										
									var jsonDataForMarketingMaterialSource = {											
											name : productMarketingMaterialSource["name"],
											shortName : productMarketingMaterialSource["shortName"],
											feature1 : productMarketingMaterialSource["feature1"],
											feature2 : productMarketingMaterialSource["feature2"],
											feature3 : productMarketingMaterialSource["feature3"],
											feature4 : productMarketingMaterialSource["feature4"],
											feature5 : productMarketingMaterialSource["feature5"],
											description : productMarketingMaterialSource["description"],
											searchTerms1 : productMarketingMaterialSource["searchTerms1"],
											searchTerms2 : productMarketingMaterialSource["searchTerms2"],
											searchTerms3 : productMarketingMaterialSource["searchTerms3"],
											searchTerms4 : productMarketingMaterialSource["searchTerms4"],
											searchTerms5 : productMarketingMaterialSource["searchTerms5"],
											note : productMarketingMaterialSource["note"],
											products : angular.toJson(productLineItemsForMarketingMaterialSource),
											productWithVariation : $scope.productWithVariation
									};
									
									var productMarketingMaterialSource = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											jsonData : JSON.stringify(jsonDataForMarketingMaterialSource),
											status : baseProduct["productMarketingMaterialSource"]["status"]
									};
																		
									//productInfoMarketSide
									//productMarketingMaterialMarketSide																	
									var productInfoMarketSideProducts = [];
									var productInfoMarketSideWhole = [];
									var productMarketingMaterialMarketSideProducts = [];									
									var productMarketingMaterialMarketSideWhole = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
										
											productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]=[];
											
											for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems){
												                    
												if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse($scope.productInfoMarketSideItems[productInfoMarketSideItem]);
													
													if($scope.marketSideRegionList[marketSideRegion]==productInfoMarketSideProduct["country"]){
														
														var productsInfoMarketSide = $scope.getProductInfoMarketSideProducts(productInfoMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productsInfoMarketSide);														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productInfoMarketSideProduct;
													
													}
																																																																				
												}//end if($scope.productInfoMarketSideItems.hasOwnProperty(productInfoMarketSideItem))
												
											}//end for(var productInfoMarketSideItem in $scope.productInfoMarketSideItems)	
											
											for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems){
												
												if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem)){
													
													var productMarketingMaterialMarketSideProduct = JSON.parse($scope.productMarketingMaterialMarketSideItems[productMarketingMaterialMarketSideItem]);  		
													
													if($scope.marketSideRegionList[marketSideRegion]==productMarketingMaterialMarketSideProduct["country"]){
														
														var productsMarketingMaterialMarketSide = $scope.getProductInfoMarketSideProducts(productMarketingMaterialMarketSideProduct["products"],$scope.marketSideRegionList[marketSideRegion]);
														productMarketingMaterialMarketSideProduct["products"] = productsMarketingMaterialMarketSide;
														productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]] = JSON.parse(productMarketingMaterialMarketSideProduct["products"]);
														productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = productMarketingMaterialMarketSideProduct;
														
													}
																		
												}//end if($scope.productMarketingMaterialMarketSideItems.hasOwnProperty(productMarketingMaterialMarketSideItem))
																							
											}//end for(var productMarketingMaterialMarketSideItem in $scope.productMarketingMaterialMarketSideItems)
																																		
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
																				
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																																
											for(var productInfoMarketSide in productInfoMarketSideWhole){
																							
												if(productInfoMarketSideWhole.hasOwnProperty(productInfoMarketSide)){
																								
													if(typeof productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
														productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] = {
																country : $scope.marketSideRegionList[marketSideRegion],
																hsCode : "",
																dutyRate : "",
																note : "",
																products : angular.toJson([]),	
																productWithVariation : $scope.productWithVariation																	
														};			
																												
													}
																																																				
												}
														
											}
																															
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)		
									
									for(var marketSideRegion in $scope.marketSideRegionList){
										
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
									
											for(var productMarketingMaterialMarketSide in productMarketingMaterialMarketSideWhole){
												
												if(productMarketingMaterialMarketSideWhole.hasOwnProperty(productMarketingMaterialMarketSide)){
													
												if(typeof productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]] == 'undefined'){
														
													productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]= {
															name : "",
															shortName : "",
															feature1 : "",
															feature2 : "",
															feature3 : "",
															feature4 : "",
															feature5 : "",
															description : "",
															searchTerms1 : "",
															searchTerms2 : "",
															searchTerms3 : "",
															searchTerms4 : "",
															searchTerms5 : "",
															note : "",
															country : $scope.marketSideRegionList[marketSideRegion],
															products : angular.toJson([]),
															productWithVariation : $scope.productWithVariation
													};			
																											
													}
																										
												}
																							
											}
																						
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)	
																			
									for(var applicableRegionRecord in $scope.applicableRegionRecords){
									
										if ($scope.applicableRegionRecords.hasOwnProperty(applicableRegionRecord)) {
								
											var applicableRegionRecord = $scope.applicableRegionRecords[applicableRegionRecord];
											
											if(applicableRegionRecord["type"] == "add"){
											
												var marketplace = [];
												
												for(var regionToMarketplace in $scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]]){
																					
												if ($scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]].hasOwnProperty(regionToMarketplace)) {
																																												
													marketplace.push({name:$scope.regionToMarketplaceMap[applicableRegionRecord["applicableRegion"]][regionToMarketplace],
															 applied:false,
															 SSBP:"",
															 SSBPtax:""
													});
																						
												}
																					
												}	
																																				
											productInfoMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]["SKU"],
												DRSSKU : applicableRegionRecord["content"]["DRSSKU"],
												type1 : applicableRegionRecord["content"]["type1"],
												type1Value : applicableRegionRecord["content"]["type1Value"],
												type2 : applicableRegionRecord["content"]["type2"],
												type2Value : applicableRegionRecord["content"]["type2Value"],
												marketplace : marketplace,
												MSRP:"",
												MSRPtax:"",
												DDPprice:"",
												variationProductQuantity:"",
												packageDimension1 : "",
												packageDimension2 : "",
												packageDimension3 : "",
												packageDimensionUnit : "",
												packageWeight : "",
												packageWeightUnit : "",
												cartonPackageDimension1 : "",
												cartonPackageDimension2 : "",
												cartonPackageDimension3 : "",
												cartonPackageDimensionUnit : "",
												cartonPackageWeight : "",
												cartonPackageWeightUnit : "",												
												plasticBagWarningLabel: "No",
												disposeOfUnfulfillableInventory: "remove",
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});	
										
											productMarketingMaterialMarketSideProducts[applicableRegionRecord["applicableRegion"]].push({
												SKU : applicableRegionRecord["content"]['SKU'],
												DRSSKU : applicableRegionRecord["content"]['DRSSKU'],
												type1 : applicableRegionRecord["content"]['type1'],
												type1Value : applicableRegionRecord["content"]['type1Value'],
												type2 : applicableRegionRecord["content"]['type2'],
												type2Value : applicableRegionRecord["content"]['type2Value'],
												name : "",
												character : 0,												
												mainImageLineItems : [],
												variationImageLineItems : [],
												otherImageLineItems : [],
												status : applicableRegionRecord["content"]["applicableRegion"][applicableRegionRecord["applicableRegion"]]["status"]
											});
											
											}
											
										}
										
									}
																		
									var productInfoMarketSide = [];
									var productMarketingMaterialMarketSide = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
									
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																						
											var productLineItemsForMarketSide = [];
											var productLineItemsForMarketingMaterialMarketSide = [];
											
											for(var productInfoMarketSideProduct in productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productInfoMarketSideProduct)){
													
													productLineItemsForMarketSide.push(productInfoMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productInfoMarketSideProduct]);												
													
												}
																																																
											}
											
											for(var productMarketingMaterialMarketSideProduct in productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]]){
												
												if(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(productMarketingMaterialMarketSideProduct)){
													
													productLineItemsForMarketingMaterialMarketSide.push(productMarketingMaterialMarketSideProducts[$scope.marketSideRegionList[marketSideRegion]][productMarketingMaterialMarketSideProduct]);
													
												}
																							
											}
											
											var jsonDataForMarketSide = {
													country : $scope.marketSideRegionList[marketSideRegion],
													hsCode : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["hsCode"], 
													dutyRate : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["dutyRate"],
													note : productInfoMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],
													products : angular.toJson(productLineItemsForMarketSide),	
													productWithVariation : $scope.productWithVariation																	
											};
																																					
											var jsonDataForMarketingMaterialMarketSide = {
													name : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["name"],
													shortName : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["shortName"],
													feature1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature1"],
													feature2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature2"],
													feature3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature3"],
													feature4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature4"],
													feature5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["feature5"],
													description : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["description"],
													searchTerms1 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms1"],
													searchTerms2 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms2"],
													searchTerms3 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms3"],
													searchTerms4 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms4"],
													searchTerms5 : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["searchTerms5"],
													note : productMarketingMaterialMarketSideWhole[$scope.marketSideRegionList[marketSideRegion]]["note"],												
													country : $scope.marketSideRegionList[marketSideRegion],
													products : angular.toJson(productLineItemsForMarketingMaterialMarketSide),
													productWithVariation : $scope.productWithVariation
											};
																						
											var statusForMarketSide = "Pending supplier action";
											
											for(var productInfoMarketSideItem in  baseProduct["productInfoMarketSide"]){
												
												if(baseProduct["productInfoMarketSide"].hasOwnProperty(productInfoMarketSideItem)){
													
													var productInfoMarketSideProduct = JSON.parse(baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["data"]);
													
													if(productInfoMarketSideProduct["country"] == $scope.marketSideRegionList[marketSideRegion]){
													
														statusForMarketSide = baseProduct["productInfoMarketSide"][productInfoMarketSideItem]["status"];
														break;
													}
													
												}
																									
											}
											
											var marketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketSide),
													status : statusForMarketSide
											};
											
											var marketingMaterialMarketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketingMaterialMarketSide)													
											};	
																					
											productInfoMarketSide.push(marketSide);
											productMarketingMaterialMarketSide.push(marketingMaterialMarketSide);	
																																
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
										
									}//end for(var marketSideRegion in $scope.marketSideRegionList)
																	
									var developingProduct = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											productInfoSource : JSON.stringify(productInfoSource),
											productInfoMarketSide : JSON.stringify(productInfoMarketSide),
											productMarketingMaterialSource : JSON.stringify(productMarketingMaterialSource),
											productMarketingMaterialMarketSide : JSON.stringify(productMarketingMaterialMarketSide)
										};

									if(form.$invalid || $scope.BaseProductCodeExisting
											|| $scope.SKUValueDuplicated || $scope.GTINValueDuplicated || $scope.productLineItems.length == 0){

											$scope.formValidated = false;
											$('#validation_message').show();
											scrollToAnchor('s5_body_padding');

									}else{

											$scope.formValidated = true;
											$("#save,#saveDraft,#submit,#update,#return,#approve").prop('disabled', true);
											$('#validation_message').hide();

										$.ajax({
											url : "${pageContext.request.contextPath}/updateCoreProductInformation",
											type : 'POST',
											data : JSON.stringify(developingProduct),
											contentType : 'application/json',
											success : function(data) {

												window.location.href = "${pageContext.request.contextPath}"
														+ data;

											},
											error : function(data, status,
													er) {
												console.log("error: "
														+ data
														+ " status: "
														+ status + " er:"
														+ er);
											}
										});

									}

								};
								

								$scope.getProductInfoMarketSideProducts = function(products,region) {
																	
									var products = JSON.parse(products);
									
									for(var product in products){
																				
										if (products.hasOwnProperty(product)) {
																						
											products[product]["status"] = $scope.getProductStatusByRegion(products[product]["SKU"],region);
											
										}
										
									}
								
									return JSON.stringify(products);
																											
								};
								
								$scope.getProductStatusByRegion = function(sku,region) {
								
									var status = null; 
															
									for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
											if($scope.productLineItems[productLineItem]["SKU"] == sku){
											
												status = $scope.productLineItems[productLineItem]['applicableRegion'][region]['status'];												
												break;												
											}
																																	
										}
																				
									}
									
									return status;
									
								};
																
								$scope.saveDevelopingProduct = function(form) {
								
									//generateApplicableRegionBP
									var applicableRegionBP = [];
									
										for(var productLineItem in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
													
											$scope.productLineItems[productLineItem]['applicableRegionList'] = [];
											
											for(var marketSideRegion in $scope.marketSideRegionList){

												if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
																										
													var idx = $scope.productLineItems[productLineItem]['applicableRegionList'].indexOf($scope.marketSideRegionList[marketSideRegion]);
													
													var idxForApplicableRegion = $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]];
													
													if (typeof idxForApplicableRegion == 'undefined'){
	 													
														$scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]] = {"applicable":false,"status":"unselected"};
														 														
													}
													
													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status'] == "selected"){
														
														if(idx == -1)$scope.productLineItems[productLineItem]['applicableRegionList'].push($scope.marketSideRegionList[marketSideRegion]);
														
													}
																																					
												}

											}
																																												
										}
																			
									}
																										
									for(var product in $scope.productLineItems){
										
										if ($scope.productLineItems.hasOwnProperty(product)) {
																		
											applicableRegionBP = applicableRegionBP.concat($scope.productLineItems[product]['applicableRegionList']);
											$scope.productLineItems[product]['status'] = "edit";
											
										}
										
									}
										
									//check for batteryLineItems
									for (var i = $scope.batteryLineItems.length-1 ; i >= 0; i--){									
										if($scope.batteryLineItems[i]['batteryType']=='') {
											$scope.batteryLineItems.splice(i, 1);
										}
									}
												
									//check for referenceFileLineItems
									for (var i = $scope.referenceFileLineItems.length-1 ; i >= 0; i--){									
										if(($scope.referenceFileLineItems[i]['link']=='')&&($scope.referenceFileLineItems[i]['file']=='')) {
											$scope.referenceFileLineItems.splice(i, 1);
										}
									}
																		
									//productInfoSource
									$scope.note = $('#note').val();
									$scope.innerNote = $('#innerNote').val();								           
									var productInfoSource = {
											applicationSN : $scope.applicationSN,
											supplierKcode : $scope.supplierKcode,
											productNameEnglish : $scope.productNameEnglish,
											productNameLocal : $scope.productNameLocal,
											proposalProductCategory : $scope.proposalProductCategory,
											brand : $scope.brand,
											brandEng: $scope.brandEng,
											baseProductCode : $scope.baseProductCode,
											manufacturerLeadTime : $scope.manufacturerLeadTime,
											allergy : $scope.allergy,
											allergyName : $scope.allergyName,
											materials : angular
													.toJson($scope.materialLineItems),
											referenceLinks : angular
													.toJson($scope.referenceLinkLineItems),
											applicableRegionBP : unique(applicableRegionBP),
											productWithVariation : $scope.productWithVariation,
											variationType1 : $scope.type1,
											variationType2 : $scope.type2,
											products : angular
													.toJson($scope.productLineItems),
											originalPlace : $scope.originalPlace,
											medical : $scope.medical,
											wirelessTech : $scope.wirelessTech,
											documentationForSupplierSideCountry : $scope.documentationForSupplierSideCountry,
											otherWirelessTechs : angular
													.toJson($scope.otherWirelessTechLineItems),
											hsCode : $scope.hsCode,
											referenceFiles : angular
													.toJson($scope.referenceFileLineItems),
											containedMaterial : $scope.containedMaterial,
											hazardousMaterials : angular
													.toJson($scope.hazardousMaterialLineItems),
											batteries : angular
													.toJson($scope.batteryLineItems),
											note : $scope.note,
											innerNote : $scope.innerNote
										};
									
									//productMarketingMaterialSource									
									var productLineItemsForMarketingMaterialSource = [];
									
									for(var productLineItem in $scope.productLineItems){
									
										if ($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
											productLineItemsForMarketingMaterialSource.push({
												SKU : $scope.productLineItems[productLineItem]['SKU'],
												type1 : $scope.productLineItems[productLineItem]['type1'],
												type1Value : $scope.productLineItems[productLineItem]['type1Value'],
												type2 : $scope.productLineItems[productLineItem]['type2'],
												type2Value : $scope.productLineItems[productLineItem]['type2Value'],
												name : "",
												character : 0,												
												mainImageLineItems : [],
												variationImageLineItems : [],
												otherImageLineItems : []						
											});
																	
										}
																									
									}
									
									var jsonDataForMarketingMaterialSource = {											
											name : "",
											shortName : "",
											feature1 : "",
											feature2 : "",
											feature3 : "",
											feature4 : "",
											feature5 : "",
											description : "",
											searchTerms1 : "",
											searchTerms2 : "",
											searchTerms3 : "",
											searchTerms4 : "",
											searchTerms5 : "",
											note : "",
											products : angular.toJson(productLineItemsForMarketingMaterialSource),
											productWithVariation : $scope.productWithVariation
									};
									
									var productMarketingMaterialSource = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											jsonData : JSON.stringify(jsonDataForMarketingMaterialSource)													
									};
									
									//productInfoMarketSide
									//productMarketingMaterialMarketSide
									var productInfoMarketSide = [];
									var productMarketingMaterialMarketSide = [];
									
									for(var marketSideRegion in $scope.marketSideRegionList){
									
										if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion)) {
										
											var productLineItemsForMarketSide = [];
											var productLineItemsForMarketingMaterialMarketSide = [];
											
											for(var productLineItem in $scope.productLineItems){	
												
												if($scope.productLineItems.hasOwnProperty(productLineItem)) {
											
													if($scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['applicable']){
																												
														var marketplace = [];
														
														for(var regionToMarketplace in $scope.regionToMarketplaceMap[$scope.marketSideRegionList[marketSideRegion]]){
																							
														if ($scope.regionToMarketplaceMap[$scope.marketSideRegionList[marketSideRegion]].hasOwnProperty(regionToMarketplace)) {
																																																
															marketplace.push({name:$scope.regionToMarketplaceMap[$scope.marketSideRegionList[marketSideRegion]][regionToMarketplace],
																	 		  applied:false,
																	 		  SSBP:"",
																	 		  SSBPtax:""
															});
																								
														}
																							
														}
														
														productLineItemsForMarketSide.push({
															SKU : $scope.productLineItems[productLineItem]['SKU'],
															DRSSKU : $scope.productLineItems[productLineItem]['DRSSKU'],
															type1 : $scope.productLineItems[productLineItem]['type1'],
															type1Value : $scope.productLineItems[productLineItem]['type1Value'],
															type2 : $scope.productLineItems[productLineItem]['type2'],
															type2Value : $scope.productLineItems[productLineItem]['type2Value'],
															marketplace : marketplace,
															MSRP:"",
															MSRPtax:"",
															DDPprice:"",
															variationProductQuantity:"",
															packageDimension1 : "",
															packageDimension2 : "",
															packageDimension3 : "",
															packageDimensionUnit : "",
															packageWeight : "",
															packageWeightUnit : "",
															cartonPackageDimension1 : "",
															cartonPackageDimension2 : "",
															cartonPackageDimension3 : "",
															cartonPackageDimensionUnit : "",
															cartonPackageWeight : "",
															cartonPackageWeightUnit : "",															
															plasticBagWarningLabel: "No",
															disposeOfUnfulfillableInventory: "remove",
															status : $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status']
														});
														
														productLineItemsForMarketingMaterialMarketSide.push({
															SKU : $scope.productLineItems[productLineItem]['SKU'],
															DRSSKU : $scope.productLineItems[productLineItem]['DRSSKU'],
															type1 : $scope.productLineItems[productLineItem]['type1'],
															type1Value : $scope.productLineItems[productLineItem]['type1Value'],
															type2 : $scope.productLineItems[productLineItem]['type2'],
															type2Value : $scope.productLineItems[productLineItem]['type2Value'],
															name : "",
															character : 0,															
															mainImageLineItems : [],
															variationImageLineItems : [],
															otherImageLineItems : [],
															status : $scope.productLineItems[productLineItem]['applicableRegion'][$scope.marketSideRegionList[marketSideRegion]]['status']
														});
																											
													}//end if($scope.productLineItems[productLineItem].applicableRegion.indexOf($scope.marketSideRegionList[marketSideRegion]) != "-1")
											
												}//end if($scope.productLineItems.hasOwnProperty(productLineItem))
												
											}//end for(var productLineItem in $scope.productLineItems)
											
											var jsonDataForMarketSide = {
													country : $scope.marketSideRegionList[marketSideRegion],
													hsCode : "",
													dutyRate : "",
													note : "",
													products : angular.toJson(productLineItemsForMarketSide),	
													productWithVariation : $scope.productWithVariation																	
											};
												
											var jsonDataForMarketingMaterialMarketSide = {
													name : "",
													shortName : "",
													feature1 : "",
													feature2 : "",
													feature3 : "",
													feature4 : "",
													feature5 : "",
													description : "",
													searchTerms1 : "",
													searchTerms2 : "",
													searchTerms3 : "",
													searchTerms4 : "",
													searchTerms5 : "",
													note : "",
													country : $scope.marketSideRegionList[marketSideRegion],
													products : angular.toJson(productLineItemsForMarketingMaterialMarketSide),
													productWithVariation : $scope.productWithVariation
											};
											
											var marketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketSide)																																				
											};
											
											var marketingMaterialMarketSide = {
													supplierKcode : $scope.supplierKcode,
													productBaseCode : $scope.baseProductCode,
													country : $scope.marketSideRegionList[marketSideRegion],
													jsonData : JSON.stringify(jsonDataForMarketingMaterialMarketSide)													
											};	
																					
											productInfoMarketSide.push(marketSide);
											productMarketingMaterialMarketSide.push(marketingMaterialMarketSide);	
											
										}//end if($scope.marketSideRegionList.hasOwnProperty(marketSideRegion))
									
									}//end for(var marketSideRegion in $scope.marketSideRegionList)

									var developingProduct = {
											supplierKcode : $scope.supplierKcode,
											productBaseCode : $scope.baseProductCode,
											productInfoSource : JSON.stringify(productInfoSource),
											productInfoMarketSide : JSON.stringify(productInfoMarketSide),
											productMarketingMaterialSource : JSON.stringify(productMarketingMaterialSource),
											productMarketingMaterialMarketSide : JSON.stringify(productMarketingMaterialMarketSide),
											serialNumber : $scope.applicationSN
										};
									

									if(form.$invalid || $scope.BaseProductCodeExisting
											|| $scope.SKUValueDuplicated || $scope.GTINValueDuplicated || $scope.productLineItems.length == 0){

											$scope.formValidated = false;
											$('#validation_message').show();
											scrollToAnchor('s5_body_padding');

									}else{

											$scope.formValidated = true;
											$("#save,#saveDraft,#submit,#update,#return,#approve").prop('disabled', true);
											$('#validation_message').hide();

										$.ajax({
											url : "${pageContext.request.contextPath}/oa/sd",
											type : 'POST',
											data : JSON.stringify(developingProduct),
											contentType : 'application/json',
											success : function(data) {

												window.location.href = "${pageContext.request.contextPath}"
														+ data;

											},
											error : function(data, status,
													er) {
												console.log("error: "
														+ data
														+ " status: "
														+ status + " er:"
														+ er);
											}
										});

									}

								};

								function scrollToAnchor(divid){
								    var divTag = $("div[id='"+ divid +"']");
								    $('html,body').animate({scrollTop: divTag.offset().top},'slow');
								}

								function unique(list) {
								    var result = [];
								    $.each(list, function(i, e) {
								        if ($.inArray(e, result) == -1) result.push(e);
								    });
								    return result;
								}
								
								//set one row of battery line item
								if(!$scope.batteryLineItems.length) $scope.addBatteryLineItem();
								if(!$scope.referenceFileLineItems.length) $scope.addReferenceInfo();
															
							} ]);
</script>
<style>
     .fileUpload {
         position: relative;
         overflow: hidden;
         margin: 10px;
     }
     .fileUpload input.upload {
         position: absolute;
         top: 0;
         right: 0;
         margin: 0;
         padding: 0;
         font-size: 20px;
         cursor: pointer;
         opacity: 0;
         filter: alpha(opacity=0);
     }
     .validation_message{
     	clear: both;
     	background-color: #f2dede;
     	margin: 15px auto 0 auto;
     	font-size: 20px;
     	display: none;
     }
</style>
</head>
<div id="validation_message_tag" class="row">
	<div class="col-md-12 max-width" >
		<p id ="validation_message" class="validation_message">
			<spring:message code="productInfo.formValidation.msg" />
		</p>
	</div>
</div>
<div class="row">
    <div class="col-md-12 max-width" >
    <ol class="breadcrumb">
        <sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
        	<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a></li>
        	<li class="breadcrumb-item"><span>${baseProductCode} ${productName}</span></li>
        	<li class="breadcrumb-item active"><span>[<spring:message code="detailsOfBaseProduct.edit" />] <spring:message code="detailsOfBaseProduct.productInformation" /></span></li>
        </sec:authorize>
        <sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
        	<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="productVersion.AllBaseProductList" /></a> </li>
	  		<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/NewDevelopingProductList?kcode=${supplierKcode}">${supplierKcode} <spring:message code="productVersion.supplierBaseProductList" /></a></li>
        	<li class="breadcrumb-item"><span>${baseProductCode} ${productName}</span> <div class="bnext"></li>
       	 	<li class="breadcrumb-item active"><span>[<spring:message code="detailsOfBaseProduct.edit" />] <spring:message code="detailsOfBaseProduct.productInformation" /></span></li>
        </sec:authorize>
    </ol>
    </div>
</div>
<div class="max-width" ng-app="developingProduct"
	ng-controller="developingProductCtrl">
		<div class="container-fluid">
		<div class="row">
		<div class="col-md-12">
		</div>
		</div>
		<div class="row">
			<div class="col-md-12">
			
			    <c:choose>
					<c:when test="${type eq 'Create'}">
					<div class="page-heading"><spring:message code="productInfoSourceVersion.productGeneralInformationTitle" />
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
							<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />								   						
						</a>  				
	  				</sec:authorize>					
					</div>											
					</c:when>
					<c:otherwise>											
					<div class="page-heading">${productName}</div>
					<div class="change_product">   
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
							<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />								   						
						</a>  				
	  				</sec:authorize>
	  				<div class="text-right">
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
					<c:choose>
						<c:when test="${type eq 'Create'}">
						<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="detailsOfBaseProduct.backToList" /></a>					
						<input type="button" class="btn btn-primary" ng-click="saveDevelopingProduct(productInfoSourceVersion)"
						value="<spring:message code="productInfoSourceVersion.submit" />" />
						</c:when>
						<c:otherwise>
						<a class="btn btn-success" href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><spring:message code="detailsOfBaseProduct.back" /></a>					
						<input type="button" class="btn btn-primary" ng-click="saveDraftForProductInfoSource(productInfoSourceVersion)"
						value="<spring:message code="detailsOfBaseProduct.saveDraft" />" />
						<input type="button" class="btn btn-warning" ng-click="submitProductInfoSource(productInfoSourceVersion)"
						value="<spring:message code="detailsOfBaseProduct.submit" />" />
						</c:otherwise>
					</c:choose>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
					<a class="btn btn-success" href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><spring:message code="detailsOfBaseProduct.back" /></a>											
					<input id="update" type="button" class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" ng-click="updateProductInfoSource(productInfoSourceVersion)"
						value="<spring:message code="detailsOfBaseProduct.update" />" />
					<button type="button" class="btn btn-default"  style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="returnProductInfoSource(productInfoSourceVersion)" >
					<i class="fas fa-step-backward"></i>  <spring:message code="detailsOfBaseProduct.return" />	
					</button>																			
					<button type="button" class="btn btn-default"  style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="approveProductInfoSource()"
					  ng-disabled= "BaseProductCodeExisting||SKUValueDuplicated||GTINValueDuplicated||productInfoSourceVersion.$invalid||productLineItems.length == 0" >
					<spring:message code="detailsOfBaseProduct.approve" />  <i class="fas fa-step-forward"></i>
					</button>																	
					</sec:authorize>
					</div>												
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</div>	
				
		<div class="row" style="padding:5px;">
		<div class="col-md-12">
		<span  style="margin:3px;background-color:#cfcfcf;font-size:9pt;padding:2px;" class="btn btn-default disabled pull-right"><a href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><img style="width:16px;" src="<c:url value="/resources/images/UN.png"/>"> <spring:message code="detailsOfBaseProduct.productGeneralInformation" /> </a></span>		
		<c:forEach items="${marketSideList}" var="marketSide">
			<span  style="margin:3px;padding:2px;font-size:9pt;" class="btn btn-default pull-right">
			<a href="${pageContext.request.contextPath}/MarketSideProductInformation/${baseProductCode}/${marketSide}?supplierKcode={{supplierKcode}}">
			<img style="width:16px;" src="<c:url value="/resources/images/${marketSide}.png"/>"> ${marketSide} </a></span>
		</c:forEach>			
		</div>
		</div>
		<div class="row mb-2">
		<div class="col-md-12 mt-1">
			<div>
                <ul class="nav justify-content-center" id="process-step" role="tablist">
                    <li class="nav-item current "><a class=" active nav-link" href="#home" aria-controls="home" role="tab" data-toggle="tab"><spring:message code="productInfoSourceVersion.basicInfoTitle" /></a></li>
                    <li class="nav-item"><a class=" nav-link" href="#profile" aria-controls="profile" role="tab" data-toggle="tab" ><spring:message code="productInfoSourceVersion.variationProductTitle" /></a></li>
                    <li class="nav-item"><a class=" nav-link" href="#messages" aria-controls="messages" role="tab" data-toggle="tab" ><spring:message code="productInfoSourceVersion.complianceInfoTitle" /></a></li>
                    <li class="nav-item"><a class=" nav-link" href="#settings" aria-controls="settings" role="tab" data-toggle="tab" ><spring:message code="productInfoSourceVersion.referenceInformation" /></a></li>
                </ul>              
            </div>
		</div>
        </div>		
		<form name="productInfoSourceVersion">
		<div class="tab-content">		
		<div role="tabpanel" class="tab-pane fade show active" id="home">
		<br/>			
		<div class="row rowframe">
						<div class="col-md-8 offset-md-2">
				
				    <div class="form-group row">
						<label class="col-sm-4 col-form-label" for="ApplicationSN"><spring:message code="productInfoSourceVersion.ApplicationSN" /></label>

					<div class="col-sm-8">
					<!-- sndp -->
					<select class="form-control" name="applicationSN" ng-model="applicationSN">
								<option value="">--- Select ---</option>								
								<c:forEach items="${applicationSnList}" var="applicationSn">
									<option value="${applicationSn}">${applicationSn}</option>
								</c:forEach>
						</select>		
					
					</div>
					</div>
					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="supplierKcode"><spring:message code="productInfoSourceVersion.supplier" /><span class="text-danger"> *</span></label>
					<div class="col-sm-8 my-auto">
					<c:choose>
								<c:when test="${isDrsUser eq true}">
								<select class="form-control" name="supplierKcode" ng-model="supplierKcode" ng-change="getDRSBaseProductCode()" ng-disabled="operatorType=='Edit'" required>
									<option value="">--- Select ---</option>
									<c:forEach var="supplier" items="${supplierKcodeToShortEnUsNameMap}">
									<option value="${supplier.key}">${supplier.key} ${supplier.value}</option>
									</c:forEach>
								</select>

								<small class="text-danger form-text" ng-show="productInfoSourceVersion.supplierKcode.$error.required && productInfoSourceVersion.supplierKcode.$dirty">
								<spring:message code="productInfoSourceVersion.supplier_req" />
								</small>
								</c:when>
								<c:otherwise>
								${userCompanyKcode}
								</c:otherwise>
							</c:choose>
					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for=""><spring:message code="productInfoSourceVersion.productNameEn" /><span class="text-danger"> *</span></label>
					<div class="col-sm-8">
					<input
							class="form-control" id="productNameEnglish" ng-model="productNameEnglish" required>

					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="productNameLocal"><spring:message code="productInfoSourceVersion.productNameLocal" /></label>
					<div class="col-sm-8">
					<input
								class="form-control" id="productNameLocal" ng-model="productNameLocal"></td>
					</div>
				</div>
					<div class="form-group row">
					<label class="col-sm-4 col-form-label" data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.proposalProductCategory_hint" />"><spring:message code="productInfoSourceVersion.proposalProductCategory" /> <a target="_blank" href="<spring:message code="productInfoSourceVersion.proposalProductCategory_link" />"><i class="fas fa-info-circle fa-1x"></i></a></label>

					<div class="col-sm-8"><select
								class="form-control" ng-model="proposalProductCategory">
									<option value="">--- Select ---</option>
									<option value="3DPrintedProducts"><spring:message code="ProposalProductCategory.3DPrintedProducts" /></option>
									<option value="amazonDeviceAccessories"><spring:message code="ProposalProductCategory.amazonDeviceAccessories" /></option>
									<option value="amazonKindle"><spring:message code="ProposalProductCategory.amazonKindle" /></option>
									<option value="automotivePowersports"><spring:message code="ProposalProductCategory.automotivePowersports" /></option>
									<option value="babyProducts"><spring:message code="ProposalProductCategory.babyProducts" /></option>
									<option value="beauty"><spring:message code="ProposalProductCategory.beauty" /></option>
									<option value="books"><spring:message code="ProposalProductCategory.books" /></option>
									<option value="cameraPhoto"><spring:message code="ProposalProductCategory.cameraPhoto" /></option>
									<option value="cellPhoneDevices"><spring:message code="ProposalProductCategory.cellPhoneDevices" /></option>
									<option value="clothingAccessories"><spring:message code="ProposalProductCategory.clothingAccessories" /></option>
									<option value="collectibleCoins"><spring:message code="ProposalProductCategory.collectibleCoins" /></option>
									<option value="consumerElectronics"><spring:message code="ProposalProductCategory.consumerElectronics" /></option>
									<option value="electronicsAccessories"><spring:message code="ProposalProductCategory.electronicsAccessories" /></option>
									<option value="entertainmentCollectibles"><spring:message code="ProposalProductCategory.entertainmentCollectibles" /></option>
									<option value="healthPersonalCare"><spring:message code="ProposalProductCategory.healthPersonalCare" /></option>
									<option value="homeGarden"><spring:message code="ProposalProductCategory.homeGarden" /></option>
									<option value="independentDesign"><spring:message code="ProposalProductCategory.independentDesign" /></option>
									<option value="industrialScientific"><spring:message code="ProposalProductCategory.industrialScientific" /></option>
									<option value="jewelry"><spring:message code="ProposalProductCategory.jewelry" /></option>
									<option value="kitchen"><spring:message code="ProposalProductCategory.kitchen" /></option>
									<option value="luggageTravelAccessories"><spring:message code="ProposalProductCategory.luggageTravelAccessories" /></option>
									<option value="majorAppliances"><spring:message code="ProposalProductCategory.majorAppliances" /></option>
									<option value="music"><spring:message code="ProposalProductCategory.music" /></option>
									<option value="musicalInstruments"><spring:message code="ProposalProductCategory.musicalInstruments" /></option>
									<option value="officeProducts"><spring:message code="ProposalProductCategory.officeProducts" /></option>
									<option value="outdoors"><spring:message code="ProposalProductCategory.outdoors" /></option>
									<option value="personalComputers"><spring:message code="ProposalProductCategory.personalComputers" /></option>
									<option value="shoesHandbagsSunglasses"><spring:message code="ProposalProductCategory.shoesHandbagsSunglasses" /></option>
									<option value="softwareComputerVideoGames"><spring:message code="ProposalProductCategory.softwareComputerVideoGames" /></option>
									<option value="sports"><spring:message code="ProposalProductCategory.sports" /></option>
									<option value="sportsCollectibles"><spring:message code="ProposalProductCategory.sportsCollectibles" /></option>
									<option value="toolsHomeImprovement"><spring:message code="ProposalProductCategory.toolsHomeImprovement" /></option>
									<option value="toysGames"><spring:message code="ProposalProductCategory.toysGames" /></option>
									<option value="videoDVD"><spring:message code="ProposalProductCategory.videoDVD" /></option>
									<option value="videoGames"><spring:message code="ProposalProductCategory.videoGames" /></option>
									<option value="videoGameConsoles"><spring:message code="ProposalProductCategory.videoGameConsoles" /></option>
									<option value="watches"><spring:message code="ProposalProductCategory.watches" /></option>								
							</select>
					</div>
				</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="brandEnglish"><spring:message code="productInfoSourceVersion.brandEnglish" /></label>
					<div class="col-sm-8">
					<input id="brandEnglish" class="form-control" ng-model="brandEng">
					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="brandLocal"><spring:message code="productInfoSourceVersion.brandlocal" /></label>
					<div class="col-sm-8">
					<input class="form-control" ng-model="brand">
					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="supplierBaseProductCode" data-toggle="tooltip" data-placement="top" data-html="true" title="<spring:message code="productInfoSourceVersion.supplierBaseProductCode_hint"/>"><spring:message code="productInfoSourceVersion.supplierBaseProductCode" /><span class="text-danger"> *</span></label>
					<div class="col-sm-8">
					<input class="form-control" name="supplierBaseProductCode" ng-model="supplierBaseProductCode" ng-pattern="/^[a-zA-Z0-9\-]+$/" ng-change="isBaseProductCodeExist()" ng-disabled="operatorType=='Edit'" required>
					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="baseProductCode"><spring:message code="productInfoSourceVersion.baseProductCode" /> <span class="text-danger">*</span></label>
					<div class="col-sm-8">
					<input ng-model="baseProductCode" name="baseProductCode" class="form-control" readonly="true">


							<small class="text-danger form-text" ng-show="productInfoSourceVersion.supplierBaseProductCode.$error.required && productInfoSourceVersion.supplierBaseProductCode.$dirty">
								<spring:message code="productInfoSourceVersion.baseProductCode_req" />
							</small>
							<small class="text-danger form-text" ng-show="productInfoSourceVersion.supplierBaseProductCode.$error.pattern">
								<spring:message code="productInfoSourceVersion.baseProductCode_format" />
							</small>
							<small class="text-danger form-text" ng-show="BaseProductCodeExisting">
								<spring:message code="productInfoSourceVersion.baseProductCode_duplicated" />
							</small>
					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for="mlt" data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.manufacturerLeadTime_hint" />"><spring:message code="productInfoSourceVersion.manufacturerLeadTime" /></label>
					<div class="col-sm-8">
					<input ng-model="manufacturerLeadTime" class="form-control" name="manufacturerLeadTime" ng-pattern="/^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$|^999$/" >
							<small class="text-danger form-text" ng-show="productInfoSourceVersion.manufacturerLeadTime.$error.pattern">
								<spring:message code="productInfoSourceVersion.MLT_format" /></small>
					</div>
					</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for=""><spring:message code="productInfoSourceVersion.allergy" /></label><br>
					<div class="col-sm-8 my-auto">
					<div class="form-check form-check-inline">
								<input class="form-check-input" id="allergyNo"
								type="radio" name="allergy" value="no"
								ng-checked="allergy==false" ng-click="checkAllergy()"><label class="form-check-label" for="allergyNo"><spring:message code="productInfoSourceVersion.allergyNo" /> </label>
								</div>
					<div class="form-check form-check-inline">
								<input class="form-check-input" id="allergyYes" type="radio" name="allergy"
								value="yes" ng-checked="allergy==true && allergyName =='latex'"
								ng-click="checkAllergy()"><label class="form-check-label" for="allergyYes"> <spring:message code="productInfoSourceVersion.allergyYes" /> </label>
								</div>
					<div class="form-check form-check-inline">
								<input
								id="allergyYesOther" class="form-check-input" type="radio" name="allergy" value="yes"
								ng-checked="allergy==true && allergyName !='latex'"
								ng-click="checkAllergy()">
								<label class="form-check-label" for="allergyYesOther"><spring:message code="productInfoSourceVersion.allergyYesOther" /> </label>
								</div>
					</div>
					</div>

					<div class="form-group row">

								 <label class="col-sm-4" for="allergyOther"><spring:message code="productInfoSourceVersion.allergyYesOtherName" /></label>
								 <div class="col-sm-8"> <input
								id="allergyOther" class="form-control"
								ng-model="allergyOther"
								ng-disabled="allergy ==false || (allergy==true && allergyName =='latex')"
								ng-change="changeAllergyName()">
								</div>
								</div>

					<div class="form-group row">
					<label class="col-sm-4 col-form-label" for=""><spring:message code="productInfoSourceVersion.materialAndPercentage" /></label>
					<div class="col-sm-8" ng-repeat="materialLineItem in materialLineItems">
									<div class="row">
										<div class="col-sm-12">
											<input class="form-control" style="display: inline;"
												ng-model="materialLineItem.material">
											
										</div>
									</div>
								</div>
					</div>

					<div class="form-group row" ng-repeat="referenceLinkLineItem in referenceLinkLineItems">
					<label class="col-sm-4 col-form-label" data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.productReferenceLink_hint" />"><spring:message code="productInfoSourceVersion.productReferenceLink" /></label>
					<div class="col" >
											<input class="form-control"
												ng-model="referenceLinkLineItem.referenceLink">
												</div>
											<div class="col-sm-1">
											<button type="button" class="btn btn-default mt-1 mt-md-0" ng-show="$index > 0"
												ng-click="removeReferenceLinkLineItem(referenceLinkLineItem)">
												<i class="fas fa-minus"></i>
											</button>
											<button type="button" class="btn btn-default mt-1 mt-md-0" ng-hide="$index > 0"
											ng-click="addReferenceLinkLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										</div>
										</div>
										</div>
		
				
			</div><!--  end of rowframe -->
			</div><!-- end of tabpanel -->						
			<div role="tabpanel" class="tab-pane fade" id="profile">
			<div class="row rowframe">
			<div id="variationTypes" class="col-md-12 m-3">
				<div class="form-inline justify-content-center">
				 <div class="form-check form-check-inline">
							<input
								id="withVariation" type="radio" class="form-check-input"
							 name="variationTypes"
								value="Yes" ng-click="checkVariationTypes()" ng-disabled="productLineItems.length > 0" ng-checked="productWithVariation=='1'">
							<label class="form-check-label" for="withVariation"><spring:message code="productInfoSourceVersion.variationTypes" /></label>
							</div>
							<select id="type1" class="form-control mx-1" ng-model="type1" ng-change="checkVariationTypeDuplicated()" ng-disabled="productLineItems.length > 0"
								>
									<option value="color"><spring:message code="VariationType.color" /></option>
									<option value="sizeCapacity"><spring:message code="VariationType.sizeCapacity" /></option>
									<option value="packageQuantity"><spring:message code="VariationType.packageQuantity" /></option>
									<option value="material"><spring:message code="VariationType.material" /></option>
									<option value="regionalSpecification"><spring:message code="VariationType.regionalSpecification" /></option>
							</select> <select id="type2" class="form-control mx-1" ng-model="type2" ng-change="checkVariationTypeDuplicated()" ng-disabled="productLineItems.length > 0"
								>
									<option value="">--- Select ---</option>
									<option value="color"><spring:message code="VariationType.color" /></option>
									<option value="sizeCapacity"><spring:message code="VariationType.sizeCapacity" /></option>
									<option value="packageQuantity"><spring:message code="VariationType.packageQuantity" /></option>
									<option value="regionalSpecification"><spring:message code="VariationType.regionalSpecification" /></option>
							</select>

							<div class="form-check form-check-inline">
							<input id="noVariation"
								type="radio" class="form-check-input"
								 name="variationTypes"
								value="No" ng-click="checkVariationTypes()" ng-disabled="productLineItems.length > 0" ng-checked="productWithVariation=='0'">
							<label for="noVariation" class="form-check-label"><spring:message code="productInfoSourceVersion.noVariation" /></label>
							</div>

							<c:choose>
								<c:when test="${type eq 'Create'}"> 
								 <button id="createWithVariation" type="button"
									class="btn btn-primary ml-1" ng-click="createProduct(withVariation)" ng-disabled="productLineItems.length > 0 || variationTypeDuplicated"><spring:message code="productInfoSourceVersion.createVariationProduct" /></button>								 
								 <button id="reset" type="button"
									class="btn btn-default ml-1" ng-click="resetProduct()"><spring:message code="productInfoSourceVersion.resetVariationProduct" /></button>	
								</c:when>
								</c:choose>

							<small class="text-danger form-text" ng-show="variationTypeDuplicated">
									<spring:message code="productInfoSourceVersion.variationType_duplicated" />
							</small>
			</div>
			<div id="productSection" class="row"
				ng-if="productLineItems.length > 0">
				<div class="col-md-12">

			<div class="form-row my-3" ng-repeat="productLineItem in productLineItems" >
				<div class= "col" ng-if="productWithVariation=='1'&&type1!=''">
					<label for="" ng-if="productWithVariation=='1'&&type1!=''">{{type1Column}}<span class="text-danger">*</span></label>
					<input class="form-control" name="type1Value{{$id}}" ng-model="productLineItem.type1Value" ng-disabled="productLineItem.status=='edit' && formValidated == true" required>
					<small class="text-danger form-text" ng-show="productInfoSourceVersion.type1Value{{$id}}.$error.required && productInfoSourceVersion.type1Value{{$id}}.$dirty || (productLineItem.type1Value == '' && formValidated == false)">
								<spring:message code="productInfoSourceVersion.variationType_req" />
					</small>
					</div>
				<div class="col" ng-if="productWithVariation=='1'&&type2!=''">
					<label for="" ng-if="productWithVariation=='1'&&type2!=''">{{type2Column}}<span class="text-danger">*</span></label>
					<input class="form-control" name="type2Value{{$id}}" ng-model="productLineItem.type2Value" ng-disabled="productLineItem.status=='edit' && formValidated == true" required>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.type2Value{{$id}}.$error.required && productInfoSourceVersion.type2Value{{$id}}.$dirty || (productLineItem.type2Value == '' && formValidated == false)">
								<spring:message code="productInfoSourceVersion.variationType_req" />
								</small>
					</div>
				<dic class="col">
					<label for=""><span data-toggle="tooltip" data-placement="top" data-html="true" title="<spring:message code="productInfoSourceVersion.SKUcode_hint" />"><spring:message code="productInfoSourceVersion.SKUcode" /><span class="text-danger">*</span></span></label>
					<input class="form-control" name="SKU{{$id}}" ng-model="productLineItem.SKU" ng-change="checkSKUValueDuplicated(productLineItem)" ng-disabled="productLineItem.status=='edit' && formValidated == true" ng-pattern="/^[a-zA-Z0-9\-]+$/" required>
					<small class="text-danger form-text" ng-show="productInfoSourceVersion.SKU{{$id}}.$error.required && productInfoSourceVersion.SKU{{$id}}.$dirty || (productLineItem.SKU == '' && formValidated == false)">
									<spring:message code="productInfoSourceVersion.sku_req" />									
								</small>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.SKU{{$id}}.$error.pattern">
									<spring:message code="productInfoSourceVersion.sku_format" />
								</small>
					</dic>
				<div class="col-md-3">
					<label for=""><span data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.GTIN_hint" />"><spring:message code="productInfoSourceVersion.GTIN" /></span></label>
					<div class="form-inline">
					<c:choose>
								<c:when test="${type eq 'Create'}">
								<input
									class="form-control mr-1" name="GTINValue{{$id}}"
									ng-model="productLineItem.GTINValue" ng-change="checkGTINValueDuplicated()" ng-pattern="/^[0-9\X]+$/" ng-minlength="productLineItem.GTINTypeLength" ng-maxlength="productLineItem.GTINTypeLength"> <select
									class="form-control"
									ng-model="productLineItem.GTINType" ng-change="setGTINTypeLength(productLineItem.GTINType,productLineItem)">
										<option value="EAN"><spring:message code="GTIN.EAN" /></option>
										<option value="UPC"><spring:message code="GTIN.UPC" /></option>
								</select>								
								<sec:authorize access="hasAnyRole('${auth['GTIN_PROVIDER_DRS']}')">
								<span class="form-check form-check-inline">
								<input type="checkbox" class="form-control" name="GTINProvider" ng-model="productLineItem.providedByDRS" value="true"  id="SKU{{$id}}_GTINProvider"> <spring:message code="GTIN.providerDRS" />
								<label for="SKU{{$id}}_GTINProvider" > <spring:message code="GTIN.providerDRS" /></label>
								</span>	
								</sec:authorize>
								</c:when>
								<c:otherwise>
								<sec:authorize access="hasAnyRole('${auth['GTIN_PROVIDER_DRS']}')">
								<input
									class="form-control mr-1" name="GTINValue{{$id}}"
									ng-model="productLineItem.GTINValue" ng-change="checkGTINValueDuplicated()" ng-pattern="/^[0-9\X]+$/" ng-minlength="productLineItem.GTINTypeLength" ng-maxlength="productLineItem.GTINTypeLength"> <select
									class="form-control"
									ng-model="productLineItem.GTINType" ng-change="setGTINTypeLength(productLineItem.GTINType,productLineItem)">
										<option value="EAN"><spring:message code="GTIN.EAN" /></option>
										<option value="UPC"><spring:message code="GTIN.UPC" /></option>
								</select>
								<span class="form-check form-check-inline">
								<input type="checkbox" class="form-control" name="GTINProvider" ng-model="productLineItem.providedByDRS" value="true"  id="SKU{{$id}}_GTINProvider">
								<label for="SKU{{$id}}_GTINProvider" > <spring:message code="GTIN.providerDRS" /></label>
								</span>	
								</sec:authorize>								
								<sec:authorize access="hasAnyRole('${auth['GTIN_PROVIDER_SUPPLIER']}')">
								<span ng-if="productLineItem.providedByDRS == true">
								<spring:message code="GTIN.providerSupplier_view" />															
								</span>
								<span ng-if="productLineItem.providedByDRS == false">
								<input
									class="form-control mr-1" name="GTINValue{{$id}}"
									ng-model="productLineItem.GTINValue" ng-change="checkGTINValueDuplicated()" ng-pattern="/^[0-9\X]+$/" ng-minlength="productLineItem.GTINTypeLength" ng-maxlength="productLineItem.GTINTypeLength"> <select
									class="form-control"
									ng-model="productLineItem.GTINType" ng-change="setGTINTypeLength(productLineItem.GTINType,productLineItem)">
										<option value="EAN"><spring:message code="GTIN.EAN" /></option>
										<option value="UPC"><spring:message code="GTIN.UPC" /></option>
								</select>															
								</span>								
								</sec:authorize>
								</c:otherwise>
								</c:choose>
							</div>
								<div class="text-danger"
									ng-show="productInfoSourceVersion.GTINValue{{$id}}.$error.maxlength || productInfoSourceVersion.GTINValue{{$id}}.$error.minlength">
									{{productLineItem.GTINType}} has to be {{productLineItem.GTINTypeLength}} digits.
								</div>
								<div class="text-danger"
									ng-show="productInfoSourceVersion.GTINValue{{$id}}.$error.pattern">
									{{productLineItem.GTINType}} has to be numbers.
								</div>

					</div>

				<div class="col-md-3">
					<label for=""><span data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.FCAPrice_hint" />"><spring:message code="productInfoSourceVersion.FCAPrice" /></span> <a target="_blank" href="<spring:message code="Joomla_root_link" />/fee/drs-cashflow-glossary#FCAprice"><i class="fas fa-info-circle"></i></a></label>
					<input
									class="form-control" name="FCAPrice{{$id}}"
									ng-model="productLineItem.FCAPrice" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/">TWD
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.FCAPrice{{$id}}.$error.pattern">
									<spring:message code="productInfoSourceVersion.FCAPrice_format" />
					</small>

				</div>
				<div class="col-md-3">
					<label for=""><spring:message code="productInfoSourceVersion.applicableRegion" /></label>
				<div class="form-inline">
					<div class="form-check form-check-inline">
								 <input class="form-check-input" id="{{$id}}CA" type="checkbox" name="applicableRegion[]" value="CA" ng-checked="productLineItem.applicableRegion.CA.applicable" ng-click="setApplicable($id,'CA',productLineItem,productLineItem.applicableRegionList)">
								 <label class="form-check-label" for="{{$id}}CA" >CA</label>
								</div>
								<div class="form-check form-check-inline">
								 <input class="form-check-input" id="{{$id}}DE" type="checkbox" name="applicableRegion[]" value="DE" ng-checked="productLineItem.applicableRegion.DE.applicable" ng-click="setApplicable($id,'DE',productLineItem,productLineItem.applicableRegionList)">
								 <label class="form-check-label" for="{{$id}}DE" >DE</label>
								</div>
								 <div class="form-check form-check-inline">
								 <input  class="form-check-input" id="{{$id}}ES" type="checkbox" name="applicableRegion[]" value="ES" ng-checked="productLineItem.applicableRegion.ES.applicable" ng-click="setApplicable($id,'ES',productLineItem,productLineItem.applicableRegionList)">
								<label class="form-check-label" for="{{$id}}ES" >ES</label>
								 </div>
								 <div class="form-check form-check-inline" >
								 <input class="form-check-input" id="{{$id}}FR" type="checkbox" name="applicableRegion[]" value="FR" ng-checked="productLineItem.applicableRegion.FR.applicable" ng-click="setApplicable($id,'FR',productLineItem,productLineItem.applicableRegionList)">
								<label class="form-check-label" for="{{$id}}FR" >FR</label>
								 </div>
								<div class="form-check form-check-inline ">
								 <input class="form-check-input" id="{{$id}}IT" type="checkbox" name="applicableRegion[]" value="IT" ng-checked="productLineItem.applicableRegion.IT.applicable" ng-click="setApplicable($id,'IT',productLineItem,productLineItem.applicableRegionList)">
								 <label class="form-check-label" for="{{$id}}IT" >IT</label>
								</div>
								 <div class="form-check form-check-inline">
								 <input class="form-check-input" id="{{$id}}UK" type="checkbox" name="applicableRegion[]" value="UK" ng-checked="productLineItem.applicableRegion.UK.applicable" ng-click="setApplicable($id,'UK',productLineItem,productLineItem.applicableRegionList)">
								 <label class="form-check-label" for="{{$id}}UK" >UK</label>
								 </div>
								 <div class="form-check form-check-inline">
								 <input class="form-check-input" id="{{$id}}US" type="checkbox" name="applicableRegion[]" value="US" ng-checked="productLineItem.applicableRegion.US.applicable" ng-click="setApplicable($id,'US',productLineItem,productLineItem.applicableRegionList)">
								 <label class="form-check-label" for="{{$id}}US" >US</label>
				</div>							</div>
					</div>

				<div class="col form-inline" ng-if="productWithVariation=='1'">

					<button type="button" class="btn btn-default mr-1"
										ng-click="addProductLineItem()">
										<i class="fas fa-plus"></i></span>
									</button>

									<button type="button" class="btn btn-default"
										ng-click="removeProductLineItem(productLineItem)">
										<i class="fas fa-minus"></i></span>
									</button>
					</div>

				</div>
				<hr>

				<div ng-if="productWithVariation=='1'&& type2!=''">
								<small class="text-danger form-text" ng-show="SKUValueDuplicated">
									<spring:message code="productInfoSourceVersion.SKU_duplicated" />
								</small>
								<small class="text-danger form-text" ng-show="GTINValueDuplicated">
									<spring:message code="productInfoSourceVersion.GTIN_duplicated" />
								</small>
							</div>
							<div ng-if="productWithVariation=='1'&& type2==''">
								<small class="text-danger form-text" ng-show="SKUValueDuplicated">
									<spring:message code="productInfoSourceVersion.SKU_duplicated" />
								</small>
								<small class="text-danger form-text" ng-show="GTINValueDuplicated">
									<spring:message code="productInfoSourceVersion.GTIN_duplicated" />
								</small>
							</div>
				</div>

			</div>

<!-- SKU Dimensions -->
			<div class="row" id="sizeSection" class="row"
				ng-if="productLineItems.length > 0">
				<div class="col-md-12">
				<div class="form-row" style="background:#ddd;">

				<div class="col-md-2">
				<label for=""><spring:message code="productInfoSourceVersion.variationProduct" /></label>
				</div>

				<div class="col-md-4">
				<label for=""><span data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.productDimension_hint" />"><spring:message code="productInfoSourceVersion.productDimension" /></span></label>
				<div class="form-inline">
				<input
									id="packageDimension1ToApply" name="packageDimension1ToApply" ng-model="packageDimension1ToApply" class="form-control mx-1"
									style="width:15%" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									id="packageDimension2ToApply" name="packageDimension2ToApply" ng-model="packageDimension2ToApply" class="form-control mx-1"
									style="width:15%"  ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									id="packageDimension3ToApply" name="packageDimension3ToApply" ng-model="packageDimension3ToApply" class="form-control mx-1"
									style="width:15%"  ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> <select
									id="packageDimensionUnitToApply" class="form-control"
									>
										<option value="">--- Select ---</option>
										<option value="cm"><spring:message code="Dimension.cm" /></option>
										<option value="m"><spring:message code="Dimension.m" /></option>
										<option value="in"><spring:message code="Dimension.in" /></option>
										<option value="ft"><spring:message code="Dimension.ft" /></option>
								</select>
								</div>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.packageDimension1ToApply.$error.pattern || productInfoSourceVersion.packageDimension2ToApply.$error.pattern || productInfoSourceVersion.packageDimension3ToApply.$error.pattern">
								<spring:message code="productInfoSourceVersion.productDimension_format" />
								</small>
				</div>

				<div class="col-md-4">
				<label for=""><span data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.productWeight_hint" />"><spring:message code="productInfoSourceVersion.productWeight" /></span></label>
				<div class="form-inline">
				<input
									id="packageWeightToApply" name="packageWeightToApply" ng-model="packageWeightToApply" class="form-control mr-1"
									style="width:30%" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> <select
									id="packageWeightUnitToApply" class="form-control" style="width:30%"
									>
										<option value="">--- Select ---</option>
										<option value="g"><spring:message code="Weight.g" /></option>
										<option value="kg"><spring:message code="Weight.kg" /></option>
										<option value="oz"><spring:message code="Weight.oz" /></option>
										<option value="lb"><spring:message code="Weight.lb" /></option>
								</select>
								<div class="text-danger" ng-show="productInfoSourceVersion.packageWeightToApply.$error.pattern">
								<spring:message code="productInfoSourceVersion.productWeight_format" />
								</div>
								<button type="button" class="btn btn-default ml-3"
										ng-click="applyProductSizeToAll()" ng-disabled="productInfoSourceVersion.packageDimension1ToApply.$error.pattern || productInfoSourceVersion.packageDimension2ToApply.$error.pattern || productInfoSourceVersion.packageDimension3ToApply.$error.pattern || productInfoSourceVersion.packageWeightToApply.$error.pattern">
										<span><spring:message code="productInfoSourceVersion.applyToAll" /></span>
									</button>
							</div>
				</div>

				<div class="col-md-2">


				</div>
				</div>


				<div class="form-row" ng-repeat="productLineItem in productLineItems">

				<div class="col-md-2">
				<label for=""></label>
				<div class="my-auto">
				{{supplierKcode}}-{{productLineItem.SKU}}<span
									ng-if="productWithVariation=='1'"> ({{productLineItem.type1Value}}<span
										ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)
								</span>
				</div>

				</div>

				<div class="col-md-4">
				<label for=""></label>
				<div class="form-inline">
					<input
									class="form-control mx-1" name="packageDimension1{{$id}}" style="width: 15%;"
									ng-model="productLineItem.packageDimension1" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									class="form-control mx-1" name="packageDimension2{{$id}}" style="width: 15%;"
									ng-model="productLineItem.packageDimension2" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> X <input
									class="form-control mx-1" name="packageDimension3{{$id}}" style="width: 15%;"
									ng-model="productLineItem.packageDimension3" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> <select
									class="form-control"
									ng-model="productLineItem.packageDimensionUnit">
										<option value="">--- Select ---</option>
										<option value="cm"><spring:message code="Dimension.cm" /></option>
										<option value="m"><spring:message code="Dimension.m" /></option>
										<option value="in"><spring:message code="Dimension.in" /></option>
										<option value="ft"><spring:message code="Dimension.ft" /></option>
								</select>
								</div>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.packageDimension1{{$id}}.$error.pattern || productInfoSourceVersion.packageDimension2{{$id}}.$error.pattern || productInfoSourceVersion.packageDimension3{{$id}}.$error.pattern">
								<spring:message code="productInfoSourceVersion.productDimension_format" />
								</small>
				</div>

				<div class="col-md-4">
				<label for=""></label>
				<div class="form-inline">
					<input
									class="form-control mr-1" name="packageWeight{{$id}}" style="width: 30%; display: inline;"
									ng-model="productLineItem.packageWeight" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"> <select
									class="form-control" style="width: 30%; display: inline;"
									ng-model="productLineItem.packageWeightUnit">
										<option value="">--- Select ---</option>
										<option value="g"><spring:message code="Weight.g" /></option>
										<option value="kg"><spring:message code="Weight.kg" /></option>
										<option value="oz"><spring:message code="Weight.oz" /></option>
										<option value="lb"><spring:message code="Weight.lb" /></option>
								</select>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.packageWeight{{$id}}.$error.pattern">
								<spring:message code="productInfoSourceVersion.productWeight_format" />
								</small>
				</div>
				</div>

				<div class="col-md-2">

				</div>

				</div>
			</div>
			</div>
			</div>
			</div><!-- end of rwoframe -->
			</div><!-- end of tabpanel -->			
			<div role="tabpanel" class="tab-pane fade" id="messages">
   		<br/>
        <div class="row rowframe">        		
				<div class="col-md-8 offset-md-2">

				<div class="form-group row">
				<label class="col-sm-4" for="originalPlace"> <spring:message code="productInfoSourceVersion.originalPlace" /> </label>
				<div class="col-md-8">
				<select
								class="form-control" ng-model="originalPlace">
									<option value="">--- Select ---</option>
									<option value="Taiwan"><spring:message code="OriginalPlace.Taiwan" /></option>
									<option value="China"><spring:message code="OriginalPlace.China" /></option>
									<option value="US"><spring:message code="OriginalPlace.US" /></option>
									<option value="UK"><spring:message code="OriginalPlace.UK" /></option>
							</select>
				</div>
			</div>
				<div class="form-group row">
				<label class="col-sm-4" for="medicalDevice"><spring:message code="productInfoSourceVersion.medicalDevice" /></label>
				</br>
				<div class="col-md-8">
				<div class="form-check form-check-inline">
							<input id="medicalYes"
								type="radio" class="form-check-input" name="medical" ng-checked="medical==true"
								ng-click="checkMedical()" value="medicalDeviceYes"> 
								<label class="form-check-label" for="medicalYes"><spring:message code="productInfoSourceVersion.medicalDeviceYes" /></label>
								</div>


								<div class="form-check form-check-inline">
								 <input
								id="medicalNo" class="form-check-input" type="radio" name="medical"
								ng-checked="medical==false" ng-click="checkMedical()" value="medicalDeviceNo">
								<label class="form-check-label" for="medicalNo"><spring:message code="productInfoSourceVersion.medicalDeviceNo" /></label>
								</div>
							</div>
				</div>

				<div class="form-group row">
				<label class="col-sm-4" ><spring:message code="productInfoSourceVersion.wirelessTechnology" /></label> </br>
				<div class="col-md-8">
				<select id="wirelessTech"
								class="form-control" ng-model="wirelessTech" multiple="multiple">
								    <option value="None"><spring:message code="WirelessTechnology.None" /></option>						
									<option value="bluetooth"><spring:message code="WirelessTechnology.bluetooth" /></option>
									<option value="Wifi"><spring:message code="WirelessTechnology.Wifi" /></option>
									<option value="RF"><spring:message code="WirelessTechnology.RF" /></option>
									<option value="NFC"><spring:message code="WirelessTechnology.NFC" /></option>
									<option value="Ultrasonic"><spring:message code="WirelessTechnology.Ultrasonic" /></option>
									<option value="FIRIR"><spring:message code="WirelessTechnology.FIRIR" /></option>
							</select>
						</div>
				</div>
				<div class="form-group row">
				<label class="col-sm-4"><spring:message code="productInfoSourceVersion.otherWirelessTechnology" /></label>
				<div class="col-md-8">
					<div ng-repeat="otherWirelessTechLineItem in otherWirelessTechLineItems"
									style="padding-bottom: 10px">
									<div class="row">
										<div class="col-sm-11">
											<input id="otherWirelessTech{{$index}}" class="form-control"
												ng-model="otherWirelessTechLineItem.otherWirelessTech">
										</div>
										<div class="col-sm-1">
											<button type="button" class="btn btn-default mt-1 mt-md-0" ng-show="$index > 0"
												ng-click="removeOtherWirelessTechLineItem(otherWirelessTechLineItem)">
												<i class="fas fa-minus"></i>
											</button>
											<button type="button" class="btn btn-default mt-1 mt-md-0" ng-hide="$index > 0"
											ng-click="addOtherWirelessTechLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										</div>
									</div>
								</div>
							</div>
				</div>
				<div class="form-group row">
				<label class="col-sm-4" data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.hSCodeForSupplySideCountry_hint" />"><spring:message code="productInfoSourceVersion.hSCodeForSupplySideCountry" /> <a target="_blank" href="https://fbfh.trade.gov.tw/rich/text/indexfh.asp"><i class="fas fa-info-circle"></i></a></label>

				<div class="col-md-8">
				<input class="form-control" ng-model="hsCode">
				</div>
			</div>

				<div class="form-group row">
				<label class="col-sm-4"><spring:message code="productInfoSourceVersion.documentationForSupplierSideCountry" /></label><br>
				<div class="col-md-8">
				<select id="documentationForSupplierSideCountry"
								class="form-control" ng-model="documentationForSupplierSideCountry" multiple="multiple">
									<option value="none"><spring:message code="DocumentationForSupplierSideCountry.none" /></option>
									<option value="unsure"><spring:message code="DocumentationForSupplierSideCountry.unsure" /></option>
									<option value="C02Required"><spring:message code="DocumentationForSupplierSideCountry.C02Required" /></option>
									<option value="C02NotRequired"><spring:message code="DocumentationForSupplierSideCountry.C02NotRequired" /></option>
									<option value="504"><spring:message code="DocumentationForSupplierSideCountry.504" /></option>
									<option value="602"><spring:message code="DocumentationForSupplierSideCountry.602" /></option>
									<option value="mp1"><spring:message code="DocumentationForSupplierSideCountry.mp1" /></option>	
									<option value="mw0"><spring:message code="DocumentationForSupplierSideCountry.mw0" /></option>									
									<option value="other"><spring:message code="DocumentationForSupplierSideCountry.other" /></option>
							</select>
						</div>
				</div>

				<div class="form-group row">
				<label class="col-sm-4"><spring:message code="productInfoSourceVersion.containMaterial" /></label><br>
				<div class="col-md-8">
				<select id="containedMaterial"
								class="form-control width-600" ng-model="containedMaterial" multiple="multiple" ng-change="checkContainedMaterial()">
									<option value="none"><spring:message code="ContainedMaterial.none" /></option>									
									<option value="powder"><spring:message code="ContainedMaterial.powder" /></option>
									<option value="liquid"><spring:message code="ContainedMaterial.liquid" /></option>
									<option value="gel"><spring:message code="ContainedMaterial.gel" /></option>
									<option value="battery"><spring:message code="ContainedMaterial.battery" /></option>
									<option value="gas"><spring:message code="ContainedMaterial.gas" /></option>
							</select>
						</div>
				</div>
				<div class="form-group row">
				<label class="col-sm-4"> <spring:message code="productInfoSourceVersion.hazardousMaterials" /> </label>
				<div class="col-md-8">
				<div ng-repeat="hazardousMaterialLineItem in hazardousMaterialLineItems"
									style="padding-bottom: 10px">
									<div class="row">
										<div class="col-sm-11">
											<input class="form-control"
												ng-model="hazardousMaterialLineItem.hazardousMaterial">
										</div>
										<div class="col-sm-1">
											<button type="button" class="btn btn-default mt-1 mt-md-0" ng-show="$index > 0"
												ng-click="removeHazardousMaterialLineItem(hazardousMaterialLineItem)">
												<i class="fas fa-minus"></i>
											</button>
											<button type="button" class="btn btn-default mt-1 mt-md-0" ng-hide="$index > 0"
											ng-click="addHazardousMaterialLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										</div>
										</div>
									</div>
								</div>
							</div>
				</div>

			</div><!-- end of rowframe -->
			</div><!-- end of tabpanel -->			
			<div role="tabpanel" class="tab-pane fade" id="settings">
			<br/>
			<div class="row rowframe">
			<div class="row" ng-hide="containedMaterial.indexOf('battery') == -1 || containedMaterial.indexOf('battery') == null" >
				<div class="col-md-6">
					<div class="page-heading"><spring:message code="productInfoSourceVersion.batteryInfoTitle" /></div>
					<span class="text-danger"><spring:message code="productVersion.fileNaming" /></span>
				</div>				

			<div class="col-md-12">
			<div class="form-row mb-3" ng-repeat="batteryLineItem in batteryLineItems" >
			<div class="col-1-md text-center">
			<label><spring:message code="productInfoSourceVersion.batteryNo" /></label>
			<div class="form-group">
			{{$index+1}}
			</div>
			</div>
			<div class="col-2-md">
			<label><span data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.batteryType_hint" />"><spring:message code="productInfoSourceVersion.batteryType" /></span><span ng-if="batteryRequired" class="text-danger">*</span></label>
			<select
									class="form-control" name="batteryType{{$id}}" ng-model="batteryLineItem.batteryType" ng-change="isPackingWayRequired(this)" ng-required="batteryRequired">
										<option value="12V"><spring:message code="BatteryType.12V" /></option>
										<option value="9V"><spring:message code="BatteryType.9V" /></option>
										<option value="A"><spring:message code="BatteryType.A" /></option>
										<option value="AA"><spring:message code="BatteryType.AA" /></option>
										<option value="AAA"><spring:message code="BatteryType.AAA" /></option>
										<option value="AAAA"><spring:message code="BatteryType.AAAA" /></option>
										<option value="C"><spring:message code="BatteryType.C" /></option>
										<option value="CR123A"><spring:message code="BatteryType.CR123A" /></option>
										<option value="CR2"><spring:message code="BatteryType.CR2" /></option>
										<option value="CR5"><spring:message code="BatteryType.CR5" /></option>
										<option value="D"><spring:message code="BatteryType.D" /></option>
										<option value="LithiumIon"><spring:message code="BatteryType.LithiumIon" /></option>
										<option value="LithiumMetal"><spring:message code="BatteryType.LithiumMetal" /></option>
										<option value="nimh"><spring:message code="BatteryType.nimh" /></option>										
										<option value="P76"><spring:message code="BatteryType.P76" /></option>
										<option value="productSpecific"><spring:message code="BatteryType.productSpecific" /></option>
								</select>
			<small class="text-danger form-text" ng-show="productInfoSourceVersion.batteryType{{$id}}.$error.required && productInfoSourceVersion.batteryType{{$id}}.$dirty || (batteryRequired == true  && batteryLineItem.batteryType == '' && formValidated == false)">
									<spring:message code="productInfoSourceVersion.batteryType_req" />
								</small>
			</div>
			<div class="col-1-md text-center">
			<label><spring:message code="productInfoSourceVersion.rechargeable" /><span ng-if="batteryRequired" class="text-danger">*</span></label>
			<div class="form-check">
			<input type="checkbox"
									class="form-check-input" name="rechargeable" value="true" id="rechargable_{{$index+1}}"
									ng-model="batteryLineItem.rechargeable">
								<label class="form-check-label" for="rechargable_{{$index+1}}"></label>
			</div>
			</div>
			<div class="col-2-md">
			<label><spring:message code="productInfoSourceVersion.batterySpecification" /><span ng-if="batteryRequired" class="text-danger">*</span></label>
			<input
									class="form-control" name="cellsNumber{{$id}}" ng-model="batteryLineItem.cellsNumber" ng-pattern="/^[1-9][0-9]*$/" placeholder="<spring:message code='productInfoSourceVersion.cellsNumber' />" ng-required="batteryRequired"> [Cells]
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.cellsNumber{{$id}}.$error.required && productInfoSourceVersion.cellsNumber{{$id}}.$dirty || (batteryRequired == true  && batteryLineItem.cellsNumber == '' && formValidated == false)">
									<spring:message code="productInfoSourceVersion.cellsNumber_req" />
								</small>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.cellsNumber{{$id}}.$error.pattern">
									<spring:message code="productInfoSourceVersion.cellsNumber_format" />
								</small>
								
								<input
									class="form-control" name="votage{{$id}}" ng-model="batteryLineItem.votage" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" placeholder="<spring:message code='productInfoSourceVersion.votage' />" ng-required="batteryRequired"> [V]
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.votage{{$id}}.$error.required && productInfoSourceVersion.votage{{$id}}.$dirty || (batteryRequired == true  && batteryLineItem.votage == '' && formValidated == false)">
									<spring:message code="productInfoSourceVersion.votage_req" />
								</small>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.votage{{$id}}.$error.pattern">
									<spring:message code="productInfoSourceVersion.votage_format" />
								</small>
								
								<input
									class="form-control" name="capacity{{$id}}" ng-model="batteryLineItem.capacity" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" placeholder="<spring:message code='productInfoSourceVersion.capacity' />" ng-required="batteryRequired"> [mAh]
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.capacity{{$id}}.$error.required && productInfoSourceVersion.capacity{{$id}}.$dirty || (batteryRequired == true  && batteryLineItem.capacity == '' && formValidated == false)">
									<spring:message code="productInfoSourceVersion.capacity_req" />
								</small>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.capacity{{$id}}.$error.pattern">
									<spring:message code="productInfoSourceVersion.capacity_format" />
								</small>
								
								<input
									class="form-control" name="weight{{$id}}" ng-model="batteryLineItem.weight" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" placeholder="<spring:message code='productInfoSourceVersion.weight' />" ng-required="batteryRequired"> [g]
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.weight{{$id}}.$error.required && productInfoSourceVersion.weight{{$id}}.$dirty || (batteryRequired == true  && batteryLineItem.weight == '' && formValidated == false)">
									<spring:message code="productInfoSourceVersion.weight_req" />
								</small>
								<small class="text-danger form-text" ng-show="productInfoSourceVersion.weight{{$id}}.$error.pattern">
									<spring:message code="productInfoSourceVersion.weight_format" />
								</small>
			</div>
			<div class="col-1-md">
			<label><spring:message code="productInfoSourceVersion.batteryFile" /><span ng-if="batteryRequired" class="text-danger">*</span></label>
				<div id="fileArea">

    								<input id="text_batteryFile{{$index+1}}" class="form-control" placeholder="請選擇欲上傳的檔案" disabled />
    								<div class="fileUpload btn btn-primary input-group-addon">
        								<i class="fas fa-folder-open"></i> 選擇
        								<input id="batteryFile{{$index+1}}" type="file" name="files" class="upload"  onchange="batteryFileOnChange($(this).attr('id'))"/>
   			 						</div>

								<button type="button" class="btn btn-primary"
									ng-click="uploadBatteryFile(batteryLineItem,$index+1)">
									<span><spring:message code="productInfoSourceVersion.Upload" /></span>
								</button>
								</div>	
								<div id="BatteryFileUploadFail{{$index+1}}" class="text-danger"></div>
									<div
										ng-repeat="batteryFileLineItem in batteryLineItem.batteryFileLineItems"
										style="padding: 5px">
										<div class="row">
											<div class="col-sm-8"><a href="${pageContext.request.contextPath}/BatteryFile?fileName={{batteryFileLineItem.file}}">{{batteryFileLineItem.file}}</a></div>
											<div class="col-sm-1">
												<button type="button" class="btn btn-link" style="padding:0px 12px;"
													ng-click="removeBatteryFile(batteryFileLineItem.file,batteryLineItem,batteryFileLineItem)">
													<i class="fas fa-minus"></i>
												</button>
											</div>
											<div class="col-sm-3"></div>
										</div>
									</div>
									<input type="hidden" id="batteryFileLineItems{{$index}}" name="batteryFileLineItems{{$id}}" ng-model="batteryLineItem.batteryFileLineItems" value="{{batteryLineItem.batteryFileLineItems}}" ng-required="batteryRequired"></input>
									<small class="text-danger form-text" style="float:left" ng-show="batteryRequired == true && batteryLineItem.batteryFileLineItems.length == 0 && formValidated == false">
										<spring:message code="productInfoSourceVersion.batteryRelatedFiles_req" />
									</small>
			</div>
			<div class="col-1-md">
			<label>SKU<span ng-if="batteryRequired" class="text-danger">*</span></label>
			<button type="button" class="btn btn-link" style="color: #000;" data-toggle="collapse" data-target="#SKU_div_battery_{{$id}}" aria-expanded="false" aria-controls="SKU_div_battery_{{$id}}" ng-show="batteryLineItem.appliedVariationProduct.length==productLineItems.length"><i class="fas fa-list-ul"></i></button>
								<button type="button" class="btn btn-link" data-toggle="collapse" data-target="#SKU_div_battery_{{$id}}" aria-expanded="false" aria-controls="SKU_div_battery_{{$id}}" ng-hide="batteryLineItem.appliedVariationProduct.length==productLineItems.length"><i class="fas fa-list-ul"></i></button>

							<div class="collapse" id="SKU_div_battery_{{$id}}">
							<div class="well" style="padding:5px;">
							<div class="row">
							 <div class="form-inline" ng-repeat="productLineItem in productLineItems">
									<div class="form-check">
										<input class="form-check-input" type="checkbox" name="appliedVariationProduct[]" id="appliedVariationProduct_{{$id}}"
											ng-checked="batteryLineItem.appliedVariationProduct.indexOf(productLineItem.SKU) > -1"
											value="{{productLineItem.SKU}}"
											ng-click="setAppliedVariationProduct(productLineItem.SKU,batteryLineItem)">
										<label class="form-check-label" for="appliedVariationProduct_{{$id}}">
										<span>{{supplierKcode}}-{{productLineItem.SKU}} <span
											ng-if="productWithVariation=='1'">({{productLineItem.type1Value}}<span
												ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)
										</span>
										</span>
										</label>
									</div>	
							</div>
							</div>
							</div>
							</div>
								<input type="hidden" id="appliedVariationProduct{{$id}}" name="appliedVariationProduct{{$id}}" ng-model="batteryLineItem.appliedVariationProduct" value="{{batteryLineItem.appliedVariationProduct}}" ng-required="batteryRequired"></input>
								<small class="text-danger form-text" ng-show="batteryRequired == true && batteryLineItem.appliedVariationProduct.length==0 && formValidated == false">
									<spring:message code="productInfoSourceVersion.batteryRelatedSKU_req" />
								</small>
			</div>
			<div class="col-2-md">
			<label><spring:message code="productInfoSourceVersion.lithiumBatteryPackingWay" /></label>
			<select class="form-control" name="packingWay{{$id}}" ng-model="batteryLineItem.packingWay" ng-required="batteryLineItem.isPackingWayRequired">
										<option value="">--- Select ---</option>
										<option value="packedByThemselves"><spring:message code="LithiumBattery.PackedByThemselves" /></option>
										<option value="packedWithEquipment"><spring:message code="LithiumBattery.PackedWithEquipment" /></option>
										<option value="packedInEquipment"><spring:message code="LithiumBattery.PackedInEquipment" /></option>
									</select>
									<span ng-if="batteryLineItem.isPackingWayRequired" class="text-danger">*</span>
									<small class="text-danger" ng-show="productInfoSourceVersion.packingWay{{$id}}.$error.required && productInfoSourceVersion.packingWay{{$id}}.$dirty || (batteryLineItem.isPackingWayRequired == true  && batteryLineItem.packingWay == '' && formValidated == false)">
										<spring:message code="productInfoSourceVersion.lithiumBatteryPackingWay_req" />
									</small>
			</div>
			<div class="col-1-md">
			<label><button type="button" id="addBattery" class="btn btn-default"
							ng-click="addBatteryLineItem()">
							<i class="fas fa-plus"></i></button></label>

							<button type="button" class="btn btn-default"
										ng-click="removeBatteryLineItem(batteryLineItem)">
										<i class="fas fa-minus"></i>
									</button>
			</div>
			</div>
			</div>
			</div>

			<div class="row">
				<div class="col-md-12">
					<div class="page-heading"><spring:message code="productInfoSourceVersion.referenceInformation" /></div>
					<span class="text-danger"><spring:message code="productVersion.fileNaming" /></span>	
				</div>
			</div>
			<div class="row">
			<div class="col-md-12">

			<div class="form-row mb-3" ng-repeat="referenceFileLineItem in referenceFileLineItems">

			<div class="col text-center">
			<div class="form-group">
			<label><spring:message code="productInfoSourceVersion.referenceNo" /></label>
			<div class="form-group">{{$index+1}}</div>
			</div>
			</div>

			<div class="col-md-2">
			<label><span data-toggle="tooltip" data-placement="top" title="<spring:message code="productInfoSourceVersion.referenceFileName_hint" />"><spring:message code="productInfoSourceVersion.referenceFileName" /></span></label>
			<div id="fileArea" style="float:right;" ng-show="!referenceFileLineItem.file">
									<!-- <input id="referenceFile" class="btn btn-default" type="file" name="file" style="display: inline;"> -->
								    <input id="text_referenceFile{{$index+1}}" class="form-control" placeholder="請選擇欲上傳的檔案" disabled />
								    <div class="fileUpload btn btn-primary input-group-addon">
								        <i class="fas fa-folder-open"></i> 選擇
								        <input id="referenceFile{{$index+1}}" type="file" name="files" class="upload" onchange="referenceFileOnChange($(this).attr('id'))" />
								    </div>
								<button type="button" class="btn btn-primary"
									ng-click="uploadReferenceFile(referenceFileLineItem,$index+1)">								
									<span><spring:message code="productInfoSourceVersion.Upload" /></span>
								</button>
								</div>
								<div id="ReferenceFileUploadFail{{$index+1}}" class="text-danger"></div>
								<div ng-show="referenceFileLineItem.file">
								<a href="${pageContext.request.contextPath}/ReferenceFile?fileName={{referenceFileLineItem.file}}">{{referenceFileLineItem.file}}</a>								
								</div>								
			</div>

			<div class="col">
			<label><spring:message code="productInfoSourceVersion.referenceLink" /></label>
			<input id="referenceLink" class="form-control" style="width:220px;display:inline;" ng-model="referenceFileLineItem.link" >
			</div>

			<div class="col">
			<label><spring:message code="productInfoSourceVersion.referenceType" /><spring:message code="productInfoSourceVersion.slash" /><spring:message code="productInfoSourceVersion.applicableRegions" /></label>
			<script type="text/javascript">
								$(document).ready(function() {
    								$('.referenceType').multiselect({buttonWidth:'220px'});
								});
								</script>								
								<select class="form-control referenceType" ng-model="referenceFileLineItem.type" multiple="multiple">
   				 						<optgroup label="<spring:message code="referenceType.compliance" />">
        								<option value="ce"><spring:message code="referenceType.ce" /></option>
        						 		<option value="reach"><spring:message code="referenceType.reach" /></option>
        								<option value="rohs"><spring:message code="referenceType.rohs" /></option>
										<option value="weee"><spring:message code="referenceType.weee" /></option>
										<option value="fcc"><spring:message code="referenceType.fcc" /></option>
										<option value="fda"><spring:message code="referenceType.fda" /></option>
										<option value="msds"><spring:message code="referenceType.msds" /></option>
										<option value="ncc"><spring:message code="referenceType.ncc" /></option>
										<option value="bluetooth"><spring:message code="referenceType.bluetooth" /></option>
										<option value="cp65"><spring:message code="referenceType.cp65" /></option>
										<option value="complianceOther"><spring:message code="referenceType.complianceOther" /></option>
    									</optgroup>
    									<optgroup label="<spring:message code="referenceType.productOperationInformation" />">
        								<option value="umFile"><spring:message code="referenceType.umFile" /></option>
        								<option value="umLink"><spring:message code="referenceType.umLink" /></option>
        								<option value="operationVideo"><spring:message code="referenceType.operationVideo" /></option>
										</optgroup>
    									<optgroup label="<spring:message code="referenceType.OtherReferenceMaterial" />">
        								<option value="specificationTable"><spring:message code="referenceType.specificationTable" /></option>
        								<option value="marketingVideo"><spring:message code="referenceType.marketingVideo" /></option>
        								<option value="productPackageDesign"><spring:message code="referenceType.productPackageDesign" /></option>
										<option value="insurance"><spring:message code="referenceType.insurance" /></option>
										<option value="OtherReferenceMaterialOther"><spring:message code="referenceType.OtherReferenceMaterialOther" /></option>
    									</optgroup>
								</select>
								<hr style="margin:5px 2px;" />
												<span class="form-check form-check-inline">
												<input class="form-check-input" id="ref_{{$id}}CA" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('CA') > -1"
												value="CA" ng-click="setApplicableRegionForReferenceFile('CA',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}CA" >CA</label>
												</span>
												<span class="form-check form-check-inline">		
												<input class="form-check-input" id="ref_{{$id}}DE" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('DE') > -1"
												value="DE" ng-click="setApplicableRegionForReferenceFile('DE',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}DE" >DE</label>	
												</span>
												<span class="form-check form-check-inline">										
												<input class="form-check-input" id="ref_{{$id}}ES" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('ES') > -1"
												value="ES" ng-click="setApplicableRegionForReferenceFile('ES',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}ES" >ES</label>	
												</span>
												<span class="form-check form-check-inline">											
												<input class="form-check-input" id="ref_{{$id}}FR" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('FR') > -1"
												value="FR" ng-click="setApplicableRegionForReferenceFile('FR',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}FR" >FR</label>		
												</span>
												<span class="form-check form-check-inline">								
												<input class="form-check-input" id="ref_{{$id}}IT" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('IT') > -1"
												value="IT" ng-click="setApplicableRegionForReferenceFile('IT',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}IT" >IT</label>	
												</span>
												<span class="form-check form-check-inline">																																														
												<input class="form-check-input" id="ref_{{$id}}TW" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('TW') > -1"
												value="TW" ng-click="setApplicableRegionForReferenceFile('TW',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}TW" >TW</label>	
												</span>
												<span class="form-check form-check-inline">																																													
												<input class="form-check-input" id="ref_{{$id}}UK" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('UK') > -1"
												value="UK" ng-click="setApplicableRegionForReferenceFile('UK',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}UK" >UK</label>	
												</span>
												<span class="form-check form-check-inline">																						
												<input class="form-check-input" id="ref_{{$id}}US" type="checkbox" class="applicableRegion{{$id}}" name="applicableRegion[]"
												ng-checked="referenceFileLineItem.applicableRegion.indexOf('US') > -1"
												value="US" ng-click="setApplicableRegionForReferenceFile('US',referenceFileLineItem)" /> <label class="form-check-label" for="ref_{{$id}}US" >US</label>	
												</span>										
			</div>

			<div class="col">
			<label><spring:message code="productInfoSourceVersion.referenceLinkDescription" /></label>
			<input class="form-control" style="margin-bottom:5px;width:200px;" ng-model="referenceFileLineItem.description">

			</div>

			<div class="col">
			<label>SKU</label>
			<button type="button" class="btn btn-link" style="color: #000; margin-left:10px;" data-toggle="collapse" data-target="#SKU_div_{{$id}}" aria-expanded="false" aria-controls="SKU_div_{{$id}}" ng-show="referenceFileLineItem.appliedVariationProduct.length==productLineItems.length"><i class="fas fa-list"></i></button>
								<button type="button" class="btn btn-link" style="margin-left:10px;" data-toggle="collapse" data-target="#SKU_div_{{$id}}" aria-expanded="false" aria-controls="SKU_div_{{$id}}" ng-hide="referenceFileLineItem.appliedVariationProduct.length==productLineItems.length"><i class="fas fa-list-ul"></i></button>
			<div class="collapse" id="SKU_div_{{$id}}"  style="padding-left:20px;">
							<div class="well" style="padding:5px;">
							<div class="row" >							
							  <div class="col-md-12" ng-repeat="productLineItem in productLineItems">
							  <div class="checkbox checkbox-success" style="margin-left:20px;">
												<input type="checkbox" name="appliedVariationProduct[]"  style="display:none;"
													ng-checked="referenceFileLineItem.appliedVariationProduct.indexOf(productLineItem.SKU) > -1"
													value="{{productLineItem.SKU}}" id="{{productLineItem.SKU}}_{{$id}}"
													ng-click="setAppliedVariationProductForReferenceFile(productLineItem.SKU,referenceFileLineItem)" />
												<label for="{{productLineItem.SKU}}_{{$id}}">	
												<span>{{supplierKcode}}-{{productLineItem.SKU}} 
												<span ng-if="productWithVariation=='1'">({{productLineItem.type1Value}}<span ng-if="productLineItem.type2!=''">-{{productLineItem.type2Value}}</span>)</span>
												</span>
												</label>
							</div>					
							</div>							
							</div>
							</div>
							  </div>
			</div>

			<div class="col">
			<button type="button" id="addReferenceLink" class="btn btn-default"
							ng-click="addReferenceInfo()">
							<i class="fas fa-plus"></i>
						</button>
			<span ng-if="referenceFileLineItem.file == ''">
									<button type="button" class="btn btn-default"
												ng-click="removeReferenceFileLineItemWithoutFile(referenceFileLineItem)">
												<i class="fas fa-minus"></i>
											</button>
									</span>
									<span ng-if="referenceFileLineItem.file !== ''">
									<button type="button" class="btn btn-default"
												ng-click="removeReferenceFileLineItem(referenceFileLineItem)">
												<i class="fas fa-minus"></i>
											</button>
			</div>

			</div>


			</div>
			</div>

			</div><!-- end of rowframe -->
			</div><!-- end of tabpanel -->			
		</div> <!-- end of Tabs -->				
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading"><spring:message code="productInfoSourceVersion.note" /></div>
				</div>
			</div>			
			<div id="noteRow" class="row">
				<div class="col-md-12">
				<textarea id="note" class="form-control" rows="10" ng-model="note"></textarea>
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
			</div>
			<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading"><spring:message code="productInfoSourceVersion.innerNote" /></div>
				</div>
			</div>			
			<div id="innerNoteRow" class="row">
				<div class="col-md-12">
				<textarea id="innerNote" class="form-control" rows="10" ng-model="innerNote"></textarea>
				</div>
			</div>		
			<script type="text/javascript">
			$('#innerNote').wysihtml5({
			    "events": {
			        "load": function() {

			            $("#innerNoteRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");
			        	               
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
			</sec:authorize>			
			<div style="padding-bottom: 10px"></div>						
			<div class="row">
				<div class="col-md-12 text-right">
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
	  					<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
							<i class="fas fa-question-circle"></i>	<spring:message code="detailsOfBaseProduct.help" />								   						
						</a>  				
	  				</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">					
					<c:choose>
						<c:when test="${type eq 'Create'}">
						<a class="btn btn-success" href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code="detailsOfBaseProduct.backToList" /></a>					
						<input id="save" type="button" class="btn btn-primary" ng-click="saveDevelopingProduct(productInfoSourceVersion)"
						value="<spring:message code="productInfoSourceVersion.submit" />" />
						</c:when>
						<c:otherwise>
						<a class="btn btn-success" href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><spring:message code="detailsOfBaseProduct.back" /></a>					
						<input id="saveDraft" type="button" class="btn btn-primary" ng-click="saveDraftForProductInfoSource(productInfoSourceVersion)"
						value="<spring:message code="detailsOfBaseProduct.saveDraft" />" />
						<input id="submit" type="button" class="btn btn-warning" ng-click="submitProductInfoSource(productInfoSourceVersion)"
						value="<spring:message code="detailsOfBaseProduct.submit" />" />
						</c:otherwise>
					</c:choose>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
					<a class="btn btn-success" href="${pageContext.request.contextPath}/CoreProductInformation/${baseProductCode}"><spring:message code="detailsOfBaseProduct.back" /></a>											
					<input id="update" type="button" class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" ng-click="updateProductInfoSource(productInfoSourceVersion)"
						value="<spring:message code="detailsOfBaseProduct.update" />" />
					<button id="return" class="btn btn-default"  style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="returnProductInfoSource(productInfoSourceVersion)" >
					<i class="fas fa-step-backward"></i>  <spring:message code="detailsOfBaseProduct.return" />	
					</button>																					
					<button id="approve" class="btn btn-default"  style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="approveProductInfoSource(productInfoSourceVersion)" >
					<spring:message code="detailsOfBaseProduct.approve" />  <i class="fas fa-step-forward"></i> 
					</button>				
					</sec:authorize>										
				</div>
			</div>
		</form>
	</div>
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
<script>
function batteryFileOnChange(id){
    if (!$('#'+id).val()) {
        return;
    }
   $('#text_'+id).val($('#'+id).val());
}

function referenceFileOnChange(id){
	if (!$('#'+id).val()) {
	    return;
	}
	$('#text_'+id).val($('#'+id).val());
}
</script>	