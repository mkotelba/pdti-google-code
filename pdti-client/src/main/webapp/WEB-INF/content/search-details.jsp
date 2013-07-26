<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
        <div class="container">
            <h3><s:text name="searchDetailedResultsLegend" /></h3>
            <table class="table table-bordered table-condensed">
                <tr class="info"><td><h4>DIRECTORY ID: <s:property value="searchResultToDetail.directoryId"/></h4></td></tr>
                <tr class="info"><td><h4>DIRECTORY URI: <s:property value="searchResultToDetail.directoryUri"/></h4></td></tr>
	            <tr class="info"><td><h4>DN: <s:property value="searchResultToDetail.dn"/></h4></td></tr>
	            <s:iterator value="searchResultToDetail.attributes">
					<tr><td><strong><s:property value="key" /></strong></td></tr>
					<s:iterator value="value">
					<tr><td><div style="margin-left:50px;"><s:property/></div></td></tr>
					</s:iterator>
				</s:iterator>
            </table>
            <a href="<s:url value="/"/>"><s:text name="back" /></a>
		</div>
    </body>
</html>
