<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>Shopify report - DRS</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>

	var app = angular.module('ShopifyReport', []);
	
	app.controller('ShopifyReportCtrl',function($scope) {
		$scope.orderReportFileNameArray = [];
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/ShopifyReport/listOrderReportFiles/',
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				orderReportFileNameListFromUco = data;
				for ( var orderReportFileName in orderReportFileNameListFromUco) {
					if (orderReportFileNameListFromUco.hasOwnProperty(orderReportFileName)) {
						$scope.orderReportFileNameArray.push({fileName:orderReportFileNameListFromUco[orderReportFileName]});
					}
				}
				$scope.$apply();
			}
		});
		
		$scope.paymentTransactionReportFileNameArray = [];
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/ShopifyReport/listPaymentTransactionReportFiles/',
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				paymentTransactionReportFileNameListFromUco = data;
				for ( var paymentTransactionReportFileName in paymentTransactionReportFileNameListFromUco) {
					if (paymentTransactionReportFileNameListFromUco.hasOwnProperty(paymentTransactionReportFileName)) {
						$scope.paymentTransactionReportFileNameArray.push({fileName:paymentTransactionReportFileNameListFromUco[paymentTransactionReportFileName]});
					}
				}
				$scope.$apply();
			}
		});

		$scope.salesReportFileNameArray = [];
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/ShopifyReport/listSalesReportFiles/',
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				salesReportFileNameListFromUco = data;	
				for( var salesReportFileName in salesReportFileNameListFromUco){
					if (salesReportFileNameListFromUco.hasOwnProperty(salesReportFileName)) {
						$scope.salesReportFileNameArray.push({fileName:salesReportFileNameListFromUco[salesReportFileName]});
					}
				}
				$scope.$apply();
			}					
		});
		
	});
</script>
</head>
<div class="max-width" ng-app="ShopifyReport" ng-controller="ShopifyReportCtrl">
	<div class="container-fluid">		
		<div class="row"><div class="col-md-12"><div class="page-heading">Upload Shopify report</div></div></div>
		<div class="row">
			<div class="col-md-6">
				<c:url var="action" value="/ShopifyReport/uploadFile"></c:url>
				<form method="POST" enctype="multipart/form-data" action="${action}">
					<table>
						<tr>
							<td><input class="btn btn-default" type="file" name="file"></td>
							<td><input class="btn btn-primary" type="submit" value="Upload"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>	
		<div class="row"><div class="col-md-12"><div style="font-size:20px;color:#333333;">Order report files</div></div></div>
		<div class="row">
			<div class="col-md-6">
				<table class="table">
					<thead>
						<tr>
							<th>File Name</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="file in orderReportFileNameArray">
							<td>{{file.fileName}}</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/ShopifyReport/importOrderReport/{{file.fileName}}">Import</a>
							</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/ShopifyReport/deleteOrderReport/{{file.fileName}}">Delete</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row"><div class="col-md-12"><div style="font-size:20px;color:#333333;">Payment transaction report files</div></div></div>
		<div class="row">
			<div class="col-md-6">
				<table class="table">
					<thead>
						<tr>
							<th>File Name</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="file in paymentTransactionReportFileNameArray">
							<td>{{file.fileName}}</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/ShopifyReport/importPaymentTransactionReport/{{file.fileName}}">Import</a>
							</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/ShopifyReport/deletePaymentTransactionReport/{{file.fileName}}">Delete</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row"><div class="col-md-12"><div style="font-size:20px;color:#333333;">Sales report files</div></div></div>
		<div class="row">
			<div class="col-md-6">
				<table class="table">
					<thead>
						<tr>
							<th>File Name</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="file in salesReportFileNameArray">
							<td>{{file.fileName}}</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/ShopifyReport/importSalesReport/{{file.fileName}}">Import</a>
							</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/ShopifyReport/deleteSalesReport/{{file.fileName}}">Delete</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>