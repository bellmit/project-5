<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
  <title>
    <spring:message code='header.inventoryShipments' /> - DRS
  </title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
  <!-- <link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet" /> -->
  <link rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css"
    integrity="sha256-nbyata2PJRjImhByQzik2ot6gSHSU4Cqdz5bNYL2zcU=" crossorigin="anonymous" />
  <!-- <link rel="stylesheet" href="../../../resources/css/select2-bootstrap4.min.css"> -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
  <script language="javascript" type="text/javascript">
    /* New floatThead event */
    // Windows' y value is inverted to Unix like OSes
    document.addEventListener("scroll", (e) => {
      const $table = $("#InventoryShipments");
      const os = window.navigator.userAgent;
      if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
        if ($table.offset().top >= 0) {
          $table.floatThead();
        } else {
          $table.floatThead("destroy");
        }
      } else {
        if ($table.offset().top <= 0) {
          $table.floatThead();
        } else {
          $table.floatThead("destroy");
        }
      }
    }, true);

    $(function () {
      /* if ($(window).width() >= 1024) {
        $('#InventoryShipments').floatThead({
          scrollingTop: $("#s5_menu_wrap").height(),
          zIndex: 100
        });
        $('#InventoryShipments').on("floatThead", function (e, isFloated, $floatContainer) {
          if (isFloated) {
            $floatContainer.addClass("table-floated");
          } else {
            $floatContainer.removeClass("table-floated");
          }
        })
      }; */
      $("#sellerSelector").select2({
        theme: "bootstrap"
      });
    });
  </script>
  <style>
    thead {
      background: #fff;
    }

    #ivs-title {
      font-size: 2rem;
    }

    #shipmentStatus {
      width: 200px;
    }

    .card {
      padding: 3rem;
    }

    .table-responsive {
      overflow: hidden;
    }

    @media only screen and (max-width: 576px) {
      #shipmentStatus {
        width: 100%;
      }
    }
  </style>
</head>

