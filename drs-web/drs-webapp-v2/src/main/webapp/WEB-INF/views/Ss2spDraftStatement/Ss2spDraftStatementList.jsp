<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<head>
<title>Produce SS2SP draft settlement - DRS</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
/*
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
*/

	angular.module('SS2SPdraftSettlement', []).controller('SS2SPdraftSettlementCtrl',function($scope) {																															
		$scope.SS2SPDraftStatementLineItems = [];						
		var ss2spDraftStatementList = ${ss2spDraftStatementList};							
		for(var ss2spDraftStatement in ss2spDraftStatementList['items']){			
			if (ss2spDraftStatementList['items'].hasOwnProperty(ss2spDraftStatement)) {		  	
				$scope.SS2SPDraftStatementLineItems.push({
					startTimeUTC:ss2spDraftStatementList['items'][ss2spDraftStatement].periodStartUtc,
					endTimeUTC:ss2spDraftStatementList['items'][ss2spDraftStatement].periodEndUtc,
					statementId:ss2spDraftStatementList['items'][ss2spDraftStatement].statementId,
					currency:ss2spDraftStatementList['items'][ss2spDraftStatement].currency,
					total:ss2spDraftStatementList['items'][ss2spDraftStatement].total,
					balance:ss2spDraftStatementList['items'][ss2spDraftStatement].balance});
			}		
		}

		$scope.createDraft = function() {
			var supplierKcode = $("#supplierKcode").val();
			var ajaxUrl = '${pageContext.request.contextPath}/DraftSettlement/createSs2spDraftStatements/';
			var message = "";
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					supplierKcode : supplierKcode
				},
				dataType : "json",
				success : function(data) {
					message = data;
					$("#message").html(message);
					$scope.getSs2spDraftStatementList();
				}
			});
		};
						
		$scope.createDraftForAll = function() {
			//var startDateInput = $("#startDateInput").val();
			//var endDateInput = $("#endDateInput").val();
			$.getJSON(
				"${pageContext.request.contextPath}/DraftSettlement/createSs2spDraftStatementForAll/",
				{},
				function( data ) {
					message = data;
					$("#message").html(message);
					$scope.getSs2spDraftStatementList();
			});
		};
		
		$scope.deleteAllDraft = function() {
			$.getJSON(
				"${pageContext.request.contextPath}/DraftSettlement/deleteAllDraft/",
				function( data ) {
					$scope.getSs2spDraftStatementList();
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
						location.href = "${pageContext.request.contextPath}/ss2spdraftstatements/commitAll";
					}
				},
				{
					text : "No",
					name : "no",
					click : function() { $(this).dialog('close'); }
				}					]
			});
		};
						
		$scope.getSs2spDraftStatementList = function(){			
			var ajaxUrl = '${pageContext.request.contextPath}/DraftSettlement/getSs2spDraftStatementList/';
			var ss2spDraftStatementList = null;										
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",								
				dataType : "json",
				success : function(data) {
					ss2spDraftStatementList = data														
					$scope.SS2SPDraftStatementLineItems.splice(0,$scope.SS2SPDraftStatementLineItems.length)
					$scope.$apply();														 																											
					for(var ss2spDraftStatement in ss2spDraftStatementList['items']){						
						if (ss2spDraftStatementList['items'].hasOwnProperty(ss2spDraftStatement)) {						
					  		$scope.SS2SPDraftStatementLineItems.push({
					  			startTimeUTC:ss2spDraftStatementList['items'][ss2spDraftStatement].periodStartUtc,
					  			endTimeUTC:ss2spDraftStatementList['items'][ss2spDraftStatement].periodEndUtc,
					  			statementId:ss2spDraftStatementList['items'][ss2spDraftStatement].statementId,
					  			currency:ss2spDraftStatementList['items'][ss2spDraftStatement].currency,
					  			total:ss2spDraftStatementList['items'][ss2spDraftStatement].total,
					  			balance:ss2spDraftStatementList['items'][ss2spDraftStatement].balance});
						}						
					}					
					$scope.$apply();					
				}
			});																	
		};																										
	});
</script>
</head>
<div class="max-width" ng-app="SS2SPdraftSettlement" ng-controller="SS2SPdraftSettlementCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Create SS2SP draft statements</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<label class="control-label">Supplier</label>
				<select id="supplierKcode" class="form-control" style="width:25%;display: inline;">
					<option value="">--- Select ---</option>
					<c:forEach var="supplierKcode" items="${supplierKcodeToCompanyNameMap}">
						<option value="${supplierKcode.key}">${supplierKcode.key} ${supplierKcode.value}</option>
					</c:forEach>
				</select>
				<a class="btn btn-primary" ng-click="createDraft()">Create</a>
				<a class="btn btn-info" ng-click="createDraftForAll()">Create For All Supplier</a>
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
				<div class="page-heading">Draft Statements</div>
			</div>
		</div>
		<div class="row mb-3"> 
		<a class="btn btn-default" ng-click="deleteAllDraft()">Delete All</a>
		<a class="btn btn-danger" ng-click="confirmAndCommitAllDraft()">Commit All</a>		
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center"><spring:message code='ss2spStatement.periodStart' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.periodEnd' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.statementId' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.total' /></th>
							<th class="text-right"><spring:message code='ss2spStatement.stmntTotalAmount' /></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="SS2SPDraftStatementItem in SS2SPDraftStatementLineItems">		
							<td class="text-center">{{SS2SPDraftStatementItem.startTimeUTC}}</td>
							<td class="text-center">{{SS2SPDraftStatementItem.endTimeUTC}}</td>
							<td class="text-center">
								<a href="${pageContext.request.contextPath}/${rootUrl}/{{SS2SPDraftStatementItem.statementId}}">{{SS2SPDraftStatementItem.statementId}}</a>
							</td>
							<td class="text-right">{{SS2SPDraftStatementItem.currency}} {{SS2SPDraftStatementItem.total}}</td>
							<td class="text-right">{{SS2SPDraftStatementItem.currency}} {{SS2SPDraftStatementItem.balance}}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div id="dialog-confirm"></div>	
	</div>
</div>