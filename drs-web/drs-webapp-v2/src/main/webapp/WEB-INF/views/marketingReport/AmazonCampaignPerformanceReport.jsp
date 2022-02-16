<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Import campaign performance report - DRS</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	jQuery(window).on("load", function(e) {

		jQuery("#startDateInput,#endDateInput").datepicker({
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
			},
			dateFormat : 'yy-mm-dd'
		});

	});
	
	var app = angular.module('CampaignPerformanceReport', []);
	app.controller('CampaignPerformanceReportCtrl',function($scope) {
		
		$scope.CampaignPerformanceReportBriefLineItems = [];						
						
		$scope.findBriefCampaignInformation = function(index) {
			
			var campaignPerformanceReportMap = null
													
			$.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/CampaignPerformanceReportList/findBriefCampaignInformation/',
				contentType : "application/json; charset=utf-8",
				data : {
					page : index,
					marketplaceId : $("#marketplaceId").val(),
					utcDateStart : $("#startDateInput").val(),
					utcDateEnd : $("#endDateInput").val(),
					sku : $("#sku").val()
				},
				dataType : "json",
				success : function(data) {
					
					campaignPerformanceReportMap = data;
					var campaignPerformanceReportData = campaignPerformanceReportMap.data;
					
					$scope.CampaignPerformanceReportBriefLineItems.splice(0,$scope.CampaignPerformanceReportBriefLineItems.length)
					$scope.$apply();
					
					for(var campaignPerformanceReport in campaignPerformanceReportData){
						
						if (campaignPerformanceReportData.hasOwnProperty(campaignPerformanceReport)) {
						
							$scope.CampaignPerformanceReportBriefLineItems.push({Name:campaignPerformanceReportData[campaignPerformanceReport].campaignName,
								  AdGroup:campaignPerformanceReportData[campaignPerformanceReport].adGroupName,	
								  SKU:campaignPerformanceReportData[campaignPerformanceReport].advertisedSku,
								  Keyword:campaignPerformanceReportData[campaignPerformanceReport].keyword,
								  MatchType:campaignPerformanceReportData[campaignPerformanceReport].matchType,
								  StartDate:campaignPerformanceReportData[campaignPerformanceReport].startDate,
								  EndDate:campaignPerformanceReportData[campaignPerformanceReport].endDate,
								  Clicks:campaignPerformanceReportData[campaignPerformanceReport].clicks				  
							});
													
						}
						
					}
					
					$scope.$apply();
					
					$(".pagination").empty();
					
					var first = "1";
					var last = campaignPerformanceReportMap.totalPages;
					var prev = Number(campaignPerformanceReportMap.currentPageIndex) -1;
					var next = Number(campaignPerformanceReportMap.currentPageIndex) +1;
										
					if(campaignPerformanceReportMap.totalPages > 1){
					
					if(campaignPerformanceReportMap.currentPageIndex != 1){
						$(".pagination").append("<li><a href='#' onclick='angular.element(this).scope().findBriefCampaignInformation(1)'>&lt;&lt;</a></li>");														
						$(".pagination").append("<li><a href='#' onclick='angular.element(this).scope().findBriefCampaignInformation("+prev+")'>&lt;</a></li>");																										
					}					
					
					for (i = 1; i <= campaignPerformanceReportMap.totalPages; i++) { 
					   
						if(i == campaignPerformanceReportMap.currentPageIndex){						
							$(".pagination").append("<li class='active'><a href='#' onclick='angular.element(this).scope().findBriefCampaignInformation("+i+")'>" + i + "</a></li>");														
						}else{													
							$(".pagination").append("<li><a href='#' onclick='angular.element(this).scope().findBriefCampaignInformation("+i+")'>" + i + "</a></li>");							
						}
																							
					}
					
					if(campaignPerformanceReportMap.currentPageIndex != campaignPerformanceReportMap.totalPages){
						$(".pagination").append("<li><a href='#' onclick='angular.element(this).scope().findBriefCampaignInformation("+next+")'>&gt;</a></li>");														
						$(".pagination").append("<li><a href='#' onclick='angular.element(this).scope().findBriefCampaignInformation("+last+")'>&gt;&gt;</a></li>");																																					
					}
					
					}
															
				}

			});
						
		};
				
	});	
</script>
</head>
<div class="max-width" ng-app="CampaignPerformanceReport" ng-controller="CampaignPerformanceReportCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Import campaign performance report</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<c:url var="action" value="CampaignPerformanceReportList/uploadFile"></c:url>

				<form method="POST" enctype="multipart/form-data" action="${action}">
					<table>
						<tr>
							<td class="h4">Marketplace</td>
							<td class="h4">Type</td>
							<td class="h4">Report file</td>
							<td></td>
						</tr>
						<tr>
							<td>
								<select class="form-control" name="marketplaceId" style="width:150px;">
								<option value="">---select---</option>
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}">${marketplace.name}</option>
								</c:forEach>
								</select>
							</td>
	                        <td>
								<select class="form-control" name="importType" style="width:150px;">
								<option value="0">DCP</option>
								<option value="1">ECM</option>
								</select>
							</td>
							<td><input type="file" name="file"></td>
							<td><input class="btn btn-primary" type="submit" value="Import" onclick="this.disabled=true;this.value='Sending, please wait...';this.form.submit();"></td>

						</tr>
					</table>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Result</div>
			</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div id="result" class="panel-body">
						<div id="message" class="text-success">${message}</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Find Brief Campaign Information</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-center"><label class="control-label">Marketplace</label>
						<td class="text-center"><label class="control-label">Start date (UTC)</label></td>
						<td class="text-center"><label class="control-label">End date (UTC)</label></td>
						<td class="text-center"><label class="control-label">SKU</label></td>
						<td></td>
					</tr>
					<tr>
						<td>
						<select id="marketplaceId" class="form-control" name="marketplaceId" style="width:150px;">
							<option value="">---select---</option>
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}">${marketplace.name}</option>
							</c:forEach>
						</select>
						</td>
						<td><input id="startDateInput" class="form-control"></td>
						<td><input id="endDateInput" class="form-control"></td>
						<td><input id="sku" class="form-control"></td>
						<td><a class="btn btn-primary" ng-click="findBriefCampaignInformation(1)">Find</a></td>
					</tr>
				</table>			
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th>Name</th>
							<th>Ad group</th>
							<th>SKU</th>
							<th>Keyword</th>
							<th class="text-center">Match type</th>
							<th class="text-center">Start date</th>
							<th class="text-center">End date</th>
							<th class="text-right">Clicks</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="CampaignPerformanceReportBriefLineItem in CampaignPerformanceReportBriefLineItems">
							<td>{{CampaignPerformanceReportBriefLineItem.Name}}</td>
							<td>{{CampaignPerformanceReportBriefLineItem.AdGroup}}</td>
							<td>{{CampaignPerformanceReportBriefLineItem.SKU}}</td>
							<td>{{CampaignPerformanceReportBriefLineItem.Keyword}}</td>
							<td class="text-center">{{CampaignPerformanceReportBriefLineItem.MatchType}}</td>
							<td class="text-center">{{CampaignPerformanceReportBriefLineItem.StartDate}}</td>
							<td class="text-center">{{CampaignPerformanceReportBriefLineItem.EndDate}}</td>
							<td class="text-right">{{CampaignPerformanceReportBriefLineItem.Clicks}}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">				
				<center>	
				<nav>
					<ul class="pagination pagination-sm justify-content-center">
					</ul>
				</nav>				
				</center>
			</div>		
		</div>
	</div>
</div>