<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="head_common.jsp"%>
</head>
<body>
	<%@ include file="title_bar.jsp"%>
    <s:text name="tests.done.text" /> <s:property value="wsdlUrl"/>
	<%@ include file="display_test_results.jsp"%>
</body>
</html>