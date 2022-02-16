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
			Edit international transaction - DRS		
		</c:when>
		<c:otherwise>				
			Create international transaction - DRS						
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
jQuery(window).on("load", function(e)   {
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
		$('#supplier').select2({ theme: "bootstrap"});

	});
	angular.module('internationalTransaction', []).controller('internationalTransactionShipmentCtrl', function($scope) {
		
		$scope.addLineItem = function(){		    
			$scope.lineItems.push({lineSeq:'',itemKey:'',subtotal:''});
			var id = Number($scope.lineItems.length)-1;
			$scope.updatedItem(id);			 
		};
		
		$scope.removeLineItem = function(item){		 	
			if($scope.lineItems.length > 1){			 		     
				 var idx = $scope.lineItems.indexOf(item);	 				 
				 $scope.lineItems.splice(idx,1);
			 	$scope.calculateTotal();			 
			}			 
		};
		
		$scope.getItems = function(){						 
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/InternationalTransaction/getLineItemKeyNameMap/',
				contentType : "application/json; charset=utf-8",
				data : {cashFlowDirectionKey : $scope.cashFlowDirectionKey},
				dataType : "json",
				success : function(items) {																
					for(i = 0; i < $scope.lineItems.length; i++){							
						$('#item'+i).empty();
						$('#item'+i).append($('<option>').text("-- select --").val(""));														
						$.each(items,function(key,value){																								
							$('#item'+i).append($('<option>').text(value).val(key));							
						});																					
					}											
				},
				error : function(data, status,er) {						
					for(i = 0; i < $scope.lineItems.length; i++){							
						$('#item'+i).empty();																												
					}						
				}									
			});					
		};
		
		$scope.updatedItem = function(id){
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/InternationalTransaction/getLineItemKeyNameMap/',
				contentType : "application/json; charset=utf-8",
				data : {cashFlowDirectionKey : $scope.cashFlowDirectionKey},
				dataType : "json",
				success : function(items) {																						
					$('#item'+id).empty();
					$('#item'+id).append($('<option>').text("-- select --").val(""));							
					$.each(items,function(key,value){																								
						$('#item'+id).append($('<option>').text(value).val(key));							
					});																																						
				}									
			});						
		};
				
		$scope.calculateTotal = function(){				
			var amount = 0;
			var total = 0;			 
			for(var i = 0; i < $scope.lineItems.length;i++){				 	
				amount = Number($scope.lineItems[i].subtotal);				
				total +=  amount;				 				
			}			 
			$scope.total = total;			 			 
		};
		 
		$scope.getCurrencyByMsdcKcode = function(){				 
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/InternationalTransaction/getCurrencyByMsdcKcode/',
				contentType : "application/json; charset=utf-8",
				data : { msdcKcode : $scope.msdcKcode },
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
				url : '${pageContext.request.contextPath}/InternationalTransaction/getSplrKcodeNameMap/',
				contentType : "application/json; charset=utf-8",
				data : { ssdcKcode : $scope.ssdcKcode },
				dataType : "json",
				success : function(splrKcode) {						
					var supplierElement = $('#supplier');
					supplierElement.empty()
					supplierElement.append($('<option>').text("--- Select ---").val(""));
					$.each(splrKcode,function(key,value){
						supplierElement.append($('<option>').text(key+" "+value).val(key));
					});
					$('#supplier').val($scope.splrKcode);						
				}									
			});						 
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
							location.href = "${pageContext.request.contextPath}/InternationalTransaction/" + id + "/delete";
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
			var InternationalTransactionJson = ${InternationalTransactionJson};
			$scope.msdcKcode = InternationalTransactionJson.msdcKcode;
			$scope.ssdcKcode = InternationalTransactionJson.ssdcKcode;			
			$scope.splrKcode = InternationalTransactionJson.splrKcode;			
			$scope.getSupplier();
			$scope.utcDate = InternationalTransactionJson.utcDate;
			$scope.cashFlowDirectionKey = InternationalTransactionJson.cashFlowDirectionKey;
			$scope.note = InternationalTransactionJson.note;
			$scope.currency = InternationalTransactionJson.currency
			$scope.total = InternationalTransactionJson.total
			$scope.lineItems = InternationalTransactionJson.lineItems;
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/InternationalTransaction/getLineItemKeyNameMap/',
				contentType : "application/json; charset=utf-8",
				data : { cashFlowDirectionKey : $scope.cashFlowDirectionKey },
				dataType : "json",
				success : function(items) {															
					for(i = 0; i < $scope.lineItems.length; i++){						
						$('#item'+i).empty();
						$('#item'+i).append($('<option>').text("-- select --").val(""));						
						$.each(items,function(key,value){																								
							$('#item'+i).append($('<option>').text(value).val(key));							
						});						
						$('#item'+i).val(InternationalTransactionJson['lineItems'][i]['itemKey']);						
					}										
				}								
			});															
		}else{			
			$scope.lineItems = [{lineSeq:'',itemKey:'',subtotal:''}];
			$scope.total = 0;
			$scope.currency = "";			
		}				 
	});			
