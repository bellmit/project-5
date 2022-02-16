<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
	<title>Access Denied - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Access Denied</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p>
					<spring:message code="accessDeny" /> <a href="${pageContext.request.contextPath}"><spring:message code="accessDeny.backToDashboard" /></a>
				</p>
			</div>
		</div>
	</div>
</div>