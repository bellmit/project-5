<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
	<title>Error - DRS</title>
<script>
	var t = 5;		
	function showTime(){
		t -= 1;		    
		if(t==0){
			location.href='${pageContext.request.contextPath}/';
		}		    		    
		setTimeout("showTime()",1000);
	}		
	showTime();		
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Error</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p>
					Oops, we've encountered an error and page will be redirected to home after 5 seconds!
				</p>				
			</div>
		</div>
	</div>
</div>