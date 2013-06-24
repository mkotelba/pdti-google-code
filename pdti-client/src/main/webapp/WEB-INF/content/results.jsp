<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
        <s:if test="searchResultEntries">
            <s:iterator value="searchResultEntries">
                searchResultEntry DN: <s:property value="dn" />
                <s:iterator value="attr">
                    property name: <s:property value="name" /> property value: <s:property value="value" />
                </s:iterator>
            </s:iterator>
        </s:if>
        <s:if test="errorMessage">Error message: <s:property value="errorMessage"/></s:if>
        <p><a href="<s:url value="/"/>"><s:text name="back" /></a></p>
    </body>
</html>
