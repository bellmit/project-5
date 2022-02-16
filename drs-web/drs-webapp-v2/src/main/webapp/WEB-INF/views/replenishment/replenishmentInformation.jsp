<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<spring:message code="replenishmentInformation.title" /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	angular.module('replenishmentInfo', []).controller('replenishmentInfoCtrl', function($scope) {	
		var ReplenishmentInfoJson = ${ReplenishmentInfoJson};
		var lineItems = ReplenishmentInfoJson.lineItems;
		$scope.lineItems = [];		
		for (i = 0; i < lineItems.length; i++) { 
			$scope.lineItems.push({
				warehouseId:lineItems[i].warehouseId,
				warehouseName:lineItems[i].warehouseName,
				daysSpentForAmazonReceiving:lineItems[i].daysSpentForAmazonReceiving,
				daysSpentForSpwCalculation:lineItems[i].daysSpentForSpwCalculation,
				daysSpentForCourier:lineItems[i].daysSpentForCourier,
				daysSpentForAirFreight:lineItems[i].daysSpentForAirFreight,
				daysSpentForSurfaceFreight:lineItems[i].daysSpentForSurfaceFreight
			});				
		}				
	});	
</script>
</head>
<div class="max-width" ng-app="replenishmentInfo" ng-controller="replenishmentInfoCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="replenishmentInformation.title" />
				</div>
			</div>
		</div>
		<form:form action="${pageContext.request.contextPath}/replenishment-information/save" name="ReplenishmentInfo" modelAttribute="ReplenishmentInfo">							
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th><spring:message code="replenishmentInformation.warehouseName" /></th>
							<th><spring:message code="replenishmentInformation.daysSpentForAmazonReceiving" /></th>
							<th><spring:message code="replenishmentInformation.daysSpentForSpwCalculation" /></th>
							<th><spring:message code="replenishmentInformation.daysSpentForCourier" /></th>
							<th><spring:message code="replenishmentInformation.daysSpentForAirFreight" /></th>
							<th><spring:message code="replenishmentInformation.daysSpentForSurfaceFreight" /></th>							
						</tr>
					</thead>
					<tbody>
						<tr id="item{{$index}}" ng-repeat="item in lineItems">
							<td>
								{{item.warehouseName}}
								<input name="lineItem[{{$id}}].warehouseId" type="hidden" ng-model="item.warehouseId" value ="{{item.warehouseId}}">				
								<input name="lineItem[{{$id}}].warehouseName" type="hidden" ng-model="item.warehouseName" value ="{{item.warehouseName}}">									
							</td>
							<td>
								<input name="lineItem[{{$id}}].daysSpentForAmazonReceiving" class="form-control" ng-model="item.daysSpentForAmazonReceiving" required>												
							</td>
							<td>
								<input name="lineItem[{{$id}}].daysSpentForSpwCalculation" class="form-control" ng-model="item.daysSpentForSpwCalculation" required>												
							</td>
							<td>
								<input name="lineItem[{{$id}}].daysSpentForCourier" class="form-control" ng-model="item.daysSpentForCourier" required>					
							</td>
							<td>
								<input name="lineItem[{{$id}}].daysSpentForAirFreight" class="form-control" ng-model="item.daysSpentForAirFreight" required>													
							</td>
							<td>
								<input name="lineItem[{{$id}}].daysSpentForSurfaceFreight" class="form-control" ng-model="item.daysSpentForSurfaceFreight" required>										
							</td>						
						</tr>
					</tbody>	
				</table>							
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-right">
				<input class="btn btn-primary" type="submit" value="<spring:message code="replenishmentInformation.save" />" ng-disabled="ReplenishmentInfo.$invalid" />										
			</div>
		</div>
		</form:form>					
	</div>
</div>