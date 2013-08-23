<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<%@ include file="head.jsp"%>
	<body>
        <div class="container">
	        <h3><s:text name="title" /></h3>
	        <hr/>
            <s:form action="execute" class="form-inline">
                <fieldset>
                    <s:textfield style="width: 400px;" key="wsdlUrl"/>
                    <s:textfield style="width: 400px;" key="baseDn"/>
	                <s:submit />
                </fieldset>
            </s:form>
        </div>
    </body>
</html>