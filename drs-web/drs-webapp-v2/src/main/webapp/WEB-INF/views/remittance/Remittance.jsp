<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	${title} - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>		
<script>
	$(document).ready(function() {
		var earliestAvailableUtcDate = '${earliestAvailableUtcDate}';
		$('#dateSent,#dateReceived,#bankPayment').attr('readonly', true);
		jQuery("#dateSent,#dateReceived").datepicker({
			beforeShow: function() {
	        	setTimeout(function(){
	            	$('.ui-datepicker').css('z-index', 200);
	        	}, 0);
	    	},
			dateFormat : 'yy-mm-dd',
			minDate: earliestAvailableUtcDate
		});
		
		//make dropdown combo box
		$("#sender").select2();
		$("#receiver").select2();
		
	});
	var app = angular.module('remittance', []);
	app.controller('remittanceCtrl', function($scope) {
	    $scope.feeIncluded = "true";
	    $scope.feeAmount = 0;
	    $scope.statementName = "";
		if(document.URL.indexOf("edit") > -1){
			$scope.dateSetted = false;
			var RemittanceJson = ${RemittanceJson};
	    	$scope.dateSent = RemittanceJson.utcDateSent;
	    	$scope.dateReceived = RemittanceJson.utcDateReceived;
	    	$scope.sender = RemittanceJson.sender;
	    	$scope.receiver = RemittanceJson.receiver;
	    	$scope.currency = RemittanceJson.currency;
			$scope.amount = RemittanceJson.amount;
			$scope.feeAmount = RemittanceJson.feeAmount;
			if (RemittanceJson.feeIncluded == "Yes") {
			    $scope.feeIncluded = "true";
			} else {
			    $scope.feeIncluded = "false";
			}
			var statementName = RemittanceJson.statementName;
			if (statementName.startsWith("STM-")) {
			    $scope.statementName = RemittanceJson.statementName.substring(4);
			} else {
			    $scope.statementName = RemittanceJson.statementName;
			}
			$scope.bankPayment = RemittanceJson.bankPayment;
			$scope.reference = RemittanceJson.reference;
		}else{
			$scope.dateSetted = true;
		}
		$scope.dateSetting = function(){
			$scope.dateSetted = false;
 		};

        $scope.updateBankPayment = function() {
            if ($scope.feeIncluded == "true") {
                $scope.bankPayment = parseInt($scope.amount, 10)
            } else {
                $scope.bankPayment = parseInt($scope.amount, 10) + parseInt($scope.feeAmount, 10);
            }
        };

        $scope.updateReference = function() {
            if ($scope.feeIncluded == "true") {
                $scope.reference = "STM-" + $scope.statementName + " 扣匯費" + $scope.feeAmount;
            } else {
                $scope.reference = "STM-" + $scope.statementName + " 不扣匯費" + $scope.feeAmount;
            }
        };
	});
