<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>

<title>
	<spring:message code="ccc.title"/> : ${CustomerCareCase.caseId} - DRS
</title>

<style type="text/css">
.removePseudoElements:before{
	content: none;
}

.form-control{
	border-raius: 0px;
}

html {
	font-size:14px !important;
}

</style>

<link href="<c:url value="/resources/css/bootstrap3-glyphicons.css"/>" type="text/css" rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap3-wysihtml5.css"/>" type="text/css" rel="stylesheet">

<script type='text/javascript' src="<c:url value="/resources/js/bootstrap3-wysihtml5.all.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jquery-ui-timepicker-addon.css"/>"></link>
<script type='text/javascript' src="<c:url value="/resources/js/jquery-ui-timepicker-addon.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/date.format.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/Countable.js"/>"></script>	
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script src="https://code.jquery.com/jquery-migrate-3.0.0.min.js"></script>

<script>
	$(function() {
		$.widget("ui.tooltip", $.ui.tooltip, {
    		options: {
            	content: function () {
                	return $(this).prop('title');
            	}
        	}
    	});	
		$("#noteNoOfBillableWords").tooltip({
			show: null,
			position: { 
				my: "left-60 top-150", 
				at: "right center" 
			}
		});	
		$("#noteNoOfExtraActions").tooltip({
			show: null,
			position: { 
				my: "left-60 top-200", 
			   	at: "right center" 
			}
		});	
	});

	function deleteConfirm(caseId) {
		$("#dialog-confirm").html("<spring:message code="ccc.deleteCaseWarning" /> ");
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [{
				text : "<spring:message code='ccc.yes' /> ",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/CustomerCareCases/" + caseId +"/delete";
				}
			}, {
				text : "<spring:message code='ccc.no' /> ",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});			
	}

	function deleteMessageConfirm(caseId,lineSeq){		
		$("#dialog-message-confirm").html("<spring:message code="ccc.deleteMsgWarning" /> ");		
		$("#dialog-message-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [{
				text : "<spring:message code='ccc.yes' /> ",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/CustomerCareCases/" + caseId +"/"+ lineSeq + "/deleteMsg";
				}
			}, {
				text : "<spring:message code='ccc.no' /> ",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});		
	}
		
	var app = angular.module('message', []);
	app.controller('messageCtrl', function($scope) {				
		var area = document.getElementById('contents');
		$scope.contents = "";		 		
		function callback (counter) { 		   
 			if(counter['words'] <= 1){ 			  
 				document.getElementById("wordCounter").innerHTML = counter['words'] +" word"; 			 
 		  	}else{ 			  
 			 	document.getElementById("wordCounter").innerHTML = counter['words'] +" words"; 			 
 		  	} 		   		   		  
		}		
		Countable.live(area, callback);		
		$('#contentRow .wysihtml5-sandbox').contents().find('body').on("change keyup paste blur click",function() {	    	
	    	var descriptionContent =  $("#contentRow .wysihtml5-sandbox").contents().find('.contents').html(); 	    	    	
	    	$('#contents').val(descriptionContent);	    	    	
	    	var area = document.getElementById('contents');			
			function callback (counter) {	 		   
	 			if(counter['words'] <= 1){ 			  
	 				document.getElementById("wordCounter").innerHTML = counter['words'] +" word";			  
	 			 	$scope.contents = "";
	 			 	$scope.$apply();	 			 
	 		  	}else{ 			  
	 			 	document.getElementById("wordCounter").innerHTML = counter['words'] +" words"; 			  		  
	 			 	$scope.contents = descriptionContent;
	 			 	$scope.$apply();	 			 		  
	 		  	}	 		   		   		  
			}			
			Countable.live(area, callback);	    	
	    });	    
	    $('#contentRow .wysihtml5-sandbox').on('load', function(e){	
	    	$('#contentRow .wysihtml5-sandbox').contents().find('body').on("change keyup paste blur click",function() {				
	    		var descriptionContent =  $("#contentRow .wysihtml5-sandbox").contents().find('.contents').html(); 	    	    	
	        	$('#contents').val(descriptionContent);	        	    	
	        	var area = document.getElementById('contents');	    		
	    		function callback (counter) {	     		  
	     			if(counter['words'] <= 1){ 			  
	     			 	document.getElementById("wordCounter").innerHTML = counter['words'] +" word";			  
	     			 	$scope.contents = "";
	     			 	$scope.$apply();	     			     	 		  
	     		  	}else{ 			  
	     			 	document.getElementById("wordCounter").innerHTML = counter['words'] +" words"; 			  		  
	     			 	$scope.contents = descriptionContent;
	     			 	$scope.$apply();	
	     		  	}	     		   		   		  
	    		}	    		
	    		Countable.live(area, callback);	        		   		
	    	});
	    });		
		getIssues();
		$scope.issue="";
		$scope.responseTemplate="";
		$scope.CustomerType = [{customer : ''}];
		$scope.MSDCType = [];
		
		function emptyList(id) {
			document.getElementById(id).options.length = 0;
		}
		
		function getIssues(){
			var categoryId = Number($('#categoryName').val());			
			var typeId = Number($('#issueType').val());						
			var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssues/';
			var issues = null;			
			$.ajax({
				type : 'get',			
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					categoryId : categoryId,
					typeId : typeId				
				},
				dataType : "json",
				success : function(data) {					
					issues = data;					
					emptyList("issue");										
					if(Object.keys(issues).length > 0){										
						var selectIssue = document.getElementById("issue");
						var optIssue = document.createElement("option");
						optIssue.value = "";
						optIssue.textContent = "---Select---";
						selectIssue.appendChild(optIssue);																			
					}					
					for ( var key in issues) {					
						optIssue = document.createElement("option");
						optIssue.value = key;
						optIssue.textContent = issues[key];
						selectIssue.appendChild(optIssue);											
					}														
					emptyList("otherTemplate");															
					$scope.issue="";
					$scope.responseTemplate="";					
					$scope.Message.issue.$$lastCommittedViewValue="";
					$scope.Message.issue.$$rawModelValue="";
					$scope.Message.issue.$modelValue="";
					$scope.Message.issue.$viewValue="";					
					$scope.Message.responseTemplate.$$lastCommittedViewValue="";
					$scope.Message.responseTemplate.$$rawModelValue="";
					$scope.Message.responseTemplate.$modelValue="";
					$scope.Message.responseTemplate.$viewValue="";																									
				},
				error: function(jqXHR, textStatus, errorThrown) 
				{
					emptyList("issue");
					emptyList("otherTemplate");										
					$scope.issue="";
					$scope.responseTemplate="";					
					$scope.Message.issue.$$lastCommittedViewValue="";
					$scope.Message.issue.$$rawModelValue="";
					$scope.Message.issue.$modelValue="";
					$scope.Message.issue.$viewValue="";					
					$scope.Message.responseTemplate.$$lastCommittedViewValue="";
					$scope.Message.responseTemplate.$$rawModelValue="";
					$scope.Message.responseTemplate.$modelValue="";
					$scope.Message.responseTemplate.$viewValue="";																			
				}							
			});
		}
		
		$scope.checkMessageType = function() {			
			var checkedValue = null;			
			if (document.getElementById('CustomerType').checked) {
				checkedValue = document.getElementById('CustomerType').value;
			}			
			if (document.getElementById('MSDCType').checked) {
				checkedValue = document.getElementById('MSDCType').value;
			}												
			$('#startDate').val('');
			$('#endDate').val('');
			$('#timeTaken').val('');
			$('#chargeToSKU').val('');
			$('#addToSettleableTransaction').prop('checked', false);			
			$('#wordCount').val('');
			$('#standardActionCount').val('');
			$('#DRSChargeByWord').val('');
			$('#costPerHour').val('');
			if (checkedValue == "customer_msg") {
				$scope.CustomerType = [{customer : ''}];
				$scope.MSDCType = [];				
				$("#insertTemplateArea").hide();				
			} else {				
				$scope.CustomerType = [];
				$scope.MSDCType = [{MSDC : ''}];				
				$("#insertTemplateArea").show();
			}			
		}
						
		$scope.getTimeNow = function(target) {						
			var now = new Date();									
			$('#'+target).val(dateFormat(now, "yyyy-mm-dd HH:MM:ss o"));			
			$scope.getTimeTaken();			
		};
						
		function msToTime(s) {		 			
			function addZ(n) {
				return (n<10? '0':'') + n;
			}
			var ms = s % 1000;
			s = (s - ms) / 1000;
			var secs = s % 60;
			s = (s - secs) / 60;
			var mins = s % 60;
			var hrs = (s - mins) / 60;
			return addZ(hrs) + ':' + addZ(mins) + ':' + addZ(secs);		
		}
				
		$scope.getTimeTaken = function(){			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();								     			
			var startDateFormat = startDate.replace(/-/g,"/");   
			var endDateFormat = endDate.replace(/-/g,"/");								
		    if(startDateFormat != "" && endDateFormat != ""){		    	
		    	var startDateTime = new Date(startDateFormat);		    	
		    	var endDateTime = new Date(endDateFormat);		    	
		    	var duration = endDateTime - startDateTime;		    				    	
		    	var time = msToTime(duration);		    			    			    	
		    	$scope.timeTaken = time;		    	
		    	$scope.getDrsChargeByWord();		    			    	
		    }						
		};
		
		$scope.getDrsChargeByWord = function (){			
			var feeForWordCount = 0.07;
			if( $scope.MSDCType[0]["translationFeeIncluded"] == true) feeForWordCount = 0.14;
			var wordCount = Number($scope.MSDCType[0]["wordCount"]);
			var standardActionCount = Number($scope.MSDCType[0]["standardActionCount"]);
			var DRSChargeByWord = wordCount*feeForWordCount+standardActionCount*1.4;
			$scope.MSDCType[0]["DRSChargeByWord"] = Math.round(Number(DRSChargeByWord) * Math.pow(10, 2)) / Math.pow(10, 2);
			$scope.getCostPerHour();						
		};
		
		$scope.getCostPerHour = function (){			
			var costPerHour = ($scope.MSDCType[0]["DRSChargeByWord"]/$scope.MSDCType[0]["timeTaken"])*60;			
			$scope.MSDCType[0]["costPerHour"] = Math.round(Number(costPerHour) * Math.pow(10, 2)) / Math.pow(10, 2);			
		};
		
		$scope.insertMessage = function (id){			
			var templateId = $('#'+id).val();
			var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getTemplateContent/';
			var templateContent = null;
			$('#contents').val("");
			$("#responseTemplateId").val("");	        			
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : { templateId : templateId },
				dataType : "json",
				success : function(data) {					
					templateContent = data;					
					var cursorPos = $(".wysihtml5-sandbox").contents().find('.contents').prop('selectionStart');
					var currentContents = $(".wysihtml5-sandbox").contents().find('.contents').html();
					var textBefore = currentContents.substring(0,  cursorPos );
			        var textAfter  = currentContents.substring( cursorPos, currentContents.length );			       
			        $('#contents').val( textBefore+ templateContent);			        
			        $(".wysihtml5-sandbox").contents().find('.contents').html(textBefore+ templateContent);			        
			        $("#responseTemplateId").val($("#"+id).val());			        
			        var area = document.getElementById('contents');					
			        function callback (counter) {			         		   
			        	if(counter['words'] <= 1){			         				  
			        		document.getElementById("wordCounter").innerHTML = counter['words'] +" word";			         			  
			        	}else{			         			  
			         		document.getElementById("wordCounter").innerHTML = counter['words'] +" words";			         			  		  
			        	}			         		   		   		  
			        }			        		
			        Countable.live(area, callback);			        																																												
				}											
			});						
		};
															
		$scope.getIssueTypeList = function(scope) {		    
			var categoryId = Number($('#categoryName').val());
			var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssueTypeList/';
			var issueTypeList = null;			
			$.ajax({	
				type : 'get',			
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : { categoryId : categoryId },
				dataType : "json",
				success : function(data) {					
					issueTypeList = data;										
					emptyList("issueType");					
					var selectIssueType = document.getElementById("issueType");
					var optIssueType = document.createElement("option");
					optIssueType.value = "";
					optIssueType.textContent = "<spring:message code='issue.ALL_TYPE' />";
					selectIssueType.appendChild(optIssueType);						
					for ( var issueType in issueTypeList) {
						if (issueTypeList.hasOwnProperty(issueType)) {					
							optIssueType = document.createElement("option");
							optIssueType.value = issueType;
							optIssueType.textContent = issueTypeList[issueType];
							selectIssueType.appendChild(optIssueType);
						}					
					}									
					scope.getIssues(scope);															
				},
				error: function(jqXHR, textStatus, errorThrown) 
				{										
					console.log(jqXHR);
					console.log(textStatus);
					console.log(errorThrown);									
				}					
			});			
		};
				
		$scope.getIssues = function (scope){			
			var categoryId = Number($('#categoryName').val());			
			var typeId = Number($('#issueType').val());						
			var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssues/';
			var issues = null;			
			$.ajax({
				type : 'get',			
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					categoryId : categoryId,
					typeId : typeId				
				},
				dataType : "json",
				success : function(data) {					
					issues = data;					
					emptyList("issue");										
					if(Object.keys(issues).length > 0){										
						var selectIssue = document.getElementById("issue");
						var optIssue = document.createElement("option");
						optIssue.value = "";
						optIssue.textContent = "---Select---";
						selectIssue.appendChild(optIssue);																			
					}					
					for ( var key in issues) {					
						optIssue = document.createElement("option");
						optIssue.value = key;
						optIssue.textContent = issues[key];
						selectIssue.appendChild(optIssue);											
					}														
					emptyList("otherTemplate");					
					scope.issue="";
					scope.responseTemplate="";					
					scope.Message.issue.$$lastCommittedViewValue="";
					scope.Message.issue.$$rawModelValue="";
					scope.Message.issue.$modelValue="";
					scope.Message.issue.$viewValue="";					
					scope.Message.responseTemplate.$$lastCommittedViewValue="";
					scope.Message.responseTemplate.$$rawModelValue="";
					scope.Message.responseTemplate.$modelValue="";
					scope.Message.responseTemplate.$viewValue="";																									
				},
				error: function(jqXHR, textStatus, errorThrown) 
				{					
					emptyList("issue");
					emptyList("otherTemplate");					
					scope.issue="";
					scope.responseTemplate="";					
					scope.Message.issue.$$lastCommittedViewValue="";
					scope.Message.issue.$$rawModelValue="";
					scope.Message.issue.$modelValue="";
					scope.Message.issue.$viewValue="";					
					scope.Message.responseTemplate.$$lastCommittedViewValue="";
					scope.Message.responseTemplate.$$rawModelValue="";
					scope.Message.responseTemplate.$modelValue="";
					scope.Message.responseTemplate.$viewValue="";																			
				}							
			});						
		};
			
		$scope.getTemplates = function (scope){			
			var issueId = $('#issue').val();			 
			var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getTemplates/';
			var templates = null;						
			$.ajax({
				type : 'get',				
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : { issueId : issueId },
				dataType : "json",
				success : function(data) {					
					templates = data;					
					emptyList("otherTemplate");										
					if(Object.keys(templates).length > 0){						
						var selectTemplate = document.getElementById("otherTemplate");
						var optTemplate = document.createElement("option");
						optTemplate.value = "";
						optTemplate.textContent = "---Select---";
						selectTemplate.appendChild(optTemplate);																			
					}					
					for ( var key in templates) {						
						optTemplate = document.createElement("option");
						optTemplate.value = key;
						optTemplate.textContent = templates[key];
						selectTemplate.appendChild(optTemplate);											
					}															
					scope.responseTemplate="";					
					scope.Message.responseTemplate.$$lastCommittedViewValue="";
					scope.Message.responseTemplate.$$rawModelValue="";
					scope.Message.responseTemplate.$modelValue="";
					scope.Message.responseTemplate.$viewValue="";																																				
				},
				error: function(jqXHR, textStatus, errorThrown) 
				{										
					emptyList("otherTemplate");										
					scope.responseTemplate="";					
					scope.Message.responseTemplate.$$lastCommittedViewValue="";
					scope.Message.responseTemplate.$$rawModelValue="";
					scope.Message.responseTemplate.$modelValue="";
					scope.Message.responseTemplate.$viewValue="";										
				}											
			});						
		};										
	});			
