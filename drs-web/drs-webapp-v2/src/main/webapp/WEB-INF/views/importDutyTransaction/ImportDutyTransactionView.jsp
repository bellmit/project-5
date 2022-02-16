<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>Import Duty Transaction - DRS</title>

<script>
	function deleteImportDutyTransaction(unsName) {
		$("#dialog-confirm").html("Are you sure to delete this import duty transaction?");
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
					location.href = "${pageContext.request.contextPath}/ImportDutyTransaction/delete/" + unsName;
				}
			}, {
				text : "No",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});
	}
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Import Duty Transaction</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<table class="table table-withoutBorder table-autoWidth">
					<tr>
						<td class="text-right"><b>Select UNS</b></td>
						<td>${ImportDutyTransaction.unsName}</td>
					</tr>
					<tr>
						<td class="text-right"><b>Date</b></td>
						<td>${ImportDutyTransaction.utcDate}</td>
					</tr>
					<tr>
						<td class="text-right"><b>Dst Country</b></td>
						<td class="text-center">${ImportDutyTransaction.dstCountry}</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8">
				<table class="table">
					<thead>
						<tr style="background-color: #F5F5DC;">
							<th>Source IVS</th>
							<th style="width: 30%;">SKU</th>
							<th class="text-right" style="width: 15%;">Quantity</th>
							<th class="text-right" style="width: 15%;">Amount(${ImportDutyTransaction.currency})</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ImportDutyTransaction.lineItems}" var="lineItem">
							<tr>
								<td>${lineItem.sourceIvsName}</td>
								<td>${lineItem.sku}</td>
								<td class="text-right">${lineItem.quantity}</td>
								<td class="text-right">${lineItem.amount}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tr>
						<td colspan="2"></td>
						<td class="text-right"><b>TOTAL(${ImportDutyTransaction.currency})</b></td>
						<td class="text-right active">${ImportDutyTransaction.total}</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8 text-right">
				<a class="btn btn-primary" href="${pageContext.request.contextPath}/ImportDutyTransaction/edit/${ImportDutyTransaction.unsName}">Edit</a>
				<input class="btn btn-danger" type="button" value="Delete" onclick="deleteImportDutyTransaction('${ImportDutyTransaction.unsName}');">
			</div>
		</div>
		<div id="dialog-confirm"></div>
	</div>
</div>