<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<script type="text/javascript" src="resources/js/APP02.js"></script>
<link type="text/css" rel="stylesheet" href="resources/css/APP02.css" />
<%-- <s:head /> --%>
<sx:head />

<div class="container">
	<s:if test="%{message != null}">
		<s:label name="message"/>
	</s:if>
	
	<s:form action="APP02_insertApp" id="formInsertApp" onsubmit="return validateInsert();">
		<div class="row">
			<br/><s:submit cssClass="btn btn-default btn-xs btn-primary" value="Save" cssStyle="width:100px"/><br/><br/>
		</div>
		<div class="row">
			<table class="table table-condensed table-hover table-bordered">
				<%-- <tr>
					<td class="info" style="vertical-align: middle;" width="200px">ID</td>
					<td style="vertical-align: middle;">
						<s:textfield name="appInsert.appId" id="appId" cssClass="form-control"/>
					</td>
				</tr> --%>
				<tr>
					<td class="info" style="vertical-align: middle;">Group</td>
					<td style="vertical-align: middle;">
						<s:select name="appInsert.groupId" id="groupId" list="mapGroup" cssClass="form-control" cssStyle="width:150px;" value="-1"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">OS</td>
					<td style="vertical-align: middle;">
						<s:select name="appInsert.osId" id="osId" list="mapOS" listKey="key" listValue="value" cssClass="form-control" cssStyle="width:150px;" value="-1"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Dev</td>
					<td style="vertical-align: middle;">
<%-- 						<s:select name="appInsert.uid" id="devId" list="listDev" listKey="id" listValue="name" cssClass="form-control" cssStyle="width:150px;" value="-1"/> --%>
<%-- 						<s:textfield name="appInsert.uid" id="devId" disabled="true" cssClass="form-control" cssStyle="width:150px"/> --%>
						<s:textfield name="dev.name" id="devName" disabled="true" cssClass="form-control" cssStyle="width:150px"/>
						<s:hidden name="dev.id" id="devId"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Url</td>
					<td style="vertical-align: middle;">
						<s:textfield name="appInsert.url" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Version</td>
					<td style="vertical-align: middle;">
						<s:textfield name="appInsert.version" cssClass="form-control"/>
					</td>
				</tr>
			</table>
		</div>
		<div class="row">
			<table class="table table-condensed table-hover table-bordered" id="tblProperty">
			</table>
<!-- 			<br/> -->
			<input type="button" class="btn btn-default btn-xs btn-primary" value="Add Property" id="addProperty">
			<br/><br/>
		</div>
		<%-- <div class="row">
			<s:set var="appConfig" value="appInsert.appConfig"/>
			<s:set var="parentName" value="'#appConfig'"/>
			<div class="span12">
				<div class="menu">
					<tiles:insertDefinition name="NODE"/>
				</div>
			</div>
		</div> --%>
		<div class="row" id="configTable">
			
		</div>

	</s:form>
</div>