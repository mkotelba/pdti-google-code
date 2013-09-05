<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <link href="<s:url value="css/index.css"/>" rel="stylesheet" type="text/css"/>
        <!-- workaround for bootstrap conflict with jquery that prevents dialog close icon from appearing (see: http://stackoverflow.com/questions/8681707/jqueryui-modal-dialog-does-not-show-close-button-x) -->
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" type="text/javascript" ></script>
    </head>
    <body>
        <%@ include file="header.jsp"%>
        <div class="container">
            <br/>
            <br/>
            <h2><s:text name="test.header"/></h2>
            <hr/>
            <s:form theme="simple" action="execute" validate="true" class="form-horizontal">
                <table class=".table">
                    <tr><td><s:label key="typeOfDirectory" class="control-label"/></td><td><a href="#" id="wsdl_type-dialog-link"><i class="icon-question-sign"></a></td><td><s:select key="typeOfDirectory" list="@gov.hhs.onc.pdti.testtool.DirectoryTypes@values()" value="@gov.hhs.onc.pdti.testtool.DirectoryTypes@MSPD.toString()"/></td></tr>
                    <tr><td><s:fielderror><s:param>wsdlUrl</s:param></s:fielderror></td></tr>
                    <tr><td><s:label key="wsdlUrl" class="control-label"/></td><td><a href="#" id="wsdl_url-dialog-link"><i class="icon-question-sign"></a></td><td><s:textfield key="wsdlUrl"/></td></tr>
                    <tr><td><s:fielderror><s:param>baseDn</s:param></s:fielderror></td></tr>
                    <tr><td><s:label key="baseDn" class="control-label"/></td><td><a href="#" id="basedn-dialog-link"><i class="icon-question-sign"></a></td><td><s:textfield key="baseDn"/></td></tr>
                    <tr><td><s:submit theme="simple"/></td></tr>
                </table>
            </s:form>
        </div>

        <div id="wsdl_type-dialog" title="<s:text name='typeOfDirectory'/>">
            <s:text name="wsdl.type.dialog.html"/>
        </div>

        <div id="wsdl_url-dialog" title="<s:text name='wsdlUrl'/>">
            <s:text name="wsdl.url.dialog.html"/>
        </div>

        <div id="basedn-dialog" title="<s:text name='baseDn'/>">
            <s:text name="basedn.dialog.html"/>
        </div>

        <script type="text/javascript">
            $(function() {
                $( "#wsdl_type-dialog" ).dialog({
                    autoOpen: false,
                    width: 800,
                    buttons: [
                        {
                            text: "OK",
                            click: function() {
                                $( this ).dialog( "close" );
                            }
                        }
                    ]
                });
                // Link to open the dialog
                $( "#wsdl_type-dialog-link" ).click(function( event ) {
                    $( "#wsdl_type-dialog" ).dialog( "open" );
                    event.preventDefault();
                });
                
                $( "#wsdl_url-dialog" ).dialog({
                    autoOpen: false,
                    width: 400,
                    buttons: [
                        {
                            text: "OK",
                            click: function() {
                                $( this ).dialog( "close" );
                            }
                        }
                    ]
                });
                // Link to open the dialog
                $( "#wsdl_url-dialog-link" ).click(function( event ) {
                    $( "#wsdl_url-dialog" ).dialog( "open" );
                    event.preventDefault();
                });
                
                $( "#basedn-dialog" ).dialog({
                    autoOpen: false,
                    width: 600,
                    buttons: [
                        {
                            text: "OK",
                            click: function() {
                                $( this ).dialog( "close" );
                            }
                        }
                    ]
                });
                // Link to open the dialog
                $( "#basedn-dialog-link" ).click(function( event ) {
                    $( "#basedn-dialog" ).dialog( "open" );
                    event.preventDefault();
                });
            });
        </script>
    </body>
</html>