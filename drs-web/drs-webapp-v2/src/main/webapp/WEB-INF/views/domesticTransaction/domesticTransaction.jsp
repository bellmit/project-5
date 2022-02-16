<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<c:choose>
		<c:when test="${type ne 'Create'}">				
			Edit domestic transaction - DRS		
		</c:when>
		<c:otherwise>				
			Create domestic transaction - DRS						
		</c:otherwise>
	</c:choose>
</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>

<style>
.list-group-item-info {
	color: #31708f;
    background-color: #d9edf7;
}
[data-tip] {
	position:relative;
}
[data-tip]:before {
	content:'';
	/* hides the tooltip when not hovered */
	display:none;
	content:'';
	border-left: 5px solid transparent;
	border-right: 5px solid transparent;
	border-bottom: 5px solid #1a1a1a;	
	position:absolute;
	top:30px;
	left:35px;
	z-index:8;
	font-size:0;
	line-height:0;
	width:0;
	height:0;
}
[data-tip]:after {
	display:none;
	content:attr(data-tip);
	position:absolute;
	top:35px;
	left:0px;
	padding:5px 8px;
	background:#1a1a1a;
	color:#fff;
	z-index:9;
	font-size: 0.75em;
	height:18px;
	line-height:18px;
	-webkit-border-radius: 3px;
	-moz-border-radius: 3px;
	border-radius: 3px;
	white-space:nowrap;
	word-wrap:normal;
}
[data-tip]:hover:before,
[data-tip]:hover:after {
	display:block;
}
</style>
<script>
jQuery(window).on("load", function(e) {		
		var earliestAvailableUtcDate = '${earliestAvailableUtcDate}';		
		$('#utcDate').attr('readonly', true);
		jQuery("#utcDate").datepicker({
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
			},
			dateFormat : 'yy-mm-dd',
			minDate: earliestAvailableUtcDate
		});
		$( "#supplier" ).select2({
		    theme: "bootstrap"
		});
	});

	angular.module('domesticTransaction', []).controller('domesticTransactionCtrl', function($scope) {
	
		$scope.addLineItem = function(){		    
			$scope.lineItems.push({lineSeq:'',itemKey:'',note:'',amount:''});						 
		};
		
		$scope.removeLineItem = function(item){		 	
			if($scope.lineItems.length > 1){			 		     
				var idx = $scope.lineItems.indexOf(item);	 				 
			 	$scope.lineItems.splice(idx,1);
			 	$scope.calculateAmountSubtotal();			 
			}			 
		};
		
		$scope.getCurrency = function(){			
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/DomesticTransactions/getCurrency',
				contentType : "application/json; charset=utf-8",
				data : {ssdcKcode : $scope.ssdcKcode},
				dataType : "json",
				success : function(currency) {						
					$scope.currency = currency;
					$scope.$apply();			
				}									
			});						 
		};
		
		$scope.getSupplier = function(){			 
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/DomesticTransactions/getSplrKcodeNameMap',
				contentType : "application/json; charset=utf-8",
				data : {ssdcKcode : $scope.ssdcKcode},
				dataType : "json",
				success : function(splrKcode) {						
					var supplierElement = $('#supplier');
					supplierElement.empty()
					supplierElement.append($('<option>').text("--- Select ---").val(""));
					$.each(splrKcode,function(key,value){
						supplierElement.append($('<option>').text(key+" "+value).val(key));
					});
					$('#supplier').val($scope.splrKcode);
					$scope.getCurrency();
				}
			});
		};
		
		$scope.calculateAmountSubtotal = function(){			
			var amount = 0;
			var amountSubtotal = 0;			 
			for(var i = 0; i < $scope.lineItems.length;i++){				 	
				amount = Number($scope.lineItems[i].amount);				
				amountSubtotal +=  amount;				 				
			}			 
			$scope.amountSubtotal = amountSubtotal;
			$scope.calculateAmountTax();
			$scope.calculateAmountTotal();			 
		};
		
		$scope.calculateAmountTax = function(){			 
			var amountTax = (Number($scope.amountSubtotal)*Number($scope.taxPercentage)) / 100;														
			$scope.amountTax = isNaN(amountTax) ? 0 : Math.round(amountTax);										
			$scope.calculateAmountTotal();			
		};
		 
		$scope.calculateAmountTotal = function(){			
			var amountTotal = Number($scope.amountSubtotal) + Number($scope.amountTax);	
			$scope.amountTotal = Math.round(amountTotal);			
		};
				 
		$scope.deleteTransaction = function(id){
			$("#dialog-confirm").html("Are you sure to delete this transaction?");
			$("#dialog-confirm").dialog({
				open : function() {
					$('.ui-dialog-buttonset button[name="no"]').focus();
				},
				resizable : false,
				modal : true,
				title : "Confirmation",
				height : 250,
				width : 400,
				buttons : [
					{
						text : "Yes",
						name : "yes",
						click : function() {
							$(this).dialog('close');
							location.href = "${pageContext.request.contextPath}/DomesticTransactions/" + id + "/delete";
						}
					},
					{
						text : "No",
						name : "no",
						click : function() {
							$(this).dialog('close');
						}
					} ]
			});			 
		 };
		 
		if(document.URL.indexOf("edit") > -1){
			var DomesticTransactionJson = ${DomesticTransactionJson};
			$scope.ssdcKcode = DomesticTransactionJson.ssdcKcode;
			$scope.splrKcode = DomesticTransactionJson.splrKcode;
			$scope.getSupplier();
			$scope.utcDate = DomesticTransactionJson.utcDate;
			$scope.currency = DomesticTransactionJson.currency;
			$scope.invoiceNumber = DomesticTransactionJson.invoiceNumber;
			$scope.taxPercentage = DomesticTransactionJson.taxPercentage;
			$scope.amountSubtotal = DomesticTransactionJson.amountSubtotal;
			$scope.amountTax = DomesticTransactionJson.amountTax;
			$scope.amountTotal = DomesticTransactionJson.amountTotal;
			$scope.lineItems = DomesticTransactionJson.lineItems;
			$scope.isNotEditable = !${isAllEditable};
			console.log("date: " + $scope.utcDate);
		}else{
			$scope.lineItems = [{lineSeq:'',itemKey:'',note:'',amount:''}];
			$scope.amountSubtotal = 0;
			$scope.amountTax = 0;
			$scope.amountTotal = 0;
			$scope.currency = "";
			$scope.taxPercentage = ${defaultSalesTaxPercentage};
			$scope.isNotEditable = !${isAllEditable};
		}
	});