<div class="max-width">
  <div class="text-center text-success">
    <h1>${message}</h1>
  </div>
  <div class="container-fluid">

    <!-- Card start -->
    <div class="card">
      <div class="card-body">
        <!-- Old IVS title -->
        <!-- <div class="row">
          <div class="col-md-6">
            <div id="ivs-title" class="page-heading">
              <spring:message code='header.inventoryShipments' />
            </div>
          </div>
        </div> -->
        <c:url var="action" value="/InventoryShipments"></c:url>

        <!-- List of IVS form -->
        <form:form class="form-inline mb-3" action="${action}" method="GET" name="InventoryShipment"
          modelAttribute="ShipmentIvsSearchCondition">

          <sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_SEARCH']}')">
            <label for="sellerSelector" class="control-label mr-2">
              <spring:message code="inventoryShipment.seller" /></label>
            <form:select id="sellerSelector" path="sellerCompanyKcode" class="form-control mr-2 ">
              <form:option value="">All</form:option>
              <c:forEach var="sellerKcodeToName" items="${sellerKcodeToNameMap}">
                <form:option value="${sellerKcodeToName.key}">${sellerKcodeToName.key} ${sellerKcodeToName.value}
                </form:option>
              </c:forEach>
            </form:select>
          </sec:authorize>

          <label class="control-label my-2 mr-2 ml-md-2">
            <spring:message code="inventoryShipment.marketRegion" /></label>
          <form:select path="destinationCountry" class="form-control mr-md-2">
            <form:option value="">All</form:option>
            <c:forEach var="destinationCountry" items="${destinationCountryList}">
              <form:option value="${destinationCountry}">
                <spring:message code="${destinationCountry}" />
              </form:option>
            </c:forEach>
          </form:select>

          <label class="control-label my-2 mr-2">
            <spring:message code="inventoryShipment.shippingMethod" /></label>
          <form:select path="shippingMethod" class="form-control mr-md-2">
            <form:option value="">All</form:option>
            <c:forEach var="shippingMethod" items="${shippingMethodList}">
              <form:option value="${shippingMethod}">
                <spring:message code="${shippingMethod}" />
              </form:option>
            </c:forEach>
          </form:select>

          <label class="control-label my-2 mr-2">
            <spring:message code="inventoryShipment.status" /></label>
          <form:select id="shipmentStatus" path="status" class="form-control mr-md-2">
            <form:option value="">All</form:option>
            <c:forEach var="shipmentStatus" items="${shipmentStatusList}">
              <form:option value="${shipmentStatus}">
                <spring:message code="${shipmentStatus}" />
              </form:option>
            </c:forEach>
          </form:select>
          <!-- Search button -->
          <div class="float-right">
            <button class="btn btn-primary mr-3 ml-2 float-right" type="submit"><i class="fas fa-search"></i>
              <spring:message code="inventoryShipment.search" /></button>
          </div>
          <!-- To "Create" page -->
          <sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_CREATE']}')">
            <div class="text-right">
              <div class="page-heading">
                <a class="btn btn-primary m-0" href="${pageContext.request.contextPath}/InventoryShipments/create">
                  <spring:message code="inventoryShipment.createInventoryShipment" />
                </a>
              </div>
            </div>
          </sec:authorize>
        </form:form>

        <hr class="my-4">

        <div class="row">
          <div class="col-md-12">
            <!-- Pagination top -->
            <c:choose>
              <c:when test="${totalPages > 1}">
                <c:url value="InventoryShipments" var="URL">
                  <c:forEach var="IvsSearchCondition" items="${IvsSearchConditions}">
                    <c:param name="${IvsSearchCondition.key}" value="${IvsSearchCondition.value}" />
                  </c:forEach>
                </c:url>
                <c:url var="firstUrl" value="${URL}&page=1" />
                <c:url var="lastUrl" value="${URL}&page=${totalPages}" />
                <c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
                <c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
                <div class="text-center">
                  <nav>
                    <ul class="pagination pagination-sm justify-content-center">
                      <c:choose>
                        <c:when test="${currentPageIndex != 1}">
                          <li class="page-item"><a class="page-link" href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
                          <li class="page-item"><a class="page-link" href="${prevUrl}"
                              data-pager="${currentPageIndex - 1}">&lt;</a></li>
                        </c:when>
                      </c:choose>
                      <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <c:url var="pageUrl" value="${URL}&page=${i}" />
                        <c:choose>
                          <c:when test="${i == currentPageIndex}">
                            <li class="active page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}">
                                <c:out value="${i}" /></a></li>
                          </c:when>
                          <c:otherwise>
                            <li class="page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}">
                                <c:out value="${i}" /></a></li>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                      <c:choose>
                        <c:when test="${currentPageIndex != totalPages}">
                          <li class="page-item"><a class="page-link" href="${nextUrl}"
                              data-pager="${currentPageIndex + 1}">&gt;</a></li>
                          <li class="page-item"><a class="page-link" href="${lastUrl}"
                              data-pager="${totalPages}">&gt;&gt;</a></li>
                        </c:when>
                      </c:choose>
                    </ul>
                  </nav>
                </div>
              </c:when>
            </c:choose>

            <!-- Table start -->
            <div class="table-responsive">
              <table id="InventoryShipments" class="table table-striped table-floated">
                <thead>
                  <tr>
                    <th>
                      <spring:message code='inventoryShipment.inventoryShipmentId' />
                    </th>
                    <th>
                      <spring:message code='inventoryShipment.invoiceNumber' />
                    </th>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <th>
                        <spring:message code='inventoryShipment.seller' />
                      </th>
                      <th>
                        <spring:message code='inventoryShipment.buyer' />
                      </th>
                    </sec:authorize>
                    <th class="text-center">
                      <spring:message code='inventoryShipment.FCADeliveryDate' />
                    </th>
                    <th class="text-center">
                      <spring:message code='inventoryShipment.expectedExportDate' />
                    </th>
                    <th class="text-center">
                      <spring:message code='inventoryShipment.marketRegion' />
                    </th>
                    <th class="text-center">
                      <spring:message code='inventoryShipment.shippingMethod' />
                    </th>
                    <th class="text-center">
                      <spring:message code='inventoryShipment.status' />
                    </th>
                    <th class="text-right">
                      <spring:message code='inventoryShipment.totalAmount' />
                    </th>
                    <th class="text-right">
                      <spring:message code='inventoryShipment.paid' />
                    </th>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <th>UNS</th>
                    </sec:authorize>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${ShipmentOfInventoryList}" var="shipmentOfInventory">
                    <tr>
                      <td>
                        <a
                          href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}">${shipmentOfInventory.name}</a>
                      </td>
                      <td>
                        <c:choose>
                          <c:when test="${empty shipmentOfInventory.invoiceNumber}">
                            <span class="text-danger">
                              <spring:message code='inventoryShipment.waitingInvoice' /></span>
                          </c:when>
                          <c:otherwise>
                            ${shipmentOfInventory.invoiceNumber}
                          </c:otherwise>
                        </c:choose>
                      </td>
                      <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                        <td>${shipmentOfInventory.sellerCompanyKcode}
                          ${AllCompanyKcodeToNameMap[shipmentOfInventory.sellerCompanyKcode]}</td>
                        <td>${shipmentOfInventory.buyerCompanyKcode}
                          ${AllCompanyKcodeToNameMap[shipmentOfInventory.buyerCompanyKcode]}</td>
                      </sec:authorize>
                      <td class="text-center">${shipmentOfInventory.fcaDeliveryDate}</td>
                      <td class="text-center">${shipmentOfInventory.expectedExportDate}</td>
                      <td class="text-center">
                        <spring:message code="${shipmentOfInventory.destinationCountry}" />
                      </td>
                      <td class="text-center">
                        <spring:message code="${shipmentOfInventory.shippingMethod}" />
                      </td>
                      <td class="text-center">
                        <spring:message code="${shipmentOfInventory.status}" />
                      </td>
                      <td class="text-right">${shipmentOfInventory.total}</td>
                      <td class="text-right"><a
                          href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}/InventoryPayment">${shipmentOfInventory.paidTotal}</a>
                      </td>
                      <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                        <td class="text-right"><a
                            href="${pageContext.request.contextPath}/UnifiedShipments/${shipmentOfInventory.unsName}/view">${shipmentOfInventory.unsName}</a>
                        </td>
                      </sec:authorize>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            <!-- Table end -->

            <!-- Pagination bottom -->
            <c:choose>
              <c:when test="${totalPages > 1}">
                <c:url value="InventoryShipments" var="URL">
                  <c:forEach var="IvsSearchCondition" items="${IvsSearchConditions}">
                    <c:param name="${IvsSearchCondition.key}" value="${IvsSearchCondition.value}" />
                  </c:forEach>
                </c:url>
                <c:url var="firstUrl" value="${URL}&page=1" />
                <c:url var="lastUrl" value="${URL}&page=${totalPages}" />
                <c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
                <c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
                <nav>
                  <ul class="pagination pagination-sm justify-content-center">
                    <c:choose>
                      <c:when test="${currentPageIndex != 1}">
                        <li class="page-item"><a class="page-link" href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
                        <li class="page-item"><a class="page-link" href="${prevUrl}"
                            data-pager="${currentPageIndex - 1}">&lt;</a></li>
                      </c:when>
                    </c:choose>
                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                      <c:url var="pageUrl" value="${URL}&page=${i}" />
                      <c:choose>
                        <c:when test="${i == currentPageIndex}">
                          <li class="active page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}">
                              <c:out value="${i}" /></a></li>
                        </c:when>
                        <c:otherwise>
                          <li class="page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}">
                              <c:out value="${i}" /></a></li>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                    <c:choose>
                      <c:when test="${currentPageIndex != totalPages}">
                        <li class="page-item"><a class="page-link" href="${nextUrl}"
                            data-pager="${currentPageIndex + 1}">&gt;</a></li>
                        <li class="page-item"><a class="page-link" href="${lastUrl}"
                            data-pager="${totalPages}">&gt;&gt;</a>
                        </li>
                      </c:when>
                    </c:choose>
                  </ul>
                </nav>
              </c:when>
            </c:choose>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
