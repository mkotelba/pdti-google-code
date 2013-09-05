<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <link href="<s:url value="css/index.css"/>" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%@ include file="header.jsp"%>
        <div class="container">
            <br/>
            <br/>
            <h2><s:text name="welcome.header"/></h2>
            <p class="text">
                <strong><s:text name="purpose.header"/></strong>
                <br/>
                <br/>
                <s:text name="purpose.language"/>
            </p>
            <p class="text">
                <strong><s:text name="future.plans.header"/></strong>
                <br/>
                <br/>
                <s:text name="future.plans.language"/>
            </p>
            <p class="text">
                <strong><s:text name="how.to.use.header"/></strong>
                <br/>
                <br/>
                <s:text name="how.to.use.language"/>
            </p>
            <hr/>
        </div>
    </body>
</html>