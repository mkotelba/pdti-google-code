<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<%@ include file="head.jsp"%>
<body>
	<s:form action="/search">
		<s:textfield key="typeToSearch" />
		<s:textfield key="searchAttribute" />
		<s:textfield key="searchString" />
		<s:submit />
	</s:form>
</body>
</html>