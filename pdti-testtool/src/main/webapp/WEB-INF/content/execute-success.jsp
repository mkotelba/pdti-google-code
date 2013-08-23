<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
        <div class="container">
            <h3 id="title">
                <img src="img/pdti-logo.png" id="title-img"/>
                <span id="title-content">
                    <s:text name="results.label"/>
                </span>
            </h3>
            <hr/>
            <s:property value="testResult"/>
        </div>
        <div class="back-container">
            <a href="<s:url value="/"/>"><s:text name="back"/></a>
        </div>
    </body>
</html>