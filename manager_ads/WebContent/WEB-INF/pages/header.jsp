<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="container">
	<div class="row">
		<div class="pull-right">
			<form action="j_spring_security_logout" method="post" id="logoutForm">
			  	<input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
			<script>
				function formSubmit() {
					var flgExit = confirm("Đăng xuất?");
					if (!flgExit) {
						return;
					}
					document.getElementById("logoutForm").submit();
				}
			</script>
			<s:if test="%{info.user != null && info.user.id != null && info.user.id != 0}">
				Id : <s:label name="info.user.id"/> &nbsp;&nbsp;&nbsp;<a href="javascript:formSubmit()">Thoát</a>
				<s:hidden name="info.user.id" id="userId"/>
			</s:if>
		</div>
	</div>
	<div class="row">
		<div class="pull-left">
			<s:set var="dev">dev</s:set>
			<s:set var="admin">admin</s:set>
			<s:set var="user">user</s:set>
			<s:if test="%{#dev in info.user.listRoleName}">
				<%-- <s:a href="ADV01_init" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-th-list"></span> App
				</s:a> --%>
				<s:a href="APP03_init" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-th-list"></span> List App
				</s:a>
			</s:if>
			<s:if test="%{#admin in info.user.listRoleName}">
				<s:a href="APP04_init" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-cog"></span> Admob
				</s:a>
				<s:a href="APP01_init" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-cog"></span> Group App
				</s:a>
				<s:a href="APP02_init" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-th-list"></span> App
				</s:a>
				<s:a href="APP05_init" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-th-list"></span> List Group
				</s:a>
			</s:if>
			<s:if test="%{#user in info.user.listRoleName}">
				<s:a href="ADV03_initCampaign" cssClass="btn btn-default btn-xs btn-success" style="width:100px">
					<span class="glyphicon glyphicon-th-list"></span> Campaign
				</s:a>
			</s:if>
		</div>
	</div>
</div>
