<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="container">
<%-- 	<s:form action="APP01_insertGroup"> --%>
		<div class="row">
<%-- 			<br/><s:submit value="Save" cssStyle="width:100px" cssClass="btn btn-default btn-xs btn-primary"/> <br/><br/> --%>
		</div>
		<div class="row">
			<table class="table table-condensed table-hover table-bordered">
				<tr>
					<td class="info" style="vertical-align: middle;">ID</td>
					<td style="vertical-align: middle;">
						<s:textfield name="groupAppEdit.groupId" cssClass="form-control" value=""/>
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
				<s:iterator value="groupAppEdit.listOsConfig" status="listOsConfigStatus" var="osConfig">
					<tr>
						<td  class="info" style="vertical-align: middle;">
							<s:if test="osId == 1 ">
								Ios
							</s:if>
							<s:if test="osId == 2 ">
								Android
							</s:if>
							<s:if test="osId == 3 ">
								Windows
							</s:if>
							<s:hidden name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].osId"></s:hidden>
						</td>
						<td>
							<table class="table-hover">
								<tr>
									<td width="150px;">
										AdmodIDBanner 
									</td>
									<td>
										<s:textfield name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].admodIdBanner" cssStyle="width:200px" cssClass="form-control"/>
									</td>
								</tr>
								<tr>
									<td>
										AdmodIDPopup 
									</td>
									<td>
										<s:textfield name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].admodIdPopup" cssStyle="width:200px" cssClass="form-control"/>
									</td>
								</tr>
								<tr>
									<td>
										User 
									</td>
									<td>
										<s:select name="groupAppEdit.listOsConfig[%{#listOsConfigStatus.index}].uid" list="listDev" listKey="id" listValue="name" value="-1" cssClass="form-control" cssStyle="width:200px"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
<%-- 	</s:form> --%>
</div>