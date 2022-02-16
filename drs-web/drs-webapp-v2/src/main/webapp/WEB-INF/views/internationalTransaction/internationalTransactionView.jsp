<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>International transaction</title>
<style>
.list-group-item-info {
	color: #31708f;
	background-color: #d9edf7;
}
</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">International transaction</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<table class="table table-withoutBorder table-centered no-head">
					<tr>
						<td class="text-right"><b>MSDC</b></td>						
						<td>${t.msdcKcode} ${t.msdcName}</td>							
					</tr>
					<tr>
						<td class="text-right"><b>SSDC</b></td>						
						<td>${t.ssdcKcode} ${t.ssdcName}</td>
					</tr>
					<tr>
						<td class="text-right"><b>Supplier</b></td>						
						<td>${t.splrKcode} ${t.splrName}</td>
					</tr>
					<tr>
						<td class="text-right"><b>UTC Date</b></td>						
						<td>${t.utcDate}</td>
					</tr>
					<tr>
						<td class="text-right"><b>Cash flow direction</b></td>						
						<td>${cashFlowDirections[t.cashFlowDirectionKey].displayName}</td>
					</tr>			
					<tr>
						<td class="text-right"><b>Note</b></td>						
						<td>${t.note}</td>
					</tr>					
				</table>		
			</div>
		</div>	
		<div class="row">
			<div class="col-md-12">
				<ul class="list-group">
					<li class="list-group-item list-group-item-info">
  						<div class="row">
							<div class="col-md-7">Item name</div>
							<div class="col-md-3">Note</div>
							<div class="col-md-2 text-right">Amount</div>				  			
  						</div> 			
  					</li>
					<c:forEach items="${t.lineItems}" var="lineItem">				
						<li class="list-group-item">
						<div class="row">	
							<div class="col-md-7">${lineItem.itemName}</div>
							<div class="col-md-3">${lineItem.itemNote}</div>					
							<div class="col-md-2 text-right">${lineItem.subtotal}</div>							
						</div>
						</li>			
					</c:forEach>						
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8"></div>
			<div class="col-md-2 text-right"></div>
			<div class="col-md-2 text-right" style="padding-right:30px">
				Total(${t.currency}) ${t.total}
			</div>					
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12 text-right">
			<c:if test="${t.isEditable() eq true}">
				<a class="btn btn-primary" href="${pageContext.request.contextPath}/InternationalTransaction/edit/${t.id}">Edit</a>																				
			</c:if>
			<a class="btn btn-link" href="${pageContext.request.contextPath}/InternationalTransactions">Back</a>																														
			</div>
		</div>	
	</div>
</div>	