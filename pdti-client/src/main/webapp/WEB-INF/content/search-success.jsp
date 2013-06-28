<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<%@ include file="head.jsp"%>
<body>
    <div class="container">
        <h3><s:text name="searchResultsLegend" /></h3>
        <table class="table table-striped">
			<s:if test="null == searchResults || searchResults.length == 0">
				<tr class="warning"><td><s:text name="noResultsFound" /></td></tr>
			</s:if>
			<s:else>
	            <s:iterator value="searchResults" var="searchResultWrapper">
	            <tr>
	               <td>DN: <s:property value="dn"/></td>
	               <td>
	                   <s:form action="search">
	                       <s:hidden key="url"/>
	                       <s:hidden key="providerDirectoryType"/>
                           <s:hidden key="typeToSearch"/>
                           <s:hidden key="searchAttribute"/>
                           <s:hidden key="searchString"/>
                           <s:hidden key="dn"/>
                           <s:hidden name="showDetails" value="true"/>
                           <s:submit key="viewDetails"/>
	                   </s:form>
	               </td>
	            </tr>
	            </s:iterator>
			</s:else>
	    </table>
	    <a href="<s:url value="/"/>"><s:text name="back" /></a>
	</div>
</body>
</html>
