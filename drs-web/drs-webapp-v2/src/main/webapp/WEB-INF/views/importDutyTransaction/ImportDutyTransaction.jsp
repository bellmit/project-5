<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<c:choose>
		<c:when test="${type eq 'Create'}">					
				Create Import Duty Transaction - DRS				
		</c:when>
		<c:otherwise>						
				Edit Import Duty Transaction - DRS													
		</c:otherwise>
	</c:choose>
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>	

<script>
	jQuery(window).on("load", function(e) {
		$('#utcDate').attr('readonly', true);
		jQuery("#utcDate").datepicker({
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
			},
			dateFormat : 'yy-mm-dd'
		});
		
		//make dropdown combo box
		$("#unsName").select2();
		
	});	
	angular.module('importDutyTransaction', []).controller('importDutyTransactionCtrl', function($scope) {				
		var currentURL = document.URL;		
		if(currentURL.indexOf("edit") > -1){		
			var ImportDutyTransactionItem = ${ImportDutyTransactionItem};							
			$scope.unsName = ImportDutyTransactionItem["unsName"];
			$scope.utcDate = ImportDutyTransactionItem["utcDate"];
			$("#dstCountryText").html(ImportDutyTransactionItem["dstCountry"]);
			$("#dstCountry").val(ImportDutyTransactionItem["dstCountry"]);			
			$("#currency").val(ImportDutyTransactionItem["currency"]);
			$("#currencyForAmount").html("("+ImportDutyTransactionItem["currency"]+")");
			$("#currencyForTotal").html("("+ImportDutyTransactionItem["currency"]+")");	
			$scope.lineItems = ImportDutyTransactionItem["lineItems"];			
			$scope.total = ImportDutyTransactionItem["total"];						
		}else{						
			$scope.lineItems = [];
			$scope.total = 0;							
		}
				
		$scope.getSourceIvsInfo = function(){			
			$scope.getSourceIvsInfoLineItems();
			$scope.getCountry($scope.unsName);			
		};
				
		$scope.getSourceIvsInfoLineItems = function(){				
			$scope.lineItems.splice(0,$scope.lineItems.length);						
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/ImportDutyTransaction/getLineItemInfoForCreate',
				contentType : "application/json; charset=utf-8",
				data : {unsName : $scope.unsName,},
				dataType : "json",
				success : function(sourceIvsInfoLineItems) {										
					for(var sourceIvsInfoLineItem in sourceIvsInfoLineItems){						
						if (sourceIvsInfoLineItems.hasOwnProperty(sourceIvsInfoLineItem)) {						
							$scope.lineItems.push({sourceIvsName : sourceIvsInfoLineItems[sourceIvsInfoLineItem]["sourceIvsName"],
								sku : sourceIvsInfoLineItems[sourceIvsInfoLineItem]["sku"],
								quantity : sourceIvsInfoLineItems[sourceIvsInfoLineItem]["quantity"],
								amount:	sourceIvsInfoLineItems[sourceIvsInfoLineItem]["amount"]			
							});																			
						}															
					}					
					$scope.$apply();
				}
			});	
		};
		
		$scope.getCountry = function(unsName){					
			$("#dstCountryText").html("");
			$("#dstCountry").val("");					
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/ImportDutyTransaction/getCountry',
				contentType : "application/json; charset=utf-8",
				data : {unsName : unsName,},
				dataType : "json",
				success : function(country) {							
					$("#dstCountryText").html(country);
					$("#dstCountry").val(country);
					$scope.getCurrency(country);										
				}
			});		
		};
		
		$scope.getCurrency = function(countryName){			
			$("#currency").val("");
			$("#currencyForAmount").html("");
			$("#currencyForTotal").html("");				
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/ImportDutyTransaction/getCurrency',
				contentType : "application/json; charset=utf-8",
				data : {countryName : countryName,},
				dataType : "json",
				success : function(currency) {												
					$("#currency").val(currency);
					$("#currencyForAmount").html("("+currency+")");
					$("#currencyForTotal").html("("+currency+")");											
				}
			});			
		};
		
		$scope.getTotal = function(){			
			var total = 0;			
			for(var lineItem in $scope.lineItems){				
				if($scope.lineItems.hasOwnProperty(lineItem)){										
					total += Number($scope.lineItems[lineItem]["Amount"]);					
				}								
			}			
			$scope.total = total;			
		};
			
	});
