<%@ page session="false" %>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>SS to SP Revenue share from sales & returns - DRS</title>
</head>
<table class="table table-bordered">
	<caption>Revenue share from sales & returns</caption>
	<thead>
		<tr>	
			<th class="text-center">Transaction time UTC</th>
			<th class="text-center">Related product</th>
			<th class="text-center">Item name</th>
			<th class="text-center">Item description</th>
			<th class="text-center">Source item</th>
			<th class="text-center">Source currency</th>
			<th class="text-center">Source amount</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${itemList}" var="disp">
			<tr>
				<td>${disp.transactionTimeUtc}</td>
				<td>${disp.sku}</td>
				<td>${disp.name}</td>
				<td>${disp.description}</td>
				<td>${disp.sourceName}</td>
				<td>${disp.currency}</td>
				<td>${disp.amount}</td>
			</tr>
		</c:forEach>
		<c:forEach items="${totalMap.keySet()}" var="key">
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<th>Total ${key}</th>
				<th>${totalMap.get(key)}</th>
			</tr>
		</c:forEach>
	</tbody>
</table>