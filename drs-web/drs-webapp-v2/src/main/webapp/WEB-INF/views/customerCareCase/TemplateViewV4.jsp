<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="template.responseTemplate" />: ${customerCareCaseIssueTemplate.issueName} - DRS
</title>

<script>
	function deleteConfirm(issueId,templateId) {
		$("#dialog-confirm").html("<spring:message code="template.deleteTemplateWarning" /> ");
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
			{
				text : "<spring:message code='template.yes' />",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/Issues/"+ issueId +"/"+templateId+ "/delete";
				}
			}, {
				text : "<spring:message code='template.no' />",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});	
	}
</script>
</head>
<div class="max-width">
	<div class="container-fluid">		
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="template.responseTemplate" />
				</div>
			</div>
		</div>
		<div class="row">		
			<div class="col-md-12">
				<table class="table no-head">
					<tr>
						<td class="text-right">
							<b><spring:message code="template.issueName" /></b>
						</td>
						<td>
							<a href="${pageContext.request.contextPath}/Issues/${customerCareCaseIssueTemplate.issueId}">
								${customerCareCaseIssueTemplate.issueName}
							</a>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="template.applicableLanguages" /></b>
						</td>
						<td>
							<c:forEach items="${customerCareCaseIssueTemplate.applicableLanguages}" var="applicableLanguage" varStatus="applicableLanguagesStatus">
								${applicableLanguage.fullName}
								<c:if test="${applicableLanguagesStatus.count != customerCareCaseIssueTemplate.applicableLanguages.size()}">,</c:if>
							</c:forEach>							
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="template.applicableCase" /></b>
						</td>
						<td>
							<c:forEach items="${customerCareCaseIssueTemplate.applicableCaseTypes}" var="applicableCaseTypes" varStatus="applicableCaseTypesStatus">
								<spring:message code="ccc.${applicableCaseTypes}"/>
								<c:if test="${applicableCaseTypesStatus.count != customerCareCaseIssueTemplate.applicableCaseTypes.size()}">,</c:if>							  							    							   							
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="template.applicableMarket" /></b>
						</td>
						<td>
							<c:forEach items="${customerCareCaseIssueTemplate.applicableMarketRegions}" var="applicableMarketRegions" varStatus="applicableMarketRegionsStatus">
								<spring:message code="${applicableMarketRegions}"/>
								<c:if test="${applicableMarketRegionsStatus.count != customerCareCaseIssueTemplate.applicableMarketRegions.size()}">,</c:if>
							</c:forEach>							
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="template.applicableMarketplaces" /></b>
						</td>
						<td>
							<c:forEach items="${customerCareCaseIssueTemplate.applicableMarketplaceList}" var="applicableMarketplace" varStatus="applicableMarketplacesStatus">
								${applicableMarketplace.name}
								<c:if test="${applicableMarketplacesStatus.count != customerCareCaseIssueTemplate.applicableMarketplaceList.size()}">,</c:if>
							</c:forEach>
						</td>
					</tr>
				</table>										
			</div>
		</div>
		<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
			<div class="col-md-12">	
				<div class="panel panel-default">
					<div class="panel-body">
						<% pageContext.setAttribute("newLineChar", "\n"); %>
						<p>${fn:replace(customerCareCaseIssueTemplate.contents, newLineChar,  "<br>")}</p>					
					</div>
				</div>
			</div>
		</div>		
		<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
			<div class="col-md-12 text-right">			
				<a href="${pageContext.request.contextPath}/Issues/${customerCareCaseIssueTemplate.issueId}">
					<input class="btn btn-primary" type="button" value='<spring:message code="template.backToIssue" />'/>
				</a>
				<sec:authorize access="hasAnyRole('${auth['TEMPLATE_MAINTAIN']}')">		
				<a href="${pageContext.request.contextPath}/Template/${customerCareCaseIssueTemplate.id}/edit">
					<input class="btn btn-default" type="button" value='<spring:message code="template.edit" />'/>
				</a>
				<input class="btn btn-default" type="button" value='<spring:message code="template.delete" />' onclick="deleteConfirm('${customerCareCaseIssueTemplate.issueId}','${customerCareCaseIssueTemplate.id}');"/>
				<div id="dialog-confirm"></div>				
				</sec:authorize>	
			</div>
		</div>			
	</div>
</div>	