<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="testResults">
    <div style="width:100%; height:490px; overflow:auto;">
	    <table id="testResultsTable">
	        <tr>
	            <th class="testResultsHeader"><s:text name="testsuite.header.text"/></th>
	            <th class="testResultsHeader"><s:text name="testcase.header.text"/></th>
	            <th class="testResultsHeader"><s:text name="result.header.text"/></th>
	        </tr>
	        <s:iterator value="testResults" var="testResult">
	            <tr class="testResultsRow">
	                <s:iterator value="testResult" var="stringValue">
	                    <s:if test="#stringValue.equals(\"PASSED\")">
		                    <td class="testResultsText centered green"><s:property value="stringValue"/></td>
		                </s:if>
	                    <s:elseif test="#stringValue == 'FAILED'">
	                        <td class="testResultsText centered red"><s:property value="stringValue"/></td>
	                    </s:elseif>
	                    <s:else>
	                        <td class="testResultsText"><s:property value="stringValue"/></td>
	                    </s:else>
	                </s:iterator>
	            </tr>
	        </s:iterator>
	    </table>
    </div>
    <div class="backContainer">
        <a href="<s:url action="test"/>"><s:text name="back"/></a>
    </div>
</div>