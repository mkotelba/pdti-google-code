<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <link href="<s:url value="/css/index.css"/>" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%@ include file="title_bar.jsp"%>
        <p class="text">
            <s:text name="onc.language"/>
        </p>
        <hr/>
        <s:form action="execute" validate="true">
            <s:select key="typeOfDirectory" list="@gov.hhs.onc.pdti.testtool.DirectoryTypes@values()"/>
            <s:textfield key="wsdlUrl" />
            <s:textfield key="baseDn" />
            <s:submit/>
        </s:form>
    </body>
</html>