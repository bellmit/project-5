<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
  <title>
    <spring:message code="ccc.id" />: ${caseId} -
    <spring:message code="ccc.msg" /> #${Message.lineSeq} - DRS
  </title>
  <style>
    div.d-flex.flex-row.justify-content-between.align-items-center {
      margin-top: 2rem;
    }

    div.row .col-12,
    div.d-flex.flex-row.justify-content-between.align-items-center > div:first-child {
      font-size: 1rem;
      font-weight: bold;
    }

    .card {
      padding: 2rem;
    }

    .text-heading {
      font-size: 1.5rem;
      font-weight: bolder;
    }
  </style>
</head>

<div class="max-width">
  <div class="container-fluid">
    <div class="card">
      <div class="card-body">
        <!-- Case ID -->
        <div class="row">
          <div class="col-md-12">
            <div class="page-heading text-heading">
              <spring:message code="ccc.id" /> : ${caseId} -
              <spring:message code="ccc.msg" />#${Message.lineSeq}
            </div>
          </div>
        </div>
        <hr>
        <!-- Work details -->
        <div class="row">
          <div class="col-12">
            <spring:message code="ccc.workDetails" />
          </div>
        </div>
        <!-- Main -->
        <div class="row">
          <!-- Left column -->
          <div class="col-md-6 pr-md-5">
            <sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_VIEWMSG']}')">
              <div class="d-flex flex-row justify-content-between align-items-center">
                <div>
                  <spring:message code="ccc.MS2SS_statementId" />
                </div>
                <div>
                  <c:choose>
                    <c:when test="${empty Message.ms2ssStatementId}">
                      <c:choose>
                        <c:when test="${Message.isFreeOfCharge eq 'true' }">
                          n/a
                        </c:when>
                        <c:otherwise>
                          (TBD)
                        </c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <c:choose>
                        <c:when test="${Message.isFreeOfCharge eq 'true' }">
                          n/a
                        </c:when>
                        <c:otherwise>
                          <a
                            href="${pageContext.request.contextPath}/MS2SS-Statements/${Message.ms2ssStatementId}">${Message.ms2ssStatementId}</a>
                        </c:otherwise>
                      </c:choose>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </sec:authorize>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.SS2SP_statementId" />
              </div>
              <div>
                <c:choose>
                  <c:when test="${empty Message.ss2spStatementId}">
                    <c:choose>
                      <c:when test="${Message.isFreeOfCharge eq 'true' }">
                        n/a
                      </c:when>
                      <c:otherwise>
                        (TBD)
                      </c:otherwise>
                    </c:choose>
                  </c:when>
                  <c:otherwise>
                    <c:choose>
                      <c:when test="${Message.isFreeOfCharge eq 'true' }">
                        n/a
                      </c:when>
                      <c:otherwise>
                        <a
                          href="${pageContext.request.contextPath}/${statementRootUrl}/${Message.ss2spStatementId}">${Message.ss2spStatementId}</a>
                      </c:otherwise>
                    </c:choose>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.endTime" />
              </div>
              <div>${Message.endDate}</div>
            </div>
            <sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_VIEWMSG']}')">
              <div class="d-flex flex-row justify-content-between align-items-center">
                <div>
                  <spring:message code="ccc.timeTaken" />
                </div>
                <div>${Message.timeTaken}</div>
              </div>
            </sec:authorize>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.relatedSku" />
              </div>
              <div>
                <c:if test="${empty productBaseCodeToSupplierNameMap}">
                  ${Message.chargeToSKU} ${productSkuCodeToSupplierNameMap[Message.chargeToSKU]}
                </c:if>
                <c:if test="${empty productSkuCodeToSupplierNameMap}">
                  ${Message.chargeToSKU} ${productBaseCodeToSupplierNameMap[Message.chargeToSKU]}
                </c:if>
              </div>
            </div>
          </div>
          <!-- Right column -->
          <div class="col-md-6 pl-md-5">
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.wordCount" />
              </div>
              <div>${Message.wordCount}</div>
            </div>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.standardAction" />
              </div>
              <div>${Message.standardActionCount}</div>
            </div>
            <sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_VIEWMSG']}')">
              <div class="d-flex flex-row justify-content-between align-items-center">
                <div>
                  <spring:message code="ccc.chargeByWord" />
                </div>
                <div>${Message.DRSChargeByWord}</div>
              </div>
              <div class="d-flex flex-row justify-content-between align-items-center">
                <div>
                  <spring:message code="ccc.costPerHour" />
                </div>
                <div>${Message.costPerHour}</div>
              </div>
            </sec:authorize>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.handledBy" />
              </div>
              <div>${Message.createBy}</div>
            </div>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.translationFee" />
              </div>
              <div>
                <c:choose>
                  <c:when test="${Message.includesTranslationFee eq 'true' }">
                    <spring:message code="ccc.yes" />
                  </c:when>
                  <c:otherwise>
                    <spring:message code="ccc.no" />
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
            <div class="d-flex flex-row justify-content-between align-items-center">
              <div>
                <spring:message code="ccc.freeOfCharge" />
              </div>
              <div>
                <c:choose>
                  <c:when test="${Message.isFreeOfCharge eq 'true' }">
                    <spring:message code="ccc.yes" />
                  </c:when>
                  <c:otherwise>
                    <spring:message code="ccc.no" />
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
