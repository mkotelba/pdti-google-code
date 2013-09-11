<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="head_common.jsp"%>
<link href="<s:url value="css/index.css"/>" rel="stylesheet"
	type="text/css" />
<!-- workaround for bootstrap conflict with jquery that prevents dialog close icon from appearing (see: http://stackoverflow.com/questions/8681707/jqueryui-modal-dialog-does-not-show-close-button-x) -->
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"
	type="text/javascript"></script>
<link href="<s:url value="css/test.css"/>" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
		<br /> <br />
		<h2 id="header">
			<s:text name="test.header" />
		</h2>
		<hr />
                <!-- <a href="#" id="wsdl_type-dialog-link" class="helplink"><i class="icon-question-sign"></a>
                <a href="#" id="wsdl_url-dialog-link" class="helplink"><i class="icon-question-sign"></a>
                <a href="#" id="basedn-dialog-link" class="helplink"><i class="icon-question-sign"></a> -->
        <p>
            <div id="validationerrordiv" style="color:#b94a48;">* <s:text name="required"/></div>
        </p>
		<form id="testform" class="form-horizontal">
			<div class="control-group">
				<label id="typeOfDirectoryLabel" class="control-label"
					for="typeOfDirectory"><s:text name="typeOfDirectory" /></label>
				<div class="controls">
					<s:select id="typeOfDirectory" key="typeOfDirectory"
						list="@gov.hhs.onc.pdti.testtool.DirectoryTypes@values()"
						value="@gov.hhs.onc.pdti.testtool.DirectoryTypes@MSPD.toString()"
						theme="simple" />
				</div>
			</div>
            <div id="wsdlUrlControls" class="control-group">
                <label class="control-label"
                    for="wsdlUrl"><s:text name="wsdlUrl" /></label>
                <div class="controls">
                    <s:textfield id="wsdlUrl" key="wsdlUrl" theme="simple"/>
                </div>
            </div>
            <div id="baseDnControls" class="control-group">
                <label class="control-label"
                    for="baseDn"><s:text name="baseDn" /></label>
                <div class="controls">
                    <s:textfield id="baseDn" key="baseDn" theme="simple"/>
                </div>
            </div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn"><s:text name="submit"/></button>
				</div>
			</div>
		</form>
	</div>

	<div id="wsdl_type-dialog" title="<s:text name='typeOfDirectory'/>">
		<s:text name="wsdl.type.dialog.html" />
	</div>

	<div id="wsdl_url-dialog" title="<s:text name='wsdlUrl'/>">
		<s:text name="wsdl.url.dialog.html" />
	</div>

	<div id="basedn-dialog" title="<s:text name='baseDn'/>">
		<s:text name="basedn.dialog.html" />
	</div>

	<div id="indicatordiv">
		<p>
			<img src="<s:url value="/img/ajax-loader.gif"/>"
				alt="Running tests..." />
			<s:text name="tests.running.text" />
			<span id="wsdlUrl1" class="wsdlUrl"></span>
		</p>
	</div>

	<div id="infodiv">
		<p>
			<s:text name="tests.done.text" />
			<span id="wsdlUrl2" class="wsdlUrl"></span>
		</p>
	</div>

	<div id="testResults">
		<div id="accordion"></div>
	</div>

	<script type="text/javascript">
            $(function() {
            	$("#validationerrordiv").hide();
            	
            	$("#indicatordiv").hide();
            	
            	$("#infodiv").hide();
            	
                $("#wsdl_type-dialog").dialog({
                    autoOpen: false,
                    width: 800,
                    buttons: [
                        {
                            text: "OK",
                            click: function() {
                                $( this ).dialog( "close");
                            }
                        }
                    ]
                });
                // Link to open the dialog
                $("#wsdl_type-dialog-link").click(function( event ) {
                    $("#wsdl_type-dialog").dialog( "open");
                    event.preventDefault();
                });
                
                $("#wsdl_url-dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    buttons: [
                        {
                            text: "OK",
                            click: function() {
                                $( this ).dialog( "close");
                            }
                        }
                    ]
                });
                // Link to open the dialog
                $("#wsdl_url-dialog-link").click(function( event ) {
                    $("#wsdl_url-dialog").dialog( "open");
                    event.preventDefault();
                });
                
                $("#basedn-dialog").dialog({
                    autoOpen: false,
                    width: 600,
                    buttons: [
                        {
                            text: "OK",
                            click: function() {
                                $( this ).dialog( "close");
                            }
                        }
                    ]
                });
                // Link to open the dialog
                $("#basedn-dialog-link").click(function( event ) {
                    $("#basedn-dialog").dialog( "open");
                    event.preventDefault();
                });
                
                $("#testform").submit(function(){
                	clearErrors();
                	var typeOfDirectoryValue = $("#typeOfDirectory").val();
                    var wsdlUrlValue = $("#wsdlUrl").val();
                    var baseDnValue = $("#baseDn").val();
                    if(!validate(wsdlUrlValue, baseDnValue)) {
                    	return false;
                    }
                	$("#testform").hide();
                	$("#header").html("Test Results For Your Healthcare Provider Directory Plus Instance");
                	
                	$("#wsdlUrl1").html(wsdlUrlValue);
                    $("#wsdlUrl2").html(wsdlUrlValue);
                	$("#indicatordiv").show();
                	var formInput=$(this).serialize();
                	$.getJSON('ajax/testResultsJson.action', formInput, function(data) {
                		var messagesArray = data.testResultsJson.testResults;
                        for (var i = 0; i < messagesArray.length; i++) {
	                        var message = messagesArray[i];
	                        var testSuiteName = message[0];
                            var testCaseName = message[1];
                            var testStatus = message[2];
                            var testCaseDescription = message[3];
                            var testCaseMessages = message[4];
                            var request = message[5];
                            var response = message[6];
                            var color = "#FF1919";
                            if("PASSED" == testStatus) {
                            	color = "#00CC00";
                            }
                            $("#accordion").append("<h4 style=\"background:none;background-color:" + color + ";color:#ffffff;\">" + testSuiteName + "." + testCaseName + "<div class=\"pull-right\">" + testStatus + "</div></h4>");
                            var divHtml = "<div>" +
                                "<em>Test Case Description:</em><br/>" +
                                "<p>" + testCaseDescription + "</p><hr/>" +
                                "<em>Test Case Messages:</em><br/>" +
                                "<p>" + testCaseMessages + "</p><hr/>" +
                                "<em>Request Content:</em><br/>" +
                                "<pre class=\"sh_xml\">" + request.replace(/</g, "&lt;").replace(/>/g, "&gt;") + "</pre>" +
                                "<em>Response Content:</em><br/>" +
                                "<pre class=\"sh_xml\">" + response.replace(/</g, "&lt;").replace(/>/g, "&gt;") + "</pre>" +
                                "</div>";
                            $("#accordion").append(divHtml);
                        }
                        $("#indicatordiv").hide();
                        $("#infodiv").show();
                        
                        $("#accordion").accordion({ header: "h4", collapsible: true, active: false, heightStyle: "content" });
                    });
                	return false;
                });
            });
            
            function clearErrors() {
            	$("#wsdlUrlControls").removeClass("error");
                $("#baseDnControls").removeClass("error");
            }
            
            function validate(wsdlUrlValue, baseDnValue) {
            	if("" == wsdlUrlValue || "" == baseDnValue) {
            		$("#validationerrordiv").show();
            		if("" == wsdlUrlValue) {
            			$("#wsdlUrlControls").addClass("error");
            		}
            		if("" == baseDnValue) {
                        $("#baseDnControls").addClass("error");
            		}
                    return false;
            	} else {
            		$("#validationerrordiv").hide();
                    return true;
            	}
            }
    
            $(document).ready(function () {
               sh_highlightDocument(); 
            });
        </script>
</body>
</html>