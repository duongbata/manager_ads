<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<script type="text/javascript" src="resources/js/APP05_2.js"></script>
<div class="container">
	<s:form action="APP05_editGroupApp">
		<s:if test="%{info.dataTrans.message != null}">
			<s:property value="%{info.dataTrans.message}"/>
		</s:if>
		<div class="row" style="margin-bottom: 1px;margin-top: 1px;">
			<br/><s:submit value="Save" cssStyle="width:100px" cssClass="btn btn-default btn-xs btn-primary"/> <br/><br/>
		</div>
		<div class="row">
			<table class="table table-condensed table-hover table-bordered">
				<tr>
					<td class="info" style="vertical-align: middle;" width="200px;">ID</td>
					<td style="vertical-align: middle;">
<%-- 						<s:textfield name="info.dataTrans.groupAppEdit.groupId" value="%{info.dataTrans.groupAppEdit.groupId}" cssClass="form-control"/> --%>
						<s:hidden name="groupAppEdit.groupId" value="%{groupAppEdit.groupId}"/>
						<s:property value="%{groupAppEdit.groupId}"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Name</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.groupName" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Icon</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.groupIcon" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Title</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.groupTitle" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Description</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.groupDescription" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Image Banner</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.imgBanner" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Image Vertical</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.imgVertical" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="info" style="vertical-align: middle;">Image Horizontal</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.imgHorizontal" cssClass="form-control"/>
					</td>
				</tr>
			</table>
		</div>
		<div class="row">
			<table class="table table-condensed table-hover table-bordered" id="tblOsConfig">
				<s:iterator value="groupAppEdit.listOsConfig" status="listOsConfigStatus" var="osConfig">
					<tr id="config_<s:property value="%{#listOsConfigStatus.index}"/>">
						<td  class="info" style="vertical-align: middle;width: 200px;">
							<s:select name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].osId" 
									  list="info.dataTrans.mapOs" listKey="key" listValue="value" cssStyle="width:100px" cssClass="selOsId"/>
							<button type="button" class="btn btn-default btn-xs btn-danger btnDelOsConfig" name="delConfig_<s:property value="%{#listOsConfigStatus.index}"/>"><span class="glyphicon glyphicon-remove"></span></button>
<%-- 							<s:hidden name="info.dataTrans.groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].osId"></s:hidden> --%>
						</td>
						<td>
							<table class="table-hover">
								<tr>
									<td width="150px;">
										AdmodIDBanner 
									</td>
									<td>
										<s:textfield name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].admodIdBanner" cssStyle="width:200px" cssClass="form-control idBanner"/>
									</td>
								</tr>
								<tr>
									<td>
										AdmodIDPopup 
									</td>
									<td>
										<s:textfield name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].admodIdPopup" cssStyle="width:200px" cssClass="form-control idPopup"/>
									</td>
								</tr>
								<tr>
									<td>
										User 
									</td>
									<td>
										<s:select name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].uid" 
												  list="info.dataTrans.listDev" listKey="id" listValue="name"
												  cssClass="form-control uid" cssStyle="width:200px"/>
									</td>
								</tr>
							</table>
							<%-- AdmodIDBanner <s:textfield name="info.dataTrans.groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].admodIdBanner" cssStyle="width:200px" cssClass=""/> <br/>
							AdmodIDPopup <s:textfield name="info.dataTrans.groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].admodIdPopup" cssStyle="width:200px" cssClass=""/> <br/>
							User <s:select name="info.dataTrans.groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].uid" 
												  list="info.dataTrans.listDev" listKey="id" listValue="name"
												  cssClass="form-control" cssStyle="width:200px"/> --%>
						</td>
					</tr>
				</s:iterator>
			</table>
			<input type="button" class="btn btn-default btn-xs btn-primary" id="addOsConfig" value="Add Os Config">
		</div>
	</s:form>
	<div style="display: none">
		<table id="tblOs">
			<!-- <tr>
				<td>
					<p class="osId">1</p>
				</td>
				<td>
					<p class="osName">Ios</p>
				</td>
			</tr>
			<tr>
				<td>
					<p class="osId">2</p>
				</td>
				<td>
					<p class="osName">Android</p>
				</td>
			</tr>
			<tr>
				<td>
					<p class="osId">3</p>
				</td>
				<td>
					<p class="osName">Windows</p>
				</td>
			</tr> -->
			<s:iterator value="info.dataTrans.mapOs" var="os">
				<tr>
					<%-- <td>
						<p class="osId">
							<s:property value="%{#os.key}"/>
						</p>
					</td>
					<td>
						<p class="osName">
							<s:property value="%{#os.value}"/>
						</p>
					</td> --%>
					<td>
						<s:textfield cssClass="osId" value="%{#os.key}"></s:textfield>
						<s:textfield cssClass="osName" value="%{#os.value}"></s:textfield>
					</td>
				</tr>
			</s:iterator>
		</table>
		<table id="tblDev">
			<s:if test="%{info.dataTrans.listDev.size > 0}">
				<s:iterator value="info.dataTrans.listDev" status="listDevStatus" var="dev">
					<tr>
						<td>
							<s:textfield cssClass="devId" value="%{#dev.id}"></s:textfield>
							<s:textfield cssClass="devName" value="%{#dev.name}"></s:textfield>
							<%-- <p class="devId">
								<s:property value="%{#dev.id}"/>
								
							</p>
							<p class="devName">
								<s:property value="%{#dev.name}"/>
							</p> --%>
						</td>
					</tr>
				</s:iterator>
			</s:if>
		</table>
	</div>
</div>