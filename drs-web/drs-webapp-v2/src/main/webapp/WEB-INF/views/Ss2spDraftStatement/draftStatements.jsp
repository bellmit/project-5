<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>Draft Statements - DRS</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script type="text/javascript">

	function showConfirmDialog(action,targetUrl) {
		$("#dialog-confirm").html("Sure to "+action+" All?");
		$("#dialog-confirm").dialog({
			open : function(){$('.ui-dialog-buttonset button[name="no"]').focus();},
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
					location.href=targetUrl;
				}
			},
			{
				text : "No",
				name : "no",
				click : function() {$(this).dialog('close');}
			}
		]
	});
}
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
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
				<div class="page-heading">Draft Statements</div>
			</div>
		</div>
		<div class="row mb-3">
		<button class="btn btn-default" onclick="showConfirmDialog('delete', '${pageContext.request.contextPath}/DraftSettlement/deleteAllDraft')" >Delete All</button>
		<sec:authorize access="hasAnyRole('${auth['SS2SP_DRAFT_STATEMENTS_COMMIT']}')">		
			<button class="btn btn-danger"  onclick="showConfirmDialog('confirm','${pageContext.request.contextPath}/ss2spdraftstatements/commitAll')" >Confirm All</button>
		</sec:authorize>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center"><spring:message code='ss2spStatement.periodStart' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.periodEnd' /></th>
							<th class="text-center"><spring:message code='ss2spStatement.statementId' /></th>
							<th class="text-right" ><spring:message code='ss2spStatement.total' /></th>
							<th class="text-right" ><spring:message code='ss2spStatement.stmntTotalAmount' /></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${ss2spDraftStatementList.items}" var="item">
						<tr>		
							<td class="text-center">${item.periodStartUtc}</td>
							<td class="text-center">${item.periodEndUtc}</td>
							<td class="text-center"><a href="${pageContext.request.contextPath}/${rootUrl}/${item.statementId}">${item.statementId}</a></td>
							<td class="text-right">${item.total}</td>
							<td class="text-right">${item.balance}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div id="dialog-confirm"></div>	
	</div>
</div>