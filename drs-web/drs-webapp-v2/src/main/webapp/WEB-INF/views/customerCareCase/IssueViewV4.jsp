<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
  <title><spring:message code="issue.title" /> - DRS </title>
  <link href="<c:url value='/resources/css/bootstrap3-glyphicons.css'/>" type="text/css" rel="stylesheet">
  <link href="<c:url value='/resources/css/bootstrap3-wysihtml5.css'/>" type="text/css" rel="stylesheet">
  <style>
    .card-body .row:first-child > .col-md-6 > div {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 3rem;
    }

    .card-body .row:first-child > .col-md-6 > div > div:first-child {
      font-size: 1.7rem;
      font-weight: bolder;
    }

    .btn-drs {
      padding: 0.6rem 1rem;
    }

    .heading-txt {
      font-size: 1.7rem;
      font-weight: bolder;
    }

    .card-padding {
      padding: 2rem;
    }

    #contentsRow > div > ul > li.dropdown > a svg,
    #contentsRow > div > ul > li.dropdown > a b {
      display: none;
    }

    ul.dropdown-menu {
      margin: 0;
    }

    ul.dropdown-menu::before,
    ul.dropdown-menu::after {
      display: none !important;
    }

    iframe,
    textarea {
      padding: 1rem !important;
    }
  </style>
  <script type="text/javascript" src="<c:url value='/resources/js/bootstrap3-wysihtml5.all.js'/>"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
</head>

