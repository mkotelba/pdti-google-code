<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<%@ include file="head.jsp"%>
	<body>
        <div class="container">
	        <h3><s:text name="searchLegend" /></h3>
	        <hr/>
			<s:form action="search" validate="true" class="form-inline">
                <fieldset>
                    <s:textfield key="url"/>
                    <s:select key="providerDirectoryType" list="@gov.hhs.onc.pdti.client.types.ProviderDirectoryTypes@values()" />
					<s:textfield key="requestId"/>
					<s:select key="typeToSearch" list="@gov.hhs.onc.pdti.client.types.SearchTypes@values()" />
					<s:textfield key="searchAttribute"/>
			<%--                 <s:select key="searchAttribute" list="@gov.hhs.onc.pdti.client.types.AttributeTypes@values()" /> --%>
			        <s:textfield key="searchString" />
			<%--                 <s:checkboxlist key="attributesToRetrieve" list="@gov.hhs.onc.pdti.client.types.AttributeTypes@values()" --%>
			<%--                     value="@gov.hhs.onc.pdti.client.types.AttributeTypes@values()"/> --%>
			        <s:submit />
			    </fieldset>
		    </s:form>
        </div>
    </body>
</html>