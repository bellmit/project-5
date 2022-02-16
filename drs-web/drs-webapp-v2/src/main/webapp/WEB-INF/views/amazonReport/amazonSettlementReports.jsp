<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>Amazon Settlement Report - DRS</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="<c:url value="/resources/css/jquery-fileUpload/jquery.fileupload-ui.css"/>" rel="stylesheet">
<script>
	angular.module('AmazonReport', []).controller('AmazonReportCtrl',function($scope) {
		var settlementReportInfoList = ${settlementReportInfoList};
		$("#amazonSettlementReport").find('tbody').empty();
		for(i=0; i<settlementReportInfoList.length; i++){									
			$("#amazonSettlementReport").find('tbody')
			.append($('<tr>')
				.append($('<td class="text-center">').append(settlementReportInfoList[i].dateStart))
				.append($('<td class="text-center">').append(settlementReportInfoList[i].dateEnd))
				.append($('<td class="text-center">').append(settlementReportInfoList[i].settlementId))
				.append($('<td class="text-center">').append(settlementReportInfoList[i].currency))
				.append($('<td class="text-left">').append(settlementReportInfoList[i].sourceMarketplace))
	    	);
		}
	});
</script>
</head>
<div class="max-width" ng-app="AmazonReport" ng-controller="AmazonReportCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Select Amazon Settlement Report file to upload...</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-7">
				<c:url var="action" value="/amazon-settlement-reports/uploadFileAndImport"></c:url>
				<form method="POST" enctype="multipart/form-data" action="${action}">
					<table class="no-head">
						<tr>
							<td>
								<input type="file" name="file">
							</td>
							<td>
								<select class="form-control" name="marketplaceId" style="width:150px;">
									<option value="">--- Select ---</option>
									<c:forEach items="${marketplaces}" var="marketplace">
										<option value="${marketplace.key}" >${marketplace.name}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input class="btn btn-primary" type="submit" value="Upload And Import">
							</td>
						</tr>
					</table>
					<span>${message}</span>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Amazon Settlement Report V2 Info List</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="amazonSettlementReport" class="table">
					<thead>
						<tr>
							<th class="text-center">Date Start UTC</th>
							<th class="text-center">Date End UTC</th>
							<th class="text-center">Settlement Id</th>
							<th class="text-center">Currency</th>
							<th class="text-left">Marketplace</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>