</script>
</head>
<div class="max-width" ng-app="domesticTransaction" ng-controller="domesticTransactionCtrl">
	<div class="container-fluid">	
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
						<c:when test="${type ne 'Create'}">				
							Edit domestic transaction
							<c:url var="action" value="/DomesticTransactions/update"></c:url>			
							</c:when>
						<c:otherwise>				
							Create domestic transaction
							<c:url var="action" value="/DomesticTransactions/save"></c:url>															
						</c:otherwise>
					</c:choose>				
				</div>
			</div>
		</div>
		<form:form action="${action}" name="DomesticTransaction" modelAttribute="DomesticTransaction">			
		<div class="row">	
			<div class="col-md-8">
			
			
				<table class="table table-withoutBorder no-head table-centered">
					<tr>
						<td class="text-right">
							<b>UTC Date<span class="text-danger">*</span></b>
						</td>						
						<td>						
							<form:input id ="utcDate" class="form-control" style="cursor:default;background-color:white;display:inline;width:40%" path="utcDate" ng-model="utcDate" ng-disabled="isNotEditable" />
							<span class="text-danger" ng-show="DomesticTransaction.utcDate.$error.required && DomesticTransaction.utcDate.$dirty">
								UTC Date is required
							</span>						
						</td>
					</tr>					
					<tr>
						<td class="text-right">
							<b>SSDC<span class="text-danger">*</span></b>
						</td>						
						<td>
							<form:hidden path="id" />
							<form:select class="form-control" style="display:inline;width:40%" path="ssdcKcode" ng-model="ssdcKcode"  ng-disabled="isNotEditable" ng-change="getSupplier()" required="required">
							<form:option value="">--- Select ---</form:option>																		
							<c:forEach var="ssdcKcodeName" items="${ssdcKcodeNameMap}">
							<form:option value="${ssdcKcodeName.key}">${ssdcKcodeName.key} ${ssdcKcodeName.value}</form:option>										
							</c:forEach>
							</form:select>												
							<span class="text-danger" ng-show="DomesticTransaction.ssdcKcode.$error.required && DomesticTransaction.ssdcKcode.$dirty">
								SSDC is required
							</span>																								
						</td>
					</tr>
					<tr>
						<td class="text-right"><b>Supplier<span class="text-danger">*</span></b></td>						
						<td>
							<form:select id="supplier" class="form-control" style="display:inline;width:40%" path="splrKcode"  ng-disabled="isNotEditable" ng-model="splrKcode" required="required">
					    	</form:select>						
							<span class="text-danger" ng-show="DomesticTransaction.splrKcode.$error.required && DomesticTransaction.splrKcode.$dirty">
								Supplier is required
							</span>												
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b>Invoice number</b>
						</td>						
						<td>
							<form:input id="invoiceNumber" class="form-control" style="display:inline;width:40%" path="invoiceNumber" ng-model="invoiceNumber" />						
						</td>
					</tr>							
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<ul class="list-group">
					<li class="list-group-item list-group-item-info">
  						<div class="row">
							<div class="col-md-7">Item name<span class="text-danger">*</span></div>
							<div class="col-md-2">Note</div>
							<div class="col-md-2 text-right" style="padding-right: 0px;">Amount<span class="text-danger">*</span></div>
							<div class="col-md-1 text-right"></div>
  						</div> 			
  					</li>
					<li id="newitem{{$id}}" class="list-group-item" ng-repeat="item in lineItems">
						<div class="row">	
							<div class="col-md-7">
								<input type="hidden" name="lineItem[{{$id}}].lineSeq" ng-model="item.lineSeq" value="{{$index+1}}">
								<select class="form-control" style="display:inline;width:40%" name ="lineItem[{{$id}}].itemKey"  ng-disabled="isNotEditable" ng-model="item.itemKey" required>
									<option value="">--- Select ---</option>
									<c:forEach var="lineItemTypeKeyName" items="${lineItemTypeKeyNameMap}">
									<option value="${lineItemTypeKeyName.key}"><spring:message code='${lineItemTypeKeyName.value}' /></option>										
									</c:forEach>																			
								</select>						
								<span class="text-danger" ng-show="DomesticTransaction['lineItem[{{$id}}].itemKey'].$error.required && DomesticTransaction['lineItem[{{$id}}].itemKey'].$dirty">
									Item name is required
								</span>																												
							</div>	
							<div class="col-md-2">
								<input name="lineItem[{{$id}}].note" class="form-control" style="display:inline;width:90%"  ng-disabled="isNotEditable" ng-model="item.note">
							</div>
							<div class="col-md-2 text-right" style="padding-right: 0px;">				
								<input name="lineItem[{{$id}}].amount" class="form-control" style="text-align:right;" ng-model="item.amount" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/"  ng-disabled="isNotEditable" ng-change="calculateAmountSubtotal()" required>
								<span class="text-danger" ng-show="DomesticTransaction['lineItem[{{$id}}].amount'].$error.required && DomesticTransaction['lineItem[{{$id}}].amount'].$dirty">
									Amount is required
								</span>														
								<span class="text-danger" ng-show="DomesticTransaction['lineItem[{{$id}}].amount'].$error.pattern">
									Amount must be numbers and greater than zero
								</span>								
							</div>
							<div class="col-md-1 text-right">
								<button type="button" class="btn btn-default" ng-click="removeLineItem(item)" ng-disabled="isNotEditable" >
									<i class="fas fa-trash-alt"></i>
								</button>				
							</div>
						</div>
					</li>
					<li class="list-group-item">
  						<div class="row">
							<div class="col-md-7"></div>
							<div class="col-md-2"></div>
							<div class="col-md-2 text-right"></div>
							<div class="col-md-1 text-right">
								<button type="button" class="btn btn-default" ng-click="addLineItem()" ng-disabled="isNotEditable" >
									<i class="fas fa-plus"></i>
								</button>
							</div>
  						</div> 			
  					</li>														
				</ul>
			</div>						
		</div>
		<div class="row">
			<div class="col-md-7"></div>
			<div class="col-md-2 text-right">Subtotal {{currency}}</div>
			<div class="col-md-2 text-right">
				{{amountSubtotal}}
				<form:hidden path="currency" ng-model="currency"  ng-disabled="isNotEditable" value="{{currency}}" /><form:hidden path="amountSubtotal" ng-model="amountSubtotal" value="{{amountSubtotal}}" />
			</div>
			<div class="col-md-1 text-right"></div> 					
		</div>
		<div class="row">
			<div class="col-md-7 text-right"></div>
			<div class="col-md-2 text-right" style="padding-top:8px">
				Tax ( <form:input id="taxPercentage" class="form-control" style="display:inline;width:25%" path="taxPercentage" ng-model="taxPercentage" ng-disabled="isNotEditable"  ng-change="calculateAmountTax()" /> %)
				<span class="text-danger" ng-show="DomesticTransaction.taxPercentage.$error.required && DomesticTransaction.taxPercentage.$dirty">Tax percentage is required</span>												
			 	{{currency}}
			</div>
			<div class="col-md-2 text-right" style="padding-top:15px">
				{{amountTax}}
				<form:hidden path="amountTax" ng-model="amountTax"  ng-disabled="isNotEditable" value="{{amountTax}}" />
			</div>
			<div class="col-md-1 text-right"></div>
		</div>
		<div class="row">
			<div class="col-md-7"></div>
			<div class="col-md-2 text-right">Total {{currency}}</div>
			<div class="col-md-2 text-right">
				{{amountTotal}}
				<form:hidden path="amountTotal" ng-model="amountTotal"  ng-disabled="isNotEditable" value="{{amountTotal}}" />
			</div>
			<div class="col-md-1 text-right"></div>
		</div>		
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12 text-right">
				<input class="btn btn-primary" type="submit" value="submit" ng-disabled="DomesticTransaction.$invalid" onclick="this.disabled=true;this.value='Sending, please wait...';this.form.submit();" />							
				<c:choose>
					<c:when test="${type ne 'Create'}">						
						<input class="btn btn-danger" type="button" value="Delete" ng-click="deleteTransaction('${DomesticTransaction.id}')"  ng-disabled="isNotEditable" />
					</c:when>						
				</c:choose>
				<a class="btn btn-link" href="${pageContext.request.contextPath}/DomesticTransactions">Back</a>						
				<div id="dialog-confirm"></div>					
			</div>
		</div>		
		</form:form>
	</div>
</div>