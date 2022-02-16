 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@ page contentType="text/html; charset=utf-8"%> 
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="max-width">
<footer>
	<div class="container-fluid" style="border-top:lightgray 1px solid;padding-top:10px;">
  		<div class="row"> 		  
    		<div class="col-md-3">
    			<a href="<spring:message code='Joomla_root_link' />/ecom_knowledge"><spring:message code='header.amazon-guide' /></a><br/>
    			<a href="<spring:message code='Joomla_root_link' />/blog"><spring:message code='header.blog' /></a><br/>
				<!-- <a href="<spring:message code='Joomla_root_link' />/faq"><spring:message code='header.question' /></a><br/> -->
			</div>
			<div class="col-md-3">
				<sec:authorize access="hasAnyRole('${auth['COMPANIES_LIST']}')">			
					<a href="${pageContext.request.contextPath}/Companies"><spring:message code='header.companies' /></a><br/>
				</sec:authorize>
				<a href="${pageContext.request.contextPath}/SupplementalLinks"><spring:message code='header.links' /></a><br/>		    	
				<sec:authorize access="hasAnyRole('${auth['SPECIALIST_CONTACT']}')">						
					<a href="https://docs.google.com/document/d/1B_Sy3g-uNqiUq5OojcC57ihqrLkR6beR1zksFqmCpTs/pub"><spring:message code='header.specialist-contact' /></a>
				</sec:authorize>
    		</div>	 	
    		<div class="col-md-6">
				<address>		
					善恩創新股份有限公司  KindMinds Innovations, Inc.<br>
					電話: +886-2-2837-8995 | 傳真: +886-2-2837-6257<br>
					Email: info@tw.drs.network | 地址: 11158 台北市士林區德行西路45號6樓<br><br>
					Copyright © 2019. KindMinds Innovations, Inc.<br>
    			</address>
    		</div>       	    	    	  	   	    		    		   	    	
    	</div>
  	</div>  
</footer>
</div>