<div class="max-width" ng-app="issueView" ng-controller="issueViewCtrl">
  <div class="text-center" style="color: #FF0000">
    <h1>${message}</h1>
  </div>
  <div class="container-fluid">
    <div class="card card-padding">
      <div class="card-body">
        <!-- <div class="row">
          <div class="col-md-12">
            <div class="page-heading">
              <spring:message code="issue.title" />
            </div>
          </div>
        </div> -->

        <!-- Table -->
        <div class="row">
          <div class="col-md-6 pr-md-5">
            <div>
              <div><spring:message code="issue.issueCategory" /></div>
              <div><spring:message code="issue.${categoryIdToNameMap[issue.categoryId]}" /></div>
            </div>
            <div>
              <div><spring:message code="issue.issueType" /></div>
              <div>${typeIdToNameMap[issue.typeId]}</div>
            </div>
            <div>
              <div><spring:message code="issue.issueName" /></div>
              <div>
                <c:forEach var="localeCodeToName" items="${issue.localeCodeToNameMap}">
                  ${localeCodeToName.key.fullName} ${localeCodeToName.value}<br>
                </c:forEach>
              </div>
            </div>
            <div>
              <div><spring:message code="issue.status" /></div>
              <div><spring:message code="issue.${issue.status}" /></div>
            </div>
            <!-- <table class="table no-head">
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.issueCategory" /></b>
                </td>
                <td>
                  <spring:message code="issue.${categoryIdToNameMap[issue.categoryId]}" />
                </td>
              </tr>
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.issueType" /></b>
                </td>
                <td>${typeIdToNameMap[issue.typeId]}</td>
              </tr>
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.issueName" /></b>
                </td>
                <td>
                  <c:forEach var="localeCodeToName" items="${issue.localeCodeToNameMap}">
                    ${localeCodeToName.key.fullName} ${localeCodeToName.value}<br>
                  </c:forEach>
                </td>
              </tr>
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.status" /></b>
                </td>
                <td>
                  <spring:message code="issue.${issue.status}" />
                </td>
              </tr>
            </table> -->
          </div>
          <div class="col-md-6 pl-md-5">
            <sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
              <div>
                <div><spring:message code="issue.supplier" /></div>
                <div>
                  <c:choose>
                    <c:when test="${!empty issue.supplierKcode}">
                      <a href="${pageContext.request.contextPath}/Companies/${issue.supplierKcode}" target="_blank">
                        ${issue.supplierKcode} ${supplierKcodeToShortEnUsNameMap[issue.supplierKcode]}
                      </a>
                    </c:when>
                    <c:otherwise>
                      <spring:message code="issue.anyForSupplier" />
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </sec:authorize>
            <div>
              <div><spring:message code="issue.relatedProducts" /></div>
              <div>
                <c:if test="${empty issue.relatedProductSkuCodeList}">
                  <c:forEach items="${issue.relatedProductBaseCodeList}" var="ProductBase">
                    <a href="${pageContext.request.contextPath}/BaseProduct/${ProductBase}" target="_blank">${ProductBase}
                      ${productBaseCodeToSupplierNameMap[ProductBase]}
                    </a><br>
                  </c:forEach>
                </c:if>
                <c:if test="${empty issue.relatedProductBaseCodeList}">
                  <c:forEach items="${issue.relatedProductSkuCodeList}" var="sku">
                    <a href="${pageContext.request.contextPath}/SKUs/${sku}" target="_blank">${sku}
                      ${productSkuCodeToSupplierNameMap[sku]}
                    </a><br>
                  </c:forEach>
                </c:if>
              </div>
            </div>
            <div>
              <div><spring:message code="issue.templateOccurrences" /></div>
              <div>
                <a href="${pageContext.request.contextPath}/CustomerCareCases/search?searchWords=issueId:${issue.id}">
                  ${issue.templateOccurrences}
                </a>
              </div>
            </div>
            <div>
              <div><spring:message code="issue.created" /></div>
              <div>${issue.createdDate} UTC</div>
            </div>
            <!-- <table class="table no-head">
              <sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
                <tr>
                  <td class="text-right">
                    <b>
                      <spring:message code="issue.supplier" /></b>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${!empty issue.supplierKcode}">
                        <a href="${pageContext.request.contextPath}/Companies/${issue.supplierKcode}"
                          target="_blank">${issue.supplierKcode} ${supplierKcodeToShortEnUsNameMap[issue.supplierKcode]}</a>
                      </c:when>
                      <c:otherwise>
                        <spring:message code="issue.anyForSupplier" />
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </sec:authorize>
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.relatedProducts" /></b>
                </td>
                <td>
                  <c:if test="${empty issue.relatedProductSkuCodeList}">
                    <c:forEach items="${issue.relatedProductBaseCodeList}" var="ProductBase">
                      <a href="${pageContext.request.contextPath}/BaseProduct/${ProductBase}" target="_blank">${ProductBase}
                        ${productBaseCodeToSupplierNameMap[ProductBase]}</a><br>
                    </c:forEach>
                  </c:if>
                  <c:if test="${empty issue.relatedProductBaseCodeList}">
                    <c:forEach items="${issue.relatedProductSkuCodeList}" var="sku">
                      <a href="${pageContext.request.contextPath}/SKUs/${sku}" target="_blank">${sku}
                        ${productSkuCodeToSupplierNameMap[sku]}</a><br>
                    </c:forEach>
                  </c:if>
                </td>
              </tr>
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.templateOccurrences" /></b>
                </td>
                <td>
                  <a href="${pageContext.request.contextPath}/CustomerCareCases/search?searchWords=issueId:${issue.id}">
                    ${issue.templateOccurrences}
                  </a>
                </td>
              </tr>
              <tr>
                <td class="text-right">
                  <b>
                    <spring:message code="issue.created" /></b>
                </td>
                <td>${issue.createdDate} UTC</td>
              </tr>
            </table> -->
          </div>
        </div>

        <!-- Edit -->
        <div class="row">
          <div class="col-md-12 text-right">
            <sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
              <a href="${pageContext.request.contextPath}/Issues/${issue.id}/edit">
                <input class="btn btn-default btn-drs" type="button" value='<spring:message code="issue.edit" />' />
              </a>
            </sec:authorize>
            <sec:authorize access="hasAnyRole('${auth['ISSUES_MAINTAIN']}')">
              <c:if test="${empty issue.comments && empty issue.templates}">
                <input class="btn btn-link btn-drs" type="button" value='<spring:message code="issue.delete" />'
                  onclick="deleteConfirm('${issue.id}');" />
                <div id="dialog-confirm"></div>
              </c:if>
            </sec:authorize>
          </div>
        </div>

        <hr>

        <!-- Create response template -->
        <div class="row mb-3">
          <div class="col-md-6 d-flex align-items-center">
            <div class="page-heading heading-txt">
              <spring:message code="template.responseTemplates" />
            </div>
          </div>
          <div class="col-md-6 text-right">
            <div class="page-heading">
              <sec:authorize access="hasAnyRole('${auth['TEMPLATE_MAINTAIN']}')">
                <a class="btn btn-primary btn-drs"
                  href="${pageContext.request.contextPath}/Issues/${issue.id}/createResponseTemplate">
                  <i class="fas fa-plus"></i>
                  <spring:message code='template.createResponseTemplate' />
                </a>
              </sec:authorize>
            </div>
          </div>
        </div>

        <!-- Applicable -->
        <div class="row">
          <div class="col-md-12">
            <table class="table table-hover table-striped">
              <thead>
                <tr class="bg-white">
                  <th>
                    <spring:message code='template.applicableLanguages' />
                  </th>
                  <th>
                    <spring:message code='template.applicableCase' />
                  </th>
                  <th>
                    <spring:message code='template.applicableMarket' />
                  </th>
                  <th>
                    <spring:message code='template.applicableMarketplaces' />
                  </th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${issue.templates}" var="template" varStatus="templateStatus">
                  <tr class="clickableRow"
                    onclick="document.location.href='${pageContext.request.contextPath}/Template/${template.id}'">
                    <td>
                      <c:forEach items="${template.applicableLanguages}" var="applicableLanguages"
                        varStatus="applicableLanguagesStatus">
                        ${applicableLanguages.fullName}
                        <c:if test="${applicableLanguagesStatus.count != template.applicableLanguages.size()}">,</c:if>
                      </c:forEach>
                    </td>
                    <td>
                      <c:forEach items="${template.applicableCaseTypes}" var="applicableCaseTypes"
                        varStatus="applicableCaseTypesStatus">
                        <spring:message code="ccc.${applicableCaseTypes}" />
                        <c:if test="${applicableCaseTypesStatus.count != template.applicableCaseTypes.size()}">,</c:if>
                      </c:forEach>
                    </td>
                    <td>
                      <c:forEach items="${template.applicableMarketRegions}" var="applicableMarketRegions"
                        varStatus="applicableMarketRegionsStatus">
                        <spring:message code="${applicableMarketRegions}" />
                        <c:if test="${applicableMarketRegionsStatus.count != template.applicableMarketRegions.size()}">,
                        </c:if>
                      </c:forEach>
                    </td>
                    <td>
                      <c:forEach items="${template.applicableMarketplaceList}" var="applicableMarketplaces"
                        varStatus="applicableMarketplacesStatus">
                        ${applicableMarketplaces.name}
                        <c:if test="${applicableMarketplacesStatus.count != template.applicableMarketplaceList.size()}">,
                        </c:if>
                      </c:forEach>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>

        <hr>

        <sec:authorize access="hasAnyRole('${auth['ISSUES_POST_COMMENT']}')">
          <c:url var="postCommentAction" value="/Issues/postComment?issueId=${issue.id}"></c:url>
          <form:form action="${postCommentAction}" name="Comment" class="form-horizontal text-left"
            modelAttribute="Comment">
            <div id="contentsRow" class="row mb-4">
              <div class="col-md-12">
                <form:textarea id="contents" class="form-control" path="contents" placeholder="Leave a comment" ng-model="contents" />
              </div>
            </div>
            <div class="row text-right">
              <div class="col-md-12">
                <sec:authorize access="hasAnyRole('${auth['ISSUES_POST_COMMENT_RESPONSE']}')">
                  <span class="mr-3"><spring:message code="issue.PENDING_SUPPLIER_ACTION" /></span>
                  <spring:message code="issue.yes" />
                  <form:radiobutton path="pendingSupplierAction" value="true" />
                  <spring:message code="issue.no" />
                  <form:radiobutton path="pendingSupplierAction" value="false" checked="TRUE" />
                </sec:authorize>
                <input class="btn btn-success btn-drs ml-5" type="submit" value="<spring:message code='issue.reply'/>" />
              </div>
            </div>
          </form:form>
        </sec:authorize>

        <hr>

        <!-- Discussion heading -->
        <div class="row mb-4">
          <div class="col-md-12">
            <div class="page-heading heading-txt">
              <spring:message code="issue.discussion" />
            </div>
          </div>
        </div>

        <!-- Comments -->
        <c:forEach items="${issue.comments}" var="comment" varStatus="commentStatus">
          <div class="row">
            <div class="col-md-12">
              <div class="card mb-5">
                <div class="card-header bg-light py-3 text-muted">
                  ${comment.createBy} ${comment.dateCreate}
                </div>
                <div class="card-body">
                  <% pageContext.setAttribute("newLineChar", "\n"); %>
                  <p class="card-text">${fn:replace(comment.contents, newLineChar, "<br>")}</p>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
