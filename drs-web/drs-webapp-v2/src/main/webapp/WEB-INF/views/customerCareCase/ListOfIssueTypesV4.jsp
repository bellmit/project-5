<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<header>
  <title>
    <spring:message code='issue.issueType' /> - DRS</title>
  <style>
    .card {
      padding: 2rem;
    }

    .btn-drs {
      padding: 0.6rem 1rem;
    }

    .text-heading {
      font-size: 1rem !important;
      font-weight: bolder !important;
    }
  </style>
</header>

<div class="max-width">
  <!-- <div class="text-center text-success">
    <h1>${message}</h1>
  </div> -->
  <div class="container-fluid">
    <div class="card">
      <div class="card-body">
        <div class="row">
          <div class="col-md-6">
            <!-- <div class="page-heading">
              <spring:message code='issue.issueType' />
            </div> -->
          </div>
          <div class="col-md-6 text-right">
            <div class="page-heading">
              <a class="btn btn-primary btn-drs" href="${pageContext.request.contextPath}/Issues/createIssueType">
                <spring:message code='issue.createIssueType' />
              </a>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="text-heading pl-3">
                    <spring:message code='issue.issueCategory' />
                  </th>
                  <th class="text-heading">
                    <spring:message code='issue.issueType' />
                  </th>
                  <th class="pr-3"></th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${IssueCategoryToIssueTypeAndIdMap}" var="IssueCategoryToIssueType">
                  <c:forEach items="${IssueCategoryToIssueType.value}" var="IssueType">
                    <tr>
                      <td class="pl-3">
                        <spring:message code='issue.${categoryIdToNameMap[IssueCategoryToIssueType.key]}' />
                      </td>
                      <td>${IssueType.value}</td>
                      <td class="text-right pr-3">
                        <a class="btn btn-default btn-sm"
                          href="${pageContext.request.contextPath}/Issues/editIssueType?categoryId=${IssueCategoryToIssueType.key}&typeId=${IssueType.key}">
                          <spring:message code='issue.edit' />
                        </a>
                      </td>
                    </tr>
                  </c:forEach>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