</script>
</head>
<div class="max-width" ng-app="message">
	<div class="text-center" style="color: #FF0000">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid">

		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="ccc.title"/></div>
			</div>	

		</div>
		<c:url var="updateAction" value="/CustomerCareCases/updateStatus"></c:url>
		<form:form action="${updateAction}" name="CustomerCareCase"  modelAttribute="CustomerCareCase">
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-6">
					<table class="table no-head table-withoutBorder">
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.id"/></b>
							</td>
							<td>
								${CustomerCareCase.caseId}
								<form:hidden path="caseId" />
								<form:hidden path="dateCreated" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.caseType"/></b>
							</td>
							<td>
								<spring:message code="ccc.${CustomerCareCase.caseType}" />
								<form:hidden path="caseType" />
							</td>
						</tr>
						<c:if test="${CustomerCareCase.marketplaceOrderId ne '' && CustomerCareCase.marketplaceOrderId ne null}">
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.marketplaceOrderId"/></b>
							</td>
							<td>
							<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DRS']}')">
								<a href="https://sellercentral.amazon.com/gp/orders-v2/details/ref=ag_orddet_cont_myo?ie=UTF8&orderID=${CustomerCareCase.marketplaceOrderId}" target="_blank">
									${CustomerCareCase.marketplaceOrderId}
								</a>
								<form:hidden path="marketplaceOrderId" />
							</sec:authorize>
							<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DRS_SUPPLIER']}')">
								${CustomerCareCase.marketplaceOrderId}
								<form:hidden path="marketplaceOrderId" />
							</sec:authorize>
							</td>
						</tr>
						</c:if>
						<tr>
							<td class="text-right">
								<b><spring:message code="common.marketplace"/></b>
							</td>
							<td>
								${CustomerCareCase.marketplace.name}
								<form:hidden path="marketplace" />
							</td>
						</tr>						
						<c:if test="${CustomerCareCase.marketplaceOrderId ne '' && CustomerCareCase.marketplaceOrderId ne null}">						
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.orderDate"/></b>
							</td>
							<td>
								${CustomerCareCase.marketplaceOrderDate}
								<form:hidden path="marketplaceOrderDate" />
								<form:hidden path="dateCreated" />
							</td>
						</tr>
						</c:if>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.customerName"/></b>
							</td>
							<td>
								${CustomerCareCase.customerName}
								<form:hidden path="customerName" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.drsCompany"/></b>
							</td>
							<td>
								${DrsCompanyKcodeToShortEnUsNameMap[CustomerCareCase.drsCompanyKcode]}
								<form:hidden path="drsCompanyKcode" />
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.supplier"/></b>
							</td>
							<td>
							<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DRS']}')">
								<a href="${pageContext.request.contextPath}/Companies/${CustomerCareCase.supplierKcode}">
									${CustomerCareCase.supplierKcode} ${supplierKcodeToShortEnUsNameMap[CustomerCareCase.supplierKcode]}
								</a>
								<form:hidden path="supplierKcode" />
							</sec:authorize>
							<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DRS_SUPPLIER']}')">
								${CustomerCareCase.supplierKcode} ${supplierKcodeToShortEnUsNameMap[CustomerCareCase.supplierKcode]}
							</sec:authorize>							
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.relatedProduct"/></b>
							</td>
							<td>																
								<c:if test="${empty CustomerCareCase.relatedProductSkuCodeList}">   								
									<c:forEach items="${CustomerCareCase.relatedProductBaseCodeList}" var="ProductBase">
                                    	<a href="${pageContext.request.contextPath}/BaseProduct/${ProductBase}">
                                    		${ProductBase} ${productBaseCodeToSupplierNameMap[ProductBase]}
                                    	</a>
                                    	<br>    
									</c:forEach>																	    								
								</c:if>
								<c:if test="${empty CustomerCareCase.relatedProductBaseCodeList}">
    								<c:forEach items="${CustomerCareCase.relatedProductSkuCodeList}" var="sku">
                                    	<a href="${pageContext.request.contextPath}/SKUs/${sku}">
                                    		${sku} ${productSkuCodeToSupplierNameMap[sku]}
                                    	</a>
                                    	<br>    
									</c:forEach>									
								</c:if>
								<form:hidden path="relatedProductBaseCodeList" />
								<form:hidden path="relatedProductSkuCodeList" />																							    														    
						    </td>
						</tr>
					</table>
				</div>
				<div class="col-md-6">
					<table class="table no-head table-withoutBorder">
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.issueCategory"/></b>
							</td>
							<td>							
								<c:choose>
									<c:when test="${empty CustomerCareCase.issueTypeCategoryId}">
										<spring:message code="issue.ALL_CATEGORY"/>
									</c:when>
									<c:otherwise>
										<spring:message code="issue.${issueCategoryIdToNameMap[CustomerCareCase.issueTypeCategoryId]}"/>
									</c:otherwise>
								</c:choose>												
								<form:hidden path="issueTypeCategoryId" />									
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.issueType-issue"/></b>
							</td>
							<td>
							<c:forEach items="${CustomerCareCase.relatedIssueIds}" var="relatedIssueIds" varStatus="status">							  
								<a href="${pageContext.request.contextPath}/Issues/${relatedIssueIds}">
								${issueIdToTypeMap[relatedIssueIds]} - ${issueNameMap[relatedIssueIds]}<br>
								</a>
							</c:forEach>							
							<form:hidden path="relatedIssueIds" />																					
						   </td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.status"/></b>
							</td>
							<td>
								<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DRS']}')">
									<form:select id="status" class="form-control" path="status">																		
										<form:option value="processing"><spring:message code="ccc.processing"/></form:option>																		
										<form:option value="waitingForCustomerResponse"><spring:message code="ccc.waitingForCustomerResponse"/></form:option>																											
										<form:option value="caseClosed"><spring:message code="ccc.caseClosed"/></form:option>								
									</form:select>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DRS_SUPPLIER']}')">
									<spring:message code="ccc.${CustomerCareCase.status}" />	
								</sec:authorize>
							</td>
						</tr>
						<tr>
							<td class="text-right">
								<b><spring:message code="ccc.latestActivity"/></b>
							</td>
							<td>
							<c:choose>
								<c:when test="${CustomerCareCase.latestActivityDays >= 3}">
									${CustomerCareCase.latestActivityDays} <spring:message code="ccc.days" /><spring:message code="ccc.ago" />
								</c:when>
								<c:otherwise>
						   			<c:if test="${CustomerCareCase.latestActivityDays ne '0'}">
						   		 		<c:choose>
						    				<c:when test="${CustomerCareCase.latestActivityDays > 1}">
												${CustomerCareCase.latestActivityDays} <spring:message code="ccc.days" /> 									
											</c:when>
											<c:when test="${CustomerCareCase.latestActivityDays eq 1}">
												${CustomerCareCase.latestActivityDays} <spring:message code="ccc.day" /> 									 
											</c:when>
										</c:choose>
						    			<c:choose>
											<c:when test="${CustomerCareCase.latestActivityHours > 1}">
												${CustomerCareCase.latestActivityHours} <spring:message code="ccc.hours" /><spring:message code="ccc.ago" /> 									
											</c:when>
											<c:when test="${CustomerCareCase.latestActivityHours <= 1}">
												${CustomerCareCase.latestActivityHours} <spring:message code="ccc.hour" /><spring:message code="ccc.ago" /> 									 
											</c:when>
										</c:choose>
						   			</c:if>
						   			<c:if test="${CustomerCareCase.latestActivityDays eq '0'}">
						   				<c:choose>
											<c:when test="${CustomerCareCase.latestActivityHours > 1}">
												${CustomerCareCase.latestActivityHours} <spring:message code="ccc.hours" /> 									
											</c:when>
											<c:when test="${CustomerCareCase.latestActivityHours eq 1}">
												${CustomerCareCase.latestActivityHours} <spring:message code="ccc.hour" /> 									 
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${CustomerCareCase.latestActivityMinutes > 1}">
												${CustomerCareCase.latestActivityMinutes} <spring:message code="ccc.minutes" /><spring:message code="ccc.ago" />									
											</c:when>
											<c:when test="${CustomerCareCase.latestActivityMinutes <= 1}">
												${CustomerCareCase.latestActivityMinutes} <spring:message code="ccc.minute" /><spring:message code="ccc.ago" />									 
											</c:when>
										</c:choose>						    						   
						   			</c:if>						  				   						
								</c:otherwise>
							</c:choose>						
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-12 text-right">
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_EDIT']}')">								
						<input class="btn btn-primary" type="submit" value="<spring:message code='ccc.updateStatus' />" /> 
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_EDIT']}')">					
						<a class="btn btn-success" href="${pageContext.request.contextPath}/CustomerCareCases/${CustomerCareCase.caseId}/edit">
							<spring:message code='ccc.edit'/>
						</a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_DELETE']}')">						
						<c:if test="${CustomerCareCase.messages.size() eq '1'}">										
							<input class="btn btn-link" type="button" value="<spring:message code='ccc.delete'/>" onclick="deleteConfirm('${CustomerCareCase.caseId}');" />
							<div id="dialog-confirm"></div>
						</c:if>
					</sec:authorize>		
				</div>
			</div>
			<c:forEach items="${CustomerCareCase.messages}" var="message" varStatus="messageStatus">
			<c:choose>			
				<c:when test="${messageStatus.count == CustomerCareCase.messages.size()}">
					<form:hidden path="message[${messageStatus.index}].contents" />
					<form:hidden path="message[${messageStatus.index}].lineSeq" />			
				</c:when>
			</c:choose>
			</c:forEach>
		</form:form>
		
		<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_CREATEMSG']}')">														
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code="ccc.newMsg"/></div>
			</div>
		</div>	

		
		<div id="newMessage" ng-controller="messageCtrl">
			
			<c:url var="postMsgAction" value="/CustomerCareCases/postMsg?caseId=${CustomerCareCase.caseId}"></c:url>
			
			<form:form action="${postMsgAction}" name="Message" class="form-horizontal text-left" modelAttribute="Message">
				
				<div id="customer" class="row">
					<div class="col-md-6">
					
						<table class="table table-withoutBorder">
							<tr>
								<td class="text-right">
									<b><spring:message code="ccc.replyFrom"/></b>
								</td>
								<td>
									<form:radiobutton id="CustomerType" path="messageType" value="customer_msg" ng-click="checkMessageType()" checked="TRUE" /> Customer 
									<form:radiobutton id="MSDCType" path="messageType" value="reply_msg" ng-click="checkMessageType()" /> MSDC
								</td>
							</tr>
							<tr id="DateUTCRow" ng-repeat="customer in CustomerType">
								<td class="text-right">
									<b><spring:message code="ccc.replyDate"/><span class="text-danger">*</span></b></td>
								<td>
								<script>
									$(function() {
										
										var control = {
										create : function(tp_inst, obj, unit, val, min, max, step) {$(
										'<input class="ui-timepicker-input" value="'+ val + '" style="width:50%">').appendTo(obj).spinner({
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
												tp_inst.control.value(tp_inst, obj,unit, ui.value);
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
										
									jQuery("#dateUTC").datetimepicker({
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
									
									$("#dateUTC").attr('readonly', true);
									
									});
								</script>																
								<form:input id="dateUTC" class="form-control" style="display:inline;cursor:default;background-color:white;" path="dateCreate" ng-model="customer.dateCreate" required="required"/>
								</td>
							</tr>
						</table>
						
					</div>
				</div>
				<div id="MSDC" class="row" ng-repeat="MSDC in MSDCType">
					<div class="col-md-6">
						<table class="table table-withoutBorder">							
							<tr>
								<td class="text-right">
									<b><spring:message code="ccc.endTime"/><span class="text-danger">*</span></b>
								</td>
								<td>
								<script>
									$(function() {
										var control = {
												create : function(tp_inst, obj, unit, val, min, max, step) {$(
												'<input class="ui-timepicker-input" value="'+ val + '" style="width:50%">').appendTo(obj).spinner({
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
														tp_inst.control.value(tp_inst, obj,unit, ui.value);
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
											jQuery("#endDate").datetimepicker({
												beforeShow: function() {
						        					setTimeout(function(){
						            				$('.ui-datepicker').css('z-index', 200);
						        					}, 0);
						    					},
												controlType : control,
												dateFormat : "yy-mm-dd",
												timeFormat : 'HH:mm:ss z',
												timezoneList : [ {
													value : +480,
													label : 'Taipei'
												}, {
													value : -480,
													label : 'Pacific'
												} ]
											});
											$("#endDate").attr('readonly', true);
											});
									</script>																
									<form:input id="endDate" class="form-control" style="display:inline;cursor:default;background-color:white;" path="endDate" ng-model="MSDC.endDate" required="required"/>
								</td>	
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="ccc.timeTaken"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:input id="timeTaken" class="form-control" path="timeTaken" ng-model="MSDC.timeTaken" ng-change="getDrsChargeByWord()" ng-pattern="/^(0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*)$/" required="required"/>
									<div class="text-danger" ng-show="Message.timeTaken.$error.pattern">
										<spring:message code='ccc.timeTaken_format' />
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="ccc.relatedSku"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:select id="chargeToSKU" class="form-control" path="chargeToSKU" ng-model="MSDC.chargeToSKU" required="required">
										<c:if test="${empty CustomerCareCase.relatedProductSkuCodeList}">   								
								   	 		<form:option value="" label="--- Select ---" />
								    		<c:forEach items="${productSkuCodeToSupplierNameMapUnderBases}" var="sku">
									    	<form:option value="${sku.key}" label="${sku.key} ${sku.value}" />                                            									                                                     
								    		</c:forEach>																	    								
										</c:if>
										<c:if test="${empty CustomerCareCase.relatedProductBaseCodeList}">
											<form:option value="" label="--- Select ---" /> 
    								   		<c:forEach items="${CustomerCareCase.relatedProductSkuCodeList}" var="sku">
									    	<form:option value="${sku}" label="${sku} ${productSkuCodeToSupplierNameMap[sku]}" />					
								       		</c:forEach>									
										</c:if>
									</form:select>
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									<form:checkbox id="isFreeOfCharge" path="isFreeOfCharge" /><spring:message code="ccc.freeOfCharge"/>
									<form:hidden id="responseTemplateId" path="responseTemplateId" value="" />
								</td>
							</tr>							
						</table>
					</div>
					<div class="col-md-6">
						<table class="table table-withoutBorder">
							<tr>
								<td id="noteNoOfBillableWords" class="text-right" title="<spring:message code="ccc.NoOfBillableWordsNote"/>">
									<b><spring:message code="ccc.wordCount"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:input id="wordCount" class="form-control" path="wordCount" ng-model="MSDC.wordCount" ng-change="getDrsChargeByWord()" required="required"/>
								</td>
							</tr>														
							<tr>
								<td id="noteNoOfExtraActions" class="text-right" title="<spring:message code="ccc.NoOfExtraActionsNote"/>">
									<b><spring:message code="ccc.standardAction"/><span class="text-danger">*</span></b>
								</td>
								<td>
									<form:input id="standardActionCount" class="form-control" path="standardActionCount" ng-model="MSDC.standardActionCount" ng-change="getDrsChargeByWord()" required="required"/>
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									<form:checkbox id="translationFeeIncluded" path="includesTranslationFee" ng-model="MSDC.translationFeeIncluded" ng-change="getDrsChargeByWord()" /><spring:message code="ccc.translationFee"/>
								</td>							
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="ccc.chargeByWord"/></b>
								</td>
								<td>
									<form:input id="DRSChargeByWord" class="form-control" path="DRSChargeByWord" ng-model="MSDC.DRSChargeByWord" readonly="true" />
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<b><spring:message code="ccc.costPerHour"/></b>
								</td>
								<td>
									<form:input id="costPerHour" class="form-control" path="costPerHour" ng-model="MSDC.costPerHour" readonly="true" />
								</td>
							</tr>
						</table>
					</div>
				</div>			
				<div class="row">
					<div class="col-md-12">
						<div id="insertTemplateArea" style="display:none">				
							<b><spring:message code='ccc.InsertDefaultMessage' /></b>
							<br>
							<spring:message code='template.responseTemplate' />				
							<select id="basicTemplate" class="form-control" style="width:25%;display: inline;" ng-model="applicableTemplate" ng-change="insertMessage('basicTemplate')">
								<option value="" >--- Select ---</option>
								<c:forEach var="applicableTemplate" items="${applicableTemplates}">
								<option value="${applicableTemplate.key}" >${applicableTemplate.value}</option>							
								</c:forEach>
							</select>
							<br><br>
							<b><spring:message code='ccc.InsertOtherMessageFromTemplate' /></b>
							<br>
							<spring:message code='ccc.issueCategory' />
							<select id="categoryName" name="categoryName" class="form-control" style="width:265px;display: inline;" ng-model="categoryName" ng-change="getIssueTypeList(this)">
        				   		<option value=""><spring:message code="issue.ALL_CATEGORY"/></option>
        				   		<c:forEach items="${issueCategoryIdToNameMap}" var="issueCategoryIdToName">
								<option value="${issueCategoryIdToName.key}"><spring:message code="issue.${issueCategoryIdToName.value}"/></option>
								</c:forEach> 
        				   	</select>
							<spring:message code='ccc.issueType' />
							<select id="issueType" name="issueType" class="form-control" style="width:265px;display: inline;" ng-model="issueType" ng-change="getIssues(this)">
        				   		<option value=""><spring:message code="issue.ALL_TYPE"/></option>
        				   	</select>
							<spring:message code='ccc.issue' />
							<select id="issue" name="issue" class="form-control" style="width:265px;display: inline;" ng-model="issue" ng-change="getTemplates(this)">        				   	   
        				   	</select>
							<br><br>
							<spring:message code='template.responseTemplate' />				
							<select id="otherTemplate" name="responseTemplate" class="form-control" style="width:265px;display: inline;" ng-model="responseTemplate" ng-change="insertMessage('otherTemplate')">        				   	   
        				   	</select>						
						</div>
					</div>
				</div>
				<div id="contentRow" class="row" style="margin-top: 10px; margin-bottom: 10px;">				
					<div class="col-md-12">
						<form:textarea id="contents" class="form-control contents" path="contents" rows="8" ng-model="contents" />
						<div id ="wordCounter"></div>				
					</div>			
				</div>
				<div class="row text-right" style="margin-top: 10px; margin-bottom: 10px;">
					<div class="col-md-12">						
						<input class="btn btn-primary" type="submit" value="<spring:message code='ccc.submit'/>" ng-disabled='Message.$invalid || contents==""' onclick="this.disabled=true;this.value='<spring:message code="ccc.submitButtonNote" />';this.form.submit();"/>					
						<input class="btn btn-link" type="button" value="<spring:message code='ccc.customerCareCaseList'/>" onclick="document.location.href='${pageContext.request.contextPath}/CustomerCareCases'" />											
						<input class="btn btn-link" type="button" value="<spring:message code='issue.issueList'/>" onclick="document.location.href='${pageContext.request.contextPath}/Issues'" />							
					</div>
				</div>
			</form:form>
		</div>
		</sec:authorize>						
		<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
		<c:forEach items="${CustomerCareCase.messages}" var="message" varStatus="messageStatus">
		<c:choose>
			<c:when test="${messageStatus.count != CustomerCareCase.messages.size()}">
				<script>
					app.controller('messageCtrl'+'${messageStatus.count}', function($scope) {															
						function callback (counter) {						 																				
							if(counter['words'] <= 1){						 			  
						 		document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" word";						 			  
						 	}else{						 			  
						 		document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" words";						 			  		  
						 	}						 		   		   		  
						}																								
						var radioArray = new Array();
						radioArray['customer_msg'] = 'CustomerType';
						radioArray['reply_msg'] = 'MSDCType';																										
						document.getElementById(radioArray['${message.messageType}']+'${messageStatus.count}').checked = true;
																									
						if (document.getElementById('CustomerType'+'${messageStatus.count}').checked) {
							$('#MSDCType'+'${messageStatus.count}').attr('disabled', true);
							$scope.MSDCType = [];
							$scope.CustomerType = [{customer : ''}];
							$scope.CustomerType[0]["dateCreate"] = '${message.dateCreate}';									
							var messageContentsHTML = $('#messageContent'+'${messageStatus.count}').html();									
							$scope.contents = messageContentsHTML;									
							$(".wysihtml5-sandbox").contents().find('.contents'+'${messageStatus.count}').html(messageContentsHTML);									
						}								
						if (document.getElementById('MSDCType'+'${messageStatus.count}').checked) {																		
							$('#CustomerType'+'${messageStatus.count}').attr('disabled', true);									
							$("#insertTemplateArea"+'${messageStatus.count}').show();									
							$scope.MSDCType = [{MSDC : ''}];
							$scope.CustomerType = [];									
							$scope.MSDCType[0]["endDate"] = '${message.endDate}';
							$scope.MSDCType[0]["timeTaken"] = '${message.timeTaken}';
							$scope.MSDCType[0]["chargeToSKU"] = '${message.chargeToSKU}';																		
							if('${message.isFreeOfCharge}' == 'true'){
								$scope.MSDCType[0]["isFreeOfCharge"] = true;
							}
							if('${message.includesTranslationFee}' == 'true'){
								$scope.MSDCType[0]["translationFeeIncluded"] = true;
							}
							$scope.MSDCType[0]["wordCount"] = '${message.wordCount}';
							$scope.MSDCType[0]["standardActionCount"] = '${message.standardActionCount}';
							$scope.MSDCType[0]["DRSChargeByWord"] = '${message.DRSChargeByWord}';
							$scope.MSDCType[0]["costPerHour"] = '${message.costPerHour}';								    
							var messageContentsHTML = $('#messageContent'+'${messageStatus.count}').html();													    
							$scope.contents = messageContentsHTML;								    
							$(".wysihtml5-sandbox").contents().find('.contents'+'${messageStatus.count}').html(messageContentsHTML);									
							getIssues('${messageStatus.count}');									
							$scope.issue="";
							$scope.responseTemplate="";									
						}																							
						Countable.live(document.getElementById('contents'+'${messageStatus.count}'), callback);								
						$('#contentRow .wysihtml5-sandbox').contents().find('body').on("change keyup paste blur click",function() {				    		    	
				    		var descriptionContent =  $("#contentRow .wysihtml5-sandbox").contents().find('.contents'+'${messageStatus.count}').html(); 	    	    	
				    		$('#contents'+'${messageStatus.count}').val(descriptionContent);				    		    	    	
				    		var area = document.getElementById('contents'+'${messageStatus.count}');				    				
				    			function callback (counter) {				    		 		   
				    		 		if(counter['words'] <= 1){ 			  
				    		 			document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" word";			  
				    		 			$scope.contents = "";
						     			$scope.$apply();
				    		 		}else{ 			  
				    		 			document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" words"; 			  		  
				    		 			$scope.contents = descriptionContent;
						     			$scope.$apply();	
				    		 		}				    		 		   		   		  
				    			}				    				
				    		Countable.live(area, callback);				    		    	
				    	});
						$('#contentRow .wysihtml5-sandbox').on('load', function(e){	
							$('#contentRow .wysihtml5-sandbox').contents().find('body').on("change keyup paste blur click",function() {										
								var descriptionContent =  $("#contentRow .wysihtml5-sandbox").contents().find('.contents'+'${messageStatus.count}').html(); 	    	    	
							    $('#contents'+'${messageStatus.count}').val(descriptionContent);							        	    	
							    var area = document.getElementById('contents'+'${messageStatus.count}');							    		
							    	function callback (counter) {							     		  
							     		if(counter['words'] <= 1){ 			  
							     			document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" word";			  
							     			$scope.contents = "";
							     			$scope.$apply();	     			     	 		  
							     		}else{ 			  
							     			document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" words"; 			  		  
							     			$scope.contents = descriptionContent;
							     			$scope.$apply();	
							     		}							     		   		   		  
							    	}							    		
							   	Countable.live(area, callback);							        		   		
							});
						});
										
						function emptyList(id) {
							document.getElementById(id).options.length = 0;
						}
								
						function getIssues(id){									
							var categoryId = Number($('#categoryName'+id).val());			
							var typeId = Number($('#issueType'+id).val());								
							var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssues/';
							var issues = null;												
							$.ajax({
								type : 'get',
								url : ajaxUrl,
								contentType : "application/json; charset=utf-8",
								data : {
									categoryId : categoryId,
									typeId : typeId				
								},
								dataType : "json",
								success : function(data) {											
									issues = data;											
									emptyList("issue"+id);											
									if(Object.keys(issues).length > 0){																
										var selectIssue = document.getElementById("issue"+id);
										var optIssue = document.createElement("option");
										optIssue.value = "";
										optIssue.textContent = "---Select---";
										selectIssue.appendChild(optIssue);																									
									}											
									for ( var key in issues) {											
										optIssue = document.createElement("option");
										optIssue.value = key;
										optIssue.textContent = issues[key];
										selectIssue.appendChild(optIssue);																	
									}																	
									emptyList("otherTemplate"+id);											
									$scope.issue="";
									$scope.responseTemplate="";											
									$scope.Message.issue.$$lastCommittedViewValue="";
									$scope.Message.issue.$$rawModelValue="";
									$scope.Message.issue.$modelValue="";
									$scope.Message.issue.$viewValue="";											
									$scope.Message.responseTemplate.$$lastCommittedViewValue="";
									$scope.Message.responseTemplate.$$rawModelValue="";
									$scope.Message.responseTemplate.$modelValue="";
									$scope.Message.responseTemplate.$viewValue="";																						
								},
								error: function(jqXHR, textStatus, errorThrown) 
								{											
									emptyList("issue"+id);
									emptyList("otherTemplate"+id);											
									$scope.issue="";
									$scope.responseTemplate="";											
									$scope.Message.issue.$$lastCommittedViewValue="";
									$scope.Message.issue.$$rawModelValue="";
									$scope.Message.issue.$modelValue="";
									$scope.Message.issue.$viewValue="";											
									$scope.Message.responseTemplate.$$lastCommittedViewValue="";
									$scope.Message.responseTemplate.$$rawModelValue="";
									$scope.Message.responseTemplate.$modelValue="";
									$scope.Message.responseTemplate.$viewValue="";											
									console.log(jqXHR);
									console.log(textStatus);
									console.log(errorThrown);															
								}																							
							});																		
						}
																
						$scope.checkMessageType = function(count) {									
							var checkedValue = null;									
							if (document.getElementById('CustomerType'+count).checked) {
								checkedValue = document.getElementById('CustomerType'+count).value;
							}									
							if (document.getElementById('MSDCType'+count).checked) {
								checkedValue = document.getElementById('MSDCType'+count).value;
							}																																																										
							$('#startDate'+count).val('');
							$('#endDate'+count).val('');
							$('#timeTaken'+count).val('');
							$('#chargeToSKU'+count).val('');									
							$('#wordCount'+count).val('');
							$('#standardActionCount'+count).val('');
							$('#DRSChargeByWord'+count).val('');
							$('#costPerHour'+count).val('');
							if (checkedValue == "customer_msg") {
								$("#MSDC"+count).hide();
								$("#insertTemplateArea"+count).hide();
								$("#DateUTCRow"+count).show();
							} else {
								$("#MSDC"+count).show();
								$("#insertTemplateArea"+count).show();
								$("#DateUTCRow"+count).hide();
							}
																		
						}
								
						$scope.getTimeNow = function(target,count) {									
							var now = new Date();									
							$('#'+target+count).val(dateFormat(now, "yyyy-mm-dd HH:MM:ss o"));									
							$scope.getTimeTaken(count);									
						};
												
						function msToTime(s) {								 			
							function addZ(n) {
								return (n<10? '0':'') + n;
							}
							var ms = s % 1000;
							s = (s - ms) / 1000;
							var secs = s % 60;
							s = (s - secs) / 60;
							var mins = s % 60;
							var hrs = (s - mins) / 60;
							return addZ(hrs) + ':' + addZ(mins) + ':' + addZ(secs);								
						}
										
						$scope.getTimeTaken = function(count){									
							var startDate = $("#startDate"+count).val();
							var endDate = $("#endDate"+count).val();														     			
							var startDateFormat = startDate.replace(/-/g,"/");   
							var endDateFormat = endDate.replace(/-/g,"/");														
							if(startDateFormat != "" && endDateFormat != ""){								    	
								var startDateTime = new Date(startDateFormat);		    	
								var endDateTime = new Date(endDateFormat);		    	
								var duration = endDateTime - startDateTime;
								var time = msToTime(duration);								    			    			    	
								$scope.timeTaken = time;								    	
								$scope.getDrsChargeByWord(count);								    			    	
							}									
						};
								
						$scope.getDrsChargeByWord = function (count){									
							var feeForWordCount = 0.07;
							if( $scope.MSDCType[0]["translationFeeIncluded"] == true) feeForWordCount = 0.14;
							var wordCount = Number($scope.MSDCType[0]["wordCount"]);
							var standardActionCount = Number($scope.MSDCType[0]["standardActionCount"]);
							var DRSChargeByWord = wordCount*feeForWordCount+standardActionCount*1.4;
							$scope.MSDCType[0]["DRSChargeByWord"] = Math.round(Number(DRSChargeByWord) * Math.pow(10, 2)) / Math.pow(10, 2);
							$scope.getCostPerHour();																														
						};
								
						$scope.getCostPerHour = function (){																		
							var costPerHour = ($scope.MSDCType[0]["DRSChargeByWord"]/$scope.MSDCType[0]["timeTaken"])*60;			
							$scope.MSDCType[0]["costPerHour"] = Math.round(Number(costPerHour) * Math.pow(10, 2)) / Math.pow(10, 2);										
						};
								
						$scope.openMessage = function (id){																	
							$("#collapse"+id).on('show.bs.collapse', function(){									      
								$("#messageContent"+id).hide();
								$("#editArea"+id).hide();										
								Countable.once(document.getElementById('contents'+'${messageStatus.count}'), function (counter) {																							
									if(counter['words'] <= 1){									 			  
										document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" word";									 			  
									}else{									 			  
										document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" words";									 			  		  
									}							  																			  
								});																																								
							});
																			
							$("#collapse"+id).on('hide.bs.collapse', function(){								    
								$("#messageContent"+id).show();
								$("#editArea"+id).show();																													
							});																										
						};
																																								
						$scope.collapseMessage = function (id){																	
							$("#collapse"+id).on('show.bs.collapse', function(){									      
								$("#messageContent"+id).hide();
								$("#editArea"+id).hide();										
							});																			
							$("#collapse"+id).on('hide.bs.collapse', function(){								    
								$("#messageContent"+id).show();
								$("#editArea"+id).show();										
							});																											
						};
																
						$scope.collapseMessageForSubmit = function(id){																	
							$("#messageForm"+id).submit(function(e){										
								var postData = $(this).serializeArray();
								var formURL = $(this).attr("action");
								var updatedMessage = null;										
								$.ajax({											
									url : formURL,
									type: "POST",
									data : postData,
									success:function(data, textStatus, jqXHR) {
										updatedMessage = JSON.parse(data);																							
										var str = updatedMessage['contents'].replace(/(?:\r\n|\r|\n)/g, '<br />');																			          			
										$("#messageContent"+id).html(updatedMessage['contents']);							            
									},
									error: function(jqXHR, textStatus, errorThrown) 
									{																								
									}																			
								});										
								e.preventDefault();	//STOP default action										
							});												
							$( "#messageForm"+id ).submit();										
							$("#collapse"+id).on('show.bs.collapse', function(){									      
								$("#messageContent"+id).hide();
								$("#editArea"+id).hide();										
							});									
							$("#collapse"+id).on('hide.bs.collapse', function(){								    
								$("#messageContent"+id).show();
								$("#editArea"+id).show();										
							});																											
						};
								
						$scope.insertMessage = function (target,id){								
							var templateId = $('#'+target+id).val();
							var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getTemplateContent/';
							var templateContent = null;
							$('#contents'+id).val("");
							$("#responseTemplateId"+id).val("");							        									
							$.ajax({
								type : 'get',
								url : ajaxUrl,
								contentType : "application/json; charset=utf-8",
								data : { templateId : templateId },
								dataType : "json",
								success : function(data) {											
									templateContent = data;											
									var cursorPos = $(".wysihtml5-sandbox").contents().find('.contents'+id).prop('selectionStart');
									var currentContents = $(".wysihtml5-sandbox").contents().find('.contents'+id).html();
									var textBefore = currentContents.substring(0,  cursorPos );
									var textAfter  = currentContents.substring( cursorPos, currentContents.length );									        
									$('#contents'+id).val( textBefore+ templateContent);
									$(".wysihtml5-sandbox").contents().find('.contents'+id).html(textBefore+ templateContent);									        
									$("#responseTemplateId"+id).val($("#"+target+id).val());									        
									Countable.once(document.getElementById('contents'+'${messageStatus.count}'), function (counter) {												
										if(counter['words'] <= 1){										 			  
											document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" word";										 			  
										}else{										 			  
										 	document.getElementById("wordCounter"+'${messageStatus.count}').innerHTML = counter['words'] +" words";										 			  		  
										}																				  
									});									        									        									        
								}																	
							});																											
						};
																	
						$scope.getIssueTypeList = function(scope,id) {								
							var categoryId = Number($('#categoryName'+id).val());
							var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssueTypeList/';
							var issueTypeList = null;									
							$.ajax({	
								type : 'get',
								url : ajaxUrl,
								contentType : "application/json; charset=utf-8",
								data : { categoryId : categoryId },
								dataType : "json",
								success : function(data) {											
									issueTypeList = data;													
									emptyList("issueType"+id);											
									var selectIssueType = document.getElementById("issueType"+id);
									var optIssueType = document.createElement("option");
									optIssueType.value = "";
									optIssueType.textContent = "<spring:message code='issue.ALL_TYPE' />";
									selectIssueType.appendChild(optIssueType);																																																			
									for ( var issueType in issueTypeList) {
										if (issueTypeList.hasOwnProperty(issueType)) {											
											optIssueType = document.createElement("option");
											optIssueType.value = issueType;
											optIssueType.textContent = issueTypeList[issueType];
											selectIssueType.appendChild(optIssueType);											
										}
									}										
									scope.getIssues(scope,id);														
								},
								error: function(jqXHR, textStatus, errorThrown) 
								{																
									console.log(jqXHR);
									console.log(textStatus);
									console.log(errorThrown);															
								}											
							});								
						};
										
						$scope.getIssues = function (scope,id){								
							var categoryId = Number($('#categoryName'+id).val());			
							var typeId = Number($('#issueType'+id).val());								
							var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getIssues/';
							var issues = null;												
							$.ajax({
								type : 'get',
								url : ajaxUrl,
								contentType : "application/json; charset=utf-8",
								data : {
									categoryId : categoryId,
									typeId : typeId				
								},
								dataType : "json",
								success : function(data) {											
									issues = data;											
									emptyList("issue"+id);											
									if(Object.keys(issues).length > 0){																
										var selectIssue = document.getElementById("issue"+id);
										var optIssue = document.createElement("option");
										optIssue.value = "";
										optIssue.textContent = "---Select---";
										selectIssue.appendChild(optIssue);																									
									}											
									for ( var key in issues) {											
										optIssue = document.createElement("option");
										optIssue.value = key;
										optIssue.textContent = issues[key];
										selectIssue.appendChild(optIssue);																	
									}											
									emptyList("otherTemplate"+id);											
									scope.issue="";
									scope.responseTemplate="";											
									scope.Message.issue.$$lastCommittedViewValue="";
									scope.Message.issue.$$rawModelValue="";
									scope.Message.issue.$modelValue="";
									scope.Message.issue.$viewValue="";											
									scope.Message.responseTemplate.$$lastCommittedViewValue="";
									scope.Message.responseTemplate.$$rawModelValue="";
									scope.Message.responseTemplate.$modelValue="";
									scope.Message.responseTemplate.$viewValue="";																						
								},
								error: function(jqXHR, textStatus, errorThrown) 
								{											
									emptyList("issue"+id);
									emptyList("otherTemplate"+id);											
									scope.issue="";
									scope.responseTemplate="";											
									scope.Message.issue.$$lastCommittedViewValue="";
									scope.Message.issue.$$rawModelValue="";
									scope.Message.issue.$modelValue="";
									scope.Message.issue.$viewValue="";											
									scope.Message.responseTemplate.$$lastCommittedViewValue="";
									scope.Message.responseTemplate.$$rawModelValue="";
									scope.Message.responseTemplate.$modelValue="";
									scope.Message.responseTemplate.$viewValue="";											
									console.log(jqXHR);
									console.log(textStatus);
									console.log(errorThrown);															
								}																							
							});									
						};		
						
						$scope.getTemplates = function (scope,id){									
							var issueId = $('#issue'+id).val();
							var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getTemplates/';
							var templates = null;												
							$.ajax({
								type : 'get',
								url : ajaxUrl,
								contentType : "application/json; charset=utf-8",
								data : { issueId : issueId },
								dataType : "json",
								success : function(data) {											
									templates = data;											
									emptyList("otherTemplate"+id);																						
									if(Object.keys(templates).length > 0){												
										var selectTemplate = document.getElementById("otherTemplate"+id);												
										var optTemplate = document.createElement("option");
										optTemplate.value = "";
										optTemplate.textContent = "---Select---";
										selectTemplate.appendChild(optTemplate);																									
									}											
									for ( var key in templates) {												
										optTemplate = document.createElement("option");
										optTemplate.value = key;
										optTemplate.textContent = templates[key];
										selectTemplate.appendChild(optTemplate);																	
									}																						
									scope.responseTemplate="";											
									scope.Message.responseTemplate.$$lastCommittedViewValue="";
									scope.Message.responseTemplate.$$rawModelValue="";
									scope.Message.responseTemplate.$modelValue="";
									scope.Message.responseTemplate.$viewValue="";																						
								},
								error: function(jqXHR, textStatus, errorThrown) 
								{											
									emptyList("otherTemplate"+id);											
									scope.responseTemplate="";											
									scope.Message.responseTemplate.$$lastCommittedViewValue="";
									scope.Message.responseTemplate.$$rawModelValue="";
									scope.Message.responseTemplate.$modelValue="";
									scope.Message.responseTemplate.$viewValue="";																						
								}																							
							});									
						};
																																																																																																																																								
					});
		</script> 			 		
		<div id="messageId${message.lineSeq}" class="panel panel-default" style="margin:2% 0 2% 0" ng-controller="messageCtrl${messageStatus.count}">
			<div class="panel-heading" style="padding: 15px 15px;">						
				<span class="text-muted">#${message.lineSeq} 
					<b>						  
					<c:choose>
						<c:when test="${message.messageType eq 'customer_msg'}">
							${CustomerCareCase.customerName}
						</c:when>
						<c:otherwise>									
							TrueToSource								
						</c:otherwise>
					</c:choose>
					</b>
				</span>												  											
				<span id="editArea${messageStatus.count}" class="text-muted text-right" style="float:right">
						   <spring:message code="ccc.createdBy" arguments="${message.createBy},${message.dateCreate}" />
						    <c:if test="${message.messageType eq 'reply_msg'}">
							<a class="btn btn-default"
								href="${pageContext.request.contextPath}/CustomerCareCases/${CustomerCareCase.caseId}/${message.lineSeq}">							
									<i class="fas fa-dollar-sign" aria-hidden="true"></i>								
							</a>
							</c:if>														
							<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_CREATEMSG']}')">
							<c:if test="${message.isModifiable eq 'true'}">
							<a class="btn btn-default" id="heading${messageStatus.count}" ng-model="openMessage" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${messageStatus.count}" aria-expanded="true" aria-controls="collapse${messageStatus.count}" ng-click="openMessage('${messageStatus.count}')">																 								
								<i class="fas fa-pencil-alt" aria-hidden="true"></i>    
							</a>
						    </c:if>
						    </sec:authorize>
						    <sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_CREATEMSG']}')">
						    <c:if test="${message.isModifiable eq 'true'}">
						    <a class="btn btn-default" onclick="deleteMessageConfirm('${CustomerCareCase.caseId}','${message.lineSeq}');">								
								<i class="fas fa-trash-alt" aria-hidden="true"></i>							
							</a>
							</c:if>
							</sec:authorize>
				</span>						
			</div>																																									
			<div id="collapse${messageStatus.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${messageStatus.count}">
				<div class="panel-body">						
					<div id="newPerMessage">
					<c:url var="updateMsgAction" value="/CustomerCareCases/updateMsg?caseId=${CustomerCareCase.caseId}"></c:url>
					<span class="text-danger"><spring:message code='ccc.tempValidation' /></span>	
					<form:form id="messageForm${messageStatus.count}" action="${updateMsgAction}" name="Message" class="form-horizontal text-left" modelAttribute="Message">				  
				    	<div id="customer${messageStatus.count}" class="row">
							<div class="col-md-6">
								<table class="table table-withoutBorder">
									<tr>
										<td class="text-right"><b><spring:message code="ccc.replyFrom"/></b></td>
										<td>
											<form:radiobutton id="CustomerType${messageStatus.count}" path="messageType" value="customer_msg" ng-click="checkMessageType('${messageStatus.count}')" checked="TRUE" /> Customer 
											<form:radiobutton id="MSDCType${messageStatus.count}" path="messageType" value="reply_msg" ng-click="checkMessageType('${messageStatus.count}')" /> MSDC
											<form:hidden path="lineSeq" value="${message.lineSeq}"/>		
										</td>
									</tr>
									<tr id="DateUTCRow${messageStatus.count}" ng-repeat="customer in CustomerType">
										<td class="text-right">
											<b><spring:message code="ccc.replyDate"/><span class="text-danger">*</span></b>
										</td>
										<td>
											<script>
												$(function() {	
													var control = {
														create : function(tp_inst, obj, unit, val, min, max, step) {$(
														'<input class="ui-timepicker-input" value="' + val + '" style="width:50%">').appendTo(obj).spinner({
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
																tp_inst.control.value(tp_inst, obj,unit, ui.value);
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
												jQuery("#dateUTC"+'${messageStatus.count}').datetimepicker({
													beforeShow: function() {
						        						setTimeout(function(){
						            					$('.ui-datepicker').css('z-index', 200);
						        						}, 0);
						    						},
													controlType : control,
													dateFormat : "yy-mm-dd",
													timeFormat : 'HH:mm:ss z',
													timezoneList : [ {
														value : +480,
														label : 'Taipei'
													}, {
														value : -480,
														label : 'Pacific'
													} ]
												});
												$("#dateUTC"+'${messageStatus.count}').attr('readonly', true);
												});
											</script>
												<form:input id="dateUTC${messageStatus.count}" class="form-control" style="display:inline;cursor:default;background-color:white;" path="dateCreate" ng-model="customer.dateCreate" required="required"/>
										</td>
									</tr>
								</table>
							</div>
				 		</div>
				 		<div id="MSDC${messageStatus.count}" class="row" ng-repeat="MSDC in MSDCType">
							<div class="col-md-6"> 
								<table class="table table-withoutBorder">							
									<tr>
										<td class="text-right"><b><spring:message code="ccc.endTime"/><span class="text-danger">*</span></b></td>
										<td>
											<script>
												$(function() {
													var control = {
														create : function(tp_inst, obj, unit, val, min, max, step) {$(
														'<input class="ui-timepicker-input" value="' + val + '" style="width:50%">').appendTo(obj).spinner({
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
																tp_inst.control.value(tp_inst, obj,unit, ui.value);
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
												jQuery("#endDate"+'${messageStatus.count}').datetimepicker({
													beforeShow: function() {
						        						setTimeout(function(){
						            						$('.ui-datepicker').css('z-index', 200);
						        						}, 0);
						    						},
													controlType : control,
													dateFormat : "yy-mm-dd",
													timeFormat : 'HH:mm:ss z',
													timezoneList : [ {
														value : +480,
														label : 'Taipei'
													}, {
														value : -480,
														label : 'Pacific'
													} ]
												});
												$("#endDate"+'${messageStatus.count}').attr('readonly', true);
												});
											</script>								
												<form:input id="endDate${messageStatus.count}" class="form-control" style="display:inline;cursor:default;background-color:white;" path="endDate" ng-model="MSDC.endDate" required="required"/>									
										</td>
									</tr>
									<tr>
										<td class="text-right">
											<b><spring:message code="ccc.timeTaken"/><span class="text-danger">*</span></b>
										</td>
										<td>
											<form:input id="timeTaken${messageStatus.count}" class="form-control" path="timeTaken" ng-model="MSDC.timeTaken" ng-change="getDrsChargeByWord('${messageStatus.count}')" required="required"/>
										</td>
									</tr>	
				    				<tr>
										<td class="text-right">
											<b><spring:message code="ccc.relatedSku"/><span class="text-danger">*</span></b>
										</td>
										<td>
											<form:select id="chargeToSKU${messageStatus.count}" class="form-control" path="chargeToSKU" ng-model="MSDC.chargeToSKU" required="required">
											<c:if test="${empty CustomerCareCase.relatedProductSkuCodeList}">   								
								    			<form:option value="" label="--- Select ---" />
								    			<c:forEach items="${productSkuCodeToSupplierNameMapUnderBases}" var="sku">
									          	<form:option value="${sku.key}" label="${sku.key} ${sku.value}" />                                            									                                                     
								    			</c:forEach>																	    								
											</c:if>
											<c:if test="${empty CustomerCareCase.relatedProductBaseCodeList}">
												<form:option value="" label="--- Select ---" /> 
    								   			<c:forEach items="${CustomerCareCase.relatedProductSkuCodeList}" var="sku">
									        	<form:option value="${sku}" label="${sku} ${productSkuCodeToSupplierNameMap[sku]}" />					
								       			</c:forEach>									
											</c:if>
											</form:select>
										</td>
									</tr>
									<tr>
										<td></td>
										<td>
											<form:checkbox id="isFreeOfCharge${messageStatus.count}" path="isFreeOfCharge" ng-model="MSDC.isFreeOfCharge" /><spring:message code="ccc.freeOfCharge"/>
											<form:hidden id="responseTemplateId${messageStatus.count}" path="responseTemplateId" value="" />
										</td>
									</tr>				    				    
				    			</table>				    				   					     				  
							</div>
							<div class="col-md-6">
								<table class="table table-withoutBorder">							
									<tr>
										<td class="text-right">
											<b><spring:message code="ccc.wordCount"/><span class="text-danger">*</span></b>
										</td>
										<td>
											<form:input id="wordCount${messageStatus.count}" class="form-control" path="wordCount" ng-model="MSDC.wordCount" ng-change="getDrsChargeByWord('${messageStatus.count}')" required="required"/>
										</td>
									</tr>														
									<tr>
										<td class="text-right">
											<b><spring:message code="ccc.standardAction"/><span class="text-danger">*</span></b>
										</td>
										<td>
											<form:input id="standardActionCount${messageStatus.count}" class="form-control" path="standardActionCount" ng-model="MSDC.standardActionCount" ng-change="getDrsChargeByWord('${messageStatus.count}')" required="required"/>
										</td>
									</tr>
									<tr>
										<td></td>
										<td>
											<form:checkbox id="translationFeeIncluded${messageStatus.count}" path="includesTranslationFee" ng-model="MSDC.translationFeeIncluded" ng-change="getDrsChargeByWord('${messageStatus.count}')"/><spring:message code="ccc.translationFee"/>
										</td>
									</tr>
									<tr>
										<td class="text-right">
											<b><spring:message code="ccc.chargeByWord"/></b>
										</td>
										<td>
											<form:input id="DRSChargeByWord${messageStatus.count}" class="form-control" path="DRSChargeByWord" ng-model="MSDC.DRSChargeByWord" readonly="true" />
										</td>
									</tr>
									<tr>
										<td class="text-right">
											<b><spring:message code="ccc.costPerHour"/></b>
										</td>
										<td>
											<form:input id="costPerHour${messageStatus.count}" class="form-control" path="costPerHour" ng-model="MSDC.costPerHour" readonly="true" />
										</td>
									</tr>																			    				    
				    			</table>										    										
							</div>						
						</div>
				<div class="row">
					<div class="col-md-12">
						<div id="insertTemplateArea${messageStatus.count}" style="display:none">				
							<b><spring:message code='ccc.InsertDefaultMessage' /></b>
							<br>
							<spring:message code='template.responseTemplate' />				
							<select id="basicTemplate${messageStatus.count}" class="form-control" style="width:25%;display: inline;" ng-model="applicableTemplate" ng-change="insertMessage('basicTemplate','${messageStatus.count}')">
								<option value="" >--- Select ---</option>
								<c:forEach var="applicableTemplate" items="${applicableTemplates}">
								<option value="${applicableTemplate.key}" />${applicableTemplate.value}</option>							
								</c:forEach>
							</select>
							<br><br>
							<b><spring:message code='ccc.InsertOtherMessageFromTemplate' /></b>
							<br>
							<spring:message code='ccc.issueCategory' />
							<select id="categoryName${messageStatus.count}" name="categoryName" class="form-control" style="width:265px;display: inline;" ng-model="categoryName" ng-change="getIssueTypeList(this,'${messageStatus.count}')">
        				   		<option value=""><spring:message code="issue.ALL_CATEGORY"/></option>
        				   	   	<c:forEach items="${issueCategoryIdToNameMap}" var="issueCategoryIdToName">
								<option value="${issueCategoryIdToName.key}"><spring:message code="issue.${issueCategoryIdToName.value}"/></option>
							    </c:forEach>
        				   	</select>
        					<spring:message code='ccc.issueType' />
        					<select id="issueType${messageStatus.count}" name="issueType" class="form-control" style="width:265px;display: inline;" ng-model="issueType" ng-change="getIssues(this,'${messageStatus.count}')">
        				   		<option value=""><spring:message code="issue.ALL_TYPE"/></option>
        				   	</select>
        					<spring:message code='ccc.issue' />
        					<select id="issue${messageStatus.count}" name="issue" class="form-control" style="width:265px;display: inline;" ng-model="issue" ng-change="getTemplates(this,'${messageStatus.count}')">        				   	   
        				   	</select>
        					<br><br>		   	   
        					<spring:message code='template.responseTemplate' />	   	   				
							<select id="otherTemplate${messageStatus.count}" name="responseTemplate" class="form-control" style="width:265px;display: inline;" ng-model="responseTemplate" ng-change="insertMessage('otherTemplate','${messageStatus.count}')">        				   	   
        				   	</select>									
						</div>
					</div>
				</div>																	
				<div id="contentRow" class="row" style="margin-top: 10px; margin-bottom: 10px;">
					<div class="col-md-12">
						<form:textarea id="contents${messageStatus.count}" class="form-control contents${messageStatus.count}" path="contents" rows="8" ng-model="contents" />
						<div id ="wordCounter${messageStatus.count}"></div>				
					</div>
				</div>
				<div class="row text-right" style="margin-top: 10px; margin-bottom: 10px;">
					<div class="col-md-12">					
						<a class="btn btn-primary" id="heading${messageStatus.count}" ng-model="submitMessage" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${messageStatus.count}" aria-expanded="true" aria-controls="collapse${messageStatus.count}" ng-click="collapseMessageForSubmit('${messageStatus.count}')" ng-disabled='Message.$invalid || contents==""'>																 					
							<spring:message code='ccc.submit' />					
						</a>					
						<a id="heading${messageStatus.count}" class="btn btn-link" ng-model="cancelMessage" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${messageStatus.count}" aria-expanded="true" aria-controls="collapse${messageStatus.count}" ng-click="collapseMessage('${messageStatus.count}')">																 
							<spring:message code='ccc.cancel'/>
						</a>
					</div>
				</div>
		</form:form>
			</div>						
		</div>					
		</div>
			<% pageContext.setAttribute("newLineChar", "\n"); %>
			<div id="messageContent${messageStatus.count}" class="panel-body removePseudoElements">						
				<p>${fn:replace(message.contents, newLineChar,  "<br>")}</p>						
			</div>
		</div>
		<script type="text/javascript">
			$('#contents'+'${messageStatus.count}').wysihtml5({
				"events": {
				 	"load": function() {
						$("#contentRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");				        	               
					}
				},toolbar: {		   
					"html": true, //Button which allows you to edit the generated HTML. Default false   		     		   
	    			"link": false, //Button to insert a link. Default true
	    			"image": true, //Button to insert an image. Default true,
	    			"blockquote": false //Blockquote 		   
				}
			});				
		</script>								  			 						 
			</c:when>			
		</c:choose>
		</c:forEach>
		<div id="dialog-message-confirm"></div>
		</div>		  
		<c:forEach items="${CustomerCareCase.messages}" var="message" varStatus="messageStatus">
			<c:choose>			
			<c:when test="${messageStatus.count == CustomerCareCase.messages.size()}">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-heading" style="padding: 15px 15px;">
							<span class="text-muted">#${message.lineSeq} <b>${CustomerCareCase.customerName}</b></span>
							<span class="text-muted text-right" style="float:right"><spring:message code="ccc.createdBy" arguments="${message.createBy},${message.dateCreate}" /></span>						
						</div>
						<div class="panel-body">
					  		<% pageContext.setAttribute("newLineChar", "\n"); %>
					 	 	<p>${fn:replace(message.contents, newLineChar,  "<br>")}</p>
						</div>
					</div>
				</div>				
			</div>
			</c:when>
			</c:choose>
		</c:forEach>		
		<script type="text/javascript">
			$('#contents').wysihtml5({
	    		"events": {
	        		"load": function() {
	            		$("#contentRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");	        	               
	        		}
	    		},toolbar: {		   
	    	 		"html": true, //Button which allows you to edit the generated HTML. Default false   		     		   
			 		"link": false, //Button to insert a link. Default true
			 		"image": true, //Button to insert an image. Default true,
			 		"blockquote": false //Blockquote  		   
		  		}
			});	
		</script>						
	</div>
</div>