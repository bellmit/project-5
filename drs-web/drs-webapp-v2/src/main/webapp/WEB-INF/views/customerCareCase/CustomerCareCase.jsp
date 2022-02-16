<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<c:choose>
		<c:when test="${type eq 'Create'}">
			<spring:message code="ccc.create"/> - DRS	
		</c:when>
		<c:otherwise>
			<spring:message code="ccc.edit"/>: <spring:message code="ccc.name"/> - DRS
        </c:otherwise>
	</c:choose>
</title>
<link href="<c:url value="/resources/css/bootstrap-multiselect.css"/>" type="text/css" rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap-example.css"/>" type="text/css" rel="stylesheet">
<link href="<c:url value="/resources/css/prettify.css"/>" type="text/css" rel="stylesheet">
<script type='text/javascript' src="<c:url value="/resources/js/bootstrap-multiselect.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/bootstrap-multiselect-collapsible-groups.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/prettify.js"/>"></script>	
	

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jquery-ui-timepicker-addon.css"/>"></link>
<script type='text/javascript' src="<c:url value="/resources/js/jquery-ui-timepicker-addon.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>	


<style>

.dropdown-menu {
max-height: 500px;
overflow-y: auto;
overflow-x: hidden;
}

</style>

<script>	
jQuery(window).on("load", function(e) {
		var control = {
			create : function(tp_inst, obj, unit, val, min,max, step) {
			$('<input class="ui-timepicker-input" value="'+ val + '" style="width:50%">')
				.appendTo(obj)
				.spinner({
					min : min,
					max : max,
					step : step,
					change : function(e, ui) { // key events
						// don't call if api was used and not key press
						if (e.originalEvent !== undefined)
							tp_inst._onTimeChange();
							tp_inst._onSelectHandler();
					},
					spin : function(e, ui) { // spin events
						tp_inst.control.value( tp_inst, obj, unit, ui.value);
						tp_inst._onTimeChange();
						tp_inst._onSelectHandler();
					}
				});
			return obj;
			},
			options : function(tp_inst, obj, unit, opts, val) {
				if (typeof (opts) == 'string' && val !== undefined)
					return obj.find('.ui-timepicker-input').spinner(opts, val);
					return obj.find('.ui-timepicker-input').spinner(opts);
			},
			value : function(tp_inst, obj, unit, val) {
				if (val !== undefined)
					return obj.find('.ui-timepicker-input').spinner('value', val);
					return obj.find('.ui-timepicker-input').spinner('value');
			}
		};		
		jQuery("#dateCreated,#marketplaceOrderDate").datetimepicker({
			beforeShow: function() {
				setTimeout(function(){
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
			},
			controlType : control,
			dateFormat : "yy-mm-dd",
			timeFormat : 'HH:mm z',
			timezoneList : [ {
				value : +480,
				label : 'Taipei'
			}, {
				value : -480,
				label : 'Pacific'
			} ]
		});						
		$("#dateCreated,#marketplaceOrderDate").attr('readonly', true);							
	});

	$(document).ready(function() {		
		$('#example-multiple-optgroups').multiselect();
		$(".multiselect").css("width", "100%");
		$(".btn-group").css("width", "100%");
		$(".multiselect-container").css("width", "100%");
		$("#supplier").select2({ theme: "bootstrap"});

	});
	
	angular.module('customerCareCase', []).controller('customerCareCaseCtrl',function($scope) {						
		var currentURL = document.URL;
		var marketplaceMapJson = ${marketplaceMapJson};
		var marketplaceIdToDrsCompanyMapJson = ${marketplaceIdToDrsCompanyMapJson};
		var marketplaceToDrsCompanyMapJson = ${marketplaceToDrsCompanyMapJson};						
		$scope.baseProductLineItems = [];
		$scope.SKULineItems = [];										
		if(currentURL.indexOf("edit") > -1){						
			var CustomerCareCaseJson = ${CustomerCareCaseJson};
			console.log(CustomerCareCaseJson);
			$scope.withOrderIds = [];
			$scope.noneOrderIds = [];													
			$scope.caseType = CustomerCareCaseJson.caseType;							
			//marketplaceOrderId
			if(CustomerCareCaseJson.marketplaceOrderId != "" && CustomerCareCaseJson.marketplaceOrderId != null){								
				$('#orderIdYes').prop('checked', true);											
				$scope.withOrderIds.push({
					marketplaceOrderId:CustomerCareCaseJson.marketplaceOrderId,
					marketplace:CustomerCareCaseJson.marketplace,
					marketplaceOrderDate:CustomerCareCaseJson.marketplaceOrderDate,
					customerName:CustomerCareCaseJson.customerName,
					drsCompanyKcode:marketplaceToDrsCompanyMapJson[CustomerCareCaseJson.marketplace]
				});																																																
			}else{								
				$('#orderIdNo').prop('checked', true);								
				$scope.noneOrderIds.push({
					customerName:CustomerCareCaseJson.customerName,
					marketplace:CustomerCareCaseJson.marketplace,
					drsCompanyKcode:marketplaceToDrsCompanyMapJson[CustomerCareCaseJson.marketplace]
				});																								
			}																					
			$scope.supplier = CustomerCareCaseJson.supplierKcode;							
			$scope.issueTypeCategory = CustomerCareCaseJson.issueTypeCategoryId
			$scope.dateCreated = CustomerCareCaseJson.dateCreated;								
			$scope.relatedIssueIds = [];							
			for (i = 0; i < CustomerCareCaseJson.relatedIssueIds.length; i++) { 								
				$scope.relatedIssueIds[i] = CustomerCareCaseJson.relatedIssueIds[i];															
			}																
			$scope.message = CustomerCareCaseJson.message[CustomerCareCaseJson.message.length - 1]["contents"];
			var ajaxUrlForIssueTypeToIssuesMap = '${pageContext.request.contextPath}/CustomerCareCases/getIssueTypeToIssuesMap/';
			var issueTypeToIssuesMap = null;
			var baseList = null;
			var skuList = null;							
			if(CustomerCareCaseJson.relatedProductBaseCodeList != null) baseList = CustomerCareCaseJson.relatedProductBaseCodeList.toString();
			if(CustomerCareCaseJson.relatedProductSkuCodeList != null) skuList = CustomerCareCaseJson.relatedProductSkuCodeList.toString();							
			$.ajax({
				type : 'get',
				url : ajaxUrlForIssueTypeToIssuesMap,
				contentType : "application/json; charset=utf-8",
				data : {
					baseList : baseList,
					skuList : skuList,
					category : Number(CustomerCareCaseJson.issueTypeCategoryId)
				},
				dataType : "json",
				success : function(data) {
					issueTypeToIssuesMap = data
					$("#example-multiple-optgroups").children().remove("optgroup");
																		
					if(Object.keys(issueTypeToIssuesMap).length > 0){
										
						var $select = $('#example-multiple-optgroups');
						$.each(issueTypeToIssuesMap, function(key, value){
							var group = $('<optgroup label="' + key + '" />');
								$.each(value, function(key,value){
									$('<option />').val(key).html(value).appendTo(group);
								});
							group.appendTo($select);
						});																			   
					}									
					$('#example-multiple-optgroups').val(CustomerCareCaseJson.relatedIssueIds);									
					$('#example-multiple-optgroups').multiselect('rebuild');																											
				}																			
			});	
																					
			if(CustomerCareCaseJson.relatedProductBaseCodeList != null){
		
				$("#relatedProduct-BP").prop('checked', true);
								
				$("#sku").hide();
				$("#baseProduct").show();
								
				var relatedProductBaseCodeList = CustomerCareCaseJson.relatedProductBaseCodeList;
								
				for(var baseCode in relatedProductBaseCodeList){
									
					if (relatedProductBaseCodeList.hasOwnProperty(baseCode)) {
									
						$scope.baseProductLineItems.push({baseProduct : relatedProductBaseCodeList[baseCode]});
									
					}
									
				}
				
				var supplierKcode = CustomerCareCaseJson.supplierKcode;
				var marketplace = CustomerCareCaseJson.marketplace;
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductBaseCodeToSupplierNameMap/';
				var productBaseCodeToSupplierNameMap = null;

				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {
						supplierKcode : supplierKcode
					},
					dataType : "json",
					success : function(data) {
						productBaseCodeToSupplierNameMap = data;				
						for (i = 0; i < $scope.baseProductLineItems.length; i++) {											
							emptyList("baseProduct" + i);											
							if (Object.keys(productBaseCodeToSupplierNameMap).length > 0) {
								var selectBaseCode = document.getElementById("baseProduct"+ i);
								var optBaseCode = document.createElement("option");
								optBaseCode.value = "";
								optBaseCode.textContent = "--- Select Base Product ---";
								selectBaseCode.appendChild(optBaseCode);
							}											
							for ( var key in productBaseCodeToSupplierNameMap) {
								optBaseCode = document.createElement("option");
								optBaseCode.value = key;
								optBaseCode.textContent = key + " " + productBaseCodeToSupplierNameMap[key];
								selectBaseCode.appendChild(optBaseCode);
							}																						
							selectBaseCode.value = CustomerCareCaseJson['relatedProductBaseCodeList'][i];																																	
						}//end for (i = 0; i < $scope.baseProductLineItems.length; i++)																																																		
					}																																			
				});																																								
			}else if(CustomerCareCaseJson.relatedProductSkuCodeList != null){								
				$("#relatedProduct-SKU").prop('checked', true);								
				$("#sku").show();
				$("#baseProduct").hide();								
				var relatedProductSkuCodeList = CustomerCareCaseJson.relatedProductSkuCodeList;								
				for(var skuCode in relatedProductSkuCodeList){									
					if (relatedProductSkuCodeList.hasOwnProperty(skuCode)) {									
						$scope.SKULineItems.push({sku : relatedProductSkuCodeList[skuCode]});									
					}									
				}											

			
				var supplierKcode = CustomerCareCaseJson.supplierKcode;
				var marketplace = CustomerCareCaseJson.marketplace;								
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductSkuCodeToSupplierNameMap/';
				var productSkuCodeToSupplierNameMap = null;
				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {
						supplierKcode : supplierKcode
					},
					dataType : "json",
					success : function(data) {
						productSkuCodeToSupplierNameMap = data;
						for (i = 0; i < $scope.SKULineItems.length; i++) {
							emptyList("sku" + i);
							if (Object.keys(productSkuCodeToSupplierNameMap).length > 0) {
								var selectSku = document.getElementById("sku"+ i);
								var optSku = document.createElement("option");
								optSku.value = "";
								optSku.textContent = "--- Select SKU ---";
								selectSku.appendChild(optSku);
							}
						
							
							for ( var key in productSkuCodeToSupplierNameMap) {
								optSku = document.createElement("option");
								optSku.value = key;
								optSku.textContent = key + " " + productSkuCodeToSupplierNameMap[key];
								selectSku.appendChild(optSku);
								
							}												
							selectSku.value = CustomerCareCaseJson['relatedProductSkuCodeList'][i];
							
						} //end for(i = 0; i < $scope.SKULineItems.length; i++)
					}
				});																						
			}															
			}else{							
				$scope.SKULineItems = [{sku : ''}];
				$scope.withOrderIds = [];
				$scope.noneOrderIds = [{noneOrder : ''}];							
			}
						
			$scope.checkOrderId = function() {													
				var checkedValue = null;							
				if (document.getElementById('orderIdYes').checked) {
					checkedValue = document.getElementById('orderIdYes').value;
				}							
				if (document.getElementById('orderIdNo').checked) {
					checkedValue = document.getElementById('orderIdNo').value;
				}														
				if (checkedValue == "yes") {
					$scope.withOrderIds = [{withOrder : ''}];
					$scope.noneOrderIds = [];								
				} else {	
					$scope.withOrderIds = [];
					$scope.noneOrderIds = [{noneOrder : ''}];																							
				}																											
			};
												
			$scope.getDrsCompany = function() {					
				var marketplace = $("#marketplace").val();													    
				if($scope.withOrderIds.length==1)$scope.withOrderIds[0]["drsCompanyKcode"] = marketplaceToDrsCompanyMapJson[marketplace];
				if($scope.noneOrderIds.length==1)$scope.noneOrderIds[0]["drsCompanyKcode"] = marketplaceToDrsCompanyMapJson[marketplace];							
				$scope.getRelatedProductList();														
			};

			$scope.addBaseProductLineItem = function() {
				$scope.baseProductLineItems.push({baseProduct : ''});
				updateProductBaseCodeToSupplierNameMap($scope.baseProductLineItems.length - 1);
			};

			$scope.removeBaseProductLineItem = function( baseProductLineItem) {
				if ($scope.baseProductLineItems.length > 1) {
					var idx = $scope.baseProductLineItems.indexOf(baseProductLineItem);
					$scope.baseProductLineItems.splice(idx, 1);
				}
				$scope.updateBaseProductList();
			};

			$scope.addSKULineItem = function() {
				$scope.SKULineItems.push({sku : ''});
				updateProductSkuCodeToSupplierNameMap($scope.SKULineItems.length - 1);
			};

			$scope.removeSKULineItem = function(SKULineItem) {
				if ($scope.SKULineItems.length > 1) {
					var idx = $scope.SKULineItems.indexOf(SKULineItem);
					$scope.SKULineItems.splice(idx, 1);
				}
				$scope.updateSkuList();
			};

			$scope.checkRelatedProduct = function() {
				var id = document.querySelector('input[name="relatedProduct"]:checked').id;
				var supplierKcode = document.getElementById('supplier').value;
				
				if(document.getElementById('marketplace') != null){
					marketplace = document.getElementById('marketplace').value;		
				}else{
					marketplace = $scope.withOrderIds[0]["marketplace"]
				}	

									
				if (id == "relatedProduct-BP") {
					for (var i = $scope.SKULineItems.length - 1; i >= 0; i--) {
						$scope.SKULineItems.splice(i, 1);
					}								
					$('#relatedProductSkuCodeList').val("");								
					document.getElementById("sku").style.display = "none";
					document.getElementById("baseProduct").style.display = "block";
					$scope.baseProductLineItems = [{baseProduct : ''}];
					getProductBaseCodeToSupplierNameMap(supplierKcode,marketplace);
					$scope.getIssueTypeToIssues();																
				} else if (id == "relatedProduct-SKU") {
					for (var i = $scope.baseProductLineItems.length - 1; i >= 0; i--) {
						$scope.baseProductLineItems.splice(i, 1);
					}								
					$('#relatedProductBaseCodeList').val("");
					document.getElementById("baseProduct").style.display = "none";
					document.getElementById("sku").style.display = "block";
					$scope.SKULineItems = [{sku : ''}];
					
					getProductSkuCodeToSupplierNameMap(supplierKcode,marketplace);
					$scope.getIssueTypeToIssues();
				}
			};

			$scope.updateSkuList = function() {
				var skuArray = new Array();
				for ( var key in $scope.SKULineItems) {                                
					if ($scope.SKULineItems.hasOwnProperty(key)) {								
						if ($scope.SKULineItems[key]['sku'] !== "") {
							skuArray.push($scope.SKULineItems[key]['sku']);
						}								
					}
				}
				document.getElementById('relatedProductSkuCodeList').value = skuArray.toString();
				$scope.getIssueTypeToIssues();							
			};

			$scope.updateBaseProductList = function() {
				var baseProductArray = new Array();							
				for ( var key in $scope.baseProductLineItems) {
					if ($scope.baseProductLineItems.hasOwnProperty(key)) {								
						if ($scope.baseProductLineItems[key]['baseProduct'] !== "") {
							baseProductArray.push($scope.baseProductLineItems[key]['baseProduct']);
						}								
					}
				}
				document.getElementById('relatedProductBaseCodeList').value = baseProductArray.toString();							
				$scope.getIssueTypeToIssues();							
			};

			$scope.getRelatedProductList = function() {
			
				var id = document.querySelector('input[name="relatedProduct"]:checked').id;
				var supplierKcode = document.getElementById('supplier').value;
				
				var marketplace = ""
				if(document.getElementById('marketplace') != null){
					marketplace = document.getElementById('marketplace').value;		
				}else{
					marketplace = $scope.withOrderIds[0]["marketplace"]
				}
				
			
				if (id == "relatedProduct-BP") {
					$scope.SKULineItems = [];
					$scope.baseProductLineItems = [{baseProduct : ''}];
					getProductBaseCodeToSupplierNameMap(supplierKcode,marketplace);
					$scope.getIssueTypeToIssues();								
				} else if (id == "relatedProduct-SKU") {
					$scope.baseProductLineItems = [];
					$scope.SKULineItems = [{sku : ''}];
					getProductSkuCodeToSupplierNameMap(supplierKcode,marketplace);
					$scope.getIssueTypeToIssues();								
				}
			};
			$scope.geOrderInfo = function() {														
				isOrderIdExist();							
			};
												
			function isOrderIdExist(){														
				var orderId = $('#marketplaceOrderId').val();
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/geOrderInfo/';
				var orderInfo = null;                            
				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {orderId : orderId},
					dataType : "json",
					success : function(data) {											
						orderInfo = data;																						
						if(orderInfo != null){												
							$scope.withOrderIds[0]["marketplace"] = marketplaceMapJson[orderInfo['marketpaceId']];
							$scope.withOrderIds[0]["marketplaceOrderDate"] = orderInfo['orderDate'];
							$scope.withOrderIds[0]["customerName"] = orderInfo['customerName'];
							$scope.withOrderIds[0]["drsCompanyKcode"] = marketplaceIdToDrsCompanyMapJson[orderInfo['marketpaceId']];
						}else{												
							$scope.withOrderIds[0]["marketplace"] = "";
							$scope.withOrderIds[0]["marketplaceOrderDate"] = "";
							$scope.withOrderIds[0]["customerName"] = "";
							$scope.withOrderIds[0]["drsCompanyKcode"] = "";																								
						}			
											
						$scope.$apply();											
					}																							
				});							
			}
												
			function emptyList(id) {
				document.getElementById(id).options.length = 0;
			}

			function getProductBaseCodeToSupplierNameMap(supplierKcodeParam,marketplaceParam) {
				var supplierKcode = supplierKcodeParam;
				var marketplace = marketplaceParam;
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductBaseCodeToSupplierNameMap/';
				var productBaseCodeToSupplierNameMap = null;
				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {
						supplierKcode : supplierKcode
					},
					dataType : "json",
					success : function(data) {
						productBaseCodeToSupplierNameMap = data;
						for (i = 0; i < $scope.baseProductLineItems.length; i++) {
							emptyList("baseProduct" + i);
							if (Object.keys(productBaseCodeToSupplierNameMap).length > 0) {
								var selectBaseProduct = document.getElementById("baseProduct"+ i);
								var optBaseProduct = document.createElement("option");
								optBaseProduct.value = "";
								optBaseProduct.textContent = "--- Select Base Product ---";
								selectBaseProduct.appendChild(optBaseProduct);
							}
							for ( var key in productBaseCodeToSupplierNameMap) {
								optBaseProduct = document.createElement("option");
								optBaseProduct.value = key;
								optBaseProduct.textContent = key + " "+ productBaseCodeToSupplierNameMap[key];
								selectBaseProduct.appendChild(optBaseProduct);
							}
						}//end for(i = 0; i < $scope.baseProductLineItems.length; i++) 
					}
				});
			}

			function updateProductBaseCodeToSupplierNameMap(id) {
				var supplierKcode = document.getElementById('supplier').value;
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductBaseCodeToSupplierNameMap/';
				var productBaseCodeToSupplierNameMap = null;
				if (supplierKcode != "" && marketplace !="") {
					$.ajax({
						type : 'get',
						url : ajaxUrl,
						contentType : "application/json; charset=utf-8",
						data : {
							supplierKcode : supplierKcode
						},
						dataType : "json",
						success : function(data) {
							productBaseCodeToSupplierNameMap = data;
							emptyList("baseProduct" + id);
							if (Object.keys(productBaseCodeToSupplierNameMap).length > 0) {
								var selectBaseProduct = document.getElementById("baseProduct"+ id);
								var optBaseProduct = document.createElement("option");
								optBaseProduct.value = "";
								optBaseProduct.textContent = "--- Select Base Product ---";
								selectBaseProduct.appendChild(optBaseProduct);
							}
							for ( var key in productBaseCodeToSupplierNameMap) {
								optBaseProduct = document.createElement("option");
								optBaseProduct.value = key;
								optBaseProduct.textContent = key + " " + productBaseCodeToSupplierNameMap[key];
								selectBaseProduct.appendChild(optBaseProduct);
							}
						}
					});
				}// end if(supplierKcode !="")
			}

			function getProductSkuCodeToSupplierNameMap(supplierKcodeParam,marketplaceParam) {

				var supplierKcode = supplierKcodeParam;
				var marketplace = marketplaceParam;
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductSkuCodeToSupplierNameMap/';
				var productSkuCodeToSupplierNameMap = null;

				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {
						supplierKcode : supplierKcode
					},
					dataType : "json",
					success : function(data) {
						productSkuCodeToSupplierNameMap = data;

						for (i = 0; i < $scope.SKULineItems.length; i++) {

							emptyList("sku" + i);

							if (Object.keys(productSkuCodeToSupplierNameMap).length > 0) {

								var selectSku = document.getElementById("sku"+ i);
								var optSku = document.createElement("option");
								optSku.value = "";
								optSku.textContent = "--- Select SKU ---";
								selectSku.appendChild(optSku);

							}
							
							for ( var key in productSkuCodeToSupplierNameMap) {
								optSku = document.createElement("option");
								optSku.value = key;
								optSku.textContent = key + " " + productSkuCodeToSupplierNameMap[key];
								selectSku.appendChild(optSku);
							}
						
						} //end for(i = 0; i < $scope.SKULineItems.length; i++)

					}

				});

			}

			function updateProductSkuCodeToSupplierNameMap(id) {
			
				var supplierKcode = document.getElementById('supplier').value;
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductSkuCodeToSupplierNameMap/';
				var productSkuCodeToSupplierNameMap = null;
				if (supplierKcode != "" && marketplace != "") {
				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {
						supplierKcode : supplierKcode
					},
					dataType : "json",
					success : function(data) {
						productSkuCodeToSupplierNameMap = data;
						emptyList("sku" + id);
						if (Object.keys(productSkuCodeToSupplierNameMap).length > 0) {
							var selectSku = document.getElementById("sku" + id);
							var optSku = document.createElement("option");
							optSku.value = "";
							optSku.textContent = "--- Select SKU ---";
							selectSku.appendChild(optSku);
						}
						for ( var key in productSkuCodeToSupplierNameMap) {
							optSku = document.createElement("option");
							optSku.value = key;
							optSku.textContent = key + " " + productSkuCodeToSupplierNameMap[key];
							selectSku.appendChild(optSku);
						}
					}
				});
				}// end if(supplierKcode !="") 								   
			}
												
			$scope.getIssueTypeToIssues = function(){							
				var baseList = $('#relatedProductBaseCodeList').val();
				var skuList = $('#relatedProductSkuCodeList').val();
				var category = $('#issueTypeCategory').val();					
				$scope.relatedIssueIds = "";							
				retrieveIssueTypeToIssues(baseList,skuList,category);						
			};	
					
			function retrieveIssueTypeToIssues(baseList,skuList,category){														
				var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssueTypeToIssuesMap/';
				var issueTypeToIssuesMap = null;					
				$.ajax({
					type : 'get',
					url : ajaxUrl,
					contentType : "application/json; charset=utf-8",
					data : {
						baseList : baseList,
						skuList : skuList,
						category : category
					},
					dataType : "json",
					success : function(data) {
						issueTypeToIssuesMap = data										
						$("#example-multiple-optgroups").children().remove("optgroup");																																				
						if(Object.keys(issueTypeToIssuesMap).length > 0){										
							var $select = $('#example-multiple-optgroups');
							$.each(issueTypeToIssuesMap, function(key, value){
								var group = $('<optgroup label="' + key + '" />');
								$.each(value, function(key,value){
									$('<option />').val(key).html(value).appendTo(group);
								});
								group.appendTo($select);
							});																			   
						}									

						$('#example-multiple-optgroups').multiselect('rebuild');																																																												
					}																														
				});								
			}																								
		});
