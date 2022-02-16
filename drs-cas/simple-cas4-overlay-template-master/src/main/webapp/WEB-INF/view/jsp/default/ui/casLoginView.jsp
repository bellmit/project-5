<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">


<head>

<title>Login - DRS</title>
<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->
	<link rel="icon" type="image/png" href="images/icons/favicon.ico"/>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bootstrap.min.css"/>">

	<link rel="stylesheet" type="text/css"  href="<c:url value="/resources/css/font-awesome.min.css"/>">
	
	<link rel="stylesheet" type="text/css"  href="<c:url value="/resources/css/material-design-iconic-font.min.css"/>">
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/animate.css"/>">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/hamburgers.min.css"/>">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/animsition.min.css"/>">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/select2.min.css"/>">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/daterangepicker.css"/>">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/util.css"/>">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css"/>">

 	<link href="<c:url value="/resources/images/drs-favicon.ico"/>" rel="shortcut icon" type="image/vnd.microsoft.icon">

	
</head>


<body>


<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-t-5 p-b-20">
			<a></a>
		
			<form:form method="post" id="fm1" commandName="${commandName}"  class="login100-form validate-form">
					
				
					
					<span class="login100-form-avatar">
						<img src="<c:url value="/resources/images/b1.png"/>"  alt="AVATAR">
					</span>

					<div class="wrap-input100 validate-input m-t-35 m-b-35" data-validate = "Enter username">
					 <input class="input100 has-val" type="text" id="username"  name="username" > 
					 <span class="focus-input100" data-placeholder="Username"></span>
					 
							
					</div>

					<div class="wrap-input100 validate-input m-b-50" data-validate="Enter password">
						<input class="input100 has-val" type="password" id="password" name="password">
					
						
						<span class="focus-input100" data-placeholder="Password"></span>
						
						
					</div>
	<div style="text-align: center">
								<p class="text-danger">	
								<form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />
								</p>
								</div>
					<div class="container-login100-form-btn">
								
						   <input class="login100-form-btn" accesskey="l" 
							      	value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
					</div>

					
												    							   
				<div style="text-align: center; margin-bottom: 5%">		
					
					<div class="form-group" style="text-align: center;">  
							      <input type="hidden" name="lt" value="${loginTicket}" />
							      <input type="hidden" name="execution" value="${flowExecutionKey}" />
							      <input type="hidden" name="_eventId" value="submit" />							      							      
							   
							   <!--     <input class="btn btn-large btn-default" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="5" type="reset" />	-->						      
						    </div>		
				</form:form>
			</div>
		</div>
	</div>



	<script src="<c:url value="/resources/js/jquery-3.2.1.min.js"/>"></script>
	<script src="<c:url value="/resources/js/animsition.min.js"/>"></script>
	<script src="<c:url value="/resources/js/popper.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/resources/js/select2.min.js"/>"></script>
	<script src="<c:url value="/resources/js/main.js"/>"></script>

	
</body>
</html>
