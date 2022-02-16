<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>${title} - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div style="margin: 0px 15%;">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading">${title}</div>
				</div>
			</div>
			<c:url var="action" value="${actionURL}"></c:url>
			<form:form action="${action}" name="SupplementalLink" modelAttribute="SupplementalLink">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-withoutBorder">
							<tr>
								<td class="text-right"><b>Hyperlink name</b></td>
								<td><form:input class="form-control" path="name" /></td>
							</tr>
							<tr>
								<td class="text-right"><b>Hyperlink URL</b></td>
								<td><form:input class="form-control" path="url" /></td>
							</tr>
							<tr>
								<td class="text-right"><b>Description</b></td>
								<td><form:input class="form-control" path="description" /></td>
							</tr>
							<tr>
								<td class="text-right"><b>apply to</b></td>
								<td>
									<form:select class="form-control" style="width:50%;" path="supplierKcode">
										<form:option value="">All</form:option>
										<c:forEach items="${SupplierKcodeList}" var="SupplierKcode">
										<form:option value="${SupplierKcode}">${SupplierKcode}</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
					<div class="col-md-12 text-right">						
						<form:hidden path="id" />							
						<input class="btn btn-primary" type="submit" value="Submit" style="float: right" />
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>