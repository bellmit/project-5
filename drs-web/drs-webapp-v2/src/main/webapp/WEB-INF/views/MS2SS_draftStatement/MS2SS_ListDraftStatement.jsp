<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Produce MS2SS draft settlement - DRS</title>


<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
jQuery(window).on("load", function(e) {
		jQuery("#startDateInput,#endDateInput").datepicker({
			beforeShow: function() {
				setTimeout(function(){
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
				    	},
			dateFormat : 'yy-mm-dd'
		});				
	});
	angular.module('MS2SSdraftSettlement', []).controller('MS2SSdraftSettlementCtrl',function($scope) {			
		$scope.MS2SSDraftStatementLineItems = [];	
		var ms2ssDraftStatementList = ${statements};			
		for(var ms2ssDraftStatement in ms2ssDraftStatementList){		    			    
			if (ms2ssDraftStatementList.hasOwnProperty(ms2ssDraftStatement)){		    	
				$scope.MS2SSDraftStatementLineItems.push({
					startTimeUTC:ms2ssDraftStatementList[ms2ssDraftStatement].periodStartUtc,				
					endTimeUTC:ms2ssDraftStatementList[ms2ssDraftStatement].periodEndUtc,	
					statementId:ms2ssDraftStatementList[ms2ssDraftStatement].statementId,
					currency:ms2ssDraftStatementList[ms2ssDraftStatement].currency,
					balance:ms2ssDraftStatementList[ms2ssDraftStatement].balance});				
		    }							
		}										
		$scope.createDraft = function() {
			var Issuer = $("#Issuer").val();
			var Receiver = $("#Receiver").val();
			var startDateInput = $("#startDateInput").val();
			var endDateInput = $("#endDateInput").val();
			var ajaxUrl = '${pageContext.request.contextPath}/ms2ssdraftstatements/createMs2ssDraftStatements/';
			var message = "";				
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					Issuer : Issuer,
					Receiver : Receiver,
					utcDateStart : startDateInput,
					utcDateEnd : endDateInput
				},
				dataType : "json",
				success : function(data) {
					message = data;
					$("#message").html(message);								
					$scope.getMs2ssDraftStatementList();								
				}
			});
		};
			
		$scope.deleteAllDraft = function() {
			$.getJSON("${pageContext.request.contextPath}/ms2ssdraftstatements/deleteAllDraft/",function( data ) {
				$scope.getMs2ssDraftStatementList();
			});
		};
			
		$scope.confirmAndCommitAllDraft = function() {
			$("#dialog-confirm").html("Are you sure to commit all listed draft statements?");
			$("#dialog-confirm").dialog({
				open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
				resizable : false,
				modal : true,
				title : "Confirmation",
				height : 200,
				width : 300,
				buttons : [
					{
						text : "Yes",
						name : "yes",
						click : function() {
							$(this).dialog('close');
							location.href = "${pageContext.request.contextPath}/ms2ssdraftstatements/commitAll";
						}
					},
					{
						text : "No",
						name : "no",
						click : function() { $(this).dialog('close'); }
					}
				]
			});
		};
						
		$scope.getMs2ssDraftStatementList = function(){				
			var ajaxUrl = '${pageContext.request.contextPath}/ms2ssdraftstatements/getMs2ssDraftStatementList/';
			var ms2ssDraftStatementList = null;											
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",								
				dataType : "json",
				success : function(data) {
					ms2ssDraftStatementList = data;						
					$scope.MS2SSDraftStatementLineItems.splice(0,$scope.MS2SSDraftStatementLineItems.length)
					$scope.$apply();						
					for(var ms2ssDraftStatement in ms2ssDraftStatementList){						
						if (ms2ssDraftStatementList.hasOwnProperty(ms2ssDraftStatement)){							
							$scope.MS2SSDraftStatementLineItems.push({
								startTimeUTC:ms2ssDraftStatementList[ms2ssDraftStatement].periodStartUtc,							
								endTimeUTC:ms2ssDraftStatementList[ms2ssDraftStatement].periodEndUtc,	
								statementId:ms2ssDraftStatementList[ms2ssDraftStatement].statementId,
								currency:ms2ssDraftStatementList[ms2ssDraftStatement].currency,
								balance:ms2ssDraftStatementList[ms2ssDraftStatement].balance});							
						}																												
					}							
					$scope.$apply();						
				}
			});
																		
		};
						
	});
</script>
</head>
<div class="max-width" ng-app="MS2SSdraftSettlement" ng-controller="MS2SSdraftSettlementCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Produce MS2SS draft settlement</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<label class="control-label">Issuer</label> 
				<select id="Issuer" class="form-control" style="width:15%;display: inline;">
					<option value="">--- Select ---</option>					
					<c:forEach var="drsCompanyKcode" items="${drsCompanyKcodeToEnUsNameMap}">
						<option value="${drsCompanyKcode.key}">${drsCompanyKcode.key} ${drsCompanyKcode.value}</option>
					</c:forEach>
				</select> 
				<label class="control-label">Receiver</label> 
				<select id="Receiver" class="form-control" style="width:15%;display: inline;">
					<option value="">--- Select ---</option>					
					<c:forEach var="drsCompanyKcode" items="${drsCompanyKcodeToEnUsNameMap}">
						<option value="${drsCompanyKcode.key}">${drsCompanyKcode.key} ${drsCompanyKcode.value}</option>
					</c:forEach>
				</select> 											
				<label class="control-label">Start Date Input</label> 
				<input id="startDateInput" class="form-control" style="width:15%;display: inline;"> 
				<label class="control-label">End Date Input</label> 
				<input id="endDateInput" class="form-control" style="width:15%;display: inline;"> 
				<a class="btn btn-primary" ng-click="createDraft()">create draft</a>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Result</div>
			</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div id="result" class="panel-body">
						<div id="message" class="text-success">
							${message}
						</div>
					</div>
				</div>
			</div>
		</div>		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Draft MS2SS Statements</div>			
			</div>
		</div>
		<a class="btn btn-default" ng-click="deleteAllDraft()">Delete All</a>
		<a class="btn btn-danger" ng-click="confirmAndCommitAllDraft()">Commit All</a>			
		<div class="row">
			<div class="col-md-12">	
				<table class="table">
					<thead>
						<tr>
							<th class="text-center">Period start UTC</th>
							<th class="text-center">Period end UTC</th>
							<th class="text-center">Statement ID</th>
							<th class="text-right">Total</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="MS2SSDraftStatementItem in MS2SSDraftStatementLineItems">		
							<td class="text-center">{{MS2SSDraftStatementItem.startTimeUTC}}</td>
							<td class="text-center">{{MS2SSDraftStatementItem.endTimeUTC}}</td>
							<td class="text-center"><a href="${pageContext.request.contextPath}/${rootUrl}/{{MS2SSDraftStatementItem.statementId}}">{{MS2SSDraftStatementItem.statementId}}</a></td>
							<td class="text-right">{{MS2SSDraftStatementItem.currency}} {{MS2SSDraftStatementItem.balance}}</td>		
						</tr>	
					</tbody>
				</table>
			</div>			
		</div>
		<div id="dialog-confirm"></div>					
	</div>
</div>