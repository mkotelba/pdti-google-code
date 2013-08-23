<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<%@ include file="head.jsp"%>
	<body>
        <div class="container">
	        <h3 id="title">
                <img src="img/pdti-logo.png" id="title-img"/>
                <span id="title-content">
                    <s:text name="title"/>
                </span>
            </h3>
	        <hr/>
            <s:form action="execute" class="form-inline">
                <fieldset>
                    <s:textfield key="wsdlUrl"/>
                    <s:textfield key="baseDn"/>
	                <s:submit/>
                </fieldset>
            </s:form>
        </div>
    </body>
</html>