</script>
</head>
<div class="max-width" ng-app="remittance" ng-controller="remittanceCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6" style="margin:0px 25%;">
				<div class="page-heading text-center">
					${title}									
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6" style="margin:0px 25%;">
			<c:url var="action" value="${actionURL}"></c:url>
			<form:form action="${action}" name="Remittance" modelAttribute="Remittance" novalidate="novalidate">
				<table class="table table-withoutBorder table-autoWidth table-centered">
					<tr>
						<td class="text-right"><b><spring:message code='remittance.id' /></b></td>
						<c:choose>
							<c:when test="${type eq 'Create'}">
								<td>to be auto-generated<form:hidden path="remittanceId" /></td>
							</c:when>
							<c:otherwise>
								<td><form:input class="form-control" style="width:95%;display: inline;" path="remittanceId" readonly="true" /></td>
							</c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.dateSent' /></b>
						</td>
						<td><form:input id="dateSent" class="form-control" style="width:95%;display:inline;cursor:default;background-color:white;" path="utcDateSent" ng-model="dateSent" ng-change="dateSetting()" /></td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.dateReceived' /></b>
						</td>
						<td>
							<form:input id="dateReceived" class="form-control" style="width:95%;display:inline;cursor:default;background-color:white;" path="utcDateReceived" ng-model="dateReceived" ng-change="dateSetting()" />
							<p class="help-block"><spring:message code='remittance.helpMsg' /></p>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.sender' /></b>
						</td>
						<td>
							<form:select id="sender" class="form-control" style="width:95%;display: inline;" path="sender" ng-model="sender" required="required">
								<form:option value="" label="--- Select ---" />
								<c:forEach var="SenderAndReceiverCompany" items="${SenderAndReceiverCompanies}">
								<form:option value="${SenderAndReceiverCompany.key}" label="${SenderAndReceiverCompany.key} ${SenderAndReceiverCompany.value}" />
								</c:forEach>									
							</form:select>
							<span class="text-danger">*</span>
							<div class="text-danger" ng-show="Remittance.sender.$error.required && Remittance.sender.$dirty">
								Sender is required
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.receiver' /></b>
						</td>
						<td>
							<form:select id="receiver" class="form-control" style="width:95%;display: inline;" path="receiver" ng-model="receiver" required="required">
								<form:option value="" label="--- Select ---" />
								<c:forEach var="SenderAndReceiverCompany" items="${SenderAndReceiverCompanies}">
								<form:option value="${SenderAndReceiverCompany.key}" label=" ${SenderAndReceiverCompany.key} ${SenderAndReceiverCompany.value}" />
								</c:forEach>
							</form:select>
							<span class="text-danger">*</span>
							<div class="text-danger" ng-show="Remittance.receiver.$error.required && Remittance.receiver.$dirty">
								Receiver is required
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.currency' /></b>
						</td>
						<td>
							<form:select id="currency" class="form-control" style="width:95%;display: inline;" path="currency" ng-model="currency" required="required">
								<form:option value="">--- Select ---</form:option>
								<form:option value="TWD"><spring:message code="TWD"/></form:option>
								<form:option value="USD"><spring:message code="USD"/></form:option>																							
							</form:select>
							<span class="text-danger">*</span>
							<div class="text-danger" ng-show="Remittance.currency.$error.required && Remittance.currency.$dirty">
								Currency  is required
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.amount' /></b>
						</td>
						<td>
							<form:input id="amount" class="form-control" style="width:95%;display: inline;" path="amount"
                                ng-model="amount" ng-change="updateBankPayment()"
                                ng-pattern="/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" required="required" />
							<span class="text-danger">*</span>
							<div class="text-danger" ng-show="Remittance.amount.$error.required && Remittance.amount.$dirty">
								Amount is required
							</div>
							<div class="text-danger" ng-show="Remittance.amount.$error.pattern">
								Amount should be a number and greater then zero
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<label class="control-label">
								<spring:message code='remittance.feeAmount' />
							</label>
						</td>
						<td>
							<form:input id="feeAmount" class="form-control" style="width:95%;display: inline;" path="feeAmount"
                                value="0" ng-model="feeAmount" ng-change="updateBankPayment(); updateReference();"
                                ng-pattern="/^0*[0-9]+(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/" required="required" />
							<span class="text-danger">*</span>
							<div class="text-danger" ng-show="Remittance.feeAmount.$error.required && Remittance.feeAmount.$dirty">
								Amount is required
							</div>
							<div class="text-danger" ng-show="Remittance.feeAmount.$error.pattern">
								Amount should be a positive number
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<label class="control-label">
								<spring:message code='remittance.feeIncluded' />
							</label>
						</td>
						<td>
							<form:select id="feeIncluded" class="form-control" style="width:95%;display: inline;" path="feeIncluded"
							    ng-model="feeIncluded" ng-change="updateBankPayment(); updateReference();" >
							    <form:option value="true" label="Yes" />
							    <form:option value="false" label="No" />
							</form:select>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<label class="control-label">
								<spring:message code='remittance.statementName' />
							</label>
						</td>
						<td>
							<form:input id="statementName" class="form-control" style="width:95%;display: inline;" path ="statementName"
							    ng-model="statementName" ng-change="updateReference()" />
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<label class="control-label">
								<spring:message code='remittance.bankPayment' />
							</label>
						</td>
						<td>
							<form:input id="bankPayment" class="form-control" style="width:95%;display: inline;" path ="bankPayment"
							    ng-model="bankPayment" />
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<label class="control-label">
								<spring:message code='remittance.reference' />
							</label>
						</td>
						<td>
							<form:input id="reference" class="form-control" style="width:95%;display: inline;"
							    path ="reference" ng-model="reference" />
						</td>						
					</tr>
					<tr>
						<td colspan="2">
							<spring:message code='remittance.submit' var='submit'/>
							<input class="btn btn-primary" ng-disabled='dateSetted || Remittance.$invalid' type="submit" value="${submit}" style="float: right" />
						</td>
					</tr>
					</table>
				</form:form>				
			</div>
		</div>
	</div>
</div>