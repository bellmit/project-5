<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
	<title>Calculate monthly storage fee - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Calculate monthly storage fee</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<form action="CalculateMonthlyStorageFee/calculate" method="post">
					<label class="control-label">Year</label>
					<select id="year" class="form-control" name="year" style="width:12%;display: inline;">
						<option value="">---select---</option>
						<c:forEach items="${yearList}" var="year">
							<option value="${year}">${year}</option>
						</c:forEach>
					</select>
					<label class="control-label">Month</label>
					<select id="month" class="form-control" name="month" style="width:12%;display: inline;">
						<option value="">---select---</option>
						<c:forEach items="${monthList}" var="month">
							<option value="${month}">${month}</option>
						</c:forEach>							
					</select>
					<input class="btn btn-primary" type="submit" value="Submit" onclick="this.disabled=true;this.value='Sending, please wait...';this.form.submit();">				
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
	</div>
</div>