</script>
</head>
<div class="max-width" ng-app="internationalTransaction" ng-controller="internationalTransactionShipmentCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<c:choose>
					<c:when test="${type ne 'Create'}">				
						Edit international transaction
						<c:url var="action" value="/InternationalTransaction/update"></c:url>			
					</c:when>
					<c:otherwise>				
						Create international transaction
						<c:url var="action" value="/InternationalTransaction/save"></c:url>															
					</c:otherwise>
					</c:choose>				
				</div>
			</div>
		</div>
		<form:form action="${action}" name="InternationalTransaction" modelAttribute="InternationalTransaction">			
		<div class="row">
			<div class="col-md-6">
					<table class="table table-withoutBorder no-head">
						<tr>
							<td class="text-right"><b>UTC Date</b></td>						
							<td>
								<form:input id ="utcDate" class="form-control" style="cursor:default;background-color:white;display:inline;width:50%" path="utcDate" ng-model="utcDate" />
								<span class="text-danger">*</span>
								<span class="text-danger" ng-show="InternationalTransaction.utcDate.$error.required && InternationalTransaction.utcDate.$dirty">
									UTC Date is required
								</span>						
							</td>
						</tr>
						<tr>
							<td class="text-right"><b>Cash flow direction</b></td>
							<td>
								<form:select class="form-control" style="display:inline;width:50%" path="cashFlowDirectionKey" ng-model="cashFlowDirectionKey" ng-change="getItems()" required="required">
									<form:option value="">--- Select ---</form:option>
									<c:forEach var="cashFlowDirection" items="${cashFlowDirections}">
									<form:option value="${cashFlowDirection.key}">${cashFlowDirection.displayName}</form:option>						
									</c:forEach>
								</form:select>
								<span class="text-danger">*</span>
								<span class="text-danger" ng-show="InternationalTransaction.cashFlowDirectionKey.$error.required && InternationalTransaction.cashFlowDirectionKey.$dirty">
									Cash flow direction is required
								</span>														
							</td>
						</tr>
						<tr>
						<td class="text-right"><b>MSDC</b></td>						
						<td>
							<form:hidden path="id" />
							<form:select class="form-control" style="display:inline;width:50%" path="msdcKcode" ng-model="msdcKcode" ng-change="getCurrencyByMsdcKcode()" required="required">
								<form:option value="">--- Select ---</form:option>																		
								<c:forEach var="msdcKcodeName" items="${msdcKcodeNameMap}">
								<form:option value="${msdcKcodeName.key}">${msdcKcodeName.key} ${msdcKcodeName.value}</form:option>										
								</c:forEach>					
							</form:select>
							<span class="text-danger">*</span>
							<span class="text-danger" ng-show="InternationalTransaction.msdcKcode.$error.required && InternationalTransaction.msdcKcode.$dirty">
								MSDC is required
							</span>								
						</td>							
					</tr>
					<tr>
						<td class="text-right"><b>SSDC</b></td>						
						<td>
							<form:select class="form-control" style="display:inline;width:50%" path="ssdcKcode" ng-model="ssdcKcode" ng-change="getSupplier()" required="required">
								<form:option value="">--- Select ---</form:option>																		
								<c:forEach var="ssdcKcodeName" items="${ssdcKcodeNameMap}">
								<form:option value="${ssdcKcodeName.key}">${ssdcKcodeName.key} ${ssdcKcodeName.value}</form:option>										
								</c:forEach>										
							</form:select>
							<span class="text-danger">*</span>
							<span class="text-danger" ng-show="InternationalTransaction.ssdcKcode.$error.required && InternationalTransaction.ssdcKcode.$dirty">
								SSDC is required
							</span>																								
						</td>
					</tr>
					<tr>
						<td class="text-right"><b>Supplier</b></td>						
						<td>
							<form:select id="supplier" class="form-control" style="display:inline;width:70%" path="splrKcode" ng-model="splrKcode" required="required">					
							</form:select>
							<span class="text-danger">*</span>
							<span class="text-danger" ng-show="InternationalTransaction.splrKcode.$error.required && InternationalTransaction.splrKcode.$dirty">
								Supplier is required
							</span>												
						</td>
					</tr>
					<tr>
						<td class="text-right"><b>Note</b></td>						
						<td>
							<form:input class="form-control" path="note" ng-model="note" />				
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
								<select id="item{{$index}}" class="form-control" style="display:inline;width:40%" name ="lineItem[{{$id}}].itemKey" ng-model="item.itemKey" required>										
								</select>
								<span class="text-danger" ng-show="InternationalTransaction['lineItem[{{$id}}].itemKey'].$error.required && InternationalTransaction['lineItem[{{$id}}].itemKey'].$dirty">
									Item name is required
								</span>																			
							</div>
							<div class="col-md-2">
								<input name="lineItem[{{$id}}].itemNote" class="form-control" style="display:inline;width:90%" ng-model="item.itemNote">										  																
							</div>
							<div class="col-md-2 text-right" style="padding-right: 0px;">				
								<input name="lineItem[{{$id}}].subtotal" class="form-control" style="display:inline;width:90%;text-align:right" ng-model="item.subtotal" ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" ng-change="calculateTotal()" required>				
								<span class="text-danger" ng-show="InternationalTransaction['lineItem[{{$id}}].subtotal'].$error.required && InternationalTransaction['lineItem[{{$id}}].subtotal'].$dirty">
									Amount is required
								</span>														
								<span class="text-danger" ng-show="InternationalTransaction['lineItem[{{$id}}].subtotal'].$error.pattern">
									Amount must be numbers and greater than zero
								</span>								
							</div>
							<div class="col-md-1 text-right">
								<button type="button" class="btn btn-default" ng-click="removeLineItem(item)">
									<i class="fas fa-trash-alt"></i>
								</button>				
							</div>
						</div>	  			  				
  					</li>
  					<li class="list-group-item">
  						<div class="row">
							<div class="col-md-7"></div>
							<div class="col-md-2"></div>
							<div class="col-md-2"></div>
							<div class="col-md-1 text-right">
								<button type="button" class="btn btn-default" ng-click="addLineItem()">
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
			<div class="col-md-2 text-right"></div>
			<div class="col-md-2 text-right">
				Total({{currency}}) {{total}}
				<form:hidden path="currency" ng-model="currency" value="{{currency}}" />
				<form:hidden path="total" ng-model="total" value="{{total}}" />										
			</div>
			<div class="col-md-1 text-right"></div> 					
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12 text-right">
				<input class="btn btn-primary" type="submit" value="submit" ng-disabled="InternationalTransaction.$invalid" onclick="this.disabled=true;this.value='Sending, please wait...';this.form.submit();"/>							
				<c:choose>
				<c:when test="${type ne 'Create'}">						
					<input class="btn btn-danger" type="button" value="Delete" ng-click="deleteTransaction('${InternationalTransaction.id}')" />
				</c:when>						
				</c:choose>
				<a class="btn btn-link" href="${pageContext.request.contextPath}/InternationalTransactions">Back</a>						
				<div id="dialog-confirm"></div>					
			</div>
		</div>	
	</form:form>	
	</div>
</div>	