</script>
</head>
<div class="container" ng-app="customerCareCase" ng-controller="customerCareCaseCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
						<c:when test="${type eq 'Create'}">
							<spring:message code="ccc.create"/>
			    			<c:url var="action" value="/CustomerCareCases/save"></c:url>
						</c:when>
						<c:otherwise>
							<spring:message code="ccc.edit"/>: <spring:message code="ccc.name"/>
			    			<c:url var="action" value="/CustomerCareCases/update"></c:url>
						</c:otherwise>
					</c:choose>
				</div>
				<span class="text-danger"><spring:message code='ccc.tempValidation' /></span>	
			</div>
		</div>		
		<form:form action="${action}" name="CustomerCareCase" class="form-horizontal text-left"  modelAttribute="CustomerCareCase">
		
		<div class="row">
		<div class="col-sm-6">

		<c:choose>
			<c:when test="${type eq 'Edit'}">
		<div class="form-group row">
		    <label for="caseId" class="col-sm-4 col-form-label"><spring:message code="ccc.id"/></label>
		    <div class="col-sm-8">
		      <form:input id="caseId" path="caseId" class="form-control" readonly="true" />
		    </div>
		  </div>
		  </c:when>
		</c:choose>
		  
		  <div class="form-group row">
		    <label for="caseType" class="col-sm-4 col-form-label"><spring:message code="ccc.caseType"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <c:choose>
					<c:when test="${type eq 'Create'}">
						<form:hidden id="caseId" path="caseId" />
					</c:when>
					</c:choose> 
						<form:select id="caseType" class="form-control" path="caseType" ng-model="caseType" required="required">
							<form:option value="" label="--- Select ---" />
							<c:forEach items="${caseTypeList}" var="caseType">									
								<form:option value="${caseType}"><spring:message code="ccc.${caseType}" /></form:option>								
							</c:forEach>
						</form:select>							
				<small class="text-danger form-text" ng-show="CustomerCareCase.caseType.$error.required && CustomerCareCase.caseType.$dirty">
					<spring:message code='ccc.caseType_req' />
				</small>
		    </div>
		  </div>
		  
		  <div class="form-group row">
		    <label for="orderId" class="col-sm-4 col-form-label"><spring:message code="ccc.orderId"/></label>
		    <div class="col-sm-8">
		      <div class="form-check form-check-inline">
				  <input class="form-check=input" id="orderIdYes" name="orderId" value="yes" type="radio" ng-click="checkOrderId()" >
				  <label class="form-check-label" for="inlineCheckbox1"><spring:message code="ccc.orderIdYes"/></label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check=input" id="orderIdNo" name="orderId" value="no" type="radio" ng-click="checkOrderId()" checked>
				  <label class="form-check-label" for="inlineCheckbox2"><spring:message code="ccc.orderIdNo"/></label>
				</div>
		    </div>
		  </div>
		  
		  <div class="form-group row" ng-repeat="withOrderId in withOrderIds" >
		    <label for="marketplaceOrderId" class="col-sm-4 col-form-label"><spring:message code="ccc.marketplaceOrderId"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="marketplaceOrderId" class="form-control" path="marketplaceOrderId" ng-model="withOrderId.marketplaceOrderId" ng-change="geOrderInfo();" required="required"/>						
				<small class="text-danger form-text" ng-show="CustomerCareCase.marketplaceOrderId.$error.required && CustomerCareCase.marketplaceOrderId.$dirty">
					<spring:message code='ccc.orderId_req' />
				</small>
		    </div>
		  </div>
		  
		  <div class="form-group row" ng-repeat="withOrderId in withOrderIds">
		    <label for="marketplaceOrderDate" class="col-sm-4 col-form-label"><spring:message code="ccc.orderDate"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="marketplaceOrderDate" class="form-control" style="display:inline;cursor:default;background-color:white;" path="marketplaceOrderDate" ng-model="withOrderId.marketplaceOrderDate" required="required"/>
							<small class="text-danger form-text" ng-show="CustomerCareCase.marketplaceOrderDate.$error.required && CustomerCareCase.marketplaceOrderDate.$dirty">
								<spring:message code='ccc.orderDate_req' />
							</small>
		    </div>
		  </div>
		  
		  <div class="form-group row" ng-repeat="withOrderId in withOrderIds" >
		    <label for="customerName" class="col-sm-4 col-form-label"><spring:message code="ccc.customerName"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="customerName" class="form-control" path="customerName" ng-model="withOrderId.customerName" required="required"/>
							<small class="text-danger form-text" ng-show="CustomerCareCase.customerName.$error.required && CustomerCareCase.customerName.$dirty">
								<spring:message code='ccc.customerName_req' />
							</small>
		    </div>
		  </div>

		   <div class="form-group row" ng-repeat="withOrderId in withOrderIds">
		    <label for="DRSCompany" class="col-sm-4 col-form-label"><spring:message code="ccc.drsCompany"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="DRSCompany" class="form-control" path="drsCompanyKcode" ng-model="withOrderId.drsCompanyKcode" required="required" readonly="true"/>
				<small class="text-danger form-text" ng-show="CustomerCareCase.drsCompanyKcode.$error.required && CustomerCareCase.drsCompanyKcode.$dirty">
					<spring:message code='ccc.drsCompany_req' />
				</small>
		    </div>
		  </div>

		  <div class="form-group row" ng-repeat="noneOrderId in noneOrderIds" >
		    <label for="customerName" class="col-sm-4 col-form-label"><spring:message code="ccc.customerName"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="customerName" class="form-control" path="customerName" ng-model="noneOrderId.customerName" required="required"/>
							<small class="text-danger form-text" ng-show="CustomerCareCase.customerName.$error.required && CustomerCareCase.customerName.$dirty">
								<spring:message code='ccc.customerName_req' />
							</small>
		    </div>
		  </div>
		  
		  <div class="form-group row" ng-repeat="noneOrderId in noneOrderIds">
		    <label for="marketplace" class="col-sm-4 col-form-label"><spring:message code="common.marketplace"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:select id="marketplace" class="form-control" path="marketplace" ng-model="noneOrderId.marketplace" ng-change="getDrsCompany()" required="required">
				<form:option value="" label="--- Select ---" />
				<c:forEach items="${marketplaceList}" var="m">
					<form:option value="${m.name()}">${m.name}</form:option>
				</c:forEach>
			</form:select>	
			<small class="text-danger form-text" ng-show="CustomerCareCase.contactChannel.$error.required && CustomerCareCase.contactChannel.$dirty">
				<spring:message code='ccc.marketplace_req' />
			</small>		
		    </div>
		  </div>
		  
		   <div class="form-group row" ng-repeat="noneOrderId in noneOrderIds">
		    <label for="DRSCompany" class="col-sm-4 col-form-label"><spring:message code="ccc.drsCompany"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="DRSCompany" class="form-control" path="drsCompanyKcode" ng-model="noneOrderId.drsCompanyKcode" required="required" readonly="true"/>
				<small class="text-danger form-text" ng-show="CustomerCareCase.drsCompanyKcode.$error.required && CustomerCareCase.drsCompanyKcode.$dirty">
					<spring:message code='ccc.drsCompany_req' />
				</small>
		    </div>
		  </div>
		  
		  <div class="form-group row">
		    <label for="supplier" class="col-sm-4 col-form-label"><spring:message code="ccc.supplier"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:select id="supplier" class="form-control"
								path="supplierKcode" ng-model="supplier"
								ng-change="getRelatedProductList()" required="required">
								<form:option value="" label="--- Select ---" />
								<c:forEach var="supplier" items="${supplierKcodeToShortEnUsNameMap}">
								<form:option value="${supplier.key}" label="${supplier.key} ${supplier.value}" />
								</c:forEach>
							</form:select>						
							<div class="text-danger" ng-show="CustomerCareCase.supplierKcode.$error.required && CustomerCareCase.supplierKcode.$dirty">
								<spring:message code='ccc.supplier_req' />
							</div>
		    </div>
		  </div>
		  
		  <div class="form-group row">
		    <label for="relatedProduct-BP" class="col-sm-4 col-form-label"><spring:message code="ccc.relatedProduct"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <div class="form-check form-check-inline">
				   <input class="form-check-input" name="relatedProduct" id="relatedProduct-BP" value="Base Product" type="radio" ng-click="checkRelatedProduct()">
				  <label class="form-check-label" for="inlineCheckbox1"><spring:message code="ccc.baseProduct"/></label>
				</div>
		     
		      
			<div class="form-check form-check-inline">
			  <input name="relatedProduct" class="form-check-input" id="relatedProduct-SKU" value="SKU" type="radio" ng-click="checkRelatedProduct()" checked="TRUE">
			  <label class="form-check-label" for="inlineCheckbox1"><spring:message code="ccc.sku"/></label>
			</div>
		    </div>
		  </div>
		  
		  <div id="baseProduct" style="display: none;">
		<div ng-repeat="baseProductLineItem in baseProductLineItems">
		  <div class="form-group row">
		    <label for="baseProduct" class="col-sm-4 col-form-label"> Product </label>
		    <div class="col-sm-6">
		      <select id="baseProduct{{$index}}" class="form-control"
												name="baseProduct{{$index}}" ng-model="baseProductLineItem.baseProduct"
												ng-change="updateBaseProductList()" required>
											</select>	
											<small class="text-danger form-text" ng-show="CustomerCareCase.baseProduct{{$index}}.$error.required && CustomerCareCase.baseProduct{{$index}}.$dirty">
												<spring:message code='ccc.baseProduct_req' />
											</small>
		    </div>
			<div class="col-sm-1" style="padding-left: 0px">
											<button type="button" class="btn btn-default" ng-click="removeBaseProductLineItem(baseProductLineItem)">
												<i class="fas fa-trash-alt"></i>
											</button>
										</div>
			<div class="col-sm-1" style="padding-left: 0px">
										<button type="button" class="btn btn-default" ng-click="addBaseProductLineItem()">
											<i class="fas fa-plus"></i>
										</button>
										<form:hidden id="relatedProductBaseCodeList" path="relatedProductBaseCodeList" />
									</div>
		  </div>

		  	</div>
							</div>




		<div id="sku">
			<div ng-repeat="SKULineItem in SKULineItems">
		  <div class="form-group row">
		    <label for="sku" class="col-sm-4 col-form-label">SKU</label>
		    <div class="col-sm-6">
		      <select id="sku{{$index}}" class="form-control" name="sku{{$index}}" ng-model="SKULineItem.sku" ng-change="updateSkuList()" required>
											</select>											
											<small class="text-danger form-text" ng-show="CustomerCareCase.sku{{$index}}.$error.required && CustomerCareCase.sku{{$index}}.$dirty">
												<spring:message code='ccc.sku_req' />
											</small>

		    </div>
		    <div class="col-sm-1" style="padding-left: 0px">
											<button type="button" class="btn btn-default" ng-click="removeSKULineItem(SKULineItem)">
												<i class="fas fa-trash-alt"></i>
											</button>
										</div>
			<div class="col-sm-1" style="padding-left: 0px">
										<button type="button" class="btn btn-default" ng-click="addSKULineItem()">
											<span class="fas fa-plus"></span>
										</button>
										<form:hidden id="relatedProductSkuCodeList" path="relatedProductSkuCodeList" />
									</div>
									</div>
		  </div>
			</div>
		</div>






			<div class="col-md-6">

			<div class="form-group row">
		    <label for="issueTypeCategory" class="col-sm-4 col-form-label"><spring:message code="ccc.issueCategory"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:select id="issueTypeCategory" class="form-control" path="issueTypeCategoryId" ng-model="issueTypeCategory" ng-change="getIssueTypeToIssues()" required="required">
								<spring:message code="issue.ALL_CATEGORY" var="ALL_CATEGORY"/>
								<form:option value="" label="${ALL_CATEGORY}"/>
								<c:forEach items="${categoryIdToNameMap}" var="categoryIdToName">									
								<form:option value="${categoryIdToName.key}"><spring:message code="issue.${categoryIdToName.value}"/></form:option>								
								</c:forEach>							
							</form:select>						
							<small class="text-danger form-text" ng-show="CustomerCareCase.issueTypeCategoryId.$error.required && CustomerCareCase.issueTypeCategoryId.$dirty">
								<spring:message code='ccc.issueCategory_req' />
							</small>
		    </div>
		  </div>

		  <div class="form-group row">
		    <label for="example-multiple-optgroups" class="col-sm-4 col-form-label"><spring:message code="ccc.issue"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <select id="example-multiple-optgroups" name="relatedIssueIds" multiple="multiple" ng-model="relatedIssueIds" required>
							</select>						
							<div class="text-danger" ng-show="CustomerCareCase.relatedIssueIds.$error.required && CustomerCareCase.relatedIssueIds.$dirty">
								<spring:message code='ccc.issue_req' />
							</div>
		    </div>
		  </div>

		  			<div class="form-group row">
		  			<div class="col-sm-12 offset-sm-4">
		   <a class="btn btn-link" target="_blank" href="${pageContext.request.contextPath}/Issues/create">
								<spring:message code='issue.createIssue' /></a>
								</div>
		  </div>

		  <div class="form-group row">
		    <label for="dateCreated" class="col-sm-4 col-form-label"><spring:message code="ccc.customerContactTime"/><span class="text-danger">*</span></label>
		    <div class="col-sm-8">
		      <form:input id="dateCreated" class="form-control" style="display:inline;cursor:default;background-color:white;" path="dateCreated" ng-model="dateCreated" required="required"/>
							<small class="text-danger form-text" ng-show="CustomerCareCase.dateCreated.$error.required && CustomerCareCase.dateCreated.$dirty">
								<spring:message code='ccc.customerContactTime_req' />
							</small>
		    </div>
		  </div>

			  <div class="form-group row">
		    <label for="status" class="col-sm-4 col-form-label"><spring:message code="ccc.status"/></label>
		    <div class="col-sm-8">
		      <form:select id="status" class="form-control" path="status">
 								<form:option value="processing"><spring:message code="ccc.processing"/></form:option>
 								<form:option value="waitingForCustomerResponse"><spring:message code="ccc.waitingForCustomerResponse"/></form:option>
 								<form:option value="caseClosed"><spring:message code="ccc.caseClosed"/></form:option>																												
							</form:select>
		    </div>
		  </div>

			</div>
		</div>

		<div class="row my-3">
			<div class="col-md-12">
				<label><spring:message code="ccc.origMsg"/><span class="text-danger">*</span></label>
				<c:forEach items="${CustomerCareCase.messages}" varStatus="message">
					<c:if test="${message.last}">
						<form:textarea id="originalMessageFromCustomer${message.index}"
							class="form-control" path="message[${message.index}].contents"
							rows="4" ng-model="message" required="required"/>
						<form:hidden path="message[${message.index}].lineSeq" />
						<div class="text-danger" ng-show="CustomerCareCase['message[${message.index}].contents'].$error.required && CustomerCareCase['message[${message.index}].contents'].$dirty">
							<spring:message code='ccc.origMsg_req' />
						</div>	
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-right">							
				<input class="btn btn-primary" type="submit" value="<spring:message code='ccc.submit' />" ng-disabled="CustomerCareCase.$invalid" />
			    <c:choose>
					<c:when test="${type eq 'Edit'}">		
						<a class="btn btn-link" href="${pageContext.request.contextPath}/CustomerCareCases/${CustomerCareCase.caseId}">
							<spring:message code='ccc.cancel' />
						</a>
					</c:when>			
				</c:choose>
			</div>
		</div>
	</form:form>
	</div>
	</div>
</div>