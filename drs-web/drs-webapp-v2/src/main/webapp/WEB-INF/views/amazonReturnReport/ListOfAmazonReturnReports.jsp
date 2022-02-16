<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>
	Amazon return report - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	angular.module('AmazonReturnReport', []).controller('AmazonReturnReportCtrl',function($scope) {
		$scope.uploadedFiles = [];
		var ajaxUrl = '${pageContext.request.contextPath}/AmazonReturnReport/listUploadedFiles/';
		var uploadedFilesList = null;	
		$.ajax({
			type : 'get',
			url : ajaxUrl,
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				uploadedFilesList = data;
				for ( var uploadedFile in uploadedFilesList) {
					if (uploadedFilesList.hasOwnProperty(uploadedFile)) {
						$scope.uploadedFiles.push({fileName : uploadedFilesList[uploadedFile]});
					}
				}
				$scope.$apply();
			}
		});																				
	});
</script>
</head>
<div class="max-width" ng-app="AmazonReturnReport" ng-controller="AmazonReturnReportCtrl">
	<div class="container-fluid">		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Amazon return report</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<c:url var="action" value="/AmazonReturnReport/uploadFile"></c:url>
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
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Report files</div>
			</div>
		</div>
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
						<tr ng-repeat="uploadedFile in uploadedFiles">
							<td>{{uploadedFile.fileName}}</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/AmazonReturnReport/importFile/{{uploadedFile.fileName}}">
									Import
								</a>
							</td>
							<td>
								<a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/AmazonReturnReport/deleteUploadedFile/{{uploadedFile.fileName}}">
									Delete 
								</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>	
	</div>
</div>