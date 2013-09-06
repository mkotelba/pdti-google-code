<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
em {
    font-weight: bold;
}
</style>
<div id="testResults">
    <div id="accordion">
     <s:iterator value="testResults" var="testResult">
         <s:if test="#testResult[2].equals(\"PASSED\")">
             <h4 style="background:none;background-color:#00CC00;color:#ffffff;"><s:property value="#testResult[0]"/>.<s:property value="#testResult[1]"/><div class="pull-right"><s:property value="#testResult[2]"/></div></h4>
         </s:if>
         <s:else>
             <h4 style="background:none;background-color:#FF1919;color:#ffffff;"><s:property value="#testResult[0]"/>.<s:property value="#testResult[1]"/><div class="pull-right"><s:property value="#testResult[2]"/></div></h4>
         </s:else>
         <div>
            <em><s:text name="description"/>:</em>
            <br/>
            <p><s:property value="#testResult[3]"/></p>
            <hr/>
            <em><s:text name="messages"/>:</em>
            <br/>
            <p>
            <s:iterator value="#testResult[4]" var="message">
                <s:property value="#message"/><br/>
            </s:iterator>
            </p>
            <hr/>
            <em><s:text name="response"/>:</em>
            <br/>
            <p><s:property value="#testResult[5]"/></p>
         </div>
     </s:iterator>
    </div>
    <script type="text/javascript">
	    $(function() {
	        $( "#accordion" ).accordion({ header: "h4", collapsible: true, active: false });
	    });
    </script>
</div>