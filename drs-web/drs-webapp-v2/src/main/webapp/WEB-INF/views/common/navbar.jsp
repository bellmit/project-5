<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>


  <!-- Navbar -->
      <nav class="navbar navbar-expand-lg navbar-absolute fixed-top navbar-transparent">
        <div class="container-fluid">
          <div class="navbar-wrapper">
            <div class="navbar-minimize">
              <button id="minimizeSidebar" class="btn btn-icon btn-round">
                <i class="nc-icon nc-minimal-right text-center visible-on-sidebar-mini"></i>
                <i class="nc-icon nc-minimal-left text-center visible-on-sidebar-regular"></i>
              </button>
            </div>
            <div class="navbar-toggle">
              <button type="button" class="navbar-toggler">
                <span class="navbar-toggler-bar bar1"></span>
                <span class="navbar-toggler-bar bar2"></span>
                <span class="navbar-toggler-bar bar3"></span>
              </button>
            </div>
            <!--<a class="navbar-brand" href="#pablo"></a>-->
          </div>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navigation" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-bar navbar-kebab"></span>
            <span class="navbar-toggler-bar navbar-kebab"></span>
            <span class="navbar-toggler-bar navbar-kebab"></span>
          </button>
          <div class="collapse navbar-collapse justify-content-end" id="navigation">

            <ul class="navbar-nav">


                 <li class="nav-item btn-rotate dropdown">
                              <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="nc-icon nc-circle-10"></i>
                                <p>
                                  <span class="d-lg-none d-md-block">Some Actions</span>
                                </p>
                              </a>
                              <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">

                          		<a class="dropdown-item" href="#">${user.userDisplayName}</a>
                          		<div class="dropdown-divider"></div>


                                  <sec:authorize access="hasAnyRole('${auth['COMPANIES_NAVIGATION']}')">
                                        <sec:authorize access="hasAnyRole('${auth['COMPANIES_VIEW_BY_ONE']}')">
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/Company/view"><spring:message code='header.companyInfoMaintenance' /> </a>
                                        </sec:authorize>
                                        <sec:authorize access="hasAnyRole('${auth['COMPANIES_EDIT_BY_ONE']}')">
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/Company/edit"><spring:message code='header.companyInfoMaintenance' /></a>
                                        </sec:authorize>
                                  </sec:authorize>

								<sec:authorize access="hasAnyRole('${auth['ARTICLE_MANAGEMENT']}')">
            					    <a class="dropdown-item" href="<spring:message code='Joomla_root_link' />/account/article-management"><spring:message code='header.article-management' /></a>
            					</sec:authorize>

            					<a class="dropdown-item" href="<c:url value="/resources/files/DRSChannelParticipationAgreement-zh_TW.pdf"/>"><spring:message code='header.agreement' /></a>

            					<sec:authorize access="hasAnyRole('${auth['DRS_TYPOGRAPHY']}')">
            					    <a class="dropdown-item" href="<spring:message code='Joomla_root_link' />/account/drs_typography"><spring:message code='header.drs-typography' /></a>
            				    </sec:authorize>


                                <a class="dropdown-item" 	href="<c:url value="/logout" />"><spring:message code='header.logout' /></a>
                              </div>
                            </li>


            </ul>
          </div>
        </div>
      </nav>
      <!-- End Navbar -->