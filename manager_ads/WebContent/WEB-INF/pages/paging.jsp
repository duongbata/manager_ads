<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="text-left">
	<ul class="sync-pagination pagination-sm pagination">
		<li class="first disabled"><a href="#">First</a></li>
		<li class="prev disabled"><a href="#">Prev</a></li>
		<s:iterator value="info.dataTrans.listPage" status="listPageStatus" var="page">
			<s:if test="%{#listPageStatus.index == 0}">
				<li class="page active"><a href="#">1</a></li>
			</s:if>
			<s:else>
				<li class="page"><a href="#"><s:property value="%{#listPageStatus.index + 1}"/></a></li>
			</s:else>	
		</s:iterator>
		<li class="next"><a href="#">Next</a></li>
		<li class="last"><a href="#">Last</a></li>
	</ul>
</div>