<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<head>
<title><spring:message code='emailReminder.title' /></title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js"></script>
</head>
<script>
$(function() {
	$('#includeSuppliers').multiselect({
		disableIfEmpty: true,
		includeSelectAllOption: true,
		maxHeight: 300,
		buttonWidth: '200px',
		numberDisplayed: 3
	});
	$('#excludeSuppliers').multiselect({
		disableIfEmpty: true,
		includeSelectAllOption: true,
		maxHeight: 300,
		buttonWidth: '200px',
		numberDisplayed: 3
	});
	
	$('#selectLimit').val($('#hiddenLimit').val());
	
	$('#sendEmail').on('click', function() {
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/sendEmail"
		});
	});
	
	$('#selectLimit').on('input', function() {
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/updateFeeLimit",
			data : {
				limit : $(this).val()
			}
		});
	});
	
});	
</script>
<style>

.reminder {
	color: #5cb85c;
	font-size: 14px;
}

.vertical-divider {
	border-right: 1px solid lightgray;
}

.center-align {
    padding: 8px;
    text-align: center;
    vertical-align: middle;
}

.arrows input {
	height: 50px;
	width: 100px;
}

#selectLimit {
	width: 100px;
	margin: 0 auto;
}

</style>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading"><spring:message code='emailReminder.header' /></div>
			</div>
		</div>
		<div class="row">	
			<div class="col-md-12">		
				<div class="center-align">
					<h4><spring:message code='emailReminder.ltsfHeader' /></h4>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="center-align reminder">
					<b><i>
						<spring:message code='emailReminder.ltsfDescEmailSent' /><br>
						<spring:message code='emailReminder.ltsfDescChargeDates' />
					</i></b>
					<br>
					<hr>
				</div>
			</div>
		</div>
		<div class="row">
			<form action="emailReminder" method="POST">	
			<div class="col-md-7 vertical-divider">				
				<table class="table table-withoutBorder">
					<tr>
						<td colspan="3">
						<div class="center-align">
							<b>
								<spring:message code='emailReminder.ltsfDescUpdateList' />
							</b>
							<br>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="center-align">
							<b><spring:message code='emailReminder.ltsfIncludedSuppliers' /></b>
						</div>
						</td>
						<td></td>
						<td>
						<div class="center-align">
							<b><spring:message code='emailReminder.ltsfExcludedSuppliers' /></b>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="center-align">
							<select multiple="multiple" class="form-control" id="includeSuppliers" name="includeSuppliers" style="display:inline">
								<c:forEach var="includeKcode" items="${includeSuppliers}">
									<option value="${includeKcode.key}">${includeKcode.key} ${includeKcode.value}</option>
								</c:forEach>
							</select>
						</div>
						</td>
						<td>
						<div class="center-align arrows">
							<input class="btn btn-primary" type="submit" formaction="update" value="&#x226B; Update &#x226A;">
						</div>
						</td>
						<td>
						<div class="center-align">
							<select multiple="multiple" class="form-control" id="excludeSuppliers" name="excludeSuppliers" style="display:inline">
								<c:forEach var="excludeKcode" items="${excludeSuppliers}">
									<option value="${excludeKcode.key}">${excludeKcode.key} ${excludeKcode.value}</option>
								</c:forEach>
							</select>
						</div>
						</td>
					</tr>
				</table>		
			</div>
			<div class="col-md-5 vertical-divider">
				<table class="table table-withoutBorder">
					<tr>
						<td>
						<div class="center-align">
							<b><spring:message code='emailReminder.ltsfDescFeeLimit' /></b>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="center-align">
							<input type="hidden" value="${limit}" id="hiddenLimit" />
							<select class="form-control" name="limit" id="selectLimit" >
								<option value="0.0">0.0</option>
								<option value="20.0">20.0</option>
								<option value="50.0">50.0</option>
								<option value="100.0">100.0</option>
								<option value="250.0">250.0</option>
								<option value="500.0">500.0</option>
							</select>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="center-align">
							<input id="sendEmail" class="btn btn-primary" type="button" value="<spring:message code='emailReminder.ltsfSendEmail' />">
						</div>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
		<hr>
	</div>
</div>		
		