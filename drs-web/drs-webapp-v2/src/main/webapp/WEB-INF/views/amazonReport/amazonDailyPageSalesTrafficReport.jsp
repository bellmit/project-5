<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<head>
<title><spring:message code="dailyPageSalesTrafficReport.title" /> - DRS</title>

<script>
	jQuery(window).on("load", function(e) {
		jQuery("#date").datepicker({
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css(
									'z-index', 200);
					}, 0);
				},
				dateFormat : 'yy-mm-dd'
			});		
		$('#year').val(${year});
		$('#month').val(${month});		
	});
	
	function deleteConfirm(marketplaceId,date) {
		$("#dialog-confirm").html("Are you sure to delete this data?");
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
				location.href = "${pageContext.request.contextPath}/AmazonDailyPageSalesTrafficReport/" + marketplaceId +"/"+date+"/delete";
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
					<spring:message code="dailyPageSalesTrafficReport.title" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-9">										
				<form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/AmazonDailyPageSalesTrafficReport/uploadAndImport">
					<table>
						<tr>
							<td><input type="file" name="file"></td>
							<td style="padding-right:10px">
							<spring:message code="dailyPageSalesTrafficReport.marketPlace" />    
							<select class="form-control" name="marketplaceKey" style="display:inline;width:150px;">
								<option value="">---select---</option>								
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}">${marketplace.name}</option>
								</c:forEach>								
							</select>
							</td>

							<td style="padding-right:10px">
							<spring:message code="dailyPageSalesTrafficReport.date" /> 
							<input id="date" class="form-control" name="date" style="display:inline;width:150px;">							
							</td>
							<td style="padding-right:10px">
							   Type
                               <select class="form-control" name="importType" style="width:150px;">
                               <option value="0">DCP</option>
                               <option value="1">ECM</option>
                               </select>
                             </td>
							<td>
							<br>
							<input class="btn btn-primary" type="submit"
							value="<spring:message code='dailyPageSalesTrafficReport.import' />" >
							</td>
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
					History
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form method="POST" action="${pageContext.request.contextPath}/AmazonDailyPageSalesTrafficReport">
					<table>
						<tr>
							<td style="padding-right:10px"><spring:message code="dailyPageSalesTrafficReport.year" /></td>
							<td style="padding-right:10px">
							<select id="year" class="form-control" name="year" style="display:inline;width:150px;">
								<option value="">---select---</option>								
								<c:forEach items="${yearList}" var="year">
									<option value="${year}">${year}</option>
								</c:forEach>								
							</select>
							</td>
							<td style="padding-right:10px"><spring:message code="dailyPageSalesTrafficReport.month" /></td>
							<td style="padding-right:10px">
							<select id="month" class="form-control" name="month" style="display:inline;width:150px;">
								<option value="">---select---</option>								
								<c:forEach items="${monthList}" var="month">
									<option value="${month}">${month}</option>
								</c:forEach>								
							</select>
							</td>
							<td style="padding-right:10px"><input class="btn btn-primary" type="submit" value="<spring:message code="dailyPageSalesTrafficReport.search" />" ></td>
						</tr>
					</table>
				</form>	
			</div>
		</div>
		<div style="padding-bottom: 10px"></div>	
		<div class="row">
			<div class="col-md-6">
				<table class="table" style="padding:10px">
					<thead>
						<tr>
							<th></th>
							<c:forEach items="${marketplaces}" var="marketplace">
								<th class="text-center">${marketplace.name}</th>
							</c:forEach>
						</tr>														
					</thead>
					<tbody>
					<c:forEach items="${importStatuses}" var="importStatus">
						<tr>
							<td class="text-center">${importStatus.key}</td>
							<c:forEach items="${importStatus.value}" var="Status">
							<td class="text-center">
							<c:choose>
								<c:when test="${Status.value eq true}">
							    	<input style="padding:0px 12px;" class="btn btn-link" type="button" value="DELETE" onclick="deleteConfirm('${Status.key.key}','${importStatus.key}');" />
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>			
							</c:choose>							
							</td>
						</c:forEach>										
						</tr>						
					</c:forEach>					
					</tbody>
				</table>
				<div id="dialog-confirm"></div>
			</div>
		</div>
	</div>
</div>