<script>
  function deleteConfirm(issueId) {
    $("#dialog-confirm").html('<spring:message code="issue.deleteIssueWarning" /> ');
    $("#dialog-confirm").dialog({
      open: function () { $('.ui-dialog-buttonset button[name="no"]').focus(); },
      resizable: false,
      modal: true,
      title: "Confirmation",
      height: 250,
      width: 400,
      buttons: [
        {
          text: "<spring:message code='issue.yes' />",
          name: "yes",
          click: function () {
            $(this).dialog('close');
            location.href = "${pageContext.request.contextPath}/Issues/" + issueId + "/delete";
          }
        }, {
          text: "<spring:message code='issue.no' />",
          name: "no",
          click: function () {
            $(this).dialog('close');
          }
        }
      ]
    });
  }

  angular.module('issueView', []).controller('issueViewCtrl', function ($scope) { });

  $('#contents').wysihtml5({
    "events": {
      "load": function () {
        $("#contentsRow .wysihtml5-sandbox").addClass("inspectlet-disable-iframe-inject");
      }
    }, toolbar: {
      "html": true, //Button which allows you to edit the generated HTML. Default false
      "link": false, //Button to insert a link. Default true
      "image": false, //Button to insert an image. Default true,
      "blockquote": false, //Blockquote
      "fa": true
    }
  });
</script>
