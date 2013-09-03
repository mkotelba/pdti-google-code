<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <link href="<s:url value="css/index.css"/>" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%@ include file="title_bar.jsp"%>
        <p class="text">
            <s:text name="onc.language"/>
        </p>
        <hr/>
        <s:form theme="css_xhtml" action="execute" validate="true">
            <table>
	            <tr style="vertical-align:middle;">
	               <td><s:select key="typeOfDirectory" list="@gov.hhs.onc.pdti.testtool.DirectoryTypes@values()"/></td>
	               <td>(<s:a href="#" id="wsdl_type-dialog-link" class="ui-state-default ui-corner-all">?</s:a>)</td>
	            </tr>
                <tr>
                   <td><s:textfield key="wsdlUrl" /></td>
                   <td>(<s:a href="#" id="wsdl_url-dialog-link" class="ui-state-default ui-corner-all">?</s:a>)</td>
                </tr>
                <tr>
                   <td><s:textfield key="baseDn" /></td>
                   <td>(<s:a href="#" id="basedn-dialog-link" class="ui-state-default ui-corner-all">?</s:a>)</td>
                </tr>
                <tr>
                   <td><s:submit/></td>
                </tr>
            </table>
        </s:form>

		<div id="wsdl_type-dialog" title="WSDL Type">
		    <p>
		    Please select the type of WSDL that you wish to test. The MSPD effort supports two WSDL types:
		    <ul>
		        <li>The <em>"IHE WSDL"</em> is the published version of the IHE Provider Directories WSDL directly from their specifications. See <a href="http://www.ihe.net/Technical_Framework/upload/IHE_ITI_Suppl_HPD_Rev1-1_TI_2010-08-10.pdf">http://www.ihe.net/Technical_Framework/upload/IHE_ITI_Suppl_HPD_Rev1-1_TI_2010-08-10.pdf</a>, or for the actual WSDL see HPD_ProviderInformationDirectory.wsdl at <a href="ftp://ftp.ihe.net/TF_Implementation_Material/ITI/wsdl">ftp://ftp.ihe.net/TF_Implementation_Material/ITI/wsdl</a>.</li>
		        <li>The <em>"MSPD WSDL"</em> was developed to add functionality over the IHE WSDL, notably improved error handling and federation support. See <a href="http://modularspecs.siframework.org/Provider+Directories+Artifacts">http://modularspecs.siframework.org/Provider+Directories+Artifacts</a>.</li>
		    </ul>
		    </p>
		</div>

        <div id="wsdl_url-dialog" title="WSDL URL">
            <p>Please provide the URL on your Provider Directories server at which the WSDL can be reached.</p>
        </div>

        <div id="basedn-dialog" title="Base DN">
            <p>Please provide the base DN of your data source. This is at the level above Providers, Organizations, <i>etc.</i> For example, the test data provided in the MSPD code base has a base DN of "o=dev.provider-directories.com,dc=hpd".</p>
        </div>

        <script type="text/javascript">
            $(function() {
                $( "#wsdl_type-dialog" ).dialog({
                    autoOpen: false,
                    width: 400,
                    buttons: [
                        {
                            text: "Ok",
                            click: function() {
                                $( this ).dialog( "close" );
                            }
                        },
                        {
                            text: "Cancel",
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
                            text: "Ok",
                            click: function() {
                                $( this ).dialog( "close" );
                            }
                        },
                        {
                            text: "Cancel",
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
                    width: 400,
                    buttons: [
                        {
                            text: "Ok",
                            click: function() {
                                $( this ).dialog( "close" );
                            }
                        },
                        {
                            text: "Cancel",
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