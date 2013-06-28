<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
            <p><s:iterator value="searchResultEntries">
                searchResultEntry DN: <s:property value="dn" />
                <s:iterator value="attr">
                    property name: <s:property value="name" /> property value: <s:property value="value" />
                </s:iterator>
            </s:iterator></p>
            <p><a href="<s:url value="/"/>"><s:text name="back" /></a></p>
    </body>
</html>
