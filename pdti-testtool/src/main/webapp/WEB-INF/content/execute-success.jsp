<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <link href="<s:url value="/css/results.css"/>" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%@ include file="title_bar.jsp"%>
        <s:text name="tests.done.text" /> 
        <span class="wsdlUrl"><s:property value="wsdlUrl"/></span>
        <%@ include file="display_test_results.jsp"%>
    </body>
</html>