 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@ page contentType="text/html; charset=utf-8"%> 
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

   <footer class="footer footer-black  footer-white ">
        <div class="container-fluid">
          <div class="row">
            <nav class="footer-nav">
              <ul>
                <li>
                 <a href="<spring:message code='Joomla_root_link' />/ecom_knowledge"><spring:message code='header.amazon-guide' /></a>
                </li>
                <li>
                  <a href="<spring:message code='Joomla_root_link' />/blog"><spring:message code='header.blog' /></a>
                </li>
                 <sec:authorize access="hasAnyRole('${auth['COMPANIES_LIST']}')">
                <li>
                  <a href="${pageContext.request.contextPath}/Companies"><spring:message code='header.companies' /></a>
                </li>
                </sec:authorize>
                <li>
                	<a href="${pageContext.request.contextPath}/SupplementalLinks"><spring:message code='header.links' /></a>
                </li>
                 <sec:authorize access="hasAnyRole('${auth['SPECIALIST_CONTACT']}')">
                 <li>
                   <a href="https://docs.google.com/document/d/1B_Sy3g-uNqiUq5OojcC57ihqrLkR6beR1zksFqmCpTs/pub"><spring:message code='header.specialist-contact' /></a>
                 </li>
                 </sec:authorize>
              </ul>
            </nav>
            <div class="credits ml-auto">
              <span class="copyright">
                ©
                <script>
                  document.write(new Date().getFullYear())
                </script>. KindMinds Innovations, Inc. 善恩創新股份有限公司<br>
                                             					電話: +886-2-2837-8995 | 傳真: +886-2-2837-6257<br>
                                             					Email: info@tw.drs.network | 地址: 11158 台北市士林區德行西路45號6樓
              </span>


            </div>
          </div>
        </div>

      </footer>


