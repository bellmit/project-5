<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />		
		<meta http-equiv="Cache-Control" content="no-cache, must-revalidate, no-store" />
		<meta http-equiv="Pragma" content="no-cache" />        
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

        <link href="<c:url value="/resources/images/drs-favicon.ico"/>" rel="shortcut icon" type="image/vnd.microsoft.icon">


        <link href="<c:url value="/resources/css/bootstra-3.0.2.min.css"/>" rel="stylesheet">
		<link href="<c:url value="/resources/css/font-awesome-3.2.1.min.css"/>" type="text/css" rel="stylesheet">

        <link href="<c:url value="/resources/css/drs.css"/>" type="text/css" rel="stylesheet">

        <script type='text/javascript' src="<c:url value="/resources/js/jquery-1.9.1.min.js"/>"></script>
        <script type='text/javascript' src="<c:url value="/resources/js/bootstrap-3.0.2.min.js"/>"></script>
        <script type='text/javascript' src="<c:url value="/resources/js/jquery-fileupload/vendor/jquery.ui.widget.js"/>"></script>
		<script type='text/javascript' src="<c:url value="/resources/js/jquery-fileupload/jquery.iframe-transport.js"/>"></script>
		<script type='text/javascript' src="<c:url value="/resources/js/jquery-fileupload/jquery.fileupload.js"/>"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.validate-1.11.1.js"></script>		

        <link rel="shortcut icon" href="/bootstrap/img/favicon.ico">
        <link rel="apple-touch-icon" href="/bootstrap/img/apple-touch-icon.png">
        <link rel="apple-touch-icon" sizes="72x72" href="/bootstrap/img/apple-touch-icon-72x72.png">
        <link rel="apple-touch-icon" sizes="114x114" href="/bootstrap/img/apple-touch-icon-114x114.png">		           

    	<script type="text/javascript">
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
				(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
				m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
				})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
				ga('create', 'UA-71702187-1', 'auto');
				ga('send', 'pageview');								
   	 	</script>   

    </head>
  	<body>        	
   		<tiles:insertAttribute name="body" /> 
    </body>
</html>