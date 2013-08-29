<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <link href="<s:url value="/css/results.css"/>" rel="stylesheet" type="text/css"/>
        <meta http-equiv="refresh" content="2;url=<s:url includeParams="all"/>"/>
    </head>
    <body>
        <%@ include file="title_bar.jsp"%>
        <div>
            <img id="indicator" src="<s:url value="/img/ajax-loader.gif"/>" alt="Loading ..."/>
            <s:text name="tests.running.text"/>
            <span class="wsdlUrl"><s:property value="wsdlUrl"/></span>
        </div>
        <%@ include file="display_test_results.jsp"%>
    </body>
</html>