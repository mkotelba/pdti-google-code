<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
        <div class="container">
            <h3><s:text name="results.label" /></h3>
            <hr/>
            <s:property value="testResult"/>
        </div>
        <div style="display:block; text-align:center">
            <a href="<s:url value="/"/>"><s:text name="back" /></a>
        </div>
    </body>
</html>