</script>
</head>
<div class="max-width" ng-app="importDutyTransaction" ng-controller="importDutyTransactionCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
						<c:when test="${type eq 'Create'}">					
							Create Import Duty Transaction	
							<c:url var="action" value="/ImportDutyTransaction/save"></c:url>			
						</c:when>
						<c:otherwise>						
							Edit Import Duty Transaction
							<c:url var="action" value="/ImportDutyTransaction/update"></c:url>													
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>		
		<form:form name="ImportDutyTransaction" action="${action}" modelAttribute="ImportDutyTransaction">
		<div class="row">
			<div class="col-md-4">
				<table class="table table-withoutBorder table-autoWidth">
						<tr>
							<td class="text-right"><b>Select UNS</b></td>
							<td>							
							<c:choose>
								<c:when test="${type eq 'Create'}">					
									<form:select class="form-control" path="unsName" ng-model="unsName" ng-change="getSourceIvsInfo()" required="required">
									<form:option value="" label="--- Select ---" />								
									<c:forEach var="unsName" items="${unsNameList}">
									<form:option value="${unsName}">${unsName}</form:option>
									</c:forEach>									
									</form:select>
									<div class="text-danger" ng-show="ImportDutyTransaction.unsName.$error.required && ImportDutyTransaction.unsName.$dirty">	
										UNS is required.
									</div>												
								</c:when>
								<c:otherwise>						
									{{unsName}}	<form:hidden path="unsName" value="{{unsName}}" />											
								</c:otherwise>
							</c:choose>																				
							</td>
						</tr>
						<tr>
							<td class="text-right"><b>Date</b></td>
							<td>
								<form:input id="utcDate" class="form-control" style="cursor:default;" path="utcDate" ng-model="utcDate" required="required" />
								<div class="text-danger" ng-show="ImportDutyTransaction.utcDate.$error.required && ImportDutyTransaction.utcDate.$dirty">	
									Date is required.
								</div>		
							</td>
						</tr>
						<tr>
							<td class="text-right"><b>Dst Country</b></td>
							<td class="text-center">
								<span id="dstCountryText"></span>
								<form:hidden id ="dstCountry" path="dstCountry" value="" />
								<form:hidden id ="currency" path="currency" value="" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-md-8">
					<table class="table">
						<thead>
							<tr style="background-color: #F5F5DC;">
								<th>Source IVS</th>
								<th style="width: 30%;">SKU</th>
								<th class="text-right" style="width: 15%;">Quantity</th>
								<th class="text-right" style="width: 15%;">Amount<span id="currencyForAmount"></span></th>
							</tr>
						</thead>
						<tbody>
						<tr ng-repeat="item in lineItems">	
							<td>{{item.sourceIvsName}}<input type="hidden" name="lineItem[{{$id}}].sourceIvsName" value="{{item.sourceIvsName}}"/></td>
							<td>{{item.sku}}<input type="hidden" name="lineItem[{{$id}}].sku" value="{{item.sku}}"/></td>
							<td class="text-right">{{item.quantity}}<input type="hidden" name="lineItem[{{$id}}].quantity" value="{{item.quantity}}"/></td>
							<td>
								<input class="form-control" style="text-align:right" name="lineItem[{{$id}}].amount" value="{{item.amount}}" ng-model="item.amount" ng-change="getTotal()" ng-pattern="/^(?:[0-9]\d*)?(?:\.\d+)?$/" required="required"/>
								<div class="text-danger" ng-show="ImportDutyTransaction['lineItem[{{$id}}].amount'].$error.required && ImportDutyTransaction['lineItem[{{$id}}.amount'].$dirty">								
									Amount is required.
								</div>							
								<div class="text-danger" ng-show="ImportDutyTransaction['lineItem[{{$id}}].amount'].$error.pattern">								
									Amount should be a positive number and could be zero.
								</div>
							</td>						
						</tr>
						</tbody>
						<tr>
							<td colspan="2"></td>
							<td class="text-right"><b>TOTAL<span id="currencyForTotal"></span></b></td>
							<td class="text-right active">{{total}}<input type="hidden" name="total" value="{{total}}"/></td>
						</tr>					
					</table>
				</div>
			</div>
			<div class="row" ng-if="lineItems.length > 0">
				<div class="col-md-8 text-right">
					<a class="btn btn-link" href="${pageContext.request.contextPath}/ImportDutyTransaction">cancel</a>
					<input class="btn btn-primary" ng-disabled="ImportDutyTransaction.$invalid" type="submit" value="submit">
				</div>
			</div>			
		</form:form>
	</div>
</div>