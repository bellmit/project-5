<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

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
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">

<link href="<c:url value="/resources/css/bootstrap3-wysihtml5.css"/>" type="text/css" rel="stylesheet">




<style type="text/css">
.hover_img a { position:relative; }
.hover_img a span { position:absolute; display:none; z-index:99;}
.hover_img a:hover span { display:block; top:-410px;border:2px ridge silver;}
.hover_img img{height:400px;}
</style>


<script type='text/javascript' src="<c:url value="/resources/js/Countable.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/wysihtml5x-toolbar.min.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/handlebars.runtime.min.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/bootstrap3-wysihtml5.min.js"/>"></script>
<script src="<c:url value="/resources/js/push-content.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>


<script>

	var idleTime = 0;
	
	$(document).ready( function() {    		
		var idleInterval = setInterval(timerIncrement, 60000); // 1 minute			
		$(this).mousemove(function (e) {				 
			idleTime = 0;
		});
		$(this).keypress(function (e) {
			idleTime = 0;			       
		});			 
		function timerIncrement() {
			idleTime = idleTime + 1;
			if (idleTime > 10) { // 10 minutes
				var url = '${pageContext.request.contextPath}/ProductMarketingMaterial/avoidSessionTimeout';
		        $.get( url );
			}
		}			   
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
		var pushRight = new Menu({
			wrapper: '#o-wrapper',
			type: 'push-right',
			menuOpenerClass: '.c-button',
			maskId: '#c-mask'
		});
		$('.c-button').each(function(){					 					 
			$(this).on('click', function(e) {
				e.preventDefault;
				pushRight.open();
				$('#refContent').attr('src',$(this).attr('data-src'));
			});				 
		});				
		$('#refContent').on("load",function(){
			$('#refContent').contents().find('#s5_body_padding').hide();
			$('#refContent').contents().find('.breadcrumb').hide();
			$('#refContent').contents().find('.nav').hide();					
			$('#refContent').contents().find('.btn').hide();
			$('#refContent').contents().find('.prod-status').hide();					
			$('#refContent').contents().find('footer').hide();					
			$('#refContent').show();
		});				
		$("#saveDraft,#submit,#update,#return,#approve").one('click', function (event) {  
			event.preventDefault();        
			$(this).prop('disabled', true);
		});				
	});

	function copyContent(){		
		var iscope = document.getElementById('refContent').contentWindow.angular.element("#app").scope();
		var $scope = angular.element('#app').scope();
		
		$scope.productName = iscope.productName;
		$scope.productShortName = iscope.productShortName;
		$scope.feature1 = iscope.feature1;
		$scope.feature2 = iscope.feature2;
		$scope.feature3 = iscope.feature3;
		$scope.feature4 = iscope.feature4;
		$scope.feature5 = iscope.feature5;
		$scope.description = iscope.description;							
		$('#descriptionRow .wysihtml5-sandbox').contents().find('body').html($scope.description);
		$('#descriptionRow .wysihtml5-sandbox').contents().find('body').trigger('click');
		$scope.searchTerms1 = iscope.searchTerms1;
		$scope.searchTerms2 = iscope.searchTerms2;
		$scope.searchTerms3 = iscope.searchTerms3; 
		$scope.searchTerms4 = iscope.searchTerms4;
		$scope.searchTerms5 = iscope.searchTerms5;
		$scope.note = iscope.note;
		$('#noteRow .wysihtml5-sandbox').contents().find('body').html($scope.note);			
		$scope.$apply();		    
		Countable.live(document.getElementById("productName"), $scope.callbackForProductName);
		Countable.live(document.getElementById("productShortName"), $scope.callbackForProductShortName);
		Countable.live(document.getElementById("feature1"), $scope.callbackForFeature1);
		Countable.live(document.getElementById("feature2"), $scope.callbackForFeature2);
		Countable.live(document.getElementById("feature3"), $scope.callbackForFeature3);
		Countable.live(document.getElementById("feature4"), $scope.callbackForFeature4);
		Countable.live(document.getElementById("feature5"), $scope.callbackForFeature5);		    
		Countable.live(document.getElementById("searchTerms1"), $scope.callbackForSearchTerms1);
		Countable.live(document.getElementById("searchTerms2"), $scope.callbackForSearchTerms2);
		Countable.live(document.getElementById("searchTerms3"), $scope.callbackForSearchTerms3);
		Countable.live(document.getElementById("searchTerms4"), $scope.callbackForSearchTerms4);
		Countable.live(document.getElementById("searchTerms5"), $scope.callbackForSearchTerms5);		    
	};	

	function selectMainImageFile(element){		
		var id = $(element).attr('id');
		var fileData = $(element).prop("files")[0];		
		$('#'+id+'Text').val(fileData["name"]);		
	}
	
	function selectVariationImageFile(element){		
		var id = $(element).attr('id');
		var fileData = $(element).prop("files")[0];		
		$('#'+id+'Text').val(fileData["name"]);		
	}
		
	function selectOtherImageFile(element){		
		var id = $(element).attr('id');
		var fileData = $(element).prop("files")[0];		
		$('#'+id+'Text').val(fileData["name"]);				
	}
			
	angular.module('productMarketingMaterialSourceVersion', []).controller('productMarketingMaterialSourceVersionCtrl',function($scope) {				
		var product  = ${product};				
		var productMarketingMaterialVersion = JSON.parse(product["data"]);
		var baseProductCode = '${baseProductCode}';
		var supplierKcode = '${supplierKcode}';	
		var variationCode = '${variationCode}';
		var mmId = '${mmId}';	
		$scope.baseProductCode = baseProductCode;
		$scope.supplierKcode = supplierKcode;		
		$scope.variationCode = variationCode;
		$scope.mmId = mmId;	
		$scope.productName = productMarketingMaterialVersion["name"];				
		$scope.productShortName = productMarketingMaterialVersion["shortName"];								
		$scope.feature1 = productMarketingMaterialVersion["feature1"];											
		$scope.feature2 = productMarketingMaterialVersion["feature2"];								
		$scope.feature3 = productMarketingMaterialVersion["feature3"];								
		$scope.feature4 = productMarketingMaterialVersion["feature4"];			
		$scope.feature5 = productMarketingMaterialVersion["feature5"];																				
		$scope.description = productMarketingMaterialVersion["description"];																
		$scope.searchTerms1 = productMarketingMaterialVersion["searchTerms1"];						
		$scope.searchTerms2 = productMarketingMaterialVersion["searchTerms2"];				
		$scope.searchTerms3 = productMarketingMaterialVersion["searchTerms3"]; 				
		$scope.searchTerms4 = productMarketingMaterialVersion["searchTerms4"];				
		$scope.searchTerms5 = productMarketingMaterialVersion["searchTerms5"];																				
		$scope.note = productMarketingMaterialVersion["note"];																								
		$scope.productLineItems = JSON.parse(productMarketingMaterialVersion["products"]);
		$scope.idx = null;
		var i;
		for(i = 0; i < $scope.productLineItems.length; i++){															
			if($scope.productLineItems[i]['SKU'] == $scope.variationCode){
				$scope.idx = i;	
				break;
			} 			
		}
		$scope.productWithVariation = productMarketingMaterialVersion["productWithVariation"];
		$scope.status = product["status"];				
		$("#productName").val(productMarketingMaterialVersion["name"]);								
		$("#productShortName").val(productMarketingMaterialVersion["shortName"]);				
		$("#feature1").val(productMarketingMaterialVersion["feature1"]);
		$("#feature2").val(productMarketingMaterialVersion["feature2"]);				
		$("#feature3").val(productMarketingMaterialVersion["feature3"]);
		$("#feature4").val(productMarketingMaterialVersion["feature4"]);				
		$("#feature5").val(productMarketingMaterialVersion["feature5"]);
		$("#description").html(productMarketingMaterialVersion["description"]);
		$("#description").val(productMarketingMaterialVersion["description"]);				
		$("#description").trigger('click');
		$("#searchTerms1").val(productMarketingMaterialVersion["searchTerms1"]);				
		$("#searchTerms2").val(productMarketingMaterialVersion["searchTerms2"]);				
		$("#searchTerms3").val(productMarketingMaterialVersion["searchTerms3"]);				
		$("#searchTerms4").val(productMarketingMaterialVersion["searchTerms4"]);				
		$("#searchTerms5").val(productMarketingMaterialVersion["searchTerms5"]);
		$("#note").html(productMarketingMaterialVersion["note"]);
		$("#note").trigger('click');
		
		
		/*
		function callbackForProductName (counter) {															 
			if(counter['all'] == 1){ 			  
				document.getElementById("wordCounterForProductName").innerHTML = counter['all'] +" character";			  
			 }else{			  
			 	document.getElementById("wordCounterForProductName").innerHTML = counter['all'] +" characters"; 			  		  
			 }
		}
		Countable.live(document.getElementById("productName"), callbackForProductName);					
		$scope.callbackForProductName = callbackForProductName;				
		function callbackForProductShortName (counter) { 		   
			if(counter['all'] ==1 ){ 			  
			 	document.getElementById("wordCounterForProductShortName").innerHTML = counter['all'] +" character";				  
			}else{			  
			 	document.getElementById("wordCounterForProductShortName").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}
		Countable.live(document.getElementById("productShortName"), callbackForProductShortName);
		$scope.callbackForProductShortName = callbackForProductShortName;
		function callbackForFeature1 (counter) { 		   
			if(counter['all'] == 1){ 			  
			 	document.getElementById("wordCounterForFeature1").innerHTML = counter['all'] +" character";			  
			}else{			  
			 	document.getElementById("wordCounterForFeature1").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}
		Countable.live(document.getElementById("feature1"), callbackForFeature1);
		$scope.callbackForFeature1 = callbackForFeature1;
		function callbackForFeature2 (counter) { 		   
			if(counter['all'] ==1 ){ 			  
			 	document.getElementById("wordCounterForFeature2").innerHTML = counter['all'] +" character";				  
			}else{			  
			 	document.getElementById("wordCounterForFeature2").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}
		Countable.live(document.getElementById("feature2"), callbackForFeature2);
		$scope.callbackForFeature2 = callbackForFeature2;
		function callbackForFeature3 (counter) { 		   
			if(counter['all'] == 1 ){ 			  
			 	document.getElementById("wordCounterForFeature3").innerHTML =counter['all'] +" character";			  
			}else{			  
			 	document.getElementById("wordCounterForFeature3").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}
		Countable.live(document.getElementById("feature3"), callbackForFeature3);
		$scope.callbackForFeature3 = callbackForFeature3;
		function callbackForFeature4 (counter) { 		   
			if(counter['all'] == 1 ){ 			  
			 	document.getElementById("wordCounterForFeature4").innerHTML =counter['all'] +" character";			  
			}else{			  
			 	document.getElementById("wordCounterForFeature4").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}
		Countable.live(document.getElementById("feature4"), callbackForFeature4);
		$scope.callbackForFeature4 = callbackForFeature4;
		function callbackForFeature5 (counter) { 		   
			if(counter['all'] == 1){ 			  
				document.getElementById("wordCounterForFeature5").innerHTML = counter['all'] +" character";				  
			}else{			  
			 	document.getElementById("wordCounterForFeature5").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}
		Countable.live(document.getElementById("feature5"), callbackForFeature5);
		$scope.callbackForFeature5 = callbackForFeature5;
		function callbackForSearchTerms1 (counter) { 		   
			if(counter['all'] == 1){ 			  
			 	document.getElementById("wordCounterForSearchTerms1").innerHTML = counter['all'] +" character";			  
			}else{			  
			 	document.getElementById("wordCounterForSearchTerms1").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}		
		Countable.live(document.getElementById("searchTerms1"), callbackForSearchTerms1);
		$scope.callbackForSearchTerms1 = callbackForSearchTerms1;
		function callbackForSearchTerms2 (counter) { 		   
			if(counter['all'] == 1){ 			  
			 	document.getElementById("wordCounterForSearchTerms2").innerHTML = counter['all'] +" character";			  
			}else{			  
			 	document.getElementById("wordCounterForSearchTerms2").innerHTML = counter['all'] +" characters"; 			  		  
			} 		   		   		  
		}		
		Countable.live(document.getElementById("searchTerms2"), callbackForSearchTerms2);
		$scope.callbackForSearchTerms2 = callbackForSearchTerms2;
		function callbackForSearchTerms3 (counter) { 		   
			 if(counter['all'] == 1){ 			  
			 	document.getElementById("wordCounterForSearchTerms3").innerHTML = counter['all'] +" character";			  
			 }else{			  
			 	document.getElementById("wordCounterForSearchTerms3").innerHTML = counter['all'] +" characters"; 			  		  
			 } 		   		   		  
		}		
		Countable.live(document.getElementById("searchTerms3"), callbackForSearchTerms3);
		$scope.callbackForSearchTerms3 = callbackForSearchTerms3;
		function callbackForSearchTerms4 (counter) { 		   
			 if(counter['all'] == 1){ 			  
			 	document.getElementById("wordCounterForSearchTerms4").innerHTML = counter['all'] +" character";			  
			 }else{			  
			 	document.getElementById("wordCounterForSearchTerms4").innerHTML = counter['all'] +" characters"; 			  		  
			 } 		   		   		  
		}		
		Countable.live(document.getElementById("searchTerms4"), callbackForSearchTerms4);
		$scope.callbackForSearchTerms4 = callbackForSearchTerms4;
		function callbackForSearchTerms5 (counter) { 		   
			 if(counter['all'] == 1){ 			  
			 	document.getElementById("wordCounterForSearchTerms5").innerHTML = counter['all'] +" character";			  
			 }else{			  
			 	document.getElementById("wordCounterForSearchTerms5").innerHTML = counter['all'] +" characters"; 			  		  
			 } 		   		   		  
		}		
		Countable.live(document.getElementById("searchTerms5"), callbackForSearchTerms5);
		$scope.callbackForSearchTerms5 = callbackForSearchTerms5;
		$scope.getCharactersForSKU = function(item,id){										
			item.character = $("#"+id).val().length;				 								
		};				
		*/
		
		$scope.uploadMainImageFile = function(variationCode,index,region){				
			var fileData = $("#mainImage" + index).prop("files")[0];
			var form_data = new FormData();
			form_data.append("file", fileData);													
			$.ajax({
				url : "${pageContext.request.contextPath}/ProductMarketingMaterial/uploadMainImageFile?region="+region,
				dataType : 'script',
				cache : false,
				contentType : false,
				processData : false,
				data : form_data,
				type : 'POST',
				success : function(data) {
					var result = JSON.parse(data)
					if(result == "fail"){
						$("#MainImageUploadFail"+index).text('<spring:message code="productVersion.fileUploadDuplication" />');	
					}else{
						$("#MainImageUploadFail"+index).text("");
						var idx = null;
						var i;
						for(i = 0; i < $scope.productLineItems.length; i++){															
							if($scope.productLineItems[i]['SKU'] == variationCode){
								idx = i;	
								break;
							} 			
						}						
						$scope.productLineItems[idx]['mainImageLineItems'].push({file : fileData['name']});
						$scope.$apply();																								
					}																			
				}
			});
			
		};
		
		$scope.removeMainImageFile = function(mainImageFile,variationCode,mainImageLineItem,region){							
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/ProductMarketingMaterial/removeMainImageFile',					
				data : {
					mainImageFile : mainImageFile,
					region : region
				},
				success : function(data) {
					var idxOfProductLineItems = null;
					var i;
					for(i = 0; i < $scope.productLineItems.length; i++){															
						if($scope.productLineItems[i]['SKU'] == variationCode){
							idxOfProductLineItems = i;	
							break;
						} 			
					}					
					var idxOfMainImageLineItems = $scope.productLineItems[idxOfProductLineItems]['mainImageLineItems'].indexOf(mainImageLineItem);				
					$scope.productLineItems[idxOfProductLineItems]['mainImageLineItems'].splice(idxOfMainImageLineItems,1);
					$scope.$apply();						
				}									
			});																				
		};
			
		$scope.uploadVariationImageFile = function(variationCode,index,region){				
			var fileData = $("#variationImage" + index).prop("files")[0];
			var form_data = new FormData();
			form_data.append("file", fileData);							
			$.ajax({
				url : "${pageContext.request.contextPath}/ProductMarketingMaterial/uploadVariationImageFile?region="+region,
				dataType : 'script',
				cache : false,
				contentType : false,
				processData : false,
				data : form_data,
				type : 'POST',
				success : function(data) {
					var result = JSON.parse(data)
					if(result == "fail"){
						$("#VariationImageUploadFail"+index).text('<spring:message code="productVersion.fileUploadDuplication" />');	
					}else{
						$("#VariationImageUploadFail"+index).text("");						
						var idx = null;
						var i;
						for(i = 0; i < $scope.productLineItems.length; i++){															
							if($scope.productLineItems[i]['SKU'] == variationCode){
								idx = i;	
								break;
							} 			
						}
						$scope.productLineItems[idx]['variationImageLineItems'].push({file : fileData['name']});
						$scope.$apply();																								
					}																					
				}
			});							
		};
			
		$scope.removeVariationImageFile = function(variationImageFile,variationCode,variationImageLineItem,region){								
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/ProductMarketingMaterial/removeVariationImageFile',
				data : {
					variationImageFile : variationImageFile,
					region : region
				},				
				success : function(data) {
					var idxOfProductLineItems = null;
					var i;
					for(i = 0; i < $scope.productLineItems.length; i++){															
						if($scope.productLineItems[i]['SKU'] == variationCode){
							idxOfProductLineItems = i;	
							break;
						} 			
					}
					var idxOfVariationImageLineItems = $scope.productLineItems[idxOfProductLineItems]['variationImageLineItems'].indexOf(variationImageLineItem);						
					$scope.productLineItems[idxOfProductLineItems]['variationImageLineItems'].splice(idxOfVariationImageLineItems,1);	
					$scope.$apply();						
				}									
			});																								
		};
			
		$scope.uploadOtherImageFile = function(variationCode,index,region){				
			var fileData = $("#otherImage" + index).prop("files")[0];
			var form_data = new FormData();
			form_data.append("file", fileData);
				$.ajax({
					url : "${pageContext.request.contextPath}/ProductMarketingMaterial/uploadOtherImageFile?region="+region,
					dataType : 'script',
					cache : false,
					contentType : false,
					processData : false,
					data : form_data,
					type : 'POST',
					success : function(data) {
						var result = JSON.parse(data)
						if(result == "fail"){
							$("#OtherImageUploadFail"+index).text('<spring:message code="productVersion.fileUploadDuplication" />');	
						}else{
							$("#OtherImageUploadFail"+index).text("");
							var idx = null;
							var i;
							for(i = 0; i < $scope.productLineItems.length; i++){															
								if($scope.productLineItems[i]['SKU'] == variationCode){
									idx = i;	
									break;
								} 			
							}
							$scope.productLineItems[idx]['otherImageLineItems'].push({file : fileData['name']});
							$scope.$apply();																								
						}																			
					}
				});															
			};
			
		$scope.removeOtherImageFile = function(otherImageFile,variationCode,otherImageLineItem,region){			
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/ProductMarketingMaterial/removeOtherImageFile',
				data : {
					otherImageFile : otherImageFile,
					region : region
				},
				success : function(data) {
					var idxOfProductLineItems = null;
					var i;
					for(i = 0; i < $scope.productLineItems.length; i++){															
						if($scope.productLineItems[i]['SKU'] == variationCode){
							idxOfProductLineItems = i;	
							break;
						} 			
					}
					var idxOfOtherImageLineItems = $scope.productLineItems[idxOfProductLineItems]['otherImageLineItems'].indexOf(otherImageLineItem);
					$scope.productLineItems[idxOfProductLineItems]['otherImageLineItems'].splice(idxOfOtherImageLineItems,1);
					$scope.$apply();						
				}									
			});																		
		};
			
		$scope.saveDraftForProductMarketingMaterialSource = function(){			
			
			$scope.description = $('#description').val();					
			$scope.note = $('#note').val();
			var jsonData = {
				name : $scope.productName,
				shortName : $scope.productShortName,
				feature1 : $scope.feature1,
				feature2 : $scope.feature2,
				feature3 : $scope.feature3,
				feature4 : $scope.feature4,
				feature5 : $scope.feature5,
				description : $scope.description,
				searchTerms1 : $scope.searchTerms1,
				searchTerms2 : $scope.searchTerms2,
				searchTerms3 : $scope.searchTerms3,
				searchTerms4 : $scope.searchTerms4,
				searchTerms5 : $scope.searchTerms5,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};				
			var formData = {						
				productBaseCode : $scope.baseProductCode,
				supplierKcode : $scope.supplierKcode,
				mmId : $scope.mmId,
				variationCode : $scope.variationCode,
				jsonData : JSON.stringify(jsonData)
			};											
			$.ajax({
				url : "${pageContext.request.contextPath}/saveDraftForSourceProductMarketingMaterial",
				type : 'POST',
				data : JSON.stringify(formData),
				contentType : 'application/json',
				success : function(data) {						
					window.location.href = "${pageContext.request.contextPath}" + data;

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
			
		$scope.submitProductMarketingMaterialSource = function(){				
			$scope.description = $('#description').val();
			$scope.note = $('#note').val();
			var jsonData = {
				name : $scope.productName,
				shortName : $scope.productShortName,
				feature1 : $scope.feature1,
				feature2 : $scope.feature2,
				feature3 : $scope.feature3,
				feature4 : $scope.feature4,
				feature5 : $scope.feature5,
				description : $scope.description,
				searchTerms1 : $scope.searchTerms1,
				searchTerms2 : $scope.searchTerms2,
				searchTerms3 : $scope.searchTerms3,
				searchTerms4 : $scope.searchTerms4,
				searchTerms5 : $scope.searchTerms5,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};				
			var formData = {						
				productBaseCode : $scope.baseProductCode,
				supplierKcode : $scope.supplierKcode,
				mmId : $scope.mmId,
				variationCode : $scope.variationCode,
				jsonData : JSON.stringify(jsonData)
			};											
			$.ajax({
				url : "${pageContext.request.contextPath}/submitSourceProductMarketingMaterial",
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
			
		$scope.approveProductMarketingMaterialSource = function(){				
			$scope.description = $('#description').val();
			$scope.note = $('#note').val();
			var jsonData = {
				name : $scope.productName,
				shortName : $scope.productShortName,
				feature1 : $scope.feature1,
				feature2 : $scope.feature2,
				feature3 : $scope.feature3,
				feature4 : $scope.feature4,
				feature5 : $scope.feature5,
				description : $scope.description,
				searchTerms1 : $scope.searchTerms1,
				searchTerms2 : $scope.searchTerms2,
				searchTerms3 : $scope.searchTerms3,
				searchTerms4 : $scope.searchTerms4,
				searchTerms5 : $scope.searchTerms5,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};				
			var formData = {						
				productBaseCode : $scope.baseProductCode,
				supplierKcode : $scope.supplierKcode,
				mmId : $scope.mmId,
				variationCode : $scope.variationCode,
				jsonData : JSON.stringify(jsonData)
			};											
			$.ajax({
				url : "${pageContext.request.contextPath}/approveSourceProductMarketingMaterial",
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
			
		
		
		$scope.returnProductMarketingMaterialSource = function(){				
			$scope.description = $('#description').val();
			$scope.note = $('#note').val();								
			var jsonData = {
				name: $scope.productName,
				shortName: $scope.productShortName,
				feature1: $scope.feature1,
				feature2: $scope.feature2,
				feature3: $scope.feature3,
				feature4: $scope.feature4,
				feature5: $scope.feature5,
				description: $scope.description,
				searchTerms1: $scope.searchTerms1,
				searchTerms2: $scope.searchTerms2,
				searchTerms3: $scope.searchTerms3,
				searchTerms4: $scope.searchTerms4,
				searchTerms5: $scope.searchTerms5,
				note: $scope.note,
				products: angular.toJson($scope.productLineItems),
				productWithVariation: $scope.productWithVariation
			};				
			
			var formData = {						
				productBaseCode : $scope.baseProductCode,
				supplierKcode : $scope.supplierKcode,
				mmId : $scope.mmId,
				variationCode : $scope.variationCode,
				jsonData : JSON.stringify(jsonData)
			};			
			
			$.ajax({
				url : "${pageContext.request.contextPath}/returnSourceProductMarketingMaterial",
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
						
		
		$scope.updateProductMarketingMaterialSource = function(){				
			$scope.description = $('#description').val();
			$scope.note = $('#note').val();
			var jsonData = {
				name : $scope.productName,
				shortName : $scope.productShortName,
				feature1 : $scope.feature1,
				feature2 : $scope.feature2,
				feature3 : $scope.feature3,
				feature4 : $scope.feature4,
				feature5 : $scope.feature5,
				description : $scope.description,
				searchTerms1 : $scope.searchTerms1,
				searchTerms2 : $scope.searchTerms2,
				searchTerms3 : $scope.searchTerms3,
				searchTerms4 : $scope.searchTerms4,
				searchTerms5 : $scope.searchTerms5,
				note : $scope.note,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};				
			var formData = {						
				productBaseCode : $scope.baseProductCode,
				supplierKcode : $scope.supplierKcode,
				mmId : $scope.mmId,
				variationCode : $scope.variationCode,
				jsonData : JSON.stringify(jsonData)
			};											
			$.ajax({
				url : "${pageContext.request.contextPath}/updateSourceProductMarketingMaterial",
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
		
		// update marketside marketing material
		$scope.updateProductMarketingMaterialMarketSide = function() {					
			$scope.description = $('#description').val();
			$scope.note = $('#note').val();
			var marketSideRegion = '${marketSideRegion}';								
			var jsonData = {
				name : $scope.productName,
				shortName : $scope.productShortName,
				feature1 : $scope.feature1,
				feature2 : $scope.feature2,
				feature3 : $scope.feature3,
				feature4 : $scope.feature4,
				feature5 : $scope.feature5,
				description : $scope.description,
				searchTerms1 : $scope.searchTerms1,
				searchTerms2 : $scope.searchTerms2,
				searchTerms3 : $scope.searchTerms3,
				searchTerms4 : $scope.searchTerms4,
				searchTerms5 : $scope.searchTerms5,
				note : $scope.note,
				country: marketSideRegion,
				products : angular.toJson($scope.productLineItems),
				productWithVariation : $scope.productWithVariation
			};				
			var formData = {						
				productBaseCode : $scope.baseProductCode,
				supplierKcode : $scope.supplierKcode,
				mmId : $scope.mmId,
				variationCode : $scope.variationCode,
				country: marketSideRegion,
				jsonData : JSON.stringify(jsonData)
			};								
			$.ajax({                                        
				url : "${pageContext.request.contextPath}/updateMarketSideProductMarketingMaterial",
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
			
		$('#sourceCode').click(function() {
			var descriptionContent =  $('#descriptionRow .wysihtml5-sandbox').contents().find('body').html().replace(/\⁺/g,'&#8314;'); 
			alert(descriptionContent);
		});
						
	});
	
</script>


<style type="text/css">

#o-wrapper{
	background-color: #eeeeee;
}

#app{
	padding-top: 15px;
	background-color: white;

}


.productname{
	font-size: 2rem;
}

.title-button-row{
	float: right;
	margin-top: 20px;
}

/* apply other regions button */
.c-buttons{
	margin-top: 15px;
	margin-right: 10px;
}

/*===================================================================== */

.form-title{
	font-weight: bold;	
	
}


#shortNameTitle{
	font-size: 2rem;
	margin-top: 0;

}


/*===================================================================== */

.upload-pic-btn{
	margin-right: 0 !important;
	margin-left: 1rem;
	height: 34px;
}


.img-input{
	height: 34px;
}


.uploaded-pic-item{
	margin-top: 10px;

}

/* ===================================================================== */

#noteRow{
	margin-bottom: 20px;
}


#endPageButtonRow{
	margin-top: 10px;
	padding-bottom: 45px;
}


</style>

</head>


<div id="o-wrapper" class="o-wrapper">


<div id="app" class="max-width" ng-app="productMarketingMaterialSourceVersion" ng-controller="productMarketingMaterialSourceVersionCtrl">

<div class="row">


<div class="col-md-6">
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
</div>

<div class="col-md-6">

	<div class="title-button-row">
		
		<!-- show help for supplier -->
		<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
			<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
				<span class="fas fa-question-circle"></span>	
				<spring:message code="detailsOfBaseProduct.help" />								   						
			</a>  				
		</sec:authorize>
 			
		<c:choose>
		
			<c:when test="${type eq 'source'}">
				
				<!-- back  -->				
				<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?
					supplierKcode=${supplierKcode}&prevSkuCode=${variationCode}&prevType=core">
					<spring:message code="detailsOfBaseProduct.back" />
				</a>																	
				
					
				<!-- supplier view -->
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
					
					<!-- save draft -->
					<input type="button" class="btn btn-primary" ng-click="saveDraftForProductMarketingMaterialSource()"
					value="<spring:message code="detailsOfBaseProduct.saveDraft" />" />
					
					<!-- submit -->								
					<button class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="submitProductMarketingMaterialSource()">
						<spring:message code="detailsOfBaseProduct.submit" />  <i class="fas fa-forward"></i>
					</button>		
																
				</sec:authorize>
				
				<!-- drs view -->
				<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
					
					<!-- save -->
					<input type="button" class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" 
					ng-click="updateProductMarketingMaterialSource()" value="<spring:message code="detailsOfBaseProduct.update" />" />	
								
					<!-- return -->							
					<button class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="returnProductMarketingMaterialSource()">
						<i class="fas fa-backward"></i>  <spring:message code="detailsOfBaseProduct.return" />
					</button>			
								
					<!-- approve -->																						
					<button  class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="approveProductMarketingMaterialSource()">
						<spring:message code="detailsOfBaseProduct.approve" />  <i class="fas fa-forward"></i>
					</button>																	
				</sec:authorize>
					
			</c:when>
			
			<c:otherwise>
					
					
					<!-- back  -->				
					<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?
						supplierKcode=${supplierKcode}&prevSkuCode=${variationCode}&prevType=${marketSideRegion}">
						<spring:message code="detailsOfBaseProduct.back" />
					</a>							
							
					<!-- submit  -->	
					<input type="button" class="btn btn-primary" ng-click="updateProductMarketingMaterialMarketSide()"
					value="<spring:message code="productMarketingMaterial.submit" />" />		
														
			</c:otherwise>
			
		</c:choose>		
		
		
		
	</div>		

</div>
</div>

<div class="row">

	<div class="col-md-6">
	

		<h3>
			${variationCode}
			
			<c:choose>
				
				<c:when test="${type eq 'source'}">
					Core Information
				</c:when>
				
				<c:otherwise>
					${marketSideRegion}
				</c:otherwise>
			</c:choose>				
			
		</h3>
	
	</div>
		
	<div class="col-md-6">	

		<!-- apply other region -->
		<div class="apply-other-regions-section row">
			
			<div class="col-md-12">
			
				<div class="c-buttons pull-right" style="color:gray;">
				<i class="fas fa-link" aria-hidden="true"></i>
				<spring:message code="productMarketingMaterial.applyOtherRegion" />		
				
				<c:choose>
					<c:when test="${type eq 'source'}">
						<c:forEach items="${marketSideList}" var="marketSide">
		 					<button id="c-button--push-right-${marketSide}" class="c-button" data-src="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}/${marketSide}?supplierKcode=${supplierKcode}"> ${marketSide} </button>
						</c:forEach>
					</c:when>
					
					<c:otherwise>
		 				<button id="c-button--push-right-source" class="c-button" data-src="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}?supplierKcode=${supplierKcode}"> 
		 					<spring:message code="detailsOfBaseProduct.productGeneralInformation" /> 
		 				</button>		
						<c:forEach items="${marketSideList}" var="marketSide">
							<c:if test="${marketSide ne marketSideRegion}">
								<button id="c-button--push-right-${marketSide}" class="c-button" data-src="${pageContext.request.contextPath}/pmm/${baseProductCode}/${variationCode}/${marketSide}?supplierKcode=${supplierKcode}"> ${marketSide} </button>
							</c:if>
						</c:forEach>	
					</c:otherwise>
				</c:choose>	
			
			</div>

	</div>
	
	
	</div>

</div>	
</div>	
	
	
<!-- form start -->	
<form>	

	<!-- title section -->
		<div class="form-group">
			<h3><label class="form-title"><spring:message code="productMarketingMaterial.variationProductName" /></label></h3>
			<input id="productName" class="form-control" ng-model="productName">
		<small class="form-text text-muted"><span><spring:message code="productMarketingMaterial.50Title" /></span></small>
		</div>
		
	
	<c:if test="${type ne 'source'}">
		<div class="form-group">
		<h3 class="form-title"><label><spring:message code="productMarketingMaterial.productShortName" /></label></h3>
		<input id="productShortName" class="form-control" ng-model="productShortName">
		<small class="form-text text-muted"><span><spring:message code="productMarketingMaterial.50Title" /></span></small>
		</div>
	</c:if>	
	
	
	<!-- Features -->
	<div class="row">
		<div class="col-md-12">
			<h3 class="form-title">
				<spring:message code="productMarketingMaterial.productFeaturesTitle" />
			</h3>
			<span><spring:message code="productMarketingMaterial.100Feature" /></span>	
		</div>
	</div>
	
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">1</label>
	<div class="col-sm-11">
	<input id="feature1" class="form-control" ng-model="feature1">
						<div class="form-text text-muted"><span id="wordCounterForFeature1"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">2</label>
	<div class="col-sm-11">
	<input id="feature2" class="form-control" ng-model="feature2">
						<div class="form-text text-muted"><span id="wordCounterForFeature2"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">3</label>
	<div class="col-sm-11">
	<input id="feature3" class="form-control" ng-model="feature3">
						<div class="form-text text-muted"><span id="wordCounterForFeature3"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">4</label>
	<div class="col-sm-11">
		<input id="feature4" class="form-control" ng-model="feature4">
						<div class="form-text text-muted"><span id="wordCounterForFeature4"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">5</label>
	<div class="col-sm-11">
	<input id="feature5" class="form-control" ng-model="feature5">
						<div class="form-text text-muted"><span id="wordCounterForFeature5"></span></div>
	</div>
	</div>
	
			
	
	<!-- Description -->
	<div class="row">
		<div class="col-md-12">
			<h3 class="form-title">
				<spring:message code="productMarketingMaterial.productDescriptionTitle" />
			</h3>
		</div>
	</div>
	
	<div id="descriptionRow" class="row">
		<div class="col-md-12">
			<textarea id="description" class="form-control" style="height:400px;" ng-model="description">
			${desc}
			</textarea>
		
			<div id ="wordCounterForDescription">0 characters</div>
			<span><spring:message code="productMarketingMaterial.2000Description" /></span>			
			
		</div>
	</div>
	</br>
	
	
	
	<!-- Search Terms -->
	<div class="row">
		<div class="col-md-12">
			<h3 class="form-title">
				<spring:message code="productMarketingMaterial.searchTermsTitle" />
			</h3>	
		</div>
	</div>
	
	
		<div class="form-row">
	<label class="col-sm-1 col-form-label">1</label>
	<div class="col-sm-11">
	<input id="searchTerms1" class="form-control" ng-model="searchTerms1">
						<div class="form-text form-muted"><span id="wordCounterForSearchTerms1"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">2</label>
	<div class="col-sm-11">
	<input id="searchTerms2" class="form-control" ng-model="searchTerms2">
						<div class="form-text form-muted"><span id="wordCounterForSearchTerms2"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">3</label>
	<div class="col-sm-11">
<input id="searchTerms3" class="form-control" ng-model="searchTerms3">
						<div class="form-text form-muted"><span id="wordCounterForSearchTerms3"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">4</label>
	<div class="col-sm-11">
			<input id="searchTerms4" class="form-control" ng-model="searchTerms4">
						<div class="form-text form-muted"><span id="wordCounterForSearchTerms4"></span></div>
	</div>
	</div>
	
	<div class="form-row">
	<label class="col-sm-1 col-form-label">5</label>
	<div class="col-sm-11">
	<input id="searchTerms5" class="form-control" ng-model="searchTerms5">
						<div class="form-text form-muted"><span id="wordCounterForSearchTerms5"></span></div>
	</div>
	</div>

	
	<!-- picture upload section -->
	<div class="row">
		<div class="col-md-12">
			<h3 class="form-title">
				<spring:message code="productMarketingMaterial.pictureOfVariationTitle" />				
			</h3>
		</div>
	</div>	
	
	<div id="pictureUploadRow" class="row">

            <!-- main image col -->
            <div class="col-md-4">
            
				<h4><spring:message code="productMarketingMaterial.mainImages" /></h4>
				
				<div id="fileArea">
					<div class="input-group">
					
						<!-- choose file -->
						<input id="mainImage{{$index+1}}Text" class="form-control img-input" placeholder="請選擇欲上傳的檔案" disabled/>
						<div class="fileUpload btn btn-primary input-group-addon" style="margin:0px">
  								選擇
  								<input id="mainImage{{$index+1}}" type="file" name="files" accept="image/*" class="upload" onchange="selectMainImageFile(this)" />
						</div>
						
						<!-- upload btn -->
						<button type="button" class="btn btn-primary upload-pic-btn"
							ng-click="uploadMainImageFile(variationCode,$index+1,'${region}')">
							<span><spring:message code="productVersion.Upload" /></span>
						</button>
						
					</div>
				</div>
				
				<div id="MainImageUploadFail{{$index+1}}" class="text-danger"></div>				
				<div ng-repeat="mainImageLineItem in productLineItems[idx].mainImageLineItems" class="input-group uploaded-pic-item">
				  	<div class="form-control">
						 
						<a href="${pageContext.request.contextPath}/MainImageFile?fileName={{mainImageLineItem.file}}&region=${region}">{{mainImageLineItem.file}}</a>
						
				  	</div>
				  	<div class="input-group-addon">
				  		<button type="button" class="btn btn-link"
								ng-click="removeMainImageFile(mainImageLineItem.file,variationCode,mainImageLineItem,'${region}')">
								<span class="fas fa-trash fa-lg"></span>
						</button>
				  	</div>
				</div>
		
            </div>

            <!-- swatch image col -->
            <div class="col-md-4">
              <h4><spring:message code="productMarketingMaterial.variationImages" /></h4>
              
              
             	<div id="fileArea">
					<div class="input-group">
					
						<!-- choose file -->
						<input id="variationImage{{$index+1}}Text" class="form-control img-input" placeholder="請選擇欲上傳的檔案" disabled/>
						<div class="fileUpload btn btn-primary input-group-addon" style="margin:0px">
  								選擇
  								<input id="variationImage{{$index+1}}" type="file" name="files" accept="image/*" 
  									class="upload" onchange="selectVariationImageFile(this)" />
						</div>
						
						<!-- upload btn -->
						<button type="button" class="btn btn-primary upload-pic-btn"
							ng-click="uploadVariationImageFile(variationCode,$index+1,'${region}')">
							<span><spring:message code="productVersion.Upload" /></span>
						</button>
						
					</div>
				</div>
				
				<div id="VariationImageUploadFail{{$index+1}}" class="text-danger"></div>								
				<div ng-repeat="variationImageLineItem in productLineItems[idx].variationImageLineItems" class="input-group uploaded-pic-item">
				  <div class="form-control">
				  		<a href="${pageContext.request.contextPath}/VariationImageFile?fileName={{variationImageLineItem.file}}&region=${region}">{{variationImageLineItem.file}}</a>						  
				  </div>
				  <div class="input-group-addon">
				  		<button type="button" class="btn btn-link"
								ng-click="removeVariationImageFile(variationImageLineItem.file,variationCode,variationImageLineItem,'${region}')">
								<span class="fas fa-trash fa-lg"></span>
						</button>
				  </div>				
				</div>

            </div>
                        
            <div class="col-md-4">
              <h4><spring:message code="productMarketingMaterial.otherImages" /></h4>

				<div id="fileArea">
					<div class="input-group">
					
						<!-- choose file -->
						<input id="otherImage{{$index+1}}Text" class="form-control img-input" placeholder="請選擇欲上傳的檔案" disabled/>
						<div class="fileUpload btn btn-primary input-group-addon" style="margin:0px">
  								選擇
  								<input id="otherImage{{$index+1}}" type="file" name="files" accept="image/*" 
  									class="upload" onchange="selectOtherImageFile(this)" />
						</div>
						
						<!-- upload btn -->
						<button type="button" class="btn btn-primary upload-pic-btn"
							ng-click="uploadOtherImageFile(variationCode,$index+1,'${region}')">
							<span><spring:message code="productVersion.Upload" /></span>
						</button>
						
					</div>
				</div>
								
				<div id="OtherImageUploadFail{{$index+1}}" class="text-danger"></div>
				<div ng-repeat="otherImageLineItem in productLineItems[idx].otherImageLineItems" class="input-group uploaded-pic-item">
				  <div class="form-control">
				  		<a href="${pageContext.request.contextPath}/OtherImageFile?fileName={{otherImageLineItem.file}}&region=${region}">{{otherImageLineItem.file}}</a>	
							
				  </div>
				  <div class="input-group-addon">
				  		<button type="button" class="btn btn-link"
								ng-click="removeOtherImageFile(otherImageLineItem.file,variationCode,otherImageLineItem,'${region}')">
								<span class="fas fa-trash fa-lg"></span>
						</button>
				  </div>				
				</div>
				
            </div>
          </div> <!-- picture upload container -->
    </br>
	
	
	
	
	<!-- Notes Section -->
	<div id="noteSection">
	
		<div class="row">
			<div class="col-md-12">
				<h3 class="form-title">
					<spring:message code="productMarketingMaterial.note" />
				</h3>
			</div>
		</div>			
		
		<div id="noteRow" class="row">
			<div class="col-md-12">
				<textarea id="note" class="form-control" rows="10" ng-model="note"></textarea>
			</div>
		</div>	
	
	</div>

	
		
	
	<!-- End page button row -->
	<div id="endPageButtonRow" class="row">
	
		<div class="col-md-12 text-right">
		
		
			<!-- show help for supplier -->
			<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
  				<a style="font-size:13pt;" href="<c:url value="/resources/files/HelpAboutProductOnboarding.pdf"/>" target="_blank">
					<i class="fas fa-question-circle"></i>	
					<spring:message code="detailsOfBaseProduct.help" />								   						
				</a>  				
  			</sec:authorize>
  			
			<c:choose>
			
				<c:when test="${type eq 'source'}">
					
					<!-- back  -->				
					<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?
						supplierKcode=${supplierKcode}&prevSkuCode=${variationCode}&prevType=core">
						<spring:message code="detailsOfBaseProduct.back" />
					</a>																	
					
						
					<!-- supplier view -->
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_SUPPLIER']}')">
						
						<!-- save draft -->
						<input type="button" class="btn btn-primary" ng-click="saveDraftForProductMarketingMaterialSource()"
						value="<spring:message code="detailsOfBaseProduct.saveDraft" />" />
						
						<!-- submit -->								
						<button class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-click="submitProductMarketingMaterialSource()">
							<spring:message code="detailsOfBaseProduct.submit" />  <i class="fas fa-forward"></i>
						</button>		
																	
					</sec:authorize>
					
					<!-- drs view -->
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">
						
						<!-- save -->
						<input type="button" class="btn btn-primary" ng-show="status =='Pending DRS review' || status =='Finalized'" 
						ng-click="updateProductMarketingMaterialSource()" value="<spring:message code="detailsOfBaseProduct.update" />" />	
									
						<!-- return -->							
						<button class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="returnProductMarketingMaterialSource()">
							<i class="fas fa-backward"></i>  <spring:message code="detailsOfBaseProduct.return" />
						</button>			
									
						<!-- approve -->																						
						<button  class="btn btn-default" style="border-bottom: 2px #3d85c6 solid;" ng-show="status =='Pending DRS review'" ng-click="approveProductMarketingMaterialSource()">
							<spring:message code="detailsOfBaseProduct.approve" />  <i class="fas fa-forward"></i>
						</button>																	
					</sec:authorize>
						
				</c:when>
				
				<c:otherwise>
						
						
						<!-- back  -->				
						<a class="btn btn-success" href="${pageContext.request.contextPath}/pmmi/${baseProductCode}?
							supplierKcode=${supplierKcode}&prevSkuCode=${variationCode}&prevType=${marketSideRegion}">
							<spring:message code="detailsOfBaseProduct.back" />
						</a>							
								
						<!-- submit  -->	
						<input type="button" class="btn btn-primary" ng-click="updateProductMarketingMaterialMarketSide()"
						value="<spring:message code="productMarketingMaterial.submit" />" />		
															
				</c:otherwise>
				
			</c:choose>		
			
		</div>
	</div>
		
	</form>

</div>




</div><!-- o-wrapper -->



<!-- wysihtml5 script -->			
<script type="text/javascript">
	$('#note').wysihtml5({
		"events": {
		"load": function() {
		            $("#noteRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");			        	               
		 }
		 },toolbar: {		   
		 	"font-styles": false, //Font styling, e.g. h1, h2, etc. Default true
			"lists": false, //(Un)ordered lists, e.g. Bullets, Numbers. Default true
			"html": true, //Button which allows you to edit the generated HTML. Default false
			"link": true, //Button to insert a link. Default true
			"image": false, //Button to insert an image. Default true,
   			"blockquote": false, //Blockquote 		   
   			"emphasis": false, // Italics, bold, etc. 	
   		    "fa": true
		}
	});				
</script>	

<div id="c-mask" class="c-mask"></div><!-- /c-mask -->


<nav id="c-menu--push-right" class="c-menu c-menu--push-right">
	<button class="c-menu__close" onclick="copyContent()">Copy</button> 
  	<iframe id="refContent" style="border:none;height:100%;width:100%;display:none;"></iframe>
</nav><!-- /c-menu push-right -->


<script type="text/javascript">

$('#description').wysihtml5({
	"events": {
    "load": function() {
     $("#descriptionRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");
     	var descriptionContent =  $("#descriptionRow .wysihtml5-sandbox").contents().find('body').html().replace(/\⁺/g,'&#8314;');                  
        if(descriptionContent.length == 1){ 			  
       		$("#wordCounterForDescription").html(descriptionContent.length +" character"); 
        }else{			  
        	$("#wordCounterForDescription").html(descriptionContent.length +" characters"); 
        }
    }
    },toolbar: {		   
		"font-styles": false, //Font styling, e.g. h1, h2, etc. Default true
		"lists": false, //(Un)ordered lists, e.g. Bullets, Numbers. Default true
		"html": true, //Button which allows you to edit the generated HTML. Default false
		"link": false, //Button to insert a link. Default true
		"image": false, //Button to insert an image. Default true,
	    "blockquote": false, //Blockquote 
	    "fa": true,
	}
});
$( "a[data-wysihtml5-command='italic']" ).hide();
$( "a[data-wysihtml5-command='underline']" ).hide();
$( "a[data-wysihtml5-command='small']" ).hide();
$('#descriptionRow .wysihtml5-sandbox').contents().find('body').on("change keyup paste blur click",function() {
	var descriptionContent =  $('#descriptionRow .wysihtml5-sandbox').contents().find('body').html().replace(/\⁺/g,'&#8314;');
	if(descriptionContent.length == 1){ 			  
		$("#wordCounterForDescription").html(descriptionContent.length +" character"); 
    }else{			  
	    $("#wordCounterForDescription").html(descriptionContent.length +" characters"); 
    } 
});
$('#descriptionRow .wysihtml5-sandbox').on("load",function(e){	
	$('#descriptionRow .wysihtml5-sandbox').contents().find('body').on("change keyup paste blur click",function() {
		var descriptionContent =  $("#descriptionRow .wysihtml5-sandbox").contents().find('body').html().replace(/\⁺/g,'&#8314;');	
		if(descriptionContent.length == 1){ 			  
			$("#wordCounterForDescription").html(descriptionContent.length +" character"); 
    	}else{			  
	    	$("#wordCounterForDescription").html(descriptionContent.length +" characters"); 
    	} 
	});
});

</script>



<!-- modal for apply other region -->
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

