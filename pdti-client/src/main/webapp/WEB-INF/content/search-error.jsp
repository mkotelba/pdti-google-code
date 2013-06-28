<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
        <p>Error message: <s:property value="errorMessage"/></p>
        <p><a href="<s:url value="/"/>"><s:text name="back" /></a></p>
    </body>
</html>
