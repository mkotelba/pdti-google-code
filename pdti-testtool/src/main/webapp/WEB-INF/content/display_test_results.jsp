<%@ taglib prefix="s" uri="/struts-tags"%>
<div style="height:400px; overflow:auto;">
<table style="width:100%">
    <tr><th style="align:left"><s:text name="testsuite.header.text"/></th><th style="align:left"><s:text name="testcase.header.text"/></th><th style="align:left"><s:text name="result.header.text"/></th></tr>
    <s:iterator value="testResults" var="testResult">
    <tr>
        <s:iterator value="testResult"><td><s:property/></td></s:iterator>
    </tr>
    </s:iterator>
</table>
</div>