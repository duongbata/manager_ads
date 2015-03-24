<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<script type="text/javascript" src="resources/js/jquery.twbsPagination.js"></script>
<script type="text/javascript" src="resources/js/APP05.js"></script>
<s:form action="/APP05_init">
	<div class="container">
	<div class="row">
	<div class="panel filterable">
		<div class="">
			<div class="pull-right">
				<%-- <button type="button" class="btn btn-default btn-xs" id="btnAdd"
					style="width: 100px; ">
					<span class="glyphicon glyphicon-plus"></span> Add
				</button> &nbsp;&nbsp; --%>
				<button type="button" class="btn btn-default btn-xs btn-filter"
					style="width: 100px;">
					<span class="glyphicon glyphicon-filter"></span> Filter
				</button>
			</div>
		</div>
		<br/><br/>
		<table class="table table-bordered table-hover">
			<thead>
				<tr class="filters">
					<th style="background-color: #337ab7">#</th>
					<th style="background-color: #337ab7"><input type="text" class="form-control" placeholder="Name"
						disabled></th>
					<th style="background-color: #337ab7"><input type="text" class="form-control" placeholder="App"
						disabled></th>
					<th style="background-color: #337ab7"><input type="text" class="form-control" placeholder="Description"
						disabled></th>
					<th style="background-color: #337ab7"><input type="text" class="form-control" placeholder="Title"
						disabled></th>
					<th style="background-color: #337ab7">Icon</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="%{info.dataTrans.listGroupApp.size > 0}">
					<s:iterator value="info.dataTrans.listGroupApp" status="listGroupAppStatus" var="group">
						<tr>
							<td style="vertical-align: middle;">
								<s:property value="%{#listGroupAppStatus.index +1}"/>
							</td>
							<td style="vertical-align: middle;">
								<s:set var="link">/APP05_initEditGroupApp.action?groupId=${groupId}</s:set>
								<s:a value="%{#link}">
									<s:property value="%{#group.groupName}"/>
								</s:a>
							</td>
							<td style="vertical-align: middle;">
								<s:iterator value="#group.listAppBean" status="listAppBeanStatus" var="appBean">
									<s:set var="linkApp">/APP05_initDetailApp.action?uid=${uid}&&appId=${appId}</s:set>
									<s:a value="%{#linkApp}">
										<s:property value="%{#appBean.osId}"/><br/>	
									</s:a>
								</s:iterator>
							</td>
							<td style="vertical-align: middle;">
								<s:property value="%{#group.groupDescription}"/>
							</td>
							<td style="vertical-align: middle;">
								<s:property value="%{#group.groupTitle}"/>
							</td>
							<td style="vertical-align: middle;">
								<img width="50px;" height="50px;" src="<s:property value="%{#group.groupIcon}"/>"></img>
							</td>
						</tr>
					</s:iterator>
				</s:if>
			</tbody>
		</table>
	</div>
	<br/>
</div>
</div>
</s:form>
