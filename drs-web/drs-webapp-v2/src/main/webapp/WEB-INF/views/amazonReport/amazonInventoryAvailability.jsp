<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<head>
<title>
	<spring:message code="inventoryAvailability.title" /> - DRS
</title>
<script>
	jQuery(window).on("load", function(e) {
		jQuery("#date").datepicker({
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
			},
			dateFormat : 'yy-mm-dd',
			maxDate: 0
		});		
		$('#year').val(${year});
		$('#month').val(${month});		
	});
	
	function clearConfirm(date) {
		$("#dialog-confirm").html("Are you sure to clear this data?");
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();
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
						location.href = "${pageContext.request.contextPath}/updateProductSkuFBAInventoryAvailability/"+date+"/clear";
				}
			},
			{
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
				<div class="page-heading">
					<spring:message code="inventoryAvailability.title" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-9">	
				<span class="text-info">
					<b><spring:message code="inventoryAvailability.hint" /></b>
				</span>
				<div style="padding-bottom: 10px"></div>												
				<form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/updateProductSkuFBAInventoryAvailability/update">
					<table class="no-head">
						<tr>
							<td><input type="file" name="file"></td>							
							<td style="padding-right:10px">
								<spring:message code="inventoryAvailability.date" /> 
								<input id="date" class="form-control" name="date" style="display:inline;width:150px;">							
							</td>
							<td><input class="btn btn-primary" type="submit" value="<spring:message code="inventoryAvailability.update" />" ></td>
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
						<span class="text-success">
							&nbsp${message}
						</span>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					Updated history
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form method="POST" action="${pageContext.request.contextPath}/updateProductSkuFBAInventoryAvailability">
					<table class="no-head">
						<tr>
							<td style="padding-right:10px"><spring:message code="inventoryAvailability.year" /></td>
							<td style="padding-right:10px">
								<select id="year" class="form-control" name="year" style="display:inline;width:150px;">
								<option value="">---select---</option>								
								<c:forEach items="${yearList}" var="year">
								<option value="${year}">${year}</option>
								</c:forEach>								
								</select>
							</td>
							<td style="padding-right:10px"><spring:message code="inventoryAvailability.month" /></td>
							<td style="padding-right:10px">
							<select id="month" class="form-control" name="month" style="display:inline;width:150px;">
								<option value="">---select---</option>								
								<c:forEach items="${monthList}" var="month">
								<option value="${month}">${month}</option>
								</c:forEach>								
							</select>
							</td>
							<td style="padding-right:10px"><input class="btn btn-primary" type="submit" value="<spring:message code="inventoryAvailability.search" />" ></td>
						</tr>
					</table>
				</form>	
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-6">
				<span class="text-info"><b>(CLEAR: Inventory Availability is already updated. Click here to clear it as NULL.)</b></span>
				<div style="padding-bottom: 10px"></div>
				<table class="table" style="padding:10px">
					<thead>
						<tr>
							<th></th>
							<th class="text-center">History</th>												
						</tr>														
					</thead>
					<tbody>
					<c:forEach items="${updateStatuses}" var="updateStatus">
						<tr>
							<td class="text-center">${updateStatus.key}</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${updateStatus.value eq true}">
							    		<input style="padding:0px 12px;" class="btn btn-link" type="button" value="CLEAR" onclick="clearConfirm('${updateStatus.key}');" />
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>			
							</c:choose>
							</td>						
						</tr>	
					</c:forEach>																									
					</tbody>
				</table>
				<div id="dialog-confirm"></div>
			</div>
		</div>	
	</div>
</div>	