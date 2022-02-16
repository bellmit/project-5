<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login - DRS</title>
<link href="<c:url value="/resources/css/bootstra-3.0.2.min.css"/>"
	rel="stylesheet"></link>
<script type='text/javascript'
	src="<c:url value="/resources/js/jquery-1.9.1.min.js"/>"></script>
</head>
<body>
	<div id="cpisdiv">
		<div class="hd"></div>
		<div class="bd">
			<div class="login">
				<div class="container">
					<div class="login-inner"
						style="width: 50%; margin: 50px auto 0px auto; border-width: 1px; border-style: solid; border-color: #DDD; background-color: #F8F8FF">
						<form method="post" action="j_spring_security_check"
							class="form-horizontal">
							<div style="text-align: center">
								<h2 class="form-signin-heading">Log in to DRS</h2>
							</div>
							<div class="form-group" style="margin-right: 10%">
								<label class="col-sm-4 control-label"><spring:message code='login.username' /></label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="j_username" />
								</div>
							</div>
							<div class="form-group" style="margin-right: 10%">
								<label class="col-sm-4 control-label"><spring:message code='login.password' /></label>
								<div class="col-sm-8">
									<input type="password" class="form-control" name="j_password" />
								</div>
							</div>
							<c:if test="${not empty param.error}">
								<center>
									<p class="bg-danger"><spring:message code='login.wrongInfo' /></p>
								</center>
							</c:if>
							<div style="text-align: center; margin-bottom: 5%">
								<button class="btn btn-large btn-primary" type="submit"><spring:message code='login.submit' /></button>
								<button class="btn btn-large btn-default" type="reset"><spring:message code='login.reset' /></button>
								<!-- <div style="margin-left:75%"><a href="?locale=en_US">English</a>|<a href="?locale=zh_TW">繁體中文</a></div> -->
							</div>
						</form>
					</div>
					<!-- end <div class="login-inner"> -->
				</div>
				<!-- <div class="container"> -->
			</div>
			<!-- end <div class="login"> -->
		</div>
		<!-- end <div class="bd"> -->
		<div class="ft"></div>
	</div>
	<!-- end <div id="cpisdiv"> -->
</body>
</html>